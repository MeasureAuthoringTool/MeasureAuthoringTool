package org.ifmc.mat.client.shared.search;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Widget;

public interface SearchDisplay {
	
	public Widget asWidget();
	public HasClickHandlers getSearchButton();
	public HasValue<String> getSearchString();
	public int getPageSize();
	public HasPageSizeSelectionHandler getPageSizeSelectionTool();
	public HasPageSelectionHandler getPageSelectionTool();

}
