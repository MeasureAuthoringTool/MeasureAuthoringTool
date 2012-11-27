package mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

public class PageSortEvent extends GwtEvent<PageSortEventHandler> {
	public static GwtEvent.Type<PageSortEventHandler> TYPE = 
		new GwtEvent.Type<PageSortEventHandler>();
	
	private int column;
	
	public PageSortEvent(int column) {
		this.column = column;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSortEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageSortEventHandler handler) {
		handler.onPageSort(this);
	}

	public static GwtEvent.Type<PageSortEventHandler> getType() {
		return TYPE;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	

}
