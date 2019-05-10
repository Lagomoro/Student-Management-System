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
	public static final String ORGANIZATION = "山东大学齐鲁软件学院";
	public static final String LISENCE = "Copyright (c) 2019 Lagomoro All Rights Reserved.";

	public static void main(String[] args) {
		//加载语言库
		LanguageModel.launch();
		//读取配置文件
		StorageModel.start();
		
		//注册指令
		CommandModel.start();
		
		//启动周期更新线程
		ThreadController.startUpdate();
		//窗口控制器运行
		WindowController.run();
		
		//开始网络连接
		ClientController.run();
	}
	
}
