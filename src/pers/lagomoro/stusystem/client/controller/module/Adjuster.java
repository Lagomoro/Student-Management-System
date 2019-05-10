package pers.lagomoro.stusystem.client.controller.module;

import pers.lagomoro.stusystem.client.controller.AdjusterController;

public abstract class Adjuster {
	
	protected String key = AdjusterController.NORMAL;
	
	protected double tick = 0;
	protected boolean cyclic;
	protected boolean roundMode;
	protected boolean always;
	protected boolean cyclicTemp = true;
	
	public Adjuster(){

	}
	
	public Adjuster(String key){
		this.setKey(key);
	}
	
	public void update() {
		if(!this.isDestroy() && this.tick != 0) {
			this.changeValue();
			this.onValueChange();		
		}
	};

	protected void changeValue() {
		if(this.isCyclic()) {
			if(this.isRoundMode()) {
				this.addValue(this.cyclicTemp ? 1 : -1);
				if(this.isOutBoundEnd()) {
					this.cyclicTemp = false;
				} else if(this.isOutBoundStart()) {
					this.cyclicTemp = true;
				}
			} else {
				this.addValue(1);
				if(this.isOutBoundEnd()) {
					this.setBoundValue(false);
				}
			}
		} else {
			this.addValue(1);
			if(this.isOutBoundEnd()) {
				this.setBoundValue(true);
			}
		}
	};
	
	protected abstract void addValue(int i);

	protected abstract void onValueChange();
	
	public boolean isDestroy() {
		return this.isOutBoundEnd() && !this.isCyclic() && !this.isAlways();
	};

	protected abstract boolean isOutBoundStart();

	protected abstract boolean isOutBoundEnd();
	
	protected abstract void setBoundValue(boolean b);

	public abstract void reset(Adjuster adjuster);

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public boolean isCyclic() {
		return cyclic;
	}

	public void setCyclic(boolean cyclic) {
		this.cyclic = cyclic;
	}

	public boolean isRoundMode() {
		return roundMode;
	}

	public void setRoundMode(boolean roundMode) {
		this.roundMode = roundMode;
	}

	public boolean isAlways() {
		return always;
	}

	public void setAlways(boolean always) {
		this.always = always;
	}

}
