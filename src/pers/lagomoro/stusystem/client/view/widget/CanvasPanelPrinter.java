package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import jpen.PLevel;
import jpen.PLevelEvent;
import jpen.event.PenAdapter;
import jpen.owner.multiAwt.AwtPenToolkit;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.view.Scene_PaintAndGuess;
import pers.lagomoro.stusystem.client.view.module.StyledPoint;

@SuppressWarnings("serial")
public class CanvasPanelPrinter extends CanvasPanel {

	protected StyledPoint lastPoint;
	protected Vector<Vector<StyledPoint>> lines;
	protected Vector<Vector<StyledPoint>> undoList;
	protected Graphics2D graphics2d;
	
	protected Color paintColor;
	protected int paintRadius;
	
	protected boolean locked;
	
	protected Ellipse2D.Double stroke;
	
	protected boolean actSmoothness = true;
	protected double penLevel = -1;
	
	public CanvasPanelPrinter() {
		
	}

	public CanvasPanelPrinter(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setBackground(Color.WHITE);
		this.setBounds(0, 0, 200, 200);
		this.lines = new Vector<Vector<StyledPoint>>();
		this.undoList = new Vector<Vector<StyledPoint>>();
		this.paintColor = Color.BLACK;
		this.paintRadius = 4;
		this.stroke = new Ellipse2D.Double();
		AwtPenToolkit.addPenListener(this, new PenAdapter() {
			@Override
			public void penLevelEvent(PLevelEvent e) {
				switch(e.pen.getKind().getType()) {
				case STYLUS : penLevel = e.pen.getLevelValue(PLevel.Type.PRESSURE);break;
				default: penLevel = -1;break;
				}
			}
	    });
	}
	
	@Override
	protected void onMouseReleased(MouseEvent e) {
		super.onMouseReleased(e);
		if(this.locked) return;
		if(this.lastPoint != null) {
			Graphics2D graphics = this.getGraphics2D();
			StyledPoint tempPoint = this.lastPoint.copy();
			tempPoint.setLocation(e.getX(), e.getY());
			tempPoint.setAngle(Math.atan2(tempPoint.y - this.lastPoint.y, tempPoint.x - this.lastPoint.x));
			tempPoint.setRadius(this.paintRadius *(penLevel < 0 ? 1 : penLevel));
			drawLineContact(graphics, this.lastPoint, tempPoint);
			this.lines.get(this.lines.size() - 1).add(tempPoint);
			this.sendCommand("AddPoint", fromPoint(tempPoint));
			this.lastPoint = null;
		}
		this.refresh();
	}
	
	@Override
	protected void onMouseDragged(MouseEvent e) {
		super.onMouseDragged(e);
		if(this.locked) return;
		if(this.lastPoint != null) {
			if(Math.hypot(e.getX() - this.lastPoint.x, e.getY() - this.lastPoint.y) > 4) {
				Graphics2D graphics = this.getGraphics2D();
				StyledPoint tempPoint = this.lastPoint.copy();
				tempPoint.setLocation(e.getX(), e.getY());
				tempPoint.setAngle(Math.atan2(tempPoint.y - this.lastPoint.y, tempPoint.x - this.lastPoint.x));
				tempPoint.setRadius(this.paintRadius *(penLevel < 0 ? 1 : penLevel));
				drawLineContact(graphics, this.lastPoint, tempPoint);
				this.lastPoint = tempPoint;
				this.lines.get(this.lines.size() - 1).add(tempPoint);
				this.sendCommand("AddPoint", fromPoint(tempPoint));
			}
		}else {
			this.lines.add(new Vector<StyledPoint>());
			this.undoList.clear();
			this.lastPoint = new StyledPoint(e.getX(), e.getY(), new Color(this.paintColor.getRGB()), this.paintRadius *(penLevel < 0 ? 1 : penLevel), 0);
			this.lines.get(this.lines.size() - 1).add(this.lastPoint.copy());
			this.sendCommand("LineStart", fromPoint(this.lastPoint));
		}
		this.refresh();
	}
	
	@Override
	public void update(){
		
	}
	
	public void undoAll(){
		while(this.lines.size() > 0) {
			this.undoList.add(this.lines.remove(this.lines.size() - 1));
		}
		this.sendCommand("UndoAll", "0");
		this.repaintAll();
		this.refresh();
	}
	
	public void redoAll(){
		while(this.undoList.size() > 0) {
			this.lines.add(this.undoList.remove(this.undoList.size() - 1));
		}
		this.sendCommand("RedoAll", "0");
		this.repaintAll();
		this.refresh();
	}
	
	public void undo(){
		if(this.lines.size() > 0) {
			this.undoList.add(this.lines.remove(this.lines.size() - 1));
			this.sendCommand("Undo", "0");
			this.repaintAll();
			this.refresh();
		}
	}
	
	public void redo(){
		if(this.undoList.size() > 0) {
			this.lines.add(this.undoList.remove(this.undoList.size() - 1));
			this.sendCommand("Redo", "0");
			this.repaintAll();
			this.refresh();
		}
	}
	
	public boolean canUndo() {
		return this.lines != null && this.lines.size() > 0;
	}
	
	public boolean canRedo() {
		return this.undoList != null && this.undoList.size() > 0;
	}
	
	public void setPaintColor(Color paintColor) {
		this.paintColor = paintColor;
	}
	public void setPaintRadius(int paintRadius) {
		this.paintRadius = paintRadius;
		paintRadius += 1;
        Image image = new BufferedImage(144, 144, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        GraphicsController.setHint(graphics);
        graphics.setColor(Color.BLACK);
        graphics.setStroke(new BasicStroke(4, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
        graphics.drawOval(72 - paintRadius*4, 72 - paintRadius*4, paintRadius * 8, paintRadius * 8);
        Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(16, 16), "");
        this.setCursor(cursor);
        
	}
	public void setSmoothness(boolean actSmoothness) {
		this.actSmoothness = actSmoothness;
		this.repaintAll();
		this.refresh();
	}
	
	public Color getPaintColor() {
		return paintColor;
	}
	public int getPaintRadius() {
		return paintRadius;
	}
	public boolean getSmoothness() {
		return this.actSmoothness;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
		this.getGraphics2D();
	}
	
	public Graphics2D getGraphics2D() {
		if(this.image == null) {
			this.image = createImage(this.getWidth(), this.getHeight());
		}
		if(this.image != null && this.graphics2d == null) {
			this.graphics2d = (Graphics2D)this.image.getGraphics();
			this.graphics2d.setColor(this.getBackground());
			this.graphics2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
		if(this.graphics2d != null) GraphicsController.setHint(this.graphics2d);
		return this.graphics2d;
	}
	
	protected void drawLineContact(Graphics2D graphics, StyledPoint p1, StyledPoint p2) {
		double distance = Math.hypot(p2.x - p1.x, p2.y - p1.y);
		
		Point2D.Double tempPoint = new Point2D.Double(0, 0);
		double realAngel_1 = p1.angle + ((p1.angle < 0 && p2.angle > 0 && Math.abs(p2.angle - p1.angle) > Math.PI) ? Math.PI*2 : 0);
		double realAngel_2 = p2.angle + ((p2.angle < 0 && p1.angle > 0 && Math.abs(p2.angle - p1.angle) > Math.PI) ? Math.PI*2 : 0);
		boolean calculate = !(Math.abs(p1.angle) < 0.0001 || Math.abs(realAngel_1 - realAngel_2) > Math.PI/2);
		calculate = this.actSmoothness ? calculate : this.actSmoothness;
		double tempAngle = realAngel_1;
		double tempRadius = p1.radius;
		graphics.setColor(p1.color);
		for(int i = 0;i < distance;i++) {
			double tempX = 0;
			double tempY = 0;
			double paintX = 0;
			double paintY = 0;
			if (calculate) {
				tempX = tempPoint.x + Math.cos(tempAngle);
				tempY = tempPoint.y + Math.sin(tempAngle);
				double row = Math.hypot(tempX, tempY);
				double sita = Math.atan2(tempY, tempX) + (realAngel_2 - realAngel_1)/2;
				paintX = row * Math.cos(sita);
				paintY = row * Math.sin(sita);
			}else{
				tempX = tempPoint.x + (p2.x - p1.x)/distance;
				tempY = tempPoint.y + (p2.y - p1.y)/distance;
				paintX = tempX;
				paintY = tempY;
			}
			
			double realX = p1.x + paintX;
			double realY = p1.y + paintY;
			stroke.setFrame(realX - tempRadius, realY - tempRadius, tempRadius*2, tempRadius*2);
			graphics.fill(stroke);
			tempPoint.setLocation(tempX, tempY);
			tempAngle += (realAngel_2 - tempAngle)/(distance - i);
			tempRadius += (double)(p2.radius - p1.radius)/distance;
		}
		stroke.setFrame(p2.x - p2.radius, p2.y - p2.radius, p2.radius*2, p2.radius*2);
		graphics.fill(stroke);
	}
	
	public void setLocked(boolean locked) {
		this.locked = locked;
		this.lastPoint = null;
		this.lines.clear();
		this.undoList.clear();
		this.repaintAll();
		this.refresh();
	}
	
	public void setLines(Vector<Vector<StyledPoint>> line) {
		this.lines = line;
		this.undoList.clear();
		this.repaintAll();
		this.refresh();
	}
	
	public void active(String command, String data){
		switch(command) {
		case "addPoint" : {
			StyledPoint point = toPoint(data);
			this.lines.get(this.lines.size() - 1).add(point);
			this.drawLineContact(this.getGraphics2D(), this.lastPoint, point);
			this.lastPoint = point;
			this.refresh();
			break;
		}
		case "lineStart" : {
			this.lines.add(new Vector<StyledPoint>());
			this.undoList.clear();
			StyledPoint point = toPoint(data);
			this.lines.get(this.lines.size() - 1).add(point);
			this.lastPoint = point;
			this.refresh();
			break;
		}
		case "undo" : this.undo();break;
		case "redo" : this.redo();break;
		case "undoAll" : this.undoAll();break;
		case "redoAll" : this.redoAll();break;
		}
	}
	
	public StyledPoint toPoint(String data) {
		String[] pointData = data.split(",");
		return new StyledPoint(Double.parseDouble(pointData[0]), Double.parseDouble(pointData[1]), new Color(Integer.parseInt(pointData[2])), Double.parseDouble(pointData[3]), Double.parseDouble(pointData[4]));
	}
	
	public String fromPoint(StyledPoint point) {
		return point.x + "," + point.y + "," + point.color.getRGB() + "," + point.radius + "," + point.angle;
	}
	
	public void sendCommand(String command, String data) {
		((Scene_PaintAndGuess)this.getParent()).sendCommand(command, data);
	};
	
	public Vector<Vector<StyledPoint>> getLines() {
		return lines;
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.drawImage(this.image, 0, 0, this);
	}
	
	protected void repaintAll() {
		Graphics2D graphics = this.getGraphics2D();
		graphics.setColor(this.getBackground());
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		graphics.setColor(Color.BLUE);
		for(Vector<StyledPoint> line : this.lines) {
			if(line.size() > 1) {
				for (int i = 0; i < line.size() - 1; i++) {
					this.drawLineContact(graphics, line.get(i), line.get(i + 1));
				}
			}
			if(line.size() > 0) {
				this.drawLineContact(graphics, line.get(0), line.get(0));
			}
		}
	}
	
}
