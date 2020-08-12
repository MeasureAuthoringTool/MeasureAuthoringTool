package mat.client.admin.reports;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import mat.client.shared.ContentWithHeadingWidget;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;

import java.util.ArrayList;
import java.util.List;


public class ManageAdminReportingView implements ManageAdminReportingPresenter.Display {
	private static final int PAGE_SIZE = 5;
	public static interface Observer {
		
		/**
		 * generateActiveUserReport.
		 * @param model
		 *            the ReportModel
		 */
		void generateReport(ReportModel model);
	}
	/** The container panel. */
	private ContentWithHeadingWidget containerPanel = new ContentWithHeadingWidget();
	/** Flow Panel.**/
	private FlowPanel mainPanel = new FlowPanel();
	/** The observer. */
	private Observer observer;
	/**
	 * Constructor.
	 */
	public ManageAdminReportingView() {
		CellTable<ReportModel> table = new CellTable<ReportModel>();
		List<ReportModel> modelList = createReportModelList();
		ListDataProvider<ReportModel> listDataProvider = new ListDataProvider<ReportModel>();
		table.setPageSize(PAGE_SIZE);
		table.redraw();
		listDataProvider.refresh();
		listDataProvider.getList().addAll(modelList);
		addColumnToTable(table);
		listDataProvider.addDataDisplay(table);
		VerticalPanel cellTablePanel = new VerticalPanel();
		cellTablePanel.setStyleName("cellTablePanel_Report");
		Label invisibleLabel = (Label) LabelBuilder
				.buildInvisibleLabel(
						"adminReportTableSummary",
						"In the Following Report table Report Name in First Column"
								+ "Generate CSV in Second Column.");
		table.getElement().setAttribute("id", "reportQDMTable");
		table.getElement().setAttribute("aria-describedby",
				"adminReportTableSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(table);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		mainPanel.setStyleName("contentPanel");
		containerPanel.setContent(mainPanel);
		containerPanel.setHeading("Administrator Reports", "AdminReports");
	}
	/**
	 * Add Columns to Report Table.
	 * @param table - CellTable.
	 */
	private void addColumnToTable(CellTable<ReportModel> table) {
		Label tableHeader = new Label("Generate Administrator Reports");
		tableHeader.getElement().setId("AdminReportHeader_Label");
		tableHeader.setStyleName("measureGroupingTableHeader");
		tableHeader.getElement().setAttribute("tabIndex", "0");
		com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
		TableCaptionElement caption = elem.createCaption();
		caption.appendChild(tableHeader.getElement());
		// Name Column
		Column<ReportModel, SafeHtml> nameColumn = new Column<ReportModel, SafeHtml>(
				new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ReportModel object) {
				StringBuilder title = new StringBuilder();
				String value = null;
				value = object.getReportName();
				title = title.append("Report Name : ").append(value);
				return CellTableUtility.getColumnToolTip(value,
						title.toString());
			}
		};
		table.addColumn(nameColumn, SafeHtmlUtils
				.fromSafeConstant("<span title=\"Report Name\">" + "Report Name"
						+ "</span>"));
		 MatButtonCell buttonCell = new MatButtonCell("Generate CSV Report", "btn btn-link", "fa fa-file-text", "Generate CSV Report");
		Column<ReportModel, String> buttonColumn = new Column<ReportModel, String>(buttonCell) {
			@Override
			public String getValue(ReportModel object) {
				// The value to display in the button.
				return "";
			}
		};
		buttonColumn.setFieldUpdater(new FieldUpdater<ReportModel, String>() {
			@Override
			public void update(int index, ReportModel object, String value) {
				if (object != null) {
					observer.generateReport(object);
				}
			}
		});
		table.addColumn(buttonColumn, SafeHtmlUtils
				.fromSafeConstant("<span title=\"Generate CSV\">" + "Generate CSV"
						+ "</span>"));
		table.setWidth("100%");
		table.setColumnWidth(0, 50.0, Unit.PCT);
		table.setColumnWidth(1, 50.0, Unit.PCT);
	}
	/**
	 * This method creates static list of Report Model which is used to set the content inside cell table.
	 * @return List<ReportModel>
	 */
	private List<ReportModel> createReportModelList() {
		List<ReportModel> modelList = new ArrayList<ReportModel>();
		ReportModel userModelReport = new ReportModel();
		userModelReport.setReportName("Active User Report");
		userModelReport.setButtonLabel("Generate CSV");
		userModelReport.setToBeGenerated("User");
		modelList.add(userModelReport);
		
		ReportModel allUserModelReport = new ReportModel();
		allUserModelReport.setReportName("All Users Report");
		allUserModelReport.setButtonLabel("Generate CSV");
		allUserModelReport.setToBeGenerated("AllUser");
		modelList.add(allUserModelReport);
		
		ReportModel orgModelReport = new ReportModel();
		orgModelReport.setReportName("Active Organization Report");
		orgModelReport.setButtonLabel("Generate CSV");
		orgModelReport.setToBeGenerated("Org");
		modelList.add(orgModelReport);
		
		ReportModel measureModelReport = new ReportModel();
		measureModelReport.setReportName("Measure Ownership Report");
		measureModelReport.setButtonLabel("Generate CSV");
		measureModelReport.setToBeGenerated("Measure");
		modelList.add(measureModelReport);
		
		ReportModel cqlLibraryModelReport = new ReportModel();
		cqlLibraryModelReport.setReportName("CQL Library Ownership Report");
		cqlLibraryModelReport.setButtonLabel("Generate CSV");
		cqlLibraryModelReport.setToBeGenerated("Library");
		modelList.add(cqlLibraryModelReport);
		
		return modelList;
	}
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	@Override
	public ContentWithHeadingWidget getContainerPanel() {
		return containerPanel;
	}
	public void setContainerPanel(ContentWithHeadingWidget containerPanel) {
		this.containerPanel = containerPanel;
	}
	public Observer getObserver() {
		return observer;
	}
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
