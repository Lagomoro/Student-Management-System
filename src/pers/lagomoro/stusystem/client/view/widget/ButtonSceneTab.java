package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.ImageModel;

@SuppressWarnings("serial")
public class ButtonSceneTab extends ButtonStyled{
	
	protected String sceneKey;
	protected String username;

	public ButtonSceneTab(String key, String username, String textKey, Color hoverColor, Color defaultColor, Color touchColor){
		super(textKey, hoverColor, defaultColor, touchColor);
		this.setSceneKey(key);
		this.setUsername(username);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setHorizontalAlignment(SwingConstants.LEFT);
		this.setBorder(BorderFactory.createEmptyBorder(0, 55, 0, 0));
		this.setBounds(0, 0, 190, 50);
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintImage(graphics);
		this.paintClose(graphics);
	}

	protected void paintImage(Graphics2D graphics) {
		if(this.username != null) {
			graphics.setColor(GraphicsController.DARK_TOUCH_COLOR);
			graphics.fillOval(10, (this.getBounds().height - 40)/2, 40, 40);
			graphics.drawImage(ImageModel.getUserImage(this.username), 10, (this.getBounds().height - 40)/2, 40, 40, null);
		}
	}
	
	protected void paintClose(Graphics2D graphics) {
		if(this.status > 0) {
			graphics.setColor(Color.WHITE);
			graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
			int targetX = this.getBounds().width - 15;
			int targetY = this.getBounds().height/2;
			graphics.drawLine(targetX - 7, targetY - 7, targetX + 7, targetY + 7);
			graphics.drawLine(targetX - 7, targetY + 7, targetX + 7, targetY - 7);
		}
	}
	
	public String getSceneKey() {
		return sceneKey;
	}

	protected void setSceneKey(String sceneKey) {
		this.sceneKey = sceneKey;
	}

	protected void setUsername(String username) {
		this.username = username;
		if(username != null) this.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 0));
	}
	
}
