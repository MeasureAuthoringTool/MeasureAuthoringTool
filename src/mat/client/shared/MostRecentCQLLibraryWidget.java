package mat.client.shared;

import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.ClickableSafeHtmlCell;

/** The Class MostRecentMeasureWidget.
 * 
 * @author jnarang */
public class MostRecentCQLLibraryWidget extends Composite implements HasSelectionHandlers<CQLLibraryDataSetObject> {
	
	/** The Interface Observer.
	 * 
	 * @author jnarang. */
	public static interface Observer {

		void onEditClicked(CQLLibraryDataSetObject object);
		/** On export clicked.
		 * @param result the result */
	//	void onExportClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		//void onEditClicked(ManageMeasureSearchModel.Result result);
	}
	/** Cell Table Column Count. */
	private static final int MAX_TABLE_COLUMN_SIZE = 3;
	/** Cell Table Instance. */
	private CellTable<CQLLibraryDataSetObject> cellTable = new CellTable<CQLLibraryDataSetObject>();
	/** HandlerManager Instance. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** ManageMeasureSearchModel Instance. */
	private SaveCQLLibraryResult result;
	/** Observer Instance. */
	private Observer observer;
	/** VerticalPanel Instance which hold's View for Most Recent Measure. */
	private VerticalPanel searchPanel = new VerticalPanel();
	/** Method to Add Column's in Table.
	 * @param table the table
	 * @return the cell table */
	private CellTable<CQLLibraryDataSetObject> addColumnToTable(final CellTable<CQLLibraryDataSetObject> table) {
		if (table.getColumnCount() != MAX_TABLE_COLUMN_SIZE) {
			Label searchHeader = new Label("Recent Activity");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<CQLLibraryDataSetObject, SafeHtml> libraryName =
					new Column<CQLLibraryDataSetObject, SafeHtml>(new
							ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" tabindex=\"-1\">");
					sb.appendHtmlConstant("<span id='div2' title=\" " + object.getCqlName() + "\" tabindex=\"0\">" + object.getCqlName() + "</span>");
					sb.appendHtmlConstant("</a></div>");
					return sb.toSafeHtml();
				}
			};
			libraryName.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
				@Override
				public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
					SelectionEvent.fire(MostRecentCQLLibraryWidget.this, object);
				}
			});
			table.addColumn(libraryName, SafeHtmlUtils.fromSafeConstant(
					"<span title='CQL Library Name Column'>" + "CQL Library Name" + "</span>"));
			Column<CQLLibraryDataSetObject, SafeHtml> version =
					new Column<CQLLibraryDataSetObject, SafeHtml>(
							new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(version, SafeHtmlUtils.fromSafeConstant(
					"<span title='Version'>" + "Version" + "</span>"));
			//Edit
			Column<CQLLibraryDataSetObject, SafeHtml> editColumn =
					new Column<CQLLibraryDataSetObject, SafeHtml>(
							new ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String title;
					String cssClass = "btn btn-link";
					String iconCss;
					if (object.isEditable()) {
						if (object.isLocked()) {
							String emailAddress = object.getLockedUserInfo().getEmailAddress();
							title = "Library in use by " + emailAddress;
							iconCss = "fa fa-lock fa-lg";
						} else {
							title = "Edit";
							iconCss = "fa fa-pencil fa-lg";
							
						}
						sb.appendHtmlConstant("<button type=\"button\" title='"
								+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
					} else {
						title = "Read-Only";
						iconCss = "fa fa-newspaper-o fa-lg";
						sb.appendHtmlConstant("<button type=\"button\" title='"
								+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Read-Only</span></button>");
					}
					
					return sb.toSafeHtml();
				}
			};
			editColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, SafeHtml>() {
				@Override
				public void update(int index, CQLLibraryDataSetObject object, SafeHtml value) {
					if (object.isEditable() && !object.isLocked()) {
						observer.onEditClicked(object);
					}
				}
			});
			table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));
			
			table.setColumnWidth(0, 50.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 25.0, Unit.PCT);
			
		}
		return table;
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler
	 * (com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<CQLLibraryDataSetObject> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	/** Method to create Recent Measure Cell Table. */
	void buildCellTable() {
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<CQLLibraryDataSetObject> sortProvider = new ListDataProvider<CQLLibraryDataSetObject>();
		cellTable.setPageSize(2);
		cellTable.redraw();
		cellTable.setRowCount(result.getCqlLibraryDataSetObjects().size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"recentActivitySummary",
						"In the following Recent Activity table, CQL Library Name is given in first column,"
								+ " Version in second column.");
		cellTable.getElement().setAttribute("id", "MostRecentActivityCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "recentActivitySummary");
		searchPanel.add(invisibleLabel);
		searchPanel.add(cellTable);
	}
	/** Builds the most recent widget.
	 * @return VerticalPanel. */
	public VerticalPanel buildMostRecentWidget() {
		searchPanel.clear();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("recentSearchPanel");
		// searchPanel.getElement().setAttribute("tabIndex", "0");
		/*
		 * Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel(new Label("RecentActivityTable"), "RecentActivityTable");
		 * searchPanel.add(invisibleLabel); Element element = searchPanel.getElement(); element.setAttribute("aria-role", "panel");
		 * element.setAttribute("aria-labelledby", "RecentActivityTable"); element.setAttribute("aria-live", "assertive");
		 * element.setAttribute("aria-atomic", "true"); element.setAttribute("aria-relevant", "all"); element.setAttribute("role", "alert");
		 */
		if ((result != null) && (result.getCqlLibraryDataSetObjects().size() > 0)) {
			buildCellTable();
		} else {
			Label searchHeader = new Label("Recent Activity");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Recent Activity</p>");
			searchPanel.add(searchHeader);
			searchPanel.add(new SpacerWidget());
			searchPanel.add(desc);
		}
		return searchPanel;
	}
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.Widget#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	
	/** Gets the manageMeasureSearchModel Instance.
	 * 
	 * @return the measureSearchModel */
	public SaveCQLLibraryResult getResult() {
		return result;
	}
	
	/** Gets the select id for edit tool.
	 * 
	 * @return the select id for edit tool */
	public HasSelectionHandlers<CQLLibraryDataSetObject> getSelectIdForEditTool() {
		return this;
	}
	
	/** Sets the manageMeasureSearchModel Instance.
	 * 
	 * @param measureSearchModel the measureSearchModel to set */
	public void setResult(SaveCQLLibraryResult result) {
		this.result = result;
	}
	/** Sets the observer.
	 * @param observer the new observer */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
