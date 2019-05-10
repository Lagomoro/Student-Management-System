package pers.lagomoro.stusystem.server.controller.module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import pers.lagomoro.stusystem.server.controller.ThreadController;
import pers.lagomoro.stusystem.server.model.CommandModel;
import pers.lagomoro.stusystem.server.model.LogModel;

public class ConnecterPaintAndGuess extends Connecter{

	private BufferedReader reader;
	private BufferedWriter writer;
	private ServerPaintAndGuess server;

	public ConnecterPaintAndGuess(String key, Socket socket, ServerPaintAndGuess server) {
		super(key, socket);
		this.server = server;
	}

	@Override
	protected void initialize(Socket socket) {
		super.initialize(socket);
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (ConnectException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(this.active) {
			this.receiveCommand();
		}
	}
	
	protected void receiveCommand() {
		try {
			String temp;
			if((temp = this.reader.readLine()) != null) {
				LogModel.log("Receive from client <" + this.getKey() + "> : " + temp);
				String[] command = temp.split(CommandModel.SEPARATE);
				this.server.commandActive(command[0], command[1]);
			}
		} catch (IOException e) {
//			e.printStackTrace();
			LogModel.log("Disconnect from client <" + this.getKey() + "> : client shutdown.");
			this.release();
		}
	}

	@Override
	public void send(String text) {
		try {
			LogModel.log("Send to client <" + this.getKey() + "> : " + text);
			this.writer.write(text);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
//			e.printStackTrace();
			this.release();
		}
	}
	
	public void sendCommand(String command, String json) {
		this.send(command + CommandModel.SEPARATE + json);
	}

	@Override
	public void release() {
		try {
			this.socket.close();
			this.reader.close();
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.active = false;
		this.server.removeConnecter(this.getKey());
    	ThreadController.removeThread(this.getKey());
	}

}
