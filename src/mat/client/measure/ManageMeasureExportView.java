package mat.client.measure;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.MessageAlert;
import mat.client.shared.SpacerWidget;

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
	private RadioButton eMeasureRadio = new RadioButton("format", "eMeasure");
	
	/** The code list radio. */
	//private RadioButton codeListRadio = new RadioButton("format", "Measure Value Set");
	
	/** The e measure package radio. */
	private RadioButton eMeasurePackageRadio = new RadioButton("format", "eMeasure Package");
	
	/** The ELM radio */
	private RadioButton elmRadio = new RadioButton("format", "ELM"); 
	
	private RadioButton cqlLibraryRadio = new RadioButton("format", "CQL Library");
	
	/** The save button. */
	private Button saveButton = new Button("Save");
	
	/** The cancel button. */
	private Button cancelButton = new Button("Cancel");
	
	/** The open button. */
	private Button openButton = new Button("Open");
	

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
		VerticalPanel vp = new VerticalPanel();
		
		if(isTopLevelUser) {
			vp.add(simpleXMLRadio);
		}
		vp.add(eMeasureRadio);
		vp.add(cqlLibraryRadio);
		vp.add(elmRadio);
		vp.add(eMeasurePackageRadio);
		content.add(vp);
		eMeasurePackageRadio.setValue(true);
		/*content.add(wrapRadioButton(eMeasureRadio));
		//content.add(wrapRadioButton(codeListRadio));
		content.add(wrapRadioButton(cqlLibraryRadio));
		content.add(wrapRadioButton(elmRadio));
		content.add(wrapRadioButton(eMeasurePackageRadio));*/
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());

		/*FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("measureExportButtonContainer");*/
		ButtonToolBar buttonPanel = new ButtonToolBar();
		saveButton.setType(ButtonType.PRIMARY);
		cancelButton.setType(ButtonType.DANGER);
		//openButton.setStyleName("btn btn-gray");;
		openButton.setType(ButtonType.PRIMARY);
		saveButton.setTitle("Save");
		cancelButton.setTitle("Cancel");
		openButton.setTitle("Open");
		buttonPanel.add(saveButton);
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);
		//cancelButton.addStyleName("cancelButton");
		content.add(buttonPanel);
		content.add(new SpacerWidget());
		
	}
	
	/**
	 * Wrap radio button.
	 * 
	 * @param w
	 *            the w
	 * @return the widget
	 */
	private Widget wrapRadioButton(RadioButton w) {
		SimplePanel p = new SimplePanel();
		p.add(w);
		return p;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.ManageMeasurePresenter.BaseDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return content;
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
		return eMeasureRadio.getValue();
	}
	
	/* (non-Javadoc
	 * @see mat.client.measure.ManageMeasurePresenter.ExportDisplay#isELM()
	 */
	public boolean isELM() {
		return elmRadio.getValue(); 
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
		return eMeasurePackageRadio.getValue();
	}
	
	@Override
	public boolean isCQLLibrary() {
		return cqlLibraryRadio.getValue();
	}
}
