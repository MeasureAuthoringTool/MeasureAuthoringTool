package mat.client.export.measure;

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
import mat.client.shared.MessagePanel;
import mat.client.shared.SpacerWidget;
import mat.client.util.FeatureFlagConstant;
import mat.model.SecurityRole;
import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

public class ManageMeasureExportView implements ExportDisplay {

	private FlowPanel content = new FlowPanel();
	
	private Button measureNameLink = new Button();

	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	private RadioButton simpleXMLRadio = new RadioButton("format", "SimpleXML");

	private RadioButton xmlRadio = new RadioButton("format", "XML");

	private RadioButton allRadio = new RadioButton("format", "All");

	private final RadioButton transferToMadieRadio = new RadioButton("format", "Transfer To MADiE");

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

	private final MessagePanel messagePanel;

	public ManageMeasureExportView() {
		
		content.add(new SpacerWidget());
		createMeasureInformationContent();
		content.add(new SpacerWidget());

		messagePanel = new MessagePanel();
		messagePanel.setWidth("625px");
		content.add(messagePanel);
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
		measureNameLink.setStyleName("btn-wrap-text", true);
		
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
	public void setExportOptionsBasedOnVersion(String releaseVersion, boolean isCompositeMeasure, String measureModel, String currentMeasureOwnerId) {

		vp.clear();

		if (MatContext.get().getLoggedInUserRole().equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)) {
			vp.add(simpleXMLRadio);
		}

		if (ModelTypeHelper.FHIR.equalsIgnoreCase(measureModel)
				&& MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
			vp.add(xmlRadio);
			vp.add(jsonRadio);
			vp.add(humanReadableRadio);
			vp.add(allRadio);
			// Transfer to Madie is displayed only for the measure owner
			if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MADIE)
					&& currentMeasureOwnerId.equals(MatContext.get().getCurrentUserInfo().userId)) {
				vp.add(transferToMadieRadio);
			}
		} else {
			vp.add(humanReadableRadio);
			vp.add(hqmfRadio);
			if (isV5OrGreater(releaseVersion)) {
				vp.add(cqlLibraryRadio);
				vp.add(elmRadio);
				vp.add(jsonRadio);
			}
			vp.add(eCQMPackageRadio);
			vp.add(compositeMeasurePackageRadio);
		}
		resetRadioButtonValues(isCompositeMeasure);
	}

	private boolean isV5OrGreater(String version) {
		boolean result = false;
		try {
			result = Double.parseDouble(version.substring(1,version.length())) >= 5.0;
		} catch (NullPointerException|NumberFormatException re) {
			//Just eat it.
		}
		return result;
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
		xmlRadio.setValue(false);
		allRadio.setValue(false);
		transferToMadieRadio.setValue(false);
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

	@Override
	public boolean isXml() {
		return xmlRadio.getValue();
	}

	@Override
	public boolean isAll() {
		return allRadio.getValue();
	}

	@Override
	public boolean isTransferToMadieRadio() {
		return transferToMadieRadio.getValue();
	}

	public void displaySuccessMessage(String message) {
		messagePanel.clearAlerts();
		messagePanel.getSuccessMessageAlert().createAlert(message);
	}

	public void displayErrorMessage(String message) {
		messagePanel.clearAlerts();
		messagePanel.getErrorMessageAlert().createAlert(message);
	}

	public void clearAlerts() {
		messagePanel.clearAlerts();
	}
}
