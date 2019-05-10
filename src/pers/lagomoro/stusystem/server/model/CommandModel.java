package pers.lagomoro.stusystem.server.model;

import java.util.HashMap;

import pers.lagomoro.stusystem.server.controller.ConsoleController;
import pers.lagomoro.stusystem.server.controller.ServerController;
import pers.lagomoro.stusystem.server.model.module.Command;

public class CommandModel {
	
	private static HashMap<String, Command> commandMap = new HashMap<String, Command>();
	
	public static final String SEPARATE = " = ";
	
	//ָ�������Ƿ����
	public static boolean haveCommand(String key) {
		return commandMap.containsKey(key);
	}
	
	//��ȡָ������
	public static Command getCommand(String key) {
		return commandMap.get(key);
	}
	
	//���������
	public static void addCommand(String key, Command command) {
		commandMap.put(key, command);
	}
	
	//��������
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
