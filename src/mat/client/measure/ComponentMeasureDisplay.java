package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.buttons.BackSaveCancelButtonBar;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.resource.CellTableResource;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

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
	
	private static final int PAGE_SIZE = 25;
	SearchWidgetBootStrap searchWidgetBootStrap = new SearchWidgetBootStrap("Search", "Search");
	private CellTable<ManageMeasureSearchModel.Result> availableMeasuresTable;
	private CellTable<ManageMeasureSearchModel.Result> appliedComponenentTable;
	private BackSaveCancelButtonBar buttonBar = new BackSaveCancelButtonBar("componentMeasures");
	
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
		appliedComponentMeasuresPanel.add(appliedComponentMeasureHeader);
		appliedComponentMeasuresPanel.add(new SpacerWidget());
		
		appliedComponenentTable = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		appliedComponenentTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		appliedComponenentTable.setWidth("100%");
		buildAppliedComponentMeasuresTableColumns();
		appliedComponentMeasuresPanel.add(appliedComponenentTable);
		return appliedComponentMeasuresPanel;
	}

	private Panel buildAvailableMeasuresTable() {
		availableMeasuresPanel.clear();
		availableMeasuresPanel.setType(PanelType.PRIMARY);
		availableMeasuresPanel.setWidth("100%");
		availableMeasureHeader.setText("Available Measures");
		availableMeasuresPanel.add(availableMeasureHeader);
		availableMeasuresPanel.add(new SpacerWidget());
		
		availableMeasuresTable = new CellTable<ManageMeasureSearchModel.Result>(PAGE_SIZE,
				(Resources) GWT.create(CellTableResource.class));
		availableMeasuresTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		availableMeasuresTable.setWidth("100%");
		buildAvailableMeasuresTableColumns();
		availableMeasuresPanel.add(availableMeasuresTable);
		return availableMeasuresPanel;
	}
	
	private void buildAppliedComponentMeasuresTableColumns() {
		appliedComponenentTable.setPageSize(PAGE_SIZE);
		appliedComponenentTable.redraw();
		appliedComponenentTable.setRowData(appliedComponentMeasuresList);
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return getMeasureNameColumnToolTip(object);
			}
		};
		appliedComponenentTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
				+ "Measure Name" + "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		appliedComponenentTable.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));
		

		Column<ManageMeasureSearchModel.Result, SafeHtml> scoringType = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getScoringType());
			}
		};
		appliedComponenentTable.addColumn(scoringType, SafeHtmlUtils
				.fromSafeConstant("<span title='Measure Scoring'>" + "Measure Scoring"
						+ "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> aliasColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("Assign Alias");
			}
		};
		appliedComponenentTable.addColumn(aliasColumn, SafeHtmlUtils
				.fromSafeConstant("<span title='Assign Alias'>" + "Assign Alias"
						+ "</span>"));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> emptyColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("");
			}
		};
		appliedComponenentTable.addColumn(emptyColumn, SafeHtmlUtils
				.fromSafeConstant(""));
		
		Column<ManageMeasureSearchModel.Result, SafeHtml> deleteColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip("Delete");
			}
		};
		appliedComponenentTable.addColumn(deleteColumn, SafeHtmlUtils
				.fromSafeConstant("<span title='Delete'>" + "Delete"
						+ "</span>"));
		
	}
	
	private void buildAvailableMeasuresTableColumns() {
		availableMeasuresTable.setPageSize(PAGE_SIZE);
		availableMeasuresTable.redraw();
		availableMeasuresTable.setRowData(availableMeasuresList);
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return getMeasureNameColumnToolTip(object);
			}
		};
		availableMeasuresTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
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
				return CellTableUtility.getColumnToolTip("");
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
				boolean isSelected = false;
				if (availableMeasuresList != null
						&& availableMeasuresList.size() > 0) {
					for (int i = 0; i < availableMeasuresList.size(); i++) {
						if (availableMeasuresList.get(i).getId()
								.equalsIgnoreCase(object.getId())) {
							isSelected = true;
							break;
						}
					}
				} else {
					isSelected = false;
				}
				return isSelected;
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
}
