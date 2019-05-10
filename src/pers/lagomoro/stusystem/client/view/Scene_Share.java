package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JFileChooser;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.module.ConnecterFile;
import pers.lagomoro.stusystem.client.controller.module.FileEvent;
import pers.lagomoro.stusystem.client.controller.module.FileProcessListener;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.data.DataFile;
import pers.lagomoro.stusystem.data.DataUpdate;
import pers.lagomoro.stusystem.client.view.widget.ItemFile;
import pers.lagomoro.stusystem.client.view.widget.TextView;

@SuppressWarnings("serial")
public class Scene_Share extends Scene{
	
	protected ScrollPane scrollPane;
	protected View shareView;
	
	protected View buttonView;
	
	protected int processing = 0;
	
	public Scene_Share(){
		super(SceneController.SCENE_SHARE, null, "UI->Window->Main::Share");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		CommandModel.addCommand("share_SceneCallback", json -> this.shareCallBack(json));
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
		CommandModel.commandActive("share_SceneUpdate", DataUpdate.parse(ClientController.getMainConnectorKey()));
	}
	
	protected void createContent() {
		int placeY = 15;
		this.drawText("UI->Scene->Share::Title", new Rectangle(20, placeY, 500, 40), 24);
		placeY += 40;
		this.drawText("UI->Scene->Share::Description", new Rectangle(20, placeY, 500, 20), 12);

		this.buttonView = new View();
		this.buttonView.setBounds(0, placeY - 55, 110, 77);
		this.add(this.buttonView);
		
		int placeX = 0;
		TextView uploadText = new TextView("UI->Scene->Share::UploadFile");
		uploadText.setBounds(new Rectangle(placeX, placeY + 2, 100, 20));
		this.buttonView.add(uploadText);
		ButtonStyled uploadButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setDialogTitle(LanguageModel.get("UI->Scene->Share::ChooseFile"));
    			fileChooser.setApproveButtonText(LanguageModel.get("UI->Scene->Share::Approve"));
				if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					processing += 1;
					ItemFile itemFile = new ItemFile(new DataFile(null, null, file.getName(), file.length(), new Date(), 0));
					itemFile.setProcess(new FileEvent(-1, 0, 0));
					shareView.add(itemFile, 0);
					shareView.refresh();
					ConnecterFile fileConnecter = new ConnecterFile();
					fileConnecter.addListener(new FileProcessListener() {
						@Override
						public void onProcessingUpload(FileEvent e) {
							super.onProcessingUpload(e);
							itemFile.setProcess(e);
						};
						@Override
						public void onSuccessUpload(FileEvent e) {
							super.onSuccessUpload(e);
							processing -= 1;
						};
						@Override
						public void onFailUpload(FileEvent e) {
							super.onFailUpload(e);
							processing -= 1;
							shareView.remove(itemFile);
							shareView.refresh();
						};
					});
					new Thread(() -> ClientController.uploadFile(file.getPath(), fileConnecter)).start();
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 3, this.getHeight()/3);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3, 3);
		    }
		};
		uploadButton.setBounds(placeX + 29, placeY - 40, 35, 35);
		uploadButton.setShade(5);
		uploadButton.setRadius(20);
		this.buttonView.add(uploadButton);
		
		placeY += 35;
		this.shareView = new View();	
		this.shareView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
		this.scrollPane = new ScrollPane(this.shareView);
		this.scrollPane.setBounds(0, placeY, 0, 0);
		this.add(scrollPane, BorderLayout.CENTER);
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
		this.shareView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.shareView.refresh();
	}
	
	protected void shareCallBack(String jsonList) {
		if(processing > 0) return;
		this.shareView.removeAll();
		LinkedList<DataFile> files = DataFile.fromJsonList(jsonList);
		for(DataFile file : files) {
			this.shareView.add(new ItemFile(file));
		}
		this.refresh();
	}
	
}
