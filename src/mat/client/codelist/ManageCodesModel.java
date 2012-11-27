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

public class ManageCodesModel extends PagingFacade<Code> implements IsSerializable{

	private static String[] headers = new String[] {"Select","Code","Descriptor"};
	private static String[] widths = new String[] { "5%", "30%", "50%"};
	
	private HashMap<Code, CustomCheckBox> checkboxMap = new HashMap<Code, CustomCheckBox>();
	
	public ManageCodesModel(List<Code> data,boolean value) {
		super(data);
		for(Code c : data) {
			    CustomCheckBox cb = new CustomCheckBox("Select Code",false);
			    cb.setValue(value);
				checkboxMap.put(c, cb);
		}
		
	}
	
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public String getKey(Code dataObject) {
		return null;
	}

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

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return (columnIndex==0);
	}
	

}
