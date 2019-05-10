package pers.lagomoro.stusystem.client.view.component;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.MusicController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.TextViewStyled;
import pers.lagomoro.stusystem.client.view.widget.ViewStyled;

@SuppressWarnings("serial")
public class WindowTitled extends Window{

	protected String textKey;
	protected TextViewStyled titleTextView;
	
	protected int startX;
	protected int startY;

	public WindowTitled(){
		super();
	}
	
	public WindowTitled(String key){
		super(key);
	}
	
	public WindowTitled(String key, String titleKey){
		super(key);
		this.setTextKey(titleKey);
	}

	@Override
	protected void create() {
		super.create();
		this.createTitle();
	}

	protected void createTitle() {
		ButtonStyled minimizeButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				WindowController.applyWindowIconify(getKey());
			}
			
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3, 2);
		    }
		};
		minimizeButton.setBounds(getWidth() - 90, 15, 30, 30);
		minimizeButton.setShade(5);
		minimizeButton.setRadius(15);
		minimizeButton.setToolTipKey("UI->Window::ToolTip->Minimize");
		this.add(minimizeButton);
		
		ButtonStyled closeButton = new ButtonStyled(null, GraphicsController.WARNING_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				WindowController.applyWindowClose(getKey());
			}
			
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				graphics.drawLine(this.getWidth()/3+1, this.getHeight()/3+1, this.getWidth()*2/3, this.getHeight()*2/3);
				graphics.drawLine(this.getWidth()/3+1, this.getHeight()*2/3, this.getWidth()*2/3, this.getHeight()/3+1);
		    }
		};
		closeButton.setBounds(getWidth() - 50, 15, 30, 30);
		closeButton.setShade(5);
		closeButton.setRadius(15);
		closeButton.setToolTipKey("UI->Window::ToolTip->Close");
		this.add(closeButton);
		
		titleTextView = new TextViewStyled(null) {
			@Override
			protected void onMousePressed(MouseEvent e) {
				super.onMousePressed(e);
				startX = e.getXOnScreen();
                startY = e.getYOnScreen();
			}
			
	        @Override
	        protected void onMouseDragged(MouseEvent e) {
	        	super.onMouseDragged(e);
	        	int deltaX = e.getXOnScreen() - startX;
	        	int deltaY = e.getYOnScreen() - startY;
	        	WindowController.applyWindowMoving(getKey(), deltaX, deltaY);
	        	startX = e.getXOnScreen();
                startY = e.getYOnScreen();
	        }
	        
	        @Override
	        public void paint(Graphics2D graphics) {
	        	super.paint(graphics);
	        	if(MusicController.getPlayStatus() > 0) {
		    		graphics.setColor(MusicController.getPlayColor());
		    		graphics.fillRoundRect(this.shade, this.shade, this.getWidth() - this.shade * 2, this.getHeight() - this.shade * 2, this.radius * 2, this.radius * 2);
	        	}
	        }
	        
	    	@Override
	    	public void update() {
	    		super.update();
	    		if(MusicController.getPlayStatus() > 0) {
	    			this.revalidate();
	    			this.repaint();
	    		}
	    	}
	        
		};
		titleTextView.setBounds(65, 15, getWidth() - 170, 30);
		titleTextView.setShade(5);
		titleTextView.setRadius(15);
		this.add(titleTextView);
		
		ViewStyled logoView = new ViewStyled() {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.drawString("M", 11, 27);
			}
		};
		logoView.setBounds(20, 15, 30, 30);
		logoView.setShade(5);
		logoView.setRadius(15);
		this.add(logoView);
		
	}
	
	public void setTextKey(String textKey) {
		this.textKey = textKey;
		this.titleTextView.setTextKey(textKey);
	}
	
}
