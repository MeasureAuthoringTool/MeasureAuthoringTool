package mat.client.codelist;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.shared.search.SearchResults;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageValueSetSearchModel.
 * 
 * @author aschmidt
 */
public class ManageValueSetSearchModel implements IsSerializable, SearchResults<ManageValueSetSearchModel.Result>{
	
	/**
	 * The Class Result.
	 */
	public static class Result implements IsSerializable {
		
		/** The id. */
		private String id;
		
		/** The oid. */
		private String oid;
		
		/** The name. */
		private String name;
		
		/** The last modified. */
		private Timestamp lastModified;
		
		/** The draft. */
		private boolean draft;
		
		/** The is grouped. */
		private boolean isGrouped;
		
		/**
		 * Gets the id.
		 * 
		 * @return the id
		 */
		public String getId() {
			return id;
		}
		
		/**
		 * Sets the id.
		 * 
		 * @param id
		 *            the new id
		 */
		public void setId(String id) {
			this.id = id;
		}
		
		/**
		 * Gets the oid.
		 * 
		 * @return the oid
		 */
		public String getOid() {
			return oid;
		}
		
		/**
		 * Sets the oid.
		 * 
		 * @param oid
		 *            the new oid
		 */
		public void setOid(String oid) {
			this.oid = oid;
		}
		
		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Sets the name.
		 * 
		 * @param name
		 *            the new name
		 */
		public void setName(String name) {
			this.name = name;
		}
		
		/**
		 * Gets the last modified.
		 * 
		 * @return the last modified
		 */
		public Timestamp getLastModified() {
			return lastModified;
		}
		
		/**
		 * Sets the last modified.
		 * 
		 * @param lastModified
		 *            the new last modified
		 */
		public void setLastModified(Timestamp lastModified) {
			this.lastModified = lastModified;
		}
		
		/**
		 * Checks if is draft.
		 * 
		 * @return true, if is draft
		 */
		public boolean isDraft() {
			return draft;
		}
		
		/**
		 * Sets the draft.
		 * 
		 * @param draft
		 *            the new draft
		 */
		public void setDraft(boolean draft) {
			this.draft = draft;
		}
		
		/**
		 * Checks if is grouped.
		 * 
		 * @return true, if is grouped
		 */
		public boolean isGrouped() {
			return isGrouped;
		}
		
		/**
		 * Sets the grouped.
		 * 
		 * @param isGrouped
		 *            the new grouped
		 */
		public void setGrouped(boolean isGrouped) {
			this.isGrouped = isGrouped;
		}	
		
	}
	
	/** The data. */
	private List<Result> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The page count. */
	private int pageCount;
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<Result> data) {
		this.data = data;
	}
    
    /**
	 * Gets the data.
	 * 
	 * @return the data
	 */
    public List<Result>  getData(){
    	if(data == null)
    		data = new ArrayList<Result>();
    	return data;
    }
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/**
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	public String getKey(int row) {
		return data.get(row).getId();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	public Result get(int row) {
		return data.get(row);
	}
	
	/**
	 * Gets the page count.
	 * 
	 * @return the page count
	 */
	public int getPageCount() {
		return pageCount;
	}
	
	/**
	 * Sets the page count.
	 * 
	 * @param pageCount
	 *            the new page count
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
	return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		return null;
	}
	
}

