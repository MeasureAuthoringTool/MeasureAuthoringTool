package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.List;
import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.MeasureComposerPresenter;
import mat.client.clause.QDSAppliedListModel;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.DateBoxWithCalendar;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.HasVisible;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MeasureSearchFilterWidget;
import mat.client.shared.MessageDelegate;
import mat.client.shared.PrimaryButton;
import mat.client.shared.ReadOnlyHelper;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.Author;
import mat.model.MeasureType;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.UUIDUtilClient;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

// TODO: Auto-generated Javadoc
/**
 * The Class MetaDataPresenter.
 */
public class MetaDataPresenter  implements MatPresenter {
	
	
	/**
	 * The Interface MetaDataDetailDisplay.
	 */
	public static interface MetaDataDetailDisplay {
		
		/**
		 * Gets the measure name.
		 * 
		 * @return the measure name
		 */
		public Label getMeasureName();
		
		/**
		 * Gets the short name.
		 * 
		 * @return the short name
		 */
		public Label getShortName();
		
		//US 421. Measure scoring choice is now part of measure creation process. So, this module just displays the choice.
		/**
		 * Gets the measure scoring.
		 * 
		 * @return the measure scoring
		 */
		public Label getMeasureScoring();
		
		/**
		 * Gets the edits the authors button.
		 * 
		 * @return the edits the authors button
		 */
		public HasClickHandlers getEditAuthorsButton();
		
		/**
		 * Gets the edits the measure type button.
		 * 
		 * @return the edits the measure type button
		 */
		public HasClickHandlers getEditMeasureTypeButton();
		
		/**
		 * Gets the focus panel.
		 * 
		 * @return the focus panel
		 */
		public HasKeyDownHandlers getFocusPanel();
		
		/**
		 * Gets the version number.
		 * 
		 * @return the version number
		 */
		public Label getVersionNumber();
		//public HasValue<String> getMeasureId();
		/**
		 * Gets the sets the name.
		 * 
		 * @return the sets the name
		 */
		public HasValue<String> getSetName();
		
		/**
		 * Gets the e measure identifier.
		 * 
		 * @return the e measure identifier
		 */
		public Label geteMeasureIdentifier();
		
		/**
		 * Gets the nqf id.
		 * 
		 * @return the nqf id
		 */
		public HasValue<String> getNqfId();
		
		/**
		 * Gets the finalized date.
		 * 
		 * @return the finalized date
		 */
		public Label getFinalizedDate();
		
		/**
		 * Gets the measurement from period.
		 * 
		 * @return the measurement from period
		 */
		public String getMeasurementFromPeriod();
		
		/**
		 * Gets the measurement from period input box.
		 * 
		 * @return the measurement from period input box
		 */
		public DateBoxWithCalendar getMeasurementFromPeriodInputBox();
		
		/**
		 * Gets the measurement to period.
		 * 
		 * @return the measurement to period
		 */
		public String getMeasurementToPeriod();
		
		/**
		 * Gets the measurement to period input box.
		 * 
		 * @return the measurement to period input box
		 */
		public DateBoxWithCalendar getMeasurementToPeriodInputBox();
		
		/**
		 * Gets the measure type.
		 * 
		 * @return the measure type
		 */
		public String getMeasureType();
		
		/**
		 * Gets the measure steward.
		 * 
		 * @return the measure steward
		 */
		public ListBoxMVP getMeasureSteward();
		
		//US 413. Introduced Measure Steward Other option.
		/**
		 * Gets the measure steward list box.
		 * 
		 * @return the measure steward list box
		 */
		public HasValue<String> getMeasureStewardListBox();
		
		/**
		 * Gets the measure steward value.
		 * 
		 * @return the measure steward value
		 */
		public String getMeasureStewardValue();
		
		/**
		 * Gets the measure steward other.
		 * 
		 * @return the measure steward other
		 */
		public TextBox getMeasureStewardOther();
		
		/**
		 * Gets the measure steward other value.
		 * 
		 * @return the measure steward other value
		 */
		public String getMeasureStewardOtherValue();
		
		/**
		 * Gets the endorseby nqf.
		 * 
		 * @return the endorseby nqf
		 */
		public HasValue<Boolean> getEndorsebyNQF();
		
		/**
		 * Gets the not endorseby nqf.
		 * 
		 * @return the not endorseby nqf
		 */
		public HasValue<Boolean> getNotEndorsebyNQF();
		
		/**
		 * Gets the measure status.
		 * 
		 * @return the measure status
		 */
		/*	public ListBoxMVP getMeasureStatus();*/
		
		/**
		 * Gets the measure status value.
		 * 
		 * @return the measure status value
		 */
		/*	public String getMeasureStatusValue();*/
		
		/**
		 * Gets the author.
		 * 
		 * @return the author
		 */
		public String getAuthor();
		
		/**
		 * Sets the authors list.
		 * 
		 * @param author
		 *            the new authors list
		 */
		public void setAuthorsSelectedList(List<Author> author);
		
		/**
		 * Gets the authors selected list.
		 *
		 * @return the authors selected list
		 */
		public List<Author> getAuthorsSelectedList();
		
		/**
		 * Sets the measure type list.
		 * 
		 * @param measureType
		 *            the new measure type list
		 */
		public void setMeasureTypeList(List<MeasureType> measureType);
		
		/**
		 * Gets the description.
		 * 
		 * @return the description
		 */
		public HasValue<String> getDescription();
		
		/**
		 * Gets the copyright.
		 * 
		 * @return the copyright
		 */
		public HasValue<String> getCopyright();
		
		/**
		 * Gets the clinical recommendation.
		 * 
		 * @return the clinical recommendation
		 */
		public HasValue<String> getClinicalRecommendation();
		
		/**
		 * Gets the definitions.
		 * 
		 * @return the definitions
		 */
		public HasValue<String> getDefinitions();
		
		/**
		 * Gets the guidance.
		 * 
		 * @return the guidance
		 */
		public HasValue<String> getGuidance();
		
		/**
		 * Gets the transmission format.
		 * 
		 * @return the transmission format
		 */
		public HasValue<String> getTransmissionFormat();
		
		/**
		 * Gets the rationale.
		 * 
		 * @return the rationale
		 */
		public HasValue<String> getRationale();
		
		/**
		 * Gets the improvement notation.
		 * 
		 * @return the improvement notation
		 */
		public HasValue<String> getImprovementNotation();
		
		/**
		 * Gets the stratification.
		 * 
		 * @return the stratification
		 */
		public HasValue<String> getStratification();
		
		/**
		 * Gets the risk adjustment.
		 * 
		 * @return the risk adjustment
		 */
		public HasValue<String> getRiskAdjustment();
		
		/**
		 * Gets the reference.
		 * 
		 * @return the reference
		 */
		public HasValue<String> getReference();
		
		/**
		 * Gets the supplemental data.
		 * 
		 * @return the supplemental data
		 */
		public HasValue<String> getSupplementalData();
		
		/**
		 * Gets the disclaimer.
		 * 
		 * @return the disclaimer
		 */
		public HasValue<String> getDisclaimer();
		
		/**
		 * Gets the initial patient pop.
		 * 
		 * @return the initial patient pop
		 */
		public HasValue<String> getInitialPop();
		
		/**
		 * Gets the denominator.
		 * 
		 * @return the denominator
		 */
		public HasValue<String> getDenominator();
		
		/**
		 * Gets the denominator exclusions.
		 * 
		 * @return the denominator exclusions
		 */
		public HasValue<String> getDenominatorExclusions();
		
		/**
		 * Gets the numerator.
		 * 
		 * @return the numerator
		 */
		public HasValue<String> getNumerator();
		
		/**
		 * Gets the numerator exclusions.
		 * 
		 * @return the numerator exclusions
		 */
		public HasValue<String> getNumeratorExclusions();
		
		/**
		 * Gets the denominator exceptions.
		 * 
		 * @return the denominator exceptions
		 */
		public HasValue<String> getDenominatorExceptions();
		
		/**
		 * Gets the measure population.
		 * 
		 * @return the measure population
		 */
		public HasValue<String> getMeasurePopulation();
		
		/**
		 * Gets the measure observations.
		 * 
		 * @return the measure observations
		 */
		public HasValue<String> getMeasureObservations();
		
		/**
		 * Gets the rate aggregation.
		 * 
		 * @return the rate aggregation
		 */
		public HasValue<String> getRateAggregation();
		
		/**
		 * Gets the emeasure id.
		 * 
		 * @return the emeasure id
		 */
		public HasValue<String> getEmeasureId();
		
		/**
		 * Gets the generate emeasure id button.
		 * 
		 * @return the generate emeasure id button
		 */
		public HasClickHandlers getGenerateEmeasureIdButton();
		
		/**
		 * Sets the generate emeasure id button enabled.
		 * 
		 * @param b
		 *            the new generate emeasure id button enabled
		 */
		public void setGenerateEmeasureIdButtonEnabled(boolean b);
		
		/**
		 * Gets the reference values.
		 * 
		 * @return the reference values
		 */
		public List<String> getReferenceValues();
		
		/**
		 * Sets the reference values.
		 * 
		 * @param values
		 *            the values
		 * @param editable
		 *            the editable
		 */
		public void setReferenceValues(List<String> values, boolean editable);
		
		/**
		 * Sets the adds the edit buttons visible.
		 * 
		 * @param b
		 *            the new adds the edit buttons visible
		 */
		public void setAddEditButtonsVisible(boolean b);
		
		/**
		 * Enable endorse by radio buttons.
		 * 
		 * @param b
		 *            the b
		 */
		public void enableEndorseByRadioButtons(boolean b);
		
		/**
		 * Sets the save button enabled.
		 * 
		 * @param b
		 *            the new save button enabled
		 */
		public void setSaveButtonEnabled(boolean b);
		
		/**
		 * Gets the save button.
		 * 
		 * @return the save button
		 */
		public HasClickHandlers getSaveButton();
		
		//US 413. Interfaces to show or clear out Steward Other text boxes.
		/**
		 * Show other text box.
		 */
		public void showOtherTextBox();
		
		/**
		 * Hide other text box.
		 */
		public void hideOtherTextBox();
		
		/**
		 * Gets the save btn.
		 * 
		 * @return the save btn
		 */
		public Button getSaveBtn();
		
		/**
		 * Gets the delete measure.
		 * 
		 * @return the delete measure
		 */
		public Button getDeleteMeasure();
		
		/**
		 * Gets the measure population exclusions.
		 *
		 * @return the measure population exclusions
		 */
		HasValue<String> getMeasurePopulationExclusions();
		
		/**
		 * Builds the cell table.
		 *
		 * @param appliedListModel the applied list model
		 * @param isEditable the is editable
		 */
		public void buildCellTable(QDSAppliedListModel appliedListModel,
				boolean isEditable);
		
		/**
		 * Builds the measure type cell table.
		 *
		 * @param measureTypeDTOList the measure type dto list
		 * @param isEditable the is editable
		 */
		public void buildMeasureTypeCellTable(List<MeasureType> measureTypeDTOList,
				boolean isEditable);
		
		/**
		 * Gets the qdm selected list.
		 *
		 * @return the qdm selected list
		 */
		public List<QualityDataSetDTO> getQdmSelectedList();
		
		/**
		 * Sets the qdm selected list.
		 *
		 * @param qdmSelectedList the new qdm selected list
		 */
		public void setQdmSelectedList(List<QualityDataSetDTO> qdmSelectedList);
		
		/**
		 * Gets the component measure selected list.
		 *
		 * @return the component measure selected list
		 */
		List<ManageMeasureSearchModel.Result> getComponentMeasureSelectedList();
		
		/**
		 * Sets the component measure selected list.
		 *
		 * @param componentMeasureSelectedList the new component measure selected list
		 */
		void setComponentMeasureSelectedList(
				List<ManageMeasureSearchModel.Result> componentMeasureSelectedList);
		
		/**
		 * Gets the search string.
		 *
		 * @return the search string
		 */
		HasValue<String> getSearchString();
		
		/**
		 * Gets the search button.
		 *
		 * @return the search button
		 */
		PrimaryButton getSearchButton();
		
		/**
		 * Gets the adds the edit cmponent measures.
		 *
		 * @return the adds the edit cmponent measures
		 */
		HasClickHandlers getAddEditComponentMeasures();
		
		/**
		 * Gets the dialog box.
		 *
		 * @return the dialog box
		 */
		DialogBox getDialogBox();
		
		/**
		 * As component measures widget.
		 *
		 * @return the widget
		 */
		Widget asComponentMeasuresWidget();
		
		/**
		 * Builds the component measures selected list.
		 *
		 * @param result the result
		 * @param editable the editable
		 */
		void buildComponentMeasuresSelectedList(List<ManageMeasureSearchModel.Result> result, boolean editable);
		
		/**
		 * Gets the measure type selected list.
		 *
		 * @return the measure type selected list
		 */
		List<MeasureType> getMeasureTypeSelectedList();
		
		/**
		 * Sets the measure type selected list.
		 *
		 * @param measureTypeSelectedList the new measure type selected list
		 */
		void setMeasureTypeSelectedList(List<MeasureType> measureTypeSelectedList);
		
		/**
		 * Builds the author cell table.
		 *
		 * @param authorList the author list
		 * @param editable the editable
		 */
		public void buildAuthorCellTable(List<Author> authorList,
				boolean editable);
		
		ErrorMessageDisplay getSaveErrorMsg();
		
		ErrorMessageDisplayInterface getErrorMessageDisplay();
		
		Widget asWidget();
		
		SuccessMessageDisplayInterface getSuccessMessageDisplay();
	
		void setMeasureStewardOptions(List<? extends HasListBox> itemList);
	}
	
	/**
	 * The Interface AddEditAuthorsDisplay.
	 */
	public static interface AddEditAuthorsDisplay {
		
		/**
		 * Gets the author.
		 * 
		 * @return the author
		 */
		public String getAuthor();
		
		/**
		 * Gets the author input box.
		 * 
		 * @return the author input box
		 */
		public HasValue<String> getAuthorInputBox();
		
		/**
		 * Gets the other author.
		 * 
		 * @return the other author
		 */
		public HasValue<String> getOtherAuthor();
		
		/**
		 * Builds the author cell table.
		 *
		 * @param authorList the author list
		 * @param editable the editable
		 */
		public void buildAuthorCellTable(List<Author> authorList,
				boolean editable);
		
		/**
		 * Gets the adds the to measure developer list btn.
		 *
		 * @return the adds the to measure developer list btn
		 */
		public Button getAddToMeasureDeveloperListBtn();
		
		/**
		 * Gets the adds the button.
		 *
		 * @return the adds the button
		 */
		public Button getAddButton();
		
		/**
		 * Gets the measure dev input.
		 *
		 * @return the measure dev input
		 */
		TextBox getMeasureDevInput();
		
		/**
		 * Sets the measure dev input.
		 *
		 * @param measureDevInput the new measure dev input
		 */
		void setMeasureDevInput(TextBox measureDevInput);
		
		/**
		 * Gets the author selected list.
		 *
		 * @return the author selected list
		 */
		List<Author> getAuthorSelectedList();
		
		/**
		 * Gets the list of all author.
		 *
		 * @return the list of all author
		 */
		List<Author> getListOfAllAuthor();
		
		/**
		 * Sets the author selected list.
		 *
		 * @param authorSelectedList the new author selected list
		 */
		void setAuthorSelectedList(List<Author> authorSelectedList);
		
		/**
		 * Builds the author cell table.
		 *
		 * @param authorSelectedList the author selected list
		 * @param editable the editable
		 * @param authorSelectedList2 the author selected list2
		 */
		public void buildAuthorCellTable(List<Author> authorSelectedList,
				boolean editable, List<Author> authorSelectedList2);
		
		/**
		 * Gets the adds the edit cancel button.
		 *
		 * @return the adds the edit cancel button
		 */
		Button getAddEditCancelButton();
		
		Widget asWidget();
		SuccessMessageDisplay getSuccessMessageDisplay();
		HasClickHandlers getReturnButton();
	
	}
	
	//TODO by Ravi
	/**
	 * The Interface AddEditComponentMeasuresDisplay.
	 */
	public static interface AddEditComponentMeasuresDisplay {
		
		/**
		 * Gets the measure search filter widget.
		 *
		 * @return the measure search filter widget
		 */
		MeasureSearchFilterWidget getMeasureSearchFilterWidget();
		
		/**
		 * Gets the selected filter.
		 *
		 * @return the selected filter
		 */
		int getSelectedFilter();
		
		/**
		 * Gets the ret button.
		 *
		 * @return the ret button
		 */
		public Button getRetButton();
		
		/**
		 * Gets the addto component measures button handler.
		 *
		 * @return the addto component measures button handler
		 */
		HasClickHandlers getAddtoComponentMeasuresButtonHandler();
		
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
		
		/* (non-Javadoc)
		 * @see mat.client.measure.metadata.BaseMetaDataPresenter.BaseAddEditDisplay#getSuccessMessageDisplay()
		 */
		public SuccessMessageDisplayInterface getSuccessMessageDisplay();
		
		/**
		 * Gets the addto component measures btn.
		 *
		 * @return the addto component measures btn
		 */
		public Button getAddtoComponentMeasuresBtn();
		
		/**
		 * Gets the component measures list.
		 *
		 * @return the component measures list
		 */
		List<Result> getComponentMeasuresList();
		
		void buildCellTable(ManageMeasureSearchModel result,
				final String searchText, List<ManageMeasureSearchModel.Result> measureSelectedList);
		HasClickHandlers getReturnButton();
		
		Widget asWidget();
		
	}
	
	
	/** The page size. */
	private int PAGE_SIZE = 25;
	
	/** The panel. */
	private SimplePanel panel = new SimplePanel();
	
	/** The meta data display. */
	private MetaDataDetailDisplay metaDataDisplay;
	
	/** The add edit authors display. */
	private AddEditAuthorsDisplay addEditAuthorsDisplay;
	
	/** The add edit component measures display. */
	private AddEditComponentMeasuresDisplay addEditComponentMeasuresDisplay;
	
	/** The current measure detail. */
	private ManageMeasureDetailModel currentMeasureDetail;
	
	/** The current authors list. */
	private ManageAuthorsModel currentAuthorsList;
	
	/** The current measure type list. */
	private ManageMeasureTypeModel currentMeasureTypeList;
	
	/** The author list. */
	private List<Author> authorList = new ArrayList<Author>();
	
	/** The measure type list. */
	private List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
	
	/** The db author list. */
	private List<Author> dbAuthorList = new ArrayList<Author>();
	
	/** The db measure type list. */
	private List<MeasureType> dbMeasureTypeList = new ArrayList<MeasureType>();
	
	/** The db qdm selected list. */
	private List<QualityDataSetDTO> dbQDMSelectedList = new ArrayList<QualityDataSetDTO>();
	
	/** The db component measures selected list. */
	private List<ManageMeasureSearchModel.Result> dbComponentMeasuresSelectedList = new ArrayList<ManageMeasureSearchModel.Result>();
	
	/**
	 * Gets the db component measures selected list.
	 *
	 * @return the db component measures selected list
	 */
	public List<ManageMeasureSearchModel.Result> getDbComponentMeasuresSelectedList() {
		return dbComponentMeasuresSelectedList;
	}
	
	/**
	 * Sets the db component measures selected list.
	 *
	 * @param dbComponentMeasuresSelectedList the new db component measures selected list
	 */
	public void setDbComponentMeasuresSelectedList(
			List<ManageMeasureSearchModel.Result> dbComponentMeasuresSelectedList) {
		this.dbComponentMeasuresSelectedList = dbComponentMeasuresSelectedList;
	}
	
	/** The empty widget. */
	private SimplePanel emptyWidget = new SimplePanel();
	
	/** The previous continue buttons. */
	private HasVisible previousContinueButtons;
	
	/** The last request time. */
	private long lastRequestTime;
	
	/** The max emeasure id. */
	private int maxEmeasureId;
	
	/** The editable. */
	private boolean editable = false;
	
	/** The is sub view. */
	private boolean isSubView = false;
	
	/** The measure xml model. */
	private MeasureXmlModel measureXmlModel; // will hold the measure xml. 02/2013
	
	/** The is measure details loaded. */
	private boolean isMeasureDetailsLoaded = false;
	
	/** The service. */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	
	/** The manage measure search model. */
	private ManageMeasureSearchModel manageMeasureSearchModel;
	
	/** The is component measures selected. */
	private boolean isComponentMeasuresSelected = false;
	
	
	/**
	 * Instantiates a new meta data presenter.
	 *
	 * @param mDisplay the m display
	 * @param aDisplay the a display
	 * @param mtDisplay the mt display
	 * @param cmDisplay the cm display
	 * @param pcButtons the pc buttons
	 * @param lp the lp
	 */
	public MetaDataPresenter(MetaDataDetailDisplay mDisplay, AddEditAuthorsDisplay aDisplay, AddEditComponentMeasuresView cmDisplay, HasVisible pcButtons, ListBoxCodeProvider lp) {
		previousContinueButtons = pcButtons;
		this.metaDataDisplay = mDisplay;
		this.addEditAuthorsDisplay = aDisplay;
		this.addEditComponentMeasuresDisplay = cmDisplay;
		getMeasureStewardList(lp);
		HandlerManager eventBus = MatContext.get().getEventBus();
		metaDataDisplay.getEditAuthorsButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getMetaDataDisplay().getSaveErrorMsg().clear();
				displayAddEditAuthors();
			}
		});
		
		metaDataDisplay.getAddEditComponentMeasures().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getMetaDataDisplay().getSaveErrorMsg().clear();
				addEditComponentMeasuresDisplay.getSearchString().setValue("");
				displayAddEditComponentMeasures();
			}
		});
		metaDataDisplay.getDeleteMeasure().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(final ClickEvent event) {
				DeleteMeasureConfirmationBox.showDeletionConfimationDialog();
				DeleteMeasureConfirmationBox.getConfirm().addClickHandler(new ClickHandler() {
					
					@Override
					public void onClick(final ClickEvent event) {
						
						checkPasswordForMeasureDeletion(DeleteMeasureConfirmationBox.getPasswordEntered());
						DeleteMeasureConfirmationBox.getDialogBox().hide();
					}
				});
			}
		});
		addEditAuthorsDisplay.getAddEditCancelButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addEditAuthorsDisplay.getMeasureDevInput().setText("");
				
			}
		});
		/*addEditAuthorsDisplay.getCancelButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addEditAuthorsDisplay.getMeasureDevInput().setValue("");
				//addEditAuthorsDisplay.hideTextBox();
			}
		});*/
		addEditAuthorsDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				isSubView = false;
				metaDataDisplay.setSaveButtonEnabled(editable);
				getMeasureDeveloperAuthors();
				backToDetail();
			}
		});
		
		addEditComponentMeasuresDisplay.getReturnButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				isSubView = false;
				metaDataDisplay.setSaveButtonEnabled(editable);
				getComponentMeasures();
				backToDetail();
			}
		} );
//		
//		addEditAuthorsDisplay.getSaveButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(final ClickEvent event) {
//				if (addEditAuthorsDisplay.getAuthor().equals(MatContext.PLEASE_SELECT)) {
//					//do nothing
//				} else if (!addEditAuthorsDisplay.getAuthor().startsWith("Other")) {
//					if (!addEditAuthorsDisplay.getAuthor().equals("")) {
//						//addToAuthorsList(addEditAuthorsDisplay.getAuthor());
//						addEditAuthorsDisplay.getAuthorInputBox().setValue("");
//					}
//				} else {
//					if (!addEditAuthorsDisplay.getOtherAuthor().getValue().equals("")) {
//						//addToAuthorsList(addEditAuthorsDisplay.getOtherAuthor().getValue());
//						addEditAuthorsDisplay.getOtherAuthor().setValue("");
//					}
//				}
//			}
//		});
//		addEditAuthorsDisplay.getRemoveButton().addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(final ClickEvent event) {
//				//  removeSelectedAuthor();
//			}
//		});
		
//		addEditAuthorsDisplay.getAuthorInputBox().addValueChangeHandler(new ValueChangeHandler<String>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<String> event) {
//				String authorValue = event.getValue();
//				String changedAuthorValue = addEditAuthorsDisplay.getAuthor();
//				if (changedAuthorValue.startsWith("Other")) {
//					addEditAuthorsDisplay.showTextBox();
//				} else {
//					addEditAuthorsDisplay.hideTextBox();
//				}
//				
//			}
//		});
		//US 413. Added value change listener to show or clear out Steward Other text box based on the selection.
		metaDataDisplay.getMeasureStewardListBox().addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				String value = metaDataDisplay.getMeasureStewardValue();
				if (value.startsWith("Other")) {
					metaDataDisplay.showOtherTextBox();
				} else {
					metaDataDisplay.hideOtherTextBox();
				}
				
			}
		});
		
		metaDataDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Search String: "+ metaDataDisplay.getSearchString().getValue());
				//searchMeasuresList(metaDataDisplay.getSearchString().getValue(),1,Integer.MAX_VALUE,1);
			}
		});
		
		metaDataDisplay.getSaveButton().addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				isComponentMeasuresSelected = false;
				saveMetaDataInformation(true);
			}
			
		});
		
		metaDataDisplay.getFocusPanel().addKeyDownHandler(new KeyDownHandler(){
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				//control-alt-s is save
				if (event.isAltKeyDown() && event.isControlKeyDown() && (event.getNativeKeyCode() == 83)) {
					saveMetaDataInformation(true);
				}
			}
		});
		//This event will be called when measure has been selected from measureLibrary
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				isMeasureDetailsLoaded = false;
				if (event.getMeasureId() != null) {
					isMeasureDetailsLoaded = true;
					//getMeasureDetail();
					getMeasureAndLogRecentMeasure();
				} else {
					displayEmpty();
				}
			}
		});
		
		metaDataDisplay.getGenerateEmeasureIdButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				generateAndSaveNewEmeasureid();
			}
		});
		
		addEditComponentMeasuresDisplay.getAddtoComponentMeasuresButtonHandler().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addEditComponentMeasuresDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getCOMPONENT_MEASURES_ADDED_SUCCESSFULLY());
				metaDataDisplay.setComponentMeasureSelectedList(addEditComponentMeasuresDisplay.getComponentMeasuresList());
			}
		});
		addEditAuthorsDisplay.getAddToMeasureDeveloperListBtn().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				currentMeasureDetail.setAuthorSelectedList(addEditAuthorsDisplay.getAuthorSelectedList());
				
				metaDataDisplay.setAuthorsSelectedList(currentMeasureDetail.getAuthorSelectedList());
				addEditAuthorsDisplay.getSuccessMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getMeasureDeveloperAddedSuccessfully());
			}
		});
		addEditAuthorsDisplay.getAddButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				String name = addEditAuthorsDisplay.getMeasureDevInput().getValue().trim();
				if((name!=null) && !name.isEmpty()){
					addToTheList(name);
				}
			}
		});
		addEditComponentMeasuresDisplay.getSearchButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				searchMeasuresList(addEditComponentMeasuresDisplay.getSearchString().getValue(), 1, PAGE_SIZE, 1);
			}
		});
		
		emptyWidget.add(new Label("No Measure Selected"));
	}
	
	private void getMeasureStewardList(ListBoxCodeProvider listBoxCodeProvider) {
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

	}

	/**
	 * Adds the to the list.
	 *
	 * @param name the name
	 */
	protected void addToTheList(String name) {
		final Author newListObject = new Author();
		newListObject.setAuthorName(name);
		newListObject.setOrgId(UUIDUtilClient.uuid());
		addEditAuthorsDisplay.getListOfAllAuthor().add(newListObject);
		addEditAuthorsDisplay.buildAuthorCellTable(addEditAuthorsDisplay.getListOfAllAuthor(), editable,
				currentMeasureDetail.getAuthorSelectedList());
		
	}
	
	//TODO by Ravi
	
	/**
	 * Gets the all add edit authors.
	 *
	 * @return the all add edit authors
	 */
	protected void getAllAddEditAuthors() {
		service.getAllAddEditAuthors(new AsyncCallback<List<Author>>() {
			
			@Override
			public void onSuccess(List<Author> authorList) {
				List<String> orgIDList = new ArrayList<String>();
				for(int j=0;j<authorList.size();j++){
					orgIDList.add(authorList.get(j).getOrgId());
				}
				
				for(int i=0;i<currentMeasureDetail.getAuthorSelectedList().size();i++){
					if(!orgIDList.contains(currentMeasureDetail.getAuthorSelectedList().get(i).getOrgId())){
						authorList.add(currentMeasureDetail.getAuthorSelectedList().get(i));
					}
				}
				addEditAuthorsDisplay.buildAuthorCellTable(authorList, editable, currentMeasureDetail.getAuthorSelectedList());
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
		
	}
	
	/**
	 * Gets the component measures.
	 *
	 * @return the component measures
	 */
	public final void getComponentMeasures(){
		currentMeasureDetail.setComponentMeasuresSelectedList(metaDataDisplay.getComponentMeasureSelectedList());
		List<String> listIds = new ArrayList<String>();
		for(int i = 0;i<currentMeasureDetail.getComponentMeasuresSelectedList().size();i++){
			listIds.add(currentMeasureDetail.getComponentMeasuresSelectedList().get(i).getId());
		}
		if((listIds!=null) && (listIds.size()>0)){
			MatContext
			.get()
			.getMeasureService().getComponentMeasures(listIds, new AsyncCallback<ManageMeasureSearchModel>() {
				
				@Override
				public void onFailure(Throwable caught) {
				}
				
				@Override
				public void onSuccess(ManageMeasureSearchModel result) {
					List<ManageMeasureSearchModel.Result> measureSelectedList = result.getData();
					currentMeasureDetail.setComponentMeasuresSelectedList(measureSelectedList);
					metaDataDisplay.buildComponentMeasuresSelectedList(measureSelectedList, editable);
				}
			});
		} else {
			metaDataDisplay.buildComponentMeasuresSelectedList(currentMeasureDetail.getComponentMeasuresSelectedList(),
					editable);
		}
		
	}
	
	
	
	/**
	 * Search measures list.
	 *
	 * @param searchText the search text
	 * @param startIndex the start index
	 * @param pageSize the page size
	 * @param filter the filter
	 */
	private void searchMeasuresList(final String searchText, int startIndex, int pageSize,
			int filter){
		addEditComponentMeasuresDisplay.getSuccessMessageDisplay().clear();
		showAdminSearchingBusy(true);
		metaDataDisplay.setSaveButtonEnabled(false);
		MatContext
		.get()
		.getMeasureService()
		.search(searchText, startIndex, pageSize, filter,
				new AsyncCallback<ManageMeasureSearchModel>() {
			
			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay
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
			public void onSuccess(ManageMeasureSearchModel result) {
				showAdminSearchingBusy(false);
				manageMeasureSearchModel = result;
				addEditComponentMeasuresDisplay.buildCellTable(manageMeasureSearchModel,searchText,
						currentMeasureDetail.getComponentMeasuresSelectedList());
			}
		});
	}
	
	/**
	 * Show admin searching busy.
	 *
	 * @param busy the busy
	 */
	private void showAdminSearchingBusy(boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		((Button) addEditComponentMeasuresDisplay.getSearchButton()).setEnabled(!busy);
		((TextBox) (addEditComponentMeasuresDisplay.getSearchString())).setEnabled(!busy);
		((Button) addEditComponentMeasuresDisplay.getReturnButton()).setEnabled(!busy);
		((Button) addEditComponentMeasuresDisplay.getAddtoComponentMeasuresBtn()).setEnabled(!busy);
	}
	
	
	/**
	 * Gets the applied qdm list.
	 *
	 * @param checkForSupplementData the check for supplement data
	 * @return the applied qdm list
	 */
	private final void getAppliedQDMList(boolean checkForSupplementData) {
		String measureId = MatContext.get().getCurrentMeasureId();
		if ((measureId != null) && !measureId.equals("")) {
			service.getAppliedQDMForItemCount(measureId,
					checkForSupplementData,
					new AsyncCallback<List<QualityDataSetDTO>>()  {
				@Override
				public void onFailure(final Throwable caught) {
					Window.alert(MatContext.get().getMessageDelegate()
							.getGenericErrorMessage());
				}
				
				/**
				 * On success.
				 *
				 * @param result the result
				 */
				@Override
				public void onSuccess(
						final List<QualityDataSetDTO> result) {
					QDSAppliedListModel appliedListModel = new QDSAppliedListModel();
					appliedListModel.setAppliedQDMs(result);
					metaDataDisplay.buildCellTable(appliedListModel, editable);
				}
			});
			
		}
		
	}
	
	/**
	 * Gets the all measure types.
	 *
	 * @return the all measure types
	 */
	private void getAllMeasureTypes() {
		service.getAllMeasureTypes(new AsyncCallback<List<MeasureType>>() {
			
			@Override
			public void onSuccess(List<MeasureType> result) {
				metaDataDisplay.buildMeasureTypeCellTable(result, editable);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	
	/**
	 * Fire successfull deletion event.
	 * 
	 * @param isSuccess
	 *            the is success
	 * @param message
	 *            the message
	 */
	private void fireSuccessfullDeletionEvent(boolean isSuccess, String message){
		MeasureDeleteEvent deleteEvent = new MeasureDeleteEvent(isSuccess, message);
		MatContext.get().getEventBus().fireEvent(deleteEvent);
	}
	
	/**
	 * Fire back to measure library event.
	 */
	private void fireBackToMeasureLibraryEvent() {
		BackToMeasureLibraryPage backToMeasureLibraryPage = new BackToMeasureLibraryPage();
		MatContext.get().getEventBus().fireEvent(backToMeasureLibraryPage);
	}
	
	
	/**
	 * Generate and save new emeasureid.
	 */
	private void generateAndSaveNewEmeasureid() {
		MeasureServiceAsync service = MatContext.get().getMeasureService();
		service.generateAndSaveMaxEmeasureId(currentMeasureDetail, new AsyncCallback<Integer>() {
			
			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+caught.getLocalizedMessage(), 0);
				
			}
			
			@Override
			public void onSuccess(Integer result) {
				maxEmeasureId = result.intValue();
				if (maxEmeasureId < 1000000) {
					metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
					metaDataDisplay.getEmeasureId().setValue(maxEmeasureId + "");
					((TextBox) metaDataDisplay.getEmeasureId()).setFocus(true);
				}
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
	 * Display empty.
	 */
	private void displayEmpty() {
		previousContinueButtons.setVisible(false);
		panel.clear();
		panel.add(emptyWidget);
	}
	
	/**
	 * Display detail.
	 */
	public void displayDetail() {
		previousContinueButtons.setVisible(true);
		prepopulateFields();
		panel.clear();
		if (editable) {
			if ("0".equals(metaDataDisplay.getEmeasureId().getValue())) {
				metaDataDisplay.setGenerateEmeasureIdButtonEnabled(true);
				metaDataDisplay.getEmeasureId().setValue("");
			} else if (metaDataDisplay.getEmeasureId() != null) {
				metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
			}
		} else {
			metaDataDisplay.setGenerateEmeasureIdButtonEnabled(false);
			if ("0".equals(metaDataDisplay.getEmeasureId().getValue())) {
				metaDataDisplay.getEmeasureId().setValue("");
			}
		}
		
		panel.add(metaDataDisplay.asWidget());
	}
	
	/**
	 * Back to detail.
	 */
	public void backToDetail() {
		previousContinueButtons.setVisible(true);
		panel.clear();
		panel.add(metaDataDisplay.asWidget());
		Mat.focusSkipLists("MeasureComposer");
	}
	
	/**
	 * Prepopulate fields.
	 */
	private void prepopulateFields() {
		metaDataDisplay.getNqfId().setValue(currentMeasureDetail.getNqfId());
		metaDataDisplay.geteMeasureIdentifier().setText(currentMeasureDetail.getMeasureSetId());
		metaDataDisplay.getSetName().setValue(currentMeasureDetail.getGroupName());
		metaDataDisplay.getMeasureName().setText(currentMeasureDetail.getName());
		metaDataDisplay.getShortName().setText(currentMeasureDetail.getShortName());
		metaDataDisplay.getMeasureScoring().setText(currentMeasureDetail.getMeasScoring());
		metaDataDisplay.getClinicalRecommendation().setValue(currentMeasureDetail.getClinicalRecomms());
		metaDataDisplay.getDefinitions().setValue(currentMeasureDetail.getDefinitions());
		metaDataDisplay.getDescription().setValue(currentMeasureDetail.getDescription());
		
		metaDataDisplay.getDisclaimer().setValue(currentMeasureDetail.getDisclaimer());
		metaDataDisplay.getRiskAdjustment().setValue(currentMeasureDetail.getRiskAdjustment());
		metaDataDisplay.getRateAggregation().setValue(currentMeasureDetail.getRateAggregation());
		metaDataDisplay.getInitialPop().setValue(currentMeasureDetail.getInitialPop());
		metaDataDisplay.getDenominator().setValue(currentMeasureDetail.getDenominator());
		metaDataDisplay.getDenominatorExclusions().setValue(currentMeasureDetail.getDenominatorExclusions());
		metaDataDisplay.getNumerator().setValue(currentMeasureDetail.getNumerator());
		metaDataDisplay.getNumeratorExclusions().setValue(currentMeasureDetail.getNumeratorExclusions());
		metaDataDisplay.getDenominatorExceptions().setValue(currentMeasureDetail.getDenominatorExceptions());
		metaDataDisplay.getMeasurePopulation().setValue(currentMeasureDetail.getMeasurePopulation());
		metaDataDisplay.getMeasureObservations().setValue(currentMeasureDetail.getMeasureObservations());
		metaDataDisplay.getMeasurePopulationExclusions().setValue(currentMeasureDetail.getMeasurePopulationExclusions());
		
		metaDataDisplay.getCopyright().setValue(currentMeasureDetail.getCopyright());
		if ((currentMeasureDetail.getEndorseByNQF() != null) && currentMeasureDetail.getEndorseByNQF().equals(true)) {
			metaDataDisplay.getEndorsebyNQF().setValue(true);
		} else {
			metaDataDisplay.getNotEndorsebyNQF().setValue(true);
		}
		
		/*metaDataDisplay.getMeasureStatus().setValueMetadata(currentMeasureDetail.getMeasureStatus());*/
		metaDataDisplay.getGuidance().setValue(currentMeasureDetail.getGuidance());
		metaDataDisplay.getTransmissionFormat().setValue(currentMeasureDetail.getTransmissionFormat());
		metaDataDisplay.getImprovementNotation().setValue(currentMeasureDetail.getImprovNotations());
		metaDataDisplay.getSupplementalData().setValue(currentMeasureDetail.getSupplementalData());
		metaDataDisplay.getFinalizedDate().setText(currentMeasureDetail.getFinalizedDate());
		metaDataDisplay.getMeasurementFromPeriodInputBox().setValue(currentMeasureDetail.getMeasFromPeriod());
		metaDataDisplay.getMeasurementToPeriodInputBox().setValue(currentMeasureDetail.getMeasToPeriod());
		metaDataDisplay.getVersionNumber().setText(currentMeasureDetail.getVersionNumber());
		
		//US 413. Populate Steward and Steward Other value if any.
		String steward = currentMeasureDetail.getMeasSteward();
		metaDataDisplay.getMeasureSteward().setValueMetadata(currentMeasureDetail.getMeasSteward());
		if ((metaDataDisplay.getMeasureSteward().getSelectedIndex() == 0) && (steward != null) && !steward.equals("")) {
			steward = "Other";
			currentMeasureDetail.setMeasStewardOther(currentMeasureDetail.getMeasSteward());
			metaDataDisplay.getMeasureSteward().setValueMetadata(steward);
		}
		boolean setSteward = (steward != null) && steward.equalsIgnoreCase("Other");
		if (setSteward) {
			metaDataDisplay.showOtherTextBox();
			metaDataDisplay.getMeasureStewardOther().setValue(currentMeasureDetail.getMeasStewardOther());
			
		} else {
			metaDataDisplay.hideOtherTextBox();
		}
		
		metaDataDisplay.getRationale().setValue(currentMeasureDetail.getRationale());
		metaDataDisplay.getStratification().setValue(currentMeasureDetail.getStratification());
		metaDataDisplay.getRiskAdjustment().setValue(currentMeasureDetail.getRiskAdjustment());
		//authorSelectedList
		if (currentMeasureDetail.getAuthorSelectedList() != null) {
			metaDataDisplay.setAuthorsSelectedList(currentMeasureDetail.getAuthorSelectedList());
		} else {
			List<Author> authorList = new ArrayList<Author>();
			metaDataDisplay.setAuthorsSelectedList(authorList);
			currentMeasureDetail.setAuthorSelectedList(authorList);
		}
		dbAuthorList.clear();
		dbAuthorList.addAll(currentMeasureDetail.getAuthorSelectedList());
		getMeasureDeveloperAuthors();
		authorList = currentMeasureDetail.getAuthorSelectedList();
		//measureTypeSelectList
		if (currentMeasureDetail.getMeasureTypeSelectedList() != null) {
			metaDataDisplay.setMeasureTypeSelectedList(currentMeasureDetail.getMeasureTypeSelectedList());
		} else {
			List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
			metaDataDisplay.setMeasureTypeSelectedList(measureTypeList);
			currentMeasureDetail.setMeasureTypeSelectedList(measureTypeList);
		}
		dbMeasureTypeList.clear();
		dbMeasureTypeList.addAll(currentMeasureDetail.getMeasureTypeSelectedList());
		getAllMeasureTypes();
		measureTypeList = currentMeasureDetail.getMeasureTypeSelectedList();
		
		if (currentMeasureDetail.getQdsSelectedList() != null) {
			metaDataDisplay.setQdmSelectedList(currentMeasureDetail.getQdsSelectedList());
		} else {
			List<QualityDataSetDTO> qdmList = new ArrayList<QualityDataSetDTO>();
			metaDataDisplay.setQdmSelectedList(qdmList);
			currentMeasureDetail.setQdsSelectedList(qdmList);
		}
		dbQDMSelectedList.clear();
		dbQDMSelectedList.addAll(currentMeasureDetail.getQdsSelectedList());
		getAppliedQDMList(true);
		//Component Measures List
		if (currentMeasureDetail.getComponentMeasuresSelectedList() != null) {
			metaDataDisplay.setComponentMeasureSelectedList(currentMeasureDetail.getComponentMeasuresSelectedList());
		} else {
			List<ManageMeasureSearchModel.Result> componentMeasuresList = new ArrayList<ManageMeasureSearchModel.Result>();
			metaDataDisplay.setComponentMeasureSelectedList(componentMeasuresList);
			currentMeasureDetail.setComponentMeasuresSelectedList(componentMeasuresList);
		}
		getComponentMeasures();
		dbComponentMeasuresSelectedList.clear();
		dbComponentMeasuresSelectedList.addAll(currentMeasureDetail.getComponentMeasuresSelectedList());
		editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		if (currentMeasureDetail.getReferencesList() != null) {
			metaDataDisplay.setReferenceValues(currentMeasureDetail.getReferencesList(), editable);
		} else {
			metaDataDisplay.setReferenceValues(new ArrayList<String>(), editable);
		}
		metaDataDisplay.setAddEditButtonsVisible(editable);
		ReadOnlyHelper.setReadOnlyForCurrentMeasure(metaDataDisplay.asWidget(),editable);
		metaDataDisplay.enableEndorseByRadioButtons(editable);
		metaDataDisplay.setSaveButtonEnabled(editable);
		metaDataDisplay.getEmeasureId().setValue(currentMeasureDetail.geteMeasureId()+"");
		
		if ((currentMeasureDetail.getMeasureOwnerId() != null) && !currentMeasureDetail.getMeasureOwnerId()
				.equalsIgnoreCase(MatContext.get().getLoggedinUserId())) {
			metaDataDisplay.getDeleteMeasure().setEnabled(false);
		} else {
			metaDataDisplay.getDeleteMeasure().setEnabled(true);
		}
		currentMeasureDetail.setEditable(editable);
	}
	
	/**
	 * Gets the measure developer authors.
	 *
	 * @return the measure developer authors
	 */
	public void getMeasureDeveloperAuthors() {
		if (currentMeasureDetail.getAuthorSelectedList() != null) {
			
			metaDataDisplay.buildAuthorCellTable(currentMeasureDetail.getAuthorSelectedList(), editable);
		} else {
			List<Author> authorList = new ArrayList<Author>();
			metaDataDisplay.buildAuthorCellTable(authorList, editable);
			currentMeasureDetail.setAuthorSelectedList(authorList);
		}
		
	}
	
	/**
	 * Save meta data information.
	 * 
	 * @param dispSuccessMsg
	 *            the disp success msg
	 */
	public void saveMetaDataInformation(final boolean dispSuccessMsg) {
		metaDataDisplay.getSaveErrorMsg().clear();
		metaDataDisplay.getErrorMessageDisplay().clear();
		metaDataDisplay.getSuccessMessageDisplay().clear();
		metaDataDisplay.getSaveBtn().setFocus(true);
		updateModelDetailsFromView();
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			Mat.showLoadingMessage();
			MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(true);
			MatContext.get().getMeasureService().saveMeasureDetails(currentMeasureDetail,
					new AsyncCallback<SaveMeasureResult>() {
				
				@Override
				public void onSuccess(SaveMeasureResult result) {
					
					if (result.isSuccess()) {
						Mat.hideLoadingMessage();
						if (dispSuccessMsg) {
							metaDataDisplay.getSuccessMessageDisplay().setMessage(MatContext.get()
									.getMessageDelegate().getChangesSavedMessage());
						}
						
						MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
						MatContext.get().getMeasureService().getMeasure(MatContext.get().getCurrentMeasureId(),
								new AsyncCallback<ManageMeasureDetailModel>() {
							
							@Override
							public void onFailure(Throwable caught) {
								
							}
							
							@Override
							public void onSuccess(ManageMeasureDetailModel result) {
								currentMeasureDetail = result;
								displayDetail();
								
							}
							
						});
					} else {
						Mat.hideLoadingMessage();
						MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
						metaDataDisplay.getErrorMessageDisplay().setMessage(MessageDelegate
								.getMeasureSaveServerErrorMessage(result.getFailureReason()));
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
					Mat.hideLoadingMessage();
					MatContext.get().getSynchronizationDelegate().setSavingMeasureDetails(false);
					metaDataDisplay.getErrorMessageDisplay().setMessage(caught.getLocalizedMessage());
				}
			});
		}
	}
	
	/**
	 * Update model details from view.
	 */
	private void updateModelDetailsFromView() {
		updateModelDetailsFromView(currentMeasureDetail, metaDataDisplay);
	}
	
	/**
	 * Update model details from view.
	 * 
	 * @param currentMeasureDetail
	 *            the current measure detail
	 * @param metaDataDisplay
	 *            the meta data display
	 */
	public void updateModelDetailsFromView(ManageMeasureDetailModel currentMeasureDetail, MetaDataDetailDisplay metaDataDisplay) {
		currentMeasureDetail.setName(metaDataDisplay.getMeasureName().getText());
		currentMeasureDetail.setShortName(metaDataDisplay.getShortName().getText());
		currentMeasureDetail.setFinalizedDate(metaDataDisplay.getFinalizedDate().getText());
		currentMeasureDetail.setClinicalRecomms(metaDataDisplay.getClinicalRecommendation().getValue());
		currentMeasureDetail.setDefinitions(metaDataDisplay.getDefinitions().getValue());
		currentMeasureDetail.setDescription(metaDataDisplay.getDescription().getValue());
		currentMeasureDetail.setDisclaimer(metaDataDisplay.getDisclaimer().getValue());
		currentMeasureDetail.setRiskAdjustment(metaDataDisplay.getRiskAdjustment().getValue());
		currentMeasureDetail.setRateAggregation(metaDataDisplay.getRateAggregation().getValue());
		currentMeasureDetail.setInitialPop(metaDataDisplay.getInitialPop().getValue());
		currentMeasureDetail.setDenominator(metaDataDisplay.getDenominator().getValue());
		currentMeasureDetail.setDenominatorExclusions(metaDataDisplay.getDenominatorExclusions().getValue());
		currentMeasureDetail.setNumerator(metaDataDisplay.getNumerator().getValue());
		currentMeasureDetail.setNumeratorExclusions(metaDataDisplay.getNumeratorExclusions().getValue());
		currentMeasureDetail.setDenominatorExceptions(metaDataDisplay.getDenominatorExceptions().getValue());
		currentMeasureDetail.setMeasurePopulation(metaDataDisplay.getMeasurePopulation().getValue());
		currentMeasureDetail.setMeasureObservations(metaDataDisplay.getMeasureObservations().getValue());
		
		currentMeasureDetail.setCopyright(metaDataDisplay.getCopyright().getValue());
		currentMeasureDetail.setEndorseByNQF(metaDataDisplay.getEndorsebyNQF().getValue());
		currentMeasureDetail.setGuidance(metaDataDisplay.getGuidance().getValue());
		currentMeasureDetail.setTransmissionFormat(metaDataDisplay.getTransmissionFormat().getValue());
		currentMeasureDetail.setImprovNotations(metaDataDisplay.getImprovementNotation().getValue());
		currentMeasureDetail.setMeasFromPeriod(metaDataDisplay.getMeasurementFromPeriod());
		
		//US 413. Update Steward and Steward Other values from the UI.
		String stewardValue = metaDataDisplay.getMeasureStewardValue();
		if (nullCheck(stewardValue)) {
			currentMeasureDetail.setMeasSteward(stewardValue);
		} else {
			currentMeasureDetail.setMeasSteward(null);
		}
		currentMeasureDetail.setMeasStewardOther(metaDataDisplay.getMeasureStewardOtherValue());
		currentMeasureDetail.setMeasToPeriod(metaDataDisplay.getMeasurementToPeriod());
		currentMeasureDetail.setSupplementalData(metaDataDisplay.getSupplementalData().getValue());
		/*if (nullCheck(metaDataDisplay.getMeasureStatusValue())) {
			currentMeasureDetail.setMeasureStatus(metaDataDisplay.getMeasureStatusValue());
		}*/
		currentMeasureDetail.setRationale(metaDataDisplay.getRationale().getValue());
		currentMeasureDetail.setReferencesList(metaDataDisplay.getReferenceValues());
		currentMeasureDetail.setMeasureSetId(metaDataDisplay.geteMeasureIdentifier().getText());
		currentMeasureDetail.setGroupName(metaDataDisplay.getSetName().getValue());
		currentMeasureDetail.setStratification(metaDataDisplay.getStratification().getValue());
		currentMeasureDetail.setRiskAdjustment(metaDataDisplay.getRiskAdjustment().getValue());
		currentMeasureDetail.setVersionNumber(metaDataDisplay.getVersionNumber().getText());
		currentMeasureDetail.setAuthorSelectedList(metaDataDisplay.getAuthorsSelectedList());
		currentMeasureDetail.setMeasureTypeSelectedList(measureTypeList);
		currentMeasureDetail.setQdsSelectedList(metaDataDisplay.getQdmSelectedList());
		currentMeasureDetail.setComponentMeasuresSelectedList(metaDataDisplay.getComponentMeasureSelectedList());
		currentMeasureDetail.setToCompareAuthor(dbAuthorList);
		currentMeasureDetail.setToCompareMeasure(dbMeasureTypeList);
		currentMeasureDetail.setToCompareItemCount(dbQDMSelectedList);
		currentMeasureDetail.setToCompareComponentMeasures(dbComponentMeasuresSelectedList);
		currentMeasureDetail.setNqfId(metaDataDisplay.getNqfId().getValue());
		currentMeasureDetail.setMeasurePopulationExclusions(metaDataDisplay.getMeasurePopulationExclusions().getValue());
		if ((metaDataDisplay.getEmeasureId().getValue() != null) && !metaDataDisplay.getEmeasureId().getValue().equals("")) {
			currentMeasureDetail.seteMeasureId(new Integer(metaDataDisplay.getEmeasureId().getValue()));
		}
	}
	
	
	
	/**
	 * Null check.
	 * 
	 * @param value
	 *            the value
	 * @return true, if successful
	 */
	private boolean nullCheck(String value) {
		return  !value.equalsIgnoreCase("--Select--") && !value.equals("");
	}
	
	
	/**
	 * Display add edit authors.
	 */
	private void displayAddEditAuthors() {
		isSubView = true;
		//addEditAuthorsDisplay.setReturnToLink("Return to Previous");
		currentAuthorsList = new ManageAuthorsModel(currentMeasureDetail.getAuthorSelectedList());
		//currentAuthorsList.setPageSize(SearchView.PAGE_SIZE_ALL);
		getAllAddEditAuthors();
		panel.clear();
		panel.add(addEditAuthorsDisplay.asWidget());
		previousContinueButtons.setVisible(false);
		Mat.focusSkipLists("MeasureComposer");
	}
	/**
	 * Display add edit component measures.
	 */
	private void displayAddEditComponentMeasures() {
		isSubView = true;
		clearMessages();
		VerticalPanel vPanel = new VerticalPanel();
		searchMeasuresList("",1,PAGE_SIZE,1);
		panel.clear();
		panel.setStyleName("contentWithHeadingPanel");
		vPanel.add(addEditComponentMeasuresDisplay.asWidget());
		vPanel.add(new SpacerWidget());
		vPanel.add(new SpacerWidget());
		vPanel.add(addEditComponentMeasuresDisplay.getRetButton());
		panel.add(vPanel);
		previousContinueButtons.setVisible(false);
		Mat.focusSkipLists("MeasureComposer");
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		if ((MatContext.get().getCurrentMeasureId() == null)
				|| MatContext.get().getCurrentMeasureId().equals("")) {
			displayEmpty();
		} else {
			if (!isMeasureDetailsLoaded) { // this check is made so that when measure is clicked from Measure library, its not called twice.
				currentMeasureDetail = null;
				lastRequestTime = System.currentTimeMillis();
				getMeasureDetail();
			} else {
				isMeasureDetailsLoaded = false;
			}
		}
		getAppliedQDMList(true);
		getAllMeasureTypes();
		MeasureComposerPresenter.setSubSkipEmbeddedLink("MetaDataView.containerPanel");
		Mat.focusSkipLists("MeasureComposer");
		clearMessages();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		/*if(currentMeasureDetail != null) {// Removed Auto Save
			saveMetaDataInformation(false);
		}*/
		//This is done to reset measure composure tab to show "No Measure Selected" as when measure is deleted,it should not show Any sub tabs under MeasureComposure.
		//MatContext.get().getCurrentMeasureInfo().setMeasureId("");
		clearMessages();
	}
	
	/** Gets the measure and logs in this measure as recently used measure in recent measure activity log.
	 * 
	 * @return the measure and log recent measure */
	private void getMeasureAndLogRecentMeasure() {
		MatContext.get().getMeasureService().getMeasureAndLogRecentMeasure(MatContext.get().getCurrentMeasureId(),
				MatContext.get().getLoggedinUserId(), getAsyncCallBack());
	}
	
	/** Gets the measure detail.
	 * 
	 * @return the measure detail */
	private void getMeasureDetail(){
		MatContext.get().getMeasureService().getMeasure(MatContext.get().getCurrentMeasureId(),
				getAsyncCallBack());
	}
	
	/** Gets the async call back.
	 * 
	 * @return the async call back */
	private AsyncCallback<ManageMeasureDetailModel> getAsyncCallBack() {
		return new AsyncCallback<ManageMeasureDetailModel>() {
			final long callbackRequestTime = lastRequestTime;
			@Override
			public void onFailure(Throwable caught) {
				metaDataDisplay.getErrorMessageDisplay().setMessage(MatContext.get()
						.getMessageDelegate().getGenericErrorMessage());
				MatContext.get().recordTransactionEvent(null, null, null,
						"Unhandled Exception: " +caught.getLocalizedMessage(), 0);
			}
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				if (callbackRequestTime == lastRequestTime) {
					currentMeasureDetail = result;
					//					loadMeasureXml(result.getId());
					displayDetail();
					fireMeasureEditEvent();
				}
			}
		};
	}
	
	/**
	 * Check password for measure deletion.
	 * 
	 * @param password
	 *            the password
	 */
	private void checkPasswordForMeasureDeletion(String password) {
		
		MatContext.get().getLoginService().isValidPassword(MatContext.get()
				.getLoggedinLoginId(), password, new AsyncCallback<Boolean>() {
			
			@Override
			public void onFailure(Throwable caught) {
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(false, null);
			}
			
			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					deleteMeasure();
				} else {
					fireBackToMeasureLibraryEvent();
					fireSuccessfullDeletionEvent(false, MatContext.get()
							.getMessageDelegate().getMeasureDeletionInvalidPwd());
				}
			}
		});
	}
	
	
	/**
	 * Delete measure.
	 */
	private void deleteMeasure() {
		MatContext.get().getMeasureService().saveAndDeleteMeasure(MatContext.get().getCurrentMeasureId(), new AsyncCallback<Void>(){
			
			@Override
			public void onFailure(Throwable caught) {
				fireBackToMeasureLibraryEvent();
				
				fireSuccessfullDeletionEvent(false, null);
			}
			
			@Override
			public void onSuccess(Void result) {
				MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null,
						"MEASURE_DELETE_EVENT", "Measure Successfully Deleted", ConstantMessages.DB_LOG);
				// this is set to avoid showing dirty check message if user has modified Measure details and is deleting without saving.
				currentMeasureDetail.setDeleted(true);
				MatContext.get().setMeasureDeleted(true);
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(true, MatContext.get().getMessageDelegate().getMeasureDeletionSuccessMgs());
				
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
	 * Clear messages.
	 */
	private void  clearMessages() {
		metaDataDisplay.getErrorMessageDisplay().clear();
		metaDataDisplay.getSuccessMessageDisplay().clear();
	}
	
	/**
	 * Gets the meta data display.
	 * 
	 * @return the metaDataDisplay
	 */
	public MetaDataDetailDisplay getMetaDataDisplay() {
		return metaDataDisplay;
	}
	
	/**
	 * Sets the meta data display.
	 * 
	 * @param metaDataDisplay
	 *            the metaDataDisplay to set
	 */
	public void setMetaDataDisplay(MetaDataDetailDisplay metaDataDisplay) {
		this.metaDataDisplay = metaDataDisplay;
	}
	
	/**
	 * Gets the current measure detail.
	 * 
	 * @return the currentMeasureDetail
	 */
	public ManageMeasureDetailModel getCurrentMeasureDetail() {
		return currentMeasureDetail;
	}
	
	/**
	 * Sets the current measure detail.
	 * 
	 * @param currentMeasureDetail
	 *            the currentMeasureDetail to set
	 */
	public void setCurrentMeasureDetail(
			ManageMeasureDetailModel currentMeasureDetail) {
		this.currentMeasureDetail = currentMeasureDetail;
	}
	
	/**
	 * Gets the current authors list.
	 * 
	 * @return the currentAuthorsList
	 */
	public ManageAuthorsModel getCurrentAuthorsList() {
		return currentAuthorsList;
	}
	
	/**
	 * Sets the current authors list.
	 * 
	 * @param currentAuthorsList
	 *            the currentAuthorsList to set
	 */
	public void setCurrentAuthorsList(ManageAuthorsModel currentAuthorsList) {
		this.currentAuthorsList = currentAuthorsList;
	}
	
	/**
	 * Gets the author list.
	 * 
	 * @return the authorList
	 */
	public List<Author> getAuthorList() {
		return authorList;
	}
	
	/**
	 * Sets the author list.
	 * 
	 * @param authorList
	 *            the authorList to set
	 */
	public void setAuthorList(List<Author> authorList) {
		this.authorList = authorList;
	}
	
	/**
	 * Gets the db author list.
	 * 
	 * @return the dbAuthorList
	 */
	public List<Author> getDbAuthorList() {
		return dbAuthorList;
	}
	
	/**
	 * Sets the db author list.
	 * 
	 * @param dbAuthorList
	 *            the dbAuthorList to set
	 */
	public void setDbAuthorList(List<Author> dbAuthorList) {
		this.dbAuthorList = dbAuthorList;
	}
	
	/**
	 * Gets the db measure type list.
	 * 
	 * @return the dbMeasureTypeList
	 */
	public List<MeasureType> getDbMeasureTypeList() {
		return dbMeasureTypeList;
	}
	
	/**
	 * Sets the db measure type list.
	 * 
	 * @param dbMeasureTypeList
	 *            the dbMeasureTypeList to set
	 */
	public void setDbMeasureTypeList(List<MeasureType> dbMeasureTypeList) {
		this.dbMeasureTypeList = dbMeasureTypeList;
	}
	
	/**
	 * Gets the db qdm selected list.
	 *
	 * @return the db qdm selected list
	 */
	public List<QualityDataSetDTO> getDbQDMSelectedList() {
		return dbQDMSelectedList;
	}
	
	/**
	 * Sets the db qdm selected list.
	 *
	 * @param dbQDMSelectedList the new db qdm selected list
	 */
	public void setDbQDMSelectedList(List<QualityDataSetDTO> dbQDMSelectedList) {
		this.dbQDMSelectedList = dbQDMSelectedList;
	}
	
	/**
	 * Sets the focus for save.
	 */
	public void setFocusForSave() {
		getMetaDataDisplay().getSaveBtn().setFocus(true);
	}
	
	/**
	 * Gets the adds the edit authors display.
	 * 
	 * @return the addEditAuthorsDisplay
	 */
	public AddEditAuthorsDisplay getAddEditAuthorsDisplay() {
		return addEditAuthorsDisplay;
	}
	
	/**
	 * Sets the adds the edit authors display.
	 * 
	 * @param addEditAuthorsDisplay
	 *            the addEditAuthorsDisplay to set
	 */
	public void setAddEditAuthorsDisplay(AddEditAuthorsDisplay addEditAuthorsDisplay) {
		this.addEditAuthorsDisplay = addEditAuthorsDisplay;
	}
	
	/**
	 * Checks if is sub view.
	 * 
	 * @return the isSubView
	 */
	public boolean isSubView() {
		return isSubView;
	}
	
	/**
	 * Sets the sub view.
	 * 
	 * @param isSubView
	 *            the isSubView to set
	 */
	public void setSubView(boolean isSubView) {
		this.isSubView = isSubView;
	}
	
	/**
	 * Gets the measure xml model.
	 * 
	 * @return the measureXmlModel
	 */
	public MeasureXmlModel getMeasureXmlModel() {
		return measureXmlModel;
	}
	
	/**
	 * Sets the measure xml model.
	 * 
	 * @param measureXmlModel
	 *            the measureXmlModel to set
	 */
	public void setMeasureXmlModel(MeasureXmlModel measureXmlModel) {
		this.measureXmlModel = measureXmlModel;
	}
	
}
