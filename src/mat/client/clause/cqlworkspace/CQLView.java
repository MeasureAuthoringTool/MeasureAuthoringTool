package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.buttons.BlueButton;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;

public class CQLView {
	
	
	VerticalPanel cqlViewVP = new VerticalPanel();

	private AceEditor cqlAceEditor = new AceEditor();
	
	private Button exportErrorFile = new BlueButton("Button_exportErrorFile", "Export Error File");
	
	
	HTML heading = new HTML();
	
	public CQLView(){
		cqlViewVP.clear();
		exportErrorFile.setIcon(IconType.DOWNLOAD);
		exportErrorFile.setTitle("Click to download Export Error File.");
		cqlAceEditor.startEditor();
	}
	
	public VerticalPanel buildView(boolean showExportButton){
		cqlViewVP.clear();
		cqlViewVP.getElement().setId("cqlViewCQL_Id");
		heading.addStyleName("leftAligned");
		Panel viewCQLPanel = new Panel(PanelType.PRIMARY);	
		viewCQLPanel.setMarginTop(20);
		viewCQLPanel.setId("ViewCQLPanel_Id");
		
		PanelHeader viewCQLHeader = new PanelHeader();
		viewCQLHeader.setText("View CQL file here");
		viewCQLHeader.setTitle("View CQL file here");
		viewCQLHeader.setId("ViewCQLPanelHeader_id");
		
		PanelBody viewCQLBody = new PanelBody();
		viewCQLBody.setId("ViewCQLPanelBody");
		viewCQLBody.add(cqlAceEditor);
		
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);

		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("650px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		cqlAceEditor.setUseWrapMode(true);
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.redisplay();
		
		viewCQLPanel.add(viewCQLHeader);
		
		viewCQLPanel.add(viewCQLBody);
		cqlViewVP.add(heading);
		cqlViewVP.add(new SpacerWidget());
		if(showExportButton) {
			cqlViewVP.add(exportErrorFile);
		}
		
		
		cqlViewVP.add(viewCQLPanel);
		cqlViewVP.setStyleName("cqlRightContainer");
		cqlViewVP.setWidth("700px");
		cqlViewVP.setStyleName("marginLeft15px");
		return cqlViewVP;
	}

	
	public AceEditor getCqlAceEditor() {
		return cqlAceEditor;
	}

	public void setCqlAceEditor(AceEditor cqlAceEditor) {
		this.cqlAceEditor = cqlAceEditor;
	}
	
	public void resetAll() {
		getCqlAceEditor().setText("");
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public Button getExportErrorFile() {
		return exportErrorFile;
	}

	public void setExportErrorFile(Button exportErrorFile) {
		this.exportErrorFile = exportErrorFile;
	}
}
