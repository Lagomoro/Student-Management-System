package pers.lagomoro.stusystem.client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

@SuppressWarnings("serial")
public class Scene_Game extends Scene{
	
	public Scene_Game(){
		super(SceneController.SCENE_GAME, null, "UI->Window->Main::Game");
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Game::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Game::Description", new Rectangle(20, placeY, 500, 20), 12);

		placeY += 35;
		ButtonStyled paintAndGuessButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
//				WindowController.focusScene(new Scene_PaintAndGuess(), getSceneController());
				WindowController.focusScene(new Scene_PaintAndGuessGameRoom(), getSceneController());
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				drawContent(graphics, Color.PINK, this.getBounds(), LanguageModel.get("UI->Scene->Game::Paint&Guess"), LanguageModel.get("UI->Scene->Game::StartGame"));
			}
		};
		paintAndGuessButton.setBounds(30, placeY, 250, 100);
		this.add(paintAndGuessButton);
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
