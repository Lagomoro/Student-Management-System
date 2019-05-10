package pers.lagomoro.stusystem.client.view.module;

import java.awt.Color;

public class StyledPoint {

	public double x;
	public double y;
	public Color color;
	public double radius;
	public double angle;
	
	public StyledPoint(double x, double y, Color color, double radius, double angle) {
		this.x = x;
		this.y = y;
		this.color = color;
		this.radius = radius;
		this.angle = angle;
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRadius(double penLevel) {
		this.radius = penLevel;
	}
	
	public void setAngle(double angle) {
		this.angle = angle;
	}
	
	public StyledPoint copy() {
		return new StyledPoint(this.x, this.y, this.color, this.radius, this.angle);
	}

}
