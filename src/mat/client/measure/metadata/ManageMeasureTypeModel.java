package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.shared.search.PagingFacade;
import mat.model.MeasureType;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureTypeModel extends PagingFacade<MeasureType> implements IsSerializable{
	private static String[] headers = new String[] {"Select","Measure Type"};
	private static String[] widths = new String[] { "5%", "20%"};
	
	private HashMap<MeasureType, CustomCheckBox> checkboxMap = new HashMap<MeasureType, CustomCheckBox>();
	
	public ManageMeasureTypeModel(List<MeasureType> data) {
		super(data);
		for(MeasureType c : data) {
			CustomCheckBox cb = new CustomCheckBox("Select MeasureType",false);
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
	public String getKey(MeasureType dataObject) {
		return null;
	}

	@Override
	public Widget getValueImpl(MeasureType MeasureType, int column) {
			Widget value;
		switch(column) {
		case 0:
			value = checkboxMap.get(MeasureType);
			break;
		case 1:
			value = new Label(MeasureType.getDescription());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

	public List<MeasureType> getSelectedMeasureType() {
		List<MeasureType> retList = new ArrayList<MeasureType>();
		List<MeasureType> data = getData();
		for(int i = 0; i < data.size(); i++) {
			MeasureType MeasureType = data.get(i);
			CustomCheckBox cb = checkboxMap.get(MeasureType);
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
		return false;
	}

}

