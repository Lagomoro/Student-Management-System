package pers.lagomoro.stusystem.server.controller;

import java.util.HashMap;
import java.util.LinkedList;

import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.model.MySQLModel;

public class UserController {

	private static HashMap<String, String> userMap = new HashMap<String, String>();

	public static void login(String username, String connecterKey) {
		userMap.put(username, connecterKey);
		LogModel.log("User <" + username + "> login with connecter <" + connecterKey + ">.");
	}
	
	public static void logout(String username) {
		if(haveUser(username)) {
			LogModel.log("User <" + username + "> logout from connecter <" + userMap.get(username) + ">.");
			userMap.replace(username, null);
			userMap.remove(username);
		}
	}
	
	public static String getUsername(String connecterKey) {
		String username = null;
		for (String user : userMap.keySet()) {
			if(userMap.get(user) != null && userMap.get(user).equals(connecterKey)) {
				username = user;
				break;
			}
		}
		return username;
	}
	
	public static boolean haveConnecter(String connecterKey) {
		return userMap.containsValue(connecterKey);
	}
	
	public static boolean haveUser(String username) {
		return userMap.containsKey(username);
	}
	
	public static boolean isLogin(String username) {
		return userMap.containsKey(username) && userMap.get(username) != null;
	}
	
	public static String getConnecterKey(String username) {
		return isLogin(username) ? userMap.get(username) : null;
	}

	public static LinkedList<String> getAllConnecterKeyFromClass(int class_id) {
		LinkedList<String> usernameList = MySQLModel.getAllUserInClass(class_id);
		LinkedList<String> connecterList = new LinkedList<String>();
		for(String username : usernameList) {
			if(isLogin(username)) connecterList.add(getConnecterKey(username));
		}
		return connecterList;
	}
	
	public static LinkedList<String> getAllConnecterKey() {
		LinkedList<String> connecterList = new LinkedList<String>();
		for (String user : userMap.keySet()) {
			if(isLogin(user)) connecterList.add(userMap.get(user));
		}
		return connecterList;
	}
}
