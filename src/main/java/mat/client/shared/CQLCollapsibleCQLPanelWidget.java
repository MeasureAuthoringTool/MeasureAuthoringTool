package mat.client.shared;

import com.google.gwt.dom.client.Style.Unit;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.PanelGroup;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;

public class CQLCollapsibleCQLPanelWidget {
	
	private PanelCollapse panelViewCQLCollapse = new PanelCollapse();
	private AceEditor viewCQLAceEditor = new AceEditor();
	private Anchor viewCQLAnchor = new Anchor();
	
	/**
	 * Method to build collapsible View CQL Panel with Ace Editor.
	 * 
	 * @return PanelGroup
	 */
	public PanelGroup buildViewCQLCollapsiblePanel() {
		PanelGroup panelGroup = new PanelGroup();
		panelGroup.setId("panelGroup");
		Panel panel = new Panel(PanelType.PRIMARY);
		PanelHeader header = new PanelHeader();

		header.add(viewCQLAnchor);

		panelViewCQLCollapse.setId("panelCollapse");
		PanelBody body = new PanelBody();

		viewCQLAceEditor.setMode(AceEditorMode.CQL);
		viewCQLAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		viewCQLAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		viewCQLAceEditor.setSize("655px", "200px");
		viewCQLAceEditor.setAutocompleteEnabled(true);
		viewCQLAceEditor.addAutoCompletions();
		viewCQLAceEditor.setUseWrapMode(true);
		viewCQLAceEditor.removeAllMarkers();
		viewCQLAceEditor.clearAnnotations();
		viewCQLAceEditor.redisplay();
		viewCQLAceEditor.getElement().setAttribute("id", "Define_ViewAceEditorID");
		viewCQLAceEditor.setReadOnly(true);

		body.add(viewCQLAceEditor);
		panelViewCQLCollapse.add(body);

		panel.add(header);
		panel.add(panelViewCQLCollapse);

		panelGroup.add(panel);

		return panelGroup;

	}

	public PanelCollapse getPanelViewCQLCollapse() {
		return panelViewCQLCollapse;
	}

	public AceEditor getViewCQLAceEditor() {
		return viewCQLAceEditor;
	}

	public Anchor getViewCQLAnchor() {
		return viewCQLAnchor;
	}

}
