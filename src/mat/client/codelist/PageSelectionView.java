/**
 * 
 */
package mat.client.codelist;

import mat.client.shared.MatContext;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.PageSelectionEvent;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.shared.ConstantMessages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class PageSelectionView.
 * 
 * @param <T>
 *            the generic type
 * @author vandavar
 */
@SuppressWarnings("rawtypes")
public class PageSelectionView<T> implements HasSelectionHandlers<T>, HasPageSelectionHandler{
	
	
	/** The page selector. */
	protected Panel pageSelector = new HorizontalPanel();
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The Constant DEFAULT_PAGE. */
	public static final int DEFAULT_PAGE = 1;
	
	/** The current page. */
	private int currentPage = DEFAULT_PAGE;
	
	/* (non-Javadoc)
	 * @see com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.HasPageSelectionHandler#addPageSelectionHandler(mat.client.shared.search.PageSelectionEventHandler)
	 */
	@Override
	public HandlerRegistration addPageSelectionHandler(
			PageSelectionEventHandler handler) {
		return handlerManager.addHandler(PageSelectionEvent.getType(), handler);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<T> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	/**
	 * As widget.
	 * 
	 * @return the widget
	 */
	public Widget asWidget() {
		return pageSelector;
	}
	
	/**
	 * Build Page Selector links for a given page count.
	 * 
	 * @param pageCount
	 *            the page count
	 * @return the widget
	 */
	public Widget buildPageSelector(int pageCount) {
		if(pageCount > 1){
			pageSelector.clear();
			pageSelector.getElement().setAttribute("id", "ManageCodesPagination");
			pageSelector.getElement().setAttribute("aria-role", "textbox");
			pageSelector.getElement().setAttribute("aria-labelledby", "LiveRegion");
			pageSelector.getElement().setAttribute("aria-live", "assertive");
			pageSelector.getElement().setAttribute("aria-atomic", "true");
			pageSelector.getElement().setAttribute("aria-relevant", "all");
			pageSelector.getElement().setAttribute("role", "alert");
			pageSelector.add(mat.client.shared.LabelBuilder.buildInvisibleLabel(new Label(), "Pagination Added Page"));
			int maxDisplay = ConstantMessages.MAX_PAGE_DISPLAY; 
			int start = (int) (currentPage/maxDisplay) * 10;
	
			if((currentPage % maxDisplay) > 0){
				start++;
			}
			start = (start > maxDisplay)?start:1; // default to 1 if less than max display
	
			int end = start + (maxDisplay - 1);
	
			if(end > pageCount){ //reset to max display
				end = pageCount;
			}
			
			if(start > maxDisplay){
				pageSelector.add(buildPageLink(0, "First"));
				pageSelector.add(new HTML("&nbsp;&nbsp;"));			
			}
			
			//add for previous
			if(currentPage!=1){
				pageSelector.add(buildPageLink(-9, "Prev"));
			}
			pageSelector.add(new HTML("&nbsp;&nbsp;"));
	
				
			for(int i = start; i <= end; i++) {			
				pageSelector.add(buildPageLink(i));
				if(i != end){
					//pageSelector.add(new HTML("&nbsp;|&nbsp;"));
					pageSelector.add(new HTML("&nbsp;"));
				}else{
					pageSelector.add(new HTML("&nbsp;"));
				}
			}
			
			if(pageCount > end ){
				int dots = pageCount - end;
				if(dots > ConstantMessages.PAGE_DOTS_DISPLAY){
					dots = ConstantMessages.PAGE_DOTS_DISPLAY;
				}
				for(int j=1; j <= dots; j++){
					end++;
					pageSelector.add(buildPageLink(end, "."));
					pageSelector.add(new HTML("&nbsp;"));
				}
	
				//add last page
				pageSelector.add(buildPageLink(pageCount));
				pageSelector.add(new HTML("&nbsp;"));			
			}
			if(currentPage != pageCount){
				//add for next
				pageSelector.add(buildPageLink(-1, "Next"));
				pageSelector.add(new HTML("&nbsp;&nbsp;"));
			
				//add for last
				pageSelector.add(buildPageLink(pageCount, "Last"));
				pageSelector.add(new HTML("&nbsp;"));
			}
	
			return pageSelector;
		}else{
			//Show the pageSelector only when there is more than one page.
			pageSelector.clear();
			return null;
		}
	}
	
	/**
	 * Builds the page link.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @return the widget
	 */
	private Widget buildPageLink(final int pageNumber) {
		return buildPageLink(pageNumber, null);
	}

	/**
	 * Builds the page link.
	 * 
	 * @param pageNumber
	 *            the page number
	 * @param mnemonic
	 *            the mnemonic
	 * @return the widget
	 */
	private Widget buildPageLink(final int pageNumber, String mnemonic) {
		
		Widget widget = null;
		String label = null;
		
		if(mnemonic != null){
			if(mnemonic.equalsIgnoreCase("Next")){
				label = "Next>";
			}else if(mnemonic.equalsIgnoreCase("Last")){
				label = "Last>>";
			}else if(mnemonic.equalsIgnoreCase("First")){
				label = "<<First";
			}else if(mnemonic.equalsIgnoreCase("Prev")){
				label = "<Prev";
			}else{
				label = ".";
			}
			widget = buildAnchorForPageSelection(label, pageNumber, mnemonic);
		}else{
			if(currentPage != pageNumber) {
				label = Integer.toString(pageNumber);
				widget = buildAnchorForPageSelection(label, pageNumber, mnemonic);
			}else {
				label = Integer.toString(pageNumber);
				Widget w = new HTML("<b>" + label + "</b>");
				w.setStyleName("page-numbers-current");
				FocusPanel fp = new FocusPanel();
				fp.add(w);
				fp.setTitle("Currently displaying the page "+label+" records.");
				return fp;
			}
		}
		return widget;
	}
	
	/**
	 * Builds the anchor for page selection.
	 * 
	 * @param label
	 *            the label
	 * @param pageNumber
	 *            the page number
	 * @param mnemonic
	 *            the mnemonic
	 * @return the anchor
	 */
	private Anchor buildAnchorForPageSelection(String label, final int pageNumber, String mnemonic){
		Anchor a = new Anchor(label);
		a.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				MatContext.get().clearDVIMessages();
				//currentPage = pageNumber;
				//
				// if we are switching the page size to all,
				// set the page size and fire an event to switch
				// to the first page
				//				
				PageSelectionEvent evt = new PageSelectionEvent(pageNumber);
				PageSelectionView.this.fireEvent(evt);
			}
		});
		if(mnemonic != null){
			a.setTitle("Display the page \""+mnemonic+"\" records.");
		}else{
			a.setTitle("Display the page "+pageNumber+" records.");
		}
		if(!label.equalsIgnoreCase(".")){
			a.setStyleName("page-numbers");
		}
		return a;
	}
	
	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * Sets the current page.
	 * 
	 * @param pageNumber
	 *            the new current page
	 */
	public void setCurrentPage(int pageNumber) {
		currentPage = pageNumber;
	}
	
}
