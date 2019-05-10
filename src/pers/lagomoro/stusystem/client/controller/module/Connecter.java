/*============================================================*/
/*  Connecter.java                                            */
/*------------------------------------------------------------*/
/*  网络连接管理器。                                                                             */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.client.controller.module;

import java.io.IOException;

import java.net.Socket;
import java.net.UnknownHostException;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.ConnecterController;
import pers.lagomoro.stusystem.server.controller.ThreadController;

public abstract class Connecter extends Thread{

	protected String key = ConnecterController.CONNECTER;
	
	protected Socket socket;
	
	protected boolean active = true;
	
	public Connecter(){

	}
	
	public Connecter(String key, int port){
		this.setKey(key);
		this.initialize(port);
	}

	protected void initialize(int port) {
		try {
			this.socket = new Socket(ClientController.getServerAddress(), port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public abstract void send(String text);
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
}
