/*============================================================*/
/*  ThreadManager.java                                        */
/*------------------------------------------------------------*/
/*  ������߳�״̬���̵߳Ĵ���                                                         */
/*  Author: Lagomoro<Yongrui Wang>                            */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

public class ThreadController {
	
	private static HashMap<String, Thread> threadMap = new HashMap<String, Thread>();
	
	public static final int UPDATE_MESC = 60;
	
	//�̳߳���������ά���̵߳�һ��
	public static final String THREAD_UPDATE = "Thread_Update";
		
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
	
    //ע��һ�������̣߳�����ϵͳʵʱ���¡�
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
    
    //ʵʱ�����߳�
    private static void update() {
    	WindowController.update();
    }
	
}
