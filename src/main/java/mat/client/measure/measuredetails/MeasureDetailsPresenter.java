package mat.client.measure.measuredetails;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private String scoringType;
	private boolean isCompositeMeasure;
	
	public MeasureDetailsPresenter() {
		navigationPanel = new MeasureDetailsNavigation(scoringType, isCompositeMeasure);
		navigationPanel.setObserver(this);
		measureDetailsView = new MeasureDetailsView(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
	}
	
	@Override
	public void beforeClosingDisplay() {
		this.scoringType = null;
		//TODO reset panel
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
	
	private void populateMeasureDetailsModelAndDisplayNavigationMenu() {
		this.scoringType = MatContext.get().getCurrentMeasureScoringType();
		MatContext.get().getMeasureService().isCompositeMeasure(MatContext.get().getCurrentMeasureId(), isCompositeMeasureCallBack());
	}

	private AsyncCallback<Boolean> isCompositeMeasureCallBack() {
		return new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO handle error here
				
			}

			@Override
			public void onSuccess(Boolean isCompositeMeasure) {
				navigationPanel.buildNavigationMenu(scoringType, isCompositeMeasure);
			}
		};
	}
}
