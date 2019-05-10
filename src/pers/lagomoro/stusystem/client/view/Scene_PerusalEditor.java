package pers.lagomoro.stusystem.client.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

import javax.swing.BorderFactory;

import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.SceneController;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.component.ScrollPane;
import pers.lagomoro.stusystem.client.view.module.VerticalFlowLayout;
import pers.lagomoro.stusystem.client.view.widget.ButtonStyled;
import pers.lagomoro.stusystem.client.view.widget.ItemEditVote;
import pers.lagomoro.stusystem.client.view.widget.TextView;
import pers.lagomoro.stusystem.client.view.widget.View;
import pers.lagomoro.stusystem.client.view.widget.ViewStyled;

@SuppressWarnings("serial")
public class Scene_PerusalEditor extends Scene_NoticeEditor{

	protected int maxChoose = 1;
	protected TextView maxChooseView;
	protected ButtonStyled addButton;
	protected View voteView;
	
	protected View buttonView;
	
	public Scene_PerusalEditor(){
		super(SceneController.SCENE_PERUSAL_EDITOR, null, null);
	}
	
	public Scene_PerusalEditor(String key){
		super(SceneController.SCENE_PERUSAL_EDITOR, null, key);
	}

	@Override
	protected void create() {
		super.create();
		this.createVote();
	}

	protected void createVote() {
		View voteView = new View();
		voteView.setPreferredSize(new Dimension(0, 200));
		voteView.setLayout(new BorderLayout());
		this.add(voteView, BorderLayout.SOUTH);
		
		ViewStyled voteBar = new ViewStyled() {
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(Color.GRAY);
				graphics.fillRect(0, 0, this.getWidth(), 2);
				graphics.fillRect(0, 39, this.getWidth(), 1);
			}
		};
		voteBar.setPreferredSize(new Dimension(0, 40));
		voteBar.setBackground(GraphicsController.EMPTY_COLOR);
		voteView.add(voteBar, BorderLayout.NORTH);
		
		int placeX = 0;
		int placeY = 0;
		
		TextView voteNumberText = new TextView("UI->Scene->PersualEditor::VoteNumber");
		voteNumberText.setBounds(placeX, placeY, 130, 40);
		voteNumberText.setBackground(Color.BLUE);
		voteNumberText.setFont(LanguageModel.getDefaultFont(18));
		voteBar.add(voteNumberText);
		
		placeX += voteNumberText.getWidth();
		ButtonStyled minusButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				minusMaxChoose();
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3 + 2, 2);
		    }
		};
		minusButton.setBounds(placeX, placeY + 8, 24, 24);
		minusButton.setShade(1);
		minusButton.setRadius(15);
		voteBar.add(minusButton);
		
		placeX += minusButton.getWidth();
		this.maxChooseView = new TextView(Integer.toString(this.maxChoose));
		this.maxChooseView.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
		this.maxChooseView.setBounds(placeX, placeY, 40, 40);
		this.maxChooseView.setBackground(Color.BLUE);
		this.maxChooseView.setFont(LanguageModel.getDefaultFont(18));
		voteBar.add(this.maxChooseView);
		
		placeX += this.maxChooseView.getWidth();
		this.addButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				addMaxChoose();
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3 + 2, 2);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 2, this.getHeight()/3 + 2);
		    }
		};
		this.addButton.setBounds(placeX, placeY + 8, 24, 24);
		this.addButton.setShade(1);
		this.addButton.setRadius(15);
		voteBar.add(this.addButton);
		
		this.buttonView = new View();
		this.buttonView.setBounds(0, 0, 120, 40);
		voteBar.add(this.buttonView);
		
		placeX = 0;
		TextView newChooseText = new TextView("UI->Scene->PersualEditor::NewChoose");
		newChooseText.setBounds(placeX, placeY, 95, 40);
		newChooseText.setBackground(Color.BLUE);
		newChooseText.setFont(LanguageModel.getDefaultFont(18));
		this.buttonView.add(newChooseText);
		
		placeX += newChooseText.getWidth();
		ButtonStyled newChooseButton = new ButtonStyled(null, GraphicsController.HOVER_COLOR, GraphicsController.DEFAULT_COLOR, GraphicsController.TOUCH_COLOR) {
			@Override
			protected void onMouseClicked(MouseEvent e) {
				super.onMouseClicked(e);
				addNewChoose();
			}
			@Override
			protected void paint(Graphics2D graphics) {
				super.paint(graphics);
				graphics.setColor(GraphicsController.DEFAULT_TEXT_COLOR);
				graphics.fillRect(this.getWidth()/3, this.getHeight()/2 - 1, this.getWidth()/3 + 2, 2);
				graphics.fillRect(this.getWidth()/2 - 1, this.getHeight()/3, 2, this.getHeight()/3 + 2);
		    }
		};
		newChooseButton.setBounds(placeX, placeY + 8, 24, 24);
		newChooseButton.setShade(1);
		newChooseButton.setRadius(15);
		this.buttonView.add(newChooseButton);
		
		placeY += 40;
		this.voteView = new View();	
		this.voteView.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, true) {
			@Override
			public Dimension preferredLayoutSize(Container target) {
				Dimension temp = super.preferredLayoutSize(target);
				temp.height += 5;
				return temp;
			}
		});
		voteView.add(new ScrollPane(this.voteView), BorderLayout.CENTER);
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		this.buttonView.setLocation(width - this.buttonView.getWidth() - 13, this.buttonView.getY());
		super.setBounds(x, y, width, height);
	}
	
	@Override
	public void update() {
		super.update();
		this.voteView.update();
		this.setMaxChoose(0);
	}
	
	@Override
	public void refresh() {
		super.refresh();
		this.voteView.refresh();
	}
	
	protected void minusMaxChoose() {
		this.setMaxChoose(-1);
	}
	
	protected void addMaxChoose() {
		this.setMaxChoose(1);
	}
	
	protected void setMaxChoose(int value) {
		if(this.maxChoose + value > 0 && this.maxChoose + value <= this.voteView.getComponentCount()) this.maxChoose += value;
		if(this.maxChoose > this.voteView.getComponentCount() && this.voteView.getComponentCount() >= 1) this.maxChoose = this.voteView.getComponentCount();
		if(this.maxChoose <= 0) this.maxChoose = 1;
		this.maxChooseView.setText(Integer.toString(this.maxChoose));
		this.maxChooseView.setBounds(this.maxChooseView.getX(), this.maxChooseView.getY(), 13 + (int)(this.getFont().getStringBounds(Integer.toString(this.maxChoose), new FontRenderContext(new AffineTransform(), true, true)).getWidth()), 40);
		this.addButton.setBounds(this.maxChooseView.getX() + this.maxChooseView.getWidth() + 1, this.addButton.getY(), this.addButton.getWidth(), this.addButton.getHeight());
	}
	
	public int getMaxChoose() {
		return this.maxChoose;
	}
	
	public String getOptions() {
		String options = "";
		for(Component comp : this.voteView.getComponents()) {
			ItemEditVote option = (ItemEditVote) comp;
			options += option.getText() + ";";
		}
		return options.length() > 0 ? options.substring(0, options.length() - 1) : "";
	}
	
	protected void addNewChoose() {
		this.voteView.add(new ItemEditVote());
		this.voteView.refresh();
	}
}
