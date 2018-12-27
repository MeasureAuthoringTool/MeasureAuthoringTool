package mat.client.measure.measuredetails.views;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.DropDownMenu;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;

import mat.client.buttons.InfoToolBarButton;
import mat.client.buttons.RichTextEditorInfoDropDownMenu;
import mat.client.shared.editor.RichTextEditor;

public class MeasureDetailsRichTextEditor {
	private RichTextEditor richTextEditor;
	private ButtonGroup infoButtonGroup = new ButtonGroup();
	private Button infoButton;
	
	public MeasureDetailsRichTextEditor(FlowPanel mainPanel) {
		richTextEditor = new RichTextEditor();
		HorizontalPanel infoPanel = new HorizontalPanel();
		buildInfoButtonGroup();
		infoPanel.add(infoButtonGroup);
		HorizontalPanel textAreaPanel = new HorizontalPanel();
		textAreaPanel.add(richTextEditor);
        textAreaPanel.setWidth("625px");
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
