package mat.client;

import java.util.List;

import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;
import org.gwtbootstrap3.client.ui.constants.ProgressType;

import mat.client.shared.FocusableImageButton;
import mat.client.shared.FocusableWidget;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.shared.VerticalFlowPanel;
import mat.client.util.ClientConstants;
import mat.client.util.FooterPanelBuilderUtility;
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

// TODO: Auto-generated Javadoc
/**
 * The Class MainLayout.
 */
public abstract class MainLayout {
	
	/** The active umls image. */
	private static Image activeUmlsImage;
	
	/** The alert image. */
	private static Image alertImage = new Image(ImageResources.INSTANCE.alert());
	
	/** The alert title. */
	private static String alertTitle = ClientConstants.MAINLAYOUT_ALERT_TITLE;
	
	/** The Constant DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS. */
	private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 500;
	
	/** The in active umls image. */
	private static  Image inActiveUmlsImage;
	
	/** The loading panel. */
	private static Panel loadingPanel;
	
	/** The loading widget. */
	private static HTML loadingWidget = new HTML(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);
	
	/** The show umls state. */
	private static Panel showUMLSState;
	
	/** The skip list holder. */
	protected static FocusableWidget skipListHolder;
	
	/** The umls active status label. */
	static HTML umlsActiveStatusLabel;
	
	/** The umls inactive status label. */
	static HTML umlsInactiveStatusLabel;
	
	/** The welcome user label. */
	static HTML welcomeUserLabel;
	
	/** The version label. */
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
			/*getLoadingPanel().remove(alertImage);
			getLoadingPanel().remove(loadingWidget);
			getLoadingPanel().removeStyleName("msg-alert");*/
			getLoadingPanel().getElement().removeAttribute("role");
		}
	}
	
	/**
	 * Gets the loading panel.
	 *
	 * @return the loading panel
	 */
	protected static Panel getLoadingPanel(){
		return loadingPanel;
	}
	
	/**
	 * Gets the show umls state.
	 *
	 * @return the show umls state
	 */
	protected static Panel getShowUMLSState(){
		return showUMLSState;
	}
	
	/**
	 * Gets the skip list.
	 *
	 * @return the skip list
	 */
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
	
	/**
	 * Hide umls active.
	 */
	public static void hideUMLSActive(){
		getShowUMLSState().clear();
		getShowUMLSState().remove(activeUmlsImage);
		getShowUMLSState().remove(umlsActiveStatusLabel);
		
		getShowUMLSState().add(inActiveUmlsImage);
		getShowUMLSState().add(umlsInactiveStatusLabel);
		getShowUMLSState().getElement().setAttribute("role", "alert");
	}
	
	/**
	 * Show loading message.
	 */
	public static void showLoadingMessage(){
		getLoadingPanel().clear();
		/*alertImage.getElement().setId("LoadingImage");
		getLoadingPanel().add(alertImage);
		loadingWidget.getElement().setAttribute("id", "LoadingPanel");
		getLoadingPanel().add(loadingWidget);
		getLoadingPanel().setStyleName("msg-alert");
		getLoadingPanel().getElement().setAttribute("role", "alert");*/
		
		progress.setActive(true);
		progress.setType(ProgressType.STRIPED);
		
	//	bar.setText("Loading Please Wait...");
		bar.setType(ProgressBarType.INFO);
		bar.setWidth("100%");
		bar.setPercent(50.00);
		bar.setText("Please wait.Loaded " +50+"% ");
		
		
		
		
		/*bar.getElement().setAttribute("role", "progressbar");
		bar.getElement().setAttribute("aria-valuenow", "90");
		bar.getElement().setAttribute("aria-valuemin", "0");
		bar.getElement().setAttribute("aria-valuemax", "100");
		bar.getElement().setAttribute("aria-valuetext", "Loading Please wait");*/
		progress.add(bar);
		progress.setId("LoadingPanel");
		getLoadingPanel().add(progress);
		/*int delay = 50;
		
		if(delay > 0){
			
			final Timer timer = new Timer() {
				@Override
				public void run() {
					 double percentage = bar.getPercent();
					 percentage+= 10.00;
					 bar.setPercent(percentage);
					 bar.setText("Please wait.Loaded " +(int)percentage+"%.");
					 if(percentage == 100) {
						 percentage =50.00;
					 }
				}
			};
			timer.schedule(delay);
		}*/
		//getLoadingPanel().setStyleName("msg-alert");
		getLoadingPanel().setWidth("99%");
		getLoadingPanel().getElement().setAttribute("role", "alert");
		MatContext.get().getLoadingQueue().add("node");
	}
	
	/**
	 * Show sign out message.
	 */
	public static void showSignOutMessage(){
		loadingWidget = new HTML(ClientConstants.MAINLAYOUT_SIGNOUT_WIDGET_MSG);
		showLoadingMessage();
	}
	
	/**
	 * Show umls active.
	 */
	public static void showUMLSActive(){
		getShowUMLSState().clear();
		if(getShowUMLSState().getElement().hasChildNodes()){
			getShowUMLSState().remove(inActiveUmlsImage);
			getShowUMLSState().remove(umlsInactiveStatusLabel);
		}
		
		getShowUMLSState().add(activeUmlsImage);
		getShowUMLSState().add(umlsActiveStatusLabel);
		getShowUMLSState().getElement().setAttribute("role", "alert");
	}
	
	/** The content. */
	private FocusPanel content;
	
	
	/** The log out panel. */
	private HorizontalFlowPanel logOutPanel;
	
	/** The welcome user panel. */
	private HorizontalFlowPanel welcomeUserPanel;
	
	/** The version panel. */
	private FlowPanel versionPanel;
	
	/**
	 * Builds the content panel.
	 *
	 * @return the panel
	 */
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
	
	/**
	 * Builds the loading panel.
	 *
	 * @return the panel
	 */
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
	
	/**
	 * Builds the skip content.
	 *
	 * @return the panel
	 */
	private Panel buildSkipContent() {
		skipListHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Main Content"));
		Mat.removeInputBoxFromFocusPanel(skipListHolder.getElement());
		return skipListHolder;
	}
	
	/**
	 * Builds the top panel.
	 *
	 * @return the panel
	 */
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
		HTML desc = new HTML("<h4 style=\"font-size:0;\">Measure Authoring Tool</h4>");// Doing this for 508 when CSS turned off
		com.google.gwt.user.client.Element heading = desc.getElement();
		DOM.insertChild(titleImage.getElement(), heading, 0);
		versionPanel = new VerticalFlowPanel();
		versionPanel.getElement().setId("versionPanel_VerticalalFlowPanel");
		versionPanel.setStyleName("versionPanel");
		VerticalPanel titleVerticalPanel = new VerticalPanel();
		titleVerticalPanel.addStyleName("versionPanel");
		titleVerticalPanel.add(titleImage);
		titleVerticalPanel.add(versionPanel);
		horizontalBanner.add(titleVerticalPanel);
		logOutPanel = new HorizontalFlowPanel();
		logOutPanel.getElement().setId("logOutPanel_HorizontalFlowPanel");
		logOutPanel.addStyleName("logoutPanel");
		welcomeUserPanel = new HorizontalFlowPanel();
		welcomeUserPanel.getElement().setId("welcomeUserPanel_HorizontalFlowPanel");
		welcomeUserPanel.setStyleName("welcomeUserPanel");
		showUMLSState = buildUMLStatePanel();
		showUMLSState.addStyleName("umlsStatePanel");
		VerticalPanel vp = new VerticalPanel();
		vp.add(logOutPanel);
		vp.add(showUMLSState);
		vp.add(welcomeUserPanel);
		vp.add(new SpacerWidget());
		vp.addStyleName("logoutAndUMLSPanel");
		horizontalBanner.add(vp);

		topPanel.add(horizontalBanner);
		topPanel.add(buildLoadingPanel());
		return topPanel;

	}
	
	/**
	 * Builds the uml state panel.
	 *
	 * @return the panel
	 */
	private Panel buildUMLStatePanel() {
		showUMLSState = new HorizontalFlowPanel();
		showUMLSState.getElement().setAttribute("id", "showUMLSStateContainer");
		showUMLSState.getElement().setAttribute("aria-role", "application");
		showUMLSState.getElement().setAttribute("aria-labelledby", "LiveRegion");
		showUMLSState.getElement().setAttribute("aria-live", "assertive");
		showUMLSState.getElement().setAttribute("aria-atomic", "true");
		showUMLSState.getElement().setAttribute("aria-relevant", "all");
		activeUmlsImage = new Image(ImageResources.INSTANCE.bullet_green());
		activeUmlsImage.setStylePrimaryName("imageMiddleAlign");
		umlsActiveStatusLabel = new HTML("<h9>UMLS Active</h9>");
		umlsActiveStatusLabel.getElement().setAttribute("tabIndex", "0");
		umlsActiveStatusLabel.setStylePrimaryName("htmlDescription");
		inActiveUmlsImage = new Image(ImageResources.INSTANCE.bullet_red());
		inActiveUmlsImage.setStylePrimaryName("imageMiddleAlign");
		umlsInactiveStatusLabel = new HTML("<h9>UMLS Inactive</h9>");
		umlsInactiveStatusLabel.getElement().setAttribute("tabIndex", "0");
		umlsInactiveStatusLabel.setStylePrimaryName("htmlDescription");
		return showUMLSState;
	}
	/**
	 * Fetch andcreate footer links.
	 *
	 * @return the html
	 */
	private HTML fetchAndcreateFooterLinks() {
		MatContext.get().getLoginService().getFooterURLs(new AsyncCallback<List<String>>() {
			@Override
			public void onFailure(Throwable caught) {
				//This will create Footer links with default values.
				//createFooterLinks(footerLinks);
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
	
	/**
	 * Gets the content panel.
	 *
	 * @return the content panel
	 */
	protected  FocusPanel getContentPanel() {
		return content;
	}
	
	/**
	 * Gets the logout panel.
	 *
	 * @return the logout panel
	 */
	protected HorizontalFlowPanel getLogoutPanel(){
		return logOutPanel;
	}
	
	/**
	 * Gets the navigation list.
	 *
	 * @return the navigation list
	 */
	protected Widget getNavigationList(){
		return null;
	}
	
	
	
	/**
	 * Inits the entry point.
	 */
	protected abstract void initEntryPoint();
	
	/**
	 * On module load.
	 */
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
	
	/**
	 * Sets the id.
	 *
	 * @param widget the widget
	 * @param id the id
	 */
	protected void setId(final Widget widget, final String id) {
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}
	
	/**
	 * Sets the logout.
	 *
	 * @param logOutPanel the new logout
	 */
	protected void setLogout(final HorizontalFlowPanel logOutPanel){
		this.logOutPanel = logOutPanel;
	}
	
	/**
	 * Gets the welcome user panel.
	 *
	 * @param userFirstName the user first name
	 * @return the welcome user panel
	 */
	public HorizontalFlowPanel getWelcomeUserPanel(String userFirstName) {
		welcomeUserLabel = new HTML("<h9><b>Welcome, "+ userFirstName+"</b></h9>");
		welcomeUserLabel.getElement().setId("welcomeUserLabel_HTML");
		welcomeUserLabel.getElement().setAttribute("tabIndex", "0");
		welcomeUserLabel.setStylePrimaryName("htmlDescription");
		welcomeUserPanel.add(welcomeUserLabel);
		return welcomeUserPanel;
	}
	
	/**
	 * Gets the Version panel.
	 *
	 * @return the Flow panel
	 */
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
	
}
