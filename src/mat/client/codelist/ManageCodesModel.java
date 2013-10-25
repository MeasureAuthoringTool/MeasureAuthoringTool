package mat.client.codelist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.search.PagingFacade;
import mat.model.Code;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageCodesModel.
 */
public class ManageCodesModel extends PagingFacade<Code> implements IsSerializable{

	/** The headers. */
	private static String[] headers = new String[] {"Select","Code","Descriptor"};
	
	/** The widths. */
	private static String[] widths = new String[] { "5%", "30%", "50%"};
	
	/** The checkbox map. */
	private HashMap<Code, CustomCheckBox> checkboxMap = new HashMap<Code, CustomCheckBox>();
	
	/**
	 * Instantiates a new manage codes model.
	 * 
	 * @param data
	 *            the data
	 * @param value
	 *            the value
	 */
	public ManageCodesModel(List<Code> data,boolean value) {
		super(data);
		for(Code c : data) {
			    CustomCheckBox cb = new CustomCheckBox("Select Code " + c.getCode(),false);
			    cb.setValue(value);
				checkboxMap.put(c, cb);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(Code dataObject) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
	@Override
	public Widget getValueImpl(Code code, int column) {
			Widget value;
		switch(column) {
		case 0:
			value = checkboxMap.get(code);
			break;
		case 1:
			value = new Label(code.getCode());
			break;
		case 2:
			value = new Label(code.getDescription());
			break;
     	
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

		

	
	/**
	 * Gets the selected codes.
	 * 
	 * @return the selected codes
	 */
	public List<Code> getSelectedCodes() {
		List<Code> retList = new ArrayList<Code>();
		List<Code> data = getData();
		for(int i = 0; i < data.size(); i++) {
			Code code = data.get(i);
			CustomCheckBox cb = checkboxMap.get(code);
			if(cb.getValue().equals(Boolean.TRUE)) {
				retList.add(get(i));
			}
		}
		return retList;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return (columnIndex==0);
	}
	

}
