package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.DTO.CompositeMeasureScoreDTO;
import mat.DTO.OperatorDTO;
import mat.DTO.UserPreferenceDTO;
import mat.client.Enableable;
import mat.client.admin.service.AdminService;
import mat.client.admin.service.AdminServiceAsync;
import mat.client.audit.service.AuditService;
import mat.client.audit.service.AuditServiceAsync;
import mat.client.bonnie.BonnieService;
import mat.client.bonnie.BonnieServiceAsync;
import mat.client.clause.QDMAvailableValueSetWidget;
import mat.client.clause.QDSAttributesService;
import mat.client.clause.QDSAttributesServiceAsync;
import mat.client.clause.QDSCodeListSearchView;
import mat.client.codelist.HasListBox;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.codelist.service.CodeListService;
import mat.client.codelist.service.CodeListServiceAsync;
import mat.client.cqlconstant.service.CQLConstantService;
import mat.client.cqlconstant.service.CQLConstantServiceAsync;
import mat.client.event.CQLLibrarySelectedEvent;
import mat.client.event.ForgottenPasswordEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.MeasureSelectedEvent;
import mat.client.featureFlag.service.FeatureFlagRemoteService;
import mat.client.featureFlag.service.FeatureFlagRemoteServiceAsync;
import mat.client.login.LoginModel;
import mat.client.login.service.HarpService;
import mat.client.login.service.HarpServiceAsync;
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
import mat.model.GlobalCopyPasteObject;
import mat.model.MeasureType;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.CQLIdentifierObject;
import mat.shared.CompositeMethodScoringConstant;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import mat.shared.SaveUpdateCQLResult;

public class MatContext implements IsSerializable {

    private CQLModel cqlModel;

    private boolean isUMLSLoggedIn = false;

    private boolean doLibraryLockUpdates = false;

    private boolean doMeasureLockUpdates = false;

    private boolean doUserLockUpdates = false;

    // how often to perform lock time updates
    private final int lockUpdateTime = 2 * 60 * 1000;

    private final int userLockUpdateTime = 2 * 60 * 1000;

    public final static String PLEASE_SELECT = "--Select--";

    private static MatContext instance = new MatContext();

    private String currentModule;

    private String bonnieLink;

    private LoginServiceAsync loginService;

    private HarpServiceAsync harpService;

    private MeasureServiceAsync measureService;

    private FeatureFlagRemoteServiceAsync featureFlagService;

    private CQLConstantServiceAsync cqlConstantService;

    private CQLLibraryServiceAsync cqlLibraryService;

    private PackageServiceAsync packageServiceAsync;

    private SessionManagementServiceAsync sessionService;

    private AdminServiceAsync adminService;

    private MyAccountServiceAsync myAccountService;

    private CodeListServiceAsync codeListService;

    private VSACAPIServiceAsync vsacapiServiceAsync;

    private QDSAttributesServiceAsync qdsAttributesServiceAsync;

    private PopulationServiceAsync populationService;

    private BonnieServiceAsync bonnieService;

    private HandlerManager eventBus;

    private TimeoutManager timeoutManager;

    private MeasureSelectedEvent currentMeasureInfo;

    private CQLLibrarySelectedEvent currentLibraryInfo;

    private boolean isMeasureDeleted;

    private ListBoxCodeProvider listBoxCodeProvider;

    private AuditServiceAsync auditService;

    private String userId;

    private String idToken;

    private String accessToken;

    private String userEmail;

    private String loginId;

    private String userRole;

    private UserPreferenceDTO userPreference;

    private QDSCodeListSearchView qdsView;

    private QDMAvailableValueSetWidget modifyQDMPopUpWidget;

    private ManageMeasureSearchView manageMeasureSearchView;

    private ManageMeasureSearchModel manageMeasureSearchModel;

    private SynchronizationDelegate synchronizationDelegate = new SynchronizationDelegate();

    private int errorTabIndex;

    private boolean isErrorTab;

    public List<String> timings = new ArrayList<String>();

    public List<String> functions = new ArrayList<String>();

    public List<String> relationships = new ArrayList<String>();

    public List<String> comparisonOps = new ArrayList<String>();

    public List<String> logicalOps = new ArrayList<String>();

    public List<String> setOps = new ArrayList<String>();

    public Map<String, String> operatorMapKeyShort = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, String> operatorMapKeyLong = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    public Map<String, String> removedRelationshipTypes = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);

    private GlobalCopyPasteObject globalCopyPaste;

    public List<CQLIdentifierObject> valuesets = new ArrayList<CQLIdentifierObject>();

    public List<CQLQualityDataSetDTO> valueSetCodeQualityDataSetList = new ArrayList<CQLQualityDataSetDTO>();

    public List<CQLIdentifierObject> definitions = new ArrayList<CQLIdentifierObject>();

    public List<CQLIdentifierObject> parameters = new ArrayList<CQLIdentifierObject>();

    public List<CQLIdentifierObject> funcs = new ArrayList<CQLIdentifierObject>();

    public List<String> includes = new ArrayList<String>();

    private List<CQLIdentifierObject> includedDefNames = new ArrayList<CQLIdentifierObject>();

    private List<CQLIdentifierObject> includedFuncNames = new ArrayList<CQLIdentifierObject>();

    private List<CQLIdentifierObject> includedValueSetNames = new ArrayList<CQLIdentifierObject>();

    private List<CQLIdentifierObject> includedParamNames = new ArrayList<CQLIdentifierObject>();

    private List<CQLIdentifierObject> includedCodeNames = new ArrayList<CQLIdentifierObject>();

    private List<String> shorcutKeyUnits = new ArrayList<String>();

    private CQLConstantContainer cqlConstantContainer = new CQLConstantContainer();

    //VSAC Programs and Releases
    private HashMap<String, List<String>> programToReleases = new HashMap<>();

    private HashMap<String, String> programToLatestProfile = new HashMap<>();

    private HashMap<String, List<? extends HasListBox>> selectionMap = new HashMap<>();

    private List<MeasureType> measureTypeList = new ArrayList<>();

    private Map<String, String> expressionToReturnTypeMap = new HashMap<>();

    private Map<String, Boolean> featureFlagMap = new HashMap<>();

    private Map<String, String> harpUserInfo = new HashMap<>();

    public void clearDVIMessages() {
        if (qdsView != null) {
            qdsView.getSuccessMessageDisplay().clear();
            qdsView.getErrorMessageDisplay().clear();
        }
    }

    public void clearModifyPopUpMessages() {
        if (modifyQDMPopUpWidget != null) {
            modifyQDMPopUpWidget.getApplyToMeasureSuccessMsg().clear();
            modifyQDMPopUpWidget.getErrorMessageDisplay().clear();
            modifyQDMPopUpWidget.getSuccessMessagePanel().clear();
            modifyQDMPopUpWidget.getErrorMessagePanel().clear();
        }
    }

    public void setQDSView(QDSCodeListSearchView view) {
        qdsView = view;
    }

    public ListBoxCodeProvider getListBoxCodeProvider() {
        return listBoxCodeProvider;
    }

    public void setListBoxCodeProvider(ListBoxCodeProvider listBoxCodeProvider) {
        this.listBoxCodeProvider = listBoxCodeProvider;
    }

    public void setUserInfo(String userId, String userEmail, String userRole, String loginId, UserPreferenceDTO userPreference) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.userRole = userRole;
        this.loginId = loginId;
        this.userPreference = userPreference;
    }

    protected MatContext() {
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

        // US 439. Start the timeout timer when the user clicked the forgotten password link
        eventBus.addHandler(ForgottenPasswordEvent.TYPE, new ForgottenPasswordEvent.Handler() {
            @Override
            public void onForgottenPassword(ForgottenPasswordEvent event) {
                getTimeoutManager().startActivityTimers(ConstantMessages.LOGIN_MODULE);
            }
        });
    }

    public HandlerManager getEventBus() {
        return eventBus;
    }

    public void logException(String message, Throwable t) {
        StackTraceElement[] elementArr = t.getStackTrace();
        for (StackTraceElement element : elementArr) {
            element.toString().concat("\r\n");
        }
    }

    public MyAccountServiceAsync getMyAccountService() {
        if (myAccountService == null) {
            myAccountService = (MyAccountServiceAsync) GWT.create(MyAccountService.class);
        }
        return myAccountService;
    }

    public CodeListServiceAsync getCodeListService() {
        if (codeListService == null) {
            codeListService = (CodeListServiceAsync) GWT.create(CodeListService.class);
        }
        return codeListService;
    }

    public AdminServiceAsync getAdminService() {
        if (adminService == null) {
            adminService = (AdminServiceAsync) GWT.create(AdminService.class);
        }
        return adminService;
    }

    public LoginServiceAsync getLoginService() {
        if (loginService == null) {
            loginService = GWT.create(LoginService.class);
        }
        return loginService;
    }

    public HarpServiceAsync getHarpService() {
        if (harpService == null) {
            harpService = GWT.create(HarpService.class);
        }
        return harpService;
    }

    public VSACAPIServiceAsync getVsacapiServiceAsync() {
        if (vsacapiServiceAsync == null) {
            vsacapiServiceAsync = (VSACAPIServiceAsync) GWT.create(VSACAPIService.class);
        }
        return vsacapiServiceAsync;
    }

    public void setVsacapiServiceAsync(VSACAPIServiceAsync vsacapiServiceAsync) {
        this.vsacapiServiceAsync = vsacapiServiceAsync;
    }

    private SessionManagementServiceAsync getSessionService() {
        if (sessionService == null) {
            sessionService = (SessionManagementServiceAsync) GWT.create(SessionManagementService.class);
        }
        return sessionService;
    }

    public CQLConstantServiceAsync getCQLContantService() {

        if (cqlConstantService == null) {
            cqlConstantService = (CQLConstantServiceAsync) GWT.create(CQLConstantService.class);
        }

        return cqlConstantService;
    }

    public MeasureServiceAsync getMeasureService() {
        if (measureService == null) {
            measureService = (MeasureServiceAsync) GWT.create(MeasureService.class);
        }
        return measureService;
    }

    public FeatureFlagRemoteServiceAsync getFeatureFlagService() {
        if (featureFlagService == null) {
            featureFlagService = (FeatureFlagRemoteServiceAsync) GWT.create(FeatureFlagRemoteService.class);
        }
        return featureFlagService;
    }

    public CQLLibraryServiceAsync getCQLLibraryService() {
        if (cqlLibraryService == null) {
            cqlLibraryService = (CQLLibraryServiceAsync) GWT.create(CQLLibraryService.class);

        }
        return cqlLibraryService;

    }

    public AuditServiceAsync getAuditService() {
        if (auditService == null) {
            auditService = (AuditServiceAsync) GWT.create(AuditService.class);
        }
        return auditService;
    }

    public PackageServiceAsync getPackageService() {
        if (packageServiceAsync == null) {
            packageServiceAsync = (PackageServiceAsync) GWT.create(PackageService.class);
        }
        return packageServiceAsync;
    }

    public BonnieServiceAsync getBonnieService() {
        if (bonnieService == null) {
            bonnieService = (BonnieServiceAsync) GWT.create(BonnieService.class);
        }
        return bonnieService;
    }

    public static MatContext get() {
        return instance;
    }

    public String getLoggedInUserRole() {
        return userRole;
    }

    public UserPreferenceDTO getLoggedInUserPreference() {
        return userPreference;
    }

    public void setLoggedInUserPreference(UserPreferenceDTO userPreference) {
        this.userPreference = userPreference;
    }

    public String getLoggedinUserId() {
        return userId;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getLoggedinLoginId() {
        return loginId;
    }

    public String getLoggedInUserEmail() {
        return userEmail;
    }

    public void changePasswordSecurityQuestions(LoginModel model, AsyncCallback<LoginResult> asyncCallback) {
        getLoginService().changePasswordSecurityAnswers(model, asyncCallback);
    }

    public void isValidUser(String username, String Password, String oneTimePassword, AsyncCallback<LoginModel> callback) {
        getLoginService().isValidUser(username, Password, oneTimePassword, callback);
    }

    public void initSession(Map<String, String> harpUserInfo, AsyncCallback<LoginModel> callback) {
        getLoginService().initSession(harpUserInfo, callback);
    }

    public void getListBoxData(AsyncCallback<CodeListService.ListBoxData> listBoxCallback) {
        getCodeListService().getListBoxData(listBoxCallback);
    }

    public void getCurrentUser(AsyncCallback<SessionManagementService.Result> userCallback) {
        getSessionService().getCurrentUser(userCallback);
    }

    public void restartTimeoutWarning() {
        getTimeoutManager().startActivityTimers(ConstantMessages.MAT_MODULE);
    }

    public void restartUMLSSignout() {
        getTimeoutManager().startUMLSTimer();
    }

    public String getCurrentMeasureId() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getMeasureId();
        } else {
            return "";
        }
    }

    public String getCurrentMeasureName() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getMeasureName();
        } else {
            return "";
        }
    }

    public String getCurrentMeasureModel() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getMeasureModel();
        } else {
            return "";
        }
    }


    public String getCurrentMeasureVersion() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getMeasureVersion();
        } else {
            return "";
        }
    }

    public void setCurrentMeasureVersion(String s) {
        if (currentMeasureInfo != null) {
            currentMeasureInfo.setMeasureVersion(s);
        }
    }

    public void setCurrentMeasureScoringType(String s) {

        if (currentMeasureInfo != null) {
            currentMeasureInfo.setScoringType(s);
        }
    }

    public void setCurrentModule(String moduleName) {
        currentModule = moduleName;
    }

    public String getCurrentModule() {
        return currentModule;
    }

    public boolean isCurrentMeasureModelFhir() {
        return "FHIR".equals(getCurrentMeasureModel());
    }

    @Deprecated
    public String getCurrentMeasureScoringType() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getScoringType();
        } else {
            return "";
        }
    }

    public void setCurrentMeasureName(String measureName) {
        if (currentMeasureInfo != null) {
            currentMeasureInfo.setMeasureName(measureName);
        }
    }

    public String getCurrentShortName() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.getShortName();
        } else {
            return "";
        }
    }

    public void setCurrentShortName(String shortName) {
        if (currentMeasureInfo != null) {
            currentMeasureInfo.setShortName(shortName);
        }
    }

    public boolean isCurrentMeasureEditable() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.isEditable();
        } else {
            return false;
        }
    }

    public boolean isCurrentLibraryEditable() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.isEditable();
        } else {
            return false;
        }
    }

    public boolean isCurrentMeasureLocked() {
        if (currentMeasureInfo != null) {
            return currentMeasureInfo.isLocked();
        } else {
            return false;
        }
    }

    public boolean isCurrentLibraryLocked() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.isLocked();
        } else {
            return false;
        }
    }

    public boolean isDraftLibrary() {
        return (currentLibraryInfo != null) ? currentLibraryInfo.isDraft() : false;
    }

    public boolean isDraftMeasure() {
        return (currentMeasureInfo != null) ? currentMeasureInfo.isDraft() : false;
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
        path = path.substring(0, path.lastIndexOf('/'));
        path += html;
        urlBuilder.setPath(path);
        Window.Location.replace(urlBuilder.buildString());
    }

    public void redirectToMatPage(String html) {
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        urlBuilder.setHash("mainTab0");
        String path = Window.Location.getPath();
        path = path.substring(0, path.lastIndexOf('/'));
        path += html;
        urlBuilder.setPath(path);
        Window.Location.replace(urlBuilder.buildString());
    }

    public void openURL(String html) {
        Window.open(html, "User_Guide", "");

    }


    public void openNewHtmlPage(String html) {
        String windowFeatures = "toolbar=no, location=no, personalbar=no, menubar=yes, scrollbars=yes, resizable=yes";
        UrlBuilder urlBuilder = Window.Location.createUrlBuilder();
        String path = Window.Location.getPath();
        path = path.substring(0, path.lastIndexOf('/'));
        path += html;
        urlBuilder.setPath(path);
        Window.open(urlBuilder.buildString(), "_blank", windowFeatures);
    }

    public void setAriaHidden(Widget widget, Boolean value) {
        setAriaHidden(widget, value.toString());
    }

    public void setAriaHidden(Widget widget, String value) {
        widget.getElement().setAttribute("aria-hidden", value);
    }

    /**
     * This method is the hub for dynamic visibility for The app.
     * The setting of aria attributes or styles is delegated to a shared location
     * and that logic is invoked where needed.
     * <p>
     * If we need another way of making objects "invisible" then we need to modify
     * widget.setVisible(visible);
     *
     * @param widget  The widget to be rendered or no longer rendered
     * @param visible Widget's rendering status
     */
    @SuppressWarnings("deprecation")
    public void setVisible(Widget widget, Boolean visible) {
        widget.setVisible(visible);
        // disable the widget, maybe best to check if this is a FocusWidget and make an explicit setEnabled call
        DOM.setElementPropertyBoolean(widget.getElement(), "disabled", !visible);
        //NOTE: if (visible = true) then (ARIA not hidden)
        setAriaHidden(widget, !visible);
    }

    @SuppressWarnings("rawtypes")
    public HashMap enableRegistry = new HashMap<String, Enableable>();

    @SuppressWarnings("rawtypes")
    public HashMap tabRegistry = new HashMap<String, TabPanel>();

    public MeasureSelectedEvent getCurrentMeasureInfo() {
        return currentMeasureInfo;
    }

    public CQLLibrarySelectedEvent getCurrentLibraryInfo() {
        return currentLibraryInfo;
    }

    public void setCurrentLibraryInfo(CQLLibrarySelectedEvent currentLibraryInfo) {
        this.currentLibraryInfo = currentLibraryInfo;
    }

    public void setCurrentMeasureInfo(MeasureSelectedEvent evt) {
        currentMeasureInfo = evt;
    }

    /*
     * MeasureLock Service --- contains logic to set and release the lock.
     *
     */
    private MeasureLockService measureLockService = new MeasureLockService();

    public MeasureLockService getMeasureLockService() {
        return measureLockService;
    }

    private LibraryLockService libraryLockService = new LibraryLockService();

    public LibraryLockService getLibraryLockService() {
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
    private MATQueue loadingQueue = new MATQueue();

    public MATQueue getLoadingQueue() {
        return loadingQueue;
    }

    public boolean isLoading() {
        return !getLoadingQueue().isEmpty();
    }

    /*
     * Message store to prevent duplicated messages
     */
    private MessageDelegate messageDelegate = new MessageDelegate();

    public MessageDelegate getMessageDelegate() {
        return messageDelegate;
    }

    public void fireLoadingAlert() {
        Window.alert(MatContext.get().getMessageDelegate().getAlertLoadingMessage());
    }

    private TimeoutManager getTimeoutManager() {
        if (timeoutManager == null) {
            timeoutManager = new TimeoutManager();
        }
        return timeoutManager;
    }

    public void startMeasureLockUpdate() {
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

    public void stopMeasureLockUpdate() {
        doMeasureLockUpdates = false;
    }


    public void startLibraryLockUpdate() {
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

    public void stopLibraryLockUpdate() {
        doLibraryLockUpdates = false;
    }

    public void startUserLockUpdate() {
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


    public void setUserSignInDate(String userid) {
        if (userid != null) {
            getMyAccountService().setUserSignInDate(userid, new AsyncCallback<Void>() {
                @Override
                public void onFailure(final Throwable caught) {
                }

                @Override
                public void onSuccess(final Void result) {
                }
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
                public void onFailure(final Throwable caught) {
                }

                @Override
                public void onSuccess(final Void result) {
                }
            });
        }
    }

    /*
     * assuming text is of form *:<<category>>-<<oid>>
     * where * could contain a -
     * return text without -<<oid>>
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

    public String stripOffOID(String item) {
        int idx = item.lastIndexOf('-');
        if (idx < 0) {
            return item;
        }
        return item.substring(0, idx).trim();
    }

    public void fireMeasureEditEvent() {
        MeasureEditEvent evt = new MeasureEditEvent();
        MatContext.get().getEventBus().fireEvent(evt);
    }

    public void recordTransactionEvent(String primaryId, String secondaryId, String activityType, String additionalInfo, int logLevel) {
        String userId = getLoggedinUserId();
        String userEmail = "[" + getLoggedInUserEmail() + "] ";
        getAuditService().recordTransactionEvent(primaryId, secondaryId, activityType, userId,
                userEmail + additionalInfo, logLevel, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(final Throwable caught) {
                    }

                    @Override
                    public void onSuccess(final Boolean result) {
                    }
                });
    }


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


    public SynchronizationDelegate getSynchronizationDelegate() {
        return synchronizationDelegate;
    }


    public boolean isAlreadySignedIn(Date lastSignOut, Date lastSignIn, Date current) {
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

    public ManageMeasureSearchView getManageMeasureSearchView() {
        return manageMeasureSearchView;
    }

    public void setManageMeasureSearchView(
            ManageMeasureSearchView manageMeasureSearchView) {
        this.manageMeasureSearchView = manageMeasureSearchView;
    }

    public ManageMeasureSearchModel getManageMeasureSearchModel() {
        return manageMeasureSearchModel;
    }

    public void setManageMeasureSearchModel(
            ManageMeasureSearchModel manageMeasureSearchModel) {
        this.manageMeasureSearchModel = manageMeasureSearchModel;
    }

    public boolean isErrorTab() {
        return isErrorTab;
    }

    public void setErrorTab(boolean isErrorTab) {
        this.isErrorTab = isErrorTab;
    }

    public int getErrorTabIndex() {
        return errorTabIndex;
    }

    public void setErrorTabIndex(int errorTabIndex) {
        this.errorTabIndex = errorTabIndex;
    }

    public void handleSignOut(final String activityType, final String redirectTo) {
        MatContext.get().getSynchronizationDelegate().setLogOffFlag();
        MatContext.get().setUMLSLoggedIn(false);
        MatContext.get().getLoginService().updateOnSignOut(MatContext.get().getLoggedinUserId(),
                MatContext.get().getLoggedInUserEmail(), activityType, new AsyncCallback<String>() {

                    @Override
                    public void onSuccess(final String result) {
                        redirect();
                    }

                    @Override
                    public void onFailure(final Throwable caught) {
                        redirect();
                    }

                    private void redirect() {
                        if (redirectTo != null && !redirectTo.isEmpty()) {
                            MatContext.get().redirectToHtmlPage(redirectTo);
                        }
                    }
                });
    }

    public void getAllOperators() {
        getCodeListService().getAllOperators(new AsyncCallback<List<OperatorDTO>>() {

            @Override
            public void onFailure(final Throwable caught) {
            }

            @Override
            public void onSuccess(List<OperatorDTO> result) {
                for (OperatorDTO operatorDTO : result) {
                    operatorMapKeyShort.put(operatorDTO.getId(), operatorDTO.getOperator());
                    operatorMapKeyLong.put(operatorDTO.getOperator(), operatorDTO.getId());
                    if (operatorDTO.getOperatorType().equals("1")) {
                        logicalOps.add(operatorDTO.getOperator());
                    } else if (operatorDTO.getOperatorType().equals("2")) {
                        timings.add(operatorDTO.getOperator());
                    } else if (operatorDTO.getOperatorType().equals("3")) {
                        relationships.add(operatorDTO.getOperator());
                    } else if (operatorDTO.getOperatorType().equals("4")) {
                        functions.add(operatorDTO.getOperator());
                    } else if (operatorDTO.getOperatorType().equals("5")) {
                        comparisonOps.add(operatorDTO.getOperator());
                    } else if (operatorDTO.getOperatorType().equals("6")) {
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

    public void setModifyQDMPopUpWidget(
            QDMAvailableValueSetWidget modifyQDMPopUpWidget) {
        this.modifyQDMPopUpWidget = modifyQDMPopUpWidget;
    }


    public boolean isMeasureDeleted() {
        return isMeasureDeleted;
    }


    public void setMeasureDeleted(boolean isMeasureDeleted) {
        this.isMeasureDeleted = isMeasureDeleted;
    }


    public boolean isUMLSLoggedIn() {
        return isUMLSLoggedIn;
    }


    public void setUMLSLoggedIn(boolean isUMLSLoggedIn) {
        this.isUMLSLoggedIn = isUMLSLoggedIn;
    }

    public List<String> getAllowedPopulationsInPackage() {

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


    public GlobalCopyPasteObject getGlobalCopyPaste() {
        return globalCopyPaste;
    }


    public void setGlobalCopyPaste(GlobalCopyPasteObject globalCopyPaste) {
        this.globalCopyPaste = globalCopyPaste;
    }

    public void getCurrentReleaseVersion(AsyncCallback<String> currentReleaseVersionCallback) {
        getSessionService().getCurrentReleaseVersion(currentReleaseVersionCallback);
    }

    public List<CQLIdentifierObject> getValuesets() {
        return this.valuesets;
    }

    public void setValuesets(List<CQLQualityDataSetDTO> valuesets) {

        List<CQLIdentifierObject> valuesetIdentifiers = new ArrayList<>();

        for (int i = 0; i < valuesets.size(); i++) {
            CQLIdentifierObject valuesetIdentifier = null;
            if (valuesets.get(i).getType() != null) {
                valuesetIdentifier = new CQLIdentifierObject(null, valuesets.get(i).getDisplayName(), valuesets.get(i).getId());
            } else {
                valuesetIdentifier = new CQLIdentifierObject(null, valuesets.get(i).getName(), valuesets.get(i).getId());
            }

            valuesetIdentifiers.add(valuesetIdentifier);
        }

        this.valueSetCodeQualityDataSetList = valuesets;

        this.valuesets = valuesetIdentifiers;
    }

    public List<CQLIdentifierObject> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<CQLIdentifierObject> definitions) {
        this.definitions = definitions;
    }

    public List<CQLIdentifierObject> getParameters() {
        return parameters;
    }

    public void setParameters(List<CQLIdentifierObject> parameters) {
        this.parameters = parameters;
    }

    public List<CQLIdentifierObject> getFuncs() {
        return funcs;
    }

    public void setFuncs(List<CQLIdentifierObject> funcs) {
        this.funcs = funcs;
    }

    public QDSAttributesServiceAsync getQdsAttributesServiceAsync() {

        if (qdsAttributesServiceAsync == null) {
            qdsAttributesServiceAsync = (QDSAttributesServiceAsync) GWT.create(QDSAttributesService.class);
        }

        return qdsAttributesServiceAsync;
    }

    public void setQdsAttributesServiceAsync(QDSAttributesServiceAsync qdsAttributesServiceAsync) {
        this.qdsAttributesServiceAsync = qdsAttributesServiceAsync;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public String getCurrentCQLLibraryId() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getCqlLibraryId();
        } else {
            return "";
        }
    }

    public String getCurrentCQLLibraryLockedUserId() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getLockedUserId();
        } else {
            return "";
        }
    }

    public String getCurrentCQLLibraryLockedUserName() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getLockedUserName();
        } else {
            return "";
        }
    }

    public String getCurrentCQLLibraryLockedUserEmail() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getLockedUserEmail();
        } else {
            return "";
        }
    }

    public String getCurrentCQLLibraryeName() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getLibraryName();
        } else {
            return "";
        }
    }

    public boolean isCurrentModelTypeFhir() {
        return currentMeasureInfo != null ? isCurrentMeasureModelFhir() : isCurrentCQLLibraryModelTypeFhir();
    }

    public boolean isCurrentCQLLibraryModelTypeFhir() {
        return "FHIR".equals(currentLibraryInfo.getLibraryModelType());
    }

    public String getCurrentCQLLibraryModelType() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getLibraryModelType();
        } else {
            return "";
        }
    }

    public String getCurrentCQLLibraryVersion() {
        if (currentLibraryInfo != null) {
            return currentLibraryInfo.getCqlLibraryVersion();
        } else {
            return "";
        }
    }

    public void setCurrentCQLLibraryVersion(String s) {
        if (currentLibraryInfo != null) {
            currentLibraryInfo.setCqlLibraryVersion(s);
        }
    }

    public CQLLibraryServiceAsync getLibraryService() {
        if (cqlLibraryService == null) {
            cqlLibraryService = (CQLLibraryServiceAsync) GWT.create(CQLLibraryService.class);
        }
        return cqlLibraryService;
    }

    /**
     * Checks if is measure is CQL Measure depending
     * on Measure release version. If the releaseVersion is null, then it is not a CQL measure.
     *
     * @param releaseVersion the release version
     * @return true, if is CQL measure, false if it is not a cql measure or the release version is null
     */
    public boolean isCQLMeasure(String releaseVersion) {

        if (releaseVersion == null) {
            return false;
        }

        String str[] = releaseVersion.replace("v", "").split("\\.");
        int versionInt = Integer.parseInt(str[0]);
        if (versionInt < 5) {
            return false;
        }

        return true;
    }

    public List<CQLIdentifierObject> getIncludedDefNames() {
        return includedDefNames;
    }

    public void setIncludedDefNames(List<CQLIdentifierObject> includedDefNames) {
        this.includedDefNames = includedDefNames;
    }

    public List<CQLIdentifierObject> getIncludedFuncNames() {
        return includedFuncNames;
    }

    public void setIncludedFuncNames(List<CQLIdentifierObject> includedFuncNames) {
        this.includedFuncNames = includedFuncNames;
    }

    public List<CQLIdentifierObject> getIncludedValueSetNames() {
        return includedValueSetNames;
    }

    public void setIncludedValueSetNames(List<CQLIdentifierObject> includedValueSetNames) {
        this.includedValueSetNames = includedValueSetNames;
    }

    public List<CQLIdentifierObject> getIncludedParamNames() {
        return includedParamNames;
    }

    public void setIncludedParamNames(List<CQLIdentifierObject> includedParamNames) {
        this.includedParamNames = includedParamNames;
    }

    public List<CQLIdentifierObject> getIncludedCodeNames() {
        return includedCodeNames;
    }

    public void setIncludedCodeNames(List<CQLIdentifierObject> includedCodeNames) {
        this.includedCodeNames = includedCodeNames;
    }

    public void setIncludedValues(SaveUpdateCQLResult result) {
        includedValueSetNames = result.getCqlModel().getCQLIdentifierValueSet();
        includedCodeNames = result.getCqlModel().getCQLIdentifierCode();
        includedParamNames = result.getCqlModel().getCQLIdentifierParam();
        includedDefNames = result.getCqlModel().getCQLIdentifierDefinitions();
        includedFuncNames = result.getCqlModel().getCQLIdentifierFunctions();
    }

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

    public HashSet<String> getNonQuotesUnits() {
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

    public CQLConstantContainer getCqlConstantContainer() {
        return cqlConstantContainer;
    }

    public void setCqlConstantContainer(CQLConstantContainer cqlConstantContainer) {
        this.cqlConstantContainer = cqlConstantContainer;
    }

    public List<CQLQualityDataSetDTO> getValueSetCodeQualityDataSetList() {
        return valueSetCodeQualityDataSetList;
    }

    public void setShortCutKeyUnits() {
        shorcutKeyUnits.clear();
        for (Map.Entry<String, String> unitsMap : cqlConstantContainer.getCqlUnitMap().entrySet()) {

            MatContext.get();
            if (!unitsMap.getValue().equalsIgnoreCase(PLEASE_SELECT)) {
                if (!getNonQuotesUnits().contains(unitsMap.getValue())) {
                    shorcutKeyUnits.add("'" + unitsMap.getValue() + "'");
                } else {
                    shorcutKeyUnits.add(unitsMap.getValue());
                }
            }
        }
    }

    public List<String> getShorcutKeyUnits() {
        return shorcutKeyUnits;
    }


    public String getCurrentQDMVersion() {
        if (cqlConstantContainer == null) {
            return "";
        } else {
            return cqlConstantContainer.getCurrentQDMVersion();
        }

    }

    public PopulationServiceAsync getPopulationService() {

        if (populationService == null) {
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

    public Map<String, String> getProgramToLatestProfile() {
        return programToLatestProfile;
    }

    public void setProgramToLatestProfile(Map<String, String> programToLatestProfile) {
        this.programToLatestProfile = (HashMap<String, String>) programToLatestProfile;
    }

    public void getProgramsAndReleasesFromVSAC() {

        MatContext.get().getVsacapiServiceAsync().getVSACProgramsReleasesAndProfiles(new AsyncCallback<VsacApiResult>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
            }

            @Override
            public void onSuccess(VsacApiResult result) {
                if (result != null) {
                    MatContext.get().setProgramToReleases(result.getProgramToReleases());
                    MatContext.get().setProgramToLatestProfile(result.getProgramToProfiles());
                }

            }
        });

    }

    public Map<String, List<? extends HasListBox>> getSelectionMap() {
        return selectionMap;
    }

    public void setSelectionMap(Map<String, List<? extends HasListBox>> selectionMap) {
        this.selectionMap = (HashMap<String, List<? extends HasListBox>>) selectionMap;
    }

    public CQLModel getCQLModel() {
        return cqlModel;
    }

    public void setCQLModel(CQLModel model) {
        if (model != null) {
            this.cqlModel = model;
        }
    }

    public void buildBonnieLink() {
        BonnieServiceAsync bonnie = (BonnieServiceAsync) GWT.create(BonnieService.class);

        bonnie.getBonnieAccessLink(new AsyncCallback<String>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
            }

            @Override
            public void onSuccess(String result) {
                bonnieLink = result;
            }

        });
    }

    public String getBonnieLink() {
        return bonnieLink;
    }

    public void createSelectionMap(List<? extends HasListBox> result) {
        List<? extends HasListBox> defaultList = result;
        List<? extends HasListBox> proportionRatioList = defaultList.stream().filter(x -> "Proportion".equals(x.getItem()) || "Ratio".equals(x.getItem())).collect(Collectors.toList());
        List<? extends HasListBox> continuousVariableList = defaultList.stream().filter(x -> "Continuous Variable".equals(x.getItem())).collect(Collectors.toList());
        setSelectionMap(new HashMap<String, List<? extends HasListBox>>() {
            private static final long serialVersionUID = -8329823017052579496L;

            {
                put(MatContext.PLEASE_SELECT, defaultList);
                put(CompositeMethodScoringConstant.ALL_OR_NOTHING, proportionRatioList);
                put(CompositeMethodScoringConstant.OPPORTUNITY, proportionRatioList);
                put(CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR, continuousVariableList);
            }
        });
    }

    public List<CompositeMeasureScoreDTO> buildCompositeScoringChoiceList() {
        List<CompositeMeasureScoreDTO> compositeChoices = new ArrayList<>();
        compositeChoices.add(new CompositeMeasureScoreDTO("1", CompositeMethodScoringConstant.ALL_OR_NOTHING));
        compositeChoices.add(new CompositeMeasureScoreDTO("2", CompositeMethodScoringConstant.OPPORTUNITY));
        compositeChoices.add(new CompositeMeasureScoreDTO("3", CompositeMethodScoringConstant.PATIENT_LEVEL_LINEAR));
        return compositeChoices;
    }

    public void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption) {
        listBox.clear();
        listBox.addItem(defaultOption, "");
        if (itemList != null) {
            for (HasListBox listBoxContent : itemList) {
                listBox.addItem(listBoxContent.getItem(), listBoxContent.getItem());
            }
        }
    }

    public List<String> getPatientBasedIndicatorOptions(String measureScoringMethod) {
        List<String> patientBasedList = new ArrayList<>();
        patientBasedList.add("No");
        if (!MatConstants.CONTINUOUS_VARIABLE.equalsIgnoreCase(measureScoringMethod)) {
            patientBasedList.add("Yes");
        }
        return patientBasedList;
    }

    public void getMeasureTypeListFromDataBase() {
        getMeasureService().getAllMeasureTypes(new AsyncCallback<List<MeasureType>>() {

            @Override
            public void onFailure(Throwable caught) {
                // noop
            }

            @Override
            public void onSuccess(List<MeasureType> result) {
                result.sort(Comparator.comparing(MeasureType::getDescription));
                MatContext.get().setMeasureTypeList(result);
            }
        });
    }

    public void setFeatureFlags(Map<String, Boolean> featureFlagMap) {
        this.featureFlagMap = featureFlagMap;
    }

    // returns if specific feature flag is on/off
    public boolean getFeatureFlagStatus(String flag) {
        return featureFlagMap.getOrDefault(flag, false);
    }

    public List<MeasureType> getMeasureTypeList() {
        return measureTypeList;
    }

    public void setMeasureTypeList(List<MeasureType> measureTypeList) {
        this.measureTypeList = measureTypeList;
    }

    public ClickHandler addClickHandlerToResetTimeoutWarning() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                MatContext.get().restartTimeoutWarning();
            }
        };
    }

    public Map<String, String> getExpressionToReturnTypeMap() {
        return expressionToReturnTypeMap;
    }

    public void setExpressionToReturnTypeMap(Map<String, String> expressionToReturnTypeMap) {
        this.expressionToReturnTypeMap = expressionToReturnTypeMap;
    }

    public Map<String, String> getHarpUserInfo() {
        return harpUserInfo;
    }

    public void setHarpUserInfo(Map<String, String> harpUserInfo) {
        this.harpUserInfo = harpUserInfo;
    }
}
