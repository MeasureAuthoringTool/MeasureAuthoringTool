package mat.client.measure;

import java.util.List;

import mat.client.shared.search.PagingFacade;
import mat.model.History;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageMeasureHistoryModel extends PagingFacade<History> implements IsSerializable{
	private static String[] headers = new String[] {"Log Entry"};
	private static String[] widths = new String[] { "50%"};
	
	
	public ManageMeasureHistoryModel(List<History> historyData){
		super(historyData);
	}


	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}


	@Override
	public String getKey(History dataObject) {
		// TODO Auto-generated method stub
		return null;
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
	public boolean isColumnFiresSelection(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}


	@Override
	public Widget getValueImpl(History dataObject, int column) {
		Widget value;
	    switch(column){
	    case 0:
	    	value = new Label( "This is the measure history log");
	    	break;
	    default:
	       value = new Label("");
	    }
		return value;
	}
}
