package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.data.DataPerusal;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.data.JsonDataController;
import pers.lagomoro.stusystem.client.view.widget.ItemPerusal;
import pers.lagomoro.stusystem.client.view.widget.TextView;

@SuppressWarnings("serial")
public class Scene_Perusal extends Scene{
	
	protected ScrollPane scrollPane;
	protected View perusalView;
	
	protected View buttonView;

	public Scene_Perusal(){
		super(SceneController.SCENE_PERUSAL, null, "UI->Window->Main::Perusal");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("perusal_SceneCallback", jsonList -> this.perusalCallBack(jsonList));
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("perusal_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Perusal::Title", new Rectangle(20, placeY, 300, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Perusal::Description", new Rectangle(20, placeY, 300, 20), 12);

		this.buttonView = new View();
		this.buttonView.setBounds(0, placeY - 55, 110, 77);
		this.add(this.buttonView);
		
		int placeX = 0;
		TextView releaseText = new TextView("UI->Scene->Perusal::ReleasePerusal");
		releaseText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		this.buttonView.add(releaseText);
		ButtonStyled releaseButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.focusScene(new Scene_PerusalEditor("UI->Scene->Perusal::ToPerusal") {
					@Override
					protected void onSendClicked(MouseEvent e) {
						String text = Base64.encodeBase64String(JsonDataController.serializeObject(this.editorPane.getStyledDocument()));
						String json = DataPerusal.parse(ClientController.getMainConnectorKey(), this.titlePane.getText(), text, this.getMaxChoose(), this.getOptions());
						CommandModel.commandActive("sendPerusal", json);
						super.onSendClicked(e);
					}
				}, getSceneController());
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
		this.perusalView = new View();	
		this.scrollPane = new ScrollPane(this.perusalView);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
		this.perusalView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
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
		this.perusalView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.perusalView.refresh();
	}
	
	protected void perusalCallBack(String jsonList) {
		LinkedList<DataPerusal> perusals = DataPerusal.fromJsonList(jsonList);
		if(perusals.size() == this.perusalView.getComponentCount()) {
			for(int i = 0;i < perusals.size(); i++) {
				DataPerusal perusal = perusals.get(i);
				ItemPerusal perusalItem = ((ItemPerusal)this.perusalView.getComponent(i));
				perusalItem.setPerusal(perusal);
				if(perusalItem.getHeight() > 60) perusalItem.open();
			}
		}else {
			this.perusalView.removeAll();
			for(DataPerusal perusal : perusals) {
				this.perusalView.add(new ItemPerusal(perusal));
			}
		}
		this.refresh();
	}
	
}

