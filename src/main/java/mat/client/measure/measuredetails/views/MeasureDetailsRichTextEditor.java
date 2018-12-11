package mat.client.measure.measuredetails.views;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.buttons.InfoToolBarButton;
import mat.client.buttons.RichTextEditorInfoDropDownMenu;
import mat.client.shared.editor.RichTextEditor;
import mat.client.shared.editor.RichTextToolbar;

public class MeasureDetailsRichTextEditor {
	private RichTextEditor richTextEditor;
	private ButtonGroup infoButtonGroup = new ButtonGroup();
	private Button infoButton;
	
	public MeasureDetailsRichTextEditor(FlowPanel mainPanel) {
		richTextEditor = new RichTextEditor();
		//RichTextToolbar toolBar = new RichTextToolbar(richTextEditor);
		//toolBar.setWidth("100%");
		HorizontalPanel infoPanel = new HorizontalPanel();
		buildInfoButtonGroup();
		infoPanel.add(infoButtonGroup);
		VerticalPanel textAreaPanel = new VerticalPanel();
		//textAreaPanel.add(toolBar);
		textAreaPanel.add(richTextEditor);
		
        
        textAreaPanel.setWidth("95%");
        mainPanel.add(infoPanel);
        mainPanel.add(textAreaPanel);
	}
	
	private void buildInfoButtonGroup() {
		infoButton = new InfoToolBarButton("");
		DropDownMenu dropDownMenu = new RichTextEditorInfoDropDownMenu();
		
		infoButtonGroup.getElement().setAttribute("class", "btn-group");
		infoButtonGroup.add(infoButton);
		infoButtonGroup.add(dropDownMenu);
		infoButtonGroup.getElement().setAttribute("style", "margin-top:-10px;");
	}

	public void setReadOnly(boolean readOnly) {
		richTextEditor.setEnabled(!readOnly);
		infoButton.setEnabled(!readOnly);
	}
	
	public RichTextEditor getRichTextEditor() {
		return richTextEditor;
	}

	public void setRichTextEditor(RichTextEditor richTextEditor) {
		this.richTextEditor = richTextEditor;
	}
}
