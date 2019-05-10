package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

import pers.lagomoro.stusystem.client.view.Scene_About;
import pers.lagomoro.stusystem.client.view.Scene_Class;
import pers.lagomoro.stusystem.client.view.Scene_Game;
import pers.lagomoro.stusystem.client.view.Scene_Home;
import pers.lagomoro.stusystem.client.view.Scene_MemberList;
//import pers.lagomoro.stusystem.client.view.Scene_Message;
import pers.lagomoro.stusystem.client.view.Scene_Option;
import pers.lagomoro.stusystem.client.view.Scene_Perusal;
import pers.lagomoro.stusystem.client.view.Scene_Share;
import pers.lagomoro.stusystem.client.view.Scene_Vote;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.WindowSceneable;

public class SceneController {
	
	//场景常量，用于维持场景单一性
	public static final String SCENE_HOME     = "Scene_Home";
	public static final String SCENE_CLASS    = "Scene_Class";
	public static final String SCENE_MESSAGE  = "Scene_Message";
	public static final String SCENE_VOTE     = "Scene_Vote";
	public static final String SCENE_PERUSAL  = "Scene_Perusal";
	public static final String SCENE_SHARE    = "Scene_Share";
	public static final String SCENE_GAME     = "Scene_Game";
	public static final String SCENE_OPTION   = "Scene_Option";
	public static final String SCENE_ABOUT    = "Scene_About";
	
	public static final String SCENE_NOTICE_EDITOR = "Scene_NoticeEditor";
	public static final String SCENE_PERUSAL_EDITOR = "Scene_PerusalEditor";
	public static final String SCENE_MEMBER_LIST = "Scene_MemberList";
	
	public static final String SCENE_USER_INFORMATION_CHANGE = "Scene_UserInformationChange";

	public static final String SCENE_PAINT_AND_GUESS = "Scene_Paint&Guess";
	public static final String SCENE_PAINT_AND_GUESS_GAMEROOM = "Scene_Paint&Guess_Gameroom";

	public static final String SCENE_CHATROOM = "Scene_ChatRoom";
	public static final String SCENE  = "Scene";
	
	//存储变量
	private HashMap<String, Scene> sceneMap = new HashMap<String, Scene>();
	private WindowSceneable window;
	
	public SceneController(WindowSceneable window) {
		this.initialize(window);
	}
	
	private void initialize(WindowSceneable window) {
		this.window = window;
	}
	
	public WindowSceneable getWindow() {
		return window;
	}
	
    //场景的实时更新
	public void update() {
		for (String key : this.sceneMap.keySet()) {
			this.sceneMap.get(key).update();
		}
	}
	
    public void refresh() {
    	for (String key : this.sceneMap.keySet()) {
			this.sceneMap.get(key).refresh();
		}
	}
	
	//指定场景是否存在
	public boolean haveScene(String key) {
		return this.sceneMap.containsKey(key);
	}
	
	//获取指定场景
	public Scene getScene(String key) {
		return this.sceneMap.get(key);
	}
	
	//添加新场景
	public void addScene(Scene scene) {
		this.sceneMap.put(scene.getKey(), scene);
		this.window.addScene(scene);
	}
	
	public void addSceneAt(Scene scene, int x, int y) {
		this.sceneMap.put(scene.getKey(), scene);
		this.window.addSceneAt(scene, x, y);
	}
	
	//焦点转移至场景并自动创建
	public void focusScene(Scene scene) {
		if(this.haveScene(scene.getKey())) {
			this.focusScene(scene.getKey());
		}else {
			this.addScene(scene);
			this.focusScene(scene.getKey());
		}
	}
	
	//焦点转移至场景并自动创建
	public void focusScene(String key) {
		if(this.haveScene(key)) {
			WindowController.refresh();
			this.window.focusScene(this.sceneMap.get(key));
		}else {
			Scene scene = newScene(key);
			if (scene == null) return;
			this.addScene(scene);
			this.focusScene(key);
		}
	}
	
	//删除场景
	public void removeScene(String key) {
		this.window.removeScene(this.sceneMap.get(key));
		this.sceneMap.remove(key);
	}
	
	//获取新场景
	public Scene newScene(String key) {
		switch(key) {
		case SCENE_HOME    : return new Scene_Home();
		case SCENE_CLASS   : return new Scene_Class();
//		case SCENE_MESSAGE : return new Scene_Message();
		case SCENE_VOTE    : return new Scene_Vote();
		case SCENE_PERUSAL : return new Scene_Perusal();
		case SCENE_SHARE   : return new Scene_Share();
		case SCENE_GAME    : return new Scene_Game();
		case SCENE_OPTION  : return new Scene_Option();
		case SCENE_ABOUT   : return new Scene_About();
		
		case SCENE_MEMBER_LIST : return new Scene_MemberList();
		default : return null;
		}
	}
	
}
