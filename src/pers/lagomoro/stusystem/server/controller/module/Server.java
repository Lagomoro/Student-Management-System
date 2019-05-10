/*============================================================*/
/*  Server.java                                               */
/*------------------------------------------------------------*/
/*  服务器管理器。                                                                                */
/*  Author: Lagomoro <Yongrui Wang>                           */
/*============================================================*/
package pers.lagomoro.stusystem.server.controller.module;

import java.io.IOException;
import java.net.ServerSocket;

import pers.lagomoro.stusystem.server.controller.ServerController;
import pers.lagomoro.stusystem.server.controller.ThreadController;
import pers.lagomoro.stusystem.server.model.LogModel;

public abstract class Server extends Thread{

	protected String key = ServerController.SERVER;
	
	protected ServerSocket server;
	
	protected boolean active = true;
	
	public Server(){
		
	}
	
	public Server(String key, int port){
		this.setKey(key);
		this.initialize(port);
	}

	protected void initialize(int port) {
		try {
			this.server = new ServerSocket(port);
			LogModel.log("Server launched <" + this.getKey() + ">.");
			LogModel.log("<" + this.getKey() + "> Ready to receive connection...");
		} catch (IOException e) {
			LogModel.shutdown("Server crashed <" + e.toString() + ">.");
		}
	}
	
	@Override
	public void run() {
		while (this.active) {
			this.update();
		}
	}
	
	protected abstract void update();

	public void release() {
    	try {
    		this.server.close();
		}catch(IOException e){
			e.printStackTrace();
		}
    	this.active = false;
    	ThreadController.removeThread(this.getKey());
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getLocalPort() {
		return this.server.getLocalPort();
	}

}
