package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import mat.client.CustomPager;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
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
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;


/**
 * The Class MeasurePackageClauseCellListWidget.
 */
public class MeasurePackageClauseCellListWidget {
	/**
	 * The HTML templates used to render the cell.
	 */
	interface Templates extends SafeHtmlTemplates {
		/**
		 * The template for this Cell, which includes styles and a value.
		 * @param value the safe value. Since the value type is {@link SafeHtml},
		 *          it will not be escaped before including it in the template.
		 *          Alternatively, you could make the value type String, in which
		 *          case the value would be escaped.
		 * @return a {@link SafeHtml} instance
		 */
		@SafeHtmlTemplates.Template("<div style=\"margin-left:5px;\">{0}</div>")
		SafeHtml cell(SafeHtml value);
	}
	/** Create a singleton instance of the templates used to render the cell. */
	private static Templates templates = GWT.create(Templates.class);
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> leftCellList;
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> rightCellList;
	/** The cell list. */
	private CellList<String> associatedCellList;
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
	/** The cancel itemcount list. */
	//private SecondaryButton cancelItemcountList = new SecondaryButton("Cancel");
	/** The pagesize. */
	private final int PAGESIZE = 3;
	/** The add Clause Right. */
	private Button addClauseRight = buildAddButton("customAddRightButton");
	/** The add Clause left. */
	private Button addClauseLeft = buildAddButton("customAddLeftButton");
	/** The add all Clause right. */
	private Button addAllClauseRight = buildDoubleAddButton("customAddALlRightButton");
	/** The add all Clause left. */
	private Button addAllClauseLeft = buildDoubleAddButton("customAddAllLeftButton");
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
	private CellTable<QualityDataSetDTO> table;
	/** Applied QDM List.**/
	private List<QualityDataSetDTO> appliedQdmList;
	/** The panel. */
	private VerticalPanel panel = new VerticalPanel();
	/** List of Elements in Grouping List. */
	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Clause List.*/
	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/*** Gets the Grouping cell list.
	 * @return the cellList.	 */
	public CellList<MeasurePackageClauseDetail> getRightCellList() {
		rightCellList = new CellList<MeasurePackageClauseDetail>(new ClauseCell());
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		rightCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (rightCellListSelectionModel.getSelectedObject() == null) {
					return;
				}
				if(leftCellListSelectionModel.getSelectedObject() !=null) {
					leftCellListSelectionModel.clear();
				}
				System.out.println("selectionModel.getSelectedObject()" + rightCellListSelectionModel.getSelectedObject());
				MeasurePackageClauseDetail measurePackageClauseDetail = rightCellListSelectionModel.getSelectedObject();
				if (((measurePackageClauseDetail.getType().equals("denominator"))
						|| (measurePackageClauseDetail.getType().equals("numerator")))) {
					addCellTable();
					getItemCountTableButtonPanel();
					disclosurePanelItemCountTable.setVisible(true);
					disclosurePanelAssociations.setVisible(true);
					disclosurePanelItemCountTable.setOpen(false);
					disclosurePanelAssociations.setOpen(false);
				} else {
					addCellTable();
					getItemCountTableButtonPanel();
					disclosurePanelItemCountTable.setVisible(true);
					disclosurePanelAssociations.setVisible(false);
					disclosurePanelItemCountTable.setOpen(false);
					disclosurePanelAssociations.setOpen(false);
				}
			}
		});
		rightCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(groupingPopulationList);
		rightCellListDataProvider.addDataDisplay(rightCellList);
		rightCellList.setSelectionModel(rightCellListSelectionModel
				, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		return rightCellList;
	}
	/**
	 * Gets the Clause Cell List.
	 * @return CellList.
	 */
	public CellList<MeasurePackageClauseDetail> getLeftCellList() {
		leftCellList = new CellList<MeasurePackageClauseDetail>(new ClauseCell());
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		leftCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(clausesPopulationList);
		leftCellListDataProvider.addDataDisplay(leftCellList);
		leftCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (leftCellListSelectionModel.getSelectedObject() == null) {
					return;
				}
				if(rightCellListSelectionModel.getSelectedObject() !=null) {
					rightCellListSelectionModel.clear();
				}
				System.out.println("selectionModel.getSelectedSet()" + leftCellListSelectionModel.getSelectedSet());
				disclosurePanelItemCountTable.setVisible(false);
				disclosurePanelAssociations.setVisible(false);
			}
		});
		leftCellList.setSelectionModel(leftCellListSelectionModel
				, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		return leftCellList;
	}
	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public Widget getWidget(){
		return mainFlowPanel;
	}
	
	/**
	 * Builds the item count widget.
	 *
	 * @return the widget
	 */
	private Widget buildItemCountWidget(){
		panel.clear();
		disclosurePanelItemCountTable.clear();
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchCriteriaPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchHorizontalPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setAttribute("id", "ModifyButtonLayout");
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
		FlowPanel searchCriteriaPanel = new FlowPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchCriteriaPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.addStyleName("leftAligned");
		HorizontalPanel searchHorizontalPanel = new HorizontalPanel();
		searchCriteriaPanel.getElement().setAttribute("id", "ModifySearchHorizontalPanel");
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(searchHorizontalPanel);
		searchCriteriaPanel.add(new SpacerWidget());
		searchCriteriaPanel.add(new SpacerWidget());
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setAttribute("id", "ModifyButtonLayout");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		ScrollPanel associateListScrollPanel = new ScrollPanel();
		associateListScrollPanel.add(getLeftCellList());
		disclosurePanelAssociations.add(associateListScrollPanel);
		disclosurePanelAssociations.setOpen(false);
		disclosurePanelAssociations.setVisible(false);
		return disclosurePanelAssociations;
	}
	/**
	 * Instantiates a new cell list with context menu.
	 */
	public MeasurePackageClauseCellListWidget() {
		addClickHandlersToDisclosurePanels();
		leftPagerPanel.addStyleName("measurePackageCellListscrollable");
		leftPagerPanel.setDisplay(getLeftCellList());
		leftRangeLabelPager.setDisplay(getLeftCellList());
		rightPagerPanel.addStyleName("measurePackageCellListscrollable");
		rightPagerPanel.setDisplay(getRightCellList());
		rightRangeLabelPager.setDisplay(getRightCellList());
		Label packageTabName = new Label("Package Grouping");
		packageTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(packageTabName);
		mainFlowPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel leftCellListVPanel = new VerticalPanel();
		leftCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Clauses </b>"));
		leftCellListVPanel.add(leftPagerPanel);
		VerticalPanel rightCellListVPanel = new VerticalPanel();
		rightCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Package Grouping </b>"));
		rightCellListVPanel.add(rightPagerPanel);
		hp.add(leftCellListVPanel);
		hp.add(buildClauseAddButtonWidget());
		hp.add(rightCellListVPanel);
		VerticalPanel vp = new VerticalPanel();
		disclosurePanelItemCountTable.clear();
		disclosurePanelAssociations.clear();
		vp.add(buildItemCountWidget());
		vp.add(buildAddAssociationWidget());
		hp.add(vp);
		packageName.addStyleName("measureGroupPackageName");
		mainFlowPanel.add(packageName);
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
	 * Build Ok - Cancel Button Widget in Item Count Disclosure Panel.
	 * @return Widget.
	 */
	private Widget getItemCountTableButtonPanel() {
		itemCountButtonPanel.addStyleName("rightAlignButton");
		itemCountButtonPanel.add(saveItemcountList);
		//itemCountButtonPanel.add(cancelItemcountList);
		return itemCountButtonPanel;
	}
	
	
	public CellTable<QualityDataSetDTO> addColumntoTable(){
		MatCheckBoxCell chkBtnCell = new MatCheckBoxCell(false,true);
		Column<QualityDataSetDTO, Boolean> selectColumn = new Column<QualityDataSetDTO, Boolean>(chkBtnCell){
			
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				return itemCountSelection.isSelected(object);
			}};
			
			selectColumn.setFieldUpdater(new FieldUpdater<QualityDataSetDTO, Boolean>() {
				
				@Override
				public void update(int index,QualityDataSetDTO object,
						Boolean value) {
					
					itemCountSelection.setSelected(object, value);
				}
			});
			
			table.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select"
					+ "</span>"));
			
			TextColumn<QualityDataSetDTO> dateColumn = new TextColumn<QualityDataSetDTO>() {
				@Override
				public String getValue(QualityDataSetDTO object) {
					return object.getCodeListName();
				}
			};
			table.addColumn(dateColumn, SafeHtmlUtils
					.fromSafeConstant("<span title='Name'>" + "Name"
							+ "</span>"));
			
			TextColumn<QualityDataSetDTO> addressColumn = new TextColumn<QualityDataSetDTO>() {
				@Override
				public String getValue(QualityDataSetDTO object) {
					return object.getDataType();
				}
			};
			table.addColumn(addressColumn, SafeHtmlUtils.fromSafeConstant("<span title='Data Type'>" + "Data Type"
					+ "</span>"));
			
			return table;
			
	}
	
	/**
	 * Adds the cell table.
	 *
	 * @return the panel
	 */
	public Panel addCellTable(){
		panel.clear();
		table = new CellTable<QualityDataSetDTO>();
		itemCountSelection = new MultiSelectionModel<QualityDataSetDTO>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		table.setSelectionModel(itemCountSelection);
		ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
		table.setPageSize(PAGESIZE);
		table.setRowData(getAppliedQdmList());
		table.setRowCount(getAppliedQdmList().size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(getAppliedQdmList());
		table = addColumntoTable();
		sortProvider.addDataDisplay(table);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(table);
		spager.setPageSize(PAGESIZE);
		panel.setStylePrimaryName("valueSetSearchPanel");
		panel.add(table);
		panel.add(new SpacerWidget());
		panel.add(spager);
		panel.add(new SpacerWidget());
		panel.add(itemCountButtonPanel);
		return panel;
	}
	/**
	 * Adds the cell table.
	 *
	 * @return the panel
	 */
	
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url
	 * @return the button
	 */
	private Button buildAddButton(String imageUrl) {
		Button btn = new Button();
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Builds the double add button.
	 *
	 * @param imageUrl the image url
	 * @return the button
	 */
	private Button buildDoubleAddButton(String imageUrl) {
		Button btn = new Button();
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
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
	
	private void clauseButtonHandlers(){
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
	/**
	 * Clause Cell Class.
	 *
	 */
	class ClauseCell implements Cell<MeasurePackageClauseDetail> {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getName() != null) {
				// If the value comes from the user, we escape it to avoid XSS attacks.
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());
				SafeHtml rendered = templates.cell(safeValue);
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
	
	
	
}
