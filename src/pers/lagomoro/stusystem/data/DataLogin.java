package pers.lagomoro.stusystem.data;

public class DataLogin extends JsonData{

	public String connecterKey;
	public String username;
	public String password;
	public boolean authority;
	public int callback;
	
	/**
	 * Login.
	 */
	public DataLogin(String connecterKey, String username, String password) {
		this.connecterKey = connecterKey;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Login callback.
	 */
	public DataLogin(int callback, String username, boolean authority) {
		this.callback = callback;
		this.username = username;
		this.authority = authority;
	}

	public static DataLogin fromJson(String json) {
		return JsonDataController.fromJson(json, DataLogin.class);
	}
	
	public static DataLogin fromData(String connecterKey, String username, String password) {
		return new DataLogin(connecterKey, username, password);
	}
	
	public static DataLogin fromData(int callback, String username, boolean authority) {
		return new DataLogin(callback, username, authority);
	}
	
	public static String parse(String connecterKey, String username, String password) {
		return new DataLogin(connecterKey, username, password).toString();
	}
	
	public static String parse(int callback, String username, boolean authority) {
		return new DataLogin(callback, username, authority).toString();
	}
	
}
