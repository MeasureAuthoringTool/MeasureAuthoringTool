package mat.client.admin;

import java.util.List;

import mat.client.shared.search.SearchResults;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageUsersSearchModel implements SearchResults<ManageUsersSearchModel.Result>,
		IsSerializable {
	public static class Result implements IsSerializable {
		private String key;
		private String firstName;
		private String lastName;
		private String orgName;
		private String loginId;
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}

		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		/**
		 * @param loginId the loginId to set
		 */
		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}
		/**
		 * @return the loginId
		 */
		public String getLoginId() {
			return loginId;
		}
		
	}
	
	private static String[] headers = new String[] { "Name", "Organization" };
	private static String[] widths = new String[] { "50%", "50%" };

	private List<Result> data;
	private int startIndex;
	private int resultsTotal;
	
	public void setData(List<Result> data) {
		this.data = data;
	}
	
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	@Override
	public int getNumberOfColumns() {
		return 2;
	}

	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}


	@Override
	public Widget getValue(int row, int column) {
		Label value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getFirstName() + " " + 
				data.get(row).getLastName());
			break;
		case 1:
			value = new Label(data.get(row).getOrgName());
			break;
		default: 
			value = new Label("Unknown Column Index");
		}
		return value;
	}

	@Override
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	

	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}


	@Override
	public String getKey(int row) {
		return data.get(row).getKey();
	}


	@Override
	public Result get(int row) {
		return data.get(row);
	}


	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

}
