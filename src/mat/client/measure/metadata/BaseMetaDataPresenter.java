package mat.client.measure.metadata;

import java.util.List;

import mat.client.codelist.HasListBox;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.metadata.MetaDataPresenter.AddEditAuthorsDisplay;
import mat.client.measure.metadata.MetaDataPresenter.AddEditMeasureTypeDisplay;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.search.SearchResults;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class BaseMetaDataPresenter.
 */
public class BaseMetaDataPresenter {
	
	/**
	 * Instantiates a new base meta data presenter.
	 * 
	 * @param metaDataDisplay
	 *            the meta data display
	 * @param aDisplay
	 *            the a display
	 * @param mtDisplay
	 *            the mt display
	 * @param listBoxCodeProvider
	 *            the list box code provider
	 */
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
	
	/**
	 * The Interface BaseMetaDataDisplay.
	 */
	protected static interface BaseMetaDataDisplay{
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Sets the measure steward options.
		 * 
		 * @param texts
		 *            the new measure steward options
		 */
		public void setMeasureStewardOptions(List<? extends HasListBox> texts);
		
		/**
		 * Sets the object status options.
		 * 
		 * @param texts
		 *            the new object status options
		 */
		public void setObjectStatusOptions(List<? extends HasListBox> texts);
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the save error msg.
		 * 
		 * @return the save error msg
		 */
		public ErrorMessageDisplay  getSaveErrorMsg();

	}
	
	/**
	 * The Interface BaseAddEditDisplay.
	 * 
	 * @param <T>
	 *            the generic type
	 */
	protected static interface BaseAddEditDisplay<T>{
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Builds the data table.
		 * 
		 * @param searchResults
		 *            the search results
		 */
		public void buildDataTable(SearchResults<T> searchResults);
		
		/**
		 * Builds the cell table.
		 *
		 * @param result the result
		 * @param searchText the search text
		 * @param measureSelectedList the measure selected list
		 */
		public void buildCellTable(ManageMeasureSearchModel result, String searchText, 
				List<ManageMeasureSearchModel.Result> measureSelectedList);
		
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
		 * Sets the options.
		 * 
		 * @param texts
		 *            the new options
		 */
		public void setOptions(List<? extends HasListBox> texts);
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Show text box.
		 */
		public void showTextBox();
		
		/**
		 * Hide text box.
		 */
		public void hideTextBox();
		
		/**
		 * Sets the return to link.
		 * 
		 * @param s
		 *            the new return to link
		 */
		public void setReturnToLink(String s);

		
	}

}
