package mat.client.measure.measuredetails;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.MeasureDeleteEvent;
import mat.client.shared.ui.DeleteConfirmDialogBox;
import mat.shared.ConstantMessages;
import mat.client.event.MeasureSelectedEvent;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.measuredetails.components.MeasureDetailsComponent;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.measure.measuredetails.translate.ManageMeasureDetailModelMapper;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private String scoringType;
	private boolean isCompositeMeasure;
	private long lastRequestTime;
	
	public MeasureDetailsPresenter() {
		navigationPanel = new MeasureDetailsNavigation(scoringType, isCompositeMeasure);
		navigationPanel.setObserver(this);
		measureDetailsView = new MeasureDetailsView(new MeasureDetailsComponent(), MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
		addEventHandlers();
	}
	
	@Override
	public void beforeClosingDisplay() {
		this.scoringType = null;
		isCompositeMeasure = false;
	}

	@Override
	public void beforeDisplay() {
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
		DeleteConfirmDialogBox dialogBox = new DeleteConfirmDialogBox();
		dialogBox.showDeletionConfimationDialog(MatContext.get().getMessageDelegate().getDELETE_MEASURE_WARNING_MESSAGE());
		String password = dialogBox.getPasswordEntered();
		dialogBox.getConfirmButton().addClickHandler(event -> deleteMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinLoginId(), password));
	}
	
	private void deleteMeasure(String measureId, String loggedInUserId, String password) {
		MatContext.get().getMeasureService().deleteMeasure(MatContext.get().getCurrentMeasureId(), MatContext.get().getLoggedinLoginId(), password, deleteMeasureCallback());
	}
	
	public AsyncCallback<Void> deleteMeasureCallback() {
		return new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(false, caught.getMessage());
			}

			@Override
			public void onSuccess(Void result) {				
				MatContext.get().recordTransactionEvent(MatContext.get().getCurrentMeasureId(), null, "MEASURE_DELETE_EVENT", "Measure Successfully Deleted", ConstantMessages.DB_LOG);
				MatContext.get().setMeasureDeleted(true);
				fireBackToMeasureLibraryEvent();
				fireSuccessfullDeletionEvent(true, MatContext.get().getMessageDelegate().getMeasureDeletionSuccessMgs());	
			}
		};
	}
	
	private void fireSuccessfullDeletionEvent(boolean isSuccess, String message){
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
					MeasureDetailsComponent measureDetailsComponent = manageMeasureDetailModelMapper.getMeasureDetailsComponent();
					measureDetailsView = new MeasureDetailsView(measureDetailsComponent, MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
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