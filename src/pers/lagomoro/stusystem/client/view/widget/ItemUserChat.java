package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;

import java.awt.geom.AffineTransform;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.data.InfoUserChat;

@Deprecated
@SuppressWarnings("serial")
public class ItemUserChat extends ButtonStyled{
	
	protected InfoUserChat userChat;
	
	public ItemUserChat() {
		super(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR);
		this.setUserChat(new InfoUserChat());
	}
	
	public ItemUserChat(InfoUserChat user) {
		super(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR);
		this.setUserChat(user);
	}
	
	@Override
	protected void create() {
		super.create();
		this.setBounds(0, 0, 250, 60);
	}

	public void setUserChat(InfoUserChat userChat) {
		this.userChat = userChat;
	}

	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintImage(graphics);
		this.paintNickname(graphics, 4);
		this.paintText(graphics, 38);
		this.paintDate(graphics, 8);
		this.paintCount(graphics, 34);
	}
	
	protected void paintImage(Graphics2D graphics) {
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.fillRoundRect(10, 5, this.getHeight() - 10, this.getHeight() - 10, this.getHeight() - 10, this.getHeight() - 10);
	}
	
	protected void paintNickname(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString(this.userChat.nickname, this.getHeight() + 10, placeY + 20);
	}
	
	protected void paintText(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.drawString(this.userChat.text, this.getHeight() + 10, placeY + 10);
	}

	protected void paintDate(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.drawString(this.userChat.timestamp.toString(), this.getWidth() - 10 -(int)(graphics.getFont().getStringBounds(this.userChat.timestamp.toString(), new FontRenderContext(new AffineTransform(), true, true)).getWidth()), placeY + 10);
	}
	
	protected void paintCount(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.RED);
		graphics.fillOval(this.getWidth() - 30, placeY, 20, 20);
		graphics.setColor(Color.WHITE);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.drawString(Integer.toString(this.userChat.count), this.getWidth() - 30 + (this.userChat.count < 10 ? 7 : 3), placeY + 15);
	}
	
}
