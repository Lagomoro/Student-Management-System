package pers.lagomoro.stusystem.client.view.module;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicScrollBarUI;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

//自定义滚动条UI
public class ScrollBarUI extends BasicScrollBarUI {
	
	private int barWidth;
	
	public ScrollBarUI(int barWidth) {
		super();
		this.barWidth = barWidth;
	}
	
	@Override
	protected void configureScrollBarColors() {
		this.thumbColor = Color.WHITE;
		this.thumbHighlightColor = Color.BLUE;
		this.thumbDarkShadowColor = Color.BLACK;
		this.thumbLightShadowColor = Color.BLACK;
		
		this.trackColor = GraphicsController.HOVER_COLOR;
		this.setThumbBounds(0, 0, 3, 10);
		this.trackHighlightColor = Color.LIGHT_GRAY;
	}
	
	/**
	 * 设置滚动条的宽度
	 */
	@Override
	public Dimension getPreferredSize(JComponent c) {
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			c.setPreferredSize(new Dimension(this.barWidth, 0));
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			c.setPreferredSize(new Dimension(0, this.barWidth));
		}
		return super.getPreferredSize(c);
	}
	
	/**
	 * 绘制轨道
	 */
	@Override
	protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
		Graphics2D graphics = (Graphics2D) g;
		GraphicsController.setHint(graphics);
		
		graphics.setColor(this.trackColor);
		graphics.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
		
		if(trackHighlight == DECREASE_HIGHLIGHT){
			paintDecreaseHighlight(g);
		}else if(trackHighlight == INCREASE_HIGHLIGHT){
			paintIncreaseHighlight(g);
		}
		
		if (this.scrollbar.getOrientation() == JScrollBar.VERTICAL) {
			/*graphics.setColor(incrButton.getModel().isPressed() ? this.trackColor : this.thumbColor);
			graphics.fillRect(trackBounds.x, trackBounds.y - trackBounds.width, trackBounds.width, trackBounds.width);
			graphics.setColor(decrButton.getModel().isPressed() ? this.trackColor : this.thumbColor);
			graphics.fillRect(trackBounds.x, trackBounds.y + trackBounds.height, trackBounds.width, trackBounds.width);
			
			graphics.setColor(this.thumbDarkShadowColor);
			graphics.drawRect(trackBounds.x, trackBounds.y - trackBounds.width, trackBounds.width, trackBounds.width);
			graphics.drawRect(trackBounds.x, trackBounds.y + trackBounds.height, trackBounds.width, trackBounds.width);
			*/
			graphics.setColor(this.thumbDarkShadowColor);
			int targetX = trackBounds.x + trackBounds.width/2;
			int targetY = trackBounds.y - trackBounds.width/2;
			graphics.fillPolygon(new int[]{targetX, targetX - 4, targetX + 4}, new int[]{targetY - 2, targetY + 2, targetY + 2}, 3);
			targetX = trackBounds.x + trackBounds.width/2;
			targetY = trackBounds.y + trackBounds.height + trackBounds.width/2;
			graphics.fillPolygon(new int[]{targetX, targetX - 4, targetX + 4}, new int[]{targetY + 2, targetY - 2, targetY - 2}, 3);
		}
		if (this.scrollbar.getOrientation() == JScrollBar.HORIZONTAL) {
			/*graphics.setColor(incrButton.getModel().isPressed() ? this.trackColor : this.thumbColor);
			graphics.fillRect(trackBounds.x - trackBounds.height, trackBounds.y, trackBounds.height, trackBounds.height);
			graphics.setColor(decrButton.getModel().isPressed() ? this.trackColor : this.thumbColor);
			graphics.fillRect(trackBounds.x + trackBounds.width, trackBounds.y, trackBounds.height, trackBounds.height);
			
			graphics.setColor(this.thumbDarkShadowColor);
			graphics.drawRect(trackBounds.x - trackBounds.height, trackBounds.y, trackBounds.height, trackBounds.height);
			graphics.drawRect(trackBounds.x + trackBounds.width, trackBounds.y, trackBounds.height, trackBounds.height);
			*/
			graphics.setColor(this.thumbDarkShadowColor);
			int targetX = trackBounds.x - trackBounds.height/2;
			int targetY = trackBounds.y + trackBounds.height/2;
			graphics.fillPolygon(new int[]{targetX - 2, targetX + 2, targetX + 2}, new int[]{targetY, targetY - 4, targetY + 4}, 3);
			targetX = trackBounds.x + trackBounds.width + trackBounds.height/2;
			targetY = trackBounds.y + trackBounds.height/2;
			graphics.fillPolygon(new int[]{targetX + 2, targetX - 2, targetX - 2}, new int[]{targetY, targetY + 4, targetY - 4}, 3);
		}
	}
	
	/**
	 * 绘制拖动柄
	 */
	@Override
	protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
		if(thumbBounds.isEmpty() || !this.scrollbar.isEnabled())     {
			return;
		}

		Graphics2D graphics = (Graphics2D) g;
		GraphicsController.setHint(graphics);
		
		int w = thumbBounds.width;
		int h = thumbBounds.height;
		
		graphics.translate(thumbBounds.x, thumbBounds.y);
		
		graphics.setColor(thumbDarkShadowColor);
		graphics.drawRect(1, 1, w - 2, h - 2);
		
		graphics.setColor(thumbColor);
		graphics.fillRect(1, 1, w - 2, h - 2);

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }
	
	@Override
	protected JButton createIncreaseButton(int orientation) {
		ButtonStyled button = new ButtonStyled();
		return button;
	}
	
	@Override
	protected JButton createDecreaseButton(int orientation) {
		ButtonStyled button = new ButtonStyled();
		return button;
	}
	
	@Override
	protected void layoutVScrollbar(JScrollBar sb) {
		Dimension sbSize = sb.getSize();
		Insets sbInsets = sb.getInsets();
		
		int itemW = sbSize.width - (sbInsets.left + sbInsets.right);
		int itemX = sbInsets.left;
		
		int decrButtonH = itemW;
		int decrButtonY = sbInsets.top;
		int incrButtonH = itemW;
		int incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
		
		int sbInsetsH = sbInsets.top + sbInsets.bottom;
		int sbButtonsH = decrButtonH + incrButtonH;
		int gaps = decrGap + incrGap;
		float trackH = sbSize.height - (sbInsetsH + sbButtonsH) - gaps;
		
		float min = sb.getMinimum();
		float extent = sb.getVisibleAmount();
		float range = sb.getMaximum() - min;
		float value = sb.getValue();
		
		int thumbH = (range <= 0) ? getMaximumThumbSize().height : (int)(trackH * (extent / range));
		thumbH = Math.max(thumbH, getMinimumThumbSize().height);
		thumbH = Math.min(thumbH, getMaximumThumbSize().height);
		
		int thumbY = incrButtonY - incrGap - thumbH;
		if (value < (sb.getMaximum() - sb.getVisibleAmount())) {
			float thumbRange = trackH - thumbH;
			thumbY = (int)(0.5f + (thumbRange * ((value - min) / (range - extent))));
			thumbY +=  decrButtonY + decrButtonH + decrGap;
		}

		int sbAvailButtonH = (sbSize.height - sbInsetsH);
		if (sbAvailButtonH < sbButtonsH) {
			incrButtonH = decrButtonH = sbAvailButtonH / 2;
			incrButtonY = sbSize.height - (sbInsets.bottom + incrButtonH);
		}
		decrButton.setBounds(itemX, decrButtonY, itemW, decrButtonH);
		incrButton.setBounds(itemX, incrButtonY, itemW, incrButtonH);
		
		int itrackY = decrButtonY + decrButtonH + decrGap;
		int itrackH = incrButtonY - incrGap - itrackY;
		trackRect.setBounds(itemX, itrackY, itemW, itrackH);

		if(thumbH >= (int)trackH)       {
			if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
				setThumbBounds(itemX, itrackY, itemW, itrackH);
			} else {
				setThumbBounds(0, 0, 0, 0);
			}
		}
		else {
			if ((thumbY + thumbH) > incrButtonY - incrGap) {
				thumbY = incrButtonY - incrGap - thumbH;
			}
			if (thumbY  < (decrButtonY + decrButtonH + decrGap)) {
				thumbY = decrButtonY + decrButtonH + decrGap + 1;
			}
			setThumbBounds(itemX, thumbY, itemW, thumbH);
		}
	}
	
	@Override
	protected void layoutHScrollbar(JScrollBar sb){
		Dimension sbSize = sb.getSize();
		Insets sbInsets = sb.getInsets();
		
		int itemH = sbSize.height - (sbInsets.top + sbInsets.bottom);
		int itemY = sbInsets.top;
		
		boolean ltr = sb.getComponentOrientation().isLeftToRight();
		
		int leftButtonW = itemH;
		int rightButtonW = itemH;
		
		if (!ltr) {
			int temp = leftButtonW;
			leftButtonW = rightButtonW;
			rightButtonW = temp;
		}
		int leftButtonX = sbInsets.left;
		int rightButtonX = sbSize.width - (sbInsets.right + rightButtonW);
		int leftGap = ltr ? decrGap : incrGap;
		int rightGap = ltr ? incrGap : decrGap;
		
		int sbInsetsW = sbInsets.left + sbInsets.right;
		int sbButtonsW = leftButtonW + rightButtonW;
		float trackW = sbSize.width - (sbInsetsW + sbButtonsW) - (leftGap + rightGap);
		
		float min = sb.getMinimum();
		float max = sb.getMaximum();
		float extent = sb.getVisibleAmount();
		float range = max - min;
		float value = sb.getValue();
		
		int thumbW = (range <= 0) ? getMaximumThumbSize().width : (int)(trackW * (extent / range));
		thumbW = Math.max(thumbW, getMinimumThumbSize().width);
		thumbW = Math.min(thumbW, getMaximumThumbSize().width);
		
		int thumbX = ltr ? rightButtonX - rightGap - thumbW : leftButtonX + leftButtonW + leftGap;
		if (value < (max - sb.getVisibleAmount())) {
			float thumbRange = trackW - thumbW;
			if( ltr ) {
				thumbX = (int)(0.5f + (thumbRange * ((value - min) / (range - extent))));
			} else {
				thumbX = (int)(0.5f + (thumbRange * ((max - extent - value) / (range - extent))));
			}
			thumbX += leftButtonX + leftButtonW + leftGap;
		}
		
		int sbAvailButtonW = (sbSize.width - sbInsetsW);
		if (sbAvailButtonW < sbButtonsW) {
			rightButtonW = leftButtonW = sbAvailButtonW / 2;
			rightButtonX = sbSize.width - (sbInsets.right + rightButtonW + rightGap);
		}
		
		(ltr ? decrButton : incrButton).setBounds(leftButtonX, itemY, leftButtonW, itemH);
		(ltr ? incrButton : decrButton).setBounds(rightButtonX, itemY, rightButtonW, itemH);
		
		int itrackX = leftButtonX + leftButtonW + leftGap;
		int itrackW = rightButtonX - rightGap - itrackX;
		trackRect.setBounds(itrackX, itemY, itrackW, itemH);
		
		if (thumbW >= (int)trackW) {
			if (UIManager.getBoolean("ScrollBar.alwaysShowThumb")) {
				setThumbBounds(itrackX, itemY, itrackW, itemH);
			} else {
				setThumbBounds(0, 0, 0, 0);
			}
		}
		else {
			if (thumbX + thumbW > rightButtonX - rightGap) {
				thumbX = rightButtonX - rightGap - thumbW;
			}
			if (thumbX  < leftButtonX + leftButtonW + leftGap) {
				thumbX = leftButtonX + leftButtonW + leftGap + 1;
			}
		setThumbBounds(thumbX, itemY, thumbW, itemH);
		}
	}
}
