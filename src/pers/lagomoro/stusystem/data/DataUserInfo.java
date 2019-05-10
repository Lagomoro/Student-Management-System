package pers.lagomoro.stusystem.data;

public class DataUserInfo extends JsonData{
	
	public String username;
	public String nickname;
	public String classname;
	public String image_data;
	public boolean authority;

	public DataUserInfo(String username, String nickname, String classname, String image_data, boolean authority) {
		this.username = username;
		this.nickname = nickname;
		this.classname = classname;
		this.image_data = image_data;
		this.authority = authority;
	}
	
	public static DataUserInfo fromJson(String json) {
		return JsonDataController.fromJson(json, DataUserInfo.class);
	}
	
	public static DataUserInfo fromData(String username, String nickname, String classname, String image_data, boolean authority) {
		return new DataUserInfo(username, nickname, classname, image_data, authority);
	}
	
	public static String parse(String username, String nickname, String classname, String image_data, boolean authority) {
		return new DataUserInfo(username, nickname, classname, image_data, authority).toString();
	}
	
}
