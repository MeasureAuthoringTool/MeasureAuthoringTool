package mat.client.shared;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mat.client.Enableable;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.AdminServiceAsync;
import mat.client.audit.service.AuditService;
import mat.client.audit.service.AuditServiceAsync;
import mat.client.clause.ClauseService;
import mat.client.clause.ClauseServiceAsync;
import mat.client.clause.QDSCodeListSearchView;
import mat.client.clause.view.DiagramViewImpl;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.login.LoginModel;
import mat.client.login.service.LoginService;
import mat.client.login.service.LoginServiceAsync;
import mat.client.login.service.SessionManagementService;
import mat.client.login.service.SessionManagementServiceAsync;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measurepackage.service.PackageService;
import mat.client.measurepackage.service.PackageServiceAsync;
import mat.client.myAccount.service.MyAccountService;
import mat.client.myAccount.service.MyAccountServiceAsync;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

public class MatContext implements IsSerializable {
	
	private boolean doMeasureLockUpdates = false;
	private boolean doUserLockUpdates = false;
	// how often to perform lock time updates
	private final int lockUpdateTime = 2*60*1000;
	private final int userLockUpdateTime = 2*60*1000;
	
	public static final String PLEASE_SELECT = "--Select--";
	private static final long serialVersionUID = 1L;

	private static MatContext instance = new MatContext();

	private String currentModule;
	
	private LoginServiceAsync loginService;
	
	private MeasureServiceAsync measureService;
	
	private PackageServiceAsync measurePackageService;

	private SessionManagementServiceAsync sessionService;
	
	private AdminServiceAsync adminService;
	
	private MyAccountServiceAsync myAccountService;
	
	private CodeListServiceAsync codeListService;
	
	private HandlerManager eventBus;
	
	private TimeoutManager timeoutManager;
	
	private MeasureSelectedEvent currentMeasureInfo;
	
	private ManageMeasureDetailModel measureDetailModel;
	
	private ListBoxCodeProvider listBoxCodeProvider;
	
	private ClauseServiceAsync clauseService;
	
	private AuditServiceAsync auditService;
	
	private String userId;
	private String userEmail;
	private String loginId;
	private String userRole;
	
	
	private ZoomFactorService zoomFactorService = new ZoomFactorService();
	
	private DiagramViewImpl dviWindow;
	
	private QDSCodeListSearchView qdsView;
	
	private SuccessMessageDisplay successMessage1;
	private SuccessMessageDisplay successMessage2;
	
	private ErrorMessageDisplay errorMessage1;
	private ErrorMessageDisplay errorMessage2;
	
	private SynchronizationDelegate synchronizationDelegate = new SynchronizationDelegate();
	
	public void clearDVIMessages(){
		if(dviWindow != null){
			dviWindow.clearMessages();
		}
		if(qdsView !=null){
			qdsView.getSuccessMessagePanel().clear();
			qdsView.getErrorMessagePanel().clear();
		}
	}
	
	
	public void setQDSView(QDSCodeListSearchView view){
		qdsView=view;
	}
	
	
	//register the Value Set search messages
	//register the property editor messages
	
	public void setErrorMessage1(ErrorMessageDisplay msg){
		this.errorMessage1 = msg;
	}
	
	public void setDVIWindow(DiagramViewImpl window){
		this.dviWindow = window;	
	}
	
	
	public ListBoxCodeProvider getListBoxCodeProvider() {
		return listBoxCodeProvider;
	}

	public void setListBoxCodeProvider(ListBoxCodeProvider listBoxCodeProvider) {
		this.listBoxCodeProvider = listBoxCodeProvider;
	}

	public void setUserInfo(String userId, String userEmail, String userRole,String loginId) {
		this.userId = userId;
		this.userEmail = userEmail;
		this.userRole = userRole;
		this.loginId=loginId;
		//setUserSignInDate(userId);
	}
	protected MatContext(){
		
		GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
			
			@Override
			public void onUncaughtException(Throwable e) {
				GWT.log("An uncaught Exception Occured",e);
				MatContext.this.logException("Uncaught Client Exception",e);
			}
		});
		eventBus = new HandlerManager(null);
		
		eventBus.addHandler(MeasureSelectedEvent.TYPE, new MeasureSelectedEvent.Handler() {
			
			@Override
			public void onMeasureSelected(MeasureSelectedEvent event) {
				currentMeasureInfo = event;
			}
		});
	
		//US 439. Start the timeout timer when the user clicked the forgotten password link
		eventBus.addHandler(ForgottenPasswordEvent.TYPE, new ForgottenPasswordEvent.Handler(){
			@Override
			public void onForgottenPassword(ForgottenPasswordEvent event) {
				getTimeoutManager().startActivityTimers(ConstantMessages.LOGIN_MODULE);
			}			
		});
	}
	
	public HandlerManager getEventBus() {
		return eventBus;
	}
	
	public void logException(String message, Throwable t){
		String s = message + "\r\n" + t.getMessage() +"\r\n";
		StackTraceElement[] elementArr = t.getStackTrace();
		for(StackTraceElement element : elementArr){
			s += element.toString() + "\r\n";
		}
	}
	
	public MyAccountServiceAsync getMyAccountService(){
		if(myAccountService == null){
			myAccountService= (MyAccountServiceAsync) GWT.create(MyAccountService.class);
		}
		return myAccountService;
	}
	
	public CodeListServiceAsync getCodeListService(){
		if(codeListService == null){
			codeListService= (CodeListServiceAsync) GWT.create(CodeListService.class);
		}
		return codeListService;
	}
	
	public AdminServiceAsync getAdminService(){
		if(adminService == null){
			adminService = (AdminServiceAsync) GWT.create(AdminService.class);
		}
		return adminService;
	}
	public LoginServiceAsync getLoginService(){
		if(loginService == null){
			loginService = (LoginServiceAsync) GWT.create(LoginService.class);
		}
		return loginService;
	}
	
	private SessionManagementServiceAsync getSessionService(){
		if(sessionService == null){
			sessionService = (SessionManagementServiceAsync) GWT.create(SessionManagementService.class);
		}
		return sessionService;
	}

	public MeasureServiceAsync getMeasureService(){
		if(measureService == null){
			measureService = (MeasureServiceAsync) GWT.create(MeasureService.class);
		}
		return measureService;
	}
	
	public AuditServiceAsync getAuditService(){
		if(auditService == null){
			auditService = (AuditServiceAsync) GWT.create(AuditService.class);
		}
		return auditService;
	}

	
	public ClauseServiceAsync getClauseService(){
		if(clauseService == null){
			clauseService = (ClauseServiceAsync) GWT.create(ClauseService.class);
		}
		return clauseService;
	}
	
	public PackageServiceAsync getPackageService(){
		if(measurePackageService == null){
			measurePackageService = (PackageServiceAsync) GWT.create(PackageService.class);
		}
		return measurePackageService;
	}
	
	public static MatContext get(){
		return instance;
	}
	
	public String getLoggedInUserRole() {
		return userRole;
	}
	
	public String getLoggedinUserId(){
		return userId;
	}
	public String getLoggedinLoginId(){
		return loginId;
	}
	public String getLoggedInUserEmail() {
		return userEmail;
	}
	public void changePasswordSecurityQuestions(LoginModel model, AsyncCallback<String> asyncCallback) {
		getLoginService().changePasswordSecurityAnswers(model, asyncCallback);
	}
	
	public void isValidUser(String username, String Password,AsyncCallback<LoginModel> callback){
		getLoginService().isValidUser(username, Password, callback);
	}
	
	public void getListBoxData(AsyncCallback<CodeListService.ListBoxData> listBoxCallback){
		getCodeListService().getListBoxData(listBoxCallback);
	}

	public void getCurrentUserRole(AsyncCallback<SessionManagementService.Result> userRoleCallback){
		getSessionService().getCurrentUserRole(userRoleCallback);
	}
	
	
	public void getSecurityQuestions(final AsyncCallback<List<NameValuePair>> callback) {
		String[] questions = new String[] {
			"What is your father's middle name?",
			"What was the name of your first pet?",
			"What was the name of your first school?",
			"In what city were you born?",
			"What was the make of your first car?",
			"What is the name of the company of your first job?",
		};
	
		List<NameValuePair> retList = new ArrayList<NameValuePair>();
		for(int i = 0; i < questions.length; i++) {
			NameValuePair nvp = new NameValuePair();
			nvp.setName(questions[i]);
			nvp.setValue(questions[i]);
			retList.add(nvp);
		}
		callback.onSuccess(retList);
	}
	
	public void restartTimeoutWarning() {
		getTimeoutManager().startActivityTimers(ConstantMessages.MAT_MODULE);
	}

	public String getCurrentMeasureId() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureId();
		}
		else {
			return "";
		}
	}

	public String getCurrentMeasureName() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureName();
		}
		else {
			return "";
		}
	}
	
	public String getCurrentMeasureVersion() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureVersion();
		}
		else {
			return "";
		}
	}

	public void setCurrentMeasureVersion(String s) {
		if(currentMeasureInfo != null) 
			currentMeasureInfo.setMeasureVersion(s);
	}
	
	
	public void setCurrentMeasureScoringType(String s){
		
		if(currentMeasureInfo!=null)
			currentMeasureInfo.setScoringType(s);
	}
	
	public void setCurrentModule(String moduleName){
		this.currentModule = moduleName;
	}
	
	public String getCurrentModule(){
		return currentModule;
	}
	public String getCurrentMeasureScoringType(){
		if(currentMeasureInfo != null){
			return currentMeasureInfo.getScoringType();
		}
		else{
			return "";
		}
	}
	
	public void setCurrentMeasureName(String measureName) {
		if(currentMeasureInfo != null) {
			currentMeasureInfo.setMeasureName(measureName);
		}
	}
	
	public String getCurrentShortName() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getShortName();
		}
		else {
			return "";
		}
	}
	
	public void setCurrentShortName(String shortName) {
		if(currentMeasureInfo != null) {
			currentMeasureInfo.setShortName(shortName);
		}
	}
	
	public boolean isCurrentMeasureEditable() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.isEditable();
		}
		else {
			return false;
		}
	}
	
	public boolean isCurrentMeasureLocked(){
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.isLocked();
		}
		else {
			return false;
		}
	}
	
	public void renewSession() {
		getSessionService().renewSession(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable arg0) {
				Window.alert("Error renewing session " + arg0.getMessage());
			}

			@Override
			public void onSuccess(Void arg0) {
			}
			
		});
	}
	
	public void redirectToHtmlPage(String html) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path=path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.Location.replace(urlBuilder.buildString());
	}
	public void openURL(String html){
		Window.open(html, "User_Guide", "");
		
	}
	
	
	public void openNewHtmlPage(String html) {
		String windowFeatures = "toolbar=no, location=no, personalbar=no, menubar=yes, scrollbars=yes, resizable=yes"; 
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path=path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
//		Window.open(urlBuilder.buildString(),"_self",windowFeatures);
		Window.open(urlBuilder.buildString(),"_blank",windowFeatures);
	}
	
	public void setAriaHidden(Widget widget, Boolean value){
		setAriaHidden(widget, value.toString());
	}
	
	public void setAriaHidden(Widget widget, String value){
		widget.getElement().setAttribute("aria-hidden", value);
	}
	/**
	 * This method is the hub for dynamic visibility for The app.
	 * The setting of aria attributes or styles is delegated to a shared location 
	 * and that logic is invoked where needed.
	 * 
	 * If we need another way of making objects "invisible" then we need to modify 
	 * widget.setVisible(visible);
	 * 
	 * @param widget The widget to be rendered or no longer rendered
	 * @param visible Widget's rendering status
	 */
	public void setVisible(Widget widget, Boolean visible){
		// TODO likely we will change this invocation
		widget.setVisible(visible);
		// disable the widget, maybe best to check if this is a FocusWidget and make an explicit setEnabled call
		DOM.setElementPropertyBoolean(widget.getElement(), "disabled", !visible);
		//NOTE: if (visible = true) then (ARIA not hidden)
		setAriaHidden(widget, !visible);
	}
	
	/*
	 * field used to register TabPanels for history tracking
	 * When instantiating a TabPanel, add a selection handler responsible for adding a History item
	 * to History identifying the TabPanel in the registry. 
		      
	 MatContext.get().tabRegistry.put(MY_TAB_PANEL_ID,tabLayout);
		tabLayout.addSelectionHandler(new SelectionHandler<Integer>(){
		      public void onSelection(SelectionEvent<Integer> event) {
		        History.newItem(MY_TAB_PANEL_ID + event.getSelectedItem(), false);
		      }});

	 */
	
	
	public HashMap enableRegistry = new HashMap<String, Enableable>();
	public HashMap tabRegistry = new HashMap<String, TabPanel>();
	
	public ZoomFactorService getZoomFactorService(){
		return this.zoomFactorService;
	}

	public MeasureSelectedEvent getCurrentMeasureInfo(){
		return currentMeasureInfo;
	}
	
	public void setCurrentMeasureInfo(MeasureSelectedEvent evt){
		this.currentMeasureInfo = evt;
	}
	
	/*
	 * MeasureLock Service --- contains logic to set and release the lock.
	 * 
	 */
	private MeasureLockService measureLockService = new MeasureLockService();
	
	public MeasureLockService getMeasureLockService(){
	    return measureLockService;
	}
	
	/*
	 * Loading queue
	 * used to track loading behavior in the MAT
	 * 
	 * add operation on MainLayout.showLoadingMessage
	 * poll operation on MainLayout.hideLoadingMessage
	 * 
	 * when the queue is empty, then loading is done
	 * NOTE a queue is required because >= 1 add operations could be invoked before a subsequent poll 
	 */
	private MATQueue loadingQueue = new MATQueue();
	public MATQueue getLoadingQueue(){
		return loadingQueue;
	}
	public boolean isLoading(){
		return !getLoadingQueue().isEmpty();
	}
	
	/*
	 * Message store to prevent duplicated messages
	 */
	private MessageDelegate messageDelegate = new MessageDelegate();
	public MessageDelegate getMessageDelegate(){
		return messageDelegate;
	}
	
	public void fireLoadingAlert(){
		Window.alert(MatContext.get().getMessageDelegate().getAlertLoadingMessage());
	}
	
	private TimeoutManager getTimeoutManager(){
		if(timeoutManager == null)
			timeoutManager = new TimeoutManager();
		return timeoutManager;
	}
	
	/**
	 * run a repeating process that updates the current measure lock while flag doMeasureLockUpdates returns true 
	 */
	public void startMeasureLockUpdate(){
		if(!doMeasureLockUpdates){
			doMeasureLockUpdates = true;
			Timer t = new Timer() {
				@Override
				public void run() {
					if(doMeasureLockUpdates){
						getMeasureLockService().setMeasureLock();
					}else{
						//terminate job
						this.cancel();
					}
						
				}
			};
			t.scheduleRepeating(lockUpdateTime);
		}
	}
	/**
	 * set flag doMeasureLockUpdates to false the repeating process will verify based on its value
	 */
	public void stopMeasureLockUpdate(){
		doMeasureLockUpdates = false;
	}
	
	/**
	 * run a repeating process that updates the current measure lock while flag doMeasureLockUpdates returns true 
	 */
	public void startUserLockUpdate(){
		if(!doUserLockUpdates){
			doUserLockUpdates = true;
			Timer t = new Timer() {
				@Override
				public void run() {
					if(doUserLockUpdates){
						setUserSignInDate(getLoggedinUserId());
					}else{
						//terminate job
						this.cancel();
					}
						
				}
			};
			t.scheduleRepeating(userLockUpdateTime);
		}
	}
	public void setUserSignInDate(String userid){
		if(userid != null){
			getMyAccountService().setUserSignInDate(userid, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {}
				@Override
				public void onSuccess(Void result) {}
			});
		}
	}
	/**
	 * set flag doUserLockUpdates to false the repeating process will verify based on its value
	 */
	public void stopUserLockUpdate(){
		doUserLockUpdates = false;
		String userid = getLoggedinUserId();
		if(userid != null){
			getMyAccountService().setUserSignOutDate(userid, new AsyncCallback<Void>() {
				@Override
				public void onFailure(Throwable caught) {}
				@Override
				public void onSuccess(Void result) {}
			});
		}
	}
	
	/*
	 * assuming text is of form *:<<category>>-<<oid>>
	 * where * could contain a -
	 * return text without -<<oid>>
	 */
	public String getTextSansOid(String text) {
		if(text == null)
			return text;
		int d = text.lastIndexOf('-');
		int c = text.lastIndexOf(':');
		if(d > 0 && d > c)
			return text.substring(0,d);
		else
			return text;
	}
	
	public String stripOffOID(String item){
		int idx = item.lastIndexOf('-');
		if(idx < 0)
			return item;
		return item.substring(0,idx).trim();
	}
	
	public void recordTransactionEvent(String primaryId, String secondaryId, String activityType, String additionalInfo, int logLevel){
		String userId = getLoggedinUserId();
		String userEmail = "["+getLoggedInUserEmail()+"] ";
	    getAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId, userEmail+additionalInfo, logLevel, new AsyncCallback<Boolean>(){
			@Override
			public void onFailure(Throwable caught) {}
			@Override
			public void onSuccess(Boolean result) {}
		});
	}

	public SynchronizationDelegate getSynchronizationDelegate() {
		return synchronizationDelegate;
	}
	
	public boolean isAlreadySignedIn(Date lastSignOut, Date lastSignIn, Date current){
		/*
		 * ASSUMPTION: while signed in... lastSignIn is updated every 2 minutes
		 * (1) lastSignOut == null --> see if last sign in time > 3 minutes ago
		 * (2) lastSignOut < lastSignIn --> see if last sign in time > 3 minutes ago
		 * (3)lastSignOut > lastSignIn --> not signed in
		 */
		boolean alreadySignedIn = (lastSignIn == null) ? false : 
			(lastSignOut == null || lastSignOut.before(lastSignIn)) ? 
				(current.getTime() - lastSignIn.getTime() < (3*60*1000)) : 
					false;
		return alreadySignedIn;
	}
	
}
