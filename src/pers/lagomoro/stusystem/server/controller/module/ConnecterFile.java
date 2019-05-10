package pers.lagomoro.stusystem.server.controller.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import pers.lagomoro.stusystem.server.controller.module.FileProcessListener;
import pers.lagomoro.stusystem.server.model.LogModel;
import pers.lagomoro.stusystem.server.controller.module.FileEvent;
import pers.lagomoro.stusystem.server.controller.ThreadController;

public class ConnecterFile extends Connecter{

	protected int status = 0;
	protected static final int UPLOAD = 1;
	protected static final int DOWNLOAD = 2;

	protected String path;
	
	protected FileProcessListener listener;
	
	public ConnecterFile(String key, Socket socket) {
		super(key, socket);
	}

	@Override
	public void run() {
		switch (this.status) {
		case UPLOAD:upload(this.path);break;
		case DOWNLOAD:download(this.path);break;
		default:break;
		}
	}
	
	@Override
	public void send(String filePath) {
		
	}
	
	public void startUpload(String path){
		this.status = 1;
		this.path = path;
		ThreadController.runThread(this.getKey());
	}
	
	public void startDownload(String path){
		this.status = 2;
		this.path = path;
		ThreadController.runThread(this.getKey());
	}
	
	protected void upload(String path) {
		File file = new File(path);
		if(file.exists()) {
			FileInputStream fileInput = null;
			DataOutputStream dataOutput = null;
			try {
				fileInput = new FileInputStream(file);
				dataOutput = new DataOutputStream(this.socket.getOutputStream());
				if(this.listener != null) this.listener.onStartUpload(new FileEvent(0, 0, 0));
				LogModel.log("Start send file <" + file.getName() + "> to Connecter <" + this.getKey() + ">...");
				byte[] bytes = new byte[1024];
				int length = 0;
				while((length = fileInput.read(bytes, 0, bytes.length)) != -1) {  
					dataOutput.write(bytes, 0, length);
					dataOutput.flush();
					if(this.listener != null) this.listener.onProcessingUpload(new FileEvent(0, 0, 0));
				}
				if(this.listener != null) this.listener.onSuccessUpload(new FileEvent(-1, 0, 0));
				LogModel.log("Sussessful send file <" + file.getName() + "> to Connecter <" + this.getKey() + ">.");
			} catch (IOException e) {
				if(this.listener != null) this.listener.onFailUpload(new FileEvent(-1, 0, 0));
				LogModel.log("Disconnect from connecter <" + this.getKey() + "> : client shutdown.");
			} finally {
				try {
					if(fileInput != null) fileInput.close();
					if(dataOutput != null) dataOutput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.release();
	}
	
	protected void download(String path) {
		File file = new File(path);
		FileOutputStream fileOutput = null;
		DataInputStream dataInput = null;
		try {
			fileOutput = new FileOutputStream(file);
			dataInput = new DataInputStream(this.socket.getInputStream());  
			if(this.listener != null) this.listener.onStartDownload(new FileEvent(0, 0, 0));
			LogModel.log("Start receive file <" + file.getName() + "> from Connecter <" + this.getKey() + ">...");
			byte[] bytes = new byte[1024];
			int length = 0;
			while((length = dataInput.read(bytes, 0, bytes.length)) != -1) {  
				fileOutput.write(bytes, 0, length);  
				fileOutput.flush();  
				if(this.listener != null) this.listener.onProcessingDownload(new FileEvent(0, 0, 0));
			}  
			if(this.listener != null) this.listener.onSuccessDownload(new FileEvent(-1, 0, 0)); 
			LogModel.log("Successful receive file <" + file.getName() + "> from Connecter <" + this.getKey() + ">.");
		} catch (IOException e) {
			if(this.listener != null) this.listener.onFailDownload(new FileEvent(-1, 0, 0)); 
			LogModel.log("Disconnect from connecter <" + this.getKey() + "> : client shutdown.");
		} finally {
			try {
				if(fileOutput != null) fileOutput.close();
				if(dataInput != null) dataInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.release();
	}
	
	@Override
	public void release() {
		super.release();
		LogModel.log("Connecter <" + this.getKey() + "> shutdown successfully.");
	}
	
	public void addListener(FileProcessListener listener){
		this.listener = listener;
	}
	
}
