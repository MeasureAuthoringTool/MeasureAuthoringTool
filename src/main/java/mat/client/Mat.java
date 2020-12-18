package mat.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.admin.ManageAdminPresenter;
import mat.client.admin.ManageCQLLibraryAdminPresenter;
import mat.client.admin.ManageCQLLibraryAdminView;
import mat.client.admin.reports.ManageAdminReportingPresenter;
import mat.client.admin.reports.ManageAdminReportingView;
import mat.client.bonnie.BonnieModal;
import mat.client.codelist.ListBoxCodeProvider;
import mat.client.cql.CQLLibraryHistoryView;
import mat.client.cql.CQLLibraryShareView;
import mat.client.cql.CQLLibraryVersionView;
import mat.client.cql.NewLibraryView;
import mat.client.event.BackToLoginPageEvent;
import mat.client.event.BackToMeasureLibraryPage;
import mat.client.event.CQLLibraryEditEvent;
import mat.client.event.EditCompositeMeasureEvent;
import mat.client.event.HarpSupportEvent;
import mat.client.event.LogoffEvent;
import mat.client.event.MeasureEditEvent;
import mat.client.event.ReturnToLoginEvent;
import mat.client.event.SwitchUserEvent;
import mat.client.event.TimedOutEvent;
import mat.client.export.ManageExportView;
import mat.client.harp.HarpUserVerificationEvent;
import mat.client.harp.HarpUserVerificationPresenter;
import mat.client.harp.HarpUserVerificationView;
import mat.client.harp.SuccessfulHarpLoginEvent;
import mat.client.login.LoginModel;
import mat.client.login.service.CurrentUserInfo;
import mat.client.measure.ComponentMeasureDisplay;
import mat.client.measure.ManageMeasureHistoryView;
import mat.client.measure.ManageMeasurePresenter;
import mat.client.measure.ManageMeasureSearchView;
import mat.client.measure.ManageMeasureShareView;
import mat.client.measure.ManageMeasureVersionView;
import mat.client.measure.NewCompositeMeasureView;
import mat.client.measure.NewMeasureView;
import mat.client.measure.TransferOwnershipView;
import mat.client.myAccount.MyAccountPresenter;
import mat.client.myAccount.MyAccountView;
import mat.client.myAccount.PersonalInformationPresenter;
import mat.client.myAccount.PersonalInformationView;
import mat.client.shared.MatContext;
import mat.client.shared.MatTabLayoutPanel;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.ui.MATTabPanel;
import mat.client.umls.ManageUmlsPresenter;
import mat.client.umls.UmlsLoginDialogBox;
import mat.client.umls.service.VsacTicketInformation;
import mat.client.util.ClientConstants;
import mat.client.util.FeatureFlagConstant;
import mat.shared.ConstantMessages;
import mat.shared.HarpConstants;
import mat.shared.bonnie.result.BonnieUserInformationResult;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static mat.shared.HarpConstants.OKTA_TOKEN_STORAGE;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mat extends MainLayout implements EntryPoint, Enableable, TabObserver {

    private static final Logger logger = Logger.getLogger(Mat.class.getSimpleName());
    public static boolean harpUserVerificationInProgress = false;
    private final ListBoxCodeProvider listBoxCodeProvider = new ListBoxCodeProvider();
    private Map<String, String> harpUserInfo = new HashMap<>();
    private Panel content;
    private HarpUserVerificationPresenter harpUserVerificationPresenter;
    private List<MatPresenter> presenterList;
    private MatPresenter adminPresenter;
    private MatPresenter myAccountPresenter;
    private String currentUserRole = ClientConstants.USER_STATUS_NOT_LOGGEDIN;
    private MatTabLayoutPanel mainTabLayout;
    private String mainTabLayoutID = ConstantMessages.MAIN_TAB_LAYOUT_ID;
    private MeasureComposerPresenter measureComposer;
    private ManageMeasurePresenter measureLibrary;
    private ManageCQLLibraryAdminPresenter cqlLibraryAdminPresenter;
    private CqlComposerPresenter cqlComposer;
    private CqlLibraryPresenter cqlLibrary;
    private ManageAdminReportingPresenter reportingPresenter;
    private int tabIndex;

    public static void focusSkipLists(String skipstr) {
        Widget widget = SkipListBuilder.buildSkipList(skipstr);
        getSkipList().clear();
        getSkipList().add(widget);
    }

    public static native String getUserAgent() /*-{
        return navigator.userAgent.toLowerCase();
    }-*/;


    public static void removeInputBoxFromFocusPanel(Element element) {
        if (element.hasChildNodes() && element.getFirstChild().getNodeName().equalsIgnoreCase("input")) {
            // this is done for 508 issue to fix the input box in FF
            element.removeChild(element.getFirstChild());
        }
    }

    private final AsyncCallback<CurrentUserInfo> loadCurrentUserCallback = new AsyncCallback<CurrentUserInfo>() {

        @Override
        public void onFailure(final Throwable caught) {
            logger.log(Level.SEVERE, "Error in getCurrentUser. Error message: " + caught.getMessage(), caught);
            logout();
        }

        @Override
        public void onSuccess(final CurrentUserInfo result) {
            logger.log(Level.INFO, "SessionService::getCurrentUser -> onSuccess");
            MatContext.get().getCurrentReleaseVersion(new AsyncCallback<String>() {

                @Override
                public void onFailure(Throwable caught) {
                    logger.log(Level.SEVERE, "Error in getCurrentReleaseVersion. Error message: " + caught.getMessage(), caught);
                    logout();
                }

                @Override
                public void onSuccess(String resultMatVersion) {
                    logger.log(Level.INFO, "SessionService::getCurrentReleaseVersion -> onSuccess");
                    if (result == null || (checkIfResultIsNotNull(result) && !result.activeSessionId.equals(result.currentSessionId))) {
                        logger.log(Level.SEVERE, "Current session is not valid.");
                        logout();
                    } else {
                        final Date lastSignIn = result.signInDate;
                        final Date lastSignOut = result.signOutDate;
                        final Date current = new Date();
                        final boolean isAlreadySignedIn = MatContext.get().isAlreadySignedIn(lastSignOut, lastSignIn, current);
                        MatContext.get().setUserSignInDate(result.userId);
                        MatContext.get().setUserInfo(result);
                        MatContext.get().setMatVersion(resultMatVersion);
                        if (ClientConstants.ADMINISTRATOR.equals(MatContext.get().getLoggedInUserRole())) {
                            getBecomeUser().addClickHandler(event -> {
                                logger.log(Level.INFO, "Become a USER");
                                switchRole(ClientConstants.USER_ROLE);
                            });
                            getBecomeTopLevelUser().addClickHandler(event -> {
                                logger.log(Level.INFO, "Become a top level USER");
                                switchRole(ClientConstants.SUPER_USER_ROLE);
                            });
                        }

                        if (!MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
                            Map<String, Boolean> featureFlagMap = MatContext.get().getFeatureFlags();
                            featureFlagMap.put(FeatureFlagConstant.MAT_ON_FHIR, MatContext.get().getCurrentUserInfo().isFhirAccessible);
                            MatContext.get().setFeatureFlags(featureFlagMap);
                        }

                        loadMatWidgets(isAlreadySignedIn);
                    }
                }

                private boolean checkIfResultIsNotNull(CurrentUserInfo result) {
                    return result != null && result.activeSessionId != null && result.currentSessionId != null;
                }
            });
        }
    };


    private void switchUser(String newUserId) {
        logger.log(Level.INFO, "Switching to user: " + newUserId);
        showLoadingMessage();
        MatContext.get().getLoginService().switchUser(harpUserInfo, newUserId, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "LoginService::.switchUser -> onFailure: " + caught.getMessage(), caught);
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
            }

            @Override
            public void onSuccess(Void result) {
                logger.log(Level.INFO, "LoginService::.switchRole -> onSuccess");
                getLinksPanel().clear();
                getContentPanel().clear();
                MatContext.get().getCurrentUser(loadCurrentUserCallback);
            }
        });
    }

    private void switchRole(String newRole) {
        logger.log(Level.INFO, "Switching to role: " + newRole);
        showLoadingMessage();
        MatContext.get().getLoginService().switchRole(newRole, new AsyncCallback<Void>() {

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "LoginService::.switchRole -> onFailure: " + caught.getMessage(), caught);
                Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
            }

            @Override
            public void onSuccess(Void result) {
                logger.log(Level.INFO, "LoginService::.switchRole -> onSuccess");
                MatContext.get().setLoggedInUserRole(newRole);
                getLinksPanel().clear();
                getContentPanel().clear();
                loadMatWidgets(true);
            }
        });
    }

    private final AsyncCallback<LoginModel> harpUserSessionSetupCallback = new AsyncCallback<LoginModel>() {
        @Override
        public void onFailure(Throwable throwable) {
            logger.log(Level.SEVERE, "LoginService::initSession -> onFailure: " + throwable.getMessage(), throwable);
            if (throwable.getMessage().contains("MAT_ACCOUNT_REVOKED_LOCKED")) {
                String msg = "HARP User does not have access to the MAT, redirecting to access support page";
                logger.log(Level.SEVERE, msg);
                MatContext.get().recordTransactionEvent(null, null, "MAT_ACCOUNT_REVOKED_LOCKED", msg, 0);
            } else if (throwable.getMessage().contains("HARP_ID_NOT_FOUND")) {
                String msg = "Harp ID not found";
                logger.log(Level.SEVERE, msg);
                MatContext.get().recordTransactionEvent(null, null, "MAT_HARP_ID_NOT_FOUND", msg, 0);
            }
            logger.log(Level.INFO, "Fire HarpSupportEvent");
            MatContext.get().getEventBus().fireEvent(new HarpSupportEvent());

        }

        @Override
        public void onSuccess(LoginModel loginModel) {
            logger.log(Level.INFO, "LoginService::initSession -> onSuccess");
            initPage();
        }
    };

    private MatPresenter buildAdminPresenter() {
        ManageAdminPresenter adminPresenter = new ManageAdminPresenter();
        return adminPresenter;
    }


    private MeasureComposerPresenter buildMeasureComposer() {
        return new MeasureComposerPresenter();
    }


    private CqlComposerPresenter buildCqlComposer() {
        return new CqlComposerPresenter();
    }


    private ManageMeasurePresenter buildMeasureLibraryWidget(Boolean isAdmin) {
        ManageMeasurePresenter measurePresenter = null;
        if (isAdmin) {
            ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
            TransferOwnershipView transferOS = new TransferOwnershipView();
            ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();

            measurePresenter = new ManageMeasurePresenter(measureSearchView, null, null, null, null, null, historyView,
                    null, transferOS);
        } else {
            ManageMeasureSearchView measureSearchView = new ManageMeasureSearchView();
            NewMeasureView measureDetailView = new NewMeasureView();
            NewCompositeMeasureView compositeMeasureDetailView = new NewCompositeMeasureView();
            ManageMeasureVersionView versionView = new ManageMeasureVersionView();
            ManageMeasureShareView measureShareView = new ManageMeasureShareView();
            ManageMeasureHistoryView historyView = new ManageMeasureHistoryView();
            ManageExportView exportView = new ManageExportView();
            ComponentMeasureDisplay componentMeasureDisplay = new ComponentMeasureDisplay();
            componentMeasureDisplay.getBreadCrumbPanel().setVisible(false);

            measurePresenter = new ManageMeasurePresenter(measureSearchView, measureDetailView,
                    compositeMeasureDetailView, componentMeasureDisplay, measureShareView, exportView,
                    historyView, versionView, null);
        }

        return measurePresenter;

    }

    private CqlLibraryPresenter buildCqlLibraryWidget() {
        CqlLibraryView cqlLibraryView = new CqlLibraryView();
        NewLibraryView detailView = new NewLibraryView();
        CQLLibraryVersionView versionView = new CQLLibraryVersionView();
        CQLLibraryShareView shareView = new CQLLibraryShareView();
        CQLLibraryHistoryView historyView = new CQLLibraryHistoryView();
        CqlLibraryPresenter cqlLibraryPresenter = new CqlLibraryPresenter(cqlLibraryView, detailView,
                versionView, shareView, historyView);
        return cqlLibraryPresenter;
    }


    private MatPresenter buildMyAccountWidget() {
        PersonalInformationView informationView = new PersonalInformationView();
        PersonalInformationPresenter personalInfoPrsnter = new PersonalInformationPresenter(informationView);
        MyAccountPresenter accountPresenter = new MyAccountPresenter(new MyAccountView(personalInfoPrsnter));
        return accountPresenter;
    }

    @Override
    protected void initEntryPoint() {
        MatContext.get().setCurrentModule(ConstantMessages.MAT_MODULE);
        content = getContentPanel();

        getSignOut().addClickHandler(event -> logout());

        MatContext.get().getEventBus().addHandler(BackToLoginPageEvent.TYPE, event -> logout());

        MatContext.get().getEventBus().addHandler(LogoffEvent.TYPE, event -> {
            Mat.hideLoadingMessage();
            Mat.showSignOutMessage();
            logout();
        });

        MatContext.get().getEventBus().addHandler(HarpSupportEvent.TYPE, event -> {
            Mat.hideLoadingMessage();
            logout(ClientConstants.HTML_HARP_SUPPORT);
        });

        MatContext.get().getEventBus().addHandler(HarpUserVerificationEvent.TYPE, event -> {
            logger.log(Level.INFO, "Processing HarpUserVerificationEvent");
            final HarpUserVerificationView harpUserVerificationView = new HarpUserVerificationView();
            harpUserVerificationPresenter = new HarpUserVerificationPresenter(harpUserVerificationView);
            content.clear();
            harpUserVerificationInProgress = true;
            harpUserVerificationPresenter.go(content);
        });

        MatContext.get().getEventBus().addHandler(SwitchUserEvent.TYPE, event -> {
            logger.log(Level.INFO, "Processing SwitchUserEvent");
            switchUser(event.getNewUserId());
        });

        MatContext.get().getEventBus().addHandler(ReturnToLoginEvent.TYPE, event -> {
            logger.log(Level.INFO, "Processing ReturnToLoginEvent");
            content.clear();
            logout();
        });

        MatContext.get().getEventBus().addHandler(SuccessfulHarpLoginEvent.TYPE, event -> {
            logger.log(Level.INFO, "Processing SuccessfulHarpLoginEvent");
            content.clear();
            harpUserVerificationInProgress = false;
            getLinksPanel().clear();
            initPage();
        });

        String oktaToken = getOktaToken();

        if (oktaToken != null) {
            // Get user info from the Tokens in Local Storage.
            parseOktaToken(oktaToken);
            String accessToken = MatContext.get().getAccessToken();

            // Validate tokens
            MatContext.get().getHarpService().validateToken(accessToken, new AsyncCallback<Boolean>() {
                @Override
                public void onFailure(Throwable throwable) {
                    logger.log(Level.SEVERE, "HarpService::validateToken -> onFailure: Invalid token", throwable);
                    // Invalid token
                    MatContext.get().getEventBus().fireEvent(new LogoffEvent());
                }

                @Override
                public void onSuccess(Boolean aBoolean) {
                    logger.log(Level.INFO, "HarpService::validateToken -> onSuccess");
                    getUserInfo(accessToken);
                }
            });
        } else {
            // Invalid token
            logger.log(Level.INFO, "OktaToken is missing. Logoff the user.");
            MatContext.get().getEventBus().fireEvent(new LogoffEvent());
        }
    }

    private String getOktaToken() {
        // The HARP Sign-In widget stores tokens in Local Storage.
        Storage localStorage = Storage.getLocalStorageIfSupported();

        return localStorage == null ? null : localStorage.getItem(OKTA_TOKEN_STORAGE);
    }

    private void linkHarpMatAccounts() {
        logger.log(Level.INFO, "linkHarpMatAccounts - enter");
        // Check if user is already linked with a MAT Account
        MatContext.get().getLoginService().checkForAssociatedHarpId(harpUserInfo.get(HarpConstants.HARP_ID), new AsyncCallback<Boolean>() {

            @Override
            public void onFailure(Throwable caught) {
                String msg = "Error in checkForAssociatedHarpId. Error message: " + caught.getMessage();
                logger.log(Level.SEVERE, msg, caught);
                MatContext.get().recordTransactionEvent(null, null, "MAT_ACCOUNT_HARP_LINK", msg, 0);
                logger.log(Level.INFO, "Fire HarpSupportEvent");
                MatContext.get().getEventBus().fireEvent(new HarpSupportEvent());
            }

            @Override
            public void onSuccess(Boolean result) {
                logger.log(Level.INFO, "LoginService::checkForAssociatedHarpId -> onSuccess " + result);
                if (result) {
                    // Update the User name from HARP and proceed to MAT home page.
                    MatContext.get().initSession(harpUserInfo, harpUserSessionSetupCallback);
                } else {
                    // Display form panel to fetch userId and password
                    logger.log(Level.INFO, "Fire HarpUserVerificationEvent");
                    MatContext.get().getEventBus().fireEvent(new HarpUserVerificationEvent());
                }
            }
        });
        logger.log(Level.INFO, "linkHarpMatAccounts - exit");
    }

    private void parseOktaToken(String oktaToken) {
        JSONValue tokens = JSONParser.parseStrict(oktaToken);
        String accessToken = tokens.isObject().get("accessToken").isObject().get("accessToken").isString().stringValue();
        String idToken = tokens.isObject().get("idToken").isObject().get("idToken").isString().stringValue();

        MatContext.get().setIdToken(idToken);
        MatContext.get().setAccessToken(accessToken);
        harpUserInfo.put(HarpConstants.ACCESS_TOKEN, accessToken);
        harpUserInfo.put(HarpConstants.HARP_ID,
                tokens.isObject().get("idToken").isObject().get("claims").isObject().get("preferred_username").isString().stringValue());
    }

    private void getUserInfo(String accessToken) {
        MatContext.get().getHarpService().getUserInfo(accessToken, new AsyncCallback<Map<String, String>>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.log(Level.SEVERE, "HarpService::getUserInfo -> onFailure", throwable);
                // Invalid token
                MatContext.get().getEventBus().fireEvent(new LogoffEvent());
            }

            @Override
            public void onSuccess(Map<String, String> userInfo) {
                logger.log(Level.INFO, "HarpService::getUserInfo -> onSuccess");

                // Retrieve user details from Okta's userinfo endpoint.
                harpUserInfo.putAll(userInfo);
                MatContext.get().setHarpUserInfo(harpUserInfo);

                linkHarpMatAccounts();
            }
        });
    }

    private void initPage() {
        MatContext.get().getFeatureFlagService().findFeatureFlags(new AsyncCallback<Map<String, Boolean>>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error in FeatureFlagService.findFeatureFlags. Error message: " + caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(Map<String, Boolean> result) {
                logger.log(Level.INFO, "FeatureFlagService::findFeatureFlags -> onSuccess");
                MatContext.get().setFeatureFlags(result);
            }
        });
        MatContext.get().setListBoxCodeProvider(listBoxCodeProvider);

        // Init session with current user info.
        MatContext.get().getCurrentUser(loadCurrentUserCallback);

        History.addValueChangeHandler(event -> {
            final String historyToken = event.getValue();

            if ((historyToken == null) || historyToken.isEmpty()) {
                History.newItem(mainTabLayoutID + 0, false);
            } else if (!MatContext.get().isLoading()) {
                // Parse the history token

                try {
                    for (Object key : MatContext.get().tabRegistry.keySet()) {
                        if (key instanceof String) {
                            String k = (String) key;
                            if (historyToken.contains(k)) {
                                final String tabIndexToken = historyToken.substring(k.length());
                                final int tabIndex = Integer.parseInt(tabIndexToken);
                                MATTabPanel tp = (MATTabPanel) MatContext.get().tabRegistry.get(key);
                                /* Suppressing selection of MAIN_TAB_LAYOUT_ID+mainTabLayout.selectedIndex
                                 * if already selected
                                 */
                                if (!History.getToken().equals(mainTabLayoutID + mainTabLayout.getSelectedIndex())) {
                                    tp.selectTab(tabIndex);
                                }
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    History.newItem(mainTabLayoutID + 0, false);
                }
            } else {
                MatContext.get().fireLoadingAlert();
                //reload
                History.newItem(historyToken, false);
            }
        });


        MatContext.get().getEventBus().addHandler(MeasureEditEvent.TYPE, event -> {
            mainTabLayout.selectTab(presenterList.indexOf(measureComposer));
            focusSkipLists("MeasureComposer");
        });

        MatContext.get().getEventBus().addHandler(CQLLibraryEditEvent.TYPE, event -> {
            mainTabLayout.selectTab(presenterList.indexOf(cqlComposer));
            focusSkipLists("CqlComposer");
        });

        MatContext.get().getEventBus().addHandler(BackToMeasureLibraryPage.TYPE, event -> {
            mainTabLayout.selectTab(presenterList.indexOf(measureLibrary));
            focusSkipLists("Measure Library");
        });

        MatContext.get().getEventBus().addHandler(EditCompositeMeasureEvent.TYPE, event -> {
        });

        GWT.setUncaughtExceptionHandler(caught -> {
            logger.log(Level.SEVERE, "UncaughtException: " + caught.getMessage(), caught);
            hideLoadingMessage();
            MatContext.get().recordTransactionEvent(null, null, null,
                    "Unhandled Exception: " + caught.getLocalizedMessage(), 0);
        });
    }

    @SuppressWarnings("unchecked")
    private void loadMatWidgets(boolean isAlreadySignedIn) {
        logger.log(Level.INFO, "loadMatWidgets");
        MatContext.get().startUserLockUpdate();
        MatContext.get().recordTransactionEvent(null, null, "LOGIN_EVENT", null, 1);

        mainTabLayout = new MatTabLayoutPanel(this);
        mainTabLayout.getElement().setAttribute("id", "matMainTabPanel");
        mainTabLayout.getElement().setAttribute("aria-role", "TabList");

        MatContext.get().tabRegistry.put(mainTabLayoutID, mainTabLayout);
        MatContext.get().enableRegistry.put(mainTabLayoutID, this);
        mainTabLayout.addSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(final SelectionEvent<Integer> event) {
                final int index = event.getSelectedItem();
                final String newToken = mainTabLayoutID + index;
                if (!History.getToken().equals(newToken)) {
                    MatContext.get().recordTransactionEvent(null, null, "MAIN_TAB_EVENT", newToken, 1);
                    History.newItem(newToken, false);
                }
            }

        });

        String title;
        tabIndex = 0;
        presenterList = new LinkedList<>();
        currentUserRole = MatContext.get().getLoggedInUserRole();

        buildLinksPanel();
        if (!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            MatContext.get().getCQLConstants();
            MatContext.get().getMeasureTypeListFromDataBase();

            measureLibrary = buildMeasureLibraryWidget(false);
            title = ClientConstants.TITLE_MEASURE_LIB;
            mainTabLayout.add(measureLibrary.getWidget(), title, true);
            presenterList.add(measureLibrary);

            measureComposer = buildMeasureComposer();
            title = ClientConstants.TITLE_MEASURE_COMPOSER;
            mainTabLayout.add(measureComposer.getWidget(), title, true);
            presenterList.add(measureComposer);

            cqlLibrary = buildCqlLibraryWidget();
            title = ClientConstants.TITLE_CQL_LIB;
            mainTabLayout.add(cqlLibrary.getWidget(), title, true);
            presenterList.add(cqlLibrary);

            cqlComposer = buildCqlComposer();
            title = ClientConstants.TITLE_CQL_COMPOSER;
            mainTabLayout.add(cqlComposer.getWidget(), title, true);
            presenterList.add(cqlComposer);

            title = ClientConstants.TITLE_MY_ACCOUNT;
            myAccountPresenter = buildMyAccountWidget();
            mainTabLayout.add(myAccountPresenter.getWidget(), title, false);
            presenterList.add(myAccountPresenter);

            tabIndex = presenterList.indexOf(myAccountPresenter);
            createUMLSLinks();
            createBonnieLinks();
            setUMLSActiveLink();
            setBonnieActiveLink();
        } else if (currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            adminPresenter = buildAdminPresenter();
            title = ClientConstants.TITLE_ADMIN;
            mainTabLayout.add(adminPresenter.getWidget(), title, true);
            presenterList.add(adminPresenter);

            measureLibrary = buildMeasureLibraryWidget(true);
            title = ClientConstants.TITLE_MEASURE_LIB_CHANGE_OWNERSHIP;
            mainTabLayout.add(measureLibrary.getWidget(), title, true);
            presenterList.add(measureLibrary);

            ManageCQLLibraryAdminView cqlLibraryAdminView = new ManageCQLLibraryAdminView();
            CQLLibraryHistoryView historyView = new CQLLibraryHistoryView();
            TransferOwnershipView transferOS = new TransferOwnershipView();
            cqlLibraryAdminPresenter = new ManageCQLLibraryAdminPresenter(cqlLibraryAdminView, historyView, transferOS);
            title = ClientConstants.CQL_LIBRARY_OWNERSHIP;
            mainTabLayout.add(cqlLibraryAdminPresenter.getWidget(), title, true);
            presenterList.add(cqlLibraryAdminPresenter);

            ManageAdminReportingView adminReportingView = new ManageAdminReportingView();
            reportingPresenter = new ManageAdminReportingPresenter(adminReportingView);
            title = ClientConstants.ADMIN_REPORTS;
            mainTabLayout.add(reportingPresenter.getWidget(), title, true);
            presenterList.add(reportingPresenter);

            title = ClientConstants.TITLE_ADMIN_ACCOUNT;
            myAccountPresenter = buildMyAccountWidget();
            mainTabLayout.add(myAccountPresenter.getWidget(), title, true);
            presenterList.add(myAccountPresenter);
            tabIndex = presenterList.indexOf(myAccountPresenter);
        } else {
            logger.log(Level.INFO, "Fire LogoffEvent");
            MatContext.get().getEventBus().fireEvent(new LogoffEvent());
        }
        mainTabLayout.setHeight("100%");

        setHeader(MatContext.get().getMatVersion().replaceAll("[a-zA-Z]", ""), getHomeLink());

        setSignedInAsNameOrg();

        getHomeLink().addClickHandler(event -> MatContext.get().redirectToMatPage(ClientConstants.HTML_MAT));

        getProfile().addClickHandler(event -> mainTabLayout.selectTab(presenterList.indexOf(myAccountPresenter)));

        getSignOut().addClickHandler(event -> MatContext.get().getEventBus().fireEvent(new LogoffEvent()));

        setIndicatorsHidden();

        if (!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            mainTabLayout.selectTab(presenterList.indexOf(measureLibrary));
        } else if (currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)) {
            mainTabLayout.selectTab(presenterList.indexOf(adminPresenter));
        }

        getContentPanel().add(mainTabLayout);

        getContentPanel().addKeyPressHandler(event -> MatContext.get().restartTimeoutWarning());

        getContentPanel().addClickHandler(event -> MatContext.get().restartTimeoutWarning());

        getUMLSButton().addClickHandler(event -> showUMLSModal(MatContext.get().getLoggedInUserFirstName(), isAlreadySignedIn));

        getBonnieSignInButton().addClickHandler(event -> showBonnieModal());

        getBonnieDisconnectButton().addClickHandler(event -> revokeBonnieAccessTokenForUser());

        /*
         * This block adds a special generic handler for any mouse clicks in the mainContent for ie browser.
         * Need to add this handler in order to keep track of the user activity in IE8 Browser.
         */
        if (getUserAgent().contains(ClientConstants.MSIE)) {
            getContentPanel().addMouseUpHandler(event -> MatContext.get().restartTimeoutWarning());
        }

        MatContext.get().getEventBus().addHandler(TimedOutEvent.TYPE, event -> {
            if (measureComposer != null) {
                Mat.focusSkipLists("MainContent");
            }
        });

        hideLoadingMessage();

        MatContext.get().restartTimeoutWarning();
    }

    private void logout() {
        logout(null);
    }

    private void logout(String redirectTo) {
        logger.log(Level.INFO, "logout " + redirectTo);
        // Revoke Access Token
        MatContext.get().getHarpService().revoke(MatContext.get().getAccessToken(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.log(Level.SEVERE, "HarpService::revoke -> onFailure:" + throwable.getMessage(), throwable);
                // log user out by removing their Okta browser session.
                harpOktaLogout(redirectTo);
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                logger.log(Level.SEVERE, "HarpService::revoke -> onSuccess:" + aBoolean);
                harpOktaLogout(redirectTo);
            }
        });
    }

    private void harpOktaLogout(String redirectTo) {
        logger.log(Level.INFO, "harpOktaLogout " + redirectTo);
        MatContext.get().getHarpService().getHarpUrl(new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable throwable) {
                logger.log(Level.SEVERE, "HarpService::getHarpUrl -> onFailure " + throwable.getMessage(), throwable);
            }

            @Override
            public void onSuccess(String harpUrl) {
                logger.log(Level.INFO, "HarpService::getHarpUrl -> onSuccess");
                MatContext.get().getSynchronizationDelegate().setLogOffFlag();
                harpLogout(harpUrl);
                removeOktaTokens();
                if (redirectTo == null) {
                    // MAT logout operation, but don't redirect.
                    MatContext.get().handleSignOut("SIGN_OUT_EVENT", redirectTo);

                    // Redirect to Login after 1 second wait.
                    redirectToLogin();
                }
                // MAT Logout operation with specified redirect.
                MatContext.get().handleSignOut("SIGN_OUT_EVENT", redirectTo);
            }
        });
    }

    private void removeOktaTokens() {
        logger.log(Level.INFO, "removeOktaTokens");
        Storage localStorage = Storage.getLocalStorageIfSupported();
        localStorage.removeItem(OKTA_TOKEN_STORAGE);
    }

    private void showUMLSModal(String userFirstName, boolean isAlreadySignedIn) {
        final UmlsLoginDialogBox login = new UmlsLoginDialogBox();
        login.showUMLSLogInDialog(this.measureComposer.getCQLWorkspacePresenter(), this.cqlComposer.getCQLStandaloneWorkspacePresenter());
        new ManageUmlsPresenter(login, userFirstName, isAlreadySignedIn);
        login.showModal();
    }

    private void showBonnieModal() {
        BonnieModal bonnieModal = new BonnieModal();
        bonnieModal.show();
    }

    private void revokeBonnieAccessTokenForUser() {
        MatContext.get().getBonnieService().revokeBonnieAccessTokenForUser(MatContext.get().getLoggedinUserId(),
                new AsyncCallback<Void>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        logger.log(Level.SEVERE, "Error in BonnieService.revokeBonnieAccessTokenForUser. Error message: " + caught.getMessage(), caught);
                        hideBonnieActive(false);
                    }

                    @Override
                    public void onSuccess(Void result) {
                        hideBonnieActive(true);
                        getBonnieSignInButton().getElement().focus();
                        getBonnieSignInButton().getElement().setAttribute("role", "alert");
                        getBonnieSignInButton().getElement().setAttribute("aria-label", "Click here to sign in to Bonnie.");
                    }

                });
    }

    private void setUMLSActiveLink() {
        MatContext.get().getVsacapiServiceAsync().getTicketGrantingToken(new AsyncCallback<VsacTicketInformation>() {

            @Override
            public void onSuccess(VsacTicketInformation result) {
                boolean loggedInToUMLS = (result != null);

                hideUMLSActive(!loggedInToUMLS);
                MatContext.get().setUMLSLoggedIn(loggedInToUMLS);
            }

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error in VsacapiService.getTicketGrantingToken. Error message: " + caught.getMessage(), caught);
                hideUMLSActive(true);
                MatContext.get().setUMLSLoggedIn(false);
            }
        });
    }

    private void setBonnieActiveLink() {
        String matUserId = MatContext.get().getLoggedinUserId();
        MatContext.get().getBonnieService().getBonnieUserInformationForUser(matUserId, new AsyncCallback<BonnieUserInformationResult>() {

            @Override
            public void onSuccess(BonnieUserInformationResult result) {
                hideBonnieActive(false);
            }

            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "Error in BonnieService.getBonnieUserInformationForUser. Error message: " + caught.getMessage(), caught);
                hideBonnieActive(true);
            }
        });
    }

    private void redirectToLogin() {
        logger.log(Level.INFO, "redirectToLogin");
        hideLoadingMessage();
        /*
         * Added a timer to have a delay before redirect since
         * this was causing the firefox javascript exception.
         */
        final Timer timer = new Timer() {
            @Override
            public void run() {
                // Let's sign out the user, so there is no lurking okta token.
                MatContext.get().redirectToHtmlPage(ClientConstants.HTML_LOGIN);
            }
        };
        timer.schedule(1000);
    }

    @Override
    public void setEnabled(boolean enabled) {
        mainTabLayout.setEnabled(enabled);
    }

    @Override
    public boolean isValid() {
        MatContext.get().setErrorTabIndex(-1);
        MatContext.get().setErrorTab(false);
        Integer selectedIndex = mainTabLayout.getSelectedIndex();

        if (presenterList.get(selectedIndex) instanceof TabObserver) {
            TabObserver tabObserver = (TabObserver) presenterList.get(selectedIndex);
            return tabObserver.isValid();
        }

        return true;
    }

    @Override
    public void updateOnBeforeSelection() {
        MatPresenter presenter = presenterList.get(mainTabLayout.getSelectedIndex());
        if (presenter != null) {
            MatContext.get().setAriaHidden(presenter.getWidget(), "false");
            presenter.beforeDisplay();
        }
    }

    @Override
    public void showUnsavedChangesError() {
        Integer selectedIndex = mainTabLayout.getSelectedIndex();
        MatPresenter presenter = presenterList.get(selectedIndex);
        if (presenter != null && presenter instanceof TabObserver) {
            MatPresenter targetPresenter = presenterList.get(mainTabLayout.getTargetSelection());
            if (targetPresenter != null) {
                if (presenter instanceof MeasureComposerPresenter) {
                    ((MeasureComposerPresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
                } else if (presenter instanceof CqlComposerPresenter) {
                    ((CqlComposerPresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
                } else if (presenter instanceof ManageMeasurePresenter) {
                    ((ManageMeasurePresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
                } else if (presenter instanceof CqlLibraryPresenter) {
                    ((CqlLibraryPresenter) presenter).setTabTargets(mainTabLayout, presenter, targetPresenter);
                }
            }
            ((TabObserver) presenter).showUnsavedChangesError();
        }
    }

    @Override
    public void notifyCurrentTabOfClosing() {
        Integer selectedIndex = mainTabLayout.getSelectedIndex();
        MatPresenter presenter = presenterList.get(selectedIndex);
        if (presenter instanceof TabObserver) {
            TabObserver tabObserver = (TabObserver) presenterList.get(selectedIndex);
            tabObserver.notifyCurrentTabOfClosing();
        }
        presenter.beforeClosingDisplay();
    }
}
