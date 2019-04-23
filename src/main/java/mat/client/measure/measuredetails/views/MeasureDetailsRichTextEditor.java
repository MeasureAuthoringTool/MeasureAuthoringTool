package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.shared.editor.RichTextEditor;

public class MeasureDetailsRichTextEditor {
	private RichTextEditor richTextEditor;
	
	public MeasureDetailsRichTextEditor(FlowPanel mainPanel) {
		richTextEditor = new RichTextEditor();
		HorizontalPanel infoPanel = new HorizontalPanel();
		HorizontalPanel textAreaPanel = new HorizontalPanel();
		textAreaPanel.add(richTextEditor);
        textAreaPanel.setWidth("625px");
        mainPanel.add(infoPanel);
        mainPanel.add(textAreaPanel);
	}

	public void setReadOnly(boolean readOnly) {
		richTextEditor.setEnabled(!readOnly);
	}
	
	public RichTextEditor getRichTextEditor() {
		return richTextEditor;
	}

	public void setRichTextEditor(RichTextEditor richTextEditor) {
		this.richTextEditor = richTextEditor;
	}
}
