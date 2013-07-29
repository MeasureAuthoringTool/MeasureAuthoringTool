package mat.client.measure;

import java.sql.Timestamp;

import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResults;



import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdminMeasureSearchResultAdaptor implements SearchResults<ManageMeasureSearchModel.Result> {
	
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date", "Status", "History" ,"TransferMeasureClear"};
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%","10%" };
	
	private boolean isHistoryClicked;

	public static interface Observer {
		public void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
		public void onHistoryClicked(ManageMeasureSearchModel.Result result);
	}
		
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	private ManageMeasureSearchModel.Result lastSelectedMeasureList;
	private ManageMeasureSearchModel.Result selectedMeasureList;
	
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

	public ManageMeasureSearchModel getData() {
		return data;
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
	
	public ManageMeasureSearchModel.Result getLastSelectedMeasureList() {
		return lastSelectedMeasureList;
	}

	public void setLastSelectedMeasureList(
			ManageMeasureSearchModel.Result lastSelectedMeasureList) {
		this.lastSelectedMeasureList = lastSelectedMeasureList;
	}

	public ManageMeasureSearchModel.Result getSelectedMeasureList() {
		return selectedMeasureList;
	}

	public void setSelectedMeasureList(
			ManageMeasureSearchModel.Result selectedMeasureList) {
		this.selectedMeasureList = selectedMeasureList;
	}

	
	//TODO - need to remove this method going forward as we replace the Grid Table with Cel T
	@Override
	public Widget getValue(int row, int column) {
		return null;
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
	
	public SingleSelectionModel<ManageMeasureSearchModel.Result> addSelectionHandlerOnTable(){
		final SingleSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new  SingleSelectionModel<ManageMeasureSearchModel.Result>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				ManageMeasureSearchModel.Result measureListObject = selectionModel.getSelectedObject();
				if(measureListObject !=null){
					//lastSelectedCodeList = codeListObject;
					MatContext.get().clearDVIMessages();
					
					setLastSelectedMeasureList(measureListObject);
					setSelectedMeasureList(measureListObject);
					MatContext.get().getEventBus().fireEvent(new OnChangeOptionsEvent());

				}
			}
		});
		return selectionModel;
	}

	private SafeHtml getColumnToolTip(String columnText, StringBuilder title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='"+title + "'>"+columnText+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	@SuppressWarnings("unchecked")
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table){
		
		if(table.getColumnCount() !=6 ){	
			
			TextColumn<ManageMeasureSearchModel.Result > measureName = new TextColumn<ManageMeasureSearchModel.Result >() {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return object.getName();
				}
			};
			table.addColumn(measureName, "Measure Name");
			
			
			TextColumn<ManageMeasureSearchModel.Result > ownerName = new TextColumn<ManageMeasureSearchModel.Result >() {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return object.getOwnerfirstName() + "  " + object.getOwnerLastName();
				}
			};
			table.addColumn(ownerName, "Owner Name");
			
			TextColumn<ManageMeasureSearchModel.Result > ownerEmailAddress = new TextColumn<ManageMeasureSearchModel.Result >() {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return object.getOwnerEmailAddress();
				}
			};
			table.addColumn(ownerEmailAddress, "Owner E-mail Address");
						
			TextColumn<ManageMeasureSearchModel.Result > version = new TextColumn<ManageMeasureSearchModel.Result >() {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return object.getVersion();
				}
			};
			table.addColumn(version, "Version");
			
			
			Cell<String> historyButton = new MatButtonCell();
			Column historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
			  @Override
			  public String getValue(ManageMeasureSearchModel.Result object) {
			    return "History";
			  }
			};
			
			historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
				  @Override
				  public void update(int index, ManageMeasureSearchModel.Result object, String value) {
					  observer.onHistoryClicked(object);
				  }
				});
			table.addColumn(historyColumn , "History");
			
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column transferColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(transferCB) {
			  @Override
			  public Boolean getValue(ManageMeasureSearchModel.Result object) {
			    return object.isTransferable();
			  }
			};
			
			transferColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
				  @Override
				  public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
					  object.setTransferable(value);
					  observer.onTransferSelectedClicked(object);
				  }
				});
			table.addColumn(transferColumn , "Transfer");
					
			
			
		}
		
		return table;
		
	}

	public boolean isHistoryClicked() {
		return isHistoryClicked;
	}

	public void setHistoryClicked(boolean isHistoryClicked) {
		this.isHistoryClicked = isHistoryClicked;
	}
	
		
	
}
