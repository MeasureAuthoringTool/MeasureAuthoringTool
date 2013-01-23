package mat.client;

import java.util.List;

import mat.client.shared.FocusableImageButton;
import mat.client.shared.FocusableWidget;

import mat.client.shared.MatContext;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;

import mat.client.util.ClientConstants;
import mat.client.util.FooterPanelBuilderUtility;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MainLayout {
	
	private FocusPanel content;
	private static Panel loadingPanel;
	private SimplePanel logOutPanel;
	
	
	
	protected static FocusableWidget skipListHolder;
	
	private static HTML loadingWidget = new HTML(ClientConstants.MAINLAYOUT_LOADING_WIDGET_MSG);
	private static Image alertImage = new Image(ImageResources.INSTANCE.alert());
	private static String alertTitle = ClientConstants.MAINLAYOUT_ALERT_TITLE;
	private static final int DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS = 500;
	
	public final void onModuleLoad() {
		
		final Panel skipContent = buildSkipContent();
		
		final Panel topBanner = buildTopPanel();
		final Panel footerPanel = buildFooterPanel();
		final Panel contentPanel = buildContentPanel();
		final Panel loadingPanel = buildLoadingPanel();
		final FlowPanel container = new FlowPanel();
		//container.add(new SpacerWidget());
		//container.add(new SpacerWidget());
		container.add(topBanner);
		container.add(loadingPanel);
		container.add(contentPanel);
		container.add(footerPanel);
	
		
		RootPanel.get().clear();
		if(RootPanel.get("skipContent")!= null){
			RootPanel.get("skipContent").add(skipContent);
		}
		RootPanel.get("mainContent").add(container);
		
		initEntryPoint();

	}
	
	private Panel buildSkipContent() {
		 skipListHolder = new FocusableWidget(SkipListBuilder.buildSkipList("Skip to Main Content"));
		 return skipListHolder;
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
		return content;
	}
	
	
	private Panel buildLoadingPanel() {
		loadingPanel = new HorizontalPanel();
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
	 * Builds the Footer Panel for the Login and Mat View. Currently, it displays the 
	 * 'Accessibility Policy' , 'Terms Of Use' , 'Privacy Policy' 'User Guide' links with CMS LOGO.
	 * @return Panel
	 */
	private Panel buildFooterPanel() {
		
		FlowPanel footerMainPanel = new FlowPanel();
		footerMainPanel.setStylePrimaryName("footer");		
		footerMainPanel.add(FooterPanelBuilderUtility.buildFooterLogoPanel());
				
//		final VerticalPanel footerLinksPanel = new VerticalPanel();
//		footerLinksPanel.setStylePrimaryName("footer-nav");
//		
//		HTML helpFullLinks = new HTML("&nbsp;&nbsp;<h2>Helpful Links</h2>");
//		footerLinksPanel.add(helpFullLinks);
//		
//		final HorizontalPanel footerLinks = new HorizontalPanel();
//		fetchAndcreateFooterLinks(footerLinks);
//		
//		footerLinksPanel.add(footerLinks);
		
		footerMainPanel.add(fetchAndcreateFooterLinks());
		return footerMainPanel;
}
	
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
				//This will create Footer links with values from server.
				//createFooterLinks(footerLinks);
			}
		
		});
		return FooterPanelBuilderUtility.buildFooterLinksPanel();
	}

//	private void createFooterLinks(HorizontalPanel footerLinks) {
//		
//		final String pipeHTML = "&nbsp;&nbsp;<b>|</b>";
//		
//		final Anchor policyAnchor = FooterPanelBuilderUtility.createFooterLink(ClientConstants.TEXT_ACCESSIBILITY_POLICY, null, ConstantMessages.LOGIN_MODULE, 
//				null,ClientConstants.ACCESSIBILITY_POLICY_URL);
//		footerLinks.add(policyAnchor);
//		HTML pipe = new HTML(pipeHTML);
//		footerLinks.add(pipe);
//		
//		final Anchor privacyPolicyAnchor = FooterPanelBuilderUtility.createFooterLink(ClientConstants.TEXT_PRIVACYPOLICY, null, ConstantMessages.LOGIN_MODULE, 
//		null,ClientConstants.PRIVACYPOLICY_URL);
//		footerLinks.add(privacyPolicyAnchor);
//		HTML pipe_2 = new HTML(pipeHTML);
//		footerLinks.add(pipe_2);
//		
//		final Anchor termsOfUseAnchor = FooterPanelBuilderUtility.createFooterLink(ClientConstants.TEXT_TERMSOFUSE, null, ConstantMessages.LOGIN_MODULE, 
//		null,ClientConstants.TERMSOFUSE_URL);
//		footerLinks.add(termsOfUseAnchor);
//		HTML pipe_3 = new HTML(pipeHTML);
//		footerLinks.add(pipe_3);
//		
//		final Anchor foiaAnchor = FooterPanelBuilderUtility.createFooterLink(ClientConstants.TEXT_FOIA, null, ConstantMessages.LOGIN_MODULE, 
//																null,ClientConstants.EXT_LINK_FOIA);
//		footerLinks.add(foiaAnchor);
//		HTML pipe_4 = new HTML(pipeHTML);
//		footerLinks.add(pipe_4);
//		
//		
//		final Anchor linkAnchor = FooterPanelBuilderUtility.createFooterLink(ClientConstants.TEXT_USER_GUIDE, null, ConstantMessages.LOGIN_MODULE, 
//		null,ClientConstants.USERGUIDE_URL);
//		footerLinks.add(linkAnchor);
//		
//	}

	private Panel buildTopPanel() {
		final HorizontalPanel topBanner = new HorizontalPanel();
		setId(topBanner, "title");
		topBanner.setStylePrimaryName("topBanner");
		//To Do Add logo 
		//FocusableImageButton logo = new FocusableImageButton(ImageResources.INSTANCE.logo_###(),"MAT");
		//logo.setStylePrimaryName("topBannerImage");
		//topBanner.add(logo);
		final FocusableImageButton titleImage= new FocusableImageButton(ImageResources.INSTANCE.g_header_title(),"Measure Authoring Tool");
		titleImage.setStylePrimaryName("topBannerImage");
		topBanner.add(titleImage);
		logOutPanel = new SimplePanel();
		logOutPanel.addStyleName("logoutPanel");
		topBanner.add(logOutPanel);
		return topBanner;      
	}
	
	protected abstract void initEntryPoint();
	
	protected  FocusPanel getContentPanel() {
		return content;
	}
	
	protected static Panel getLoadingPanel(){
		return loadingPanel;
	}
	
	protected static FocusableWidget getSkipList(){
		return skipListHolder;
	}
	
	protected void setId(final Widget widget, final String id) {
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}
	
	protected void setLogout(final SimplePanel logOutPanel){
		this.logOutPanel = logOutPanel;
	}
	
	protected SimplePanel getLogoutPanel(){
		return logOutPanel;
	}
	protected Widget getNavigationList(){
		return null;
	}
	
	public static void showLoadingMessage(){
		getLoadingPanel().clear();
		getLoadingPanel().add(alertImage);
		getLoadingPanel().add(loadingWidget);
		getLoadingPanel().setStyleName("msg-alert");
		getLoadingPanel().getElement().setAttribute("role", "alert");
		MatContext.get().getLoadingQueue().add("node");
	}
	
	/**
	 * no arg method adds default delay to loading message hide op
	 */
	public static void hideLoadingMessage(){
		hideLoadingMessage(DEFAULT_LOADING_MSAGE_DELAY_IN_MILLISECONDS);
	}
	
	/**
	 * delay hiding of loading message artifacts by 'delay' milliseconds
	 * NOTE delay cannot be <= 0 else exception is thrown
	 * public method to allow setting of delay
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
	 * clear the loading panel
	 * remove css style
	 * reset the loading queue
	 */
	private static void delegateHideLoadingMessage(){
		MatContext.get().getLoadingQueue().poll();
		if(MatContext.get().getLoadingQueue().size() == 0){
			getLoadingPanel().clear();
			getLoadingPanel().remove(alertImage);
			getLoadingPanel().remove(loadingWidget);
			getLoadingPanel().removeStyleName("msg-alert");
			getLoadingPanel().getElement().removeAttribute("role");
		}
	}
	
}
