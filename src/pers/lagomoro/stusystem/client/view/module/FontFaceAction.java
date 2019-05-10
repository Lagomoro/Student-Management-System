package pers.lagomoro.stusystem.client.view.module;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.UIManager;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit.StyledTextAction;

@SuppressWarnings("serial")
public class FontFaceAction extends StyledTextAction {
	
	protected String family;
	
	public FontFaceAction(String family) {
		super(StyleConstants.FontFamily.toString());
		this.family = family;
	}
	
	public void actionPerformed(ActionEvent e) {
		JEditorPane editor = getEditor(e);
		if (editor != null) {
			String family = this.family;
			if ((e != null) && (e.getSource() == editor)) {
				String s = e.getActionCommand();
				if (s != null) {
					family = s;
				}
			}
			if (family != null) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				StyleConstants.setFontFamily(attr, family);
				setCharacterAttributes(editor, attr, false);
			} else {
				UIManager.getLookAndFeel().provideErrorFeedback(editor);
			}
		}
	}
	
	public void setFace(String family) {
		this.family = family;
	}
	
	public void fontFaceAction(JEditorPane editor, String family) {
		if (editor != null) {
			MutableAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setFontFamily(attr, family);
			setCharacterAttributes(editor, attr, false);
		}
	}
}