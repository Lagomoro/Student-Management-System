package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.BoldAction;
import javax.swing.text.StyledEditorKit.ItalicAction;
import javax.swing.text.StyledEditorKit.UnderlineAction;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.component.Window;
import pers.lagomoro.stusystem.client.view.module.FontColorAction;
import pers.lagomoro.stusystem.client.view.module.StrikeThroughAction;
import pers.lagomoro.stusystem.client.view.module.SubscriptAction;
import pers.lagomoro.stusystem.client.view.module.SuperscriptAction;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.TextPane;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.view.widget.ItemChat;
import pers.lagomoro.stusystem.client.view.widget.ViewStyled;
import pers.lagomoro.stusystem.data.DataChat;

@SuppressWarnings("serial")
public class Scene_ChatRoom extends Scene {

	protected TextPane editorPane;
	protected ButtonStyled sendButton;
	protected View chatView;
	
	ScrollPane messageScrollPane;
	
	protected BoldAction boldAction; 
	protected ItalicAction italicAction; 
	protected UnderlineAction underlineAction; 
	protected StrikeThroughAction strikeThroughAction;
	
	protected SubscriptAction subscriptAction;
	protected SuperscriptAction superscriptAction;
	
	protected FontColorAction fontColorAction;
	
	protected ButtonStyled boldButton;
	protected ButtonStyled italicButton;
	protected ButtonStyled underlineButton;
	protected ButtonStyled strikeThroughButton;
	protected ButtonStyled subscriptButton;
	protected ButtonStyled superscriptButton;
	
	protected ButtonStyled fontColorButton;
	
	protected Color targetColor;
	
	protected JPopupMenu menu;
	protected JMenuItem withdrawItem;

	public Scene_ChatRoom(String title, String username){
		super(SceneController.SCENE_CHATROOM + "_" + title, username, title);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
		
		this.boldAction = new StyledEditorKit.BoldAction(); 
		this.italicAction = new StyledEditorKit.ItalicAction(); 
		this.underlineAction = new StyledEditorKit.UnderlineAction();
		this.strikeThroughAction = new StrikeThroughAction();
		
		this.subscriptAction = new SubscriptAction();
		this.superscriptAction = new SuperscriptAction();
		
		this.fontColorAction = new FontColorAction(this.targetColor);
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
		this.createPopUp();
	}
	
	protected void createContent() {
		this.chatView = new View();	
		this.chatView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
		this.messageScrollPane = new ScrollPane(this.chatView);
		this.add(this.messageScrollPane, BorderLayout.CENTER);
		
		View inputView = new View();
		inputView.setLayout(new BorderLayout());
		inputView.setPreferredSize(new Dimension(0, 200));
		this.add(inputView, BorderLayout.SOUTH);
		
		View toolBar = new View() {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(Color.GRAY);
				graphics.fillRect(0, 0, this.getWidth(), 1);
				graphics.fillRect(0, this.getHeight() - 1, this.getWidth(), 1);
			}
		};
		toolBar.setPreferredSize(new Dimension(0, 45));
		inputView.add(toolBar, BorderLayout.NORTH);
		
		this.createToolBar(toolBar);
		
		this.sendButton = new ButtonStyled("UI->Scene->ChatRoom::Send", GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				onSendClicked(e);
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.getForeground());
				graphics.drawRect(0, 0, this.getWidth(), this.getHeight());
			}
		};
		this.sendButton.setBounds(0, 160, 120, 30);
		this.sendButton.setFont(LanguageModel.getDefaultFont(16));
		inputView.add(this.sendButton);
		
		this.editorPane = new TextPane();	
		this.editorPane.setFont(LanguageModel.getDefaultFont());
		this.editorPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
		ScrollPane scrollPane = new ScrollPane(this.editorPane);
		scrollPane.setBounds(0, 0, 0, 160);
		inputView.add(scrollPane, BorderLayout.CENTER);
	}
	
	protected void createToolBar(View toolBar){
		int placeX = 10; 
		int placeY = 5;
		ButtonStyled imageButton = new ButtonStyled("", GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				JFileChooser fileChooser = new JFileChooser("./");
				fileChooser.setDialogTitle(LanguageModel.get("UI->Scene->NoticeEditor::ChoosePicture"));
    			FileFilter filter = new FileNameExtensionFilter(LanguageModel.get("UI->Scene->NoticeEditor::PictureFliter"), "jpg", "png");
    			fileChooser.addChoosableFileFilter(filter);
    			fileChooser.setAcceptAllFileFilterUsed(false);
    			fileChooser.setApproveButtonText(LanguageModel.get("UI->Scene->NoticeEditor::Approve"));
				if(fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) {
					ImageIcon image = new ImageIcon(fileChooser.getSelectedFile().getPath());
    				double percent = Math.min((double)ItemChat.PANE_WIDTH / image.getIconWidth() ,1);
    				editorPane.insertIcon(new ImageIcon(image.getImage().getScaledInstance((int)(percent * image.getIconWidth()), (int)(percent * image.getIconHeight()), 0)));
					editorPane.refresh();
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.defaultColor);
				graphics.fillRect(6, 6, this.getWidth() - 12, this.getHeight() - 12);
				graphics.setColor(Color.BLACK);
				graphics.drawRect(6, 6, this.getWidth() - 12, this.getHeight() - 12);
				graphics.setColor(GraphicsController.HINT_BLUE_COLOR);
				graphics.fillPolygon(new int[]{8, 8, 27, 27, 24, 19, 12}, new int[]{17, 27, 27, 20, 16, 20, 13}, 7);
				graphics.setColor(new Color(250, 230, 100));
				graphics.fillOval(18, 9, 5, 5);
		    }
		};
		imageButton.setBounds(placeX, placeY, 35, 35);
		imageButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->InsertPicture");
		toolBar.add(imageButton);
		
		placeX += 40; 
		this.addCutOff(toolBar, placeX, placeY);
		
		placeX += 5; 
		this.boldButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(Font.BOLD, 18));
				graphics.drawString("B", (this.getWidth() - 12)/ 2, (this.getHeight() + 16)/2);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isBold(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.boldButton.setBounds(placeX, placeY, 35, 35);
		this.boldButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->Bold");
		this.boldButton.setAction(this.boldAction);
		this.boldButton.refresh();
		toolBar.add(this.boldButton);
		
		placeX += 40;
		this.italicButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(new Font("MS Reference Sans Serif", Font.ITALIC, 19));
				graphics.drawString("I", (this.getWidth() - 8)/ 2, (this.getHeight() + 16)/2);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isItalic(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.italicButton.setBounds(placeX, placeY, 35, 35);
		this.italicButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->Italic");
		this.italicButton.setAction(this.italicAction);
		this.italicButton.refresh();
		toolBar.add(this.italicButton);
		
		placeX += 40;
		this.underlineButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(17));
				graphics.drawString("U", (this.getWidth() - 12)/ 2, (this.getHeight() + 14)/2);
				graphics.fillRect(this.getWidth()/3 + 1, (this.getHeight() + 16)/2, this.getWidth()/3, 1);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isUnderline(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.underlineButton.setBounds(placeX, placeY, 35, 35);
		this.underlineButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->Underline");
		this.underlineButton.setAction(this.underlineAction);
		this.underlineButton.refresh();
		toolBar.add(this.underlineButton);
		
		placeX += 40;
		this.strikeThroughButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(Font.PLAIN, 16));
				graphics.drawString("abc", (this.getWidth() - 26)/ 2, (this.getHeight() + 14)/2);
				graphics.fillRect(this.getWidth()/8, this.getHeight()/2 + 3, this.getWidth()*3/4 + 1, 1);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isStrikeThrough(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.strikeThroughButton.setBounds(placeX, placeY, 35, 35);
		this.strikeThroughButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->StrikeThrough");
		this.strikeThroughButton.setAction(this.strikeThroughAction);
		this.strikeThroughButton.refresh();
		toolBar.add(this.strikeThroughButton);
		
		placeX += 40;
		this.subscriptButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(Font.BOLD, 18));
				graphics.drawString("x", (this.getWidth() - 14)/ 2, (this.getHeight() + 14)/2);
				graphics.setColor(GraphicsController.HINT_BLUE_COLOR);
				graphics.setFont(LanguageModel.getDefaultFont(Font.BOLD, 9));
				graphics.drawString("2", (this.getWidth() + 8)/ 2, (this.getHeight() + 18)/2);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isSubscript(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.subscriptButton.setBounds(placeX, placeY, 35, 35);
		this.subscriptButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->Subscript");
		this.subscriptButton.setAction(this.subscriptAction);
		this.subscriptButton.refresh();
		toolBar.add(this.subscriptButton);
		
		placeX += 40;
		this.superscriptButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(Font.BOLD, 18));
				graphics.drawString("x", (this.getWidth() - 14)/ 2, (this.getHeight() + 14)/2);
				graphics.setColor(GraphicsController.HINT_BLUE_COLOR);
				graphics.setFont(LanguageModel.getDefaultFont(Font.BOLD, 9));
				graphics.drawString("2", (this.getWidth() + 8)/ 2, (this.getHeight() - 2)/2);
		    }
			@Override
			protected boolean lockEvent() {
				return StyleConstants.isSuperscript(((StyledEditorKit)editorPane.getEditorKit()).getInputAttributes());
			}
		};
		this.superscriptButton.setBounds(placeX, placeY, 35, 35);
		this.superscriptButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->Superscript");
		this.superscriptButton.setAction(this.superscriptAction);
		this.superscriptButton.refresh();
		toolBar.add(this.superscriptButton);
		
		placeX += 40; 
		this.addCutOff(toolBar, placeX, placeY);
		
		placeX += 5;
		this.fontColorButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setFont(LanguageModel.getDefaultFont(Font.PLAIN, 18));
				graphics.drawString("A", (this.getWidth() - 12)/ 2, (this.getHeight() + 8)/2);
				graphics.setColor(targetColor);
				graphics.fillRect(this.getWidth()/6 + 1, this.getHeight() - 11, this.getWidth()*2/3, 5);
		    }
		};
		this.fontColorButton.setBounds(placeX, placeY, 35, 35);
		this.fontColorButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->FontColor");
		this.fontColorButton.setAction(this.fontColorAction);
		this.fontColorButton.refresh();
		toolBar.add(this.fontColorButton);
		
		ButtonStyled colorChooseButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				Color returnColor = JColorChooser.showDialog(Scene_ChatRoom.this, "ÑÕÉ«Ñ¡ÔñÆ÷", targetColor);
				if(returnColor != null) {
					setFontColor(returnColor);
					fontColorAction.fontColorAction(editorPane, targetColor);
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.fillPolygon(new int[]{10, 6, 14}, new int[]{20, 16, 16}, 3);
		    }
		};
		colorChooseButton.setBounds(placeX + 35, placeY, 20, 35);
		colorChooseButton.setToolTipKey("UI->Window->NoticeEditor::ToolTip->FontColor");
		toolBar.add(colorChooseButton);
	}
	
	protected void addCutOff(View toolBar, int placeX, int placeY) {
		ViewStyled cutOffStyled = new ViewStyled();
		cutOffStyled.setBackground(GraphicsController.TOUCH_COLOR);
		cutOffStyled.setBounds(placeX, placeY + 3, 1, 29);
		toolBar.add(cutOffStyled);
	}
	
	protected void createPopUp() {
		this.menu = new JPopupMenu();
		this.withdrawItem = new JMenuItem(LanguageModel.get("UI->Scene->ChatRoom::WithdrawItem"));
		this.menu.add(withdrawItem);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.sendButton.setLocation(width - 150, this.sendButton.getY());
		super.setBounds(x, y, width, height);
	}
	
	protected void setFontColor(Color color) {
		this.targetColor = color;
		this.fontColorAction.setColor(this.targetColor);
		this.fontColorButton.repaint();
	}
	
	@Override
	public void update() {
		super.update();
		this.chatView.update();
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.chatView.refresh();
		this.withdrawItem.setText(LanguageModel.get("UI->Scene->ChatRoom::WithdrawItem"));
	}
	
	public void updateCallback(String jsonList) {
//		this.chatView.removeAll();
		LinkedList<DataChat> chats = DataChat.fromJsonList(jsonList);
//		for(DataChat chat : chats) {
//			this.chatView.add(new ItemChat(chat));
//		}
		for(int i = 0;i < chats.size(); i++) {
			DataChat chat = chats.get(i);
			if(i < this.chatView.getComponentCount()) {
				DataChat oldChat = ((ItemChat)this.chatView.getComponent(i)).getChat();
				if(oldChat.withdraw != chat.withdraw) {
					((ItemChat)this.chatView.getComponent(i)).setChat(chat);
					this.refresh();
				}
			}else {
				this.chatView.add(new ItemChat(chats.get(i)));
				this.messageScrollPane.getViewport().setViewPosition(new Point(0, this.chatView.getPreferredSize().height));
				this.refresh();
			}
		}
	}
	
	public void onSendClicked(MouseEvent e){
		this.editorPane.setText("");
		this.editorPane.setFont(LanguageModel.getDefaultFont());
	}
	
	public void showWithDrawMenu(int x, int y, DataChat chat){
		for (ActionListener listener : this.withdrawItem.getActionListeners()) {
			this.withdrawItem.removeActionListener(listener);
		}
		this.withdrawItem.addActionListener(e ->{
			this.onWithDraw(chat);
		});
		Window window = this.getSceneController().getWindow();
		this.menu.show(window, x - window.getX(), y - window.getY());
	}
	
	public void onWithDraw(DataChat chat) {
		
	}
	
}
