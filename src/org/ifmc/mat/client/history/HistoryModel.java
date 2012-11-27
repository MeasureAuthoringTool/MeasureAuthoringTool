package org.ifmc.mat.client.history;


import java.util.List;

import org.ifmc.mat.DTO.AuditLogDTO;
import org.ifmc.mat.client.audit.AuditLogWidget;
import org.ifmc.mat.client.shared.search.PagingFacade;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HistoryModel extends PagingFacade<AuditLogDTO> implements IsSerializable{

	private static String[] headers = new String[] {"Log Entry"};
	private static String[] widths = new String[] { "100%"};
	
	public HistoryModel(List<AuditLogDTO> data) {
		super(data);		
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
	public String getKey(AuditLogDTO dataObject) {
		return null;
	}

	@Override
	public Widget getValueImpl(AuditLogDTO auditLogDTO, int column) {
		//System.out.println("COLUMN:" + column);
		Widget value;
		String textValue = null;
		switch(column) {
		case 0:
			/*textValue = auditLogDTO.getActivityType();
			value = new Label(textValue);
			System.out.println("VALUE:" + textValue);*/
			value = new AuditLogWidget(auditLogDTO.getActivityType(), 
												auditLogDTO.getUserId(), 
												auditLogDTO.getEventTs(), 
												auditLogDTO.getAdditionlInfo());
			break;
		case 1:
			textValue = auditLogDTO.getUserId();
			value = new Label(textValue);
			break;			
		case 2:
			textValue = String.valueOf(auditLogDTO.getEventTs());
			value = new Label(textValue);
			break;
		case 3:
			textValue = String.valueOf(auditLogDTO.getAdditionlInfo());
			value = new Label(textValue);
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

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

}
