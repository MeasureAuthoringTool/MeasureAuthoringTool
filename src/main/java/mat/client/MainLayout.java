package mat.client;

import java.util.List;

import org.gwtbootstrap3.client.ui.AnchorButton;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.DropDownMenu;
import org.gwtbootstrap3.client.ui.ListDropDown;
import org.gwtbootstrap3.client.ui.ListItem;
import org.gwtbootstrap3.client.ui.Navbar;
import org.gwtbootstrap3.client.ui.NavbarCollapse;
import org.gwtbootstrap3.client.ui.NavbarLink;
import org.gwtbootstrap3.client.ui.NavbarNav;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;
import org.gwtbootstrap3.client.ui.constants.ProgressType;
import org.gwtbootstrap3.client.ui.constants.Styles;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.buttons.IndicatorButton;
import mat.client.shared.FocusableWidget;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.util.ClientConstants;
import mat.client.util.FooterPanelBuilderUtility;

public abstract class MainLayout {
	
	private static Image alertImage = new Image(ImageResources.INSTANCE.alert());
	
	private static String alertTitle = ClientConstants.MAINLAYOUT_ALERT_TITLE;
	
	private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 500;
	
	private static Panel loadingPanel;

	private FocusPanel content;
	
	private HorizontalPanel linksPanel = new HorizontalPanel();

	private static HTML loadingWidget = new HTML(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);
	
	private static IndicatorButton showUMLSState;
	private static IndicatorButton showBonnieState;
	
	protected static FocusableWidget skipListHolder;

	static Progress progress = new Progress();
	static ProgressBar bar = new ProgressBar();
	
	NavbarLink homeLink = new NavbarLink();
	
	static ListItem signedInAsName = new ListItem();
	AnchorListItem profile = new AnchorListItem("MAT Account");
	AnchorListItem signOut = new AnchorListItem("Sign Out");
	
	public static final String HEADING = "Measure Authoring Tool";
	
	/**
	 * clear the loading panel
	 * remove css style
	 * reset the loading queue.
	 */
	private static void delegateHideLoadingMessage(){
		MatContext.get().getLoadingQueue().poll();
		if(MatContext.get().getLoadingQueue().size() == 0){
			getLoadingPanel().clear();
			getLoadingPanel().getElement().removeAttribute("role");
		}
	}
	
	protected static Panel getLoadingPanel(){
		return loadingPanel;
	}

	protected static FocusableWidget getSkipList(){
		return skipListHolder;
	}
	
	/**
	 * no arg method adds default delay to loading message hide op.
	 */
	public static void hideLoadingMessage(){
		bar.setPercent(100.00);
		bar.setText("Loaded 100% ");
		hideLoadingMessage(DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS);
	}
	
	
	/**
	 * delay hiding of loading message artifacts by 'delay' milliseconds
	 * NOTE delay cannot be <= 0 else exception is thrown
	 * public method to allow setting of delay.
	 *
	 * @param delay the delay
	 */
	public static void hideLoadingMessage(final int delay){
		if(delay > 0){
			final Timer timer = new Timer() {
				@Override
				public void run() {
					delegateHideLoadingMessage();
				}
			};
			timer.schedule(delay);
		}
		else{
			delegateHideLoadingMessage();
		}
	}
	
	public static void showLoadingMessage(){
		getLoadingPanel().clear();
		
		progress.setActive(true);
		progress.setType(ProgressType.STRIPED);
		
		bar.setType(ProgressBarType.INFO);
		bar.setWidth("100%");
		bar.setPercent(50.00);
		bar.setText("Please wait. Loaded 50%");
		
		
		progress.add(bar);
		progress.setId("LoadingPanel");
		getLoadingPanel().add(progress);

		getLoadingPanel().setWidth("99%");
		getLoadingPanel().getElement().setAttribute("role", "alert");
		MatContext.get().getLoadingQueue().add("node");
	}

	
	public static void showSignOutMessage(){
		loadingWidget = new HTML(ClientConstants.MAINLAYOUT_SIGNOUT_WIDGET_MSG);
		showLoadingMessage();
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
	

	private Panel buildLoadingPanel() {
		loadingPanel = new HorizontalPanel();
		loadingPanel.setHeight("30px");
		loadingPanel.getElement().setAttribute("id", "loadingContainer");
		loadingPanel.getElement().setAttribute("aria-role", "loadingwidget");
		loadingPanel.getElement().setAttribute("aria-labelledby", "LiveRegion");
		loadingPanel.getElement().setAttribute("aria-live", "assertive");
		loadingPanel.getElement().setAttribute("aria-atomic", "true");
		loadingPanel.getElement().setAttribute("aria-relevant", "all");
		
		loadingPanel.setStylePrimaryName("mainContentPanel");
		setId(loadingPanel, "loadingContainer");
		alertImage.setTitle(alertTitle);
		alertImage.getElement().setAttribute("alt", alertTitle);
		loadingWidget.setStyleName("padLeft5px");
		return loadingPanel;
	}
	
	private Panel buildSkipContent() {
		skipListHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Main Content"));
		Mat.removeInputBoxFromFocusPanel(skipListHolder.getElement());
		return skipListHolder;
	}
	
	private Panel buildTopPanel() {
		final VerticalPanel topPanel = new VerticalPanel();
		topPanel.add(buildHeader());
		topPanel.add(buildLoadingPanel());
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
		showBonnieState = new IndicatorButton("Disconnect from Bonnie", "Sign in to Bonnie");
		showUMLSState = new IndicatorButton("UMLS Active", "Sign in to UMLS");

		linksPanel.add(showUMLSState.getPanel()); 
		linksPanel.add(showBonnieState.getPanel());
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
		sb.appendHtmlConstant("<span style=\"font-size:0px;\" tabindex=\"0\">Profile</span>"); 
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
		li.getElement().setTabIndex(0);
		li.setStyleName("profileText", true);
		return li;
	}

	public static void setSignedInName(String name) {
		signedInAsName.setText(name);
		signedInAsName.setTitle(name);
		signedInAsName.setStyleName("labelStyling", true);
		signedInAsName.setStyleName("profileText", true);
		signedInAsName.getElement().setTabIndex(0);
	}
	
	private void setAccessibilityForLinks() {
		profile.setStyleName(Styles.DROPDOWN );
		profile.getWidget(0).setTitle("MAT Account");

		signOut.setStyleName(Styles.DROPDOWN);
		signOut.getWidget(0).setTitle("Sign Out");
	}
	
	private DropDownMenu buildDropDownMenu() {
		DropDownMenu ddm = new DropDownMenu();
	
		setAccessibilityForLinks();
		
		ddm.add(buildSignedInAs());
		ddm.add(signedInAsName);
		ddm.add(buildDivider());
		ddm.add(profile);
		ddm.add(signOut);
		ddm.setStyleName(Styles.DROPDOWN_MENU);
		ddm.addStyleDependentName(Styles.RIGHT);
		
		return ddm;
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
	
	public void setHeader(String version, NavbarLink link) {
		setLinkTextAndTitle(HEADING + " v" + version, link);
		link.setTitle(HEADING + " version " + version);
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
			}
			
			@Override
			public void onSuccess(List<String> result) {
				//Set the Footer URL's on the ClientConstants for use by the app in various locations.
				ClientConstants.ACCESSIBILITY_POLICY_URL = result.get(0);
				ClientConstants.PRIVACYPOLICY_URL = result.get(1);
				ClientConstants.TERMSOFUSE_URL = result.get(2);
				ClientConstants.USERGUIDE_URL = result.get(3);
			}
			
		});
		return FooterPanelBuilderUtility.buildFooterLinksPanel();
	}

	protected  FocusPanel getContentPanel() {
		return content;
	}

	protected Widget getNavigationList(){
		return null;
	}

	protected abstract void initEntryPoint();

	public final void onModuleLoad() {
		
		final Panel skipContent = buildSkipContent();
		
		final Panel topBanner = buildTopPanel();
		final Panel footerPanel = buildFooterPanel();
		final Panel contentPanel = buildContentPanel();
		
		final FlowPanel container = new FlowPanel();
		container.add(topBanner);
		container.add(contentPanel);
		container.add(footerPanel);
		
		RootPanel.get().clear();
		if(RootPanel.get("skipContent")!= null){
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
	
	//method to easily remove bonnie link from page
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

	public void setSignOut(AnchorListItem signOut) {
		this.signOut = signOut;
	}

}
