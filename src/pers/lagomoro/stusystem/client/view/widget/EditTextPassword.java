package pers.lagomoro.stusystem.client.view.widget;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;

import pers.lagomoro.stusystem.client.controller.module.Adjuster_Color;

@SuppressWarnings("serial")
public class EditTextPassword extends EditTextUserName{
	
	protected String realText = "";
	protected String passWord;
	public static final char CODE = '*';
	
	protected int refuseEvent = 0;
	protected int changeEnsured = -1;

	public EditTextPassword(){
		super();
	}
	
	public EditTextPassword(String text){
		super(text);
	}
	
	public EditTextPassword(String text, Color lostColor, Color focusColor){
		super(text, lostColor, focusColor);
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.enableInputMethods(false);
	}
	
	@Override
	public void cut() {
        if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        } else {
            super.cut();
        }
    }

	@Override
    public void copy() {
        if (getClientProperty("JPasswordField.cutCopyAllowed") != Boolean.TRUE) {
            UIManager.getLookAndFeel().provideErrorFeedback(this);
        } else {
            super.copy();
        }
    }
    
	@Override
	protected void onFocusLost(FocusEvent e) {
		super.onFocusLost(e);
		if(!this.isEmpty()) {
			ensureChange();
		}
	}

	@Override
	public void onMousePressed(MouseEvent e) {
		super.onMousePressed(e);
		if(e.getX() > this.getWidth() * 6 / 7) {
			this.refuseEvent = 2;
			this.setText(realText);
			this.setCaretPosition(this.getText().length());
		}else {
			this.ensureChange();				
		}
	}
	
	@Override
	public void onMouseDragged(MouseEvent e) {
		super.onMouseDragged(e);
		this.ensureChange();
	}
	
	@Override
	protected void onMouseReleased(MouseEvent e) {
		super.onMouseReleased(e);
		this.ensureChange();
	}
	
	@Override
	protected void onDocumentRemove(DocumentEvent e) {
		super.onDocumentRemove(e);
		if(refuseEvent > 0) {
			refuseEvent --;
		}else if(!isEmpty()) {
			realText = realText.substring(0, getText().length());
			passWord = "";
			for(int i = 0;i < realText.length(); i++) {
				passWord += CODE;
			}
		}else {
			realText = "";
		}
	}

	@Override
	protected void onDocumentInsert(DocumentEvent e) {
		super.onDocumentInsert(e);
		if(refuseEvent > 0) {
			refuseEvent --;
		}else if(!isEmpty() && realText.length() < getText().length()) {
			String text = getText();
			String lastChar = text.substring(realText.length(), text.length());
			passWord = "";
			for(int i = 0;i < realText.length(); i++) {
				passWord += CODE;
			}
			passWord += lastChar;
			realText += lastChar;
			changeEnsured = 120;
		}
	}
	
	@Override
	public void update() {
		super.update();
		if(this.refuseEvent > 0) {
			this.refuseEvent = 0;
		}
		if(passWord != null) {
			this.refuseEvent = 2;
			this.setText(passWord);
			this.setCaretPosition(this.getText().length());
			passWord = null;
		}
		if(this.changeEnsured > 0) {
			this.changeEnsured --;
		}else if(this.changeEnsured == 0){
			this.ensureChange();
		}
	}
	
	protected void ensureChange() {
		this.changeEnsured = -1;
		this.setCaretPosition(this.getText().length());
		passWord = "";
		for(int i = 0;i < realText.length(); i++) {
			passWord += CODE;
		}
	}
	
	@Override
	protected void paint(Graphics2D graphics) {
		super.paint(graphics);
		graphics.setColor(((Adjuster_Color)getAdjuster("Color")).getValue());
		if(!this.isEmpty()) {
			graphics.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
			graphics.drawOval(this.getWidth() * 6 / 7 + 5, this.getHeight()/2 - 8, 16, 16);
			graphics.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
			graphics.fillOval(this.getWidth() * 6 / 7 + 11, this.getHeight()/2 - 2, 4, 4);
		}
	}

	public String getRealText() {
		return this.realText;
	}
}
