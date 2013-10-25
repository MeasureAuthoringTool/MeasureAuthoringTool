package mat.client.codelist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.search.PagingFacade;
import mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageGroupCodeListsModel.
 */
class ManageGroupCodeListsModel extends PagingFacade<GroupedCodeListDTO> implements IsSerializable{

	/** The headers. */
	private static String[] headers = new String[] {"Select","Value Set","Descriptor"};
	
	/** The widths. */
	private static String[] widths = new String[] { "5%", "30%", "50%"};
	
	/** The checkbox map. */
	private HashMap<GroupedCodeListDTO, CustomCheckBox> checkboxMap = 
		new HashMap<GroupedCodeListDTO, CustomCheckBox>();
	
	/**
	 * Instantiates a new manage group code lists model.
	 * 
	 * @param codesData
	 *            the codes data
	 * @param value
	 *            the value
	 */
	public ManageGroupCodeListsModel(List<GroupedCodeListDTO> codesData,boolean value) {
		super(codesData);
		for(GroupedCodeListDTO dto : codesData) {
			CustomCheckBox cb = new CustomCheckBox("Select Value Set",false);
			cb.setValue(value);
			checkboxMap.put(dto, cb);
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
	public String getKey(GroupedCodeListDTO dataObject) {
		return dataObject.getId();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
	@Override
	public Widget getValueImpl(GroupedCodeListDTO dataObject, int column) {
		Widget value;
		switch(column) {
		case 0:
			value = checkboxMap.get(dataObject);
			break;
		case 1:
			value = new Label(dataObject.getName());
			break;
		case 2:
			value = new Label(dataObject.getDescription());
			break;
     	
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}
	
	/**
	 * Gets the selected.
	 * 
	 * @return the selected
	 */
	public List<GroupedCodeListDTO> getSelected() {
		List<GroupedCodeListDTO> retList = new ArrayList<GroupedCodeListDTO>();
		List<GroupedCodeListDTO> data = getData();
		for(int i = 0; i < data.size(); i++) {
			GroupedCodeListDTO dto = data.get(i);
			CustomCheckBox cb = checkboxMap.get(dto);
			if(cb.getValue().equals(Boolean.TRUE)) {
				retList.add(get(i));
			}
		}
		return retList;
	}

	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return (columnIndex==0);
	}

}
