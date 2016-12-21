package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*import mat.shared.CustomBootStrapCheckBox;*/
import org.gwtbootstrap3.client.ui.CheckBox;

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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import mat.client.CustomPager;
import mat.client.clause.QDSAppliedListModel;
import mat.client.codelist.HasListBox;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SaveCancelButtonBar;
import mat.client.shared.SearchWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.umls.service.VSACAPIServiceAsync;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.QualityDataSetDTO;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.ConstantMessages;



/**
 * The Class QDMAppliedSelectionView.
 */
public class CQLQDMAppliedView implements HasSelectionHandlers<Boolean> {
	
	static final String GROUPING_QDM = " (G)";
	static final String EXTENSIONAL_QDM = " (E)";
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		
		/**
		 * On modify clicked.
		 * 
		 * @param result
		 *            the result
		 */
		void onModifyClicked(CQLQualityDataSetDTO result);
		
		/**
		 * On delete clicked.
		 *
		 * @param result            the result
		 * @param index the index
		 */
		void onDeleteClicked(CQLQualityDataSetDTO result, int index);
		
		
		/**
		 * On top qdm paste clicked.
		 */
		void onTopQDMPasteClicked();
		
		/**
		 * On bottom qdm paste clicked.
		 */
		void onBottomQDMPasteClicked();
	}
	
	/** The observer. */
	private Observer observer;
	
	/** The expansion Identifier selection. */
	/*private CustomCheckBox defaultExpIdentifierSel = new CustomCheckBox("Select an Expansion Identifier",
			"Use a default Expansion Identifier ?", 1);*/
	/*CustomBootStrapCheckBox defaultExpIdentifierSel = new CustomBootStrapCheckBox("Use a default Expansion Identifier ?");*/
	private CheckBox defaultExpIdentifierSel = new CheckBox();
//	ToggleSwitch toggleSwitch = new ToggleSwitch();
	/** The vsac profile list box. */
	private ListBoxMVP defaultExpIdentifierListBox = new ListBoxMVP();
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	/** The vsacapi service async. */
	VSACAPIServiceAsync vsacapiServiceAsync = MatContext.get()
			.getVsacapiServiceAsync();
	
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	
	/** The update vsac error message panel. */
	private ErrorMessageDisplay updateVSACErrorMessagePanel = new ErrorMessageDisplay();
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 15;
	
	/** The table. */
	private CellTable<CQLQualityDataSetDTO> table;
	
	/** The sort provider. */
	private ListDataProvider<CQLQualityDataSetDTO> listDataProvider;
	
	/** The update button. */
	private Button updateVSACButton = new PrimaryButton("Update From VSAC ","primaryButton");
	
	/** The apply button. */
	private Button applyDefaultExpansionIdButton = new PrimaryButton("Apply", "primaryButton");
	
	/** The version list. */
	private List<String> versionList = new ArrayList<String>();
	
	/** The profile list. */
	private List<String> expIdentifierList = new ArrayList<String>();
	
	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel = new SuccessMessageDisplay();
	
	/** The update vsac success message panel. */
	private SuccessMessageDisplay updateVSACSuccessMessagePanel = new SuccessMessageDisplay();
	
	/** The last selected object. */
	private CQLQualityDataSetDTO lastSelectedObject;
	
	/** The expansion pro list box. */
	ListBoxMVP qdmExpIdentifierListBox = new ListBoxMVP();
	
	/** The version list box. */
	ListBoxMVP versionListBox = new ListBoxMVP();
	
	
	/** The name input. */
	private MatTextBox nameInput = new MatTextBox();
	
	/** The is editable. */
	private boolean isEditable;
	
	/** The specific occurrence check box. */
	private CheckBox specificOcurChkBox;
	
	/** The in progress message display. */
	private InProgressMessageDisplay inProgressMessageDisplay = new InProgressMessageDisplay();
	
	/** The search header. */
	private  Label searchHeader = new Label("Search");
	
	/** The vsac profile header. */
	private Label defaultExpIdentifierHeader = new Label("Apply Expansion Identifier");
	
	/** The spager. */
	private MatSimplePager spager;
	
	/** The save cancel button bar. */
	private SaveCancelButtonBar saveCancelButtonBar = new SaveCancelButtonBar();
	
	/** The search widget. */
	private SearchWidget searchWidget = new SearchWidget("Retrieve OID",
			"Enter OID", "textSearchWidget");
	
	/** The main panel. */
	VerticalPanel mainPanel;
	
	/** The qdm selected list. */
	private List<CQLQualityDataSetDTO> qdmSelectedList;
	
	/** The selection model. */
	private MultiSelectionModel<CQLQualityDataSetDTO> selectionModel;
	
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
	public CQLQDMAppliedView() {
		
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
		hp.add(buildElementWithVSACExpansionIdentifier());
		
		verticalPanel.getElement().setId("vPanel_VerticalPanel");
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(successMessagePanel);
		verticalPanel.add(errorMessagePanel);
		errorMessagePanel.getElement().setId(
				"errorMessagePanel_ErrorMessageDisplay");
		
		HorizontalPanel bottomButtonLayOut = new HorizontalPanel();
		
		bottomButtonLayOut.setStyleName("continueButton");
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		hPanel.setWidth("600px");
		hPanel.add(updateVSACButton);
		hPanel.add(bottomButtonLayOut);
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
		updateVSACButton.setTitle("Retrieve the most recent versions of value sets from VSAC");
		updateVSACButton.getElement().setId("updateVsacButton_Button");
//		verticalPanel.add(topButtonLayOut);
		verticalPanel.add(cellTablePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hPanel);
		verticalPanel.add(inProgressMessageDisplay);
		verticalPanel.add(updateVSACSuccessMessagePanel);
		verticalPanel.add(updateVSACErrorMessagePanel);
		
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id",
				"subQDMAPPliedListContainerPanel");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
		//MatContext.get().setQDMAppliedSelectionView(this);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#buildPasteTopPanel(boolean)
	 */
	
	
	
	
	
	/**
	 * Builds the element with vsac expansion identifier.
	 *
	 * @return the widget
	 */
	private Widget buildElementWithVSACExpansionIdentifier() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		mainPanel.setWidth("95%");
		mainPanel.add(buildDefaultExpIdentifierPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}
	
	
	
	
	/**
	 * Builds the vsac exp identifier panel.
	 *
	 * @return the widget
	 */
	private Widget buildDefaultExpIdentifierPanel() {
		/*defaultExpIdentifierSel.getElement().setId("ExpansionIdentifierSelection_ChkBox");*/
		defaultExpIdentifierListBox.setWidth("180px");
		defaultExpIdentifierListBox.getElement().setId("DefaultExpansionIdentifier_ListBox");
		defaultExpIdentifierListBox.getElement().setTitle("Expansion Identifier Selection List");
		applyDefaultExpansionIdButton.setTitle("Apply Expansion Identifier to all the QDM Element(s).");
		applyDefaultExpansionIdButton.getElement().setId("applyToQDM_button");
		defaultExpIdentifierListBox.addItem("--Select--");
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.setWidth("290px");
		searchPanel.setHeight("227px");
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("valueSetSearchPanel");
		defaultExpIdentifierHeader.getElement().setId("searchHeader_Label");
		defaultExpIdentifierHeader.setStyleName("valueSetHeader");
		defaultExpIdentifierHeader.getElement().setAttribute("tabIndex", "0");
		defaultExpIdentifierHeader.getElement().setTitle("Apply VSAC Expansion Identifier to Measure.");
		searchPanel.add(defaultExpIdentifierHeader);
		searchPanel.add(new SpacerWidget());
		Grid queryGrid = new Grid(5, 1);
		
		HorizontalPanel qdmHorizontalPanel = new HorizontalPanel();
		qdmHorizontalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		qdmHorizontalPanel.getElement().setId("horizontalPanel_HorizontalPanel");
		InlineLabel defaultExpIdentifierLabel = new InlineLabel("Add Default Expansion Identifier");
		
		defaultExpIdentifierLabel.setStyleName("qdmLabel");
		defaultExpIdentifierSel.setStyleName("gwt-CheckBox");
		qdmHorizontalPanel.add(defaultExpIdentifierLabel);
		qdmHorizontalPanel.add(defaultExpIdentifierSel);
		qdmHorizontalPanel.setStyleName("horizontalPanel");
		queryGrid.setWidget(0, 0, qdmHorizontalPanel);
		queryGrid.setWidget(1, 0, new SpacerWidget());
		queryGrid.setWidget(2, 0, defaultExpIdentifierListBox);
		queryGrid.setWidget(3, 0, new SpacerWidget());
		queryGrid.setWidget(4, 0, applyDefaultExpansionIdButton);
		queryGrid.setStyleName("secondLabel");
		searchPanel.add(queryGrid);
		return searchPanel;
	}
	
	
	
	/**
	 * Builds the element with vsac value set widget.
	 *
	 * @return the widget
	 */
	private Widget buildElementWithVSACValueSetWidget() {
		mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		
		mainPanel.add(buildSearchPanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}
	
	
	/**
	 * Builds the search panel.
	 *
	 * @return the widget
	 */
	private Widget buildSearchPanel() {
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("valueSetSearchPanel");
		
		searchHeader.getElement().setId("searchHeader_Label");
		searchHeader.setStyleName("valueSetHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		searchHeader.getElement().setTitle("Search by OID and Name");
		searchPanel.add(searchHeader);
		searchPanel.add(new SpacerWidget());
		
		nameInput.getElement().setId("nameInput_TextBox");
		nameInput.getElement().setAttribute("tabIndex", "0");
		nameInput.setTitle("Enter Name");
		nameInput.setWidth("250px");
		
		qdmExpIdentifierListBox.getElement().setId("QDMExpansionIdentifier_ListBox");
		qdmExpIdentifierListBox.getElement().setTitle("Expansion Identifier Selection List");
		qdmExpIdentifierListBox.setEnabled(false);
		qdmExpIdentifierListBox.setWidth("250px");
		versionListBox.getElement().setId("Version_ListBox");
		versionListBox.getElement().setTitle("Version Selection List");
		versionListBox.setEnabled(false);
		versionListBox.setWidth("250px");
				
		saveCancelButtonBar.getSaveButton().setText("Apply");
		searchWidget.getSearchInput().setWidth("220px");
		searchWidget.getSearchInput().setHeight("20px");
		searchWidget.getSearchInput().setTitle("Enter OID");
		Grid queryGrid = new Grid(7, 1);
		queryGrid.setWidget(0, 0, LabelBuilder.buildLabel(new Label(), "Name"));
		queryGrid.setWidget(1, 0, nameInput);
		
		queryGrid.setWidget(2, 0, LabelBuilder.buildLabel("Expansion Identifier", "Expansion Identifier"));
		queryGrid.setWidget(3, 0, qdmExpIdentifierListBox);
		
		queryGrid.setWidget(4, 0, LabelBuilder.buildLabel("Version", "Version"));
		queryGrid.setWidget(5, 0, versionListBox);
		queryGrid.setWidget(6, 0, saveCancelButtonBar);
		queryGrid.setStyleName("secondLabel");
		searchPanel.add(searchWidget);
		searchPanel.add(new SpacerWidget());
		searchPanel.add(queryGrid);
		return searchPanel;
	}
	
	/**
	 * Builds the cell table.
	 *
	 * @param appliedListModel            the applied list model
	 * @param isEditable the is editable
	 */
	////@Override
	public void buildAppliedQDMCellTable(List<CQLQualityDataSetDTO> appliedValueSetList, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if ((appliedValueSetList != null)
				&& (appliedValueSetList.size() > 0)) {
			table = new CellTable<CQLQualityDataSetDTO>();
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
					pagerResources, false, 0, true);
			spager.setDisplay(table);
			spager.setPageStart(0);
			Label invisibleLabel;
			if(isEditable){
				invisibleLabel = (Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied QDM Elements table Name in First Column"
										+ "OID in Second Column, DataType in Third Column, Expansion Identifier in Fourth Column,"
										+ "Version in Fifth Column and Modify in Sixth Column where the user can Edit and Delete "
										+ "the existing QDM. The Applied QDM elements are listed alphabetically in a table.");
			} else {
				invisibleLabel = (Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied QDM Elements table Name in First Column"
										+ "OID in Second Column, DataType in Third Column, Expansion Identifier in Fourth Column,"
										+ "Version in Fifth Column and Select in Sixth Column. The Applied QDM elements are listed alphabetically in a table.");
			}
			table.getElement().setAttribute("id", "AppliedQDMTable");
			table.getElement().setAttribute("aria-describedby",
					"appliedQDMTableSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			
		} else {
			Label searchHeader = new Label("QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No QDM Elements.</p>");
			cellTablePanel.add(searchHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @param table the table
	 * @param sortHandler the sort handler
	 * @param isEditable the is editable
	 * @return the cell table
	 */
	private CellTable<CQLQualityDataSetDTO> addColumnToTable(
			final CellTable<CQLQualityDataSetDTO> table,
			ListHandler<CQLQualityDataSetDTO> sortHandler, boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			Label searchHeader = new Label("QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("measureGroupingTableHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
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
					// if the QDM element is not user defined, add (G) for Grouping or (E) for extensional.
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						if (object.getTaxonomy().equalsIgnoreCase("Grouping")) {
							qdmType = GROUPING_QDM;
						} else {
							qdmType = EXTENSIONAL_QDM;
						}
					}
					
						value = value.append(object.getCodeListName()).append(qdmType);
						title = title.append("Name : ").append(value);
					
					title.append("");
					return CellTableUtility.getColumnToolTip(value.toString(),
							title.toString());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Name\">" + "Name"
							+ "</span>"));
			
			// OID Column
			Column<CQLQualityDataSetDTO, SafeHtml> oidColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String oid = null;
					if (object.getOid().equalsIgnoreCase(
							ConstantMessages.USER_DEFINED_QDM_OID)) {
						title = title.append("OID : ").append(
								ConstantMessages.USER_DEFINED_QDM_NAME);
						oid = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
					} else {
						title = title.append("OID : ").append(object.getOid());
						oid = object.getOid();
					}
					return getOIDColumnToolTip(oid, title,
							object.isHasModifiedAtVSAC(),
							object.isNotFoundInVSAC());
				}
			};
			table.addColumn(oidColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"OID\">" + "OID"
							+ "</span>"));
			
			
			// Expansion Identifier Column
			Column<CQLQualityDataSetDTO, SafeHtml> expansionColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					if (object.getExpansionIdentifier() != null) {
						StringBuilder title = new StringBuilder();
						title = title.append("Expansion Identifier : ").append(
								object.getExpansionIdentifier());
						return CellTableUtility.getColumnToolTip(
								object.getExpansionIdentifier(), title.toString());
					}
					
					return null;
				}
			};
			table.addColumn(expansionColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Expansion Identifier\">"
							+ "Expansion Identifier" + "</span>"));
			
			// Version Column
			Column<CQLQualityDataSetDTO, SafeHtml> versionColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (!object.getOid().equalsIgnoreCase(
							ConstantMessages.USER_DEFINED_QDM_OID) ) {
						if(object.getExpansionIdentifier()==null){
							if ((object.getVersion()!=null)  &&
									(object.getVersion().equalsIgnoreCase("1.0")
											|| object.getVersion().equalsIgnoreCase("1"))) {
								title = title.append("Version : ").append(
										"Most Recent");
								version = "Most Recent";
							} else {
								title = title.append("Version : ").append(object.getVersion());
								version = object.getVersion();
							}
						} else {
							version = "";
						}
					}else {
						version = "";
					}
					return CellTableUtility.getColumnToolTip(version,
							title.toString());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Version\">" + "Version"
							+ "</span>"));
			
			String colName = "Modify";
			
			if(!isEditable){
				colName = "Select";
			}
			
			// Modify by Delete Column
			table.addColumn(new Column<CQLQualityDataSetDTO, CQLQualityDataSetDTO>(
					getCompositeCellForQDMModifyAndDelete(isEditable)) {
				
				@Override
				public CQLQualityDataSetDTO getValue(CQLQualityDataSetDTO object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "
					+ colName + "</span>"));
			
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			table.setColumnWidth(3, 14.0, Unit.PCT);
			table.setColumnWidth(4, 2.0, Unit.PCT);
		}
		
		return table;
	}
	
	/**
	 * Adds the selection handler on table.
	 * 
	 * @param appliedListModel
	 *            the applied list model
	 * @return the single selection model
	 */
	/*public SingleSelectionModel<CQLQualityDataSetDTO> addSelectionHandlerOnTable(
			final QDSAppliedListModel appliedListModel) {
		final SingleSelectionModel<CQLQualityDataSetDTO> selectionModel = new SingleSelectionModel<CQLQualityDataSetDTO>();
		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				CQLQualityDataSetDTO qualityDataSetDTO = selectionModel
						.getSelectedObject();
				if (qualityDataSetDTO != null) {
					errorMessagePanel.clear();
					appliedListModel.setLastSelected(selectionModel
							.getSelectedObject());
					System.out
					.println("appliedListModel.getLastSelected() =======>>>>"
							+ appliedListModel
							.getLastSelected());
				}
			}
		});
		return selectionModel;
	}
*/	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#asWidget()
	 */
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	//@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#
	 * getVSACProfileInput()
	 */
	/**
	 * Gets the VSAC profile input.
	 *
	 * @return the VSAC profile input
	 */
	//@Override
	public HasValueChangeHandlers<Boolean> getDefaultExpIDInput() {
//		return defaultExpIdentifierSel;
		return null;
	}
	//@Override
	public CheckBox getDefaultExpIdentifierSel(){
		return defaultExpIdentifierSel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getSpecificOccChkBox()
	 */
	/**
	 * Gets the specific occ chk box.
	 *
	 * @return the specific occ chk box
	 */
	//@Override
	public CheckBox getSpecificOccChkBox(){
		return specificOcurChkBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getDataTypeText(mat.client.shared.ListBoxMVP)
	 */
	/**
	 * Gets the data type text.
	 *
	 * @param inputListBox the input list box
	 * @return the data type text
	 */
	//@Override
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getDataTypeValue(mat.client.shared.ListBoxMVP)
	 */
	/**
	 * Gets the data type value.
	 *
	 * @param inputListBox the input list box
	 * @return the data type value
	 */
	//@Override
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getVersionValue(mat.client.shared.ListBoxMVP)
	 */
	/**
	 * Gets the version value.
	 *
	 * @param inputListBox the input list box
	 * @return the version value
	 */
	//@Override
	public String getVersionValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getExpansionProfileValue(mat.client.shared.ListBoxMVP)
	 */
	/**
	 * Gets the expansion Identifier value.
	 *
	 * @param inputListBox the input list box
	 * @return the expansion Identifier value
	 */
	//@Override
	public String getExpansionIdentifierValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#
	 * getVSACProfileListBox()
	 */
	/**
	 * Gets the VSAC expansion Identifier list box.
	 *
	 * @return the VSAC expansion Identifier list box
	 */
	//@Override
	public ListBoxMVP getVSACExpansionIdentifierListBox() {
		return defaultExpIdentifierListBox;
	}
	
	/**
	 * Check for enable.
	 * 
	 * @return true, if successful
	 */
	private boolean checkForEnable() {
		return MatContext.get().getMeasureLockService()
				.checkForEditPermission();
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#
	 * getErrorMessageDisplay()
	 */
	/**
	 * Gets the error message display.
	 *
	 * @return the error message display
	 */
	//@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#
	 * setVSACProfileListBox()
	 */
	/**
	 * Sets the vsac expansion identifier list box.
	 */
	//@Override
	public void setDefaultExpansionIdentifierListBox() {
		defaultExpIdentifierListBox.clear();
		defaultExpIdentifierListBox.addItem("--Select--");
		for (int i = 0; (i < getExpIdentifierList().size())
				&& (getExpIdentifierList() != null); i++) {
			defaultExpIdentifierListBox.addItem(getExpIdentifierList().get(i));
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setVSACProfileListBoxOptions(java.util.List)
	 */
	/**
	 * Sets the VSAC profile list box.
	 *
	 * @param texts the new VSAC profile list box
	 */
	//@Override
	public void setQDMExpIdentifierListBox(List<? extends HasListBox> texts){
		setQDMExpIdentifierListBoxItems(qdmExpIdentifierListBox, texts, MatContext.PLEASE_SELECT);
	}
	
	
	/**
	 * Sets the profile list box items.
	 *
	 * @param dataTypeListBox the data type list box
	 * @param itemList the item list
	 * @param defaultOption the default option
	 */
	private void setQDMExpIdentifierListBoxItems(ListBoxMVP dataTypeListBox,
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
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setVSACVersionListBoxOptions(java.util.List)
	 */
	/**
	 * Sets the VSAC version list box options.
	 *
	 * @param texts the new VSAC version list box options
	 */
	//@Override
	public void setQDMVersionListBoxOptions(List<? extends HasListBox> texts){
		setVersionListBoxItems(versionListBox, texts, MatContext.PLEASE_SELECT);
	}
	
	
	/**
	 * Sets the version list box items.
	 *
	 * @param dataTypeListBox the data type list box
	 * @param itemList the item list
	 * @param defaultOption the default option
	 */
	private void setVersionListBoxItems(ListBoxMVP dataTypeListBox,
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
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#
	 * resetVSACValueSetWidget()
	 */
	/**
	 * Reset vsac value set widget.
	 */
	//@Override
	public void resetVSACValueSetWidget() {
		defaultExpIdentifierSel.setValue(false);
		//toggleSwitch.setValue(false);
		defaultExpIdentifierListBox.clear();
		defaultExpIdentifierListBox.setEnabled(false);
		defaultExpIdentifierListBox.addItem("--Select--");
		
		if(checkForEnable()){
			qdmExpIdentifierListBox.setEnabled(false);
			versionListBox.setEnabled(false);
			//dataTypeListBox.setEnabled(false);
			searchWidget.getSearchInput().setTitle("Enter OID");
			nameInput.setTitle("Enter Name");
			//saveButton.setEnabled(false);
			
		}
		searchHeader.setText("Search");
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.shared.HasHandlers#fireEvent(com.google.gwt.event
	 * .shared.GwtEvent)
	 */
	/**
	 * Fire event.
	 *
	 * @param event the event
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.event.logical.shared.HasSelectionHandlers#addSelectionHandler
	 * (com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	/**
	 * Adds the selection handler.
	 *
	 * @param handler the handler
	 * @return the handler registration
	 */
	//@Override
	public HandlerRegistration addSelectionHandler(
			SelectionHandler<Boolean> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	/**
	 * Gets the observer.
	 * 
	 * @return the observer
	 */
	public Observer getObserver() {
		return observer;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setObserver
	 * (mat.client.clause.VSACProfileSelectionView.Observer)
	 */
	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	//@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/**
	 * Gets the composite cell for bulk export.
	 *
	 * @param isEditable the is editable
	 * @return the composite cell for bulk export
	 */
	private CompositeCell<CQLQualityDataSetDTO> getCompositeCellForQDMModifyAndDelete(boolean isEditable) {
		final List<HasCell<CQLQualityDataSetDTO, ?>> cells = new LinkedList<HasCell<CQLQualityDataSetDTO, ?>>();
		if(isEditable){
			cells.add(getModifyQDMButtonCell());
			cells.add(getDeleteQDMButtonCell());
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
	
	/**
	 * Gets the modify qdm button cell.
	 * 
	 * @return the modify qdm button cell
	 */
	private HasCell<CQLQualityDataSetDTO, SafeHtml> getModifyQDMButtonCell() {
		
		HasCell<CQLQualityDataSetDTO, SafeHtml> hasCell = new HasCell<CQLQualityDataSetDTO, SafeHtml>() {
			
			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}
			
			//@Override
			public FieldUpdater<CQLQualityDataSetDTO, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<CQLQualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object,
							SafeHtml value) {
						if ((object != null)) {
							//observer.onModifyClicked(object);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(CQLQualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Modify QDM";
				String cssClass = "customEditButton";
				if(isEditable){
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title
							+ "' class=\" " + cssClass + "\">Editable</button>");
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title
							+ "' class=\" " + cssClass + "\" disabled/>Editable</button>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	/**
	 * Gets the delete qdm button cell.
	 * 
	 * @return the delete qdm button cell
	 */
	private HasCell<CQLQualityDataSetDTO, SafeHtml> getDeleteQDMButtonCell() {
		
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
				String title = "Click to Delete QDM";
				String cssClass;
				//Disabling the delete button from 5.0 onwards
				object.setUsed(true);
				if (object.isUsed()) {
					cssClass = "customDeleteDisableButton";
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass
							+ "\"disabled/>Delete</button>");
				} else {
					cssClass = "customDeleteButton";
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='"
							+ title + "' class=\" " + cssClass
							+ "\"/>Delete</button>");
				}
				return sb.toSafeHtml();
			}
		};
		
		return hasCell;
	}
	
	
	/**
	 * Gets the QDM check box cell.
	 *
	 * @return the QDM check box cell
	 */
	private HasCell<CQLQualityDataSetDTO, Boolean> getQDMCheckBoxCell(){
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
						if(isCBChecked) {
							qdmSelectedList.add(object);
						} else{
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
	
	
	
	/**
	 * Gets the OID column tool tip.
	 * 
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @param hasImage
	 *            the has image
	 * @param isUserDefined
	 *            the is user defined
	 * @return the OID column tool tip
	 */
	private SafeHtml getOIDColumnToolTip(String columnText,
			StringBuilder title, boolean hasImage, boolean isUserDefined) {
		if (hasImage && !isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"QDM Updated From VSAC.\""
					+ "title = \"QDM Updated From VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else if (hasImage && isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : QDM not available in VSAC.\""
					+ " title=\"QDM not available in VSAC.\"/>"
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
	
	/**
	 * Gets the version list.
	 *
	 * @return the version list
	 */
	public List<String> getVersionList() {
		return versionList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getAddNewQDMButton()
	 */
	/**
	 * Gets the cancel qdm button.
	 *
	 * @return the cancel qdm button
	 */
//	@Override
	public Button getCancelQDMButton() {
		return saveCancelButtonBar.getCancelButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getApplyButton()
	 */
	/**
	 * Gets the apply button.
	 *
	 * @return the apply button
	 */
	//@Override
	public Button getApplyDefaultExpansionIdButton(){
		return applyDefaultExpansionIdButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getRetrieveFromVSACButton()
	 */
	/**
	 * Gets the retrieve from vsac button.
	 *
	 * @return the retrieve from vsac button
	 */
	//@Override
	public PrimaryButton getRetrieveFromVSACButton(){
		return searchWidget.getSearchButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getSaveButton()
	 */
	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	//@Override
	public Button getSaveButton(){
		return saveCancelButtonBar.getSaveButton();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getUpdateFromVSACButton()
	 */
	/**
	 * Gets the update from vsac button.
	 *
	 * @return the update from vsac button
	 */
	//@Override
	public Button getUpdateFromVSACButton(){
		return updateVSACButton;
	}
	/**
	 * Gets the profile list.
	 *
	 * @return the profile list
	 */
	public List<String> getExpIdentifierList() {
		return expIdentifierList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setProfileList(java.util.List)
	 */
	/**
	 * Sets the profile list.
	 *
	 * @param expIdentifierList the new profile list
	 */
	//@Override
	public void setExpIdentifierList(List<String> expIdentifierList) {
		this.expIdentifierList = expIdentifierList;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getSelectedElementToRemove()
	 */
	/**
	 * Gets the selected element to remove.
	 *
	 * @return the selected element to remove
	 */
	//@Override
	public CQLQualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getDataTypesListBox()
	 */
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getVersionListBox()
	 */
	/**
	 * Gets the version list box.
	 *
	 * @return the version list box
	 */
	//@Override
	public ListBoxMVP getVersionListBox() {
		return versionListBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getExpansionProfileListBox()
	 */
	/**
	 * Gets the VSAC profile list box.
	 *
	 * @return the VSAC profile list box
	 */
	//@Override
	public ListBoxMVP getQDMExpIdentifierListBox() {
		return qdmExpIdentifierListBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getOIDInput()
	 */
	/**
	 * Gets the OID input.
	 *
	 * @return the OID input
	 */
	//@Override
	public MatTextBox getOIDInput() {
		return searchWidget.getSearchInput();
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getUserDefinedInput()
	 */
	/**
	 * Gets the user defined input.
	 *
	 * @return the user defined input
	 */
	//@Override
	public MatTextBox getUserDefinedInput() {
		return nameInput;
	}
	
	/**
	 * Checks if is editable.
	 *
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return isEditable;
	}
	
	
	/**
	 * Sets the editable.
	 *
	 * @param isEditable the new editable
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getInProgressMessageDisplay()
	 */
	/**
	 * Gets the in progress message display.
	 *
	 * @return the in progress message display
	 */
	//@Override
	public InProgressMessageDisplay getInProgressMessageDisplay() {
		return inProgressMessageDisplay;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getSearchHeader()
	 */
	/**
	 * Gets the search header.
	 *
	 * @return the search header
	 */
	//@Override
	public Label getSearchHeader() {
		return searchHeader;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getSuccessMessageDisplay()
	 */
	/**
	 * Gets the success message display.
	 *
	 * @return the success message display
	 */
	//@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessagePanel;
	}
	
	/**
	 * Gets the list data provider.
	 *
	 * @return the list data provider
	 */
	//@Override
	public ListDataProvider<CQLQualityDataSetDTO> getListDataProvider(){
		return listDataProvider;
	}
	
	/**
	 * Gets the simple pager.
	 *
	 * @return the simple pager
	 */
	//@Override
	public MatSimplePager getSimplePager(){
		return spager;
	}
	
	/**
	 * Gets the celltable.
	 *
	 * @return the celltable
	 */
	//@Override
	public CellTable<CQLQualityDataSetDTO> getCelltable(){
		return table;
	}
	
	/**
	 * Gets the pager.
	 *
	 * @return the pager
	 */
	//@Override
	public MatSimplePager getPager(){
		return spager;
	}
	
	/**
	 * Gets the update vsac error message panel.
	 *
	 * @return the update vsac error message panel
	 */
	//@Override
	public ErrorMessageDisplay getUpdateVSACErrorMessagePanel() {
		return updateVSACErrorMessagePanel;
	}
	
	/**
	 * Gets the update vsac success message panel.
	 *
	 * @return the update vsac success message panel
	 */
	//@Override
	public SuccessMessageDisplay getUpdateVSACSuccessMessagePanel() {
		return updateVSACSuccessMessagePanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getMainPanel()
	 */
	//@Override
	public VerticalPanel getMainPanel(){
		return mainPanel;
	}
	
	
	/**
	 * Resets the  message panel.
	 */
	public void resetQDSMsgPanel() {
		getSuccessMessageDisplay().clear();
		getErrorMessageDisplay().clear();
		getUpdateVSACSuccessMessagePanel().clear();
		getUpdateVSACErrorMessagePanel().clear();
	}
	
	
}
