package org.ifmc.mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

public class PageSizeSelectionEvent extends GwtEvent<PageSizeSelectionEventHandler> {
	public static GwtEvent.Type<PageSizeSelectionEventHandler> TYPE = 
		new GwtEvent.Type<PageSizeSelectionEventHandler>();
	
	private int pageSize;
	
	public PageSizeSelectionEvent(int size) {
		pageSize = size;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSizeSelectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageSizeSelectionEventHandler handler) {
		handler.onPageSizeSelection(this);
	}

	public static GwtEvent.Type<PageSizeSelectionEventHandler> getType() {
		return TYPE;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


}
