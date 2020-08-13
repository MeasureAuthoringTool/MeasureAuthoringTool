package mat.client.cqlworkspace.components;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.VerticalPanel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.cqlworkspace.GenericLeftNavSectionView;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.MatTextBox;
import mat.model.ComponentMeasureTabObject;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.PanelType;

public class CQLComponentLibraryView extends GenericLeftNavSectionView  {

	private static final String CQL_LIBRARY_VIEWER = "CQL Library Viewer";
	private static final String LABEL_WIDTH = "150px";
	private static final String LABEL_STYLE = "font-size:90%;margin-left:15px;";
	private static final String TEXT_BOX_STYLE = "margin-left:15px;margin-bottom:-15px;width:250px;height:32px;";
	
	private FormLabel measureLabel = new FormLabel();
	private FormLabel ownerLabel = new FormLabel ();
	private FormLabel libraryLabel = new FormLabel ();
	private MatTextBox measureTextBox = new MatTextBox();
	private MatTextBox ownerTextBox = new MatTextBox();
	private MatTextBox libraryTextBox = new MatTextBox();
	private AceEditor cqlAceEditor = new AceEditor();
	private InAppHelp inAppHelp = new InAppHelp("");
	public ManageMeasurePresenter mmp;
	
	public CQLComponentLibraryView() {
		super();
		setTextBoxes();
		setLabels();
		buildHeading();
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanelIncludeSection");
		verticalPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		verticalPanel.add(new SpacerWidget());	
		verticalPanel.add(getMessagePanel());
		verticalPanel.add(new SpacerWidget());
		
		FormGroup measureGroup = new FormGroup();
		measureGroup.add(measureLabel);
		measureGroup.add(measureTextBox);
		
		FormGroup ownerGroup = new FormGroup();
		ownerGroup.add(ownerLabel);
		ownerGroup.add(ownerTextBox);
		
		FormGroup libGroup = new FormGroup();
		libGroup.add(libraryLabel);
		libGroup.add(libraryTextBox);

		verticalPanel.add(measureGroup);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(ownerGroup);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(libGroup);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(buildAceEditor());
		verticalPanel.setWidth("700px");
		containerPanel.getElement().setAttribute("id", "ComponentSectionContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
	}
	
	private void buildHeading() {
		setHeading("CQL Workspace > Components", "ComponentsSectionContainerPanel");
		super.heading.addStyleName("leftAligned");
	}
	
	private void setLabels() {
		measureLabel.getElement().setAttribute("style", LABEL_STYLE);
		measureLabel.setWidth(LABEL_WIDTH);
		measureLabel.setFor("measureTextBox");
		measureLabel.setText("Measure Name");
		measureLabel.setTitle("Measure Name");
		
		ownerLabel.getElement().setAttribute("style", LABEL_STYLE);
		ownerLabel.setWidth(LABEL_WIDTH);
		ownerLabel.setFor("ownerTextBox");
		ownerLabel.setText("Owner Name");
		ownerLabel.setTitle("Owner Name");
		
		libraryLabel.getElement().setAttribute("style", LABEL_STYLE);
		libraryLabel.setWidth(LABEL_WIDTH);
		libraryLabel.setFor("libraryTextBox");		
		libraryLabel.setText("CQL Library Name");
		libraryLabel.setTitle("CQL Library Name");
	}
	
	private void setTextBoxes() {
		reset();
		
		measureTextBox.getElement().setAttribute("style", TEXT_BOX_STYLE);
		measureTextBox.setEnabled(false);
		measureTextBox.getElement().setId("measureTextBox");
		
		ownerTextBox.getElement().setAttribute("style", TEXT_BOX_STYLE);
		ownerTextBox.setEnabled(false);
		ownerTextBox.getElement().setId("ownerTextBox");
		
		libraryTextBox.getElement().setAttribute("style", TEXT_BOX_STYLE);
		libraryTextBox.setEnabled(false);
		libraryTextBox.getElement().setId("libraryTextBox");
	}
	
	private Panel buildAceEditor() {
		cqlAceEditor.startEditor();
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setText("");
		cqlAceEditor.setSize("650px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		cqlAceEditor.setUseWrapMode(true);
		cqlAceEditor.clearAnnotations();
		
		Label viewCQlFileLabel = new Label(LabelType.INFO);
		viewCQlFileLabel.setText(CQL_LIBRARY_VIEWER);
		viewCQlFileLabel.setTitle(CQL_LIBRARY_VIEWER);
		
		Panel viewCQLPanel = new Panel(PanelType.PRIMARY);	
		viewCQLPanel.setMarginTop(20);
		viewCQLPanel.setId("IncludeCQLViewPanel_Id");
		
		PanelHeader viewCQLHeader = new PanelHeader();
		viewCQLHeader.setText(CQL_LIBRARY_VIEWER);
		viewCQLHeader.setTitle(CQL_LIBRARY_VIEWER);
		viewCQLHeader.setId("IncludeCQLViewPanelHeader_id");
		
		PanelBody viewCQLBody = new PanelBody();
		viewCQLBody.setId("IncludeCQLViewBody_Id");
		viewCQLBody.add(cqlAceEditor);
		
		viewCQLPanel.add(viewCQLHeader);
		viewCQLPanel.add(viewCQLBody);
		
		return viewCQLPanel;
	}
	
	public void setHeading(String text, String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}
	
	public void setMeasureName(String name) {
		measureTextBox.setTitle(name);
		measureTextBox.setText(name);
	}
	
	public void setOwnerName(String name) {
		ownerTextBox.setTitle(name);
		ownerTextBox.setText(name);
	}
	
	public void setLibraryName(String name) {
		libraryTextBox.setTitle(name);
		libraryTextBox.setText(name);
	}
		
	public void reset() {
		setMeasureName("");
		setOwnerName("");
		setLibraryName("");
	}

	public void setPageInformation(ComponentMeasureTabObject componentMeasureTabObject) {
		setMeasureName(componentMeasureTabObject.getMeasureName());
		setOwnerName(componentMeasureTabObject.getOwnerName());
		setLibraryName(componentMeasureTabObject.getLibraryName());
		cqlAceEditor.setText(componentMeasureTabObject.getLibraryContent());
		
	}

	public void clearAceEditor() {
		cqlAceEditor.setText("");
		cqlAceEditor.redisplay();
	}
	
	public AceEditor getCQLAceEditor() {
		return cqlAceEditor;
	}
	
	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}

	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}

}