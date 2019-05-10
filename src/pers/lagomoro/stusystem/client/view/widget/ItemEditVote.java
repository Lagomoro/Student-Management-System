package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

import pers.lagomoro.stusystem.client.controller.GraphicsController;

@SuppressWarnings("serial")
public class ItemEditVote extends View {
	
	protected EditTextUserName chooseEditText;
	protected ButtonStyled removeButton;

	public ItemEditVote() {
		super();
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setLayout(new BorderLayout());
	}
	
	@Override
	protected void create() {
		super.create();
		this.createViewLeft();
		this.createEditText();
		this.createButton();
		this.setBounds(0, 0, 0, 45);
	}

	protected void createViewLeft() {
		View leftView = new View();
		leftView.setPreferredSize(new Dimension(25, 45));
		this.add(leftView, BorderLayout.WEST);
	}

	protected void createEditText() {
		View centerView = new View() {
			@Override
			public void setBounds(int x, int y, int width, int height) {
				chooseEditText.setBounds(0, 0, width, chooseEditText.getHeight());
				super.setBounds(x, y, width, height);
			}
		};
		centerView.setPreferredSize(new Dimension(25, 45));
		this.add(centerView, BorderLayout.CENTER);
		
		this.chooseEditText = new EditTextUserName("UI->Scene->PersualEditor::ChooseText", Color.LIGHT_GRAY, Color.BLACK);
		this.chooseEditText.setBackground(GraphicsController.EMPTY_COLOR);
		this.chooseEditText.setBounds(0, 0, 0, 50);
		centerView.add(this.chooseEditText);
	}
	
	protected void createButton() {
		View rightView = new View();
		rightView.setPreferredSize(new Dimension(110 - 35, 45));
		this.add(rightView, BorderLayout.EAST);
		
//		ButtonStyled upButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
//			@Override
//			protected void onMouseClicked(MouseEvent e) {
//				super.onMouseClicked(e);
//				View temp = (View)ItemEditVote.this.getParent();
//				int place = temp.getComponentZOrder(ItemEditVote.this);
//				if(place > 0) {
//					temp.setComponentZOrder(ItemEditVote.this, place - 1);
//					temp.refresh();
//				}
//			}
//			@Override
//			protected void paint(Graphics2D graphics) {
//				super.paint(graphics);
//				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
//				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
//				int deltaY = -3;
//				graphics.drawLine(this.getWidth()/3, this.getHeight()*2/3 + 1 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
//				graphics.drawLine(this.getWidth()*2/3 + 1, this.getHeight()*2/3 + 1 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
//		    }
//		};
//		upButton.setBounds(2, 13, 24, 24);
//		upButton.setShade(1);
//		upButton.setRadius(15);
//		rightView.add(upButton);
		
		ButtonStyled downButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				View temp = (View)ItemEditVote.this.getParent();
				int place = temp.getComponentZOrder(ItemEditVote.this);
				if(place < temp.getComponentCount() - 1) {
					temp.setComponentZOrder(ItemEditVote.this, place + 1);
					temp.refresh();
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				int deltaY = 3;
				graphics.drawLine(this.getWidth()/3, this.getHeight()/3 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
				graphics.drawLine(this.getWidth()*2/3 + 1, this.getHeight()/3 + deltaY, this.getWidth()/2, this.getHeight()/2 + deltaY);
		    }
		};
		downButton.setBounds(37 - 35, 13, 24, 24);
		downButton.setShade(1);
		downButton.setRadius(15);
		rightView.add(downButton);
		
		ButtonStyled removeButton = new ButtonStyled(null, GraphicsController.WARNING_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				View temp = (View)ItemEditVote.this.getParent();
				temp.remove(ItemEditVote.this);
				temp.refresh();
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER));
				graphics.drawLine(this.getWidth()/3, this.getHeight()/3, this.getWidth()*2/3 + 1, this.getHeight()*2/3 + 1);
				graphics.drawLine(this.getWidth()/3, this.getHeight()*2/3 + 1, this.getWidth()*2/3 + 1, this.getHeight()/3);
		    }
		};
		removeButton.setBounds(72 - 35, 13, 24, 24);
		removeButton.setShade(1);
		removeButton.setRadius(15);
		rightView.add(removeButton);
	}

	public String getText() {
		return this.chooseEditText.getText();
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintHint(graphics);
	}
	
	protected void paintHint(Graphics2D graphics) {
		graphics.setColor(new Color(0, 200, 0));
		graphics.fillOval(15, (this.getHeight() - 10)/2 + 3, 10, 10);
	}

}
