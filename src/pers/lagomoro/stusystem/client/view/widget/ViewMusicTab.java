package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.MusicController;

@SuppressWarnings("serial")
public class ViewMusicTab extends View{
	
	@Override
	protected void paint(Graphics2D graphics) {
		if(MusicController.getPlayStatus() > 0) {
			graphics.setColor(new Color(255, 255, 255, 150));
			if(MusicController.getPlayStatus() > 1) {
				graphics.fillRect(this.getWidth()*2/5, this.getHeight()*2/5, this.getHeight()/15, this.getHeight()/5);
				graphics.fillRect(this.getWidth()*8/15, this.getHeight()*2/5, this.getHeight()/15, this.getHeight()/5);
			}else {
				graphics.fillPolygon(new int[]{this.getWidth()*2/5 + 6, this.getWidth()*2/5 + 6, this.getWidth()*3/5 + 2}, new int[]{this.getHeight()*2/5, this.getHeight()*3/5, this.getHeight()/2}, 3);
			}
			double[] drawInstance = MusicController.getDrawInstance();
			for(int i = 0; i < drawInstance.length; i++) {
				double instace = Math.abs(Math.sin((int)drawInstance[i]) + Math.cos((int)drawInstance[i]));
				int height = (int) (instace * this.getWidth()/12);
				this.drawArc(graphics, this.getWidth()/2, this.getHeight()/2, this.getWidth()/4, i * 360 / drawInstance.length, 1, height, GraphicsController.STROKE_OUTSIDE);
			}
			
		}
		
		super.paint(graphics);
	}
	
	public void drawArc(Graphics2D graphics, int ox, int oy, int radius, int startAngle, int arcAngle, int lineWidth, int drawMode) {
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - radius - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = radius * 2 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		graphics.drawArc(ox + sp, oy + sp, sd, sd, startAngle, arcAngle);
	}
	
	@Override
	public void update() {
		super.update();
		if(MusicController.getPlayStatus() > 0) {
			this.revalidate();
			this.repaint();
		}
	}

}
