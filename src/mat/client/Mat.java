package mat.client;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

import mat.client.admin.ManageAdminPresenter;
import mat.client.admin.ManageCQLLibraryAdminPresenter;
import mat.client.admin.ManageCQLLibraryAdminView;
import mat.client.admin.reports.ManageAdminReportingPresenter;
import mat.client.admin.reports.ManageAdminReportingView;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.cql.CQLLibraryDetailView;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.event.BackToLoginPageEvent;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.LogoffEvent;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureEditEvent;
import mat.client.event.TimedOutEvent;
import mat.client.login.service.SessionManagementService;
import mat.client.measure.ManageMeasureDetailView;
import mat.client.measure.ManageMeasureExportView;
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
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.ui.MATTabPanel;
import mat.client.umls.ManageUmlsPresenter;
import mat.client.umls.UmlsLoginView;
import mat.client.util.ClientConstants;
import mat.model.SecurityRole;
import mat.shared.ConstantMessages;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mat extends MainLayout implements EntryPoint, Enableable, TabObserver{
	
	/**
	 * The Class EnterKeyDownHandler.
	 */
	class EnterKeyDownHandler implements KeyDownHandler {
		
		/** The counter. */
		final private int counter;
		
		/**
		 * Instantiates a new enter key down handler.
		 *
		 * @param index the index
		 */
		public EnterKeyDownHandler(int index){
			counter = index;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.event.dom.client.KeyDownHandler#onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent)
		 */
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				mainTabLayout.selectTab(counter);
			}
		}
	}
	
	/**
	 * Focus skip lists.
	 *
	 * @param skipstr the skipstr
	 */
	public static void focusSkipLists(String skipstr){
		Widget widget = SkipListBuilder.buildSkipList(skipstr);
		getSkipList().clear();
		getSkipList().add(widget);
		getSkipList().setFocus(true);
	}
	
	/**
	 * Gets the user agent.
	 *
	 * @return the user agent
	 */
	public static native String getUserAgent() /*-{
		return navigator.userAgent.toLowerCase();
	}-*/;
	
	/**
	 * Removes the input box from focus panel.
	 *
	 * @param element the element
	 */
	public static void removeInputBoxFromFocusPanel(Element element) {
		if(element.hasChildNodes() && element.getFirstChild().getNodeName().equalsIgnoreCase("input")){// this is done for 508 issue to fix the input box in FF
			element.removeChild(element.getFirstChild());
		}
	}
	
	private List<MatPresenter> presenterList;
	private	MatPresenter adminPresenter;
	private ClosingEvent closingEvent;
	String currentUserRole = ClientConstants.USER_STATUS_NOT_LOGGEDIN;
	final private ListBoxCodeProvider listBoxCodeProvider = new ListBoxCodeProvider();
	private MatTabLayoutPanel mainTabLayout;
	private String mainTabLayoutID;
	private ManageUmlsPresenter manageUmlsPresenter;
	private MeasureComposerPresenter measureComposer;
	private ManageMeasurePresenter measureLibrary;
	private ManageCQLLibraryAdminPresenter cqlLibraryAdminPresenter;
	private CqlComposerPresenter cqlComposer;
	private CqlLibraryPresenter cqlLibrary;
	private ManageAdminReportingPresenter reportingPresenter;
	private int tabIndex;
	
	
	/** The user role callback. */
	private  final AsyncCallback<SessionManagementService.Result> userRoleCallback = new AsyncCallback<SessionManagementService.Result>(){
		
		@Override
		public void onFailure(final Throwable caught) {
			redirectToLogin();
			//			Window.alert("User Role is not set in the session");
		}
		
		@Override
		public void onSuccess(final SessionManagementService.Result result) {	
			MatContext.get().getCurrentReleaseVersion(new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					
				}

				@Override
				public void onSuccess(String resultMatVersion) {
					if(result == null){
						redirectToLogin();
					}else{
						final Date lastSignIn = result.signInDate;
						final Date lastSignOut = result.signOutDate;
						final Date current = new Date();
						final boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);
						if(isAlreadySignedIn){
							redirectToLogin();
						}
						else{
							MatContext.get().setUserSignInDate(result.userId);
							MatContext.get().setUserInfo(result.userId, result.userEmail, result.userRole,result.loginId);
							loadMatWidgets(result.userFirstName, isAlreadySignedIn, resultMatVersion);
						}
					}
					
				}
			});
			}
	};
	
	/** Builds the admin presenter.
	 * 
	 * @return the mat presenter */
	private MatPresenter buildAdminPresenter() {
		ManageAdminPresenter adminPresenter = new ManageAdminPresenter();
		return adminPresenter;
	}
	
	/**
	 * Builds the measure composer.
	 *
	 * @return the measure composer presenter
	 */
	private MeasureComposerPresenter buildMeasureComposer() {
		return new MeasureComposerPresenter();
	}
	
	/**
	 * Builds the cql composer.
	 *
	 * @return the cql composer presenter
	 */
	private CqlComposerPresenter buildCqlComposer() {
		return new CqlComposerPresenter();
	}
	
	/**
	 * Builds the measure library widget.
	 *
	 * @param isAdmin the is admin
	 * @return the manage measure presenter
	 */
	private ManageMeasurePresenter buildMeasureLibraryWidget(Boolean isAdmin) {
		ManageMeasurePresenter measurePresenter = null;
		if(isAdmin){
			ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
			TransferOwnershipView transferOS = new TransferOwnershipView();
			ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();
			
			measurePresenter = new ManageMeasurePresenter(measureSearchView, null, null, null, historyView, null, /*null,*/ transferOS);
		}else{
			ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
			ManageMeasureDetailView measureDetailView = new ManageMeasureDetailView();
			ManageMeasureVersionView versionView = new ManageMeasureVersionView();
			ManageMeasureShareView measureShareView = new ManageMeasureShareView();
			ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();
			ManageMeasureExportView measureExportView;
			if (currentUserRole.equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)){
				measureExportView = new ManageMeasureExportView(true);
			}else{
				measureExportView = new ManageMeasureExportView(false);
			}
			
			measurePresenter = new ManageMeasurePresenter(measureSearchView, measureDetailView, measureShareView, measureExportView, historyView, versionView, /*measureDraftView,*/ null);
		}
		return measurePresenter;
		
	}
	
	/**
	 * Builds the cql library widget.
	 *
	 * @param isAdmin the is admin
	 * @return the cql library presenter
	 */
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
	/**
	 * Builds the my account widget.
	 *
	 * @return the mat presenter
	 */
	private MatPresenter buildMyAccountWidget() {
		PersonalInformationView informationView = new PersonalInformationView();
		PersonalInformationPresenter personalInfoPrsnter = new PersonalInformationPresenter(informationView);
		SecurityQuestionsPresenter quesPresenter = new SecurityQuestionsPresenter(new SecurityQuestionsView());
		
		ChangePasswordPresenter passwordPresenter = new ChangePasswordPresenter(new ChangePasswordView());
		
		
		MyAccountPresenter accountPresenter = new MyAccountPresenter(new MyAccountView(personalInfoPrsnter,
				quesPresenter, passwordPresenter));
		return accountPresenter;
	}
	
	/**
	 * Builds the umls widget.
	 *
	 * @param userFirstName the user first name
	 * @param isAlreadySignedIn the is already signed in
	 * @return the mat presenter
	 */
	private MatPresenter buildUMLSWidget(String userFirstName, boolean isAlreadySignedIn){
		UmlsLoginView umlsLoginView = new UmlsLoginView();
		ManageUmlsPresenter manageUmlsPresenter = new ManageUmlsPresenter(umlsLoginView, userFirstName, isAlreadySignedIn);
		return manageUmlsPresenter;
	}
	
	/**
	 * Call sign out.
	 */
	private void callSignOut(){
		MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {
			
			@Override
			public void onFailure(Throwable arg0) {
				redirectToLogin();
			}
			
			@Override
			public void onSuccess(Void arg0) {
				redirectToLogin();
			}
		});
	}
	
	/**
	 * Call sign out without redirect.
	 */
	private void callSignOutWithoutRedirect(){
		MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {
			@Override
			public void onFailure(Throwable arg0) {}
			@Override
			public void onSuccess(Void arg0) {}
		});
		//		 closeBrowser();
	}
	
	/**
	 * Close browser.
	 */
	private native void closeBrowser()
	/*-{
		$wnd.open('', '_self');
		$wnd.close();
	}-*/;
	
	/* (non-Javadoc)
	 * @see mat.client.MainLayout#initEntryPoint()
	 */
	@Override
	protected void initEntryPoint() {
		MatContext.get().setCurrentModule(ConstantMessages.MAT_MODULE);
		showLoadingMessage();
		MatContext.get().setListBoxCodeProvider(listBoxCodeProvider);
		MatContext.get().getCurrentUserRole(userRoleCallback);
		mainTabLayoutID = ConstantMessages.MAIN_TAB_LAYOUT_ID;
		/*
		 * logic used to process history for registered TabPanels
		 * See field MatContext.tabRegistry
		 * When instantiating a TabPanel, add a selection handler responsible for adding a History item
		 * to History identifying the TabPanel in the registry.
				      
			 MatContext.get().tabRegistry.put(MY_TAB_PANEL_ID,tabLayout);
				tabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
				      public void onSelection(SelectionEvent<Integer> event) {
				        History.newItem(MY_TAB_PANEL_ID + event.getSelectedItem(), false);
				      }});

		 */
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				final String historyToken = event.getValue();
				
				if((historyToken == null) || historyToken.isEmpty()){
					History.newItem(mainTabLayoutID + 0, false);
				}
				else if(!MatContext.get().isLoading())   {
					// Parse the history token
					
					try {
						for(Object key : MatContext.get().tabRegistry.keySet()){
							if(key instanceof String){
								String k = (String) key;
								if(historyToken.contains(k)){
									final String tabIndexToken = historyToken.substring(k.length());
									final int tabIndex = Integer.parseInt(tabIndexToken);
									MATTabPanel tp = (MATTabPanel) MatContext.get().tabRegistry.get(key);
									/* Suppressing selection of MAIN_TAB_LAYOUT_ID+mainTabLayout.selectedIndex
									 * if already selected
									 */
									if(!History.getToken().equals(mainTabLayoutID+mainTabLayout.getSelectedIndex())) {
										tp.selectTab(tabIndex);
									}
								}
							}
						}
					} catch (IndexOutOfBoundsException e) {
						History.newItem(mainTabLayoutID + 0, false);
					}
				}else{
					MatContext.get().fireLoadingAlert();
					//reload
					History.newItem(historyToken, false);
				}
			}
		});
		
		
		MatContext.get().getEventBus().addHandler(MeasureEditEvent.TYPE, new MeasureEditEvent.Handler() {
			@Override
			final public void onMeasureEdit(MeasureEditEvent event) {
				mainTabLayout.selectTab(presenterList.indexOf(measureComposer));
				focusSkipLists("MeasureComposer");
			}
		});
		
		MatContext.get().getEventBus().addHandler(CQLLibraryEditEvent.TYPE, new CQLLibraryEditEvent.Handler() {
			@Override
			final public void onCQLLibraryEdit(CQLLibraryEditEvent event) {
				mainTabLayout.selectTab(presenterList.indexOf(cqlComposer));
				focusSkipLists("CqlComposer");
			}
		});
		
		MatContext.get().getEventBus().addHandler(BackToMeasureLibraryPage.TYPE, new BackToMeasureLibraryPage.Handler() {
			
			@Override
			public void onDeleted(BackToMeasureLibraryPage event) {
				mainTabLayout.selectTab(presenterList.indexOf(measureLibrary));
				focusSkipLists("Measure Library");
			}
		});
		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			@Override
			public void onUncaughtException(Throwable arg0) {
				//stack traces from errors cannot be propagated to the user
				arg0.printStackTrace();
				MatContext.get().recordTransactionEvent(null, null, null, "Unhandled Exception: "+arg0.getLocalizedMessage(), 0);
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}
		});
	}
	
	/**
	 * Load mat widgets.
	 *
	 * @param userFirstName the user first name
	 * @param isAlreadySignedIn the is already signed in
	 * @param resultMatVersion 
	 */
	@SuppressWarnings("unchecked")
	private void loadMatWidgets(String userFirstName, boolean isAlreadySignedIn, String resultMatVersion){
		//US212 begin updating user sign in time at regular intervals
		MatContext.get().startUserLockUpdate();
		//US154 LOGIN_EVENT
		MatContext.get().recordTransactionEvent(null, null, "LOGIN_EVENT", null, 1);
		
		mainTabLayout = new MatTabLayoutPanel(this);
		mainTabLayout.getElement().setAttribute("id", "matMainTabPanel");
		mainTabLayout.getElement().setAttribute("aria-role", "TabList");
		
		MatContext.get().tabRegistry.put(mainTabLayoutID,mainTabLayout);
		MatContext.get().enableRegistry.put(mainTabLayoutID,this);
		mainTabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			@Override
			public void onSelection(final SelectionEvent<Integer> event) {
				final int index = event.getSelectedItem();
				// suppressing token dup
				final String newToken = mainTabLayoutID + index;
				if(!History.getToken().equals(newToken)){
					MatContext.get().recordTransactionEvent(null, null, "MAIN_TAB_EVENT", newToken, 1);
					History.newItem(newToken, false);
				}	
			}
			
		});
		
		String title = ClientConstants.TEXT_THE_TITLE;
		tabIndex = 0;
		presenterList = new LinkedList<MatPresenter>();
		currentUserRole = MatContext.get().getLoggedInUserRole();
		
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			MatContext.get().getCQLConstants();
			
			measureLibrary = buildMeasureLibraryWidget(false);
			title = ClientConstants.TITLE_MEASURE_LIB;
			mainTabLayout.add(measureLibrary.getWidget(), title);
			presenterList.add(measureLibrary);
			
			measureComposer= buildMeasureComposer();
			title = ClientConstants.TITLE_MEASURE_COMPOSER;
			mainTabLayout.add(measureComposer.getWidget(), title);
			presenterList.add(measureComposer);
			
			cqlLibrary = buildCqlLibraryWidget();
			title = ClientConstants.TITLE_CQL_LIB;
			mainTabLayout.add(cqlLibrary.getWidget(), title);
			presenterList.add(cqlLibrary);
			
			cqlComposer= buildCqlComposer();
			title = ClientConstants.TITLE_CQL_COMPOSER;
			mainTabLayout.add(cqlComposer.getWidget(), title);
			presenterList.add(cqlComposer);
			
			title = ClientConstants.TITLE_MY_ACCOUNT;
			MatPresenter myAccountPresenter = buildMyAccountWidget();
			mainTabLayout.add(myAccountPresenter.getWidget(), title);
			presenterList.add(myAccountPresenter);
			
			title= ClientConstants.TITLE_UMLS;
			manageUmlsPresenter = (ManageUmlsPresenter) buildUMLSWidget(userFirstName, isAlreadySignedIn);
			mainTabLayout.add(manageUmlsPresenter.getWidget(), title);
			presenterList.add(manageUmlsPresenter);
			
			tabIndex = presenterList.indexOf(manageUmlsPresenter);
			hideUMLSActive();
		}
		else if(currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR))
		{
			adminPresenter = buildAdminPresenter();
			title = ClientConstants.TITLE_ADMIN;
			mainTabLayout.add(adminPresenter.getWidget(), title);
			presenterList.add(adminPresenter);
			
			measureLibrary = buildMeasureLibraryWidget(true);
			title = ClientConstants.TITLE_MEASURE_LIB_CHANGE_OWNERSHIP;
			mainTabLayout.add(measureLibrary.getWidget(), title);
			presenterList.add(measureLibrary);
			
			ManageCQLLibraryAdminView cqlLibraryAdminView = new ManageCQLLibraryAdminView();
			CQLLibraryHistoryView historyView = new CQLLibraryHistoryView();
			TransferOwnershipView transferOS = new TransferOwnershipView();
			cqlLibraryAdminPresenter = new ManageCQLLibraryAdminPresenter(cqlLibraryAdminView,historyView,transferOS);
			title = "CQL Library Ownership";
			mainTabLayout.add(cqlLibraryAdminPresenter.getWidget(), title);
			presenterList.add(cqlLibraryAdminPresenter);
			
			ManageAdminReportingView adminReportingView = new ManageAdminReportingView();
			reportingPresenter = new ManageAdminReportingPresenter(adminReportingView);
			title = "Administrator Reports";
			mainTabLayout.add(reportingPresenter.getWidget(), title);
			presenterList.add(reportingPresenter);
			
			title = ClientConstants.TITLE_ADMIN_ACCOUNT;
			MatPresenter myAccountPresenter = buildMyAccountWidget();
			mainTabLayout.add(myAccountPresenter.getWidget(), title);
			presenterList.add(myAccountPresenter);
			tabIndex = presenterList.indexOf(myAccountPresenter);
		}
		else {
			Window.alert("Unrecognized user role " + currentUserRole);
			MatContext.get().getEventBus().fireEvent(new LogoffEvent());
		}
		
		mainTabLayout.setHeight("100%");
		
		Anchor signout = new Anchor(ClientConstants.ANCHOR_SIGN_OUT);
		signout.setTitle("Sign Out");
		signout.getElement().setAttribute("id", "Anchor_signOut");
		signout.addClickHandler(new MATClickHandler() {
			
			@SuppressWarnings("rawtypes")
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().getEventBus().fireEvent(new LogoffEvent());
			}
		});
		
		getLogoutPanel().add(signout);
		getWelcomeUserPanel(userFirstName);
		getVersionPanel(resultMatVersion);
		/*
		 * no delay desired when hiding loading message here
		 * tab selection below will fail if loading
		 */
		hideLoadingMessage(0);
		
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			mainTabLayout.selectTab(presenterList.indexOf(manageUmlsPresenter));
		}
		else if(currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			mainTabLayout.selectTab(presenterList.indexOf(adminPresenter));
		}
		
		getContentPanel().add(mainTabLayout);
		
		getContentPanel().addKeyPressHandler(new KeyPressHandler() {
			
			@Override
			public void onKeyPress(KeyPressEvent event) {
				MatContext.get().restartTimeoutWarning();
			}
		});
		
		getContentPanel().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().restartTimeoutWarning();
			}
		});
		
		/*
		 * This block adds a special generic handler for any mouse clicks in the mainContent for ie browser.
		 * Need to add this handler in order to keep track of the user activity in IE8 Browser.
		 */
		if(getUserAgent().contains(ClientConstants.MSIE)){
			getContentPanel().addMouseUpHandler(new MouseUpHandler() {
				
				@Override
				public void onMouseUp(MouseUpEvent event) {
					MatContext.get().restartTimeoutWarning();
				}
			});
		}
		
		MatContext.get().getEventBus().addHandler(BackToLoginPageEvent.TYPE, new BackToLoginPageEvent.Handler() {
			
			@Override
			public void onLoginFailure(BackToLoginPageEvent event) {
				redirectToLogin();
			}
		});
		MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, new LogoffEvent.Handler() {
			@Override
			public void onLogoff(LogoffEvent event) {
				Mat.hideLoadingMessage();
				Mat.showSignOutMessage();
				MatContext.get().getSynchronizationDelegate().setLogOffFlag();
				MatContext.get().handleSignOut("SIGN_OUT_EVENT", true);
			}
		});
		
		Window.addCloseHandler(new CloseHandler<Window>() {
			@Override
			public void onClose(CloseEvent<Window> arg0) {
				if(!MatContext.get().getSynchronizationDelegate().getLogOffFlag()){
					MatContext.get().handleSignOut("WINDOW_CLOSE_EVENT", false);
				}
			}
		});
		
		MatContext.get().getEventBus().addHandler(TimedOutEvent.TYPE,new TimedOutEvent.Handler() {
			@Override
			public void onTimedOut(TimedOutEvent event) {
				if(measureComposer != null){
					Mat.focusSkipLists("MainContent");
				}
			}
		});
		
		
		
		MatContext.get().restartTimeoutWarning();
	}
	
	/**
	 * Redirect to login.
	 */
	private void redirectToLogin() {
		hideLoadingMessage();
		/*
		 * Added a timer to have a delay before redirect since
		 * this was causing the firefox javascript exception.
		 */
		final Timer timer = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
			}
		};
		timer.schedule(1000);
		
	}
	
	
	/**
	 * implementing enabled interface
	 * consider adding ui component enablement by invoking setEnabled on the active presenter.
	 *
	 * @param enabled the new enabled
	 */
	@Override
	public void setEnabled(boolean enabled){
		mainTabLayout.setEnabled(enabled);
	}

	@Override
	public boolean isValid() {
		MatContext.get().setErrorTabIndex(-1);
		MatContext.get().setErrorTab(false);
		Integer selectedIndex = mainTabLayout.getSelectedIndex();
		
		if(presenterList.get(selectedIndex) instanceof TabObserver) {
			TabObserver tabObserver = (TabObserver) presenterList.get(selectedIndex);
			return tabObserver.isValid();
		}

		return true;
	}

	@Override
	public void updateOnBeforeSelection() {
		MatPresenter presenter = presenterList.get(mainTabLayout.getSelectedIndex());
		if (presenter != null) {
			MatContext.get().setAriaHidden(presenter.getWidget(),  "false");
			presenter.beforeDisplay();
		}
	}
	
	@Override
	public void showUnsavedChangesError() {
		Integer selectedIndex = mainTabLayout.getSelectedIndex();
		MatPresenter presenter = presenterList.get(selectedIndex);
		if(presenter != null && presenter instanceof TabObserver) {
			MatPresenter targetPresenter = presenterList.get(mainTabLayout.getTargetSelection());
			if(targetPresenter != null) {
				if(presenter instanceof MeasureComposerPresenter) {
					((MeasureComposerPresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
				} else if(presenter instanceof CqlComposerPresenter) {
					((CqlComposerPresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
				}
			}
			((TabObserver) presenter).showUnsavedChangesError();
		}
	}

	@Override
	public void notifyCurrentTabOfClosing() {
		Integer selectedIndex = mainTabLayout.getSelectedIndex();
		MatPresenter presenter = presenterList.get(selectedIndex);
		if(presenter instanceof TabObserver) {
			TabObserver tabObserver = (TabObserver) presenterList.get(selectedIndex);
			tabObserver.notifyCurrentTabOfClosing();
		}
		presenter.beforeClosingDisplay();
	}
	
}
