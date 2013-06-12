package mat.client.codelist;

import java.util.List;

import mat.client.ImageResources;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.RequiredIndicator;
import mat.client.shared.SaveCompleteCancelButtonBar;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.TextAreaWithMaxLength;
import mat.shared.ConstantMessages;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class BaseDetailView implements BaseDetailPresenter.BaseDisplay {

	private String oidTitle = "Select 'Use System Generated OID' to have the system "+
	"assign an OID to the Value Set.  If an OID "+
	"has been assigned to the Value Set outside of "+
	"the Measure Authoring Tool, manually enter the OID.";
	
	protected SimplePanel mainPanel = new SimplePanel();

	protected HTML required = new HTML(RequiredIndicator.get() + " indicates required field");
	private Anchor createNewAnchor = new Anchor(ConstantMessages.CREATE_NEW_VALUE_SET);
	private Anchor createNewGroupedAnchor = new Anchor(ConstantMessages.CREATE_NEW_GROUPED_VALUE_SET);
	
	protected TextBox nameInput = new TextBox();
	
	protected ListBoxMVP stewardInput = new ListBoxMVP();
	protected Label stewardLabel;
	//US 413. Empty panel and input textbox for Steward Other option. 
	protected VerticalPanel emptyTextBoxHolder = new VerticalPanel();
	protected TextBox stewardOtherInput = new TextBox();
	
	protected ListBoxMVP categoryInput = new ListBoxMVP();
	protected TextAreaWithMaxLength rationaleInput = new TextAreaWithMaxLength();
	protected TextAreaWithMaxLength commentsInput = new TextAreaWithMaxLength();
	protected ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	protected SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	protected ListBoxMVP codeSystemInput = new ListBoxMVP();
	protected TextBox codeSystemVersionInput = new TextBox();
	
	protected TextAreaWithMaxLength codeListOidInput = new TextAreaWithMaxLength();
	private Button generateOidButton = new SecondaryButton("Use System Generated OID");
	
	public DateBoxWithCalendar lastModifiedDate = new DateBoxWithCalendar(DateBoxWithCalendar.MDYHMA);
	
	protected SaveCompleteCancelButtonBar buttonBar = new SaveCompleteCancelButtonBar();
	protected SummaryWidgetBase<?> codeListsSummary = getSummaryWidget();
	private Panel createNewPanel;
	private Label lastModify = new Label("Last Modified");
	
	
	public BaseDetailView(String nameType){
		if(nameType == null)
			nameType = "Name";
			
		
		HorizontalPanel mainContent = new HorizontalPanel();
		mainPanel.setStylePrimaryName("searchResultsContainer");
		mainPanel.addStyleName("leftAligned");
		FlowPanel fPanel = new FlowPanel();
		fPanel.setStyleName("leftSideForm");
		fPanel.add(required);
		fPanel.add(new SpacerWidget());
    
		fPanel.add(errorMessages);
		fPanel.add(successMessages);
		
		fPanel.add(LabelBuilder.buildRequiredLabel(nameInput, nameType));
		fPanel.add(nameInput);
		fPanel.add(new SpacerWidget());
		
		VerticalPanel verStewardPanel = new VerticalPanel();
		stewardLabel = (Label) LabelBuilder.buildRequiredLabel(stewardInput, "Steward");
		verStewardPanel.add(stewardLabel);
		verStewardPanel.add(stewardInput);		
		verStewardPanel.add(emptyTextBoxHolder);		

		//US 413
		fPanel.add(verStewardPanel);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildRequiredLabel(categoryInput, "Category"));
		fPanel.add(categoryInput);
		fPanel.add(new SpacerWidget());
		
		if(shouldDisplayCodeSystem()) {
			FlowPanel codeSystemPanel = new FlowPanel();
			codeSystemPanel.add(LabelBuilder.buildRequiredLabel(codeSystemInput, "Code System"));
			codeSystemPanel.addStyleName("floatLeft");
			codeSystemPanel.add(codeSystemInput);
			fPanel.add(codeSystemPanel);
			
			FlowPanel codeSystemVersionPanel = new FlowPanel();
			codeSystemVersionPanel.add(LabelBuilder.buildRequiredLabel(codeSystemVersionInput, "Code System Version"));
			codeSystemVersionPanel.addStyleName("floatLeft");
			codeSystemVersionInput.setTitle("The version of the Code System used to define a value set.  "+
											"The format (ex. version number or year) will vary based upon the Code System selected." );
			codeSystemVersionPanel.add(codeSystemVersionInput);
			
			codeSystemVersionPanel.addStyleName("codeListCodeSystemPanel");
			fPanel.add(codeSystemVersionPanel);
		}
		
		SpacerWidget clearBoth = new SpacerWidget();
		clearBoth.addStyleName("clearBoth");
		fPanel.add(clearBoth);
		
		FlowPanel oidPanel1 = new FlowPanel();
		oidPanel1.add(LabelBuilder.buildRequiredLabel(codeListOidInput, "OID"));
		oidPanel1.addStyleName("floatLeft");
		oidPanel1.add(codeListOidInput);
		fPanel.add(oidPanel1);
		
		FlowPanel oidPanel2 = new FlowPanel();
		oidPanel2.addStyleName("floatLeft");
		oidPanel2.add(generateOidButton);
		oidPanel2.addStyleName("codeListCodeSystemPanel");
		fPanel.add(oidPanel2);
		
		SpacerWidget clearBoth2 = new SpacerWidget();
		clearBoth2.addStyleName("clearBoth");
		fPanel.add(clearBoth2);
		
		Label cautionLabel = new Label(ConstantMessages.OID_CAUTION);
		cautionLabel.setWidth("60%");
		fPanel.add(cautionLabel);
		
		SpacerWidget clearBoth3 = new SpacerWidget();
		clearBoth2.addStyleName("clearBoth");
		fPanel.add(clearBoth3);
		
		fPanel.add(lastModify);
		fPanel.add(lastModifiedDate);
		lastModifiedDate.setLabel("Last Modified");
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildRequiredLabel(rationaleInput, "Rationale"));
		fPanel.add(rationaleInput);
		fPanel.add(new SpacerWidget());
		
		fPanel.add(LabelBuilder.buildLabel(commentsInput, "Comments"));
		fPanel.add(commentsInput);
		fPanel.add(new SpacerWidget());

		nameInput.setMaxLength(255);
		codeSystemVersionInput.setMaxLength(255);
		codeListOidInput.setMaxLength(255);
		rationaleInput.setMaxLength(15000);
		commentsInput.setMaxLength(2000);
		stewardOtherInput.setMaxLength(200);
		buttonBar.getSaveButton().setTitle("Save As Draft");
		buttonBar.getSaveCompleteButton().setTitle("Save As Complete");
		buttonBar.getCancelButton().setTitle("Cancel");
		fPanel.add(buttonBar);
		createNewPanel = buildCodeListLink();
		fPanel.add(createNewPanel);
		
		mainContent.add(fPanel);
		mainContent.add(codeListsSummary);
		mainPanel.add(mainContent);
		mainPanel.setStyleName("contentPanel");
		
		nameInput.setWidth("230px");
		stewardOtherInput.setWidth("415px");
		codeListOidInput.setWidth("230px");
		commentsInput.setWidth("330px");
		rationaleInput.setWidth("330px");
		commentsInput.setVisibleLines(3);
		rationaleInput.setVisibleLines(3);
		
		codeListOidInput.setTitle(oidTitle);
		
		generateOidButton.setTitle(oidTitle);
	}

	
	
	
	protected abstract SummaryWidgetBase<?> getSummaryWidget();
	protected abstract boolean shouldDisplayCodeSystem();
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	@Override
	public HasClickHandlers getSaveCompleteButton() {
		return buttonBar.getSaveCompleteButton();
	}

	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	@Override
	public void setTitle(String text) {
		
	}

	@Override
	public HasValue<String> getName() {
		return nameInput;
	}

	@Override
	public HasValue<String> getOrganisation() {
		return stewardInput;
	}
	
	//US 413
	/* Returns the text value for the Steward selection. 
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#getStewardValue()
	 */
	@Override
	public String getStewardValue() {
		return stewardInput.getItemText(stewardInput.getSelectedIndex());
	}

	//US 413
	/* Returns the Steward Other TextBox object.
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#getStewardOther()
	 */
	@Override
	public TextBox getStewardOther() {
		return stewardOtherInput;
	}

	//US 413
	/* Returns the Steward Other value.
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#getStewardOtherValue()
	 */
	@Override
	public String getStewardOtherValue() {
		return stewardOtherInput.getValue();
	}

	@Override
	public HasValue<String> getCategory() {		
		return categoryInput;
	}

	@Override
	public ListBoxMVP getCategoryListBox() {		
		return categoryInput;
	}

	
	@Override
	public HasValue<String> getRationale() {
		return rationaleInput;
	}

	@Override
	public HasValue<String> getComments() {
		return commentsInput;
	}

	@Override
	public HasValue<String> getOid() {
		return codeListOidInput;
	}
	
	@Override
	public String getOidTitle(){
		return oidTitle;
	}
	
	private void setListBoxOptions(ListBox input, List<? extends HasListBox> itemList,String defaultText) {
		input.clear();
		if(defaultText != null) {
			input.addItem(defaultText, "");
		}
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				input.addItem(listBoxContent.getItem(),"" +listBoxContent.getValue());
			}
		}
	}
	@Override
	public void setCategoryOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(categoryInput, itemList, MatContext.PLEASE_SELECT);
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public void setCodeSystemOptions(List<? extends HasListBox> texts) {
		setListBoxItems(codeSystemInput, texts, MatContext.PLEASE_SELECT);
	}
	
	
	@Override
	public void setStewardOptions(List<? extends HasListBox> texts) {
		setListBoxItems(stewardInput, texts, MatContext.PLEASE_SELECT);
	}

	@Override
	public String getCodeSystemValue(){
		if(codeSystemInput.getSelectedIndex() >= 0) {
			return codeSystemInput.getValue(codeSystemInput.getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	private void setListBoxItems(ListBoxMVP listBox, List<? extends HasListBox> itemList, String defaultOption){
		listBox.clear();
		listBox.addItem(defaultOption,"");
		if(itemList != null){
			for(int i = 0; i < itemList.size(); i++){
				HasListBox listBoxContent = itemList.get(i);
				//Ensure Title is set to the full name
				listBox.insertItem(listBoxContent.getItem(),"" +listBoxContent.getValue(), listBoxContent.getItem(), i);
			}
		}
	}
	
	@Override
	public HasValue<String> getCodeSystem() {
		return codeSystemInput;
	} 
	
	@Override
	public HasValue<String> getCodeSystemVersion() {
		return codeSystemVersionInput;
	}

	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessages;
	}

	@Override
	public void setSaveButtonEnabled(boolean enabled) {
		buttonBar.getSaveButton().setEnabled(enabled);
	}
	
	@Override
	public void setOIDButtonEnabled(boolean enabled) {
		generateOidButton.setEnabled(enabled);
	}
	public HasClickHandlers getCreateNewButton() {
		return createNewAnchor;
	}
	
	public HasClickHandlers getCreateNewGroupedButton() {
		return createNewGroupedAnchor;
	}
	
	public HasClickHandlers getGenerateOidButton() {
		return generateOidButton;
	}
	
	private Panel buildCodeListLink() {
		HorizontalPanel fp = new HorizontalPanel();
		fp.addStyleName("codeListStyle");
		createNewAnchor.setTitle("Create Value Set");
		createNewAnchor.getElement().setAttribute("alt", "Create Value Set");
		createNewGroupedAnchor.setTitle("Create Grouped Value Set");
		createNewGroupedAnchor.getElement().setAttribute("alt", "Create Grouped Value Set");
		HorizontalPanel codeList = new HorizontalPanel();
		codeList.addStyleName("codeListLink");
		Image addImage1 = new Image(ImageResources.INSTANCE.addImage());
		addImage1.setAltText("Create Value Set");
		codeList.add(addImage1);
		codeList.add(createNewAnchor);
		
		HorizontalPanel groupedCodeList = new HorizontalPanel();
		Image addImage2 = new Image(ImageResources.INSTANCE.addImage());
		addImage2.setAltText("Create Grouped Value Set");
		groupedCodeList.add(addImage2);
		groupedCodeList.add(createNewGroupedAnchor);
		
		fp.add(codeList);
		fp.add(groupedCodeList);
		return fp;
	}
	
	//US 413
	/* Clears out the Steward Other panel and re-draw the Steward Other input components
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#showOtherTextBox()
	 */
	@Override
	public void showOtherTextBox() {
		clearOtherPanel();
		emptyTextBoxHolder.add(new SpacerWidget());
		Widget otherSpecify = LabelBuilder.buildRequiredLabel(stewardOtherInput, "User Defined Steward");
		otherSpecify.getElement().setAttribute("id", "Added UserDefineSteward");
		otherSpecify.getElement().setAttribute("aria-role", "textbox");
		otherSpecify.getElement().setAttribute("aria-labelledby", "LiveRegion");
		otherSpecify.getElement().setAttribute("aria-live", "assertive");
		otherSpecify.getElement().setAttribute("aria-atomic", "true");
		otherSpecify.getElement().setAttribute("aria-relevant", "all");
		otherSpecify.getElement().setAttribute("role", "alert");
		emptyTextBoxHolder.add(otherSpecify);		
		emptyTextBoxHolder.add(stewardOtherInput);		
	}

	//US 413
	/* Clears out the Steward Other panel by calling local method .
	 * @see mat.client.codelist.BaseDetailPresenter.BaseDisplay#hideOtherTextBox()
	 */
	@Override
	public void hideOtherTextBox() {
		clearOtherPanel();
		
	}

	//US 413
	/**
	 * Local method to clear out the Steward other panel. 
	 */
	private void clearOtherPanel(){	
		stewardLabel.getElement().setAttribute("id", "Removed UserDefineSteward");
		stewardLabel.getElement().setAttribute("aria-role", "textbox");
		stewardLabel.getElement().setAttribute("aria-labelledby", "LiveRegion");
		stewardLabel.getElement().setAttribute("aria-live", "assertive");
		stewardLabel.getElement().setAttribute("aria-atomic", "true");
		stewardLabel.getElement().setAttribute("aria-relevant", "all");
		stewardLabel.getElement().setAttribute("role", "alert");
		stewardOtherInput.setValue(null);
		emptyTextBoxHolder.clear();
		Element element = lastModifiedDate.calendar.getElement();
		if(lastModifiedDate.isEnabled()){
			element.focus();
		}
		element.setAttribute("aria-role", "command");
		element.setAttribute("aria-labelledby", "LiveRegion");
		element.setAttribute("aria-live", "assertive");
		element.setAttribute("aria-atomic", "true");
		element.setAttribute("aria-relevant", "all");
		element.setAttribute("role", "alert");
	}
	
	@Override
	public DateBoxWithCalendar getLastModifiedDate() {
		return lastModifiedDate;
	}
	
}
