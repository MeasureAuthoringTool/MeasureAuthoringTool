package mat.client.measure.measuredetails;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;
import mat.client.shared.MatMenuItem;
import mat.client.shared.MeasureDetailsConstants;

public class MeasureDetailsPresenter implements MatPresenter, MeasureDetailsObserver {
	private MeasureDetailsView measureDetailsView;
	private MeasureDetailsNavigation navigationPanel;
	
	public MeasureDetailsPresenter() {
		//TODO set observer on navigationPanel
		navigationPanel = new MeasureDetailsNavigation();
		navigationPanel.setObserver(this);
		measureDetailsView = new MeasureDetailsView(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION, navigationPanel);
		navigationPanel.setActiveMenuItem(MeasureDetailsConstants.MeasureDetailsItems.GENERAL_MEASURE_INFORMATION);
	}
	
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		//TODO reset panel
	}

	@Override
	public void beforeDisplay() {
		// TODO Auto-generated method stub
		//TODO focus skip lists
	}

	@Override
	public Widget getWidget() {
		return measureDetailsView.getWidget();
	}

	@Override
	public void onMenuItemClicked(MatMenuItem menuItem) {
		GWT.log(menuItem.displayName() + " clicked!");
	}
}
