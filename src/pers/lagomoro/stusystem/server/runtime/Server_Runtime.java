/*============================================================*/
/*  Server_Main.java                                                 */
/*------------------------------------------------------------*/
/*  Ӧ�ó�����ڡ�                                                                                */
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
		//���ط�������־ģ��
		LogModel.start();
		//�������ݿ�
		MySQLModel.run();
		
		//ע��ָ��
		CommandModel.start();
		
		//��������̨����
		ConsoleController.run();
		//����������
		ServerController.run();
	}
}
