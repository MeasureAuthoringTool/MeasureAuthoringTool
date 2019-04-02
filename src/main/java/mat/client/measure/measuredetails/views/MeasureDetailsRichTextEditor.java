package mat.client.measure.measuredetails.views;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.editor.RichTextEditor;
import mat.client.shared.editor.RichTextToolbar;

public class MeasureDetailsRichTextEditor {
	private RichTextEditor richTextEditor;
	private RichTextToolbar toolBar;
	
	public MeasureDetailsRichTextEditor(FlowPanel mainPanel) {
		richTextEditor = new RichTextEditor();
		toolBar = new RichTextToolbar(richTextEditor);
		toolBar.setWidth("100%");
		HorizontalPanel infoPanel = new HorizontalPanel();
		VerticalPanel textAreaPanel = new VerticalPanel();
		textAreaPanel.add(toolBar);
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
