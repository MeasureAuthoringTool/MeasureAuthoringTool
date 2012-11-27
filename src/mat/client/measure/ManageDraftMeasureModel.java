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

class ManageDraftMeasureModel extends PagingFacade<ManageMeasureSearchModel.Result> {
	private static String[] headers = new String[] { "Select","Measure Name","Version"};
	private static String[] widths = new String[] { "2%", "80%","10%"};

	private HashMap<ManageMeasureSearchModel.Result, RadioButton> radioButtonMap = new HashMap<ManageMeasureSearchModel.Result, RadioButton>();
	
	public ManageDraftMeasureModel(List<ManageMeasureSearchModel.Result> data) {
		super(data);
		for(ManageMeasureSearchModel.Result c : data) {
			    RadioButton rb = new RadioButton("RowGroup","");
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
	
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	
	public void setData(ManageMeasureSearchModel data) {
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
	public String getKey(Result dataObject) {
		return null;
	}


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
	
	public ManageMeasureSearchModel.Result getSelectedMeasure() {
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
	}
	

}