package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * MeasureObsClausePresenter extends {@link XmlTreePresenter} and implements.
 * 
 * {@link MatPresenter}.
 */
public class MeasureObsClausePresenter extends XmlTreePresenter implements MatPresenter {
	/**
	 * Root Name for Presenter.
	 */
	private static final String MEASURE_OBS = "measureObservations";
	/**
	 * Panel Instance.
	 */
	private SimplePanel panel = new SimplePanel();
	/**
	 * MeasureService Instance.
	 */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	/**
	 * Default Constructor.
	 */
	public MeasureObsClausePresenter() {
		setRootNode(MEASURE_OBS);
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		panel.clear();
		loadXmlTree(panel,"measureObservationsPanel");
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
}
