package pers.lagomoro.stusystem.data;

import java.util.Date;
import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataChat extends JsonData{
	
	public String connecterKey;
	public String username;
	public String username_to;
	public String nickname;
	public String document;
	public boolean withdraw;
	public Date timestamp;
	public int id;

	/**
	 * Send chat (class).
	 */
	public DataChat(String connecterKey, String username_to, String document) {
		this.connecterKey = connecterKey;
		this.username_to = username_to;
		this.document = document;
	}
	
	/**
	 * Send chat (private).
	 */
	public DataChat(String connecterKey, String document) {
		this.connecterKey = connecterKey;
		this.document = document;
	}
	
	/**
	 * Withdraw chat.
	 */
	public DataChat(String connecterKey, String username, int id, String username_to) {
		this.connecterKey = connecterKey;
		this.username = username;
		this.id = id;
		this.username_to = username_to;
	}
	
	/**
	 * Update chat.
	 */
	public DataChat(String username, String nickname, String document, boolean withdraw, Date timestamp, int id) {
		this.username = username;
		this.nickname = nickname;
		this.document = document;
		this.withdraw = withdraw;
		this.timestamp = timestamp;
		this.id = id;
	}
	
	public static DataChat fromJson(String json) {
		return JsonDataController.fromJson(json, DataChat.class);
	}
	
	public static LinkedList<DataChat> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataChat>>(){});
	}
	
	public static DataChat fromData(String connecterKey, String username_to, String document) {
		return new DataChat(connecterKey, username_to, document);
	}
	
	public static DataChat fromData(String connecterKey, String document) {
		return new DataChat(connecterKey, document);
	}
	
	public static DataChat fromData(String connecterKey, String username, int id, String username_to) {
		return new DataChat(connecterKey, username, id, username_to);
	}
	
	public static DataChat fromData(String username, String nickname, String document, boolean withdraw, Date timestamp, int id) {
		return new DataChat(username, nickname, document, withdraw, timestamp, id);
	}
	
	public static String parse(String connecterKey, String username_to, String document) {
		return new DataChat(connecterKey, username_to, document).toString();
	}
	
	public static String parse(String connecterKey, String document) {
		return new DataChat(connecterKey, document).toString();
	}
	
	public static String parse(String connecterKey, String username, int id, String username_to) {
		return new DataChat(connecterKey, username, id, username_to).toString();
	}
	
	public static String parse(String username, String nickname, String document, boolean withdraw, Date timestamp, int id) {
		return new DataChat(username, nickname, document, withdraw, timestamp, id).toString();
	}
	
}
