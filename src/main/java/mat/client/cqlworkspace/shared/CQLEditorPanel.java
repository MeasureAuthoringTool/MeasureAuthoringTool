package mat.client.cqlworkspace.shared;

import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.user.client.ui.Composite;

public class CQLEditorPanel extends Composite {

	private Panel panel;
	private PanelHeader header;
	private PanelBody body;
	private String text;
	private CQLEditor editor;

	public CQLEditorPanel(String text, boolean isReadOnly) {
		this.text = text;
		this.editor = new CQLEditor(isReadOnly);
		this.editor.getElement().getElementsByTagName("textarea").getItem(0).setTitle(text);
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
		
		panel.setMarginBottom(-10.00);
		return panel;
	}
	
	@Override
	public void setSize(String width, String height) {
		setEditorSize(width, height);
	}
	
	public CQLEditor getEditor() {
		return editor;
	}
	
	public void setHeaderText(String text) {
		this.header.setText(text);
		this.header.setTitle(text);
	}
	
	public void setEditorSize(String width, String height) {
		this.editor.setSize(width, height);
	}
	
	/**
	 * Set's id for all of the different elements
	 * @param name the name of the editor
	 */
	public void setId(String name) {
		this.editor.getElement().setAttribute("id", name + "_CQLEditor");
		this.panel.setId(name + "_panel");
	}
}
