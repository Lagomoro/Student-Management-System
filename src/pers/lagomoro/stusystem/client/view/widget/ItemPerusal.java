package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.text.html.HTMLDocument;

import org.apache.commons.codec.binary.Base64;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.CommandModel;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.module.ButtonSelectGroup;
import pers.lagomoro.stusystem.client.view.module.LimitDocument;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.module.DataClassVoteItem;
import pers.lagomoro.stusystem.data.DataPerusal;
import pers.lagomoro.stusystem.data.JsonDataController;

@SuppressWarnings("serial")
public class ItemPerusal extends View {
	
	protected TextPaneHTMLEditor textPane;
	protected View headView;
	protected View itemView;
	
	protected View votePane;
	protected View voteButtonView;
	protected ButtonSelectGroup voteSelectGroup;

	protected JTextArea textComment;
	protected View commentButtonView;
	protected View commentPane;
	
	protected DataPerusal perusal;
	protected HTMLDocument document;
	
	protected ButtonStyled deleteButton;
	protected ButtonStyled openButton;
	
	public ItemPerusal(DataPerusal perusal) {
		super();
		this.setPerusal(perusal);
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
		this.createVote();
		this.createComment();
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
		this.deleteButton = new ButtonStyled(null, GraphicsController.WARNING_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				CommandModel.commandActive("deletePerusal", DataPerusal.parse(ClientController.getMainConnectorKey(), perusal.id));
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
		buttonView.add(this.deleteButton);
		
		placeX -= 45;
		this.openButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(this.isSelected()) {
					this.setSelected(false);
					ItemPerusal.this.close();
				}else {
					this.setSelected(true);
					ItemPerusal.this.open();
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
		this.openButton.setBounds(placeX, 15, 30, 30);
		this.openButton.setShade(1);
		this.openButton.setRadius(15);
		buttonView.add(this.openButton);
	}
	
	protected void createVote() {
		this.voteSelectGroup = new ButtonSelectGroup();
		
		this.voteButtonView = new View();
		this.voteButtonView.setLayout(new BorderLayout());
		this.itemView.add(this.voteButtonView);
		
		View voteEast = new View();
		voteEast.setLayout(new BorderLayout());
		voteButtonView.add(voteEast, BorderLayout.EAST);
		
		voteEast.add(new TextView("UI->Scene->Perusal::SendVote"), BorderLayout.CENTER);
		
		View voteEastEast = new View();
		voteEastEast.setPreferredSize(new Dimension(40, 40));
		voteEast.add(voteEastEast, BorderLayout.EAST);
		
		ButtonStyled voteSendButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				String json = DataPerusal.parse(ClientController.getMainConnectorKey(), perusal.id, getChooses());
				CommandModel.commandActive("insertPerusalChoose", json);
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3 + 2, 2);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 2, this.getHeight()/3 + 2);
			}
		};
		voteSendButton.setBounds(2, 8, 24, 24);
		voteSendButton.setShade(1);
		voteSendButton.setRadius(15);
		voteEastEast.add(voteSendButton);
		
		this.votePane = new View();
		this.votePane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
		this.itemView.add(this.votePane);
	}
	
	protected void createComment() {
		this.commentButtonView = new View();
		this.commentButtonView.setLayout(new BorderLayout());
		this.itemView.add(this.commentButtonView);
		
		View commentEast = new View();
		commentEast.setLayout(new BorderLayout());
		commentButtonView.add(commentEast, BorderLayout.EAST);

		commentEast.add(new TextView("UI->Scene->Perusal::SendComment"), BorderLayout.CENTER);

		View commentEastEast = new View();
		commentEastEast.setPreferredSize(new Dimension(40, 40));
		commentEast.add(commentEastEast, BorderLayout.EAST);
		
		ButtonStyled commentSendButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				String json = DataPerusal.parse(ClientController.getMainConnectorKey(), perusal.id, textComment.getText());
				CommandModel.commandActive("insertPerusalComment", json);
				textComment.setText("");
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3 + 2, 2);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 2, this.getHeight()/3 + 2);
			}
		};
		commentSendButton.setBounds(2, 8, 24, 24);
		commentSendButton.setShade(1);
		commentSendButton.setRadius(15);
		commentEastEast.add(commentSendButton);
		
		this.textComment = new JTextArea();
		this.textComment.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		this.textComment.setFont(LanguageModel.getDefaultFont());
		this.textComment.setLineWrap(true);
		this.textComment.setDocument(new LimitDocument(150));
		this.textComment.setBackground(GraphicsController.EMPTY_COLOR);
		this.textComment.setOpaque(false);
		this.itemView.add(this.textComment);
		
		this.commentPane = new View();
		this.commentPane.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
		this.itemView.add(this.commentPane);
	}
	
	protected int getContentHeight(int width) {
		TextPaneHTMLEditor testPane = new TextPaneHTMLEditor();
		testPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		testPane.setFont(LanguageModel.getDefaultFont());
		testPane.setSize(width, Short.MAX_VALUE);
		testPane.setStyledDocument(this.document);
		return testPane.getPreferredSize().height;
	}
	
	protected int getCommentHeight(int width, String text) {
		JTextArea comment = new JTextArea();
		comment.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		comment.setFont(LanguageModel.getDefaultFont());
		comment.setLineWrap(true);
		comment.setSize(width, Short.MAX_VALUE);
		comment.setText(text);
		return comment.getPreferredSize().height;
	}
		
	protected int getAllHeight() {
		return this.textPane.getHeight() + 
				this.voteButtonView.getHeight() + 
				this.votePane.getHeight() + 
				this.commentButtonView.getHeight() + 
				this.textComment.getHeight() + 
				this.commentPane.getHeight();
	}
	
	protected int getCommentHeight() {
		for(Component comp : this.commentPane.getComponents()) {
			JTextArea comment = (JTextArea)comp;
			comment.setBounds(0, 0, this.getWidth(), getCommentHeight(this.getWidth(), comment.getText()));
		}
		return this.commentPane.getPreferredSize().height;
	}
	
	protected void setAllBounds() {
		this.textPane.setBounds(0, 0, this.getWidth(), this.getContentHeight(this.getWidth()));
		this.voteButtonView.setBounds(0, this.textPane.getY() + this.textPane.getHeight(), this.getWidth(), 40);
		this.votePane.setBounds(0, this.voteButtonView.getY() + this.voteButtonView.getHeight(), this.getWidth(), this.votePane.getComponentCount() * 45);
		this.commentButtonView.setBounds(0, this.votePane.getY() + this.votePane.getHeight(), this.getWidth(), 40);
		this.textComment.setBounds(0, this.commentButtonView.getY() + this.commentButtonView.getHeight(), this.getWidth(), 165);
		this.commentPane.setBounds(0, this.textComment.getY() + this.textComment.getHeight(), this.getWidth(), this.getCommentHeight());
	}
	
	public void setPerusal(DataPerusal perusal) {
		this.perusal = perusal;
		this.setDocument(this.getDocumentFromString(perusal.document));
		this.setVote();
		this.setComment();
		if(ClientController.canSensitiveOperation(perusal.username)) {
			this.openButton.setLocation(110 - 95, this.openButton.getY());
			this.deleteButton.setVisible(true);
		}else {
			this.openButton.setLocation(110 - 50, this.openButton.getY());
			this.deleteButton.setVisible(false);
		}
	}
	
	protected HTMLDocument getDocumentFromString(String objectString) {
		return this.document = (HTMLDocument)JsonDataController.deserializeObject(Base64.decodeBase64(objectString));
	}

	public void setDocument(HTMLDocument document){
		this.textPane.setStyledDocument(document);
	}
	
	public void setVote(){
		this.votePane.removeAll();
		if(this.perusal.options.length() == 0) {
			this.voteButtonView.setVisible(false);
		}else {
			boolean userVotes = this.userVotes();
			this.voteButtonView.setVisible(!userVotes);
			String[] chooseList = this.perusal.options.split(";");
			DataClassVoteItem[] infos = calculateVote(chooseList);
			this.voteSelectGroup.setMaxCount(this.perusal.max_choose);
			for(int i = 0; i < chooseList.length; i++) {
				ItemVote vote = new ItemVote(chooseList[i], this.voteSelectGroup);
				if(userVotes) vote.setPercent(infos[i]);
				this.voteSelectGroup.add(vote.getChooseButton());
				this.votePane.add(vote);
			}
		}
	}
	
	public void setComment(){
		this.commentPane.removeAll();
		int length = this.perusal.comments.length();
		if(length <= 2) return;
		String[] commentList = this.perusal.comments.substring(1, length - 2).split(";");
		for(String commentString : commentList) {
			JTextArea comment = new JTextArea() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					g.setColor(Color.LIGHT_GRAY);
					g.fillRect(0, 0, this.getWidth(), 1);
				}
			};
			comment.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
			comment.setFont(LanguageModel.getDefaultFont());
			comment.setBackground(GraphicsController.EMPTY_COLOR);
			comment.setLineWrap(true);
			comment.setEditable(false);
			comment.setText(commentString);
			this.commentPane.add(comment);
		}
	}
	
	protected boolean userVotes() {
		int length = this.perusal.chooses.length();
		if(length <= 2) return false;
		String[] chooses = this.perusal.chooses.substring(1, length - 2).split(";");
		boolean currentUserChoose = false;
		for(String choose : chooses) {
			String[] temp = choose.split(":");
			if(ClientController.isCurrentUser(temp[0])) currentUserChoose = true;
		}
		return currentUserChoose;
	}
	
	protected int getChooseTime() {
		int length = this.perusal.chooses.length();
		if(length <= 2) return 0;
		return this.perusal.chooses.substring(1, length - 2).split(";").length;
	}
	
	protected DataClassVoteItem[] calculateVote(String[] chooseList) {
		int length = this.perusal.chooses.length();
		if(length <= 2) return null;
		String[] chooses = this.perusal.chooses.substring(1, length - 2).split(";");
		int[] count = new int[chooseList.length];
		for(String choose : chooses) {
			String[] temp = choose.split(":");
			for(int i = 0;i < count.length;i++) {
				count[i] += Integer.parseInt(temp[1].substring(i, i+1));
			}
		}
		DataClassVoteItem[] output = new DataClassVoteItem[chooseList.length];
		for(int i = 0;i < count.length;i++) {
			output[i] = new DataClassVoteItem(count[i], chooses.length);
		}
		return output;
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
	
	protected String getChooses() {
		String vote = "";
		for(Component comp : this.votePane.getComponents()) {
			ItemVote voteItem = (ItemVote) comp;
			vote += voteItem.getChooseButton().isSelected() ? "1" : "0";
		}
		return vote;
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintHint(graphics);
		this.paintTitle(graphics, 8);
		this.paintDateAndUsername(graphics, 34);
		this.paintSeparate(graphics, 60);
		this.paintVote(graphics, 60);
		this.paintComment(graphics, 60);
	}
	
	protected void paintHint(Graphics2D graphics) {
		graphics.setColor(new Color(0, 200, 0));
		graphics.fillOval(15, 25, 10, 10);
	}
	
	protected void paintTitle(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString(this.perusal.title, 40, placeY + 20);
	}
	
	protected void paintSeparate(Graphics2D graphics, int placeY) {
		graphics.setColor(Color.LIGHT_GRAY);
		graphics.fillRect(0, 0, this.getWidth(), 2);
		graphics.fillRect(0, placeY, this.getWidth(), 1);
		
		graphics.fillRect(0, placeY + this.textPane.getHeight(), this.getWidth(), 1);
		if(this.perusal.options.length() > 0) graphics.fillRect(0, placeY + this.textPane.getHeight() + 40 - 1, this.getWidth(), 1);
		
		graphics.fillRect(0, placeY + this.textPane.getHeight() + 40 + this.votePane.getHeight(), this.getWidth(), 1);
		graphics.fillRect(0, placeY + this.textPane.getHeight() + 40 + this.votePane.getHeight() + 40 - 1, this.getWidth(), 1);
	}
	
	protected void paintDateAndUsername(Graphics2D graphics, int placeY) {
		graphics.setFont(LanguageModel.getDefaultFont(12));
		graphics.setColor(GraphicsController.HINT_LIGHTBLUE_COLOR);
		graphics.drawString(this.perusal.nickname, 40, placeY + 14);
		graphics.setColor(Color.DARK_GRAY);
		graphics.drawString(this.perusal.timestamp.toString(), 40 + 10 + (int)(graphics.getFont().getStringBounds(this.perusal.nickname, new FontRenderContext(new AffineTransform(), true, true)).getWidth()), placeY + 14);
	}
	
	protected void paintVote(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		String text1 = String.format(LanguageModel.get("UI->Scene->Perusal::StartVote"), this.perusal.max_choose, this.voteSelectGroup.getSelectionCount());
		String text2 = LanguageModel.get("UI->Scene->Perusal::NoVote");
		String text3 = String.format(LanguageModel.get("UI->Scene->Perusal::GetVote"), this.getChooseTime());
		graphics.drawString(this.perusal.options.length() > 0 ? (this.userVotes() ? text3 : text1) : text2, 40, placeY + this.textPane.getHeight() + 26);
	}
	
	protected void paintComment(Graphics2D graphics, int placeY) {
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.setFont(LanguageModel.getDefaultFont(18));
		graphics.drawString(String.format(LanguageModel.get("UI->Scene->Perusal::StartComment") + "(%s/150)", this.textComment.getText().length()), 40, placeY + this.textPane.getHeight() + 40 + this.votePane.getHeight() + 26);
	}
	
}
