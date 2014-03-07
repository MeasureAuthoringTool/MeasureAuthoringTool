package mat.client.shared;

import java.util.ArrayList;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

public class CellListWithContextMenu {
	CellList<String> cellList;
	ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
	RangeLabelPager rangeLabelPager = new RangeLabelPager();
	private DisclosurePanel disclosurePanelItemCountTable = new DisclosurePanel("Add Item Count");
	private DisclosurePanel disclosurePanelAssociations = new DisclosurePanel("Add Associations");
	FlowPanel mainFlowPanel = new FlowPanel();
	/** The add qdm elements to measure. */
	private PrimaryButton saveGrouping = new PrimaryButton("Save Grouping", "primaryButton");
	/**
	 * @return the cellList
	 */
	public CellList<String> getCellList() {
		cellList = new CellList<String>(getSampleCompositeCell());
		final MultiSelectionModel<String> selectionModel = new MultiSelectionModel<String>();
		cellList.setPageSize(10);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
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
		dataProvider.addDataDisplay(cellList);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				System.out.println("selectionModel.getSelectedSet()" + selectionModel.getSelectedSet());
			}
		});
		cellList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<String> createDefaultManager());
		return cellList;
	}
	
	public Widget getWidget(){
		return mainFlowPanel;
	}
	
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
	
	private Widget buildItemCountWidget(){
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
		disclosurePanelItemCountTable.add(searchCriteriaPanel);
		disclosurePanelItemCountTable.setOpen(true);
		return disclosurePanelItemCountTable;
		
	}
	
	
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
		disclosurePanelAssociations.add(searchCriteriaPanel);
		disclosurePanelAssociations.setOpen(true);
		return disclosurePanelAssociations;
		
	}
	
	public CellListWithContextMenu() {
		pagerPanel.addStyleName("scrollable");
		pagerPanel.setDisplay(getCellList());
		rangeLabelPager.setDisplay(getCellList());
		Label qdmTabName = new Label("Measure Package Grouping");
		qdmTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(qdmTabName);
		mainFlowPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(pagerPanel);
		VerticalPanel vp = new VerticalPanel();
		vp.add(buildItemCountWidget());
		vp.add(buildAddAssociationWidget());
		hp.add(vp);
		mainFlowPanel.add(hp);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(saveGrouping);
		mainFlowPanel.setStylePrimaryName("valueSetSearchPanel");
	}
}
