/**
 * 
 */
package mat.client.codelist;

import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.model.Code;

/**
 * @author vandavar 
 * A subclass for searchview to handle the ManageCodes Differently without view size
 *
 */
public class ManageCodesSearchView extends SearchView<Code> {
	
	private int DEFAULT_PAGE_SIZE = 50;
	
	//build data table for manage codes search
	//Depend on the manageCodeListDetailModel for the totalResults and totalPages
	//Same method is used to build both the codessummaryView and  manageCodes
	public void buildManageCodesDataTable(final SearchResults results, boolean isAscending,boolean isChecked,int totalNumberofCodes,
			int totalPages,int currentPage){
		if(results == null) {
			return;
		}
		int numRows = results.getNumberOfRows();
		int numColumns = results.getNumberOfColumns();
		dataTable.clear();
		dataTable.resize((int)numRows + 1, (int)numColumns);
		dataTable.getElement().setAttribute("id", "ManageCodeTable");
		dataTable.getElement().setAttribute("aria-role", "grid");
		dataTable.getElement().setAttribute("aria-live", "assertive");
		dataTable.getElement().setAttribute("aria-atomic", "true");
		dataTable.getElement().setAttribute("aria-relevant", "all");
		dataTable.getElement().setAttribute("role", "alert");
		buildSearchResultsColumnHeaders(numRows,numColumns,results, isAscending,isChecked);
		buildSearchResults(numRows,numColumns,results);
		int viewStartNumber = findViewingNumber(DEFAULT_PAGE_SIZE,currentPage);
		setViewingRange2(viewStartNumber, viewStartNumber + numRows - 1, totalNumberofCodes);
    }
	
	
	
	
	@Override
	public int getPageSize(){
		return DEFAULT_PAGE_SIZE;
	}

}
