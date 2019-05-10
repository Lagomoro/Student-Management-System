package pers.lagomoro.stusystem.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonDataController {
	
	public static Gson phaser = new Gson();

	public static String toJson(Object object) {
		return phaser.toJson(object);
	}
	
	public static <T> String toJsonList(LinkedList<T> objectList) {
		return phaser.toJson(objectList);
	}
	
	public static <T> T fromJson(String json, Type type) {
		return phaser.fromJson(json, type);
	}
	
	public static <T> LinkedList<T> fromJsonList(String jsonList, TypeToken<LinkedList<T>> typetoken) {
		return phaser.fromJson(jsonList, typetoken.getType());
	}
	
	public static byte[] serializeObject(Object object){
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
			out.writeObject(object);
			byte[] objectStr = byteArrayOutputStream.toByteArray();
			out.close();
			byteArrayOutputStream.close();
			return objectStr;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object deserializeObject(byte[] objectStr){
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(objectStr);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object object =  objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			return object;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
    
}
