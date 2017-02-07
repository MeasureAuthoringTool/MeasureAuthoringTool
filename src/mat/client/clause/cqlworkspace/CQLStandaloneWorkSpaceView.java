package mat.client.clause.cqlworkspace;

import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.SpacerWidget;

public class CQLStandaloneWorkSpaceView implements CQLStandaloneWorkSpacePresenter.ViewDisplay{
	
	/** The main horizontal panel. */
	HorizontalPanel mainHPPanel = new HorizontalPanel();

	/** The main horizontal panel. */
	private VerticalPanel mainPanel = new VerticalPanel();

	/** The main v panel. */
	private VerticalPanel mainVPanel = new VerticalPanel();

	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	
	/** The right hand nav panel. */
	private VerticalPanel rightHandNavPanel = new VerticalPanel();

	/** The message panel. */
	private HorizontalPanel messagePanel = new HorizontalPanel();
	
	@Override
	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	@Override
	public void buildView() {
		mainFlowPanel.setWidth("700px");
		mainPanel.getElement().setId("CQLStandaloneWorkSpaceView.containerPanel");
		mainPanel.add(new SpacerWidget());
		messagePanel.setStyleName("marginLeft15px");
		mainPanel.add(messagePanel);
		mainPanel.add(mainFlowPanel);
		
	}
	@Override
	public Widget asWidget(){
		mainPanel.clear();
		buildView();
		return mainPanel;
	}

	@Override
	public VerticalPanel getMainVPanel() {
		return mainVPanel;
	}

	@Override
	public HorizontalPanel getMainHPanel() {
		return mainHPPanel;
	}

	@Override
	public FlowPanel getMainFlowPanel() {
		return mainFlowPanel;
	}

}
