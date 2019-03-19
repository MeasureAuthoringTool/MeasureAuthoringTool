package mat.client.shared.editor;

import org.gwtbootstrap3.client.ui.TextArea;

public class RichTextEditor extends TextArea {
	
	public RichTextEditor(){
		super();
		this.setCharacterWidth(100);
	    this.setVisibleLines(25);
	}
	
	public String getPlainText() {
		return this.getText();
	}

	public String getFormattedText() {
		return this.getText();
	}
	
	public void setEditorText(String text) {
		this.setText(text);
	}
}
