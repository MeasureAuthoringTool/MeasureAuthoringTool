package mat.client.codelist;

import java.util.List;

import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSelectAllHandler;
import mat.client.shared.search.SearchResults;
import mat.shared.ListObjectModelValidator;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class BaseDetailPresenter.
 */
public abstract class BaseDetailPresenter {
	
	/**
	 * The Interface BaseDisplay.
	 */
	protected static interface BaseDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Sets the title.
		 * 
		 * @param title
		 *            the new title
		 */
		public void setTitle(String title);
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Sets the save button enabled.
		 * 
		 * @param Enabled
		 *            the new save button enabled
		 */
		public void setSaveButtonEnabled(boolean Enabled);	
		
		/**
		 * Sets the save complete button enabled.
		 * 
		 * @param enabled
		 *            the new save complete button enabled
		 */
		public void setSaveCompleteButtonEnabled(boolean enabled);
		
		/**
		 * Sets the oID button enabled.
		 * 
		 * @param enabled
		 *            the new oID button enabled
		 */
		public void setOIDButtonEnabled(boolean enabled);
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public HasValue<String> getName();		
		
		/**
		 * Gets the organisation.
		 * 
		 * @return the organisation
		 */
		public HasValue<String> getOrganisation();
		//US 413. Introduced Steward Other 
		/**
		 * Gets the steward value.
		 * 
		 * @return the steward value
		 */
		public String getStewardValue();
		
		/**
		 * Gets the steward other.
		 * 
		 * @return the steward other
		 */
		public TextBox getStewardOther();
		
		/**
		 * Gets the steward other value.
		 * 
		 * @return the steward other value
		 */
		public String getStewardOtherValue();
		
		/**
		 * Gets the category.
		 * 
		 * @return the category
		 */
		public HasValue<String> getCategory();
		
		//US 404. Interface to return Category List box 
		/**
		 * Gets the category list box.
		 * 
		 * @return the category list box
		 */
		public ListBoxMVP getCategoryListBox();
		
		/**
		 * Gets the code system.
		 * 
		 * @return the code system
		 */
		public HasValue<String> getCodeSystem();
		
		/**
		 * Gets the code system list box.
		 * 
		 * @return the code system list box
		 */
		public ListBoxMVP getCodeSystemListBox();
		
		/**
		 * Gets the code system value.
		 * 
		 * @return the code system value
		 */
		public String getCodeSystemValue();
		
		/**
		 * Gets the code system version.
		 * 
		 * @return the code system version
		 */
		public HasValue<String> getCodeSystemVersion();
		
		/**
		 * Gets the rationale.
		 * 
		 * @return the rationale
		 */
		public HasValue<String> getRationale();
		
		/**
		 * Gets the comments.
		 * 
		 * @return the comments
		 */
		public HasValue<String> getComments();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Sets the category options.
		 * 
		 * @param texts
		 *            the new category options
		 */
		void setCategoryOptions(List<? extends HasListBox> texts);
		
		/**
		 * Sets the code system options.
		 * 
		 * @param texts
		 *            the new code system options
		 */
		void setCodeSystemOptions(List<? extends HasListBox> texts);
		
		/**
		 * Sets the steward options.
		 * 
		 * @param texts
		 *            the new steward options
		 */
		void setStewardOptions(List<? extends HasListBox> texts);
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the oid.
		 * 
		 * @return the oid
		 */
		public HasValue<String> getOid();
		
		/**
		 * Gets the generate oid button.
		 * 
		 * @return the generate oid button
		 */
		public HasClickHandlers getGenerateOidButton();

		//US 413. Interface to show or hide Steward Other text box based on the selection.
		/**
		 * Show other text box.
		 */
		public void showOtherTextBox();
		
		/**
		 * Hide other text box.
		 */
		public void hideOtherTextBox();
		/*US537*/
		/**
		 * Gets the last modified date.
		 * 
		 * @return the last modified date
		 */
		public DateBoxWithCalendar getLastModifiedDate();
		
		/**
		 * Gets the save complete button.
		 * 
		 * @return the save complete button
		 */
		public HasClickHandlers getSaveCompleteButton();
		
		/**
		 * enable or disable only widgets based on whether or not the Value Set
		 * is a Draft.
		 * 
		 * @param isDraft
		 *            the is draft
		 */
		public void enableValueSetWidgetsBasedOnDraft(boolean isDraft);
		
		/**
		 * Gets the oid title.
		 * 
		 * @return the oid title
		 */
		public String getOidTitle();
		
		/**
		 * Gets the other specify.
		 * 
		 * @return the other specify
		 */
		Label getOtherSpecify();
		
		/**
		 * Gets the steward label.
		 * 
		 * @return the steward label
		 */
		Label getStewardLabel();
	}
	
	/**
	 * The Interface BaseAddDisplay.
	 * 
	 * @param <T>
	 *            the generic type
	 */
	protected static interface BaseAddDisplay<T> {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Sets the title.
		 * 
		 * @param title
		 *            the new title
		 */
		public void setTitle(String title);
		
		/**
		 * Sets the return to link.
		 * 
		 * @param title
		 *            the new return to link
		 */
		public void setReturnToLink(String title);
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the removes the button.
		 * 
		 * @return the removes the button
		 */
		public HasClickHandlers getRemoveButton();
		
		/**
		 * Gets the return button.
		 * 
		 * @return the return button
		 */
		public HasClickHandlers getReturnButton();
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		public HasPageSelectionHandler getPageSelectionTool();
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		public int getPageSize();
		
		/**
		 * Builds the data table.
		 * 
		 * @param codeLists
		 *            the code lists
		 * @param checked
		 *            the checked
		 * @param totalResult
		 *            the total result
		 * @param totalPages
		 *            the total pages
		 * @param currentPage
		 *            the current page
		 */
		public void buildDataTable(SearchResults<T> codeLists,boolean checked,int totalResult, int totalPages,int currentPage);
		
		/**
		 * Gets the select all tool.
		 * 
		 * @return the select all tool
		 */
		public HasSelectAllHandler getSelectAllTool();
	}

	/**
	 * Instantiates a new base detail presenter.
	 * 
	 * @param detailDisplay
	 *            the detail display
	 * @param listBoxProvider
	 *            the list box provider
	 */
	protected BaseDetailPresenter(final BaseDisplay detailDisplay, ListBoxCodeProvider listBoxProvider) {
		listBoxProvider.getCategoryList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				detailDisplay.setCategoryOptions(result);
			}
		});
		
		listBoxProvider.getStewardList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				detailDisplay.setStewardOptions(result);
			}
		});
	}
	
	/**
	 * Gets the detail display.
	 * 
	 * @return the detail display
	 */
	protected abstract BaseDisplay getDetailDisplay();
	
	/** The oid exists. */
	protected boolean oidExists = false;
	/*
	 * This flag keeps track of whether the newCodeList/new GroupedCodeList has been clicked, to give the edit/readonly permission.
	 */
	/** The is new code list. */
	protected boolean isNewCodeList = false; 
	
	/**
	 * Builds the validation messages.
	 * 
	 * @param model
	 *            the model
	 * @return the list
	 */
	protected List<String> buildValidationMessages(ManageCodeListDetailModel model) {
		ListObjectModelValidator validator = new ListObjectModelValidator();
		List<String> messages = validator.ValidateListObject(model);
		return messages;
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @param model
	 *            the model
	 * @return true, if is valid
	 */
	protected final boolean isValid(ManageCodeListDetailModel model) {
		List<String> message = buildValidationMessages(model);		
		boolean valid = message.size() == 0;
		if(!valid) {
			getDetailDisplay().getErrorMessageDisplay().setMessages(message);
		}
		else {
			getDetailDisplay().getErrorMessageDisplay().clear();
		}
		return valid;
	}
	
	/**
	 * Checks if is editable.
	 * 
	 * @return true, if is editable
	 */
	protected boolean isEditable(){
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}
}
