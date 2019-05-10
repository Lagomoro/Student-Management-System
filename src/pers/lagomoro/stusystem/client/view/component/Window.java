/**
 * @class: Window.java 
 * @description: Window Class #<extends JFrame>
 * @author: Lagomoro <Yongrui Wang>
 * 
 * @submit: 2019/03/23
 **/
package pers.lagomoro.stusystem.client.view.component;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import pers.lagomoro.stusystem.client.controller.AdjusterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.view.module.Controllable;

@SuppressWarnings("serial")
public class Window extends JFrame{
	
	protected String key = WindowController.WINDOW;
	protected AdjusterController adjusterController;
	
	public Window(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}
	
	public Window(String key){
		super();
		this.setKey(key);
		this.initialize();
		this.create();
		this.refresh();
	}
		
	protected void initialize(){
		this.setLayout(null);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setResizable(false);
		this.setBackground(GraphicsController.EMPTY_COLOR);
		this.setIconImage(ImageModel.getLogoImage());
	}
		
	protected void create() {
		this.createListener();
		this.createController();
		this.createAdjuster();
	}
	
	/**
	 * Create event listeners.
	 **/
	protected void createListener() {
		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowIconified(WindowEvent e) {  super.windowIconified(e);   onIconified(e);}
			@Override
			public void windowDeiconified(WindowEvent e) {super.windowDeiconified(e); onDeiconified(e);}
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
	
	protected void onIconified(WindowEvent e) {}	
	protected void onDeiconified(WindowEvent e) {}
	protected void onClosed(WindowEvent e) {}

	protected void onFocusGain(WindowEvent e) {}	
	protected void onFocusLost(WindowEvent e) {}
	
	public void onApplyLink() {}	
	public void onRemoveLink() {}
	
	protected void createController() {
		this.adjusterController = new AdjusterController();
	}

	protected void createAdjuster() {
		
	}	
	
	public void addAdjuster(Adjuster adjuster) {
		this.adjusterController.addAdjuster(adjuster);
	}
	
	public Adjuster getAdjuster(String key) {
		return this.adjusterController.getAdjuster(key);
	}
	
	protected int getDoubleInt(String key) {
		return (int)((Adjuster_Double)this.getAdjuster(key)).getValue();
	}
	
	protected double getDouble(String key) {
		return ((Adjuster_Double)this.getAdjuster(key)).getValue();
	}
	
	protected Color getColor(String key) {
		return ((Adjuster_Color)this.getAdjuster(key)).getValue();
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
