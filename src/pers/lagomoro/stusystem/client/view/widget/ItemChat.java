package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.text.DefaultStyledDocument;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.Scene_ChatRoom;
import pers.lagomoro.stusystem.data.DataChat;
import pers.lagomoro.stusystem.data.JsonDataController;

@SuppressWarnings("serial")
public class ItemChat extends View {
	
	protected boolean right;
	protected TextPane textPane;
	protected DataChat chat;
	
	protected View textView;
	
	public static final int PANE_WIDTH = 400;
	
	protected DefaultStyledDocument document;
	
	public ItemChat(DataChat chat) {
		super();
		this.setChat(chat);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
	}
	
	@Override
	protected void create() {
		super.create();
		this.createTextPane();
	}
	
	protected void createTextPane() {
		this.textView = new View();
		this.textView.setPreferredSize(new Dimension(PANE_WIDTH + 180, 0));
		this.add(this.textView, BorderLayout.WEST);
		
		this.textPane = new TextPane() {
			@Override
			protected void onMouseRightClicked(MouseEvent e) {
				rightClickedText(e);
			}
		};
		this.textPane.setFont(LanguageModel.getDefaultFont());
		this.textPane.setBackground(GraphicsController.EMPTY_COLOR);
		this.textPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		this.textPane.setEditable(false);
		this.textPane.setOpaque(false);
		this.textView.add(this.textPane);
	}
	
	protected int getContentHeight(int width) {
		TextPane testPane = new TextPane();
		testPane.setFont(LanguageModel.getDefaultFont());
		testPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		testPane.setSize(width, Short.MAX_VALUE);
		testPane.setStyledDocument(this.document);
		return testPane.getPreferredSize().height;
	}
	
	protected int getStringWidth(String text, Font font) {
		return (int)font.getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth();
	}
	
	public TextPane getTextPane() {
		return textPane;
	}
	
	public void setChat(DataChat chat) {
		this.chat = chat;
		this.right = ClientController.isCurrentUser(chat.username);
		this.setDocument(this.getDocumentFromString(chat.document));
		this.textPane.setBounds(90, 60, PANE_WIDTH, this.getContentHeight(PANE_WIDTH));
		this.add(this.textView, this.right ? BorderLayout.EAST : BorderLayout.WEST);
		this.setBounds(0, 0, this.getWidth(), chat.withdraw ? 60 : this.textPane.getHeight() + 80);
	}
	
	public DataChat getChat() {
		return chat;
	}
	
	protected DefaultStyledDocument getDocumentFromString(String objectString) {
		return this.document = (DefaultStyledDocument)JsonDataController.deserializeObject(Base64.decodeBase64(objectString));
	}

	public void setDocument(DefaultStyledDocument document){
		this.textPane.setStyledDocument(document);
	}
	
	protected void rightClickedText(MouseEvent e) {
		super.onMouseRightClicked(e);
		((Scene_ChatRoom)this.getParent().getParent().getParent().getParent()).showWithDrawMenu(e.getXOnScreen(), e.getYOnScreen(), this.chat);
	}

	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintDate(graphics, 0);
		if(!this.chat.withdraw) {
			this.paintHeadImage(graphics, 20);
			this.paintUsername(graphics, 30);
			this.paintBubble(graphics, 30);
		}
	}
	
	protected void paintDate(Graphics2D graphics, int placeY) {
		this.paintHint(graphics, this.chat.withdraw ? String.format(LanguageModel.get("UI->Scene->ChatRoom::Withdraw"), this.chat.username) : this.chat.timestamp.toString(), placeY);
	}
	
	protected void paintHeadImage(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.HOVER_COLOR);
		graphics.fillOval(this.right ? this.getWidth() - 65 : 15, placeY + 15, 50, 50);
		graphics.drawImage(ImageModel.getUserImage(this.chat.username), this.right ? this.getWidth() - 65 : 15, placeY + 15, 50, 50, null);
	}
	
	protected void paintUsername(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.drawString(this.chat.nickname, this.right ? this.getWidth() - this.getStringWidth(this.chat.nickname, graphics.getFont()) - 80 : 80, placeY + 15);
	}
	
	protected void paintBubble(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.HOVER_COLOR);
		graphics.fillRoundRect(this.right ? this.getWidth() - PANE_WIDTH - 100 : 80, placeY + 20, PANE_WIDTH + 20, this.getHeight() - placeY - 30, 20, 20);
		graphics.fillPolygon(this.right ? new int[]{this.getWidth() - 70, this.getWidth() - 80, this.getWidth() - 80} : new int[]{70, 80, 80}, new int[]{placeY + 30, placeY + 28, placeY + 40}, 3);
	}
	
	protected void paintHint(Graphics2D graphics, String hint, int placeY) {
		graphics.setFont(LanguageModel.getDefaultFont(12));
		int width = this.getStringWidth(hint, graphics.getFont());
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRoundRect((this.getWidth() - width - 10)/2, placeY + 12, width + 10, 18, 18, 18);
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.drawString(hint, (this.getWidth() - width)/2, placeY + 25);
	}
	
}
