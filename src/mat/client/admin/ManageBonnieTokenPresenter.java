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
		ManageBonnieTokenView manageBonnieTokenView = new ManageBonnieTokenView();
		ManageUsersDetailView manageUsersDetailView = new ManageUsersDetailView();
		ManageUserHistoryView manageUserHistoryView = new ManageUserHistoryView();
		manageUsersPresenter = new ManageUsersPresenter(manageBonnieTokenView, manageUsersDetailView, manageUserHistoryView);
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
