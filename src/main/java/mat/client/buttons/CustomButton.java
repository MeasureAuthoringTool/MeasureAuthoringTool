package mat.client.buttons;

import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;

public class CustomButton extends Button { 
	
	private String text; 
	
	Image img;
	
	public CustomButton(){
		super();
		getElement().removeAttribute("class");
	}
	
	
	public void setResource(ImageResource imageResource, String imageTitle){ 
		img = new Image(imageResource); 
		img.setAltText(imageTitle);
		String definedStyles = img.getElement().getAttribute("style"); 
		img.getElement().setAttribute("style", definedStyles + "; vertical-align:middle;"); 
		DOM.insertBefore(getElement(), img.getElement(), DOM.getFirstChild(getElement())); 
		setText(imageTitle);
	}
	
	@Override 
	public void setText(String text) { 
		this.text = text; 
		Element span = DOM.createElement("span"); 
		span.setInnerText(text); 
		span.setAttribute("style", "padding-left:3px; vertical-align:middle;");
		DOM.insertChild(getElement(), span, 0); 
	}
	
	@Override 
	public String getText() { 
		return this.text; 
	} 
	
	public Image getImage(){
		return this.img;
	}
}

