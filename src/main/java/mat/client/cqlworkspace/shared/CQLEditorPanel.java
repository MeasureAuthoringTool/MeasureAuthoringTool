package mat.client.cqlworkspace.shared;

import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.gwt.Widget;

import com.google.gwt.user.client.ui.Composite;

public class CQLEditorPanel extends Composite {

	private Panel panel;
	private PanelHeader header;
	private PanelBody body;
	private String text;
	private MATAceEditor editor;

	public CQLEditorPanel(String text, boolean isReadOnly) {
		this.text = text;
		this.editor = new MATAceEditor(isReadOnly);
		initWidget(buildWidget());
	}
	
	private Panel buildWidget() {
		panel = new Panel(PanelType.PRIMARY);
		header = new PanelHeader();
		setHeaderText(this.text);
		
		body = new PanelBody();
		body.add(this.editor);
		
		panel.add(header);
		panel.add(body);
		
		return panel;
	}
	
	public MATAceEditor getEditor() {
		return editor;
	}
	
	public void setHeaderText(String text) {
		this.header.setText(text);
		this.header.setTitle(text);
	}
	
	public void setEditorSize(String width, String height) {
		this.editor.setSize(width, height);
	}
}
