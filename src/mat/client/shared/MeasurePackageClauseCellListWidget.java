package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mat.client.CustomPager;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * The Class MeasurePackageClauseCellListWidget.
 */
public class MeasurePackageClauseCellListWidget {
	/**
	 * The HTML templates used to render the ClauseCell.
	 */
	interface Templates extends SafeHtmlTemplates {
		/**
		 * The template for this Cell, which includes styles and a value.
		 * @param title - Title for div.
		 * @param value the safe value. Since the value type is {@link SafeHtml},
		 *          it will not be escaped before including it in the template.
		 *          Alternatively, you could make the value type String, in which
		 *          case the value would be escaped.
		 * @return a {@link SafeHtml} instance
		 */
		@SafeHtmlTemplates.Template("<div title=\"{0}\" style=\"margin-left:5px;\">{1}</div>")
		SafeHtml cell(String title, SafeHtml value);
	}
	/** Create a singleton instance of the templates used to render the cell. */
	private static Templates templates = GWT.create(Templates.class);
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> leftCellList;
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> rightCellList;
	/** The Association cell list. */
	private CellList<MeasurePackageClauseDetail> associatedCellList;
	/** The Right cell LIst pager panel. */
	private ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel();
	/** The Left cell LIst pager panel. */
	private ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel();
	/** Flow Panel for OK-Cancel buttons in Item Count Table. **/
	private FlowPanel itemCountButtonPanel = new FlowPanel();
	/** The range label pager. */
	private RangeLabelPager rightRangeLabelPager = new RangeLabelPager();
	/** The range label pager. */
	private RangeLabelPager leftRangeLabelPager = new RangeLabelPager();
	/** The disclosure panel item count table. */
	private DisclosurePanel disclosurePanelItemCountTable = new DisclosurePanel("Add/Edit Item Count");
	/** The disclosure panel associations. */
	private DisclosurePanel disclosurePanelAssociations = new DisclosurePanel("Add Associations");
	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	/** The add Grouping to measure. */
	private PrimaryButton saveGrouping = new PrimaryButton("Save Grouping", "primaryButton");
	/** The save itemcount list. */
	private SecondaryButton saveItemcountList = new SecondaryButton("Ok");
	/** The pagesize. */
	private final int PAGESIZE = 3;
	/** The add Clause Right. */
	private Button addClauseRight = buildAddButton("customAddRightButton", "AddClauseToRight");
	/** The add Clause left. */
	private Button addClauseLeft = buildAddButton("customAddLeftButton", "AddClauseToLeft");
	/** The add all Clause right. */
	private Button addAllClauseRight = buildDoubleAddButton("customAddALlRightButton", "AddAllClauseToRight");
	/** The add all Clause left. */
	private Button addAllClauseLeft = buildDoubleAddButton("customAddAllLeftButton", "AddAllClauseToLeft");
	/** The package name. */
	private Label packageName = new Label();
	/** Item Count Table Selection Model. */
	private MultiSelectionModel<QualityDataSetDTO> itemCountSelection;
	/** Clauses Selection Model.	 */
	private SingleSelectionModel<MeasurePackageClauseDetail> leftCellListSelectionModel	=
			new SingleSelectionModel<MeasurePackageClauseDetail>();
	/** Grouping Selection Model.	 */
	private SingleSelectionModel<MeasurePackageClauseDetail> rightCellListSelectionModel =
			new SingleSelectionModel<MeasurePackageClauseDetail>();
	/** List Data Provider for Right(Package Clauses) cell List.*/
	private ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider;
	/** List Data Provider for Left(Clause) cell List. */
	private ListDataProvider<MeasurePackageClauseDetail> leftCellListDataProvider;
	/** The Constant ITEMCOUNTLIST. */
	private CellTable<QualityDataSetDTO> itemCountCellTable;
	/** Applied QDM List.**/
	private List<QualityDataSetDTO> appliedQdmList;
	/** The panel. */
	private VerticalPanel panel = new VerticalPanel();
	
	private VerticalPanel vPanel = new VerticalPanel();
	/** List of Elements in Grouping List. */
	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Clause List.*/
	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> associatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	private CellList<MeasurePackageClauseDetail> associatedPOPCellList;
	/** Error Message in Package Grouping Section. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	private SingleSelectionModel<MeasurePackageClauseDetail> associatedSelectionModel;
	
	private Map<String, MeasurePackageClauseDetail>  map = new HashMap<String, MeasurePackageClauseDetail>();
	
	private List<QualityDataSetDTO> itemCountSelectionList ;
	
	
	/*** Gets the Grouping cell list.
	 * @return the cellList.	 */
	public CellList<MeasurePackageClauseDetail> getRightCellList() {
		rightCellList = new CellList<MeasurePackageClauseDetail>(new RightClauseCell());
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		rightCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(groupingPopulationList);
		rightCellListDataProvider.addDataDisplay(rightCellList);
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			rightCellList.setSelectionModel(rightCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			rightCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		return rightCellList;
	}
	/**
	 * Gets the Clause Cell List.
	 * @return CellList.
	 */
	public CellList<MeasurePackageClauseDetail> getLeftCellList() {
		leftCellList = new CellList<MeasurePackageClauseDetail>(new LeftClauseCell());
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		leftCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(clausesPopulationList);
		leftCellListDataProvider.addDataDisplay(leftCellList);
		leftCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (leftCellListSelectionModel.getSelectedObject() == null) {
					return;
				}
				//if (rightCellListSelectionModel.getSelectedObject() != null) {
				rightCellListSelectionModel.clear();
				//}
				System.out.println("selectionModel.getSelectedSet()" + leftCellListSelectionModel.getSelectedSet());
				disclosurePanelItemCountTable.setVisible(false);
				disclosurePanelAssociations.setVisible(false);
			}
		});
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			leftCellList.setSelectionModel(leftCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			leftCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		return leftCellList;
	}
	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public final Widget getWidget() {
		mainFlowPanel.getElement().setAttribute("id", "MeasurePackageClauseWidget_FlowPanel");
		return mainFlowPanel;
	}
	/**
	 * Builds the item count widget.
	 *
	 * @return the widget
	 */
	private Widget buildItemCountWidget() {
		panel.clear();
		disclosurePanelItemCountTable.clear();
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "MeasurePackageClause_FlowPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "MeasurePackageClause_HorizontalPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setAttribute("id", "MeasurePackageClauseButtonLayout");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		disclosurePanelItemCountTable.add(panel);
		disclosurePanelItemCountTable.setOpen(false);
		disclosurePanelItemCountTable.setVisible(false);
		return disclosurePanelItemCountTable;
	}
	/**
	 * Builds the add association widget.
	 *
	 * @return the widget
	 */
	private Widget buildAddAssociationWidget() {
		disclosurePanelAssociations.clear();
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "MeasurePackageClauseAssoWidgt_FlowPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "MeasurePackageClauseAssoWidgt_HoriPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setAttribute("id", "MeasurePackageClauseAssoWidgtButtonLayout");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		disclosurePanelAssociations.add(vPanel);
		disclosurePanelAssociations.setOpen(false);
		disclosurePanelAssociations.setVisible(false);
		return disclosurePanelAssociations;
	}
	/**
	 * Instantiates a new cell list with context menu.
	 */
	public MeasurePackageClauseCellListWidget() {
		disclosurePanelAssociations.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_DisclosurePanel");
		disclosurePanelItemCountTable.getElement().setAttribute("id", "MeasurePackageClause_ItemCnt_DisclosurePanel");
		addClickHandlersToDisclosurePanels();
		leftPagerPanel.addStyleName("measurePackageCellListscrollable");
		leftPagerPanel.setDisplay(getLeftCellList());
		leftRangeLabelPager.setDisplay(getLeftCellList());
		rightPagerPanel.addStyleName("measurePackageCellListscrollable");
		rightPagerPanel.setDisplay(getRightCellList());
		rightRangeLabelPager.setDisplay(getRightCellList());
		Label packageTabName = new Label("Package Grouping");
		packageTabName.getElement().setAttribute("id", "MeasurePackageClause_PackageHeading_Lbl");
		packageTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(packageTabName);
		mainFlowPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel leftCellListVPanel = new VerticalPanel();
		leftCellListVPanel.getElement().setAttribute("id", "MeasurePackageClause_LeftCellListVPanel");
		leftCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Clauses </b>"));
		leftCellListVPanel.add(leftPagerPanel);
		VerticalPanel rightCellListVPanel = new VerticalPanel();
		rightCellListVPanel.getElement().setAttribute("id", "MeasurePackageClause_RightCellListVPanel");
		rightCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Package Grouping </b>"));
		rightCellListVPanel.add(rightPagerPanel);
		hp.add(leftCellListVPanel);
		hp.add(buildClauseAddButtonWidget());
		hp.add(rightCellListVPanel);
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setAttribute("id", "MeasurePackageClause_MainVPanel");
		disclosurePanelItemCountTable.clear();
		disclosurePanelAssociations.clear();
		vp.add(buildItemCountWidget());
		vp.add(buildAddAssociationWidget());
		hp.add(vp);
		hp.getElement().setAttribute("id", "MeasurePackageClause_MainHoriPanel");
		packageName.addStyleName("measureGroupPackageName");
		mainFlowPanel.add(packageName);
		mainFlowPanel.add(errorMessages);
		mainFlowPanel.add(successMessages);
		mainFlowPanel.add(hp);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(saveGrouping);
		mainFlowPanel.setStylePrimaryName("valueSetSearchPanel");
	}
	/**
	 * Click Handlers for Disclosure Panel.
	 */
	private void addClickHandlersToDisclosurePanels() {
		disclosurePanelItemCountTable.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				disclosurePanelItemCountTable.setOpen(true);
				disclosurePanelAssociations.setOpen(false);
			}
		});
		disclosurePanelAssociations.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				disclosurePanelAssociations.setOpen(true);
				disclosurePanelItemCountTable.setOpen(false);
			}
		});
	}
	
	private void addClickHandlersToAddItemCountList(){
		saveItemcountList.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println("Selected ItemCountList: "+ itemCountSelectionList.size());
				map.get(rightCellListSelectionModel.getSelectedObject().getName()).setItemCountList(itemCountSelectionList);
			}
		});
	}
	/**
	 * Build Ok - Cancel Button Widget in Item Count Disclosure Panel.
	 * @return Widget.
	 */
	private Widget getItemCountTableButtonPanel() {
		itemCountButtonPanel.addStyleName("rightAlignButton");
		itemCountButtonPanel.add(saveItemcountList);
		return itemCountButtonPanel;
	}
	/**
	 * Add Columns to Item Count Cell Table.
	 * @return CellTable.
	 */
	private CellTable<QualityDataSetDTO> addColumntoTable() {
		MatCheckBoxCell chkBtnCell = new MatCheckBoxCell(false , true);
		Column<QualityDataSetDTO, Boolean> selectColumn = new Column<QualityDataSetDTO, Boolean>(chkBtnCell){
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				object.setUsedMP(false);
				if ((itemCountSelectionList!=null)&& (itemCountSelectionList.size() > 0)) {
					for (int i = 0; i < itemCountSelectionList.size(); i++) {
						if (itemCountSelectionList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
							object.setUsedMP(true);
							break;
						}
					}
				} else {
					object.setUsedMP(false);
				}
				return object.isUsedMP();
			}
		};
		selectColumn.setFieldUpdater(new FieldUpdater<QualityDataSetDTO, Boolean>() {
			@Override
			public void update(int index, QualityDataSetDTO object,
					Boolean value) {
				itemCountSelection.setSelected(object, value);
				if (value) {
					itemCountSelectionList.add(object);
				} else {
					for (int i = 0; i < itemCountSelectionList.size(); i++) {
						if (itemCountSelectionList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
							itemCountSelectionList.remove(i);
							break;
						}
					}
					
				}
			}
		});
		itemCountCellTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select"
				+ "</span>"));
		
		
		Column<QualityDataSetDTO, SafeHtml> codeListName = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String value;
				String QDMDetails = "";
				
				if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					QDMDetails = "(User defined)";
				}  else {
					String version = object.getVersion();
					String effectiveDate = object.getEffectiveDate();
					
					if (effectiveDate != null) {
						
						QDMDetails = "(OID: " + object.getOid() + ", Effective Date: " + effectiveDate + ")";
					}  else if (!version.equals("1.0") && !version.equals("1")) {
						
						QDMDetails = "(OID: " + object.getOid() + ", Version: " + version + ")";
					} else {
						
						QDMDetails = "(OID: " + object.getOid() + ")";
					}
				}
				
				if ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) {
					value = object.getOccurrenceText() + " of " + object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + QDMDetails + " \" tabIndex=\"0\" >" + value + " </span>");
					
				} else {
					value = object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + QDMDetails + " \" tabIndex=\"0\">" + value + " </span>");
				}
				
				return sb.toSafeHtml();
			}
		};
		
		itemCountCellTable.addColumn(codeListName, SafeHtmlUtils.fromSafeConstant("<span title='Name'>" + "Name"
				+ "</span>"));
		
		
		Column<QualityDataSetDTO, SafeHtml> vsacDataType = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
			
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				sb.appendHtmlConstant("<span title=\"" + object.getDataType() + " \" tabIndex=\"0\" >"
						+ object.getDataType() + " </span>");
				
				return sb.toSafeHtml();
			}
		};
		
		itemCountCellTable.addColumn(vsacDataType, SafeHtmlUtils.fromSafeConstant("<span title='Data Type'>" + "Data Type"
				+ "</span>"));
		
		return itemCountCellTable;
	}
	/**
	 * Adds the cell table.
	 *
	 * @return the panel
	 */
	private Panel buildItemCountCellTable() {
		panel.clear();
		if (getAppliedQdmList().size() > 0) {
			itemCountCellTable = new CellTable<QualityDataSetDTO>();
			itemCountSelection = new MultiSelectionModel<QualityDataSetDTO>();
			itemCountCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			itemCountCellTable.setSelectionModel(itemCountSelection);
			ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
			itemCountCellTable.setPageSize(PAGESIZE);
			itemCountCellTable.setRowData(getAppliedQdmList());
			itemCountCellTable.setRowCount(getAppliedQdmList().size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(getAppliedQdmList());
			itemCountCellTable = addColumntoTable();
			sortProvider.addDataDisplay(itemCountCellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
			spager.setPageStart(0);
			spager.setDisplay(itemCountCellTable);
			spager.setPageSize(PAGESIZE);
			panel.setStylePrimaryName("valueSetSearchPanel");
			panel.add(itemCountCellTable);
			panel.add(new SpacerWidget());
			panel.add(spager);
			panel.add(new SpacerWidget());
			panel.add(itemCountButtonPanel);
		} else {
			panel.setStylePrimaryName("valueSetSearchPanel");
			panel.add(new SpacerWidget());
			panel.add(new SpacerWidget());
			panel.add(new SpacerWidget());
			panel.add(new HTML("<b>No Applied Elements.</b>"));
			panel.add(new SpacerWidget());
			panel.add(new SpacerWidget());
			panel.add(new SpacerWidget());
		}
		return panel;
	}
	
	public List<QualityDataSetDTO> getItemCountSelectionList() {
		return itemCountSelectionList;
	}
	
	public Panel getAssociatedPOPCellListWidget(){
		vPanel.clear();
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		associatedSelectionModel = new SingleSelectionModel<MeasurePackageClauseDetail>();
		associatedPOPCellList = new CellList<MeasurePackageClauseDetail>(getAssociatedPOPCompositeCell());
		associatedSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
				Window.alert("Associated Selection handler:"+ associatedSelectionModel.getSelectedObject().getName());
			}
		});
		associatedPOPCellList.setSelectionModel(associatedSelectionModel,
				DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		associatedPOPCellList.setPageSize(10);
		associatedPOPCellList.setRowData(associatedPopulationList);
		associatedPOPCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<MeasurePackageClauseDetail> dataProvider =
				new ListDataProvider<MeasurePackageClauseDetail>(associatedPopulationList);
		dataProvider.addDataDisplay(associatedPOPCellList);
		vPanel.setSize("200px", "200px");
		vPanel.add(associatedPOPCellList);
		return vPanel;
	}
	
	public Cell<MeasurePackageClauseDetail> getAssociatedPOPCompositeCell()
	{
		ArrayList<HasCell<MeasurePackageClauseDetail, ?>> hasCells = new ArrayList<HasCell<MeasurePackageClauseDetail, ?>>();
		
		hasCells.add(new HasCell<MeasurePackageClauseDetail, Boolean>() {
			
			private RadioButtonCell rbCell = new RadioButtonCell(true, true);
			@Override
			
			public Cell<Boolean> getCell() {
				return rbCell;
			}
			
			@Override
			public FieldUpdater<MeasurePackageClauseDetail, Boolean> getFieldUpdater() {
				return new FieldUpdater<MeasurePackageClauseDetail, Boolean>() {
					
					@Override
					public void update(int index,
							MeasurePackageClauseDetail object, Boolean value) {
						//Window.alert("RadioButton Clicked:"+ object.getName());
						associatedSelectionModel.setSelected(object, value);
					}
				};
			}
			
			@Override
			public Boolean getValue(MeasurePackageClauseDetail object) {
				return associatedSelectionModel.isSelected(object);
			} });
		
		hasCells.add(new HasCell<MeasurePackageClauseDetail, String>(){
			private TextCell cell = new TextCell();
			@Override
			public Cell<String> getCell() {
				return cell;
			}
			
			
			@Override
			public String getValue(MeasurePackageClauseDetail object) {
				return object.getName();
			}
			
			
			@Override
			public FieldUpdater<MeasurePackageClauseDetail, String> getFieldUpdater() {
				
				return new FieldUpdater<MeasurePackageClauseDetail, String>() {
					
					@Override
					public void update(int index, MeasurePackageClauseDetail object,
							String value) {
						
						
					}
				};
			}});
		
		
		Cell<MeasurePackageClauseDetail> myClassCell = new CompositeCell<MeasurePackageClauseDetail>(hasCells) {
			
			
			@Override
			public void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb)
			{
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected Element getContainerElement(Element parent)
			{
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
			
			@Override
			protected <X> void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb, HasCell<MeasurePackageClauseDetail, X> hasCell)
			{
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</font></td>");
			}
			
		};
		
		return myClassCell;
	}
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url
	 * @param id - String.
	 * @return the button
	 */
	private Button buildAddButton(String imageUrl , String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Builds the double add button.
	 *
	 * @param imageUrl the image url
	 * @param id - String Id.
	 * @return the button
	 */
	private Button buildDoubleAddButton(String imageUrl , String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Widget to add Left/right/leftAll/RightAll button's.
	 * @return - Widget.
	 */
	private Widget buildClauseAddButtonWidget() {
		VerticalPanel clauseButtonPanel = new VerticalPanel();
		clauseButtonPanel.setStyleName("qdmElementAddButtonPanel");
		clauseButtonPanel.getElement().setAttribute("id", "ClauseButtonVerticalPanel");
		addClauseRight.setTitle("Add Clauses to Grouping");
		addClauseRight.getElement().setAttribute("alt", "Add Clauses to Grouping");
		addClauseLeft.setTitle("Remove Clauses from Grouping");
		addClauseLeft.getElement().setAttribute("alt", "Remove Clauses from Grouping");
		addAllClauseRight.setTitle("Add all Clauses to Grouping");
		addAllClauseRight.getElement().setAttribute("alt", "Add all Clauses to Grouping");
		addAllClauseLeft.setTitle("Remove all Clauses from Grouping");
		addAllClauseLeft.getElement().setAttribute("alt", "Remove all Clauses from Grouping");
		clauseButtonPanel.add(addClauseRight);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addClauseLeft);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addAllClauseRight);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addAllClauseLeft);
		clauseButtonHandlers();
		return clauseButtonPanel;
	}
	/**
	 * Button Left/Right/LeftAll/RightAll handler's.
	 */
	private void clauseButtonHandlers() {
		addClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ((clausesPopulationList.size() > 0)
						&& (leftCellListSelectionModel.getSelectedObject() != null)) {
					groupingPopulationList.add(leftCellListSelectionModel.getSelectedObject());
					clausesPopulationList.remove(leftCellListSelectionModel.getSelectedObject());
					Collections.sort(groupingPopulationList);
					Collections.sort(clausesPopulationList);
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
					leftCellListSelectionModel.clear();
				}
			}
		});
		addClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ((groupingPopulationList.size() > 0)
						&& (rightCellListSelectionModel.getSelectedObject() != null)) {
					clausesPopulationList.add(rightCellListSelectionModel.getSelectedObject());
					groupingPopulationList.remove(rightCellListSelectionModel.getSelectedObject());
					Collections.sort(groupingPopulationList);
					Collections.sort(clausesPopulationList);
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
					rightCellListSelectionModel.clear();
					disclosurePanelAssociations.setVisible(false);
					disclosurePanelItemCountTable.setVisible(false);
				}
			}
		});
		addAllClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (clausesPopulationList.size() != 0) {
					groupingPopulationList.addAll(clausesPopulationList);
					clausesPopulationList.removeAll(clausesPopulationList);
					Collections.sort(groupingPopulationList);
					rightCellListSelectionModel.clear();
					leftCellListSelectionModel.clear();
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
				}
			}
		});
		addAllClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (groupingPopulationList.size() != 0) {
					clausesPopulationList.addAll(groupingPopulationList);
					groupingPopulationList.removeAll(groupingPopulationList);
					Collections.sort(clausesPopulationList);
					rightCellListSelectionModel.clear();
					leftCellListSelectionModel.clear();
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
					disclosurePanelAssociations.setVisible(false);
					disclosurePanelItemCountTable.setVisible(false);
				}
			}
		});
	}
	
	private int countDetailsWithType(
			List<MeasurePackageClauseDetail> clauseList,  String type) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		int count = 0;
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (type.equals(detail.getType()) && !associatedPopulationList.contains(detail)) {
				associatedPopulationList.add(detail);
				count++;
			}
		}
		return count;
	}
	/**
	 * Right CellList Clause Cell Class.
	 *
	 */
	class RightClauseCell implements Cell<MeasurePackageClauseDetail> {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getName() != null) {
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());
				SafeHtml rendered = templates.cell(value.getName(), safeValue);
				sb.append(rendered);
			}
		}
		@Override
		public boolean dependsOnSelection() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public Set<String> getConsumedEvents() {
			// TODO Auto-generated method stub
			return Collections.singleton("click");
		}
		@Override
		public boolean handlesSelection() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
			addClickHandlersToAddItemCountList();
			itemCountSelectionList = new ArrayList<QualityDataSetDTO>();
			map.put(rightCellListSelectionModel.getSelectedObject().getName(),rightCellListSelectionModel.getSelectedObject());
			MeasurePackageClauseDetail measureDetail = map.get(rightCellListSelectionModel.getSelectedObject().getName());
			if((measureDetail.getItemCountList()!=null) && (measureDetail.getItemCountList().size()>0)){
				itemCountSelectionList = measureDetail.getItemCountList();
			}
			System.out.println("ItemCountList :"+itemCountSelectionList);
			if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
				leftCellListSelectionModel.clear();
				String scoring = MatContext.get().getCurrentMeasureScoringType();
				//Show Association only for Ratio Measures.
				if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)
						&& ((value.getType().equals("denominator"))
								|| (value.getType().equals("numerator")))) {
					buildItemCountCellTable();
					getItemCountTableButtonPanel();
					//getAssociatedPOPCellListWidget();
					disclosurePanelItemCountTable.setVisible(true);
					disclosurePanelItemCountTable.setOpen(false);
					// If More than one Populations are added in Grouping, Add Association Widget is shown
					// otherwise available population is added to Denominator and Numerator Association List.
					if (countDetailsWithType(groupingPopulationList, ConstantMessages.POPULATION_CONTEXT_ID) >= 2) {
						getAssociatedPOPCellListWidget();
						disclosurePanelAssociations.setVisible(true);
						disclosurePanelAssociations.setOpen(false);
					} else  {
						disclosurePanelAssociations.setVisible(false);
						disclosurePanelAssociations.setOpen(false);
					}
				} else {
					buildItemCountCellTable();
					getItemCountTableButtonPanel();
					//getAssociatedPOPCellListWidget();
					disclosurePanelItemCountTable.setVisible(true);
					disclosurePanelAssociations.setVisible(false);
					disclosurePanelItemCountTable.setOpen(false);
					disclosurePanelAssociations.setOpen(false);
					associatedPopulationList.clear();
				}
			}
		}
		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
		}
	}
	/**
	 * Left CellList Clause Cell Class.
	 *
	 */
	class LeftClauseCell implements Cell<MeasurePackageClauseDetail> {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getName() != null) {
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());
				SafeHtml rendered = templates.cell(value.getName(), safeValue);
				sb.append(rendered);
			}
		}
		@Override
		public boolean dependsOnSelection() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public Set<String> getConsumedEvents() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public boolean handlesSelection() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
		}
		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
		}
	}
	/**
	 * @return the groupingPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getGroupingPopulationList() {
		return groupingPopulationList;
	}
	
	/**
	 * @param groupingPopulationList the groupingPopulationList to set
	 */
	public void setGroupingPopulationList(List<MeasurePackageClauseDetail> groupingPopulationList) {
		this.groupingPopulationList = (ArrayList<MeasurePackageClauseDetail>) groupingPopulationList;
	}
	
	/**
	 * @return the clausesPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getClausesPopulationList() {
		return clausesPopulationList;
	}
	
	/**
	 * @param clauses the clausesPopulationList to set
	 */
	public void setClausesPopulationList(List<MeasurePackageClauseDetail> clauses) {
		clausesPopulationList = (ArrayList<MeasurePackageClauseDetail>) clauses;
	}
	/**
	 * @return the rightPagerPanel
	 */
	public ShowMorePagerPanel getRightPagerPanel() {
		return rightPagerPanel;
	}
	
	/**
	 * @return the leftPagerPanel
	 */
	public ShowMorePagerPanel getLeftPagerPanel() {
		return leftPagerPanel;
	}
	
	/**
	 * @return the rightRangeLabelPager
	 */
	public RangeLabelPager getRightRangeLabelPager() {
		return rightRangeLabelPager;
	}
	
	/**
	 * @return the leftRangeLabelPager
	 */
	public RangeLabelPager getLeftRangeLabelPager() {
		return leftRangeLabelPager;
	}
	/**
	 * @return the disclosurePanelItemCountTable
	 */
	public DisclosurePanel getDisclosurePanelItemCountTable() {
		return disclosurePanelItemCountTable;
	}
	/**
	 * @return the disclosurePanelAssociations
	 */
	public DisclosurePanel getDisclosurePanelAssociations() {
		return disclosurePanelAssociations;
	}
	
	public List<QualityDataSetDTO> getAppliedQdmList() {
		return appliedQdmList;
	}
	
	public void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList) {
		this.appliedQdmList = appliedQdmList;
	}
	/**
	 * @return the packageName
	 */
	public Label getPackageName() {
		return packageName;
	}
	/**
	 * @param packageName the packageName to set
	 */
	public void setPackageName(Label packageName) {
		this.packageName = packageName;
	}
	/**
	 * @return the saveGrouping
	 */
	public PrimaryButton getSaveGrouping() {
		return saveGrouping;
	}
	/**
	 * @param saveGrouping the saveGrouping to set
	 */
	public void setSaveGrouping(PrimaryButton saveGrouping) {
		this.saveGrouping = saveGrouping;
	}
	/**
	 * @return the addClauseRight
	 */
	public Button getAddClauseRight() {
		return addClauseRight;
	}
	/**
	 * @return the addClauseLeft
	 */
	public Button getAddClauseLeft() {
		return addClauseLeft;
	}
	/**
	 * @return the addAllClauseRight
	 */
	public Button getAddAllClauseRight() {
		return addAllClauseRight;
	}
	/**
	 * @return the addAllClauseLeft
	 */
	public Button getAddAllClauseLeft() {
		return addAllClauseLeft;
	}
	/**
	 * @return the errorMessages
	 */
	public ErrorMessageDisplay getErrorMessages() {
		return errorMessages;
	}
	/**
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(ErrorMessageDisplay errorMessages) {
		this.errorMessages = errorMessages;
	}
	/**
	 * @return the successMessages
	 */
	public SuccessMessageDisplay getSuccessMessages() {
		return successMessages;
	}
	/**
	 * @param successMessages the successMessages to set
	 */
	public void setSuccessMessages(SuccessMessageDisplay successMessages) {
		this.successMessages = successMessages;
	}
	/**
	 * @return the associatedCellList
	 */
	public CellList<MeasurePackageClauseDetail> getAssociatedCellList() {
		return associatedCellList;
	}
	/**
	 * @param associatedCellList the associatedCellList to set
	 */
	public void setAssociatedCellList(CellList<MeasurePackageClauseDetail> associatedCellList) {
		this.associatedCellList = associatedCellList;
	}
	/**
	 * @return the associatedPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getAssociatedPopulationList() {
		return associatedPopulationList;
	}
	/**
	 * @param associatedPopulationList the associatedPopulationList to set
	 */
	public void setAssociatedPopulationList(ArrayList<MeasurePackageClauseDetail> associatedPopulationList) {
		this.associatedPopulationList = associatedPopulationList;
	}
}
