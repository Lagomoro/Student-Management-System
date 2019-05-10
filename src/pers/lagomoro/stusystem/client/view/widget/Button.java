/**
 * @class: Button.java 
 * @description: Superclass Button of buttons #<extends JButton>
 * @author: Lagomoro <Yongrui Wang>
 * 
 * @submit: 2019/03/23
 **/
package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import pers.lagomoro.stusystem.client.controller.AdjusterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.client.view.module.Controllable;

@SuppressWarnings("serial")
public class Button extends JButton implements Controllable{
	
	protected int clickTime = 0;
	protected boolean dragAppend = false;
	protected String textKey;
	protected String toolTipKey;
	protected MouseEvent event;
	protected AdjusterController adjusterController;
	
	public Button(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}

	public Button(String textKey) {
		super();
		this.setTextKey(textKey);
		this.initialize();
		this.create();
		this.refresh();
	}

	protected void initialize() {
		this.setOpaque(false);
		this.setFocusPainted(false);
        this.setBorderPainted(false);
        this.setBorder(null);
        this.setContentAreaFilled(false);
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
		MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e)  {onMouseEntered(e); }
            @Override
            public void mouseExited(MouseEvent e)   {onMouseExited(e);  }
            @Override
            public void mousePressed(MouseEvent e)  {
				event = e;
				onMousePressed(e);
			}
            @Override
            public void mouseReleased(MouseEvent e) {
            	dragAppend = false;
            	onMouseReleased(e);
                if(getModel().isRollover()) {
                	onMouseReleasedInside(e);
                } else {
                	onMouseReleasedOutside(e);
	            }
            }
			@Override
            public void mouseClicked(MouseEvent e) {
				switch(e.getButton()) {
				case MouseEvent.BUTTON1 : {
					if(clickTime >= StorageModel.getIntegerVariable(StorageModel.SYSTEM_LONG_CLICK_TIME)){
		    			onMouseLongClicked(e);
		    		} else {
		    			onMouseClicked(e);
		    		}
					break;
				}
				case MouseEvent.BUTTON3 :{
					onMouseRightClicked(e);
					break;
				}
				}
            }
			@Override
            public void mouseMoved(MouseEvent e)  {
				onMouseMoved(e);
			}
			@Override
            public void mouseDragged(MouseEvent e) {
				if(!dragAppend && Math.hypot(e.getX() - event.getX(), e.getY() - event.getY()) > StorageModel.getIntegerVariable(StorageModel.SYSTEM_DRAGGED_DISTANCE)) {
					onMouseDraggedLot(event);
					dragAppend = true; 
				}
				onMouseDragged(e);
			}
        };
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
	}

	protected void onMouseEntered(MouseEvent e) {}	
	protected void onMouseExited(MouseEvent e) {}
	
	protected void onMousePressed(MouseEvent e) {}
	protected void onMouseLongPressed(MouseEvent e) {}
	
	protected void onMouseClicked(MouseEvent e) {}
	protected void onMouseRightClicked(MouseEvent e) {}
	protected void onMouseLongClicked(MouseEvent e) {}
	
	protected void onMouseReleased(MouseEvent e) {}
	protected void onMouseReleasedInside(MouseEvent e) {}
	protected void onMouseReleasedOutside(MouseEvent e) {}
	
	protected void onMouseMoved(MouseEvent e) {}
	protected void onMouseDragged(MouseEvent e) {}
	protected void onMouseMovedLot(MouseEvent e) {}
	protected void onMouseDraggedLot(MouseEvent e) {}
	
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
		if(getModel().isPressed() && getModel().isRollover()) {
			this.clickTime ++;
			if(this.clickTime == StorageModel.getIntegerVariable(StorageModel.SYSTEM_LONG_CLICK_TIME)) {
				this.onMouseLongPressed(this.event);
			}
		}else{
			this.clickTime = 0;
		}
	}
	
	public void refresh() {
		this.setText(this.textKey == null ? null : LanguageModel.get(this.textKey) == null ? this.textKey : LanguageModel.get(this.textKey));
		this.setToolTipText(this.toolTipKey == null ? null : LanguageModel.get(this.toolTipKey) == null ? this.toolTipKey : LanguageModel.get(this.toolTipKey));
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Show drawing interface.
	 **/
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		GraphicsController.setHint(graphics);
		this.paint(graphics);
        super.paintComponent(g);
    }
	
	protected void paint(Graphics2D graphics) {
		
	}
	
	public void setTextKey(String textKey) {
		this.textKey = textKey;
		this.refresh();
	}
	
	public void setToolTipKey(String toolTipKey) {
		this.toolTipKey = toolTipKey;
		this.refresh();
	}
	
}
