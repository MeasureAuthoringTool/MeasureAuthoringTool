package mat.client.export.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.CancelButton;
import mat.client.buttons.SaveButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.model.SecurityRole;

public class ManageMeasureExportView implements ExportDisplay {
	
	private FlowPanel content = new FlowPanel();
	
	private Button measureNameLink = new Button();

	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	private RadioButton simpleXMLRadio = new RadioButton("format", "SimpleXML");
	
	private RadioButton hqmfRadio = new RadioButton("format", "HQMF");
	
	private RadioButton humanReadableRadio = new RadioButton("format", "Human Readable");
	
	private RadioButton eCQMPackageRadio = new RadioButton("format", "eCQM Package");
	
	private RadioButton elmRadio = new RadioButton("format", "ELM"); 
	
	private RadioButton jsonRadio = new RadioButton("format", "JSON");
	
	private RadioButton cqlLibraryRadio = new RadioButton("format", "CQL Library");
	
	private RadioButton compositeMeasurePackageRadio = new RadioButton("format", "eCQM Package");
	
	private Button saveButton = new SaveButton("manageMeasureexportview");
	
	private Button cancelButton = new CancelButton("manageMeasureexportview");
	
	private Button openButton = new Button("Open");
	
	VerticalPanel vp = new VerticalPanel();

	public ManageMeasureExportView(boolean isTopLevelUser) {
		
		content.add(new SpacerWidget());
		createMeasureInformationContent();
		content.add(new SpacerWidget());

		FormLabel label = new FormLabel();
		label.setText("Select an export option: "); 
		label.setTitle("Select an export option: ");
		content.add(label);
		content.add(new SpacerWidget());
		
		content.add(vp);
			
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());

		ButtonToolBar buttonPanel = new ButtonToolBar();
		
		openButton.setType(ButtonType.PRIMARY);
		openButton.setTitle("Open");
		buttonPanel.add(saveButton);
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);
		
		content.add(buttonPanel);
		content.add(new SpacerWidget());
		
	}
	
	public void createMeasureInformationContent() {
		measureNameLink.getElement().setId("measureNameLabel");
		measureNameLink.setType(ButtonType.LINK);
		
		FormGroup group = new FormGroup();
		FormLabel measureNameLabel = new FormLabel();
		measureNameLabel.setText("Measure: ");
		measureNameLabel.setTitle("Measure: ");
		measureNameLabel.setFor("measureNameLabel");
		
		HorizontalPanel panel = new HorizontalPanel();
		group.add(measureNameLabel);
		group.add(measureNameLink);
		panel.add(group);
		
		content.add(panel);
	}

	@Override
	public Widget asWidget() {
		return content;
	}
	
	@Override
	public void setExportOptionsBasedOnVersion(String releaseVersion, boolean isCompositeMeasure) {
		
		vp.clear();
		
		if(MatContext.get().getLoggedInUserRole().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)) {
			vp.add(simpleXMLRadio);
		}
		
		vp.add(humanReadableRadio);
		vp.add(hqmfRadio);
		if(releaseVersion.startsWith("v5")){
			vp.add(cqlLibraryRadio);
			vp.add(elmRadio);
			vp.add(jsonRadio);
		}				
		vp.add(eCQMPackageRadio);
		vp.add(compositeMeasurePackageRadio);
		resetRadioButtonValues(isCompositeMeasure);
	}
	
	@Override
	public void showCompositeMeasure(boolean isComposite) {
		simpleXMLRadio.setVisible(!isComposite);
		hqmfRadio.setVisible(!isComposite);
		humanReadableRadio.setVisible(!isComposite);
		cqlLibraryRadio.setVisible(!isComposite);
		elmRadio.setVisible(!isComposite);
		jsonRadio.setVisible(!isComposite);
		eCQMPackageRadio.setVisible(!isComposite);
		compositeMeasurePackageRadio.setVisible(isComposite);
	}

	private void resetRadioButtonValues(boolean isComposite) {
		simpleXMLRadio.setValue(false);
		hqmfRadio.setValue(false);
		humanReadableRadio.setValue(false);
		cqlLibraryRadio.setValue(false);
		elmRadio.setValue(false);
		jsonRadio.setValue(false);
		eCQMPackageRadio.setValue(!isComposite);
		compositeMeasurePackageRadio.setValue(isComposite);
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	@Override
	public HasClickHandlers getOpenButton() {
		return openButton;
	}

	@Override
	public boolean isSimpleXML() {
		return simpleXMLRadio.getValue();
	}

	public boolean isHQMF() {
		return hqmfRadio.getValue(); 
	}

	public boolean isHumanReadable() {
		return humanReadableRadio.getValue(); 
	}

	public boolean isELM() {
		return elmRadio.getValue(); 
	}
	
	public boolean isJSON() {
		return jsonRadio.getValue(); 
	}

	@Override
	public boolean isEMeasurePackage() {
		return eCQMPackageRadio.getValue();
	}

	@Override
	public boolean isCQLLibrary() {
		return cqlLibraryRadio.getValue();
	}
	
	@Override
	public boolean isCompositeMeasurePackageRadio() {
		return compositeMeasurePackageRadio.getValue();
	}
	
	public Button getMeasureNameLink() {
		return measureNameLink;
	}
}
