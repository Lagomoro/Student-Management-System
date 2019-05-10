package pers.lagomoro.stusystem.data;

import java.util.Date;
import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataNotice extends JsonData{

	public String connecterKey;
	public String username;
	public String nickname;
	public String title;
	public String document;
	public Date timestamp;
	public int id;
	
	/**
	 * Send notice
	 */
	public DataNotice(String connecterKey, String title, String document) {
		this.connecterKey = connecterKey;
		this.title = title;
		this.document = document;
	}
	
	/**
	 * Update notice
	 */
	public DataNotice(String username, String nickname, String title, String document, Date timestamp, int id) {
		this.username = username;
		this.nickname = nickname;
		this.title = title;
		this.document = document;
		this.timestamp = timestamp;
		this.id = id;
	}
	
	/**
	 * Delete Notice.
	 */
	public DataNotice(String connecterKey, int id) {
		this.connecterKey = connecterKey;
		this.id = id;
	}

	public static DataNotice fromJson(String json) {
		return JsonDataController.fromJson(json, DataNotice.class);
	}
	
	public static LinkedList<DataNotice> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataNotice>>(){});
	}
	
	public static DataNotice fromData(String connecterKey, String title, String document) {
		return new DataNotice(connecterKey, title, document);
	}
	
	public static DataNotice fromData(String username, String nickname, String title, String document, Date timestamp, int id) {
		return new DataNotice(username, nickname, title, document, timestamp, id);
	}
	
	public static DataNotice fromData(String connecterKey, int id) {
		return new DataNotice(connecterKey, id);
	}

	public static String parse(String connecterKey, String title, String document) {
		return new DataNotice(connecterKey, title, document).toString();
	}
	
	public static String parse(String username, String nickname, String title, String document, Date timestamp, int id) {
		return new DataNotice(username, nickname, title, document, timestamp, id).toString();
	}
	
	public static String parse(String connecterKey, int id) {
		return new DataNotice(connecterKey, id).toString();
	}
	
}
