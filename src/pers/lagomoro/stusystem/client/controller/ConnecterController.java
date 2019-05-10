/*============================================================*/
/*  ConnecterManager.java                                     */
/*------------------------------------------------------------*/
/*  网络连接的管理器。                                                                          */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

import pers.lagomoro.stusystem.client.controller.module.Connecter;
import pers.lagomoro.stusystem.client.controller.ThreadController;

public class ConnecterController {
	
	private static HashMap<String, Connecter> connecterMap = new HashMap<String, Connecter>();
	
	//连接常量，用于维持窗口单一性
	public static final String CONNECTER = "Connecter";
	public static final String CONNECTER_MAIN = "Connecter_Main";
	public static final String CONNECTER_FILE = "Connecter_File";
	public static final String CONNECTER_PAINT_AND_GUESS = "Connecter_PaintAndGuess";
	
	public static int connecterID = 0;
	
	//指定连接是否存在
	public static boolean haveConnecter(String key) {
		return connecterMap.containsKey(key);
	}
	
	//获取指定连接
	public static Connecter getConnecter(String key) {
		return connecterMap.get(key);
	}
	
	//向指定连接发送消息
	public static void send(String key, String text) {
		connecterMap.get(key).send(text);
	}
	
	//添加新连接
	public static void addConnecter(Connecter connecter) {
		connecterMap.put(connecter.getKey(), connecter);
		ThreadController.runThread(connecter.getKey(), connecter);
	}
	
	//准备新连接
	public static void prepareConnecter(Connecter connecter) {
		connecterMap.put(connecter.getKey(), connecter);
		ThreadController.addThread(connecter.getKey(), connecter);
	}
	
	//删除连接
	public static void releaseConnecter(String key) {
		connecterMap.get(key).release();
	}
	
	public static void removeConnecter(String key) {
		connecterMap.remove(key);
	}
	
	public static int getConnecterID() {
		return connecterID++;
	}
	
}
