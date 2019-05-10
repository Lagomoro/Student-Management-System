package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

import pers.lagomoro.stusystem.client.controller.module.Adjuster;

public class AdjusterController {
	
	//存储变量
	private HashMap<String, Adjuster> adjusterMap = new HashMap<String, Adjuster>();
	
	//调节器常量，用于维持调节器单一性
	public static final String NORMAL = "Adjuster";

    //调节器的实时更新
	public void update() {
		for (String key : this.adjusterMap.keySet()) {
			this.checkDestroy(key);
		}
		for (String key : this.adjusterMap.keySet()) {
			this.adjusterMap.get(key).update();
		}

	}
	
    private boolean checkDestroy(String key) {
		if(this.adjusterMap.get(key).isDestroy()) {
			this.removeAdjuster(key);
			return true;
		}
		return false;
	}
	
	//指定调节器是否存在
	public boolean haveAdjuster(String key) {
		return this.adjusterMap.containsKey(key);
	}
	
	//获取指定调节器
	public Adjuster getAdjuster(String key) {
		return this.adjusterMap.get(key);
	}
	
	//添加新调节器
	public void addAdjuster(Adjuster adjuster) {
		if(this.haveAdjuster(adjuster.getKey())) {
			adjuster.reset(this.adjusterMap.get(adjuster.getKey()));
		} else {
			this.adjusterMap.put(adjuster.getKey(), adjuster);
		}
	}
	
	//设置调节器
	public void setAdjuster(Adjuster adjuster) {
		if(this.haveAdjuster(adjuster.getKey())) {
			adjuster.reset(this.adjusterMap.get(adjuster.getKey()));
		}
	}
	
	//删除场景
	public void removeAdjuster(String key) {
		this.adjusterMap.remove(key);
	}

}
