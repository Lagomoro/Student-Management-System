package pers.lagomoro.stusystem.client.controller.module;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.ConnecterController;
import pers.lagomoro.stusystem.client.controller.ThreadController;

public class ConnecterFile extends Connecter {
	
	protected int status = 0;
	protected static final int UPLOAD = 1;
	protected static final int DOWNLOAD = 2;

	protected String path;
	
	protected FileProcessListener listener;

	public ConnecterFile(){
		super(ConnecterController.CONNECTER_FILE + "_" + ConnecterController.getConnecterID(), ClientController.FILE_PORT);
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
				byte[] bytes = new byte[1024];
				int length = 0;
				long process = 0;
				long speed = 0;
				LocalDateTime time = LocalDateTime.now();
				for(int i = 0;(length = fileInput.read(bytes, 0, bytes.length)) != -1;i++) {
					dataOutput.write(bytes, 0, length);
					dataOutput.flush();
					if(Duration.between(time, LocalDateTime.now()).toMillis() >= 500) {
						speed = (long)(1000d / Duration.between(time, LocalDateTime.now()).toMillis() * 1024 * i);
						time = LocalDateTime.now();
						i = 0;
					}
					process += length;
					if(this.listener != null) this.listener.onProcessingUpload(new FileEvent(process, speed, file.length()));
				}
				if(this.listener != null) this.listener.onSuccessUpload(new FileEvent(-2, 0, 0));
			} catch (IOException e) {
				if(this.listener != null) this.listener.onFailUpload(new FileEvent(-2, 0, 0));
				System.out.println("File MD5 already have, connecter release.");
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
			byte[] bytes = new byte[1024];
			int length = 0;
			long process = 0;
			long speed = 0;
			LocalDateTime time = LocalDateTime.now();
			for(int i = 0;(length = dataInput.read(bytes, 0, bytes.length)) != -1;i++) {
				fileOutput.write(bytes, 0, length);  
				fileOutput.flush();  
				if(Duration.between(time, LocalDateTime.now()).toMillis() >= 500) {
					speed = (long)(1000d / Duration.between(time, LocalDateTime.now()).toMillis() * 1024 * i);
					time = LocalDateTime.now();
					i = 0;
				}
				process += length;
				if(this.listener != null) this.listener.onProcessingDownload(new FileEvent(process, speed, 0));
			}  
			if(this.listener != null) this.listener.onSuccessDownload(new FileEvent(-2, 0, 0));
		} catch (IOException e) {
			if(this.listener != null) this.listener.onFailDownload(new FileEvent(-2, 0, 0));
			e.printStackTrace();
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
	
	public void addListener(FileProcessListener listener){
		this.listener = listener;
	}
	
}
