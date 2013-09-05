package mat.client.measure;

import java.sql.Timestamp;
import java.util.Comparator;

import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.search.SearchResults;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Widget;

public class AdminMeasureSearchResultAdaptor implements SearchResults<ManageMeasureSearchModel.Result> {
	
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date", "Status", "History" ,"TransferMeasureClear"};
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%","10%" };
	
	private boolean isHistoryClicked;

	public static interface Observer {
		public void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
		public void onHistoryClicked(ManageMeasureSearchModel.Result result);
	}
		
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	
	private Observer observer;
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
	
		
	//TODO - need to remove this method going forward as we replace the Grid Table with Cel T
	@Override
	public Widget getValue(int row, int column) {
		return null;
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
	@SuppressWarnings("deprecation")
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
	
/*	
	public MultiSelectionModel<CodeListSearchDTO> addSelectionHandlerOnTable(){
		final MultiSelectionModel<CodeListSearchDTO> selectionModel = new  MultiSelectionModel<CodeListSearchDTO>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<CodeListSearchDTO> codeListSet = selectionModel.getSelectedSet();
				if(codeListSet !=null){
					//lastSelectedCodeList = codeListObject;
					MatContext.get().clearDVIMessages();
					MatContext.get().clearModifyPopUpMessages();
					List<CodeListSearchDTO> selectedCodeList = new ArrayList<CodeListSearchDTO>(codeListSet);
					setLastSelectedCodeList(selectedCodeList);
					setSelectedCodeList(selectedCodeList);
					MatContext.get().getEventBus().fireEvent( new OnChangeOptionsEvent());

				}
			}
		});
		return selectionModel;
	}
	*/
	
	private SafeHtml getColumnToolTip(String title){
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>"+title+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table ,ListHandler<ManageMeasureSearchModel.Result> sortHandler){
		
		if(table.getColumnCount() !=6 ){	
			
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return getColumnToolTip(object.getName());
				}
			};
			measureName.setSortable(true);
			sortHandler.setComparator(measureName,new Comparator<ManageMeasureSearchModel.Result>() {
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name' tabindex=\"0\">" +"Measure Name"+ "</span>"));

			
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return getColumnToolTip(object.getOwnerfirstName() + "  " + object.getOwnerLastName());
				}
			};
			
			ownerName.setSortable(true);
			sortHandler.setComparator(ownerName,new Comparator<ManageMeasureSearchModel.Result>() {
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerfirstName().compareTo(o2.getOwnerfirstName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerName,SafeHtmlUtils.fromSafeConstant("<span title='Owner' tabindex=\"0\">" +"Owner"+ "</span>"));
			
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerEmailAddress = new Column<ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return getColumnToolTip(object.getOwnerEmailAddress());
				}
			};
			
			ownerEmailAddress.setSortable(true);
			sortHandler.setComparator(ownerEmailAddress,new Comparator<ManageMeasureSearchModel.Result>() {
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerEmailAddress().compareTo(o2.getOwnerEmailAddress()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title='Owner E-mail Address' tabindex=\"0\">" +"Owner E-mail Address"+ "</span>"));
						
						
			Column<ManageMeasureSearchModel.Result, SafeHtml> eMeasureID = new Column<ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return  getColumnToolTip("" + object.geteMeasureId());
				}
			};
			table.addColumn(eMeasureID, SafeHtmlUtils.fromSafeConstant("<span title='eMeasure Id' tabindex=\"0\">" +"eMeasure Id"+ "</span>"));
			
			
			Cell<String> historyButton = new MatButtonCell("Click to view history");
			Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
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
			table.addColumn(historyColumn , SafeHtmlUtils.fromSafeConstant("<span title='History' tabindex=\"0\">" +"History"+ "</span>"));
			
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column<Result, Boolean> transferColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(transferCB) {
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
			table.addColumn(transferColumn , SafeHtmlUtils.fromSafeConstant("<span title='Check for Ownership Transfer' tabindex=\"0\">" +"Transfer"+ "</span>"));
			table.setColumnWidth(0, 30.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 15.0, Unit.PCT);
			table.setColumnWidth(4, 5.0, Unit.PCT);
			table.setColumnWidth(5, 5.0, Unit.PCT);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			
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
