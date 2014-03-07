package mat.client.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mat.client.CustomPager;
import mat.client.measure.ManageMeasureSearchModel;
import mat.model.QualityDataSetDTO;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

// TODO: Auto-generated Javadoc
/**
 * The Class CellListWithContextMenu.
 */
public class CellListWithContextMenu {
	
	/** The cell list. */
	CellList<String> leftCellList;
	
	CellList<String> rightCellList;
	
	CellList<String> associatedCellList;
	
	/** The pager panel. */
	ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel();
	
	ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel();
	
	FlowPanel buttonPanel = new FlowPanel();
	
	/** The range label pager. */
	RangeLabelPager rightRangeLabelPager = new RangeLabelPager();
	
	RangeLabelPager leftRangeLabelPager = new RangeLabelPager();
	
	/** The disclosure panel item count table. */
	private DisclosurePanel disclosurePanelItemCountTable = new DisclosurePanel("Add/Edit Item Count");
	
	/** The disclosure panel associations. */
	private DisclosurePanel disclosurePanelAssociations = new DisclosurePanel("Add Associations");
	
	/** The main flow panel. */
	FlowPanel mainFlowPanel = new FlowPanel();
	/** The add qdm elements to measure. */
	private PrimaryButton saveGrouping = new PrimaryButton("Save Grouping", "primaryButton");
	
	/** The save itemcount list. */
	private SecondaryButton saveItemcountList = new SecondaryButton("Ok");
	
	/** The cancel itemcount list. */
	private SecondaryButton cancelItemcountList = new SecondaryButton("Cancel");
	
	/** The pagesize. */
	private final int PAGESIZE = 5;
	
	MultiSelectionModel<QualityDataSetDTO> itemCountSelection;
	
    private Button addQDMRight = buildAddButton("customAddRightButton");
	
	/** The add qdm left. */
	private Button addQDMLeft = buildAddButton("customAddLeftButton");
	
	/** The add all qdm right. */
	private Button addAllQDMRight = buildDoubleAddButton("customAddALlRightButton");
	
	/** The add all qdm left. */
	private Button addAllQDMLeft = buildDoubleAddButton("customAddAllLeftButton");
	
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
	
	/**
	 * Gets the cell list.
	 *
	 * @return the cellList
	 */
	public CellList<String> getRightCellList() {
		rightCellList = new CellList<String>(new TextCell());
		final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>();
		rightCellList.setPageSize(10);
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ArrayList<String> populationList = new ArrayList<String>();
		populationList.add("Initial Population 1");
		populationList.add("Initial Population 2");
		populationList.add("Initial Population 3");
		populationList.add("Measure Population 1");
		populationList.add("Measure Population 2");
		populationList.add("Measure Population Exclusions 1");
		populationList.add("Measure Observation 1");
		populationList.add("Measure Observation 2");
		populationList.add("Measure Observation 3");
		ListDataProvider<String> dataProvider = new ListDataProvider<String>(populationList);
		dataProvider.addDataDisplay(rightCellList);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("selectionModel.getSelectedSet()" + selectionModel.getSelectedSet());
//				addCellTable();
//				//getAssociatedCellList();
//				getButtonPanel();
//				disclosurePanelItemCountTable.setOpen(true);
//				disclosurePanelAssociations.setOpen(true);
			}
		});
		rightCellList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<String> createDefaultManager());
		return rightCellList;
	}
	
	public CellList<String> getLeftCellList() {
		//leftCellList = new CellList<String>(getSampleCompositeCell());
		leftCellList = new CellList<String>(new TextCell());
		final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>();
		leftCellList.setPageSize(10);
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ArrayList<String> populationList = new ArrayList<String>();
		populationList.add("Initial Population 1");
		populationList.add("Initial Population 2");
		populationList.add("Initial Population 3");
		populationList.add("Measure Population 1");
		populationList.add("Measure Population 2");
		populationList.add("Measure Population Exclusions 1");
		populationList.add("Measure Observation 1");
		populationList.add("Measure Observation 2");
		populationList.add("Measure Observation 3");
		ListDataProvider<String> dataProvider = new ListDataProvider<String>(populationList);
		dataProvider.addDataDisplay(leftCellList);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("selectionModel.getSelectedSet()" + selectionModel.getSelectedSet());
				addCellTable();
				//getAssociatedCellList();
				getButtonPanel();
				disclosurePanelItemCountTable.setOpen(true);
				disclosurePanelAssociations.setOpen(true);
			}
		});
		leftCellList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<String> createDefaultManager());
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
	public Cell getSampleCompositeCell()
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
			/*@Override
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
			}*/
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
		//disclosurePanelItemCountTable.add(buttonPanel);
		
		disclosurePanelItemCountTable.setOpen(false);
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
		disclosurePanelAssociations.add(associatedPanel);
		disclosurePanelAssociations.setOpen(false);
		return disclosurePanelAssociations;
		
	}
	
	/**
	 * Instantiates a new cell list with context menu.
	 */
	public CellListWithContextMenu() {
		addQDMButtonClickHandlers();
		leftPagerPanel.addStyleName("scrollable");
		leftPagerPanel.setDisplay(getLeftCellList());
		leftRangeLabelPager.setDisplay(getLeftCellList());
		rightPagerPanel.addStyleName("scrollable");
		rightPagerPanel.setDisplay(getRightCellList());
		rightRangeLabelPager.setDisplay(getRightCellList());
		Label qdmTabName = new Label("Measure Package Grouping");
		qdmTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(qdmTabName);
		mainFlowPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(leftPagerPanel);
		hp.add(buildQDMElementAddButtonWidget());
		hp.add(rightPagerPanel);
		VerticalPanel vp = new VerticalPanel();
		vp.add(buildItemCountWidget());
		vp.add(buildAddAssociationWidget());
		hp.add(vp);
		mainFlowPanel.add(hp);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(saveGrouping);
		mainFlowPanel.setStylePrimaryName("valueSetSearchPanel");
	}
	
	
	private void addQDMButtonClickHandlers() {
		
		saveItemcountList.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				List<QualityDataSetDTO> list= new ArrayList<CellListWithContextMenu.QualityDataSetDTO>();
				list.addAll(itemCountSelection.getSelectedSet());
				for(int i=0; i<itemCountSelection.getSelectedSet().size();i++){
					itemCountSelection.isSelected(list.get(0));
				}
			}
		});
		
		cancelItemcountList.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				
			}
		});
		
	}

	public Widget getButtonPanel(){
		buttonPanel.addStyleName("rightAlignButton");
		buttonPanel.add(saveItemcountList);
		buttonPanel.add(cancelItemcountList);
		
		return buttonPanel;
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

			MatCheckBoxCell rdBtnCell = new MatCheckBoxCell();
			Column<QualityDataSetDTO, Boolean> selectColumn = new Column<QualityDataSetDTO, Boolean>(rdBtnCell){

				@Override
				public Boolean getValue(QualityDataSetDTO object) {
					return itemCountSelection.isSelected(object);
				}};
			
				selectColumn.setFieldUpdater(new FieldUpdater<CellListWithContextMenu.QualityDataSetDTO, Boolean>() {
					
					@Override
					public void update(int index,
							mat.client.shared.CellListWithContextMenu.QualityDataSetDTO object,
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
	      panel.add(buttonPanel);
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
	
	private Widget buildQDMElementAddButtonWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("qdmElementAddButtonPanel");
		addQDMRight.setTitle("Add QDM element to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt","Add QDM element to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove QDM Element from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt","Remove QDM Element from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all QDM Elements to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt","Add all QDM Elements to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all QDM Elements from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt","Remove all QDM Elements from Supplemental Data Elements");
		panel.add(addQDMRight);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addQDMLeft);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addAllQDMRight);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addAllQDMLeft);
		return panel;
	}
}
