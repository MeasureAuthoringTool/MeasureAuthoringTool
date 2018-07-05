package mat.client.shared;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

/**
 * The Class CustomButton.
 */
public class CustomButton extends Button { 
	
	/** The text. */
	private String text; 
	
	/** The img. */
	Image img;
	
	/**
	 * Instantiates a new custom button.
	 */
	public CustomButton(){
		super();
		getElement().removeAttribute("class");
	}
	
	/**
	 * Sets the resource.
	 * 
	 * @param imageResource
	 *            the image resource
	 * @param imageTitle
	 *            the image title
	 */
	public void setResource(ImageResource imageResource, String imageTitle){ 
		img = new Image(imageResource); 
		img.setAltText(imageTitle);
		String definedStyles = img.getElement().getAttribute("style"); 
		img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle;"); 
		DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement())); 
		setText(imageTitle);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ButtonBase#setText(java.lang.String)
	 */
	@Override 
	public void setText(String text) { 
		this.text = text; 
		Element span = DOM.createElement("span"); 
		span.setInnerText(text); 
		span.setAttribute("style", "padding-left:3px; vertical-align:middle;");
		DOM.insertChild(getElement(), span, 0); 
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.ButtonBase#getText()
	 */
	@Override 
	public String getText() { 
		return this.text; 
	} 
	
	/**
	 * Gets the image.
	 * 
	 * @return the image
	 */
	public Image getImage(){
		return this.img;
	}
}

