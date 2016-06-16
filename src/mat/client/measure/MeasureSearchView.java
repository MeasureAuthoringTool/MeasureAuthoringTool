package mat.client.measure;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

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
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.ClientConstants;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureSearchView.
 * @author jnarang
 *
 */
public class MeasureSearchView  implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	/** The Constant COL_SIZE. */
	private static final int COL_SIZE = 6;
	/** The selected measure list. */
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	/** The observer. */
	private Observer observer;
	/** The admin observer. */
	private AdminObserver adminObserver;
	/** The table. */
	private CellTable<ManageMeasureSearchModel.Result> table;
	/** The even. */
	private Boolean even;
	/** The cell table css style. */
	private List<String> cellTableCssStyle;
	/** The cell table even row. */
	private String cellTableEvenRow = "cellTableEvenRow";
	/** The cell table odd row. */
	private String cellTableOddRow = "cellTableOddRow";
	/** The index. */
	private int index;
	
	/** The measure list label. */
	private String measureListLabel;
	/**
	 * MultiSelectionModel on Cell Table.
	 */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
	/** The selected list. */
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
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		void onEditClicked(ManageMeasureSearchModel.Result result);
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
		/**
		 * On clear all bulk export clicked.
		 */
		void onClearAllBulkExportClicked();
	}
	/**
	 * Instantiates a new measure search view.
	 * @param view
	 *            the string
	 */
	public MeasureSearchView(String view) {
		this();
	}
	/**
	 * Instantiates a new measure search view.
	 */
	public MeasureSearchView() {
		mainPanel.getElement().setId("measureserachView_mainPanel");
		mainPanel.setStylePrimaryName("measureSearchResultsContainer");
		mainPanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("serachView_mainPanel");
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @return the cell table
	 */
	//TO DO : Consider re factoring this method as code lines are more then 150.
	private CellTable<ManageMeasureSearchModel.Result> addColumnToTable() {
		Label measureSearchHeader = new Label(getMeasureListLabel());
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		measureSearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		table.setSelectionModel(selectionModel);
		
		//Measure Name Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return getMeasureNameColumnToolTip(object);
			}
		};
		measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
				SelectionEvent.fire(MeasureSearchView.this, object);
			}
		});
		table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
				+ "Measure Name" + "</span>"));
		
		// Version Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		table.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));
		
		//Finalized Date
		Column<ManageMeasureSearchModel.Result, SafeHtml> finalizedDate = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				if (object.getFinalizedDate() != null) {
					return CellTableUtility.getColumnToolTip(convertTimestampToString(object.getFinalizedDate()));
				} 
				return null;
			}
		};
		table.addColumn(finalizedDate, SafeHtmlUtils
				.fromSafeConstant("<span title='Finalized Date'>" + "Finalized Date"
						+ "</span>"));
		
		//History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
		Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, 
				String>(historyButton) {
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
				table.addColumn(historyColumn, SafeHtmlUtils.fromSafeConstant("<span title='History'>"
						+ "History" + "</span>"));
				
				//Edit
				Column<ManageMeasureSearchModel.Result, SafeHtml> editColumn =
						new Column<ManageMeasureSearchModel.Result, SafeHtml>(
								new ClickableSafeHtmlCell()) {
					@Override
					public SafeHtml getValue(Result object) {
						return getEditColumnToolTip(object);
					}
				};
				editColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
					@Override
					public void update(int index, Result object,
							SafeHtml value) {
						if (object.isEditable() && !object.isMeasureLocked()) {
							observer.onEditClicked(object);
						}
					}
				});
				table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));
				
				//Share
				Column<ManageMeasureSearchModel.Result, SafeHtml> shareColumn = new Column<ManageMeasureSearchModel.Result, 
						SafeHtml>(new ClickableSafeHtmlCell()) {
					@Override
					public SafeHtml getValue(Result object) {						
						return getShareColumnToolTip(object);
					}
				};
				shareColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
						if(object.isSharable())
							observer.onShareClicked(object);
					}
				});
				table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));
				
				//Clone
				Column<ManageMeasureSearchModel.Result, SafeHtml> cloneColumn = new Column<ManageMeasureSearchModel.Result, 
						SafeHtml>(new ClickableSafeHtmlCell()) {
							@Override
							public SafeHtml getValue(Result object) {
								return getCloneColumnToolTip(object);
							}
				};
				cloneColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
						if(object.isClonable())
							observer.onCloneClicked(object);
					}
				});
				table.addColumn(cloneColumn, SafeHtmlUtils.fromSafeConstant("<span title='Clone'>" + "Clone" + "</span>"));
				
				//Export Column header
				
				Header<SafeHtml> bulkExportColumnHeader = getBulkExportColumnHeader();
				bulkExportColumnHeader.setUpdater(new ValueUpdater<SafeHtml>() {
					@Override
					public void update(SafeHtml value) {
						clearBulkExportCheckBoxes();
					}
				});
				
				
				table.addColumn(new Column<Result, Result>(getCompositeCellForBulkExport()) {
					@Override
					public Result getValue(Result object) {
						return object;
					}
				}, bulkExportColumnHeader);
				return table;
	}
	
	/**
	 * Gets the measure name column tool tip.
	 *
	 * @param object the object
	 * @return the measure name column tool tip
	 */
	private SafeHtml getMeasureNameColumnToolTip(ManageMeasureSearchModel.Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String cssClass = "customCascadeButton";
		if (object.isMeasureFamily()) {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\">"
					+ "<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		} else {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\" >");
			sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\""
					+ object.getName() + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		}
		return sb.toSafeHtml();		
	}
	
	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getEditColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass;
		if (object.isEditable()) {
			if (object.isMeasureLocked()) {
				String emailAddress = object.getLockedUserInfo().getEmailAddress();
				title = "Measure in use by " + emailAddress;
				cssClass = "customLockedButton";
			} else {
				title = "Edit";
				cssClass = "customEditButton";
			}
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">Edit</button>");
		} else {
			title = "Read-Only";
			cssClass = "customReadOnlyButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled>Read-Only</button>");
		}
		
		return sb.toSafeHtml();
	}
	
	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getShareColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass;
		if (object.isSharable()) {
			title = "Shareable";
			cssClass = "customShareButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">Shareable</button>");
		} else {
			title = "Shareable";
			cssClass = "customGrayedShareButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled>Shareable</button>");
		}
		
		return sb.toSafeHtml();
	}

	/**
	 * Gets the history column tool tip.
	 *
	 * @param object the object
	 * @return the history column tool tip
	 */
	private SafeHtml getCloneColumnToolTip(Result object){
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass;
		
		if (object.isClonable()) {
			title = "Clonable";
			cssClass = "customCloneButton"; 
			sb.appendHtmlConstant("<button type=\"button\" title='"
				+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">Clonable</button>");
		} else {
			title = "Clonable";
			cssClass = "customGrayedCloneButton";
			sb.appendHtmlConstant("<button type=\"button\" title='"
					+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled>Clonable</button>");
		}
		
		return sb.toSafeHtml();
	}
	
	/**
	 * Gets the bulk export column header.
	 *
	 * @return the bulk export column header
	 */
	private Header<SafeHtml> getBulkExportColumnHeader(){
		Header<SafeHtml> bulkExportColumnHeader = new Header<SafeHtml>(new ClickableSafeHtmlCell()) {
			private String cssClass = "transButtonWidth";
			private String title = "Click to Clear All";
			@Override
			public SafeHtml getValue() {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<span>Export</span><button type=\"button\" title='"
						+ title + "' tabindex=\"0\" class=\" " + cssClass + "\">"
						+ "<span class='textCssStyle'>(Clear)</span></button>");
				return sb.toSafeHtml();
			}
		};
		return bulkExportColumnHeader;
	}
	
	/**
	 * Gets the composite cell for bulk export.
	 *
	 * @return the composite cell for bulk export
	 */
	private CompositeCell<Result> getCompositeCellForBulkExport(){
		final List<HasCell<Result, ?>> cells = new LinkedList<HasCell<Result, ?>>();
		cells.add(getBulkExportButtonCell());
		cells.add(getCheckBoxCell());
		CompositeCell<Result> cell = new CompositeCell<Result>(cells) {
			@Override
			public void render(Context context, Result object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				for (HasCell<Result, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected <X> void render(Context context, Result object,
					SafeHtmlBuilder sb, HasCell<Result, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces'>");
				if ((object != null) && object.isExportable()) {
					cell.render(context, hasCell.getValue(object), sb);
				} else {
					sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
				}
				sb.appendHtmlConstant("</td>");
			}
			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}
		};
		return cell;
	}
	
	/**
	 * Gets the bulk export button cell.
	 *
	 * @return the bulk export button cell
	 */
	private HasCell<Result, SafeHtml> getBulkExportButtonCell(){
		
		HasCell<Result, SafeHtml> hasCell = new HasCell<ManageMeasureSearchModel.Result, SafeHtml>() {
			
			ClickableSafeHtmlCell exportButonCell = new ClickableSafeHtmlCell();
			@Override
			public Cell<SafeHtml> getCell() {
				return exportButonCell;
			}

			@Override
			public FieldUpdater<Result, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<Result, SafeHtml>() {
					@Override
					public void update(int index, Result object, SafeHtml value) {
						if ((object != null) && object.isExportable()) {
						observer.onExportClicked(object);
						}
					}
				};
			}

			@Override
			public SafeHtml getValue(Result object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "";
				String cssClass = "";
				if((object != null) && (object.getHqmfReleaseVersion() != null)) {
					if(object.getHqmfReleaseVersion().equalsIgnoreCase("v3")){
						cssClass = "customExportButton";
						title = "Click to Export MAT v3";
						sb.appendHtmlConstant("<button type=\"button\" title='" + title 
								+ "' tabindex=\"0\" class=\" " + cssClass + "\"/>Export MAT v3</button>");		
					} else if(object.getHqmfReleaseVersion().equalsIgnoreCase("v4")){
						cssClass = "customExportButtonRed";
						title = "Click to Export MAT v4";
						sb.appendHtmlConstant("<button  type=\"button\" title='" + title 
								+ "' tabindex=\"0\" class=\" " + cssClass + "\">Export MAT v4</button>");	
					} else if(object.getHqmfReleaseVersion().equalsIgnoreCase("v4.3")) {
							cssClass = "customExportButtonRed";
							title = "Click to Export MAT v4.3";
							sb.appendHtmlConstant("<button  type=\"button\" title='" + title 
									+ "' tabindex=\"0\" class=\" " + cssClass + "\">Export MAT v4.3</button>");	
					} else if(object.getHqmfReleaseVersion().equalsIgnoreCase("v5.0")) {
						cssClass = "customExportButtonRed";
						title = "Click to Export MAT v5.0";
						sb.appendHtmlConstant("<button  type=\"button\" title='" + title 
								+ "' tabindex=\"0\" class=\" " + cssClass + "\">Export MAT v5.0</button>");	
					}
				}
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Gets the check box cell.
	 *
	 * @return the check box cell
	 */
	private HasCell<Result, Boolean> getCheckBoxCell(){
		HasCell<Result, Boolean> hasCell = new HasCell<ManageMeasureSearchModel.Result, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(Result object) {
				boolean isSelected = false;
				if (selectedList.size() > 0) {
				for (int i = 0; i < selectedList.size(); i++) {
					if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
						isSelected = true;
						selectionModel.setSelected(object, isSelected);
						break;
					}
				}
			} else {
				isSelected = false;
				selectionModel.setSelected(object, isSelected);
				}
				return isSelected;			
			}
			@Override
			public FieldUpdater<Result, Boolean> getFieldUpdater() {
				return new FieldUpdater<Result, Boolean>() {
					@Override
					public void update(int index, Result object,
							Boolean isCBChecked) {
						if(isCBChecked)
							selectedList.add(object);
						else{
							for (int i = 0; i < selectedList.size(); i++) {
								if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									selectedList.remove(i);
									break;
								}
							}
						}
						selectionModel.setSelected(object, isCBChecked);
						observer.onExportSelectedClicked(object, isCBChecked);
					}
				};
			}
		};
		return hasCell;
	}
	
	
	/**
	 * Clear bulk export check boxes.
	 */
	public void clearBulkExportCheckBoxes(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedList);
		selectedList.clear();
		for (ManageMeasureSearchModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
		}
		table.redraw();
		observer.onClearAllBulkExportClicked();
	}
	
	
	/**
	 * Builds the cell table.
	 *
	 * @param results the results
	 * @param filter the filter
	 * @param searchText the search text
	 */
	public void buildCellTable(ManageMeasureSearchModel results,final int filter, final String searchText) {
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
			//cellTable = new CellTable<ManageMeasureSearchModel.Result>();
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
		            //Window.alert(caught.getMessage());
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
		        
		        MatContext
				.get()
				.getMeasureService()
				.search(searchText,start + 1, start + PAGE_SIZE, filter,callback);
		      }
		    };
		   
			
			provider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
			spager.setPageStart(0);
			buildCellTableCssStyle();
			spager.setDisplay(table);
			spager.setPageSize(PAGE_SIZE);
			table.setWidth("100%");
			 if(ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get()
						.getLoggedInUserRole())){
			    	table = addColumnToAdminTable(sortHandler);
			    }else{
			    	table = addColumnToTable();
			    }
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
					"In the following Measure List table, Measure Name is given in first column,"
							+ " Version in second column, Finalized Date in third column,"
							+ "History in fourth column, Edit in fifth column, Share in sixth column"
							+ "Clone in seventh column and Export in eight column.");
			table.getElement().setAttribute("id", "MeasureSearchCellTable");
			table.getElement().setAttribute("aria-describedby", "measureSearchSummary");
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 23.0, Unit.PCT);
			table.setColumnWidth(3, 2.0, Unit.PCT);
			table.setColumnWidth(4, 2.0, Unit.PCT);
			table.setColumnWidth(5, 2.0, Unit.PCT);
			table.setColumnWidth(6, 2.0, Unit.PCT);
			table.setColumnWidth(7, 22.0, Unit.PCT);
			    
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		}
		
		else{
			Label measureSearchHeader = new Label(getMeasureListLabel());
			measureSearchHeader.getElement().setId("measureSearchHeader_Label");
			measureSearchHeader.setStyleName("recentSearchHeader");
			measureSearchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No "+ getMeasureListLabel()+".</p>");
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
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName(),object.getOwnerfirstName()
							+ "  " + object.getOwnerLastName());
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
						return (o2 != null) ? o1.getOwnerfirstName().compareTo(o2.getOwnerfirstName()) : 1;
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
			Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
			Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) {
				@Override
				public String getValue(ManageMeasureSearchModel.Result object) {
					return "History";
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
	/**
	 * Builds the cell table css style.
	 */
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

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchView#asWidget()
	 */
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchView#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchView#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
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
	 * Gets the observer.
	 *
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}
	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
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
	 * Convert timestamp to string.
	 *
	 * @param ts - Timestamp.
	 * @return String.
	 */
	private String convertTimestampToString(Timestamp ts) {
		String tsStr;
		if (ts == null) {
			tsStr = "";
		} else {
			int hours = ts.getHours();
			String ap = hours < 12 ? "AM" : "PM";
			int modhours = hours % 12;
			String mins = ts.getMinutes() + "";
			if (mins.length() == 1) {
				mins = "0" + mins;
			}
			String hoursStr = modhours == 0 ? "12" : modhours+"";
			tsStr = (ts.getMonth() + 1) + "/" + ts.getDate() + "/" + (ts.getYear() + 1900) + " "
					+ hoursStr + ":" + mins + " "+ap;
		}
		return tsStr;
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
	
	/**
	 * Clear transfer check boxes.
	 */
	public void clearTransferCheckBoxes() {	
		for (ManageMeasureSearchModel.Result result : getSelectedList()) {
			result.setTransferable(false);
		}
		getSelectedList().clear();
		getData().setData(selectedMeasureList);
		table.redraw();
	}
}
