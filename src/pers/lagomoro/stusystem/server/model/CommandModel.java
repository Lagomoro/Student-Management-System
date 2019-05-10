package pers.lagomoro.stusystem.server.model;

import java.util.HashMap;

import pers.lagomoro.stusystem.server.controller.ConsoleController;
import pers.lagomoro.stusystem.server.controller.ServerController;
import pers.lagomoro.stusystem.server.model.module.Command;

public class CommandModel {
	
	private static HashMap<String, Command> commandMap = new HashMap<String, Command>();
	
	public static final String SEPARATE = " = ";
	
	//指定命令是否存在
	public static boolean haveCommand(String key) {
		return commandMap.containsKey(key);
	}
	
	//获取指定命令
	public static Command getCommand(String key) {
		return commandMap.get(key);
	}
	
	//添加新命令
	public static void addCommand(String key, Command command) {
		commandMap.put(key, command);
	}
	
	//运行命令
	public static void commandActive(String key, String data) {
		if(haveCommand(key)) {
			getCommand(key).CommandActive(data);
		}
	}

	public static void start() {
		ServerController.addCommand();
		ConsoleController.addCommand();
	}
	
}
