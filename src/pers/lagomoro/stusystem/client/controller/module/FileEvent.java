package pers.lagomoro.stusystem.client.controller.module;

public class FileEvent{
	
	public long process;
	public long speed;
	public long length;
	
	public FileEvent(long process, long speed, long length) {
		this.process = process;
		this.speed = speed;
		this.length = length;
	}
	
}
