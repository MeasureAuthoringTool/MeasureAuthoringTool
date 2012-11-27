package org.ifmc.mat.client.shared.search;

class PagingToolHelper {
	private SearchResults<?> results;
	private int currentPageSize;
	
	PagingToolHelper(SearchResults<?> results, int currentPageSize) {
		this.results = results;
		this.currentPageSize = currentPageSize;
	}

	int getCurrentPage() {
		int currentPage = results.getStartIndex() / currentPageSize + 1;
		return currentPage;
	}
	
	int getNumberOfPages() {
		int numberOfPages = results.getResultsTotal() / currentPageSize;
		if(results.getResultsTotal() % currentPageSize > 0) {
			numberOfPages++;
		}
		return numberOfPages;
	}
	
	boolean hasPreviousPage() {
		return getCurrentPage() != 1;
	}
	
	boolean hasNextPage() {
		return getCurrentPage() != getNumberOfPages();
	}
}
