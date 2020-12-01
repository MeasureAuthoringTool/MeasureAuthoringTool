package mat.client.cqlworkspace.codes;

import com.google.gwt.cell.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
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
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import mat.client.CustomPager;
import mat.client.Mat;
import mat.client.buttons.CancelButton;
import mat.client.buttons.CodesValuesetsButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.*;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import mat.model.MatCodeTransferObject;
import mat.model.cql.CQLCode;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.StringUtility;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.*;
import org.gwtbootstrap3.client.ui.constants.ButtonType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CQLCodesView {
	private static final Logger log = Logger.getLogger(CQLCodesView.class.getSimpleName());


	private static final int TABLE_ROW_COUNT = 10;

	private static final String APPLY = "Apply";
	private static final String CODE = "Code";
	private static final String CODE_SYSTEM = "Code System";
	private static final String CODE_DESCRIPTOR = "Code Descriptor";
	private static final String CODE_SYSTEM_VERSION = "Code System Version";
	private static final String INCLUDE_CODE_SYSTEM_VERSION = "Click to include code system version";
	private static final String NOT_INCLUDE_CODE_SYSTEM_VERSION = "Click to not include code system version";

	private static final String SPAN_END = "</span>";

	public static interface Delegator {

		void onDeleteClicked(CQLCode result, int index);

		void onModifyClicked(CQLCode object);

	}

	private boolean isEditable;
	private boolean isLoading; 

	private String codeSystemOid;

	private CQLCode lastSelectedObject;
	private CQLCode validateCodeObject;

	private List<CQLCode> codesSelectedList;
	private List<CQLCode> allCodes;

	private MatTextBox codeDescriptorInput = new MatTextBox();
	private MatTextBox codeInput = new MatTextBox();
	private MatTextBox codeSystemInput = new MatTextBox();
	private MatTextBox codeSystemVersionInput = new MatTextBox();

	private CustomCheckBox includeCodeSystemVersionCheckBox;
	private CustomQuantityTextBox suffixTextBox = new CustomQuantityTextBox(4);

	private Button applyButton = new Button(APPLY);
	private Button cancelButton = new CancelButton("CQLCodesView");

	private CodesValuesetsButtonToolBar copyPasteClearButtonToolBar = new CodesValuesetsButtonToolBar("codes");

	private CellTable<CQLCode> table;

	private ListDataProvider<CQLCode> listDataProvider;

	private MultiSelectionModel<CQLCode> selectionModel;

	private VerticalPanel mainPanel;

	private Panel cellTablePanel = new Panel();
	private PanelHeader searchHeader = new PanelHeader();
	private PanelBody cellTablePanelBody = new PanelBody();

	private SimplePanel containerPanel = new SimplePanel();
	SimplePanel cellTableMainPanel = new SimplePanel();

	private MatSimplePager spager;

	private Delegator delegator;

	private HandlerManager handlerManager = new HandlerManager(this);

	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get().getVsacapiServiceAsync();

	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Retrieve", "Enter Code Identifier");

	HTML heading = new HTML();

	private InAppHelp inAppHelp = new InAppHelp("");
	private ErrorHandler errorHandler = new ErrorHandler();

	public CQLCodesView() {
		heading.addStyleName("leftAligned");

		HorizontalPanel mainHPanel = new HorizontalPanel();
		mainHPanel.getElement().setId("mainPanel_HorizontalPanel");
		mainHPanel.add(buildHeadingAndCodesWidgetVP());

		containerPanel.getElement().setAttribute("id", "codesContainerPanel");
		containerPanel.add(mainHPanel);
		containerPanel.setStyleName("cqlcodesContentPanel");
	}

	private HorizontalPanel buildCodesWidgetHP() {
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.getElement().setId("simplePanel_SimplePanel");
		simplePanel.setWidth("5px");

		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		hp.add(buildElementWithCodesWidget());
		hp.add(simplePanel);

		return hp;
	}

	private VerticalPanel buildHeadingAndCodesWidgetVP() {
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(buildCodesWidgetHP());
		verticalPanel.add(new SpacerWidget());
		return verticalPanel;
	}

	public SimplePanel buildCellTableWidget(){
		cellTableMainPanel.clear();

		VerticalPanel vPanel = new VerticalPanel();
		vPanel.setStyleName("cqlqdsContentPanel");
		vPanel.getElement().setId("hPanel_HorizontalPanel");
		vPanel.setWidth("100%");
		vPanel.add(copyPasteClearButtonToolBar.getButtonToolBar());
		vPanel.add(cellTablePanel);

		cellTableMainPanel.add(vPanel);

		return cellTableMainPanel;
	}

	private Widget buildElementWithCodesWidget() {
		mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		mainPanel.add(buildSearchPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}

	private Widget buildSearchPanel() {
		Panel searchPanel = new Panel();

		PanelBody searchPanelBody = buildPanelBody();

		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("cqlvalueSetSearchPanel");
		searchHeader.setStyleName("CqlWorkSpaceTableHeader");
		searchPanel.add(searchHeader);
		searchPanel.setHeight("350px");
		searchPanel.add(searchPanelBody);

		return searchPanel;
	}

	private PanelBody buildPanelBody() {
		PanelBody searchPanelBody = new PanelBody();
		searchPanelBody.add(new SpacerWidget());
		searchPanelBody.add(buildCodesVP());
		return searchPanelBody;
	}

	private VerticalPanel buildCodesVP() {
		Grid searchGrid = buildSearchGrid();
		Grid codeDescriptorAndSuffixGrid = buildCodeDescriptorAndSuffixGrid();
		Grid codeGrid = buildCodeGrid();

		VerticalPanel codeFormGroup = new VerticalPanel();
		codeFormGroup.add(searchGrid);
		codeFormGroup.add(codeDescriptorAndSuffixGrid);
		codeFormGroup.add(codeGrid);

		return codeFormGroup;
	}

	private Grid buildSearchGrid() {
		Grid searchGrid = new Grid(1, 1);
		searchGrid.setWidget(0, 0, buildSearchVP());
		searchGrid.setStyleName("secondLabel");
		return searchGrid;
	}

	private Grid buildCodeDescriptorAndSuffixGrid() {
		Grid codeDescriptorAndSuffixGrid = new Grid(1, 2);
		codeDescriptorAndSuffixGrid.setWidget(0, 0, buildCodeDescriptorVP());
		codeDescriptorAndSuffixGrid.setWidget(0, 1, buildSuffixVP());
		codeDescriptorAndSuffixGrid.setStyleName("code-grid");
		return codeDescriptorAndSuffixGrid;
	}

	private Grid buildCodeGrid() {
		Grid codeGrid = new Grid(2, 3);
		codeGrid.setWidget(0, 0, buildCodeVP());
		codeGrid.setWidget(0, 1, buildCodeSystemVP());
		codeGrid.setWidget(0, 2, buildCodeSystemVersionVP());
		codeGrid.setWidget(1, 0, buildApplyCancelButtonsVP());
		codeGrid.getCellFormatter().getElement(1, 1).setAttribute("colspan", "2");
		codeGrid.setWidget(1, 1, buildCodeSystemHP());
		codeGrid.setStyleName("code-grid");
		return codeGrid;
	}

	private VerticalPanel buildSearchVP() {
		VerticalPanel searchWidgetFormGroup = new VerticalPanel();
		sWidget.setSearchBoxWidth("530px");
		sWidget.getGo().setEnabled(true);
		sWidget.getGo().setTitle("Retrieve Code");
		sWidget.getSearchBox().addBlurHandler(errorHandler.buildRequiredBlurHandler(sWidget.getSearchBox(), searchWidgetFormGroup));
		searchWidgetFormGroup.add(buildFormLabel("Code URL", "codeURLInput_TextBox"));
		searchWidgetFormGroup.add(sWidget.getSearchWidget());
		searchWidgetFormGroup.add(new SpacerWidget());
		return searchWidgetFormGroup;
	}

	private VerticalPanel buildCodeDescriptorVP() {
		codeDescriptorInput.setTitle(CODE_DESCRIPTOR);
		codeDescriptorInput.setWidth("450px");
		codeDescriptorInput.getElement().setId("codeDescriptorInput_TextBox");

		VerticalPanel codeDescriptorGroup = new VerticalPanel();
		codeDescriptorGroup.add(buildFormLabel(CODE_DESCRIPTOR, "codeDescriptorInput_TextBox"));
		codeDescriptorGroup.add(codeDescriptorInput);
		return codeDescriptorGroup;
	}

	private VerticalPanel buildSuffixVP() {
		FormLabel suffixLabel = new FormLabel();
		suffixLabel.setText("Suffix (Max Length 4)");
		suffixLabel.setTitle("Suffix");
		suffixLabel.setFor("suffixInput_TextBox");

		suffixTextBox.setTitle("Suffix must be an integer between 1-4 characters");
		suffixTextBox.getElement().setId("suffixInput_TextBox");

		VerticalPanel suffixGroup = new VerticalPanel();
		suffixGroup.add(suffixLabel);
		suffixGroup.add(suffixTextBox);

		return suffixGroup;
	}

	private VerticalPanel buildCodeVP() {
		codeInput.setTitle(CODE);
		codeInput.getElement().setId("codeInput_TextBox");
		codeInput.setHeight("30px");

		VerticalPanel codeGroup = new VerticalPanel();
		codeGroup.add(buildFormLabel(CODE, "codeInput_TextBox"));
		codeGroup.add(codeInput);

		return codeGroup;
	}

	private VerticalPanel buildCodeSystemVP() {
		codeSystemInput.setTitle(CODE_SYSTEM);
		codeSystemInput.getElement().setId("codeSystemInput_TextBox");
		codeSystemInput.setWidth("280px");
		codeSystemInput.setHeight("30px");

		VerticalPanel codeSystemGroup = new VerticalPanel();
		codeSystemGroup.add(buildFormLabel(CODE_SYSTEM, "codeSystemInput_TextBox"));
		codeSystemGroup.add(codeSystemInput);

		return codeSystemGroup;
	}

	private VerticalPanel buildCodeSystemVersionVP() {
		codeSystemVersionInput.getElement().setId("codeSystemVersionInput_TextBox");
		codeSystemVersionInput.setTitle(CODE_SYSTEM_VERSION);
		codeSystemVersionInput.setHeight("30px");

		VerticalPanel versionFormGroup = new VerticalPanel();
		versionFormGroup.add(buildFormLabel(CODE_SYSTEM_VERSION, "codeSystemVersionInput_TextBox"));
		versionFormGroup.add(codeSystemVersionInput);

		return versionFormGroup;
	}

	private VerticalPanel buildApplyCancelButtonsVP() {
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		applyButton.setText(APPLY);
		applyButton.setTitle(APPLY);
		applyButton.setType(ButtonType.PRIMARY);
		buttonToolBar.add(applyButton);
		buttonToolBar.add(cancelButton);

		VerticalPanel buttonFormGroup = new VerticalPanel();
		buttonFormGroup.add(buttonToolBar);
		buttonFormGroup.add(new SpacerWidget());

		return buttonFormGroup;
	}

	private HorizontalPanel buildCodeSystemHP() {
		HorizontalPanel includeCodeSystemPanel = new HorizontalPanel();

		includeCodeSystemPanel.setHeight("30px");
		includeCodeSystemPanel.getElement().getStyle().setProperty("width", "100%");
		includeCodeSystemPanel.getElement().getStyle().setProperty("textAlign", "right");
		includeCodeSystemPanel.getElement().getStyle().setProperty("verticalAlign", "middle");

		includeCodeSystemVersionCheckBox = new CustomCheckBox(NOT_INCLUDE_CODE_SYSTEM_VERSION, NOT_INCLUDE_CODE_SYSTEM_VERSION, false);
		includeCodeSystemVersionCheckBox.getElement().setId("includeCodeSystemversion_CheckBox");

		includeCodeSystemVersionCheckBox.addValueChangeHandler(event -> onIncludeCodeSystemVersionChange());

		if(includeCodeSystemVersionCheckBox.getValue()) {
			includeCodeSystemVersionCheckBox.setTitle(NOT_INCLUDE_CODE_SYSTEM_VERSION);
		} else {
			includeCodeSystemVersionCheckBox.setTitle(INCLUDE_CODE_SYSTEM_VERSION);
		}

		includeCodeSystemPanel.add(includeCodeSystemVersionCheckBox);
		includeCodeSystemPanel.add(buildIncludeCSVLabel());

		return includeCodeSystemPanel;
	}

	private FormLabel buildIncludeCSVLabel() {
		FormLabel includeCodeSystemVersionLabel = buildFormLabel("Include Code System Version", "includeCodeSystemversion_CheckBox");
		includeCodeSystemVersionLabel.getElement().getStyle().setProperty("marginTop", "5px");
		includeCodeSystemVersionLabel.getElement().getStyle().setProperty("fontWeight", "700");
		includeCodeSystemVersionLabel.getElement().getStyle().setProperty("marginLeft", "-20px");
		return includeCodeSystemVersionLabel;
	}

	private FormLabel buildFormLabel(String label, String setFor) {
		FormLabel formLabel = new FormLabel();
		formLabel.setText(label);
		formLabel.setTitle(label);
		formLabel.setFor(setFor);
		return formLabel;
	}

	private void onIncludeCodeSystemVersionChange() {
		if(includeCodeSystemVersionCheckBox.getValue()) {
			includeCodeSystemVersionCheckBox.setTitle(NOT_INCLUDE_CODE_SYSTEM_VERSION);
		} else {
			includeCodeSystemVersionCheckBox.setTitle(INCLUDE_CODE_SYSTEM_VERSION);
		}
	}

	public Widget asWidget() {
		return containerPanel;
	}

	public void resetVSACCodeWidget() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			sWidget.getSearchBox().setTitle("Enter the Code URL");
		}
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		searchHeader.clear();
		searchHeader.add(searchHeaderText);
	}

	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	public Delegator getDelegator() {
		return delegator;
	}

	public void setDelegator(Delegator delegator) {
		this.delegator = delegator;
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	public Button getCancelCodeButton() {
		return cancelButton;
	}

	public Button getRetrieveFromVSACButton(){
		return sWidget.getGo();
	}

	public Button getApplyButton(){
		return applyButton;
	}

	public CQLCode getSelectedElementToRemove() {
		return lastSelectedObject;
	}

	public TextBox getCodeSearchInput() {
		return sWidget.getSearchBox();
	}

	public TextBox getCodeDescriptorInput() {
		return codeDescriptorInput;
	}

	public TextBox getCodeInput() {
		return codeInput;
	}

	public TextBox getCodeSystemInput() {
		return codeSystemInput;
	}

	public TextBox getCodeSystemVersionInput() {
		return codeSystemVersionInput;
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

	public ListDataProvider<CQLCode> getListDataProvider(){
		return listDataProvider;
	}

	public MatSimplePager getSimplePager(){
		return spager;
	}

	public CellTable<CQLCode> getCelltable(){
		return table;
	}

	public VerticalPanel getMainPanel(){
		return mainPanel;
	}

	public void setWidgetsReadOnly(boolean editable){

		getCodeSearchInput().setEnabled(editable);
		getSuffixTextBox().setEnabled(editable);
		getCodeDescriptorInput().setEnabled(false);
		getCodeInput().setEnabled(false);
		getCodeSystemInput().setEnabled(false);

		getCodeSystemVersionInput().setEnabled(false);
		getRetrieveFromVSACButton().setEnabled(editable);
		getCancelCodeButton().setEnabled(editable);
		getIncludeCodeSystemVersionCheckBox().setEnabled(isEditable);
		getApplyButton().setEnabled(false);

	}

	public void setWidgetToDefault() {
		getCodeSearchInput().setValue("");
		getCodeDescriptorInput().setValue("");
		getCodeInput().setValue("");
		getCodeSystemInput().setValue("");
		getSuffixTextBox().setValue("");
		getCodeSystemVersionInput().setValue("");
		getApplyButton().setEnabled(false);
		getIncludeCodeSystemVersionCheckBox().setValue(false);
	}

	public void showSearchingBusyOnCodes(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		getRetrieveFromVSACButton().setEnabled(!busy);
	}

	public SimplePanel getCellTableMainPanel(){
		return cellTableMainPanel;
	}

	public void clearCellTableMainPanel(){
		cellTableMainPanel.clear();
	}

	public void resetCQLCodesSearchPanel() {
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		getSearchHeader().clear();
		getSearchHeader().add(searchHeaderText);
		getRetrieveFromVSACButton().setEnabled(true);
		getCodeSearchInput().setEnabled(true);
		getCodeSearchInput().setValue("");

		getCodeDescriptorInput().setValue("");
		getCodeInput().setValue("");
		getCodeSystemInput().setValue("");
		getCodeSystemVersionInput().setValue("");
		setCodeSystemOid("");
		getSuffixTextBox().setValue("");
		getApplyButton().setEnabled(false);
		getIncludeCodeSystemVersionCheckBox().setValue(false);
	}

	public void buildCodesCellTable(List<CQLCode> codesTableList, boolean checkForEditPermission) {
		log.log(Level.INFO,"Entering buildCodesCellTable " + codesTableList);
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("100%");

		PanelHeader codesElementsHeader = new PanelHeader();
		codesElementsHeader.getElement().setId("searchHeader_Label");
		codesElementsHeader.setStyleName("CqlWorkSpaceTableHeader");
		codesElementsHeader.getElement().setAttribute("tabIndex", "-1");

		HTML searchHeaderText = new HTML("<strong>Applied Codes</strong>");
		codesElementsHeader.add(searchHeaderText);
		cellTablePanel.add(codesElementsHeader);

		if (codesTableList != null && !codesTableList.isEmpty()) {
			log.log(Level.INFO,"codesTableList not empty ");

			allCodes = codesTableList;
			log.log(Level.INFO,"removeEscapedCharsFromList " + codesTableList);
			StringUtility.removeEscapedCharsFromList(codesTableList);
			log.log(Level.INFO,"removeEscapedCharsFromList complete");
			codesSelectedList = new ArrayList<>();
			table = new CellTable<>();
			setEditable(checkForEditPermission);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			listDataProvider = new ListDataProvider<>();

			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(codesTableList);
			ListHandler<CQLCode> sortHandler = new ListHandler<>(listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			table = addColumnToTable();
			listDataProvider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"valuesetAndCodes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel;

			invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
					.buildInvisibleLabel(
							"appliedCodeTableSummary",
							"In the Following Applied Codes table Descriptor in First Column"
									+ "Code in Second Column, Code System in Third Column, Version in Fourth Column,"
									+ "Version Included in Fifth Column, Edit in the Sixth Column, Delete in the Seventh Column, and Copy in the Eigth column."
									+ "The Applied Codes are listed alphabetically in a table.");

			table.getElement().setAttribute("id", "AppliedCodeTable");
			table.getElement().setAttribute("aria-describedby", "appliedCodeTableSummary");

			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(spager);
			cellTablePanel.add(cellTablePanelBody);


		} else {
			HTML desc = new HTML("<p> No Codes.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
		log.log(Level.INFO,"Leaving buildCodesCellTable ");

	}

	private CellTable<CQLCode> addColumnToTable() {
		log.log(Level.INFO,"Entering addColumnToTable ");
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			Label appliedCodesLabel = new Label("Applied Codes");
			appliedCodesLabel.getElement().setId("searchHeader_Label");
			appliedCodesLabel.getElement().setAttribute("tabIndex", "-1");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			appliedCodesLabel.setVisible(false);
			caption.appendChild(appliedCodesLabel.getElement());
			selectionModel = new MultiSelectionModel<>();
			table.setSelectionModel(selectionModel);

			Column<CQLCode, SafeHtml> nameColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getDisplayName();

					log.log(Level.INFO,"nameColumn value=" + value);

					title.append("Descriptor : ").append(value);
					title.append("");

					return CellTableUtility.getCodeDescriptorColumnToolTip(value, title.toString(),object.getSuffix());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Descriptor\">" + "Descriptor"+ SPAN_END));
			Column<CQLCode, SafeHtml> codeColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getCodeOID();
					log.log(Level.INFO,"codeColumn value=" + value);
					title.append("Code : ").append(value);
					title.append("");
					return CellTableUtility.getColumnToolTip(value, title.toString());
				}
			};
			table.addColumn(codeColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Code\">"+ CODE  + SPAN_END));

			Column<CQLCode, SafeHtml> codeSystemColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getCodeSystemName();
					log.log(Level.INFO,"codeSystemColumn value=" + value);
					title.append("CodeSystem : ").append(value);
					title.append("");
					return CellTableUtility.getColumnToolTip(value, title.toString());
				}
			};

			table.addColumn(codeSystemColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"CodeSystem\">" + "CodeSystem" + SPAN_END));


			Column<CQLCode, SafeHtml> versionColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					log.log(Level.INFO,"versionColumn value=" + object.getCodeSystemVersion());
					StringBuilder title = new StringBuilder();
					title.append("Version : ").append(object.getCodeSystemVersion());

					title.append("");
					return CellTableUtility.getColumnToolTip(object.getCodeSystemVersion(), title.toString());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + SPAN_END));			

			Column<CQLCode, SafeHtml> isVersionIncludedColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue(CQLCode object) {

					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					log.log(Level.INFO,"isVersionIncludedColumn value=" + object.isIsCodeSystemVersionIncluded());
					if (object.isIsCodeSystemVersionIncluded()) {
						sb.appendHtmlConstant("<div title=\"Version Included\" align=\"right\">");						
						sb.appendHtmlConstant("<i class=\"fa fa-check\" aria-hidden=\"true\" style=\"color:limegreen;\"></i>");
						sb.appendHtmlConstant("<span style=\"color: transparent;\">Yes</span>");

					} else {
						sb.appendHtmlConstant("<div title=\"Version Not Included\">");								
						sb.appendHtmlConstant("&nbsp;");						
					}
					sb.appendHtmlConstant("</div>");

					return sb.toSafeHtml();
				}

			};
			table.addColumn(isVersionIncludedColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version Included\">" + "Version Included" + SPAN_END));

			String colName = "";
			colName = "Edit";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(isEditable, getModifyButtonCell())) {

				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + SPAN_END));

			colName = "Delete";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(isEditable, getDeleteButtonCell())) {

				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + SPAN_END));	

			colName = "Copy";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(true, getCheckBoxCell())) {

				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + SPAN_END));

			table.setWidth("100%", true);
			table.setColumnWidth(0, 30.0, Unit.PCT);
			table.setColumnWidth(1, 15.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			table.setColumnWidth(3, 10.0, Unit.PCT);
			table.setColumnWidth(4, 9.00, Unit.PCT);
			table.setColumnWidth(5, 6.00, Unit.PCT);
			table.setColumnWidth(6, 8.00, Unit.PCT);	
			table.setColumnWidth(7, 7.00, Unit.PCT);	
			table.setStyleName("tableWrap");
		}

		return table;
	}


	private CompositeCell<CQLCode> getCompositeCell(boolean isEditable, HasCell<CQLCode, ?> cellToAdd) {
		final List<HasCell<CQLCode, ?>> cells = new LinkedList<HasCell<CQLCode, ?>>();
		if(isEditable) {
			cells.add(cellToAdd);
		}

		return new CompositeCell<CQLCode>(cells) {
			@Override
			public void render(Context context, CQLCode object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
				for (HasCell<CQLCode, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected <X> void render(Context context, CQLCode object, SafeHtmlBuilder sb, HasCell<CQLCode, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"-1\">");
				if(object != null) {
					cell.render(context, hasCell.getValue(object), sb);
				}

				sb.appendHtmlConstant("</td>");
			}

			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
		};

	}

	private HasCell<CQLCode, Boolean> getCheckBoxCell(){
		return new HasCell<CQLCode, Boolean>() {

			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(CQLCode object) {
				boolean isSelected = false;
				if (!codesSelectedList.isEmpty()) {
					for (int i = 0; i < codesSelectedList.size(); i++) {
						if (codesSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
							isSelected = true;
							selectionModel.setSelected(object, isSelected);
							break;
						}
					}
				} else {
					selectionModel.setSelected(object, isSelected);
				}

				if(isSelected) {
					cell.setTitle("Click to remove " + object.getName() + " from clipboard");
				} else {
					cell.setTitle("Click to add " + object.getName() + " to clipboard");
				}

				return isSelected;
			}
			@Override
			public FieldUpdater<CQLCode, Boolean> getFieldUpdater() {
				return new FieldUpdater<CQLCode, Boolean>() {
					@Override
					public void update(int index, CQLCode object, Boolean isCBChecked) {

						if (isCBChecked) {
							codesSelectedList.add(object);
						} else {
							for (int i = 0; i < codesSelectedList.size(); i++) {
								if (codesSelectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									codesSelectedList.remove(i);
									break;
								}
							}
						}

						if(isCBChecked) {
							cell.setTitle("Click to remove " + object.getName() + " from clipboard");
						} else {
							cell.setTitle("Click to add " + object.getName() + " to clipboard");
						}

						selectionModel.setSelected(object, isCBChecked);
					}

				};
			}


		};

	}

	private HasCell<CQLCode, ?> getModifyButtonCell() {

		return new HasCell<CQLCode, SafeHtml>() {

			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();

			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}

			@Override
			public FieldUpdater<CQLCode, SafeHtml> getFieldUpdater() {

				return new FieldUpdater<CQLCode, SafeHtml>() {
					@Override
					public void update(int index, CQLCode object, SafeHtml value) {
						if ((object != null)) {
							int indexOfSuffix = object.getCodeName().lastIndexOf(" ("+object.getSuffix()+")");
							if(indexOfSuffix >0) {
								object.setCodeName(object.getCodeName().substring(0,indexOfSuffix));
							}
							delegator.onModifyClicked(object);
						}
					}
				};
			}

			@Override
			public SafeHtml getValue(CQLCode object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to modify code";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-pencil fa-lg";
				if(isEditable){
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' class=\" " + cssClass + "\" style=\"color: darkgoldenrod; margin-left: -15px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' class=\" " + cssClass + "\" disabled style=\"color: black; margin-left: -15px\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
				}

				return sb.toSafeHtml();
			}
		};

	}


	private HasCell<CQLCode, SafeHtml> getDeleteButtonCell() {

		return new HasCell<CQLCode, SafeHtml>() {

			ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();

			@Override
			public Cell<SafeHtml> getCell() {
				return deleteButonCell;
			}

			@Override
			public FieldUpdater<CQLCode, SafeHtml> getFieldUpdater() {

				return new FieldUpdater<CQLCode, SafeHtml>() {
					@Override
					public void update(int index, CQLCode object, SafeHtml value) {
						if (object != null) {
							lastSelectedObject = object;
							delegator.onDeleteClicked(object, index);
						}
					}
				};
			}

			@Override
			public SafeHtml getValue(CQLCode object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to delete Code";
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-trash fa-lg";
				sb.appendHtmlConstant("<button type=\"button\" title='"
						+ title + "' class=\" " + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
				return sb.toSafeHtml();
			}
		};

	}

	public void clearSelectedCheckBoxes(){
		if(table!=null){
			List<CQLCode> displayedItems = new ArrayList<>();
			displayedItems.addAll(codesSelectedList);
			codesSelectedList = new  ArrayList<>();
			for (CQLCode dto : displayedItems) {
				selectionModel.setSelected(dto, false);
			}
			table.redraw();
		}
	}

	public void selectAll(){
		if(table!=null){
			for (CQLCode code : allCodes){
				if (!codesSelectedList.contains(code)) {
					codesSelectedList.add(code);
				}
				selectionModel.setSelected(code, true);
			}
			table.redraw();
		}
	}

	public String getCodeSystemOid() {
		return codeSystemOid;
	}

	public void setCodeSystemOid(String codeSystemOid) {
		this.codeSystemOid = codeSystemOid;
	}

	public CustomQuantityTextBox getSuffixTextBox() {
		return suffixTextBox;
	}

	public void setSuffixTextBox(CustomQuantityTextBox suffixTextBox) {
		this.suffixTextBox = suffixTextBox;
	}

	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}

	public boolean getIsLoading() {
		return isLoading;
	}

	public void setIsLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}


	public Button getClearButton(){
		return copyPasteClearButtonToolBar.getClearButton();
	}

	public Button getCopyButton(){
		return copyPasteClearButtonToolBar.getCopyButton();
	}

	public Button getSelectAllButton() {
		return copyPasteClearButtonToolBar.getSelectAllButton();
	}

	public Button getPasteButton(){
		return copyPasteClearButtonToolBar.getPasteButton();
	}

	public void setIsEditable(boolean isEditable) {		
		getIncludeCodeSystemVersionCheckBox().setEnabled(isEditable);
		getCancelCodeButton().setEnabled(isEditable);
		getRetrieveFromVSACButton().setEnabled(isEditable);
		this.setIsLoading(!isEditable);
	}

	public List<CQLCode> getCodesSelectedList() {
		return codesSelectedList;
	}

	public List<CQLCode> setMatCodeList(List<CQLCode> copiedCodeList, List<CQLCode> appliedCodeTableList) {
		List<CQLCode> codesToPaste = new ArrayList<>();
		for(CQLCode cqlCode: copiedCodeList) {
			boolean isDuplicate = appliedCodeTableList.stream().anyMatch(c -> c.getDisplayName().equals(cqlCode.getDisplayName()));
			if(!isDuplicate) {
				codesToPaste.add(cqlCode);
			}
		}
		return codesToPaste;
	}	

	public CustomCheckBox getIncludeCodeSystemVersionCheckBox() {
		return includeCodeSystemVersionCheckBox;
	}

	public void setIncludeCodeSystemVersionCheckBox(CustomCheckBox includeCodeSystemVersionCheckBox) {
		this.includeCodeSystemVersionCheckBox = includeCodeSystemVersionCheckBox;
	}

	public boolean checkCodeInAppliedCodeTableList(String displayName, List<CQLCode> appliedCodeTableList) {
		return appliedCodeTableList.stream().anyMatch(code -> code.getDisplayName().equalsIgnoreCase(displayName));		
	}

	public CQLCode getValidateCodeObject() {
		return validateCodeObject;
	}

	public void setValidateCodeObject(CQLCode validateCodeObject) {
		this.validateCodeObject = validateCodeObject;
	}

	public MatCodeTransferObject getCodeTransferObject(String libraryId, CQLCode refCode) {
		MatCodeTransferObject transferObject = new MatCodeTransferObject();
		transferObject.setCqlCode(refCode);
		transferObject.setId(libraryId);
		transferObject.scrubForMarkUp();

		return (transferObject.isValidModel() && transferObject.isValidCodeData(this.getValidateCodeObject())) ? transferObject : null;
	}

	public List<CQLCode> getAllCodes(){
		return allCodes;
	}


	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}


	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}

}
