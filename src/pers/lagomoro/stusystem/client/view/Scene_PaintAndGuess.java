package pers.lagomoro.stusystem.client.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import pers.lagomoro.stusystem.client.controller.ClientController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.controller.module.ConnecterPaintAndGuess;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Scene;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.CanvasPanelPrinter;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.view.widget.ViewStyled;
import pers.lagomoro.stusystem.data.DataPaintAndGuess;
import pers.lagomoro.stusystem.data.DataUpdate;

@SuppressWarnings("serial")
public class Scene_PaintAndGuess extends Scene{
	
	protected CanvasPanelPrinter canvas;
	protected boolean locked;
	protected DataPaintAndGuess bank;
	protected int time = -1;
	
	protected ButtonStyled smoothButton;
	protected ViewStyled toolBarGame;
	
	protected ConnecterPaintAndGuess connecter;
	
	protected int checkClose = 0;
	
	public Scene_PaintAndGuess(String key){
		super(SceneController.SCENE_PAINT_AND_GUESS + "_" + key, null, key);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setBackground(GraphicsController.HOVER_COLOR);
		this.setLayout(new BorderLayout());
	}
	
	@Override
	protected void create(){
		super.create();
		this.createContent();
	}
	
	protected void createContent() {
		this.createToolBar();
		this.createCanvas();
		this.createMessageBar();
		this.createEmptyView();
	}
	
	protected void createToolBar() {
		ViewStyled toolBar = new ViewStyled();
		toolBar.setPreferredSize(new Dimension(0, 100));
		toolBar.setBackground(GraphicsController.EMPTY_COLOR);
		this.add(toolBar, BorderLayout.NORTH);
		
		this.createAct(toolBar, 50, 10);
		
		this.toolBarGame = new ViewStyled();
		this.toolBarGame.setBounds(0, 50, 800, 50);
		this.toolBarGame.setBackground(GraphicsController.EMPTY_COLOR);
		toolBar.add(this.toolBarGame);
		
		this.createFile(this.toolBarGame, 50, 10);
		this.createSize(this.toolBarGame, 135, 10);
		this.createColor(this.toolBarGame, 340, 10);
	}
	
	protected void createAct(ViewStyled toolBar, int actX, int actY) {
		int placeX = actX; 
		int placeY = actY;
		this.smoothButton = new ButtonStyled("UI->Scene->PaintAndGuess::ActSmoothness", GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected boolean lockEvent() {
				return canvas != null && canvas.getSmoothness();
			}
		};
		this.smoothButton.setBounds(placeX, placeY, 150, 35);
		this.smoothButton.addActionListener(e -> {
			canvas.setSmoothness(!canvas.getSmoothness());
		});
		this.smoothButton.refresh();
		toolBar.add(this.smoothButton);
	}

	protected void createFile(ViewStyled toolBar, int fileX, int fileY) {
		int placeX = fileX; 
		int placeY = fileY;
		ButtonStyled undoButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor((canvas != null && canvas.canUndo()) ? GraphicsController.HINT_BLUE_COLOR : GraphicsController.TOUCH_COLOR);
				int targetX = 5;
				int targetY = getHeight()/2;
				graphics.fillPolygon(new int[]{targetX, targetX + 8, targetX + 8, targetX + 24, targetX + 24, targetX + 8, targetX + 8},
						new int[]{targetY, targetY + 10, targetY + 6, targetY + 6, targetY - 6, targetY - 6, targetY - 10}, 7);
		    }
			@Override
			public void update() {
				super.update();
				if(canvas != null) this.setActive(canvas.canUndo());
			}
			@Override
			protected void onMouseLongPressed(MouseEvent e) {
				super.onMouseLongPressed(e);
				canvas.undoAll();
			}
		};
		undoButton.setBounds(placeX, placeY, 35, 35);
		undoButton.setToolTipKey("UI->Scene->PaintAndGuess::Undo");
		undoButton.addActionListener(e -> canvas.undo());
		undoButton.refresh();
		toolBar.add(undoButton);
		
		placeX += 40;
		ButtonStyled redoButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor((canvas != null && canvas.canRedo()) ? GraphicsController.HINT_BLUE_COLOR : GraphicsController.TOUCH_COLOR);
				int targetX = 30;
				int targetY = getHeight()/2;
				graphics.fillPolygon(new int[]{targetX, targetX - 8, targetX - 8, targetX - 24, targetX - 24, targetX - 8, targetX - 8},
						new int[]{targetY, targetY + 10, targetY + 6, targetY + 6, targetY - 6, targetY - 6, targetY - 10}, 7);
		    }
			@Override
			public void update() {
				super.update();
				if(canvas != null)this.setActive(canvas.canRedo());
				this.refresh();
			}
			@Override
			protected void onMouseLongPressed(MouseEvent e) {
				super.onMouseLongPressed(e);
				canvas.redoAll();
			}
		};
		redoButton.setBounds(placeX, placeY, 35, 35);
		redoButton.setToolTipKey("UI->Scene->PaintAndGuess::Redo");
		redoButton.addActionListener(e -> canvas.redo());
		redoButton.refresh();
		toolBar.add(redoButton);
	}
	
	protected void createSize(ViewStyled toolBar, int sizeX, int sizeY) {
		int placeX = sizeX; 
		int placeY = sizeY;
		int[] radiusList = {1,2,4,6,8};
		for (int i = 0; i < radiusList.length; i++) {
			int temp = radiusList[i];
			ButtonStyled button = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
				protected int radius = temp;
//				@Override
//				protected void onMouseClicked(MouseEvent e) {
//					super.onMouseClicked(e);
//					canvas.setPaintRadius(this.radius);
//				}
				@Override
				protected void paint(Graphics2D graphics) {
					super.paint(graphics);
					graphics.setColor(Color.BLACK);
					graphics.fillOval(this.getWidth()/2 - this.radius, this.getHeight()/2 - this.radius, this.radius*2, this.radius*2);
				}
				@Override
				protected boolean lockEvent() {
					return canvas != null && canvas.getPaintRadius() == this.radius;
				}
			};
			button.setBounds(placeX, placeY, 35, 35);
			button.addActionListener(e -> canvas.setPaintRadius(temp));
			button.refresh();
			toolBar.add(button);
			placeX += 40;
		}
	}
	
	protected void createColor(ViewStyled toolBar, int colorX, int colorY) {
		int placeX = colorX; 
		int placeY = colorY;
		View colorView = new View() {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				int radius = canvas == null ? 4 : canvas.getPaintRadius();
				graphics.setColor(canvas == null ? Color.BLACK : canvas.getPaintColor());
				graphics.fillRect(3, 3, this.getWidth() - 5, this.getHeight() - 6);		
				graphics.setColor(canvas == null ? Color.WHITE : this.getCursorColor(canvas.getPaintColor()));
				graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));	
				radius += 1;
				graphics.drawOval(this.getWidth()/2 - radius + 1, this.getHeight()/2 - radius, radius*2, radius*2);
				graphics.setColor(Color.BLACK);
				graphics.drawRect(0, 0, this.getWidth() + 1, this.getHeight());		
			}
			protected Color getCursorColor(Color color) {
				int r = color.getRed(), g = color.getGreen(), b = color.getBlue();
				if(r == g && g == b)
					return r < 125 ? Color.WHITE : Color.BLACK;
				else
					return g < 125 ? Color.WHITE : Color.BLACK;
			}
		};
		colorView.setBounds(placeX, placeY, 35, 35);
		colorView.refresh();
		toolBar.add(colorView);
		
		placeX += 35;
		ButtonStyled colorButton = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseDragged(MouseEvent e) {
				super.onMouseDragged(e);
				canvas.setPaintColor(this.getPaintColor(e.getX(), e.getY()));
			}
			@Override
			protected void onMousePressed(MouseEvent e) {
				super.onMousePressed(e);
				canvas.setPaintColor(this.getPaintColor(e.getX(), e.getY()));
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				for (int i = 0; i < this.getWidth(); i++) {
					graphics.setColor(getPaintColor(i, 0));
					graphics.fillRect(i, 0, 1, this.getHeight()/2);
					graphics.setColor(getPaintColor(i, this.getHeight()));
					graphics.fillRect(i, this.getHeight()/2, 1, this.getHeight()/2 + 1);
				}
			}
			protected Color getPaintColor(int x, int y){
				int sx = Math.max(0, Math.min(this.getWidth() , x));
				int sy = Math.max(0, Math.min(this.getHeight(), y));
				return sy < this.getHeight()/2 ? Color.getHSBColor(0.0f, 0.0f, (float)sx/this.getWidth()) : Color.getHSBColor((float)sx/this.getWidth(), 1.0f, 1.0f);
			}
		};
		colorButton.setBounds(placeX, placeY, 370, 35);
		colorButton.refresh();
		toolBar.add(colorButton);
	}
	
	protected void createCanvas() {
		this.canvas = new CanvasPanelPrinter();
		this.canvas.setPaintRadius(4);
		this.add(this.canvas, BorderLayout.CENTER);
	}
	
	protected void createMessageBar() {
		View messageBar = new View();
		messageBar.setPreferredSize(new Dimension(0, 50));
		this.add(messageBar, BorderLayout.SOUTH);
	}
	
	protected void createEmptyView() {
		View tempWest = new View();
		tempWest.setPreferredSize(new Dimension(50, 0));
		this.add(tempWest, BorderLayout.WEST);
		
		View tempEast = new View();
		tempEast.setPreferredSize(new Dimension(50, 0));
		this.add(tempEast, BorderLayout.EAST);
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
		this.canvas.setLocked(locked);
		this.toolBarGame.setVisible(!locked);
		this.smoothButton.setLocation(this.smoothButton.getX(), locked ? 55 : 10);
	}
	
	public void setConnecter(ConnecterPaintAndGuess connecter) {
		this.connecter = connecter;
	}
	
	@Override
	protected void onFocusLost(FocusEvent e) {
		super.onFocusLost(e);
		this.canvas.setVisible(false);
	}
	
	@Override
	protected void onFocusGain(FocusEvent e) {
		super.onFocusGain(e);
		this.canvas.setVisible(true);
	}
	
	@Override
	public void update() {
		super.update();
		this.checkClose = 0;
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.setColor(this.getBackground());
		graphics.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), 10, 10);
		graphics.fillRect(0, 0, this.getWidth()/2, this.getHeight());
		
		int placeY = this.locked ? 95 : 50;
		graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
		graphics.fillRect(50, placeY, this.getWidth()/2 - 75, 2);
		graphics.fillRect(50 + this.getWidth()/2 - 25, placeY, this.getWidth()/2 - 75, 2);
		graphics.setFont(LanguageModel.getDefaultFont(40));
		graphics.drawString(time < 0 ? "- -" : (time < 10 ? "0" + time : Integer.toString(time)), this.getWidth()/2 - 25 + 1, placeY - 5);
		graphics.setFont(LanguageModel.getDefaultFont(20));
		if(this.bank != null) {
			String hint_0 = String.format(LanguageModel.get("UI->Scene->PaintAndGuess::StringCount"), this.bank.item.length());
			if(this.locked) {
				graphics.drawString("" + (time < 55 ? hint_0 : "") + (time < 50 ? "¡¢" + this.bank.hint_1 : "") + (time < 15 ? "¡¢" + this.bank.hint_2 : ""), this.getWidth()/2 + 40, placeY - 10);
			}else {
				graphics.drawString(this.bank.item, this.getWidth()/2 + 40, placeY - 10);	
			}
			if(time < 5) {
				String text = String.format(LanguageModel.get("UI->Scene->PaintAndGuess::Answer"), this.bank.item);
				graphics.drawString(text, this.getWidth()/2 - 40 - (int)(graphics.getFont().getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth()), placeY - 10);					
			}
		}else {
			graphics.drawString(LanguageModel.get("UI->Scene->PaintAndGuess::Loading"), this.getWidth()/2 + 40, placeY - 10);
		}
	}
	
	public void commandActive(String command, String json){
		if(this.checkClose > 5) {
			this.connecter.release();
			this.bank = null;
		}
		this.checkClose += 1;
		switch(command) {
		case "guessSuccess" : break;
		case "guessFail" : break;
		case "startDraw" : {
			this.setLocked(false);
			this.bank = DataPaintAndGuess.fromJson(json);
			break;
		}
		case "startGuess" : {
			this.setLocked(true);
			this.bank = DataPaintAndGuess.fromJson(json);
			break;
		}
		case "timeUpdate" : {
			this.time = Integer.parseInt(json);
			break;
		}
		case "reset" : {
			this.bank = null;
			break;
		}
		case "redraw:AddPoint" : this.canvas.active("addPoint", json);break;
		case "redraw:LineStart" : this.canvas.active("lineStart", json);break;
		case "redraw:Undo" : this.canvas.active("undo", json);break;
		case "redraw:Redo" : this.canvas.active("redo", json);break;
		case "redraw:UndoAll" : this.canvas.active("undoAll", json);break;
		case "redraw:RedoAll" : this.canvas.active("redoAll", json);break;
		}
	}
	
	public void sendCommand(String command, String data) {
		if(this.bank != null && !this.locked) {
			this.connecter.sendCommand("draw:" + command, DataUpdate.parse(ClientController.getConnectorKey(this.connecter, ".PaintAndGuess"), data));
		}
	}
	
}
