package org.ifmc.mat.client.measure.metadata;

import java.util.List;

import org.ifmc.mat.client.codelist.HasListBox;
import org.ifmc.mat.client.shared.ErrorMessageDisplayInterface;
import org.ifmc.mat.client.shared.LabelBuilder;
import org.ifmc.mat.client.shared.ListBoxMVP;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.SuccessMessageDisplayInterface;
import org.ifmc.mat.client.shared.search.SearchResults;
import org.ifmc.mat.client.shared.search.SearchView;
import org.ifmc.mat.model.Author;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AddEditAuthorsView extends AddEditMetadataBaseView implements MetaDataPresenter.AddEditAuthorsDisplay{

	private ListBoxMVP authorInput;
	private SearchView<Author> view;
	
	public AddEditAuthorsView() {
		super();
		DOM.setElementAttribute(asWidget().getElement(), "id", "AddEditAuthorsView.asWidget");
	}
	
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	protected Widget getValueInput() {
		if(authorInput == null){
			authorInput = new ListBoxMVP();
		}
		return authorInput;
	}

	@Override
	protected String getValueInputLabel() {
		return "Measure Developer Name";
	}

	@Override
	protected SearchView<?> getSearchView() {
		if(view == null){
			view = new SearchView<Author>();
			//view.setWidth("75%");
		}
		return view;
	}

	@Override
	public void buildDataTable(SearchResults<Author> searchResults) {
		view.buildDataTable(searchResults);
	}

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
	
	@Override
	public void setOptions(List<? extends HasListBox> itemList) {
		setListBoxOptions(authorInput, itemList,  MatContext.PLEASE_SELECT);
	}

	@Override
	public String getAuthor() {
		return authorInput.getItemTitle(authorInput.getSelectedIndex());
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
	public HasValue<String> getAuthorInputBox() {
		return authorInput;
	}

	@Override
	public void showTextBox() {
		VerticalPanel otherTextBoxVP = new VerticalPanel();
		Widget otherSpecify = LabelBuilder.buildInvisibleLabel(otherSpecifyBox, "OtherSpecify");
		otherTextBoxVP.add(otherSpecify);
		otherTextBoxVP.add(otherSpecifyBox);
		emptyTextBoxHolder.add(otherTextBoxVP);
	}

	@Override
	public void hideTextBox() {
		emptyTextBoxHolder.clear();
		
	}


	@Override
	public HasValue<String> getOtherAuthor() {
		return otherSpecifyBox;
	}

}
