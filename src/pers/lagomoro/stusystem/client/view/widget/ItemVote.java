package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.module.ButtonSelectGroup;
import pers.lagomoro.stusystem.client.view.module.DataClassVoteItem;

@SuppressWarnings("serial")
public class ItemVote extends View {
	
	protected String chooseText = " ";

	protected TextView chooseTextView;
	protected ButtonStyled chooseButton;
	protected ButtonSelectGroup voteSelectGroup;
	
	protected DataClassVoteItem voteItemInfo;
	
	public ItemVote(ButtonSelectGroup voteSelectGroup) {
		super();
		this.voteSelectGroup = voteSelectGroup;
	}
	
	public ItemVote(String chooseText, ButtonSelectGroup voteSelectGroup) {
		super();
		this.chooseText = chooseText;
		this.voteSelectGroup = voteSelectGroup;
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
		this.chooseTextView = new TextView(this.chooseText);
		this.chooseTextView.setBackground(GraphicsController.EMPTY_COLOR);
		this.chooseTextView.setBounds(0, 0, 0, 50);
		this.add(this.chooseTextView, BorderLayout.CENTER);
	}
	
	protected void createButton() {
		View rightView = new View();
		rightView.setPreferredSize(new Dimension(40, 45));
		this.add(rightView, BorderLayout.EAST);
		
		this.chooseButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				if(this.isSelected()) {
					voteSelectGroup.setSelected(this, false);
				}else {
					voteSelectGroup.setSelected(this, true);
				}
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(Color.BLUE);
				if(this.isSelected()) graphics.fillOval(this.getWidth()/4, this.getHeight()/4, this.getWidth()/2 + 1, this.getHeight()/2 + 1);
			}
		};
		this.chooseButton.setBounds(2, 10, 24, 24);
		this.chooseButton.setShade(1);
		this.chooseButton.setRadius(15);
		rightView.add(this.chooseButton);
	}
	
	public ButtonStyled getChooseButton() {
		return chooseButton;
	}
	
	public void setPercent(DataClassVoteItem voteItemInfo) {
		this.voteItemInfo = voteItemInfo;
		chooseButton.setVisible(false);
	}

	@Override
	public void update() {
		super.update();
		if(this.chooseText != this.chooseTextView.getText()) this.chooseTextView.setText(this.chooseText);
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		this.paintPercent(graphics);
		this.paintChoose(graphics);
		this.paintHint(graphics);
	}
	
	protected void paintPercent(Graphics2D graphics) {
		if(this.voteItemInfo != null) {
			double percent = (double)this.voteItemInfo.count/ this.voteItemInfo.chooseTimes;
			graphics.setColor(new Color(230, 230, 230));
			graphics.fillRect(0, 0, (int)(this.getWidth() * percent), this.getHeight());
			graphics.setColor(GraphicsController.DARK_TOUCH_COLOR);
			graphics.setFont(LanguageModel.getDefaultFont(25));
			String text = this.voteItemInfo.count + " / " + this.voteItemInfo.chooseTimes;
			graphics.drawString(text, this.getWidth() - 30 - (int)(graphics.getFont().getStringBounds(text, new FontRenderContext(new AffineTransform(), true, true)).getWidth()), (this.getHeight() + 20) /2);
		}
	}
	
	protected void paintHint(Graphics2D graphics) {
		graphics.setColor(new Color(0, 200, 0));
		graphics.fillOval(15, (this.getHeight() - 10)/2, 10, 10);
	}
	
	protected void paintChoose(Graphics2D graphics) {
		if(this.voteItemInfo == null) {
			graphics.setColor(Color.LIGHT_GRAY);
			if(this.chooseButton.isSelected()) graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		}
	}

}
