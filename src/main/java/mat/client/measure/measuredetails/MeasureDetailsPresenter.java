package mat.client.measure.measuredetails;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.Mat;
import mat.client.MatPresenter;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.shared.ConstantMessages;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;
import mat.shared.measure.measuredetails.models.MeasureDetailsModel;
import mat.shared.measure.measuredetails.translate.ManageMeasureDetailModelMapper;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private String scoringType;
	private boolean isCompositeMeasure;
	private boolean isMeasureEditable;
	private long lastRequestTime;
	private DeleteConfirmDialogBox dialogBox;
	MeasureDetailsModel measureDetailsModel;

	public MeasureDetailsPresenter() {
		navigationPanel = new MeasureDetailsNavigation(scoringType, isCompositeMeasure);
		navigationPanel.setObserver(this);
		measureDetailsModel = new MeasureDetailsModel();
		measureDetailsView = new MeasureDetailsView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		addEventHandlers();
	}
	
	@Override
	public void beforeClosingDisplay() {
		Mat.hideLoadingMessage();
		navigationPanel.updateState(MeasureDetailState.BLANK);
		this.scoringType = null;
		isCompositeMeasure = false;
		isMeasureEditable = true;
	}

	@Override
	public void beforeDisplay() {
		clearAlerts();
		setIsLoading();
		MatContext.get().getMeasureService().getMeasureDetailsAndLogRecentMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinUserId(),getAsyncCallBackForMeasureAndLogRecentMeasure());
	}

	private void setIsLoading() {
		measureDetailsView.clear();
		measureDetailsView.setReadOnly(true);
		Mat.showLoadingMessage();
	}

	@Override
	public Widget getWidget() {
		return measureDetailsView.getWidget();
	}

	@Override
	public void handleMenuItemClick(MatDetailItem menuItem) {
		measureDetailsView.buildDetailView(menuItem);
		measureDetailsView.setFocusOnHeader();
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
		navigationPanel.updateState(measureDetailsView.getState());
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

	private void displayMeasureDetailsView() {
		this.scoringType = MatContext.get().getCurrentMeasureScoringType();
		navigationPanel.buildNavigationMenu(scoringType, isCompositeMeasure);
		measureDetailsView.buildDetailView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		isMeasureEditable = !MatContext.get().getMeasureLockService().checkForEditPermission();
		measureDetailsView.setReadOnly(isMeasureEditable);
		measureDetailsView.getDeleteMeasureButton().setEnabled(isDeletable());
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		navigationPanel.updateState(measureDetailsView.getState());
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
		measureDetailsView.getErrorMessageAlert().clear();
		measureDetailsView.getErrorMessageAlert().clearAlert();
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
	
	private AsyncCallback<MeasureDetailsModel> getAsyncCallBackForMeasureAndLogRecentMeasure() {
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
					displayMeasureDetailsView();
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
		ConfirmationDialogBox confirmationDialog = measureDetailsView.getSaveConfirmation();
		if(confirmationDialog != null) {
			confirmationDialog.getYesButton().addClickHandler(event -> saveMeasureDetails());
			confirmationDialog.show();
			confirmationDialog.getYesButton().setFocus(true);
		} else {
			saveMeasureDetails();
		}
	}

	private void saveMeasureDetails() {
		measureDetailsView.getMeasureDetailsComponentModel().accept(measureDetailsModel);
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
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(SaveMeasureResult result) {
				// TODO Auto-generated method stub
				
			}
		};
	}
}