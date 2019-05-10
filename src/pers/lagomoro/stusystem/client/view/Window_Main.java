package pers.lagomoro.stusystem.client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.MusicController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.WindowSceneable;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.ViewStyled;

@SuppressWarnings("serial")
public class Window_Main extends WindowSceneable {
	
	protected ButtonStyled homeButton;
	protected ButtonStyled classButton;
	protected ButtonStyled messageButton;
	protected ButtonStyled voteButton;
	protected ButtonStyled perusalButton;
	protected ButtonStyled shareButton;
	protected ButtonStyled gameButton;
	protected ButtonStyled optionButton;
	protected ButtonStyled aboutButton;
	
	protected ButtonStyled pauseButton;

	public Window_Main(){
		super(WindowController.WINDOW_MAIN, "UI->Window->Main::Title");
	}
	
	@Override
    protected void initialize(){
		super.initialize();
	}
    
	@Override
	protected void create() {
		super.create();
		this.createContents();
	}

	protected void createContents() {
		this.createToolBar();
		this.sceneCard.setBounds(20 - 5, 110 - 5, this.getWidth() - 40 + 10, this.getHeight() - 130 + 10);
		this.sceneController.focusScene(SceneController.SCENE_HOME);
	}

	protected void createToolBar() {
		ViewStyled toolBar = new ViewStyled();
		toolBar.setBounds(20, 60, this.getWidth() - 40, 45);
		toolBar.setShade(5);
		toolBar.setRadius(5);
		this.add(toolBar);
		
		int placeX = 10; 
		int placeY = 10;
		homeButton = new ButtonStyled("UI->Window->Main::Home", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_HOME, sceneController);
			}
		};
		homeButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(homeButton);
		
		placeX += 60;
		this.classButton = new ButtonStyled("UI->Window->Main::Class", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_CLASS, sceneController);
			}
		};
		this.classButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.classButton);
		
//		placeX += 60;
//		this.messageButton = new ButtonStyled("UI->Window->Main::Message", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
//			@Override
//			protected void onMouseClicked(MouseEvent e) {
//				super.onMouseClicked(e);
//				WindowController.focusScene(SceneController.SCENE_MESSAGE, sceneController);
//			}
//		};
//		this.messageButton.setBounds(placeX, placeY, 55, 35);
//		toolBar.add(this.messageButton);
		
//		placeX += 60;
//		this.voteButton = new ButtonStyled("UI->Window->Main::Vote", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
//			@Override
//			protected void onMouseClicked(MouseEvent e) {
//				super.onMouseClicked(e);
//				WindowController.focusScene(SceneController.SCENE_VOTE, sceneController);
//			}
//		};
//		this.voteButton.setBounds(placeX, placeY, 55, 35);
//		toolBar.add(this.voteButton);
		
		placeX += 60;
		this.perusalButton = new ButtonStyled("UI->Window->Main::Perusal", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_PERUSAL, sceneController);
			}
		};
		this.perusalButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.perusalButton);
		
		placeX += 60;
		this.shareButton = new ButtonStyled("UI->Window->Main::Share", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_SHARE, sceneController);
			}
		};
		this.shareButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.shareButton);

		placeX += 60;
		this.gameButton = new ButtonStyled("UI->Window->Main::Game", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_GAME, sceneController);
			}
		};
		this.gameButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.gameButton);
		
		placeX += 60;
		this.optionButton = new ButtonStyled("UI->Window->Main::Option", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_OPTION, sceneController);
			}
		};
		this.optionButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.optionButton);
		
		placeX += 60;
		this.aboutButton = new ButtonStyled("UI->Window->Main::About", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(SceneController.SCENE_ABOUT, sceneController);
			}
		};
		this.aboutButton.setBounds(placeX, placeY, 55, 35);
		toolBar.add(this.aboutButton);
		
		placeX = this.getWidth() - 75;
		ButtonStyled windowButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusWindow(new Window_Child(Double.toString(Math.random()), "UI->Window->Child::Title"));
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				graphics.drawRect(this.getWidth()/4, this.getHeight()/3, this.getWidth()/2, this.getHeight()/2);
				graphics.drawRect(this.getWidth()/2, this.getHeight()/4, this.getWidth()/3, this.getHeight()/3);
			}
		};
		windowButton.setBounds(placeX, placeY, 35, 35);
		toolBar.add(windowButton);
		
		placeX -= 40;
		ButtonStyled playButton= new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(MusicController.getPlayStatus() > 0) {
					MusicController.stop();
				}else {
					JFileChooser fileChooser = new JFileChooser("./");
					fileChooser.setDialogTitle(LanguageModel.get("UI->Window->Main::ChooseMusic"));
	    			FileFilter filter = new FileNameExtensionFilter(LanguageModel.get("UI->Window->Main::MusicFliter"), "wav");
	    			fileChooser.addChoosableFileFilter(filter);
	    			fileChooser.setAcceptAllFileFilterUsed(false);
	    			fileChooser.setApproveButtonText(LanguageModel.get("UI->Window->Main::Approve"));
					if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
						MusicController.play(fileChooser.getSelectedFile().getPath());
					}
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				if(MusicController.getPlayStatus() > 0) {
					graphics.setColor(Color.RED);
					graphics.fillRect(10, 10, 15, 15);
				}else {
					graphics.setColor(Color.GREEN);
					graphics.fillPolygon(new int[]{10, 10, 25}, new int[]{9, 25, 17}, 3);
				}
			}
		};
		playButton.setBounds(placeX, placeY, 35, 35);
		toolBar.add(playButton);

		placeX -= 40;
		this.pauseButton= new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(MusicController.getPlayStatus() > 1) {
					MusicController.replay();
				}else if(MusicController.getPlayStatus() > 0) {
					MusicController.pause();
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				if(MusicController.getPlayStatus() > 1) {
					graphics.setColor(Color.GREEN);
					graphics.fillPolygon(new int[]{10, 10, 25}, new int[]{9, 25, 17}, 3);
				} else if(MusicController.getPlayStatus() > 0) {
					graphics.setColor(Color.RED);
					graphics.fillRect(10, 10, 5, 15);
					graphics.fillRect(20, 10, 5, 15);
				}
			}
		};
		this.pauseButton.setBounds(placeX, placeY, 35, 35);
		toolBar.add(pauseButton);
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.refreshToolBounds();
	}
	
	@Override
	public void update() {
		super.update();
		this.pauseButton.setVisible(MusicController.getPlayStatus() > 0);
	}

	protected void refreshToolBounds() {
		int placeX = 10;
		placeX = this.refreshButton(placeX, this.homeButton);
		placeX = this.refreshButton(placeX, this.classButton);
//		placeX = this.refreshButton(placeX, this.messageButton);
//		placeX = this.refreshButton(placeX, this.voteButton);
		placeX = this.refreshButton(placeX, this.perusalButton);
		placeX = this.refreshButton(placeX, this.shareButton);
		placeX = this.refreshButton(placeX, this.gameButton);
		placeX = this.refreshButton(placeX, this.optionButton);
		placeX = this.refreshButton(placeX, this.aboutButton);
	}
	
	protected int refreshButton(int placeX, ButtonStyled button) {
		Rectangle bound = button.getBounds();
		bound.x = placeX;
		bound.width = Math.max(button.getPreferredSize().width + 10, 55);
		button.setBounds(bound);
		return placeX + bound.width + 5;
	}
	
}
