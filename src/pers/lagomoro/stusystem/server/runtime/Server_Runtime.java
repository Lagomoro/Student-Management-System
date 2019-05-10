/*============================================================*/
/*  Server_Main.java                                                 */
/*------------------------------------------------------------*/
/*  应用程序入口。                                                                                */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.server.runtime;

import pers.lagomoro.stusystem.server.model.CommandModel;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.model.MySQLModel;
import pers.lagomoro.stusystem.server.controller.ConsoleController;
import pers.lagomoro.stusystem.server.controller.ServerController;

public class Server_Runtime {

	public static void main(String[] args) {
		//加载服务器日志模块
		LogModel.start();
		//加载数据库
		MySQLModel.run();
		
		//注册指令
		CommandModel.start();
		
		//启动控制台管理
		ConsoleController.run();
		//启动服务器
		ServerController.run();
	}
}
