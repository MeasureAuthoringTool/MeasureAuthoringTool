package mat.client.measure.metadata;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.search.PagingFacade;
import mat.model.Author;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Class ManageAuthorsModel.
 */
public class ManageAuthorsModel extends PagingFacade<Author> implements IsSerializable{
	
	/** The headers. */
	private static String[] headers = new String[] {"Select","Measure Developer"};
	
	/** The widths. */
	private static String[] widths = new String[] { "1%", "5%"};
	
	/** The checkbox map. */
	private HashMap<Author, CustomCheckBox> checkboxMap = new HashMap<Author, CustomCheckBox>();
	
	/**
	 * Instantiates a new manage authors model.
	 * 
	 * @param data
	 *            the data
	 */
	public ManageAuthorsModel(List<Author> data) {
		super(data);
		for(Author c : data) {
			CustomCheckBox cb = new CustomCheckBox("Select Measure Developer",false);
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
	public String getKey(Author dataObject) {
		return null;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
	@Override
	public Widget getValueImpl(Author author, int column) {
			Widget value;
		switch(column) {
		case 0:
			value = checkboxMap.get(author);
			break;
		case 1:
			value = new Label(author.getAuthorName());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}

		

	
	/**
	 * Gets the selected author.
	 * 
	 * @return the selected author
	 */
	public List<Author> getSelectedAuthor() {
		List<Author> retList = new ArrayList<Author>();
		List<Author> data = getData();
		for(int i = 0; i < data.size(); i++) {
			Author author = data.get(i);
			CustomCheckBox cb = checkboxMap.get(author);
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
