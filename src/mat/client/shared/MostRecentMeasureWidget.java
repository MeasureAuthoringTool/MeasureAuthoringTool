package mat.client.shared;

import java.util.ArrayList;

import com.google.gwt.cell.client.Cell;
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

import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

/** The Class MostRecentMeasureWidget.
 * 
 * @author jnarang */
public class MostRecentMeasureWidget extends Composite implements HasSelectionHandlers<ManageMeasureSearchModel.Result> {
	
	/** The Interface Observer.
	 * 
	 * @author jnarang. */
	public static interface Observer {
		/** On export clicked.
		 * @param result the result */
		void onExportClicked(ManageMeasureSearchModel.Result result);
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		void onEditClicked(ManageMeasureSearchModel.Result result);
	}
	/** Cell Table Column Count. */
	private static final int MAX_TABLE_COLUMN_SIZE = 4;
	/** Cell Table Instance. */
	private CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
	/** HandlerManager Instance. */
	private HandlerManager handlerManager = new HandlerManager(this);
	/** ManageMeasureSearchModel Instance. */
	private ManageMeasureSearchModel measureSearchModel;
	/** Observer Instance. */
	private Observer observer;
	/** VerticalPanel Instance which hold's View for Most Recent Measure. */
	private VerticalPanel searchPanel = new VerticalPanel();
	/** Method to Add Column's in Table.
	 * @param table the table
	 * @return the cell table */
	private CellTable<Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table) {
		if (table.getColumnCount() != MAX_TABLE_COLUMN_SIZE) {
			Label searchHeader = new Label("Recent Activity");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			Column<ManageMeasureSearchModel.Result, SafeHtml> measureName =
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(new
							ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					sb.appendHtmlConstant("<div id='container' tabindex=\"-1\"><a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" tabindex=\"-1\">");
					sb.appendHtmlConstant("<span id='div2' title=\" " + object.getName() + "\" tabindex=\"0\">" + object.getName() + "</span>");
					sb.appendHtmlConstant("</a></div>");
					return sb.toSafeHtml();
				}
			};
			measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
					SelectionEvent.fire(MostRecentMeasureWidget.this, object);
				}
			});
			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant(
					"<span title='Measure Name Column'>" + "Measure Name" + "</span>"));
			Column<ManageMeasureSearchModel.Result, SafeHtml> version =
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(
							new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(version, SafeHtmlUtils.fromSafeConstant(
					"<span title='Version'>" + "Version" + "</span>"));
			//Edit
			Column<ManageMeasureSearchModel.Result, SafeHtml> editColumn =
					new Column<ManageMeasureSearchModel.Result, SafeHtml>(
							new ClickableSafeHtmlCell()) {
				@Override
				public SafeHtml getValue(Result object) {
					/*SafeHtmlBuilder sb = new SafeHtmlBuilder();
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
					}*/
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String title;
					String cssClass = "btn btn-link";
					String iconCss;
					if (object.isEditable()) {
						if (object.isMeasureLocked()) {
							String emailAddress = object.getLockedUserInfo().getEmailAddress();
							title = "Measure in use by " + emailAddress;
							//cssClass = "customLockedButton";
							iconCss = "fa fa-lock fa-lg";
						} else {
							title = "Edit";
							//cssClass = "customEditButton";
							iconCss = "fa fa-pencil fa-lg";
							
						}
						sb.appendHtmlConstant("<button type=\"button\" title='"
								+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
						//<span class=\"invisibleButtonText\">Edit</span>
					} else {
						title = "Read-Only";
						//cssClass = "customReadOnlyButton";
						iconCss = "fa fa-newspaper-o fa-lg";
						sb.appendHtmlConstant("<button type=\"button\" title='"
								+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Read-Only</span></button>");
						//<span class=\"invisibleButtonText\">Read-Only</span>
					}
					
					return sb.toSafeHtml();
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
			
			Cell<SafeHtml> exportButton = new ClickableSafeHtmlCell();
			Column<Result,SafeHtml> exportColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(exportButton) {

				@Override
				public SafeHtml getValue(Result object) {
					/*SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String title = "";
					String cssClass = "";
					if ((object != null) && object.isExportable() && (object.getHqmfReleaseVersion() != null)) {
						if (object.getHqmfReleaseVersion().equals("v3")) {
							title = "Click to Export MATv3";
							cssClass = "customExportButton";
							sb.appendHtmlConstant("<button type=\"button\" title='" + title
									+ "' tabindex=\"0\" class=\" " + cssClass + "\">Click to Export MATv3</button>");
						} else {
							cssClass = "customExportButtonRed";
							title = "Click to Export MAT " + object.getHqmfReleaseVersion();
							sb.appendHtmlConstant(
									"<button  type=\"button\" title='" + title + "' tabindex=\"0\" class=\" " + cssClass
											+ "\">Export MAT " + object.getHqmfReleaseVersion() + "</button>");
						}
					}*/
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					String title = "";
					String cssClass = "btn btn-link";
					String iconClass = "fa fa-download fa-lg";
					if((object != null) && object.isExportable() && (object.getHqmfReleaseVersion() != null)) {
						if(object.getHqmfReleaseVersion().equalsIgnoreCase("v3")){
							//cssClass = "customExportButton";
							title = "Click to Export MAT v3";
							sb.appendHtmlConstant("<button type=\"button\" title='" + title 
									+ "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: gray;\"/> <i class=\" " + iconClass  + "\"></i><span style=\"font-size:0;\">Export MAT v3 </span></button>");	
							//<span class=\"invisibleButtonText\">Export MAT v3</span>
						} else {
							//cssClass = "customExportButtonRed";
							title = "Click to Export MAT " +  object.getHqmfReleaseVersion();
							sb.appendHtmlConstant("<button  type=\"button\" title='" + title 
									+ "' tabindex=\"0\" class=\" " + cssClass + "\" ><i class=\" " + iconClass  + "\"></i><span style=\"font-size:0;\">"+"Export MAT "+object.getHqmfReleaseVersion()+"</span></button>");
							//<span class=\"invisibleButtonText\">Export MAT "+object.getHqmfReleaseVersion()+"</span>
						}
					}
					return sb.toSafeHtml();
					}
				};
			exportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
				@Override
				public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
					if ((object != null) && object.isExportable()) {
						observer.onExportClicked(object);
					}
				}
			});
			table.addColumn(exportColumn,
					SafeHtmlUtils.fromSafeConstant("<span title='Export'>"
							+ "Export" + "</span>"));
			
			table.setColumnWidth(0, 50.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 5.0, Unit.PCT);
			table.setColumnWidth(3, 20.0, Unit.PCT);
		}
		return table;
	}
	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler
	 * (com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	/** Method to create Recent Measure Cell Table. */
	void buildCellTable() {
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
		ArrayList<ManageMeasureSearchModel.Result> selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(measureSearchModel.getData());
		cellTable.setPageSize(2);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(measureSearchModel.getData());
		cellTable = addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"recentActivitySummary",
						"In the following Recent Activity table, Measure Name is given in first column,"
								+ " Version in second column and Export in third column.");
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
		if ((measureSearchModel != null) && (measureSearchModel.getData().size() > 0)) {
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
	public ManageMeasureSearchModel getMeasureSearchModel() {
		return measureSearchModel;
	}
	
	/** Gets the select id for edit tool.
	 * 
	 * @return the select id for edit tool */
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	
	/** Sets the manageMeasureSearchModel Instance.
	 * 
	 * @param measureSearchModel the measureSearchModel to set */
	public void setMeasureSearchModel(ManageMeasureSearchModel measureSearchModel) {
		this.measureSearchModel = measureSearchModel;
	}
	/** Sets the observer.
	 * @param observer the new observer */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
