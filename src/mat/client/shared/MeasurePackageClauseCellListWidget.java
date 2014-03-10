package mat.client.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import mat.client.CustomPager;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
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

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackageClauseCellListWidget.
 */
public class MeasurePackageClauseCellListWidget {
	
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
	private SecondaryButton cancelItemcountList = new SecondaryButton("Cancel");
	/** The pagesize. */
	private final int PAGESIZE = 5;
	/**
	 * Item Count Table Selection Model. */
	private MultiSelectionModel<QualityDataSetDTO> itemCountSelection;
	/**
	 * Clauses Selection Model.	 */
	private MultiSelectionModel<MeasurePackageClauseDetail> leftCellListSelectionModel
	= new MultiSelectionModel<MeasurePackageClauseDetail>();
	/**
	 * Grouping Selection Model.	 */
	private MultiSelectionModel<MeasurePackageClauseDetail> rightCellListSelectionModel =
			new MultiSelectionModel<MeasurePackageClauseDetail>();
	/** The add Clause Right. */
	private Button addClauseRight = buildAddButton("customAddRightButton");
	/** The add Clause left. */
	private Button addClauseLeft = buildAddButton("customAddLeftButton");
	/** The add all Clause right. */
	private Button addAllClauseRight = buildDoubleAddButton("customAddALlRightButton");
	/** The add all Clause left. */
	private Button addAllClauseLeft = buildDoubleAddButton("customAddAllLeftButton");
	/**
	 * List Data Provider for Right(Package Clauses) cell List.
	 */
	private ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider;
	/**
	 * List Data Provider for Left(Clause) cell List.
	 */
	private ListDataProvider<MeasurePackageClauseDetail> leftCellListDataProvider;
	/** The Constant ITEMCOUNTLIST. */
	private static final List<QualityDataSetDTO> ITEMCOUNTLIST = Arrays.asList(
			new QualityDataSetDTO("AMI1 I9","Diagnosis, Active"),
			new QualityDataSetDTO("birth date","Patient Characteristic Birthdate"),
			new QualityDataSetDTO("Hemodialysis ","Procedure, Performed"),
			new QualityDataSetDTO("birth date","Patient Characteristic Birthdate"),
			new QualityDataSetDTO("birth date","Patient Characteristic Birthdate"),
			new QualityDataSetDTO("birth date","Patient Characteristic Birthdate"));
	
	/** The panel. */
	private VerticalPanel panel = new VerticalPanel();
	
	private ScrollPanel associatedPanel = new ScrollPanel();
	
	
	
	private ScrollPanel initialPopPanel2 = new ScrollPanel();
	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList = new ArrayList<MeasurePackageClauseDetail>();
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
	
	ArrayList<MeasurePackageClauseDetail> clausesPopulationList = new ArrayList<MeasurePackageClauseDetail>();
	
	/**
	 * Gets the cell list.
	 *
	 * @return the cellList
	 */
	public CellList<MeasurePackageClauseDetail> getRightCellList() {
		rightCellList = new CellList<MeasurePackageClauseDetail>(new ClauseCell());
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		
		rightCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(groupingPopulationList);
		rightCellListDataProvider.addDataDisplay(rightCellList);
		rightCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("selectionModel.getSelectedSet()" + rightCellListSelectionModel.getSelectedSet());
				addCellTable();
				//getAssociatedCellList();
				getItemCountTableButtonPanel();
				disclosurePanelItemCountTable.setOpen(true);
				disclosurePanelAssociations.setOpen(false);
				disclosurePanelItemCountTable.setVisible(true);
				disclosurePanelAssociations.setVisible(true);
				
			}
		});
		rightCellList.setSelectionModel(rightCellListSelectionModel, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		return rightCellList;
	}
	
	public CellList<MeasurePackageClauseDetail> getLeftCellList() {
		leftCellList = new CellList<MeasurePackageClauseDetail>(new ClauseCell());
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		leftCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(clausesPopulationList);
		leftCellListDataProvider.addDataDisplay(leftCellList);
		leftCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("selectionModel.getSelectedSet()" + leftCellListSelectionModel.getSelectedSet());
				disclosurePanelItemCountTable.setVisible(false);
				disclosurePanelAssociations.setVisible(false);
			}
		});
		leftCellList.setSelectionModel(leftCellListSelectionModel, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		return leftCellList;
	}
	
	//	public Widget getAssociatedCellList() {
	//		associatedPanel.clear();
	//		associatedCellList = new CellList<String>(getSampleCompositeCell());
	//		final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>();
	//		//associatedCellList.setPageSize(5);
	//		associatedCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
	//		ArrayList<String> populationList = new ArrayList<String>();
	//		populationList.add("Initial Population 1");
	//		populationList.add("Initial Population 2");
	//		populationList.add("Initial Population 3");
	//		populationList.add("Measure Population 1");
	//		populationList.add("Measure Population 2");
	//		populationList.add("Measure Population Exclusions 1");
	//		populationList.add("Measure Observation 1");
	//		populationList.add("Measure Observation 2");
	//		populationList.add("Measure Observation 3");
	//		ListDataProvider<String> dataProvider = new ListDataProvider<String>(populationList);
	//		dataProvider.addDataDisplay(associatedCellList);
	//		associatedPanel.setHeight("200px");
	//		associatedPanel.setAlwaysShowScrollBars(true);
	//		associatedPanel.add(associatedCellList);
	//		return associatedPanel;
	//	}
	
	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public Widget getWidget(){
		return mainFlowPanel;
	}
	
	/**
	 * Gets the sample composite cell.
	 *
	 * @return the sample composite cell
	 */
	/*public Cell getSampleCompositeCell()
	{
		ArrayList<HasCell<String, ?>> hasCells = new ArrayList<HasCell<String, ?>>();
		final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>();
		hasCells.add(new HasCell<String, Boolean>() {
			//private CheckboxCell cbCell = new CheckboxCell(true, false);
			
			private MatCheckBoxCell cbCell = new MatCheckBoxCell();
			@Override
			public Cell<Boolean> getCell() {
				return cbCell;
			}
			
			@Override
			public FieldUpdater<String, Boolean> getFieldUpdater() {
				return new FieldUpdater<String, Boolean>() {
					
					@Override
					public void update(int index, String object, Boolean value) {
						System.out.println("object ==== >" + object);
						System.out.println("Value ==== >" + value);
					}
					
				};
			}
			
			@Override
			public Boolean getValue(String object) {
				return selectionModel.isSelected(object);
			} });
		
		hasCells.add(new HasCell<String, String>() {
			private TextCell cell = new TextCell();
			@Override
			public Cell<String> getCell() {
				return cell;
			}
			
			@Override
			public FieldUpdater<String, String> getFieldUpdater() {
				return null;
			}
			
			@Override
			public String getValue(String object) {
				String value;
				return object;
			}});
		
		
		Cell<String> myClassCell = new CompositeCell<String>(hasCells) {
			@Override
			public Set getConsumedEvents() {
				return Collections.singleton("click");
			}
			
			@Override
			public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, String value,
					NativeEvent event, ValueUpdater<String> valueUpdater) {
				super.onBrowserEvent(context, parent, value, event, valueUpdater);
				event.preventDefault();
				event.stopPropagation();
				Window.alert(" right click on " + value);
			}
			@Override
			public void render(Context context, String value, SafeHtmlBuilder sb)
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
			protected <X> void render(Context context, String value, SafeHtmlBuilder sb, HasCell<String, X> hasCell)
			{
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</font></td>");
			}
			
		};
		
		return myClassCell;
	}*/
	
	
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
	private Widget buildAddAssociationWidget(){
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
		//		disclosurePanelAssociations.add(getLeftCellList());
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
		Label qdmTabName = new Label("Measure Package Grouping");
		qdmTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(qdmTabName);
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
		vp.add(buildItemCountWidget());
		vp.add(buildAddAssociationWidget());
		hp.add(vp);
		mainFlowPanel.add(hp);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(saveGrouping);
		mainFlowPanel.setStylePrimaryName("valueSetSearchPanel");
	}
	
	
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
	
	public Widget getItemCountTableButtonPanel(){
		itemCountButtonPanel.addStyleName("rightAlignButton");
		itemCountButtonPanel.add(saveItemcountList);
		itemCountButtonPanel.add(cancelItemcountList);
		
		return itemCountButtonPanel;
	}
	
	/**
	 * The Class QualityDataSetDTO.
	 */
	private static class QualityDataSetDTO {
		
		/** The data type. */
		private final String dataType;
		
		/** The name. */
		private final String name;
		
		/**
		 * Instantiates a new quality data set dto.
		 *
		 * @param name the name
		 * @param dataType the data type
		 */
		public QualityDataSetDTO(String name,String dataType) {
			this.name = name;
			this.dataType = dataType;
		}
	}
	
	/**
	 * Adds the cell table.
	 *
	 * @return the panel
	 */
	public Panel addCellTable(){
		panel.clear();
		CellTable<QualityDataSetDTO> table = new CellTable<QualityDataSetDTO>();
		itemCountSelection = new MultiSelectionModel<QualityDataSetDTO>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		table.setSelectionModel(itemCountSelection);
		
		MatCheckBoxCell chkBtnCell = new MatCheckBoxCell(false,true);
		Column<QualityDataSetDTO, Boolean> selectColumn = new Column<QualityDataSetDTO, Boolean>(chkBtnCell){
			
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				return itemCountSelection.isSelected(object);
			}};
			
			selectColumn.setFieldUpdater(new FieldUpdater<MeasurePackageClauseCellListWidget.QualityDataSetDTO, Boolean>() {
				
				@Override
				public void update(int index,
						mat.client.shared.MeasurePackageClauseCellListWidget.QualityDataSetDTO object,
						Boolean value) {
					
					itemCountSelection.setSelected(object, value);
				}
			});
			
			table.addColumn(selectColumn, SafeHtmlUtils.fromSafeConstant("<span title='Select'>" + "Select"
					+ "</span>"));
			
			TextColumn<QualityDataSetDTO> dateColumn = new TextColumn<QualityDataSetDTO>() {
				@Override
				public String getValue(QualityDataSetDTO object) {
					return object.name;
				}
			};
			table.addColumn(dateColumn, SafeHtmlUtils
					.fromSafeConstant("<span title='Name'>" + "Name"
							+ "</span>"));
			
			TextColumn<QualityDataSetDTO> addressColumn = new TextColumn<QualityDataSetDTO>() {
				@Override
				public String getValue(QualityDataSetDTO object) {
					return object.dataType;
				}
			};
			table.addColumn(addressColumn, SafeHtmlUtils.fromSafeConstant("<span title='Data Type'>" + "Data Type"
					+ "</span>"));
			ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
			table.setPageSize(PAGESIZE);
			table.setRowCount(ITEMCOUNTLIST.size(), true);
			table.setRowData(ITEMCOUNTLIST);
			sortProvider.refresh();
			sortProvider.getList().addAll(ITEMCOUNTLIST);
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
	 * @return
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
		return clauseButtonPanel;
	}
	
	static class ClauseCell implements Cell<MeasurePackageClauseDetail> {
		
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
		
		/**
		 * Create a singleton instance of the templates used to render the cell.
		 */
		private static Templates templates = GWT.create(Templates.class);
		
		
		
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			
			if(value.getName() != null) {
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
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
			// TODO Auto-generated method stub
			
		}
	}
}
