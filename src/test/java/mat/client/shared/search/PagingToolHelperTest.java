package mat.client.shared.search;

import com.google.gwt.user.client.ui.Widget;
import junit.framework.TestCase;
import org.junit.Test;

public class PagingToolHelperTest extends TestCase {
	private static class SearchResultsForTest implements SearchResults<String> {
		private int startIndex;
		private int resultsTotal;
		private int numberOfRows;
		
		void setNumberOfRows(int num) {
			numberOfRows = num;
		}
		void setStartIndex(int i) {
			startIndex = i;
		}
		void setResultsTotal(int count) {
			resultsTotal = count;
		}
		
		@Override
		public boolean isColumnFiresSelection(int columnIndex) {
			return false;
		}
		@Override
		public int getStartIndex() {
			return startIndex;
		}
		@Override
		public int getResultsTotal() {
			return resultsTotal;
		}
		@Override
		public int getNumberOfColumns() {
			return 0;
		}
		@Override
		public int getNumberOfRows() {
			return numberOfRows;
		}
		@Override
		public String get(int row) {
			return null;
		}
		@Override
		public String getKey(int row) {
			return null;
		}
		@Override
		public String getColumnHeader(int columnIndex) {
			return null;
		}
		@Override
		public boolean isColumnSortable(int columnIndex) {
			return false;
		}
		@Override
		public String getColumnWidth(int columnIndex) {
			return null;
		}
		@Override
		public Widget getValue(int row, int column) {
			return null;
		}
		@Override
		public boolean isColumnSelectAll(int columnIndex) {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	@Test
	public void testFirstPageOnePageTotalFullPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(10);
		searchResults.setStartIndex(1);
		searchResults.setNumberOfRows(10);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 1);
		assertTrue(helper.getNumberOfPages() == 1);
		assertFalse(helper.hasNextPage());
		assertFalse(helper.hasPreviousPage());
	}
	
	@Test
	public void testFirstPageOnePageTotalPartialPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(10);
		searchResults.setStartIndex(1);
		searchResults.setNumberOfRows(5);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 1);
		assertTrue(helper.getNumberOfPages() == 1);
		assertFalse(helper.hasNextPage());
		assertFalse(helper.hasPreviousPage());
	}
	
	@Test
	public void testFirstPageManyPagesTotalFullPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(11);
		searchResults.setStartIndex(1);
		searchResults.setNumberOfRows(10);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 1);
		assertTrue(helper.getNumberOfPages() == 2);
		assertTrue(helper.hasNextPage());
		assertFalse(helper.hasPreviousPage());
	}
	
	@Test
	public void testSecondPageTwoPagesTotalFullPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(20);
		searchResults.setStartIndex(11);
		searchResults.setNumberOfRows(10);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 2);
		assertTrue(helper.getNumberOfPages() == 2);
		assertFalse(helper.hasNextPage());
		assertTrue(helper.hasPreviousPage());
	}
	
	@Test
	public void testSecondPageTwoPagesTotalPartialPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(17);
		searchResults.setStartIndex(11);
		searchResults.setNumberOfRows(7);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 2);
		assertTrue(helper.getNumberOfPages() == 2);
		assertFalse(helper.hasNextPage());
		assertTrue(helper.hasPreviousPage());
	}
	
	@Test
	public void testSecondPageThreePagesTotalPartialPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(27);
		searchResults.setStartIndex(11);
		searchResults.setNumberOfRows(10);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 2);
		assertTrue(helper.getNumberOfPages() == 3);
		assertTrue(helper.hasNextPage());
		assertTrue(helper.hasPreviousPage());
	}
	
	@Test
	public void testSecondPageTwoTotalSingleRecordOnLastPage() {
		SearchResultsForTest searchResults = new SearchResultsForTest();
		searchResults.setResultsTotal(11);
		searchResults.setStartIndex(11);
		searchResults.setNumberOfRows(10);
		
		int pageSize = 10;
		PagingToolHelper helper = new PagingToolHelper(searchResults, pageSize);
		
		assertTrue(helper.getCurrentPage() == 2);
		assertTrue(helper.getNumberOfPages() == 2);
		assertFalse(helper.hasNextPage());
		assertTrue(helper.hasPreviousPage());
	}
}
