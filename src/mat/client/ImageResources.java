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
 */
public interface ImageResources extends ClientBundle {
	
	ImageResources INSTANCE= GWT.create(ImageResources.class);
	
	@Source("images/alert.png")
	ImageResource alert();
		
	@Source("images/cross-button.png")
	ImageResource close();

	@Source("images/cms_gov_footer.png")
	ImageResource cms_gov_footer();

	@Source("images/createMeasure.png")
	ImageResource createMeasure();
	
	@Source("images/g_header_title.gif")
	ImageResource g_header_title();

	@Source("images/g_lock.gif")
	ImageResource g_lock();

	@Source("images/help.gif")
	ImageResource help();

	@Source("images/hhslogo.png")
	ImageResource hhslogo();

	@Source("images/icon_success_sm.gif")
	ImageResource icon_success_sm();

	@Source("images/error.png")
	ImageResource msg_error();

	@Source("images/success.png")
	ImageResource msg_success();

	@Source("images/search.png")
	ImageResource search_zoom();
	
	@Source("images/go_up.png")
	ImageResource go_up();
	
	@Source("images/go_down.png")
	ImageResource go_down();
	
	@Source("images/richTextToolbar.png")
	ImageResource rich_Text_Toolbar();
	
	@Source("images/copyIcon.png")
	ImageResource getCopy();
	
	@Source("images/pasteIcon.png")
	ImageResource getPaste();
	
	@Source("images/eraserIcon.png")
	ImageResource getErase();
	
	@Source("images/grayscale_pasteIcon.png")
	ImageResource getGrayScalePaste();
}
