package mat.client.measure.measuredetails;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ComponentMeasureDisplay;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.navigation.MeasureDetailsAnchorListItem;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.views.ReferencesView;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;
import mat.client.shared.MessagePanel;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.shared.CompositeMeasureValidationResult;
import mat.shared.ConstantMessages;
import mat.shared.StringUtility;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.models.MeasureDetailsRichTextAbstractModel;
import mat.shared.measure.measuredetails.models.MeasureStewardDeveloperModel;
import mat.shared.measure.measuredetails.models.ReferencesModel;
import mat.shared.measure.measuredetails.translate.ManageMeasureDetailModelMapper;
import mat.shared.measure.measuredetails.validate.GeneralInformationValidator;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private ComponentMeasureDisplay componentMeasureDisplay;
	private String scoringType;
	private boolean isCompositeMeasure;
	private boolean isReadOnly;
	private boolean isPatientBased;
	private long lastRequestTime;
	private DeleteConfirmDialogBox dialogBox;
	private ManageCompositeMeasureDetailModel currentCompositeMeasureDetails = new ManageCompositeMeasureDetailModel();
	
	private SimplePanel panel;
	
	MeasureDetailsModel measureDetailsModel;
	
	Boolean showCompositeEdit = false;

	public MeasureDetailsPresenter() {
		panel = new SimplePanel();
		navigationPanel = new MeasureDetailsNavigation(scoringType, isPatientBased, isCompositeMeasure);
		navigationPanel.setObserver(this);
		measureDetailsModel = new MeasureDetailsModel();
		measureDetailsView = new MeasureDetailsView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel, this);
		measureDetailsView.setMeasureDetailsObserver(this);
		measureDetailsView.getWidget().addStyleName("measureDetailsSwitchPanel");
		componentMeasureDisplay = new ComponentMeasureDisplay();
		componentMeasureDisplay.getBackButton().setVisible(false);
		componentMeasureDisplay.getBreadCrumbPanel().setVisible(true);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		addEventHandlers();
	}
	
	@Override
	public void beforeClosingDisplay() {
		Mat.hideLoadingMessage();
		navigationPanel.updateState(MeasureDetailState.BLANK);
		this.scoringType = null;
		isPatientBased = false;
		isCompositeMeasure = false;
		isReadOnly = false;
	}

	@Override
	public void beforeDisplay() {
		clearAlerts();
		setIsLoading();
		getDataBaseInfomation(false, false);
	}

	private void getDataBaseInfomation(boolean goToComposite, boolean displaySuccessMessage) {
		MatContext.get().getMeasureService().getMeasureDetailsAndLogRecentMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinUserId(),getAsyncCallBackForMeasureAndLogRecentMeasure(goToComposite, displaySuccessMessage));
	}
	
	private void setIsLoading() {
		measureDetailsView.clear();
		measureDetailsView.setReadOnly(true);
		Mat.showLoadingMessage();
	}

	@Override
	public Widget getWidget() {
		return panel;
	}

	public MeasureDetailsView getView() {
		return measureDetailsView;
	}
	
	private void displayDetails(Boolean goToComposite, Boolean displaySuccessMessage) {
		panel.clear();
		panel.add(measureDetailsView.getWidget());
		showCompositeEdit = false;
		if(goToComposite) {
			handleMenuItemClick(MeasureDetailsItems.COMPONENT_MEASURES);
		}
		if(displaySuccessMessage) {
			measureDetailsView.displaySuccessMessage("Component Measures have been successfully updated");
		}
	}
	
	private void displayEditComposite(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		showCompositeEdit = true;
		this.currentCompositeMeasureDetails = manageCompositeMeasureDetailModel;
		panel.clear();
		componentMeasureDisplay.getComponentMeasureSearch().clearFields(false);
		componentMeasureDisplay.getMessagePanel().clearAlerts();
		componentMeasureDisplay.getComponentMeasureSearch().setAliasMapping(manageCompositeMeasureDetailModel.getAliasMapping());
		componentMeasureDisplay.getComponentMeasureSearch().setAppliedComponentMeasuresList(manageCompositeMeasureDetailModel.getAppliedComponentMeasures());
		componentMeasureDisplay.getComponentMeasureSearch().buildSearch();
		
		panel.add(componentMeasureDisplay.asWidget());
		componentMeasureDisplay.setComponentBusy(false);
	}
	
	@Override
	public void handleMenuItemClick(MatDetailItem menuItem) {
		clearAlerts();
		if(isDirty()) {
			measureDetailsView.clearAlerts();
			measureDetailsView.displayDirtyCheck();
			measureDetailsView.getMessagePanel().getWarningConfirmationNoButton().addClickHandler(event -> handleWarningConfirmationNoClick());
			measureDetailsView.getMessagePanel().getWarningConfirmationYesButton().addClickHandler(event -> handleWarningConfirmationYesClick(menuItem));
			measureDetailsView.getMessagePanel().getWarningConfirmationYesButton().setFocus(true);
		} else {
			navigateTo(menuItem);
		}
	}
	
	@Override
	public void handleEditCompositeMeasures(ManageCompositeMeasureDetailModel manageCompositeMeasureDetailModel) {
		if(!isReadOnly) {
			displayEditComposite(manageCompositeMeasureDetailModel);
		}
	}
	
	private void navigateTo(MatDetailItem menuItem) {		
		measureDetailsView.buildDetailView(menuItem, this);
		navigationPanel.setActiveMenuItem(menuItem);
		measureDetailsView.setFocusOnHeader();
	}
	
	private void handleWarningConfirmationYesClick(MatDetailItem menuItem) {
		clearAlerts();
		navigateTo(menuItem);
	}

	private void handleWarningConfirmationNoClick() {
		clearAlerts();
	}

	public boolean isDirty() {
		boolean isDirty = false;
		if(!isReadOnly) {
			if(showCompositeEdit) {
				isDirty = componentMeasureDisplay.getComponentMeasureSearch().isDirty();
			} else if( measureDetailsView.getMeasureDetailsComponentModel() != null) {
				isDirty = measureDetailsView.getMeasureDetailsComponentModel().isDirty(measureDetailsModel);
			}
			if(!isDirty && measureDetailsView.getCurrentMeasureDetail() == MeasureDetailsItems.REFERENCES) {
				ReferencesView referencesView = (ReferencesView) measureDetailsView.getComponentDetailView();
				isDirty = referencesView.isEditorDirty();
			}
		}

		return isDirty; 
	}
	
	@Override
	public void handleDeleteMeasureButtonClick() {
		if(isDeletable()) {
			clearAlerts();
			dialogBox = new DeleteConfirmDialogBox();
			dialogBox.showDeletionConfimationDialog(MatContext.get().getMessageDelegate().getDELETE_MEASURE_WARNING_MESSAGE());
			dialogBox.getConfirmButton().addClickHandler(event -> deleteMeasure());
		}
	}
	
	@Override
	public void handleStateChanged() {
		updateNavPillStates();
	}
	
	private void deleteMeasure() {
		MatContext.get().getMeasureService().deleteMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinLoginId(), dialogBox.getPasswordEntered(), deleteMeasureCallback());
	}
	
	public AsyncCallback<Void> deleteMeasureCallback() {
		return new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if(caught instanceof AuthenticationException) {
					dialogBox.setMessage(caught.getMessage());
					dialogBox.getPassword().setText("");
				} else if(caught instanceof DeleteMeasureException) {
					showErrorAlert(caught.getMessage());
					dialogBox.closeDialogBox();
				} else {
					showErrorAlert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
					dialogBox.closeDialogBox();
				}
			}

			@Override
			public void onSuccess(Void result) {				
				dialogBox.closeDialogBox();
				MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null, "MEASURE_DELETE_EVENT", "Measure Successfully Deleted", ConstantMessages.DB_LOG);
				MatContext.get().setMeasureDeleted(true);
				fireBackToMeasureLibraryEvent();
				fireMeasureDeletionEvent(true, MatContext.get().getMessageDelegate().getMeasureDeletionSuccessMgs());	
			}
		};
	}
	
	private void fireMeasureDeletionEvent(boolean isSuccess, String message){
		MeasureDeleteEvent deleteEvent = new MeasureDeleteEvent(isSuccess, message);
		MatContext.get().getEventBus().fireEvent(deleteEvent);
	}
	
	private void fireBackToMeasureLibraryEvent() {
		BackToMeasureLibraryPage backToMeasureLibraryPage = new BackToMeasureLibraryPage();
		MatContext.get().getEventBus().fireEvent(backToMeasureLibraryPage);
	}

	private void displayMeasureDetailsView(Boolean goToComposite, Boolean displaySuccessMessage) {
		this.scoringType = measureDetailsModel.getGeneralInformationModel().getScoringMethod();
		this.isPatientBased = measureDetailsModel.getGeneralInformationModel().isPatientBased();
		navigationPanel.buildNavigationMenu(scoringType, isPatientBased, isCompositeMeasure);
		measureDetailsView.buildDetailView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel, this);
		isReadOnly = !MatContext.get().getMeasureLockService().checkForEditPermission();
		measureDetailsView.setReadOnly(isReadOnly);
		measureDetailsView.getDeleteMeasureButton().setEnabled(isDeletable());
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		updateNavPillStates();
		displayDetails(goToComposite, displaySuccessMessage);
	}


	private void addEventHandlers() {
		HandlerManager eventBus = MatContext.get().getEventBus();
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				MatContext.get().fireMeasureEditEvent();
			}
		});
		measureDetailsView.getDeleteMeasureButton().addClickHandler(event -> handleDeleteMeasureButtonClick());
		measureDetailsView.getSaveButton().addClickHandler(event -> handleSaveButtonClick());
		componentMeasureDisplay.getBackButton().addClickHandler(event -> displayCompositeMeasuresOnMeasureDetails(false));
		componentMeasureDisplay.getCancelButton().addClickHandler(event -> displayCompositeMeasuresOnMeasureDetails(false));
		componentMeasureDisplay.getSaveButton().addClickHandler(event -> saveCompositeMeasures());
	}

	
	
	private void saveCompositeMeasures() {

		componentMeasureDisplay.setComponentBusy(true);
		updateCompositeDetailsFromComponentMeasureDisplay();
		
		MatContext.get().getMeasureService().validateCompositeMeasure(currentCompositeMeasureDetails, new AsyncCallback<CompositeMeasureValidationResult>() {

			@Override
			public void onFailure(Throwable caught) {
				componentMeasureDisplay.setComponentBusy(false);
			}

			@Override
			public void onSuccess(CompositeMeasureValidationResult result) {
				currentCompositeMeasureDetails = result.getModel();
				if(isValidCompositeMeasureForSave(result.getMessages())){
					saveCompositeMeasure();
				}
			}
		});
	}
		
		
	private boolean isValidCompositeMeasureForSave(List<String> message) {
		boolean valid = message.size() == 0;
		componentMeasureDisplay.getSuccessMessage().clearAlert();
		if(!valid) {
			String errorMessage = "";
			if(message.size() > 0) {
				errorMessage = message.get(0);
				componentMeasureDisplay.setComponentBusy(false);
			}
			componentMeasureDisplay.getErrorMessageDisplay().createAlert(errorMessage);
		} else {
			componentMeasureDisplay.getErrorMessageDisplay().clearAlert();
		}
		return valid;
	}
	
	private void saveCompositeMeasure() {
		MatContext.get().getMeasureService().saveCompositeMeasure(currentCompositeMeasureDetails, new AsyncCallback<SaveMeasureResult>() {

			@Override
			public void onFailure(Throwable caught) {
				componentMeasureDisplay.getErrorMessageDisplay().createAlert(caught.getLocalizedMessage());
				componentMeasureDisplay.setComponentBusy(false);
			}

			@Override
			public void onSuccess(SaveMeasureResult result) {
				componentMeasureDisplay.setComponentBusy(false);
				displayCompositeMeasuresOnMeasureDetails(true);
			}
			
		});
	}
	
	private void updateCompositeDetailsFromComponentMeasureDisplay() {
		currentCompositeMeasureDetails.setAppliedComponentMeasures(componentMeasureDisplay.getComponentMeasureSearch().getAppliedComponentMeasuresList());
		currentCompositeMeasureDetails.setAliasMapping(componentMeasureDisplay.getComponentMeasureSearch().getAliasMapping());
	}

	private void displayCompositeMeasuresOnMeasureDetails(Boolean displaySuccessMessage) {
		componentMeasureDisplay.setComponentBusy(true);
		componentMeasureDisplay.getMessagePanel().clearAlerts();
		componentMeasureDisplay.getComponentMeasureSearch().clearFields(false);
		getDataBaseInfomation(true, displaySuccessMessage);
	}

	protected AsyncCallback<ManageCompositeMeasureDetailModel> getAsyncCallBackForCompositeMeasureAndLogRecentMeasure() {
		return new AsyncCallback<ManageCompositeMeasureDetailModel>() {
			final long callbackRequestTime = lastRequestTime;
			@Override
			public void onFailure(Throwable caught) {
				handleAsyncFailure(caught);
			}
			
			@Override
			public void onSuccess(ManageCompositeMeasureDetailModel result) {
				if (callbackRequestTime == lastRequestTime) {
					ManageMeasureDetailModelMapper manageMeasureDetailModelMapper = new ManageMeasureDetailModelMapper(result);
					measureDetailsModel = manageMeasureDetailModelMapper.getMeasureDetailsModel(isCompositeMeasure);					
					MatContext.get().fireMeasureEditEvent();
				}
			}
		};
	}

	private void clearAlerts() {
		measureDetailsView.getMessagePanel().clearAlerts();
	}
	
	private void showErrorAlert(String message) {
		clearAlerts();
		measureDetailsView.getErrorMessageAlert().createAlert(message);
	}	
	
	private boolean isDeletable() {
		return isMeasureOwner() && !MatContext.get().isCurrentMeasureLocked();
	}
	
	private boolean isMeasureOwner() {
		return measureDetailsModel.getOwnerUserId() == MatContext.get().getLoggedinUserId();
	}
	
	private AsyncCallback<MeasureDetailsModel> getAsyncCallBackForMeasureAndLogRecentMeasure(Boolean goToComposite, Boolean displaySuccessMessage) {
		return new AsyncCallback<MeasureDetailsModel>() {
			final long callbackRequestTime = lastRequestTime;
			@Override
			public void onFailure(Throwable caught) {
				handleAsyncFailure(caught);
			}
			@Override
			public void onSuccess(MeasureDetailsModel result) {
				setCompositeMeasure(result.isComposite());
				handleAsyncSuccess(result, callbackRequestTime);
			}
			
			private void handleAsyncSuccess(MeasureDetailsModel result, long callbackRequestTime) {
				Mat.hideLoadingMessage();
				if (callbackRequestTime == lastRequestTime) {
					measureDetailsModel = result;
					displayMeasureDetailsView(goToComposite, displaySuccessMessage);
				}
			}
		};
	}
	
	private void handleAsyncFailure(Throwable caught) {
		Mat.hideLoadingMessage();
		showErrorAlert(caught.getMessage());
		MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " +caught.getLocalizedMessage(), 0);
	}
	
	public boolean isCompositeMeasure() {
		return isCompositeMeasure;
	}

	public void setCompositeMeasure(boolean isCompositeMeasure) {
		this.isCompositeMeasure = isCompositeMeasure;
	}

	@Override
	public void handleSaveButtonClick() {
		if(!isReadOnly) {
			if(measureDetailsView.getCurrentMeasureDetail() == MeasureDetailsItems.REFERENCES) {
				ReferencesView referencesView = (ReferencesView) measureDetailsView.getComponentDetailView();
				referencesView.saveModel();
			} else {
				List<String> validationErrors = measureDetailsView.getMeasureDetailsComponentModel().validateModel(measureDetailsModel);
				if(validationErrors == null || validationErrors.isEmpty()) {
					ConfirmationDialogBox confirmationDialog = measureDetailsView.getSaveConfirmation();
					if(confirmationDialog != null) {
						showSaveConfirmationDialog(confirmationDialog);
					} else {
						saveMeasureDetails();
					}
				} else {
					String validationErrorMessage = validationErrors.stream().collect(Collectors.joining("\n"));
					measureDetailsView.displayErrorMessage(validationErrorMessage);
				}
			}
		}
	}

	private void showSaveConfirmationDialog(ConfirmationDialogBox confirmationDialog) {
		confirmationDialog.getYesButton().addClickHandler(event -> saveMeasureDetails());
		confirmationDialog.getNoButton().addClickHandler(event -> measureDetailsView.resetForm());
		confirmationDialog.show();
		confirmationDialog.getYesButton().setFocus(true);
	}

	public void saveMeasureDetails() {
		measureDetailsView.getComponentDetailView().getObserver().handleValueChanged();
		measureDetailsView.getMeasureDetailsComponentModel().update(measureDetailsModel);
		ManageMeasureDetailModelMapper mapper = new ManageMeasureDetailModelMapper(measureDetailsModel);
		ManageMeasureDetailModel manageMeasureDetails = mapper.convertMeasureDetailsToManageMeasureDetailModel();
		if(measureDetailsModel.isComposite()) {
			MatContext.get().getMeasureService().saveCompositeMeasure((ManageCompositeMeasureDetailModel) manageMeasureDetails, getSaveCallback());
		} else {
			MatContext.get().getMeasureService().saveMeasureDetails(manageMeasureDetails, getSaveCallback());
		}
	}

	private AsyncCallback<SaveMeasureResult> getSaveCallback() {
		return new AsyncCallback<SaveMeasureResult>() {
			@Override
			public void onFailure(Throwable caught) {
				measureDetailsView.displayErrorMessage(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(SaveMeasureResult result) {
				MatDetailItem activeMenuItem = navigationPanel.getActiveMenuItem();
				scoringType = measureDetailsModel.getGeneralInformationModel().getScoringMethod();
				isPatientBased = measureDetailsModel.getGeneralInformationModel().isPatientBased();
				MatContext.get().setCurrentMeasureScoringType(scoringType);
				navigationPanel.buildNavigationMenu(scoringType, isPatientBased, isCompositeMeasure);
				measureDetailsView.buildDetailView(measureDetailsModel, navigationPanel.getActiveMenuItem(), navigationPanel, getMeasureDetailsObserver());
				isReadOnly = !MatContext.get().getMeasureLockService().checkForEditPermission();
				measureDetailsView.setReadOnly(isReadOnly);
				measureDetailsView.getDeleteMeasureButton().setEnabled(isDeletable());
				measureDetailsView.displaySuccessMessage("Changes for the " +  measureDetailsView.getCurrentMeasureDetail().displayName() + " section have been successfully saved.");
				handleStateChanged();
				navigationPanel.setActiveMenuItem(activeMenuItem);
			}
		};
	}
	
	private MeasureDetailsObserver getMeasureDetailsObserver() {
		return this;
	}
	
	private void updateNavPillStates() {
		navigationPanel.getMenuItemMap().keySet().forEach(k -> {
			MeasureDetailState navPillState = getStateForModelByKey(k);
			MeasureDetailsAnchorListItem anchorListItem = navigationPanel.getMenuItemMap().get(k);
			if(anchorListItem != null) {
				anchorListItem.setState(navPillState);
			}
			
		});
	}

	private MeasureDetailState getStateForModelByKey(MatDetailItem k) {
		if(k instanceof MeasureDetailsItems) {
			switch((MeasureDetailsItems) k) {
			case GENERAL_MEASURE_INFORMATION:
				return getGeneralInformationState(measureDetailsModel);
			case STEWARD:
				return getMeasureStewardAndDeveloperState(measureDetailsModel.getMeasureStewardDeveloperModel());
			case DESCRIPTION:
				return getRichTextEditableTabState(measureDetailsModel.getDescriptionModel());
			case COPYRIGHT:
				return getRichTextEditableTabState(measureDetailsModel.getCopyrightModel());
			case DISCLAIMER:
				return getRichTextEditableTabState(measureDetailsModel.getDisclaimerModel());
			case MEASURE_TYPE:
				return getMeasureTypeState(measureDetailsModel);
			case STRATIFICATION:
				return getRichTextEditableTabState(measureDetailsModel.getStratificationModel());
			case RISK_ADJUSTMENT:
				return getRichTextEditableTabState(measureDetailsModel.getRiskAdjustmentModel());
			case RATE_AGGREGATION:
				return getRichTextEditableTabState(measureDetailsModel.getRateAggregationModel());
			case RATIONALE:
				return getRichTextEditableTabState(measureDetailsModel.getRationaleModel());
			case CLINICAL_RECOMMENDATION:
				return getRichTextEditableTabState(measureDetailsModel.getClinicalRecommendationModel());
			case IMPROVEMENT_NOTATION:
				return getRichTextEditableTabState(measureDetailsModel.getImprovementNotationModel());
			case DEFINITION:
				return getRichTextEditableTabState(measureDetailsModel.getDefinitionModel());
			case GUIDANCE:
				return getRichTextEditableTabState(measureDetailsModel.getGuidanceModel());
			case TRANSMISSION_FORMAT:
				return getRichTextEditableTabState(measureDetailsModel.getTransmissionFormatModel());
			case SUPPLEMENTAL_DATA_ELEMENTS:
				return getRichTextEditableTabState(measureDetailsModel.getSupplementalDataElementsModel());
			case MEASURE_SET:
				return getRichTextEditableTabState(measureDetailsModel.getMeasureSetModel());
			case POPULATIONS:
				return getPopulationsState(measureDetailsModel);
			case REFERENCES:
				return getReferencesState(measureDetailsModel.getReferencesModel());
			case COMPONENT_MEASURES:
				return getComponentMeasureState(measureDetailsModel.getCompositeMeasureDetailModel());
			default: 
				return MeasureDetailState.BLANK;
			}
		} else if (k instanceof PopulationItems) {
			switch((PopulationItems) k) {
			case INITIAL_POPULATION:
				return getRichTextEditableTabState(measureDetailsModel.getInitialPopulationModel());
			case MEASURE_POPULATION:
				return getRichTextEditableTabState(measureDetailsModel.getMeasurePopulationModel());
			case MEASURE_POPULATION_EXCLUSIONS:
				return getRichTextEditableTabState(measureDetailsModel.getMeasurePopulationExclusionsModel());
			case DENOMINATOR:
				return getRichTextEditableTabState(measureDetailsModel.getDenominatorModel());
			case DENOMINATOR_EXCLUSIONS:
				return getRichTextEditableTabState(measureDetailsModel.getDenominatorExclusionsModel());
			case NUMERATOR:
				return getRichTextEditableTabState(measureDetailsModel.getNumeratorModel());
			case NUMERATOR_EXCLUSIONS:
				return getRichTextEditableTabState(measureDetailsModel.getNumeratorExclusionsModel());
			case DENOMINATOR_EXCEPTIONS:
				return getRichTextEditableTabState(measureDetailsModel.getDenominatorExceptionsModel());
			case MEASURE_OBSERVATIONS:
				return getRichTextEditableTabState(measureDetailsModel.getMeasureObservationsModel());
			
			default: 
				return MeasureDetailState.BLANK;
			}		
		}
		
		return MeasureDetailState.BLANK;
	}
	
	private MeasureDetailState getComponentMeasureState(ManageCompositeMeasureDetailModel compositeMeasureDetailModel) {
		if(compositeMeasureDetailModel.getAppliedComponentMeasures().size() >= 2) {
			return MeasureDetailState.COMPLETE;
		} else {
			return MeasureDetailState.BLANK;
		}
	}

	private MeasureDetailState getReferencesState(ReferencesModel referencesModel) {
		if(referencesModel.getReferences() != null && !referencesModel.getReferences().isEmpty()) {
			return MeasureDetailState.COMPLETE;
		}
		return MeasureDetailState.BLANK;
	}

	private MeasureDetailState getMeasureStewardAndDeveloperState(MeasureStewardDeveloperModel model) {
		if (StringUtility.isEmptyOrNull(model.getStewardId()) && model.getSelectedDeveloperList().isEmpty()) {
			return MeasureDetailState.BLANK;
		} else if (StringUtility.isNotBlank(model.getStewardId()) && !model.getSelectedDeveloperList().isEmpty()){
			return MeasureDetailState.COMPLETE;
		} else {
			return MeasureDetailState.INCOMPLETE;
		}
	}
	
	private MeasureDetailState getGeneralInformationState(MeasureDetailsModel measureDetailsModel) {
		GeneralInformationValidator modelValidator = new GeneralInformationValidator();
		return modelValidator.getModelState(measureDetailsModel.getGeneralInformationModel(), measureDetailsModel.isComposite());
		
	}

	private MeasureDetailState getMeasureTypeState(MeasureDetailsModel model) {
		
		if(model.getMeasureTypeModeModel().getMeasureTypeList() == null) {
			return MeasureDetailState.BLANK;
		}
		
		// composite measures always have at least one measure type selected. So we should only show a green checkmark
		// if the composite measure has more than two in its measure type list. 
		if(model.isComposite()) {
			if(model.getMeasureTypeModeModel().getMeasureTypeList().size() > 1) {
				return MeasureDetailState.COMPLETE;
			}
		} else {
			if(model.getMeasureTypeModeModel().getMeasureTypeList().size() > 0) {
				return MeasureDetailState.COMPLETE;
			}
		}
		
		return MeasureDetailState.BLANK;
	}

	private MeasureDetailState getRichTextEditableTabState(MeasureDetailsRichTextAbstractModel model) {
		if(model != null) {
			if(model.getPlainText() == null || model.getPlainText().isEmpty()) {
				return MeasureDetailState.BLANK;
			} else {
				return MeasureDetailState.COMPLETE;
			}
		}
		return MeasureDetailState.BLANK;
	}
	
	private MeasureDetailState getPopulationsState(MeasureDetailsModel measureDetailsModel) {
		List<MeasureDetailsRichTextAbstractModel> applicableModels = new ArrayList<>();
		if(scoringType.equals(MeasureDetailsConstants.getCohort())) {
			applicableModels.add(measureDetailsModel.getInitialPopulationModel());
		} else if (scoringType.equals(MeasureDetailsConstants.getContinuousVariable())) {
			applicableModels.add(measureDetailsModel.getInitialPopulationModel());
			applicableModels.add(measureDetailsModel.getMeasurePopulationModel());
			applicableModels.add(measureDetailsModel.getMeasurePopulationExclusionsModel());
			applicableModels.add(measureDetailsModel.getMeasureObservationsModel());
		} else if(scoringType.equals(MeasureDetailsConstants.getProportion())) {
			applicableModels.add(measureDetailsModel.getInitialPopulationModel());
			applicableModels.add(measureDetailsModel.getDenominatorModel());
			applicableModels.add(measureDetailsModel.getDenominatorExclusionsModel());
			applicableModels.add(measureDetailsModel.getNumeratorModel());
			applicableModels.add(measureDetailsModel.getNumeratorExclusionsModel());
			applicableModels.add(measureDetailsModel.getDenominatorExceptionsModel());
		} else if (scoringType.equals(MeasureDetailsConstants.getRatio())) {
			applicableModels.add(measureDetailsModel.getInitialPopulationModel());
			applicableModels.add(measureDetailsModel.getDenominatorModel());
			applicableModels.add(measureDetailsModel.getDenominatorExclusionsModel());
			applicableModels.add(measureDetailsModel.getNumeratorModel());
			applicableModels.add(measureDetailsModel.getNumeratorExclusionsModel());
		}
		
		return calculateStateOffOfList(applicableModels);
	}
	
	private MeasureDetailState calculateStateOffOfList(List<MeasureDetailsRichTextAbstractModel> applicableModels) {
		int completedPopulationCount = 0;
		for(MeasureDetailsRichTextAbstractModel model : applicableModels) {
			if(getRichTextEditableTabState(model) == MeasureDetailState.COMPLETE) {
				completedPopulationCount++;
			}
		}
		
		if(completedPopulationCount == 0) {
			return MeasureDetailState.BLANK;
		} else if(completedPopulationCount == applicableModels.size()) {
			return MeasureDetailState.COMPLETE;
		} else {
			return MeasureDetailState.INCOMPLETE;
		}
	}

	public MessagePanel getMessagePanel() {
		if(showCompositeEdit) {
			return componentMeasureDisplay.getMessagePanel();
		} else {
			return measureDetailsView.getMessagePanel();
		}
	}
}
