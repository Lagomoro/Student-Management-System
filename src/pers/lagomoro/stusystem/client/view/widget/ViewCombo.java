package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.SwingConstants;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.WindowController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.Flash;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;

@SuppressWarnings("serial")
public class ViewCombo<E> extends ViewStyled{
	
	protected E[] comboList;
	protected int chooseFontSize = LanguageModel.DEFAULT_FONTSIZE;
	protected int chooseBoundSize = 8;
	protected int maxItem = 15;
	
	protected EditTextStyled editText;
	protected ButtonStyled button;
	protected Flash comboMenu;
	protected ViewStyled comboView;
	
	protected Frame owner;
	
	public ViewCombo() {
		super();
	}
	
	public ViewCombo(Frame owner) {
		super();
		this.setOwner(owner);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
	}
	
	@Override
	protected void create() {
		super.create();
		this.createContent();
	}

	protected void createContent() {
		this.editText = new EditTextStyled() {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				graphics.drawRect(0, 0, this.getWidth(), this.getHeight());
			}
	    };
		this.add(this.editText);
		
		this.button = new ButtonStyled(null, GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				WindowController.refresh();
				if(comboMenu == null) createChooseWindow();
				showList();
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(this.textColor);
				int targetX = getWidth()/2;
				int targetY = getHeight()/2 + 1;
				graphics.fillPolygon(new int[]{targetX, targetX - 4, targetX + 4}, new int[]{targetY + 2, targetY - 2, targetY - 2}, 3);
		    }
		};
		this.add(this.button);
		
		this.comboView = new ViewStyled();
		this.comboView.setBorder(null);
		this.comboView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true));
	}
	
	protected void createChooseWindow() {
		this.comboMenu = new Flash(this.owner) {
			@Override
			protected void onFocusLost(WindowEvent e) {
				super.onFocusLost(e);
				hideList();
			}
		};
		this.comboMenu.setAlwaysOnTop(true);
		ScrollPane scrollPane = new ScrollPane(this.comboView, false, true);
		scrollPane.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
		this.comboMenu.setContentPane(scrollPane);
	}
	
	protected void showList() {
		this.button.lock();
		
		int width = Math.max(this.comboMenu.getPreferredSize().width, this.getWidth());
		int height = (this.comboList == null ? 1 : (this.comboList.length > this.maxItem ? this.maxItem : this.comboList.length)) * (this.chooseFontSize + this.chooseBoundSize);
		this.comboMenu.setSize(width + 2, height + 3);
		Point aPoint = this.getLocationOnScreen();
		this.comboMenu.setLocation(aPoint.x, aPoint.y + this.getHeight());
		this.comboMenu.setVisible(true);
	}
	
	protected void hideList() {
		this.button.unlock();
		this.comboMenu.setVisible(false);
	}
	
	/**
	 * Set.
	 **/
	@Override
	public void setBounds(int x, int y, int width, int height) {
		if(this.button != null) this.button.setBounds(width - 20, 0, 20, height);
		if(this.editText != null) this.editText.setBounds(0, 0, width - 20, height);
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void setFont(Font font) {
		if(this.editText != null) this.editText.setFont(font);
		super.setFont(font);
	}
	
	public void setOwner(Frame owner) {
		this.owner = owner;
	}
	
	public void setChooseFontSize(int size) {
		this.chooseFontSize = size;
	}
	
	public void setChooseBoundSize(int size) {
		this.chooseBoundSize = size;
	}
	
	public void setMaxItem(int size) {
		this.maxItem = size;
	}
	
	public void setPadding(int padding) {
		this.editText.setPadding(padding);
	}
	
	public void setSet(E[] list) {
		this.comboList = list;
		this.comboView.removeAll();
		for(E item : list) {
			ButtonStyled button = new ButtonStyled(item.toString(), GraphicsController.DARK_HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.DARK_TOUCH_COLOR) {
				public E itemIndex = item;
				@Override
				protected void onMouseClicked(MouseEvent e) {
					super.onMouseClicked(e);
					hideList();
					select(this.itemIndex, true, true);
				}
			};
			button.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
			button.setFont(LanguageModel.getDefaultFont(this.chooseFontSize));
			button.setBounds(0, 0, this.comboView.getWidth(), this.chooseFontSize + this.chooseBoundSize);
			button.setHorizontalAlignment(SwingConstants.LEFT);
			this.setButton(button, item);
			this.comboView.add(button);
		}
		this.select(0, false);
	}
	
	protected void setButton(ButtonStyled button, E item){}
	
	public void select(E item, boolean event, boolean setText) {
		if(setText) {
			this.editText.setText(item.toString());
			this.editText.setCaretPosition(0);
		}
		for(E itemS : comboList) {
			if(itemS.equals(item)) {
				if(event) this.onSelectItem(item);
				return;
			}
		}
		if(event) this.onSelectOther(item, setText);
	}
	public void select(int index, boolean event) {
		if(index > comboList.length) return;
		this.select(comboList[index], event, true);			
	}
	public boolean select(String key, boolean event) {
		for(E item : comboList) {
			if(item.toString().equals(key)) {
				this.select(item, event, true);
				return true;
			}
		}
		this.editText.setText(key);
		this.editText.setCaretPosition(0);
		if(event) this.onSelectOther(key);
		return false;
	}

	protected void onSelectItem(E item) {}
	protected void onSelectOther(E item, boolean setKey) {}
	protected void onSelectOther(String key) {}

	@Override
	public void update() {
		super.update();
		this.comboView.update();
		if(this.comboMenu != null && this.comboMenu.isVisible()) {
			Point aPoint = this.getLocationOnScreen();
			this.comboMenu.setLocation(aPoint.x, aPoint.y + this.getHeight());
		}
	}
	
	@Override
	public void refresh() {
		super.refresh();
		if(this.comboView != null) this.comboView.refresh();
		if(this.comboMenu != null) {
			this.comboMenu.dispose();
			this.createChooseWindow();
		}
	}
}
