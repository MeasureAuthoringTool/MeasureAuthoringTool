package mat.client.clause;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SecondaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.SearchView;
import mat.model.CodeListSearchDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class QDSCodeListSearchView  implements QDSCodeListSearchPresenter.SearchDisplay {

	private SimplePanel containerPanel = new SimplePanel();
	private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private DisclosurePanel disclosurePanel = new DisclosurePanel("Element without VSAC value set");
	private DisclosurePanel disclosurePanelCellTable = new DisclosurePanel("Element with VSAC value set");
	private TextBox userDefinedInput = new TextBox();
	private TextBox searchInput = new TextBox();
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE, "Specific Occurrence",true); //US 450
	private Button addToMeasure = new PrimaryButton("Search","primaryButton");
	private Button psuedoQDMToMeasure = new PrimaryButton("Apply to Measure", "primaryButton");
	private Button psuedoQDMCancel = new SecondaryButton("Cancel");
	private SimplePanel dataTypePanel = new SimplePanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel;
	
	private ErrorMessageDisplay errorMessageUserDefinedPanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessageUserDefinedPanel = new SuccessMessageDisplay();
	private ListBoxMVP dataTypeInput = new ListBoxMVP();
	private ListBoxMVP allDataTypeInput = new ListBoxMVP();
	private FocusableWidget messageFocus;
	//private Button removeButton = new Button("Remove");
	VerticalPanel listBoxVPanel = new VerticalPanel();
   // private ScrollPanel sp;
    private ValueSetSearchFilterPanel vssfp = new ValueSetSearchFilterPanel();
   
	private  ValueChangeHandler<String> dataTypeChangeHandler = new ValueChangeHandler<String>() {
		@Override
		public void onValueChange(ValueChangeEvent<String> event) {
			specificOccurrence.setValue(false);
			String selectedValue = event.getValue();
		    if(!selectedValue.isEmpty()&& !selectedValue.equals("")){
		    	addToMeasure.setEnabled(true);
		    }
		    else{
		    	addToMeasure.setEnabled(false);
		    }
		}
	};
	
    
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return successMessagePanel;
	}
	
	public ErrorMessageDisplay getErrorMessagePanel(){
		return errorMessagePanel;
	}
	
	public QDSCodeListSearchView() {
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		messageFocus = new FocusableWidget(successMessagePanel);
		VerticalPanel vp = new VerticalPanel();
		/*FlowPanel header = new FlowPanel();
		header.addStyleName("codeListHeader");
		Label codeListLabel = new Label("QDM");
		FocusableWidget  labelFocus = new FocusableWidget(codeListLabel);
		header.add(labelFocus);
		dataTypeInput.addValueChangeHandler(dataTypeChangeHandler);
		specificOccurrence.setEnabled(false);
		addToMeasure.setEnabled(false);
		*/
	
		vp.add(buildCellTableWidget());
		vp.add(new SpacerWidget());
		vp.add(buildUserDefinedDisclosureWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		
		HorizontalPanel mainPanel = new HorizontalPanel();
		
		mainPanel.add(vp);
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQDSView(this);
	}
		
	private Widget buildInitialDisabledWidget(){
		dataTypeInput.addItem(MatContext.PLEASE_SELECT);
		dataTypeInput.setEnabled(false);
		dataTypePanel.clear();
		dataTypePanel.add(buildDataTypeWidget());
		return dataTypePanel;
	}
	
	private Widget buildDataTypeWidget(){
		FlowPanel fPanel = new FlowPanel();
		fPanel.addStyleName("leftAligned");
		fPanel.add(new SpacerWidget());
		fPanel.add(LabelBuilder.buildLabel(dataTypeInput, "Select Data Type"));
		fPanel.add(dataTypeInput);
		dataTypeInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();	
					}
				});

		fPanel.add(new SpacerWidget());
		return fPanel;
	}
	
	private Widget buildSpecificOccurrenceWidget(){
		FlowPanel fPanel = new FlowPanel();
		fPanel.addStyleName("leftAligned");
		fPanel.add(new SpacerWidget());		
		fPanel.add(specificOccurrence);
		return fPanel;
	}
	
	private Widget buildSearchWidget(){
		HorizontalPanel hp = new HorizontalPanel();
		FlowPanel fp1 = new FlowPanel();
		//sp1.addStyleName("codeListLink");
		fp1.add(searchInput);
		searchInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		
		fp1.add(searchButton);
		searchButton.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();
					}
				});
		fp1.add(new SpacerWidget());
		hp.setStylePrimaryName("marginLeft");
		hp.add(fp1);
		return hp;
	}
	
	private Widget buildUserDefinedDisclosureWidget(){
		HorizontalPanel horiPanel = new HorizontalPanel();
		VerticalPanel valueSetPanel = new VerticalPanel();
		VerticalPanel dataTypePanel = new VerticalPanel();
		
		Widget widgetValueSet =LabelBuilder.buildLabel(userDefinedInput, "Name");
		valueSetPanel.add(widgetValueSet);
		valueSetPanel.add(new SpacerWidget());
		userDefinedInput.setWidth("230px");
		userDefinedInput.setMaxLength(255);
		valueSetPanel.add(userDefinedInput);
		
		Widget widgetDataType = LabelBuilder.buildLabel("Select Data Type", "Select Data Type");
		dataTypePanel.add(widgetDataType);
		dataTypePanel.add(new SpacerWidget());
		dataTypePanel.add(allDataTypeInput);
		allDataTypeInput.addFocusHandler(
				new FocusHandler() {
					@Override
					public void onFocus(FocusEvent event) {
						MatContext.get().clearDVIMessages();	
					}
		});
		dataTypePanel.setStyleName("marginLeftRight");
		horiPanel.add(valueSetPanel);
		horiPanel.add(dataTypePanel);
		
		HorizontalPanel buttonHorizontalPanel = new HorizontalPanel();
		psuedoQDMToMeasure.setTitle("Apply to Measure");
		buttonHorizontalPanel.add(psuedoQDMToMeasure);
		buttonHorizontalPanel.add(new SpacerWidget());
		psuedoQDMCancel.setTitle("Cancel");
		buttonHorizontalPanel.add(psuedoQDMCancel);
		
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.add(horiPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(successMessageUserDefinedPanel);
		mainPanel.add(errorMessageUserDefinedPanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(buttonHorizontalPanel);
		mainPanel.add(new SpacerWidget());
		disclosurePanel.add(mainPanel);
		return disclosurePanel;
	}
	
	private Widget buildCellTableWidget(){
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.setSize("800px", "200px");
		//HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.add(new SpacerWidget());
		/*Widget searchWidget = buildSearchWidget();
		Widget label = LabelBuilder.buildLabel(searchInput, "Search for a Value Set");
		searchInput.setHeight("18px");
		searchCriteriaPanel.add(label);
		searchCriteriaPanel.add(new SpacerWidget());
		searchHorizontalPanel.add(vssfp.getPanel());
		searchHorizontalPanel.add(searchWidget);
		*/searchCriteriaPanel.add(errorMessagePanel);
		/*searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(view.asWidget());
		*/searchCriteriaPanel.add(messageFocus);
		/*searchCriteriaPanel.add(buildInitialDisabledWidget());
		searchCriteriaPanel.add(buildSpecificOccurrenceWidget());
		*/searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(addToMeasure);
		disclosurePanelCellTable.add(searchCriteriaPanel);
		disclosurePanelCellTable.setOpen(true);
		return disclosurePanelCellTable;
		
	}
	@Override
	public Widget asWidget() {
		return containerPanel;
	}


	
	@Override
	public HasClickHandlers getSearchButton() {
		return searchButton;
	}
	
	

	
	
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return view;
	}
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return view;
	}
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		return view;
	}

	
	@Override
	public ListBoxMVP getDataTypeInput(){
		return dataTypeInput;
	}
	
	
	@Override
	public CustomCheckBox getSpecificOccurrenceInput(){
		return specificOccurrence;
	}
	

	@Override
	public HasValue<String> getSearchString() {
		return searchInput;
	}


	@Override
	public int getPageSize() {
		return view.getPageSize();
	}


	@Override
	public HasClickHandlers getAddToMeasureButton() {
		return  addToMeasure; 
	}


	@Override
	public Widget getDataTypeWidget() {
		return dataTypePanel;
	}

    
	@Override
	public FocusableWidget getMsgFocusWidget(){
		return messageFocus;
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}

	
	@Override
	public void scrollToBottom(){
	//	sp.scrollToBottom();
	}
    
	
	
	@Override
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if(inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	@Override
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if(inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		}
		else {
			return "";
		}
	}

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
	public void setDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypeInput, texts, MatContext.PLEASE_SELECT);
	}


	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
	}


	@Override
	public void setAddToMeasureButtonEnabled(boolean enabled) {
		addToMeasure.setEnabled(enabled);
	}

    @Override
    public Button getApplyToMeasure(){
    	return addToMeasure;
    }
	
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption() {
		return view;
	}

	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return vssfp;
	}
	
	public DisclosurePanel getDisclosurePanel() {
		return disclosurePanel;
	}

	public void setDisclosurePanel(DisclosurePanel disclosurePanel) {
		this.disclosurePanel = disclosurePanel;
	}

	public DisclosurePanel getDisclosurePanelCellTable() {
		return disclosurePanelCellTable;
	}

	public void setDisclosurePanelCellTable(DisclosurePanel disclosurePanelCellTable) {
		this.disclosurePanelCellTable = disclosurePanelCellTable;
	}

	public TextBox getUserDefinedInput() {
		return userDefinedInput;
	}

	public void setUserDefinedInput(TextBox userDefinedInput) {
		this.userDefinedInput = userDefinedInput;
	}

	public Button getPsuedoQDMToMeasure() {
		return psuedoQDMToMeasure;
	}

	public void setPsuedoQDMToMeasure(Button psuedoQDMToMeasure) {
		this.psuedoQDMToMeasure = psuedoQDMToMeasure;
	}

	public Button getPsuedoQDMCancel() {
		return psuedoQDMCancel;
	}

	public void setPsuedoQDMCancel(Button psuedoQDMCancel) {
		this.psuedoQDMCancel = psuedoQDMCancel;
	}

	public ListBoxMVP getAllDataTypeInput() {
		return allDataTypeInput;
	}

	public void setAllDataTypeInput(ListBoxMVP allDataTypeInput) {
		this.allDataTypeInput = allDataTypeInput;
	}

	public ErrorMessageDisplay getErrorMessageUserDefinedPanel() {
		return errorMessageUserDefinedPanel;
	}

	public void setErrorMessageUserDefinedPanel(
			ErrorMessageDisplay errorMessageUserDefinedPanel) {
		this.errorMessageUserDefinedPanel = errorMessageUserDefinedPanel;
	}

	public SuccessMessageDisplay getSuccessMessageUserDefinedPanel() {
		return successMessageUserDefinedPanel;
	}

	public void setSuccessMessageUserDefinedPanel(
			SuccessMessageDisplay successMessageUserDefinedPanel) {
		this.successMessageUserDefinedPanel = successMessageUserDefinedPanel;
	}

	public void setEnabled(boolean enabled){
		//search button
		searchButton.setEnabled(enabled);
		//search text
		searchInput.setEnabled(enabled);
		//search list
		getValueSetSearchFilterPanel().setEnabled(enabled);
		//data type radios
		view.setEnabled(enabled);
	}

	@Override
	public void buildQDSDataTable(QDSCodeListSearchModel results ,boolean isTableEnabled) {
		/**Code Commented for USer Story MAT-2360 : Hide Elements With Value Set Contents.
		**/
		//buildTableQDS(results,isTableEnabled);
	}
	/**Code Commented for USer Story MAT-2360 : Hide Elements With Value Set Contents.
	**/
	/*private void buildTableQDS( QDSCodeListSearchModel results,boolean isTableEnabled){
		 
		CellTable<CodeListSearchDTO> table = new CellTable<CodeListSearchDTO>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<CodeListSearchDTO> sortProvider = new ListDataProvider<CodeListSearchDTO>();
		  
		// Display 50 rows in one page or all records.
		table.setPageSize(50);
		table.setSelectionModel(results.addSelectionHandlerOnTable());
		//table = results.addColumnToTable(table,isTableEnabled);
		
		table.redraw();
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData());
		ListHandler<CodeListSearchDTO> sortHandler = new ListHandler<CodeListSearchDTO>(sortProvider.getList());
		table.addColumnSortHandler(sortHandler);
		table = results.addColumnToTable(table,isTableEnabled,sortHandler);
		sortProvider.addDataDisplay(table);
		//Used custom pager class - for disabling next/last button when on last page and for showing correct pagination number.
		MatSimplePager spager;
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
	    spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
        spager.setDisplay(table);
        spager.setPageStart(0);
        spager.setToolTipAndTabIndex(spager);
        view.getvPanelForQDMTable().clear();
        view.getvPanelForQDMTable().add(table);
		view.getvPanelForQDMTable().add(new SpacerWidget());
		view.getvPanelForQDMTable().add(spager);

	}*/

	@Override
	public void setAllDataTypeOptions(List<? extends HasListBox> texts) {
		setListBoxItems(allDataTypeInput, texts, MatContext.PLEASE_SELECT);
	}
	
}
