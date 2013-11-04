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
	
	/** The instance. */
	ImageResources INSTANCE= GWT.create(ImageResources.class);
	
	
	/**
	 * Adds the all left.
	 *
	 * @return the image resource
	 */
	@Source("images/addAllLeft.png")
	ImageResource addAllLeft();
	
	
	/**
	 * Adds the all right.
	 *
	 * @return the image resource
	 */
	@Source("images/addAllRight.png")
	ImageResource addAllRight();
	
	/**
	 * Adds the image.
	 *
	 * @return the image resource
	 */
	@Source("images/addImage.png")
	ImageResource addImage();
	
	/**
	 * Adds the left.
	 *
	 * @return the image resource
	 */
	@Source("images/addLeft.png")
	ImageResource addLeft();
	
	/**
	 * Adds the right.
	 *
	 * @return the image resource
	 */
	@Source("images/addRight.png")
	ImageResource addRight();
	
	/**
	 * Alert.
	 *
	 * @return the image resource
	 */
	@Source("images/alert.png")
	ImageResource alert();
	
	/**
	 * Application_cascade.
	 *
	 * @return the image resource
	 */
	@Source("images/application_cascade.png")
	ImageResource application_cascade();
	
	/**
	 * Arrow_filter.
	 *
	 * @return the image resource
	 */
	@Source("images/arrow_filter.png")
	ImageResource arrow_filter();
	
	/**
	 * Bullet_green.
	 *
	 * @return the image resource
	 */
	@Source("images/bullet_green.png")
	ImageResource bullet_green();
	
	/**
	 * Bullet_red.
	 *
	 * @return the image resource
	 */
	@Source("images/bullet_red.png")
	ImageResource bullet_red();
	
	
	/**
	 * Calendar.
	 *
	 * @return the image resource
	 */
	@Source("images/calendar.gif")
	ImageResource calendar();
	
	/**
	 * Clock.
	 *
	 * @return the image resource
	 */
	@Source("images/clock.png")
	ImageResource clock();
	
	/**
	 * Close.
	 *
	 * @return the image resource
	 */
	@Source("images/cross-button.png")
	ImageResource close();
	
	/* ### New Logo to be added @Source("images/###_logo.gif")
	public ImageResource ###_logo();*/
	/**
	 * Cms_gov_footer.
	 *
	 * @return the image resource
	 */
	@Source("images/cms_gov_footer.png")
	ImageResource cms_gov_footer();
	
	/**
	 * Collapsed.
	 *
	 * @return the image resource
	 */
	@Source("images/collapsed.gif")
	ImageResource collapsed();
	
	
	/**
	 * createMeasure.
	 *
	 * @return the image resource
	 */
	@Source("images/createMeasure.png")
	ImageResource createMeasure();
	
	/**
	 * Expanded.
	 *
	 * @return the image resource
	 */
	@Source("images/expanded.gif")
	ImageResource expanded();
	
	
	/**
	 * G_close panel.
	 *
	 * @return the image resource
	 */
	@Source("images/close_panel.png")
	ImageResource g_closePanel();
	
	/**
	 * G_delete.
	 *
	 * @return the image resource
	 */
	@Source("images/g_delete.png")
	ImageResource g_delete();
	
	/**
	 * G_header_title.
	 *
	 * @return the image resource
	 */
	@Source("images/g_header_title.gif")
	ImageResource g_header_title();
	
	/**
	 * G_lock.
	 *
	 * @return the image resource
	 */
	@Source("images/g_lock.gif")
	ImageResource g_lock();
	
	/**
	 * G_open panel.
	 *
	 * @return the image resource
	 */
	@Source("images/open_panel.png")
	ImageResource g_openPanel();
	
	/**
	 * G_package_edit.
	 *
	 * @return the image resource
	 */
	@Source("images/g_package_edit.png")
	ImageResource g_package_edit();
	
	/**
	 * G_package_go.
	 *
	 * @return the image resource
	 */
	@Source("images/g_package_go.gif")
	ImageResource g_package_go();
	
	/**
	 * G_package_share.
	 *
	 * @return the image resource
	 */
	@Source("images/g_package_share.png")
	ImageResource g_package_share();
	
	/**
	 * G_page_copy.
	 *
	 * @return the image resource
	 */
	@Source("images/g_page_copy.gif")
	ImageResource g_page_copy();
	
	/**
	 * G_trash.
	 *
	 * @return the image resource
	 */
	@Source("images/trash.png")
	ImageResource g_trash();
	
	/**
	 * Help.
	 *
	 * @return the image resource
	 */
	@Source("images/help.gif")
	ImageResource help();
	
	/**
	 * Hhslogo.
	 *
	 * @return the image resource
	 */
	@Source("images/hhslogo.png")
	ImageResource hhslogo();
	
	/**
	 * Icon_new window.
	 *
	 * @return the image resource
	 */
	@Source("images/icon_newWindow.gif")
	ImageResource icon_newWindow();
	
	/**
	 * Icon_success_sm.
	 *
	 * @return the image resource
	 */
	@Source("images/icon_success_sm.gif")
	ImageResource icon_success_sm();
	
	/**
	 * Logo_ifmc_power.
	 *
	 * @return the image resource
	 */
	@Source("images/logo_ifmc_power.gif")
	ImageResource logo_ifmc_power();
	
	/**
	 * Msg_error.
	 *
	 * @return the image resource
	 */
	@Source("images/error.png")
	ImageResource msg_error();
	
	/**
	 * Msg_success.
	 *
	 * @return the image resource
	 */
	@Source("images/success.png")
	ImageResource msg_success();
	
	/**
	 * Read only.
	 *
	 * @return the image resource
	 */
	@Source("images/application_view_detail_1.png")
	ImageResource ReadOnly();
	
	/**
	 * Search_zoom.
	 *
	 * @return the image resource
	 */
	@Source("images/search.png")
	ImageResource search_zoom();
}
