package mat.client.measure;

import java.sql.Timestamp;

import mat.client.ImageResources;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.search.SearchResults;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdminMeasureSearchResultAdaptor implements SearchResults<ManageMeasureSearchModel.Result> {
	
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date", "Status", "History" ,"TransferClear"};
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%","10%" };

	public static interface Observer {
		public void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
		public void onHistoryClicked(ManageMeasureSearchModel.Result result);
	}
		
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	private Observer observer;
	private ClickHandler clickHandler = buildClickHandler();

	private ManageMeasureSearchModel.Result getResultForId(String id) {
		for(int i = 0; i < data.getNumberOfRows(); i++) {
			if(id.equals(data.getKey(i))) {
				return get(i);
			}
		}
		Window.alert("Could not find id " + id);
		return null;
	}
	
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
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
	public int getNumberOfRows() {
		return data.getNumberOfRows();
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
		return columnIndex == 0;
	}

	@Override
	public Widget getValue(int row, int column) {
		Widget value = null;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getName());
			break;
		case 1:
			value = new Label(data.get(row).getVersion());
			break;
		case 2:
			Timestamp ts = data.get(row).getFinalizedDate();
			String text = ts == null ? "" : convertTimestampToString(ts);
			value = new Label(text);
			break;
		case 3:
			value = new Label(data.get(row).getStatus());
			break;
		case 4: 
			value = getImage("history", ImageResources.INSTANCE.clock(),data.get(row).getId());
			break;
		case 5:
			if(data.get(row).isTransferable()){
				value = getTransferCheckBox(data.get(row).getId());
			}else
				value = new Label();
			break;
		default: 
			value = new Label();
		}
		return value;
	}
	
	private Widget getImage(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		FocusableImageButton image = new FocusableImageButton(url,action);
		setImageStyle(image);
		setId(image, action, key);
		addListener(image);
		holder.add(image);
		return holder;
	}
	
	
	private HorizontalPanel getTransferCheckBox(String key){
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.setStyleName("searchTableCenteredHolder");
		CustomCheckBox transFerCheckBox = new CustomCheckBox("Transfer", false);	
		transFerCheckBox.getElement().setId("Transfer_" + key);
		transFerCheckBox.addClickHandler(clickHandler);
		hPanel.add(transFerCheckBox);
		return hPanel;
		
	}
	
	
	private void addListener(FocusableImageButton image) {
		image.addClickHandler(clickHandler);
	}
	private void setImageStyle(FocusableImageButton image) {
		image.setStylePrimaryName("measureSearchResultIcon");
	}
	private void setId(FocusableImageButton image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}
	private ClickHandler buildClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = ((Widget)event.getSource()).getElement().getId();
				int index = id.indexOf('_');
				String action = id.substring(0, index);
				String key = id.substring(index + 1);
				ManageMeasureSearchModel.Result result = getResultForId(key);
				if(observer != null) {
					if("Transfer".equals(action)){
						CustomCheckBox transferCB = (CustomCheckBox)event.getSource();
						result.setTransferable((transferCB.getValue()));
						observer.onTransferSelectedClicked(result);
					}else if("history".equals(action)){
						observer.onHistoryClicked(result);
					}
				}
			}
		};
	}
	@Override
	public int getStartIndex() {
		return data.getStartIndex();
	}

	@Override
	public int getResultsTotal() {
		return data.getResultsTotal();
	}

	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}


	@Override
	public ManageMeasureSearchModel.Result get(int row) {
		return data.get(row);
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	/**
	 * TODO make use of a utility class ex. DateUtility.java though it must be usable on the client side
	 * currently it is not usable on the client side
	 * @param ts
	 * @return
	 */
	public String convertTimestampToString(Timestamp ts){
		int hours = ts.getHours();
		String ap = hours < 12 ? "AM" : "PM";
		int modhours = hours % 12;
		String mins = ts.getMinutes()+"";
		if(mins.length()==1)
			mins = "0"+mins;
		
		String hoursStr = modhours == 0 ? "12" : modhours+"";
		
		String tsStr = (ts.getMonth()+1)+"/"+ts.getDate()+"/"+(ts.getYear()+1900)+" "+hoursStr+":"+mins+" "+ap;
		return tsStr;
	}
}
