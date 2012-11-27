package org.ifmc.mat.client.measure;

import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.MeasureNameLabel;
import org.ifmc.mat.client.shared.PrimaryButton;
import org.ifmc.mat.client.shared.SecondaryButton;
import org.ifmc.mat.client.shared.SpacerWidget;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureExportView implements ManageMeasurePresenter.ExportDisplay {
	private FlowPanel content = new FlowPanel();
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private RadioButton simpleXMLRadio = new RadioButton("format", "SimpleXML");
	private RadioButton eMeasureRadio = new RadioButton("format", "eMeasure");
	private RadioButton codeListRadio = new RadioButton("format", "Measure Value Set");
	private RadioButton eMeasurePackageRadio = new RadioButton("format", "eMeasure Package");
	private PrimaryButton saveButton = new PrimaryButton("Save");
	private SecondaryButton cancelButton = new SecondaryButton("Cancel");
	private SecondaryButton openButton = new SecondaryButton("Open");
	private boolean isTopLevelUser = false;
	

	public ManageMeasureExportView(boolean isTopLevelUser) {
		this.isTopLevelUser=isTopLevelUser;
		if(this.isTopLevelUser)
			simpleXMLRadio.setValue(Boolean.TRUE);
		else
			eMeasureRadio.setValue(Boolean.TRUE);
		content.setStylePrimaryName("contentPanel");
		content.addStyleName("leftAligned");
		
		content.add(measureNameLabel);
		
		content.add(new Label("Select an export option"));
		content.add(new SpacerWidget());

		if(isTopLevelUser)
			content.add(wrapRadioButton(simpleXMLRadio));
		content.add(wrapRadioButton(eMeasureRadio));
		content.add(wrapRadioButton(codeListRadio));
		content.add(wrapRadioButton(eMeasurePackageRadio));
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		
		FlowPanel buttonPanel = new FlowPanel();
		buttonPanel.addStyleName("measureExportButtonContainer");
		buttonPanel.add(saveButton);
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);
		cancelButton.addStyleName("cancelButton");
		content.add(buttonPanel);
		
	}
	
	private Widget wrapRadioButton(RadioButton w) {
		SimplePanel p = new SimplePanel();
		p.add(w);
		return p;
	}
	
	@Override
	public Widget asWidget() {
		return content;
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
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

	@Override
	public boolean isEMeasure() {
		return eMeasureRadio.getValue();
	}
	
	@Override
	public boolean isCodeList() {
		return codeListRadio.getValue();
	}
	
	@Override
	public boolean isEMeasurePackage() {
		return eMeasurePackageRadio.getValue();
	}
}
