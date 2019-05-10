package pers.lagomoro.stusystem.client.view;

import java.awt.Rectangle;

import javax.swing.SwingConstants;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.runtime.Client_Runtime;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.widget.TextView;

@SuppressWarnings("serial")
public class Scene_About extends Scene{
	
	TextView aboutTextView;
	
	public Scene_About(){
		super(SceneController.SCENE_ABOUT, null, "UI->Window->Main::About");
	}
	
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->About::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText(LanguageModel.get("UI->Scene->About::Version") + Client_Runtime.VERSION, new Rectangle(20, placeY, 500, 20), 12);

		placeY += 35;
		this.drawText(LanguageModel.get("UI->Scene->About::Author") + Client_Runtime.AUTHOR, new Rectangle(20, placeY, 500, 20), 16);
		placeY += 25;
		this.drawText(LanguageModel.get("UI->Scene->About::Organization") + Client_Runtime.ORGANIZATION, new Rectangle(20, placeY, 500, 20), 16);
		
		this.aboutTextView = this.drawText(Client_Runtime.LISENCE, new Rectangle(20, placeY, 530, 20), 12);
		this.aboutTextView.setForeground(GraphicsController.DARK_TOUCH_COLOR);
		this.aboutTextView.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.aboutTextView.setBounds(20, this.getHeight() - 40, this.getWidth() - 20, this.aboutTextView.getHeight());
		super.setBounds(x, y, width, height);
	}

}
