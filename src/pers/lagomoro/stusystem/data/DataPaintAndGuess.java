package pers.lagomoro.stusystem.data;

import java.util.LinkedList;

import com.google.gson.reflect.TypeToken;

public class DataPaintAndGuess extends JsonData {

	public String item;
	public String hint_1;
	public String hint_2;
	
	/**
	 * Update paint and guess.
	 */
	public DataPaintAndGuess(String item, String hint_1, String hint_2) {
		this.item = item;
		this.hint_1 = hint_1;
		this.hint_2 = hint_2;
	}

	public static DataPaintAndGuess fromJson(String json) {
		return JsonDataController.fromJson(json, DataPaintAndGuess.class);
	}
	
	public static LinkedList<DataPaintAndGuess> fromJsonList(String jsonList) {
		return JsonDataController.fromJsonList(jsonList, new TypeToken<LinkedList<DataPaintAndGuess>>(){});
	}
	
	public static DataPaintAndGuess fromData(String item, String hint_1, String hint_2) {
		return new DataPaintAndGuess(item, hint_1, hint_2);
	}
	
	public static String parse(String item, String hint_1, String hint_2) {
		return new DataPaintAndGuess(item, hint_1, hint_2).toString();
	}
	
}
