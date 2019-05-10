package pers.lagomoro.stusystem.client.view.module;

import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit.StyledTextAction;

@SuppressWarnings("serial")
public class FontColorAction extends StyledTextAction{

	protected Color fg;

	public FontColorAction(Color fg) {
		super(StyleConstants.Foreground.toString());
		this.fg = fg;
	}
	
	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			Color fg = this.fg;
			if ((e != null) && (e.getSource() == editor)) {
				String s = e.getActionCommand();
				try {
					fg = Color.decode(s);
				} catch (NumberFormatException nfe) {
				}
			}
			if (fg != null) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setForeground(attr, fg);
				this.setCharacterAttributes(editor, attr, false);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(editor);
			}
		}
	}
	
	public void setColor(Color fg) {
		this.fg = fg;
	}
	
	public void fontColorAction(JEditorPane editor, Color color) {
		if (editor != null) {
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setForeground(attr, color);
			setCharacterAttributes(editor, attr, false);
		}
	}

}
