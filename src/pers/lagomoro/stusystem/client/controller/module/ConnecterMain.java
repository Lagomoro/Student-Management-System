package pers.lagomoro.stusystem.client.controller.module;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ConnectException;
import java.net.UnknownHostException;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.ConnecterController;
import pers.lagomoro.stusystem.client.model.CommandModel;

public class ConnecterMain extends Connecter {

    private BufferedReader reader;
	private BufferedWriter writer;
	
	public ConnecterMain(){
		super(ConnecterController.CONNECTER_MAIN, ClientController.SERVER_PORT);
	}

	@Override
	protected void initialize(int port) {
		super.initialize(port);
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		} catch (ConnectException e) {
			//服务器没开
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
				System.out.println("Receive from server: " + (temp.length() > 200 ? temp.substring(0, 200) + "...(" + temp.length() + ")" : temp));
				String[] command = temp.split(CommandModel.SEPARATE);
				CommandModel.commandActive(command[0], command[1]);
			}
		} catch (IOException e) {
			System.out.println("Server crashed.");
			this.release();
		}
	}
	
	@Override
	public void send(String text) {
		try {
			System.out.println("Send to server: " + (text.length() > 200 ? text.substring(0, 200) + "...(" + text.length() + ")" : text));
			this.writer.write(text);
			this.writer.newLine();
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendCommand(String command, String json) {
		this.send(command + CommandModel.SEPARATE + json);
	}
	
	@Override
	public void release() {
		super.release();
		try {
			this.reader.close();
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
