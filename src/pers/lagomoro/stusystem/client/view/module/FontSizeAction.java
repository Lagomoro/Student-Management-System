package pers.lagomoro.stusystem.client.view.module;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit.StyledTextAction;

@SuppressWarnings("serial")
public class FontSizeAction extends StyledTextAction{

	protected int size;

	public FontSizeAction(int size) {
		super(StyleConstants.FontSize.toString());
		this.size = size;
	}
	
	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			int size = this.size;
			if ((e != null) && (e.getSource() == editor)) {
				String s = e.getActionCommand();
				try {
					size = Integer.parseInt(s, 10);
				} catch (NumberFormatException nfe) {
				}
			}
			if (size != 0) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setFontSize(attr, size);
				setCharacterAttributes(editor, attr, false);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(editor);
			}
		}
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public void fontSizeAction(JEditorPane editor, int size) {
		if (editor != null) {
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setFontSize(attr, size);
			setCharacterAttributes(editor, attr, false);
		}
	}

}
