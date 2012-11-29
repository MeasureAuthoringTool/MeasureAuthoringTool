/**
 * 
 */
package mat.client.util;

import mat.client.shared.MatContext;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;

/**
 * @author jnarang
 *
 */
public class FooterLinksUtility {
	
	
	public static Anchor createFooterLink(final String text,final String styleName,final String module,final String htmlPage,final String URL){
		final Anchor anchor = new Anchor(text);
		anchor.setTitle(text);
		anchor.setStyleName(styleName);
		anchor.getElement().setAttribute("alt", text);
		
		anchor.addClickHandler(
			new ClickHandler() {
				@Override
				public void onClick(final ClickEvent event) {
					if(!MatContext.get().getCurrentModule().equalsIgnoreCase(module)){
						MatContext.get().restartTimeoutWarning();
					}
					if((URL == null) && (htmlPage !=null)){
						final String page = htmlPage;
						MatContext.get().openNewHtmlPage(page);
					}else{
						MatContext.get().openURL(URL);
						
					}
					
				}
			});
		
		return anchor;
	}

}
