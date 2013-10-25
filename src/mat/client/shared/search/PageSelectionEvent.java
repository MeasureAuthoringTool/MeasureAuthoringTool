package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class PageSelectionEvent.
 */
public class PageSelectionEvent extends GwtEvent<PageSelectionEventHandler> {
	
	/** The type. */
	public static GwtEvent.Type<PageSelectionEventHandler> TYPE = 
		new GwtEvent.Type<PageSelectionEventHandler>();
	
	/** The page number. */
	private int pageNumber;
	
	/**
	 * Instantiates a new page selection event.
	 * 
	 * @param page
	 *            the page
	 */
	public PageSelectionEvent(int page) {
		pageNumber = page;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSelectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(PageSelectionEventHandler handler) {
		handler.onPageSelection(this);
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public static GwtEvent.Type<PageSelectionEventHandler> getType() {
		return TYPE;
	}

	/**
	 * Gets the page number.
	 * 
	 * @return the page number
	 */
	public int getPageNumber() {
		return pageNumber;
	}

	/**
	 * Sets the page number.
	 * 
	 * @param pageNumber
	 *            the new page number
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

}
