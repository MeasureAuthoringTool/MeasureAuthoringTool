package mat.client.clause;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.codelist.ValueSetSearchFilterPanel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.QDMAppliedListWidget;
import mat.client.clause.QDMAvailableValueSetWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.VerticalFlowPanel;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataSetDTO;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class QDSCodeListSearchView  implements QDSCodeListSearchPresenter.SearchDisplay {

	private SimplePanel containerPanel = new SimplePanel();
	
	/*private Button searchButton = new PrimaryButton("Search","primaryGreyLeftButton");
	private TextBox searchInput = new TextBox();
	private SearchView<CodeListSearchDTO> view = new SearchView<CodeListSearchDTO>(true);
	private CustomCheckBox specificOccurrence = new CustomCheckBox(ConstantMessages.TOOLTIP_FOR_OCCURRENCE, "Specific Occurrence",true); //US 450
	private Button addToMeasure = new PrimaryButton("Apply to Measure","primaryButton");
	private SimplePanel dataTypePanel = new SimplePanel();*/
	/*private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel;
	private FocusableWidget messageFocus;
	*/
	private QDMAvailableValueSetWidget qdmAvailableValueSetWidget = new QDMAvailableValueSetWidget();
	private QDMAppliedListWidget appliedListWidget = new QDMAppliedListWidget();
	public Button removeButton = new Button("Remove");
	
    
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return qdmAvailableValueSetWidget.getSuccessMessagePanel();
	}
	
	public ErrorMessageDisplay getErrorMessagePanel(){
		return qdmAvailableValueSetWidget.getErrorMessagePanel();
	}
	
	public QDSCodeListSearchView() {
		VerticalFlowPanel mainPanel = new VerticalFlowPanel();
		//mainPanel.setWidth("500px");
	//	mainPanel.add(appliedListWidget.getMainAppliedQDMPanel());
		mainPanel.add(qdmAvailableValueSetWidget.getMainPanel());
		
		
		/*HorizontalSplitPanel   mainPanel = new HorizontalSplitPanel();
		mainPanel.add(appliedListWidget.getMainAppliedQDMPanel());
		mainPanel.add(qdmAvailableValueSetWidget.getMainPanel());*/
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQDSView(this);
	}
		
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	@Override
	public HasClickHandlers getSearchButton() {
		return qdmAvailableValueSetWidget.getSearchButton();
	}
	
	@Override
	public HasPageSelectionHandler getPageSelectionTool() {
		return qdmAvailableValueSetWidget.getView();
	}
	@Override
	public HasPageSizeSelectionHandler getPageSizeSelectionTool() {
		return qdmAvailableValueSetWidget.getView();
	}
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectIdForQDSElement() {
		return qdmAvailableValueSetWidget.getView();
	}

	
	@Override
	public ListBoxMVP getDataTypeInput(){
		return qdmAvailableValueSetWidget.getDataTypeInput();
	}
	
	
	@Override
	public CustomCheckBox getSpecificOccurrenceInput(){
		return qdmAvailableValueSetWidget.getSpecificOccurrenceInput();
	}
	

	@Override
	public HasValue<String> getSearchString() {
		return qdmAvailableValueSetWidget.getSearchString();
	}


	@Override
	public int getPageSize() {
		return qdmAvailableValueSetWidget.getView().getPageSize();
	}


	@Override
	public HasClickHandlers getAddToMeasureButton() {
		return  qdmAvailableValueSetWidget.getAddToMeasureButton(); 
	}


	@Override
	public Widget getDataTypeWidget() {
		return qdmAvailableValueSetWidget.getDataTypeWidget();
	}

    
	@Override
	public FocusableWidget getMsgFocusWidget(){
		return qdmAvailableValueSetWidget.getMsgFocusWidget();
	}
	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return qdmAvailableValueSetWidget.getErrorMessageDisplay();
	}

	
	@Override
	public void scrollToBottom(){
	//	sp.scrollToBottom();
	}
    
	
	
	@Override
	public String getDataTypeValue() {
		if(qdmAvailableValueSetWidget.getDataTypeInput().getSelectedIndex() >= 0) {
			return qdmAvailableValueSetWidget.getDataTypeInput().getValue(qdmAvailableValueSetWidget.getDataTypeInput().getSelectedIndex());
		}
		else {
			return "";
		}
	}
	
	@Override
	public String getDataTypeText() {
		if(qdmAvailableValueSetWidget.getDataTypeInput().getSelectedIndex() >= 0) {
			return qdmAvailableValueSetWidget.getDataTypeInput().getItemText(qdmAvailableValueSetWidget.getDataTypeInput().getSelectedIndex());
		}
		else {
			return "";
		}
	}

		@Override
	public void setDataTypeOptions(List<? extends HasListBox> texts) {
		qdmAvailableValueSetWidget.setListBoxItems(qdmAvailableValueSetWidget.getDataTypeInput(), texts, MatContext.PLEASE_SELECT);
	}


	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return qdmAvailableValueSetWidget.getSuccessMessagePanel();
	}


	@Override
	public void setAddToMeasureButtonEnabled(boolean enabled) {
		((Button) qdmAvailableValueSetWidget.getAddToMeasureButton()).setEnabled(enabled);
	}

    @Override
    public Button getApplyToMeasure(){
    	return (Button) qdmAvailableValueSetWidget.getAddToMeasureButton();
    }
	
	@Override
	public HasSelectionHandlers<CodeListSearchDTO> getSelectedOption() {
		return qdmAvailableValueSetWidget.getView();
	}

	@Override
	public ValueSetSearchFilterPanel getValueSetSearchFilterPanel() {
		return qdmAvailableValueSetWidget.getValueSetSearchFilterPanel();
	}
	@Override
	public void setEnabled(boolean enabled){
		qdmAvailableValueSetWidget.setEnabled(enabled);
	}

	@Override
	public void buildQDSDataTable(QDSCodeListSearchModel results) {
		qdmAvailableValueSetWidget.buildTableQDS(results);
	}
	
	@Override
	public Button getRemoveButton() {
		return removeButton;
	}

	@Override
	public SuccessMessageDisplayInterface getRemoveMeasureSuccessMsg() {
		
		return appliedListWidget.getSuccessMessagePanel();
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageRemoveDisplay() {
		
		return appliedListWidget.getErrorMessagePanel();
	}

	@Override
	public void buildCellList(QDSAppliedListModel appliedListModel) {
		appliedListWidget.buildCellList(appliedListModel);
		
	}

	@Override
	public QualityDataSetDTO getSelectedElementToRemove() {
		return appliedListWidget.lastSelectedObject;
	}
	
}
