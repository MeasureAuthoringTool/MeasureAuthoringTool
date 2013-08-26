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
	 ImageResource addAllLeft();
	
	
	@Source("images/addAllRight.png")
	 ImageResource addAllRight();
	
	@Source("images/addImage.png")
	 ImageResource addImage();
	
	@Source("images/addLeft.png")
	 ImageResource addLeft();
	
	@Source("images/addRight.png")
	 ImageResource addRight();
	
	@Source("images/calendar.gif")
	 ImageResource calendar();
	
	@Source("images/collapsed.gif")
	 ImageResource collapsed();
	
	@Source("images/expanded.gif")
	 ImageResource expanded();
	
	@Source("images/g_delete.png")
	 ImageResource g_delete();
	
	@Source("images/g_header_title.gif")
	ImageResource g_header_title();
	
	
	@Source("images/g_lock.gif")
	ImageResource g_lock();
	
	@Source("images/trash.png")
	ImageResource g_trash();
	
	@Source("images/open_panel.png")
	ImageResource g_openPanel();
	
	@Source("images/close_panel.png")
	ImageResource g_closePanel();
	
	@Source("images/g_package_edit.png")
	 ImageResource g_package_edit();
	
	
	@Source("images/g_package_go.gif")
	ImageResource g_package_go();
	
	@Source("images/g_package_share.png")
	ImageResource g_package_share();
	
	
	@Source("images/g_page_copy.gif")
	ImageResource g_page_copy();
	
	@Source("images/error.png")
	ImageResource msg_error();
	
	@Source("images/success.png")
	ImageResource msg_success();
	
	@Source("images/help.gif")
	ImageResource help();
	
	@Source("images/icon_newWindow.gif")
	ImageResource icon_newWindow();
	
	@Source("images/icon_success_sm.gif")
	ImageResource icon_success_sm();
	
	@Source("images/logo_ifmc_power.gif")
	ImageResource logo_ifmc_power();
	
	/* ### New Logo to be added @Source("images/###_logo.gif")
	public ImageResource ###_logo();*/
	@Source("images/cms_gov_footer.png")
	ImageResource cms_gov_footer();
	
	@Source("images/hhslogo.png")
	ImageResource hhslogo();
	
	@Source("images/application_view_detail_1.png")
	ImageResource ReadOnly();
	
	@Source("images/alert.png")
	ImageResource alert();
	
	@Source("images/clock.png")
	ImageResource clock();
	
	@Source("images/application_cascade.png")
	ImageResource application_cascade();
}
