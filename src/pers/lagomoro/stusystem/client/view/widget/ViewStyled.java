package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;

import pers.lagomoro.stusystem.client.controller.GraphicsController;

@SuppressWarnings("serial")
public class ViewStyled extends View{
	
	protected int radius = 0;
	protected int shade = 0;
	
	public ViewStyled(){
		super();
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setBackground(GraphicsController.DEFAULT_BACKGROUND_COLOR);
	}
	
	/**
	 * Set radius.
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
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintShade(graphics);
		this.paintBackground(graphics);
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
		graphics.setColor(this.getBackground());
		graphics.fillRoundRect(this.shade, this.shade, this.getWidth() - this.shade * 2, this.getHeight() - this.shade * 2, this.radius * 2, this.radius * 2);
	}
	
}
