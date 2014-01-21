package mat.client.clause.clauseworkspace.presenter;

import mat.client.MatPresenter;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.shared.MatContext;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClauseWorkSpacePresenter extends XmlTreePresenter implements MatPresenter {
	/** The simplepanel. */
	private SimplePanel simplepanel = new SimplePanel();
	
	/** The flow panel. */
	FlowPanel flowPanel = new FlowPanel();
	
	/** The service. */
	MeasureServiceAsync service = MatContext.get().getMeasureService();
	
	public ClauseWorkSpacePresenter() {
		simplepanel.setStyleName("contentPanel");
		simplepanel.add(flowPanel);
	}
	@Override
	public void beforeClosingDisplay() {
	}
	
	@Override
	public void beforeDisplay() {
	}
	
	@Override
	public Widget getWidget() {
		return simplepanel;
	}
}
