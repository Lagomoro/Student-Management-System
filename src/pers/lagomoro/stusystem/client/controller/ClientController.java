package pers.lagomoro.stusystem.client.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import org.apache.commons.codec.digest.DigestUtils;

import pers.lagomoro.stusystem.client.controller.module.Connecter;
import pers.lagomoro.stusystem.client.controller.module.ConnecterFile;
import pers.lagomoro.stusystem.client.controller.module.ConnecterMain;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.data.DataFile;
import pers.lagomoro.stusystem.data.DataProfileImage;
import pers.lagomoro.stusystem.client.controller.ConnecterController;

public class ClientController {

	//public static final String SERVER_ADDRESS = "121.250.216.182";
	public static final String SERVER_ADDRESS = "127.0.0.1";
	public static final int SERVER_PORT = 20000;
	public static final int FILE_PORT = 20001;
	
	public static ConnecterMain connecterMain; 
	
	public static String username = "";
	public static boolean authority = false;
	
	//主连接入口，由Main函数调用。
	public static void run() {
		connecterMain = new ConnecterMain();
		ConnecterController.addConnecter(connecterMain);
	}
	
	public static String getServerAddress() {
		return StorageModel.getStringVariable(StorageModel.SERVER_IP);
	}
	
	public static String getMainConnectorKey(){
		return getConnectorKey(connecterMain, ".Main");
	}
	
	public static String getConnectorKey(Connecter connecter, String tail){
		Socket socket = connecter.getSocket();
		return socket.getLocalAddress() + ":" + socket.getLocalPort() + tail;
	}
	
	public static void login(String username, boolean authority) {
		ClientController.username = username;
		ClientController.authority = authority;
	}
	
	public static void logout() {
		ClientController.username = "";
		ClientController.authority = false;
	}
	
	public static boolean isCurrentUser(String username) {
		return ClientController.username.equals(username);
	}
	
	public static boolean isAdmin() {
		return ClientController.authority;
	}
	
	public static boolean canSensitiveOperation(String username) {
		return isCurrentUser(username) || isAdmin();
	}
	
	public static void addCommand() {
		CommandModel.addCommand("login", json -> connecterMain.sendCommand("login", json));
		CommandModel.addCommand("logout", json -> connecterMain.sendCommand("logout", json));
		
		CommandModel.addCommand("changeNickname", json -> connecterMain.sendCommand("changeNickname", json));
		CommandModel.addCommand("changePassword", json -> connecterMain.sendCommand("changePassword", json));
		CommandModel.addCommand("changeImageData", json -> connecterMain.sendCommand("changeImageData", json));
		CommandModel.addCommand("profileImage_Callback", jsonList -> ImageModel.refreshUserProfile(DataProfileImage.fromJsonList(jsonList)));
		
		CommandModel.addCommand("sendNotice", json -> connecterMain.sendCommand("sendNotice", json));
		CommandModel.addCommand("deleteNotice", json -> connecterMain.sendCommand("deleteNotice", json));
		
		CommandModel.addCommand("sendPerusal", json -> connecterMain.sendCommand("sendPerusal", json));
		CommandModel.addCommand("insertPerusalChoose", json -> connecterMain.sendCommand("insertPerusalChoose", json));
		CommandModel.addCommand("insertPerusalComment", json -> connecterMain.sendCommand("insertPerusalComment", json));
		CommandModel.addCommand("deletePerusal", json -> connecterMain.sendCommand("deletePerusal", json));
		
		CommandModel.addCommand("uploadFile", json -> {});//请使用ClientController.uploadFile
		CommandModel.addCommand("downloadFile", json -> {});//请使用ClientController.downloadFile
		CommandModel.addCommand("deleteFile", json -> connecterMain.sendCommand("deleteFile", json));

		CommandModel.addCommand("sendUserChat", json -> connecterMain.sendCommand("sendUserChat", json));
		CommandModel.addCommand("withdrawUserChat", json -> connecterMain.sendCommand("withdrawUserChat", json));
		CommandModel.addCommand("sendClassChat", json -> connecterMain.sendCommand("sendClassChat", json));
		CommandModel.addCommand("withdrawClassChat", json -> connecterMain.sendCommand("withdrawClassChat", json));
		
		CommandModel.addCommand("class_SceneUpdate", json -> connecterMain.sendCommand("class_SceneUpdate", json));
		CommandModel.addCommand("notice_SceneUpdate", json -> connecterMain.sendCommand("notice_SceneUpdate", json));
		CommandModel.addCommand("perusal_SceneUpdate", json -> connecterMain.sendCommand("perusal_SceneUpdate", json));
		CommandModel.addCommand("share_SceneUpdate", json -> connecterMain.sendCommand("share_SceneUpdate", json));
		CommandModel.addCommand("member_SceneUpdate", json -> connecterMain.sendCommand("member_SceneUpdate", json));
		CommandModel.addCommand("userChat_SceneUpdate", json -> connecterMain.sendCommand("userChat_SceneUpdate", json));
		CommandModel.addCommand("classChat_SceneUpdate", json -> connecterMain.sendCommand("classChat_SceneUpdate", json));
		CommandModel.addCommand("userInfo_SceneUpdate", json -> connecterMain.sendCommand("userInfo_SceneUpdate", json));

		CommandModel.addCommand("paintAndGuess_SceneUpdate", json -> connecterMain.sendCommand("paintAndGuess_SceneUpdate", json));
		CommandModel.addCommand("newPaintAndGuessGameRoom", json -> connecterMain.sendCommand("newPaintAndGuessGameRoom", json));
		CommandModel.addCommand("refreshPaintAndGuessGameRoom", json -> connecterMain.sendCommand("refreshPaintAndGuessGameRoom", json));
	}
	
	public static void uploadFile(String path, ConnecterFile fileConnecter) {
		File file = new File(path);
		if(file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(path);
				String md5Path = DigestUtils.md5Hex(inputStream);
				ConnecterController.prepareConnecter(fileConnecter);
				fileConnecter.startUpload(path);
				String json = DataFile.parse(getMainConnectorKey(), getConnectorKey(fileConnecter, ".File"), file.getName(), md5Path, file.length());
				connecterMain.sendCommand("uploadFile", json);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void downloadFile(String path, ConnecterFile fileConnecter, DataFile file) {
		ConnecterController.prepareConnecter(fileConnecter);
		String json = DataFile.parse(getMainConnectorKey(), getConnectorKey(fileConnecter, ".File"), file.id);
		connecterMain.sendCommand("downloadFile", json);
		fileConnecter.startDownload(path);
	}
	
}
