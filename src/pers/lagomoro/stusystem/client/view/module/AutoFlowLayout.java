package pers.lagomoro.stusystem.client.view.module;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

@SuppressWarnings("serial")
public class AutoFlowLayout extends FlowLayout {
	
	public static final int TOP = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;
	
	private int hgap;
	private int vgap;
	
    public AutoFlowLayout() {
        super();
    }
    
    public AutoFlowLayout(int align) {
        super(align);
    }

    public AutoFlowLayout(int align, int hgap, int vgap) {
        super(align);
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
	@Override
    public Dimension preferredLayoutSize(Container target){
    	synchronized (target.getTreeLock()) {
    		Dimension dim = new Dimension(0, 0);
    		int nmembers = target.getComponentCount();
    		boolean firstVisibleComponent = true;
    		Insets insets = target.getInsets();
    		Dimension temp = new Dimension(insets.left + this.hgap, this.getAlignment() == TOP ? 0 : insets.top + this.vgap);
    		
            for (int i = 0; i < nmembers; i++){
    			Component m = target.getComponent(i);
    			if (m.isVisible()){
    				Dimension d = new Dimension(m.getWidth(), m.getHeight());//m.getPreferredSize();
    				if(temp.width + d.width + this.hgap > target.getWidth() - insets.right) {
    					if (firstVisibleComponent) {
                            firstVisibleComponent = false;
                        }else{
        					dim.height += this.vgap;
        				}
    					dim.width = Math.max(dim.width, temp.width + insets.right + this.hgap);
    					dim.height += temp.height;
    					temp.width = insets.left + this.hgap + d.width + this.hgap;
    					temp.height = d.height;
    				}else {
    					temp.width += d.width + this.hgap;
    					if(d.height > temp.height) {
    						temp.height = d.height;
    					}
    				}
    			}
    		}
            if(temp.height != 0) {
            	dim.height += temp.height;
            }
            dim.height += insets.top + insets.bottom + this.vgap * 2;
    		return dim;
    	}
	}

    @Override
	public Dimension minimumLayoutSize(Container target){
    	synchronized (target.getTreeLock()) {
    		Dimension dim = new Dimension(0, 0);
    		int nmembers = target.getComponentCount();
    		boolean firstVisibleComponent = true;
    		Insets insets = target.getInsets();
    		Dimension temp = new Dimension(insets.left + this.hgap, this.getAlignment() == TOP ? 0 : insets.top + this.vgap);
    		
            for (int i = 0; i < nmembers; i++){
    			Component m = target.getComponent(i);
    			if (m.isVisible()){
    				Dimension d = new Dimension(m.getWidth(), m.getHeight());//m.getPreferredSize();
    				if(temp.width + d.width + this.hgap > target.getWidth() - insets.right) {
    					if (firstVisibleComponent) {
                            firstVisibleComponent = false;
                        }else{
        					dim.height += this.vgap;
        				}
    					dim.width = Math.max(dim.width, temp.width + insets.right + this.hgap);
    					dim.height += temp.height;
    					temp.width = insets.left + this.hgap + d.width + this.hgap;
    					temp.height = d.height;
    				}else {
    					temp.width += d.width + this.hgap;
    					if(d.height > temp.height) {
    						temp.height = d.height;
    					}
    				}
    			}
    		}
            if(temp.height != 0) {
            	dim.height += temp.height;
            }
            dim.height += insets.top + insets.bottom + this.vgap * 2;
    		return dim;
    	}
	}

	public void layoutContainer(Container target){
		Insets insets = target.getInsets();
		int nmembers = target.getComponentCount();
		
		Dimension dim = new Dimension(target.getWidth(), this.getAlignment() == TOP ? 0 : insets.top + this.vgap);
		Dimension temp = new Dimension(insets.left + this.hgap, 0);
		
        for (int i = 0; i < nmembers; i++){
			Component m = target.getComponent(i);
			if (m.isVisible()){
				Dimension d = new Dimension(m.getWidth(), m.getHeight());//m.getPreferredSize();
				if(temp.width + d.width + this.hgap > dim.width - insets.right) {
					dim.height += temp.height + this.vgap;
					temp.width = insets.left + this.hgap;
					m.setLocation(temp.width, dim.height);
					temp.width += d.width + this.hgap;
					temp.height = d.height;
				}else {
					m.setLocation(temp.width, dim.height);
					temp.width += d.width + this.hgap;
					if(d.height > temp.height) {
						temp.height = d.height;
					}
				}
			}
		}
	}
	
}