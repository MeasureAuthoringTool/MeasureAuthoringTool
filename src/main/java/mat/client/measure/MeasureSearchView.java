package mat.client.measure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.resource.CellTableResource;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasureLibraryResultTable;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.ClientConstants;
import mat.shared.MeasureSearchModel;

public class MeasureSearchView implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	private VerticalPanel cellTablePanel = new VerticalPanel();
	private FlowPanel mainPanel = new FlowPanel();
	private static final int PAGE_SIZE = 25;
	private static final int COL_SIZE = 6;
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;
	private HandlerManager handlerManager = new HandlerManager(this);
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	private AdminObserver adminObserver;
	private CellTable<ManageMeasureSearchModel.Result> table;
	private Boolean even;
	private List<String> cellTableCssStyle;
	private String cellTableEvenRow = "cellTableEvenRow";
	private String cellTableOddRow = "cellTableOddRow";
	private int index;
	private String measureListLabel;
	private MeasureLibraryResultTable measureLibraryResultTable = new MeasureLibraryResultTable();
	
	List<ManageMeasureSearchModel.Result> selectedList;
	/**
	 * An asynchronous update interface for receiving notifications
	 * about Admin information as the Admin is constructed.
	 */
	public static interface AdminObserver {
		/**
		 * On history clicked.
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On transfer selected clicked.
		 * @param result
		 *            the result
		 */
		void onTransferSelectedClicked(ManageMeasureSearchModel.Result result);
	}
	public static interface Observer {
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		/**
		 * On clone clicked.
		 * @param result
		 *            the result
		 */
		void onCloneClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On share clicked.
		 * @param result
		 *            the result
		 */
		void onShareClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On export clicked.
		 * @param result
		 *            the result
		 */
		void onExportClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On history clicked.
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On export selected clicked.
		 * @param checkBox
		 *            the check box
		 */
		void onExportSelectedClicked(CustomCheckBox checkBox);
		/**
		 * On export selected clicked.
		 *
		 * @param result the result
		 * @param isCBChecked the Boolean.
		 */
		void onExportSelectedClicked(ManageMeasureSearchModel.Result result, boolean  isCBChecked);

		void onClearAllBulkExportClicked();
		
		void onCreateClicked(ManageMeasureSearchModel.Result object);
	}
	/**
	 * Instantiates a new measure search view.
	 * @param view
	 *            the string
	 */
	public MeasureSearchView(String view) {
		this();
	}

	public MeasureSearchView() {
		mainPanel.getElement().setId("measureserachView_mainPanel");
		mainPanel.setStylePrimaryName("measureSearchResultsContainer");
		mainPanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("serachView_mainPanel");
	}

	/**
	 * Builds the cell table.
	 *
	 * @param results the results
	 * @param filter the filter
	 * @param MeasureSearchModel 
	 * 		which represents the model of the selections the user selected to do an advance search
	 */
	public void buildCellTable(ManageMeasureSearchModel results,final int filter, MeasureSearchModel model) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if((results.getData()!=null) && (results.getData().size() > 0)){
			table = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
					(Resources) GWT.create(CellTableResource.class));
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			selectedList = new ArrayList<ManageMeasureSearchModel.Result>();
			selectedMeasureList = new ArrayList<Result>();
			selectedMeasureList.addAll(results.getData());
			table.setRowData(selectedMeasureList);
			table.setRowCount(results.getResultsTotal(), true);
			table.setPageSize(PAGE_SIZE);
			table.redraw();

			ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<
					ManageMeasureSearchModel.Result>(results.getData());
		    AsyncDataProvider<ManageMeasureSearchModel.Result> provider = new AsyncDataProvider<ManageMeasureSearchModel.Result>() {
		      @Override
		      protected void onRangeChanged(HasData<ManageMeasureSearchModel.Result> display) {
		        final int start = display.getVisibleRange().getStart();
		        index = start;
		        AsyncCallback<ManageMeasureSearchModel> callback = new AsyncCallback<ManageMeasureSearchModel>() {
		          @Override
		          public void onFailure(Throwable caught) {
		          }
		          @Override
		          public void onSuccess(ManageMeasureSearchModel result) {
		        	  List<ManageMeasureSearchModel.Result> manageMeasureSearchList = 
		        			  new ArrayList<ManageMeasureSearchModel.Result>();		        	  
		        	  manageMeasureSearchList.addAll(result.getData());
		        	  selectedMeasureList = manageMeasureSearchList;
		        	  buildCellTableCssStyle();
		            updateRowData(start, manageMeasureSearchList);
		          }
		        };
		        
		        model.setStartIndex(start + 1);
		        model.setPageSize(start + PAGE_SIZE);

		        model.setIsMyMeasureSearch(filter);
		        
		        MatContext.get().getMeasureService().search(model, callback);
		      }
		    };
		   
			
			provider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureLib");
			spager.setPageStart(0);
			buildCellTableCssStyle();
			spager.setDisplay(table);
			spager.setPageSize(PAGE_SIZE);
			table.setWidth("100%");
			 if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
						.getLoggedInUserRole())){
			    	table = addColumnToAdminTable(sortHandler);
			    	Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
							"In the following Measure List table, Measure Name is given in first column,"
									+ " Version in second column, Finalized Date in third column,"
									+ "History in fourth column, Edit in fifth column, Share in sixth column"
									+ "Clone in seventh column and Export in eight column.");
					table.getElement().setAttribute("id", "MeasureSearchCellTable");
					table.getElement().setAttribute("aria-describedby", "measureSearchSummary");
					cellTablePanel.add(invisibleLabel);
			    }else{
			    	table = measureLibraryResultTable.addColumnToTable(getMeasureListLabel(), table, selectedList, true, MeasureSearchView.this);
			    	Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
							"In the following Measure List table, Measure Name is given in first column,"
									+ " Version in second column, Version/Draft in third column for creating version/draft,"
									+ "History in fourth column, Edit in fifth column, Share in sixth column"
									+ "Clone in seventh column and Export in eight column.");
					table.getElement().setAttribute("id", "MeasureSearchCellTable");
					table.getElement().setAttribute("aria-describedby", "measureSearchSummary");
					
					MatSimplePager topSPager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,
							"measureLibTopSpager");
					topSPager.setPageStart(0);
					topSPager.setDisplay(table);
					topSPager.setPageSize(PAGE_SIZE);
					
					cellTablePanel.add(new SpacerWidget());
					cellTablePanel.add(topSPager);
					cellTablePanel.add(new SpacerWidget());
		
					cellTablePanel.add(invisibleLabel);
			    }
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 23.0, Unit.PCT);
			table.setColumnWidth(3, 2.0, Unit.PCT);
			table.setColumnWidth(4, 2.0, Unit.PCT);
			table.setColumnWidth(5, 2.0, Unit.PCT);
			table.setColumnWidth(6, 2.0, Unit.PCT);
			table.setColumnWidth(7, 22.0, Unit.PCT);
			    
			
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		}	else{
			Label measureSearchHeader = new Label(getMeasureListLabel());
			measureSearchHeader.getElement().setId("measureSearchHeader_Label");
			measureSearchHeader.setStyleName("recentSearchHeader");
			measureSearchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML(MatContext.get().getMessageDelegate().getNoMeasuresMessage());
			cellTablePanel.add(measureSearchHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
			
		}
	}
	
	
	/**
	 * Adds the column to admin table.
	 *
	 * @param sortHandler the sort handler
	 * @return the cell table
	 */
	private CellTable<ManageMeasureSearchModel.Result> addColumnToAdminTable(
			ListHandler<ManageMeasureSearchModel.Result> sortHandler) {
		if (table.getColumnCount() != COL_SIZE) {
			Label searchHeader = new Label("Select Measures to Transfer Ownership.");
			searchHeader.getElement().setId("measureTransferOwnerShipCellTableCaption_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getName(), object.getName());
				}
			};
			measureName.setSortable(true);
			sortHandler.setComparator(measureName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
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
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title=\"Measure Name\">"
					+ "Measure Name" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerName = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result manageMeasureSearchModelResult) {
					return CellTableUtility.getColumnToolTip(manageMeasureSearchModelResult.getOwnerFirstName()
							+ "  " + manageMeasureSearchModelResult.getOwnerLastName(),manageMeasureSearchModelResult.getOwnerFirstName()
							+ "  " + manageMeasureSearchModelResult.getOwnerLastName());
				}
			};
			ownerName.setSortable(true);
			sortHandler.setComparator(ownerName, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
				public int compare(ManageMeasureSearchModel.Result o1, ManageMeasureSearchModel.Result o2) {
					if (o1 == o2) {
						return 0;
					}
					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerFirstName().compareTo(o2.getOwnerFirstName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerEmailAddress = new Column<
					ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerEmailAddress(),object.getOwnerEmailAddress());
				}
			};
			ownerEmailAddress.setSortable(true);
			sortHandler.setComparator(ownerEmailAddress, new Comparator<ManageMeasureSearchModel.Result>() {
				@Override
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
			table.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner E-mail Address\">"
					+ "Owner E-mail Address" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> eMeasureID = new Column<ManageMeasureSearchModel.Result,
					SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip("" + object.geteMeasureId(), "" + object.geteMeasureId());
				}
			};
			table.addColumn(eMeasureID, SafeHtmlUtils.fromSafeConstant("<span title=\"eMeasure Id\">"
					+ "eMeasure Id" + "</span>"));
			//MAT-9000. Changes to Measure Library Ownership table to use bootstrap history column icon. 
			Cell<String> historyButton = new MatButtonCell("Click to view history", "btn btn-link", "fa fa-clock-o fa-lg" , "History");			
			Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return "";
				}
			};
			historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, String value) {
					adminObserver.onHistoryClicked(object);
				}
			});
			table.addColumn(historyColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"History\">" + "History" + "</span>"));
			
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column<Result, Boolean> transferColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(transferCB) {
				@Override
				public Boolean getValue(ManageMeasureSearchModel.Result object) {
					if (selectedList.size() > 0) {
						for (int i = 0; i < selectedList.size(); i++) {
							if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
								object.setTransferable(true);
								break;
							}
						}
					} else {
						object.setTransferable(false);
						}
					return object.isTransferable();
				}
			};
			transferColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
					if(value){
						if(!selectedList.contains(object)){
						selectedList.add(object);
						}
					} else {
						for (int i = 0; i < selectedList.size(); i++) {
							if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
								selectedList.remove(i);
								break;
							}
						}
					}
					object.setTransferable(value);
					adminObserver.onTransferSelectedClicked(object);
				}
			});
			table.addColumn(transferColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Check for Ownership Transfer\">"
					+ "Transfer </span>"));
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

	private void buildCellTableCssStyle() {
		cellTableCssStyle = new ArrayList<String>();
		for (int i = 0; i < selectedMeasureList.size(); i++) {
			cellTableCssStyle.add(i, null);
		}
		table.setRowStyles(new RowStyles<ManageMeasureSearchModel.Result>() {
			@Override
			public String getStyleNames(ManageMeasureSearchModel.Result rowObject, int rowIndex) {
				if(rowIndex > PAGE_SIZE - 1){
					rowIndex = rowIndex - index;
				}
				if (rowIndex != 0) {
					if (cellTableCssStyle.get(rowIndex) == null) {
						if (even) {
							if (rowObject.getMeasureSetId().equalsIgnoreCase(
									selectedMeasureList.get(rowIndex - 1).getMeasureSetId())) {
								even = true;
								cellTableCssStyle.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							} else {
								even = false;
								cellTableCssStyle.add(rowIndex, cellTableEvenRow);
								return cellTableEvenRow;
							}
						} else {
							if (rowObject.getMeasureSetId().equalsIgnoreCase(
									selectedMeasureList.get(rowIndex - 1).getMeasureSetId())) {
								even = false;
								cellTableCssStyle.add(rowIndex, cellTableEvenRow);
								return cellTableEvenRow;
							} else {
								even = true;
								cellTableCssStyle.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							}
						}
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				} else {
					if (cellTableCssStyle.get(rowIndex) == null) {
						even = true;
						cellTableCssStyle.add(rowIndex, cellTableOddRow);
						return cellTableOddRow;
					} else {
						return cellTableCssStyle.get(rowIndex);
					}
				}
			}
		});
	}

	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	/**
	 * Gets the select id for edit tool.
	 *
	 * @return the select id for edit tool
	 */
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	/**
	 * Gets the data.
	 *
	 * @return the data
	 */
	public ManageMeasureSearchModel getData() {
		return data;
	}
	/**
	 * Sets the data.
	 *
	 * @param data the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}

	/**
	 * Getter measureListLabel.
	 * @return String.
	 */
	public String getMeasureListLabel() {
		return measureListLabel;
	}
	
	/**
	 * Set measureListLabel.
	 *
	 * @param measureListLabel the new measure list label
	 */
	public void setMeasureListLabel(String measureListLabel) {
		this.measureListLabel = measureListLabel;
	}
	
	
	/**
	 * Sets the admin observer.
	 *
	 * @param adminObserver the new admin observer
	 */
	public void setAdminObserver(AdminObserver adminObserver) {
		this.adminObserver = adminObserver;
	}
	
	/**
	 * Gets the selected list.
	 *
	 * @return the selected list
	 */
	public List<ManageMeasureSearchModel.Result> getSelectedList() {
		return selectedList;
	}
	

	public void clearTransferCheckBoxes() {	
		for (ManageMeasureSearchModel.Result result : getSelectedList()) {
			result.setTransferable(false);
		}
		getSelectedList().clear();
		getData().setData(selectedMeasureList);
		table.redraw();
	}
	
	public VerticalPanel getCellTablePanel() {
		return cellTablePanel;
	}
	
	public void setObserver(Observer observer) {
		measureLibraryResultTable.setObserver(observer);
	}
	
	public void clearBulkExportCheckBoxes() {
		measureLibraryResultTable.clearBulkExportCheckBoxes();
	}
}
