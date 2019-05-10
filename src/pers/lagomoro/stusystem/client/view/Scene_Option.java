package pers.lagomoro.stusystem.client.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.client.model.module.Language;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.module.ButtonLockGroup;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

@SuppressWarnings("serial")
public class Scene_Option extends Scene{
	
	protected ButtonLockGroup languagePackGroup;
	
	protected ButtonStyled outputButton;
	protected ButtonStyled userInfoButton;

	public Scene_Option(){
		super(SceneController.SCENE_OPTION, null, "UI->Window->Main::Option");
	}
	
	@Override
	protected void create(){
		super.create();
		this.createButtonGroup();
		this.createContent();
	}
	
	protected void createButtonGroup() {
		this.languagePackGroup = new ButtonLockGroup();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Option::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Option::Description", new Rectangle(20, placeY, 500, 20), 12);

		placeY += 35;
		this.userInfoButton = new ButtonStyled("UI->Scene->Option::UserInfoChange", GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(new Scene_UserInformationChange(), getSceneController());
			}
		};
		this.userInfoButton.setBounds(30, placeY, 120, 30);
		this.userInfoButton.setFont(LanguageModel.getDefaultFont(16));
		this.add(userInfoButton);

		placeY += 45;
		this.drawText("UI->Scene->Option::LanguageOption", new Rectangle(20, placeY, 300, 20), 16);
		
		this.outputButton = new ButtonStyled("UI->Scene->Option::LanguageOutput", GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setDialogTitle(LanguageModel.get("UI->Scene->Option::LanguageOutput"));
				fileChooser.setApproveButtonText(LanguageModel.get("UI->Scene->Option::Approve"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					LanguageModel.outputDefaultLanguage(fileChooser.getSelectedFile().getPath());
				}
			}
		};
		this.outputButton.setBounds(550 - 120, placeY - 5, 120, 30);
		this.outputButton.setFont(LanguageModel.getDefaultFont(16));
		this.add(this.outputButton);
		
		placeY += 30;
		String[] languageSet = LanguageModel.getLanguagePackList().toArray(new String[0]);
		for(int i = 0; i <= languageSet.length; i++) {
			Language language = LanguageModel.getLanguage(i > 0 ? languageSet[i - 1] : "default");
			ButtonStyled button = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.HOVER_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
				@Override
				protected boolean lockEvent() {
					return languagePackGroup.isSelected(this);
				}
				@Override
				protected void onMouseClicked(MouseEvent e) {
					super.onMouseClicked(e);
					languagePackGroup.setSelected(this, true);
					LanguageModel.setLanguage(language.getPackname());
					StorageModel.save();
					WindowController.refresh();
				}
				@Override
				protected void paint(Graphics2D graphics) {
					super.paint(graphics);
					graphics.setColor(this.textColor);
					graphics.drawString(language.getPackname(), 10, 25);
					graphics.setFont(LanguageModel.getDefaultFont(14));
					graphics.drawString(language.getVersion(), 205, 24);
					graphics.drawString(language.getAuthor(), 10, 49);
					graphics.drawString(language.getDescription(), 10, 69);
					graphics.drawString(language.getSubmit(), 10, 89);
				}
			};
			button.setBounds(30 + i % 2 * 270, placeY, 250, 100);
			button.setLocked(LanguageModel.getLanguage().getPackname().equals(language.getPackname()));
			if(i % 2 == 1) placeY += 110;
			this.languagePackGroup.add(button);
			this.add(button);
		}
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.refreshButton();
	}

	protected void refreshButton() {
		Rectangle bound = this.outputButton.getBounds();
		bound.width = Math.max(this.outputButton.getPreferredSize().width + 10, 120);
		bound.x = 550 - bound.width;
		this.outputButton.setBounds(bound);
		bound = this.userInfoButton.getBounds();
		bound.width = Math.max(this.userInfoButton.getPreferredSize().width + 10, 120);
		this.userInfoButton.setBounds(bound);
	}

}
