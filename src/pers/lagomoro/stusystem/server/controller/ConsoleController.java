package pers.lagomoro.stusystem.server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.codec.digest.DigestUtils;

import pers.lagomoro.stusystem.client.controller.ThreadController;
import pers.lagomoro.stusystem.server.controller.module.Console;
import pers.lagomoro.stusystem.server.model.CommandModel;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.model.MySQLModel;

public class ConsoleController {
	
	protected static BufferedReader inputReader;

	public static void run() {
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		ThreadController.runThread("Console", new Thread() {
    		@Override
            public void run() {
    			try {
    				while (true) {
    					String line;
    		    		while (true) {
    		    			line = inputReader.readLine();
    		    			if(line != null) {
    		    				String[] temp = line.split(" ");
    		    				CommandModel.commandActive(temp[0], line.substring(temp[0].length() + 1));
    		    			}
    		    		}
    	    		}
    			} catch (IOException e) {
					e.printStackTrace();
				}
    		}
    	});
	}

	public static Console transConsole(String data) {
		return new Console(data);
	}
	
	public static String MD5(String data) {
		return DigestUtils.md5Hex(data);
	}
	
	public static void addCommand() {
		CommandModel.addCommand("insertUser", data -> { Console c = transConsole(data);
			printCallback(c.length() == 2 ? MySQLModel.insertUser(c.s(1), MD5(c.s(2))) : MySQLModel.insertUser(c.s(1), MD5(c.s(2)), c.s(3), c.s(4), c.i(5), c.b(6)), new String[]{"Insert success.", "Current user already exist."}, data);});
		CommandModel.addCommand("resetPassword", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.resetPassword(c.s(1), MD5(c.s(2)), MD5(c.s(3))), new String[]{"Update success.", "Unknown user.", "Invalid password"}, data);});
		CommandModel.addCommand("updatePassword" , data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updatePassword(c.s(1), MD5(c.s(2))), new String[]{"Update success.", "Unknown user."}, data);});
		CommandModel.addCommand("updateNickname" , data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateNickname(c.s(1), c.s(2)), new String[]{"Update success.", "Unknown user."}, data);});
//		CommandModel.addCommand("updateImageUrl" , data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.updateImageUrl(c.s(1), c.s(2)), new String[]{"Update success.", "Unknown user."}, data);});
		CommandModel.addCommand("updateClassID"  , data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateClassID(c.s(1), c.i(2)), new String[]{"Update success.", "Unknown user."}, data);});
		CommandModel.addCommand("updateAuthority", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateAuthority(c.s(1), c.b(2)), new String[]{"Update success.", "Unknown user."}, data);});
		
		CommandModel.addCommand("insertClass", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.insertClass(c.s(1)), new String[]{"Insert success.", "Current class already exist."}, data);});
		CommandModel.addCommand("updateClassClassName", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateClassClassname(c.i(1), c.s(2)), new String[]{"Update success.", "Unknown class."}, data);});

//		CommandModel.addCommand("insertUserChat", data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.insertUserChat(c.s(1), c.s(2), c.s(3), false, MySQLModel.getTimestamp()), new String[]{"Insert success.", "Unknown chat."}, data);});
		CommandModel.addCommand("updateUserChatWithdraw", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateUserChatWithdraw(c.s(1), c.i(2), c.b(3)), new String[]{"Update success.", "Unknown chat."}, data);});

//		CommandModel.addCommand("insertClassChat", data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.insertClassChat(c.i(1), c.s(2), c.s(3), false, MySQLModel.getTimestamp()), new String[]{"Insert success.", "Unknown chat."}, data);});
		CommandModel.addCommand("updateClassChatWithdraw", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.updateClassChatWithdraw(c.i(1), c.i(2), c.b(3)), new String[]{"Update success.", "Unknown chat."}, data);});

//		CommandModel.addCommand("insertClassNotice", data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.insertClassNotice(c.i(1), c.s(2), c.s(3), c.s(4), MySQLModel.getTimestamp()), new String[]{"Insert success.", "Unknown notice."}, data);});
		CommandModel.addCommand("deleteClassNotice", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.deleteClassNotice(c.i(1), c.i(2)), new String[]{"Delete success.", "Unknown notice."}, data);});

//		CommandModel.addCommand("insertClassFile", data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.insertClassFile(c.i(1), c.s(2), c.s(3), c.i(4), c.s(5), MySQLModel.getTimestamp()), new String[]{"Insert success.", "Unknown file."}, data);});
		CommandModel.addCommand("deleteClassFile", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.deleteClassFile(c.i(1), c.i(2)), new String[]{"Delete success.", "Unknown file."}, data);});

//		CommandModel.addCommand("insertClassPerusal", data -> { Console c = transConsole(data);
//			printCallback(MySQLModel.insertClassPerusal(c.i(1), c.s(2), c.s(3), c.s(4), MySQLModel.getTimestamp(), c.i(5), c.s(6)), new String[]{"Insert success.", "Unknown perusal."}, data);});
		CommandModel.addCommand("deleteClassPerusal", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.deleteClassPerusal(c.i(1), c.i(2)), new String[]{"Delete success.", "Unknown perusal."}, data);});
		CommandModel.addCommand("insertClassPerusalChoose", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.insertClassPerusalChoose(c.i(1), c.i(2), c.s(3)), new String[]{"Update success.", "Unknown perusal."}, data);});
		CommandModel.addCommand("insertClassPerusalComment", data -> { Console c = transConsole(data);
			printCallback(MySQLModel.insertClassPerusalComment(c.i(1), c.i(2), c.s(3)), new String[]{"Update success.", "Unknown perusal."}, data);});
	}
	
	public static void printCallback(int a, String[] callback, String data) {
		System.out.println(callback[Math.min(callback.length - 1, a)]);
		if(a == 0) LogModel.adminLog("Server@localhost", 0, "root", data);
	}
}
