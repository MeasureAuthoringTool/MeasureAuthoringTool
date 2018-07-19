package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.CustomPager;
import mat.client.buttons.BackSaveCancelButtonBar;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.resource.CellTableResource;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.MeasureSearchModel;

public class ComponentMeasureDisplay implements BaseDisplay {
	private SimplePanel mainPanel = new SimplePanel();
	private MessageAlert errorMessages = new ErrorMessageAlert();
	protected HelpBlock helpBlock = new HelpBlock();
	private Panel availableMeasuresPanel = new Panel();
	private Panel appliedComponentMeasuresPanel = new Panel();
	
	private List<ManageMeasureSearchModel.Result> availableMeasuresList = new ArrayList<ManageMeasureSearchModel.Result>();
	private List<ManageMeasureSearchModel.Result> appliedComponentMeasuresList = new ArrayList<ManageMeasureSearchModel.Result>();
	private PanelHeader availableMeasureHeader = new PanelHeader();
	private PanelHeader appliedComponentMeasureHeader = new PanelHeader();
	
	private static final int PAGE_SIZE = 10;
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	private CellTable<ManageMeasureSearchModel.Result> availableMeasuresTable;
	private CellTable<ManageMeasureSearchModel.Result> appliedComponentTable;
	private BackSaveCancelButtonBar buttonBar = new BackSaveCancelButtonBar("componentMeasures");
	private int index;
	
	public ComponentMeasureDisplay() {
		buildMainPanel();
	}
	
	@Override
	public Widget asWidget() {
		return mainPanel;
	}

	@Override
	public MessageAlert getErrorMessageDisplay() {
		return errorMessages;
	}

	private void buildMainPanel() {
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		mainPanel.getElement().setId("mainPanel_SimplePanel");
		
		VerticalPanel contentPanel = new VerticalPanel();
		contentPanel.setWidth("100%");
		
		VerticalPanel measureFilterVP = new VerticalPanel();
		measureFilterVP.setWidth("100%");
		measureFilterVP.getElement().setId("panel_measureFilterVP");
		
		searchWidgetBootStrap.getSearchWidget().setWidth("100%");

		measureFilterVP.add(searchWidgetBootStrap.getSearchWidget());
		measureFilterVP.getElement().getStyle().setMarginLeft(3, Unit.PX);
		contentPanel.add(measureFilterVP);
		
		contentPanel.add(new SpacerWidget());
		
		availableMeasuresPanel = buildAvailableMeasuresTable();
		contentPanel.add(availableMeasuresPanel);
		contentPanel.add(new SpacerWidget());
		
		appliedComponentMeasuresPanel = buildAppliedComponentMeasuresTable();
		contentPanel.add(appliedComponentMeasuresPanel);
		contentPanel.add(new SpacerWidget());
		
		contentPanel.add(buttonBar);
		
		mainPanel.add(contentPanel);
	}
	
	private Panel buildAppliedComponentMeasuresTable() {
		appliedComponentMeasuresPanel.clear();
		appliedComponentMeasuresPanel.setWidth("100%");
		appliedComponentMeasuresPanel.setType(PanelType.PRIMARY);
		appliedComponentMeasureHeader.setText("Applied Component Measures");
		appliedComponentMeasureHeader.setTitle("Applied Component Measures");
		appliedComponentMeasureHeader.getElement().setAttribute("tabIndex", "0");
		appliedComponentMeasuresPanel.add(appliedComponentMeasureHeader);
		appliedComponentMeasuresPanel.add(new SpacerWidget());
		
		appliedComponentTable = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		appliedComponentTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		appliedComponentTable.setWidth("100%");
		buildAppliedComponentMeasuresTableColumns();
		appliedComponentMeasuresPanel.add(appliedComponentTable);
    	Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("appliedComponentMeasureSearchSummary",
				"In the following Applied Component Measure table, Measure Name is given in first column,"
						+ " Version in second column, Measure Scoring in third column,"
						+ "Assign Alias in fourth column, Delete in fifth column.");
    	appliedComponentMeasuresPanel.add(invisibleLabel);
		return appliedComponentMeasuresPanel;
	}

	private Panel buildAvailableMeasuresTable() {
		availableMeasuresPanel.clear();
		availableMeasuresPanel.setType(PanelType.PRIMARY);
		availableMeasuresPanel.setWidth("100%");
		availableMeasureHeader.setText("Available Measures");
		availableMeasureHeader.setTitle("Available Measures");
		availableMeasureHeader.getElement().setAttribute("tabIndex", "0");
		availableMeasuresPanel.add(availableMeasureHeader);
		availableMeasuresPanel.add(new SpacerWidget());
		
		availableMeasuresTable = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		availableMeasuresTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		availableMeasuresTable.setWidth("100%");
		buildAvailableMeasuresTableColumns();
		availableMeasuresPanel.add(availableMeasuresTable);
    	Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("availableComponentMeasureSearchSummary",
				"In the following Available Component Measure table, Measure Name is given in first column,"
						+ " Version in second column, Measure Scoring in third column,"
						+ "Patient-based Indicator in fourth column, Owner in fifth column, Select in sixth column.");
    	availableMeasuresPanel.add(invisibleLabel);
		return availableMeasuresPanel;
	}
	
	private void buildAppliedComponentMeasuresTableColumns() {
		appliedComponentTable.setPageSize(PAGE_SIZE);
		appliedComponentTable.redraw();
		appliedComponentTable.setRowData(appliedComponentMeasuresList);
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getName());
			}
		};
		appliedComponentTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name'>"
				+ "Measure Name" + "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		appliedComponentTable.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));
		

		Column<ManageMeasureSearchModel.Result, SafeHtml> scoringType = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getScoringType());
			}
		};
		appliedComponentTable.addColumn(scoringType, SafeHtmlUtils
				.fromSafeConstant("<span title='Measure Scoring'>" + "Measure Scoring"
						+ "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> aliasColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("Assign Alias");
			}
		};
		appliedComponentTable.addColumn(aliasColumn, SafeHtmlUtils
				.fromSafeConstant("<span title='Assign Alias'>" + "Assign Alias"
						+ "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> emptyColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("");
			}
		};
		appliedComponentTable.addColumn(emptyColumn, SafeHtmlUtils
				.fromSafeConstant(""));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> deleteColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("Delete");
			}
		};
		appliedComponentTable.addColumn(deleteColumn, SafeHtmlUtils
				.fromSafeConstant("<span title='Delete'>" + "Delete"
						+ "</span>"));
		
	}
	
	public void populateAvailableMeasuresTableCells(ManageMeasureSearchModel
			manageMeasureSearchModel, int filter, MeasureSearchModel model) {
		availableMeasuresList = new ArrayList<>();
		availableMeasuresList.addAll(manageMeasureSearchModel.getData());
		availableMeasuresTable.setRowCount(manageMeasureSearchModel.getResultsTotal(), true);
		availableMeasuresPanel = buildAvailableMeasuresTable();
		
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
						if ((result.getData() != null) && (result.getData().size() > 0)) {
							List<ManageMeasureSearchModel.Result> manageMeasureSearchList = 
									new ArrayList<ManageMeasureSearchModel.Result>();		        	  
							manageMeasureSearchList.addAll(result.getData());
							availableMeasuresList = manageMeasureSearchList;
							updateRowData(start, manageMeasureSearchList);
						} else {
							availableMeasuresPanel.clear();
							availableMeasuresPanel.setType(PanelType.PRIMARY);
							availableMeasuresPanel.setWidth("100%");
							availableMeasureHeader.setText("Available Measures");
							availableMeasureHeader.setTitle("Available Measures");
							availableMeasureHeader.getElement().setAttribute("tabIndex", "0");
							HTML desc = new HTML("<p> No available measures. </p>");
							availableMeasuresPanel.clear();
							availableMeasuresPanel.add(availableMeasureHeader);
							availableMeasuresPanel.add(new SpacerWidget());
							availableMeasuresPanel.add(desc);
						}
					}
				};

				model.setStartIndex(start + 1);
				model.setPageSize(start + PAGE_SIZE);

				model.setIsMyMeasureSearch(filter);

				MatContext.get().getMeasureService().search(model, callback);
			}
		};


		provider.addDataDisplay(availableMeasuresTable);
		
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"componentMeasureDisplay");
		spager.setPageStart(0);
		spager.setDisplay(availableMeasuresTable);
		spager.setPageSize(PAGE_SIZE);
		availableMeasuresPanel.add(new SpacerWidget());
		availableMeasuresPanel.add(spager);
		availableMeasuresTable.redraw();
	}
	
	private void buildAvailableMeasuresTableColumns() {
		availableMeasuresTable.setPageSize(PAGE_SIZE);
		availableMeasuresTable.setRowData(availableMeasuresList);
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getName());
			}
		};
		availableMeasuresTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name'>"
				+ "Measure Name" + "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		availableMeasuresTable.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));
		

		Column<ManageMeasureSearchModel.Result, SafeHtml> scoringType = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getScoringType());
			}
		};
		availableMeasuresTable.addColumn(scoringType, SafeHtmlUtils
				.fromSafeConstant("<span title='Measure Scoring'>" + "Measure Scoring"
						+ "</span>"));
	
		Column<ManageMeasureSearchModel.Result, SafeHtml> patientBasedIndicator = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				String patientBasedString = (object.isPatientBased() != null) ? Boolean.toString(object.isPatientBased()) : "";
				return CellTableUtility.getColumnToolTip(patientBasedString);
			}
		};
		availableMeasuresTable.addColumn(patientBasedIndicator, SafeHtmlUtils.fromSafeConstant("<span title=\"Patient-based Indicator\">" + "Patient-based Indicator" + "</span>"));
		
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> ownerName = new Column<
				ManageMeasureSearchModel.Result, SafeHtml>(new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getOwnerfirstName()
						+ "  " + object.getOwnerLastName(),object.getOwnerfirstName()
						+ "  " + object.getOwnerLastName());
			}
		};
		availableMeasuresTable.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));
		
		MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		availableMeasuresTable.setSelectionModel(selectionModel);
		
		MatCheckBoxCell chbxCell = new MatCheckBoxCell(false, true);
		Column<ManageMeasureSearchModel.Result, Boolean> selectColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(
				chbxCell) {

			@Override
			public Boolean getValue(Result object) {
				return false;
			}
		};

		selectColumn
				.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {

					@Override
					public void update(int index, Result object, Boolean value) {
						selectionModel.setSelected(object, value);
						if (value) {
							availableMeasuresList.add(object);
						} else {
							for (int i = 0; i < availableMeasuresList
									.size(); i++) {
								if (availableMeasuresList.get(i).getId()
										.equalsIgnoreCase(object.getId())) {
									availableMeasuresList.remove(i);
									break;
								}
							}
						}

					}
				});
		availableMeasuresTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Select\">" + "Select" + "</span>"));
		availableMeasuresTable.redraw();
	}
	
	public Button getSaveButton() {
		return buttonBar.getSaveButton();
	}
	
	public Button getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	public Button getBackButton() {
		return buttonBar.getBackButton();
	}
	
	public Button getSearchButton() {
		return searchWidgetBootStrap.getGo();
	}
	
	public HasValue<String> getSearchString() {
		return searchWidgetBootStrap.getSearchBox();
	}
}