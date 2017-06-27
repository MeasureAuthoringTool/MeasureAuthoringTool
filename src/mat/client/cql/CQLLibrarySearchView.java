package mat.client.cql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.ButtonCell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.CustomPager;
import mat.client.clause.cqlworkspace.DeleteCQLLibraryConfirmDialogBox;
import mat.client.measure.service.SaveCQLLibraryResult;
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
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;

// TODO: Auto-generated Javadoc
/**
 * The Class CQLLibrarySearchView.
 */
public class CQLLibrarySearchView implements HasSelectionHandlers<CQLLibraryDataSetObject> {

	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;
	/** The selected libraries list. */
	private List<CQLLibraryDataSetObject> availableLibrariesList;
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** The data. */
	private CQLLibraryDataSetObject data = new CQLLibraryDataSetObject();
	/** The observer. */
	private Observer observer;
	/** The admin observer. */
	private AdminObserver adminObserver;
	/** The table. */
	private CellTable<CQLLibraryDataSetObject> table;
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

	/** The library list label. */
	private String cqlLibraryListLabel;
	/**
	 * MultiSelectionModel on Cell Table.
	 */
	private MultiSelectionModel<CQLLibraryDataSetObject> selectionModel;
	/** The selected list. */
	List<CQLLibraryDataSetObject> selectedList;
	
	/** The delete CQL library confirm dialog box. */
	private DeleteCQLLibraryConfirmDialogBox deleteCQLLibraryConfirmDialogBox = new DeleteCQLLibraryConfirmDialogBox();

	/**
	 * The Interface Observer.
	 */
	public static interface Observer {

		/**
		 * On share clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onShareClicked(CQLLibraryDataSetObject result);

		/**
		 * On history clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(CQLLibraryDataSetObject result);
		
		/**
		 * On create clicked.
		 *
		 * @param object the object
		 */
		void onCreateClicked(CQLLibraryDataSetObject object);
		
		/**
		 * On delete clicked.
		 *
		 * @param object the object
		 */
		void onDeleteClicked(CQLLibraryDataSetObject object);

	}

	/**
	 * The Interface Observer.
	 */
	public static interface AdminObserver {

		/**
		 * On history clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onHistoryClicked(CQLLibraryDataSetObject result);

		/**
		 * This method is called when information about an Admin
		 * which was previously requested using an asynchronous
		 * interface becomes available.
		 *
		 * @param result the result
		 */
		void onTransferSelectedClicked(CQLLibraryDataSetObject result);

	}

	/**
	 * Builds the CQL library cell table.
	 *
	 * @return the flow panel
	 */
	public FlowPanel buildCQLLibraryCellTable() {
		mainPanel.clear();
		mainPanel.getElement().setId("cqlLibrarySearchView_mainPanel");
		mainPanel.setStylePrimaryName("measureSearchResultsContainer");
		mainPanel.add(new SpacerWidget());
		mainPanel.getElement().setId("cqlCellTablePanel_VerticalPanel");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("serachView_mainPanel");
		return mainPanel;
	}

	/**
	 * Builds the cell table.
	 *
	 * @param result            the result
	 * @param searchText            the search text
	 * @param filter the filter
	 */
	public void buildCellTable(SaveCQLLibraryResult result, final String searchText, final int filter) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if ((result != null) && (result.getCqlLibraryDataSetObjects().size() > 0)) {
			table = new CellTable<CQLLibraryDataSetObject>(PAGE_SIZE, (Resources) GWT.create(CellTableResource.class));
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			selectedList = new ArrayList<CQLLibraryDataSetObject>();
			availableLibrariesList = new ArrayList<CQLLibraryDataSetObject>();
			availableLibrariesList.addAll(result.getCqlLibraryDataSetObjects());
			table.setRowData(availableLibrariesList);
			table.setRowCount(result.getResultsTotal(), true);
			table.setPageSize(PAGE_SIZE);
			table.redraw();

			AsyncDataProvider<CQLLibraryDataSetObject> provider = new AsyncDataProvider<CQLLibraryDataSetObject>() {
				@Override
				protected void onRangeChanged(HasData<CQLLibraryDataSetObject> display) {
					final int start = display.getVisibleRange().getStart();
					index = start;
					AsyncCallback<SaveCQLLibraryResult> callback = new AsyncCallback<SaveCQLLibraryResult>() {
						@Override
						public void onFailure(Throwable caught) {
							Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						}

						@Override
						public void onSuccess(SaveCQLLibraryResult result) {
							List<CQLLibraryDataSetObject> manageCQLLibrarySearchList = new ArrayList<CQLLibraryDataSetObject>();
							manageCQLLibrarySearchList.addAll(result.getCqlLibraryDataSetObjects());
							availableLibrariesList = manageCQLLibrarySearchList;
							buildCellTableCssStyle();
							updateRowData(start, manageCQLLibrarySearchList);
						}
					};

					MatContext.get().getCQLLibraryService().search(searchText, filter, index + 1, index + PAGE_SIZE,
							callback);
				}
			};

			provider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,
					"cqlLib");
			spager.setPageStart(0);
			buildCellTableCssStyle();
			spager.setDisplay(table);
			spager.setPageSize(PAGE_SIZE);
			table.setWidth("100%");
			if (ClientConstants.ADMINISTRATOR.equalsIgnoreCase(MatContext.get().getLoggedInUserRole())) {
				addColumnToAdminTable();
				Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("CQLLibraryTransferOwnershipSummary",
						"In the following CQL Library Transfer Ownership Cell table, CQL Library Name is given in first column,"
								+ " Owner in second column, Owner E-mail Address in third column,"
								+ "History in fourth column, Transfer Check box in fifth column");
				table.getElement().setAttribute("id", "CQLLibrarySearchCellTable");
				table.getElement().setAttribute("aria-describedby", "CQLLibraryTransferOwnershipSummary");
				table.setColumnWidth(0, 50.0, Unit.PCT);
				table.setColumnWidth(1, 15.0, Unit.PCT);
				table.setColumnWidth(2, 15.0, Unit.PCT);
				table.setColumnWidth(3, 5.0, Unit.PCT);
				table.setColumnWidth(4, 5.0, Unit.PCT);

				cellTablePanel.add(invisibleLabel);

			} else {
				table = addColumnToTable();
				Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("CQLLibrarySearchSummary",
						"In the following CQL Library Cell table, CQL Library Name is given in first column,"
								+ " Version in second column, Create Version or Draft in third column,"
								+ "History in fourth column, Share in fifth column");
				table.getElement().setAttribute("id", "CQLLibrarySearchCellTable");
				table.getElement().setAttribute("aria-describedby", "CQLLibrarySearchSummary");
				table.setColumnWidth(0, 50.0, Unit.PCT);
				table.setColumnWidth(1, 15.0, Unit.PCT);
				table.setColumnWidth(2, 15.0, Unit.PCT);
				table.setColumnWidth(3, 5.0, Unit.PCT);
				table.setColumnWidth(4, 5.0, Unit.PCT);
			
				cellTablePanel.add(invisibleLabel);
			}

			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		} else {
			Label cqlLibrarySearchHeader = new Label(getCQLlibraryListLabel());
			cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
			cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
			cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No " + getCQLlibraryListLabel() + ".</p>");
			cellTablePanel.add(cqlLibrarySearchHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);

		}
	}

	/**
	 * Adds the column to table For Non Admin.
	 *
	 * @return the cell table
	 */
	private CellTable<CQLLibraryDataSetObject> addColumnToAdminTable() {
		Label cqlLibrarySearchHeader = new Label(getCQLlibraryListLabel());
		cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
		cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cqlLibrarySearchHeader.getElement());
		/*
		 * selectionModel = new MultiSelectionModel<CQLLibraryDataSetObject>();
		 * table.setSelectionModel(selectionModel);
		 */

		// CQL Library Name Column
		Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getCQLLibraryNameColumnToolTip(object);
			}
		};
		/*
		 * cqlLibraryName.setFieldUpdater(new
		 * FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
		 * 
		 * @Override public void update(int index, CQLLibraryDataSetObject
		 * object, SafeHtml value) {
		 * SelectionEvent.fire(CQLLibrarySearchView.this, object); } });
		 */
		table.addColumn(cqlLibraryName,
				SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

		// Version Column
		Column<CQLLibraryDataSetObject, SafeHtml> ownerName = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return CellTableUtility.getColumnToolTip(object.getOwnerFirstName() + "  " + object.getOwnerLastName(),
						object.getOwnerFirstName() + "  " + object.getOwnerLastName());
			}
		};
		table.addColumn(ownerName,
				SafeHtmlUtils.fromSafeConstant("<span title='ownerName'>" + "Owner Name" + "</span>"));

		// Finalized Date
		Column<CQLLibraryDataSetObject, SafeHtml> emailAddress = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return CellTableUtility.getColumnToolTip(object.getOwnerEmailAddress());

			}
		};
		table.addColumn(emailAddress, SafeHtmlUtils
				.fromSafeConstant("<span title='Owner E-mail Address'>" + "Owner E-mail Address" + "</span>"));

		// History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
		Column<CQLLibraryDataSetObject, String> historyColumn = new Column<CQLLibraryDataSetObject, String>(
				historyButton) {
			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				return "History";
			}
		};
		historyColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, String>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, String value) {
				adminObserver.onHistoryClicked(object);
			}
		});
		table.addColumn(historyColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));

		Cell<Boolean> transferCB = new MatCheckBoxCell();
		Column<CQLLibraryDataSetObject, Boolean> transferColumn = new Column<CQLLibraryDataSetObject, Boolean>(
				transferCB) {
			@Override
			public Boolean getValue(CQLLibraryDataSetObject object) {
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
		transferColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, Boolean value) {
				if (value) {
					if (!selectedList.contains(object)) {
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
		table.addColumn(transferColumn,
				SafeHtmlUtils.fromSafeConstant("<span title=\"Check for Ownership Transfer\">" + "Transfer </span>"));
		return table;
	}

	/**
	 * Adds the column to table For Non Admin.
	 *
	 * @return the cell table
	 */
	private CellTable<CQLLibraryDataSetObject> addColumnToTable() {
		Label cqlLibrarySearchHeader = new Label(getCQLlibraryListLabel());
		cqlLibrarySearchHeader.getElement().setId("cqlLibrarySearchHeader_Label");
		cqlLibrarySearchHeader.setStyleName("recentSearchHeader");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		cqlLibrarySearchHeader.getElement().setAttribute("tabIndex", "0");
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(cqlLibrarySearchHeader.getElement());
		selectionModel = new MultiSelectionModel<CQLLibraryDataSetObject>();
		table.setSelectionModel(selectionModel);

		// CQL Library Name Column
		Column<CQLLibraryDataSetObject, SafeHtml> cqlLibraryName = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getCQLLibraryNameColumnToolTip(object);
			}
		};
		cqlLibraryName.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
				SelectionEvent.fire(CQLLibrarySearchView.this, object);
			}
		});
		table.addColumn(cqlLibraryName,
				SafeHtmlUtils.fromSafeConstant("<span title='CQL Library Name'>" + "CQL Library Name" + "</span>"));

		// Version Column
		Column<CQLLibraryDataSetObject, SafeHtml> version = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		table.addColumn(version, SafeHtmlUtils.fromSafeConstant("<span title='Version'>" + "Version" + "</span>"));

		ButtonCell buttonCell = new ButtonCell(ButtonType.LINK);
		Column<CQLLibraryDataSetObject,String> draftOrVersionCol = new Column<CQLLibraryDataSetObject, String>(buttonCell) {

			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				// TODO Auto-generated method stub
				return object.getCqlName();
			}
			
			@Override
			public void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb) {
				if (object.isDraftable()) {
					sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" title =\"Click to create draft\" tabindex=\"0\">");
					sb.appendHtmlConstant("<i class=\"fa fa-pencil-square-o fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Draft</span>");
					sb.appendHtmlConstant("</button>");
				} else if (object.isVersionable()) {
					sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" tabindex=\"0\" title =\"Click to create version\" style=\"color: goldenrod;\" >");
					sb.appendHtmlConstant("<i class=\"fa fa-star fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Version</span>");
					sb.appendHtmlConstant("</button>");
				}
				
			}
			@Override
			public void onBrowserEvent(Context context, Element elem, CQLLibraryDataSetObject object,
					NativeEvent event) {
				// TODO Auto-generated method stub
				//super.onBrowserEvent(context, elem, object, event);
				String type = event.getType();
				if(type.equalsIgnoreCase(BrowserEvents.CLICK)){
					if(!object.isDraftable() && !object.isVersionable()){
						event.preventDefault();
					} else {
						observer.onCreateClicked(object);
					}
				} else {
					if(!object.isDraftable() && !object.isVersionable()){
						event.preventDefault();
					}
				}
				
			}
			
		};
		
		table.addColumn(draftOrVersionCol,
				SafeHtmlUtils.fromSafeConstant("<span title='Create Version/Draft'>" + "Create Version/Draft" + "</span>"));
		
		//Delete Column
		ButtonCell deleteButtonCell = new ButtonCell(ButtonType.LINK);
		Column<CQLLibraryDataSetObject,String> deleteColumn = new Column<CQLLibraryDataSetObject, String>(deleteButtonCell) {

			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				return object.getCqlName();
			}
			
			@Override
			public void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb) {
				if (object.isDeletable()) {
					sb.appendHtmlConstant("<button class=\"btn btn-link\" type=\"button\" title =\"Click to delete library\" tabindex=\"0\">");
					sb.appendHtmlConstant("<i class=\"fa fa-trash fa-lg\" style=\"margin-left: 15px;\"></i>");
					sb.appendHtmlConstant("<span class=\"invisibleButtonText\">Create Draft</span>");
					sb.appendHtmlConstant("</button>");
				}
			}
			@Override
			public void onBrowserEvent(Context context, Element elem, CQLLibraryDataSetObject object,
					NativeEvent event) {
				String type = event.getType();
				if(type.equalsIgnoreCase(BrowserEvents.CLICK)){
					if(!object.isDeletable()){
						event.preventDefault();
					} else {
						observer.onDeleteClicked(object);
					}
				} else {
					if(!object.isDeletable()){
						event.preventDefault();
					}
				}
			}
			
		};
		
		table.addColumn(deleteColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete" + "</span>"));

		
		
		// History
		Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
		Column<CQLLibraryDataSetObject, String> historyColumn = new Column<CQLLibraryDataSetObject, String>(
				historyButton) {
			@Override
			public String getValue(CQLLibraryDataSetObject object) {
				return "History";
			}
		};
		historyColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, String>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, String value) {
				observer.onHistoryClicked(object);
			}
		});
		table.addColumn(historyColumn,
				SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));

		// Share
		Column<CQLLibraryDataSetObject, SafeHtml> shareColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				return getShareColumnToolTip(object);
			}
		};
		shareColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
				if (object.isSharable())
					observer.onShareClicked(object);
			}
		});
		table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));

		return table;
	}

	/**
	 * Gets the history column tool tip.
	 *
	 * @param object
	 *            the object
	 * @return the history column tool tip
	 */
	private SafeHtml getShareColumnToolTip(CQLLibraryDataSetObject object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String title;
		String cssClass;
		if (object.isSharable()) {
			title = "Shareable";
			cssClass = "customShareButton";
			sb.appendHtmlConstant("<button type=\"button\" title='" + title + "' tabindex=\"0\" class=\" " + cssClass
					+ "\">Shareable</button>");
		} else {
			title = "Shareable";
			cssClass = "customGrayedShareButton";
			sb.appendHtmlConstant("<button type=\"button\" title='" + title + "' tabindex=\"0\" class=\" " + cssClass
					+ "\" disabled>Shareable</button>");
		}

		return sb.toSafeHtml();
	}

	/**
	 * Builds the cell table css style.
	 */
	private void buildCellTableCssStyle() {
		cellTableCssStyle = new ArrayList<String>();
		for (int i = 0; i < availableLibrariesList.size(); i++) {
			cellTableCssStyle.add(i, null);
		}
		table.setRowStyles(new RowStyles<CQLLibraryDataSetObject>() {
			@Override
			public String getStyleNames(CQLLibraryDataSetObject rowObject, int rowIndex) {
				if (rowIndex > PAGE_SIZE - 1) {
					rowIndex = rowIndex - index;
				}
				if (rowIndex != 0) {
					if (cellTableCssStyle.get(rowIndex) == null) {
						if (even) {
							if (rowObject.getCqlSetId()
									.equalsIgnoreCase(availableLibrariesList.get(rowIndex - 1).getCqlSetId())) {
								even = true;
								cellTableCssStyle.add(rowIndex, cellTableOddRow);
								return cellTableOddRow;
							} else {
								even = false;
								cellTableCssStyle.add(rowIndex, cellTableEvenRow);
								return cellTableEvenRow;
							}
						} else {
							if (rowObject.getCqlSetId()
									.equalsIgnoreCase(availableLibrariesList.get(rowIndex - 1).getCqlSetId())) {
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
	 * Gets the CQL Library name column tool tip.
	 *
	 * @param object
	 *            the object
	 * @return the CQL Library name column tool tip
	 */
	private SafeHtml getCQLLibraryNameColumnToolTip(CQLLibraryDataSetObject object) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		String cssClass = "customCascadeButton";
		if (object.isFamily()) {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\">"
					+ "<button id='div1' class='textEmptySpaces' tabindex=\"-1\" disabled='disabled'></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getCqlName() + "\" tabindex=\"0\">"
					+ object.getCqlName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		} else {
			sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
					+ "style=\"text-decoration:none\" tabindex=\"-1\" >");
			sb.appendHtmlConstant("<button id='div1' type=\"button\" title=\"" + object.getCqlName()
					+ "\" tabindex=\"-1\" class=\" " + cssClass + "\"></button>");
			sb.appendHtmlConstant("<span id='div2' title=\" " + object.getCqlName() + "\" tabindex=\"0\">"
					+ object.getCqlName() + "</span>");
			sb.appendHtmlConstant("</a></div>");
		}
		return sb.toSafeHtml();
	}

	/**
	 * Convert timestamp to string.
	 *
	 * @param ts
	 *            - Timestamp.
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
			String hoursStr = modhours == 0 ? "12" : modhours + "";
			tsStr = (ts.getMonth() + 1) + "/" + ts.getDate() + "/" + (ts.getYear() + 1900) + " " + hoursStr + ":" + mins
					+ " " + ap;
		}
		return tsStr;
	}

	/**
	 * Gets the cql Library List Label.
	 *
	 * @return the cql Library List Label
	 */
	public String getCQLlibraryListLabel() {
		return cqlLibraryListLabel;
	}

	/**
	 * Sets the cqlLibrary list label.
	 *
	 * @param cqlLibraryListLabel
	 *            the new cqlLibrary list label
	 */
	public void setCQLLibraryListLabel(String cqlLibraryListLabel) {
		this.cqlLibraryListLabel = cqlLibraryListLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event.
	 * shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#
	 * addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<CQLLibraryDataSetObject> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return mainPanel;
	}

	/**
	 * Gets the cell table panel.
	 *
	 * @return the cell table panel
	 */
	public VerticalPanel getCellTablePanel() {
		return cellTablePanel;
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
	 * Gets the admin observer.
	 *
	 * @return the admin observer
	 */
	public AdminObserver getAdminObserver() {
		return adminObserver;
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
	 * Gets the available libraries list.
	 *
	 * @return the available libraries list
	 */
	public List<CQLLibraryDataSetObject> getAvailableLibrariesList() {
		return availableLibrariesList;
	}

	/**
	 * Gets the table.
	 *
	 * @return the table
	 */
	public CellTable<CQLLibraryDataSetObject> getTable() {
		return table;
	}

	/**
	 * Sets the available libraries list.
	 *
	 * @param availableLibrariesList the new available libraries list
	 */
	public void setAvailableLibrariesList(List<CQLLibraryDataSetObject> availableLibrariesList) {
		this.availableLibrariesList = availableLibrariesList;
	}

	/**
	 * Gets the delete CQL library confirm dialog box.
	 *
	 * @return the delete CQL library confirm dialog box
	 */
	public DeleteCQLLibraryConfirmDialogBox getDeleteCQLLibraryConfirmDialogBox() {
		return deleteCQLLibraryConfirmDialogBox;
	}

	/**
	 * Sets the delete CQL library confirm dialog box.
	 *
	 * @param deleteCQLLibraryConfirmDialogBox the new delete CQL library confirm dialog box
	 */
	public void setDeleteCQLLibraryConfirmDialogBox(DeleteCQLLibraryConfirmDialogBox deleteCQLLibraryConfirmDialogBox) {
		this.deleteCQLLibraryConfirmDialogBox = deleteCQLLibraryConfirmDialogBox;
	}
}
