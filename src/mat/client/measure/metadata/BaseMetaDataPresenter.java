package mat.client.measure.metadata;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay;
import mat.client.measure.metadata.MetaDataPresenter.AddEditMeasureTypeDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class BaseMetaDataPresenter {
	protected BaseMetaDataPresenter(final BaseMetaDataDisplay metaDataDisplay,
			final AddEditAuthorsDisplay aDisplay,final AddEditMeasureTypeDisplay mtDisplay, ListBoxCodeProvider listBoxCodeProvider) {
		listBoxCodeProvider.getStewardList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				metaDataDisplay.setMeasureStewardOptions(result);
			}
		});
		
		listBoxCodeProvider.getAuthorsList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				aDisplay.setOptions(result);
			}
		});
		
		listBoxCodeProvider.getMeasureTypeList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				mtDisplay.setOptions(result);
			}
		});
		listBoxCodeProvider.getStatusList(new AsyncCallback<List<? extends HasListBox>>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(List<? extends HasListBox> result) {
				metaDataDisplay.setObjectStatusOptions(result);
			}
		});
	}
	
	protected static interface BaseMetaDataDisplay{
		public Widget asWidget();
		public void setMeasureStewardOptions(List<? extends HasListBox> texts);
		public void setObjectStatusOptions(List<? extends HasListBox> texts);
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
	}
	
	protected static interface BaseAddEditDisplay<T>{
		public Widget asWidget();
		public void buildDataTable(SearchResults<T> searchResults);
		public HasClickHandlers getSaveButton();
		public HasClickHandlers getCancelButton();
		public HasClickHandlers getRemoveButton();
		public HasClickHandlers getReturnButton();
		public void setOptions(List<? extends HasListBox> texts);
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		public void showTextBox();
		public void hideTextBox();
		public void setReturnToLink(String s);
		
	}

}
