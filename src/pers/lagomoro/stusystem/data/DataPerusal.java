package pers.lagomoro.stusystem.data;

import java.util.Date;
import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataPerusal extends JsonData{
	
	public String connecterKey;
	public String username;
	public String nickname;
	public String title;
	public String document;
	public Date timestamp;
	public int id;
	
	public int max_choose;
	public String options;
	
	public String chooses;
	public String comments;
	
	public String data;
	
	/**
	 * Send perusal
	 */
	public DataPerusal(String connecterKey, String title, String document, int max_choose, String options) {
		this.connecterKey = connecterKey;
		this.title = title;
		this.document = document;
		this.max_choose = max_choose;
		this.options = options;
	}
	
	/**
	 * Update perusal
	 */
	public DataPerusal(String username, String nickname, String title, String document, Date timestamp, int max_choose, String options, String chooses, String comments, int id) {
		this.username = username;
		this.nickname = nickname;
		this.title = title;
		this.document = document;
		this.timestamp = timestamp;
		this.max_choose = max_choose;
		this.options = options;
		this.chooses = chooses;
		this.comments = comments;
		this.id = id;
	}
	
	/**
	 * Insert choose/comment
	 */
	public DataPerusal(String connecterKey, int id, String data) {
		this.connecterKey = connecterKey;
		this.data = data;
		this.id = id;
	}
	
	/**
	 * Delete perusal.
	 */
	public DataPerusal(String connecterKey, int id) {
		this.connecterKey = connecterKey;
		this.id = id;
	}
	
	public static DataPerusal fromJson(String json) {
		return JsonDataController.fromJson(json, DataPerusal.class);
	}
	
	public static LinkedList<DataPerusal> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataPerusal>>(){});
	}
	
	public static DataPerusal fromData(String connecterKey, String title, String document, int max_choose, String options) {
		return new DataPerusal(connecterKey, title, document, max_choose, options);
	}
	
	public static DataPerusal fromData(String username, String nickname, String title, String document, Date timestamp, int max_choose, String options, String chooses, String comments, int id) {
		return new DataPerusal(username, nickname, title, document, timestamp, max_choose, options, chooses, comments, id);
	}
	
	public static DataPerusal fromData(String connecterKey, int id, String data) {
		return new DataPerusal(connecterKey, id, data);
	}
	
	public static DataPerusal fromData(String connecterKey, int id) {
		return new DataPerusal(connecterKey, id);
	}
	
	public static String parse(String connecterKey, String title, String document, int max_choose, String options) {
		return new DataPerusal(connecterKey, title, document, max_choose, options).toString();
	}
	
	public static String parse(String username, String nickname, String title, String document, Date timestamp, int max_choose, String options, String chooses, String comments, int id) {
		return new DataPerusal(username, nickname, title, document, timestamp, max_choose, options, chooses, comments, id).toString();
	}
	
	public static String parse(String connecterKey, int id, String data) {
		return new DataPerusal(connecterKey, id, data).toString();
	}
	
	public static String parse(String connecterKey, int id) {
		return new DataPerusal(connecterKey, id).toString();
	}
	
}
