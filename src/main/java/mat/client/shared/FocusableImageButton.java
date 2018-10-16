package mat.client.shared;

import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Image;

/**
 * The Class FocusableImageButton.
 */
public class FocusableImageButton extends FocusableWidget {
	
	/** The enable status. */
	private boolean enableStatus=true;
	
	/** The image. */
	private Image image;

	public FocusableImageButton(ImageResource imageUrl, String title) {
		super(new Image(imageUrl));
		image = (Image)getWidget();
		setTitle(title);
		DOM.setElementProperty(getWidget().getElement(), "alt", title);
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		((Image)getWidget()).setTitle(title);	
	}
	
	/**
	 * Sets the url.
	 * 
	 * @param url
	 *            the new url
	 */
	public void setUrl(String url) {
		image.setUrl(url);
	}
	
	/**
	 * Enable image.
	 * 
	 * @param enable
	 *            the enable
	 */
	public void enableImage(boolean enable) {
		if(enable) {
			removeStyleName("DisableImg");
			enableStatus = true;
		} else {
			addStyleName("DisableImg");
			enableStatus = false;
		}
	}
	
	/**
	 * Checks if is enabled.
	 * 
	 * @return true, if is enabled
	 */
	public boolean isEnabled(){
		return enableStatus;
	}
}
