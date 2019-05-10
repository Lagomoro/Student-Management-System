package pers.lagomoro.stusystem.server.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class LogModel {
	
	public static final String LOG_PATH = MySQLModel.SERVER_BASE + "log/";
	
	public static File file;
	public static File fileAdmin;
	
	public static void start() {
		File base = new File(MySQLModel.SERVER_BASE);
        if (!base.exists()) base.mkdir();
		
        File documents = new File(LOG_PATH);
        if (!documents.exists()) documents.mkdir();
		
        file = new File(LOG_PATH + "serverLog " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".log");
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fileAdmin = new File(LOG_PATH + "adminLog " + new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()) + ".log");
		try {
			BufferedWriter output = new BufferedWriter(new FileWriter(fileAdmin));
			output.flush();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		log("Log file created.");
	}
	
	public static void log(String log) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			Vector<String> logList = new Vector<String>();
			String line = input.readLine();
			while(line != null) {
				logList.add(line);
				line = input.readLine();
			}
			input.close();
			
			BufferedWriter output = new BufferedWriter(new FileWriter(file));
			for(String s : logList) {
				output.write(s + "\r\n");
			}
			if(log.length() > 200) log = log.substring(0, 200) + "...(" + log.length() + ")";
			line = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] " + log;
			output.write(line + "\r\n");
			output.flush();
			output.close();
			System.out.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static void adminLog(String ip, int class_id, String username, String log) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(fileAdmin)));
			Vector<String> logList = new Vector<String>();
			String line = input.readLine();
			while(line != null) {
				logList.add(line);
				line = input.readLine();
			}
			input.close();
			
			BufferedWriter output = new BufferedWriter(new FileWriter(fileAdmin));
			for(String s : logList) {
				output.write(s + "\r\n");
			}
			line = "[" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "] <IP : " + ip + "> <CLASSID : " + class_id + "> <USERNAME : " + username + "> : " + log;
			output.write(line + "\r\n");
			output.flush();
			output.close();
			System.err.println("Warning : Administrator sensitive operation detected!");
			System.err.println(line);
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	
	public static void shutdown(String log) {
		log(log);
		System.exit(0);
	}
	
}
