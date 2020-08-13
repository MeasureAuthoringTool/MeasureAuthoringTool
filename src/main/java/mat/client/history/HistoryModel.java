package mat.client.history;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.audit.AuditLogWidget;
import mat.client.shared.search.PagingFacade;
import mat.dto.AuditLogDTO;

import java.util.List;

/**
 * The Class HistoryModel.
 */
public class HistoryModel extends PagingFacade<AuditLogDTO> implements IsSerializable{

	/** The headers. */
	private static String[] headers = new String[] {"Log Entry"};
	
	/** The widths. */
	private static String[] widths = new String[] { "100%"};
	
	/**
	 * Instantiates a new history model.
	 * 
	 * @param data
	 *            the data
	 */
	public HistoryModel(List<AuditLogDTO> data) {
		super(data);		
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
	public String getKey(AuditLogDTO dataObject) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
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
