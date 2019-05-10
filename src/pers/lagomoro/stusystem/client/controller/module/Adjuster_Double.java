package pers.lagomoro.stusystem.client.controller.module;

public class Adjuster_Double extends Adjuster{
	
	private double value;
	private double start;
	private double end;

	public Adjuster_Double(){
		super();
	}
	
	public Adjuster_Double(String key){
		super(key);
	}
	
	public Adjuster_Double(String key, double start, double end, double tick) {
		super(key);
		this.initialize(start, end, tick);
	}

	protected void initialize(double start, double end, double tick) {
		this.setValue(start);
		this.start = start;
		this.end = end;
		this.tick = tick;
	}

	@Override
	protected void addValue(int i) {
		this.setValue(this.getValue() + i * ((this.end - this.start) / this.tick));
	}

	@Override
	protected void onValueChange() {
		
	}

	@Override
	protected boolean isOutBoundStart() {
		return this.end > this.start ? (this.getValue() <= this.start) : (this.getValue() >= this.start);
	}

	@Override
	protected boolean isOutBoundEnd() {
		return this.end > this.start ? (this.getValue() >= this.end) : (this.getValue() <= this.end);
	}

	@Override
	protected void setBoundValue(boolean b) {
		if(!b) this.setValue(start); else this.setValue(end);
	}

	@Override
	public void reset(Adjuster adjuster) {
		((Adjuster_Double)adjuster).reset(this.start,this.end,this.tick);
	}
	
	public void reset(double start, double end, double tick) {
		this.start = start;
		this.end = end;
		this.tick = tick;
	}

	public double getValue() {
		return this.value;
	}
	
	public int getIntValue() {
		return (int)this.value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
