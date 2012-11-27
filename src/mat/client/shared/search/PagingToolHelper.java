package mat.client.shared.search;

public class PagingToolHelper {
	private SearchResults<?> results;
	private int currentPageSize;
	
	public PagingToolHelper(SearchResults<?> results, int currentPageSize) {
		this.results = results;
		this.currentPageSize = currentPageSize;
	}

	public int getCurrentPage() {
		int currentPage = results.getStartIndex() / currentPageSize + 1;
		return currentPage;
	}
	
	public int getNumberOfPages() {
		int numberOfPages = results.getResultsTotal() / currentPageSize;
		if(results.getResultsTotal() % currentPageSize > 0) {
			numberOfPages++;
		}
		return numberOfPages;
	}
	
	public boolean hasPreviousPage() {
		return getCurrentPage() != 1;
	}
	
	public boolean hasNextPage() {
		return getCurrentPage() != getNumberOfPages();
	}
}
