package pers.lagomoro.stusystem.data;

import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataProfileImage extends JsonData{

	public String username;
	public String image_data;
	
	/**
	 * Get profile image (private chat)
	 */
	public DataProfileImage(String username, String image_data) {
		this.username = username;
		this.image_data = image_data;
	}

	public static DataProfileImage fromJson(String json) {
		return JsonDataController.fromJson(json, DataProfileImage.class);
	}
	
	public static LinkedList<DataProfileImage> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataProfileImage>>(){});
	}
	
	public static DataProfileImage fromData(String username, String image_data) {
		return new DataProfileImage(username, image_data);
	}
	
	public static String parse(String username, String image_data) {
		return new DataProfileImage(username, image_data).toString();
	}
	
}
