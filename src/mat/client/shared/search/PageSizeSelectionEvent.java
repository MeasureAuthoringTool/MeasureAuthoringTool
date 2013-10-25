package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class PageSizeSelectionEvent.
 */
public class PageSizeSelectionEvent extends GwtEvent<PageSizeSelectionEventHandler> {
	
	/** The type. */
	public static GwtEvent.Type<PageSizeSelectionEventHandler> TYPE = 
		new GwtEvent.Type<PageSizeSelectionEventHandler>();
	
	/** The page size. */
	private int pageSize;
	
	/**
	 * Instantiates a new page size selection event.
	 * 
	 * @param size
	 *            the size
	 */
	public PageSizeSelectionEvent(int size) {
		pageSize = size;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSizeSelectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(PageSizeSelectionEventHandler handler) {
		handler.onPageSizeSelection(this);
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public static GwtEvent.Type<PageSizeSelectionEventHandler> getType() {
		return TYPE;
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the new page size
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
