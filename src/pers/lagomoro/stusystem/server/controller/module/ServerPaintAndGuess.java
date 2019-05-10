package pers.lagomoro.stusystem.server.controller.module;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;

import pers.lagomoro.stusystem.data.DataPaintAndGuess;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.server.controller.ServerController;
import pers.lagomoro.stusystem.server.controller.ThreadController;
import pers.lagomoro.stusystem.server.model.CommandModel;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.model.MySQLModel;

public class ServerPaintAndGuess extends Server {
	
	private HashMap<String, ConnecterPaintAndGuess> connecterMap;
	private LinkedList<DataPaintAndGuess> bank;
	private DataPaintAndGuess nowPlay;
	private boolean gameStart;
	
	public static final int MAX = 2;
	public static final int TIME = 60;

	public ServerPaintAndGuess(ServerSocket serverSocket){
		super();
		this.setKey(ServerController.SERVER_PAINT_AND_GUESS + ":" + serverSocket.getLocalPort());
		this.initialize(serverSocket);
	}
	
	protected void initialize(ServerSocket serverSocket) {
		this.server = serverSocket;
		this.connecterMap = new HashMap<String, ConnecterPaintAndGuess>();
		this.bank = MySQLModel.getPaintAndGuess();
	}

	@Override
	protected void update() {
		if(this.connecterMap.size() < MAX) {
			try {
				Socket socket = this.server.accept();
				LogModel.log("<" + socket.getInetAddress() + ":" + socket.getPort() + "> Linked to <" + this.getKey() + ">.");
				LogModel.log("Connecter created <" + socket.getInetAddress() + ":" + socket.getPort() + ".PaintAndGuess>.");
				this.addConnecter(new ConnecterPaintAndGuess(socket.getInetAddress() + ":" + socket.getPort() + ".PaintAndGuess", socket, this));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			this.gameStart = true;
			for(String connecter : this.connecterMap.keySet()) {
				this.chooseBank();
				commandActive("sendBank", connecter);
				int time = TIME;
				do {
					try {
						commandActive("sendTime", Integer.toString(time));
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch(Exception e) {
						break;
					}
					if(this.connecterMap.size() < MAX) break;
				}while((time = time-1) > 0);
				if(this.connecterMap.size() < MAX) {
					commandActive("resetAll", "0");	
					break;
				}
			}
			this.gameStart = false;
			CommandModel.commandActive("refreshPaintAndGuessGameRoom", "0");
		}
	}
	
	public boolean isBusy() {
		return this.gameStart;
	}

	public void addConnecter(ConnecterPaintAndGuess connecter) {
		if(this.gameStart) {
			connecter.release();
		}else {
			this.connecterMap.put(connecter.getKey(), connecter);
			ThreadController.runThread(connecter.getKey(), connecter);
		}
	}
	
	public void removeConnecter(String key) {
		this.connecterMap.remove(key);
	}
	
	public void commandActive(String command, String json) {
		switch(command) {
		case "guess" : {
			DataUpdate data = DataUpdate.fromJson(json);
			if(this.isRight(data.tag)) {
				sendCommendToCurrent(data.connecterKey, "guessSuccess", "0");
			}else {
				sendCommendToCurrent(data.connecterKey, "guessFail", "0");
			}
			break;
		}
		case "draw" : {
			DataUpdate data = DataUpdate.fromJson(json);
			sendCommendToAllOther(data.connecterKey, "redraw", data.tag);
			break;
		}
		case "sendBank" : {
			sendCommendToCurrent(json, "startDraw", this.nowPlay.toString());			
			sendCommendToAllOther(json, "startGuess", this.nowPlay.toString());
			break;
		}
		case "sendTime" : {
			sendCommendToAll("timeUpdate", json);	
			break;
		}
		case "resetAll" : {
			sendCommendToAll("reset", json);	
			break;
		}
		case "draw:AddPoint" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:AddPoint", data.tag); break;}
		case "draw:LineStart" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:LineStart", data.tag); break;}
		case "draw:Undo" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:Undo", data.tag); break;}
		case "draw:Redo" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:Redo", data.tag); break;}
		case "draw:UndoAll" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:UndoAll", data.tag); break;}
		case "draw:RedoAll" : {DataUpdate data = DataUpdate.fromJson(json); sendCommendToAllOther(data.connecterKey, "redraw:RedoAll", data.tag); break;}
		}
	}
	
	private boolean isRight(String guess) {
		return guess.equals(this.nowPlay.item);
	}
	
	private void chooseBank() {
		this.nowPlay = this.bank.get((int)(Math.random() * this.bank.size()));
	}
	
	public void sendCommendToCurrent(String connecterKey, String command, String json) {
		for(String connecter : this.connecterMap.keySet()) {
			if(connecter.equals(connecterKey)) this.connecterMap.get(connecter).sendCommand(command, json);
		}
	}
	
	public void sendCommendToAllOther(String connecterKey, String command, String json) {
		for(String connecter : this.connecterMap.keySet()) {
			if(!connecter.equals(connecterKey)) this.connecterMap.get(connecter).sendCommand(command, json);
		}
	}
	
	public void sendCommendToAll(String command, String json) {
		for(String connecter : this.connecterMap.keySet()) {
			this.connecterMap.get(connecter).sendCommand(command, json);
		}
	}
	
}
