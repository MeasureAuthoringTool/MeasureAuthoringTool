package mat.client.codelist;

import java.util.List;

import mat.client.measure.TransferMeasureOwnerShipModel.Result;
import mat.client.shared.search.SearchResults;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class TransferOwnerShipModel implements SearchResults<TransferOwnerShipModel.Result>,
		IsSerializable {
	public static class Result implements IsSerializable {
		private String key;
		private String firstName;
		private String lastName;
		private String emailId;
		private boolean isSelected;
		public String getEmailId() {
			return emailId;
		}
		public void setEmailId(String emailId) {
			this.emailId = emailId;
		}
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
		public boolean isSelected() {
			return isSelected;
		}
		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}
	}
	
	private static String[] headers = new String[] { "Name", "email address", "Select" };
	private static String[] widths = new String[] { "45%", "45%" ,"10%"};
	
	
	private List<Result> data;
	private int startIndex;
	private int resultsTotal;
	private String selectedItem;
	

	public List<Result> getData() {
		return data;
	}

	public void setData(List<Result> data) {
		this.data = data;
	}
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	@Override
	public int getNumberOfColumns() {
		return 3;
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
	
	public void setSelectedItem(String selectedItem) {
		this.selectedItem = selectedItem;
	}

	public String getSelectedItem() {
		return selectedItem;
	}
	
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
			value = getSelectedRadioBox(data,row); 
			value.addStyleName("searchTableCenteredHolder");
			break;
		default: 
			value = new Label("Unknown Column Index");
		}
		return value;
	}
	
	private RadioButton getSelectedRadioBox(final List<Result> data,final int row){
		final RadioButton transferRB = new RadioButton("selected",null,false);
		transferRB.getElement().removeChild(transferRB.getElement().getLastChild());// removing empty label
		NodeList<Element> inputs =  transferRB.getElement().getElementsByTagName("input");
		for (int i = 0; i < inputs.getLength(); i++) {
			inputs.getItem(i).removeAttribute("id");
			inputs.getItem(i).setAttribute("title", "Select to Transfer Record");
		}
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
