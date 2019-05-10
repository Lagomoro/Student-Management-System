package pers.lagomoro.stusystem.server.controller.module;

import java.io.IOException;

import java.net.Socket;

import pers.lagomoro.stusystem.server.controller.module.ConnecterFile;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.controller.ConnecterController;
import pers.lagomoro.stusystem.server.controller.ServerController;

public class ServerFile extends Server {
	
	public ServerFile(){
		super(ServerController.SERVER_FILE, ServerController.SERVER_FILE_PORT);
	}

	@Override
	protected void update() {
		try {
			Socket socket = this.server.accept();
			LogModel.log("<" + socket.getInetAddress() + ":" + socket.getPort() + "> Linked to <" + this.getKey() + ">, Ready for processing.");
			LogModel.log("Connecter created <" + socket.getInetAddress() + ":" + socket.getPort() + ".File>.");
			ConnecterController.prepareConnecter(new ConnecterFile(socket.getInetAddress() + ":" + socket.getPort() + ".File", socket));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
