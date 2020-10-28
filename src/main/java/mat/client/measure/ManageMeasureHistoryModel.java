package mat.client.measure;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.PagingFacade;
import mat.model.History;

import java.util.List;

/**
 * The Class ManageMeasureHistoryModel.
 */
public class ManageMeasureHistoryModel extends PagingFacade<History> implements IsSerializable{
	
	/** The headers. */
	private static String[] headers = new String[] {"Log Entry"};
	
	/** The widths. */
	private static String[] widths = new String[] { "50%"};
	
	
	/**
	 * Instantiates a new manage measure history model.
	 * 
	 * @param historyData
	 *            the history data
	 */
	public ManageMeasureHistoryModel(List<History> historyData){
		super(historyData);
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(History dataObject) {
		// TODO Auto-generated method stub
		return null;
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
	 * @see mat.client.shared.search.PagingFacade#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		// TODO Auto-generated method stub
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
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
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
