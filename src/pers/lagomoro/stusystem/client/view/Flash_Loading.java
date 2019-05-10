package pers.lagomoro.stusystem.client.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.ImageModel;
import pers.lagomoro.stusystem.client.view.component.Flash;
import pers.lagomoro.stusystem.client.view.widget.CanvasPanelLogo;

@SuppressWarnings("serial")
public class Flash_Loading extends Flash {
	
	protected int startX;
	protected int startY;
	
	public Flash_Loading(){
		super(WindowController.FLASH_LOADING);
	}
	
	@Override
    protected void initialize(){
		super.initialize();
		this.setSize(300, 300);
	}
	
	@Override
	protected void create() {
		super.create();
		this.createCanvas();
	}

	private void createCanvas() {
		this.add(new CanvasPanelLogo(0, 0, 300, 300, 15, Color.WHITE) {
			
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
	        	WindowController.applyWindowMoving(key, deltaX, deltaY);
	        	startX = e.getXOnScreen();
                startY = e.getYOnScreen();
	        }

			@Override
			protected void paint(Graphics2D graphics) {
				int placeX = this.getWidth()/2;
				int placeY = this.getHeight()/2;
				int radius = Math.min(placeX, placeY) - 10;
				int shadeAccurcy = 5;
				Color shadeColor = new Color(0, 0, 0, 60/shadeAccurcy);
				Color roundColor = new Color(0, 0, 0, 60/shadeAccurcy);
				int logoRadius = radius / 3 + (this.scaleStrong / 2 - Math.abs(this.getAdjusterInt("scaleAdjuster") - this.scaleStrong / 2));
				for(int i = 0; i < shadeAccurcy; i++) {
			        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.getAdjusterInt("scaleAdjuster")                   , this.getAdjusterInt("angleAdjuster")      , shadeAccurcy - i, GraphicsController.STROKE_OUTSIDE, shadeColor);
			        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.scaleStrong + this.getAdjusterInt("scaleAdjuster"), 180 - this.getAdjusterInt("angleAdjuster"), shadeAccurcy - i, GraphicsController.STROKE_OUTSIDE, shadeColor);
			        
			        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.lineWidth*2 - this.getAdjusterInt("scaleAdjuster")                   , this.getAdjusterInt("angleAdjuster")      , shadeAccurcy - i, GraphicsController.STROKE_INSIDE, shadeColor);
			        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.lineWidth*2 - this.scaleStrong + this.getAdjusterInt("scaleAdjuster"), 180 - this.getAdjusterInt("angleAdjuster"), shadeAccurcy - i, GraphicsController.STROKE_INSIDE, shadeColor);				
				}
				for(int i = 0; i < shadeAccurcy; i++) {
					this.fillCircle(graphics, placeX, placeY, logoRadius/2 + i, roundColor);
				}
				this.drawImage(graphics, ImageModel.getLogoImage(), 150, 150, logoRadius, logoRadius, ImageModel.ANCHOR_MIDDLE);
				super.paint(graphics);
			}
		});
	}
	
}
