package mat.client.measure.metadata;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.measure.ManageMeasureSearchModel;
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

/**
 * The Class AddEditMeasureTypeView.
 */
public class AddEditMeasureTypeView extends AddEditMetadataBaseView implements MetaDataPresenter.AddEditMeasureTypeDisplay{

	/** The measure type input. */
	private ListBoxMVP measureTypeInput;
	
	/** The view. */
	private SearchView<MeasureType> view;
	
	/**
	 * Instantiates a new adds the edit measure type view.
	 */
	public AddEditMeasureTypeView() {
		super();
		DOM.setElementAttribute(asWidget().getElement(), "id", "AddEditMeasureTypeView.asWidget");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getValueInput()
	 */
	@Override
	protected Widget getValueInput() {
		if(measureTypeInput == null){
			measureTypeInput = new ListBoxMVP();
		}
		return measureTypeInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getValueInputLabel()
	 */
	@Override
	protected String getValueInputLabel() {
		return "Measure Type";
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getSearchView()
	 */
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null){
			view = new SearchView<MeasureType>();
		}
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<MeasureType> searchResults) {
		view.buildDataTable(searchResults);
	}

	/**
	 * Sets the list box options.
	 * 
	 * @param input
	 *            the input
	 * @param itemList
	 *            the item list
	 * @param defaultText
	 *            the default text
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#setOptions(java.util.List)
	 */
	@Override
	public void setOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(measureTypeInput, itemList, MatContext.PLEASE_SELECT);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditMeasureTypeDisplay#getMeasureType()
	 */
	@Override
	public String getMeasureType() {
		return measureTypeInput.getItemText(measureTypeInput.getSelectedIndex());
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getSaveButton()
	 */
	@Override
	public HasClickHandlers getSaveButton() {
		return buttonBar.getSaveButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getCancelButton()
	 */
	@Override
	public HasClickHandlers getCancelButton() {
		return buttonBar.getCancelButton();
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getRemoveButton()
	 */
	@Override
	public HasClickHandlers getRemoveButton() {
		return removeButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getReturnButton()
	 */
	@Override
	public HasClickHandlers getReturnButton() {
		return returnButton;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getSuccessMessageDisplay() {
		return successMessages;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditMeasureTypeDisplay#getMeasureTypeInputBox()
	 */
	@Override
	public HasValue<String> getMeasureTypeInputBox() {
		return measureTypeInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#showTextBox()
	 */
	@Override
	public void showTextBox() {
		emptyTextBoxHolder.add(otherSpecifyBox);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#hideTextBox()
	 */
	@Override
	public void hideTextBox() {
		emptyTextBoxHolder.clear();
		
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditMeasureTypeDisplay#getOtherMeasureType()
	 */
	@Override
	public HasValue<String> getOtherMeasureType() {
		return otherSpecifyBox;
	}

	@Override
	public void buildCellTable(ManageMeasureSearchModel result) {
		// TODO Auto-generated method stub
		
	}

}
