/*============================================================*/
/*  ThreadManager.java                                        */
/*------------------------------------------------------------*/
/*  管理多线程状态下线程的处理。                                                         */
/*  Author: Lagomoro<Yongrui Wang>                            */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

public class ThreadController {
	
	private static HashMap<String, Thread> threadMap = new HashMap<String, Thread>();
	
	public static final int UPDATE_MESC = 60;
	
	//线程常量，用于维持线程单一性
	public static final String THREAD_UPDATE = "Thread_Update";
		
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
	
    //注册一个更新线程，用于系统实时更新。
    public static void startUpdate() {
    	ThreadController.runThread(ThreadController.THREAD_UPDATE, new Thread() {
    		@Override
            public void run() {
    			try {
    				while(!isInterrupted()) {
        				update();
        				sleep(1000/60);
        			}
    			} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
    	});
	}
    
    //实时更新线程
    private static void update() {
    	WindowController.update();
    }
	
}
