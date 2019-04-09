package mat.client.cqlworkspace.shared;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.user.client.ui.Composite;

public class CQLEditorPanel extends Composite {

	private PanelHeader header;
	private String text;
	private CQLEditor editor;
	private PanelCollapse panelCollapse;
	private String name;
	private PanelGroup panelGroup;

	public CQLEditorPanel(String name, String text, boolean isReadOnly) {
		this.text = text;
		this.name = name;
		this.editor = new CQLEditor(isReadOnly);
		this.editor.getElement().getElementsByTagName("textarea").getItem(0).setTitle(text);
		initWidget(buildWidget());
	}
	
	private PanelGroup buildWidget() {
		panelGroup = new PanelGroup();
		panelGroup.setId(name + "_panelGroup");
		
		Panel panel = new Panel(PanelType.PRIMARY);
		panel.setId(name + "_panel");
		header = new PanelHeader();
		setHeaderText(this.text);
		
		panelCollapse = new PanelCollapse();
		panelCollapse.setId(name + "_panelCollapse");
		
		PanelBody body = new PanelBody();
		body.add(this.editor);
		
		panelCollapse.add(body);
	
		panel.add(header);
		panel.add(panelCollapse);
		panelCollapse.setIn(true);
		
		this.editor.getElement().setAttribute("id", name + "_CQLEditor");
		panel.setId(name + "_panel");	
		panelGroup.add(panel);
		
		return panelGroup;
	}
	
	@Override
	public void setSize(String width, String height) {
		setEditorSize(width, height);
	}
	
	public void setCollabsable() {
		setPanelCollapsed(true);
		header.clear();
		Anchor anchor = new Anchor();
		anchor.setText(this.text);
		anchor.setTitle(this.text);
		anchor.setColor("white");
		anchor.setDataParent("#" + name + "_panelGroup");
		anchor.setHref("#" + name + "_panelCollapse");
		anchor.setDataToggle(Toggle.COLLAPSE);
		header.add(anchor);
	}
	
	public CQLEditor getEditor() {
		return editor;
	}
	
	public PanelCollapse getPanelCollapse() {
		return this.panelCollapse;
	}
	
	public PanelGroup getPanelGroup() {
		return this.panelGroup;
	}
	
	public void setPanelCollapsed(boolean isCollapse) {
		this.panelCollapse.setIn(!isCollapse);
	}
	
	public void setHeaderText(String text) {
		this.header.setText(text);
		this.header.setTitle(text);
	}
	
	public void setEditorSize(String width, String height) {
		this.editor.setSize(width, height);
	}
}
