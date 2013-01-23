package mat.client;

import java.util.Date;

import mat.client.admin.ManageUsersDetailView;
import mat.client.admin.ManageUsersPresenter;
import mat.client.admin.ManageUsersSearchView;
import mat.client.codelist.CodeListController;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.codelist.events.EditCodeListEvent;
import mat.client.codelist.events.EditGroupedCodeListEvent;
import mat.client.event.BackToLoginPageEvent;
import mat.client.event.LogoffEvent;
import mat.client.event.MATClickHandler;
import mat.client.event.MeasureEditEvent;
import mat.client.event.TimedOutEvent;
import mat.client.login.service.SessionManagementService;
import mat.client.measure.ManageMeasureDetailView;
import mat.client.measure.ManageMeasureDraftView;
import mat.client.measure.ManageMeasureExportView;
import mat.client.measure.ManageMeasureHistoryView;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchView;
import mat.client.measure.ManageMeasureShareView;
import mat.client.measure.ManageMeasureVersionView;
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
import mat.client.shared.SynchronizationDelegate;
import mat.client.shared.ui.MATTabPanel;
import mat.client.util.ClientConstants;
import mat.model.SecurityRole;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
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
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mat extends MainLayout implements EntryPoint, Enableable{
	final private ListBoxCodeProvider listBoxCodeProvider = new ListBoxCodeProvider();
	private MatTabLayoutPanel mainTabLayout;
	private String mainTabLayoutID;
	private MeasureComposerPresenter measureComposer;
	private ManageMeasurePresenter measureLibrary;
	private	MatPresenter adminPresenter;
	private CodeListController codeListController;
	
	
	String currentUserRole = ClientConstants.USER_STATUS_NOT_LOGGEDIN;
	
	@SuppressWarnings("rawtypes")
	private  final AsyncCallback<SessionManagementService.Result> userRoleCallback = new AsyncCallback<SessionManagementService.Result>(){

		@Override
		public void onFailure(final Throwable caught) {
			redirectToLogin();
//			Window.alert("User Role is not set in the session");
		}

		public void onSuccess(final SessionManagementService.Result result) {
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
					loadMatWidgets();
				}
			}
        }
	};
	
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
			public void onValueChange(final ValueChangeEvent<String> event) {
				final String historyToken = event.getValue();
				
				    if(historyToken == null || historyToken.isEmpty()){
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
										  if(!History.getToken().equals(mainTabLayoutID+mainTabLayout.getSelectedIndex()))
											  tp.selectTab(tabIndex);
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
//				currentMeasure.setText(event.getMeasureName());
				mainTabLayout.selectTab(measureComposer);
				focusSkipLists("MeasureComposer");
			}
		});
		
		MatContext.get().getEventBus().addHandler(EditGroupedCodeListEvent.TYPE, new EditGroupedCodeListEvent.Handler(){
			@Override
			public void onEditGroupedCodeList(EditGroupedCodeListEvent event) {
				mainTabLayout.selectTab(codeListController);
				focusSkipLists("Value Set Library");
			}
		});
		
		
		MatContext.get().getEventBus().addHandler(EditCodeListEvent.TYPE, new EditCodeListEvent.Handler(){
				@Override
				public void onEditCodeList(EditCodeListEvent event) {
					mainTabLayout.selectTab(codeListController);
					focusSkipLists("Value Set Library");
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
	
	private void loadMatWidgets(){
		//US212 begin updating user sign in time at regular intervals
		MatContext.get().startUserLockUpdate();
		//US154 LOGIN_EVENT
		MatContext.get().recordTransactionEvent(null, null, "LOGIN_EVENT", null, 1);
		
		mainTabLayout = new MatTabLayoutPanel(true);
		mainTabLayout.getElement().setAttribute("id", "matMainTabPanel");
		mainTabLayout.getElement().setAttribute("aria-role", "TabList");
		
		MatContext.get().tabRegistry.put(mainTabLayoutID,mainTabLayout);
		MatContext.get().enableRegistry.put(mainTabLayoutID,this);
		mainTabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			@Override
			public void onSelection(final SelectionEvent<Integer> event) {
				final int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				final String newToken = mainTabLayoutID + index;
				if(!History.getToken().equals(newToken)){
					MatContext.get().recordTransactionEvent(null, null, "MAIN_TAB_EVENT", newToken, 1);
					History.newItem(newToken, false);
				}
			}

			});
		
		String title = ClientConstants.TEXT_THE_TITLE;			
		int tabIndex =0;
		
		currentUserRole = MatContext.get().getLoggedInUserRole();
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			
			
			codeListController = new CodeListController();
			title = ClientConstants.TITLE_VALUE_SET_LIB;	
			tabIndex = mainTabLayout.addPresenter(codeListController, mainTabLayout.fmt.normalTitle(title));
		
			measureLibrary = buildMeasureLibraryWidget(); 
			title = ClientConstants.TITLE_MEASURE_LIB;	
			tabIndex = mainTabLayout.addPresenter(measureLibrary, mainTabLayout.fmt.normalTitle(title));
			
			measureComposer= buildMeasureComposer();
			title = ClientConstants.TITLE_MEASURE_COMPOSER;	
			tabIndex = mainTabLayout.addPresenter(measureComposer, mainTabLayout.fmt.normalTitle(title));
			
			title = ClientConstants.TITLE_MY_ACCOUNT;	
			tabIndex = mainTabLayout.addPresenter(buildMyAccountWidget(), mainTabLayout.fmt.normalTitle(title));
		}
		else if(currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR))
		{
			adminPresenter = buildAdminPresenter();
			title = ClientConstants.TITLE_ADMIN;	
			tabIndex = mainTabLayout.addPresenter(adminPresenter, mainTabLayout.fmt.normalTitle(title));
			
			title = ClientConstants.TITLE_MY_ACCOUNT;	
			tabIndex = mainTabLayout.addPresenter(buildMyAccountWidget(), mainTabLayout.fmt.normalTitle(title));
			
			codeListController = new CodeListController(currentUserRole);
			title = ClientConstants.TITLE_VALUE_SET_CHANGE_OWNERSHIP;	
			tabIndex = mainTabLayout.addPresenter(codeListController, mainTabLayout.fmt.normalTitle(title));
		
			/*measureLibrary = buildMeasureLibraryWidget(); 
			title = ClientConstants.TITLE_MEASURE_LIB_CHANGE_OWNERSHIP;	
			tabIndex = mainTabLayout.addPresenter(measureLibrary, mainTabLayout.fmt.normalTitle(title));*/
			
			
		}
		else {
			Window.alert("Unrecognized user role " + currentUserRole);
			MatContext.get().getEventBus().fireEvent(new LogoffEvent());
		}
			
		
	
		mainTabLayout.setHeight("100%");
		
		Anchor signout = new Anchor(ClientConstants.ANCHOR_SIGN_OUT);
		signout.addClickHandler(new MATClickHandler() {
			
			@Override
			public void onEvent(GwtEvent event) {
				MatContext.get().getEventBus().fireEvent(new LogoffEvent());
			}
		});
	
		getLogoutPanel().add(signout);
		/*
		 * no delay desired when hiding loading message here
		 * tab selection below will fail if loading
		 */
		hideLoadingMessage(0);
		
		// delaying these invocation until after hideLoadingMessage() so these selections are not ignored
		// TODO consider using a forced tab selection
		if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			//mainTabLayout.selectTab(measureLibrary);
			mainTabLayout.selectTab(codeListController);
		}
		else if(currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
			mainTabLayout.selectTab(adminPresenter);
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
				
				public void onLoginFailure(BackToLoginPageEvent event) {
					redirectToLogin();
				}
		});
		MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, new LogoffEvent.Handler() {

			@Override
			public void onLogoff(LogoffEvent event) {
				MatContext.get().getSynchronizationDelegate().setLogOffFlag();
				//US212 no longer update user sign in time and record user sign out time
				MatContext.get().stopUserLockUpdate();
				//US154 SIGN_OUT_EVENT
				MatContext.get().recordTransactionEvent(null, null, "SIGN_OUT_EVENT", null, 1);
				mainTabLayout.close();
				Command isSavingCmd = new Command() {
			    	   public void execute() {
			    		   //CallSignout only if the clauses and measureDetails are not in the process of saving or 
			    		   //if we are on some other tabs,then just call signout without any wait.
			    		   SynchronizationDelegate synchDel = MatContext.get().getSynchronizationDelegate();
			    		  if((!synchDel.isSavingClauses() && !synchDel.isSavingMeasureDetails() 
			    				  && !MatContext.get().getMeasureLockService().isResettingLock()) || 
			    				  !measureComposer.getClauseWorkspace().getAppController().isLoaded()){
			    			  MatContext.get().setCurrentMeasureInfo(null);//This will prevent getting database error after the user has been signed out.
			    			  callSignOut();
			    		  }else{
			    			  DeferredCommand.addCommand(this);
			    		  }
			    	   }
			    	 };
			    	 if(measureComposer != null){ 
			    		 isSavingCmd.execute();
			    	 }else{
			    		 // If you login as Admin, then clicking signout need not have to wait.
			    		 MatContext.get().setCurrentMeasureInfo(null);//This will prevent getting database error after the user has been signed out.
			    		 callSignOut();
			    	 }
				}
		});
		
		MatContext.get().getEventBus().addHandler(TimedOutEvent.TYPE,new TimedOutEvent.Handler() {
			
			@Override
			public void onTimedOut(TimedOutEvent event) {
				if(measureComposer != null){//This if check will prevent admin user getting null pointer exception while timing out
//					mainTabLayout.close(); // User Story change - Removing AutoSave when Timeout.
					//Note: This will select the measureLibrary tab just before the time out Warning.
					//mainTabLayout.selectTab(measureLibrary);
//					mainTabLayout.selectTab(codeListController);//User Story change - Removing AutoSave when Timeout.
					Mat.focusSkipLists("MainContent");
				}
			}
		});
			
		Window.addCloseHandler(new CloseHandler<Window>() {
			
			@Override
			public void onClose(CloseEvent<Window> arg0) {
				if(!MatContext.get().getSynchronizationDelegate().getLogOffFlag()){
					//US212 no longer update user sign in time and record user sign out time
					MatContext.get().stopUserLockUpdate();
					MatContext.get().recordTransactionEvent(null, null, "WINDOW_CLOSE_EVENT", null, ConstantMessages.DB_LOG);
					if(MatContext.get().getCurrentMeasureInfo()!= null)
					    mainTabLayout.close();
					callSignOutWithoutRedirect();
				}
			}
		});
		
		MatContext.get().restartTimeoutWarning();
	}
	
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
	
	
	public static native String getUserAgent() /*-{
	  return navigator.userAgent.toLowerCase();
	}-*/;

	private MeasureComposerPresenter buildMeasureComposer() {
		return new MeasureComposerPresenter();
	}

	
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
	
	private void callSignOutWithoutRedirect(){
		 MatContext.get().getLoginService().signOut(new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable arg0) {}
				@Override
				public void onSuccess(Void arg0) {}
			});
//		 closeBrowser();
	}
	
	private native void closeBrowser()
	/*-{
		$wnd.open('','_self');
	    $wnd.close();
	}-*/;
	
	private MatPresenter buildMyAccountWidget() {
		PersonalInformationView informationView = new PersonalInformationView();
		PersonalInformationPresenter personalInfoPrsnter = new PersonalInformationPresenter(informationView);
		SecurityQuestionsPresenter quesPresenter = new SecurityQuestionsPresenter(new SecurityQuestionsView());
		
		ChangePasswordPresenter passwordPresenter = new ChangePasswordPresenter(new ChangePasswordView());
		
		
		MyAccountPresenter accountPresenter = new MyAccountPresenter(new MyAccountView(personalInfoPrsnter, 
																		quesPresenter, passwordPresenter));
		return accountPresenter;
	}
	
	private MatPresenter buildAdminPresenter() {
		ManageUsersSearchView musd = new ManageUsersSearchView();
		ManageUsersDetailView mudd = new ManageUsersDetailView();
		ManageUsersPresenter mup = 
			new ManageUsersPresenter(musd, mudd);
		
		return mup;
	}
	
	private ManageMeasurePresenter buildMeasureLibraryWidget() {
		ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
		ManageMeasureDetailView measureDetailView = new ManageMeasureDetailView();
		ManageMeasureVersionView versionView = new ManageMeasureVersionView();
		ManageMeasureDraftView measureDraftView = new ManageMeasureDraftView();
		ManageMeasureShareView measureShareView = new ManageMeasureShareView();
		ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();
		ManageMeasureExportView measureExportView;
		if (currentUserRole.equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE)){
			measureExportView = new ManageMeasureExportView(true);
		}else{ 
			measureExportView = new ManageMeasureExportView(false);
		}
		ManageMeasurePresenter measurePresenter = 
			new ManageMeasurePresenter(measureSearchView, measureDetailView, measureShareView, measureExportView,
					historyView,versionView,measureDraftView);
		
		return measurePresenter;
		
	}
		public static void focusSkipLists(String skipstr){
		Widget widget = SkipListBuilder.buildSkipList(skipstr);
		getSkipList().clear();
		getSkipList().add(widget);
		getSkipList().setFocus(true);
	}
	
	class EnterKeyDownHandler implements KeyDownHandler {
		final private int counter;
		public EnterKeyDownHandler(int index){
			counter = index;
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				mainTabLayout.selectTab(counter);
			}
		}
	}
	
	/**
	 * implementing enabled interface
	 * consider adding ui component enablement by invoking setEnabled on the active presenter
	 */
	public void setEnabled(boolean enabled){
		mainTabLayout.setEnabled(enabled);
	}
}
