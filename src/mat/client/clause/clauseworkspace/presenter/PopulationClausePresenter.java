package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;

import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class PopulationClausePresenter extends XmlTreePresenter implements MatPresenter {

	SimplePanel panel = new SimplePanel();
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	private static final String POPULATIONS_TAG = "populations";

	public PopulationClausePresenter() {
		setRootNode(POPULATIONS_TAG);
	}

	@Override
	public void beforeDisplay() {
		panel.clear();
		loadXmlTree(panel);
	}

	@Override
	public void beforeClosingDisplay() {

	}

	@Override
	public Widget getWidget() {
		return panel;
	}
}
