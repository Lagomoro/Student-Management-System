/**
 * @class: Button.java 
 * @description: Superclass Button of buttons #<extends JButton>
 * @author: Lagomoro <Yongrui Wang>
 * 
 * @submit: 2019/03/23
 **/
package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;

@SuppressWarnings("serial")
public class ButtonStyled extends Button{
	
	protected Color textColor;

	protected Color hoverColor;
	protected Color defaultColor;
	protected Color touchColor;
	
	protected int radius = 0;
	protected int shade = 0;
	
	protected boolean isWarning = false;
	protected boolean isProcessing = false;
	
	protected boolean locked = false;
	protected boolean active = true;
	
	protected int status = 0;
	
	public ButtonStyled(){
		super();
	}
	
	public ButtonStyled(String textKey){
		super(textKey);
	}
	
	public ButtonStyled(String textKey, Color hoverColor, Color defaultColor, Color touchColor){
		super(textKey);
		this.setColor(hoverColor, defaultColor, touchColor);
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setColor(GraphicsController.EMPTY_COLOR, GraphicsController.EMPTY_COLOR, GraphicsController.EMPTY_COLOR);
		this.setTextColor(GraphicsController.DEFAULT_TEXT_COLOR);
		this.setBackground(this.defaultColor);
	}

	@Override
	protected void onMouseEntered(MouseEvent e) {
		super.onMouseEntered(e);
		this.status = 1;
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getBackground(), this.hoverColor, 20));
	}	

	@Override
	protected void onMouseExited(MouseEvent e) {
		super.onMouseExited(e);
		this.status = 0;
		if(this.isWarning) {
			this.setWarning(false);
		}
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getBackground(), this.defaultColor, 20));
	}
	
	@Override
	protected void onMousePressed(MouseEvent e) {
		super.onMousePressed(e);
		this.status = 2;
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getBackground(), this.touchColor, 20));
	}

	@Override
	protected void onMouseReleasedInside(MouseEvent e) {
		super.onMouseReleasedInside(e);
		this.status = 1;
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getBackground(), this.hoverColor, 20));
	}
	
	@Override
	protected void createAdjuster() {
		super.createAdjuster();
		Adjuster_Color adjuster = new Adjuster_Color("Color", this.defaultColor, this.defaultColor, 20) {
			@Override
			protected void onValueChange() {
				super.onValueChange();
				setBackground(this.getValue());
				repaint();
			}
		};
		adjuster.setAlways(true);
		this.adjusterController.addAdjuster(adjuster);
		
		Adjuster_Double adjuster2 = new Adjuster_Double("Process", 0, 360, 60);
		adjuster2.setCyclic(true);
		adjuster2.setRoundMode(true);
		this.adjusterController.addAdjuster(adjuster2);
	}
	
	/**
	 * Set color of button.
	 **/
	public void setColor(Color hoverColor, Color defaultColor, Color touchColor) {
		this.hoverColor = hoverColor;
		this.defaultColor = defaultColor;
		this.touchColor = touchColor;
		if(this.adjusterController != null) {
			this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.defaultColor, this.defaultColor, 20));		
		}
	}
	public void setColor(Color defaultColor) {
		this.setColor(new Color(defaultColor.getRed() - 20, defaultColor.getGreen() - 20, defaultColor.getBlue() - 20, defaultColor.getAlpha()),
				defaultColor,
				new Color(defaultColor.getRed() + 20, defaultColor.getGreen() + 20, defaultColor.getBlue() + 20, defaultColor.getAlpha()));
	}
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		this.setForeground(textColor);
	}
	
	/**
	 * Set radius of button.
	 **/
	public void setRadius(int radius) {
		this.radius = radius;
		this.resetBorder();
	}
	
	public void setShade(int shade) {
		int delta = shade - this.shade;
		this.setBounds(this.getX() - delta, this.getY() - delta, this.getWidth() + delta*2, this.getHeight() + delta * 2);
		this.shade = shade;
		this.resetBorder();
	}
	
	public void resetBorder() {
		int border = this.radius + this.shade;
		this.setBorder(BorderFactory.createEmptyBorder(0, border, 0, border));
	}
	
	protected void setWarning(boolean warning) {
		this.isWarning = warning;
		if(this.isWarning) this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getBackground(), GraphicsController.WARNING_COLOR, 10));
	}
	
	public void setProcessing(boolean processing) {
		this.isProcessing = processing;
	}
	
	public boolean isLocked() {
		return this.locked;
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public void lock() {
		this.locked = true;
	}
	
	public void unlock() {
		this.locked = false;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void active() {
		this.active = true;
	}
	
	public void deactive() {
		this.active = false;
	}
	
	@Override
	public void update() {
		super.update();
		this.checkLocked();
	}
	
	protected void checkLocked() {
		if(this.lockEvent() != this.locked) {
			this.setLocked(this.lockEvent()); 
			this.refresh();
		}
	}

	protected boolean lockEvent() {
		return false;
	}

	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintShade(graphics);
		this.paintBackground(graphics);
		
		if(this.isProcessing) {
			graphics.setColor(new Color(0, 0, 0, 100));
			graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
			graphics.drawArc(this.getHeight()/3, this.getHeight()/3, this.getHeight()/3, this.getHeight()/3, this.getDoubleInt("Process"), this.getDoubleInt("Process") + 270);
			graphics.drawArc(this.getHeight()/3, this.getHeight()/3, this.getHeight()/3, this.getHeight()/3, 180 - this.getDoubleInt("Process"), 270 - this.getDoubleInt("Process"));
		}
	}
	
	protected void paintShade(Graphics2D graphics) {
		int i = 0;
		if(this.shade != 0) {
			graphics.setColor(new Color(0, 0, 0, GraphicsController.SHADE_ALPHA / this.shade));
			for(i = 0;i < this.shade;i++) {
				graphics.fillRoundRect(i, i, this.getWidth() - i*2, this.getHeight() - i*2, (this.radius + this.shade - i) * 2, (this.radius + this.shade - i) * 2);
			}
		}
	}
	
	protected void paintBackground(Graphics2D graphics) {
		graphics.setColor(this.locked ? (this.active ? this.touchColor : this.defaultColor) : (this.active ? this.getBackground() : this.defaultColor));
		graphics.fillRoundRect(this.shade, this.shade, this.getWidth() - this.shade * 2, this.getHeight() - this.shade * 2, this.radius * 2, this.radius * 2);
	}

}
