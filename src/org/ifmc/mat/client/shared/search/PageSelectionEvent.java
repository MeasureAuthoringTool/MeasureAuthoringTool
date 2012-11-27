package org.ifmc.mat.client.shared.search;

import com.google.gwt.event.shared.GwtEvent;

public class PageSelectionEvent extends GwtEvent<PageSelectionEventHandler> {
	public static GwtEvent.Type<PageSelectionEventHandler> TYPE = 
		new GwtEvent.Type<PageSelectionEventHandler>();
	
	private int pageNumber;
	
	public PageSelectionEvent(int page) {
		pageNumber = page;
	}
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<PageSelectionEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PageSelectionEventHandler handler) {
		handler.onPageSelection(this);
	}

	public static GwtEvent.Type<PageSelectionEventHandler> getType() {
		return TYPE;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

}
