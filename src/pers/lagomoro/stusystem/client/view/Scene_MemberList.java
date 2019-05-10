package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.AutoFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.data.DataChat;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.data.DataUser;
import pers.lagomoro.stusystem.data.JsonDataController;
import pers.lagomoro.stusystem.client.view.widget.ItemMember;

@SuppressWarnings("serial")
public class Scene_MemberList extends Scene{
	
	protected ScrollPane scrollPane;
	protected View memberView;
	
	public Scene_MemberList(){
		super(SceneController.SCENE_MEMBER_LIST, null, "UI->Scene->MemberList::Title");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("member_SceneCallback", json -> this.memberCallBack(json));
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("member_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->MemberList::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->MemberList::Description", new Rectangle(20, placeY, 500, 20), 12);

		placeY += 35;
		this.memberView = new View();	
		this.memberView.setLayout(new AutoFlowLayout(AutoFlowLayout.TOP, 20, 10));
		this.scrollPane = new ScrollPane(this.memberView, false, true);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.scrollPane.setBounds(this.scrollPane.getX(), this.scrollPane.getY(), width, height - 90);
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void update() {
		super.update();
		this.memberView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.memberView.refresh();
	}
	
	protected void memberCallBack(String jsonList) {
		this.memberView.removeAll();
		LinkedList<DataUser> users = DataUser.fromJsonList(jsonList);
		for(DataUser userInfo : users) {
			ItemMember member = new ItemMember(userInfo) {
				@Override
				protected void onMouseClicked(MouseEvent e) {
					super.onMouseClicked(e);
					Scene_ChatRoom chatRoom = new Scene_ChatRoom(userInfo.nickname, userInfo.username) {
						@Override
						public void onSendClicked(MouseEvent e) {
							String text = Base64.encodeBase64String(JsonDataController.serializeObject(this.editorPane.getStyledDocument()));
							String json = DataChat.parse(ClientController.getMainConnectorKey(), userInfo.username, text);
							CommandModel.commandActive("sendUserChat", json);
							super.onSendClicked(e);
						}
						@Override
						public void showWithDrawMenu(int x, int y, DataChat chat) {
							if(ClientController.isCurrentUser(chat.username)) super.showWithDrawMenu(x, y, chat);
						}
						@Override
						public void onWithDraw(DataChat chat) {
							String json = DataChat.parse(ClientController.getMainConnectorKey(), chat.username, chat.id, userInfo.username);
							CommandModel.commandActive("withdrawUserChat", json);
							super.onWithDraw(chat);
						}
					};
					CommandModel.addCommand("userChat_" + userInfo.username + "_SceneCallback", jsonList -> chatRoom.updateCallback(jsonList));
					WindowController.focusScene(chatRoom, getSceneController());
					CommandModel.commandActive("userChat_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey(), userInfo.username));
				}
			};
			member.setRadius(5);
			this.memberView.add(member);
		}
		this.refresh();
	}

}
