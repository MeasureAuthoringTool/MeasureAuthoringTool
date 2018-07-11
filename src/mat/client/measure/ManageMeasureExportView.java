package mat.client.measure;

import mat.client.buttons.CancelButton;
import mat.client.buttons.SaveButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;
import mat.model.SecurityRole;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureExportView implements ExportDisplay {
	
	private FlowPanel content = new FlowPanel();
	
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	private RadioButton simpleXMLRadio = new RadioButton("format", "SimpleXML");
	
	private RadioButton hqmfRadio = new RadioButton("format", "HQMF");
	
	private RadioButton humanReadableRadio = new RadioButton("format", "Human Readable");
	
	private RadioButton eCQMPackageRadio = new RadioButton("format", "eCQM Package");
	
	private RadioButton elmRadio = new RadioButton("format", "ELM"); 
	
	private RadioButton jsonRadio = new RadioButton("format", "JSON");
	
	private RadioButton cqlLibraryRadio = new RadioButton("format", "CQL Library");
	
	private Button saveButton = new SaveButton("manageMeasureexportview");
	
	private Button cancelButton = new CancelButton("manageMeasureexportview");
	
	private Button openButton = new Button("Open");
	
	VerticalPanel vp = new VerticalPanel();

	public ManageMeasureExportView(boolean isTopLevelUser) {
		
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		content.add(measureNameLabel);
		content.add(new Label("Select an export option"));
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

	@Override
	public Widget asWidget() {
		return content;
	}
	
	@Override
	public void setVersion_Based_ExportOptions(String releaseVersion) {
		
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
		resetRadioButtonValues();
	}

	private void resetRadioButtonValues() {
		simpleXMLRadio.setValue(false);
		hqmfRadio.setValue(false);
		humanReadableRadio.setValue(false);
		cqlLibraryRadio.setValue(false);
		elmRadio.setValue(false);
		jsonRadio.setValue(false);
		eCQMPackageRadio.setValue(true);
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
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
}
