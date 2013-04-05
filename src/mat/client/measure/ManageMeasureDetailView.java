package mat.client.measure;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureNameLabel;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SpacerWidget;
import mat.client.shared.TextAreaWithMaxLength;

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
	//User Story # 1391 Sprint 11
	private String cautionMsgStr = "WARNING: Changing the 'Measure Scoring' will have the following impacts:<br/>" +
								   "- Populations in the Clause Workspace that do not apply to the 'Measure Scoring' selected will &nbsp; be deleted<br/>" +
                                   "- Existing Groupings in the Measure Packager will be deleted.";
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
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringValue()
	 */
	@Override
	public String getMeasScoringValue() {
		return measScoringInput.getItemText(measScoringInput.getSelectedIndex());
	}
	
	/* Returns the Measure Scoring ListBox object.
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#getMeasScoringChoice()
	 */
	@Override
	public ListBoxMVP getMeasScoringChoice() {
		return measScoringInput;
	}
	
	//US 421
	/* Set the value list for Measure Scoring choice list box.
	 * @see mat.client.measure.ManageMeasurePresenter.DetailDisplay#setScoringChoices(java.util.List)
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
