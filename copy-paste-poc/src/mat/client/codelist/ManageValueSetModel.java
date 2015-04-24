package mat.client.codelist;

import java.util.HashMap;
import java.util.List;

import mat.client.codelist.events.OnChangeValueSetDraftOptionsEvent;
import mat.client.shared.MatContext;
import mat.client.shared.search.PagingFacade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageValueSetModel.
 * 
 * @author aschmidt
 */
class ManageValueSetModel extends PagingFacade<ManageValueSetSearchModel.Result> {
	
	/** The headers. */
	private static String[] headers = new String[] { "Select","Value Set Name","OID"};
	
	/** The widths. */
	private static String[] widths = new String[] { "2%", "50%","30%"};

	/** The radio button map. */
	private HashMap<ManageValueSetSearchModel.Result, RadioButton> radioButtonMap = new HashMap<ManageValueSetSearchModel.Result, RadioButton>();
	
	/**
	 * Instantiates a new manage value set model.
	 * 
	 * @param data
	 *            the data
	 */
	public ManageValueSetModel(List<ManageValueSetSearchModel.Result> data) {
		super(data);
		for(ManageValueSetSearchModel.Result c : data) {
			    RadioButton rb = new RadioButton("RowGroup","");
			    /**508 fix - Radio button should not have empty label and Id should be removed. Instead Title should be used.**/
			    rb.getElement().setAttribute("id", "rb-"+c.getName());
			    rb.getElement().getFirstChildElement().removeAttribute("id");
			    rb.getElement().removeChild(rb.getElement().getLastChild());
			    rb.setTitle("Select Value Set" + c.getName());
			    rb.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						MatContext.get().clearDVIMessages();
						MatContext.get().getEventBus().fireEvent( new OnChangeValueSetDraftOptionsEvent());
					}
				});
			    radioButtonMap.put(c, rb);
		}
	}
	
	/** The data. */
	private ManageValueSetSearchModel data = new ManageValueSetSearchModel();
	
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(ManageValueSetSearchModel data) {
		this.data = data;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
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
	 * @see mat.client.shared.search.PagingFacade#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
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


	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getKey(java.lang.Object)
	 */
	@Override
	public String getKey(ManageValueSetSearchModel.Result dataObject) {
		return null;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
	@Override
	public Widget getValueImpl(ManageValueSetSearchModel.Result valueSet, int column) {
		Widget value = null;
		switch(column) {
		case 0:
			value = radioButtonMap.get(valueSet);
			break;
		case 1:
			value = new Label(valueSet.getName());
			break;
		case 2:
			value = new Label(valueSet.getOid());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}
	
	/**
	 * Gets the selected measure.
	 * 
	 * @return the selected measure
	 */
	public ManageValueSetSearchModel.Result getSelectedMeasure() {
		ManageValueSetSearchModel.Result r = new ManageValueSetSearchModel.Result();
		List<ManageValueSetSearchModel.Result> data = getData();
		for(int i = 0; i < data.size(); i++) {
			ManageValueSetSearchModel.Result m = data.get(i);
			RadioButton selectedMeasure = radioButtonMap.get(m);
			if(selectedMeasure.getValue().equals(Boolean.TRUE)) {
				r = m;
			}
		}
		return r;
	}
	

}
