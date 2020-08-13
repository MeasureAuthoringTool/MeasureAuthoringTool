package mat.client.measure.metadata;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.PagingFacade;
import mat.model.MeasureType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class ManageMeasureTypeModel.
 */
public class ManageMeasureTypeModel extends PagingFacade<MeasureType> implements IsSerializable{
	
	/** The headers. */
	private static String[] headers = new String[] {"Select","Measure Type"};
	
	/** The widths. */
	private static String[] widths = new String[] { "5%", "20%"};
	
	/** The checkbox map. */
	private HashMap<MeasureType, CustomCheckBox> checkboxMap = new HashMap<MeasureType, CustomCheckBox>();
	
	/**
	 * Instantiates a new manage measure type model.
	 * 
	 * @param data
	 *            the data
	 */
	public ManageMeasureTypeModel(List<MeasureType> data) {
		super(data);
		for(MeasureType c : data) {
			CustomCheckBox cb = new CustomCheckBox("Select MeasureType",false);
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
	public String getKey(MeasureType dataObject) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
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

	/**
	 * Gets the selected measure type.
	 * 
	 * @return the selected measure type
	 */
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
		return false;
	}

}

