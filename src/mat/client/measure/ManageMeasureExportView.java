package mat.client.measure;

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

/**
 * The Class ManageMeasureExportView.
 */
public class ManageMeasureExportView implements ManageMeasurePresenter.ExportDisplay {
	
	/** The content. */
	private FlowPanel content = new FlowPanel();
	
	/** The measure name label. */
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	/** The error messages. */
	private MessageAlert errorMessages = new ErrorMessageAlert();
	
	/** The simple xml radio. */
	private RadioButton simpleXMLRadio = new RadioButton("format", "SimpleXML");
	
	/** The e measure radio. */
	private RadioButton eCQMRadio = new RadioButton("format", "eCQM");
	
	/** The code list radio. */
	//private RadioButton codeListRadio = new RadioButton("format", "Measure Value Set");
	
	/** The e measure package radio. */
	private RadioButton eCQMPackageRadio = new RadioButton("format", "eCQM Package");
	
	/** The ELM radio */
	private RadioButton elmRadio = new RadioButton("format", "ELM"); 
	
	/** The ELM radio */
	private RadioButton jsonRadio = new RadioButton("format", "JSON");
	
	private RadioButton cqlLibraryRadio = new RadioButton("format", "CQL Library");
	
	/** The save button. */
	private Button saveButton = new Button("Save");
	
	/** The cancel button. */
	private Button cancelButton = new Button("Cancel");
	
	/** The open button. */
	private Button openButton = new Button("Open");
	
	VerticalPanel vp = new VerticalPanel();
	

	/**
	 * Instantiates a new manage measure export view.
	 * 
	 * @param isTopLevelUser
	 *            the is top level user
	 */
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
		saveButton.setType(ButtonType.PRIMARY);
		cancelButton.setType(ButtonType.DANGER);
		
		openButton.setType(ButtonType.PRIMARY);
		saveButton.setTitle("Save");
		cancelButton.setTitle("Cancel");
		openButton.setTitle("Open");
		buttonPanel.add(saveButton);
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);
		
		content.add(buttonPanel);
		content.add(new SpacerWidget());
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
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
		vp.add(eCQMRadio);
		
		if(releaseVersion.startsWith("v5")){
			vp.add(cqlLibraryRadio);
			vp.add(elmRadio);
			vp.add(jsonRadio);
		}				
		vp.add(eCQMPackageRadio);	
		eCQMPackageRadio.setValue(true);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#setMeasureName(java.lang.String)
	 */
	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#getErrorMessageDisplay()
	 */
	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return saveButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return cancelButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#getOpenButton()
	 */
	@Override
	public HasClickHandlers getOpenButton() {
		return openButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isSimpleXML()
	 */
	@Override
	public boolean isSimpleXML() {
		return simpleXMLRadio.getValue();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isEMeasure()
	 */
	@Override
	public boolean isEMeasure() {
		return eCQMRadio.getValue();
	}
	
	/* (non-Javadoc
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isELM()
	 */
	public boolean isELM() {
		return elmRadio.getValue(); 
	}
	
	/* (non-Javadoc
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isJSON()
	 */
	public boolean isJSON() {
		return jsonRadio.getValue(); 
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isCodeList()
	 */
	/*@Override
	public boolean isCodeList() {
		return codeListRadio.getValue();
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isEMeasurePackage()
	 */
	@Override
	public boolean isEMeasurePackage() {
		return eCQMPackageRadio.getValue();
	}
	
	@Override
	public boolean isCQLLibrary() {
		return cqlLibraryRadio.getValue();
	}

	
}
