package pers.lagomoro.stusystem.client.view.widget;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;

@SuppressWarnings("serial")
public class TextPaneHTMLEditor extends TextPaneStyled{
	
	public TextPaneHTMLEditor() {
		super("text/html");
	}
	
	@Override
	protected void initialize() {
		super.initialize();
		this.setEditorKit(new HTMLEditorKit());
		this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
	}
	
	public void setFile(String filepath) {
        try {
			File file = new File(filepath);
			InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
			BufferedReader input = new BufferedReader(reader);
			String line = input.readLine();
			String temp = "";
			while(line != null) {
				temp += line + "\r\n";
				line = input.readLine();
			}
			this.setText(temp);
			input.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
}
