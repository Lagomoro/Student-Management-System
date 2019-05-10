/*============================================================*/
/*  StorageManager.java                                       */
/*------------------------------------------------------------*/
/*  管理存储器的类，用于保存本地设置。                                               */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import pers.lagomoro.stusystem.client.controller.ClientController;

public class StorageModel {

	private static HashMap<String, String> storageMap = new HashMap<String, String>();
	
	private static final String SAVEFILE_NAME = "./setting.ini";
	
	public static final String SYSTEM_LANGUAGE = "System:Language";
	public static final String SYSTEM_LONG_CLICK_TIME = "System:LongClickTime";
	public static final String SYSTEM_DRAGGED_DISTANCE = "System:DraggedDistance";
	public static final String SERVER_IP = "System:ServerIP";
	
	//注册设置
	public static void register() {
		addVariable(SYSTEM_LANGUAGE, "default");//系统语言
		addVariable(SYSTEM_LONG_CLICK_TIME, "60");//长按时间
		addVariable(SYSTEM_DRAGGED_DISTANCE, "20");//拖动距离
		addVariable(SERVER_IP, ClientController.SERVER_ADDRESS);//服务器IP
	}
	
	//指定变量是否存在
	public static boolean haveVariable(String key) {
		return storageMap.containsKey(key);
	}
	
	//获取指定变量
	public static String getStringVariable(String key) {
		return storageMap.get(key);
	}
	public static int getIntegerVariable(String key) {
		return Integer.parseInt(storageMap.get(key));
	}
	public static boolean getBooleanVariable(String key) {
		return storageMap.get(key).equals("TRUE");
	}
	
	//添加变量
	public static void addVariable(String key, String value) {
		storageMap.put(key, value);
	}
	public static void addVariable(String key, int value) {
		storageMap.put(key, Integer.toString(value));
	}
	public static void addVariable(String key, boolean value) {
		storageMap.put(key, value ? "TRUE" : "FALSE");
	}
	
	//修改变量
	public static void setVariable(String key, String value) {
		storageMap.replace(key, value);
	}
	public static void setVariable(String key, int value) {
		storageMap.replace(key, Integer.toString(value));
	}
	public static void setVariable(String key, boolean value) {
		storageMap.replace(key, value ? "TRUE" : "FALSE");
	}
	
	//运行入口
	public static void start() {
		register();
		if(fileExist()) load(); else save();
	}

	//文件是否存在
	public static boolean fileExist() {
		return new File(SAVEFILE_NAME).exists();
	}
	
	//读取设置
	public static void load() {
		try {
			File file = new File(SAVEFILE_NAME);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader input = new BufferedReader(reader);
			String line = input.readLine();
			String[] temp;
			while(line != null) {
				temp = line.split(" = ");
				setVariable(temp[0], temp[1]);
				line = input.readLine();
			}
			reader.close();
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	//保存设置
	public static void save() {
		try {
			File file = new File(SAVEFILE_NAME);
			file.createNewFile();
	        BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for (String key : storageMap.keySet()) {
				output.write(key + " = " + storageMap.get(key) + "\r\n");
			}
			output.flush();
			output.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static String getFileSizeText(long size) {
		String[] temp = {"B", "KB", "MB", "GB"};
		int a = 0;
		double number = size;
		while(number/1024 >= 1) {
			number = number/1024;
			a++;
		}
		return String.format("%.2f", number) + temp[a];
	}
	
}
