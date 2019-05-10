/*============================================================*/
/*  Connecter.java                                            */
/*------------------------------------------------------------*/
/*  网络连接管理器。                                                                             */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.server.controller.module;

import java.io.IOException;

import java.net.Socket;

import pers.lagomoro.stusystem.server.controller.ConnecterController;
import pers.lagomoro.stusystem.server.controller.ThreadController;

public abstract class Connecter extends Thread{

	protected String key = ConnecterController.CONNECTER;
	
	protected Socket socket;
	
	protected boolean active = true;
	
	public Connecter(){

	}
	
	public Connecter(String key, Socket socket){
		this.setKey(key);
		this.initialize(socket);
	}

	protected void initialize(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public abstract void run();

	public void release() {
    	try {
    		this.socket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
    	this.active = false;
    	ConnecterController.removeConnecter(this.getKey());
    	ThreadController.removeThread(this.getKey());
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public abstract void send(String text);

	public Socket getSocket() {
		return socket;
	}
	
}
