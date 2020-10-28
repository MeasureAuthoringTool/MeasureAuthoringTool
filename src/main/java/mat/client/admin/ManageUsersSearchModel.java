package mat.client.admin;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.SearchResults;

import java.util.List;

/**
 * The Class ManageUsersSearchModel.
 */
public class ManageUsersSearchModel implements SearchResults<ManageUsersSearchModel.Result>,
		IsSerializable {
	
	/**
	 * The Class Result.
	 */
	public static class Result implements IsSerializable {
		
		/** The key. */
		private String key;
		
		/** The first name. */
		private String firstName;
		
		/** The last name. */
		private String lastName;
		
		/** The org name. */
		private String orgName;
		
		/** The login id. */
		private String loginId;
		
		/** The status. */
		private String status;
		
		/** The user type. */
		private String userRole;
			
		
		/**
		 * Gets the first name.
		 * 
		 * @return the first name
		 */
		public String getFirstName() {
			return firstName;
		}

		/**
		 * Gets the key.
		 * 
		 * @return the key
		 */
		public String getKey() {
			return key;
		}

		/**
		 * Gets the last name.
		 * 
		 * @return the last name
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * Gets the login id.
		 * 
		 * @return the loginId
		 */
		public String getLoginId() {
			return loginId;
		}

		/**
		 * Gets the org name.
		 * 
		 * @return the org name
		 */
		public String getOrgName() {
			return orgName;
		}
		
		/**
		 * @return the status
		 */
		public String getStatus() {
			return status;
		}

		public String getUserRole() {
			return userRole;
		}
		
		/**
		 * Sets the first name.
		 * 
		 * @param firstName
		 *            the new first name
		 */
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		
		/**
		 * Sets the key.
		 * 
		 * @param key
		 *            the new key
		 */
		public void setKey(String key) {
			this.key = key;
		}
		
		/**
		 * Sets the last name.
		 * 
		 * @param lastName
		 *            the new last name
		 */
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		/**
		 * Sets the login id.
		 * 
		 * @param loginId
		 *            the loginId to set
		 */
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		
		/**
		 * Sets the org name.
		 * 
		 * @param orgName
		 *            the new org name
		 */
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		
		/**
		 * @param status the status to set
		 */
		public void setStatus(String status) {
			this.status = status;
		}

		public void setUserRole(String userRole) {
			this.userRole = userRole;
		}

	}

	/** The headers. */
	private static String[] headers = new String[] {"Name", "Organization"};
	
	/** The widths. */
	private static String[] widths = new String[] {"50%", "50%"};

	/** The data. */
	private List<Result> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public Result get(int row) {
		return data.get(row);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	/**
	 * Gets the data list.
	 *
	 * @return the data list
	 */
	public List<Result> getDataList() {
		return data;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return data.get(row).getKey();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return 2;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		Label value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getFirstName() + " "
						+ data.get(row).getLastName());
			break;
		case 1:
			value = new Label(data.get(row).getOrgName());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

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
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
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
}
