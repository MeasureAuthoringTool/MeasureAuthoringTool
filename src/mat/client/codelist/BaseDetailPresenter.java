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

public abstract class BaseDetailPresenter {
	protected static interface BaseDisplay {
		public Widget asWidget();
		public void setTitle(String title);
		public HasClickHandlers getSaveButton();
		public void setSaveButtonEnabled(boolean Enabled);	
		public void setSaveCompleteButtonEnabled(boolean enabled);
		public void setOIDButtonEnabled(boolean enabled);
		public HasClickHandlers getCancelButton();
		public HasValue<String> getName();		
		public HasValue<String> getOrganisation();
		//US 413. Introduced Steward Other 
		public String getStewardValue();
		public TextBox getStewardOther();
		public String getStewardOtherValue();
		
		public HasValue<String> getCategory();
		
		//US 404. Interface to return Category List box 
		public ListBoxMVP getCategoryListBox();
		
		public HasValue<String> getCodeSystem();
		public ListBoxMVP getCodeSystemListBox();
		public String getCodeSystemValue();
		public HasValue<String> getCodeSystemVersion();
		public HasValue<String> getRationale();
		public HasValue<String> getComments();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		void setCategoryOptions(List<? extends HasListBox> texts);
		void setCodeSystemOptions(List<? extends HasListBox> texts);
		void setStewardOptions(List<? extends HasListBox> texts);
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public HasValue<String> getOid();
		public HasClickHandlers getGenerateOidButton();

		//US 413. Interface to show or hide Steward Other text box based on the selection.
		public void showOtherTextBox();
		public void hideOtherTextBox();
		/*US537*/
		public DateBoxWithCalendar getLastModifiedDate();
		public HasClickHandlers getSaveCompleteButton();
		
		/**
		 * enable or disable only widgets based on whether or not the Value Set is a Draft
		 * @param isDraft
		 */
		public void enableValueSetWidgetsBasedOnDraft(boolean isDraft);
		public String getOidTitle();
		Label getOtherSpecify();
		Label getStewardLabel();
	}
	
	protected static interface BaseAddDisplay<T> {
		public Widget asWidget();
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public void setTitle(String title);
		public void setReturnToLink(String title);
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public HasClickHandlers getRemoveButton();
		public HasClickHandlers getReturnButton();
		public HasPageSelectionHandler getPageSelectionTool();
		public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		public int getPageSize();
		public void buildDataTable(SearchResults<T> codeLists,boolean checked,int totalResult, int totalPages,int currentPage);
		public HasSelectAllHandler getSelectAllTool();
	}

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
	
	protected abstract BaseDisplay getDetailDisplay();
	protected boolean oidExists = false;
	/*
	 * This flag keeps track of whether the newCodeList/new GroupedCodeList has been clicked, to give the edit/readonly permission.
	 */
	protected boolean isNewCodeList = false; 
	protected List<String> buildValidationMessages(ManageCodeListDetailModel model) {
		ListObjectModelValidator validator = new ListObjectModelValidator();
		List<String> messages = validator.ValidateListObject(model);
		return messages;
	}
	
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
	
	protected boolean isEditable(){
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}
}
