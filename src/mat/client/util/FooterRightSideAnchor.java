/**
 * 
 */
package mat.client.util;

import mat.client.ImageResources;
import mat.client.shared.MatContext;
import mat.shared.ConstantMessages;

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
import com.google.gwt.user.client.ui.Image;

/**
 * @author jnarang
 *
 */
public class FooterRightSideAnchor {
	
	public static Anchor rightSideAnchorMatFooter(){
		
		final Anchor footerRightAnchor = new Anchor();
		footerRightAnchor.setStylePrimaryName("footerLogo-Right");
		footerRightAnchor.setTitle(ClientConstants.TEXT_HHS_LINK);
		
		final Image rightlogo = new Image(ImageResources.INSTANCE.hhslogo());
		
		footerRightAnchor.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
					MatContext.get().restartTimeoutWarning();
				}
				MatContext.get().openURL(ClientConstants.EXT_LINK_HHS_Site);
			}
		  }
		);
		footerRightAnchor.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getCharCode() == KeyCodes.KEY_ENTER){
					if(!MatContext.get().getCurrentModule().equalsIgnoreCase(ConstantMessages.LOGIN_MODULE)){
						MatContext.get().restartTimeoutWarning();
					}
					MatContext.get().openURL(ClientConstants.EXT_LINK_HHS_Site);
				}
			}
		});
		footerRightAnchor.addMouseOverHandler(new MouseOverHandler(){
			@Override
			public void onMouseOver(MouseOverEvent event) {
				DOM.setStyleAttribute(rightlogo.getElement(), "cursor", "pointer");
			}
		 });
		
		footerRightAnchor.addMouseOutHandler(new MouseOutHandler() {
		    @Override
		    public void onMouseOut(MouseOutEvent event) {
		        DOM.setStyleAttribute(rightlogo.getElement(), "cursor", "default");                        
		    }
		});
		footerRightAnchor.getElement().appendChild(rightlogo.getElement());
		
		return footerRightAnchor;
		
	}

}
