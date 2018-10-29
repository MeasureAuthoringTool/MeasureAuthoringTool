package mat.client.measure.measuredetails;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.measure.measuredetails.navigation.MeasureDetailsNavigation;
import mat.client.shared.MatContext;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	private String scoringType;
	
	public MeasureDetailsPresenter() {
		navigationPanel = new MeasureDetailsNavigation(scoringType);
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
		this.scoringType = MatContext.get().getCurrentMeasureScoringType();
		navigationPanel.buildNavigationMenu(scoringType);
		//TODO focus skip lists
	}

	@Override
	public Widget getWidget() {
		return measureDetailsView.getWidget();
	}

	@Override
	public void onMenuItemClicked(MatDetailItem menuItem) {
		measureDetailsView.buildDetailView(menuItem);
		GWT.log(menuItem.displayName() + " clicked!");
	}
}
