package pers.lagomoro.stusystem.data;

public class DataClass extends JsonData {

	public String classname;
	
	/**
	 * Class callback.
	 */
	public DataClass(String classname) {
		this.classname = classname;
	}

	public static DataClass fromJson(String json) {
		return JsonDataController.fromJson(json, DataClass.class);
	}
	
	public static DataClass fromData(String classname) {
		return new DataClass(classname);
	}
	
	public static String parse(String classname) {
		return new DataClass(classname).toString();
	}
	
}
