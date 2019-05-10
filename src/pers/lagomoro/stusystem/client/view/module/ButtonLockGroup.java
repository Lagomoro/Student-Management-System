package pers.lagomoro.stusystem.client.view.module;

import java.util.Enumeration;
import java.util.Vector;

import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

public class ButtonLockGroup{
	
	protected Vector<ButtonStyled> buttons = new Vector<ButtonStyled>();
	
	protected ButtonStyled selection = null;

	public ButtonLockGroup() {
		
	}
    
	public void add(ButtonStyled button) {
		if (button == null) return;
		this.buttons.addElement(button);
		if (button.isLocked()) {
			if (this.selection == null) {
				this.selection = button;
			} else {
				button.setLocked(false);
			}
		}
	}
	public void remove(ButtonStyled button) {
		if (button == null) return;
		this.buttons.removeElement(button);
		if(button.equals(this.selection)) {
			if(this.getButtonCount() > 0) {
				this.setSelected(this.buttons.get(0), true);
			}else {
				this.selection = null;
			}
		}
	}
	
	public void clearSelection() {
		if (this.selection != null) {
			ButtonStyled oldSelection = this.selection;
			this.selection = null;
			oldSelection.setLocked(false);
		}
	}
	
	public Enumeration<ButtonStyled> getElements() {
		return this.buttons.elements();
	}
	
	public ButtonStyled getSelection() {
		return this.selection;
	}
	
	public void setSelected(ButtonStyled button, boolean value) {
		if (button != null && button != this.selection) {
			ButtonStyled oldSelection = this.selection;
			this.selection = button;
			if (oldSelection != null) {
				oldSelection.setLocked(false);
			}
			button.setLocked(true);
		}
	}
	
	public boolean isSelected(ButtonStyled button) {
		return button == this.selection;
	}
	
	public int getButtonCount() {
		if (this.buttons == null) {
			return 0;
		} else {
			return this.buttons.size();
		}
	}

}
