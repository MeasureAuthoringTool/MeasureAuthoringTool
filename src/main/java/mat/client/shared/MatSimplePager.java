package mat.client.shared;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.view.client.Range;
import mat.client.CustomPager;

//public class MatSimplePager extends SimplePager {
/**
 * The Class MatSimplePager.
 */
public class MatSimplePager extends CustomPager {
	
	/**
	 * Instantiates a new mat simple pager.
	 */
	public MatSimplePager() {
		this.setRangeLimited(true);
	}

	/**
	 * Instantiates a new mat simple pager.
	 * 
	 * @param location
	 *            the location
	 * @param resources
	 *            the resources
	 * @param showFastForwardButton
	 *            the show fast forward button
	 * @param fastForwardRows
	 *            the fast forward rows
	 * @param showLastPageButton
	 *            the show last page button
	 */
	public MatSimplePager(TextLocation location, Resources resources, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton, String panelId) {
		super(location, resources, showFastForwardButton, fastForwardRows, showLastPageButton,panelId);
		this.setRangeLimited(true);
	}

	
	/* (non-Javadoc)
	 * @see mat.client.CustomPager#setPageStart(int)
	 */
	public void setPageStart(int index) {

		if (this.getDisplay() != null) {
			Range range = getDisplay().getVisibleRange();
			int pageSize = range.getLength();
			if (!isRangeLimited() && getDisplay().isRowCountExact()) {
				index = Math.min(index, getDisplay().getRowCount() - pageSize);
			}
			index = Math.max(0, index);
			if (index != range.getStart()) {
				getDisplay().setVisibleRange(index, pageSize);
			}
		}  
	}
	
	
	
	public void setPageStart(int index , int totalData) {
		  
		  if (this.getDisplay() != null) {
		   Range range = getDisplay().getVisibleRange();
		   int pageSize = range.getLength();
		   if (!isRangeLimited() && getDisplay().isRowCountExact()) {
		    index = Math.min(index, getDisplay().getRowCount() - pageSize);
		   }
		   index = Math.max(0, index);
		   if (index >= totalData) {
		    index = totalData - pageSize;
		    getDisplay().setVisibleRange(index, pageSize);
		   } else if (index != range.getStart()) {
		    getDisplay().setVisibleRange(index, pageSize);
		   }
		  }
		 }
	
	/**
	 * Custom method to add tool tip and tab index on First,Next,Previous and
	 * Last Page icons.
	 * 
	 * @param spager
	 *            the new tool tip and tab index
	 */
	
	public void setToolTipAndTabIndex(MatSimplePager spager ){
		final NodeList<Element> tdElems = spager.getElement().getElementsByTagName("td");
		for (int i = 0; i < tdElems.getLength(); i++) {
			final String toolTipText;
			if (i == 0){
				toolTipText = "First page";
				//tdElems.getItem(i).setTabIndex(0);
			}
			else if (i == 1){
				toolTipText = "Previous page";
				tdElems.getItem(i).setTabIndex(0);
			}
			else if (i == 2){
				continue;  //This is the middle td - no button 
			}
			else if (i == 3){
				toolTipText = "Next page";
				tdElems.getItem(i).setTabIndex(0);
			}
			else if (i == 4){
				toolTipText = "Last page";
				tdElems.getItem(i).setTabIndex(0);
			}
			else
				continue;
			tdElems.getItem(i).setTitle(toolTipText);
		}
		final NodeList<Element> imgElems = spager.getElement().getElementsByTagName("img");
		for (int j = 0; j < imgElems.getLength(); j++) {
			System.out.println("Image Name ::: " + imgElems.getItem(j).getId());
			Element img = imgElems.getItem(j);
			img.setId("abc");
		}

	}



}