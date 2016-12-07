package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.DTO.AuditLogDTO;
import mat.DTO.SearchHistoryDTO;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.codelist.HasListBox;
import mat.client.codelist.events.OnChangeMeasureDraftOptionsEvent;
import mat.client.codelist.events.OnChangeMeasureVersionOptionsEvent;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.history.HistoryModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchView.AdminObserver;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.measure.metadata.Grid508;
import mat.client.measure.service.MeasureCloningService;
import mat.client.measure.service.MeasureCloningServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.CreateMeasureWidget;
import mat.client.shared.CustomButton;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.FocusableWidget;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.ManageMeasureModelValidator;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureSearchFilterWidget;
import mat.client.shared.MessageDelegate;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SearchWidget;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.search.SearchResultUpdate;
import mat.client.shared.search.SearchResults;
import mat.client.util.ClientConstants;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class ManageMeasurePresenter.
 */
@SuppressWarnings("deprecation")
public class ManageMeasurePresenter implements MatPresenter {
	/**
	 * The Interface BaseDisplay.
	 */
	public static interface BaseDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
	}
	
	/**
	 * The Interface DetailDisplay.
	 */
	public static interface DetailDisplay extends BaseDisplay {
		
		/**
		 * Clear fields.
		 */
		public void clearFields();
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		// US 421. Measure scoring choice is now part of measure creation
		// process.
		/**
		 * Gets the meas scoring choice.
		 * 
		 * @return the meas scoring choice
		 */
		public ListBoxMVP getMeasScoringChoice();
		
		/**
		 * Gets the meas scoring value.
		 * 
		 * @return the meas scoring value
		 */
		public String getMeasScoringValue();
		
		/**
		 * Gets the measure version.
		 * 
		 * @return the measure version
		 */
		public HasValue<String> getMeasureVersion();
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public HasValue<String> getName();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the short name.
		 * 
		 * @return the short name
		 */
		public HasValue<String> getShortName();
		
		/**
		 * Sets the measure name.
		 * 
		 * @param name
		 *            the new measure name
		 */
		public void setMeasureName(String name);
		
		// US 421. Measure scoring choice is now part of measure creation
		// process.
		/**
		 * Sets the scoring choices.
		 * 
		 * @param texts
		 *            the new scoring choices
		 */
		void setScoringChoices(List<? extends HasListBox> texts);
		
		// US 195 showingCautionMsg
		/**
		 * Show caution msg.
		 * 
		 * @param show
		 *            the show
		 */
		public void showCautionMsg(boolean show);
		
		/**
		 * Show measure name.
		 * 
		 * @param show
		 *            the show
		 */
		public void showMeasureName(boolean show);
	}
	
	/**
	 * The Interface DraftDisplay.
	 */
	public static interface DraftDisplay extends BaseDisplay {
		/**
		 * Builds the data table.
		 * 
		 * @param manageDraftMeasureModel
		 *            the ManageDraftMeasureModel
		 */
		void buildDataTable(ManageDraftMeasureModel manageDraftMeasureModel);
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/** Gets the search button.
		 * 
		 * @return search button on search Widget. */
		HasClickHandlers getSearchButton();
		
		/** Gets the search widget.
		 * 
		 * @return SearchWidget. */
		SearchWidget getSearchWidget();
		
		/**
		 * Gets the selected measure.
		 *
		 * @return the selected measure
		 */
		Result getSelectedMeasure();
		
		/** Gets the zoom button.
		 * 
		 * @return Zoom button. */
		CustomButton getZoomButton();
	}
	
	/**
	 * The Interface ExportDisplay.
	 */
	public static interface ExportDisplay extends BaseDisplay {
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the open button.
		 * 
		 * @return the open button
		 */
		public HasClickHandlers getOpenButton();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Checks if is code list.
		 * 
		 * @return true, if is code list
		 */
		public boolean isCodeList();
		
		/**
		 * Checks if is e measure.
		 * 
		 * @return true, if is e measure
		 */
		public boolean isEMeasure();
		
		/**
		 * Checks if is ELM
		 */
		public boolean isELM();
		
		/**
		 * Checks if is e measure package.
		 * 
		 * @return true, if is e measure package
		 */
		boolean isEMeasurePackage();
		
		/**
		 * Checks if is simple xml.
		 * 
		 * @return true, if is simple xml
		 */
		public boolean isSimpleXML();
		
		/**
		 * Sets the measure name.
		 * 
		 * @param name
		 *            the new measure name
		 */
		public void setMeasureName(String name);

		boolean isCQLLibrary();
	}
	
	/**
	 * The Interface HistoryDisplay.
	 */
	public static interface HistoryDisplay extends BaseDisplay {
		
		/**
		 * Clear error message.
		 */
		public void clearErrorMessage();
		
		/**
		 * Gets the measure id.
		 * 
		 * @return the measure id
		 */
		public String getMeasureId();
		
		/**
		 * Gets the measure name.
		 * 
		 * @return the measure name
		 */
		public String getMeasureName();
		
		/**
		 * Gets the return to link.
		 * 
		 * @return the return to link
		 */
		public HasClickHandlers getReturnToLink();
		
		
		/**
		 * Sets the error message.
		 *
		 * @param s the new error message
		 */
		public void setErrorMessage(String s);
		
		/**
		 * Sets the measure id.
		 * 
		 * @param id
		 *            the new measure id
		 */
		public void setMeasureId(String id);
		
		/**
		 * Sets the measure name.
		 * 
		 * @param name
		 *            the new measure name
		 */
		public void setMeasureName(String name);
		
		/**
		 * Sets the page size.
		 *
		 * @param s the new return to link text
		 */
		//		public void setPageSize(int pageNumber);
		
		/**
		 * Sets the return to link text.
		 * 
		 * @param s
		 *            the new return to link text
		 */
		public void setReturnToLinkText(String s);
		
		/**
		 * Builds the cell table.
		 *
		 * @param results the results
		 */
		public void buildCellTable(List<AuditLogDTO> results);
	}
	
	/**
	 * The Interface SearchDisplay.
	 */
	public static interface SearchDisplay extends BaseDisplay {
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		@Override
		public Widget asWidget();
		
		/**
		 * Builds the data table.
		 *
		 * @param manageMeasureSearchModel the manage measure search model
		 * @param filter TODO
		 * @param searchText TODO
		 */
		public void buildDataTable(ManageMeasureSearchModel manageMeasureSearchModel, int filter, String searchText);
		
		/**
		 * Clear transfer check boxes.
		 */
		public void clearTransferCheckBoxes();
		
		/**
		 * Gets the clear button.
		 * 
		 * @return the clear button
		 */
		public HasClickHandlers getClearButton();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		@Override
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the error messages for transfer os.
		 * 
		 * @return the error messages for transfer os
		 */
		public ErrorMessageDisplayInterface getErrorMessagesForTransferOS();
		
		/**
		 * Gets the search button.
		 * 
		 * @return the search button
		 */
		//public HasClickHandlers getSearchButton();
		
		/**
		 * Gets the search string.
		 * 
		 * @return the search string
		 */
		//public HasValue<String> getSearchString();
		
		/**
		 * Gets the transfer button.
		 * 
		 * @return the transfer button
		 */
		public HasClickHandlers getTransferButton();
		
		/**
		 * Sets the admin observer.
		 *
		 * @param adminObserver the new admin observer
		 */
		void setAdminObserver(AdminObserver adminObserver);
		
		/**
		 * Builds the data table.
		 *
		 * @param manageMeasureSearchModel the results
		 * @param filter the filter
		 * @param searchText the search text
		 */
		public void buildCellTable(
				ManageMeasureSearchModel manageMeasureSearchModel, int filter, String searchText);
		
		
		/** Builds the most recent widget. */
		void buildMostRecentWidget();
		
		/**
		 * Clear bulk export check boxes.
		 * 
		 * @param dataTable
		 *            the data table
		 */
		public void clearBulkExportCheckBoxes(Grid508 dataTable);
		
		/**
		 * Clear selections.
		 */
		public void clearSelections();
		
		/**
		 * Gets the bulk export button.
		 * 
		 * @return the bulk export button
		 */
		public HasClickHandlers getBulkExportButton();
		
		/**
		 * Gets the creates the button.
		 * 
		 * @return the creates the button
		 */
		public HasClickHandlers getCreateButton();
		
		/** Gets the creates the measure button.
		 * 
		 * @return the creates the measure button */
		CustomButton getCreateMeasureButton();
		
		/** Gets the creates the measure widget.
		 * 
		 * @return the creates the measure widget */
		CreateMeasureWidget getCreateMeasureWidget();
		
		/**
		 * Gets the error measure deletion.
		 * 
		 * @return the error measure deletion
		 */
		public ErrorMessageDisplay getErrorMeasureDeletion();
		
		/**
		 * Gets the error message display for bulk export.
		 * 
		 * @return the error message display for bulk export
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplayForBulkExport();
		
		/**
		 * Gets the export selected button.
		 * 
		 * @return the export selected button
		 */
		public Button getExportSelectedButton();
		
		/**
		 * Gets the form.
		 * 
		 * @return the form
		 */
		public FormPanel getForm();
		
		/**
		 * Gets the measure data table.
		 * 
		 * @return the measure data table
		 */
		//public Grid508 getMeasureDataTable();
		
		/**
		 * Gets the measure search filter widget.
		 * 
		 * @return the measure search filter widget
		 */
		MeasureSearchFilterWidget getMeasureSearchFilterWidget();
		
		/** Gets the most recent measure widget.
		 * 
		 * @return the most recent measure widget */
		MostRecentMeasureWidget getMostRecentMeasureWidget();
		
		/**
		 * Gets the measure search view.
		 *
		 * @return the measure search view
		 */
		MeasureSearchView getMeasureSearchView();
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		//public HasPageSelectionHandler getPageSelectionTool();
		
		/*public MeasureSearchFilterPanel getMeasureSearchFilterPanel();*/
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		//public int getPageSize();
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		//public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/**
		 * Gets the search button.
		 * 
		 * @return the search button
		 */
		public HasClickHandlers getSearchButton();
		
		/**
		 * Gets the admin search button.
		 *
		 * @return the admin search button
		 */
		public HasClickHandlers getAdminSearchButton();
		
		/**
		 * Gets the search string.
		 * 
		 * @return the search string
		 */
		public HasValue<String> getSearchString();
		
		/**
		 * Gets the admin search string.
		 *
		 * @return the admin search string
		 */
		public HasValue<String> getAdminSearchString();
		
		/**
		 * Gets the selected filter.
		 * 
		 * @return the selected filter
		 */
		int getSelectedFilter();
		
		/**
		 * Gets the selected option.
		 * 
		 * @return the selected option
		 */
		public String getSelectedOption();
		
		/**
		 * Gets the select id for edit tool.
		 * 
		 * @return the select id for edit tool
		 */
		public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool();
		
		/**
		 * Gets the success measure deletion.
		 * 
		 * @return the success measure deletion
		 */
		public SuccessMessageDisplay getSuccessMeasureDeletion();
		
		/** Gets the zoom button.
		 * 
		 * @return the zoom button */
		CustomButton getZoomButton();
	}
	
	/**
	 * The Interface ShareDisplay.
	 */
	public static interface ShareDisplay extends BaseDisplay {
		
		/**
		 * Builds the data table.
		 *
		 * @param adapter the adapter
		 */
		public void buildDataTable(UserShareInfoAdapter adapter);
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the page selection tool.
		 * 
		 * @return the page selection tool
		 */
		//public HasPageSelectionHandler getPageSelectionTool();
		
		/**
		 * Gets the page size.
		 * 
		 * @return the page size
		 */
		//public int getPageSize();
		
		/**
		 * Gets the page size selection tool.
		 * 
		 * @return the page size selection tool
		 */
		//public HasPageSizeSelectionHandler getPageSizeSelectionTool();
		
		/**
		 * Gets the share button.
		 * 
		 * @return the share button
		 */
		public HasClickHandlers getShareButton();
		
		/**
		 * Private checkbox.
		 * 
		 * @return the checks for value change handlers
		 */
		public HasValueChangeHandlers<Boolean> privateCheckbox();
		
		/**
		 * Sets the measure name.
		 * 
		 * @param name
		 *            the new measure name
		 */
		public void setMeasureName(String name);
		
		/**
		 * Sets the private.
		 * 
		 * @param isPrivate
		 *            the new private
		 */
		public void setPrivate(boolean isPrivate);
	}
	
	/**
	 * The Interface TransferDisplay.
	 */
	public static interface TransferDisplay {
		
		/**
		 * As widget.
		 * 
		 * @return the widget
		 */
		public Widget asWidget();
		
		/**
		 * Builds the cell table.
		 *
		 * @param results the results
		 */
		void buildCellTable( TransferMeasureOwnerShipModel results);
		
		/**
		 * Builds the html for measures.
		 * 
		 * @param measureList
		 *            the measure list
		 */
		void buildHTMLForMeasures(
				List<ManageMeasureSearchModel.Result> measureList);
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		public HasClickHandlers getCancelButton();
		
		/**
		 * Gets the error message display.
		 * 
		 * @return the error message display
		 */
		public ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		/**
		 * Gets the selected value.
		 * 
		 * @return the selected value
		 */
		public String getSelectedValue();
		
		/**
		 * Gets the success message display.
		 * 
		 * @return the success message display
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the search button.
		 *
		 * @return the search button
		 */
		HasClickHandlers getSearchButton();
		
		/**
		 * Gets the search string.
		 *
		 * @return the search string
		 */
		HasValue<String> getSearchString();
		
		/**
		 * Clear radio buttons.
		 */
		public void clearRadioButtons();
	}
	
	/**
	 * The Interface VersionDisplay.
	 */
	public static interface VersionDisplay extends BaseDisplay {
		
		/**
		 * Builds the data table.
		 * 
		 * @param results
		 *            the results
		 */
		void buildDataTable(SearchResults<ManageMeasureSearchModel.Result> results);
		
		/**
		 * Gets the cancel button.
		 * 
		 * @return the cancel button
		 */
		HasClickHandlers getCancelButton();
		
		/**
		 * Gets the major radio button.
		 * 
		 * @return the major radio button
		 */
		RadioButton getMajorRadioButton();
		
		/**
		 * Gets the minor radio button.
		 * 
		 * @return the minor radio button
		 */
		RadioButton getMinorRadioButton();
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		HasClickHandlers getSaveButton();
		
		/** Gets the search button.
		 * 
		 * @return search Button from search widget. */
		HasClickHandlers getSearchButton();
		
		/** Gets the search widget.
		 * 
		 * @return search widget. */
		SearchWidget getSearchWidget();
		
		/**
		 * Gets the selected measure.
		 *
		 * @return the selected measure
		 */
		Result getSelectedMeasure();
		
		/** Gets the zoom button.
		 * 
		 * @return zoom button. */
		CustomButton getZoomButton();
	}
	
	
	/** The bulk export measure ids. */
	private List<String> bulkExportMeasureIds;
	
	/** The cancel click handler. */
	private ClickHandler cancelClickHandler = new ClickHandler() {
		@Override
		public void onClick(ClickEvent event) {
			draftDisplay.getSearchWidget().getSearchInput().setValue("");
			versionDisplay.getSearchWidget().getSearchInput().setValue("");
			detailDisplay.getName().setValue("");
			detailDisplay.getShortName().setValue("");
			searchDisplay.clearSelections();
			displaySearch();
		}
	};
	
	/** The current details. */
	private ManageMeasureDetailModel currentDetails;
	
	/** The current export id. */
	private String currentExportId;
	
	/** The current share details. */
	private ManageMeasureShareModel currentShareDetails;
	
	/** The current user role. */
	final String currentUserRole = MatContext.get().getLoggedInUserRole();
	
	/** The detail display. */
	private DetailDisplay detailDisplay;
	
	/** The draft display. */
	private DraftDisplay draftDisplay;
	
	/** The draft measure results. */
	private ManageDraftMeasureModel draftMeasureResults;
	
	/** The export display. */
	private ExportDisplay exportDisplay;
	
	/** The history display. */
	private HistoryDisplay historyDisplay;
	
	/** The history model. */
	private HistoryModel historyModel;
	
	/** The is clone. */
	private boolean isClone;
	
	/** The is create measure widget visible. */
	boolean isCreateMeasureWidgetVisible = false;
	
	/** The is measure deleted. */
	private boolean isMeasureDeleted = false;
	
	/** The is measure search filter visible. */
	boolean isMeasureSearchFilterVisible = true;
	
	/** The is search visible on draft. */
	boolean isSearchVisibleOnDraft = true;
	
	/** The is search visible on version. */
	boolean isSearchVisibleOnVersion = true;
	
	/** The sub skip content holder. */
	private static FocusableWidget subSkipContentHolder;
	
	/**
	 * Sets the sub skip embedded link.
	 *
	 * @param name the new sub skip embedded link
	 */
	public static void setSubSkipEmbeddedLink(String name) {
		if (subSkipContentHolder == null) {
			subSkipContentHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Sub Content"));
		}
		Mat.removeInputBoxFromFocusPanel(subSkipContentHolder.getElement());
		Widget w = SkipListBuilder.buildSubSkipList(name);
		subSkipContentHolder.clear();
		subSkipContentHolder.add(w);
		subSkipContentHolder.setFocus(true);
	}
	
	/** The listof measures. */
	List<ManageMeasureSearchModel.Result> listofMeasures = new ArrayList<ManageMeasureSearchModel.Result>();
	
	/** The manage measure search model. */
	private ManageMeasureSearchModel manageMeasureSearchModel;
	
	/** The measure deletion. */
	private boolean measureDeletion = false;
	
	/** The measure del message. */
	private String measureDelMessage;
	
	/** The model. */
	private TransferMeasureOwnerShipModel model = null;
	
	/** The panel. */
	private ContentWithHeadingWidget panel = new ContentWithHeadingWidget();
	
	/** The search display. */
	private SearchDisplay searchDisplay;
	
	/** The share display. */
	private ShareDisplay shareDisplay;
	
	/** The share start index. */
	private int shareStartIndex = 1;
	
	/** The start index. */
	private int startIndex = 1;
	
	/** The transfer display. */
	private TransferDisplay transferDisplay;
	
	/** The user share info. */
	private UserShareInfoAdapter userShareInfo = new UserShareInfoAdapter();
	
	/** The version display. */
	private VersionDisplay versionDisplay;
	
	/** The version measure results. */
	private ManageVersionMeasureModel versionMeasureResults;
	
	/**
	 * Instantiates a new manage measure presenter.
	 *
	 * @param sDisplayArg            the s display arg
	 * @param dDisplayArg            the d display arg
	 * @param shareDisplayArg            the share display arg
	 * @param exportDisplayArg            the export display arg
	 * @param hDisplay            the h display
	 * @param vDisplay            the v display
	 * @param dDisplay            the d display
	 * @param transferDisplay            the transfer display
	 */
	public ManageMeasurePresenter(SearchDisplay sDisplayArg,
			DetailDisplay dDisplayArg, ShareDisplay shareDisplayArg,
			ExportDisplay exportDisplayArg, HistoryDisplay hDisplay,
			VersionDisplay vDisplay, DraftDisplay dDisplay,
			final TransferDisplay transferDisplay) {
		
		searchDisplay = sDisplayArg;
		detailDisplay = dDisplayArg;
		historyDisplay = hDisplay;
		shareDisplay = shareDisplayArg;
		exportDisplay = exportDisplayArg;
		draftDisplay = dDisplay;
		versionDisplay = vDisplay;
		this.transferDisplay = transferDisplay;
		displaySearch();
		if (searchDisplay != null) {
			searchDisplay.getMeasureSearchFilterWidget().setVisible(isMeasureSearchFilterVisible);
			searchDisplay.getCreateMeasureWidget().setVisible(isCreateMeasureWidgetVisible);
			searchDisplayHandlers(searchDisplay);
		}
		
		if (draftDisplay != null) {
			draftDisplayHandlers(draftDisplay);
		}
		if (versionDisplay != null) {
			versionDisplayHandlers(versionDisplay);
		}
		if (historyDisplay != null) {
			historyDisplayHandlers(historyDisplay);
		}
		if (shareDisplay != null) {
			shareDisplayHandlers(shareDisplay);
		}
		if (transferDisplay != null) {
			transferDisplayHandlers(transferDisplay);
		}
		if (detailDisplay != null) {
			detailDisplayHandlers(detailDisplay);
		}
		if (exportDisplay != null) {
			exportDisplayHandlers(exportDisplay);
		}
		
		// This event will be called when measure is successfully deleted and
		// then MeasureLibrary is reloaded.
		MatContext
		.get()
		.getEventBus()
		.addHandler(MeasureDeleteEvent.TYPE,
				new MeasureDeleteEvent.Handler() {
			
			@Override
			public void onDeletion(MeasureDeleteEvent event) {
				displaySearch();
				if (event.isDeleted()) {
					
					isMeasureDeleted = true;
					measureDeletion = true;
					measureDelMessage = event.getMessage();
					System.out.println("Event - is Deleted : "
							+ isMeasureDeleted
							+ measureDeletion);
					System.out.println("Event - message : "
							+ measureDelMessage);
				} else {
					// searchDisplay.getErrorMeasureDeletion().setMessage("Measure deletion Failed.");
					isMeasureDeleted = false;
					measureDeletion = true;
					measureDelMessage = event.getMessage();
					System.out
					.println("Event - is NOT Deleted : "
							+ isMeasureDeleted
							+ measureDeletion);
					System.out.println("Event - message : "
							+ measureDelMessage);
				}
			}
		});
		
		HandlerManager eventBus = MatContext.get().getEventBus();
		eventBus.addHandler(OnChangeMeasureDraftOptionsEvent.TYPE,
				new OnChangeMeasureDraftOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(
					OnChangeMeasureDraftOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) draftDisplay
						.getSaveButton();
				button.setFocus(true);
			}
		});
		
		eventBus.addHandler(OnChangeMeasureVersionOptionsEvent.TYPE,
				new OnChangeMeasureVersionOptionsEvent.Handler() {
			@Override
			public void onChangeOptions(
					OnChangeMeasureVersionOptionsEvent event) {
				PrimaryButton button = (PrimaryButton) versionDisplay
						.getSaveButton();
				button.setFocus(true);
			}
		});
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		isMeasureDeleted = false;
		measureDeletion = false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		Command waitForUnlock = new Command() {
			@Override
			public void execute() {
				if (!MatContext.get().getMeasureLockService().isResettingLock()) {
					displaySearch();
				} else {
					DeferredCommand.addCommand(this);
				}
			}
		};
		if (MatContext.get().getMeasureLockService().isResettingLock()) {
			waitForUnlock.execute();
		} else {
			displaySearch();
		}
		// Commented for MAT-1929 : Retain Filters at Measure library
		// screen.This message is commented since loading Please message was
		// getting removed when search was performed.
		// Mat.hideLoadingMessage();
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Builds the export url.
	 * 
	 * @return the string
	 */
	private String buildExportURL() {
		String url = GWT.getModuleBaseURL() + "export?id=" + currentExportId
				+ "&format=";
		System.out.println("URL: " + url);
		
		url += (exportDisplay.isEMeasure() ? "emeasure" : 
				exportDisplay.isSimpleXML() ? "simplexml" :
				exportDisplay.isCodeList() ? "codelist" : 
				exportDisplay.isCQLLibrary() ? "cqlLibrary" : 
				exportDisplay.isELM() ? "elm" :	
				"zip");
		return url;
	}
	
	/**
	 * Bulk export.
	 * 
	 * @param selectedMeasureIds
	 *            the selected measure ids
	 */
	private void bulkExport(List<String> selectedMeasureIds) {
		String measureId = "";
		
		MatContext
		.get()
		.getAuditService()
		.recordMeasureEvent(selectedMeasureIds,
				"Measure Package Exported", null, true,
				new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay
				.getErrorMessageDisplay()
				.setMessage(
						"Error while adding bulk export log entry.");
			}
			
			@Override
			public void onSuccess(Void result) {
			}
		});
		
		for (String id : selectedMeasureIds) {
			measureId += id + "&id=";
		}
		measureId = measureId.substring(0, measureId.lastIndexOf("&"));
		String url = GWT.getModuleBaseURL() + "bulkExport?id=" + measureId;
		url += "&type=open";
		manageMeasureSearchModel.getSelectedExportIds().clear();
		FormPanel form = searchDisplay.getForm();
		form.setAction(url);
		form.setEncoding(FormPanel.ENCODING_URLENCODED);
		form.setMethod(FormPanel.METHOD_POST);
		form.submit();
	}
	
	/**
	 * Clear radio button selection.
	 */
	private void clearRadioButtonSelection() {
		versionDisplay.getMajorRadioButton().setValue(false);
		versionDisplay.getMinorRadioButton().setValue(false);
	}
	
	/**
	 * Clone measure.
	 * 
	 * @param currentDetails
	 *            the current details
	 * @param isDraftCreation
	 *            the is draft creation
	 */
	private void cloneMeasure(final ManageMeasureDetailModel currentDetails,
			final boolean isDraftCreation) {
		String loggedinUserId = MatContext.get().getLoggedinUserId();
		searchDisplay.getSuccessMeasureDeletion().clear();
		searchDisplay.getErrorMeasureDeletion().clear();
		MeasureCloningServiceAsync mcs = (MeasureCloningServiceAsync) GWT
				.create(MeasureCloningService.class);
		
		mcs.clone(currentDetails, loggedinUserId, isDraftCreation,
				new AsyncCallback<ManageMeasureSearchModel.Result>() {
			@Override
			public void onFailure(Throwable caught) {
				// O&M 17
				((Button) draftDisplay.getSaveButton())
				.setEnabled(true);
				
				Mat.hideLoadingMessage();
				shareDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(ManageMeasureSearchModel.Result result) {
				fireMeasureSelectedEvent(result.getId(), result
						.getVersion(), result.getName(), result
						.getShortName(), result.getScoringType(),
						result.isEditable(), result.isMeasureLocked(),
						result.getLockedUserId(result
								.getLockedUserInfo()));
				// fireMeasureEditEvent();
				Mat.hideLoadingMessage();
				isClone = false;

				// LOGIT
				if (isDraftCreation) {
					MatContext
					.get()
					.getAuditService()
					.recordMeasureEvent(
							result.getId(),
							"Draft Created",
							"Draft created based on Version "
									+ result.getVersionValue(),
									false,
									new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(
										Throwable caught) {
									// O&M 17
									((Button) draftDisplay
											.getSaveButton())
											.setEnabled(true);
								}
								
								@Override
								public void onSuccess(
										Boolean result) {
									// O&M 17
									((Button) draftDisplay
											.getSaveButton())
											.setEnabled(true);
								}
							});
					
				}
			}
		});
	}
	
	/**
	 * Creates the draft.
	 */
	private void createDraft() {
		searchMeasuresForDraft(draftDisplay.getSearchWidget().getSearchInput().getText());
		searchDisplay.getSuccessMeasureDeletion().clear();
		searchDisplay.getErrorMeasureDeletion().clear();
		draftDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		panel.getButtonPanel().clear();
		panel.setButtonPanel(null, draftDisplay.getZoomButton());
		draftDisplay.getSearchWidget().setVisible(false);
		isSearchVisibleOnDraft = false;
		panel.setHeading("My Measures > Create Draft of Existing Measure", "MeasureLibrary");
		panel.setContent(draftDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Creates the draft of selected version.
	 * 
	 * @param currentDetails
	 *            the current details
	 */
	private void createDraftOfSelectedVersion(ManageMeasureDetailModel currentDetails) {
		cloneMeasure(currentDetails, true);
	}
	
	/**
	 * Creates the new.
	 */
	private void createNew() {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		currentDetails = new ManageMeasureDetailModel();
		displayDetailForAdd();
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Creates the version.
	 */
	private void createVersion() {
		searchMeasuresForVersion(versionDisplay.getSearchWidget().getSearchInput().getValue());
		versionDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		searchDisplay.getSuccessMeasureDeletion().clear();
		searchDisplay.getErrorMeasureDeletion().clear();
		panel.getButtonPanel().clear();
		panel.setButtonPanel(null, versionDisplay.getZoomButton());
		versionDisplay.getSearchWidget().setVisible(false);
		isSearchVisibleOnVersion = false;
		panel.setHeading("My Measures > Create Measure Version of Draft", "MeasureLibrary");
		panel.setContent(versionDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
		clearRadioButtonSelection();
	}
	
	/**
	 * Detail display handlers.
	 * 
	 * @param detailDisplay
	 *            the detail display
	 */
	private void detailDisplayHandlers(final DetailDisplay detailDisplay) {
		detailDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				update();
			}
		});
		
		detailDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
		// US 421. Retrieve the Measure scoring choices from db.
		MatContext
		.get()
		.getListBoxCodeProvider()
		.getScoringList(
				new AsyncCallback<List<? extends HasListBox>>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MessageDelegate.s_ERR_RETRIEVE_SCORING_CHOICES);
					}
					
					@Override
					public void onSuccess(
							List<? extends HasListBox> result) {
						detailDisplay.setScoringChoices(result);
					}
				});
	}
	
	/**
	 * Display detail for add.
	 */
	private void displayDetailForAdd() {
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Create New Measure", "MeasureLibrary");
		setDetailsToView();
		detailDisplay.showMeasureName(false);
		detailDisplay.showCautionMsg(false);
		panel.setContent(detailDisplay.asWidget());
	}
	
	/**
	 * Display detail for clone.
	 */
	private void displayDetailForClone() {
		detailDisplay.clearFields();
		detailDisplay.setMeasureName(currentDetails.getName());
		detailDisplay.showMeasureName(true);
		detailDisplay.getMeasScoringChoice().setValueMetadata(
				currentDetails.getMeasScoring());
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Clone Measure", "MeasureLibrary");
		panel.setContent(detailDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Display detail for edit.
	 */
	private void displayDetailForEdit() {
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Edit Measure", "MeasureLibrary");
		detailDisplay.showMeasureName(false);
		detailDisplay.showCautionMsg(true);
		setDetailsToView();
		panel.setContent(detailDisplay.asWidget());
	}
	
	/**
	 * Display history.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param measureName
	 *            the measure name
	 */
	private void displayHistory(String measureId, String measureName) {
		int startIndex = 0;
		int pageSize = Integer.MAX_VALUE;
		String heading = "My Measures > History";
		if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
				.getLoggedInUserRole())) {
			heading = "Measures > History";
		}
		panel.getButtonPanel().clear();
		panel.setHeading(heading, "MeasureLibrary");
		searchHistory(measureId, startIndex, pageSize);
		historyDisplay.setMeasureId(measureId);
		historyDisplay.setMeasureName(measureName);
		panel.setContent(historyDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Display search.
	 */
	private void displaySearch() {
		
		String heading = "Measure Library";
		int filter;
		panel.setHeading(heading, "MeasureLibrary");
		setSubSkipEmbeddedLink("measureserachView_mainPanel");
		FlowPanel fp = new FlowPanel();
		fp.getElement().setId("fp_FlowPanel");
		/*setSubSkipEmbeddedLink("measureserachView_mainPanel");
		fp.add(subSkipContentHolder);*/
		if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
				.getLoggedInUserRole())) {
			heading = "";
			filter = 1;// ALL Measures
			search(searchDisplay.getAdminSearchString().getValue(), 1,
					Integer.MAX_VALUE, filter);
			fp.add(searchDisplay.asWidget());
		} else {
			// MAT-1929 : Retain filters at measure library screen
			searchDisplay.getCreateMeasureWidget().setVisible(false);
			searchDisplay.getMeasureSearchFilterWidget().setVisible(true);
			isMeasureSearchFilterVisible = true;
			isCreateMeasureWidgetVisible = false;
			searchDisplay.getMeasureSearchFilterWidget().getSearchFilterDisclosurePanel().setOpen(false);
			filter = searchDisplay.getSelectedFilter();
			search(searchDisplay.getSearchString().getValue(), 1,
					Integer.MAX_VALUE, filter);
			searchRecentMeasures();
			panel.getButtonPanel().clear();
			panel.setButtonPanel(searchDisplay.getCreateMeasureButton(), searchDisplay.getZoomButton());
			fp.add(searchDisplay.asWidget());
		}
		// MAT-1929: Retain filters at measure library screen. commented
		// resetFilters method to retain filter state.
		// searchDisplay.getMeasureSearchFilterPanel().resetFilter();
		
		//panel.setHeading(heading, "MainContent");
		
		
		// panel.setEmbeddedLink("MainContent");
		panel.setContent(fp);
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/** Display share.
	 * 
	 * @param id the id
	 * @param name the name */
	private void displayShare(String id, String name) {
		getShareDetails(id, 1);
		shareDisplay.setMeasureName(name);
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Measure Sharing", "MeasureLibrary");
		panel.setContent(shareDisplay.asWidget());
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Display transfer view.
	 *
	 * @param searchString the search string
	 * @param startIndex the start index
	 * @param pageSize the page size
	 */
	private void displayTransferView(String searchString, int startIndex, int pageSize) {
		final ArrayList<ManageMeasureSearchModel.Result> transferMeasureResults = (ArrayList<Result>) manageMeasureSearchModel
				.getSelectedTransferResults();
		pageSize = Integer.MAX_VALUE;
		searchDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessagesForTransferOS().clear();
		transferDisplay.getErrorMessageDisplay().clear();
		if (transferMeasureResults.size() != 0) {
			showSearchingBusy(true);
			MatContext
			.get()
			.getMeasureService()
			.searchUsers(searchString, startIndex, pageSize,
					new AsyncCallback<TransferMeasureOwnerShipModel>() {
				
				@Override
				public void onFailure(Throwable caught) {
					Window.alert(MatContext.get()
							.getMessageDelegate()
							.getGenericErrorMessage());
					showSearchingBusy(false);
				}
				
				@Override
				public void onSuccess(
						TransferMeasureOwnerShipModel result) {
					
					transferDisplay
					.buildHTMLForMeasures(transferMeasureResults);
					transferDisplay.buildCellTable(result);
					panel.setHeading(
							"Measure Library Ownership >  Measure Ownership Transfer",
							"MeasureLibrary");
					panel.setContent(transferDisplay.asWidget());
					showSearchingBusy(false);
					model = result;
				}
			});
		} else {
			searchDisplay.getErrorMessagesForTransferOS().setMessage(
					MatContext.get().getMessageDelegate()
					.getTransferCheckBoxErrorMeasure());
		}
		
	}
	
	/**
	 * Draft display handlers.
	 * 
	 * @param draftDisplay
	 *            the draft display
	 */
	private void draftDisplayHandlers(final DraftDisplay draftDisplay) {
		
		TextBox searchWidget = (draftDisplay.getSearchWidget().getSearchInput());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) draftDisplay.getSearchButton()).click();
				}
			}
		});
		
		draftDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				draftDisplay.getErrorMessageDisplay().clear();
				isSearchVisibleOnDraft = !isSearchVisibleOnDraft;
				draftDisplay.getSearchWidget().setVisible(isSearchVisibleOnDraft);
			}
		});
		
		draftDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchMeasuresForDraft(draftDisplay.getSearchWidget().getSearchInput().getText());
			}
		});
		
		draftDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// O&M 17
				((Button) draftDisplay.getSaveButton()).setEnabled(false);
				
				ManageMeasureSearchModel.Result selectedMeasure = draftDisplay.getSelectedMeasure();
				if ((selectedMeasure != null) && (selectedMeasure.getId() != null)) {
					MatContext.get().getMeasureService().getMeasure(selectedMeasure.getId(),
							new AsyncCallback<ManageMeasureDetailModel>() {
						@Override
						public void onFailure(Throwable caught) {
							// O&M 17
							((Button) draftDisplay.getSaveButton()).setEnabled(true);
							draftDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
									.getMessageDelegate().getGenericErrorMessage());
							MatContext.get().recordTransactionEvent(null, null,	null,
									"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
						}
						
						@Override
						public void onSuccess(
								ManageMeasureDetailModel result) {
							currentDetails = result;
							createDraftOfSelectedVersion(currentDetails);
						}
					});
				} else {
					// O&M 17
					((Button) draftDisplay.getSaveButton()).setEnabled(true);
					draftDisplay.getErrorMessageDisplay()
					.setMessage("Please select a Measure Version to create a Draft.");
				}
				
			}
		});
		draftDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
	}
	
	/**
	 * Edits the.
	 * 
	 * @param name
	 *            the name
	 */
	private void edit(String name) {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		MatContext
		.get()
		.getMeasureService()
		.getMeasure(name,
				new AsyncCallback<ManageMeasureDetailModel>() {
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay
				.getErrorMessageDisplay()
				.setMessage(
						MatContext
						.get()
						.getMessageDelegate()
						.getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(),
								0);
			}
			
			@Override
			public void onSuccess(
					ManageMeasureDetailModel result) {
				currentDetails = result;
				displayDetailForEdit();
			}
		});
	}
	
	/**
	 * Edits the clone.
	 * 
	 * @param id
	 *            the id
	 */
	private void editClone(String id) {
		detailDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		MatContext.get().getMeasureService()
		.getMeasure(id, new AsyncCallback<ManageMeasureDetailModel>() {
			
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate()
						.getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				currentDetails = result;
				displayDetailForClone();
			}
		});
	}
	
	/**
	 * Export.
	 * 
	 * @param id
	 *            the id
	 * @param name
	 *            the name
	 */
	private void export(String id, String name) {
		// US 170
		MatContext
		.get()
		.getAuditService()
		.recordMeasureEvent(id, "Measure Exported", null, true,
				new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				detailDisplay
				.getErrorMessageDisplay()
				.setMessage(
						"Error while adding export comment");
			}
			
			@Override
			public void onSuccess(Boolean result) {
			}
			
		});
		currentExportId = id;
		exportDisplay.getErrorMessageDisplay().clear();
		searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		panel.getButtonPanel().clear();
		panel.setHeading("My Measures > Export", "MeasureLibrary");
		panel.setContent(exportDisplay.asWidget());
		exportDisplay.setMeasureName(name);
		Mat.focusSkipLists("MeasureLibrary");
	}
	
	/**
	 * Export display handlers.
	 * 
	 * @param exportDisplay
	 *            the export display
	 */
	private void exportDisplayHandlers(final ExportDisplay exportDisplay) {
		exportDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		exportDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveExport();
			}
		});
		exportDisplay.getOpenButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				openExport();
			}
		});
		
	}
	
	/**
	 * Fire measure edit event.
	 */
	private void fireMeasureEditEvent() {
		MeasureEditEvent evt = new MeasureEditEvent();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	/**
	 * Fire measure selected event.
	 * 
	 * @param id
	 *            the id
	 * @param version
	 *            the version
	 * @param name
	 *            the name
	 * @param shortName
	 *            the short name
	 * @param scoringType
	 *            the scoring type
	 * @param isEditable
	 *            the is editable
	 * @param isLocked
	 *            the is locked
	 * @param lockedUserId
	 *            the locked user id
	 */
	private void fireMeasureSelectedEvent(String id, String version,
			String name, String shortName, String scoringType,
			boolean isEditable, boolean isLocked, String lockedUserId) {
		MeasureSelectedEvent evt = new MeasureSelectedEvent(id, version, name,
				shortName, scoringType, isEditable, isLocked, lockedUserId);
		searchDisplay.getSuccessMeasureDeletion().clear();
		searchDisplay.getErrorMeasureDeletion().clear();
		MatContext.get().getEventBus().fireEvent(evt);
	}
	
	/**
	 * Gets the bulk export measure ids.
	 * 
	 * @return the bulkExportMeasureIds
	 */
	public List<String> getBulkExportMeasureIds() {
		return bulkExportMeasureIds;
	}
	
	/**
	 * Gets the share details.
	 * 
	 * @param id
	 *            the id
	 * @param startIndex
	 *            the start index
	 * @return the share details
	 */
	private void getShareDetails(String id, int startIndex) {
		shareStartIndex = startIndex;
		searchDisplay.getSuccessMeasureDeletion().clear();
		searchDisplay.getErrorMeasureDeletion().clear();
		MatContext
		.get()
		.getMeasureService()
		.getUsersForShare(id, startIndex, Integer.MAX_VALUE,
				new AsyncCallback<ManageMeasureShareModel>() {
			@Override
			public void onFailure(Throwable caught) {
				shareDisplay
				.getErrorMessageDisplay()
				.setMessage(
						MatContext
						.get()
						.getMessageDelegate()
						.getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(),
								0);
			}
			
			@Override
			public void onSuccess(ManageMeasureShareModel result) {
				currentShareDetails = result;
				shareDisplay.setPrivate(currentShareDetails
						.isPrivate());
				userShareInfo.setData(result);
				shareDisplay.buildDataTable(userShareInfo);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	
	/**
	 * Gets the widget with heading.
	 * 
	 * @param widget
	 *            the widget
	 * @param heading
	 *            the heading
	 * @return the widget with heading
	 */
	public Widget getWidgetWithHeading(Widget widget, String heading) {
		FlowPanel vPanel = new FlowPanel();
		Label h = new Label(heading);
		h.addStyleName("myAccountHeader");
		h.addStyleName("leftAligned");
		vPanel.add(h);
		vPanel.add(widget);
		vPanel.addStyleName("myAccountPanel");
		widget.addStyleName("myAccountPanelContent");
		return vPanel;
	}
	
	/**
	 * History display handlers.
	 * 
	 * @param historyDisplay
	 *            the history display
	 */
	private void historyDisplayHandlers(final HistoryDisplay historyDisplay) {
		
		historyDisplay.getReturnToLink().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				displaySearch();
				
			}
		});
		
	}
	
	/**
	 * Checks if is valid.
	 * 
	 * @param model
	 *            the model
	 * @return true, if is valid
	 */
	public boolean isValid(ManageMeasureDetailModel model) {
		ManageMeasureModelValidator manageMeasureModelValidator = new ManageMeasureModelValidator();
		List<String> message = manageMeasureModelValidator.isValidMeasure(model);
		/*new ArrayList<String>();
		if ((model.getName() == null) || "".equals(model.getName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getMeasureNameRequiredMessage());
		}
		if ((model.getShortName() == null)
				|| "".equals(model.getShortName().trim())) {
			message.add(MatContext.get().getMessageDelegate()
					.getAbvNameRequiredMessage());
		}
		
		// US 421. Validate Measure Scoring choice
		String scoring = model.getMeasScoring();
		String enteredScoringValue = detailDisplay.getMeasScoringValue();
		if ((scoring == null) || !isValidValue(model.getMeasScoring())
				|| enteredScoringValue.equals("--Select--")) {
			message.add(MatContext.get().getMessageDelegate().s_ERR_MEASURE_SCORE_REQUIRED);
		}*/
		
		boolean valid = message.size() == 0;
		if (!valid) {
			detailDisplay.getErrorMessageDisplay().setMessages(message);
			Mat.hideLoadingMessage();
		} else {
			detailDisplay.getErrorMessageDisplay().clear();
			searchDisplay.getErrorMessageDisplayForBulkExport().clear();
		}
		return valid;
	}
	/**
	 * Verifies the valid value required for the list box.
	 * 
	 * @param value
	 *            the value
	 * @return true, if is valid value
	 */
	private boolean isValidValue(String value) {
		return !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
	
	/**
	 * Open export.
	 */
	private void openExport() {
		Window.open(buildExportURL() + "&type=open", "_blank", "");
	}
	
	/**
	 * Save export.
	 */
	private void saveExport() {
		Window.open(buildExportURL() + "&type=save", "_self", "");
	}
	
	/**
	 * Save finalized version.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param isMajor
	 *            the is major
	 * @param version
	 *            the version
	 */
	private void saveFinalizedVersion(final String measureId,
			final boolean isMajor, final String version) {
		Mat.showLoadingMessage();
		MatContext
		.get()
		.getMeasureService()
		.saveFinalizedVersion(measureId, isMajor, version,
				new AsyncCallback<SaveMeasureResult>() {
			@Override
			public void onFailure(Throwable caught) {
				Mat.hideLoadingMessage();
				versionDisplay
				.getErrorMessageDisplay()
				.setMessage(
						MatContext
						.get()
						.getMessageDelegate()
						.getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(),
								0);
			}
			
			@Override
			public void onSuccess(SaveMeasureResult result) {
				Mat.hideLoadingMessage();
				if (result.isSuccess()) {
					searchDisplay.clearSelections();
					displaySearch();
					String versionStr = result.getVersionStr();
					MatContext
					.get()
					.getAuditService()
					.recordMeasureEvent(
							measureId,
							"Measure Versioned",
							"Measure Version "
									+ versionStr
									+ " created",
									false,
									new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(
										Throwable caught) {
									
								}
								
								@Override
								public void onSuccess(
										Boolean result) {
									
								}
							});
				}
			}
		});
	}
	
	/**
	 * Search.
	 * 
	 * @param searchText
	 *            the search text
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 * @param filter
	 *            the filter
	 */
	private void search(final String searchText, int startIndex, int pageSize,
			final int filter) {
		final String lastSearchText = (searchText != null) ? searchText
				.trim() : null;
				
				// This to fetch all Measures if user role is Admin. This will go away
				// when Pagination will be implemented in Measure Library.
				if (currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
					//pageSize = Integer.MAX_VALUE;
					pageSize = 25;
					showSearchingBusy(true);
					MatContext
					.get()
					.getMeasureService()
					.search(searchText, startIndex, pageSize, filter,
							new AsyncCallback<ManageMeasureSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							detailDisplay
							.getErrorMessageDisplay()
							.setMessage(
									MatContext
									.get()
									.getMessageDelegate()
									.getGenericErrorMessage());
							MatContext
							.get()
							.recordTransactionEvent(
									null,
									null,
									null,
									"Unhandled Exception: "
											+ caught.getLocalizedMessage(),
											0);
							showAdminSearchingBusy(false);
						}
						
						@Override
						public void onSuccess(
								ManageMeasureSearchModel result) {
							result.setSelectedTransferIds(new ArrayList<String>());
							result.setSelectedTransferResults(new ArrayList<Result>());
							manageMeasureSearchModel = result;
							MeasureSearchView measureSearchView = new MeasureSearchView();
							measureSearchView.setData(result);
							
							MatContext.get()
							.setManageMeasureSearchModel(
									manageMeasureSearchModel);
							searchDisplay.setAdminObserver(new MeasureSearchView.AdminObserver() {
								
								@Override
								public void onTransferSelectedClicked(Result result) {
									searchDisplay
									.getErrorMessageDisplay()
									.clear();
									searchDisplay
									.getErrorMessagesForTransferOS()
									.clear();
									updateTransferIDs(result,
											manageMeasureSearchModel);
									
								}
								
								@Override
								public void onHistoryClicked(Result result) {
									historyDisplay
									.setReturnToLinkText("<< Return to MeasureLibrary Owner Ship");
									//									if (!result.isEditable()) {
									//										historyDisplay
									//										.setUserCommentsReadOnly(true);
									//									} else {
									//										historyDisplay
									//										.setUserCommentsReadOnly(false);
									//									}
									
									displayHistory(
											result.getId(),
											result.getName());
									
								}
							});
							if ((result.getResultsTotal() == 0)
									&& !lastSearchText.isEmpty()) {
								searchDisplay
								.getErrorMessageDisplay()
								.setMessage(
										MatContext
										.get()
										.getMessageDelegate()
										.getNoMeasuresMessage());
							} else {
								searchDisplay
								.getErrorMessageDisplay()
								.clear();
								searchDisplay
								.getErrorMessagesForTransferOS()
								.clear();
							}
							SearchResultUpdate sru = new SearchResultUpdate();
							sru.update(result,
									(TextBox) searchDisplay
									.getAdminSearchString(),
									lastSearchText);
							searchDisplay
							.buildDataTable(manageMeasureSearchModel, filter,searchText);
							panel.setContent(searchDisplay
									.asWidget());
							showAdminSearchingBusy(false);
							
						}
					});
				} else {
					//pageSize = Integer.MAX_VALUE;
					pageSize = 25;
					showSearchingBusy(true);
					MatContext
					.get()
					.getMeasureService()
					.search(searchText, startIndex, pageSize, filter,
							new AsyncCallback<ManageMeasureSearchModel>() {
						@Override
						public void onFailure(Throwable caught) {
							detailDisplay
							.getErrorMessageDisplay()
							.setMessage(
									MatContext
									.get()
									.getMessageDelegate()
									.getGenericErrorMessage());
							MatContext
							.get()
							.recordTransactionEvent(
									null,
									null,
									null,
									"Unhandled Exception: "
											+ caught.getLocalizedMessage(),
											0);
							showSearchingBusy(false);
						}
						
						@Override
						public void onSuccess(
								ManageMeasureSearchModel result) {
							
							if(searchDisplay.getMeasureSearchFilterWidget().
									getSelectedFilter()!=0){
								searchDisplay.getMeasureSearchView().setMeasureListLabel("All Measures");
							}else{
								searchDisplay.getMeasureSearchView().setMeasureListLabel("My Measures");
							}
							if(result.getData().size()>0){
								searchDisplay.getExportSelectedButton().setVisible(true);
							}
							else{
								searchDisplay.getExportSelectedButton().setVisible(false);
							}
							
							searchDisplay.getMeasureSearchView().setObserver(new MeasureSearchView.Observer() {
								@Override
								public void onCloneClicked(ManageMeasureSearchModel.Result result) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									isClone = true;
									editClone(result.getId());
								}
								
								@Override
								public void onEditClicked(ManageMeasureSearchModel.Result result) {
									// When edit has been clicked, no need to fire measureSelected
									// Event.
									// fireMeasureSelectedEvent(result.getId(),
									// result.getName(), result.getShortName(),
									// result.getScoringType(),result.isEditable(),result.isMeasureLocked(),
									// result.getLockedUserId(result.getLockedUserInfo()));
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									edit(result.getId());
								}
								
								@Override
								public void onExportClicked(ManageMeasureSearchModel.Result result) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									export(result.getId(), result.getName());
								}
								
								@Override
								public void onExportSelectedClicked(Result result,boolean isCBChecked) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									searchDisplay.getErrorMessageDisplayForBulkExport().clear();
									updateExportedIDs(result, manageMeasureSearchModel,isCBChecked);
									
								}
								@Override
								public void onExportSelectedClicked(CustomCheckBox checkBox) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									searchDisplay.getErrorMessageDisplayForBulkExport().clear();
									if (checkBox.getValue()) {
										if (manageMeasureSearchModel.getSelectedExportIds().size() > 89) {
											searchDisplay
											.getErrorMessageDisplayForBulkExport()
											.setMessage(
													"Export file has a limit of 90 measures");
											searchDisplay.getExportSelectedButton().setFocus(true);
											checkBox.setValue(false);
										} else {
											manageMeasureSearchModel.getSelectedExportIds().add(
													checkBox.getFormValue());
										}
									} else {
										manageMeasureSearchModel.getSelectedExportIds().remove(
												checkBox.getFormValue());
									}
								}
								
								@Override
								public void onHistoryClicked(ManageMeasureSearchModel.Result result) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									historyDisplay
									.setReturnToLinkText("<< Return to Measure Library");
									//									if (!result.isEditable()) {
									//										historyDisplay.setUserCommentsReadOnly(true);
									//									} else {
									//										historyDisplay.setUserCommentsReadOnly(false);
									//									}
									
									displayHistory(result.getId(), result.getName());
								}
								
								@Override
								public void onShareClicked(ManageMeasureSearchModel.Result result) {
									measureDeletion = false;
									isMeasureDeleted = false;
									searchDisplay.getSuccessMeasureDeletion().clear();
									searchDisplay.getErrorMeasureDeletion().clear();
									displayShare(result.getId(), result.getName());
								}
								
								@Override
								public void onClearAllBulkExportClicked() {
									
									manageMeasureSearchModel.getSelectedExportResults().removeAll(
											manageMeasureSearchModel.getSelectedExportResults());
									manageMeasureSearchModel.getSelectedExportIds().removeAll(
											manageMeasureSearchModel.getSelectedExportIds());
									
								}
								
								
							});
							result.setSelectedExportIds(new ArrayList<String>());
							result.setSelectedExportResults(new ArrayList<Result>());
							manageMeasureSearchModel = result;
							MatContext.get()
							.setManageMeasureSearchModel(
									manageMeasureSearchModel);
							
							if ((result.getResultsTotal() == 0)
									&& !lastSearchText.isEmpty()) {
								searchDisplay
								.getErrorMessageDisplay()
								.setMessage(
										MatContext
										.get()
										.getMessageDelegate()
										.getNoMeasuresMessage());
							} else {
								searchDisplay.getErrorMessageDisplay()
								.clear();
								searchDisplay
								.getErrorMessageDisplayForBulkExport()
								.clear();
								if (measureDeletion) {
									if (isMeasureDeleted) {
										searchDisplay
										.getSuccessMeasureDeletion()
										.setMessage(
												measureDelMessage);
									} else {
										if (measureDelMessage != null) {
											searchDisplay
											.getErrorMeasureDeletion()
											.setMessage(
													measureDelMessage);
										}
									}
									
								} else {
									searchDisplay
									.getSuccessMeasureDeletion()
									.clear();
									searchDisplay
									.getErrorMeasureDeletion()
									.clear();
								}
							}
							SearchResultUpdate sru = new SearchResultUpdate();
							sru.update(result, (TextBox) searchDisplay
									.getSearchString(), lastSearchText);
							searchDisplay.buildCellTable(manageMeasureSearchModel,filter,searchText);
							showSearchingBusy(false);
							
						}
						
					});
				}
	}
	
	
	/**
	 * Search display handlers.
	 * 
	 * @param searchDisplay
	 *            the search display
	 */
	private void searchDisplayHandlers(final SearchDisplay searchDisplay) {
		searchDisplay.getSelectIdForEditTool().addSelectionHandler(
				new SelectionHandler<ManageMeasureSearchModel.Result>() {
					
					@Override
					public void onSelection(
							SelectionEvent<ManageMeasureSearchModel.Result> event) {
						
						searchDisplay.getErrorMeasureDeletion().clear();
						searchDisplay.getSuccessMeasureDeletion().clear();
						measureDeletion = false;
						isMeasureDeleted = false;
						if (!currentUserRole
								.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
							final String mid = event.getSelectedItem().getId();
							Result result = event.getSelectedItem();
							
							final String name = result.getName();
							final String version = result.getVersion();
							final String shortName = result.getShortName();
							final String scoringType = result.getScoringType();
							final boolean isEditable = result.isEditable();
							final boolean isMeasureLocked = result
									.isMeasureLocked();
							final String userId = result.getLockedUserId(result
									.getLockedUserInfo());
							
							MatContext.get().getMeasureLockService()
							.isMeasureLocked(mid);
							Command waitForLockCheck = new Command() {
								@Override
								public void execute() {
									SynchronizationDelegate synchDel = MatContext
											.get().getSynchronizationDelegate();
									if (!synchDel.isCheckingLock()) {
										if (!synchDel.measureIsLocked()) {
											fireMeasureSelectedEvent(mid,
													version, name, shortName,
													scoringType, isEditable,
													isMeasureLocked, userId);
											if (isEditable) {
												MatContext
												.get()
												.getMeasureLockService()
												.setMeasureLock();
											}
										} else {
											fireMeasureSelectedEvent(mid,
													version, name, shortName,
													scoringType, false,
													isMeasureLocked, userId);
											if (isEditable) {
												MatContext
												.get()
												.getMeasureLockService()
												.setMeasureLock();
											}
										}
									} else {
										DeferredCommand.addCommand(this);
									}
								}
							};
							waitForLockCheck.execute();
						}
					}
				});
		
		searchDisplay.getMostRecentMeasureWidget().getSelectIdForEditTool().addSelectionHandler(
				new SelectionHandler<ManageMeasureSearchModel.Result>() {
					@Override
					public void onSelection(
							SelectionEvent<ManageMeasureSearchModel.Result> event) {
						searchDisplay.getErrorMeasureDeletion().clear();
						searchDisplay.getSuccessMeasureDeletion().clear();
						measureDeletion = false;
						isMeasureDeleted = false;
						if (!currentUserRole
								.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
							final String mid = event.getSelectedItem().getId();
							Result result = event.getSelectedItem();
							final String name = result.getName();
							final String version = result.getVersion();
							final String shortName = result.getShortName();
							final String scoringType = result.getScoringType();
							final boolean isEditable = result.isEditable();
							final boolean isMeasureLocked = result
									.isMeasureLocked();
							final String userId = result.getLockedUserId(result
									.getLockedUserInfo());
							MatContext.get().getMeasureLockService()
							.isMeasureLocked(mid);
							Command waitForLockCheck = new Command() {
								@Override
								public void execute() {
									SynchronizationDelegate synchDel = MatContext
											.get().getSynchronizationDelegate();
									if (!synchDel.isCheckingLock()) {
										if (!synchDel.measureIsLocked()) {
											fireMeasureSelectedEvent(mid,
													version, name, shortName,
													scoringType, isEditable,
													isMeasureLocked, userId);
											if (isEditable) {
												MatContext
												.get()
												.getMeasureLockService()
												.setMeasureLock();
											}
										} else {
											fireMeasureSelectedEvent(mid,
													version, name, shortName,
													scoringType, false,
													isMeasureLocked, userId);
											if (isEditable) {
												MatContext
												.get()
												.getMeasureLockService()
												.setMeasureLock();
											}
										}
									} else {
										DeferredCommand.addCommand(this);
									}
								}
							};
							waitForLockCheck.execute();
						}
					}
				});
		searchDisplay.getCreateButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getSuccessMeasureDeletion().clear();
				measureDeletion = false;
				isMeasureDeleted = false;
				
				if (searchDisplay.getSelectedOption().equalsIgnoreCase(
						ConstantMessages.CREATE_NEW_MEASURE)) {
					createNew();
				} else if (searchDisplay.getSelectedOption().equalsIgnoreCase(
						ConstantMessages.CREATE_NEW_DRAFT)) {
					createDraft();
				} else if (searchDisplay.getSelectedOption().equalsIgnoreCase(
						ConstantMessages.CREATE_NEW_VERSION)) {
					createVersion();
				} else {
					searchDisplay.getErrorMessageDisplayForBulkExport().clear();
					searchDisplay
					.getErrorMessageDisplay()
					.setMessage(
							"Please select an option from the Create list box.");
				}
			}
		});
		
		searchDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int startIndex = 1;
				measureDeletion = false;
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				int filter = searchDisplay.getSelectedFilter();
				search(searchDisplay.getSearchString().getValue(), startIndex,
						Integer.MAX_VALUE, filter);
			}
		});
		searchDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				if (isCreateMeasureWidgetVisible) {
					isCreateMeasureWidgetVisible = !isCreateMeasureWidgetVisible;
					searchDisplay.getCreateMeasureWidget().setVisible(isCreateMeasureWidgetVisible);
				}
				isMeasureSearchFilterVisible = !isMeasureSearchFilterVisible;
				searchDisplay.getMeasureSearchFilterWidget().setVisible(isMeasureSearchFilterVisible);
				
			}
		});
		
		searchDisplay.getCreateMeasureButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				if (isMeasureSearchFilterVisible) {
					isMeasureSearchFilterVisible = !isMeasureSearchFilterVisible;
					searchDisplay.getMeasureSearchFilterWidget().setVisible(isMeasureSearchFilterVisible);
				}
				isCreateMeasureWidgetVisible = !isCreateMeasureWidgetVisible;
				searchDisplay.getCreateMeasureWidget().setVisible(isCreateMeasureWidgetVisible);
			}
		});
		searchDisplay.getBulkExportButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				isMeasureDeleted = false;
				measureDeletion = false;
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				versionDisplay.getErrorMessageDisplay().clear();
				draftDisplay.getErrorMessageDisplay().clear();
				detailDisplay.getErrorMessageDisplay().clear();
				historyDisplay.getErrorMessageDisplay().clear();
				exportDisplay.getErrorMessageDisplay().clear();
				shareDisplay.getErrorMessageDisplay().clear();
				if (manageMeasureSearchModel.getSelectedExportIds().isEmpty()) {
					searchDisplay.getErrorMessageDisplayForBulkExport()
					.setMessage(
							MatContext.get().getMessageDelegate()
							.getMeasureSelectionError());
				} else {
					//					searchDisplay.clearBulkExportCheckBoxes(searchDisplay
					//							.getMeasureDataTable());
					//search(searchText, shareStartIndex, pageSize, filter)
					int filter = searchDisplay.getSelectedFilter();
					//					search(searchDisplay.getSearchString().getValue(), startIndex,
					//							searchDisplay.getPageSize(), filter);
					bulkExport(manageMeasureSearchModel.getSelectedExportIds());
					searchDisplay.getMeasureSearchView().clearBulkExportCheckBoxes();
					
				}
			}
		});
		FormPanel form = searchDisplay.getForm();
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String errorMsg = event.getResults();
				if ((null != errorMsg) && errorMsg.contains("Exceeded Limit")) {
					List<String> err = new ArrayList<String>();
					err.add("Export file size is " + errorMsg);
					err.add("File size limit is 100 MB");
					searchDisplay.getErrorMessageDisplayForBulkExport()
					.setMessages(err);
				} else {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
			}
		});
		
		TextBox searchWidget = (TextBox) (searchDisplay.getSearchString());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) searchDisplay.getSearchButton()).click();
				}
			}
		});
		//added by hari
		searchDisplay.getAdminSearchButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						int startIndex = 1;
						searchDisplay.getErrorMessageDisplay().clear();
						int filter = 1;
						search(searchDisplay.getAdminSearchString().getValue(),
								startIndex, Integer.MAX_VALUE, filter);
					}
				});
		
		searchDisplay.getTransferButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						searchDisplay.clearTransferCheckBoxes();
						transferDisplay.getSearchString().setValue("");
						displayTransferView("",startIndex,
								Integer.MAX_VALUE);
					}
				});
		
		searchDisplay.getClearButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				manageMeasureSearchModel.getSelectedTransferResults()
				.removeAll(
						manageMeasureSearchModel
						.getSelectedTransferResults());
				manageMeasureSearchModel.getSelectedTransferIds().removeAll(
						manageMeasureSearchModel.getSelectedTransferIds());
				int filter = 1;
				search(searchDisplay.getAdminSearchString().getValue(), 1,
						Integer.MAX_VALUE, filter);
			}
		});
		
	}
	
	/**
	 * Search history.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param startIndex
	 *            the start index
	 * @param pageSize
	 *            the page size
	 */
	private void searchHistory(String measureId, int startIndex, int pageSize) {
		List<String> filterList = new ArrayList<String>();
		
		/**
		 * Commented on : 09/05/2013 The below line is commented because Export
		 * log entry should also be displayed in the History. "filterList" is
		 * added in the restrictions to exclude while searching history. If
		 * "Export" is added in the list then log entries with ACTIVITY_TYPE =
		 * "Export" will not be displayed in History. So, to display the
		 * "Export" log entry, below line is commented.
		 * 
		 * NOTE: "Export" is replaced with "Measure Exported". From now, Export
		 * log entries will be recorded as "Measure Exported" under
		 * ACTIVITY_TYPE. Refer MAT-1629 user story for details.
		 */
		// filterList.add("Export");
		
		MatContext
		.get()
		.getAuditService()
		.executeMeasureLogSearch(measureId, startIndex, pageSize,
				filterList, new AsyncCallback<SearchHistoryDTO>() {
			@Override
			public void onFailure(Throwable caught) {
				historyDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(
						null,
						null,
						null,
						"Unhandled Exception: "
								+ caught.getLocalizedMessage(),
								0);
			}
			
			@Override
			public void onSuccess(SearchHistoryDTO data) {
				historyModel = new HistoryModel(data.getLogs());
				historyDisplay.buildCellTable(data.getLogs());
			}
		});
	}
	
	/** Search measures for draft.
	 * 
	 * @param searchText the search text */
	private void searchMeasuresForDraft(String searchText) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		MatContext.get().getMeasureService().searchMeasuresForDraft(lastSearchText,
				new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
				draftDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				draftMeasureResults = new ManageDraftMeasureModel();
				draftMeasureResults.setData(result);
				draftDisplay.buildDataTable(draftMeasureResults);
			}
		});
		
	}
	
	/**
	 * Search measures for version.
	 * 
	 * @param searchText
	 *            the search Text
	 */
	private void searchMeasuresForVersion(String searchText) {
		final String lastSearchText = (searchText != null) ? searchText.trim() : null;
		MatContext.get().getMeasureService()
		.searchMeasuresForVersion(lastSearchText, new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
				versionDisplay.getErrorMessageDisplay().setMessage(
						MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " + caught.getLocalizedMessage(), 0);
			}
			
			@Override
			public void onSuccess(
					ManageMeasureSearchModel result) {
				versionMeasureResults = new ManageVersionMeasureModel(result.getData());
				versionDisplay.buildDataTable(versionMeasureResults);
			}
		});
		
	}
	
	/** Method to Load most recent Used Measures for Logged In User. */
	private void searchRecentMeasures() {
		MatContext.get().getMeasureService().getAllRecentMeasureForUser(MatContext.get().getLoggedinUserId(),
				new AsyncCallback<ManageMeasureSearchModel>() {
			@Override
			public void onFailure(Throwable caught) {
			}
			@Override
			public void onSuccess(ManageMeasureSearchModel result) {
				searchDisplay.getMostRecentMeasureWidget().setMeasureSearchModel(result);
				searchDisplay.getMostRecentMeasureWidget().setObserver(new MostRecentMeasureWidget.Observer() {
					@Override
					public void onExportClicked(Result result) {
						measureDeletion = false;
						isMeasureDeleted = false;
						searchDisplay.getSuccessMeasureDeletion().clear();
						searchDisplay.getErrorMeasureDeletion().clear();
						export(result.getId(), result.getName());
					}
					
					@Override
					public void onEditClicked(Result result) {
						// When edit has been clicked, no need to fire measureSelected
						// Event.
						// fireMeasureSelectedEvent(result.getId(),
						// result.getName(), result.getShortName(),
						// result.getScoringType(),result.isEditable(),result.isMeasureLocked(),
						// result.getLockedUserId(result.getLockedUserInfo()));
						measureDeletion = false;
						isMeasureDeleted = false;
						searchDisplay.getSuccessMeasureDeletion().clear();
						searchDisplay.getErrorMeasureDeletion().clear();
						edit(result.getId());
						
					}
					
					
					
					
				});
				searchDisplay.buildMostRecentWidget();
			}
		});
	}
	/**
	 * Sets the bulk export measure ids.
	 * @param bulkExportMeasureIds
	 *            the bulkExportMeasureIds to set
	 */
	public void setBulkExportMeasureIds(List<String> bulkExportMeasureIds) {
		this.bulkExportMeasureIds = bulkExportMeasureIds;
	}
	
	/**
	 * Sets the details to view.
	 */
	private void setDetailsToView() {
		detailDisplay.getName().setValue(currentDetails.getName());
		detailDisplay.getShortName().setValue(currentDetails.getShortName());
		
		// US 421. Measure scoring choice is now part of measure creation
		// process.
		detailDisplay.getMeasScoringChoice().setValueMetadata(
				currentDetails.getMeasScoring());
	}
	
	/**
	 * Share display handlers.
	 * 
	 * @param shareDisplay
	 *            the share display
	 */
	private void shareDisplayHandlers(final ShareDisplay shareDisplay) {
		
		shareDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isClone = false;
				displaySearch();
			}
		});
		shareDisplay.getShareButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext
				.get()
				.getMeasureService()
				.updateUsersShare(currentShareDetails,
						new AsyncCallback<Void>() {
					
					@Override
					public void onFailure(Throwable caught) {
						shareDisplay
						.getErrorMessageDisplay()
						.setMessage(
								MatContext
								.get()
								.getMessageDelegate()
								.getGenericErrorMessage());
						MatContext
						.get()
						.recordTransactionEvent(
								null,
								null,
								null,
								"Unhandled Exception: "
										+ caught.getLocalizedMessage(),
										0);
					}
					
					@Override
					public void onSuccess(Void result) {
						displaySearch();
					}
				});
			}
		});
		
		shareDisplay.privateCheckbox().addValueChangeHandler(
				new ValueChangeHandler<Boolean>() {
					
					@Override
					public void onValueChange(ValueChangeEvent<Boolean> event) {
						MatContext
						.get()
						.getMeasureService()
						.updatePrivateColumnInMeasure(
								currentShareDetails.getMeasureId(),
								event.getValue(),
								new AsyncCallback<Void>() {
									
									@Override
									public void onFailure(
											Throwable caught) {
										
									}
									
									@Override
									public void onSuccess(Void result) {
										
									}
								});
					}
				});
		
	}
	
	/**
	 * Show admin searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showAdminSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getAdminSearchButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getAdminSearchString())).setEnabled(!busy);
		((Button) transferDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (transferDisplay.getSearchString())).setEnabled(!busy);
		
	}
	
	/**
	 * Show searching busy.
	 * 
	 * @param busy
	 *            the busy
	 */
	private void showSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) searchDisplay.getSearchButton()).setEnabled(!busy);
		((Button) searchDisplay.getBulkExportButton()).setEnabled(!busy);
		((TextBox) (searchDisplay.getSearchString())).setEnabled(!busy);
		
	}
	
	/**
	 * Transfer display handlers.
	 * 
	 * @param transferDisplay
	 *            the transfer display
	 */
	private void transferDisplayHandlers(final TransferDisplay transferDisplay) {
		
		transferDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				transferDisplay.getErrorMessageDisplay().clear();
				transferDisplay.getSuccessMessageDisplay().clear();
				boolean userSelected = false;
				for (int i = 0; i < model.getData().size(); i = i + 1) {
					if (model.getData().get(i).isSelected()) {
						userSelected = true;
						final String emailTo = model.getData().get(i)
								.getEmailId();
						final int rowIndex = i;
						
						MatContext
						.get()
						.getMeasureService()
						.transferOwnerShipToUser(
								manageMeasureSearchModel
								.getSelectedTransferIds(),
								emailTo, new AsyncCallback<Void>() {
									@Override
									public void onFailure(
											Throwable caught) {
										Window.alert(MatContext
												.get()
												.getMessageDelegate()
												.getGenericErrorMessage());
										model.getData().get(rowIndex)
										.setSelected(false);
										transferDisplay.clearRadioButtons();
									}
									
									@Override
									public void onSuccess(Void result) {
										transferDisplay
										.getSuccessMessageDisplay()
										.setMessage(
												MatContext
												.get()
												.getMessageDelegate()
												.getTransferOwnershipSuccess()
												+ emailTo);
										model.getData().get(rowIndex)
										.setSelected(false);
										transferDisplay.clearRadioButtons();
									}
								});
					}
				}
				if (userSelected == false) {
					transferDisplay.getSuccessMessageDisplay().clear();
					transferDisplay.getErrorMessageDisplay().setMessage(
							MatContext.get().getMessageDelegate()
							.getUserRequiredErrorMessage());
				}
				
			}
			
		});
		transferDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				manageMeasureSearchModel.getSelectedTransferResults()
				.removeAll(
						manageMeasureSearchModel
						.getSelectedTransferResults());
				manageMeasureSearchModel.getSelectedTransferIds().removeAll(
						manageMeasureSearchModel.getSelectedTransferIds());
				transferDisplay.getSuccessMessageDisplay().clear();
				transferDisplay.getErrorMessageDisplay().clear();
				int filter = 1;
				search("", 1, Integer.MAX_VALUE, filter);
			}
		});
		
		transferDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				transferDisplay.getSuccessMessageDisplay().clear();
				displayTransferView(transferDisplay.getSearchString().getValue(),startIndex,
						Integer.MAX_VALUE);
				
			}
		});
	}
	
	/**
	 * Update.
	 */
	private void update() {
		// exit if there is something already being saved
		if (!MatContext.get().getLoadingQueue().isEmpty()) {
			return;
		}
		
		Mat.showLoadingMessage();
		updateDetailsFromView();
		
		if (isClone && isValid(currentDetails)) {
			cloneMeasure(currentDetails, false);
		} else if (isValid(currentDetails)) {
			final boolean isInsert = currentDetails.getId() == null;
			final String name = currentDetails.getName();
			final String shortName = currentDetails.getShortName();
			final String scoringType = currentDetails.getMeasScoring();
			final String version = currentDetails.getVersionNumber();
			MatContext
			.get()
			.getMeasureService()
			.save(currentDetails,
					new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onFailure(Throwable caught) {
					detailDisplay
					.getErrorMessageDisplay()
					.setMessage(
							caught.getLocalizedMessage());
					Mat.hideLoadingMessage();
				}
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					if (result.isSuccess()) {
						if (isInsert) {
							fireMeasureSelectedEvent(
									result.getId(), version,
									name, shortName,
									scoringType, true, false,
									null);// Need to revisit
							// this, since don't
							// know how this will
							// affect
							fireMeasureEditEvent();
						} else {
							displaySearch();
						}
					} else {
						String message = null;
						switch (result.getFailureReason()) {
							case SaveMeasureResult.INVALID_DATA:
								message = "Data Validation Failed.Please verify data.";
								break;
							default:
								message = "Unknown Code "
										+ result.getFailureReason();
						}
						detailDisplay.getErrorMessageDisplay()
						.setMessage(message);
					}
					Mat.hideLoadingMessage();
				}
			});
		}
	}
	
	/**
	 * Update details from view.
	 */
	private void updateDetailsFromView() {
		currentDetails.setName(detailDisplay.getName().getValue().trim());
		currentDetails.setShortName(detailDisplay.getShortName().getValue()
				.trim());
		String measureScoring = detailDisplay.getMeasScoringValue();
		
		// US 421. Update the Measure scoring choice from the UI.
		//if (isValidValue(measureScoring)) {
		currentDetails.setMeasScoring(measureScoring);
		//}
		currentDetails.scrubForMarkUp();
		detailDisplay.getName().setValue(currentDetails.getName());
		detailDisplay.getShortName().setValue(currentDetails.getShortName());
		MatContext.get().setCurrentMeasureName(
				currentDetails.getName());
		MatContext.get().setCurrentShortName(
				currentDetails.getShortName());
		MatContext.get().setCurrentMeasureScoringType(
				currentDetails.getMeasScoring());
	}
	
	/**
	 * Update transfer i ds.
	 * 
	 * @param result
	 *            the result
	 * @param model
	 *            the model
	 */
	private void updateTransferIDs(Result result, ManageMeasureSearchModel model) {
		if (result.isTransferable()) {
			List<String> selectedIdList = model.getSelectedTransferIds();
			if (!selectedIdList.contains(result.getId())) {
				model.getSelectedTransferResults().add(result);
				selectedIdList.add(result.getId());
			}
		} else {
			for (int i = 0; i < model.getSelectedTransferIds().size(); i++) {
				if (result.getId().equals(model.getSelectedTransferResults().get(i)
						.getId())) {
					model.getSelectedTransferIds().remove(i);
					model.getSelectedTransferResults().remove(i);
				}
			}
			
		}
	}
	
	/**
	 * Update exported i ds.
	 *
	 * @param result the result
	 * @param model the model
	 * @param isCBChecked the is cb checked
	 */
	private void updateExportedIDs(Result result, ManageMeasureSearchModel model,
			boolean isCBChecked) {
		List<String> selectedIdList = model.getSelectedExportIds();;
		if (isCBChecked) {
			if (!selectedIdList.contains(result.getId())) {
				model.getSelectedExportResults().add(result);
				selectedIdList.add(result.getId());
			}
		} else {
			//			if(selectedIdList.size()>9){
			//				searchDisplay
			//				.getErrorMessageDisplayForBulkExport()
			//				.setMessage(
			//						"Export file has a limit of 90 measures");
			//				searchDisplay.getExportSelectedButton().setFocus(true);
			//			}
			for (int i = 0; i < model.getSelectedExportIds().size(); i++) {
				if (result.getId().equals(model.getSelectedExportResults().get(i)
						.getId())) {
					model.getSelectedExportIds().remove(i);
					model.getSelectedExportResults().remove(i);
				}
			}
			
		}
	}
	
	
	/**
	 * Version display handlers.
	 * 
	 * @param versionDisplay
	 *            the version display
	 */
	private void versionDisplayHandlers(final VersionDisplay versionDisplay) {
		TextBox searchWidget = (versionDisplay.getSearchWidget().getSearchInput());
		searchWidget.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					((Button) versionDisplay.getSearchButton()).click();
				}
			}
		});
		
		versionDisplay.getZoomButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchDisplay.getSuccessMeasureDeletion().clear();
				searchDisplay.getErrorMeasureDeletion().clear();
				searchDisplay.getErrorMessageDisplayForBulkExport().clear();
				searchDisplay.getErrorMessageDisplay().clear();
				versionDisplay.getErrorMessageDisplay().clear();
				isSearchVisibleOnVersion = !isSearchVisibleOnVersion;
				versionDisplay.getSearchWidget().setVisible(isSearchVisibleOnVersion);
			}
		});
		
		versionDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				searchMeasuresForVersion(versionDisplay.getSearchWidget().getSearchInput().getText());
			}
		});
		
		versionDisplay.getSaveButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				ManageMeasureSearchModel.Result selectedMeasure = versionDisplay.getSelectedMeasure();
				if (((selectedMeasure !=null) && (selectedMeasure.getId() != null))
						&& (versionDisplay.getMajorRadioButton().getValue() || versionDisplay
								.getMinorRadioButton().getValue())) {
					saveFinalizedVersion(selectedMeasure.getId(),
							versionDisplay.getMajorRadioButton().getValue(),
							selectedMeasure.getVersion());
				} else {
					versionDisplay
					.getErrorMessageDisplay()
					.setMessage(
							"Please select a Measure Name to version and select a version type of Major or Minor.");
				}
			}
		});
		
		versionDisplay.getCancelButton().addClickHandler(cancelClickHandler);
		
	}
	
}
