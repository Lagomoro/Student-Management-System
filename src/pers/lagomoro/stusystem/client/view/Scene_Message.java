package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import java.util.Date;

import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.data.InfoUserChat;
import pers.lagomoro.stusystem.client.view.widget.ItemUserChat;

@Deprecated
@SuppressWarnings({ "serial" })
public class Scene_Message extends Scene{
	
	protected ScrollPane scrollPane;
	protected View messageView;
	
	public Scene_Message(){
		super(SceneController.SCENE_MESSAGE, null, "UI->Window->Main::Message");
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Message::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Message::Description", new Rectangle(20, placeY, 500, 20), 12);

		placeY += 35;
		this.messageView = new View();	
		this.messageView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true, 20, 10));
		this.scrollPane = new ScrollPane(this.messageView);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
		for(int i = 0;i < 50;i++) {
			ItemUserChat aBubble = new ItemUserChat();
			aBubble.setUserChat(new InfoUserChat("", "2班-教头", "你今天吃饭了吗", (int)(Math.random()*99), new Date()));
			aBubble.setRadius(5);
			this.messageView.add(aBubble);	
		}
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.scrollPane.setBounds(this.scrollPane.getX(), this.scrollPane.getY(), width, height - 90);
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void update() {
		super.update();
		this.messageView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.messageView.refresh();
	}

}
