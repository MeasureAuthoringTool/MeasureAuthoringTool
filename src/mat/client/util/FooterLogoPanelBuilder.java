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
 *
 */
public class FooterLogoPanelBuilder {
	
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

}
