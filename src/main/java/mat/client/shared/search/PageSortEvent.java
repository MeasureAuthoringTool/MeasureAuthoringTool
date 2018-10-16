package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

/**
 * The Class PageSortEvent.
 */
public class PageSortEvent extends GwtEvent<PageSortEventHandler> {
	
	/** The type. */
	public static GwtEvent.Type<PageSortEventHandler> TYPE = 
		new GwtEvent.Type<PageSortEventHandler>();
	
	/** The column. */
	private int column;
	
	/**
	 * Instantiates a new page sort event.
	 * 
	 * @param column
	 *            the column
	 */
	public PageSortEvent(int column) {
		this.column = column;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#getAssociatedType()
	 */
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSortEventHandler> getAssociatedType() {
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.GwtEvent#dispatch(com.google.gwt.event.shared.EventHandler)
	 */
	@Override
	protected void dispatch(PageSortEventHandler handler) {
		handler.onPageSort(this);
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public static GwtEvent.Type<PageSortEventHandler> getType() {
		return TYPE;
	}

	/**
	 * Gets the column.
	 * 
	 * @return the column
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Sets the column.
	 * 
	 * @param column
	 *            the new column
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	

}
