package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import java.util.Map;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * StratificationClausePresenter extends {@link XmlTreePresenter} implements.
 * 
 * {@link MatPresenter}.
 */
public class StratificationClausePresenter extends XmlTreePresenter implements MatPresenter {
	/**
	 * Root Node for Stratification Tab.
	 */
	private static final String STRATIFICATION = "strata";
	
	/** The panel. {@link SimplePanel} Instance. */
	private SimplePanel panel = new SimplePanel();
	
	/** The Map of presenters. */
	private Map<Integer, MatPresenter> selectedTreeMap;
	
	/**
	 * MeasureService Instance.
	 */
	private MeasureServiceAsync service = MatContext.get().getMeasureService();
	/**
	 * Default Constructor.
	 */
	public StratificationClausePresenter() {
		setRootNode(STRATIFICATION);
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
		loadXmlTree(panel,"strataPanel");
	}
	/*
	 * (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return panel;
	}
	/**
	 * @return the selectedTreeMap
	 */
	public Map<Integer, MatPresenter> getSelectedTreeMap() {
		return selectedTreeMap;
	}
	/**
	 * @param selectedTreeMap the selectedTreeMap to set
	 */
	public void setSelectedTreeMap(Map<Integer, MatPresenter> selectedTreeMap) {
		this.selectedTreeMap = selectedTreeMap;
	}
	
}
