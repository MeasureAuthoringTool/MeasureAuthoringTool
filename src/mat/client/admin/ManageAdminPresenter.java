package mat.client.admin;
import mat.client.MatPresenter;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
/** The Class ManageAdminPresenter. */
public class ManageAdminPresenter implements MatPresenter {
	
	/** The admin account tab. */
	private final String ADMIN_ACCOUNT_TAB = "adminAccountTab";
	/** The Admin content widget. */
	private SimplePanel adminContentWidget = new SimplePanel();
	/** Flow Panel. */
	private FlowPanel fp = new FlowPanel();
	/** The Manage Organization Presenter. */
	private ManageOrganizationPresenter manageOrganizationPresenter;
	/** The Manage Users Presenter. */
	private ManageUsersPresenter manageUsersPresenter;
	/** The tab layout. */
	private MatTabLayoutPanel tabLayout;
	/** Instantiates a new Admin presenter. */
	@SuppressWarnings("unchecked")
	public ManageAdminPresenter() {
		ManageUsersSearchView musd = new ManageUsersSearchView();
		ManageUsersDetailView mudd = new ManageUsersDetailView();
		ManageUserHistoryView muhd = new ManageUserHistoryView();
		manageUsersPresenter = new ManageUsersPresenter(musd, mudd, muhd);
		ManageOrganizationView manageOrganizationView = new ManageOrganizationView();
		ManageOrganizationDetailView manageOrganizationDetailView = new ManageOrganizationDetailView();
		manageOrganizationPresenter = new ManageOrganizationPresenter(manageOrganizationView, manageOrganizationDetailView);
		tabLayout = new MatTabLayoutPanel(true);
		tabLayout.setId("qdmElementTabLayout");
		tabLayout.addPresenter(manageUsersPresenter, "Manage User");
		tabLayout.addPresenter(manageOrganizationPresenter, "Manage Organization");
		tabLayout.setHeight("98%");
		tabLayout.setId("myAccountTabLayout");
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
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeClosingDisplay()
	 */
	@Override
	public void beforeClosingDisplay() {
		tabLayout.close();
		tabLayout.updateHeaderSelection(0);
		tabLayout.setSelectedIndex(0);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.MatPresenter#beforeDisplay()
	 */
	@Override
	public void beforeDisplay() {
		fp.add(tabLayout);
		adminContentWidget.clear();
		adminContentWidget.add(fp);
		tabLayout.selectTab(manageUsersPresenter);
		manageUsersPresenter.beforeDisplay();
	}
	/* (non-Javadoc)
	 * @see mat.client.MatPresenter#getWidget()
	 */
	@Override
	public Widget getWidget() {
		return adminContentWidget;
	}
	
}
