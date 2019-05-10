package pers.lagomoro.stusystem.client.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import org.apache.commons.codec.digest.DigestUtils;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.WindowTitled;
import pers.lagomoro.stusystem.client.view.widget.EditTextPassword;
import pers.lagomoro.stusystem.client.view.widget.EditTextUserName;
import pers.lagomoro.stusystem.data.DataLogin;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

@SuppressWarnings("serial")
public class Window_Login extends WindowTitled{

	EditTextPassword passwardEditText;
	EditTextUserName userNameEditText;
	ButtonStyled loginButton;
	
	public Window_Login(){
		super(WindowController.WINDOW_LOGIN, "UI->Window->Login::Title");
	}
	
	@Override
    protected void initialize(){
		super.initialize();
		this.setSize(340, 240);
		CommandModel.addCommand("loginCallback", json -> this.loginCallBack(json));
	}
    
	@Override
	protected void create() {
		super.create();
		this.createContents();
	}

	protected void createContents() {
		this.passwardEditText = new EditTextPassword("UI->Window->Login::Password", Color.LIGHT_GRAY, Color.BLACK);
		this.passwardEditText.setBounds(20, 110, 300, 50);
		this.passwardEditText.setShade(5);
		this.passwardEditText.setStackMode(true);
		this.passwardEditText.setRadius(5);
		this.add(this.passwardEditText);

		this.userNameEditText = new EditTextUserName("UI->Window->Login::UserName", Color.LIGHT_GRAY, Color.BLACK);
		this.userNameEditText.setBounds(20, 60, 300, 50);
		this.userNameEditText.setShade(5);
		this.userNameEditText.setRadius(5);
		this.add(this.userNameEditText);

		this.loginButton = new ButtonStyled("UI->Window->Login::Login/Register", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(!this.isProcessing) {
					if(userNameEditText.isEmpty()) {
						this.setProcessing(false);
						userNameEditText.setWarning(LanguageModel.get("UI->Window->Login::Warning->UserNameEmpty"));
					}else if(passwardEditText.isEmpty()) {
						this.setProcessing(false);
						passwardEditText.setWarning(LanguageModel.get("UI->Window->Login::Warning->PasswordEmpty"));
					}else {
						CommandModel.commandActive("login", DataLogin.parse(ClientController.getMainConnectorKey(), userNameEditText.getText(), DigestUtils.md5Hex(passwardEditText.getRealText())));
						this.setProcessing(true);
						this.setTextKey("UI->Window->Login::Hint->LoginHint");
						userNameEditText.deactive();
						passwardEditText.deactive();
					}
				}
			}
		};
		this.loginButton.setBounds(20, 170, 300, 50);
		this.loginButton.setShade(5);
		this.loginButton.setRadius(5);
		this.add(this.loginButton);
	}
	
	protected void loginCallBack(String json) {
		DataLogin login = DataLogin.fromJson(json);
		switch(login.callback) {
		case 0:{
			ClientController.login(login.username, login.authority);
			WindowController.deactiveWindow(getKey());
			WindowController.deactiveWindow("Flash_Loading");
			WindowController.focusWindow(new Window_Main() {
				@Override
				protected void onClosed(WindowEvent e) {
					super.onClosed(e);
					CommandModel.commandActive("logout", DataUpdate.parse(ClientController.getMainConnectorKey()));
//					MusicController.stop();
					ClientController.logout();
					WindowController.applyCloseChild();
					WindowController.focusWindow("Window_Login");
					WindowController.focusWindow("Flash_Loading");
				}
			});
			this.loginButton.setProcessing(false);
			this.loginButton.setTextKey("UI->Window->Login::Login/Register");
			this.userNameEditText.active();
			this.passwardEditText.active();
			break;
		}
		case 1:default:{
			this.loginButton.setProcessing(false);
			this.loginButton.setTextKey("UI->Window->Login::Login/Register");
			this.userNameEditText.active();
			this.passwardEditText.active();
			this.userNameEditText.setWarning(LanguageModel.get("UI->Window->Login::Warning->UserNameUndefined"));
			break;
		}
		case 2:{
			this.loginButton.setProcessing(false);
			this.loginButton.setTextKey("UI->Window->Login::Login/Register");
			this.userNameEditText.active();
			this.passwardEditText.active();
			this.passwardEditText.setWarning(LanguageModel.get("UI->Window->Login::Warning->PasswordInvaild"));
			break;
		}
		}
	}
	
	@Override
	protected void onClosed(WindowEvent e) {
		super.onClosed(e);
		WindowController.applyExit();
	}
	
}
