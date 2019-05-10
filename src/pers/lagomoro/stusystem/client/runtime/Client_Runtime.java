/*============================================================*/
/*  Client_Runtime.java                                       */
/*------------------------------------------------------------*/
/*  The entrance of client.                                   */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.runtime;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.ThreadController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.client.model.CommandModel;

public class Client_Runtime {
	
	public static final String VERSION = "V2.1.1(20190413-0x00EA13)";
	public static final String AUTHOR = "Lagomoro";
	public static final String ORGANIZATION = "ɽ����ѧ��³���ѧԺ";
	public static final String LISENCE = "Copyright (c) 2019 Lagomoro All Rights Reserved.";

	public static void main(String[] args) {
		//�������Կ�
		LanguageModel.launch();
		//��ȡ�����ļ�
		StorageModel.start();
		
		//ע��ָ��
		CommandModel.start();
		
		//�������ڸ����߳�
		ThreadController.startUpdate();
		//���ڿ���������
		WindowController.run();
		
		//��ʼ��������
		ClientController.run();
	}
	
}
