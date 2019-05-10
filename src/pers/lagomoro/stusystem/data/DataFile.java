package pers.lagomoro.stusystem.data;

import java.util.Date;
import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataFile extends JsonData{

	public String connecterKey;
	public String serverKey;
	public String username;
	public String nickname;
	public String filename;
	public String md5Path;
	public long size;
	public Date timestamp;
	public int id;
	
	/**
	 * Upload file.
	 */
	public DataFile(String connecterKey, String serverKey, String filename, String md5Path, long size) {
		this.connecterKey = connecterKey;
		this.serverKey = serverKey;
		this.filename = filename;
		this.md5Path = md5Path;
		this.size = size;
	}
	
	/**
	 * Download file.
	 */
	public DataFile(String connecterKey, String serverKey, int id) {
		this.connecterKey = connecterKey;
		this.serverKey = serverKey;
		this.id = id;
	}
	
	/**
	 * Delete File.
	 */
	public DataFile(String connecterKey, int id) {
		this.connecterKey = connecterKey;
		this.id = id;
	}
	
	/**
	 * Update File.
	 */
	public DataFile(String username, String nickname, String filename, long size, Date timestamp, int id) {
		this.username = username;
		this.nickname = nickname;
		this.filename = filename;
		this.size = size;
		this.timestamp = timestamp;
		this.id = id;
	}

	public static DataFile fromJson(String json) {
		return JsonDataController.fromJson(json, DataFile.class);
	}
	
	public static LinkedList<DataFile> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataFile>>(){});
	}
	
	public static DataFile fromData(String connecterKey, String serverKey, String filename, String md5Path, long size) {
		return new DataFile(connecterKey, serverKey, filename, md5Path, size);
	}
	
	public static DataFile fromData(String connecterKey, int id) {
		return new DataFile(connecterKey, id);
	}
	
	public static DataFile fromData(String connecterKey, String serverKey, int id) {
		return new DataFile(connecterKey, serverKey, id);
	}
	
	public static DataFile fromData(String username, String nickname, String filename, long size, Date timestamp, int id) {
		return new DataFile(username, nickname, filename, size, timestamp, id);
	}
	
	public static String parse(String connecterKey, String serverKey, String filename, String md5Path, long size) {
		return new DataFile(connecterKey, serverKey, filename, md5Path, size).toString();
	}
	
	public static String parse(String connecterKey, int id) {
		return new DataFile(connecterKey, id).toString();
	}
	
	public static String parse(String connecterKey, String serverKey, int id) {
		return new DataFile(connecterKey, serverKey, id).toString();
	}
	
	public static String parse(String username, String nickname, String filename, long size, Date timestamp, int id) {
		return new DataFile(username, nickname, filename, size, timestamp, id).toString();
	}
	
}
