package mat.client.shared.editor;

import com.google.gwt.user.client.ui.RichTextArea;

public class RichTextEditor extends RichTextArea {
	
	public RichTextEditor(){
		super();
		this.setWidth("625px");
		this.setHeight("350px");
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
	
	public void setReadOnly(Boolean readOnly) {
		this.setEnabled(readOnly);
	}
}
