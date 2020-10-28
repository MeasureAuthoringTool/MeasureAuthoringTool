package mat.client.populationworkspace;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import mat.client.populationworkspace.model.PopulationClauseObject;
import mat.client.populationworkspace.model.PopulationDataModel;
import mat.client.populationworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.populationworkspace.model.PopulationsObject;
import mat.client.populationworkspace.model.StrataDataModel;
import mat.client.populationworkspace.model.StratificationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class CQLStratificationDetailView.
 *
 */
public class CQLStratificationDetailView implements CQLPopulationDetail {
	
	private VerticalPanel stratificationPanel;
	private CQLPopulationObserver observer;
	private StrataDataModel strataDataModel;
	private PopulationDataModel populationDataModel;

	private Map<String, Grid> parentToChildGridMap = new HashMap<String, Grid>();
	private List<Grid> parentGridList = new ArrayList<Grid>();

	private VerticalPanel mainPanel = new VerticalPanel();

	private ScrollPanel scrollPanel = new ScrollPanel();
	private boolean isDirty = false;

	/**
	 * This is a public method that is invoked to generate Stratification View for
	 * Stratification Left nav pill.
	 * 
	 * @param populationDataModel
	 * @return Vertical Panel
	 */
	public VerticalPanel buildView(PopulationDataModel populationDataModel) {
		mainPanel.clear();
		mainPanel.setHeight("250px");
		
		
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(
				"Stratifications", "Stratifications", "Save", "Add New Stratification", 100.00);
		scrollPanel.clear();
		scrollPanel.setWidth("700px");
		this.populationDataModel = populationDataModel;
		this.strataDataModel = populationDataModel.getStrataDataModel();
		
		mainPanel.add(new SpacerWidget());
		mainPanel.add(new SpacerWidget());

		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(event -> onAddNewStratificationClickHander(strataDataModel));
		cqlPopulationTopLevelButtonGroup.getSaveButton().addClickHandler(event -> onSaveStratificationClickHander(strataDataModel));
		
		mainPanel.add(cqlPopulationTopLevelButtonGroup.getAddNewButton());
		mainPanel.add(scrollPanel);
		mainPanel.add(cqlPopulationTopLevelButtonGroup.getSaveButton());
		buildStratificationView();
		return mainPanel;
	}

	private void onAddNewStratificationClickHander(StrataDataModel strataDataModel) {
		isDirty = true;
		observer.onAddNewStratificationClick(strataDataModel);
	}

	private void onSaveStratificationClickHander(StrataDataModel strataDataModel) {
		observer.onSaveClick(prepareModelForSave(strataDataModel));
	}

	/**
	 * Method to generate Stratification Grid with Stratum grid.
	 */
	private void buildStratificationView() {
		parentToChildGridMap.clear();
		parentGridList.clear();
		stratificationPanel = new VerticalPanel();

		for (StratificationsObject stratificationsObject : strataDataModel.getStratificationObjectList()) {
			Grid parentGrid = generateStratificationGrid(stratificationsObject);
			stratificationPanel.add(parentGrid);
			parentGridList.add(parentGrid);
			Grid stratumGrid = generateStratumGrid(stratificationsObject);
			parentToChildGridMap.put(stratificationsObject.getDisplayName(), stratumGrid);
			stratificationPanel.add(stratumGrid);
		}

		scrollPanel.add(stratificationPanel);

	}

	/**
	 * This method generates Grid for all stratums.
	 * 
	 * @param stratificationsObject
	 * @return Grid.
	 */
	private Grid generateStratumGrid(StratificationsObject stratificationsObject) {
		List<PopulationClauseObject> stratumClauses = stratificationsObject.getPopulationClauseObjectList();
		Grid stratumsGrid = new Grid(stratumClauses.size(), 4);
		stratumsGrid.getElement().setAttribute("style", "border-spacing:40px 10px;");
		for (int i = 0; i < stratumClauses.size(); i++) {
			buildStratumGrid(stratificationsObject, stratumsGrid, i);
		}
		return stratumsGrid;
	}



	private void buildStratumGrid(StratificationsObject stratificationsObject, Grid stratumsGrid, int i) {
		PopulationClauseObject populationClauseObject = stratificationsObject.getPopulationClauseObjectList().get(i);
		FocusPanel nameFocusPanel = new FocusPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(populationClauseObject.getDisplayName());
		nameLabel.setTitle(populationClauseObject.getDisplayName());
		nameLabel.getElement().setAttribute("aria-label", populationClauseObject.getDisplayName());
		nameLabel.setId("nameLabel" + stratificationsObject.getSequenceNumber() + "_" + i);
		nameLabel.setWidth("100px");
		nameLabel.setMarginLeft(20.00);
		nameFocusPanel.add(nameLabel);

		stratumsGrid.setWidget(i, 0, nameFocusPanel);

		// Set a listbox with all definition names in it.
		ListBox definitionListBox = new ListBox();
		definitionListBox.setSize("180px", "30px");
		definitionListBox.addItem("--Select Definition--", "");
		definitionListBox.setTitle("Select Definition List");
		definitionListBox.setId("definitionList_" + stratificationsObject.getSequenceNumber() + "_" + populationClauseObject.getDisplayName());

		for (ExpressionObject definition : populationDataModel.getDefinitionNameList()) {
			definitionListBox.addItem(definition.getName(), definition.getUuid());
		}

		SelectElement selectElement = SelectElement.as(definitionListBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
		for (int j = 0; j < options.getLength(); j++) {
			options.getItem(j).setTitle(options.getItem(j).getText());
		}

		// select a definition name in the listbox
		for (int j = 0; j < definitionListBox.getItemCount(); j++) {
			String definitionUUID = definitionListBox.getValue(j);
			if (definitionUUID.equals(populationClauseObject.getCqlExpressionUUID())) {
				definitionListBox.setItemSelected(j, true);
				break;
			}
		}

		definitionListBox.addChangeHandler(event -> onDefinitionListBoxChange());
		stratumsGrid.setWidget(i, 1, definitionListBox);

		// button for Delete
		Button deleteButton = new Button();
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_" + stratificationsObject.getSequenceNumber() + "_" + populationClauseObject.getDisplayName());
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		deleteButton.getElement().setAttribute("aria-label",
				"click this button to delete " + populationClauseObject.getDisplayName());
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		deleteButton.setMarginRight(100.00);
		deleteButton.setMarginLeft(-20.00);

		if(stratificationsObject.getPopulationClauseObjectList().size() <= 1) {
			deleteButton.setEnabled(false); 
		}

		deleteButton.addClickHandler(event -> deleteStratumButtonEventHandler(event, stratumsGrid, stratificationsObject, populationClauseObject));

		stratumsGrid.setWidget(i, 2, deleteButton);

		// button for View Human Readable
		Button viewHRButton = new Button();
		viewHRButton.setType(ButtonType.LINK);
		viewHRButton.getElement().setId("viewHRButton_" + stratificationsObject.getSequenceNumber() + "_" + populationClauseObject.getDisplayName());
		viewHRButton.setTitle("View Human Readable");
		viewHRButton.setText("View");
		viewHRButton.getElement().setAttribute("aria-label",
				"Click this button to view Human Readable for " + populationClauseObject.getDisplayName());
		viewHRButton.setIcon(IconType.BINOCULARS);
		viewHRButton.setColor("black");
		viewHRButton.setMarginLeft(-100.00);

		viewHRButton.addClickHandler(event -> viewHumanReadableStratumEventHandler(definitionListBox, populationClauseObject)); 

		stratumsGrid.setWidget(i, 3, viewHRButton);
	}
	
	private void onDefinitionListBoxChange() {
		observer.clearMessagesOnDropdown();
		setIsDirty(true);
	}

	private void viewHumanReadableStratumEventHandler(ListBox definitionListBox, PopulationClauseObject populationClauseObject) {
		PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);

		if (!definitionListBox.getSelectedItemText().equals("--Select Definition--")) {
			population.setCqlExpressionDisplayName(definitionListBox.getSelectedItemText());
			population.setCqlExpressionType("cqldefinition");
		} else {
			population.setCqlExpressionDisplayName("");
		}

		population.setCqlExpressionUUID(definitionListBox.getSelectedValue());

		observer.onViewHRClick(population);
	}

	private void deleteStratumButtonEventHandler(ClickEvent event , Grid stratumsGrid, StratificationsObject stratificationsObject , PopulationClauseObject populationClauseObject) {
		if(stratificationsObject.getPopulationClauseObjectList().size() <= 1) {
			event.stopPropagation(); 
		} else {
			isDirty = true;
			observer.onDeleteStratumClick(stratumsGrid, stratificationsObject, populationClauseObject);
		}
	}
	/**
	 * This method Generates Stratification Level Grid.
	 * 
	 * @param stratificationsObject
	 * @return Grid.
	 */
	private Grid generateStratificationGrid(StratificationsObject stratificationsObject) {
		Grid stratificationParentGrid = new Grid(1, 3);
		stratificationParentGrid.getElement().setId("grid_" + stratificationsObject.getDisplayName());
		stratificationParentGrid.getElement().setAttribute("style", "border-spacing:35px 10px;");
		FocusPanel nameFocusPanel = new FocusPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(stratificationsObject.getDisplayName());
		nameLabel.setTitle(stratificationsObject.getDisplayName());
		nameLabel.getElement().setAttribute("aria-label", stratificationsObject.getDisplayName());
		nameLabel.setId("nameLabel" + 1);
		nameFocusPanel.add(nameLabel);

		stratificationParentGrid.setWidget(0, 0, nameFocusPanel);
		stratificationParentGrid.getCellFormatter().setWidth(0, 0, "230px");

		// button for Add New Stratum
		Button addNewStratum = new Button();
		addNewStratum.setType(ButtonType.LINK);
		addNewStratum.getElement().setId("addNewStratumButton_" + stratificationsObject.getDisplayName());
		addNewStratum.setTitle("Click this button to add new stratum");
		addNewStratum.setText("Add Stratum");
		addNewStratum.getElement().setAttribute("aria-label",
				"Click this button Add Stratum under " + stratificationsObject.getDisplayName());
		addNewStratum.setIcon(IconType.PLUS);
		addNewStratum.setColor("#0964A2");
		addNewStratum.setMarginRight(150.00);
		addNewStratum.setMarginLeft(-105.00);
		addNewStratum.addClickHandler(event -> onAddNewStratumClickHandler(stratificationsObject));

		stratificationParentGrid.setWidget(0, 1, addNewStratum);

		// button for Delete
		Button deleteButton = new Button();
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_" + stratificationsObject.getDisplayName());
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		deleteButton.getElement().setAttribute("aria-label", "Click this button to delete "
				+ stratificationsObject.getDisplayName() + " and stratums attached to it.");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		deleteButton.setMarginLeft(-100.00);

		// disable delete button if there is only one left after delete
		if(strataDataModel.getStratificationObjectList().size() <= 1) {
			deleteButton.setEnabled(false);
		}

		deleteButton.addClickHandler(event -> deleteStratificationButtonEventHandler(event, stratificationParentGrid, stratificationsObject));

		stratificationParentGrid.setWidget(0, 2, deleteButton);

		return stratificationParentGrid;
	}

	private void onAddNewStratumClickHandler(StratificationsObject stratificationsObject) {
		isDirty = true;
		observer.onAddNewStratumClick(stratificationsObject);
	}

	private void deleteStratificationButtonEventHandler(ClickEvent event , Grid stratificationParentGrid, StratificationsObject stratificationsObject) {
		if (strataDataModel.getStratificationObjectList().size() <= 1) {
			event.stopPropagation();
		} else {
			isDirty = true;
			observer.onDeleteStratificationClick(stratificationParentGrid, stratificationsObject);
		}
	}

	/**
	 * Gets the observer.
	 *
	 * @return the observer
	 */
	public CQLPopulationObserver getObserver() {
		return observer;
	}

	/**
	 * Sets the observer.
	 *
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(CQLPopulationObserver observer) {
		this.observer = observer;
	}

	public PopulationDataModel getPopulationDataModel() {
		return populationDataModel;
	}

	public void setPopulationDataModel(PopulationDataModel populationDataModel) {
		this.populationDataModel = populationDataModel;
	}

	public Map<String, Grid> getParentToChildGridMap() {
		return parentToChildGridMap;
	}

	public void setParentToChildGridMap(Map<String, Grid> parentToChildGridMap) {
		this.parentToChildGridMap = parentToChildGridMap;
	}

	public List<Grid> getParentGridList() {
		return parentGridList;
	}

	public void setParentGridList(List<Grid> parentGridList) {
		this.parentGridList = parentGridList;
	}

	@Override
	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		// TODO Auto-generated method stub

	}

	@Override
	public PopulationsObject getPopulationsObject() {
		// TODO Auto-generated method stub
		return null;
	}

	public VerticalPanel getStratificationPanel() {
		return stratificationPanel;
	}

	public void setStratificationPanel(VerticalPanel stratificationPanel) {
		this.stratificationPanel = stratificationPanel;
	}

	public StrataDataModel getStrataDataModel() {
		return strataDataModel;
	}

	public void setStrataDataModel(StrataDataModel strataDataModel) {
		this.strataDataModel = strataDataModel;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return isDirty;
	}

	@Override
	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;

	}

	@Override
	public void populateGrid(List<PopulationClauseObject> popClauses, Grid populationGrid, int i) {
		// TODO Auto-generated method stub

	}

	public void addStratificationGrid(StratificationsObject stratificationsObject) {
		Grid grid = generateStratificationGrid(stratificationsObject);
		stratificationPanel.add(grid);
		parentGridList.add(grid);
	}

	public void addStratumGrid(StratificationsObject stratificationsObject) {
		Grid existingStratumsGrid = parentToChildGridMap.get(stratificationsObject.getDisplayName());
		if (existingStratumsGrid != null) {
			// resize the grid
			int newRowCount = existingStratumsGrid.getRowCount() + 1;
			existingStratumsGrid.resizeRows(newRowCount);
			buildStratumGrid(stratificationsObject, existingStratumsGrid, newRowCount - 1);
		} else {
			Grid newStratumsGrid = generateStratumGrid(stratificationsObject);
			stratificationPanel.add(newStratumsGrid);
			parentToChildGridMap.put(stratificationsObject.getDisplayName(), newStratumsGrid);
		}
	}

	private StrataDataModel prepareModelForSave(StrataDataModel strataDataModel) {
		List<StratificationsObject> modifiedList = new ArrayList<>();

		for(StratificationsObject stratificationsObject: strataDataModel.getStratificationObjectList()) {

			List<PopulationClauseObject> modifiedStratumList = new ArrayList<>();
			List<PopulationClauseObject> stratumClauses = stratificationsObject.getPopulationClauseObjectList();

			int size = stratumClauses.size();
			//Get Grid for Stratification
			for(int row=0; row < size; row++) {

				PopulationClauseObject pc = stratumClauses.get(row);

				Grid stratum = parentToChildGridMap.get(stratificationsObject.getDisplayName());
				ListBox l =  (ListBox) stratum.getWidget(row, 1);

				if ("--Select Definition--".equals(l.getSelectedItemText())){						
					pc.setCqlExpressionType("");
					pc.setCqlExpressionDisplayName("");
					pc.setCqlExpressionUUID("");					
				} else {					
					pc.setCqlExpressionType("cqldefinition");
					pc.setCqlExpressionDisplayName(getSelectedName(l.getSelectedValue()));
					pc.setCqlExpressionUUID(l.getSelectedValue());
				}
				modifiedStratumList.add(pc);
			}

			stratificationsObject.getPopulationClauseObjectList().clear();
			stratificationsObject.getPopulationClauseObjectList().addAll(modifiedStratumList);

			modifiedList.add(stratificationsObject);

		}

		strataDataModel.getStratificationObjectList().clear();
		strataDataModel.getStratificationObjectList().addAll(modifiedList);

		return strataDataModel;		
	}
	
	private String getSelectedName(String selectedUuid) {
		for(ExpressionObject o : this.populationDataModel.getDefinitionNameList()) {
			if(o.getUuid().equals(selectedUuid)) {
				return o.getName();
			}
		}
				
		return "";
	}

}
