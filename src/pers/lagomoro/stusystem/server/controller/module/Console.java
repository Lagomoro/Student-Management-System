package pers.lagomoro.stusystem.server.controller.module;

public class Console{

	public String[] command;
	
	public Console(String command) {
		this.command = command.split(" ");
	}
	
	public int getInt(int place) {
		if(place < 0 || place >= this.command.length) {
			return 0;
		}
		return Integer.parseInt(this.command[place]);
	}
	
	public String getString(int place) {
		if(place < 0 || place >= this.command.length) {
			return null;
		}
		return this.command[place];
	}
	
	public boolean getBoolean(int place) {
		if(place < 0 || place >= this.command.length) {
			return false;
		}
		return !(this.command[place].equals("false") || this.command[place].equals("FALSE") || this.command[place].equals("0"));
	}
	
	public int length() {
		return this.command.length;
	}
	
	public int i(int place) {
		return this.getInt(place - 1);
	}
	
	public String s(int place) {
		return this.getString(place - 1);
	}
	
	public boolean b(int place) {
		return this.getBoolean(place - 1);
	}
	
}
