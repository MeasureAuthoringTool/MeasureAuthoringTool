package org.ifmc.mat.client;

import org.ifmc.mat.client.shared.FocusableImageButton;
import org.ifmc.mat.client.shared.FocusableWidget;
import org.ifmc.mat.client.shared.MatContext;
import org.ifmc.mat.client.shared.SkipListBuilder;
import org.ifmc.mat.client.shared.SpacerWidget;
import org.ifmc.mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class MainLayout {
	private FocusPanel content;
	
	private static Panel loadingPanel;
	
	private SimplePanel logOutPanel;
	
	private Anchor policyAnchor;
	private Anchor linkAnchor;
	
	protected static FocusableWidget skipListHolder;
	
	private static HTML loadingWidget = new HTML("Loading Please Wait...");
	private static Image alertImage = new Image(ImageResources.INSTANCE.alert());
	private static String alertTitle = "Informational Alert";
	private static final int DEFAULT_LOADING_MESSAGE_DELAY_IN_MILLISECONDS = 500;
	
	public final void onModuleLoad() {
		
		Panel skipContent = buildSkipContent();
		
		Panel topBanner = buildTopPanel();
		Panel footerPanel = buildFooterPanel();
		Panel contentPanel = buildContentPanel();
		Panel loadingPanel = buildLoadingPanel();
		FlowPanel container = new FlowPanel();
		container.add(new SpacerWidget());
		container.add(new SpacerWidget());
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
	 * 'Accessibility Policy' link.
	 * @return Panel
	 */
	private Panel buildFooterPanel() {
		HorizontalPanel footerPanel = new HorizontalPanel();
		footerPanel.setStylePrimaryName("footer");		
		policyAnchor = new Anchor("Accessibility Policy");
		policyAnchor.setTitle("Accessibility Policy");
		policyAnchor.setStyleName("footerLink");
		policyAnchor.getElement().setAttribute("alt", "Accessibility Policy");
		
		policyAnchor.addClickHandler(
			new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
						MatContext.get().restartTimeoutWarning();
					}
					String htmlPage = "/accessibilityPolicy.html";
					MatContext.get().openNewHtmlPage(htmlPage);
				}
			}); 
		footerPanel.add(policyAnchor);
		
		
		linkAnchor = new Anchor("User Guide");
		linkAnchor.setTitle("User Guide");
		linkAnchor.setStyleName("footerLinkRight");
		linkAnchor.getElement().setAttribute("alt", "User Guide");
		
		linkAnchor.addClickHandler(
			new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
						MatContext.get().restartTimeoutWarning();
					}
					// ### MatContext.get().openURL("http://www.qualityforum.org/WorkArea/linkit.aspx?LinkIdentifier=id&ItemID=68493");
					//To Do : Temporary link - Above line has to be uncommented with new link.
					MatContext.get().openURL("###");
				}
			}); 
		footerPanel.add(linkAnchor);
		
		
		return footerPanel;
	}
	
	private Panel buildTopPanel() {
		HorizontalPanel topBanner = new HorizontalPanel();
		setId(topBanner, "title");
		topBanner.setStylePrimaryName("topBanner");
		//To Do Add logo 
		//FocusableImageButton logo = new FocusableImageButton(ImageResources.INSTANCE.logo_###(),"MAT");
		//logo.setStylePrimaryName("topBannerImage");
		//topBanner.add(logo);
		FocusableImageButton titleImage= new FocusableImageButton(ImageResources.INSTANCE.g_header_title(),"Measure Authoring Tool");
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
	
	protected void setId(Widget widget, String id) {
		DOM.setElementAttribute(widget.getElement(), "id", id);
	}
	
	protected void setLogout(SimplePanel logOutPanel){
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
		hideLoadingMessage(DEFAULT_LOADING_MESSAGE_DELAY_IN_MILLISECONDS);
	}
	
	/**
	 * delay hiding of loading message artifacts by 'delay' milliseconds
	 * NOTE delay cannot be <= 0 else exception is thrown
	 * public method to allow setting of delay
	 */
	public static void hideLoadingMessage(int delay){
		if(delay > 0){
			Timer t = new Timer() {
				@Override
				public void run() {
					delegateHideLoadingMessage();
				}
			};
			t.schedule(delay);
		}
		else
			delegateHideLoadingMessage();
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
