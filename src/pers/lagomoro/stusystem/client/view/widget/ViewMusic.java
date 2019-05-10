package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.util.LinkedList;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.MusicController;
import pers.lagomoro.stusystem.client.model.LanguageModel;

@SuppressWarnings("serial")
public class ViewMusic extends View{
	
	protected double[] angles;
	protected int[] radius;
	protected int[][] widths;
	protected int[][] sAngles;
	protected int[][] aAngles;
	
	@Override
	protected void initialize() {
		super.initialize();
		this.angles  = new double[6];
		this.radius  = new int[]{20, 40, 70, 120, 140, 185};
		this.widths  = new int[][]{{10, 5},   {15},  {5},   {10,  5},   {20,  2},   {4}};
		this.sAngles = new int[][]{{10, 300}, {120}, {50},  {50,  200}, {20,  210}, {0}};
		this.aAngles = new int[][]{{100, 50}, {220}, {150}, {100, 150}, {100, 100}, {360}};
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		if(MusicController.getPlayStatus() > 0) {
			double[] drawInstance = MusicController.getDrawInstance();
			double[] dropInstance = MusicController.getDropInstance();
			graphics.setColor(GraphicsController.DARK_HOVER_COLOR);
			graphics.translate(0, this.getHeight());
			for(int i = 0; i < drawInstance.length; i++) {
				int nowX = (i + 1) * 8;
				int realX = this.getWidth() - nowX;
				int height = Math.min((int)drawInstance[i], this.getHeight() - 5);
				graphics.fillRect(realX, -height, 7, height);
				graphics.fillRect(realX, -(int)dropInstance[i] - 5, 7, 4);
			}
			int placeY = -180;
			graphics.setFont(LanguageModel.getDefaultFont(18));
			String musicName = MusicController.getNowMusic();
			int textWidth = (int)(graphics.getFont().getStringBounds(musicName, new FontRenderContext(new AffineTransform(), true, true)).getWidth());
			
			graphics.drawRect(200 - 2, placeY + 20 - 2, this.getWidth() - 400 + 4, 10 + 4);
			graphics.fillRect(200, placeY + 20, (int)((this.getWidth() - 400) * MusicController.getProcess()), 10);
			
			graphics.setColor(Color.LIGHT_GRAY);
			graphics.drawString(musicName, (this.getWidth() - textWidth)/2, placeY);
			
			LinkedList<String> lyrics = MusicController.getCurrentLyric();
			if (lyrics != null) {
				int drawY = placeY + 85 - (lyrics.size()/2 * 5) - lyrics.size() * 10;
				int i = 0;
				for(String text : lyrics) {
					textWidth = (int)(graphics.getFont().getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth());
					graphics.drawString(text, (this.getWidth() - textWidth)/2, drawY + i * 25);	
					i++;
				}
			}
			
			graphics.setFont(LanguageModel.getDefaultFont(16));
			String currentTime = this.getTimeString(MusicController.getCurrentTime());
			textWidth = (int)(graphics.getFont().getStringBounds(currentTime, new FontRenderContext(new AffineTransform(), true, true)).getWidth());
			graphics.drawString(currentTime, 200 - 10 - textWidth, placeY + 31);
			graphics.drawString(this.getTimeString(MusicController.getMaxTime()), this.getWidth() - 190, placeY + 31);
			
			placeY = -(190 + ((this.getHeight() - 190) /2));
			double[] playBeats = MusicController.getPlayBeats();
			double[] realBeats = MusicController.getRealBeats();
			double scale = this.getHeight() > 650 ? 1 : 0.9;
			for(int i = 0; i < this.angles.length; i++) {
				for(int j = 0; j < this.widths[i].length; j++) {
					graphics.setColor(new Color(210, 210, 210, Math.min((int)(realBeats[i] * 8), 255)));
					this.drawArc(graphics, this.getWidth()/2, placeY, (int) ((this.radius[i] + playBeats[i])*scale), (int)this.angles[i] + this.sAngles[i][j], this.aAngles[i][j] - (int)playBeats[5-i], (int) Math.round(this.widths[i][j]*scale), GraphicsController.STROKE_OUTSIDE);
				}
			}
			for(int i = 0; i < realBeats.length;i++) {
				graphics.setColor(new Color(210, 210, 210, Math.min((int)(realBeats[i] * 8), 255)));
				this.drawArc(graphics, this.getWidth()/2, placeY, (int) ((this.radius[4] + playBeats[4])*scale), (int)this.angles[4] + 180 + i * 5, 4, Math.min((int)realBeats[i]/2, 30), GraphicsController.STROKE_OUTSIDE);			
			}
			
			graphics.translate(0, 0);
		}
		
		super.paint(graphics);
	}
	
	public void drawArc(Graphics2D graphics, int ox, int oy, int radius, int startAngle, int arcAngle, int lineWidth, int drawMode) {
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - radius - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = radius * 2 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		graphics.drawArc(ox + sp, oy + sp, sd, sd, startAngle, arcAngle);
	}
	
	protected String getTimeString(long mills){
		int minute = (int)(mills / 60000);
		int second = (int)((mills / 1000) % 60);
		return (minute < 10 ? "0" : "") + minute + ":" + (second < 10 ? "0" : "") + second;
	}
	
	@Override
	public void update() {
		super.update();
		if(MusicController.getPlayStatus() > 0) {
			this.revalidate();
			this.repaint();
		}
		for(int i = 0; i < this.angles.length; i++) {
			this.angles[i] += (i%2 == i/2) ? ((double)(i + 1)/4) : -((double)(i + 1)/4);
			if(this.angles[i] > 360) this.angles[i] -= 360;
			if(this.angles[i] < 0) this.angles[i] += 360;
		}
	}

}
