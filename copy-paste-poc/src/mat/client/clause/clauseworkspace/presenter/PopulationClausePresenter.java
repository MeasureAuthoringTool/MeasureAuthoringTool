package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * PopulationClausePresenter extends {@link XmlTreePresenter} and implements.
 * 
 * {@link MatPresenter}.
 */
public class PopulationClausePresenter extends XmlTreePresenter implements MatPresenter {
	/**
	 * Root Node for Population Tab.
	 */
	private static final String POPULATIONS_TAG = "populations";
	
	/** The panel. {@link SimplePanel} Instance. */
	private SimplePanel panel = new SimplePanel();
	/**
	 * MeasureService Instance.
	 */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	/**
	 * Default Constructor.
	 */
	public PopulationClausePresenter() {
		setRootNode(POPULATIONS_TAG);
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		panel.clear();
		loadXmlTree(panel,"populationsPanel");
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
