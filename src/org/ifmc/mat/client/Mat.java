package org.ifmc.mat.client;

import java.util.Date;

import org.ifmc.mat.client.admin.ManageUsersDetailView;
import org.ifmc.mat.client.admin.ManageUsersPresenter;
import org.ifmc.mat.client.admin.ManageUsersSearchView;
import org.ifmc.mat.client.codelist.CodeListController;
import org.ifmc.mat.client.codelist.ListBoxCodeProvider;
import org.ifmc.mat.client.codelist.events.EditCodeListEvent;
import org.ifmc.mat.client.codelist.events.EditGroupedCodeListEvent;
import org.ifmc.mat.client.event.BackToLoginPageEvent;
import org.ifmc.mat.client.event.LogoffEvent;
import org.ifmc.mat.client.event.MATClickHandler;
import org.ifmc.mat.client.event.MeasureEditEvent;
import org.ifmc.mat.client.event.TimedOutEvent;
import org.ifmc.mat.client.login.service.SessionManagementService;
import org.ifmc.mat.client.measure.ManageMeasureDetailView;
import org.ifmc.mat.client.measure.ManageMeasureDraftView;
import org.ifmc.mat.client.measure.ManageMeasureExportView;
import org.ifmc.mat.client.measure.ManageMeasureHistoryView;
import org.ifmc.mat.client.measure.ManageMeasurePresenter;
import org.ifmc.mat.client.measure.ManageMeasureSearchView;
import org.ifmc.mat.client.measure.ManageMeasureShareView;
import org.ifmc.mat.client.measure.ManageMeasureVersionView;
import org.ifmc.mat.client.myAccount.ChangePasswordPresenter;
import org.ifmc.mat.client.myAccount.ChangePasswordView;
import org.ifmc.mat.client.myAccount.MyAccountPresenter;
import org.ifmc.mat.client.myAccount.MyAccountView;
import org.ifmc.mat.client.myAccount.PersonalInformationPresenter;
import org.ifmc.mat.client.myAccount.PersonalInformationView;
import org.ifmc.mat.client.myAccount.SecurityQuestionsPresenter;
import org.ifmc.mat.client.myAccount.SecurityQuestionsView;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.MatTabLayoutPanel;
import org.ifmc.mat.client.shared.SkipListBuilder;
import org.ifmc.mat.client.shared.SynchronizationDelegate;
import org.ifmc.mat.client.shared.ui.MATTabPanel;
import org.ifmc.mat.model.SecurityRole;
import org.ifmc.mat.shared.ConstantMessages;

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
	
	private ListBoxCodeProvider listBoxCodeProvider = new ListBoxCodeProvider();
	private MatTabLayoutPanel mainTabLayout;
	private String MAIN_TAB_LAYOUT_ID;
	private MeasureComposerPresenter measureComposer;
	private ManageMeasurePresenter measureLibrary;
	private	MatPresenter adminPresenter;
	private CodeListController codeListController;
	
	
	String CurrentUserRole = "NotLoggedInYet";
	
	@SuppressWarnings("rawtypes")
	private  final AsyncCallback<SessionManagementService.Result> userRoleCallback = new AsyncCallback<SessionManagementService.Result>(){

		@Override
		public void onFailure(Throwable caught) {
			redirectToLogin();
//			Window.alert("User Role is not set in the session");
		}

		public void onSuccess(SessionManagementService.Result result) {
			if(result == null){
				redirectToLogin();
			}else{
				Date lastSignIn = result.signInDate;
				Date lastSignOut = result.signOutDate;
				Date current = new Date();
				boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);
				if(isAlreadySignedIn)
					redirectToLogin();
				else{
					MatContext.get().setUserSignInDate(result.userId);
					MatContext.get().setUserInfo(result.userId, result.userEmail, result.userRole);
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
		MAIN_TAB_LAYOUT_ID = ConstantMessages.MAIN_TAB_LAYOUT_ID; 
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
			public void onValueChange(ValueChangeEvent<String> event) {
				String historyToken = event.getValue();
				
				    if(historyToken == null || historyToken.isEmpty()){
				    	History.newItem(MAIN_TAB_LAYOUT_ID + 0, false);
					}
				    else if(!MatContext.get().isLoading())   {
				        // Parse the history token
				    	
				        try {
							for(Object key : MatContext.get().tabRegistry.keySet()){
								if(key instanceof String){
									String k = (String) key;
									  if(historyToken.contains(k)){
										  String tabIndexToken = historyToken.substring(k.length());
										  int tabIndex = Integer.parseInt(tabIndexToken);
										  MATTabPanel tp = (MATTabPanel) MatContext.get().tabRegistry.get(key);
										  /* Suppressing selection of MAIN_TAB_LAYOUT_ID+mainTabLayout.selectedIndex
										   * if already selected
										   */
										  if(!History.getToken().equals(MAIN_TAB_LAYOUT_ID+mainTabLayout.getSelectedIndex()))
											  tp.selectTab(tabIndex);
									}
								}
							}
				        } catch (IndexOutOfBoundsException e) {
				        	History.newItem(MAIN_TAB_LAYOUT_ID + 0, false);
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
			public void onMeasureEdit(MeasureEditEvent event) {
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
		
		MatContext.get().tabRegistry.put(MAIN_TAB_LAYOUT_ID,mainTabLayout);
		MatContext.get().enableRegistry.put(MAIN_TAB_LAYOUT_ID,this);
		mainTabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				int index = ((SelectionEvent<Integer>) event).getSelectedItem();
				// suppressing token dup
				String newToken = MAIN_TAB_LAYOUT_ID + index;
				if(!History.getToken().equals(newToken)){
					MatContext.get().recordTransactionEvent(null, null, "MAIN_TAB_EVENT", newToken, 1);
					History.newItem(newToken, false);
				}
			}

			});
		
		String title = "The Title";			
		int tabIndex =0;
		
		CurrentUserRole = MatContext.get().getLoggedInUserRole();
		if(!CurrentUserRole.equalsIgnoreCase("Administrator")){
			
			
			codeListController = new CodeListController();
			title = "Value Set Library";	
			tabIndex = mainTabLayout.addPresenter(codeListController, mainTabLayout.fmt.normalTitle(title));
		
			measureLibrary = buildMeasureLibraryWidget(); 
			title = "Measure Library";	
			tabIndex = mainTabLayout.addPresenter(measureLibrary, mainTabLayout.fmt.normalTitle(title));
			
			measureComposer= buildMeasureComposer();
			title = "Measure Composer";	
			tabIndex = mainTabLayout.addPresenter(measureComposer, mainTabLayout.fmt.normalTitle(title));
		}
		else if(CurrentUserRole.equalsIgnoreCase("Administrator"))
		{
			adminPresenter = buildAdminPresenter();
			
			title = "Admin";	
			
			tabIndex = mainTabLayout.addPresenter(adminPresenter, mainTabLayout.fmt.normalTitle(title));
		}
		else {
			Window.alert("Unrecognized user role " + CurrentUserRole);
			MatContext.get().getEventBus().fireEvent(new LogoffEvent());
		}
			
		title = "My Account";	
		tabIndex = mainTabLayout.addPresenter(buildMyAccountWidget(), mainTabLayout.fmt.normalTitle(title));
	
		mainTabLayout.setHeight("100%");
		
		Anchor signout = new Anchor("Sign Out");
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
		if(!CurrentUserRole.equalsIgnoreCase("Administrator")){
			//mainTabLayout.selectTab(measureLibrary);
			mainTabLayout.selectTab(codeListController);
		}
		else if(CurrentUserRole.equalsIgnoreCase("Administrator")){
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
		if(getUserAgent().contains("msie")){
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
					mainTabLayout.close();
					//Note: This will select the measureLibrary tab just before the time out Warning.
					//mainTabLayout.selectTab(measureLibrary);
					mainTabLayout.selectTab(codeListController);
					
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
		Timer t = new Timer() {
			@Override
			public void run() {
				MatContext.get().redirectToHtmlPage("/Login.html");
			}
		};
		t.schedule(1000);
		
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
		PersonalInformationView piv = 
			new PersonalInformationView();
		PersonalInformationPresenter pip = 
			new PersonalInformationPresenter(piv);
		
		SecurityQuestionsPresenter sqp = 
			new SecurityQuestionsPresenter(new SecurityQuestionsView());
		
		ChangePasswordPresenter cpp = 
			new ChangePasswordPresenter(new ChangePasswordView());
		
		
		MyAccountPresenter p = 
			new MyAccountPresenter(new MyAccountView(pip, sqp, cpp));
		return p;
	}
	
	private MatPresenter buildAdminPresenter() {
		ManageUsersSearchView musd = new ManageUsersSearchView();
		ManageUsersDetailView mudd = new ManageUsersDetailView();
		ManageUsersPresenter mup = 
			new ManageUsersPresenter(musd, mudd);
		
		return mup;
	}
	
	private ManageMeasurePresenter buildMeasureLibraryWidget() {
		ManageMeasureSearchView musd = new ManageMeasureSearchView();
		ManageMeasureDetailView mudd = new ManageMeasureDetailView();
		ManageMeasureVersionView mmVV = new ManageMeasureVersionView();
		ManageMeasureDraftView mmDV = new ManageMeasureDraftView();
		ManageMeasureShareView mmsv = new ManageMeasureShareView();
		ManageMeasureHistoryView mmhv = new ManageMeasureHistoryView();
		ManageMeasureExportView mmev;
		if (CurrentUserRole.equalsIgnoreCase(SecurityRole.SUPER_USER_ROLE))
			 mmev = new ManageMeasureExportView(true);
		else 
			mmev = new ManageMeasureExportView(false);
		ManageMeasurePresenter mup = 
			new ManageMeasurePresenter(musd, mudd, mmsv, mmev,mmhv,mmVV,mmDV);
		
		return mup;
		
	}
		public static void focusSkipLists(String skipstr){
		Widget w = SkipListBuilder.buildSkipList(skipstr);
		getSkipList().clear();
		getSkipList().add(w);
		getSkipList().setFocus(true);
	}
	
	class EnterKeyDownHandler implements KeyDownHandler {
		private int i = 0;
		public EnterKeyDownHandler(int index){
			i = index;
		}
		@Override
		public void onKeyDown(KeyDownEvent event) {
			if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				mainTabLayout.selectTab(i);
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
