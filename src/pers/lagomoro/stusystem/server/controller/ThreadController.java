/*============================================================*/
/*  ThreadManager.java                                        */
/*------------------------------------------------------------*/
/*  管理多线程状态下线程的处理。                                                         */
/*  Author: Lagomoro<Yongrui Wang>                            */
/*============================================================*/
package pers.lagomoro.stusystem.server.controller;

import java.util.HashMap;

public class ThreadController {
	
	private static HashMap<String, Thread> threadMap = new HashMap<String, Thread>();
		
	//添加新线程
	public static void addThread(String key,Thread thread) {
		threadMap.put(key, thread);
	}
	
	//运行已有的线程
	public static void runThread(String key) {
		threadMap.get(key).start();
	}
	
	//添加新线程并运行
	public static void runThread(String key,Thread thread) {
		threadMap.put(key, thread);
		thread.start();
	}
	
	//中断线程
	public static void stopThread(String key) {
		threadMap.get(key).interrupt();
	}
	
	//移除线程
	public static void removeThread(String key) {
		threadMap.remove(key);
	}
	
}
