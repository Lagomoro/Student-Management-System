package pers.lagomoro.stusystem.server.controller.module;

import java.io.IOException;

import java.net.Socket;

import pers.lagomoro.stusystem.server.controller.ConnecterController;
import pers.lagomoro.stusystem.server.controller.ServerController;
import pers.lagomoro.stusystem.server.model.LogModel;

public class ServerMain extends Server {

	public ServerMain(){
		super(ServerController.SERVER_MAIN, ServerController.SERVER_MAIN_PORT);
	}

	@Override
	protected void update() {
		try {
			Socket socket = this.server.accept();
			LogModel.log("<" + socket.getInetAddress() + ":" + socket.getPort() + "> Linked to <" + this.getKey() + ">.");
			LogModel.log("Connecter created <" + socket.getInetAddress() + ":" + socket.getPort() + ".Main>.");
			ConnecterController.addConnecter(new ConnecterMain(socket.getInetAddress() + ":" + socket.getPort() + ".Main", socket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
