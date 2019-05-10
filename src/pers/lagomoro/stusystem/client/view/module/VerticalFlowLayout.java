package pers.lagomoro.stusystem.client.view.module;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

@SuppressWarnings("serial")
public class VerticalFlowLayout extends FlowLayout {
	
	public static final int TOP = 0;
	public static final int MIDDLE = 1;
	public static final int BOTTOM = 2;
	
	private int hgap = 0;
	private int vgap = 0;
	
	private boolean retainHeight = false;
	
    public VerticalFlowLayout() {
        super();
    }
    
    public VerticalFlowLayout(int align) {
        super(align);
    }

    public VerticalFlowLayout(int align, int hgap, int vgap) {
        super(align);
        this.hgap = hgap;
        this.vgap = vgap;
    }
    
    public VerticalFlowLayout(int align, boolean retainHeight) {
        super(align);
        this.setRetainHeight(retainHeight);
    }
    
    public VerticalFlowLayout(int align, boolean retainHeight, int hgap, int vgap) {
        super(align);
        this.setRetainHeight(retainHeight);
        this.hgap = hgap;
        this.vgap = vgap;
    }

	public boolean isRetainHeight() {
		return retainHeight;
	}

	public void setRetainHeight(boolean retainHeight) {
		this.retainHeight = retainHeight;
	}
    
	@Override
    public Dimension preferredLayoutSize(Container target){
    	synchronized (target.getTreeLock()) {
    		Dimension dim = new Dimension(0, 0);
    		int nmembers = target.getComponentCount();
    		boolean firstVisibleComponent = true;
            
            for (int i = 0; i < nmembers; i++){
    			Component m = target.getComponent(i);
    			if (m.isVisible()){
    				Dimension d = m.getPreferredSize();
    				dim.width = Math.max(dim.width, d.width);
    				if (firstVisibleComponent) {
                        firstVisibleComponent = false;
                    }else{
    					dim.height += this.vgap;
    				}
    				if(isRetainHeight()) {
    					dim.height += m.getSize().height;
    				}else {
    					dim.height += d.height;
    				}
    				//dim.height += d.height;
    			}
    		}
    		Insets insets = target.getInsets();
    		dim.width += insets.left + insets.right + this.hgap * 2;
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
    		
    		for (int i = 0; i < nmembers; i++){
    			Component m = target.getComponent(i);
    			if (m.isVisible()){
    				Dimension d = m.getMinimumSize();
    				dim.width = Math.max(dim.width, d.width);
    				if (firstVisibleComponent) {
                        firstVisibleComponent = false;
                    } else {
    					dim.height += this.vgap;
    				}
    				if(isRetainHeight()) {
    					dim.height += m.getSize().height;
    				}else {
    					dim.height += d.height;
    				}
    				//dim.height += d.height;
    			}
    		}
    		Insets insets = target.getInsets();
    		dim.width += insets.left + insets.right + this.hgap * 2;
    		dim.height += insets.top + insets.bottom + this.vgap * 2;
    		return dim;
    	}
	}
	
    private int moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd, boolean ltr) {
    	switch (getAlignment()) {
    	case TOP:
        	y += ltr ? 0 : height;
        	break;
    	case CENTER:
        	y += height / 2;
        	break;
    	case BOTTOM:
        	y += ltr ? height : 0;
        	break;
    	case LEADING:
        	break;
    	case TRAILING:
        	y += height;
        	break;
    	}
    	for (int i = rowStart ; i < rowEnd ; i++) {
    		Component m = target.getComponent(i);
    		if (m.isVisible()) {
    			int px = x + (width - m.getSize().width) / 2;
				if (ltr) {
    				m.setLocation(px, y);
    			} else {
    				m.setLocation(px, target.getSize().height - y - m.getSize().height);
    			}
    			y += m.getSize().height + this.vgap;
    		}
    	}
    	return width;
    }


	public void layoutContainer(Container target){
		Insets insets = target.getInsets();
		int maxwidth = target.getSize().width - (insets.left + insets.right + this.hgap * 2);
		int maxheight = target.getSize().height	- (insets.top + insets.bottom + this.vgap * 2);
		int nmembers = target.getComponentCount();
		int x = insets.left + this.hgap, y = 0;
		int colw = 0, start = 0;
		
		boolean ltr = target.getComponentOrientation().isLeftToRight();

		for (int i = 0; i < nmembers; i++){
			Component m = target.getComponent(i);
			if (m.isVisible()) {
				Dimension d = m.getPreferredSize();
				if(isRetainHeight()) {
					m.setSize(maxwidth, m.getSize().height);
				}else {
					m.setSize(maxwidth, d.height);
				}
				//m.setSize(maxwidth, d.height);
				d.width = maxwidth;
				//if ((y == 0) || ((y + d.height) <= maxheight)) {
				if ((y == 0) || ((y + (isRetainHeight() ? m.getSize().height : d.height)) <= maxheight)) {
                    if (y > 0) {
                        y += this.vgap;
                    }
                    y += (isRetainHeight() ? m.getSize().height : d.height);
                    //y += d.height;
					colw = Math.max(colw, d.width);
                }else {
                	colw = this.moveComponents(target, x, insets.top + this.vgap, colw, maxheight - y, start, i, ltr);
                	y = (isRetainHeight() ? m.getSize().height : d.height);
                	//y = d.height;
					x += this.hgap + colw;
					colw = d.width;
					start = i;
                }
			}
		}
		this.moveComponents(target, x, insets.top + this.vgap, colw, maxheight - y, start, nmembers, ltr);
	}
	
}