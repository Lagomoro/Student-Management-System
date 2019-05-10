package pers.lagomoro.stusystem.client.view.component;

import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.view.widget.ViewSceneCard;

@SuppressWarnings("serial")
public class WindowSceneable extends WindowTitled{
	
	protected SceneController sceneController;
	
	protected ViewSceneCard sceneCard;
	
	public WindowSceneable(){
		super();
	}
	
	public WindowSceneable(String key, String titleKey){
		super(key, titleKey);
	}
	
	protected void initialize(){
		super.initialize();
		//this.setSize(812, 624);
		this.setSize(1024, 768);
	}
	
	protected void create() {
		super.create();
		this.createSceneCard();
	}

	protected void createSceneCard() {
		this.sceneCard = new ViewSceneCard(this.sceneController);
		this.sceneCard.setBounds(20, 60, this.getWidth() - 40, this.getHeight() - 80);
		this.sceneCard.setShade(5);
		this.sceneCard.setRadius(5);
		this.add(this.sceneCard);
	}
	
	@Override
	protected void createController() {
		super.createController();
		this.sceneController = new SceneController(this);
	}

    public void addScene(Scene scene) {
    	this.sceneCard.addScene(scene);
	}
    
    public void addSceneAt(Scene scene, int index) {
    	this.sceneCard.addSceneAt(scene, index);
	}
    
    public void addSceneAt(Scene scene, int x, int y) {
    	this.sceneCard.addSceneAt(scene, this.sceneCard.getTargetIndex(x, y));
	}
    
    public void focusScene(Scene scene) {
    	focusScene(scene.getKey());
    }
    
    public void focusScene(String key) {
    	this.sceneCard.focusScene(key);
    }

    public void removeScene(Scene scene) {
    	this.sceneCard.removeScene(scene);
    }
    
    public int getTargetIndex(int x, int y) {
    	return this.sceneCard.getTargetIndex(x, y);
    }
	
	public void update() {
		super.update();
		this.sceneController.update();
	}

    public void refresh() {
    	super.refresh();
		this.sceneController.refresh();
		this.refreshSceneTitle();
	}
    
    public void refreshSceneTitle() {
//    	for(int i = 0 ; i < this.tabbedPane.getComponentCount() - 1; i++) {
//    		((TabInfo)this.tabbedPane.getTabComponentAt(i)).setText((Scene)this.tabbedPane.getComponentAt(i));
//    	}
	}
    
	public SceneController getSceneController() {
		return sceneController;
	}

}
