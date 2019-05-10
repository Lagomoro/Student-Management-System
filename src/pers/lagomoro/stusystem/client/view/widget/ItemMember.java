package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics2D;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.data.DataUser;

@SuppressWarnings("serial")
public class ItemMember extends ButtonStyled{
	
	protected DataUser user;
	
	public ItemMember(DataUser user) {
		super(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR);
		this.setUser(user);
	}
	
	@Override
	protected void create() {
		super.create();
		this.setBounds(0, 0, 350, 60);
	}

	public void setUser(DataUser user) {
		this.user = user;
	}

	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintImage(graphics);
		this.paintNickname(graphics, 4);
		this.paintUsername(graphics, 38);
	}
	
	protected void paintImage(Graphics2D graphics) {
		graphics.setColor(GraphicsController.DARK_TOUCH_COLOR);
		graphics.fillOval(10, 5, this.getHeight() - 10, this.getHeight() - 10);
		graphics.drawImage(ImageModel.getUserImage(this.user.username), 10, 5, this.getHeight() - 10, this.getHeight() - 10, null);
	}
	
	protected void paintNickname(Graphics2D graphics, int placeY) {
		graphics.setColor(this.user.authority ? GraphicsController.HINT_BLUE_COLOR : GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString((this.user.authority ? LanguageModel.get("UI->Scene->MemberList::Admin") : "") + this.user.nickname, this.getHeight() + 10, placeY + 20);
	}
	
	protected void paintUsername(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.drawString(this.user.username, this.getHeight() + 10, placeY + 10);
	}

}
