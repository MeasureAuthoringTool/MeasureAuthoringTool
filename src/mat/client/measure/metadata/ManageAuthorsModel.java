package mat.client.measure.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mat.client.shared.search.PagingFacade;
import mat.model.Author;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ManageAuthorsModel extends PagingFacade<Author> implements IsSerializable{
	private static String[] headers = new String[] {"Select","Measure Developer"};
	private static String[] widths = new String[] { "1%", "5%"};
	
	private HashMap<Author, CustomCheckBox> checkboxMap = new HashMap<Author, CustomCheckBox>();
	
	public ManageAuthorsModel(List<Author> data) {
		super(data);
		for(Author c : data) {
			CustomCheckBox cb = new CustomCheckBox("Select Measure Developer",false);
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
	public String getKey(Author dataObject) {
		return null;
	}

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

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

}
