package mat.client.shared.search;

/**
 * The Class PagingToolHelper.
 */
public class PagingToolHelper {
	
	/** The results. */
	private SearchResults<?> results;
	
	/** The current page size. */
	private int currentPageSize;
	
	/**
	 * Instantiates a new paging tool helper.
	 * 
	 * @param results
	 *            the results
	 * @param currentPageSize
	 *            the current page size
	 */
	public PagingToolHelper(SearchResults<?> results, int currentPageSize) {
		this.results = results;
		this.currentPageSize = currentPageSize;
	}

	/**
	 * Gets the current page.
	 * 
	 * @return the current page
	 */
	public int getCurrentPage() {
		int currentPage = results.getStartIndex() / currentPageSize + 1;
		return currentPage;
	}
	
	/**
	 * Gets the number of pages.
	 * 
	 * @return the number of pages
	 */
	public int getNumberOfPages() {
		int numberOfPages = results.getResultsTotal() / currentPageSize;
		if(results.getResultsTotal() % currentPageSize > 0) {
			numberOfPages++;
		}
		return numberOfPages;
	}
	
	/**
	 * Checks for previous page.
	 * 
	 * @return true, if successful
	 */
	public boolean hasPreviousPage() {
		return getCurrentPage() != 1;
	}
	
	/**
	 * Checks for next page.
	 * 
	 * @return true, if successful
	 */
	public boolean hasNextPage() {
		return getCurrentPage() != getNumberOfPages();
	}
}
