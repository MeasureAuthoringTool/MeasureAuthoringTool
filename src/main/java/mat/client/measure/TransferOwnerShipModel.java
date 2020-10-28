package mat.client.measure;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.SearchResults;

import java.util.List;

/**
 * The Class TransferMeasureOwnerShipModel.
 */
public class TransferOwnerShipModel implements SearchResults<TransferOwnerShipModel.Result>,
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
		
		/** The email id. */
		private String emailId;
		
		/** The is selected. */
		private boolean isSelected;
		
		/**
		 * Gets the email id.
		 * 
		 * @return the email id
		 */
		public String getEmailId() {
			return emailId;
		}
		
		/**
		 * Sets the email id.
		 * 
		 * @param emailId
		 *            the new email id
		 */
		public void setEmailId(String emailId) {
			this.emailId = emailId;
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
		 * Sets the key.
		 * 
		 * @param key
		 *            the new key
		 */
		public void setKey(String key) {
			this.key = key;
		}
		
		/**
		 * Gets the first name.
		 * 
		 * @return the first name
		 */
		public String getFirstName() {
			return firstName;
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
		 * Gets the last name.
		 * 
		 * @return the last name
		 */
		public String getLastName() {
			return lastName;
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
		 * Checks if is selected.
		 * 
		 * @return true, if is selected
		 */
		public boolean isSelected() {
			return isSelected;
		}
		
		/**
		 * Sets the selected.
		 * 
		 * @param isSelected
		 *            the new selected
		 */
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
	}
	
	/** The headers. */
	private static String[] headers = new String[] { "Name", "email address", "Select" };
	
	/** The widths. */
	private static String[] widths = new String[] { "45%", "45%" ,"10%"};
	
	
	/** The data. */
	private List<Result> data;
	
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The selected item. */
	private String selectedItem;
	

	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<Result> getData() {
		return data;
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
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return 3;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
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

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
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
	@Override
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
	@Override
	public String getKey(int row) {
		return data.get(row).getKey();
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public Result get(int row) {
		return data.get(row);
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
	/**
	 * Sets the selected item.
	 * 
	 * @param selectedItem
	 *            the new selected item
	 */
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	/**
	 * Gets the selected item.
	 * 
	 * @return the selected item
	 */
	public String getSelectedItem() {
		return selectedItem;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		Widget value;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getFirstName() + " " + 
					data.get(row).getLastName());
			break;
		case 1:
			value = new Label(data.get(row).getEmailId());
			break;
		case 2:
			value = getSelectedRadioBox(data, row); 
			value.addStyleName("searchTableCenteredHolder");
			break;
		default: 
			value = new Label("Unknown Column Index");
		}
		return value;
	}
	
	/**
	 * Gets the selected radio box.
	 * 
	 * @param data
	 *            the data
	 * @param row
	 *            the row
	 * @return the selected radio box
	 */
	private RadioButton getSelectedRadioBox(final List<Result> data,final int row){
		final RadioButton transferRB = new RadioButton("selected",null,false);
		/** 508 Fix - Radio button with no label should not have empty label tag and radio button should not have Id attribute. 
	     Instead title should be set.**/
		transferRB.getElement().getFirstChildElement().removeAttribute("id");
		transferRB.getElement().removeChild(transferRB.getElement().getLastChild());
		transferRB.setTitle("Select to Transfer Ownership.");
		transferRB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> e) {
                if(e.getValue() == true)
                {
                    //result.setSelected(true);
                	/**
                	 * Reset all radio buttons to false
                	 */
                    for(Result result:data){
                    	result.setSelected(false);
                    }
                    //Set the one just selected to true
                    data.get(row).setSelected(true);
                }
//                else
//                {
//                	result.setSelected(false);
//                }
               }
			} 
         );
		return transferRB;
	}
}
