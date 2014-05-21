package mat.client.measure.metadata;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.Author;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class AddEditAuthorsView.
 */
public class AddEditAuthorsView extends AddEditMetadataBaseView implements MetaDataPresenter.AddEditAuthorsDisplay{

	/** The author input. */
	private ListBoxMVP authorInput;
	
	/** The view. */
	private SearchView<Author> view;
	
	/**
	 * Instantiates a new adds the edit authors view.
	 */
	public AddEditAuthorsView() {
		super();
		DOM.setElementAttribute(asWidget().getElement(), "id", "AddEditAuthorsView.asWidget");
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getValueInput()
	 */
	@Override
	protected Widget getValueInput() {
		if(authorInput == null){
			authorInput = new ListBoxMVP();
		}
		return authorInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getValueInputLabel()
	 */
	@Override
	protected String getValueInputLabel() {
		return "Measure Developer Name";
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.AddEditMetadataBaseView#getSearchView()
	 */
	@Override
	protected SearchView<?> getSearchView() {
		if(view == null){
			view = new SearchView<Author>();
			//view.setWidth("75%");
		}
		return view;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#buildDataTable(mat.client.shared.search.SearchResults)
	 */
	@Override
	public void buildDataTable(SearchResults<Author> searchResults) {
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
	private void setListBoxOptions(ListBoxMVP input, List<? extends HasListBox> itemList,String defaultText) {
		input.clear();
		if(defaultText != null) {
			input.addItem(defaultText, "");
		}
		if(itemList != null){
			for(HasListBox listBoxContent : itemList){
				//using new api as title is not being set properly
				input.insertItem(listBoxContent.getItem(),"" +listBoxContent.getValue(), listBoxContent.getItem(), -1);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#setOptions(java.util.List)
	 */
	@Override
	public void setOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(authorInput, itemList,  MatContext.PLEASE_SELECT);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getAuthor()
	 */
	@Override
	public String getAuthor() {
		return authorInput.getItemTitle(authorInput.getSelectedIndex());
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
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getAuthorInputBox()
	 */
	@Override
	public HasValue<String> getAuthorInputBox() {
		return authorInput;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#showTextBox()
	 */
	@Override
	public void showTextBox() {
		VerticalPanel otherTextBoxVP = new VerticalPanel();
		Widget otherSpecify = LabelBuilder.buildInvisibleLabel(otherSpecifyBox, "OtherSpecify");
		otherTextBoxVP.add(otherSpecify);
		otherTextBoxVP.add(otherSpecifyBox);
		emptyTextBoxHolder.add(otherTextBoxVP);
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#hideTextBox()
	 */
	@Override
	public void hideTextBox() {
		emptyTextBoxHolder.clear();
		
	}


	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay#getOtherAuthor()
	 */
	@Override
	public HasValue<String> getOtherAuthor() {
		return otherSpecifyBox;
	}

	/* (non-Javadoc)
	 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#buildCellTable(mat.client.measure.ManageMeasureSearchModel)
	 */
	@Override
	public void buildCellTable(ManageMeasureSearchModel result,String searchText, 
			List<ManageMeasureSearchModel.Result> measureSelectedList) {
		// TODO Auto-generated method stub
		
	}

}
