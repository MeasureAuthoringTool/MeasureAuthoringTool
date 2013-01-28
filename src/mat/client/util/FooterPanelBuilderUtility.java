/**
 * 
 */
package mat.client.util;

import mat.client.ImageResources;
import mat.client.shared.MatContext;
import mat.shared.ConstantMessages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * @author cbajikar
 * This is a utility class for building the Footer.
 * It uses direct HTML from the MAT Public Website footer.
 */
public class FooterPanelBuilderUtility {
	
	public static HTML buildFooterLogoPanel(){
		String html = new String (logosHTML);
		
		Image logoCMS = new Image(ImageResources.INSTANCE.cms_gov_footer());
		logoCMS.getElement().setId("footer-address-img");
		addClickHandlerToRestartTimeout(logoCMS);
		Image logoHHS = new Image(ImageResources.INSTANCE.hhslogo());
		logoHHS.getElement().setId("footer-cms-logo");
		addClickHandlerToRestartTimeout(logoHHS);
				
		html = html.replaceAll("\\{img_cms_logo\\}", logoCMS.toString());
		html = html.replaceAll("\\{img_hhs_logo\\}", logoHHS.toString());
		
		HTML logoHTMLDiv = new HTML(html, true);
		return logoHTMLDiv;
	}
	
	public static HTML buildFooterLinksPanel(){
		String html = new String(footerLinkHTML);
		
		html = html.replaceAll("\\{accessibility-policy\\}", ClientConstants.ACCESSIBILITY_POLICY_URL);
		html = html.replaceAll("\\{terms-of-use\\}", ClientConstants.TERMSOFUSE_URL);
		html = html.replaceAll("\\{privacy-policy\\}", ClientConstants.PRIVACYPOLICY_URL);
		html = html.replaceAll("\\{user-guide\\}", ClientConstants.USERGUIDE_URL);
		
		HTML htmlPanel = new HTML(html);
		htmlPanel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
					MatContext.get().restartTimeoutWarning();
				}
			}
		  }
		);
		
		return htmlPanel;
	}

	private static void addClickHandlerToRestartTimeout(Image image) {
		image.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
					MatContext.get().restartTimeoutWarning();
				}
			}
		  }
		);
	}

//	public static Anchor createFooterLink(final String text,final String styleName,final String module,final String htmlPage,final String URL){
//		final Anchor anchor = new Anchor(text);
//		anchor.setTitle(text);
//		anchor.getElement().setAttribute("alt", text);
//		
//		anchor.addClickHandler(
//			new ClickHandler() {
//				@Override
//				public void onClick(final ClickEvent event) {
//					if(!MatContext.get().getCurrentModule().equalsIgnoreCase(module)){
//						MatContext.get().restartTimeoutWarning();
//					}
//				}
//			});
//		
//		return anchor;
//	}
	
	
	private static String logosHTML = 
		"<div class=\"cms-address-bar\"> "+
			"<a title=\"Link to CMS.gov home page\" href=\"http://www.cms.gov/\" target=\"_blank\"> " +
				"{img_cms_logo}" +
			"</a> "+	
			"<div id=\"footer-address-text\"> " +
				"A federal government website managed by the Centers for Medicare " +
				"&amp; Medicaid Services <br> 7500 Security Boulevard, "+
				"Baltimore, MD 21244" +
			"</div> "+
			"<div> "+
				"<a title=\"Link to Health and Human Services home page\" "+
					"href=\"http://www.hhs.gov/\" target=\"_blank\"> " +
					"{img_hhs_logo} " +
				"</a> "+
			"</div>" +
		"</div>";
	
	
	private static String footerLinkHTML = 
		"<div class=\"custom-footer-nav\"> "+
		"<div> "+
			 "<div class=\"content\"> "+
				 "<h2>Helpful Links</h2> "+
				 "<ul style=\"list-style: none;\"> "+
					 "<li style=\"display: inline-block;*display: inline;\"> "+
						 "<a title=\"Accessibility Policy\" target=\"_blank\" href=\"{accessibility-policy}\"> "+
							 "Accessibility Policy "+
						 "<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
					"</li> "+
					"<li style=\"display: inline-block;*display: inline;\"><b> | </b></li> "+
					"<li style=\"display: inline-block;*display: inline;\"> "+
						"<a title=\"Privacy Policy\" target=\"_blank\" href=\"{privacy-policy}\"> "+
							"Privacy Policy "+
						"<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
					"</li> "+
					"<li style=\"display: inline-block;*display: inline;\"><b> | </b></li> "+
					"<li style=\"display: inline-block;*display: inline;\"> "+
						"<a title=\"Terms Of Use\" target=\"_blank\" href=\"{terms-of-use}\"> "+
							"Terms Of Use "+
						"<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
					"</li> "+
					"<li style=\"display: inline-block;*display: inline;\"><b> | </b></li> "+
					"<li style=\"display: inline-block;*display: inline;\"> "+
						"<a title=\"Freedom of Information Act\" target=\"_blank\" "+ "href=\"http://www.cms.gov/center/freedom-of-information-act-center.html\"> "+
							"Freedom of Information Act  "+
						"<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
					"</li> "+
					"<li style=\"display: inline-block;*display: inline;\"><b> | </b></li> "+
					"<li style=\"display: inline-block;*display: inline;\"> "+
						"<a title=\"User Guide\" target=\"_blank\" href=\"{user-guide}\"> "+
							"User Guide "+
						"<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
					"</li> "+
				"</ul> "+
			"</div> "+
		"</div> "+
	"</div> ";
}
