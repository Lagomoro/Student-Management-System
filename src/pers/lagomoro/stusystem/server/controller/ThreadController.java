/*============================================================*/
/*  ThreadManager.java                                        */
/*------------------------------------------------------------*/
/*  ������߳�״̬���̵߳Ĵ���                                                         */
/*  Author: Lagomoro<Yongrui Wang>                            */
/*============================================================*/
package pers.lagomoro.stusystem.server.controller;

import java.util.HashMap;

public class ThreadController {
	
	private static HashMap<String, Thread> threadMap = new HashMap<String, Thread>();
		
	//������߳�
	public static void addThread(String key,Thread thread) {
		threadMap.put(key, thread);
	}
	
	//�������е��߳�
	public static void runThread(String key) {
		threadMap.get(key).start();
	}
	
	//������̲߳�����
	public static void runThread(String key,Thread thread) {
		threadMap.put(key, thread);
		thread.start();
	}
	
	//�ж��߳�
	public static void stopThread(String key) {
		threadMap.get(key).interrupt();
	}
	
	//�Ƴ��߳�
	public static void removeThread(String key) {
		threadMap.remove(key);
	}
	
}
