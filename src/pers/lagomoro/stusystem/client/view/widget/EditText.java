package pers.lagomoro.stusystem.client.view.widget;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import pers.lagomoro.stusystem.client.controller.AdjusterController;
import pers.lagomoro.stusystem.client.controller.GraphicsController;
import pers.lagomoro.stusystem.client.controller.module.Adjuster;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;
import pers.lagomoro.stusystem.client.controller.module.Adjuster_Double;
import pers.lagomoro.stusystem.client.model.LanguageModel;
import pers.lagomoro.stusystem.client.view.module.Controllable;

@SuppressWarnings("serial")
public class EditText extends JTextField implements Controllable{
	
	protected String textKey;
	protected AdjusterController adjusterController;

	public EditText(){
		super();
		this.initialize();
		this.create();
		this.refresh();
	}

	public EditText(String textKey) {
		super();
		this.setTextKey(textKey);
		this.initialize();
		this.create();
		this.refresh();
	}

	protected void initialize() {
		this.setOpaque(false);
		this.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
	}
	
	protected void create() {
		this.createListener();
		this.createController();
		this.createAdjuster();
	}

	/**
	 * Create event listeners.
	 **/
	protected void createListener() {
		this.addFocusListener(new FocusAdapter() {
			@Override
		    public void focusGained(FocusEvent e) {onFocusGain(e);}
		    @Override
		    public void focusLost(FocusEvent e)   {onFocusLost(e);}
        });
		MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e)  {onMouseEntered(e); }
            @Override
            public void mouseExited(MouseEvent e)   {onMouseExited(e);  }
            @Override
            public void mousePressed(MouseEvent e)  {onMousePressed(e); }
            @Override
            public void mouseReleased(MouseEvent e) {onMouseReleased(e);}
			@Override
            public void mouseClicked(MouseEvent e)  {switch(e.getButton()) { case MouseEvent.BUTTON1 : onMouseClicked(e); break; case MouseEvent.BUTTON3 : onMouseRightClicked(e); break;}}
			@Override
            public void mouseMoved(MouseEvent e)    {onMouseMoved(e);   }
			@Override
            public void mouseDragged(MouseEvent e)  {onMouseDragged(e); }
        };
		this.addMouseListener(adapter);
		this.addMouseMotionListener(adapter);
		this.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e)  {onDocumentRemove(e); }
			@Override
			public void insertUpdate(DocumentEvent e)  {onDocumentInsert(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {onDocumentChanged(e);}
		});
	}
	
	protected void onFocusGain(FocusEvent e) {}	
	protected void onFocusLost(FocusEvent e) {}
	
	protected void onMouseEntered(MouseEvent e) {}	
	protected void onMouseExited(MouseEvent e) {}
	
	protected void onMousePressed(MouseEvent e) {}	
	protected void onMouseReleased(MouseEvent e) {}
	protected void onMouseClicked(MouseEvent e) {}
	protected void onMouseRightClicked(MouseEvent e) {}
	
	protected void onMouseMoved(MouseEvent e) {}
	protected void onMouseDragged(MouseEvent e) {}

	protected void onDocumentRemove(DocumentEvent e) {}
	protected void onDocumentInsert(DocumentEvent e) {}
	protected void onDocumentChanged(DocumentEvent e) {}
	
	protected void createController() {
		this.adjusterController = new AdjusterController();
	}
	
	protected void createAdjuster() {
		
	}	
	
	public void addAdjuster(Adjuster adjuster) {
		this.adjusterController.addAdjuster(adjuster);
	}
	
	public Adjuster getAdjuster(String key) {
		return this.adjusterController.getAdjuster(key);
	}
	
	protected int getDoubleInt(String key) {
		return (int)((Adjuster_Double)this.getAdjuster(key)).getValue();
	}
	
	protected double getDouble(String key) {
		return ((Adjuster_Double)this.getAdjuster(key)).getValue();
	}
	
	protected Color getColor(String key) {
		return ((Adjuster_Color)this.getAdjuster(key)).getValue();
	}
	
	/**
	 * Update interface.
	 **/
	public void update() {
		this.adjusterController.update();
	}
	
	public void refresh() {
		this.setText(this.textKey == null ? null : LanguageModel.get(this.textKey) == null ? this.textKey : LanguageModel.get(this.textKey));
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Show drawing interface.
	 **/
	@Override
    protected void paintComponent(Graphics g) {
		Graphics2D graphics = (Graphics2D) g;
		GraphicsController.setHint(graphics);
		this.paint(graphics);
        super.paintComponent(g);
    }
	
	protected void paint(Graphics2D graphics) {

	}

	public void setTextKey(String textKey) {
		this.textKey = textKey;
		this.refresh();
	}
	
}
