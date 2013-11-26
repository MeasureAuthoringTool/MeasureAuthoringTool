package mat.client.measure;

import java.util.HashMap;
import java.util.List;

import mat.client.codelist.events.OnChangeMeasureDraftOptionsEvent;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MatContext;
import mat.client.shared.search.PagingFacade;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class ManageDraftMeasureModel.
 */
class ManageDraftMeasureModel extends PagingFacade<ManageMeasureSearchModel.Result> {
	
	/** The headers. */
	private static String[] headers = new String[] { "Select","Measure Name","Version"};
	
	/** The widths. */
	private static String[] widths = new String[] { "2%", "80%","10%"};

	/** The radio button map. */
	private HashMap<ManageMeasureSearchModel.Result, RadioButton> radioButtonMap = new HashMap<ManageMeasureSearchModel.Result, RadioButton>();
	
	/**
	 * Instantiates a new manage draft measure model.
	 * 
	 * @param data
	 *            the data
	 */
	public ManageDraftMeasureModel(List<ManageMeasureSearchModel.Result> data) {
		super(data);
		for(ManageMeasureSearchModel.Result c : data) {
			    RadioButton rb = new RadioButton("RowGroup","");
			    /** 508 Fix - Radio button with no label should not have empty label tag and radio button should not have Id attribute. 
			     Instead title should be set.**/
			    rb.getElement().setAttribute("id", "rb-"+c.getName());
			    rb.getElement().getFirstChildElement().removeAttribute("id");
			    rb.getElement().removeChild(rb.getElement().getLastChild());
			    rb.setTitle("Select a Measure Version to create a Draft from "+ c.getName());
			    rb.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						MatContext.get().clearDVIMessages();
						MatContext.get().getEventBus().fireEvent( new OnChangeMeasureDraftOptionsEvent());
					}
				});
			    radioButtonMap.put(c, rb);
		}
	}
	
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
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
	public String getKey(Result dataObject) {
		return null;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.PagingFacade#getValueImpl(java.lang.Object, int)
	 */
	@Override
	public Widget getValueImpl(ManageMeasureSearchModel.Result measure, int column) {
		Widget value = null;
		switch(column) {
		case 0:
			value = radioButtonMap.get(measure);
			break;
		case 1:
			value = new Label(measure.getName());
			break;
		case 2:
			if(!measure.isDraft()){
				value = new Label(measure.getVersion());
			}
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
	/*public ManageMeasureSearchModel.Result getSelectedMeasure() {
		ManageMeasureSearchModel.Result r = new ManageMeasureSearchModel.Result();
		List<ManageMeasureSearchModel.Result> data = getData();
		for(int i = 0; i < data.size(); i++) {
			ManageMeasureSearchModel.Result m = data.get(i);
			RadioButton selectedMeasure = radioButtonMap.get(m);
			if(selectedMeasure.getValue().equals(Boolean.TRUE)) {
				r = m;
			}
		}
		return r;
	}*/
	
	public List<ManageMeasureSearchModel.Result> getDataList() {
		return getData();
	}
}
