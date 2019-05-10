package pers.lagomoro.stusystem.client.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.codec.digest.DigestUtils;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.EditTextStyled;
import pers.lagomoro.stusystem.data.DataLogin;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.data.DataUserInfo;

@SuppressWarnings("serial")
public class Scene_UserInformationChange extends Scene{

	EditTextStyled usernameText;
	EditTextStyled nicknameText;
	EditTextStyled classText;
	EditTextStyled oldPasswordText;
	EditTextStyled newPasswordText;
	EditTextStyled confirmNewPasswordText;
	
	ButtonStyled nicknameButton;
	ButtonStyled passwordButton;
	ButtonStyled imageButton;
	
	JLabel imageLabel;
	JLabel imageLabelSmall;
	JLabel roundLabel;
	
	DataUserInfo data;
	
	public Scene_UserInformationChange(){
		super(SceneController.SCENE_USER_INFORMATION_CHANGE, null, "UI->Scene->UserInformationChange::Title");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("userInfo_SceneCallback", json -> this.userInfoCallBack(json));
	}

	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("userInfo_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->UserInformationChange::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->UserInformationChange::Description", new Rectangle(20, placeY, 500, 20), 12);
		
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::Username", new Rectangle(20, placeY, 180, 20), 16);
		this.usernameText = new EditTextStyled();
		this.usernameText.setForeground(GraphicsController.DARK_TOUCH_COLOR);
		this.usernameText.setEditable(false);
		this.usernameText.setBounds(200, placeY - 5, 300, 30);
		this.usernameText.setRadius(5);
		this.add(this.usernameText);
			
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::Nickname", new Rectangle(20, placeY, 180, 20), 16);
		this.nicknameText = new EditTextStyled();
		this.nicknameText.setBounds(200, placeY - 5, 300, 30);
		this.nicknameText.setShade(1);
		this.nicknameText.setRadius(5);
		this.add(this.nicknameText);
		
		this.nicknameButton = new ButtonStyled("UI->Scene->UserInformationChange::ChangeNickname", GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				CommandModel.commandActive("changeNickname", DataUpdate.parse(ClientController.getMainConnectorKey(), nicknameText.getText()));
			}
		};
		this.nicknameButton.setBounds(520, placeY - 5, 120, 30);
		this.nicknameButton.setFont(LanguageModel.getDefaultFont(16));
		this.add(nicknameButton);
		
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::Classname", new Rectangle(20, placeY, 180, 20), 16);
		this.classText = new EditTextStyled();
		this.classText.setForeground(GraphicsController.DARK_TOUCH_COLOR);
		this.classText.setEditable(false);
		this.classText.setBounds(200, placeY - 5, 300, 30);
		this.classText.setRadius(5);
		this.add(this.classText);
		
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::OldPassWord", new Rectangle(20, placeY, 180, 20), 16);
		this.oldPasswordText = new EditTextStyled();
		this.oldPasswordText.setBounds(200, placeY - 5, 300, 30);
		this.oldPasswordText.setShade(1);
		this.oldPasswordText.setRadius(5);
		this.add(this.oldPasswordText);
		
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::NewPassWord", new Rectangle(20, placeY, 180, 20), 16);
		this.newPasswordText = new EditTextStyled();
		this.newPasswordText.setBounds(200, placeY - 5, 300, 30);
		this.newPasswordText.setShade(1);
		this.newPasswordText.setRadius(5);
		this.add(this.newPasswordText);

		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::ConfirmNewPassWord", new Rectangle(20, placeY, 180, 20), 16);
		this.confirmNewPasswordText = new EditTextStyled();
		this.confirmNewPasswordText.setBounds(200, placeY - 5, 300, 30);
		this.confirmNewPasswordText.setShade(1);
		this.confirmNewPasswordText.setRadius(5);
		this.add(this.confirmNewPasswordText);
		
		this.passwordButton = new ButtonStyled("UI->Scene->UserInformationChange::ChangePassWord", GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				String oldPassword = DigestUtils.md5Hex(oldPasswordText.getText());
				String newPassword = DigestUtils.md5Hex(newPasswordText.getText());
				String confirmNewPassword = DigestUtils.md5Hex(confirmNewPasswordText.getText());
				if(newPassword.equals(confirmNewPassword)) {
					CommandModel.commandActive("changePassword", DataLogin.parse(ClientController.getMainConnectorKey(), oldPassword, newPassword));					
					oldPasswordText.setText(null);
					newPasswordText.setText(null);
					confirmNewPasswordText.setText(null);
				}else {
					newPasswordText.setText(LanguageModel.get("UI->Scene->UserInformationChange::PasswordNotEqual"));
					confirmNewPasswordText.setText(LanguageModel.get("UI->Scene->UserInformationChange::PasswordNotEqual"));
				}
			}
		};
		this.passwordButton.setBounds(520, placeY - 5, 120, 30);
		this.passwordButton.setFont(LanguageModel.getDefaultFont(16));
		this.add(passwordButton);
		
		placeY += 45;
		this.drawText("UI->Scene->UserInformationChange::ProfileImage", new Rectangle(20, placeY, 180, 20), 16);
		
		this.imageLabel = new JLabel();
		this.imageLabel.setBackground(GraphicsController.HOVER_COLOR);
		this.imageLabel.setBounds(200, placeY - 5, 128, 128);
		this.add(imageLabel);
		
		this.imageLabelSmall = new JLabel();
		this.imageLabelSmall.setBackground(GraphicsController.HOVER_COLOR);
		this.imageLabelSmall.setBounds(340, placeY - 5, 64, 64);
		this.add(imageLabelSmall);
		
		this.roundLabel = new JLabel();
		this.roundLabel.setBackground(GraphicsController.HOVER_COLOR);
		this.roundLabel.setBounds(415, placeY - 5, 64, 64);
		this.add(roundLabel);
		
		this.imageButton = new ButtonStyled("UI->Scene->UserInformationChange::ChangeImage", GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setDialogTitle(LanguageModel.get("UI->Scene->NoticeEditor::ChoosePicture"));
    			FileFilter filter = new FileNameExtensionFilter(LanguageModel.get("UI->Scene->NoticeEditor::PictureFliter"), "jpg", "png");
    			fileChooser.addChoosableFileFilter(filter);
    			fileChooser.setAcceptAllFileFilterUsed(false);
    			fileChooser.setApproveButtonText(LanguageModel.get("UI->Scene->NoticeEditor::Approve"));
				if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					ImageIcon image = new ImageIcon(fileChooser.getSelectedFile().getPath());
					String imageData = ImageModel.imageToBase64(new ImageIcon(image.getImage().getScaledInstance(128, 128, 0)).getImage());
					CommandModel.commandActive("changeImageData", DataUpdate.parse(ClientController.getMainConnectorKey(), imageData));					
				}
			}
		};
		this.imageButton.setBounds(520, placeY - 5, 120, 30);
		this.imageButton.setFont(LanguageModel.getDefaultFont(16));
		this.add(imageButton);
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.setColor(GraphicsController.HOVER_COLOR);
		graphics.fillRect(200, 365, 128, 128);
		graphics.fillRect(340, 365, 64, 64);
		graphics.fillOval(415, 365, 64, 64);
	}
	
	protected void userInfoCallBack(String json) {
		DataUserInfo data = DataUserInfo.fromJson(json);
		this.data = data;
		this.usernameText.setTextKey(data.username);
		this.nicknameText.setTextKey(data.nickname);
		this.classText.setTextKey(data.classname + (data.authority ? LanguageModel.get("UI->Scene->UserInformationChange::Admin") : ""));
		BufferedImage image = ImageModel.getBase64Image(data.image_data);
		if(image != null) {
			this.imageLabel.setIcon(new ImageIcon(image));
			this.imageLabelSmall.setIcon(new ImageIcon(image.getScaledInstance(64, 64, 0)));
			this.roundLabel.setIcon(new ImageIcon(ImageModel.getRoundImage(image).getScaledInstance(64, 64, 0)));
		}
	}
	
	protected void refreshButton() {
		Rectangle bound = this.nicknameButton.getBounds();
		bound.width = Math.max(this.nicknameButton.getPreferredSize().width + 10, 120);
		this.nicknameButton.setBounds(bound);
		bound = this.passwordButton.getBounds();
		bound.width = Math.max(this.passwordButton.getPreferredSize().width + 10, 120);
		this.passwordButton.setBounds(bound);
		bound = this.imageButton.getBounds();
		bound.width = Math.max(this.imageButton.getPreferredSize().width + 10, 120);
		this.imageButton.setBounds(bound);
	}

}
