package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.ConnecterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.controller.module.ConnecterPaintAndGuess;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.AutoFlowLayout;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.client.view.widget.TextView;

@SuppressWarnings("serial")
public class Scene_PaintAndGuessGameRoom extends Scene{
	
	protected ScrollPane scrollPane;
	protected View roomView;
	
	protected View buttonView;

	public Scene_PaintAndGuessGameRoom(){
		super(SceneController.SCENE_PAINT_AND_GUESS_GAMEROOM, null, "UI->Scene->Game::Paint&Guess");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("paintAndGuess_SceneCallback", jsonList -> this.roomCallBack(jsonList));
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("paintAndGuess_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->PaintAndGuessGameRoom::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->PaintAndGuessGameRoom::Description", new Rectangle(20, placeY, 500, 20), 12);

		this.buttonView = new View();
		this.buttonView.setBounds(0, placeY - 55, 110, 77);
		this.add(this.buttonView);
		
		int placeX = 0;
		TextView releaseText = new TextView("UI->Scene->PaintAndGuessGameRoom::NewGameRoom");
		releaseText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		this.buttonView.add(releaseText);
		ButtonStyled releaseButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				CommandModel.commandActive("newPaintAndGuessGameRoom", DataUpdate.parse(ClientController.getMainConnectorKey()));
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 3, this.getHeight()/3);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3, 3);
		    }
		};
		releaseButton.setBounds(placeX + 29, placeY - 40, 35, 35);
		releaseButton.setShade(5);
		releaseButton.setRadius(20);
		this.buttonView.add(releaseButton);

		placeY += 35;
		this.roomView = new View();	
		this.scrollPane = new ScrollPane(this.roomView);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
		this.roomView.setLayout(new AutoFlowLayout(VerticalFlowLayout.TOP, 20, 20));
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.scrollPane.setBounds(this.scrollPane.getX(), this.scrollPane.getY(), width, height - 90);
		this.buttonView.setLocation(this.getWidth() - this.buttonView.getWidth(), this.buttonView.getY());
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void update() {
		super.update();
		this.roomView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.roomView.refresh();
	}
	
	protected void roomCallBack(String jsonList) {
		String[] rooms = jsonList.substring(1).split(";");
		this.roomView.removeAll();
		if(jsonList.length() <= 1) return;
		for(int i = 0;i < rooms.length; i++) {
			int port = Integer.parseInt(rooms[i]);
			ButtonStyled button = new ButtonStyled(rooms[i], GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
				@Override
				protected void onMouseClicked(MouseEvent e) {
					super.onMouseClicked(e);
					if(WindowController.getScene(SceneController.SCENE_PAINT_AND_GUESS + "_" + port) == null) {
						Scene_PaintAndGuess scene_PaintAndGuess = new Scene_PaintAndGuess(Integer.toString(port));
						ConnecterPaintAndGuess connecter = new ConnecterPaintAndGuess(Integer.toString(port), port, scene_PaintAndGuess);
						scene_PaintAndGuess.setConnecter(connecter);
						ConnecterController.addConnecter(connecter);
						WindowController.focusScene(scene_PaintAndGuess, getSceneController());
						CommandModel.commandActive("refreshPaintAndGuessGameRoom", "0");
					}else {
						WindowController.focusScene(SceneController.SCENE_PAINT_AND_GUESS + "_" + port, null);
					}
				}
			};
			button.setBounds(0, 0, 100, 40);
			button.setRadius(10);
			button.setShade(5);
			this.roomView.add(button);
		}
		this.refresh();
	}
	
}

