package pers.lagomoro.stusystem.data;

public class DataUpdate extends JsonData{

	public String connecterKey;
	public String tag;
	
	/**
	 * Update
	 */
	public DataUpdate(String connecterKey) {
		this.connecterKey = connecterKey;
	}
	
	/**
	 * Update with tag (private chat)
	 */
	public DataUpdate(String connecterKey, String tag) {
		this.connecterKey = connecterKey;
		this.tag = tag;
	}

	public static DataUpdate fromJson(String json) {
		return JsonDataController.fromJson(json, DataUpdate.class);
	}
	
	public static DataUpdate fromData(String connecterKey) {
		return new DataUpdate(connecterKey);
	}
	
	public static DataUpdate fromData(String connecterKey, String tag) {
		return new DataUpdate(connecterKey, tag);
	}
	
	public static String parse(String connecterKey) {
		return new DataUpdate(connecterKey).toString();
	}
	
	public static String parse(String connecterKey, String tag) {
		return new DataUpdate(connecterKey, tag).toString();
	}
	
}
