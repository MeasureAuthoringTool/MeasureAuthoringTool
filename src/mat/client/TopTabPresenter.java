package mat.client;

import mat.client.admin.ManageAdminPresenter;
import mat.client.admin.ManageCQLLibraryAdminView;
import mat.client.admin.reports.ManageAdminReportingView;
import mat.client.cql.CQLLibraryDetailView;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.export.ManageExportView;
import mat.client.measure.ComponentMeasureDisplay;
import mat.client.measure.ManageCompositeMeasureDetailView;
import mat.client.measure.ManageMeasureDetailView;
import mat.client.measure.ManageMeasureHistoryView;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchView;
import mat.client.measure.ManageMeasureShareView;
import mat.client.measure.ManageMeasureVersionView;
import mat.client.measure.TransferOwnershipView;
import mat.client.myAccount.ChangePasswordPresenter;
import mat.client.myAccount.ChangePasswordView;
import mat.client.myAccount.MyAccountPresenter;
import mat.client.myAccount.MyAccountView;
import mat.client.myAccount.PersonalInformationPresenter;
import mat.client.myAccount.PersonalInformationView;
import mat.client.myAccount.SecurityQuestionsPresenter;
import mat.client.myAccount.SecurityQuestionsView;
import mat.client.util.ClientConstants;

public class TopTabPresenter {
	
	private boolean isAdmin;
	private TopTabView view;
	private	MatPresenter adminPresenter;
	private ManageCQLLibraryAdminView cqlLibraryAdminPresenter;
	private ManageMeasurePresenter measureLibrary;
	private MeasureComposerPresenter measureComposer;
	private CqlLibraryPresenter cqlLibrary;
	private CqlComposerPresenter cqlComposer;
	private MyAccountPresenter matAccount;
	private CompositeMeasureEdit compositeMeasureEdit;
	private ManageAdminReportingView adminReportingView;

	public TopTabPresenter(boolean isAdmin) {
		this.view = new TopTabView(isAdmin);
		this.isAdmin = isAdmin;
		
		initAdminPresenter();
		initMeasureLibraryView();
		initMeasureComposerView();
		initAdminReportingView();
		initCqlLibraryView();
		initCqlComposerView();
		initMatAccount();
		initCompositeMeasureEdit();
	
		hideTabs();
		
		addClickHandlers();
	}
	
	private void hideTabs() {
		if(isAdmin) {
			view.getMeasureComposerItem().setVisible(false);
			view.getCqlComposerItem().setVisible(false);
		} else {
			view.getAdminPresenterItem().setVisible(false);
			view.getAdminReportingItem().setVisible(false);
		}
		view.getCompositeMeasureEditItem().setVisible(false);
	}

	private void initMeasureLibraryView() {
		view.getMeasureLibraryPane().clear();
		view.getMeasureLibItem().setActive(false);
		view.getMeasureLibraryPane().setActive(false);
		measureLibrary = buildMeasureLibrary();
		view.getMeasureLibraryPane().add(measureLibrary.getWidget());
	}
	
	private void initMeasureComposerView() {
		view.getMeasureComposerPane().clear();
		view.getMeasureComposerItem().setActive(false);
		view.getMeasureComposerPane().setActive(false);
		measureComposer = buildMeasureComposer();
		view.getMeasureComposerPane().add(measureComposer.getWidget());
	}

	private void initCqlLibraryView() {
		view.getCqlLibraryPane().clear();
		view.getCqlLibraryItem().setActive(false);
		view.getCqlLibraryPane().setActive(false);
		if(isAdmin) {
			this.cqlLibraryAdminPresenter = buildCqlLibraryAdminView();
			view.getCqlLibraryPane().add(cqlLibraryAdminPresenter.getWidgetVP());
		} else {
			cqlLibrary = buildCqlLibraryWidget();
			view.getCqlLibraryPane().add(cqlLibrary.getWidget());
		}
	}

	private void initCqlComposerView() {
		view.getCqlComposerPane().clear();
		view.getCqlComposerItem().setActive(false);
		view.getCqlComposerPane().setActive(false);
		cqlComposer = buildCqlComposer();
		view.getCqlComposerPane().add(cqlComposer.getWidget());
	}
	
	private void initMatAccount() {
		view.getMatAccountPane().clear();
		view.getMatAccountItem().setActive(false);
		view.getMatAccountPane().setActive(false);
		matAccount = buildMatAccount();
		view.getMatAccountPane().add(matAccount.getWidget());
	}
	
	private void initCompositeMeasureEdit() {
		view.getCompositeMeasureEditPane().clear();
		view.getCompositeMeasureEditItem().setActive(false);
		view.getCompositeMeasureEditPane().setActive(false);
		compositeMeasureEdit = buildCompositeMeasureEdit();
		view.getCompositeMeasureEditPane().add(compositeMeasureEdit.getWidget());
	}
	
	private void initAdminPresenter() {
		view.getAdminPresenterPane().clear();
		view.getAdminPresenterItem().setActive(false);
		view.getAdminPresenterPane().setActive(false);
		this.adminPresenter = buildAdminPresenter();
		view.getAdminPresenterPane().add(adminPresenter.getWidget());
	}
		
	private void initAdminReportingView() {
		view.getAdminReportingPane().clear();
		view.getAdminReportingItem().setActive(false);
		view.getAdminReportingPane().setActive(false);
		this.adminReportingView = new ManageAdminReportingView();
		view.getAdminReportingPane().add(adminReportingView.asWidget());
	}
	
	private MatPresenter buildAdminPresenter() {
		ManageAdminPresenter adminPresenter = new ManageAdminPresenter();
		return adminPresenter;
	}
	
	private ManageMeasurePresenter buildMeasureLibrary() {
		ManageMeasurePresenter measurePresenter = null;
		if (isAdmin) {
			ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
			TransferOwnershipView transferOS = new TransferOwnershipView();
			ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();
	
			measurePresenter = new ManageMeasurePresenter(measureSearchView, null, null, null, null, null, historyView,
					null, transferOS);
		} else {
			ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
			ManageMeasureDetailView measureDetailView = new ManageMeasureDetailView();
			ManageCompositeMeasureDetailView compositeMeasureDetailView = new ManageCompositeMeasureDetailView();
			ManageMeasureVersionView versionView = new ManageMeasureVersionView();
			ManageMeasureShareView measureShareView = new ManageMeasureShareView();
			ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();		
			ManageExportView exportView = new ManageExportView();		
			ComponentMeasureDisplay componentMeasureDisplay = new ComponentMeasureDisplay();
			
			measurePresenter = new ManageMeasurePresenter(measureSearchView, measureDetailView,
					compositeMeasureDetailView, componentMeasureDisplay, measureShareView, exportView,
					historyView, versionView, null);
		}
	
		return measurePresenter;
	}
	
	private MeasureComposerPresenter buildMeasureComposer() {
		return new MeasureComposerPresenter();
	}
	
	private CqlLibraryPresenter buildCqlLibraryWidget() {
		CqlLibraryView cqlLibraryView = new CqlLibraryView();
		CQLLibraryDetailView detailView = new CQLLibraryDetailView();
		CQLLibraryVersionView versionView = new CQLLibraryVersionView();
		CQLLibraryShareView shareView = new CQLLibraryShareView();
		CQLLibraryHistoryView historyView = new CQLLibraryHistoryView();
		CqlLibraryPresenter cqlLibraryPresenter = new CqlLibraryPresenter(cqlLibraryView, detailView, 
				versionView, shareView, historyView);
		return cqlLibraryPresenter;
	}
	
	private CqlComposerPresenter buildCqlComposer() {
		return new CqlComposerPresenter();
	}

	private MyAccountPresenter buildMatAccount() {
		PersonalInformationView informationView = new PersonalInformationView();
		PersonalInformationPresenter personalInfoPrsnter = new PersonalInformationPresenter(informationView);
		SecurityQuestionsPresenter quesPresenter = new SecurityQuestionsPresenter(new SecurityQuestionsView());
		
		ChangePasswordPresenter passwordPresenter = new ChangePasswordPresenter(new ChangePasswordView());
		
		
		MyAccountPresenter accountPresenter = new MyAccountPresenter(new MyAccountView(personalInfoPrsnter,
				quesPresenter, passwordPresenter));
		return accountPresenter;
	}
	

	private ManageCQLLibraryAdminView buildCqlLibraryAdminView() {
		return new ManageCQLLibraryAdminView();
	}
	
	private CompositeMeasureEdit buildCompositeMeasureEdit() {
		return new CompositeMeasureEdit();
	}
	
	private void addClickHandlers() {
		view.getMeasureLibItem().addClickHandler(event -> measureLibraryClickHandler());
		view.getMeasureComposerItem().addClickHandler(event -> measureComposerClickHandler());
		view.getCqlLibraryItem().addClickHandler(event -> cqlLibraryClickHandler());
		view.getCqlComposerItem().addClickHandler(event -> cqlComposerClickHandler());
		view.getMatAccountItem().addClickHandler(event -> matAccountClickHandler());
		view.getCompositeMeasureEditItem().addClickHandler(event -> compositeMeasureEditClickHandler());
		view.getAdminPresenterItem().addClickHandler(event -> adminPresenterClickHandler());
		view.getAdminReportingItem().addClickHandler(event -> adminReportingClickHandler());
	}

	private void adminPresenterClickHandler() {
		setAllTabsFalse();
		this.view.getAdminPresenterItem().setActive(true);
		this.view.getAdminPresenterPane().setActive(true);
	}
	
	private void adminReportingClickHandler() {
		setAllTabsFalse();
		this.view.getAdminReportingItem().setActive(true);
		this.view.getAdminReportingPane().setActive(true);
	}

	private void measureLibraryClickHandler() {
		setAllTabsFalse();
		this.view.getMeasureLibItem().setActive(true);
		this.view.getMeasureLibraryPane().setActive(true);
	}
	
	private void measureComposerClickHandler() {
		setAllTabsFalse();
		this.view.getMeasureComposerItem().setActive(true);
		this.view.getMeasureComposerPane().setActive(true);
	}
	
	private void cqlLibraryClickHandler() {
		setAllTabsFalse();
		this.view.getCqlLibraryItem().setActive(true);
		this.view.getCqlLibraryPane().setActive(true);
	}
	
	private void cqlComposerClickHandler() {
		setAllTabsFalse();
		this.view.getCqlComposerItem().setActive(true);
		this.view.getCqlComposerPane().setActive(true);
	}
	
	private void matAccountClickHandler() {
		setAllTabsFalse();
		this.view.getMatAccountItem().setActive(true);
		this.view.getMatAccountPane().setActive(true);
	}
	
	private void compositeMeasureEditClickHandler() {
		setAllTabsFalse();
		this.view.getCompositeMeasureEditItem().setActive(true);
		this.view.getCompositeMeasureEditPane().setActive(true);
	}
	
	private void setAllTabsFalse() {
		this.view.getAdminPresenterItem().setActive(false);
		this.view.getAdminPresenterPane().setActive(false);
		this.view.getAdminReportingItem().setActive(false);
		this.view.getAdminReportingPane().setActive(false);
		this.view.getMeasureLibItem().setActive(false);
		this.view.getMeasureLibraryPane().setActive(false);
		this.view.getMeasureComposerItem().setActive(false);
		this.view.getMeasureComposerPane().setActive(false);
		this.view.getCqlLibraryItem().setActive(false);
		this.view.getCqlLibraryPane().setActive(false);
		this.view.getCqlComposerItem().setActive(false);
		this.view.getCqlComposerPane().setActive(false);
		this.view.getMatAccountItem().setActive(false);
		this.view.getMatAccountPane().setActive(false);
		this.view.getCompositeMeasureEditItem().setActive(false);
		this.view.getCompositeMeasureEditPane().setActive(false);
	}
	
	public void setActive(String activeTab) {
			if(activeTab.equals(ClientConstants.TITLE_ADMIN)){
				adminPresenterClickHandler();
			} else if(activeTab.equals(ClientConstants.TITLE_ADMIN_ACCOUNT) || activeTab.equals(ClientConstants.TITLE_MY_ACCOUNT)) {
				matAccountClickHandler();
			} else if(activeTab.equals(ClientConstants.TITLE_CQL_COMPOSER)) {
				cqlComposerClickHandler();
			} else if(activeTab.equals(ClientConstants.ADMIN_REPORTS)) {
				adminReportingClickHandler();
			} else if(activeTab.equals(ClientConstants.TITLE_MEASURE_LIB_CHANGE_OWNERSHIP) || activeTab.equals(ClientConstants.TITLE_MEASURE_LIB)) {
				measureLibraryClickHandler();
			} else if(activeTab.equals(ClientConstants.TITLE_MEASURE_COMPOSER)) {
				measureComposerClickHandler();
			} else if(activeTab.equals(ClientConstants.TITLE_CQL_LIB) || activeTab.equals(ClientConstants.CQL_LIBRARY_OWNERSHIP)) {
				cqlLibraryClickHandler();
			} else if(activeTab.equals(ClientConstants.COMPOSITE_MEASURE_EDIT)) {
				compositeMeasureEditClickHandler();
			}
	}

	public TopTabView getView() {
		return view;
	}

	public MatPresenter getAdminPresenter() {
		return adminPresenter;
	}

	public ManageCQLLibraryAdminView getCqlLibraryAdminPresenter() {
		return cqlLibraryAdminPresenter;
	}

	public ManageMeasurePresenter getMeasureLibrary() {
		return measureLibrary;
	}

	public MeasureComposerPresenter getMeasureComposer() {
		return measureComposer;
	}

	public CqlLibraryPresenter getCqlLibrary() {
		return cqlLibrary;
	}

	public CqlComposerPresenter getCqlComposer() {
		return cqlComposer;
	}

	public MyAccountPresenter getMatAccount() {
		return matAccount;
	}

	public CompositeMeasureEdit getCompositeMeasureEdit() {
		return compositeMeasureEdit;
	}

	public ManageAdminReportingView getAdminReportingView() {
		return adminReportingView;
	}
	
	
	
}