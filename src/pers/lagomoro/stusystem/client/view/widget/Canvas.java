package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.AdjusterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;
import pers.lagomoro.stusystem.client.view.module.Controllable;

@SuppressWarnings("serial")
public class Canvas extends java.awt.Canvas implements Controllable{
	
	protected Image image;
	protected Graphics2D graphics2d;
	protected AdjusterController adjusterController;
	
	public Canvas(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}

	public Canvas(int x, int y, int width, int height) {
		super();
		this.setBounds(x, y, width, height);
		this.initialize();
		this.create();
		this.refresh();
	}

	protected void initialize() {
		
	}
	
	protected void create() {
		this.createListener();
		this.createController();
		this.createAdjuster();
	}
	
	/**
	 * Create mouse events about the canvas.
	 **/
	protected void createListener() {
		MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e)  {onMouseEntered(e); }
            @Override
            public void mouseExited(MouseEvent e)   {onMouseExited(e);  }
            @Override
            public void mousePressed(MouseEvent e)  {onMousePressed(e); }
            @Override
            public void mouseReleased(MouseEvent e) {onMouseReleased(e);}
			@Override
            public void mouseClicked(MouseEvent e)  {switch(e.getButton()) { case MouseEvent.BUTTON1 : onMouseClicked(e); break; case MouseEvent.BUTTON3 : onMouseRightClicked(e); break;}}
			@Override
            public void mouseMoved(MouseEvent e)    {onMouseMoved(e);   }
			@Override
            public void mouseDragged(MouseEvent e)  {onMouseDragged(e); }
        };
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
	}

	protected void onMouseEntered(MouseEvent e) {}	
	protected void onMouseExited(MouseEvent e) {}
	
	protected void onMousePressed(MouseEvent e) {}	
	protected void onMouseReleased(MouseEvent e) {}
	protected void onMouseClicked(MouseEvent e) {}
	protected void onMouseRightClicked(MouseEvent e) {}
	
	protected void onMouseMoved(MouseEvent e) {}
	protected void onMouseDragged(MouseEvent e) {}
	
	protected void createController() {
		this.adjusterController = new AdjusterController();
	}
	
	protected void createAdjuster() {
		
	}	
	
	public void addAdjuster(Adjuster adjuster) {
		this.adjusterController.addAdjuster(adjuster);
	}
	
	public Adjuster getAdjuster(String key) {
		return this.adjusterController.getAdjuster(key);
	}
	
	protected int getAdjusterInt(String key) {
		return ((Adjuster_Double)this.getAdjuster(key)).getIntValue();
	}
	
	public void update() {
		this.adjusterController.update();
		this.repaint();
	}
	
	public void refresh() {
		this.revalidate();
		this.repaint();
	}
	
	protected Graphics2D getGraphics2D() {
		if(this.image == null) {
			this.image = createImage(this.getWidth(), this.getHeight());
		}
		if(this.graphics2d == null) {
			this.graphics2d = (Graphics2D)this.image.getGraphics();
			GraphicsController.setHint(this.graphics2d);
		}
		return this.graphics2d;
	}
	
	@Override
	public void update(Graphics g) {
		paint(g);
	}
	
	public void paint(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		GraphicsController.setHint(graphics);
		graphics.drawImage(this.image, 0, 0, this);
		this.paint(graphics);
    }
	
	protected void paint(Graphics2D graphics) {
		
	}
	
	public void clear() {
		this.getGraphics2D().clearRect(0, 0, this.getWidth(), this.getHeight());
	}
	
	public void clearRect(int x, int y, int width, int height) {
		this.getGraphics2D().clearRect(x, y ,width, height);
	}
	
	public void setXORMode(Color alt) {
		this.getGraphics2D().setXORMode(alt);
	}
	
	public void setPaintMode() {
		this.getGraphics2D().setPaintMode();
	}
	
	public void drawCircle(int ox, int oy, int radius, int lineWidth, int drawMode, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - radius - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = radius * 2 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		graphics.drawOval(ox + sp, oy + sp, sd, sd);
	}
	
	public void fillCircle(int ox, int oy, int radius, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.fillOval(ox - radius, oy - radius, radius * 2, radius * 2);
	}
	
	public void drawArc(int ox, int oy, int radius, int startAngle, int arcAngle, int lineWidth, int drawMode, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - radius - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = radius * 2 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		graphics.drawArc(ox + sp, oy + sp, sd, sd, startAngle, arcAngle);
	}

	public void fillArc(int ox, int oy, int radius, int startAngle, int arcAngle, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.fillArc(ox - radius, oy - radius, radius * 2, radius * 2, startAngle, arcAngle);
	}

	public void drawRect(int x, int y, int width, int height, int lineWidth, int drawMode, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = 0 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		graphics.drawRect(x + sp, y + sp, width + sd, height + sd);
	}
	
	public void fillRect(int x, int y, int width, int height, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.fillRect(x, y, width, height);
	}
	
	public void drawRoundRect(int x, int y, int width, int height, int radius, int lineWidth, int drawMode, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sp = 0 - (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		int sd = 0 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth);
		int sr = radius*2 + (Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * lineWidth/2);
		graphics.drawRoundRect(x + sp, y + sp, width + sd, height + sd, sr, sr);
	}

	public void fillRoundRect(int x, int y, int width, int height, int radius, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.drawRoundRect(x, y, width, height, radius*2, radius*2);		
	}
	
	public void drawPolygon(int xPoints[], int yPoints[], int nPoints, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.drawPolygon(xPoints, yPoints, nPoints);
	}
	
	public void fillPolygon(int xPoints[], int yPoints[], int nPoints, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.fillPolygon(xPoints, yPoints, nPoints);
	}
	
	protected int[] polarToRect(int r, int s) {
		return new int[]{(int) (r * Math.cos(Math.PI*s/180)), (int) (r * Math.sin(Math.PI*s/180))};
	}
	
	public void drawRegularTriangle(int ox, int oy, int distance, int angle, int lineWidth, int drawMode, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		graphics.setStroke(new BasicStroke(lineWidth, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
		int sd = distance + (int)(Math.max(GraphicsController.STROKE_INSIDE, Math.min(GraphicsController.STROKE_OUTSIDE, drawMode)) * (lineWidth/Math.cos(60/180*Math.PI)));
		int[] dot1 = polarToRect(sd, angle);
		int[] dot2 = polarToRect(sd, angle + 120);
		int[] dot3 = polarToRect(sd, angle + 240);
		graphics.drawPolygon(new int[] {ox + dot1[0], ox + dot2[0], ox + dot3[0]}, new int[] {oy + dot1[1], oy + dot2[1], oy + dot3[1]},3);
	}
	
	public void fillRegularTriangle(int ox, int oy, int distance, int angle, Color color) {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(color);
		int[] dot1 = polarToRect(distance, angle);
		int[] dot2 = polarToRect(distance, angle + 120);
		int[] dot3 = polarToRect(distance, angle + 240);
		graphics.fillPolygon(new int[] {ox + dot1[0], ox + dot2[0], ox + dot3[0]}, new int[] {oy + dot1[1], oy + dot2[1], oy + dot3[1]},3);
	}
	
	public void drawImage(Image image, int x, int y, int width, int height, int target) {
		Graphics2D graphics = this.getGraphics2D();
		int realX = x - (target - 1) % 3 * width / 2;
		int realY = y - (9 - target) / 3 * height / 2;
		graphics.drawImage(image, realX, realY, width, height, this);
	}
	
}
