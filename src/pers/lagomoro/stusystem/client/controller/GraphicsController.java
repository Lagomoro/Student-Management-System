package pers.lagomoro.stusystem.client.controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class GraphicsController {
	
	public static final int STROKE_INSIDE = -1;
	public static final int STROKE_MIDDLE = 0;
	public static final int STROKE_OUTSIDE = 1;
	
	public static final Color EMPTY_COLOR = new Color(0, 0, 0, 1);
	public static final Color DEFAULT_TINT_COLOR = Color.LIGHT_GRAY;
	
	public static final int SHADE_ALPHA = 40;
	
	public static final Color DEFAULT_TEXT_COLOR = Color.BLACK;
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	
	public static final Color WARNING_COLOR = Color.RED;
	public static final Color HOVER_COLOR = new Color(235, 235, 235, 255);
	public static final Color DEFAULT_COLOR = Color.WHITE;
	public static final Color TOUCH_COLOR = new Color(210, 210, 210, 255);

	public static final Color DARK_HOVER_COLOR = new Color(210, 210, 210, 255);
	public static final Color DARK_TOUCH_COLOR = new Color(170, 170, 170, 255);
	
	public static final Color HINT_BLUE_COLOR = new Color(50, 100, 180);
	public static final Color HINT_LIGHTBLUE_COLOR = new Color(0, 140, 230);

	public static final Color HOVER_BLUE_COLOR = new Color(60, 180, 240, 255);
	public static final Color DEFAULT_BLUE_COLOR = new Color(20, 160, 230, 255);
	public static final Color TOUCH_BLUE_COLOR = new Color(150, 150, 150, 255);
	
	public static void setHint(Graphics2D graphics) {
		//插值提示键 = 取样：9个
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		//Alpha插值提示键 = Alpha混合算法：视觉质量优先
		graphics.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        //抗锯齿提示键 = 抗锯齿模式：最佳
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//文本抗锯齿提示键 = 抗锯齿模式：LCD显示器，优先水平方向，RGB取值
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        //LCD 文本对比呈现提示键 = 文本对比度：低
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_LCD_CONTRAST, 180);
		//字体小数规格提示键 = 光栅化子像素：开
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON );
		//呈现提示键 = 图像质量：视觉质量优先
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		//颜色呈现提示键 = 颜色转换：视觉质量优先
		graphics.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        //抖动提示键 = 色彩抖动：允许
		graphics.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        //笔划规范化控制提示键 = 几何形状：渲染到子像素
		graphics.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
}
