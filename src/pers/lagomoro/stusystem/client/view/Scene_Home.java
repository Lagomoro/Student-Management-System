package pers.lagomoro.stusystem.client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.colorchooser.ColorChooserComponentFactory;

import org.omg.CORBA.Bounds;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

@SuppressWarnings({"serial", "unused"})
public class Scene_Home extends Scene{
	
	public Scene_Home(){
		super(SceneController.SCENE_HOME, null, "UI->Window->Main::Home");
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Home::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Home::Description", new Rectangle(20, placeY, 500, 20), 12);
		
		/*
		placeY += 35;
		ButtonStyled messageButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Message"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		messageButton.setBounds(30, placeY, 250, 100);
		this.add(messageButton);
		
		ButtonStyled noticeButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Notice"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		noticeButton.setBounds(30 + 270, placeY, 250, 100);
		this.add(noticeButton);
		
		placeY += 110;
		ButtonStyled voteButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Message"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		voteButton.setBounds(30, placeY, 250, 100);
		this.add(voteButton);
		
		ButtonStyled persualButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Notice"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		persualButton.setBounds(30 + 270, placeY, 250, 100);
		this.add(persualButton);

		placeY += 110;
		ButtonStyled shareButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Message"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		shareButton.setBounds(30, placeY, 250, 100);
		this.add(shareButton);
		
		ButtonStyled gameButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, GraphicsController.DARK_TOUCH_COLOR, this.getBounds(), LanguageModel.get("UI->Scene->Home::Hint->Notice"), LanguageModel.get("UI->Scene->Home::Hint->Done"));
			}
		};
		gameButton.setBounds(30 + 270, placeY, 250, 100);
		this.add(gameButton);
		*/
	}

	protected void drawContent(Graphics2D graphics, Color color, Rectangle bounds, String text1, String text2) {
		graphics.setColor(Color.WHITE);
		graphics.fillRect(6, 6, bounds.width - 12, bounds.height - 12);
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		graphics.drawRect(6, 6, bounds.width - 12, bounds.height - 12);
		
		graphics.setFont(LanguageModel.getDefaultFont(22));
		graphics.drawString(text1, 15, 35);
		
		int targetX = bounds.width - 35;
		int targetY = bounds.height - 25;
		graphics.setFont(LanguageModel.getDefaultFont(16));
		graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		graphics.drawString(text2, targetX - 80, targetY + 6);
		graphics.drawLine(targetX + 2 , targetY - 8, targetX + 10, targetY);
		graphics.drawLine(targetX + 2 , targetY + 8, targetX + 10, targetY);
		graphics.drawLine(targetX + 12, targetY - 8, targetX + 20, targetY);
		graphics.drawLine(targetX + 12, targetY + 8, targetX + 20, targetY);
	}
}
