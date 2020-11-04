package mat.client.util;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import mat.client.ImageResources;
import mat.client.shared.MatContext;
import mat.shared.ConstantMessages;

/**
 * The Class FooterPanelBuilderUtility.
 * 
 * @author cbajikar This is a utility class for building the Footer. It uses
 *         direct HTML from the MAT Public Website footer.
 */
public class FooterPanelBuilderUtility {
	
	/**
	 * Builds the footer logo panel.
	 * 
	 * @return the html
	 */
	public static HTML buildFooterLogoPanel(){
		String html = new String (logosHTML);
		
		Image logoCMS = new Image(ImageResources.INSTANCE.cms_gov_footer());
		logoCMS.setAltText("CMS.gov");
		logoCMS.getElement().setId("footer-address-img");
		addClickHandlerToRestartTimeout(logoCMS);
		Image logoHHS = new Image(ImageResources.INSTANCE.hhslogo());
		logoHHS.setAltText("Department of Health and Human Services USA");
		logoHHS.getElement().setId("footer-cms-logo");
		addClickHandlerToRestartTimeout(logoHHS);
		System.out.println("CMS img:"+logoCMS.toString());
		System.out.println("HHS:"+logoHHS.toString());
                
        logoCMS.getElement().setAttribute("tabIndex", "-1");
		logoHHS.getElement().setAttribute("tabIndex", "-1");
		
        html = html.replaceAll("\\{img_cms_logo\\}", logoCMS.toString());
		html = html.replaceAll("\\{img_hhs_logo\\}", logoHHS.toString());
		
		
		HTML logoHTMLDiv = new HTML(html, true);

		logoHTMLDiv.getElement().setAttribute("tabIndex", "-1");
		return logoHTMLDiv;
	}
	
	/**
	 * Builds the footer links panel.
	 * 
	 * @return the html
	 */
	public static HTML buildFooterLinksPanel(){
		String html = new String(footerLinkHTML);
		
		html = html.replaceAll("\\{accessibility-policy\\}", ClientConstants.ACCESSIBILITY_POLICY_URL);
		html = html.replaceAll("\\{terms-of-use\\}", ClientConstants.TERMSOFUSE_URL);
		html = html.replaceAll("\\{privacy-policy\\}", ClientConstants.PRIVACYPOLICY_URL);
		html = html.replaceAll("\\{user-guide\\}", ClientConstants.USERGUIDE_URL);
		html = html.replaceAll("\\{contact-us\\}", ClientConstants.CONTACTUS_URL);
		
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
	
	/**
	 * Adds the click handler to restart timeout.
	 * 
	 * @param image
	 *            the image
	 */
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

	/** The logos html. */
	private static String logosHTML =
			"<div class=\"cms-address-bar\"> "+
					"<div id=\"footer-cms-logo-wrapper\" align=\"left\"> " +
					"<a id=\"footer_cms_logo_link\" title=\"CMS home page\" tabindex=\"-1\" href=\"http://www.cms.gov/\" target=\"_blank\"> " +
					"<h4 style=\"font-size:0;\">CMS.gov</h4>" +
					"{img_cms_logo}" +
					"</a> "+
					"</div> " +
					"<div id=\"footer-address-text\" tabindex=\"-1\"> " +
					"A federal government website managed by the Centers for Medicare " +
					"&amp; Medicaid Services <br> 7500 Security Boulevard, " +
					"Baltimore, MD 21244" +
					"</div> "+
					"<div id=\"footer-hhs-logo-wrapper\" align=\"right\"> " +
					"<div id=\"footer_hhs_logo\"> " +
					"<a id=\"footer_hhs_logo_link\" title=\"Health and Human Services home page\" tabindex=\"-1\" href=\"http://www.hhs.gov/\" target=\"_blank\"> " +
					"<h4 style=\"font-size:0;\">hhs.gov</h4>" +
					"{img_hhs_logo} " +
					"</a> "+
					"</div>" +
					"</div>" +
					"</div>";
	
	
	
	/** The footer link html. */
	private static String footerLinkHTML =
			 "<div class=\"custom-footer-nav\"> "+
						"<div> "+
						"<div class=\"content\"> "+
						"<h2 tabindex=\"-1\">Helpful Links</h2> "+
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
						"<li style=\"display: inline-block;*display: inline;\"><b> | </b></li> "+
						"<li style=\"display: inline-block;*display: inline;\"> "+
						"<a title=\"Contact Us\" target=\"_blank\" href=\"{contact-us}\"> "+
						"Contact Us "+
						"<span class=\"customLinkDisclaimer\"> - Opens in a new window</span> "+
						"</a> "+
						"</li> "+
						"</ul> "+
						"</div> "+
						"</div> "+
						"</div> ";
}
