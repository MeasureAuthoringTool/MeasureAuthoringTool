package mat.client.measure;

import java.sql.Timestamp;

import mat.client.ImageResources;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.search.SearchResults;

import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

class MeasureSearchResultsAdapter implements SearchResults<ManageMeasureSearchModel.Result> {
	
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date", "Status", "History" ,"Edit","Share", "Clone", "ExportClear"};
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%", "5%", "5%", "5%", "12%" };

	public static interface Observer {
		public void onEditClicked(ManageMeasureSearchModel.Result result);
		public void onCloneClicked(ManageMeasureSearchModel.Result result);
		public void onShareClicked(ManageMeasureSearchModel.Result result);
		public void onExportClicked(ManageMeasureSearchModel.Result result);
		public void onHistoryClicked(ManageMeasureSearchModel.Result result);		
		public void onExportSelectedClicked(CustomCheckBox checkBox);
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
			String emailAddress = " ";
			if(data.get(row).getLockedUserInfo() != null){
				emailAddress = data.get(row).getLockedUserInfo().getEmailAddress();
			}
			/*
			 * If the measure has been locked by some other user, then display the locked image else check for the user role, if he is top-level user 
			 * display edit image, and display "Read-Only" image if the user is a Normal User and has got view-only mode.
			*/
			if(data.get(row).isEditable()){
				if(data.get(row).isMeasureLocked()){
					value = getImageReadOnly("Measure in use by "+ emailAddress, ImageResources.INSTANCE.g_lock(), data.get(row).getId());
				}
				else
					value = getImage("edit", ImageResources.INSTANCE.g_package_edit(), data.get(row).getId());
				
			}else{
				    value = getImageReadOnly("Read-Only", ImageResources.INSTANCE.ReadOnly(), data.get(row).getId());
			}
			break;
		case 6:
			if(data.get(row).isSharable())
				value = getImage("share", ImageResources.INSTANCE.g_package_share(), data.get(row).getId());
			
			break;
		case 7:
			if(data.get(row).isClonable())
				value = getImage("clone", ImageResources.INSTANCE.g_page_copy(), data.get(row).getId());
			break;
		case 8:
			if(data.get(row).isExportable()){
//				value = getImage("export", ImageResources.INSTANCE.g_package_go(), data.get(row).getId());
				value = getImageAndCheckBox("export", ImageResources.INSTANCE.g_package_go(), data.get(row).getId(),data.get(row).getName());
			}
			break;
		default: 
			value = new Label();
		}
		return value;
	}
	
	private Widget getImage(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		holder.getElement().getStyle().setCursor(Cursor.POINTER);
		/*FocusableImageButton image = new FocusableImageButton(url,action);
		setImageStyle(image);
		setId(image, action, key);
		addListener(image);
		image.getElement().getStyle().setCursor(Cursor.POINTER);*/
		CustomButton image = new CustomButton();
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		//508 fix - Read only and locked icons do not do anything but they appear to show hand pointer on mouse hover.
		if(!action.equalsIgnoreCase("Read-Only") && !(action.contains("Measure in use")))
			addListener(image);
		else
			image.setEnabled(false);
		holder.add(image);
		return holder;
	}
	
	private Widget getImageReadOnly(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		holder.getElement().getStyle().setCursor(Cursor.POINTER);
		/*FocusableImageButton image = new FocusableImageButton(url,action);
		setImageStyle(image);
		setId(image, action, key);
		addListener(image);
		image.getElement().getStyle().setCursor(Cursor.POINTER);*/
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("readOnlyButton");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		//508 fix - Read only and locked icons do not do anything but they appear to show hand pointer on mouse hover.
		if(!action.equalsIgnoreCase("Read-Only") && !(action.contains("Measure in use")))
			addListener(image);
		else
			image.setEnabled(false);
		holder.add(image);
		return holder;
	}
	
	
	private Widget getImageAndCheckBox(String action, ImageResource url, String key,String name){
		HorizontalPanel hPanel = new HorizontalPanel();
		//hPanel.setStyleName("searchTableCenteredHolder");
		/*FocusableImageButton image = new FocusableImageButton(url,action);
		image.setStylePrimaryName("measureSearchResultIcon rightAligned");
		setId(image, action, key);
		addListener(image);*/
		hPanel.setStyleName("exportCheckBox");
		CustomButton image = new CustomButton();
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		addListener(image);
		hPanel.add(image);
				
		CustomCheckBox checkBox = new CustomCheckBox("Select Record to Export", false);	
		checkBox.setStyleName("centerAligned");
		checkBox.setFormValue(key);
		hPanel.add(checkBox);
		checkBox.getElement().setId("bulkExport_" + key);
		checkBox.addClickHandler(clickHandler);
		return hPanel;
	}
	
	
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	private void setImageStyle(FocusableImageButton image) {
		image.setStylePrimaryName("measureSearchResultIcon");
	}
	private void setId(CustomButton image, String action, String key) {
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
					if("edit".equals(action)) {
						observer.onEditClicked(result);
					}
					else if("share".equals(action)) {
						observer.onShareClicked(result);
					}
					else if("clone".equals(action)) {
						observer.onCloneClicked(result);
					}
					else if("export".equals(action)) {
						observer.onExportClicked(result);
					}
					else if("history".equals(action)){
						observer.onHistoryClicked(result);
					}else if("bulkExport".equals(action)){
						observer.onExportSelectedClicked((CustomCheckBox)event.getSource());
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
