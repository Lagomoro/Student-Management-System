package pers.lagomoro.stusystem.client.controller.module;

import java.awt.Color;

public class Adjuster_Color extends Adjuster{
	
	private double[] value = new double[4];
	private Color start;
	private Color end;

	public Adjuster_Color(){
		super();
	}
	
	public Adjuster_Color(String key){
		super(key);
	}
	
	public Adjuster_Color(String key, Color start, Color end, double tick) {
		super(key);
		this.initialize(start, end, tick);
	}

	protected void initialize(Color start, Color end, double tick) {
		this.setValue(start);
		this.start = start;
		this.end = end;
		this.tick = tick;
	}

	@Override
	protected void addValue(int i) {
		this.setValue(this.value[0] + i * ((this.end.getRed() - this.start.getRed()) / this.tick),
				this.value[1] + i * ((this.end.getGreen() - this.start.getGreen()) / this.tick),
				this.value[2] + i * ((this.end.getBlue() - this.start.getBlue()) / this.tick),
				this.value[3] + i * ((this.end.getAlpha() - this.start.getAlpha()) / this.tick));
	}

	@Override
	protected void onValueChange() {
		
	}
	
	@Override
	protected boolean isOutBoundStart() {
		return (this.end.getRed() > this.start.getRed() ? (this.value[0] < this.start.getRed()) : (this.value[0] > this.start.getRed())) ||
				(this.end.getGreen() > this.start.getGreen() ? (this.value[1] < this.start.getGreen()) : (this.value[1] > this.start.getGreen())) ||
				(this.end.getBlue() > this.start.getBlue() ? (this.value[2] < this.start.getBlue()) : (this.value[2] > this.start.getBlue())) ||
				(this.end.getAlpha() > this.start.getAlpha() ? (this.value[3] < this.start.getAlpha()) : (this.value[3] > this.start.getAlpha()));
	}

	@Override
	protected boolean isOutBoundEnd() {
		return (this.end.getRed() > this.start.getRed() ? (this.value[0] > this.end.getRed()) : (this.value[0] < this.end.getRed())) ||
				(this.end.getGreen() > this.start.getGreen() ? (this.value[1] > this.end.getGreen()) : (this.value[1] < this.end.getGreen())) ||
				(this.end.getBlue() > this.start.getBlue() ? (this.value[2] > this.end.getBlue()) : (this.value[2] < this.end.getBlue())) ||
				(this.end.getAlpha() > this.start.getAlpha() ? (this.value[3] > this.end.getAlpha()) : (this.value[3] < this.end.getAlpha()));
	}

//	@Override
//	protected boolean isOutBoundStart() {
//		return (this.end.getRed() > this.start.getRed() ? (this.value[0] <= this.start.getRed()) : (this.value[0] >= this.start.getRed())) &&
//				(this.end.getGreen() > this.start.getGreen() ? (this.value[1] <= this.start.getGreen()) : (this.value[1] >= this.start.getGreen())) &&
//				(this.end.getBlue() > this.start.getBlue() ? (this.value[2] <= this.start.getBlue()) : (this.value[2] >= this.start.getBlue())) &&
//				(this.end.getAlpha() > this.start.getAlpha() ? (this.value[3] <= this.start.getAlpha()) : (this.value[3] >= this.start.getAlpha()));
//	}
//
//	@Override
//	protected boolean isOutBoundEnd() {
//		return (this.end.getRed() > this.start.getRed() ? (this.value[0] >= this.end.getRed()) : (this.value[0] <= this.end.getRed())) &&
//				(this.end.getGreen() > this.start.getGreen() ? (this.value[1] >= this.end.getGreen()) : (this.value[1] <= this.end.getGreen())) &&
//				(this.end.getBlue() > this.start.getBlue() ? (this.value[2] >= this.end.getBlue()) : (this.value[2] <= this.end.getBlue())) &&
//				(this.end.getAlpha() > this.start.getAlpha() ? (this.value[3] >= this.end.getAlpha()) : (this.value[3] <= this.end.getAlpha()));
//	}

	@Override
	protected void setBoundValue(boolean b) {
		if(!b) this.setValue(start); else this.setValue(end);
	}

	@Override
	public void reset(Adjuster adjuster) {
		((Adjuster_Color)adjuster).reset(this.start, this.end, this.tick);
	}
	
	public void reset(Color start, Color end, double tick) {
		this.start = start;
		this.end = end;
		this.tick = tick;
	}

	public Color getValue() {
		return new Color(this.scaleValue(this.value[0]), this.scaleValue(this.value[1]), this.scaleValue(this.value[2]), this.scaleValue(this.value[3]));
	}
	
	public int scaleValue(double input) {
		return (int)(Math.max(0, Math.min(255, input)));
	}

	public void setValue(Color value) {
		this.value[0] = value.getRed();
		this.value[1] = value.getGreen();
		this.value[2] = value.getBlue();
		this.value[3] = value.getAlpha();
	}
	
	public void setValue(double r, double g, double b, double a) {
		this.value[0] = r;
		this.value[1] = g;
		this.value[2] = b;
		this.value[3] = a;
	}

}
