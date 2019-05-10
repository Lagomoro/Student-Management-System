package pers.lagomoro.stusystem.client.view.component;

import java.awt.Rectangle;

import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.widget.TextView;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.view.widget.ViewSceneCard;

@SuppressWarnings("serial")
public class Scene extends View{

    protected String key = SceneController.SCENE;
    protected String username;
    protected String tabKey;
	
	public Scene(){
		super();
	}
	
	public Scene(String key, String username, String tabKey){
		super();
		this.setKey(key);
		this.setUsername(username);
		this.setTabKey(tabKey);
	}
	
	protected TextView drawText(String textKey, Rectangle rect) {
		TextView module = new TextView(textKey);
		module.setBounds(rect);
		this.add(module);
		return module;
	}
	
	protected TextView drawText(String textKey, Rectangle rect, int fontSize) {
		TextView module = this.drawText(textKey, rect);
		module.setFont(LanguageModel.getDefaultFont(fontSize));
		return module;
	}

	public String getKey() {
		return key;
	}

	protected void setKey(String key) {
		this.key = key;
	}
	
	public String getUsername() {
		return this.username;
	}

	protected void setUsername(String username) {
		this.username = username;
	}
	
	public String getTabKey() {
		return tabKey;
	}

	protected void setTabKey(String tabKey) {
		this.tabKey = tabKey;
	}
	
	protected SceneController getSceneController() {
		return ((ViewSceneCard)(this.getParent().getParent())).getSceneController();
	}
	
}
