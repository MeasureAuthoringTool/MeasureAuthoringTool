package mat.client.admin;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.MatPresenter;
import mat.client.TabObserver;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;

import java.util.ArrayList;
import java.util.List;
/** The Class ManageAdminPresenter. */
public class ManageAdminPresenter implements MatPresenter, TabObserver {
	private List<MatPresenter> presenterList;
	private final String ADMIN_ACCOUNT_TAB = "adminAccountTab";
	/** The Admin content widget. */
	private SimplePanel adminContentWidget = new SimplePanel();
	/** Flow Panel. */
	private FlowPanel fp = new FlowPanel();
	/** The Manage Organization Presenter. */
	private ManageOrganizationPresenter manageOrganizationPresenter;
	/** The Manage Users Presenter. */
	private ManageUsersPresenter manageUsersPresenter;
	
	private ManageBonnieTokenPresenter manageBonnieTokenPresenter;
	/** The tab layout. */
	private MatTabLayoutPanel tabLayout;
	/** Instantiates a new Admin presenter. */
	@SuppressWarnings("unchecked")
	public ManageAdminPresenter() {
		presenterList = new ArrayList<>();
		ManageUsersSearchView musd = new ManageUsersSearchView();
		ManageUsersDetailView mudd = new ManageUsersDetailView();
		ManageUserHistoryView muhd = new ManageUserHistoryView();
		manageUsersPresenter = new ManageUsersPresenter(musd, mudd, muhd);
		ManageOrganizationView manageOrganizationView = new ManageOrganizationView();
		ManageOrganizationDetailView manageOrganizationDetailView = new ManageOrganizationDetailView();
		manageOrganizationPresenter = new ManageOrganizationPresenter(manageOrganizationView, manageOrganizationDetailView);
		
		manageBonnieTokenPresenter = new ManageBonnieTokenPresenter();
		tabLayout = new MatTabLayoutPanel(this);
		tabLayout.getElement().setAttribute("id", "qdmElementTabLayout");
		tabLayout.add(manageUsersPresenter.getWidget(), "Manage User", true);
		presenterList.add(manageUsersPresenter);
		tabLayout.add(manageOrganizationPresenter.getWidget(), "Manage Organization", true);
		presenterList.add(manageOrganizationPresenter);
		tabLayout.add(manageBonnieTokenPresenter.getWidget(), "Manage Bonnie Connection", true);
		presenterList.add(manageBonnieTokenPresenter);
		tabLayout.setHeight("98%");
		tabLayout.getElement().setAttribute("id", "myAccountTabLayout");
		
		MatContext.get().tabRegistry.put(ADMIN_ACCOUNT_TAB, tabLayout);
		tabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int index = event.getSelectedItem();
				// suppressing token dup
				String newToken = ADMIN_ACCOUNT_TAB + index;
				if (!History.getToken().equals(newToken)) {
					MatContext.get().recordTransactionEvent(null, null, "ADMIN_ACCT_TAB_EVENT", newToken, 1);
					History.newItem(newToken, false);
				}
			}
		});
		beforeDisplay();
	}

	@Override
	public void beforeClosingDisplay() {
		notifyCurrentTabOfClosing();
		tabLayout.updateHeaderSelection(0);
		tabLayout.setSelectedIndex(0);
	}

	@Override
	public void beforeDisplay() {
		fp.add(tabLayout);
		adminContentWidget.clear();
		adminContentWidget.add(fp);
		tabLayout.selectTab(presenterList.indexOf(manageUsersPresenter));
		manageUsersPresenter.beforeDisplay();
	}

	@Override
	public Widget getWidget() {
		return adminContentWidget;
	}
	@Override
	public boolean isValid() {
		return true;
	}
	@Override
	public void updateOnBeforeSelection() {
		MatPresenter presenter = presenterList.get(tabLayout.getSelectedIndex());
		if (presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
		}
		
	}

	@Override
	public void showUnsavedChangesError() {}

	@Override
	public void notifyCurrentTabOfClosing() {
		MatPresenter oldPresenter = presenterList.get(tabLayout.getSelectedIndex());
		if (oldPresenter != null) {
			MatContext.get().setAriaHidden(oldPresenter.getWidget(), "true");
			oldPresenter.beforeClosingDisplay();
		}
	}

}
