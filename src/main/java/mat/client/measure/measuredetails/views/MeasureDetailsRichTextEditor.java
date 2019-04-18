package mat.client.measure.measuredetails.views;

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.editor.RichTextEditor;
import mat.client.shared.editor.RichTextToolbar;

public class MeasureDetailsRichTextEditor {
	private RichTextEditor richTextEditor;
	private RichTextToolbar toolBar;
	private boolean isDisabled;
	
	public MeasureDetailsRichTextEditor(FlowPanel mainPanel) {
		richTextEditor = new RichTextEditor();
		richTextEditor.addKeyDownHandler(event -> checkForDisableEditor(event));
		
		toolBar = new RichTextToolbar(richTextEditor);
		toolBar.setWidth("100%");
		richTextEditor.addClickHandler(event -> richTextEditor.getFormatter().setFontName("Arial"));
		HorizontalPanel infoPanel = new HorizontalPanel();
		VerticalPanel textAreaPanel = new VerticalPanel();
		textAreaPanel.add(toolBar);
		textAreaPanel.add(richTextEditor);
        textAreaPanel.setWidth("625px");
        mainPanel.add(infoPanel);
        mainPanel.add(textAreaPanel);
	}

	private void checkForDisableEditor(KeyDownEvent event) {
		if(isDisabled) {
			event.preventDefault();
		}
	}

	public void setReadOnly(boolean readOnly) {
		isDisabled = readOnly;
		richTextEditor.setEnabled(!readOnly);
		toolBar.setReadOnly(!readOnly);
	}
	
	public RichTextEditor getRichTextEditor() {
		return richTextEditor;
	}

	public void setRichTextEditor(RichTextEditor richTextEditor) {
		this.richTextEditor = richTextEditor;
	}

	public RichTextToolbar getToolBar() {
		return toolBar;
	}

	public void setToolBar(RichTextToolbar toolBar) {
		this.toolBar = toolBar;
	}
}
