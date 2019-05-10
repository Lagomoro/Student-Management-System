/**
 * @class: Window.java 
 * @description: Window Class #<extends JFrame>
 * @author: Lagomoro <Yongrui Wang>
 * 
 * @submit: 2019/03/23
 **/
package pers.lagomoro.stusystem.client.view.component;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JWindow;

import pers.lagomoro.stusystem.client.controller.AdjusterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.view.module.Controllable;

@SuppressWarnings("serial")
public class Flash extends JWindow implements Controllable{
	
	protected String key = WindowController.FLASH;
	protected AdjusterController adjusterController;
	
	public Flash(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}
	
	public Flash(String key){
		super();
		this.setKey(key);
		this.initialize();
		this.create();
		this.refresh();
	}
	
	public Flash(Frame owner) {
        super(owner);
        this.initialize();
		this.create();
    }
		
	protected void initialize(){
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setBackground(GraphicsController.EMPTY_COLOR);
	}
		
	protected void create() {
		this.createListener();
		this.createController();
	}
	
	/**
	 * Create event listeners.
	 **/
	protected void createListener() {
		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowGainedFocus(WindowEvent e) {super.windowGainedFocus(e); onFocusGain(e);}
			@Override
			public void windowLostFocus(WindowEvent e) {  super.windowLostFocus(e);   onFocusLost(e);}
			@Override
			public void windowClosed(WindowEvent e) {     super.windowClosed(e);      onClosed(e);}
        };
		this.addWindowListener(adapter);
		this.addWindowFocusListener(adapter);
	}
	
	protected void onClosed(WindowEvent e) {}

	protected void onFocusGain(WindowEvent e) {}	
	protected void onFocusLost(WindowEvent e) {}
		
	protected void createController() {
		this.adjusterController = new AdjusterController();
	}

	/**
	 * Update interface.
	 **/
	public void update() {
		this.adjusterController.update();
		for (Component c: this.getContentPane().getComponents()) {
			Controllable.updateChild(c);
		}
	}

	public void refresh() {
		this.revalidate();
		this.repaint();
		for (Component c: this.getContentPane().getComponents()) {
			Controllable.refreshChild(c);
		}
	}
    
	public void active() {
		this.setVisible(true);
	}
	
	public void deactive() {
		this.setVisible(false);
	}

	public String getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key = key;
	}
	
    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        setLocationRelativeTo(null);
    }

}
