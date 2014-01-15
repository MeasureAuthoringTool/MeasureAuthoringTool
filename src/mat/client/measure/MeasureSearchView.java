package mat.client.measure;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.resource.CellTableResource;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

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
	/** The selected measure list. */
	private List<ManageMeasureSearchModel.Result> selectedMeasureList;
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	/** The observer. */
	private Observer observer;
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
	/**
	 * Measure Library Table Title.
	 */
	private String measureListLabel;
	/**
	 * MultiSelectionModel on Cell Table.
	 */
	private MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel;
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
	private CellTable<ManageMeasureSearchModel.Result> addColumnToTable() {
		Label measureSearchHeader = new Label(getMeasureListLabel());
		measureSearchHeader.getElement().setId("measureSearchHeader_Label");
		measureSearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(measureSearchHeader.getElement());
		selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		table.setSelectionModel(selectionModel);
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String cssClass = "customCascadeButton";
				if (object.isMeasureFamily()) {
					sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" >"
							+ "<button class='textEmptySpaces' disabled='disabled'></button>");
					sb.appendHtmlConstant("<span title=\" " + object.getName() + "\">" + object.getName() + "</span>");
					sb.appendHtmlConstant("</a>");
				} else {
					sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" >");
					sb.appendHtmlConstant("<button type=\"button\" title=\""
							+ object.getName() + "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
					sb.appendHtmlConstant("<span title=\" " + object.getName() + "\">" + object.getName() + "</span>");
					sb.appendHtmlConstant("</a>");
				}
				return sb.toSafeHtml();
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
				} else {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<span tabindex=\"-1\"><span>");
					return sb.toSafeHtml();
				}
			}
		};
		table.addColumn(finalizedDate, SafeHtmlUtils
				.fromSafeConstant("<span title='Finalized Date'>" + "Finalized Date"
						+ "</span>"));
		//History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
		Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton)
				{
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
						SafeHtmlBuilder sb = new SafeHtmlBuilder();
						String title;
						String cssClass;
						if (object.isEditable()) {
							title = "Edit";
							cssClass = "customEditButton";
							sb.appendHtmlConstant("<button type=\"button\" title='"
									+ title + "' tabindex=\"0\" class=\" " + cssClass + "\"></button>");
						} else {
							title = "ReadOnly";
							cssClass = "customReadOnlyButton";
							sb.appendHtmlConstant("<div title='" + title + "' class='" + cssClass + "'></div>");
						}
						return sb.toSafeHtml();
					}
				};
				editColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
					@Override
					public void update(int index, Result object,
							SafeHtml value) {
						if (object.isEditable()) {
							observer.onEditClicked(object);
						}
					}
				});
				table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));
				//Share
				Cell<String> shareButton = new MatButtonCell("Click to view sharable", "customShareButton");
				Column<Result, String> shareColumn = new Column<ManageMeasureSearchModel.Result, String>(shareButton) {
					@Override
					public String getValue(ManageMeasureSearchModel.Result object) {
						return "Share";
					}
				};
				shareColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, String value) {
						observer.onShareClicked(object);
					}
				});
				table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));
				//Clone
				Cell<String> cloneButton = new MatButtonCell("Click to view cloneable", "customCloneButton");
				Column<Result, String> cloneColumn = new Column<ManageMeasureSearchModel.Result, String>(cloneButton) {
					@Override
					public String getValue(ManageMeasureSearchModel.Result object) {
						return "Clone";
					}
				};
				cloneColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, String value) {
						observer.onCloneClicked(object);
					}
				});
				table.addColumn(cloneColumn, SafeHtmlUtils.fromSafeConstant("<span title='Clone'>" + "Clone" + "</span>"));
				//Export Column header
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
				bulkExportColumnHeader.setUpdater(new ValueUpdater<SafeHtml>() {
					@Override
					public void update(SafeHtml value) {
						clearBulkExportCheckBoxes();
					}
				});
				final List<HasCell<Result, ?>> cells = new LinkedList<HasCell<Result, ?>>();
				cells.add(new HasCell<Result, String>() {
					private Cell<String> exportButton = new MatButtonCell("Click to Export", "customExportButton");
					@Override
					public Cell<String> getCell() {
						return exportButton;
					}
					@Override
					public String getValue(Result object) {
						return "Export";
					}
					@Override
					public FieldUpdater<Result, String> getFieldUpdater() {
						return new FieldUpdater<Result, String>() {
							@Override
							public void update(int index, Result object, String value) {
								observer.onExportClicked(object);
							}
						};
					}
				});
				cells.add(new HasCell<Result, Boolean>() {
					private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
					@Override
					public Cell<Boolean> getCell() {
						return cell;
					}
					@Override
					public Boolean getValue(Result object) {
						return selectionModel.isSelected(object);
					}
					@Override
					public FieldUpdater<Result, Boolean> getFieldUpdater() {
						return new FieldUpdater<Result, Boolean>() {
							@Override
							public void update(int index, Result object,
									Boolean isCBChecked) {
								selectionModel.setSelected(object, isCBChecked);
								observer.onExportSelectedClicked(object, isCBChecked);
							}
						};
					}
				});
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
						sb.appendHtmlConstant("<td class=\"emptySpaces\">");
						if (object.isExportable()) {
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
				table.addColumn(new Column<Result, Result>(cell) {
					@Override
					public Result getValue(Result object) {
						return object;
					}
				}, bulkExportColumnHeader);
				return table;
	}
	/**
	 *
	 */
	public void clearBulkExportCheckBoxes(){
		List<Result> displayedItems = new ArrayList<Result>();
		displayedItems.addAll(selectedMeasureList);
		for (ManageMeasureSearchModel.Result msg : displayedItems) {
			selectionModel.setSelected(msg, false);
		}
		observer.onClearAllBulkExportClicked();
	}
	/**
	 * Builds the cell table.
	 *
	 * @param results the results
	 */
	public void buildCellTable(ManageMeasureSearchModel results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		table = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData());
		table.setRowData(selectedMeasureList);
		table.setPageSize(PAGE_SIZE);
		table.redraw();
		table.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData());
		table = addColumnToTable();
		sortProvider.addDataDisplay(table);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		buildCellTableCssStyle();
		spager.setDisplay(table);
		spager.setPageSize(PAGE_SIZE);
		table.setWidth("100%");
		table.setColumnWidth(0, 33.0, Unit.PCT);
		table.setColumnWidth(1, 15.0, Unit.PCT);
		table.setColumnWidth(2, 16.0, Unit.PCT);
		table.setColumnWidth(3, 5.0, Unit.PCT);
		table.setColumnWidth(4, 5.0, Unit.PCT);
		table.setColumnWidth(5, 5.0, Unit.PCT);
		table.setColumnWidth(6, 5.0, Unit.PCT);
		table.setColumnWidth(7, 20.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
				"In the following Measure List table, Measure Name is given in first column,"
						+ " Version in second column, Finalized Date in third column,"
						+ "History in fourth column, Edit in fifth column, Share in sixth column"
						+ "Clone in seventh column and Export in eight column.");
		table.getElement().setAttribute("id", "MeasureSearchCellTable");
		table.getElement().setAttribute("aria-describedby", "measureSearchSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(table);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
	}
	/**
	 * 
	 */
	private void buildCellTableCssStyle() {
		cellTableCssStyle = new ArrayList<String>();
		for (int i = 0; i < selectedMeasureList.size(); i++) {
			cellTableCssStyle.add(i, null);
		}
		table.setRowStyles(new RowStyles<ManageMeasureSearchModel.Result>() {
			@Override
			public String getStyleNames(ManageMeasureSearchModel.Result rowObject, int rowIndex) {
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
	 *Set measureListLabel.
	 * @param measureListLabel
	 */
	public void setMeasureListLabel(String measureListLabel) {
		this.measureListLabel = measureListLabel;
	}
	/**
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
}
