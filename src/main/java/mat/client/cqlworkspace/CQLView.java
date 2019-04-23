package mat.client.cqlworkspace;

import java.util.List;

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

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLError;

public class CQLView {
	
	
	VerticalPanel cqlViewVP = new VerticalPanel();

	private AceEditor cqlAceEditor = new AceEditor();
	
	private Button exportErrorFile = new Button();
	
	HTML heading = new HTML();
	
	private InAppHelp inAppHelp = new InAppHelp("");
	
	public CQLView(){
		cqlViewVP.clear();
		exportErrorFile.setType(ButtonType.PRIMARY);
		exportErrorFile.setIcon(IconType.DOWNLOAD);
		exportErrorFile.setText("Export Error File");
		exportErrorFile.setTitle("Click to download Export Error File.");
		exportErrorFile.setId("Button_exportErrorFile");
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
		cqlViewVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
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
	
	public void setViewCQLAnnotations(List<CQLError> cqlErrors, String prefix, AceAnnotationType aceAnnotationType) {
		for (CQLError error : cqlErrors) {
			int line = error.getErrorInLine();
			int column = error.getErrorAtOffeset();
			this.getCqlAceEditor().addAnnotation(line - 1, column, prefix + error.getErrorMessage(), aceAnnotationType);
		}
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}

	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}
}
