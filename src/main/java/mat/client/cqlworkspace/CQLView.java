package mat.client.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import mat.client.cqlworkspace.shared.CQLEditorPanel;
import mat.client.cqlworkspace.shared.CQLEditor;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.shared.CQLError;

public class CQLView {
	
	
	private VerticalPanel cqlViewVP = new VerticalPanel();
	private Button exportErrorFile = new Button();
	private HTML heading = new HTML();
	private InAppHelp inAppHelp = new InAppHelp("");
	private CQLEditorPanel editorPanel = new CQLEditorPanel("viewCQL", "View CQL file here", false);
	
	public CQLView(){	
		cqlViewVP.clear();
		exportErrorFile.setType(ButtonType.PRIMARY);
		exportErrorFile.setIcon(IconType.DOWNLOAD);
		exportErrorFile.setText("Export Error File");
		exportErrorFile.setTitle("Click to download Export Error File.");
		exportErrorFile.setId("Button_exportErrorFile");
	}
	
	public VerticalPanel buildView(boolean isEditable){
		cqlViewVP.clear();
		cqlViewVP.getElement().setId("cqlViewCQL_Id");
		heading.addStyleName("leftAligned");
					
		cqlViewVP.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		cqlViewVP.add(new SpacerWidget());
		
		getCqlAceEditor().setReadOnly(!isEditable);
		if(isEditable) {
			cqlViewVP.add(exportErrorFile);
		}
		
		editorPanel.setSize("650px", "500px");
		cqlViewVP.add(editorPanel);

		cqlViewVP.setStyleName("cqlRightContainer");
		cqlViewVP.setWidth("700px");
		cqlViewVP.setStyleName("marginLeft15px");
		return cqlViewVP;
	}

	public CQLEditor getCqlAceEditor() {
		return editorPanel.getEditor();
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
