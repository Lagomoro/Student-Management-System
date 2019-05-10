package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.text.html.HTMLDocument;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.data.DataNotice;
import pers.lagomoro.stusystem.data.JsonDataController;

@SuppressWarnings("serial")
public class ItemNotice extends View {
	
	protected TextPaneHTMLEditor textPane;
	protected View headView;
	protected View itemView;
	
	protected DataNotice notice;
	protected HTMLDocument document;
	
	public ItemNotice(DataNotice notice) {
		super();
		this.setNotice(notice);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
	}
	
	@Override
	protected void create() {
		super.create();
		this.createHeadPane();
		this.createButton();
		this.createItemPane();
		this.createTextPane();
		this.setBounds(0, 0, 0, 60);
	}

	protected void createHeadPane() {
		this.headView = new View();
		this.headView.setLayout(new BorderLayout());
		this.headView.setPreferredSize(new Dimension(0, 60));
		this.add(this.headView,BorderLayout.NORTH);
	}
	
	protected void createItemPane() {
		this.itemView = new View();
		this.itemView.setLayout(null);
		this.add(this.itemView,BorderLayout.CENTER);
	}

	protected void createTextPane() {
		this.textPane = new TextPaneHTMLEditor();
		this.textPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		this.textPane.setFont(LanguageModel.getDefaultFont());
		this.textPane.setBackground(GraphicsController.EMPTY_COLOR);
		this.textPane.setEditable(false);
		this.textPane.setOpaque(false);
		this.itemView.add(this.textPane);
	}
	
	protected void createButton() {
		View buttonView = new View();
		buttonView.setPreferredSize(new Dimension(110, 60));
		this.headView.add(buttonView, BorderLayout.EAST);
		
		int placeX = 110 - 50;
		ButtonStyled deleteButton = new ButtonStyled(null, GraphicsController.WARNING_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				CommandModel.commandActive("deleteNotice", DataNotice.parse(ClientController.getMainConnectorKey(), notice.id));
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
		deleteButton.setBounds(placeX, 15, 30, 30);
		deleteButton.setShade(1);
		deleteButton.setRadius(15);
		
		if(ClientController.isAdmin()) {
			buttonView.add(deleteButton);
			placeX -= 45;
		}
		
		ButtonStyled openButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(this.isSelected()) {
					this.setSelected(false);
					ItemNotice.this.close();
				}else {
					this.setSelected(true);
					ItemNotice.this.open();
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				graphics.drawLine(11, this.isSelected() ? 18 : 15, 16, this.isSelected() ? 13 : 20);
				graphics.drawLine(21, this.isSelected() ? 18 : 15, 16, this.isSelected() ? 13 : 20);
		    }
		};
		openButton.setBounds(placeX, 15, 30, 30);
		openButton.setShade(1);
		openButton.setRadius(15);
		buttonView.add(openButton);
	}
	
	protected int getContentHeight(int width) {
		TextPaneHTMLEditor testPane = new TextPaneHTMLEditor();
		testPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		testPane.setFont(LanguageModel.getDefaultFont());
		testPane.setSize(width, Short.MAX_VALUE);
		testPane.setStyledDocument(this.document);
		return testPane.getPreferredSize().height;
	}
		
	protected int getAllHeight() {
		return this.textPane.getHeight();
	}
	
	protected void setAllBounds() {
		this.textPane.setBounds(0, 0, this.headView.getWidth(), this.getContentHeight(this.getWidth()));
	}
	
	public void setNotice(DataNotice notice) {
		this.notice = notice;
		this.setDocument(this.getDocumentFromString(notice.document));
	}
	
	protected HTMLDocument getDocumentFromString(String objectString) {
		return this.document = (HTMLDocument)JsonDataController.deserializeObject(Base64.decodeBase64(objectString));
	}

	public void setDocument(HTMLDocument document){
		this.textPane.setStyledDocument(document);
	}
	
	public void open() {
		this.setAllBounds();
		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.headView.getHeight() + this.getAllHeight());
		this.getParent().getLayout().layoutContainer(this.getParent());
		((View)this.getParent()).refresh();
	}
	
	public void close() {
		this.setBounds(this.getX(), this.getY(), this.getWidth(), this.headView.getHeight());	
		this.getParent().getLayout().layoutContainer(this.getParent());
		((View)this.getParent()).refresh();
	}

	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintHint(graphics);
		this.paintTitle(graphics, 8);
		this.paintDateAndUsername(graphics, 34);
		this.paintSeparate(graphics, 60);
	}
	
	protected void paintHint(Graphics2D graphics) {
		graphics.setColor(new Color(0, 200, 0));
		graphics.fillOval(15, 25, 10, 10);
	}
	
	protected void paintTitle(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString(this.notice.title, 40, placeY + 20);
	}
	
	protected void paintSeparate(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, this.getWidth(), 2);
		graphics.fillRect(0, placeY, this.getWidth(), 1);
	}
	
	protected void paintDateAndUsername(Graphics2D graphics, int placeY) {
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.drawString(this.notice.nickname, 40, placeY + 14);
		graphics.setColor(Color.DARK_GRAY);
		graphics.drawString(this.notice.timestamp.toString(), 40 + 10 + (int)(graphics.getFont().getStringBounds(this.notice.nickname, new FontRenderContext(new AffineTransform(), true, true)).getWidth()), placeY + 14);
	}
	
}
