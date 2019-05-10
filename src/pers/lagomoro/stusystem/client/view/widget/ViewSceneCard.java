package pers.lagomoro.stusystem.client.view.widget;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.view.component.Flash;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.module.ButtonLockGroup;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;

@SuppressWarnings("serial")
public class ViewSceneCard extends ViewStyled{
	
	protected View tabView;
	protected View cardView;
	protected ViewMusic musicView;
	protected ViewMusicTab musicViewTab;
	
	protected SceneController sceneController;
	
	protected ButtonLockGroup buttonLockGroup;
	
	public ViewSceneCard(SceneController sceneController) {
		this.sceneController = sceneController;
	}
	
	@Override
	protected void create() {
		super.create();
		this.createButtonGroup();
		this.createSceneTab();
		this.createSceneCard();
	}
	
	protected void createButtonGroup() {
		this.buttonLockGroup = new ButtonLockGroup();
	}

	protected void createSceneTab() {
		this.tabView = new View();
		this.tabView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
		this.add(this.tabView);
		
		this.musicViewTab = new ViewMusicTab();
		this.add(this.musicViewTab);
	}
	
	protected void createSceneCard() {
		this.cardView = new View();
		this.cardView.setLayout(new CardLayout());
		this.cardView.add(new Scene());
		this.add(this.cardView);
		
		this.musicView = new ViewMusic();
		this.add(this.musicView);
	}
	
	public void addScene(Scene scene) {
		this.addSceneAt(scene, this.tabView.getComponentCount());
	}
	
	public void addSceneAt(Scene scene, int x, int y) {
		if(this.getTargetIndex(x, y) != -1) {
			this.addSceneAt(scene, this.getTargetIndex(x, y));
		}
	}
	
	public void addSceneAt(Scene scene, int index) {
		this.cardView.add(scene, scene.getKey());
		ButtonSceneTab sceneTabStyled = new ButtonSceneTab(scene.getKey(), scene.getUsername(), scene.getTabKey(), GraphicsController.HOVER_BLUE_COLOR, GraphicsController.DEFAULT_BLUE_COLOR, GraphicsController.TOUCH_BLUE_COLOR) {
			protected Flash drag;
			@Override
			protected boolean lockEvent() {
				return buttonLockGroup.isSelected(this);
			}
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(e.getX() > this.getWidth() - 30) {
					sceneController.removeScene(this.getSceneKey());
				}else {
					sceneController.focusScene(this.getSceneKey());
				}
			}
			@Override
			protected void onMouseDragged(MouseEvent e) {
				super.onMouseDragged(e);
				if(this.drag != null) {
					this.drag.setLocation(e.getXOnScreen() - this.event.getX(), e.getYOnScreen() - this.event.getY());
				}
			}
			@Override
			protected void onMouseDraggedLot(MouseEvent e) {
				super.onMouseDraggedLot(e);
				this.drag = new Flash();
				ButtonSceneTab tab = new ButtonSceneTab(scene.getKey(), scene.getUsername(), scene.getTabKey(), GraphicsController.HOVER_BLUE_COLOR, GraphicsController.DEFAULT_BLUE_COLOR, GraphicsController.TOUCH_BLUE_COLOR);
				tab.setBackground(this.locked ? (this.active ? this.touchColor : this.defaultColor) : (this.active ? this.getBackground() : this.defaultColor));
				tab.setTextColor(GraphicsController.DEFAULT_COLOR);
				this.drag.add(tab);
				this.drag.setSize(this.getBounds().width, this.getBounds().height);
				this.drag.setVisible(true);
				this.drag.setLocation(e.getXOnScreen() - this.event.getX(), e.getYOnScreen() - this.event.getY());
			}
			@Override
			protected void onMouseReleased(MouseEvent e) {
				super.onMouseReleased(e);
				if(this.drag != null) {
					this.drag.dispose();
					this.drag = null;
					//moveTabTo(this, e.getXOnScreen(), e.getYOnScreen());
					WindowController.processSceneDrag(ViewSceneCard.this, this, scene, e.getXOnScreen(), e.getYOnScreen());
				}
			}
		};
		sceneTabStyled.setLocked(true);
		sceneTabStyled.setTextColor(GraphicsController.DEFAULT_COLOR);
		this.buttonLockGroup.add(sceneTabStyled);
		this.tabView.add(sceneTabStyled, index);
	}
	
	public void moveTabTo(ButtonSceneTab tab, int index) {
		this.tabView.setComponentZOrder(tab, index);
		this.refresh();
	}
	
	public void moveTabTo(ButtonSceneTab tab, int x, int y) {
		if(this.getTargetIndex(x, y) != -1) {
			int index = this.getTargetIndex(x, y);
			if(index > this.tabView.getComponentZOrder(tab)) index--;
			this.moveTabTo(tab, index);
		}
	}
	
	public int getTargetIndex(int x, int y) {
		Point targetPoint = this.tabView.getLocationOnScreen();
		if(x < targetPoint.x || y < targetPoint.y || x > targetPoint.x + this.tabView.getWidth() || y > targetPoint.y + this.tabView.getHeight()) {
			return -1;
		}
		return Math.min((y - targetPoint.y + 25)/50, this.tabView.getComponentCount());
	}
    
    public void focusScene(String key) {
    	for(Component comp : this.tabView.getComponents()) {
    		if(comp instanceof ButtonSceneTab && ((ButtonSceneTab)comp).getSceneKey().equals(key)) {
    			buttonLockGroup.setSelected((ButtonSceneTab)comp, true);
    		}
    	}
    	((CardLayout)this.cardView.getLayout()).show(this.cardView, key);
    }

    public void removeScene(Scene scene) {
    	for(Component comp : this.tabView.getComponents()) {
    		if(comp instanceof ButtonSceneTab && ((ButtonSceneTab)comp).getSceneKey().equals(scene.getKey())) {
    			buttonLockGroup.remove((ButtonSceneTab)comp);
				tabView.remove((ButtonSceneTab)comp);
				if(buttonLockGroup.getButtonCount() > 0) {
					sceneController.focusScene(((ButtonSceneTab)buttonLockGroup.getSelection()).getSceneKey());
				}
    		}
    	}
    	this.cardView.remove(scene);
    	this.refresh();
    }
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.tabView.setBounds(this.shade, this.shade, 190, this.getBounds().height - this.shade * 2);
		this.cardView.setBounds(this.shade + 190, this.shade, this.getBounds().width - this.shade * 2 - 190, this.getBounds().height - this.shade * 2);
		this.musicView.setBounds(this.shade + 190, this.shade, this.getBounds().width - this.shade * 2 - 190, this.getBounds().height - this.shade * 2);
		this.musicViewTab.setBounds(this.shade, this.getHeight() - 190 - this.shade, 190, 190);
	}
	
	@Override
	public void setShade(int shade) {
		super.setShade(shade);
		this.tabView.setBounds(this.shade, this.shade, 190, this.getBounds().height - this.shade * 2);
		this.cardView.setBounds(this.shade + 190, this.shade, this.getBounds().width - this.shade * 2 - 190, this.getBounds().height - this.shade * 2);
	}

	@Override
	public void refresh() {
		super.refresh();
		this.tabView.getLayout().layoutContainer(this.tabView);
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.printCard(graphics);
	}

	protected void printCard(Graphics2D graphics) {
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.fillRoundRect(this.shade, this.shade, 190, this.getHeight() - this.shade * 2, this.radius * 2, this.radius * 2);
		graphics.fillRect(this.shade + 30, this.shade, 160, this.getHeight() - this.shade * 2);
	}
	
	public SceneController getSceneController() {
		return sceneController;
	}
}
