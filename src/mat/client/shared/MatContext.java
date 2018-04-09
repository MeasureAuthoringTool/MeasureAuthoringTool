package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

import mat.DTO.OperatorDTO;
import mat.client.Enableable;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.AdminServiceAsync;
import mat.client.audit.service.AuditService;
import mat.client.audit.service.AuditServiceAsync;
import mat.client.clause.QDMAppliedSelectionView;
import mat.client.clause.QDMAvailableValueSetWidget;
import mat.client.clause.QDSAppliedListView;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.QDSCodeListSearchView;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.cqlconstant.service.CQLConstantServiceAsync;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.login.LoginModel;
import mat.client.login.service.LoginResult;
import mat.client.login.service.LoginService;
import mat.client.login.service.LoginServiceAsync;
import mat.client.login.service.SessionManagementService;
import mat.client.login.service.SessionManagementServiceAsync;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchView;
import mat.client.measure.service.CQLLibraryService;
import mat.client.measure.service.CQLLibraryServiceAsync;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.MeasureServiceAsync;
import mat.client.measurepackage.service.PackageService;
import mat.client.measurepackage.service.PackageServiceAsync;
import mat.client.myAccount.service.MyAccountService;
import mat.client.myAccount.service.MyAccountServiceAsync;
import mat.client.population.service.PopulationService;
import mat.client.population.service.PopulationServiceAsync;
import mat.client.umls.service.VSACAPIService;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.ClientConstants;
import mat.model.GlobalCopyPasteObject;
import mat.model.VSACExpansionProfile;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLIdentifierObject;
import mat.shared.ConstantMessages;

/**
 * The Class MatContext.
 */
public class MatContext implements IsSerializable {
	
	/** The is umls logged in. */
	private boolean isUMLSLoggedIn = false;
	
	/** The do library lock updates. */
	private boolean doLibraryLockUpdates = false;

	/** The do measure lock updates. */
	private boolean doMeasureLockUpdates = false;
	
	/** The do user lock updates. */
	private boolean doUserLockUpdates = false;
	// how often to perform lock time updates
	/** The lock update time. */
	private final int lockUpdateTime = 2*60*1000;
	
	/** The user lock update time. */
	private final int userLockUpdateTime = 2*60*1000;
	
	/** The Constant PLEASE_SELECT. */
	public static final String PLEASE_SELECT = "--Select--";
		
	/** The instance. */
	private static MatContext instance = new MatContext();
	
	/** The current module. */
	private String currentModule;
	
	/** The login service. */
	private LoginServiceAsync loginService;
	
	/** The measure service. */
	private MeasureServiceAsync measureService;
	
	/** The cql constant service. */
	private CQLConstantServiceAsync cqlConstantService; 
	
	/** The cql library service. */
	private CQLLibraryServiceAsync cqlLibraryService;
	
	/** The measure package service. */
	private PackageServiceAsync measurePackageService;
	
	/** The session service. */
	private SessionManagementServiceAsync sessionService;
	
	/** The admin service. */
	private AdminServiceAsync adminService;
	
	/** The my account service. */
	private MyAccountServiceAsync myAccountService;
	
	/** The code list service. */
	private CodeListServiceAsync codeListService;
	
	/** The vsacapi service async. */
	private VSACAPIServiceAsync vsacapiServiceAsync;
	
	/** The qds attributes service async. */
	private QDSAttributesServiceAsync qdsAttributesServiceAsync;
	
	/** The Population service. */
	private PopulationServiceAsync populationService;
	
	/** The event bus. */
	private HandlerManager eventBus;
	
	/** The timeout manager. */
	private TimeoutManager timeoutManager;
	
	/** The current measure info. */
	private MeasureSelectedEvent currentMeasureInfo;
	
	/** The current library info. */
	private CQLLibrarySelectedEvent currentLibraryInfo;
	
	/** The is measure deleted. */
	private boolean isMeasureDeleted;
	
	/** The list box code provider. */
	private ListBoxCodeProvider listBoxCodeProvider;
	
	/** The audit service. */
	private AuditServiceAsync auditService;
	
	/** The user id. */
	private String userId;
	
	/** The user email. */
	private String userEmail;
	
	/** The login id. */
	private String loginId;
	
	/** The user role. */
	private String userRole;
	
	/** The qds view. */
	private QDSCodeListSearchView qdsView;
	
	/** The vsac profile view. */
	private QDMAppliedSelectionView qdmAppliedSelectionView;
	
	/** The modify qdm pop up widget. */
	private QDMAvailableValueSetWidget modifyQDMPopUpWidget;
	
	/** The manage measure search view. */
	private ManageMeasureSearchView manageMeasureSearchView;
		
	/** The manage measure search model. */
	private ManageMeasureSearchModel manageMeasureSearchModel;
		
	/** The synchronization delegate. */
	private SynchronizationDelegate synchronizationDelegate = new SynchronizationDelegate();
	
	/** The error tab index. */
	private int errorTabIndex;
	
	/** The is error tab. */
	private boolean isErrorTab;
	
	/** The timings. */
	public List<String> timings = new ArrayList<String>();
	
	/** The functions. */
	public List<String> functions = new ArrayList<String>();
	
	/** The relationships. */
	public List<String> relationships = new ArrayList<String>();
	
	/** The comparison ops. */
	public List<String> comparisonOps = new ArrayList<String>();
	
	/** The logical ops. */
	public List<String> logicalOps = new ArrayList<String>();
	
	/** The set ops. */
	public List<String> setOps = new ArrayList<String>();
	
	/** The operator map key short. */
	public Map<String, String> operatorMapKeyShort = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
	
	/** The operator map key long. */
	public Map<String, String> operatorMapKeyLong = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
	
	/** The removed relationship types. */
	public Map<String, String> removedRelationshipTypes = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
		
	
	/** The global copy paste. */
	private GlobalCopyPasteObject globalCopyPaste;
	
	
	/** The valuesets. */
	public List<CQLIdentifierObject> valuesets = new ArrayList<CQLIdentifierObject>();
	
	/** The value set code quality data set list. */
	public List<CQLQualityDataSetDTO> valueSetCodeQualityDataSetList = new ArrayList<CQLQualityDataSetDTO>();
	
	/** The definitions. */
	public List<CQLIdentifierObject> definitions = new ArrayList<CQLIdentifierObject>(); 
	
	/** The parameters. */
	public List<CQLIdentifierObject> parameters = new ArrayList<CQLIdentifierObject>();
		
	/** The funcs. */
	public List<CQLIdentifierObject> funcs = new ArrayList<CQLIdentifierObject>();
	
	/** The includes. */
	public List<String> includes = new ArrayList<String>();
		
	/** The included def names. */
	private List<CQLIdentifierObject> includedDefNames = new ArrayList<CQLIdentifierObject>();
	
	/** The included func names. */
	private List<CQLIdentifierObject> includedFuncNames = new ArrayList<CQLIdentifierObject>();
	
	/** The included value set names. */
	private List<CQLIdentifierObject> includedValueSetNames = new ArrayList<CQLIdentifierObject>();
	
	/** The included param names. */
	private List<CQLIdentifierObject> includedParamNames = new ArrayList<CQLIdentifierObject>();
	
	/** The included code names. */
	private List<CQLIdentifierObject> includedCodeNames = new ArrayList<CQLIdentifierObject>();
	
	/** The units list. */
	private List<String> shorcutKeyUnits = new ArrayList<String>();
	
	/** The cql constant container. */
	private CQLConstantContainer cqlConstantContainer = new CQLConstantContainer(); 
	
	//VSAC Programs and Releases 
	private HashMap<String, List<String>> programToReleases = new HashMap<>();

	/**
	 * Clear dvi messages.
	 */
	public void clearDVIMessages(){
		if(qdsView !=null){
			qdsView.getSuccessMessageDisplay().clear();
			qdsView.getErrorMessageDisplay().clear();
		}
	}
	
	
	/**
	 * Clear modify pop up messages.
	 */
	public void clearModifyPopUpMessages(){
		if(modifyQDMPopUpWidget !=null){
			modifyQDMPopUpWidget.getApplyToMeasureSuccessMsg().clear();
			modifyQDMPopUpWidget.getErrorMessageDisplay().clear();
			modifyQDMPopUpWidget.getSuccessMessagePanel().clear();
			modifyQDMPopUpWidget.getErrorMessagePanel().clear();
		}
	}
	
	/**
	 * Sets the qDS view.
	 * 
	 * @param view
	 *            the new qDS view
	 */
	public void setQDSView(QDSCodeListSearchView view){
		qdsView=view;
	}
	
	/**
	 * Sets the VSAC profile view.
	 *
	 * @param view the new VSAC profile view
	 */
	public void setQDMAppliedSelectionView(QDMAppliedSelectionView view){
		qdmAppliedSelectionView = view;
	}
	
	/**
	 * Sets the qds applied list view.
	 * 
	 * @param qdsAppliedListView
	 *            the new qds applied list view
	 */
	public void setQdsAppliedListView(QDSAppliedListView qdsAppliedListView) {
	}
	
	/**
	 * Sets the error message1.
	 * 
	 * @param msg
	 *            the new error message1
	 */
	public void setErrorMessage1(ErrorMessageDisplay msg){
	}
	
	
	/**
	 * Gets the list box code provider.
	 * 
	 * @return the list box code provider
	 */
	public ListBoxCodeProvider getListBoxCodeProvider() {
		return listBoxCodeProvider;
	}
	
	/**
	 * Sets the list box code provider.
	 * 
	 * @param listBoxCodeProvider
	 *            the new list box code provider
	 */
	public void setListBoxCodeProvider(ListBoxCodeProvider listBoxCodeProvider) {
		this.listBoxCodeProvider = listBoxCodeProvider;
	}
	
	/**
	 * Sets the user info.
	 * 
	 * @param userId
	 *            the user id
	 * @param userEmail
	 *            the user email
	 * @param userRole
	 *            the user role
	 * @param loginId
	 *            the login id
	 */
	public void setUserInfo(String userId, String userEmail, String userRole,String loginId) {
		this.userId = userId;
		this.userEmail = userEmail;
		this.userRole = userRole;
		this.loginId=loginId;
	}
	
	/**
	 * Instantiates a new mat context.
	 */
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
		
		eventBus.addHandler(CQLLibrarySelectedEvent.TYPE, new CQLLibrarySelectedEvent.Handler() {
			
			@Override
			public void onLibrarySelected(CQLLibrarySelectedEvent event) {
				currentLibraryInfo = event;
				
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
	
	/**
	 * Gets the event bus.
	 * 
	 * @return the event bus
	 */
	public HandlerManager getEventBus() {
		return eventBus;
	}
	
	/**
	 * Log exception.
	 * 
	 * @param message
	 *            the message
	 * @param t
	 *            the t
	 */
	public void logException(String message, Throwable t) {
		StackTraceElement[] elementArr = t.getStackTrace();
		for (StackTraceElement element : elementArr) {
			element.toString().concat("\r\n");
		}
	}
	
	/**
	 * Gets the my account service.
	 * 
	 * @return the my account service
	 */
	public MyAccountServiceAsync getMyAccountService(){
		if(myAccountService == null){
			myAccountService= (MyAccountServiceAsync) GWT.create(MyAccountService.class);
		}
		return myAccountService;
	}
	
	/**
	 * Gets the code list service.
	 * 
	 * @return the code list service
	 */
	public CodeListServiceAsync getCodeListService(){
		if(codeListService == null){
			codeListService= (CodeListServiceAsync) GWT.create(CodeListService.class);
		}
		return codeListService;
	}
	
	/**
	 * Gets the admin service.
	 * 
	 * @return the admin service
	 */
	public AdminServiceAsync getAdminService(){
		if(adminService == null){
			adminService = (AdminServiceAsync) GWT.create(AdminService.class);
		}
		return adminService;
	}
	
	/**
	 * Gets the login service.
	 * 
	 * @return the login service
	 */
	public LoginServiceAsync getLoginService(){
		if(loginService == null){
			loginService = (LoginServiceAsync) GWT.create(LoginService.class);
		}
		return loginService;
	}
	
	/**
	 * Gets the vsacapi service async.
	 * 
	 * @return the vsacapi service async
	 */
	public VSACAPIServiceAsync getVsacapiServiceAsync() {
		if(vsacapiServiceAsync == null){
			vsacapiServiceAsync = (VSACAPIServiceAsync) GWT.create(VSACAPIService.class);
		}
		return vsacapiServiceAsync;
	}
	
	
	/**
	 * Sets the vsacapi service async.
	 * 
	 * @param vsacapiServiceAsync
	 *            the new vsacapi service async
	 */
	public void setVsacapiServiceAsync(VSACAPIServiceAsync vsacapiServiceAsync) {
		this.vsacapiServiceAsync = vsacapiServiceAsync;
	}
	
	
	/**
	 * Gets the session service.
	 * 
	 * @return the session service
	 */
	private SessionManagementServiceAsync getSessionService(){
		if(sessionService == null){
			sessionService = (SessionManagementServiceAsync) GWT.create(SessionManagementService.class);
		}
		return sessionService;
	}
	
	/**
	 * Gets the CQL contant service.
	 *
	 * @return the CQL contant service
	 */
	public CQLConstantServiceAsync getCQLContantService() {
		
		if(cqlConstantService == null) {
			cqlConstantService = (CQLConstantServiceAsync) GWT.create(CQLConstantService.class);
		}
		
		return cqlConstantService; 
	}
	
	/**
	 * Gets the measure service.
	 * 
	 * @return the measure service
	 */
	public MeasureServiceAsync getMeasureService(){
		if(measureService == null){
			measureService = (MeasureServiceAsync) GWT.create(MeasureService.class);
		}
		return measureService;
	}
	
	/**
	 * Gets the CQL library service.
	 *
	 * @return the CQL library service
	 */
	public CQLLibraryServiceAsync getCQLLibraryService(){
		if(cqlLibraryService == null){
			cqlLibraryService = (CQLLibraryServiceAsync)GWT.create(CQLLibraryService.class);
			
		}
		return cqlLibraryService;
		
	}
	
	/**
	 * Gets the audit service.
	 * 
	 * @return the audit service
	 */
	public AuditServiceAsync getAuditService(){
		if(auditService == null){
			auditService = (AuditServiceAsync) GWT.create(AuditService.class);
		}
		return auditService;
	}
	
	
	/**
	 * Gets the package service.
	 * 
	 * @return the package service
	 */
	public PackageServiceAsync getPackageService(){
		if(measurePackageService == null){
			measurePackageService = (PackageServiceAsync) GWT.create(PackageService.class);
		}
		return measurePackageService;
	}
	
	/**
	 * Gets the.
	 * 
	 * @return the mat context
	 */
	public static MatContext get(){
		return instance;
	}
	
	/**
	 * Gets the logged in user role.
	 * 
	 * @return the logged in user role
	 */
	public String getLoggedInUserRole() {
		return userRole;
	}
	
	/**
	 * Gets the loggedin user id.
	 * 
	 * @return the loggedin user id
	 */
	public String getLoggedinUserId(){
		return userId;
	}
	
	/**
	 * Gets the loggedin login id.
	 * 
	 * @return the loggedin login id
	 */
	public String getLoggedinLoginId(){
		return loginId;
	}
	
	/**
	 * Gets the logged in user email.
	 * 
	 * @return the logged in user email
	 */
	public String getLoggedInUserEmail() {
		return userEmail;
	}
	
	/**
	 * Change password security questions.
	 * 
	 * @param model
	 *            the model
	 * @param asyncCallback
	 *            the async callback
	 */
	public void changePasswordSecurityQuestions(LoginModel model, AsyncCallback<LoginResult> asyncCallback) {
		getLoginService().changePasswordSecurityAnswers(model, asyncCallback);
	}
	
	/**
	 * Checks if is valid user.
	 *
	 * @param username            the username
	 * @param Password            the password
	 * @param oneTimePassword the one time password
	 * @param callback            the callback
	 */
	public void isValidUser(String username, String Password, String oneTimePassword, AsyncCallback<LoginModel> callback){
		getLoginService().isValidUser(username, Password, oneTimePassword, callback);
	}
	
	/**
	 * Gets the list box data.
	 * 
	 * @param listBoxCallback
	 *            the list box callback
	 * @return the list box data
	 */
	public void getListBoxData(AsyncCallback<CodeListService.ListBoxData> listBoxCallback){
		getCodeListService().getListBoxData(listBoxCallback);
	}
	
	/**
	 * Gets the current user role.
	 * 
	 * @param userRoleCallback
	 *            the user role callback
	 * @return the current user role
	 */
	public void getCurrentUserRole(AsyncCallback<SessionManagementService.Result> userRoleCallback){
		getSessionService().getCurrentUserRole(userRoleCallback);
	}
	
	
	/**
	 * Restart timeout warning.
	 */
	public void restartTimeoutWarning() {
		getTimeoutManager().startActivityTimers(ConstantMessages.MAT_MODULE);
	}
	
	/**
	 * Restart umls signout.
	 */
	public void restartUMLSSignout() {
		getTimeoutManager().startUMLSTimer();
	}
	
	/**
	 * Gets the current measure id.
	 * 
	 * @return the current measure id
	 */
	public String getCurrentMeasureId() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureId();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current measure name.
	 * 
	 * @return the current measure name
	 */
	public String getCurrentMeasureName() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureName();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current measure version.
	 * 
	 * @return the current measure version
	 */
	public String getCurrentMeasureVersion() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getMeasureVersion();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Sets the current measure version.
	 * 
	 * @param s
	 *            the new current measure version
	 */
	public void setCurrentMeasureVersion(String s) {
		if(currentMeasureInfo != null) {
			currentMeasureInfo.setMeasureVersion(s);
		}
	}
	
	
	/**
	 * Sets the current measure scoring type.
	 * 
	 * @param s
	 *            the new current measure scoring type
	 */
	public void setCurrentMeasureScoringType(String s){
		
		if(currentMeasureInfo!=null) {
			currentMeasureInfo.setScoringType(s);
		}
	}
	
	/**
	 * Sets the current module.
	 * 
	 * @param moduleName
	 *            the new current module
	 */
	public void setCurrentModule(String moduleName){
		currentModule = moduleName;
	}
	
	/**
	 * Gets the current module.
	 * 
	 * @return the current module
	 */
	public String getCurrentModule(){
		return currentModule;
	}
	
	/**
	 * Gets the current measure scoring type.
	 * 
	 * @return the current measure scoring type
	 */
	public String getCurrentMeasureScoringType(){
		if(currentMeasureInfo != null){
			return currentMeasureInfo.getScoringType();
		}
		else{
			return "";
		}
	}
	
	/**
	 * Sets the current measure name.
	 * 
	 * @param measureName
	 *            the new current measure name
	 */
	public void setCurrentMeasureName(String measureName) {
		if(currentMeasureInfo != null) {
			currentMeasureInfo.setMeasureName(measureName);
		}
	}
	
	/**
	 * Gets the current short name.
	 * 
	 * @return the current short name
	 */
	public String getCurrentShortName() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.getShortName();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Sets the current short name.
	 * 
	 * @param shortName
	 *            the new current short name
	 */
	public void setCurrentShortName(String shortName) {
		if(currentMeasureInfo != null) {
			currentMeasureInfo.setShortName(shortName);
		}
	}
	
	/**
	 * Checks if is current measure editable.
	 * 
	 * @return true, if is current measure editable
	 */
	public boolean isCurrentMeasureEditable() {
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.isEditable();
		}
		else {
			return false;
		}
	}

	/**
	 * Checks if is current library editable.
	 * 
	 * @return true, if is current library editable
	 */
	public boolean isCurrentLibraryEditable() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.isEditable();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if is current measure locked.
	 * 
	 * @return true, if is current measure locked
	 */
	public boolean isCurrentMeasureLocked(){
		if(currentMeasureInfo != null) {
			return currentMeasureInfo.isLocked();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Checks if is current library locked.
	 * 
	 * @return true, if is current library locked
	 */
	public boolean isCurrentLibraryLocked(){
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.isLocked();
		}
		else {
			return false;
		}
	}
	
	/**
	 * Renew session.
	 */
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
	
	/**
	 * Redirect to html page.
	 * 
	 * @param html
	 *            the html
	 */
	public void redirectToHtmlPage(String html) {
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path=path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.Location.replace(urlBuilder.buildString());
	}
	
	/**
	 * Open url.
	 * 
	 * @param html
	 *            the html
	 */
	public void openURL(String html){
		Window.open(html, "User_Guide", "");
		
	}
	
	
	/**
	 * Open new html page.
	 * 
	 * @param html
	 *            the html
	 */
	public void openNewHtmlPage(String html) {
		String windowFeatures = "toolbar=no, location=no, personalbar=no, menubar=yes, scrollbars=yes, resizable=yes";
		UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
		String path = Window.Location.getPath();
		path=path.substring(0, path.lastIndexOf('/'));
		path += html;
		urlBuilder.setPath(path);
		Window.open(urlBuilder.buildString(),"_blank",windowFeatures);
	}
	
	/**
	 * Sets the aria hidden.
	 * 
	 * @param widget
	 *            the widget
	 * @param value
	 *            the value
	 */
	public void setAriaHidden(Widget widget, Boolean value){
		setAriaHidden(widget, value.toString());
	}
	
	/**
	 * Sets the aria hidden.
	 * 
	 * @param widget
	 *            the widget
	 * @param value
	 *            the value
	 */
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
		widget.setVisible(visible);
		// disable the widget, maybe best to check if this is a FocusWidget and make an explicit setEnabled call
		DOM.setElementPropertyBoolean(widget.getElement(), "disabled", !visible);
		//NOTE: if (visible = true) then (ARIA not hidden)
		setAriaHidden(widget, !visible);
	}
	
	/** The enable registry. */
	public HashMap enableRegistry = new HashMap<String, Enableable>();
	
	/** The tab registry. */
	public HashMap tabRegistry = new HashMap<String, TabPanel>();
	
	/**
	 * Gets the current measure info.
	 * 
	 * @return the current measure info
	 */
	public MeasureSelectedEvent getCurrentMeasureInfo(){
		return currentMeasureInfo;
	}
	
	/**
	 * Gets the current library info.
	 *
	 * @return the current library info
	 */
	public CQLLibrarySelectedEvent getCurrentLibraryInfo() {
		return currentLibraryInfo;
	}


	/**
	 * Sets the current library info.
	 *
	 * @param currentLibraryInfo the new current library info
	 */
	public void setCurrentLibraryInfo(CQLLibrarySelectedEvent currentLibraryInfo) {
		this.currentLibraryInfo = currentLibraryInfo;
	}


	/**
	 * Sets the current measure info.
	 * 
	 * @param evt
	 *            the new current measure info
	 */
	public void setCurrentMeasureInfo(MeasureSelectedEvent evt){
		currentMeasureInfo = evt;
	}
	
	/*
	 * MeasureLock Service --- contains logic to set and release the lock.
	 *
	 */
	/** The measure lock service. */
	private MeasureLockService measureLockService = new MeasureLockService();
	
	/**
	 * Gets the measure lock service.
	 * 
	 * @return the measure lock service
	 */
	public MeasureLockService getMeasureLockService(){
		return measureLockService;
	}
	
	/** The library lock service. */
	private LibraryLockService libraryLockService = new LibraryLockService();
	
	/**
	 * Gets the library lock service.
	 * 
	 * @return the library lock service
	 */
	public LibraryLockService getLibraryLockService(){
		return libraryLockService;
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
	/** The loading queue. */
	private MATQueue loadingQueue = new MATQueue();
	
	/**
	 * Gets the loading queue.
	 * 
	 * @return the loading queue
	 */
	public MATQueue getLoadingQueue(){
		return loadingQueue;
	}
	
	/**
	 * Checks if is loading.
	 * 
	 * @return true, if is loading
	 */
	public boolean isLoading(){
		return !getLoadingQueue().isEmpty();
	}
	
	/*
	 * Message store to prevent duplicated messages
	 */
	/** The message delegate. */
	private MessageDelegate messageDelegate = new MessageDelegate();
	
	/**
	 * Gets the message delegate.
	 * 
	 * @return the message delegate
	 */
	public MessageDelegate getMessageDelegate(){
		return messageDelegate;
	}
	
	/**
	 * Fire loading alert.
	 */
	public void fireLoadingAlert(){
		Window.alert(MatContext.get().getMessageDelegate().getAlertLoadingMessage());
	}
	
	/**
	 * Gets the timeout manager.
	 * 
	 * @return the timeout manager
	 */
	private TimeoutManager getTimeoutManager(){
		if (timeoutManager == null){
			timeoutManager = new TimeoutManager();
		}
		return timeoutManager;
	}
	
	/**
	 * run a repeating process that updates the current measure lock while flag doMeasureLockUpdates returns true.
	 */
	public void startMeasureLockUpdate(){
		if (!doMeasureLockUpdates) {
			doMeasureLockUpdates = true;
			Timer t = new Timer() {
				@Override
				public void run() {
					if (doMeasureLockUpdates) {
						getMeasureLockService().setMeasureLock();
					} else {
						//terminate job
						this.cancel();
					}
					
				}
			};
			t.scheduleRepeating(lockUpdateTime);
		}
	}
	/**
	 * set flag doMeasureLockUpdates to false the repeating process will verify based on its value.
	 */
	public void stopMeasureLockUpdate(){
		doMeasureLockUpdates = false;
	}
	
	/**
	 * run a repeating process that updates the current library lock while flag doLibraryLockUpdates returns true.
	 */
	public void startLibraryLockUpdate(){
		if (!doLibraryLockUpdates) {
			doLibraryLockUpdates = true;
			Timer t = new Timer() {
				@Override
				public void run() {
					if (doLibraryLockUpdates) {
						getLibraryLockService().setLibraryLock();
					} else {
						//terminate job
						this.cancel();
					}
					
				}
			};
			t.scheduleRepeating(lockUpdateTime);
		}
	}
	/**
	 * set flag doLibraryLockUpdates to false the repeating process will verify based on its value.
	 */
	public void stopLibraryLockUpdate(){
		doLibraryLockUpdates = false;
	}
	
	/**
	 * run a repeating process that updates the current measure lock while flag doMeasureLockUpdates returns true.
	 */
	public void startUserLockUpdate(){
		if (!doUserLockUpdates) {
			doUserLockUpdates = true;
			Timer t = new Timer() {
				@Override
				public void run() {
					if (doUserLockUpdates) {
						setUserSignInDate(getLoggedinUserId());
					} else {
						//terminate job
						this.cancel();
					}
					
				}
			};
			t.scheduleRepeating(userLockUpdateTime);
		}
	}
	
	/**
	 * Sets the user sign in date.
	 * 
	 * @param userid
	 *            the new user sign in date
	 */
	public void setUserSignInDate(String userid) {
		if (userid != null) {
			getMyAccountService().setUserSignInDate(userid, new AsyncCallback<Void>() {
				@Override
				public void onFailure(final Throwable caught) { }
				@Override
				public void onSuccess(final Void result) { }
			});
		}
	}
	
	/**
	 * set flag doUserLockUpdates to false the repeating process will verify
	 * based on its value.
	 */
	public void stopUserLockUpdate() {
		doUserLockUpdates = false;
		String userid = getLoggedinUserId();
		if (userid != null) {
			getMyAccountService().setUserSignOutDate(userid, new AsyncCallback<Void>() {
				@Override
				public void onFailure(final Throwable caught) { }
				@Override
				public void onSuccess(final Void result) { }
			});
		}
	}
	
	/*
	 * assuming text is of form *:<<category>>-<<oid>>
	 * where * could contain a -
	 * return text without -<<oid>>
	 */
	/**
	 * Gets the text sans oid.
	 * 
	 * @param text
	 *            the text
	 * @return the text sans oid
	 */
	public String getTextSansOid(String text) {
		if (text == null) {
			return text;
		}
		int d = text.lastIndexOf('-');
		int c = text.lastIndexOf(':');
		if ((d > 0) && (d > c)) {
			return text.substring(0, d);
		} else {
			return text;
		}
	}
	
	/**
	 * Strip off oid.
	 * 
	 * @param item
	 *            the item
	 * @return the string
	 */
	public String stripOffOID(String item) {
		int idx = item.lastIndexOf('-');
		if(idx < 0){
			return item;
		}
		return item.substring(0,idx).trim();
	}
	
	/**
	 * Record transaction event.
	 * 
	 * @param primaryId
	 *            the primary id
	 * @param secondaryId
	 *            the secondary id
	 * @param activityType
	 *            the activity type
	 * @param additionalInfo
	 *            the additional info
	 * @param logLevel
	 *            the log level
	 */
	public void recordTransactionEvent(String primaryId, String secondaryId, String activityType, String additionalInfo, int logLevel){
		String userId = getLoggedinUserId();
		String userEmail = "["+getLoggedInUserEmail()+"] ";
		getAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId,
				userEmail + additionalInfo, logLevel, new AsyncCallback<Boolean>() {
			@Override
			public void onFailure(final Throwable caught) { }
			@Override
			public void onSuccess(final Boolean result) { }
		});
	}
	
	
	//recording the User Events
	/**
	 * Record user event.
	 *
	 * @param userId the user id
	 * @param event the event
	 * @param additionalInfo the additional info
	 * @param isChildLogRequired the is child log required
	 */
	public void recordUserEvent(String userId, List<String> event, String additionalInfo, boolean isChildLogRequired) {
		MatContext.get()
		.getAuditService().recordUserEvent(userId, event, additionalInfo, isChildLogRequired, new AsyncCallback<Boolean>() {
			
			@Override
			public void onFailure(Throwable caught) {
			}
			
			@Override
			public void onSuccess(Boolean result) {
			}
		});
	}
	
	/**
	 * Gets the synchronization delegate.
	 * 
	 * @return the synchronization delegate
	 */
	public SynchronizationDelegate getSynchronizationDelegate() {
		return synchronizationDelegate;
	}
	
	/**
	 * Checks if is already signed in.
	 * 
	 * @param lastSignOut
	 *            the last sign out
	 * @param lastSignIn
	 *            the last sign in
	 * @param current
	 *            the current
	 * @return true, if is already signed in
	 */
	public boolean isAlreadySignedIn(Date lastSignOut, Date lastSignIn, Date current){
		/*
		 * ASSUMPTION: while signed in... lastSignIn is updated every 2 minutes
		 * (1) lastSignOut == null --> see if last sign in time > 3 minutes ago
		 * (2) lastSignOut < lastSignIn --> see if last sign in time > 3 minutes ago
		 * (3)lastSignOut > lastSignIn --> not signed in
		 */
		boolean alreadySignedIn = (lastSignIn == null) ? false :
			((lastSignOut == null) || lastSignOut.before(lastSignIn)) ?
					((current.getTime() - lastSignIn.getTime()) < (3 * 60 * 1000)) :
						false;
					return alreadySignedIn;
	}
	
	
	/**
	 * Gets the manage measure search view.
	 * 
	 * @return the manageMeasureSearchView
	 */
	public ManageMeasureSearchView getManageMeasureSearchView() {
		return manageMeasureSearchView;
	}
	
	
	/**
	 * Sets the manage measure search view.
	 * 
	 * @param manageMeasureSearchView
	 *            the manageMeasureSearchView to set
	 */
	public void setManageMeasureSearchView(
			ManageMeasureSearchView manageMeasureSearchView) {
		this.manageMeasureSearchView = manageMeasureSearchView;
	}
	
	
	/**
	 * Gets the manage measure search model.
	 * 
	 * @return the manageMeasureSearchModel
	 */
	public ManageMeasureSearchModel getManageMeasureSearchModel() {
		return manageMeasureSearchModel;
	}
	
	
	/**
	 * Sets the manage measure search model.
	 * 
	 * @param manageMeasureSearchModel
	 *            the manageMeasureSearchModel to set
	 */
	public void setManageMeasureSearchModel(
			ManageMeasureSearchModel manageMeasureSearchModel) {
		this.manageMeasureSearchModel = manageMeasureSearchModel;
	}
	
	
	/**
	 * Checks if is error tab.
	 * 
	 * @return the isErrorTab
	 */
	public boolean isErrorTab() {
		return isErrorTab;
	}
	
	
	/**
	 * Sets the error tab.
	 * 
	 * @param isErrorTab
	 *            the isErrorTab to set
	 */
	public void setErrorTab(boolean isErrorTab) {
		this.isErrorTab = isErrorTab;
	}
	
	
	/**
	 * Gets the error tab index.
	 * 
	 * @return the errorTabIndex
	 */
	public int getErrorTabIndex() {
		return errorTabIndex;
	}
	
	
	/**
	 * Sets the error tab index.
	 * 
	 * @param errorTabIndex
	 *            the errorTabIndex to set
	 */
	public void setErrorTabIndex(int errorTabIndex) {
		this.errorTabIndex = errorTabIndex;
	}
	
	/**
	 * Method is called on SignOut/ X out / Time Out.
	 * 
	 * @param activityType
	 *            the activity type
	 * @param isRedirect
	 *            the is redirect
	 */
	public void handleSignOut(String activityType, final boolean isRedirect) {
		MatContext.get().getSynchronizationDelegate().setLogOffFlag();
		MatContext.get().setUMLSLoggedIn(false);
		MatContext.get().getLoginService().updateOnSignOut(MatContext.get().getLoggedinUserId(),
				MatContext.get().getLoggedInUserEmail(), activityType, new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(final String result) {
				if (isRedirect) {
					MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
				}
			}
			
			@Override
			public void onFailure(final Throwable caught) {
				if (isRedirect) {
					MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
				}
			}
		});
	}
	
	
	/**
	 * Gets the all operators.
	 * 
	 * @return the all operators
	 */
	public void getAllOperators(){
		getCodeListService().getAllOperators(new AsyncCallback<List<OperatorDTO>>() {
			
			@Override
			public void onFailure(final Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onSuccess(List<OperatorDTO> result) {
				for (OperatorDTO operatorDTO : result) {
					operatorMapKeyShort.put(operatorDTO.getId(), operatorDTO.getOperator());
					operatorMapKeyLong.put(operatorDTO.getOperator(), operatorDTO.getId());
					if(operatorDTO.getOperatorType().equals("1")){
						logicalOps.add(operatorDTO.getOperator());
					}else if(operatorDTO.getOperatorType().equals("2")){
						timings.add(operatorDTO.getOperator());
					}else if(operatorDTO.getOperatorType().equals("3")){
						relationships.add(operatorDTO.getOperator());
					}else if(operatorDTO.getOperatorType().equals("4")){
						functions.add(operatorDTO.getOperator());
					}else if(operatorDTO.getOperatorType().equals("5")){
						comparisonOps.add(operatorDTO.getOperator());
					}else if(operatorDTO.getOperatorType().equals("6")){
						setOps.add(operatorDTO.getOperator());
						//Collections.sort(setOps);
						Collections.reverse(setOps);
					}
					
				}
				//for adding Removed Relationship Types
				addRemovedRelationShipTypes();
			}
			
			private void addRemovedRelationShipTypes() {
				
				removedRelationshipTypes.put("Is Authorized By", "AUTH");
				removedRelationshipTypes.put("Causes", "CAUS");
				removedRelationshipTypes.put("Is Derived From", "DRIV");
				removedRelationshipTypes.put("Has Goal Of", "GOAL");
				removedRelationshipTypes.put("Has Outcome Of", "OUTC");
			}
		});
	}	
	
	/**
	 * Gets the all versions by oid.
	 *
	 * @param oid the oid
	 * @param index the index
	 * @return the all versions by oid
	 */
	public void getAllVersionsByOID(String oid, int index){
		vsacapiServiceAsync.getAllVersionListByOID(oid, new AsyncCallback<VsacApiResult>() {
			
			@Override
			public void onSuccess(VsacApiResult result) {
				if (result.getVsacExpProfileResp() != null) {
					
				}
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	/**
	 * Sets the modify qdm pop up widget.
	 * 
	 * @param modifyQDMPopUpWidget
	 *            the new modify qdm pop up widget
	 */
	public void setModifyQDMPopUpWidget(
			QDMAvailableValueSetWidget modifyQDMPopUpWidget) {
		this.modifyQDMPopUpWidget = modifyQDMPopUpWidget;
	}
	
	
	/**
	 * Checks if is measure deleted.
	 * 
	 * @return true, if is measure deleted
	 */
	public boolean isMeasureDeleted() {
		return isMeasureDeleted;
	}
	
	
	/**
	 * Sets the measure deleted.
	 * 
	 * @param isMeasureDeleted
	 *            the new measure deleted
	 */
	public void setMeasureDeleted(boolean isMeasureDeleted) {
		this.isMeasureDeleted = isMeasureDeleted;
	}
	
	
	/**
	 * Checks if is uMLS logged in.
	 * 
	 * @return true, if is uMLS logged in
	 */
	public boolean isUMLSLoggedIn() {
		return isUMLSLoggedIn;
	}	
	
	/**
	 * Sets the uMLS logged in.
	 * 
	 * @param isUMLSLoggedIn
	 *            the new uMLS logged in
	 */
	public void setUMLSLoggedIn(boolean isUMLSLoggedIn) {
		this.isUMLSLoggedIn = isUMLSLoggedIn;
	}
	
	/**
	 * Gets the allowed populations in package.
	 *
	 * @return the allowed populations in package
	 */
	public List<String> getAllowedPopulationsInPackage(){
		
		List<String> allowedPopulationsInPackage = new ArrayList<String>();
		
		allowedPopulationsInPackage.add("initialPopulation");
		allowedPopulationsInPackage.add("stratification");
		allowedPopulationsInPackage.add("measurePopulation");
		allowedPopulationsInPackage.add("measurePopulationExclusions");
		allowedPopulationsInPackage.add("measureObservation");
		allowedPopulationsInPackage.add("denominator");
		allowedPopulationsInPackage.add("denominatorExclusions");
		allowedPopulationsInPackage.add("denominatorExceptions");
		allowedPopulationsInPackage.add("numerator");
		allowedPopulationsInPackage.add("numeratorExclusions");
		return allowedPopulationsInPackage;
	}
	
	private List<String> getExpProfileList(List<VSACExpansionProfile> list) {
		List<String> expProfile = new ArrayList<String>();
		for (int i = 0; i < list.size(); i++) {
			expProfile.add(list.get(i).getName());
		}
		return expProfile;
	}
	
	
	
	/**
	 * Gets the global copy paste.
	 *
	 * @return the global copy paste
	 */
	public GlobalCopyPasteObject getGlobalCopyPaste() {
		return globalCopyPaste;
	}
	
	/**
	 * Sets the global copy paste.
	 *
	 * @param globalCopyPaste the new global copy paste
	 */
	public void setGlobalCopyPaste(GlobalCopyPasteObject globalCopyPaste) {
		this.globalCopyPaste = globalCopyPaste;
	}
	
	/**
	 * Gets the current release version.
	 *
	 * @param currentReleaseVersionCallback the current release version callback
	 * @return the current release version
	 */
	public void getCurrentReleaseVersion(AsyncCallback<String> currentReleaseVersionCallback){
		getSessionService().getCurrentReleaseVersion(currentReleaseVersionCallback);
	}


	/**
	 * Gets the valuesets.
	 *
	 * @return the valuests
	 */
	public List<CQLIdentifierObject> getValuesets() {
		return this.valuesets;
	}
	
	/**
	 * Sets the valuesets.
	 *
	 * @param valuesets the valuesets
	 */
	public void setValuesets(List<CQLQualityDataSetDTO> valuesets) {
		
		List<CQLIdentifierObject> valuesetIdentifiers = new ArrayList<>(); 
		
		for(int i = 0; i < valuesets.size(); i++) {
			CQLIdentifierObject valuesetIdentifier = null;
			if(valuesets.get(i).getType() != null){
				valuesetIdentifier  = new CQLIdentifierObject(null, valuesets.get(i).getDisplayName(),valuesets.get(i).getId());
			} else{
				valuesetIdentifier  = new CQLIdentifierObject(null, valuesets.get(i).getCodeListName(),valuesets.get(i).getId());
			}
			
			valuesetIdentifiers.add(valuesetIdentifier);
		}
		
		this.valueSetCodeQualityDataSetList = valuesets;
		
		this.valuesets = valuesetIdentifiers;
	}
	
	
	/**
	 * Gets the definitions.
	 *
	 * @return the definitions
	 */
	public List<CQLIdentifierObject> getDefinitions() {
		return definitions;
	}


	/**
	 * Sets the definitions.
	 *
	 * @param definitions the new definitions
	 */
	public void setDefinitions(List<CQLIdentifierObject> definitions) {
		this.definitions = definitions;
	}


	/**
	 * Gets the parameters.
	 *
	 * @return the parameters
	 */
	public List<CQLIdentifierObject> getParameters() {
		return parameters;
	}


	/**
	 * Sets the parameters.
	 *
	 * @param parameters the new parameters
	 */
	public void setParameters(List<CQLIdentifierObject> parameters) {
		this.parameters = parameters;
	}


	/**
	 * Gets the funcs.
	 *
	 * @return the funcs
	 */
	public List<CQLIdentifierObject> getFuncs() {
		return funcs;
	}


	/**
	 * Sets the funcs.
	 *
	 * @param funcs the new funcs
	 */
	public void setFuncs(List<CQLIdentifierObject> funcs) {
		this.funcs = funcs;
	}


	/**
	 * Gets the qds attributes service async.
	 *
	 * @return the qds attributes service async
	 */
	public QDSAttributesServiceAsync getQdsAttributesServiceAsync() {
		
		if(qdsAttributesServiceAsync == null){
			qdsAttributesServiceAsync = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
		}
		
		return qdsAttributesServiceAsync;
	}


	/**
	 * Sets the qds attributes service async.
	 *
	 * @param qdsAttributesServiceAsync the new qds attributes service async
	 */
	public void setQdsAttributesServiceAsync(QDSAttributesServiceAsync qdsAttributesServiceAsync) {
		this.qdsAttributesServiceAsync = qdsAttributesServiceAsync;
	}
				
	/**
	 * Gets the includes.
	 *
	 * @return the includes
	 */
	public List<String> getIncludes() {
		return includes;
	}


	/**
	 * Sets the includes.
	 *
	 * @param includes the new includes
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	/**
	 * Gets the current CQL Library id.
	 * 
	 * @return the CQL Library id
	 */
	public String getCurrentCQLLibraryId() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getCqlLibraryId();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current CQL Library id.
	 * 
	 * @return the CQL Library id
	 */
	public String getCurrentCQLLibraryLockedUserId() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getLockedUserId();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current CQL Library id.
	 * 
	 * @return the CQL Library id
	 */
	public String getCurrentCQLLibraryLockedUserName() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getLockedUserName();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current CQL Library id.
	 * 
	 * @return the CQL Library id
	 */
	public String getCurrentCQLLibraryLockedUserEmail() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getLockedUserEmail();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current CQL Library name.
	 * 
	 * @return the current CQL Library name
	 */
	public String getCurrentCQLLibraryeName() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getLibraryName();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Gets the current Library version.
	 * 
	 * @return the current Library version
	 */
	public String getCurrentCQLLibraryVersion() {
		if(currentLibraryInfo != null) {
			return currentLibraryInfo.getCqlLibraryVersion();
		}
		else {
			return "";
		}
	}
	
	/**
	 * Sets the current measure version.
	 * 
	 * @param s
	 *            the new current measure version
	 */
	public void setCurrentCQLLibraryVersion(String s) {
		if(currentLibraryInfo != null) {
			currentLibraryInfo.setCqlLibraryVersion(s);
		}
	}

	/**
	 * Gets the library service.
	 *
	 * @return the library service
	 */
	public CQLLibraryServiceAsync getLibraryService() {
		if(cqlLibraryService == null){
			cqlLibraryService = (CQLLibraryServiceAsync) GWT.create(CQLLibraryService.class);
		}
		return cqlLibraryService;
	}
	
	/**
	 * Checks if is measure is CQL Measure depending 
	 * on Measure release version.
	 *
	 * @param releaseVersion the release version
	 * @return true, if is CQL measure
	 */
	public boolean isCQLMeasure(String releaseVersion) {
		
		String str[] = releaseVersion.replace("v", "").split("\\.");
		int versionInt = Integer.parseInt(str[0]);
		if(versionInt<5){
			return false;
		}
		
		return true;
	}


	/**
	 * Gets the included def names.
	 *
	 * @return the included def names
	 */
	public List<CQLIdentifierObject> getIncludedDefNames() {
		return includedDefNames;
	}


	/**
	 * Sets the included def names.
	 *
	 * @param includedDefNames the new included def names
	 */
	public void setIncludedDefNames(List<CQLIdentifierObject> includedDefNames) {
		this.includedDefNames = includedDefNames;
	}


	/**
	 * Gets the included func names.
	 *
	 * @return the included func names
	 */
	public List<CQLIdentifierObject> getIncludedFuncNames() {
		return includedFuncNames;
	}


	/**
	 * Sets the included func names.
	 *
	 * @param includedFuncNames the new included func names
	 */
	public void setIncludedFuncNames(List<CQLIdentifierObject> includedFuncNames) {
		this.includedFuncNames = includedFuncNames;
	}


	/**
	 * Gets the included value set names.
	 *
	 * @return the included value set names
	 */
	public List<CQLIdentifierObject> getIncludedValueSetNames() {
		return includedValueSetNames;
	}


	/**
	 * Sets the included value set names.
	 *
	 * @param includedValueSetNames the new included value set names
	 */
	public void setIncludedValueSetNames(List<CQLIdentifierObject> includedValueSetNames) {
		this.includedValueSetNames = includedValueSetNames;
	}


	/**
	 * Gets the included param names.
	 *
	 * @return the included param names
	 */
	public List<CQLIdentifierObject> getIncludedParamNames() {
		return includedParamNames;
	}


	/**
	 * Sets the included param names.
	 *
	 * @param includedParamNames the new included param names
	 */
	public void setIncludedParamNames(List<CQLIdentifierObject> includedParamNames) {
		this.includedParamNames = includedParamNames;
	}


	/**
	 * Gets the included code names.
	 *
	 * @return the included code names
	 */
	public List<CQLIdentifierObject> getIncludedCodeNames() {
		return includedCodeNames;
	}


	/**
	 * Sets the included code names.
	 *
	 * @param includedCodeNames the new included code names
	 */
	public void setIncludedCodeNames(List<CQLIdentifierObject> includedCodeNames) {
		this.includedCodeNames = includedCodeNames;
	}
	
	/**
	 * Gets the CQL constants.
	 *
	 * @return the CQL constants
	 */
	public void getCQLConstants() {
		CQLConstantServiceAsync cqlConstantService = (CQLConstantServiceAsync) GWT.create(CQLConstantService.class);

		cqlConstantService.getAllCQLConstants(new AsyncCallback<CQLConstantContainer>() {
		
			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(CQLConstantContainer result) {
				cqlConstantContainer = result; 
				setShortCutKeyUnits();
			}
			
		});
	}
	
	
	/**
	 * Gets the non quotes units.
	 *
	 * @return the non quotes units
	 */
	public HashSet<String> getNonQuotesUnits(){
		 HashSet<String> hset = 
	               new HashSet<String>();
		hset.add("millisecond");
		hset.add("milliseconds");
		hset.add("second");
		hset.add("seconds");
		hset.add("minute");
		hset.add("minutes");
		hset.add("hour");
		hset.add("hours");
		hset.add("day");
		hset.add("days");
		hset.add("week");
		hset.add("weeks");
		hset.add("month");
		hset.add("months");
		hset.add("year");
		hset.add("years");
		
		return hset;
	}


	/**
	 * Gets the cql constant container.
	 *
	 * @return the cql constant container
	 */
	public CQLConstantContainer getCqlConstantContainer() {
		return cqlConstantContainer;
	}


	/**
	 * Sets the cql constant container.
	 *
	 * @param cqlConstantContainer the new cql constant container
	 */
	public void setCqlConstantContainer(CQLConstantContainer cqlConstantContainer) {
		this.cqlConstantContainer = cqlConstantContainer;
	}


	/**
	 * Gets the value set code quality data set list.
	 *
	 * @return the value set code quality data set list
	 */
	public List<CQLQualityDataSetDTO> getValueSetCodeQualityDataSetList() {
		return valueSetCodeQualityDataSetList;
	}


	/**
	 * Sets the units list.
	 */
	public void setShortCutKeyUnits() {
		shorcutKeyUnits.clear();
		for(Map.Entry<String,String> unitsMap : cqlConstantContainer.getCqlUnitMap().entrySet()){
			
			MatContext.get();
			if (!unitsMap.getValue().equalsIgnoreCase(MatContext.PLEASE_SELECT)) {
				if (!getNonQuotesUnits().contains(unitsMap.getValue())) {
					shorcutKeyUnits.add("'" + unitsMap.getValue() + "'");
				} else {
					shorcutKeyUnits.add(unitsMap.getValue());
				}
			}
		}
	}


	/**
	 * Gets the units list.
	 *
	 * @return the units list
	 */
	public List<String> getShorcutKeyUnits() {
		return shorcutKeyUnits;
	}


	public String getCurrentQDMVersion() {
		if(cqlConstantContainer == null){
			return "";
		} else {
			return cqlConstantContainer.getCurrentQDMVersion();
		}
		
	}

	/**
	 * Gets the Population service.
	 *
	 * @return the Population service
	 */
	public PopulationServiceAsync getPopulationService() {
		
		if(populationService == null) {
			populationService = GWT.create(PopulationService.class);
		}
		
		return populationService; 
	}

	public Map<String, List<String>> getProgramToReleases() {		
		return programToReleases;
	}


	public void setProgramToReleases(Map<String, List<String>> programToReleases) {
		this.programToReleases = (HashMap<String, List<String>>) programToReleases;
	}

	public void getProgramsAndReleasesFromVSAC() {
		
		//Get the program and releases from VSAC using REST
		MatContext.get().getVsacapiServiceAsync().getVSACProgramsAndReleases(new AsyncCallback<VsacApiResult>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
			}

			@Override
			public void onSuccess(VsacApiResult result) {
				if(result != null) {
					//set the values in the MatContext
					MatContext.get().setProgramToReleases(result.getProgramToReleases());					
				}
				
			}
		});
	
	}
	
}
