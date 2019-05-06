package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MeasureDetailsTextEditor extends Composite {
	private TextArea textEditor;
	
	public MeasureDetailsTextEditor() {
        initWidget(createMeasureDetailsEditor());
	}

	private VerticalPanel createMeasureDetailsEditor() {
		textEditor = new TextArea();
		textEditor.setWidth("100%");
		textEditor.setVisibleLines(22);
		
		
		VerticalPanel textAreaPanel = new VerticalPanel();
		textAreaPanel.add(textEditor);
        textAreaPanel.setWidth("625px");
        return textAreaPanel;
	}
	
	public String getText() {
		return textEditor.getText();
	}
	
	public void setText(String text) {
		textEditor.setText(text);
	}
	
	public void setTitle(String title) {
		textEditor.setTitle(title);
	}

	public void setReadOnly(boolean readOnly) {
		textEditor.setEnabled(!readOnly);
	}

	public TextArea getTextEditor() {
		return textEditor;
	}

	public void setTextEditor(TextArea textEditor) {
		this.textEditor = textEditor;
	}
}
