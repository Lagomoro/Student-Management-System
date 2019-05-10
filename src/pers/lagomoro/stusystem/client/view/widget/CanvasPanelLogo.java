package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics2D;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;

@SuppressWarnings("serial")
public class CanvasPanelLogo extends CanvasPanel {
	
	protected int lineWidth;
	protected int scaleStrong;
	protected Color drawColor;
	
	public CanvasPanelLogo(int x, int y, int width, int height, int lineWidth, Color drawColor) {
		super(x, y, width, height);
		this.initialize(width, height, lineWidth, drawColor);
	}
	
	protected void initialize(int width, int height, int lineWidth, Color drawColor) {
		super.initialize();
		this.lineWidth = lineWidth;
		this.scaleStrong = Math.min(width, height) / 7;
		this.drawColor = drawColor;
	}
	
	@Override
	protected void createAdjuster() {
		super.createAdjuster();
		Adjuster_Double angleAdjuster = new Adjuster_Double("angleAdjuster", 0, 360, 540);
		angleAdjuster.setCyclic(true);
		this.adjusterController.addAdjuster(angleAdjuster);
		Adjuster_Double scaleAdjuster = new Adjuster_Double("scaleAdjuster", 0, Math.min(this.getWidth(), this.getHeight()) / 7, 60);
		scaleAdjuster.setCyclic(true);
		scaleAdjuster.setRoundMode(true);
		this.adjusterController.addAdjuster(scaleAdjuster);
		Adjuster_Double opacityAdjuster = new Adjuster_Double("opacityAdjuster", 30, 255, 100) {
			@Override
			protected void onValueChange() {
				drawColor = new Color(drawColor.getRed(), drawColor.getGreen(), drawColor.getBlue(), Math.min(255, this.getIntValue()));
			}
		};
		opacityAdjuster.setCyclic(true);
		opacityAdjuster.setRoundMode(true);
		this.adjusterController.addAdjuster(opacityAdjuster);
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		int placeX = this.getWidth()/2;
		int placeY = this.getHeight()/2;
		
		int radius = Math.min(placeX, placeY) *14 / 15;
		Color paintColor = new Color(this.drawColor.getRed(), this.drawColor.getGreen(), this.drawColor.getBlue(), Math.min(255, this.drawColor.getAlpha() + 40));
		
        this.setXORMode(graphics,new Color(0, 0, 0, 255));
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.getAdjusterInt("scaleAdjuster")                   , this.getAdjusterInt("angleAdjuster")      , this.lineWidth, GraphicsController.STROKE_INSIDE, paintColor);
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.scaleStrong + this.getAdjusterInt("scaleAdjuster"), 180 - this.getAdjusterInt("angleAdjuster"), this.lineWidth, GraphicsController.STROKE_INSIDE, paintColor);
        
        this.setPaintMode(graphics);
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.getAdjusterInt("scaleAdjuster")                   , this.getAdjusterInt("angleAdjuster")      , 1, GraphicsController.STROKE_MIDDLE, this.drawColor);
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.scaleStrong + this.getAdjusterInt("scaleAdjuster"), 180 - this.getAdjusterInt("angleAdjuster"), 1, GraphicsController.STROKE_MIDDLE, this.drawColor);
        
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.lineWidth*2 - this.getAdjusterInt("scaleAdjuster")                   , this.getAdjusterInt("angleAdjuster")      , 1, GraphicsController.STROKE_MIDDLE, this.drawColor);
        this.drawRegularTriangle(graphics, placeX, placeY, radius - this.lineWidth*2 - this.scaleStrong + this.getAdjusterInt("scaleAdjuster"), 180 - this.getAdjusterInt("angleAdjuster"), 1, GraphicsController.STROKE_MIDDLE, this.drawColor);
	}
	
}
