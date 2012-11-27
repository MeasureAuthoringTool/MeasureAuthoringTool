package org.ifmc.mat.client.codelist;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ifmc.mat.client.measure.metadata.CustomCheckBox;
import org.ifmc.mat.client.shared.search.PagingFacade;
import org.ifmc.mat.model.GroupedCodeListDTO;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

class ManageGroupCodeListsModel extends PagingFacade<GroupedCodeListDTO> implements IsSerializable{

	private static String[] headers = new String[] {"Select","Value Set","Descriptor"};
	private static String[] widths = new String[] { "5%", "30%", "50%"};
	private HashMap<GroupedCodeListDTO, CustomCheckBox> checkboxMap = 
		new HashMap<GroupedCodeListDTO, CustomCheckBox>();
	
	public ManageGroupCodeListsModel(List<GroupedCodeListDTO> codesData,boolean value) {
		super(codesData);
		for(GroupedCodeListDTO dto : codesData) {
			CustomCheckBox cb = new CustomCheckBox("Select Value Set",false);
			cb.setValue(value);
			checkboxMap.put(dto, cb);
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
	public String getKey(GroupedCodeListDTO dataObject) {
		return dataObject.getId();
	}

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

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}
	
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

	
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return (columnIndex==0);
	}

}
