package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.CheckBox;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
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
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
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
import mat.client.codelist.HasListBox;
import mat.client.shared.CQLCopyPasteClearButtonToolBar;
import mat.client.shared.CustomQuantityTextBox;
import mat.client.shared.LabelBuilder;
import mat.client.shared.ListBoxMVP;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.umls.service.VsacApiResult;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLQualityDataSetDTO;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.ConstantMessages;



// TODO: Auto-generated Javadoc
/**
 * The Class QDMAppliedSelectionView.
 */
public class CQLAppliedValueSetView implements HasSelectionHandlers<Boolean>{
	
	/** The Constant GROUPING_QDM. */
	static final String GROUPING_QDM = " (G)";
	
	/** The Constant EXTENSIONAL_QDM. */
	static final String EXTENSIONAL_QDM = " (E)";
	/** The Loading flag for page. */
	private Boolean isLoading = false;
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
		
	}
	
	/** The observer. */
	private Observer observer;
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
		
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The cell table panel. */
	private Panel cellTablePanel = new Panel();
	
	/** The cell table panel body. */
	private PanelBody cellTablePanelBody = new PanelBody();
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 10;
	
	/** The table. */
	private CellTable<CQLQualityDataSetDTO> table;
	
	/** The qdm selected list. */
	private List<CQLQualityDataSetDTO> qdmSelectedList;
	

	/** The selection model. */
	private MultiSelectionModel<CQLQualityDataSetDTO> selectionModel;
	
	
	/** The sort provider. */
	private ListDataProvider<CQLQualityDataSetDTO> listDataProvider;
	
	/** The update button. */
	private Button updateVSACButton = new Button("Update From VSAC ");
	
	
	/** The version list. */
	private List<String> versionList = new ArrayList<String>();
	
	
	
	/** The last selected object. */
	private CQLQualityDataSetDTO lastSelectedObject;
	
	/** The version list box. */
	private ListBox versionListBox = new ListBox();
	
	
	/** The name input. */
	private MatTextBox nameInput = new MatTextBox();
	
	/** The Suffix input. */
	private CustomQuantityTextBox suffixInput = new CustomQuantityTextBox(4);
	
	/** The is editable. */
	private boolean isEditable;
	
	/** The specific occurrence check box. */
	private CheckBox specificOcurChkBox;
	
	
	/** The vsac profile header. */
	//private Label defaultExpProfileHeader = new Label(LabelType.INFO,"Apply Expansion Profile");
	
	/** The spager. */
	private MatSimplePager spager;
	
	/** The save cancel button bar. */
	private Button saveValueSet = new Button("Apply");
	
	/** The cancel button. */
	private Button cancelButton = new Button("Cancel");
	
	/** The s widget. */
	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Retrieve OID","Enter OID");
	
	/** The main panel. */
	private VerticalPanel mainPanel;
	
	/** The search header. */
	private PanelHeader searchHeader = new PanelHeader();
	
	/** The cell table main panel. */
	SimplePanel cellTableMainPanel = new SimplePanel();
	
	HTML heading = new HTML();
	
	CQLCopyPasteClearButtonToolBar copyPasteClearButtonToolBar = new CQLCopyPasteClearButtonToolBar("valueset");
	
	/**
	 * Instantiates a new VSAC profile selection view.
	 */
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
	
	/**
	 * Builds the cell table widget.
	 *
	 * @return the simple panel
	 */
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
		Panel searchPanel = new Panel();
		PanelBody searchPanelBody = new PanelBody();
		
		
		searchPanel.getElement().setId("searchPanel_VerticalPanel");
		searchPanel.setStyleName("cqlvalueSetSearchPanel");
		
		searchHeader.setStyleName("CqlWorkSpaceTableHeader");
		
		
		searchPanel.add(searchHeader);
		searchPanel.setWidth("650px");
		searchPanel.setHeight("355px");
		searchPanelBody.add(new SpacerWidget());
		
		nameInput.getElement().setId("nameInput_TextBox");
		nameInput.getElement().setAttribute("tabIndex", "0");
		
		nameInput.setTitle("Enter Name");
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
		
		saveValueSet.setText("Apply");
		saveValueSet.setTitle("Apply");
		saveValueSet.setType(ButtonType.PRIMARY);
		
		cancelButton.setType(ButtonType.DANGER);
		cancelButton.setTitle("Cancel");
		
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		buttonToolBar.add(saveValueSet);
		buttonToolBar.add(cancelButton);
		
		VerticalPanel buttonPanel = new VerticalPanel();
		buttonPanel.add(new SpacerWidget());
		buttonPanel.add(buttonToolBar);
		buttonPanel.add(new SpacerWidget());
		
		
		
		VerticalPanel searchWidgetFormGroup = new VerticalPanel();
		sWidget.setSearchBoxWidth("500px");
		searchWidgetFormGroup.add(sWidget.getSearchWidget());
		searchWidgetFormGroup.add(new SpacerWidget());

		VerticalPanel namePanel = new VerticalPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText("Name");
		nameLabel.setTitle("Name");
		namePanel.add(nameLabel);
		namePanel.add(nameInput);
		namePanel.add(new SpacerWidget());

		VerticalPanel suffixPanel = new VerticalPanel();
		FormLabel suffixLabel = new FormLabel();
		suffixLabel.setText("Suffix (Max Length 4)");
		suffixLabel.setTitle("Suffix");
		suffixPanel.add(suffixLabel);
		suffixPanel.add(suffixInput);
		suffixPanel.add(new SpacerWidget());

		VerticalPanel versionFormGroup = new VerticalPanel();
		FormLabel verLabel = new FormLabel();
		verLabel.setText("Version");
		verLabel.setTitle("Version");
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
		Grid versionGrid = new Grid(2, 1);
		versionGrid.setWidget(0, 0, versionFormGroup);
		versionGrid.setWidget(1, 0, buttonFormGroup);

		searchPanelBody.add(oidGrid);
		searchPanelBody.add(nameGrid);
		searchPanelBody.add(versionGrid);

		searchPanel.add(searchPanelBody);
		return searchPanel;
	}
	
	/**
	 * Builds the cell table.
	 *
	 * @param appliedValueSetList the applied value set list
	 * @param isEditable the is editable
	 */
	public void buildAppliedValueSetCellTable(List<CQLQualityDataSetDTO> appliedValueSetList, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		PanelHeader qdmElementsHeader = new PanelHeader();//new Label("QDM Elements");
		qdmElementsHeader.getElement().setId("searchHeader_Label");
		qdmElementsHeader.setStyleName("CqlWorkSpaceTableHeader");
		qdmElementsHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Applied Value Sets</strong>");
		qdmElementsHeader.add(searchHeaderText);
		cellTablePanel.add(qdmElementsHeader);
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
					pagerResources, false, 0, true,"valuesetAndCodes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel;
			if(isEditable){
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied Value Sets table Name in First Column"
										+ "OID in Second Column, TableCaptionElement in Third Column, Version in Fourth Column,"
										+ "And Modify in Fifth Column where the user can Edit and Delete "
										+ "the existing Value set. The Applied Value Sets are listed alphabetically in a table.");
				
				
			} else {
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"appliedQDMTableSummary",
								"In the Following Applied Value Sets table Name in First Column"
										+ "OID in Second Column, Expansion Profile in Third Column, Version in Fourth Column,"
										+ "and Select in Fifth Column. The Applied Value Sets are listed alphabetically in a table.");
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
			ListHandler<CQLQualityDataSetDTO> sortHandler, final boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			Label searchHeader = new Label("Value Sets");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			searchHeader.setVisible(false);
			caption.appendChild(searchHeader.getElement());
			/*selectionModel = new MultiSelectionModel<CQLQualityDataSetDTO>();
			table.setSelectionModel(selectionModel);*/
			
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
					
					return CellTableUtility.getNameColumnToolTip(value.toString(), title.toString());
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
			// Version Column
			Column<CQLQualityDataSetDTO, SafeHtml> versionColumn = new Column<CQLQualityDataSetDTO, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLQualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (!object.getOid().equalsIgnoreCase(
							ConstantMessages.USER_DEFINED_QDM_OID) ) {
						//if(object.getExpansionIdentifier()==null || object.getExpansionIdentifier().isEmpty()){
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
						/*} else {
							version = "";
						}*/
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
				
				//Edit
				Column<CQLQualityDataSetDTO, SafeHtml> editColumn =
						new Column<CQLQualityDataSetDTO, SafeHtml>(
								new ClickableSafeHtmlCell()) {
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
				editColumn.setFieldUpdater(new FieldUpdater<CQLQualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object, SafeHtml value) {
						if ((object != null)) {
							observer.onModifyClicked(object);
						}
					}
				});
				table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Modify'>" + "Modify" + "</span>"));
				
				//Delete
				Column<CQLQualityDataSetDTO, SafeHtml> deleteColumn =
						new Column<CQLQualityDataSetDTO, SafeHtml>(
								new ClickableSafeHtmlCell()) {
					@Override
					public SafeHtml getValue(CQLQualityDataSetDTO object) {
						SafeHtmlBuilder sb = new SafeHtmlBuilder();
						String title = "Click to delete value set";
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
				deleteColumn.setFieldUpdater(new FieldUpdater<CQLQualityDataSetDTO, SafeHtml>() {
					@Override
					public void update(int index, CQLQualityDataSetDTO object, SafeHtml value) {
						if ((object != null) && !object.isUsed()) {
							lastSelectedObject = object;
							observer.onDeleteClicked(object, index);
						}
					}
				});
				table.addColumn(deleteColumn, SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "" + "</span>"));
			}
			
			
			/*Column<CQLQualityDataSetDTO, Boolean> select = new Column<CQLQualityDataSetDTO, Boolean>(new CheckboxCell()) {

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
				
			};*/
			
			
			table.setColumnWidth(0, 25.0, Unit.PCT);
			table.setColumnWidth(1, 25.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			table.setColumnWidth(3, 8.0, Unit.PCT);
			table.setColumnWidth(4, 8.0, Unit.PCT);
		}
		
		return table;
	}
	
	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return containerPanel;
	}
	
	/**
	 * Gets the specific occ chk box.
	 *
	 * @return the specific occ chk box
	 */
	public CheckBox getSpecificOccChkBox(){
		return specificOcurChkBox;
	}
	
	/**
	 * Gets the data type text.
	 *
	 * @param inputListBox the input list box
	 * @return the data type text
	 */
	public String getDataTypeText(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getItemText(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the data type value.
	 *
	 * @param inputListBox the input list box
	 * @return the data type value
	 */
	public String getDataTypeValue(ListBoxMVP inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the version value.
	 *
	 * @param inputListBox the input list box
	 * @return the version value
	 */
	public String getVersionValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
	}
	
	/**
	 * Gets the expansion Profile value.
	 *
	 * @param inputListBox the input list box
	 * @return the expansion Profile value
	 */
	public String getExpansionProfileValue(ListBox inputListBox) {
		if (inputListBox.getSelectedIndex() >= 0) {
			return inputListBox.getValue(inputListBox.getSelectedIndex());
		} else {
			return "";
		}
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
	/**
	 * Sets the VSAC version list box options.
	 *
	 * @param texts the new VSAC version list box options
	 */
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
	/**
	 * Reset vsac value set widget.
	 */
	public void resetVSACValueSetWidget() {
	
		if(checkForEnable()){
			versionListBox.setEnabled(false);
			sWidget.getSearchBox().setTitle("Enter OID");
			nameInput.setTitle("Enter Name");
			
		}
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		searchHeader.clear();
		searchHeader.add(searchHeaderText);
	}
	/**
	 * Fire event.
	 *
	 * @param event the event
	 */
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	/**
	 * Adds the selection handler.
	 *
	 * @param handler the handler
	 * @return the handler registration
	 */
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
		return cancelButton;
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
	/*public Button getApplyDefaultExpansionIdButton(){
		return applyDefaultExpansionIdButton;
	}*/
	
	/* (non-Javadoc)
	 * @see mat.client.clause.VSACProfileSelectionPresenter.SearchDisplay#getRetrieveFromVSACButton()
	 */
	/**
	 * Gets the retrieve from vsac button.
	 *
	 * @return the retrieve from vsac button
	 */
	//@Override
	public org.gwtbootstrap3.client.ui.Button getRetrieveFromVSACButton(){
		return sWidget.getGo();
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
		return saveValueSet;
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
	 * Gets the selected element to remove.
	 *
	 * @return the selected element to remove
	 */
	public CQLQualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	/**
	 * Gets the version list box.
	 *
	 * @return the version list box
	 */
	public ListBox getVersionListBox() {
		return versionListBox;
	}
	
	/**
	 * Gets the OID input.
	 *
	 * @return the OID input
	 */
	public TextBox getOIDInput() {
		return sWidget.getSearchBox();
	}
	
	/**
	 * Gets the user defined input.
	 *
	 * @return the user defined input
	 */
	public TextBox getUserDefinedInput() {
		return nameInput;
	}
	
	/**
	 * Gets the user defined input.
	 *
	 * @return the user defined input
	 */
	public CustomQuantityTextBox getSuffixInput() {
		return suffixInput;
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
	 * Gets the search header.
	 *
	 * @return the search header
	 */
	public PanelHeader getSearchHeader() {
		return searchHeader;
	}
	
	
	/**
	 * Gets the list data provider.
	 *
	 * @return the list data provider
	 */
	public ListDataProvider<CQLQualityDataSetDTO> getListDataProvider(){
		return listDataProvider;
	}
	
	/**
	 * Gets the simple pager.
	 *
	 * @return the simple pager
	 */
	public MatSimplePager getSimplePager(){
		return spager;
	}
	
	/**
	 * Gets the celltable.
	 *
	 * @return the celltable
	 */
	public CellTable<CQLQualityDataSetDTO> getCelltable(){
		return table;
	}
	
	/**
	 * Gets the pager.
	 *
	 * @return the pager
	 */
	public MatSimplePager getPager(){
		return spager;
	}
	
	/**
	 * Gets the main panel.
	 *
	 * @return the main panel
	 */
	public VerticalPanel getMainPanel(){
		return mainPanel;
	}
	
	
	
	/**
	 * Sets the widgets read only.
	 *
	 * @param editable the new widgets read only
	 */
	public void setWidgetsReadOnly(boolean editable){
		
		getOIDInput().setEnabled(editable);
		getUserDefinedInput().setEnabled(editable);
		getCancelQDMButton().setEnabled(editable);
		getRetrieveFromVSACButton().setEnabled(editable);
		getUpdateFromVSACButton().setEnabled(editable);
		getSaveButton().setEnabled(false);
		getVersionListBox().setEnabled(false);
		
	}
	
	/**
	 * Sets the widget to default.
	 */
	public void setWidgetToDefault() {
		getVersionListBox().clear();
		getOIDInput().setValue("");
		getUserDefinedInput().setValue("");
		getSaveButton().setEnabled(false);
	}
	
	/**
	 * This method enable/disable's reterive and updateFromVsac button
	 * and hide/show loading please wait message.
	 *
	 * @param busy the busy
	 */
	/*public void showSearchingBusyOnQDM(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		getUpdateFromVSACButton().setEnabled(!busy);
		getRetrieveFromVSACButton().setEnabled(!busy);
	}*/
	
	/**
	 * Gets the cell table main panel.
	 *
	 * @return the cell table main panel
	 */
	public SimplePanel getCellTableMainPanel(){
		return cellTableMainPanel;
	}
	
	/**
	 * Clear cell table main panel.
	 */
	public void clearCellTableMainPanel(){
		cellTableMainPanel.clear();
	}
	
	/**
	 * Validate user defined input. In this functionality we are disabling all
	 * the fields in Search Panel except Name
	 * which are required to create new UserDefined QDM Element.
	 */
	public boolean validateUserDefinedInput(boolean isUserDefined) {
		if (getUserDefinedInput().getValue().length() > 0) {
			isUserDefined = true;
			getOIDInput().setEnabled(true);
			getUserDefinedInput()
					.setTitle(getUserDefinedInput().getValue());
		
			getVersionListBox().setEnabled(false);

			getRetrieveFromVSACButton().setEnabled(false);
			getSaveButton().setEnabled(true);
		} else {
			isUserDefined = false;
			getUserDefinedInput().setTitle("Enter Name");
			getOIDInput().setEnabled(true);
			getRetrieveFromVSACButton().setEnabled(true);
			getSaveButton().setEnabled(false);
		}
		return isUserDefined;
	}
	
	
	/**
	 * Validate oid input. depending on the OID input we are disabling and
	 * enabling the fields in Search Panel
	 */
	public boolean validateOIDInput(boolean isUserDefined) {
		if (getOIDInput().getValue().length() > 0) {
			isUserDefined = false;
			getUserDefinedInput().setEnabled(false);
			getSaveButton().setEnabled(false);
			getRetrieveFromVSACButton().setEnabled(true);
		} else if (getUserDefinedInput().getValue().length() > 0) {
			isUserDefined = true;
			//getQDMExpProfileListBox().clear();
			getVersionListBox().clear();
			getUserDefinedInput().setEnabled(true);
			getSaveButton().setEnabled(true);

		} else {
			getUserDefinedInput().setEnabled(true);
		}
		return isUserDefined;
	}
	
	/**
	 * Convert message.
	 * 
	 * @param id
	 *            the id
	 * @return the string
	 */
	public String convertMessage(final int id) {
		String message;
		switch (id) {
		case VsacApiResult.UMLS_NOT_LOGGEDIN:
			message = MatContext.get().getMessageDelegate().getUMLS_NOT_LOGGEDIN();
			break;
		case VsacApiResult.OID_REQUIRED:
			message = MatContext.get().getMessageDelegate().getUMLS_OID_REQUIRED();
			break;
		default:
			message = MatContext.get().getMessageDelegate().getVSAC_RETRIEVE_FAILED();
		}
		return message;
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
	}*/
	
	
	/**
	 * Reset QDM search panel.
	 */
	public void resetCQLValuesetearchPanel() {
		HTML searchHeaderText = new HTML("<strong>Search</strong>");
		getSearchHeader().clear();
		getSearchHeader().add(searchHeaderText);
		
		getOIDInput().setEnabled(true);
		getOIDInput().setValue("");
		getOIDInput().setTitle("Enter OID");
		
		getUserDefinedInput().setEnabled(true);
		getUserDefinedInput().setValue("");
		getUserDefinedInput().setTitle("Enter Name");
		
		getSuffixInput().setEnabled(true);
		getSuffixInput().setValue("");
		getSuffixInput().setTitle("Suffix must be an integer between 1-4 characters");
		
		getVersionListBox().clear();
		
		getVersionListBox().setEnabled(false);
		
		getSaveButton().setEnabled(false);
		
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
	
}
