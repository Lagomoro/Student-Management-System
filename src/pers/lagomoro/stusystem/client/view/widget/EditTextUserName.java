package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;
import pers.lagomoro.stusystem.client.model.LanguageModel;

@SuppressWarnings("serial")
public class EditTextUserName extends EditTextStyled {

	protected Color focusColor;
	protected Color lostColor;
	
	protected String warningString;

	protected boolean stackMode;

	public EditTextUserName(){
		super();
	}
	
	public EditTextUserName(String text){
		super(text);
	}
	
	public EditTextUserName(String text, Color lostColor, Color focusColor){
		super(text);
		this.setColor(lostColor, focusColor);
	}

	@Override
	protected void initialize() {
		super.initialize();
		this.setColor(GraphicsController.EMPTY_COLOR, GraphicsController.EMPTY_COLOR);
		this.setForeground(GraphicsController.DEFAULT_TINT_COLOR);
	}
	
	@Override
	protected void onFocusGain(FocusEvent e) {
		super.onFocusGain(e);
		if(this.isEmpty()) {
			this.setText(null);
			this.adjusterController.setAdjuster(new Adjuster_Double("Slide", 9, 0, 10));
			if(this.textColor != null) {
				this.setForeground(this.textColor);	
			}
		}
		this.unsetWarning();
		if(this.isEditable()) this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.lostColor, this.focusColor, 40));
	}
	
	@Override
	protected void onFocusLost(FocusEvent e) {
		super.onFocusLost(e);
		if(this.isEmpty()) {
			this.adjusterController.setAdjuster(new Adjuster_Double("Slide", 0, 9, 10));
			this.setForeground(GraphicsController.DEFAULT_TINT_COLOR);
		}
		this.unsetWarning();
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.focusColor, this.lostColor, 40));
	}
	
	@Override
	protected void createAdjuster() {
		super.createAdjuster();
		Adjuster_Color adjuster = new Adjuster_Color("Color", this.lostColor, this.lostColor, 20) {
			@Override
			protected void onValueChange() {
				super.onValueChange();
				repaint();
			}
		};
		adjuster.setAlways(true);
		this.adjusterController.addAdjuster(adjuster);
		
		Adjuster_Double adjuster2 = new Adjuster_Double("Slide", 9, 9, 10) {
			@Override
			protected void onValueChange() {
				super.onValueChange();
				if(isEmpty() && this.getValue() >= 9) {
					setText(LanguageModel.get(textKey));
					setForeground(GraphicsController.DEFAULT_TINT_COLOR);
				}

			}
		};
		adjuster2.setAlways(true);
		this.adjusterController.addAdjuster(adjuster2);
	}
	
	/**
	 * Set color of editText.
	 **/
	public void setColor(Color lostColor, Color focusColor) {
		this.focusColor = focusColor;
		this.lostColor = lostColor;
		if(this.adjusterController != null) {
			this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.lostColor, this.lostColor, 20));		
		}
	}
	
	@Override
	public void resetBorder() {
		int border = this.shade + 10;
		this.setBorder(BorderFactory.createEmptyBorder(0, border, 0, border));
	}
	
	public void setWarning(String text) {
		this.warningString = text;
		this.getAdjuster("Color").setCyclic(true);
		this.getAdjuster("Color").setRoundMode(true);
		this.adjusterController.setAdjuster(new Adjuster_Color("Color", this.getColor("Color"), Color.RED, 5));		
	}
	
	public void unsetWarning() {
		this.warningString = null;
		this.getAdjuster("Color").setCyclic(false);
		this.getAdjuster("Color").setRoundMode(false);
	}
	
	public void setStackMode(boolean stackMode) {
		this.stackMode = stackMode;
	}
	
	public void deactive() {
		this.setEditable(false);
		this.setForeground(GraphicsController.DEFAULT_TINT_COLOR);
	}
	
	public void active() {
		this.setEditable(true);
		this.setForeground(this.textColor);
	}
	
	public boolean isEmpty() {
		return this.getText() == null || this.getText().equals(LanguageModel.get(this.textKey)) || this.getText().equals("");
	}
	
	@Override
	public void refresh() {
		this.repaint();
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);		
		if (this.stackMode) graphics.fillRect(this.shade, 0, this.getWidth() - this.shade * 2, this.shade + this.radius);

		graphics.setColor(this.getColor("Color"));
		//graphics.fillRect(this.shade + 10, getHeight() - this.shade - 10, (getWidth() - this.shade * 2 - 20)*(16 - this.getDoubleInt("Slide"))/16, 2);
		graphics.fillRect(this.shade + 10, this.getHeight() - this.shade - 10, this.getWidth() - this.shade * 2 - 20, 2);
		
		graphics.setFont(LanguageModel.getDefaultFont(9 + this.getDoubleInt("Slide")));
		if(!this.isEmpty() || this.getDoubleInt("Slide") < 9) graphics.drawString(LanguageModel.get(this.textKey), this.shade + 10, this.shade + 11 + (this.getHeight() - this.shade - 11)/2 * this.getDoubleInt("Slide") / 9);

		graphics.setFont(LanguageModel.getDefaultFont(9));
		if(this.warningString != null) graphics.drawString(warningString, this.shade + 10 + 50, this.shade + 11);
	}

}
