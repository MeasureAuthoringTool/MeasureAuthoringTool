package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
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
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

import mat.client.CustomPager;
import mat.client.Mat;
import mat.client.shared.CQLCopyPasteClearButtonToolBar;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.MatCodeTransferObject;
import mat.model.cql.CQLCode;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.StringUtility;

public class CQLCodesView {
	
	public static interface Delegator {
		
		void onDeleteClicked(CQLCode result, int index);

		void onModifyClicked(CQLCode object);
		
	}
	
	private Delegator delegator;

	private SimplePanel containerPanel = new SimplePanel();

	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	private HandlerManager handlerManager = new HandlerManager(this);

	private Panel cellTablePanel = new Panel();

	private CellTable<CQLCode> table;

	private ListDataProvider<CQLCode> listDataProvider;

	private CQLCode lastSelectedObject;
	
	private CQLCode validateCodeObject;

	private MatTextBox codeDescriptorInput = new MatTextBox();

	private MatTextBox codeInput = new MatTextBox();

	private MatTextBox codeSystemInput = new MatTextBox();

	private MatTextBox codeSystemVersionInput = new MatTextBox();
	
	private CustomQuantityTextBox suffixTextBox = new CustomQuantityTextBox(4);
	
	private String codeSystemOid;

	private boolean isEditable;

	private MatSimplePager spager;

	private Button saveCode = new Button("Apply");

	private Button cancelButton = new Button("Cancel");

	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Retrieve","Enter Code Identifier");

	private VerticalPanel mainPanel;

	private PanelHeader searchHeader = new PanelHeader();

	SimplePanel cellTableMainPanel = new SimplePanel();

	private PanelBody cellTablePanelBody = new PanelBody();
	
	private static final int TABLE_ROW_COUNT = 10;
	
	private CheckBox includeCodeSystemVersionCheckBox;

	HTML heading = new HTML();
	
	/**
	 * Flag for if the codes view is loading
	 */
	private boolean isLoading; 
	
	
	CQLCopyPasteClearButtonToolBar copyPasteClearButtonToolBar = new CQLCopyPasteClearButtonToolBar("codes");

	private MultiSelectionModel<CQLCode> selectionModel;

	private List<CQLCode> codesSelectedList;
	
	private List<CQLCode> allCodes;
	
	public CQLCodesView() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.getElement().setId("simplePanel_SimplePanel");
		simplePanel.setWidth("5px");
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.getElement().setId("hp_HorizontalPanel");
		hp.add(buildElementWithCodesWidget());
		hp.add(simplePanel);
		
		heading.addStyleName("leftAligned");
		verticalPanel.add(heading);
		
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(new SpacerWidget());
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
			
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id",
				"codesContainerPanel");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("cqlcodesContentPanel");
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
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		Panel searchPanel = new Panel();
		PanelBody searchPanelBody = new PanelBody();


		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("cqlvalueSetSearchPanel");

		searchHeader.setStyleName("CqlWorkSpaceTableHeader");


		searchPanel.add(searchHeader);
		searchPanel.setHeight("350px");
		searchPanelBody.add(new SpacerWidget());

		saveCode.setText("Apply");
		saveCode.setTitle("Apply");
		saveCode.setType(ButtonType.PRIMARY);

		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");

		Grid searchGrid = new Grid(1, 1);
		Grid codeDescriptorAndSuffixGrid = new Grid(1, 2);
		Grid codeGrid = new Grid(2, 3);
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(saveCode);
		buttonToolBar.add(cancelButton);

		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.add(new SpacerWidget());
		buttonPanel.add(buttonToolBar);
		buttonPanel.add(new SpacerWidget());



		VerticalPanel searchWidgetFormGroup = new VerticalPanel();
		sWidget.setSearchBoxWidth("530px");
		sWidget.getGo().setEnabled(true);
		sWidget.getGo().setTitle("Retrieve Code Identifier");
		searchWidgetFormGroup.add(sWidget.getSearchWidget());
		searchWidgetFormGroup.add(new SpacerWidget());

		VerticalPanel versionFormGroup = new VerticalPanel();
		FormLabel verLabel = new FormLabel();
		verLabel.setText("Code System Version");
		verLabel.setTitle("Code System Version");
		verLabel.setFor("codeSystemVersionInput_TextBox");
		codeSystemVersionInput.getElement().setId("codeSystemVersionInput_TextBox");
		codeSystemVersionInput.setTitle("Code System Version");
		codeSystemVersionInput.setHeight("30px");

		VerticalPanel codeSystemGroup = new VerticalPanel();
		FormLabel codeSystemLabel = new FormLabel();
		codeSystemLabel.setText("Code System");
		codeSystemLabel.setTitle("Code System");
		codeSystemLabel.setFor("codeSystemInput_TextBox");
		codeSystemInput.setTitle("Code System");
		codeSystemInput.getElement().setId("codeSystemInput_TextBox");
		codeSystemInput.setWidth("280px");
		codeSystemInput.setHeight("30px");

		VerticalPanel codeGroup = new VerticalPanel();
		FormLabel codeLabel = new FormLabel();
		codeLabel.setText("Code");
		codeLabel.setTitle("Code");
		codeLabel.setFor("codeInput_TextBox");
		codeInput.setTitle("Code");
		codeInput.getElement().setId("codeInput_TextBox");
		codeInput.setHeight("30px");

		VerticalPanel codeDescriptorGroup = new VerticalPanel();
		
		FormLabel codeDescriptorLabel = new FormLabel();
		codeDescriptorLabel.setText("Code Descriptor");
		codeDescriptorLabel.setTitle("Code Descriptor");
		codeDescriptorLabel.setFor("codeDescriptorInput_TextBox");
		codeDescriptorInput.setTitle("Code Descriptor");
		codeDescriptorInput.setWidth("450px");
		codeDescriptorInput.getElement().setId("codeDescriptorInput_TextBox");

		codeDescriptorGroup.add(codeDescriptorLabel);
		codeDescriptorGroup.add(codeDescriptorInput);
		
		
		VerticalPanel suffixGroup = new VerticalPanel();
		
		FormLabel suffixLabel = new FormLabel();
		suffixLabel.setText("Suffix (Max Length 4)");
		suffixLabel.setTitle("Suffix");
		suffixLabel.setFor("suffixInput_TextBox");
		suffixTextBox.setTitle("Suffix must be an integer between 1-4 characters");
		suffixTextBox.getElement().setId("suffixInput_TextBox");

		suffixGroup.add(suffixLabel);
		suffixGroup.add(suffixTextBox);
		
		
		codeGroup.add(codeLabel);
		codeGroup.add(codeInput);
		codeSystemGroup.add(codeSystemLabel);
		codeSystemGroup.add(codeSystemInput);
		versionFormGroup.add(verLabel);
		versionFormGroup.add(codeSystemVersionInput);

		VerticalPanel buttonFormGroup = new VerticalPanel();
		buttonFormGroup.add(buttonToolBar);
		buttonFormGroup.add(new SpacerWidget());
		
		FlowPanel includeCodeSystemPanel = new FlowPanel();
		includeCodeSystemPanel.setHeight("30px");
		includeCodeSystemPanel.getElement().getStyle().setProperty("width", "100%");
		includeCodeSystemPanel.getElement().getStyle().setProperty("textAlign", "right");
		includeCodeSystemPanel.getElement().getStyle().setProperty("verticalAlign", "middle");
		FormLabel includeCodeSystemVersionLabel = new FormLabel();
		includeCodeSystemVersionLabel.setText("Include Code System Version");
		includeCodeSystemVersionLabel.setTitle("Include Code System Version");
		includeCodeSystemVersionLabel.getElement().getStyle().setProperty("fontWeight", "700");
		includeCodeSystemVersionLabel.getElement().getStyle().setProperty("marginLeft", "3px");
		includeCodeSystemVersionLabel.setFor("includeCodeSystemversion_CheckBox");
		includeCodeSystemVersionCheckBox = new CheckBox();
		includeCodeSystemVersionCheckBox.getElement().setId("includeCodeSystemversion_CheckBox");
		includeCodeSystemVersionCheckBox.setTitle("Click checkbox to select");
		includeCodeSystemPanel.add(includeCodeSystemVersionCheckBox);
		includeCodeSystemPanel.add(includeCodeSystemVersionLabel);
		
		
		searchGrid.setWidget(0, 0, searchWidgetFormGroup);
		
		searchGrid.setStyleName("secondLabel");
		
		codeDescriptorAndSuffixGrid.setWidget(0, 0, codeDescriptorGroup);
		codeDescriptorAndSuffixGrid.setWidget(0, 1, suffixGroup);
		codeDescriptorAndSuffixGrid.setStyleName("code-grid");
		codeGrid.setWidget(0, 0, codeGroup);
		codeGrid.setWidget(0, 1, codeSystemGroup);
		codeGrid.setWidget(0, 2, versionFormGroup);
		codeGrid.setWidget(1, 0, buttonFormGroup);
		codeGrid.getCellFormatter().getElement(1, 1).setAttribute("colspan", "2");
		codeGrid.setWidget(1, 1, includeCodeSystemPanel);
		codeGrid.setStyleName("code-grid");

		VerticalPanel codeFormGroup = new VerticalPanel();
		codeFormGroup.add(searchGrid);
		codeFormGroup.add(codeDescriptorAndSuffixGrid);
		codeFormGroup.add(codeGrid);

		searchPanelBody.add(codeFormGroup);

		searchPanel.add(searchPanelBody);
		return searchPanel;
	}

	public Widget asWidget() {
		return containerPanel;
	}

	private boolean checkForEnable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}

	public void resetVSACCodeWidget() {
		if(checkForEnable()){
			sWidget.getSearchBox().setTitle("Enter Code Identifier");
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

	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}

	public Button getCancelCodeButton() {
		return cancelButton;
	}

	public Button getRetrieveFromVSACButton(){
		return sWidget.getGo();
	}

	public Button getSaveButton(){
		return saveCode;
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
		getSaveButton().setEnabled(false);
		
	}

	public void setWidgetToDefault() {
		getCodeSearchInput().setValue("");
		getCodeDescriptorInput().setValue("");
		getCodeInput().setValue("");
		getCodeSystemInput().setValue("");
		getSuffixTextBox().setValue("");
		getCodeSystemVersionInput().setValue("");
		getSaveButton().setEnabled(false);
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

	public String convertMessage(final int id) {
		String message;
		switch (id) {
			case VsacApiResult.UMLS_NOT_LOGGEDIN:
				message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
				break;
			case VsacApiResult.CODE_URL_REQUIRED:
				message = MatContext.get().getMessageDelegate().getUMLS_CODE_IDENTIFIER_REQUIRED();
				break;
			case VsacApiResult.VSAC_REQUEST_TIMEOUT:
				message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_TIMEOUT();
				break;
			default:
				message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
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
		getSaveButton().setEnabled(false);
		getIncludeCodeSystemVersionCheckBox().setValue(false);
	}

	public void buildCodesCellTable(List<CQLCode> codesTableList, boolean checkForEditPermission) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("100%");
		PanelHeader codesElementsHeader = new PanelHeader();
		codesElementsHeader.getElement().setId("searchHeader_Label");
		codesElementsHeader.setStyleName("CqlWorkSpaceTableHeader");
		codesElementsHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Applied Codes</strong>");
		codesElementsHeader.add(searchHeaderText);
		cellTablePanel.add(codesElementsHeader);
		if ((codesTableList != null)
				&& (!codesTableList.isEmpty())) {
			allCodes = codesTableList;
			StringUtility.removeEscapedCharsFromList(codesTableList);
			codesSelectedList = new ArrayList<CQLCode>();
			table = new CellTable<CQLCode>();
			setEditable(checkForEditPermission);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<CQLCode>();

			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(codesTableList);
			ListHandler<CQLCode> sortHandler = new ListHandler<CQLCode>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			table = addColumnToTable();
			listDataProvider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER,
					pagerResources, false, 0, true,"valuesetAndCodes");
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
			table.getElement().setAttribute("aria-describedby",
					"appliedCodeTableSummary");

			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(spager);
			cellTablePanel.add(cellTablePanelBody);


		} else {
			HTML desc = new HTML("<p> No Codes.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
		
	}

	private CellTable<CQLCode> addColumnToTable() {
		
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			Label searchHeader = new Label("Applied Codes");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			searchHeader.setVisible(false);
			caption.appendChild(searchHeader.getElement());
			selectionModel = new MultiSelectionModel<CQLCode>();
			table.setSelectionModel(selectionModel);
			
			// Descriptor Column
			Column<CQLCode, SafeHtml> nameColumn = new Column<CQLCode, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getDisplayName();
					
					title.append("Descriptor : ").append(value);
					title.append("");
					
					return CellTableUtility.getCodeDescriptorColumnToolTip(value, title.toString(),object.getSuffix());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Descriptor\">" + "Descriptor"+ "</span>"));
			// Code Profile Column
			Column<CQLCode, SafeHtml> codeColumn = new Column<CQLCode, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getCodeOID();
					title.append("Code : ").append(value);
					title.append("");
					return CellTableUtility.getColumnToolTip(value, title.toString());
				}
			};
			table.addColumn(codeColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Code\">"+ "Code" + "</span>"));
			
			// CodeSystem Profile Column
			Column<CQLCode, SafeHtml> codeSystemColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					String value = object.getCodeSystemName();
					title.append("CodeSystem : ").append(value);
					title.append("");
					return CellTableUtility.getColumnToolTip(value, title.toString());
				}
			};
			
			table.addColumn(codeSystemColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"CodeSystem\">" + "CodeSystem" + "</span>"));
			
			
			// Version Profile Column
			Column<CQLCode, SafeHtml> versionColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLCode object) {
					StringBuilder title = new StringBuilder();
					title.append("Version : ").append(object.getCodeSystemVersion());
					title.append("");
					return CellTableUtility.getColumnToolTip(object.getCodeSystemVersion(), title.toString());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + "</span>"));			
		
			Column<CQLCode, SafeHtml> isVersionIncludedColumn = new Column<CQLCode, SafeHtml>(new SafeHtmlCell()) {

				@Override
				public SafeHtml getValue(CQLCode object) {
					
					SafeHtmlBuilder sb = new SafeHtmlBuilder();
					
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
			table.addColumn(isVersionIncludedColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version Included\">" + "Version Included" + "</span>"));
			
			String colName = "";
			// Edit Cell
			colName = "Edit";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(isEditable, getModifyButtonCell())) {
				
				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + "</span>"));
			
			// Delete Cell
			colName = "Delete";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(isEditable, getDeleteButtonCell())) {
				
				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + "</span>"));	
			
			// Copy Cell
			colName = "Copy";
			table.addColumn(new Column<CQLCode, CQLCode>(getCompositeCell(true, getCheckBoxCell())) {
				
				@Override
				public CQLCode getValue(CQLCode object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "+ colName + "</span>"));
			
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
			public void render(Context context, CQLCode object,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
				for (HasCell<CQLCode, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected <X> void render(Context context,
					CQLCode object, SafeHtmlBuilder sb,
					HasCell<CQLCode, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces' tabindex=\"0\">");
				if(object != null) {
					cell.render(context, hasCell.getValue(object), sb);
				}

				sb.appendHtmlConstant("</td>");
			}

			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
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

				return isSelected;
			}
			@Override
			public FieldUpdater<CQLCode, Boolean> getFieldUpdater() {
				return new FieldUpdater<CQLCode, Boolean>() {
					@Override
					public void update(int index, CQLCode object,
							Boolean isCBChecked) {

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
					public void update(int index, CQLCode object,
							SafeHtml value) {
						if ((object != null)) {
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
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: darkgoldenrod; margin-left: -15px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Edit</button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"color: black; margin-left: -15px\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Edit</span></button>");
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
					public void update(int index, CQLCode object,
							SafeHtml value) {
						if ((object != null) && !object.isUsed()) {
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
				if (object.isUsed()) {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" disabled style=\"margin-left: 0px;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Delete</span></button>");
				} else {
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"margin-left: 0px;\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">Delete</button>");
				}
								
				return sb.toSafeHtml();
			}
		};
				
	}
	
	public void clearSelectedCheckBoxes(){
		if(table!=null){
			List<CQLCode> displayedItems = new ArrayList<CQLCode>();
			displayedItems.addAll(codesSelectedList);
			codesSelectedList = new  ArrayList<CQLCode>();
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

	public void setReadOnly(boolean isEditable) {		
		getIncludeCodeSystemVersionCheckBox().setEnabled(isEditable);
		getSaveButton().setEnabled(isEditable);
		getCancelCodeButton().setEnabled(isEditable);
		getRetrieveFromVSACButton().setEnabled(isEditable);
		this.setIsLoading(!isEditable);
	}

	public List<CQLCode> getCodesSelectedList() {
		return codesSelectedList;
	}

	public List<CQLCode> setMatCodeList(List<CQLCode> copiedCodeList, List<CQLCode> appliedCodeTableList) {
		List<CQLCode> codesToPaste = new ArrayList<CQLCode>();
		for(CQLCode cqlCode: copiedCodeList) {
			boolean isDuplicate = appliedCodeTableList.stream().anyMatch(c -> c.getDisplayName().equals(cqlCode.getDisplayName()));
			if(!isDuplicate) {
				codesToPaste.add(cqlCode);
			}
		}
		return codesToPaste;
	}	
	
	public CheckBox getIncludeCodeSystemVersionCheckBox() {
		return includeCodeSystemVersionCheckBox;
	}

	public void setIncludeCodeSystemVersionCheckBox(CheckBox includeCodeSystemVersionCheckBox) {
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
		
		 if (transferObject.isValidModel() && 
				 transferObject.isValidCodeData(this.getValidateCodeObject())){
			 return transferObject;
		 }
		 
		 return null;
	}
	
	public List<CQLCode> getAllCodes(){
		return allCodes;
	}
	
}
