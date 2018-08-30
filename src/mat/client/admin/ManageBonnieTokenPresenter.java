package mat.client.admin;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.MatPresenter;

public class ManageBonnieTokenPresenter implements MatPresenter {
	private SimplePanel adminManageBonniePanel = new SimplePanel();
	private FlowPanel fp = new FlowPanel();
	private ManageUsersPresenter manageUsersPresenter;
	
	public ManageBonnieTokenPresenter() {
		ManageBonnieTokenView musd = new ManageBonnieTokenView();
		ManageUsersDetailView mudd = new ManageUsersDetailView();
		ManageUserHistoryView muhd = new ManageUserHistoryView();
		manageUsersPresenter = new ManageUsersPresenter(musd, mudd, muhd);
	}

	@Override
	public void beforeClosingDisplay() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDisplay() {
		fp.add(manageUsersPresenter.getWidget());
		adminManageBonniePanel.clear();
		adminManageBonniePanel.add(fp);
		manageUsersPresenter.beforeDisplay();
	}

	@Override
	public Widget getWidget() {
		return adminManageBonniePanel;
	}
}
