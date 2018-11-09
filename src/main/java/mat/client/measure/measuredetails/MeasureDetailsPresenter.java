package mat.client.measure.measuredetails;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.event.MeasureSelectedEvent;
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

	public MeasureDetailsPresenter() {
		navigationPanel = new MeasureDetailsNavigation(scoringType, isCompositeMeasure);
		navigationPanel.setObserver(this);
		GWT.log("now in the constructor");
		measureDetailsView = new MeasureDetailsView(new MeasureDetailsModel(), MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		addEventHandlers();
		
		measureDetailsView.getDeleteMeasureButton().addClickHandler(event -> handleDeleteMeasureButtonClick());
	}
	
	@Override
	public void beforeClosingDisplay() {
		this.scoringType = null;
		isCompositeMeasure = false;
	}

	@Override
	public void beforeDisplay() {
		clearAlerts();
		populateMeasureDetailsModelAndDisplayNavigationMenu();
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
		onDeleteMeasureButtonClick();
	}
	
	private void onDeleteMeasureButtonClick() {
		clearAlerts();
		dialogBox = new DeleteConfirmDialogBox();
		dialogBox.showDeletionConfimationDialog(MatContext.get().getMessageDelegate().getDELETE_MEASURE_WARNING_MESSAGE());
		dialogBox.getConfirmButton().addClickHandler(event -> deleteMeasure());
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

	private void populateMeasureDetailsModelAndDisplayNavigationMenu() {
		this.scoringType = MatContext.get().getCurrentMeasureScoringType();
		MatContext.get().getMeasureService().isCompositeMeasure(MatContext.get().getCurrentMeasureId(), isCompositeMeasureCallBack());
	}

	private AsyncCallback<Boolean> isCompositeMeasureCallBack() {
		return new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(Boolean isCompositeMeasure) {
				navigationPanel.buildNavigationMenu(scoringType, isCompositeMeasure);
			}
		};
	}
	

	private void addEventHandlers() {
		HandlerManager eventBus = MatContext.get().getEventBus();
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				MatContext.get().getMeasureService().getMeasureAndLogRecentMeasure(MatContext.get().getCurrentMeasureId(),
						MatContext.get().getLoggedinUserId(), getAsyncCallBackForMeasureAndLogRecentMeasure());
			}
		});
		measureDetailsView.getDeleteMeasureButton().addClickHandler(event -> handleDeleteMeasureButtonClick());
	}


	private void clearAlerts() {
		measureDetailsView.getErrorMessageAlert().clear();
		measureDetailsView.getErrorMessageAlert().clearAlert();
	}
	
	private void showErrorAlert(String message) {
		clearAlerts();
		measureDetailsView.getErrorMessageAlert().createAlert(message);
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
					MeasureDetailsModel measureDetailsComponent = manageMeasureDetailModelMapper.getMeasureDetailsComponent();
					measureDetailsView.buildDetailView(measureDetailsComponent, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
					MatContext.get().fireMeasureEditEvent();
				}
			}
			
			private void handleAsyncFailure(Throwable caught) {
				//TODO display an error message
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: " +caught.getLocalizedMessage(), 0);
			}
		};
	}
}