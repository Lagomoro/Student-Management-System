package pers.lagomoro.stusystem.client.view;

import java.awt.Rectangle;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.view.component.Scene;

@SuppressWarnings("serial")
public class Scene_Vote extends Scene{
	
	public Scene_Vote(){
		super(SceneController.SCENE_VOTE, null, "UI->Window->Main::Vote");
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Vote::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Vote::Description", new Rectangle(20, placeY, 500, 20), 12);

	}

}
