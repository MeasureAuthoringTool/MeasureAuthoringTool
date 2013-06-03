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
 * 
 * @author aschmidt
 *
 */
class ManageValueSetModel extends PagingFacade<ManageValueSetSearchModel.Result> {
	private static String[] headers = new String[] { "Select","Value Set Name","OID"};
	private static String[] widths = new String[] { "2%", "50%","30%"};

	private HashMap<ManageValueSetSearchModel.Result, RadioButton> radioButtonMap = new HashMap<ManageValueSetSearchModel.Result, RadioButton>();
	
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
	
	private ManageValueSetSearchModel data = new ManageValueSetSearchModel();
	
	
	public void setData(ManageValueSetSearchModel data) {
		this.data = data;
	}
	
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
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
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return false;
	}

	
	

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}


	
	@Override
	public String getKey(ManageValueSetSearchModel.Result dataObject) {
		return null;
	}


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