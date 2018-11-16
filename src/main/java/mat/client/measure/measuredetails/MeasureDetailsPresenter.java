package mat.client.measure.measuredetails;

import org.gwtbootstrap3.extras.summernote.client.event.SummernoteKeyUpEvent;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageCompositeMeasureDetailModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.MeasureDetailsModel;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.translate.ManageMeasureDetailModelMapper;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.shared.ConstantMessages;
import mat.shared.error.AuthenticationException;
import mat.shared.error.measure.DeleteMeasureException;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private String scoringType;
	private boolean isCompositeMeasure;
	private long lastRequestTime;
	private DeleteConfirmDialogBox dialogBox;
	MeasureDetailsModel measureDetailsModel;

	public MeasureDetailsPresenter() {
		measureDetailsModel = new MeasureDetailsModel();
		navigationPanel = new MeasureDetailsNavigation(scoringType, isCompositeMeasure);
		navigationPanel.setObserver(this);
		measureDetailsView = new MeasureDetailsView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		addEventHandlers();
	}
	
	@Override
	public void beforeClosingDisplay() {
		navigationPanel.updateState(MeasureDetailState.BLANK);
		this.scoringType = null;
		isCompositeMeasure = false;
	}

	@Override
	public void beforeDisplay() {
		clearAlerts();
		populateMeasureDetailsModelAndDisplayMeasureDetailsView();
	}

	@Override
	public Widget getWidget() {
		return measureDetailsView.getWidget();
	}

	@Override
	public void onMenuItemClicked(MatDetailItem menuItem) {
		measureDetailsView.buildDetailView(menuItem);
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

	private void populateMeasureDetailsModelAndDisplayMeasureDetailsView() {
		this.scoringType = MatContext.get().getCurrentMeasureScoringType();
		MatContext.get().getMeasureService().isCompositeMeasure(MatContext.get().getCurrentMeasureId(), displayMeasureDetailsCallBack());
	}

	private AsyncCallback<Boolean> displayMeasureDetailsCallBack() {
		return new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Boolean isCompositeMeasure) {
				setCompositeMeasure(isCompositeMeasure);
				measureDetailsModel.setComposite(isCompositeMeasure);
				navigationPanel.buildNavigationMenu(scoringType, isCompositeMeasure);
				measureDetailsView.buildDetailView(measureDetailsModel, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
				measureDetailsView.setReadOnly(MatContext.get().getMeasureLockService().checkForEditPermission());
				measureDetailsView.getDeleteMeasureButton().setEnabled(isDeletable());
				navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
				navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
				navigationPanel.updateState(measureDetailsView.getState());
			}
		};
	}

	private void addEventHandlers() {
		HandlerManager eventBus = MatContext.get().getEventBus();
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				MatContext.get().getMeasureService().isCompositeMeasure(MatContext.get().getCurrentMeasureId(), getMeasureCallBack());
			}
		});
		measureDetailsView.getDeleteMeasureButton().addClickHandler(event -> handleDeleteMeasureButtonClick());
	}

	protected AsyncCallback<Boolean> getMeasureCallBack() {
		return new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Boolean isCompositeMeasure) {
				setCompositeMeasure(isCompositeMeasure);
				if(isCompositeMeasure()) {
					MatContext.get().getMeasureService().getCompositeMeasure(MatContext.get().getCurrentMeasureId(), getAsyncCallBackForCompositeMeasureAndLogRecentMeasure());
				} else {
					MatContext.get().getMeasureService().getMeasureAndLogRecentMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinUserId(),getAsyncCallBackForMeasureAndLogRecentMeasure());
				}
			}
		};
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
	
	private AsyncCallback<ManageMeasureDetailModel> getAsyncCallBackForMeasureAndLogRecentMeasure() {
		return new AsyncCallback<ManageMeasureDetailModel>() {
			final long callbackRequestTime = lastRequestTime;
			@Override
			public void onFailure(Throwable caught) {
				handleAsyncFailure(caught);
			}
			@Override
			public void onSuccess(ManageMeasureDetailModel result) {
				handleAsyncSuccess(result, callbackRequestTime);
			}
			
			private void handleAsyncSuccess(ManageMeasureDetailModel result, long callbackRequestTime) {
				if (callbackRequestTime == lastRequestTime) {
					ManageMeasureDetailModelMapper manageMeasureDetailModelMapper = new ManageMeasureDetailModelMapper(result);
					measureDetailsModel = manageMeasureDetailModelMapper.getMeasureDetailsModel(isCompositeMeasure);					
					MatContext.get().fireMeasureEditEvent();
				}
			}
		};
	}
	
	private void handleAsyncFailure(Throwable caught) {
		showErrorAlert(caught.getMessage());
		MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " +caught.getLocalizedMessage(), 0);
	}
	
	public boolean isCompositeMeasure() {
		return isCompositeMeasure;
	}

	public void setCompositeMeasure(boolean isCompositeMeasure) {
		this.isCompositeMeasure = isCompositeMeasure;
	}
}