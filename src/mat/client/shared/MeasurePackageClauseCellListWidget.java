package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import mat.client.CustomPager;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import org.apache.commons.lang.StringUtils;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
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
	private static final int ASOOCIATED_LIST_SIZE = 10;
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
	/** List of Elements in Grouping List. */
	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Clause List.*/
	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> associatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements For Denominator Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> denoAssociatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements For Numerator Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> numAssociatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List Data Provider for Associate Cell List Widget.**/
	private ListDataProvider<MeasurePackageClauseDetail> associationListDataProvider;
	/** Cell List Associate Widget. **/
	private CellList<MeasurePackageClauseDetail> associatedPOPCellList;
	/** Error Message in Package Grouping Section. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	/** Success Message in Package Grouping Section.**/
	private SuccessMessageDisplay successMessages = new SuccessMessageDisplay();
	/**Selection Model for Associate Widget.**/
	private SingleSelectionModel<MeasurePackageClauseDetail> associatedSelectionModel;
	/**Map of Grouping Clauses.**/
	private Map<String, MeasurePackageClauseDetail>  groupingClausesMap = new HashMap<String, MeasurePackageClauseDetail>();
	/** List of selected Item counts for Clauses.**/
	private List<QualityDataSetDTO> itemCountSelectionList;
	
	private Label ItemCountLabel = new Label();
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
			for(MeasurePackageClauseDetail detail : groupingPopulationList){
				groupingClausesMap.put(detail.getName(), detail);
			}
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
		disclosurePanelItemCountTable.clear();
		disclosurePanelItemCountTable.add(buildItemCountCellTable());
		disclosurePanelItemCountTable.setOpen(false);
		disclosurePanelItemCountTable.setVisible(false);
		return disclosurePanelItemCountTable;
	}
	/**
	 * Builds the add association widget.
	 * @param populationList - {@link List}.
	 * @return the widget
	 */
	private Widget buildAddAssociationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		disclosurePanelAssociations.clear();
		disclosurePanelAssociations.add(getAssociatedPOPCellListWidget(populationList));
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
		disclosurePanelAssociations.clear();
		vp.add(disclosurePanelAssociations);
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
	/**
	 * Add Item count to selected populations.
	 * @param saveItemCount - {@link SecondaryButton}.
	 */
	private void addClickHandlersToAddItemCountList(SecondaryButton saveItemCount) {
		saveItemCount.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clear();
				successMessages.clear();
				if (rightCellListSelectionModel.getSelectedObject() == null) {
					return;
				}
				System.out.println("Selected ItemCountList: " + itemCountSelectionList.size());
				groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName())
				.setItemCountList(itemCountSelectionList);
				successMessages.setMessage("Successfully modified Item count(s)."
						+ " Please click save grouping to save the changes.");
			}
		});
	}
	/**
	 * Add Association to selected Clause Cell.
	 * @param secondaryButton - {@link SecondaryButton}.
	 */
	private void addClickHandlersToAddAssociation(SecondaryButton secondaryButton) {
		secondaryButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clear();
				successMessages.clear();
				MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
				MeasurePackageClauseDetail otherClauseCell = null;
				String existingUuid = groupingClausesMap.get(selectedClauseCell.getName()).getAssociatedPopulationUUID();
				String otherClauseType = null;
				if (selectedClauseCell.getType().equalsIgnoreCase("denominator")) {
					otherClauseType = "numerator";
					for (MeasurePackageClauseDetail detail : denoAssociatedPopulationList) {
						otherClauseCell = detail;
						if ((existingUuid != null)
								&& existingUuid.equals(detail.getId())) {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
								otherClauseCell = null;
							} else {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(null);
								otherClauseCell = detail;
							}
						} else {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
								otherClauseCell = null;
							} else {
								otherClauseCell = detail;
							}
						}
					}
				} else if (selectedClauseCell.getType().equalsIgnoreCase("numerator")) {
					otherClauseType = "denominator";
					for (MeasurePackageClauseDetail detail : numAssociatedPopulationList) {
						otherClauseCell = detail;
						if ((existingUuid != null)
								&& existingUuid.equals(detail.getId())) {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
								otherClauseCell = null;
							} else {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(null);
								otherClauseCell = detail;
							}
						} else {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
								otherClauseCell = null;
							} else {
								otherClauseCell = detail;
							}
						}
					}
				} else {
					for (MeasurePackageClauseDetail detail : associatedPopulationList) {
						if ((existingUuid != null)
								&& existingUuid.equals(detail.getId())) {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
							} else {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(null);
							}
						} else {
							if (detail.isAssociatedPopulation()) {
								groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
								setAssociatedPopulationUUID(detail.getId());
								break;
							}
						}
					}
				}
				if (otherClauseCell != null) {
					for (Entry<String, MeasurePackageClauseDetail> entry : groupingClausesMap.entrySet()) {
						if (entry.getValue().getType().equalsIgnoreCase(otherClauseType)) {
							MeasurePackageClauseDetail updateDetails = entry.getValue();
							groupingClausesMap.get(updateDetails.getName()).
							setAssociatedPopulationUUID(otherClauseCell.getId());
							/*if(otherClauseType.equalsIgnoreCase("denominator")){
								
							}*/
							break;
						}
					}
					
				}
				System.out.println(groupingClausesMap);
				successMessages.setMessage("Successfully modified Association."
						+ " Please click save grouping to save changes.");
			}
		});
	}
	/**
	 * Add Columns to Item Count Cell Table.
	 * @return CellTable.
	 */
	// To Do : Remove isUsedMP Flag from QualityDataSetDTO. Set isUsed running xpath while getting Applied QDM List on server side.
	private CellTable<QualityDataSetDTO> addColumntoTable() {
		MatCheckBoxCell chkBtnCell = new MatCheckBoxCell(false , true);
		Column<QualityDataSetDTO, Boolean> selectColumn = new Column<QualityDataSetDTO, Boolean>(chkBtnCell) {
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				boolean isSelected = false;
				if ((itemCountSelectionList != null) && (itemCountSelectionList.size() > 0)) {
					for (int i = 0; i < itemCountSelectionList.size(); i++) {
						if (itemCountSelectionList.get(i).getUuid().equalsIgnoreCase(object.getUuid())) {
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
				ItemCountLabel.setText("Selected Items: " + itemCountSelectionList.size());
			}
		});
		itemCountCellTable.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select"
				+ "</span>"));
		Column<QualityDataSetDTO, SafeHtml> codeListName = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(QualityDataSetDTO object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String value;
				String qdmDetails = StringUtils.EMPTY;
				if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
					qdmDetails = "(User defined)";
				}  else {
					String version = object.getVersion();
					String effectiveDate = object.getEffectiveDate();
					if (effectiveDate != null) {
						qdmDetails = "(OID: " + object.getOid() + ", Effective Date: " + effectiveDate + ")";
					}  else if (!version.equals("1.0") && !version.equals("1")) {
						qdmDetails = "(OID: " + object.getOid() + ", Version: " + version + ")";
					} else {
						qdmDetails = "(OID: " + object.getOid() + ")";
					}
				}
				if ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) {
					value = object.getOccurrenceText() + " of " + object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + qdmDetails + " \" tabIndex=\"0\" >" + value + " </span>");
				} else {
					value = object.getCodeListName();
					sb.appendHtmlConstant("<span title=\"" + qdmDetails + " \" tabIndex=\"0\">" + value + " </span>");
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
		/** The panel. */
		VerticalPanel panel = new VerticalPanel();
		if (getAppliedQdmList() != null) {
			if (getAppliedQdmList().size() > 0) {
				itemCountCellTable = new CellTable<QualityDataSetDTO>();
				itemCountSelection = new MultiSelectionModel<QualityDataSetDTO>();
				itemCountCellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
				itemCountCellTable.setSelectionModel(itemCountSelection);
				ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
				itemCountCellTable.setPageSize(PAGESIZE);
				ItemCountLabel.setText("Selected Items: " + itemCountSelectionList.size());
				if ((itemCountSelectionList != null) && (itemCountSelectionList.size() > 0)) {
					updateQDMSelectedList(getAppliedQdmList());
					List<QualityDataSetDTO> selectedQDMList = new ArrayList<QualityDataSetDTO>();
					selectedQDMList.addAll(swapQdmElements(getAppliedQdmList()));
					itemCountCellTable.setRowData(selectedQDMList);
					itemCountCellTable.setRowCount(selectedQDMList.size(), true);
					sortProvider.refresh();
					sortProvider.getList().addAll(selectedQDMList);
				} else {
					itemCountCellTable.setRowData(getAppliedQdmList());
					itemCountCellTable.setRowCount(getAppliedQdmList().size(), true);
					sortProvider.refresh();
					sortProvider.getList().addAll(getAppliedQdmList());
				}
				itemCountCellTable = addColumntoTable();
				sortProvider.addDataDisplay(itemCountCellTable);
				CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
				MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
				spager.setPageStart(0);
				spager.setDisplay(itemCountCellTable);
				spager.setPageSize(PAGESIZE);
				panel.setStylePrimaryName("valueSetSearchPanel");
				/** Flow Panel for OK-Cancel buttons in Item Count Table. **/
				FlowPanel itemCountButtonPanel = new FlowPanel();
				SecondaryButton saveItemCountList = new SecondaryButton("OK");
				itemCountButtonPanel.addStyleName("rightAlignButton");
				itemCountButtonPanel.add(saveItemCountList);
				panel.add(itemCountCellTable);
				panel.add(new SpacerWidget());
				panel.add(spager);
				panel.add(new SpacerWidget());
				panel.add(ItemCountLabel);
				panel.add(itemCountButtonPanel);
				addClickHandlersToAddItemCountList(saveItemCountList);
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
		}
		return panel;
	}
	
	/**
	 * @param qdmList
	 * @return
	 */
	private  List<QualityDataSetDTO> swapQdmElements(List<QualityDataSetDTO> qdmList){
		List<QualityDataSetDTO> qdmselectedList = new ArrayList<QualityDataSetDTO>();
		qdmselectedList.addAll(itemCountSelectionList);
		for(int i=0;i<qdmList.size();i++){
			if(!itemCountSelectionList.contains(qdmList.get(i))){
				qdmselectedList.add(qdmList.get(i));
			}
			
		}
		
		return qdmselectedList;
	}
	
	/**
	 * @param selectedList
	 */
	private void updateQDMSelectedList(List<QualityDataSetDTO> selectedList) {
		if (itemCountSelectionList.size() != 0) {
			for (int i = 0; i < itemCountSelectionList.size(); i++) {
				for (int j = 0; j < selectedList.size(); j++) {
					if (itemCountSelectionList.get(i).getUuid().equalsIgnoreCase(selectedList.get(j).getUuid())) {
						itemCountSelectionList.set(i, selectedList.get(j));
						break;
					}
				}
			}
		}
		
	}
	
	/**
	 * @param populationList - ArrayList.
	 * @return Vertical Panel.
	 */
	private Panel getAssociatedPOPCellListWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		associatedSelectionModel = new SingleSelectionModel<MeasurePackageClauseDetail>();
		associatedPOPCellList = new CellList<MeasurePackageClauseDetail>(getAssociatedPOPCompositeCell());
		associatedSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
			}
		});
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			associatedPOPCellList.setSelectionModel(associatedSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			associatedPOPCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		associatedPOPCellList.setPageSize(ASOOCIATED_LIST_SIZE);
		associatedPOPCellList.setRowData(populationList);
		associatedPOPCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		associationListDataProvider =
				new ListDataProvider<MeasurePackageClauseDetail>(populationList);
		associationListDataProvider.addDataDisplay(associatedPOPCellList);
		vPanel.setSize("200px", "170px");
		vPanel.add(associatedPOPCellList);
		FlowPanel associateWidgetButtonPanel = new FlowPanel();
		associateWidgetButtonPanel.addStyleName("rightAlignButton");
		SecondaryButton saveAssociationInClause = new SecondaryButton("Ok");
		associateWidgetButtonPanel.add(saveAssociationInClause);
		vPanel.add(associateWidgetButtonPanel);
		addClickHandlersToAddAssociation(saveAssociationInClause);
		return vPanel;
	}
	/**
	 * @return Cell.
	 */
	private Cell<MeasurePackageClauseDetail> getAssociatedPOPCompositeCell() {
		ArrayList<HasCell<MeasurePackageClauseDetail, ?>> hasCells = new ArrayList<HasCell<MeasurePackageClauseDetail, ?>>();
		hasCells.add(new HasCell<MeasurePackageClauseDetail, Boolean>() {
			private CheckboxCell chkCell = new CheckboxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return chkCell;
			}
			@Override
			public FieldUpdater<MeasurePackageClauseDetail, Boolean> getFieldUpdater() {
				return new FieldUpdater<MeasurePackageClauseDetail, Boolean>() {
					@Override
					public void update(int index,
							MeasurePackageClauseDetail object, Boolean value) {
						successMessages.clear();
						errorMessages.clear();
						MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.
								getSelectedObject();
						if (selectedClauseCell.getType().equalsIgnoreCase("denominator")) {
							for (MeasurePackageClauseDetail detail : denoAssociatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(denoAssociatedPopulationList);
							disclosurePanelAssociations.setOpen(false);
							disclosurePanelAssociations.setOpen(true);
						} else if (selectedClauseCell.getType().equalsIgnoreCase("numerator")) {
							for (MeasurePackageClauseDetail detail : numAssociatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(numAssociatedPopulationList);
							disclosurePanelAssociations.setOpen(false);
							disclosurePanelAssociations.setOpen(true);
						} else {
							for (MeasurePackageClauseDetail detail : associatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(associatedPopulationList);
							disclosurePanelAssociations.setOpen(false);
							disclosurePanelAssociations.setOpen(true);
						}
					}
				};
			}
			@Override
			public Boolean getValue(MeasurePackageClauseDetail object) {
				return object.isAssociatedPopulation();
			} });
		hasCells.add(new HasCell<MeasurePackageClauseDetail, String>() {
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
			}
		}
				);
		Cell<MeasurePackageClauseDetail> associatePopulationCell = new CompositeCell<MeasurePackageClauseDetail>(hasCells) {
			@Override
			public void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected Element getContainerElement(Element parent) {
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
			@Override
			protected <X> void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb
					, HasCell<MeasurePackageClauseDetail, X> hasCell) {
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</font></td>");
			}
			
		};
		
		return associatePopulationCell;
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
				errorMessages.clear();
				successMessages.clear();
				if ((clausesPopulationList.size() > 0)
						&& (leftCellListSelectionModel.getSelectedObject() != null)) {
					ArrayList<MeasurePackageClauseDetail> validateGroupingList = new ArrayList<MeasurePackageClauseDetail>();
					validateGroupingList.addAll(groupingPopulationList);
					validateGroupingList.add(leftCellListSelectionModel.getSelectedObject());
					if (isValid(validateGroupingList)) {
						groupingPopulationList.add(leftCellListSelectionModel.getSelectedObject());
						groupingClausesMap.put(leftCellListSelectionModel.getSelectedObject().getName(),
								leftCellListSelectionModel.getSelectedObject());
						clausesPopulationList.remove(leftCellListSelectionModel.getSelectedObject());
						Collections.sort(groupingPopulationList);
						Collections.sort(clausesPopulationList);
						getRightPagerPanel().setDisplay(getRightCellList());
						getLeftPagerPanel().setDisplay(getLeftCellList());
						leftCellListSelectionModel.clear();
					}
				}
			}
		});
		addClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clear();
				successMessages.clear();
				if ((groupingPopulationList.size() > 0)
						&& (rightCellListSelectionModel.getSelectedObject() != null)) {
					//If clause if removed, and if it is associated with any other clause,
					//all it's associations are removed.
					/*for (MeasurePackageClauseDetail detail : groupingPopulationList) {
						if ((detail.getAssociatedPopulationUUID() != null)
								&& detail.getAssociatedPopulationUUID().equalsIgnoreCase(
										rightCellListSelectionModel.getSelectedObject().getId())) {
							detail.setAssociatedPopulationUUID(null);
							groupingClausesMap.put(detail.getName(), detail);
						}
					}*/
					clausesPopulationList.add(rightCellListSelectionModel.getSelectedObject());
					groupingPopulationList.remove(rightCellListSelectionModel.getSelectedObject());
					groupingClausesMap.remove(rightCellListSelectionModel.getSelectedObject().getName());
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
				errorMessages.clear();
				successMessages.clear();
				if (clausesPopulationList.size() != 0) {
					ArrayList<MeasurePackageClauseDetail> validateGroupingList =
							new ArrayList<MeasurePackageClauseDetail>();
					validateGroupingList.addAll(clausesPopulationList);
					if (isValid(validateGroupingList)) {
						groupingPopulationList.addAll(clausesPopulationList);
						for (MeasurePackageClauseDetail detail : groupingPopulationList) {
							groupingClausesMap.put(detail.getName(), detail);
						}
						clausesPopulationList.removeAll(clausesPopulationList);
						Collections.sort(groupingPopulationList);
						rightCellListSelectionModel.clear();
						leftCellListSelectionModel.clear();
						getRightPagerPanel().setDisplay(getRightCellList());
						getLeftPagerPanel().setDisplay(getLeftCellList());
					}
				}
			}
		});
		addAllClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clear();
				successMessages.clear();
				if (groupingPopulationList.size() != 0) {
					/*for (MeasurePackageClauseDetail detail : groupingPopulationList) {
						detail.setAssociatedPopulationUUID(null);
					}*/
					clausesPopulationList.addAll(groupingPopulationList);
					for (MeasurePackageClauseDetail detail : groupingPopulationList) {
						groupingClausesMap.remove(detail.getName());
					}
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
	/**
	 * Method to count number of Clause types.
	 * @param clauseList -List.
	 * @param type - String.
	 * @return int.
	 */
	private int countTypeForAssociation(
			List<MeasurePackageClauseDetail> clauseList,  String type) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		denoAssociatedPopulationList.clear();
		numAssociatedPopulationList.clear();
		int count = 0;
		MeasurePackageClauseDetail selectedClauseNode = rightCellListSelectionModel.getSelectedObject();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (type.equals(detail.getType())) {
				if (detail.getId().equalsIgnoreCase(selectedClauseNode.getAssociatedPopulationUUID())) {
					detail.setAssociatedPopulation(true);
				} else {
					detail.setAssociatedPopulation(false);
				}
				if (selectedClauseNode.getType().equalsIgnoreCase("denominator")) {
					if (!denoAssociatedPopulationList.contains(detail)) {
						denoAssociatedPopulationList.add(detail);
					}
				} else {
					if (!numAssociatedPopulationList.contains(detail)) {
						numAssociatedPopulationList.add(detail);
					}
				}
				count++;
			}
		}
		Collections.sort(denoAssociatedPopulationList);
		Collections.sort(numAssociatedPopulationList);
		return count;
	}
	
	private int countDetailsWithType(
			final List<MeasurePackageClauseDetail> detailList, final String type) {
		int count = 0;
		for (MeasurePackageClauseDetail detail : detailList) {
			if (type.equals(detail.getType())) {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * Generate's Association List for Measure Observation in Ratio Measure's.
	 * @param clauseList - List.
	 */
	private void addPopulationForMeasureObservation(
			List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (("denominator".equalsIgnoreCase(detail.getType())
					|| "numerator".equalsIgnoreCase(detail.getType()))
					&& !associatedPopulationList.contains(detail)) {
				if (detail.getId().equalsIgnoreCase(rightCellListSelectionModel.
						getSelectedObject().getAssociatedPopulationUUID())) {
					detail.setAssociatedPopulation(true);
				} else {
					detail.setAssociatedPopulation(false);
				}
				associatedPopulationList.add(detail);
			}
		}
		Collections.sort(associatedPopulationList);
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
			return false;
		}
		@Override
		public Set<String> getConsumedEvents() {
			return Collections.singleton("click");
		}
		@Override
		public boolean handlesSelection() {
			return false;
		}
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
			errorMessages.clear();
			successMessages.clear();
			itemCountSelectionList = new ArrayList<QualityDataSetDTO>();
			groupingClausesMap.put(rightCellListSelectionModel.getSelectedObject().getName(), rightCellListSelectionModel.getSelectedObject());
			MeasurePackageClauseDetail measureDetail = groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName());
			if ((measureDetail.getItemCountList() != null) && (measureDetail.getItemCountList().size() > 0)) {
				itemCountSelectionList = measureDetail.getItemCountList();
			}
			System.out.println("ItemCountList :" + itemCountSelectionList);
			if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
				leftCellListSelectionModel.clear();
				String scoring = MatContext.get().getCurrentMeasureScoringType();
				//Show Association only for Ratio Measures.
				if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
					if ((value.getType().equalsIgnoreCase("denominator"))
							|| (value.getType().equalsIgnoreCase("numerator"))) {
						buildItemCountWidget();
						disclosurePanelItemCountTable.setVisible(true);
						disclosurePanelItemCountTable.setOpen(false);
						// If More than one Populations are added in Grouping, Add Association Widget is shown
						// otherwise available population is added to Denominator and Numerator Association List.
						if (countTypeForAssociation(groupingPopulationList
								, ConstantMessages.POPULATION_CONTEXT_ID) == 2) {
							if ((value.getType().equalsIgnoreCase("denominator"))) {
								buildAddAssociationWidget(denoAssociatedPopulationList);
							} else {
								buildAddAssociationWidget(numAssociatedPopulationList);
							}
							disclosurePanelAssociations.setVisible(true);
							disclosurePanelAssociations.setOpen(false);
						} else  {
							disclosurePanelAssociations.setVisible(false);
							disclosurePanelAssociations.setOpen(false);
						}
					} else if ((value.getType().equalsIgnoreCase("measureObservation"))) {
						addPopulationForMeasureObservation(groupingPopulationList);
						buildItemCountWidget();
						buildAddAssociationWidget(associatedPopulationList);
						disclosurePanelItemCountTable.setVisible(true);
						disclosurePanelItemCountTable.setOpen(false);
						disclosurePanelAssociations.setVisible(true);
						disclosurePanelAssociations.setOpen(false);
					} else {
						buildItemCountWidget();
						disclosurePanelItemCountTable.setVisible(true);
						disclosurePanelAssociations.setVisible(false);
						disclosurePanelItemCountTable.setOpen(false);
						disclosurePanelAssociations.setOpen(false);
						associatedPopulationList.clear();
					}
				} /*else if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equalsIgnoreCase(scoring)
						&& value.getType().equalsIgnoreCase("measureObservation")) {
					addMeasurePopulationForMeasureObservation(groupingPopulationList);
					buildAddAssociationWidget(associatedPopulationList);
					disclosurePanelAssociations.setVisible(true);
					disclosurePanelAssociations.setOpen(false);
				}*/ else {
					buildItemCountWidget();
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
			return false;
		}
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
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
			return false;
		}
		@Override
		public Set<String> getConsumedEvents() {
			return null;
		}
		@Override
		public boolean handlesSelection() {
			return false;
		}
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
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
			return false;
		}
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
		}
	}
	
	private boolean isValid(List<MeasurePackageClauseDetail> validatGroupingList) {
		List<MeasurePackageClauseDetail> detailList = validatGroupingList;
		List<String> messages = new ArrayList<String>();
		
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		
		// TODO refactor this into a common shared class so the server can use
		// it for validation also
		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING
				.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableWrongNumMessage());
			}
			
			if ((countDetailsWithType(detailList,
					ConstantMessages.NUMERATOR_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) != 0)
							|| (countDetailsWithType(detailList,
									ConstantMessages.DENOMINATOR_CONTEXT_ID) != 0)
									|| (countDetailsWithType(detailList,
											ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) != 0)
											|| (countDetailsWithType(detailList,
													ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getContinuousVariableMayNotContainMessage());
			}
			
		} else if (ConstantMessages.PROPORTION_SCORING.equalsIgnoreCase(scoring)) { /*
		 * PROPORTION at least one and only one Population,
		 * Denominator at least one or more Numerator zero or
		 * one Denominator Exclusions Denominator Exceptions and
		 * no Numerator Exclusions, Measure Population, Measure
		 * Observations
		 */
			/*
			 * at least one and only one Population, Denominator
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions, Denominator Exceptions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionTooManyMessage());
			}
			/* no Numerator Exclusions, Measure Population, Measure Observations */
			if ((countDetailsWithType(detailList,
					ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getProportionMayNotContainMessage());
			}
		} else if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) { /*
		 * at least one and only one Population, Denominator,
		 * Numerator, zero or one Denominator Exclusions and no
		 * Denominator Exceptions, Measure Population
		 * May contain one or more Measure Observation.
		 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioWrongNumMessage());
			}
			/*
			 * zero or one Denominator Exclusions
			 */
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCLUSIONS_CONTEXT_ID) > 1)
					|| (countDetailsWithType(detailList,
							ConstantMessages.NUMERATOR_EXCLUSIONS_CONTEXT_ID) > 1)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioTooManyMessage());
			}
			/*
			 * Not more than two populations are allowed.
			 * */
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 2)) {
				messages.add(MatContext.get().getMessageDelegate().getRATIO_TOO_MANY_POPULATIONS());
			}
			if ((countDetailsWithType(detailList,
					ConstantMessages.DENOMINATOR_EXCEPTIONS_CONTEXT_ID) != 0)
					|| (countDetailsWithType(detailList,
							ConstantMessages.MEASURE_POPULATION_CONTEXT_ID) != 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getRatioMayNotContainMessage());
			}
		} else if (ConstantMessages.COHORT_SCORING.equalsIgnoreCase(scoring)) {
			if ((countDetailsWithType(detailList,
					ConstantMessages.POPULATION_CONTEXT_ID) > 1)
					) {
				messages.add(MatContext.get().getMessageDelegate().getCOHORT_WRONG_NUM());
			}
		}
		if (messages.size() > 0) {
			errorMessages.setMessages(messages);
		} else {
			errorMessages.clear();
		}
		return messages.size() == 0;
	}
	/**
	 * @return List .
	 */
	public List<QualityDataSetDTO> getItemCountSelectionList() {
		return itemCountSelectionList;
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
