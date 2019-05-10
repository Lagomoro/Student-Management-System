/*============================================================*/
/*  ConnecterManager.java                                     */
/*------------------------------------------------------------*/
/*  �������ӵĹ�������                                                                          */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

import pers.lagomoro.stusystem.client.controller.module.Connecter;
import pers.lagomoro.stusystem.client.controller.ThreadController;

public class ConnecterController {
	
	private static HashMap<String, Connecter> connecterMap = new HashMap<String, Connecter>();
	
	//���ӳ���������ά�ִ��ڵ�һ��
	public static final String CONNECTER = "Connecter";
	public static final String CONNECTER_MAIN = "Connecter_Main";
	public static final String CONNECTER_FILE = "Connecter_File";
	public static final String CONNECTER_PAINT_AND_GUESS = "Connecter_PaintAndGuess";
	
	public static int connecterID = 0;
	
	//ָ�������Ƿ����
	public static boolean haveConnecter(String key) {
		return connecterMap.containsKey(key);
	}
	
	//��ȡָ������
	public static Connecter getConnecter(String key) {
		return connecterMap.get(key);
	}
	
	//��ָ�����ӷ�����Ϣ
	public static void send(String key, String text) {
		connecterMap.get(key).send(text);
	}
	
	//���������
	public static void addConnecter(Connecter connecter) {
		connecterMap.put(connecter.getKey(), connecter);
		ThreadController.runThread(connecter.getKey(), connecter);
	}
	
	//׼��������
	public static void prepareConnecter(Connecter connecter) {
		connecterMap.put(connecter.getKey(), connecter);
		ThreadController.addThread(connecter.getKey(), connecter);
	}
	
	//ɾ������
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
