package mat.client;

import java.util.List;

import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;
import org.gwtbootstrap3.client.ui.constants.ProgressType;

import com.google.gwt.user.client.DOM;
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
import mat.client.shared.FocusableImageButton;
import mat.client.shared.FocusableWidget;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.VerticalFlowPanel;
import mat.client.util.ClientConstants;
import mat.client.util.FooterPanelBuilderUtility;

public abstract class MainLayout {
	
	private static Image alertImage = new Image(ImageResources.INSTANCE.alert());
	
	private static String alertTitle = ClientConstants.MAINLAYOUT_ALERT_TITLE;
	
	private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 500;
	
	private static Panel loadingPanel;
	
	private static HTML loadingWidget = new HTML(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);
	
	private static IndicatorButton showUMLSState;

	
	private static IndicatorButton showBonnieState;
	
	protected static FocusableWidget skipListHolder;

	static HTML welcomeUserLabel;
	
	static HTML versionLabel;
	
	static Progress progress = new Progress();
	static ProgressBar bar = new ProgressBar();
	
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
		bar.setText("Loaded 100 % ");
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
		bar.setText("Please wait. Loaded " +50+"% ");
		
		
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
	

	
	private FocusPanel content;
	
	private HorizontalFlowPanel logOutPanel;
	
	private HorizontalFlowPanel welcomeUserPanel;
	
	private FlowPanel versionPanel;
	

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
		
		FlowPanel footerMainPanel = new FlowPanel();
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
		topPanel.setStylePrimaryName("topBanner");
		HorizontalPanel horizontalBanner = new HorizontalPanel();
		horizontalBanner.getElement().setId("topBanner_HorizontalPanel");
		horizontalBanner.getElement().getStyle().setProperty("width", "100%");
		setId(horizontalBanner, "title");
		final FocusableImageButton titleImage= new FocusableImageButton(ImageResources.INSTANCE.g_header_title(),"Measure Authoring Tool");
		titleImage.getElement().setId("titleImage_FocusableImageButton");
		titleImage.setStylePrimaryName("topBannerImage");
		Mat.removeInputBoxFromFocusPanel(titleImage.getElement());
		HTML desc = new HTML("<h4 style=\"font-size:0;\"><b>Measure Authoring Tool</b></h4>");// Doing this for 508 when CSS turned off
		@SuppressWarnings("deprecation")
		com.google.gwt.user.client.Element heading = desc.getElement();
		DOM.insertChild(titleImage.getElement(), heading, 0);
		versionPanel = new VerticalFlowPanel();
		versionPanel.getElement().setId("versionPanel_VerticalalFlowPanel");
		versionPanel.setStyleName("versionPanel");
		welcomeUserPanel = new HorizontalFlowPanel();
		welcomeUserPanel.getElement().setId("welcomeUserPanel_HorizontalFlowPanel");
		welcomeUserPanel.setStyleName("welcomeUserPanel");
		VerticalPanel titleVerticalPanel = new VerticalPanel();
		titleVerticalPanel.addStyleName("versionPanel");
		titleVerticalPanel.add(titleImage);
		titleVerticalPanel.add(versionPanel);
		titleVerticalPanel.add(welcomeUserPanel);
		horizontalBanner.add(titleVerticalPanel);
		logOutPanel = new HorizontalFlowPanel();
		logOutPanel.getElement().setId("logOutPanel_HorizontalFlowPanel");
		logOutPanel.addStyleName("logoutPanel");

		showBonnieState = new IndicatorButton("Bonnie Active", "Sign into Bonnie");
		showUMLSState = new IndicatorButton("UMLS Active", "Sign into UMLS");
		
		VerticalPanel vp = new VerticalPanel();
		vp.add(logOutPanel);
		vp.add(showUMLSState.getPanel());
		vp.add(showBonnieState.getPanel());

		vp.addStyleName("logoutAndUMLSPanel");
		
		horizontalBanner.add(vp);
		topPanel.add(horizontalBanner);
		topPanel.add(buildLoadingPanel());
		return topPanel;

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

	protected HorizontalFlowPanel getLogoutPanel(){
		return logOutPanel;
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
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}

	
	protected void setLogout(final HorizontalFlowPanel logOutPanel){
		this.logOutPanel = logOutPanel;
	}
	

	public HorizontalFlowPanel getWelcomeUserPanel(String userFirstName) {
		welcomeUserLabel = new HTML("<h9><b>Successful login - Welcome, "+ userFirstName+"!</b></h9>");
		welcomeUserLabel.getElement().setId("welcomeUserLabel_HTML");
		welcomeUserLabel.getElement().setAttribute("tabIndex", "0");
		welcomeUserLabel.setStylePrimaryName("htmlDescription");
		
		welcomeUserPanel.add(welcomeUserLabel);
		return welcomeUserPanel;
	}
	

	public FlowPanel getVersionPanel(String resultMatVersion) {
		//Since mat-bootstrap CSS always overrides Mat css settings hardcoded inline style for version. In future need to change this.
		String versionStyle = "font-family: Arial;font-size:small;font-weight: bold;line-height:1.25;margin-top: 15px;";
		versionLabel = new HTML("<h4 style="+'"'+versionStyle+'"'+">Version "+ resultMatVersion.replaceAll("[a-zA-Z]", "")+"</h4>");
		versionLabel.getElement().setId("version_HTML");
		versionLabel.getElement().setAttribute("tabIndex", "0");
		versionLabel.setStylePrimaryName("versionPanel");
		versionPanel.add(versionLabel);
		return versionPanel;
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
		return showUMLSState.getLink();
	}
	
	public HTML getBonnieButton() {
		return showBonnieState.getLink();
	}

	public void setIndicatorsHidden() {
		showBonnieState.hideActive(true);
		showUMLSState.hideActive(true);
	}
	
	//method to easily remove bonnie link from page
	public void removeBonnieLink() {
		showBonnieState.getPanel().removeFromParent();
	}
}
