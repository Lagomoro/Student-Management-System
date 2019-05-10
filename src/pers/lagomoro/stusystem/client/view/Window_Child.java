package pers.lagomoro.stusystem.client.view;

import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.view.component.WindowSceneable;

@SuppressWarnings("serial")
public class Window_Child extends WindowSceneable {
	
	public Window_Child(){
		super(WindowController.WINDOW_CHILD + "_" + "0","Test");
	}
	
	public Window_Child(String key, String titleKey){
		super(WindowController.WINDOW_CHILD + "_" + key, titleKey);
	}
	
}
