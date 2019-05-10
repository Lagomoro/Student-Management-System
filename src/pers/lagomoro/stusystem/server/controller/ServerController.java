/*============================================================*/
/*  ServerManager.java                                        */
/*------------------------------------------------------------*/
/*  服务器Socket的管理器。                                                                  */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.server.controller;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;

import pers.lagomoro.stusystem.data.DataChat;
import pers.lagomoro.stusystem.data.DataClass;
import pers.lagomoro.stusystem.data.DataFile;
import pers.lagomoro.stusystem.data.DataLogin;
import pers.lagomoro.stusystem.data.DataNotice;
import pers.lagomoro.stusystem.data.DataPerusal;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.data.DataUserInfo;
import pers.lagomoro.stusystem.data.JsonDataController;
import pers.lagomoro.stusystem.server.controller.module.ConnecterFile;
import pers.lagomoro.stusystem.server.controller.module.ConnecterMain;
import pers.lagomoro.stusystem.server.controller.module.FileEvent;
import pers.lagomoro.stusystem.server.controller.module.FileProcessListener;
import pers.lagomoro.stusystem.server.controller.module.Server;
import pers.lagomoro.stusystem.server.controller.module.ServerFile;
import pers.lagomoro.stusystem.server.controller.module.ServerMain;
import pers.lagomoro.stusystem.server.controller.module.ServerPaintAndGuess;
import pers.lagomoro.stusystem.server.model.CommandModel;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.model.MySQLModel;

public class ServerController {
	
	private static HashMap<String, Server> serverMap = new HashMap<String, Server>();
	
	public static final int SERVER_MAIN_PORT = 20000;
	public static final int SERVER_FILE_PORT = 20001;
	
	//连接常量，用于维持窗口单一性
	public static final String SERVER = "Server";
	public static final String SERVER_MAIN = "Server_Main";
	public static final String SERVER_FILE = "Server_File";
	public static final String SERVER_PAINT_AND_GUESS = "Server_Paint_And_Guess";
	
	//建立主服务器，由Main函数调用。
	public static void run() {
		startServer();
		addServer(new ServerMain());
		addServer(new ServerFile());
	}
	
	public static void startServer() {
		try {
			Enumeration<NetworkInterface> networkInertfaces = NetworkInterface.getNetworkInterfaces();
	        while (networkInertfaces.hasMoreElements()) {
	            NetworkInterface networkInertface = networkInertfaces.nextElement();
	            Enumeration<InetAddress> addresses = networkInertface.getInetAddresses();
	            while (addresses.hasMoreElements()) {
	                InetAddress address = addresses.nextElement();
	                if (address instanceof Inet4Address) {
	                	LogModel.log("Server info : Hardware Name <" + networkInertface.getName() + ">");
	                	LogModel.log("Server info : Host Address <" + address.getHostAddress() + ">");
	                }
	            }
	        }
			LogModel.log("Server launching on IP <" + InetAddress.getLocalHost().getHostAddress() + ">...");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		LogModel.log("Server launching on device ID <" + ManagementFactory.getRuntimeMXBean().getName() + ">...");
	}
	
	//指定连接是否存在
	public static boolean haveServer(String key) {
		return serverMap.containsKey(key);
	}
	
	//获取指定连接
	public static Server getServer(String key) {
		return serverMap.get(key);
	}
	
	//添加新服务器
	public static void addServer(Server server) {
		serverMap.put(server.getKey(), server);
		ThreadController.runThread(server.getKey(), server);
	}
	
	//删除服务器
	public static void removeServer(String key) {
		serverMap.get(key).release();
		serverMap.remove(key);
	}
	
	public static void addCommand() {
		CommandModel.addCommand("login", json -> {
			DataLogin data = DataLogin.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			int callback = MySQLModel.checkLogin(data.username, data.password);
			if(callback == 0) {
				UserController.login(data.username, data.connecterKey);
				CommandModel.commandActive("profileImage_Update", DataUpdate.parse(data.connecterKey));
			}
			connecter.sendCommand("loginCallback", DataLogin.parse(callback, data.username, MySQLModel.isAdmin(data.username)));
		});
		CommandModel.addCommand("logout", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			UserController.logout(UserController.getUsername(data.connecterKey));
		});
		CommandModel.addCommand("changeNickname", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			MySQLModel.updateNickname(UserController.getUsername(data.connecterKey), data.tag);
			CommandModel.commandActive("userInfo_SceneUpdate", json);
		});
		CommandModel.addCommand("changePassword", json -> {
			DataLogin data = DataLogin.fromJson(json);
			MySQLModel.resetPassword(UserController.getUsername(data.connecterKey), data.username, data.password);
			CommandModel.commandActive("userInfo_SceneUpdate", json);
		});
		CommandModel.addCommand("changeImageData", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			MySQLModel.updateImageData(username, data.tag);
			int classID = MySQLModel.getUserClassID(username);
			CommandModel.commandActive("userInfo_SceneUpdate", json);
			updateAllClassClient(classID, "profileImage_Update");
		});
		CommandModel.addCommand("profileImage_Update", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			connecter.sendCommand("profileImage_Callback", JsonDataController.toJsonList(MySQLModel.getAllUserProfileInClass(classID)));
		});
		
		CommandModel.addCommand("sendNotice", json -> {
			DataNotice data = DataNotice.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			if(MySQLModel.isAdmin(username)) {
				MySQLModel.insertClassNotice(classID, username, data.title, data.document, MySQLModel.getTimestamp());
				LogModel.adminLog(data.connecterKey, classID, username, "Send notice " + data.id);
				updateAllClassClient(classID, "notice_SceneUpdate");
			}
		});
		CommandModel.addCommand("deleteNotice", json -> {
			DataNotice data = DataNotice.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			if(MySQLModel.isAdmin(username)) {
				MySQLModel.deleteClassNotice(classID, data.id);
				LogModel.adminLog(data.connecterKey, classID, username, "Delete notice " + data.id);
				updateAllClassClient(classID, "notice_SceneUpdate");
			}
		});
		
		CommandModel.addCommand("sendPerusal", json -> {
			DataPerusal data = DataPerusal.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			MySQLModel.insertClassPerusal(classID, username, data.title, data.document, MySQLModel.getTimestamp(), data.max_choose, data.options);
			updateAllClassClient(classID, "perusal_SceneUpdate");
		});
		CommandModel.addCommand("insertPerusalChoose", json -> {
			DataPerusal data = DataPerusal.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			MySQLModel.insertClassPerusalChoose(classID, data.id, username + ":" + data.data);
			updateAllClassClient(classID, "perusal_SceneUpdate");
		});
		CommandModel.addCommand("insertPerusalComment", json -> {
			DataPerusal data = DataPerusal.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			MySQLModel.insertClassPerusalComment(classID, data.id, data.data);
			updateAllClassClient(classID, "perusal_SceneUpdate");
		});
		CommandModel.addCommand("deletePerusal", json -> {
			DataPerusal data = DataPerusal.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			boolean isAdmin = MySQLModel.isAdmin(username);
			boolean isCurrentUser = MySQLModel.getPerusalUsername(classID, data.id).equals(username);
			if(isAdmin || isCurrentUser) {
				MySQLModel.deleteClassPerusal(classID, data.id);
				if(!isCurrentUser) LogModel.adminLog(data.connecterKey, classID, username, "Delete perusal " + data.id);
				updateAllClassClient(classID, "perusal_SceneUpdate");
			}
		});
		
		CommandModel.addCommand("uploadFile", json -> {
			DataFile data = DataFile.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			ConnecterFile connecterFile;
			while((connecterFile = (ConnecterFile)ConnecterController.getConnecter(data.serverKey)) == null) {}
			String md5Path = MySQLModel.FILE_PATH + data.md5Path;
			File file = new File(md5Path);
			if(file.exists()) {
				LogModel.log("File with MD5 <" + data.md5Path + "> already have, connecter refused.");
				connecterFile.release();
				MySQLModel.insertClassFile(classID, username, data.filename, data.size, md5Path, MySQLModel.getTimestamp());
				updateAllClassClient(classID, "share_SceneUpdate");
			}else {
				LogModel.log("File with MD5 <" + data.md5Path + "> pass validation, process start.");
				connecterFile.addListener(new FileProcessListener() {
					@Override
					public void onSuccessDownload(FileEvent e){
						super.onSuccessDownload(e);
						MySQLModel.insertClassFile(classID, username, data.filename, data.size, md5Path, MySQLModel.getTimestamp());
						updateAllClassClient(classID, "share_SceneUpdate");
					}
				});
				connecterFile.startDownload(md5Path);
			}
		});
		CommandModel.addCommand("downloadFile", json -> {
			DataFile data = DataFile.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			ConnecterFile connecter = (ConnecterFile)ConnecterController.getConnecter(data.serverKey);
			while((connecter = (ConnecterFile)ConnecterController.getConnecter(data.serverKey)) == null) {}
			connecter.startUpload(MySQLModel.getFilePath(classID, data.id));
		});
		CommandModel.addCommand("deleteFile", json -> {
			DataFile data = DataFile.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			String filepath = MySQLModel.getFilePath(classID, data.id);
			boolean isAdmin = MySQLModel.isAdmin(username);
			boolean isCurrentUser = MySQLModel.getFileUsername(classID, data.id).equals(username);
			if(isAdmin || isCurrentUser) {
				if(MySQLModel.getMD5FileCount(MySQLModel.getFilePath(classID, data.id)) > 1){
					LogModel.log("File with MD5 <" + new File(filepath).getName() + "> have several links, delete only record.");
					MySQLModel.deleteClassFile(classID, data.id);
				}else {
					LogModel.log("File with MD5 <" + new File(filepath).getName() + "> have no links, process delete.");
					if(MySQLModel.deleteClassFile(classID, data.id) == 0) {
						File file = new File(filepath);
						if(file.exists()) file.delete();
					}
				}
				if(!isCurrentUser) LogModel.adminLog(data.connecterKey, classID, username, "Delete file " + data.id);
				updateAllClassClient(classID, "share_SceneUpdate");
			}
		});
		
		CommandModel.addCommand("sendUserChat", json -> {
			DataChat data = DataChat.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			MySQLModel.insertUserChat(username, data.username_to, data.document, false, MySQLModel.getTimestamp());
			updatePrivateChat(username, data.username_to);
		});
		CommandModel.addCommand("withdrawUserChat", json -> {
			DataChat data = DataChat.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			if(username.equals(data.username)) MySQLModel.updateUserChatWithdraw(username, data.id, true);
			updatePrivateChat(username, data.username_to);
		});
		CommandModel.addCommand("sendClassChat", json -> {
			DataChat data = DataChat.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			MySQLModel.insertClassChat(classID, username, data.document, false, MySQLModel.getTimestamp());
			updateAllClassClient(classID, "classChat_SceneUpdate");
		});
		CommandModel.addCommand("withdrawClassChat", json -> {
			DataChat data = DataChat.fromJson(json);
			String username = UserController.getUsername(data.connecterKey);
			int classID = MySQLModel.getUserClassID(username);
			boolean isAdmin = MySQLModel.isAdmin(username);
			boolean isCurrentUser = MySQLModel.getClassChatUsername(classID, data.id).equals(username);
			if(isAdmin || isCurrentUser) {
				MySQLModel.updateClassChatWithdraw(classID, data.id, true);
				if(!isCurrentUser) LogModel.adminLog(data.connecterKey, classID, username, "Withdraw Chat " + data.id);
				updateAllClassClient(classID, "classChat_SceneUpdate");
			}
		});

		CommandModel.addCommand("class_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("class_SceneCallback", DataClass.parse(MySQLModel.getUserClassName(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("notice_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("notice_SceneCallback", JsonDataController.toJsonList(MySQLModel.getUserAllNotice(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("perusal_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("perusal_SceneCallback", JsonDataController.toJsonList(MySQLModel.getUserAllPerusal(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("share_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("share_SceneCallback", JsonDataController.toJsonList(MySQLModel.getUserAllFile(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("member_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("member_SceneCallback", JsonDataController.toJsonList(MySQLModel.getAllClassUser(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("userChat_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("userChat_" + data.tag + "_SceneCallback", JsonDataController.toJsonList(MySQLModel.getUserPrivateChat(UserController.getUsername(data.connecterKey), data.tag)));
		});
		CommandModel.addCommand("classChat_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			connecter.sendCommand("classChat_SceneCallback", JsonDataController.toJsonList(MySQLModel.getUserAllChat(UserController.getUsername(data.connecterKey))));
		});
		CommandModel.addCommand("userInfo_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			String username = UserController.getUsername(data.connecterKey);
			String nickname = MySQLModel.getUserNickname(username);
			String classname = MySQLModel.getUserClassName(username);
			String image_data = MySQLModel.getUserImageData(username);
			boolean authority = MySQLModel.isAdmin(username);
			connecter.sendCommand("userInfo_SceneCallback", DataUserInfo.parse(username, nickname, classname, image_data, authority));
		});
		
		CommandModel.addCommand("paintAndGuess_SceneUpdate", json -> {
			DataUpdate data = DataUpdate.fromJson(json);
			ConnecterMain connecter = (ConnecterMain)ConnecterController.getConnecter(data.connecterKey);
			String temp = ";";
			for(String server : serverMap.keySet()) {
				if(server.startsWith(SERVER_PAINT_AND_GUESS)) {
					if(((ServerPaintAndGuess)getServer(server)).isBusy()) continue;
					temp += server.split(":")[1] + ";";
				}
			}
			if(temp.length() > 1) temp = temp.substring(0, temp.length() - 1);
			connecter.sendCommand("paintAndGuess_SceneCallback", temp);
		});
		CommandModel.addCommand("newPaintAndGuessGameRoom", json -> {
			try {
				addServer(new ServerPaintAndGuess(new ServerSocket(0)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			updateAllClient("paintAndGuess_SceneUpdate");
		});
		CommandModel.addCommand("refreshPaintAndGuessGameRoom", json -> {
			updateAllClient("paintAndGuess_SceneUpdate");
		});
	}
	
	public static void updateAllClassClient(int class_id, String command){
		LinkedList<String> connecterList = UserController.getAllConnecterKeyFromClass(class_id);
		for(String connecterKey : connecterList) {
			CommandModel.commandActive(command, DataUpdate.parse(connecterKey));
		}
	}
	
	public static void updatePrivateChat(String username, String username_to){
		if(UserController.isLogin(username)) {
			String connecterKey = UserController.getConnecterKey(username);
			CommandModel.commandActive("userChat_SceneUpdate", DataUpdate.parse(connecterKey, username_to));
		}
		if(!username.equals(username_to) && UserController.isLogin(username_to)) {
			String connecterKey = UserController.getConnecterKey(username_to);
			CommandModel.commandActive("userChat_SceneUpdate", DataUpdate.parse(connecterKey, username));
		}
	}
	
	public static void updateAllClient(String command){
		LinkedList<String> connecterList = UserController.getAllConnecterKey();
		for(String connecterKey : connecterList) {
			CommandModel.commandActive(command, DataUpdate.parse(connecterKey));
		}
	}
	
}
