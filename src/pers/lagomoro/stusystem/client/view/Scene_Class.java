package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.view.widget.ItemNotice;
import pers.lagomoro.stusystem.client.view.widget.TextView;
import pers.lagomoro.stusystem.data.DataChat;
import pers.lagomoro.stusystem.data.DataClass;
import pers.lagomoro.stusystem.data.DataNotice;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.data.JsonDataController;

@SuppressWarnings("serial")
public class Scene_Class extends Scene{
	
	protected TextView classText;
	
	protected ScrollPane scrollPane;
	protected View noticeView;

	protected View buttonView;
	
	protected String classname;

	public Scene_Class(){
		super(SceneController.SCENE_CLASS, null, "UI->Window->Main::Class");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("class_SceneCallback", json -> this.classCallBack(json));
		CommandModel.addCommand("notice_SceneCallback", jsonList -> this.noticeCallBack(jsonList));
	}

	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("class_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
		CommandModel.commandActive("notice_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Class::Title", new Rectangle(20, placeY, 300, 40), 24);
		placeY += 40;
		this.classText = this.drawText("UI->Scene->Class::NowClass", new Rectangle(20, placeY, 300, 20), 12);

		this.buttonView = new View();
		this.buttonView.setBounds(0, placeY - 55, 290, 77);
		this.add(this.buttonView);
		
		int placeX = 180;
		TextView releaseText = new TextView("UI->Scene->Class::ReleaseNotice");
		releaseText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		ButtonStyled releaseButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(new Scene_NoticeEditor("UI->Scene->Class::ToNotice") {
					@Override
					protected void onSendClicked(MouseEvent e) {
						String text = Base64.encodeBase64String(JsonDataController.serializeObject(this.editorPane.getStyledDocument()));
						String json = DataNotice.parse(ClientController.getMainConnectorKey(), this.titlePane.getText(), text);
						CommandModel.commandActive("sendNotice", json);
						super.onSendClicked(e);
					}
				}, getSceneController());
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 3, this.getHeight()/3);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3, 3);
		    }
		};
		releaseButton.setBounds(placeX + 29, placeY - 40, 35, 35);
		releaseButton.setShade(5);
		releaseButton.setRadius(20);
		
		if(ClientController.isAdmin()) {
			this.buttonView.add(releaseText);
			this.buttonView.add(releaseButton);
			placeX -= 90;
		}
		
		TextView memberText = new TextView("UI->Scene->Class::MemberList");
		memberText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		this.buttonView.add(memberText);
		ButtonStyled memberButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_MEMBER_LIST, getSceneController());
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()*2/7 + 1, this.getHeight()*2/5 - 1, this.getWidth()*3/7 + 1, 2);
				graphics.fillRect(this.getWidth()*2/7 + 1, this.getHeight()/2      , this.getWidth()*3/7 + 1, 2);
				graphics.fillRect(this.getWidth()*2/7 + 1, this.getHeight()*3/5    , this.getWidth()*3/7 + 1, 2);
		    }
		};
		memberButton.setBounds(placeX + 29, placeY - 40, 35, 35);
		memberButton.setShade(5);
		memberButton.setRadius(20);
		this.buttonView.add(memberButton);

		placeX -= 90;
		TextView chatText = new TextView("UI->Scene->Class::ClassChat");
		chatText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		this.buttonView.add(chatText);
		ButtonStyled chatButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				Scene_ChatRoom chatRoom = new Scene_ChatRoom(classname, null) {
					@Override
					public void onSendClicked(MouseEvent e) {
						String text = Base64.encodeBase64String(JsonDataController.serializeObject(this.editorPane.getStyledDocument()));
						String json = DataChat.parse(ClientController.getMainConnectorKey(), text);
						CommandModel.commandActive("sendClassChat", json);
						super.onSendClicked(e);
					}
					@Override
					public void showWithDrawMenu(int x, int y, DataChat chat) {
						if(ClientController.isCurrentUser(chat.username) || ClientController.isAdmin()) super.showWithDrawMenu(x, y, chat);
					}
					@Override
					public void onWithDraw(DataChat chat) {
						String json = DataChat.parse(ClientController.getMainConnectorKey(), chat.username, chat.id, classname);
						CommandModel.commandActive("withdrawClassChat", json);
						super.onWithDraw(chat);
					}
				};
				CommandModel.addCommand("classChat_SceneCallback", jsonList -> chatRoom.updateCallback(jsonList));
				WindowController.focusScene(chatRoom, getSceneController());
				CommandModel.commandActive("classChat_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillOval(this.getWidth()/3 - 3  , this.getHeight()/2 - 2, 5, 5);
				graphics.fillOval(this.getWidth()/2 - 2  , this.getHeight()/2 - 2, 5, 5);
				graphics.fillOval(this.getWidth()*2/3 - 2, this.getHeight()/2 - 2, 5, 5);
		    }
		};
		chatButton.setBounds(placeX + 29, placeY - 40, 35, 35);
		chatButton.setShade(5);
		chatButton.setRadius(20);
		this.buttonView.add(chatButton);
		
		placeY += 35;
		this.noticeView = new View();	
		this.scrollPane = new ScrollPane(this.noticeView);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
		this.noticeView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.scrollPane.setBounds(this.scrollPane.getX(), this.scrollPane.getY(), width, height - 90);
		this.buttonView.setLocation(this.getWidth() - this.buttonView.getWidth(), this.buttonView.getY());
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void update() {
		super.update();
		this.noticeView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.noticeView.refresh();
	}
	
	protected void classCallBack(String json) {
		DataClass classInfo = DataClass.fromJson(json);
		this.classname = classInfo.classname;
		this.classText.setTextKey(classInfo.classname == null ? "UI->Scene->Class::NowClass" : classInfo.classname);
	}
	
	protected void noticeCallBack(String jsonList) {
		this.noticeView.removeAll();
		LinkedList<DataNotice> notices = DataNotice.fromJsonList(jsonList);
		for(DataNotice notice : notices) {
			this.noticeView.add(new ItemNotice(notice));
		}
		this.refresh();
	}
	
}
