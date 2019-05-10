package pers.lagomoro.stusystem.client.view.module;

import java.awt.event.ActionEvent;

import javax.swing.JEditorPane;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.StyledEditorKit.StyledTextAction;

@SuppressWarnings("serial")
public class StrikeThroughAction extends StyledTextAction{

	public StrikeThroughAction(){ 
		super(StyleConstants.StrikeThrough.toString()); 
	} 
	
	@Override
	public void actionPerformed(ActionEvent e){ 
		JEditorPane editor = getEditor(e); 
		if (editor != null) { 
			StyledEditorKit kit = getStyledEditorKit(editor); 
			MutableAttributeSet attr = kit.getInputAttributes(); 
			SimpleAttributeSet sas = new SimpleAttributeSet(); 
			StyleConstants.setStrikeThrough(sas, !StyleConstants.isStrikeThrough(attr)); 
			this.setCharacterAttributes(editor, sas, false); 
		}
	} 
	
}
