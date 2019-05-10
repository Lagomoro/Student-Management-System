package pers.lagomoro.stusystem.data;

import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataUser extends JsonData{
	
	public String username;
	public String nickname;
	public String image_data;
	public boolean authority;

	public DataUser(String username, String nickname, String image_data, boolean authority) {
		this.username = username;
		this.nickname = nickname;
		this.image_data = image_data;
		this.authority = authority;
	}
	
	public static DataUser fromJson(String json) {
		return JsonDataController.fromJson(json, DataUser.class);
	}
	
	public static LinkedList<DataUser> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataUser>>(){});
	}
	
	public static DataUser fromData(String username, String nickname, String image_data, boolean authority) {
		return new DataUser(username, nickname, image_data, authority);
	}
	
	public static String parse(String username, String nickname, String image_data, boolean authority) {
		return new DataUser(username, nickname, image_data, authority).toString();
	}
	
}
