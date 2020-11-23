package mat.client;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.buttons.IndicatorButton;
import mat.client.event.SwitchUserEvent;
import mat.client.login.service.ShortUserInfo;
import mat.client.shared.FocusableWidget;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.util.ClientConstants;
import mat.client.util.FeatureFlagConstant;
import mat.client.util.FooterPanelBuilderUtility;
import mat.model.clause.ModelTypeHelper;
import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.ListDropDown;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarLink;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class MainLayout {

    private static final Logger logger = Logger.getLogger(MainLayout.class.getSimpleName());
    private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 500;
    private static final int SPINNER_DIALOG_DELAY_MILLIS = 2000;
    private static final String HEADING = "Measure Authoring Tool";

    private static final HTML SIMPLE_SPINNER = new HTML("<div class=\"spinner-loading spinner-loading-shadow\">" + ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG + "</div>");
    private static final String ORG_ROLE_SEP = " @ ";
    private static final String SWITCH_MAT_ACCOUNT = "Switch MAT account";
    private static final int MAX_MENU_TITLE = 50;
    private static ListItem signedInAsName = new ListItem();
    private static ListItem signedInAsOrg = new ListItem();
    private static IndicatorButton showUMLSState;
    private static IndicatorButton showBonnieState;
    private static FocusableWidget skipListHolder;

    private NavbarLink homeLink = new NavbarLink();
    private FocusPanel content;
    private HorizontalPanel linksPanel = new HorizontalPanel();
    private AnchorListItem profile = new AnchorListItem("MAT Account");
    private AnchorListItem becomeTopLevelUser = new AnchorListItem("Become a Top User");
    private AnchorListItem becomeUser = new AnchorListItem("Become a User");
    private AnchorListItem signOut = new AnchorListItem("Sign Out");
    private FormPanel logoutForm = new FormPanel("logout");

    {
        logoutForm.getElement().setAttribute("tab-index", "-1");
    }

    /**
     * hide spinner and
     * reset the loading queue.
     */
    private static void delegateHideLoadingMessage() {
        MatContext.get().getLoadingQueue().clear();
        if (MatContext.get().getLoadingQueue().size() == 0) {
            hideProgressSpinner();
        }
        setFocusToSkipToMainContent();
    }

    private static native void setFocusToSkipToMainContent() /*-{
        $doc.getElementById("menu").focus();
    }-*/;



    protected static FocusableWidget getSkipList() {
        return skipListHolder;
    }

    /**
     * no arg method adds default delay to loading message hide op.
     */
    public static void hideLoadingMessage() {
        hideLoadingMessage(DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS);
    }


    /**
     * delay hiding of loading message artifacts by 'delay' milliseconds
     * NOTE delay cannot be <= 0 else exception is thrown
     * public method to allow setting of delay.
     *
     * @param delay the delay
     */
    public static void hideLoadingMessage(final int delay) {
        if (delay > 0) {
            final Timer timer = new Timer() {
                @Override
                public void run() {
                    delegateHideLoadingMessage();
                }
            };
            timer.schedule(delay);
        } else {
            delegateHideLoadingMessage();
        }
    }

    public static void showLoadingMessage() {
        showLoadingMessage(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);
    }

    public static void showLoadingMessage(String title) {
        MatContext.get().getLoadingQueue().add("node");
        showProgressSpinner(title);
    }


    public static void showSignOutMessage() {
        showLoadingMessage(ClientConstants.MAINLAYOUT_SIGNOUT_WIDGET_MSG);
    }

    private static void showProgressSpinner(String title) {
        SIMPLE_SPINNER.setVisible(true);
        final Timer timer = new Timer() {
            @Override
            public void run() {
                if (MatContext.get().getLoadingQueue().size() != 0) {
                    SpinnerModal.showSpinnerWithTitle(title);
                    SIMPLE_SPINNER.setVisible(false);
                }
            }
        };
        timer.schedule(SPINNER_DIALOG_DELAY_MILLIS);
    }


    private static void hideProgressSpinner() {
        SpinnerModal.hideSpinner();
        SIMPLE_SPINNER.setVisible(false);
    }

    private Panel buildContentPanel() {
        content = new FocusPanel();
        content.getElement().setAttribute("id", "MainLayout.content");
        content.getElement().setAttribute("aria-role", "main");
        content.getElement().setAttribute("aria-labelledby", "LiveRegion");
        content.getElement().setAttribute("aria-live", "assertive");
        content.getElement().setAttribute("aria-atomic", "true");
        content.getElement().setAttribute("aria-relevant", "all");
        content.setStylePrimaryName("mainContentPanel");
        setId(content, "content");
        Mat.removeInputBoxFromFocusPanel(content.getElement());
        return content;
    }

    /**
     * Builds the Footer Panel for the Login and Mat View. Currently, it displays the
     * 'Accessibility Policy' , 'Terms Of Use' , 'Privacy Policy' 'User Guide' links with CMS LOGO.
     *
     * @return Panel
     */
    private Panel buildFooterPanel() {
        final FlowPanel footerMainPanel = new FlowPanel();
        footerMainPanel.getElement().setId("footerMainPanel_FlowPanel");
        footerMainPanel.setStylePrimaryName("footer");
        footerMainPanel.add(FooterPanelBuilderUtility.buildFooterLogoPanel());
        footerMainPanel.add(fetchAndcreateFooterLinks());
        return footerMainPanel;
    }

    private Panel buildSkipContent() {
        skipListHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Main Content"));
        Mat.removeInputBoxFromFocusPanel(skipListHolder.getElement());
        return skipListHolder;
    }

    private Panel buildTopPanel() {
        final VerticalPanel topPanel = new VerticalPanel();
        topPanel.add(buildHeader());
        topPanel.setStylePrimaryName("topBanner");
        return topPanel;
    }

    private HorizontalFlowPanel buildHeader() {
        final HorizontalFlowPanel hfp = new HorizontalFlowPanel();
        hfp.add(buildNavbar());
        hfp.add(linksPanel);
        return hfp;
    }

    private Navbar buildNavbar() {
        final Navbar nav = new Navbar();
        nav.setStyleName("versionBanner");
        setLinkTextAndTitle(HEADING, getHomeLink());
        nav.add(getHomeLink());
        return nav;
    }

    public void buildLinksPanel() {
        if (!Mat.harpUserVerificationInProgress) {
            showBonnieState = new IndicatorButton("Disconnect from Bonnie", "Sign in to Bonnie");
            showUMLSState = new IndicatorButton("UMLS Active", "Sign in to UMLS");

            linksPanel.add(showUMLSState.getPanel());
            linksPanel.add(showBonnieState.getPanel());
        }
        linksPanel.add(buildProfileMenu());
        linksPanel.setStyleName("navLinksBanner", true);
    }

    private AnchorButton buildProfileIcon() {
        AnchorButton ab = new AnchorButton();

        ab.setIcon(IconType.USER_CIRCLE_O);
        ab.setIconSize(IconSize.TIMES2);
        ab.setIconPosition(IconPosition.RIGHT);
        ab.setDataToggle(Toggle.DROPDOWN);
        ab.setToggleCaret(false);
        ab.setStyleName(Styles.DROPDOWN_TOGGLE);
        ab.setActive(true);
        ab.setTitle("Profile");
        ab.setId("userprofile");
        ab.setDataTarget(Styles.NAVBAR_COLLAPSE);

        SafeHtmlBuilder sb = new SafeHtmlBuilder();
        sb.appendHtmlConstant("<span style=\"font-size:0px;\" tabindex=\"-1\">Profile</span>");
        ab.getElement().setInnerSafeHtml(sb.toSafeHtml());

        return ab;
    }

    private Navbar buildDivider() {
        Navbar divider = new Navbar();
        divider.setStyleName(Styles.DIVIDER);
        divider.setWidth("100%");
        return divider;
    }

    private ListItem buildSignedInAs() {
        ListItem li = new ListItem();
        li.setText("Signed in as");
        li.setTitle("Signed in as");
        li.getElement().setTabIndex(-1);
        li.setStyleName("profileText", true);
        return li;
    }

    public static void setSignedInAsNameOrg() {
        String name = MatContext.get().getLoggedInUserFirstName() + " " + MatContext.get().getLoggedInUserLastName();
        signedInAsName.setText(name);
        signedInAsName.setTitle(name);
        signedInAsName.setStyleName("labelStyling", true);
        signedInAsName.setStyleName("profileText", true);
        signedInAsName.getElement().setTabIndex(-1);
        String orgRole = MatContext.get().getLoggedInUserRole() + ORG_ROLE_SEP + MatContext.get().getCurrentUserInfo().organizationName;
        signedInAsOrg.setText(trimTitleWithEllipses(orgRole));
        signedInAsOrg.setTitle(orgRole);
        signedInAsOrg.setStyleName("profileText", true);
        signedInAsOrg.getElement().setTabIndex(-1);
    }

    private void setAccessibilityForLinks() {
        if (!Mat.harpUserVerificationInProgress) {
            profile.setStyleName(Styles.DROPDOWN);
            profile.getWidget(0).setTitle("MAT Account");
        }
        signOut.setStyleName(Styles.DROPDOWN);
        signOut.getWidget(0).setTitle("Sign Out");
    }

    private DropDownMenu buildDropDownMenu() {
        DropDownMenu ddm = new DropDownMenu();

        setAccessibilityForLinks();

        ddm.add(buildSignedInAs());
        ddm.add(signedInAsName);
        ddm.add(signedInAsOrg);
        addUserAccountsMenu(ddm);
        ddm.add(buildDivider());
        if (!Mat.harpUserVerificationInProgress) {
            ddm.add(profile);
        }
        if (ClientConstants.ADMINISTRATOR.equals(MatContext.get().getLoggedInUserRole())) {
            ddm.add(becomeUser);
            ddm.add(becomeTopLevelUser);
        }
        ddm.add(signOut);
        ddm.setStyleName(Styles.DROPDOWN_MENU);
        ddm.addStyleDependentName(Styles.RIGHT);

        return ddm;
    }

    private void addUserAccountsMenu(DropDownMenu ddm) {
        List<ShortUserInfo> users = MatContext.get().getCurrentUserInfo().users;
        if (users.size() <= 1) {
            return;
        }
        ddm.add(buildDivider());

        ListItem switchUser = new ListItem();
        switchUser.setText(SWITCH_MAT_ACCOUNT);
        switchUser.setTitle(SWITCH_MAT_ACCOUNT);
        switchUser.getElement().setTabIndex(0);
        switchUser.addStyleName("labelStyling");
        switchUser.addStyleName("profileText");
        ddm.add(switchUser);

        for (ShortUserInfo user : users) {
            boolean selected = MatContext.get().getLoggedinUserId().equals(user.userId);
            AnchorListItem accMenuItem = new AnchorListItem();
            String name = user.role + ORG_ROLE_SEP + user.organizationName;
            accMenuItem.setText(trimTitleWithEllipses(name));
            accMenuItem.setTitle(name);
            accMenuItem.getElement().setTabIndex(0);
            accMenuItem.setStyleName(Styles.DROPDOWN);
            if (selected) {
                accMenuItem.addStyleName("labelStyling");
                accMenuItem.addStyleName("menu-select-user-account-selected");
            } else {
                accMenuItem.addStyleName("menu-select-user-account");
                accMenuItem.addClickHandler(event -> {
                    MatContext.get().getEventBus().fireEvent(new SwitchUserEvent(user.userId));
                });
            }
            ddm.add(accMenuItem);
        }
    }

    private static String trimTitleWithEllipses(String text) {
        return text.length() <= MAX_MENU_TITLE ? text : text.substring(0, MAX_MENU_TITLE) + "...";
    }

    private NavbarCollapse buildProfileMenu() {
        NavbarCollapse collapse = new NavbarCollapse();
        NavbarNav nav = new NavbarNav();

        ListDropDown ldd = new ListDropDown();
        AnchorButton icon = buildProfileIcon();
        DropDownMenu ddm = buildDropDownMenu();

        ldd.add(icon);
        ldd.add(ddm);

        nav.add(ldd);

        collapse.add(nav);
        return collapse;
    }

    /**
     * Call Okta logout operation to log a user out by removing their Okta browser session.
     * Note: When making requests to the /logout endpoint, the browser (user agent)
     * should be redirected to the endpoint. You can't use AJAX with this endpoint.
     * <p>
     * This operation performs a redirect to the post_logout_redirect_uri.
     *
     * @param harpUrl
     */
    protected void harpLogout(String harpUrl) {
        logoutForm.setMethod(FormPanel.METHOD_GET);

        VerticalPanel panel = new VerticalPanel();
        logoutForm.setWidget(panel);

        Hidden token = new Hidden();
        token.setName("id_token_hint");
        token.setValue(MatContext.get().getIdToken());

        Hidden redirect = new Hidden();
        redirect.setName("post_logout_redirect_uri");
        String path = Window.Location.getPath();
        String redirectUrl = Window.Location.createUrlBuilder()
                .setPath(path.substring(0, path.lastIndexOf('/')) + ClientConstants.HTML_LOGIN)
                .buildString();
        if (redirectUrl.contains("#")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.lastIndexOf('#'));
        }
        redirect.setValue(redirectUrl);

        panel.add(token);
        panel.add(redirect);

        RootPanel.get().add(logoutForm);

        logoutForm.setAction(harpUrl + "/logout");
        logoutForm.submit();
    }

    public void setHeader(String version, NavbarLink link) {
        String headerText = HEADING + " v" + version;
        String headerTitle = HEADING + " version " + version;
        if (MatContext.get().getFeatureFlagStatus(FeatureFlagConstant.MAT_ON_FHIR)) {
            headerText += " (" + ModelTypeHelper.FHIR + ")";
            headerTitle += " (" + ModelTypeHelper.FHIR + ")";
        }
        setLinkTextAndTitle(headerText, link);
        link.setTitle(headerTitle);
        link.getElement().setAttribute("role", "alert");
        link.getElement().setAttribute("aria-label", "Clicking this link will navigate you to Measure Library page.");
    }

    private void setLinkTextAndTitle(String text, NavbarLink link) {
        link.setText(text);
        link.setTitle(text);
        link.setTabIndex(0);
        link.setId("Anchor_" + text);
        link.setColor("#337ab7");
    }

    private HTML fetchAndcreateFooterLinks() {
        MatContext.get().getLoginService().getFooterURLs(new AsyncCallback<List<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                logger.log(Level.SEVERE, "LoginService::getFooterURLs -> onFailure: " + caught.getMessage(), caught);
            }

            @Override
            public void onSuccess(List<String> result) {
                logger.log(Level.INFO, "LoginService::getFooterURLs -> onSuccess");
                //Set the Footer URL's on the ClientConstants for use by the app in various locations.
                ClientConstants.ACCESSIBILITY_POLICY_URL = result.get(0);
                ClientConstants.PRIVACYPOLICY_URL = result.get(1);
                ClientConstants.TERMSOFUSE_URL = result.get(2);
                ClientConstants.USERGUIDE_URL = result.get(3);
            }

        });
        return FooterPanelBuilderUtility.buildFooterLinksPanel();
    }

    protected FocusPanel getContentPanel() {
        return content;
    }

    protected abstract void initEntryPoint();

    public final void onModuleLoad() {

        final Panel skipContent = buildSkipContent();

        final Panel topBanner = buildTopPanel();
        final Panel footerPanel = buildFooterPanel();
        final Panel contentPanel = buildContentPanel();

        final FlowPanel container = new FlowPanel();

        SIMPLE_SPINNER.setVisible(false);
        SIMPLE_SPINNER.getElement().setAttribute("id", "loadingSimpleSpinner");
        container.add(SIMPLE_SPINNER);
        container.add(topBanner);
        container.add(contentPanel);
        container.add(footerPanel);

        RootPanel.get().clear();
        if (RootPanel.get("skipContent") != null) {
            RootPanel.get("skipContent").add(skipContent);
        }
        RootPanel.get("mainContent").add(container);
        initEntryPoint();
    }

    protected void setId(final Widget widget, final String id) {
        widget.getElement().setAttribute("id", id);
    }


    public HorizontalPanel getLinksPanel() {
        return linksPanel;
    }

    public void setLinksPanel(HorizontalPanel linksPanel) {
        this.linksPanel = linksPanel;
    }

    public static void createUMLSLinks() {
        showUMLSState.createAllLinks();
    }

    public static void hideUMLSActive(boolean hide) {
        showUMLSState.hideActive(hide);
    }

    public static void createBonnieLinks() {
        showBonnieState.createAllLinks();
    }

    public static void hideBonnieActive(boolean hide) {
        showBonnieState.hideActive(hide);
    }

    public HTML getUMLSButton() {
        return showUMLSState.getHideLink();
    }

    public HTML getBonnieSignInButton() {
        return showBonnieState.getHideLink();
    }

    public HTML getBonnieDisconnectButton() {
        return showBonnieState.getshowLink();
    }

    public void setIndicatorsHidden() {
        showBonnieState.hideActive(true);
        showUMLSState.hideActive(true);
    }

    // method to easily remove bonnie link from page
    public void removeBonnieLink() {
        showBonnieState.getPanel().removeFromParent();
    }

    public NavbarLink getHomeLink() {
        return homeLink;
    }

    public void setHomeLink(NavbarLink homeLink) {
        this.homeLink = homeLink;
    }

    public ListItem getSignedInAsName() {
        return signedInAsName;
    }

    public void setSignedInAsName(ListItem signedInAsName) {
        this.signedInAsName = signedInAsName;
    }

    public AnchorListItem getProfile() {
        return profile;
    }

    public void setProfile(AnchorListItem profile) {
        this.profile = profile;
    }

    public AnchorListItem getSignOut() {
        return signOut;
    }

    public AnchorListItem getBecomeTopLevelUser() {
        return becomeTopLevelUser;
    }

    public AnchorListItem getBecomeUser() {
        return becomeUser;
    }

    public void setSignOut(AnchorListItem signOut) {
        this.signOut = signOut;
    }

}
