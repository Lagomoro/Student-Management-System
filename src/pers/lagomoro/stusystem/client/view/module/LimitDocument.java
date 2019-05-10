package pers.lagomoro.stusystem.client.view.module;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

@SuppressWarnings("serial")
public class LimitDocument extends PlainDocument {
	
	protected int maxLength = -1;
	
	public LimitDocument(int maxLength) {
		this.maxLength = maxLength;
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
		if(this.getLength() + str.length() < this.maxLength) {
			super.insertString(offs, str, a);
			return;
		}else {
			char[] input = str.toCharArray();
			int inputLength;
			for(inputLength = 0; inputLength < str.length(); inputLength ++) {
				if(this.getLength() + inputLength >= this.maxLength) break;
			}
			super.insertString(offs, new String(input, 0, inputLength), a);
		}
	}
	
}
