package org.ifmc.mat.client.measure;

import java.util.List;

import org.ifmc.mat.client.codelist.HasListBox;
import org.ifmc.mat.client.shared.ErrorMessageDisplay;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.MeasureNameLabel;
import org.ifmc.mat.client.shared.SaveCancelButtonBar;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.client.shared.TextAreaWithMaxLength;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureDetailView 
	implements ManageMeasurePresenter.DetailDisplay{
	
	private SimplePanel mainPanel = new SimplePanel();
	private MeasureNameLabel measureNameLabel = new MeasureNameLabel();
	
	private String nameLabel = "Name";
	private String shortNameLabel = "Abbreviated Name";
	
	//US 421. Measure scoring choice is now part of measure creation process.
	private String scoringLabel = "Measure Scoring";
	
	private TextAreaWithMaxLength name = new TextAreaWithMaxLength();
	private TextBox shortName = new TextBox();
	
	//US 421. Measure scoring choice is now part of measure creation process.	
	private ListBoxMVP  measScoringInput = new ListBoxMVP();
	
	private SaveCancelButtonBar buttonBar = new SaveCancelButtonBar();
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected HTML instructions = new HTML("Enter a measure name and abbreviated name. Then continue to the Measure Composer.");
	//US 195.
	private String cautionMsgStr = "CAUTION: Changing the 'Measure Scoring' should be avoided unless absolutely necessary. Under most circumstances the 'Measure Scoring' should only be changed when a correction is needed. Changing the 'Measure Scoring' will remove any existing groupings from your measure packager.";
    protected HTML cautionMsgPlaceHolder = new HTML();
    protected HTML requiredInstructions = new HTML("All fields are required.");
	
	public ManageMeasureDetailView() {
		
		
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");


		FlowPanel fPanel = new FlowPanel();
		fPanel.setWidth("75%");	
		fPanel.setHeight("100%");
		fPanel.add(measureNameLabel);
		fPanel.add(instructions);
		fPanel.add(new SpacerWidget());
		fPanel.add(requiredInstructions);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(errorMessages);
		
		FlowPanel leftPanel = new FlowPanel();
		fPanel.add(leftPanel);
		
		leftPanel.add(LabelBuilder.buildLabel(name, nameLabel));
		leftPanel.add(name);
		leftPanel.add(new SpacerWidget());
		
		
		leftPanel.add(LabelBuilder.buildLabel(shortName, shortNameLabel));
		leftPanel.add(shortName);
		leftPanel.add(new SpacerWidget());
		//US 195 Adding Static Caution Message
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(measScoringInput);
		hp.add(new HTML("&nbsp;"));
		hp.add(cautionMsgPlaceHolder);
		
		//US 421. Measure scoring choice is now part of measure creation process.
		leftPanel.add(LabelBuilder.buildLabel(measScoringInput, scoringLabel));
		leftPanel.add(hp);
		leftPanel.add(new SpacerWidget());

		SimplePanel buttonPanel = new SimplePanel();
		buttonPanel.add(buttonBar);
		buttonPanel.setWidth("100%");
		fPanel.add(buttonPanel);
		mainPanel.add(fPanel);
		
		shortName.setMaxLength(32);
		shortName.setWidth("192px");
		name.setMaxLength(500);
		name.setWidth("400px");
	}

	@Override
	public void setMeasureName(String name) {
		measureNameLabel.setMeasureName(name);
	}

	@Override
	public void showMeasureName(boolean show) {
		MatContext.get().setVisible(measureNameLabel,show);
	}

	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}



	@Override
	public HasValue<String> getName() {
		return name;
	}

	@Override
	public HasValue<String> getShortName() {
		return shortName;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void clearFields() {
		name.setText("");
		shortName.setText("");
		measScoringInput.setSelectedIndex(0);//default to --Select-- value.
		
	}
	
	/* Returns the text value of Measure Scoring choice.
	 * @see org.ifmc.mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringValue()
	 */
	@Override
	public String getMeasScoringValue() {
		return measScoringInput.getItemText(measScoringInput.getSelectedIndex());
	}
	
	/* Returns the Measure Scoring ListBox object.
	 * @see org.ifmc.mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringChoice()
	 */
	@Override
	public ListBoxMVP getMeasScoringChoice() {
		return measScoringInput;
	}
	
	//US 421
	/* Set the value list for Measure Scoring choice list box.
	 * @see org.ifmc.mat.client.measure.ManageMeasurePresenter.DetailDisplay#setScoringChoices(java.util.List)
	 */
	@Override
	public void setScoringChoices(List<? extends HasListBox> texts) {
		setListBoxItems(measScoringInput, texts, MatContext.PLEASE_SELECT);
	}

	/**
	 * Populates the list box with collection values.
	 * @param listBox
	 * @param itemList
	 * @param defaultOption
	 */
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				listBox.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}

	@Override
	public HasValue<String> getMeasureVersion() {
		// TODO Auto-generated method stub
		return null;
	}

	//US 195
	@Override
	public void showCautionMsg(boolean show) {
		if(show){
			cautionMsgPlaceHolder.setHTML(cautionMsgStr);
		}else{
			cautionMsgPlaceHolder.setHTML("");
		}
	}
}
