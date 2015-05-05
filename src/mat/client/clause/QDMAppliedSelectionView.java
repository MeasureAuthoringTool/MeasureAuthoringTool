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

/**
 * The Class QDMAppliedSelectionView.
 */
public class QDMAppliedSelectionView implements
QDMAppliedSelectionPresenter.SearchDisplay,
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
	}
	
	/** The observer. */
	private Observer observer;
	
	/** The profile sel. */
	private CustomCheckBox profileSel = new CustomCheckBox("Select a Profile",
			"Use a default Expansion Profile ?", 1);
	
	/** The vsac profile list box. */
	private ListBoxMVP vsacProfileListBox = new ListBoxMVP();
	
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
	private Button applyButton = new PrimaryButton("Apply", "primaryButton");
	
	/** The version list. */
	private List<String> versionList = new ArrayList<String>();
	
	/** The profile list. */
	private List<String> profileList = new ArrayList<String>();
	
	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel = new SuccessMessageDisplay();
	
	/** The update vsac success message panel. */
	private SuccessMessageDisplay updateVSACSuccessMessagePanel = new SuccessMessageDisplay();
	
	/** The last selected object. */
	private QualityDataSetDTO lastSelectedObject;
	
	/** The expansion pro list box. */
	ListBoxMVP expansionProListBox = new ListBoxMVP();
	
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
	private Label vsacProfileHeader = new Label("Apply VSAC Profile");
	
	/** The spager. */
	private MatSimplePager spager;
	
	private SaveCancelButtonBar saveCancelButtonBar = new SaveCancelButtonBar();
	
	private SearchWidget searchWidget = new SearchWidget("Retrieve OID",
			"Enter OID", "textSearchWidget");
	
	VerticalPanel mainPanel;
	
	private List<QualityDataSetDTO> qdmSelectedList;
	
	private MultiSelectionModel<QualityDataSetDTO> selectionModel;
	
	private CustomButton copyQDMButton = (CustomButton) getImage("Copy",
			ImageResources.INSTANCE.getCopy(), "Copy");
	
	private CustomButton pasteQDMButton = (CustomButton) getImage("Paste",
			ImageResources.INSTANCE.getPaste(), "Paste");
	
	private CustomButton clearQDMButton = (CustomButton) getImage("Clear",
			ImageResources.INSTANCE.getErase(), "Clear");
	
	
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
	public QDMAppliedSelectionView() {
		VerticalPanel verticalPanel = new VerticalPanel();
		SimplePanel updateButtonPanel = new SimplePanel();
		updateButtonPanel.add(updateVSACButton);
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setWidth("5px");
		/* Horizontal Button Panel
		 * for copy, paste and clear
		 * */
		/*HorizontalPanel buttonLayOut = new HorizontalPanel();
		
		buttonLayOut.add(copyQDMButton);
		buttonLayOut.add(pasteQDMButton);
		buttonLayOut.add(clearQDMButton);
		buttonLayOut.setStyleName("continueButton");*/
		
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(buildElementWithVSACValueSetWidget());
		hp.add(simplePanel);
		hp.add(buildElementWithVSACExpansionProfile());
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(successMessagePanel);
		verticalPanel.add(errorMessagePanel);
		errorMessagePanel.getElement().setId(
				"errorMessagePanel_ErrorMessageDisplay");
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(hp);
		verticalPanel.add(new SpacerWidget());
		updateVSACButton.setTitle("Retrieve the most recent versions of applied value sets from VSAC");
		updateVSACButton.getElement().setId("updateVsacButton_Button");
		// verticalPanel.add(buttonLayOut);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(cellTablePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(updateVSACButton);
		verticalPanel.add(inProgressMessageDisplay);
		verticalPanel.add(updateVSACSuccessMessagePanel);
		verticalPanel.add(updateVSACErrorMessagePanel);
		
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id",
				"subQDMAPPliedListContainerPanel");
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setVSACProfileView(this);
	}
	
	
	
	/**
	 * Builds the element with vsac expansion profile.
	 *
	 * @return the widget
	 */
	private Widget buildElementWithVSACExpansionProfile() {
		VerticalPanel mainPanel = new VerticalPanel();
		mainPanel.getElement().setId("mainPanel_VerticalPanel");
		mainPanel.setWidth("95%");
		mainPanel.add(buildVSAVExpProfilePanel());
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());
		return mainPanel;
	}
	
	
	
	
	/**
	 * Builds the vsav exp profile panel.
	 *
	 * @return the widget
	 */
	private Widget buildVSAVExpProfilePanel() {
		profileSel.getElement().setId("ProfileSelection_ChkBox");
		vsacProfileListBox.setWidth("200px");
		vsacProfileListBox.getElement().setId("VSACProfile_ListBox");
		vsacProfileListBox.getElement().setTitle("VSAC Profile Selection List");
		applyButton.setTitle("Apply the profile to all the QDM Element's");
		applyButton.getElement().setId("applyToQDM_button");
		vsacProfileListBox.addItem("--Select--");
		VerticalPanel searchPanel = new VerticalPanel();
		searchPanel.setWidth("450px");
		searchPanel.setHeight("227px");
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("valueSetSearchPanel");
		vsacProfileHeader.getElement().setId("searchHeader_Label");
		vsacProfileHeader.setStyleName("valueSetHeader");
		vsacProfileHeader.getElement().setAttribute("tabIndex", "0");
		vsacProfileHeader.getElement().setTitle("Search by OID and Name");
		searchPanel.add(vsacProfileHeader);
		searchPanel.add(new SpacerWidget());
		Grid queryGrid = new Grid(5, 1);
		queryGrid.setWidget(0, 0, profileSel);
		queryGrid.setWidget(1, 0, new SpacerWidget());
		queryGrid.setWidget(2, 0, vsacProfileListBox);
		queryGrid.setWidget(3, 0, new SpacerWidget());
		queryGrid.setWidget(4, 0, applyButton);
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
		
		expansionProListBox.getElement().setId("ExpansionProfile_ListBox");
		expansionProListBox.getElement().setTitle("Expansion Profile Selection List");
		expansionProListBox.setEnabled(false);
		expansionProListBox.setWidth("200px");
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
		queryGrid.setWidget(2, 0, LabelBuilder.buildLabel("Expansion Profile", "Expansion Profile"));
		queryGrid.setWidget(2, 1, LabelBuilder.buildLabel("Version", "Version"));
		queryGrid.setWidget(3, 0, expansionProListBox);
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
										+ "OID in Second Column, DataType in Third Column, Expansion Profile in Fourth Column,"
										+ "Version in Fifth Column and Modify in Sixth Column where the user can Edit and Delete "
										+ "the existing QDM. The Applied QDM elements are listed alphabetically in a table.");
			} else {
				invisibleLabel = (Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied QDM Elements table Name in First Column"
										+ "OID in Second Column, DataType in Third Column, Expansion Profile in Fourth Column,"
										+ "Version in Fifth Column. The Applied QDM elements are listed alphabetically in a table.");
			}
			table.getElement().setAttribute("id", "AppliedQDMTable");
			table.getElement().setAttribute("aria-describedby",
					"appliedQDMTableSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			HorizontalPanel hPanel = new HorizontalPanel();
			HorizontalPanel buttonLayOut = new HorizontalPanel();
			buttonLayOut.add(copyQDMButton);
			buttonLayOut.add(pasteQDMButton);
			buttonLayOut.add(clearQDMButton);
			buttonLayOut.setStyleName("continueButton");
			hPanel.add(spager);
			hPanel.add(buttonLayOut);
			cellTablePanel.add(hPanel);
			//cellTablePanel.add(spager);
			
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
					String value = null;
					if ((object.getOccurrenceText() != null)
							&& !object.getOccurrenceText().equals("")) {
						value = object.getOccurrenceText() + " of "
								+ object.getCodeListName();
						title = title.append("Name : ").append(value);
					} else {
						value = object.getCodeListName();
						title = title.append("Name : ").append(value);
					}
					return CellTableUtility.getColumnToolTip(value,
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
			
			// Expansion Profile Column
			Column<QualityDataSetDTO, SafeHtml> expansionColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					if (object.getExpansionProfile() != null) {
						StringBuilder title = new StringBuilder();
						title = title.append("Expansion Profile : ").append(
								object.getExpansionProfile());
						return CellTableUtility.getColumnToolTip(
								object.getExpansionProfile(), title.toString());
					}
					
					return null;
				}
			};
			table.addColumn(expansionColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Expansion Profile\">"
							+ "Expansion Profile" + "</span>"));
			
			// Version Column
			Column<QualityDataSetDTO, SafeHtml> versionColumn = new Column<QualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (!object.getOid().equalsIgnoreCase(
							ConstantMessages.USER_DEFINED_QDM_OID) ) {
						if(object.getExpansionProfile()==null){
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
			
			if(isEditable){
				// Modify by Delete Column
				table.addColumn(new Column<QualityDataSetDTO, QualityDataSetDTO>(
						getCompositeCellForQDMModifyAndDelete()) {
					
					@Override
					public QualityDataSetDTO getValue(QualityDataSetDTO object) {
						return object;
					}
				}, SafeHtmlUtils.fromSafeConstant("<span title='Modify'>"
						+ "Modify" + "</span>"));
				
			}
			
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
	public HasValueChangeHandlers<Boolean> getVSACProfileInput() {
		return profileSel;
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
	 * Gets the expansion profile value.
	 *
	 * @param inputListBox the input list box
	 * @return the expansion profile value
	 */
	@Override
	public String getExpansionProfileValue(ListBoxMVP inputListBox) {
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
	 * Gets the VSAC expansion profile list box.
	 *
	 * @return the VSAC expansion profile list box
	 */
	@Override
	public ListBoxMVP getVSACExpansionProfileListBox() {
		return vsacProfileListBox;
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
	 * Sets the vsac expansion profile list box.
	 */
	@Override
	public void setVSACExpansionProfileListBox() {
		vsacProfileListBox.clear();
		vsacProfileListBox.addItem("--Select--");
		for (int i = 0; (i < getProfileList().size())
				&& (getProfileList() != null); i++) {
			vsacProfileListBox.addItem(getProfileList().get(i));
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
	public void setVSACProfileListBox(List<? extends HasListBox> texts){
		setProfileListBoxItems(expansionProListBox, texts, MatContext.PLEASE_SELECT);
	}
	
	
	/**
	 * Sets the profile list box items.
	 *
	 * @param dataTypeListBox the data type list box
	 * @param itemList the item list
	 * @param defaultOption the default option
	 */
	private void setProfileListBoxItems(ListBoxMVP dataTypeListBox,
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
	public void setVSACVersionListBoxOptions(List<? extends HasListBox> texts){
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
		profileSel.setValue(false);
		vsacProfileListBox.clear();
		vsacProfileListBox.setEnabled(false);
		vsacProfileListBox.addItem("--Select--");
		
		if(checkForEnable()){
			expansionProListBox.setEnabled(false);
			versionListBox.setEnabled(false);
			specificOcurChkBox.setEnabled(false);
			dataTypeListBox.setEnabled(false);
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
	 * @return the composite cell for bulk export
	 */
	private CompositeCell<QualityDataSetDTO> getCompositeCellForQDMModifyAndDelete() {
		final List<HasCell<QualityDataSetDTO, ?>> cells = new LinkedList<HasCell<QualityDataSetDTO, ?>>();
		cells.add(getModifyQDMButtonCell());
		cells.add(getDeleteQDMButtonCell());
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
	public Button getApplyButton(){
		return applyButton;
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
	public List<String> getProfileList() {
		return profileList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#setProfileList(java.util.List)
	 */
	/**
	 * Sets the profile list.
	 *
	 * @param profileList the new profile list
	 */
	@Override
	public void setProfileList(List<String> profileList) {
		this.profileList = profileList;
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
	public ListBoxMVP getVSACProfileListBox() {
		return expansionProListBox;
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
	
	@Override
	public VerticalPanel getMainPanel(){
		return mainPanel;
	}
	
	private Widget getImage(String action, ImageResource url, String key) {
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonTextMeasureLibrary");
		image.setTitle(action);
		image.setResource(url, action);
		image.getElement().setAttribute("id", "MeasureSearchButton");
		return image;
	}
	
	@Override
	public CustomButton getQDMCopyButton(){
		return copyQDMButton;
	}
	
	@Override
	public CustomButton getQDMPasteButton(){
		return pasteQDMButton;
	}
	
	@Override
	public CustomButton getQDMClearButton(){
		return clearQDMButton;
	}
	
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
	
	@Override
	public List<QualityDataSetDTO> getQdmSelectedList() {
		return qdmSelectedList;
	}
	
	
	public void setQdmSelectedList(List<QualityDataSetDTO> qdmSelectedList) {
		this.qdmSelectedList = qdmSelectedList;
	}
	
	
}
