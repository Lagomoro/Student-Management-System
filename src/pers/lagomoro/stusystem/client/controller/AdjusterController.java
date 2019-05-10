package pers.lagomoro.stusystem.client.controller;

import java.util.HashMap;

import pers.lagomoro.stusystem.client.controller.module.Adjuster;

public class AdjusterController {
	
	//�洢����
	private HashMap<String, Adjuster> adjusterMap = new HashMap<String, Adjuster>();
	
	//����������������ά�ֵ�������һ��
	public static final String NORMAL = "Adjuster";

    //��������ʵʱ����
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
	
	//ָ���������Ƿ����
	public boolean haveAdjuster(String key) {
		return this.adjusterMap.containsKey(key);
	}
	
	//��ȡָ��������
	public Adjuster getAdjuster(String key) {
		return this.adjusterMap.get(key);
	}
	
	//����µ�����
	public void addAdjuster(Adjuster adjuster) {
		if(this.haveAdjuster(adjuster.getKey())) {
			adjuster.reset(this.adjusterMap.get(adjuster.getKey()));
		} else {
			this.adjusterMap.put(adjuster.getKey(), adjuster);
		}
	}
	
	//���õ�����
	public void setAdjuster(Adjuster adjuster) {
		if(this.haveAdjuster(adjuster.getKey())) {
			adjuster.reset(this.adjusterMap.get(adjuster.getKey()));
		}
	}
	
	//ɾ������
	public void removeAdjuster(String key) {
		this.adjusterMap.remove(key);
	}

}
