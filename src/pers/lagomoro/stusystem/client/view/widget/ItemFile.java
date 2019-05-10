package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.JFileChooser;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.ConnecterFile;
import pers.lagomoro.stusystem.client.controller.module.FileEvent;
import pers.lagomoro.stusystem.client.controller.module.FileProcessListener;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.model.StorageModel;
import pers.lagomoro.stusystem.data.DataFile;

@SuppressWarnings("serial")
public class ItemFile extends View {
	
	protected DataFile file;
	
	protected FileEvent fileEvent;
	protected View buttonView;
	
	protected ButtonStyled downloadButton;
	protected ButtonStyled deleteButton;
	
	public ItemFile(DataFile file) {
		super();
		this.setFile(file);
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
	}
	
	@Override
	protected void create() {
		super.create();
		this.createButton();
		this.setBounds(0, 0, 0, 65);
	}
	
	protected void createButton() {
		this.buttonView = new View();
		this.buttonView.setPreferredSize(new Dimension(110, 60));
		this.add(this.buttonView, BorderLayout.EAST);
		
		int placeX = 110 - 50;
		this.deleteButton = new ButtonStyled(null, GraphicsController.WARNING_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				CommandModel.commandActive("deleteFile", DataFile.parse(ClientController.getMainConnectorKey(), file.id));
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				graphics.drawLine(this.getWidth()/3, this.getHeight()/3, this.getWidth()*2/3 + 1, this.getHeight()*2/3 + 1);
				graphics.drawLine(this.getWidth()/3, this.getHeight()*2/3 + 1, this.getWidth()*2/3 + 1, this.getHeight()/3);
		    }
		};
		this.deleteButton.setBounds(placeX, 15, 30, 30);
		this.deleteButton.setShade(1);
		this.deleteButton.setRadius(15);	
		this.buttonView.add(this.deleteButton);
		
		placeX -= 45;
		this.downloadButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setDialogTitle(LanguageModel.get("UI->Scene->Share::ChooseDown"));
				fileChooser.setApproveButtonText(LanguageModel.get("UI->Scene->Share::Approve"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    			if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					setProcess(new FileEvent(0, 0, 0));
					ConnecterFile fileConnecter = new ConnecterFile();
					fileConnecter.addListener(new FileProcessListener() {
						@Override
						public void onProcessingDownload(FileEvent e) {
							super.onProcessingDownload(e);
							setProcess(e);
						};
						@Override
						public void onSuccessDownload(FileEvent e) {
							super.onSuccessDownload(e);
							setProcess(e);
						};
						@Override
						public void onFailDownload(FileEvent e) {
							super.onFailDownload(e);
							setProcess(e);
						};
					});
					ClientController.downloadFile(fileChooser.getSelectedFile().getPath() + "/" + file.filename, fileConnecter, file);
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				int deltaY = 7;
				graphics.drawLine(this.getWidth()/2, this.getHeight()/4, this.getWidth()/2, this.getHeight()/2 + deltaY);
				graphics.drawLine(this.getWidth()/3, this.getHeight()/3 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
				graphics.drawLine(this.getWidth()*2/3 + 1, this.getHeight()/3 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
		    }
		};
		this.downloadButton.setBounds(placeX, 15, 30, 30);
		this.downloadButton.setShade(1);
		this.downloadButton.setRadius(15);
		this.buttonView.add(this.downloadButton);
	}

	public void setFile(DataFile file) {
		this.file = file;
		if(ClientController.canSensitiveOperation(file.username)) {
			this.downloadButton.setLocation(110 - 95, this.downloadButton.getY());
			this.deleteButton.setVisible(true);
		}else {
			this.downloadButton.setLocation(110 - 50, this.downloadButton.getY());
			this.deleteButton.setVisible(false);
		}
	}
	
	public void setProcess(FileEvent e) {
		this.fileEvent = e;
		this.buttonView.setVisible(e.process < -1);
		this.refresh();
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintProcess(graphics);
		this.paintHint(graphics);
		this.paintFileImage(graphics);
		this.paintFilename(graphics, 2);
		this.paintDateAndUsername(graphics, 26);
		this.paintSize(graphics, 43);
	}
	
	protected void paintProcess(Graphics2D graphics) {
		if(this.fileEvent == null || this.fileEvent.process < -1) {
			return;
		} else {
			graphics.setColor(GraphicsController.HOVER_COLOR);
			int width;
			if(this.fileEvent.length != 0) {
				width = (int)(this.getWidth() * (double)this.fileEvent.process / this.fileEvent.length);
			} else {
				width = (int)(this.getWidth() * (double)this.fileEvent.process / this.file.size);
			}
			graphics.fillRect(0, 0, width, this.getHeight());
		}
	}
	protected void paintHint(Graphics2D graphics) {
		graphics.setColor(new Color(0, 200, 0));
		graphics.fillOval(15, (this.getHeight() - 10)/2, 10, 10);
	}
	
	protected void paintFileImage(Graphics2D graphics) {
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.fillRoundRect(30, 5, this.getHeight() - 10, this.getHeight() - 10, 20, 20);
	}
	
	protected void paintFilename(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString(this.file.filename, this.getHeight() + 30, placeY + 20);
	}
	
	protected void paintDateAndUsername(Graphics2D graphics, int placeY) {
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		if(this.fileEvent == null || this.fileEvent.process < -1) {
			graphics.drawString(this.file.nickname, this.getHeight() + 30, placeY + 14);
			graphics.setColor(Color.DARK_GRAY);
			graphics.drawString(this.file.timestamp.toString(), this.getHeight() + 30 + 10 + (int)(graphics.getFont().getStringBounds(this.file.nickname, new FontRenderContext(new AffineTransform(), true, true)).getWidth()), placeY + 14);
		} else if(this.fileEvent.process < 0){
			graphics.drawString(LanguageModel.get("UI->Scene->Share::Calculating"), this.getHeight() + 30, placeY + 14);
		} else if(this.fileEvent.process < 0.1){
			graphics.drawString(LanguageModel.get("UI->Scene->Share::Verifing"), this.getHeight() + 30, placeY + 14);
		} else if(this.fileEvent.length > 0){
			graphics.drawString(LanguageModel.get("UI->Scene->Share::Uploading"), this.getHeight() + 30, placeY + 14);
		} else {
			graphics.drawString(LanguageModel.get("UI->Scene->Share::Downloading"), this.getHeight() + 30, placeY + 14);
		}
	}
	
	protected void paintSize(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.DARK_GRAY);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		if(this.fileEvent == null || this.fileEvent.process < -1) {
			graphics.drawString(StorageModel.getFileSizeText(this.file.size), this.getHeight() + 30, placeY + 14);
		} else {
			String text = "";
			if(this.fileEvent.length != 0) {
				text = StorageModel.getFileSizeText(this.fileEvent.process) + "/" + StorageModel.getFileSizeText(this.fileEvent.length) + "(" + this.fileEvent.process * 100 / this.fileEvent.length + "%) " + StorageModel.getFileSizeText(this.fileEvent.speed) + "/s";
			} else {
				text = StorageModel.getFileSizeText(this.fileEvent.process) + "/" + StorageModel.getFileSizeText(this.file.size) + "(" + this.fileEvent.process * 100 / this.file.size + "%) " + StorageModel.getFileSizeText(this.fileEvent.speed) + "/s";
			}
			graphics.drawString(text, this.getHeight() + 30, placeY + 14);
		}
	}

}
