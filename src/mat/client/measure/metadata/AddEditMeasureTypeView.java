package mat.client.measure.metadata;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.MeasureType;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class AddEditMeasureTypeView extends AddEditMetadataBaseView implements MetaDataPresenter.AddEditMeasureTypeDisplay{

	private ListBoxMVP measureTypeInput;
	private SearchView<MeasureType> view;
	
	public AddEditMeasureTypeView() {
		super();
		DOM.setElementAttribute(asWidget().getElement(), "id", "AddEditMeasureTypeView.asWidget");
	}
	@Override
	protected Widget getValueInput() {
		if(measureTypeInput == null){
			measureTypeInput = new ListBoxMVP();
		}
		return measureTypeInput;
	}

	@Override
	protected String getValueInputLabel() {
		return "Measure Type";
	}

	@Override
	protected SearchView<?> getSearchView() {
		if(view == null){
			view = new SearchView<MeasureType>();
		}
		return view;
	}

	@Override
	public void buildDataTable(SearchResults<MeasureType> searchResults) {
		view.buildDataTable(searchResults);
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
	public void setOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(measureTypeInput, itemList, MatContext.PLEASE_SELECT);
	}

	@Override
	public String getMeasureType() {
		return measureTypeInput.getItemText(measureTypeInput.getSelectedIndex());
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
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	@Override
	public HasClickHandlers getReturnButton() {
		return returnButton;
	}

	
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	@Override
	public HasValue<String> getMeasureTypeInputBox() {
		return measureTypeInput;
	}
	
	@Override
	public void showTextBox() {
		emptyTextBoxHolder.add(otherSpecifyBox);
	}

	@Override
	public void hideTextBox() {
		emptyTextBoxHolder.clear();
		
	}

	@Override
	public HasValue<String> getOtherMeasureType() {
		return otherSpecifyBox;
	}

}
