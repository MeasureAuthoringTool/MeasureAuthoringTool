package mat.client.clause;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.codelist.HasListBox;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.CustomButton;
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
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.ConstantMessages;
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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class QDMAppliedSelectionView.
 */
public class QDMAppliedSelectionView implements QDMAppliedSelectionPresenter.SearchDisplay,
HasSelectionHandlers<Boolean> {
	
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
		void onModifyClicked(QualityDataSetDTO result);
		
		/**
		 * On delete clicked.
		 *
		 * @param result            the result
		 * @param index the index
		 */
		void onDeleteClicked(QualityDataSetDTO result, int index);
		
		
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
	private CustomCheckBox defaultExpIdentifierSel = new CustomCheckBox("Select an Expansion Identifier",
			"Use a default Expansion Identifier ?", 1);
	
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
	private CellTable<QualityDataSetDTO> table;
	
	/** The sort provider. */
	private ListDataProvider<QualityDataSetDTO> listDataProvider;
	
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
	private QualityDataSetDTO lastSelectedObject;
	
	/** The expansion pro list box. */
	ListBoxMVP qdmExpIdentifierListBox = new ListBoxMVP();
	
	/** The version list box. */
	ListBoxMVP versionListBox = new ListBoxMVP();
	
	/** The data type list box. */
	ListBoxMVP dataTypeListBox = new ListBoxMVP();
	
	/** The name input. */
	private MatTextBox nameInput = new MatTextBox();
	
	/** The is editable. */
	private boolean isEditable;
	
	/** The specific occurrence check box. */
	private CustomCheckBox specificOcurChkBox;
	
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
	private List<QualityDataSetDTO> qdmSelectedList;
	
	/** The selection model. */
	private MultiSelectionModel<QualityDataSetDTO> selectionModel;
	
	/** The copy qdm top button. */
	private CustomButton copyQDMTopButton = (CustomButton) getImage("Copy",
			ImageResources.INSTANCE.getCopy(), "Copy");
	
	/** The paste qdm top button. */
	private CustomButton pasteQDMTopButton;
	
	/** The clear qdm top button. */
	private CustomButton clearQDMTopButton = (CustomButton) getImage("Clear",
			ImageResources.INSTANCE.getErase(), "Clear");
	
	/** The copy qdm bottom button. */
	private CustomButton copyQDMBottomButton = (CustomButton) getImage("Copy",
			ImageResources.INSTANCE.getCopy(), "Copy");
	
	/** The paste qdm bottom button. */
	private CustomButton pasteQDMBottomButton;
	
	/** The clear qdm bottom button. */
	private CustomButton clearQDMBottomButton = (CustomButton) getImage("Clear",
			ImageResources.INSTANCE.getErase(), "Clear");
	
	/** The paste top button panel. */
	private SimplePanel pasteTopButtonPanel = new SimplePanel();
	
	/** The paste bottom button panel. */
	private SimplePanel pasteBottomButtonPanel = new SimplePanel();;
	
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
	public QDMAppliedSelectionView() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.getElement().setId("simplePanel_SimplePanel");
		simplePanel.setWidth("5px");
		/* Horizontal Button Panel
		 * for copy, paste and clear
		 * */
		HorizontalPanel topButtonLayOut = new HorizontalPanel();
		copyQDMTopButton.getElement().setId("copyQDMTop_button");
		clearQDMTopButton.getElement().setId("clearQDMTop_butotn");
		
		copyQDMTopButton.getElement().setAttribute("tabIndex", "0");
		clearQDMTopButton.getElement().setAttribute("tabIndex", "0");
		
		topButtonLayOut.getElement().setId("topButtonLayOut_HorzPanel");
		topButtonLayOut.add(copyQDMTopButton);
		topButtonLayOut.add(buildPasteTopPanel(checkForEnable()));
		topButtonLayOut.add(clearQDMTopButton);
		topButtonLayOut.setStyleName("continueButton");
		
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
		SimplePanel pasteTopPanel =  new SimplePanel();
		copyQDMBottomButton.getElement().setId("copyQDMBottom_button");
		clearQDMBottomButton.getElement().setId("clearQDMBottom_butotn");
		
		copyQDMBottomButton.getElement().setAttribute("tabIndex", "0");
		clearQDMBottomButton.getElement().setAttribute("tabIndex", "0");
		
		bottomButtonLayOut.getElement().setId("bottomButtonLayOut_HorzPanel");
		pasteTopPanel.add(pasteQDMBottomButton);
		
		bottomButtonLayOut.add(copyQDMBottomButton);
		bottomButtonLayOut.add(buildPasteBottomPanel(checkForEnable()));
		bottomButtonLayOut.add(clearQDMBottomButton);
		bottomButtonLayOut.setStyleName("continueButton");
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		hPanel.setWidth("930px");
		hPanel.add(updateVSACButton);
		hPanel.add(bottomButtonLayOut);
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
		updateVSACButton.setTitle("Retrieve the most recent versions of applied value sets from VSAC");
		updateVSACButton.getElement().setId("updateVsacButton_Button");
		verticalPanel.add(topButtonLayOut);
		//		verticalPanel.add(new SpacerWidget());
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
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQDMAppliedSelectionView(this);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#buildPasteTopPanel(boolean)
	 */
	@Override
	public Widget buildPasteTopPanel(final boolean isEditable){
		pasteTopButtonPanel.clear();
		if(isEditable){
			pasteQDMTopButton = (CustomButton) getImage("Paste",
					ImageResources.INSTANCE.getPaste(), "Paste");
		} else {
			pasteQDMTopButton = (CustomButton) getImage("Paste",
					ImageResources.INSTANCE.getGrayScalePaste(), "Paste");
		}
		pasteQDMTopButton.getElement().setId("pasteQDMTop_button");
		pasteQDMTopButton.getElement().setAttribute("tabIndex", "0");
		pasteTopButtonPanel.add(pasteQDMTopButton);
		
		pasteQDMTopButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isEditable){
					observer.onTopQDMPasteClicked();
				}
				
			}
		});
		return pasteTopButtonPanel;
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#buildPasteBottomPanel(boolean)
	 */
	@Override
	public Widget buildPasteBottomPanel(final boolean isEditable){
		pasteBottomButtonPanel.clear();
		if(isEditable){
			pasteQDMBottomButton = (CustomButton) getImage("Paste",
					ImageResources.INSTANCE.getPaste(), "Paste");
		} else {
			pasteQDMBottomButton = (CustomButton) getImage("Paste",
					ImageResources.INSTANCE.getGrayScalePaste(), "Paste");
		}
		pasteQDMBottomButton.getElement().setId("pasteQDMBottom_button");
		pasteQDMBottomButton.getElement().setAttribute("tabIndex", "0");
		pasteBottomButtonPanel.add(pasteQDMBottomButton);
		pasteQDMBottomButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if(isEditable){
					observer.onBottomQDMPasteClicked();
				}
				
			}
		});
		return pasteBottomButtonPanel;
	}
	
	
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
		defaultExpIdentifierSel.getElement().setId("ExpansionIdentifierSelection_ChkBox");
		defaultExpIdentifierListBox.setWidth("200px");
		defaultExpIdentifierListBox.getElement().setId("DefaultExpansionIdentifier_ListBox");
		defaultExpIdentifierListBox.getElement().setTitle("Expansion Identifier Selection List");
		applyDefaultExpansionIdButton.setTitle("Apply Expansion Identifier to all the QDM Element(s).");
		applyDefaultExpansionIdButton.getElement().setId("applyToQDM_button");
		defaultExpIdentifierListBox.addItem("--Select--");
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.setWidth("450px");
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
		queryGrid.setWidget(0, 0, defaultExpIdentifierSel);
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
		mainPanel.setWidth("95%");
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
		
		//oidInput.getElement().setId("oidInput_TextBox");
		// oidInput.getElement().setAttribute("tabIndex", "0");
		// oidInput.setTitle("Enter OID");
		// oidInput.setWidth("200px");
		nameInput.getElement().setId("nameInput_TextBox");
		nameInput.getElement().setAttribute("tabIndex", "0");
		nameInput.setTitle("Enter Name");
		nameInput.setWidth("200px");
		
		qdmExpIdentifierListBox.getElement().setId("QDMExpansionIdentifier_ListBox");
		qdmExpIdentifierListBox.getElement().setTitle("Expansion Identifier Selection List");
		qdmExpIdentifierListBox.setEnabled(false);
		qdmExpIdentifierListBox.setWidth("200px");
		versionListBox.getElement().setId("Version_ListBox");
		versionListBox.getElement().setTitle("Version Selection List");
		versionListBox.setEnabled(false);
		versionListBox.setWidth("200px");
		dataTypeListBox.setWidth("200px");
		dataTypeListBox.getElement().setId("DataType_ListBox");
		dataTypeListBox.getElement().setTitle("DataType Selection List");
		
		specificOcurChkBox = new CustomCheckBox("Specific Occurrence", true);
		specificOcurChkBox.getElement().setId("SpecificOccurrence_ChkBox");
		
		saveCancelButtonBar.getSaveButton().setText("Apply");
		searchWidget.getSearchInput().setWidth("270px");
		searchWidget.getSearchInput().setHeight("20px");
		searchWidget.getSearchInput().setTitle("Enter OID");
		Grid queryGrid = new Grid(5, 4);
		queryGrid.setWidget(0, 0, LabelBuilder.buildLabel(new Label(), "Name"));
		queryGrid.setWidget(1, 0, nameInput);
		queryGrid.setWidget(0, 1, LabelBuilder.buildLabel("Datatype", "Datatype"));
		queryGrid.setWidget(1, 1, dataTypeListBox);
		queryGrid.setWidget(2, 0, LabelBuilder.buildLabel("Expansion Identifier", "Expansion Identifier"));
		queryGrid.setWidget(2, 1, LabelBuilder.buildLabel("Version", "Version"));
		queryGrid.setWidget(3, 0, qdmExpIdentifierListBox);
		queryGrid.setWidget(3, 1, versionListBox);
		queryGrid.setWidget(4, 0, saveCancelButtonBar);
		queryGrid.setWidget(4, 1, specificOcurChkBox);
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
	@Override
	public void buildAppliedQDMCellTable(QDSAppliedListModel appliedListModel, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if ((appliedListModel.getAppliedQDMs() != null)
				&& (appliedListModel.getAppliedQDMs().size() > 0)) {
			table = new CellTable<QualityDataSetDTO>();
			setEditable(isEditable);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<QualityDataSetDTO>();
			qdmSelectedList = new ArrayList<QualityDataSetDTO>();
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(appliedListModel.getAppliedQDMs());
			ListHandler<QualityDataSetDTO> sortHandler = new ListHandler<QualityDataSetDTO>(
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
										+ "Version in Fifth Column. The Applied QDM elements are listed alphabetically in a table.");
			}
			table.getElement().setAttribute("id", "AppliedQDMTable");
			table.getElement().setAttribute("aria-describedby",
					"appliedQDMTableSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			
		} else {
			Label searchHeader = new Label("Applied QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Applied QDM Elements.</p>");
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
	private CellTable<QualityDataSetDTO> addColumnToTable(
			final CellTable<QualityDataSetDTO> table,
			ListHandler<QualityDataSetDTO> sortHandler, boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			Label searchHeader = new Label("Applied QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("measureGroupingTableHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			selectionModel = new MultiSelectionModel<QualityDataSetDTO>();
			table.setSelectionModel(selectionModel);
			
			// Name Column
			Column<QualityDataSetDTO, SafeHtml> nameColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder();
					if ((object.getOccurrenceText() != null)
							&& !object.getOccurrenceText().equals("")) {
						value = value.append(object.getOccurrenceText() + " of "
								+ object.getCodeListName());
						title = title.append("Name : ").append(value);
					} else {
						value = value.append(object.getCodeListName());
						title = title.append("Name : ").append(value);
					}
					//if object.getType().equalsIgnoreCase(anotherString)
					// if the QDM element is not user defined, add (G) for Grouping or (E) for extension
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						if (object.getTaxonomy().equalsIgnoreCase("Grouping"))
							value.append(" (G)");
						else {
							value.append(" (E)");
						}
					}
					title.append("");
					return CellTableUtility.getColumnToolTip(value.toString(),
							title.toString());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Name\">" + "Name"
							+ "</span>"));
			
			// OID Column
			Column<QualityDataSetDTO, SafeHtml> oidColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
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
							object.getHasModifiedAtVSAC(),
							object.isNotFoundInVSAC());
				}
			};
			table.addColumn(oidColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"OID\">" + "OID"
							+ "</span>"));
			
			// DataType Column
			
			Column<QualityDataSetDTO, SafeHtml> dataTypeColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					title = title.append("Datatype : ").append(
							object.getDataType());
					return getDataTypeColumnToolTip(object.getDataType(),
							title, object.getHasModifiedAtVSAC(),
							object.isDataTypeHasRemoved());
				}
			};
			table.addColumn(dataTypeColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Datatype\">" + "Datatype"
							+ "</span>"));
			
			// Expansion Identifier Column
			Column<QualityDataSetDTO, SafeHtml> expansionColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
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
			Column<QualityDataSetDTO, SafeHtml> versionColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
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
			
			
			// Modify by Delete Column
			table.addColumn(new Column<QualityDataSetDTO, QualityDataSetDTO>(
					getCompositeCellForQDMModifyAndDelete(isEditable)) {
				
				@Override
				public QualityDataSetDTO getValue(QualityDataSetDTO object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='Modify'>"
					+ "Modify" + "</span>"));
			
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 14.0, Unit.PCT);
			table.setColumnWidth(4, 14.0, Unit.PCT);
			table.setColumnWidth(5, 2.0, Unit.PCT);
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
	public SingleSelectionModel<QualityDataSetDTO> addSelectionHandlerOnTable(
			final QDSAppliedListModel appliedListModel) {
		final SingleSelectionModel<QualityDataSetDTO> selectionModel = new SingleSelectionModel<QualityDataSetDTO>();
		selectionModel
		.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				QualityDataSetDTO qualityDataSetDTO = selectionModel
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
	@Override
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
	@Override
	public HasValueChangeHandlers<Boolean> getDefaultExpIDInput() {
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
	@Override
	public CustomCheckBox getSpecificOccChkBox(){
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public void resetVSACValueSetWidget() {
		defaultExpIdentifierSel.setValue(false);
		defaultExpIdentifierListBox.clear();
		defaultExpIdentifierListBox.setEnabled(false);
		defaultExpIdentifierListBox.addItem("--Select--");
		
		if(checkForEnable()){
			qdmExpIdentifierListBox.setEnabled(false);
			versionListBox.setEnabled(false);
			specificOcurChkBox.setEnabled(false);
			dataTypeListBox.setEnabled(false);
			searchWidget.getSearchInput().setTitle("Enter OID");
			nameInput.setTitle("Enter Name");
			//saveButton.setEnabled(false);
			pasteQDMTopButton.setEnabled(true);
			pasteQDMBottomButton.setEnabled(true);
		} else {
			pasteQDMTopButton.setEnabled(false);
			pasteQDMBottomButton.setEnabled(false);
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
	@Override
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
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/**
	 * Gets the composite cell for bulk export.
	 *
	 * @param isEditable the is editable
	 * @return the composite cell for bulk export
	 */
	private CompositeCell<QualityDataSetDTO> getCompositeCellForQDMModifyAndDelete(boolean isEditable) {
		final List<HasCell<QualityDataSetDTO, ?>> cells = new LinkedList<HasCell<QualityDataSetDTO, ?>>();
		if(isEditable){
			cells.add(getModifyQDMButtonCell());
			cells.add(getDeleteQDMButtonCell());
		}
		cells.add(getQDMCheckBoxCell());
		CompositeCell<QualityDataSetDTO> cell = new CompositeCell<QualityDataSetDTO>(
				cells) {
			@Override
			public void render(Context context, QualityDataSetDTO object,
					SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table tabindex=\"-1\"><tbody><tr tabindex=\"-1\">");
				for (HasCell<QualityDataSetDTO, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			
			@Override
			protected <X> void render(Context context,
					QualityDataSetDTO object, SafeHtmlBuilder sb,
					HasCell<QualityDataSetDTO, X> hasCell) {
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
	private HasCell<QualityDataSetDTO, SafeHtml> getModifyQDMButtonCell() {
		
		HasCell<QualityDataSetDTO, SafeHtml> hasCell = new HasCell<QualityDataSetDTO, SafeHtml>() {
			
			ClickableSafeHtmlCell modifyButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return modifyButonCell;
			}
			
			@Override
			public FieldUpdater<QualityDataSetDTO, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<QualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, QualityDataSetDTO object,
							SafeHtml value) {
						if ((object != null)) {
							observer.onModifyClicked(object);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Modify QDM";
				String cssClass = "customEditButton";
				if(isEditable){
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title
							+ "' class=\" " + cssClass + "\"/>");
				} else {
					sb.appendHtmlConstant("<button tabindex=\"0\" type=\"button\" title='" + title
							+ "' class=\" " + cssClass + "\" disabled/>");
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
	private HasCell<QualityDataSetDTO, SafeHtml> getDeleteQDMButtonCell() {
		
		HasCell<QualityDataSetDTO, SafeHtml> hasCell = new HasCell<QualityDataSetDTO, SafeHtml>() {
			
			ClickableSafeHtmlCell deleteButonCell = new ClickableSafeHtmlCell();
			
			@Override
			public Cell<SafeHtml> getCell() {
				return deleteButonCell;
			}
			
			@Override
			public FieldUpdater<QualityDataSetDTO, SafeHtml> getFieldUpdater() {
				
				return new FieldUpdater<QualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, QualityDataSetDTO object,
							SafeHtml value) {
						if ((object != null) && !object.isUsed()) {
							lastSelectedObject = object;
							observer.onDeleteClicked(object, index);
						}
					}
				};
			}
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Delete QDM";
				String cssClass;
				
				if (object.isUsed()) {
					cssClass = "customDeleteDisableButton";
					sb.appendHtmlConstant("<button type=\"button\" title='"
							+ title + "' tabindex=\"0\" class=\" " + cssClass
							+ "\"disabled/>");
				} else {
					cssClass = "customDeleteButton";
					sb.appendHtmlConstant("<button tabindex=\"0\"type=\"button\" title='"
							+ title + "' class=\" " + cssClass
							+ "\"/>");
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
	private HasCell<QualityDataSetDTO, Boolean> getQDMCheckBoxCell(){
		HasCell<QualityDataSetDTO, Boolean> hasCell = new HasCell<QualityDataSetDTO, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
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
			public FieldUpdater<QualityDataSetDTO, Boolean> getFieldUpdater() {
				return new FieldUpdater<QualityDataSetDTO, Boolean>() {
					@Override
					public void update(int index, QualityDataSetDTO object,
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
	 * Gets the data type column tool tip.
	 * 
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @param hasImage
	 *            the has image
	 * @param dataTypeHasRemoved
	 *            the data type has removed
	 * @return the data type column tool tip
	 */
	private SafeHtml getDataTypeColumnToolTip(String columnText,
			StringBuilder title, boolean hasImage, boolean dataTypeHasRemoved) {
		if (hasImage && !dataTypeHasRemoved) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"DataType is Valid.\""
					+ "title = \"DataType is Valid.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText + "</span></body>" + "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant)
					.toSafeHtml();
		} else if (hasImage && dataTypeHasRemoved) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : DataType is not Valid.\""
					+ " title=\"DataType is not Valid.\"/>"
					+ "<span tabIndex = \"0\" title='" + title
					+ "' class='clauseWorkSpaceInvalidNode'>" + columnText
					+ "</span></body>" + "</html>";
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public QualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getDataTypesListBox()
	 */
	/**
	 * Gets the data types list box.
	 *
	 * @return the data types list box
	 */
	@Override
	public ListBoxMVP getDataTypesListBox() {
		return dataTypeListBox;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getVersionListBox()
	 */
	/**
	 * Gets the version list box.
	 *
	 * @return the version list box
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
	public MatTextBox getUserDefinedInput() {
		return nameInput;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setDataTypesListBoxOptions(java.util.List)
	 */
	/**
	 * Sets the data types list box options.
	 *
	 * @param texts the new data types list box options
	 */
	@Override
	public void setDataTypesListBoxOptions(List<? extends HasListBox> texts) {
		setListBoxItems(dataTypeListBox, texts, MatContext.PLEASE_SELECT);
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
	
	/**
	 * Sets the list box items.
	 *
	 * @param listBox the list box
	 * @param itemList the item list
	 * @param defaultOption the default option
	 */
	private void setListBoxItems(ListBox listBox, List<? extends HasListBox> itemList, String defaultOption) {
		listBox.clear();
		listBox.addItem(defaultOption, "");
		if (itemList != null) {
			for (HasListBox listBoxContent : itemList) {
				//MAT-4366
				if(! listBoxContent.getItem().equalsIgnoreCase("Patient Characteristic Birthdate") && ! listBoxContent.getItem().equalsIgnoreCase("Patient Characteristic Expired")) {
					listBox.addItem(listBoxContent.getItem(),""+listBoxContent.getValue());
				}
			}
			
			SelectElement selectElement = SelectElement.as(listBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement
					.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(optionElement.getText());
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getInProgressMessageDisplay()
	 */
	/**
	 * Gets the in progress message display.
	 *
	 * @return the in progress message display
	 */
	@Override
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
	@Override
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
	@Override
	public SuccessMessageDisplay getSuccessMessageDisplay() {
		return successMessagePanel;
	}
	
	/**
	 * Gets the list data provider.
	 *
	 * @return the list data provider
	 */
	@Override
	public ListDataProvider<QualityDataSetDTO> getListDataProvider(){
		return listDataProvider;
	}
	
	/**
	 * Gets the simple pager.
	 *
	 * @return the simple pager
	 */
	@Override
	public MatSimplePager getSimplePager(){
		return spager;
	}
	
	/**
	 * Gets the celltable.
	 *
	 * @return the celltable
	 */
	@Override
	public CellTable<QualityDataSetDTO> getCelltable(){
		return table;
	}
	
	/**
	 * Gets the pager.
	 *
	 * @return the pager
	 */
	@Override
	public MatSimplePager getPager(){
		return spager;
	}
	
	/**
	 * Gets the update vsac error message panel.
	 *
	 * @return the update vsac error message panel
	 */
	@Override
	public ErrorMessageDisplay getUpdateVSACErrorMessagePanel() {
		return updateVSACErrorMessagePanel;
	}
	
	/**
	 * Gets the update vsac success message panel.
	 *
	 * @return the update vsac success message panel
	 */
	@Override
	public SuccessMessageDisplay getUpdateVSACSuccessMessagePanel() {
		return updateVSACSuccessMessagePanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getMainPanel()
	 */
	@Override
	public VerticalPanel getMainPanel(){
		return mainPanel;
	}
	
	/**
	 * Gets the image.
	 *
	 * @param action the action
	 * @param url the url
	 * @param key the key
	 * @return the image
	 */
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", "MeasureSearchButton");
		return image;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMCopyTopButton()
	 */
	@Override
	public CustomButton getQDMCopyTopButton(){
		return copyQDMTopButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMPasteTopButton()
	 */
	@Override
	public CustomButton getQDMPasteTopButton(){
		return pasteQDMTopButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMClearTopButton()
	 */
	@Override
	public CustomButton getQDMClearTopButton(){
		return clearQDMTopButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMCopyBottomButton()
	 */
	@Override
	public CustomButton getQDMCopyBottomButton(){
		return copyQDMBottomButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMPasteBottomButton()
	 */
	@Override
	public CustomButton getQDMPasteBottomButton(){
		return pasteQDMBottomButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQDMClearBottomButton()
	 */
	@Override
	public CustomButton getQDMClearBottomButton(){
		return clearQDMBottomButton;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#clearQDMCheckBoxes()
	 */
	@Override
	public void clearQDMCheckBoxes(){
		if(table!=null){
			List<QualityDataSetDTO> displayedItems = new ArrayList<QualityDataSetDTO>();
			displayedItems.addAll(qdmSelectedList);
			qdmSelectedList = new  ArrayList<QualityDataSetDTO>();
			for (QualityDataSetDTO dto : displayedItems) {
				selectionModel.setSelected(dto, false);
			}
			table.redraw();
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDMAppliedSelectionPresenter.SearchDisplay#getQdmSelectedList()
	 */
	@Override
	public List<QualityDataSetDTO> getQdmSelectedList() {
		return qdmSelectedList;
	}
	
	
	/**
	 * Sets the qdm selected list.
	 *
	 * @param qdmSelectedList the new qdm selected list
	 */
	public void setQdmSelectedList(List<QualityDataSetDTO> qdmSelectedList) {
		this.qdmSelectedList = qdmSelectedList;
	}
	
	
	//	@Override
	//	public Button getYesButton(){
	//		return yesBtn;
	//	}
	//
	//	@Override
	//	public Button getNoButton(){
	//		return noBtn;
	//	}
	
	//	@Override
	//	public void showPasteDialogBox(){
	//		dialogBox.setGlassEnabled(true);
	//		dialogBox.setAnimationEnabled(true);
	//		dialogBox.setText("Warning");
	//		//dialogBox.setVisible(true);
	//		// Create a table to layout the content
	//		VerticalPanel dialogContents = new VerticalPanel();
	//		dialogContents.getElement().setId("dialogContents_VerticalPanel");
	//		dialogContents.setWidth("28em");
	//		dialogContents.setSpacing(5);
	//		dialogBox.setWidget(dialogContents);
	//		yesBtn.setStyleName("padLeft5px");
	//		noBtn.setStyleName("padLeft5px");
	//		HorizontalPanel buttonPanel = new HorizontalPanel();
	//		buttonPanel.add(yesBtn);
	//		buttonPanel.add(noBtn);
	//		WarningMessageDisplay warningMsgDispaly = new WarningMessageDisplay();
	//		warningMsgDispaly.setMessage(MatContext.get().getMessageDelegate()
	//				.getWARNING_PASTING_IN_APPLIED_QDM_ELEMENTS());
	//
	//		dialogContents.add(warningMsgDispaly);
	//		dialogContents.setCellHorizontalAlignment(warningMsgDispaly,
	//				HasHorizontalAlignment.ALIGN_LEFT);
	//		dialogContents.add(buttonPanel);
	//		dialogContents.setCellHorizontalAlignment(buttonPanel,
	//				HasHorizontalAlignment.ALIGN_LEFT);
	//		dialogBox.center();
	//		dialogBox.show();
	//	}
	
	
	//	@Override
	//	public DialogBoxWithCloseButton getDialogBox(){
	//		return dialogBox;
	//	}
	
	
}
