package mat.client.shared.search;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Interface SearchDisplay.
 */
public interface SearchDisplay {
	
	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget();
	
	/**
	 * Gets the search button.
	 * 
	 * @return the search button
	 */
	public HasClickHandlers getSearchButton();
	
	/**
	 * Gets the search string.
	 * 
	 * @return the search string
	 */
	public HasValue<String> getSearchString();
	
	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize();
	
	/**
	 * Gets the page size selection tool.
	 * 
	 * @return the page size selection tool
	 */
	public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	
	/**
	 * Gets the page selection tool.
	 * 
	 * @return the page selection tool
	 */
	public HasPageSelectionHandler getPageSelectionTool();

}
