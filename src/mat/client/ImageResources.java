/**
 * 
 */
package mat.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * An Client bundle is a construct used to improve application performance by reducing the number of 
 * round trip HTTP requests to the server to fetch images
 * 
 * 
 *  An Client bundle is a composition of many images into a single image, along with an interface for
 *  accessing the individual images from within the composite. Users can define an image bundle that 
 *  contains the images used by their application, and GWT will automatically create the composite image 
 *  and provide an implementation of the interface for accessing each individual image. 
 *  Instead of a round trip to the server for each image, only one round trip to the server for the 
 *  composite image is needed.


 * @author vandavar
 * 
 */
public interface ImageResources extends ClientBundle {
	
	ImageResources INSTANCE= GWT.create(ImageResources.class);
	
	
	@Source("images/addAllLeft.png")
	public ImageResource addAllLeft();
	
	
	@Source("images/addAllRight.png")
	public ImageResource addAllRight();
	
	@Source("images/addImage.png")
	public ImageResource addImage();
	
	@Source("images/addLeft.png")
	public ImageResource addLeft();
	
	@Source("images/addRight.png")
	public ImageResource addRight();
	
	@Source("images/calendar.gif")
	public ImageResource calendar();
	
	@Source("images/collapsed.gif")
	public ImageResource collapsed();
	
	@Source("images/expanded.gif")
	public ImageResource expanded();
	
	@Source("images/g_delete.png")
	public ImageResource g_delete();
	
	
	@Source("images/g_header_title.gif")
	public ImageResource g_header_title();
	
	
	@Source("images/g_lock.gif")
	public ImageResource g_lock();
	
	
	@Source("images/g_package_edit.png")
	public ImageResource g_package_edit();
	
	
	@Source("images/g_package_go.gif")
	public ImageResource g_package_go();
	
	@Source("images/g_package_share.png")
	public ImageResource g_package_share();
	
	
	@Source("images/g_page_copy.gif")
	public ImageResource g_page_copy();
	
	@Source("images/error.png")
	public ImageResource msg_error();
	
	@Source("images/success.png")
	public ImageResource msg_success();
	
	@Source("images/help.gif")
	public ImageResource help();
	
	@Source("images/icon_newWindow.gif")
	public ImageResource icon_newWindow();
	
	@Source("images/icon_success_sm.gif")
	public ImageResource icon_success_sm();
	
	@Source("images/logo_ifmc_power.gif")
	public ImageResource logo_ifmc_power();
	
	/* ### New Logo to be added @Source("images/###_logo.gif")
	public ImageResource ###_logo();*/
	
	@Source("images/application_view_detail_1.png")
	public ImageResource ReadOnly();
	
	@Source("images/alert.png")
	public ImageResource alert();
	
	@Source("images/clock.png")
	public ImageResource clock();
	
	@Source("images/application_cascade.png")
	public ImageResource application_cascade();
}
