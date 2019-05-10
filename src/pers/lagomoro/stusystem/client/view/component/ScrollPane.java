package pers.lagomoro.stusystem.client.view.component;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import pers.lagomoro.stusystem.client.view.module.Controllable;
import pers.lagomoro.stusystem.client.view.module.ScrollBarUI;

@SuppressWarnings("serial")
public class ScrollPane extends JScrollPane implements Controllable{
	
	public ScrollPane(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}
	
	public ScrollPane(Component view){
		super(view);
		this.initialize();
		this.create();
		this.refresh();
	}
	
	public ScrollPane(Component view, boolean h, boolean v){
		super(view);
		this.initialize();
		this.create();
		if(!h) this.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		if(!v) this.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
	}

	protected void initialize(){
		this.setOpaque(false);  
		this.getViewport().setOpaque(false); 
		this.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.getVerticalScrollBar().setUI(new ScrollBarUI(20));
		this.getHorizontalScrollBar().setUI(new ScrollBarUI(20));
		this.getVerticalScrollBar().setUnitIncrement(30);
		this.getHorizontalScrollBar().setUnitIncrement(30);
	}
		
	protected void create() {

	}
    
	public void update() {
		
	}
	
	public void refresh() {
		this.revalidate();
		this.repaint();
	}
}
