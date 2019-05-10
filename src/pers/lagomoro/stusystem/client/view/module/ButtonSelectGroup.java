package pers.lagomoro.stusystem.client.view.module;

import java.util.Enumeration;
import java.util.Vector;

import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;

public class ButtonSelectGroup{
	
	protected Vector<ButtonStyled> buttons = new Vector<ButtonStyled>();
	protected Vector<ButtonStyled> selection = new Vector<ButtonStyled>();
	
	protected int maxCount = 1;
	
	public ButtonSelectGroup() {
		
	}

	public ButtonSelectGroup(int maxCount) {
		this.maxCount = maxCount;
	}
    
	public void add(ButtonStyled button) {
		if (button == null) return;
		this.buttons.addElement(button);
		if (button.isSelected()) {
			if (this.selection.size() < this.maxCount) {
				this.selection.add(button);
			} else {
				button.setSelected(false);
			}
		}
	}
	public void remove(ButtonStyled button) {
		if (button == null) return;
		this.buttons.removeElement(button);
		if(this.selection.contains(button)) {
			this.selection.remove(button);
		}
	}
	
	public void clearSelection() {
		for(ButtonStyled button : this.selection) {
			button.setSelected(false);
		}
	}
	
	public Enumeration<ButtonStyled> getElements() {
		return this.buttons.elements();
	}
	
	public Enumeration<ButtonStyled> getSelection() {
		return this.selection.elements();
	}
	
	public void setSelected(ButtonStyled button, boolean value) {
		if(value == true) {
			if(this.maxCount == 1 && this.selection.size() == 1 && value) {
				this.selection.remove(0).setSelected(false);
			}
			if (this.selection.size() < maxCount) {
				this.selection.add(button);
				button.setSelected(true);
			}else {
				button.setSelected(false);
			}
		}else {
			if(this.selection.contains(button)) {
				this.selection.remove(button);
			}
			button.setSelected(false);
		}
		WindowController.refresh();
	}
	
	public boolean isSelected(ButtonStyled button) {
		return this.selection.contains(button);
	}
	
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	
	public int getButtonCount() {
		if (this.buttons == null) {
			return 0;
		} else {
			return this.buttons.size();
		}
	}
	
	public int getSelectionCount() {
		if (this.selection == null) {
			return 0;
		} else {
			return this.selection.size();
		}
	}

}
