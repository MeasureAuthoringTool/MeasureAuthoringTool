package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.HelpBlock;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.CustomPager;
import mat.client.codelist.HasListBox;
import mat.client.shared.CQLCopyPasteClearButtonToolBar;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.CQLValueSetTransferObject;
import mat.model.CodeListSearchDTO;
import mat.model.MatConcept;
import mat.model.MatConceptList;
import mat.model.MatValueSet;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.ConstantMessages;

public class CQLAppliedValueSetView implements HasSelectionHandlers<Boolean>{
	static final String GROUPING_QDM = " (G)";
	static final String EXTENSIONAL_QDM = " (E)";
	private Boolean isLoading = false;
	private final String TEXT_APPLY = "Apply";
	private final String TEXT_CANCEL = "Cancel";
	private final String TEXT_OID = "OID";
	private final String TEXT_NAME = "Name";
	private final String TEXT_PROGRAM = "Program";
	private final String TEXT_RELEASE = "Release";
	private final String TEXT_VERSION = "Version";
	private final String ENTER_OID = "Enter OID";
	private final String ENTER_NAME = "Enter Name";
	private final String RETRIEVE_OID = "Retrieve OID";
	
	public static interface Observer {

		void onModifyClicked(CQLQualityDataSetDTO result);

		void onDeleteClicked(CQLQualityDataSetDTO result, int index);
		
	}
	
	private Observer observer;
	private SimplePanel containerPanel = new SimplePanel();
	private HandlerManager handlerManager = new HandlerManager(this);
	private Panel cellTablePanel = new Panel();
	private PanelBody cellTablePanelBody = new PanelBody();
	private static final int TABLE_ROW_COUNT = 10;
	private CellTable<CQLQualityDataSetDTO> table;
	private List<CQLQualityDataSetDTO> qdmSelectedList;
	private List<CQLQualityDataSetDTO> allValueSetsList;
	private MultiSelectionModel<CQLQualityDataSetDTO> selectionModel;
	private ListDataProvider<CQLQualityDataSetDTO> listDataProvider;
	private Button updateVSACButton = new Button("Update From VSAC ");
	private List<String> versionList = new ArrayList<String>();
	private CQLQualityDataSetDTO lastSelectedObject;
	private ListBox versionListBox = new ListBox();
	private ListBox programListBox = new ListBox();
	private ListBox releaseListBox = new ListBox();
	private MatTextBox nameInput = new MatTextBox();
	private MatTextBox oidInput = new MatTextBox() {
		@Override
		public void onBrowserEvent(Event event) {
			super.onBrowserEvent(event);
			if("input".equals(event.getType()) && this.getText().trim().isEmpty()){
				ValueChangeEvent.fire(this, this.getText());
			}
		}
	};
	private Button goButton = new Button(RETRIEVE_OID);
	private CustomQuantityTextBox suffixInput = new CustomQuantityTextBox(4);
	private boolean isEditable;
	private CheckBox specificOcurChkBox;
	private MatSimplePager spager;
	private Button saveValueSet = new Button(TEXT_APPLY);
	private Button cancelButton = new Button(TEXT_CANCEL);
	private VerticalPanel mainPanel;
	private PanelHeader searchHeader = new PanelHeader();
	private HelpBlock helpBlock = new HelpBlock(); 
	SimplePanel cellTableMainPanel = new SimplePanel();
	HTML heading = new HTML();
	CQLCopyPasteClearButtonToolBar copyPasteClearButtonToolBar = new CQLCopyPasteClearButtonToolBar("valueset");

	public CQLAppliedValueSetView() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.getElement().setId("simplePanel_SimplePanel");
		simplePanel.setWidth("5px");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		hp.add(buildElementWithVSACValueSetWidget());
		hp.add(simplePanel);
		
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		heading.addStyleName("leftAligned");
		verticalPanel.add(heading);
		verticalPanel.add(new SpacerWidget());
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
			
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id",
				"subQDMAPPliedListContainerPanel");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
	}

	public SimplePanel buildCellTableWidget(){
		cellTableMainPanel.clear();
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("cqlqdsContentPanel");
		vPanel.getElement().setId("hPanel_HorizontalPanel");
		vPanel.setWidth("100%");
		updateVSACButton.setType(ButtonType.PRIMARY);
		updateVSACButton.setTitle("Retrieve the most recent versions of value sets from VSAC");
		updateVSACButton.getElement().setId("updateVsacButton_Button");

		updateVSACButton.setMarginTop(10);
		updateVSACButton.setMarginRight(2);
		updateVSACButton.setTitle("Update From VSAC");
		updateVSACButton.setText("Update From VSAC");
		updateVSACButton.setIcon(IconType.REFRESH);
		updateVSACButton.setIconSize(IconSize.LARGE);
		updateVSACButton.setPull(Pull.RIGHT);
		vPanel.add(updateVSACButton);
		vPanel.add(new SpacerWidget());
		vPanel.add(copyPasteClearButtonToolBar.getButtonToolBar());
		vPanel.add(cellTablePanel);

		cellTableMainPanel.add(vPanel);
		return cellTableMainPanel;
	}

	private Widget buildElementWithVSACValueSetWidget() {
		mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		
		mainPanel.add(buildSearchPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}
	

	private Widget buildSearchPanel() {
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		Panel searchPanel = new Panel();
		PanelBody searchPanelBody = new PanelBody();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("cqlvalueSetSearchPanel");
		
		searchHeader.setStyleName("CqlWorkSpaceTableHeader");
		searchPanel.add(searchHeader);
		searchPanelBody.add(new SpacerWidget());
		
		FormGroup messageFormGroup = new FormGroup(); 
		messageFormGroup.add(helpBlock);
		messageFormGroup.getElement().setAttribute("role", "alert");
		helpBlock.setColor("transparent");
		helpBlock.setHeight("0px");
		searchPanelBody.add(messageFormGroup);

		
		nameInput.getElement().setId("nameInput_TextBox");
		nameInput.getElement().setAttribute("tabIndex", "0");
		
		nameInput.setTitle(ENTER_NAME);
		nameInput.setWidth("450px");
		nameInput.setHeight("30px");
		
		suffixInput.getElement().setId("suffixInput_TextBox");
		
		suffixInput.setTitle("Suffix must be an integer between 1-4 characters");
		suffixInput.setWidth("150px");
		suffixInput.setHeight("30px");

		versionListBox.getElement().setId("Version_ListBox");
		versionListBox.getElement().setTitle("Version Selection List");
		versionListBox.setEnabled(false);
		versionListBox.setWidth("600px");
		
		saveValueSet.setText(TEXT_APPLY);
		saveValueSet.setTitle(TEXT_APPLY);
		saveValueSet.setType(ButtonType.PRIMARY);
		
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle(TEXT_CANCEL);
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(saveValueSet);
		buttonToolBar.add(cancelButton);
		
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.add(new SpacerWidget());
		buttonPanel.add(buttonToolBar);
		buttonPanel.add(new SpacerWidget());
		
		
		VerticalPanel searchWidgetFormGroup = new VerticalPanel();
		searchWidgetFormGroup.getElement().getStyle().setProperty("padding", "10px");
		searchWidgetFormGroup.getElement().getStyle().setProperty("border", "solid 1px #e8eff7");
		searchWidgetFormGroup.getElement().getStyle().setProperty("marginBottom", "10px");
		FormLabel oidLabel = new FormLabel();

		oidLabel.setText(TEXT_OID);
		oidLabel.setTitle(TEXT_OID);
		searchWidgetFormGroup.add(oidLabel);
		oidInput.setWidth("595px");
		oidInput.setTitle(ENTER_OID);
		searchWidgetFormGroup.add(oidInput);
		searchWidgetFormGroup.add(new SpacerWidget());
		
		Grid programReleaseGrid = new Grid(1,3);
		
		VerticalPanel programPanel = new VerticalPanel();
		programPanel.setWidth("225px");
		FormLabel programLabel = new FormLabel();
		programLabel.setText(TEXT_PROGRAM);
		programLabel.setTitle(TEXT_PROGRAM);
		programPanel.add(programLabel);
		programListBox.setTitle("Program selection list");
		programListBox.setWidth("200px");
		programPanel.add(programListBox);
		initProgramListBoxContent();
			
		
		VerticalPanel releasePanel = new VerticalPanel();
		releasePanel.setWidth("225px");
		FormLabel releaseLabel = new FormLabel();
		releaseLabel.setText(TEXT_RELEASE);
		releaseLabel.setTitle(TEXT_RELEASE);
		releasePanel.add(releaseLabel);
		releaseListBox.setTitle("Release selection list");
		releaseListBox.setWidth("200px");
		releasePanel.add(releaseListBox);
		initializeReleaseListBoxContent();
		
		VerticalPanel goPanel = new VerticalPanel();
		goPanel.setWidth("150px");
		goPanel.add(new SpacerWidget());
		goPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		goButton.setType(ButtonType.PRIMARY);
		goButton.setIcon(IconType.SEARCH);
		goButton.setIconPosition(IconPosition.LEFT);
		goButton.setSize(ButtonSize.SMALL);
		goButton.setTitle(RETRIEVE_OID);
		goButton.setPull(Pull.RIGHT);
		goButton.getElement().getStyle().setProperty("marginRight", "5px");
		goPanel.add(goButton);
		
		programReleaseGrid.setWidget(0, 0, programPanel);
		programReleaseGrid.setWidget(0, 1, releasePanel);
		programReleaseGrid.setWidget(0, 2, goPanel);
		programReleaseGrid.setWidth("600px");
		searchWidgetFormGroup.add(programReleaseGrid);
		searchWidgetFormGroup.add(new SpacerWidget());

		VerticalPanel namePanel = new VerticalPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(TEXT_NAME);
		nameLabel.setTitle(TEXT_NAME);
		nameLabel.setFor("nameInput_TextBox");
		namePanel.add(nameLabel);
		namePanel.add(nameInput);
		namePanel.add(new SpacerWidget());

		VerticalPanel suffixPanel = new VerticalPanel();
		FormLabel suffixLabel = new FormLabel();
		suffixLabel.setText("Suffix (Max Length 4)");
		suffixLabel.setTitle("Suffix");
		suffixLabel.setFor("suffixInput_TextBox");
		suffixPanel.add(suffixLabel);
		suffixPanel.add(suffixInput);
		suffixPanel.add(new SpacerWidget());

		VerticalPanel versionFormGroup = new VerticalPanel();
		FormLabel verLabel = new FormLabel();
		verLabel.setText(TEXT_VERSION);
		verLabel.setTitle(TEXT_VERSION);
		verLabel.setFor("Version_ListBox");
		versionFormGroup.add(verLabel);
		versionFormGroup.add(versionListBox);
		versionFormGroup.add(new SpacerWidget());

		VerticalPanel buttonFormGroup = new VerticalPanel();
		buttonFormGroup.add(buttonToolBar);
		buttonFormGroup.add(new SpacerWidget());
		
		
		Grid oidGrid = new Grid(1, 1);
		oidGrid.setWidget(0, 0, searchWidgetFormGroup);
		Grid nameGrid = new Grid(1, 2);
		nameGrid.setWidget(0, 0, namePanel);
		nameGrid.setWidget(0, 1, suffixPanel);
		nameGrid.getElement().getStyle().setProperty("marginLeft", "10px");
		Grid versionGrid = new Grid(2, 1);
		versionGrid.setWidget(0, 0, versionFormGroup);
		versionGrid.setWidget(1, 0, buttonFormGroup);
		versionGrid.getElement().getStyle().setProperty("marginLeft", "10px");

		searchPanelBody.add(oidGrid);
		searchPanelBody.add(nameGrid);
		searchPanelBody.add(versionGrid);

		searchPanel.add(searchPanelBody);
		return searchPanel;
	}
	
	public void initializeReleaseListBoxContent() {
		getReleaseListBox().clear();
		getReleaseListBox().setEnabled(false);
		getReleaseListBox().addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);
	}
	
	
	public void initProgramListBoxContent() {
		getProgramListBox().clear();
		getProgramListBox().addItem(MatContext.PLEASE_SELECT, MatContext.PLEASE_SELECT);		
	}

	public void buildAppliedValueSetCellTable(List<CQLQualityDataSetDTO> appliedValueSetList, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		PanelHeader qdmElementsHeader = new PanelHeader();
		qdmElementsHeader.getElement().setId("searchHeader_Label");
		qdmElementsHeader.setStyleName("CqlWorkSpaceTableHeader");
		qdmElementsHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Applied Value Sets</strong>");
		qdmElementsHeader.add(searchHeaderText);
		cellTablePanel.add(qdmElementsHeader);
		if ((appliedValueSetList != null)
				&& (appliedValueSetList.size() > 0)) {
			table = new CellTable<CQLQualityDataSetDTO>();
			allValueSetsList = appliedValueSetList;
			setEditable(isEditable);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<CQLQualityDataSetDTO>();
			qdmSelectedList = new ArrayList<CQLQualityDataSetDTO>();
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(appliedValueSetList);
			ListHandler<CQLQualityDataSetDTO> sortHandler = new ListHandler<CQLQualityDataSetDTO>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			table = addColumnToTable(table, sortHandler, isEditable);
			listDataProvider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER,
					pagerResources, false, 0, true,"valuesetAndCodes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel;
			if(isEditable){
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied Value Sets table Name in First Column"
										+ "OID in Second Column, Version in Third Column, Edit in the Fourth Column, Delete in the Fifth Column"
										+ "and Copy in Sixth Column. The Applied Value Sets are listed alphabetically in a table.");
				
				
			} else {
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied Value Sets table Name in First Column"
										+ "OID in Second Column, Version in Third Column, Edit in the Fourth Column, Delete in the Fifth Column"
										+ "and Copy in Sixth Column. The Applied Value Sets are listed alphabetically in a table.");
			}
			table.getElement().setAttribute("id", "AppliedQDMTable");
			table.getElement().setAttribute("aria-describedby",
					"appliedQDMTableSummary");
			
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(spager);
			cellTablePanel.add(cellTablePanelBody);
			
		} else {
			HTML desc = new HTML("<p> No value sets.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(desc);
		}
	}

	private CellTable<CQLQualityDataSetDTO> addColumnToTable(
			final CellTable<CQLQualityDataSetDTO> table,
			ListHandler<CQLQualityDataSetDTO> sortHandler, final boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT) {
			Label searchHeader = new Label("Value Sets");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			searchHeader.setVisible(false);
			caption.appendChild(searchHeader.getElement());
			selectionModel = new MultiSelectionModel<CQLQualityDataSetDTO>();
			table.setSelectionModel(selectionModel);

			// Name Column
			Column<CQLQualityDataSetDTO, SafeHtml> nameColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder();
					String qdmType = new String();
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						if (object.getTaxonomy().equalsIgnoreCase("Grouping")) {
							qdmType = GROUPING_QDM;
						} else {
							qdmType = EXTENSIONAL_QDM;
						}
					}

					value.append(object.getName()).append(qdmType);
					title.append("Name : ").append(value);

					title.append("");

					return CellTableUtility.getNameColumnToolTip(value.toString(), title.toString());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));

			// OID Column
			Column<CQLQualityDataSetDTO, SafeHtml> oidColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String oid = null;
					if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						title.append("OID : ").append(ConstantMessages.USER_DEFINED_QDM_NAME);
						oid = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
					} else {
						title.append("OID : ").append(object.getOid());
						oid = object.getOid();
					}
					return getOIDColumnToolTip(oid, title, object.isHasModifiedAtVSAC(), object.isNotFoundInVSAC());
				}
			};
			table.addColumn(oidColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"OID\">" + "OID" + "</span>"));
			
			// Version Column
			Column<CQLQualityDataSetDTO, SafeHtml> versionColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						if ((object.getVersion() != null) && (object.getVersion().equals("1.0")
								|| object.getVersion().equals("1"))) {
							version = (object.getRelease() == null || object.getRelease().isEmpty()) ? "Most Recent" : "";
							title.append("Version : ").append(version);
							
						} else {
							version = object.getVersion();
							title.append("Version : ").append(version);
						}
					} else {
						version = "";
					}
					return CellTableUtility.getColumnToolTip(version, title.toString());
				}
			};
			table.addColumn(versionColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + "</span>"));
			
			// Release Column
			Column<CQLQualityDataSetDTO, SafeHtml> releaseColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String release = "";
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						title.append("Release : ").append(object.getRelease());
						release = object.getRelease() != null ? object.getRelease() : "";
					}
					return CellTableUtility.getColumnToolTip(release, title.toString());
				}
			};
			table.addColumn(releaseColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Release\">" + "Release" + "</span>"));
			
			String colName = "";
			// Edit Column
			colName = "Edit";
			table.addColumn(new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
					getCompositeCell(isEditable, getModifyButtonCell())) {

				@Override
				public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

			// Delete Column
			colName = "Delete";
			table.addColumn(new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
					getCompositeCell(isEditable, getDeleteButtonCell())) {
				
				@Override
				public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));
			
			// Copy Column
			colName = "Copy";
			table.addColumn(new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
					getCompositeCell(true, getCheckBoxCell())) {

				@Override
				public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='" + colName + "'>  " + colName + "</span>"));

			table.setWidth("100%", true);
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			table.setColumnWidth(3, 14.0, Unit.PCT);
			table.setColumnWidth(4, 7.0, Unit.PCT);
			table.setColumnWidth(5, 8.0, Unit.PCT);
			table.setColumnWidth(6, 7.0, Unit.PCT);
			table.setStyleName("tableWrap");
		}

		return table;
	}
	
	
	private CompositeCell<CQLQualityDataSetDTO> getCompositeCell(final boolean isEditable,  HasCell<CQLQualityDataSetDTO, ?> cellToAdd) {
		final List<HasCell<CQLQualityDataSetDTO, ?>> cells = new LinkedList<HasCell<CQLQualityDataSetDTO, ?>>();
		
		if(isEditable){
			cells.add(cellToAdd);
		}
		
		CompositeCell<CQLQualityDataSetDTO> cell = new CompositeCell<CQLQualityDataSetDTO>(
				cells) {
			@Override
			public void render(Context context, CQLQualityDataSetDTO object,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
				for (HasCell<CQLQualityDataSetDTO, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			
			@Override
			protected <X> void render(Context context,
					CQLQualityDataSetDTO object, SafeHtmlBuilder sb,
					HasCell<CQLQualityDataSetDTO, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"0\">");
				if ((object != null)) {
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

	private HasCell<CQLQualityDataSetDTO, SafeHtml> getModifyButtonCell() {
		
		HasCell<CQLQualityDataSetDTO, SafeHtml> hasCell = new HasCell<CQLQualityDataSetDTO, SafeHtml>() {
			
			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}
			
			@Override
			public FieldUpdater<CQLQualityDataSetDTO, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLQualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object,
							SafeHtml value) {
						if ((object != null)) {
							observer.onModifyClicked(object);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to modify value set";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-pencil fa-lg";
				if(isEditable){
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: black;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}

	private HasCell<CQLQualityDataSetDTO, SafeHtml> getDeleteButtonCell() {
		
		HasCell<CQLQualityDataSetDTO, SafeHtml> hasCell = new HasCell<CQLQualityDataSetDTO, SafeHtml>() {
			
			ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return deleteButonCell;
			}
			
			@Override
			public FieldUpdater<CQLQualityDataSetDTO, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLQualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object,
							SafeHtml value) {
						if ((object != null) && !object.isUsed()) {
							lastSelectedObject = object;
							observer.onDeleteClicked(object, index);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to delete value set";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-trash fa-lg";
				if (object.isUsed()) {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"margin-left: 0px;margin-right: 10px;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Delete</span></button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"margin-left: 0px;margin-right: 10px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
				}
				return sb.toSafeHtml();
			}
			
			
			
		};
		
		return hasCell;
	}

	private HasCell<CQLQualityDataSetDTO, Boolean> getCheckBoxCell(){
		HasCell<CQLQualityDataSetDTO, Boolean> hasCell = new HasCell<CQLQualityDataSetDTO, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(CQLQualityDataSetDTO object) {
				boolean isSelected = false;
				if (qdmSelectedList.size() > 0) {
					for (int i = 0; i < qdmSelectedList.size(); i++) {
						if (qdmSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
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
			public FieldUpdater<CQLQualityDataSetDTO, Boolean> getFieldUpdater() {
				return new FieldUpdater<CQLQualityDataSetDTO, Boolean>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object,
							Boolean isCBChecked) {
						
						if (isCBChecked) {
							qdmSelectedList.add(object);
						} else {
							for (int i = 0; i < qdmSelectedList.size(); i++) {
								if (qdmSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									qdmSelectedList.remove(i);
									break;
								}
							}
						}
						selectionModel.setSelected(object, isCBChecked);
					}

				};
			}
			
			
		};
		return hasCell;
	}

	public Widget asWidget() {
		return containerPanel;
	}
	
	public void clearSelectedCheckBoxes(){
		if(table!=null){
			List<CQLQualityDataSetDTO> displayedItems = new ArrayList<CQLQualityDataSetDTO>();
			displayedItems.addAll(qdmSelectedList);
			qdmSelectedList = new  ArrayList<CQLQualityDataSetDTO>();
			for (CQLQualityDataSetDTO dto : displayedItems) {
				selectionModel.setSelected(dto, false);
			}
			table.redraw();
		}
	}
	
	public void selectAll(){
		if(table!=null){
			for (CQLQualityDataSetDTO dto : allValueSetsList){
				   if (!qdmSelectedList.contains(dto)) {
					   qdmSelectedList.add(dto);
				   }
				   selectionModel.setSelected(dto, true);
			}
			table.redraw();
		}
	}

	public CheckBox getSpecificOccChkBox(){
		return specificOcurChkBox;
	}

	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	public String getVersionValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	public String getExpansionProfileValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}

	private boolean checkForEnable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}

	public void setQDMVersionListBoxOptions(List<? extends HasListBox> texts){
		setVersionListBoxItems(versionListBox, texts, MatContext.PLEASE_SELECT);
	}

	private void setVersionListBoxItems(ListBox dataTypeListBox,
			List<? extends HasListBox> itemList, String defaultOption) {
		dataTypeListBox.clear();
		dataTypeListBox.addItem(defaultOption, "");
		if (itemList != null) {
			for (HasListBox listBoxContent : itemList) {
				dataTypeListBox.addItem(listBoxContent.getItem(),""+listBoxContent.getValue());
			}
			
			SelectElement selectElement = SelectElement.as(dataTypeListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(optionElement.getText());
			}
		}
	}

	public void resetVSACValueSetWidget() {
	
		if(checkForEnable()){
			versionListBox.setEnabled(false);
			oidInput.setTitle(ENTER_OID);
			nameInput.setTitle(ENTER_NAME);	
		}
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		searchHeader.clear();
		searchHeader.add(searchHeaderText);
	}

	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}

	private SafeHtml getOIDColumnToolTip(String columnText,
			StringBuilder title, boolean hasImage, boolean isUserDefined) {
		if (hasImage && !isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"Value set is updated from VSAC.\""
					+ "title = \"Value set is updated from VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else if (hasImage && isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : Value set is not available in VSAC.\""
					+ " title=\"Value set is not available in VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><span tabIndex = \"0\" title='"
					+ title + "'>" + columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		}
	}

	public List<String> getVersionList() {
		return versionList;
	}

	public Button getCancelQDMButton() {
		return cancelButton;
	}
	
	public org.gwtbootstrap3.client.ui.Button getRetrieveFromVSACButton(){
		return goButton;
	}

	public Button getSaveButton(){
		return saveValueSet;
	}
	
	public Button getUpdateFromVSACButton(){
		return updateVSACButton;
	}

	public CQLQualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}

	public ListBox getVersionListBox() {
		return versionListBox;
	}
	
	public ListBox getProgramListBox() {
		return programListBox;
	}
	
	public ListBox getReleaseListBox() {
		return releaseListBox;
	}

	public TextBox getOIDInput() {
		return oidInput;
	}
	
	public TextBox getUserDefinedInput() {
		return nameInput;
	}

	public CustomQuantityTextBox getSuffixInput() {
		return suffixInput;
	}

	public boolean isEditable() {
		return isEditable;
	}

	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	public PanelHeader getSearchHeader() {
		return searchHeader;
	}
	
	public ListDataProvider<CQLQualityDataSetDTO> getListDataProvider(){
		return listDataProvider;
	}

	public MatSimplePager getSimplePager(){
		return spager;
	}
	

	public CellTable<CQLQualityDataSetDTO> getCelltable(){
		return table;
	}
	

	public MatSimplePager getPager(){
		return spager;
	}
	

	public VerticalPanel getMainPanel(){
		return mainPanel;
	}

	public void setWidgetsReadOnly(boolean editable){
		
		getOIDInput().setEnabled(editable);
		getUserDefinedInput().setEnabled(editable);
		getCancelQDMButton().setEnabled(editable);
		getRetrieveFromVSACButton().setEnabled(editable);
		getUpdateFromVSACButton().setEnabled(editable);
		getSaveButton().setEnabled(false);
		getVersionListBox().setEnabled(false);
		getProgramListBox().setEnabled(editable);
		getSuffixInput().setEnabled(editable);
	}

	public void setWidgetToDefault() {
		getVersionListBox().clear();
		getOIDInput().setValue("");
		getUserDefinedInput().setValue("");
		getSaveButton().setEnabled(false);
	}
	
	
	public SimplePanel getCellTableMainPanel(){
		return cellTableMainPanel;
	}

	public void clearCellTableMainPanel(){
		cellTableMainPanel.clear();
	}
	

	public boolean validateUserDefinedInput() {

		boolean hasName = (getUserDefinedInput().getValue().length() > 0) ? true : false ;

		if (hasName) {
			getOIDInput().setEnabled(false);
			getProgramListBox().setEnabled(false);
			getReleaseListBox().setEnabled(false);
			getRetrieveFromVSACButton().setEnabled(false);
			
			getSaveButton().setEnabled(true);
			
			getUserDefinedInput().setTitle(getUserDefinedInput().getValue());
			
		} else {
			getOIDInput().setEnabled(true);
			getProgramListBox().setEnabled(true);
			getRetrieveFromVSACButton().setEnabled(true);
			
			getSaveButton().setEnabled(false);
			
			getUserDefinedInput().setTitle(ENTER_NAME);
		}

		return hasName;
	}
	
	public boolean validateOIDInput() {
		
		boolean isUserDefined = false;
		
		if (getOIDInput().getValue().length() > 0) {					
			getUserDefinedInput().setEnabled(false);
			getSaveButton().setEnabled(false);
			getRetrieveFromVSACButton().setEnabled(true);
			getOIDInput().setTitle(getOIDInput().getValue());

		} else if (getUserDefinedInput().getValue().length() > 0) {
			isUserDefined = true;
			getVersionListBox().clear();
			getUserDefinedInput().setEnabled(true);
			getSaveButton().setEnabled(true);
			getOIDInput().setTitle(ENTER_OID);
		} else {
			getUserDefinedInput().setEnabled(true);
		}

		return isUserDefined;
	}

	public String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
			break;
		case VsacApiResult.VSAC_REQUEST_TIMEOUT:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_TIMEOUT();
			break;
		default:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
	}

	public void resetCQLValuesetearchPanel() {
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		getSearchHeader().clear();
		getSearchHeader().add(searchHeaderText);
		
		getOIDInput().setEnabled(true);
		getOIDInput().setValue("");
		getOIDInput().setTitle(ENTER_OID);
		
		getUserDefinedInput().setEnabled(true);
		getUserDefinedInput().setValue("");
		getUserDefinedInput().setTitle(ENTER_NAME);
		
		getSuffixInput().setEnabled(true);
		getSuffixInput().setValue("");
		getSuffixInput().setTitle("Suffix must be an integer between 1-4 characters");
		
		getVersionListBox().clear();
		getVersionListBox().setEnabled(false);
		getSaveButton().setEnabled(false);
		
		initializeReleaseListBoxContent();
		getProgramListBox().setSelectedIndex(0); // go back to '--Select--'
		getProgramListBox().setEnabled(true);
		
		getUpdateFromVSACButton().setEnabled(true);
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public Boolean getIsLoading() {
		return isLoading;
	}

	public void setIsLoading(Boolean isLoading) {
		this.isLoading = isLoading;
	}
	
	
	public Button getClearButton(){
		return copyPasteClearButtonToolBar.getClearButton();
	}
	
	public Button getCopyButton(){
		return copyPasteClearButtonToolBar.getCopyButton();
	}
	
	public Button getSelectAllButton(){
		return copyPasteClearButtonToolBar.getSelectAllButton();
	}
	
	public Button getPasteButton(){
		return copyPasteClearButtonToolBar.getPasteButton();
	}

	public void setReadOnly(boolean isEditable) {		
		getCancelQDMButton().setEnabled(isEditable);
		getUpdateFromVSACButton().setEnabled(isEditable);
		getRetrieveFromVSACButton().setEnabled(isEditable);
		getProgramListBox().setEnabled(isEditable);
		this.setIsLoading(!isEditable);
	}

	public List<CQLQualityDataSetDTO> getQdmSelectedList() {
		return qdmSelectedList;
	}

	public void setQdmSelectedList(List<CQLQualityDataSetDTO> qdmSelectedList) {
		this.qdmSelectedList = qdmSelectedList;
	}
	
	
	/**
	 * Method to generate List of {@link CQLValueSetTransferObject} when user pastes the list of selected {@link CQLQualityDataSetDTO}.
	 * @param copiedValueSetList
	 * @return List of {@link CQLValueSetTransferObject}
	 */
	public List<CQLValueSetTransferObject> setMatValueSetListForValueSets(List<CQLQualityDataSetDTO> copiedValueSetList,  List<CQLQualityDataSetDTO> appliedValueSetTableList) {
		List<CQLValueSetTransferObject> cqlValueSetTransferObjectsList  = new ArrayList<CQLValueSetTransferObject>();
		for(CQLQualityDataSetDTO cqlQualityDataSetDTO : copiedValueSetList) {
			if(!checkNameInValueSetList(cqlQualityDataSetDTO.getName(), appliedValueSetTableList)) {
				CQLValueSetTransferObject cqlValueSetTransferObject = new CQLValueSetTransferObject();
				cqlValueSetTransferObject.setCqlQualityDataSetDTO(cqlQualityDataSetDTO);
				
				if(cqlQualityDataSetDTO.getOid().equals(ConstantMessages.USER_DEFINED_QDM_OID)) {
					cqlValueSetTransferObject.setUserDefinedText(cqlQualityDataSetDTO.getOriginalCodeListName());
				} else {
					MatValueSet matValueSet = new MatValueSet();
					if(!cqlQualityDataSetDTO.getVersion().isEmpty() && !cqlQualityDataSetDTO.getVersion().equals("1.0") && !cqlQualityDataSetDTO.getVersion().equals("1")) {
						cqlValueSetTransferObject.setVersion(true);
						matValueSet.setVersion(cqlQualityDataSetDTO.getVersion());
					}
					List<MatConcept> matConcepts = new ArrayList<MatConcept> ();
					MatConcept matConcept = new MatConcept();
					matValueSet.setType(cqlQualityDataSetDTO.getTaxonomy());
					matConcept.setCodeSystemName(cqlQualityDataSetDTO.getTaxonomy());
					matConcepts.add(matConcept);
					MatConceptList matConceptList = new MatConceptList();
					matConceptList.setConceptList(matConcepts);
					matValueSet.setConceptList(matConceptList);
					
					matValueSet.setID(cqlQualityDataSetDTO.getOid());
					matValueSet.setDisplayName(cqlQualityDataSetDTO.getName());
					cqlValueSetTransferObject.setMatValueSet(matValueSet);
				}
				CodeListSearchDTO codeListSearchDTO = new CodeListSearchDTO();
				codeListSearchDTO.setName(cqlQualityDataSetDTO.getOriginalCodeListName());
				cqlValueSetTransferObject.setCodeListSearchDTO(codeListSearchDTO);
				cqlValueSetTransferObjectsList.add(cqlValueSetTransferObject);
			}
			
		}
		
		return cqlValueSetTransferObjectsList;
	}

	public  boolean checkNameInValueSetList(String userDefinedInput, List<CQLQualityDataSetDTO> appliedValueSetTableList) {
		if (appliedValueSetTableList.size() > 0) {
			Iterator<CQLQualityDataSetDTO> iterator = appliedValueSetTableList.iterator();
			while (iterator.hasNext()) {
				CQLQualityDataSetDTO dataSetDTO = iterator.next();
				if (dataSetDTO.getName().equalsIgnoreCase(userDefinedInput)) {
					return true;
				}
			}
		}
		return false;
	}

	public HelpBlock getHelpBlock() {
		return helpBlock;
	}

	public void setHelpBlock(HelpBlock helpBlock) {
		this.helpBlock = helpBlock;
	}
	
	public void setSelectedValueIndex(final ListBox listbox, final String value) {
		for (int i = 0; i < listbox.getItemCount(); i++) {
            if (listbox.getValue(i).equals(value)) {
                listbox.setSelectedIndex(i);
                return;
            }
        }
	}


	public List<CQLQualityDataSetDTO> getAllValueSets() {
		return allValueSetsList;
	}
	
}
