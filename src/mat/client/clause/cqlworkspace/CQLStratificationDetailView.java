package mat.client.clause.cqlworkspace;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.clause.cqlworkspace.model.StrataDataModel;
import mat.client.clause.cqlworkspace.model.StratificationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;

/**
 * Class CQLStratificationDetailView.
 *
 */
public class CQLStratificationDetailView implements CQLPopulationDetail{
	private VerticalPanel mainVP;
	private CQLPopulationObserver observer; 
	private StrataDataModel strataDataModel;
	private PopulationDataModel populationDataModel;
	
	private Map<String, Grid> parentToChildGridMap = new HashMap<String,Grid>();
	private List<Grid> parentGridList = new ArrayList<Grid>();
	
	private VerticalPanel mainPanel = new VerticalPanel();
	
	private ScrollPanel scrollPanel = new ScrollPanel();
	
	private CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(
			"Stratifications" , "Stratifications", "Save", "Add New Stratification");
	private boolean isDirty = false;
	
	/**
	 * This is a public method that is invoked to generate Stratification View for Stratification Left nav pill.
	 * @param populationDataModel
	 * @return Vertical Panel
	 */
	public VerticalPanel buildView(PopulationDataModel populationDataModel) {
		mainPanel.clear();
		scrollPanel.clear();
		scrollPanel.setSize("700px", "450px");
		this.populationDataModel = populationDataModel;
		this.strataDataModel = populationDataModel.getStrataDataModel();
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.getElement().setAttribute("style", "margin-left:400px;");
		btnPanel.add(cqlPopulationTopLevelButtonGroup.getButtonGroup());
		
		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				observer.onAddNewStratificationClick(strataDataModel);
			}
		});
		
		mainPanel.add(btnPanel);
		mainPanel.add(scrollPanel);
		buildStratificationView();
		return mainPanel;
	}
	
	/**
	 * Method to generate Stratification Grid with Stratum grid.
	 */
	private void buildStratificationView() {
		parentToChildGridMap.clear();
		parentGridList.clear();
		mainVP = new VerticalPanel();

		for(StratificationsObject stratificationsObject : strataDataModel.getStratificationObjectList()) {
			Grid parentGrid = generateStratificationGrid(stratificationsObject);
			mainVP.add(parentGrid);
			parentGridList.add(parentGrid);
			Grid stratumGrid = generateStratumGrid(stratificationsObject);
			parentToChildGridMap.put(stratificationsObject.getDisplayName(), stratumGrid);
			mainVP.add(stratumGrid);
		}
		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isDirty = true;
				observer.onAddNewStratificationClick(strataDataModel);
			}
		});
		
		cqlPopulationTopLevelButtonGroup.getSaveButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				isDirty = false;
				
			}
		});
		scrollPanel.add(mainVP);
		
	}
	
	
	/**
	 * This method generates Grid for all stratums.
	 * @param stratificationsObject
	 * @return Grid.
	 */
	private Grid generateStratumGrid(StratificationsObject stratificationsObject) {
		List<PopulationClauseObject> stratumClauses = stratificationsObject.getPopulationClauseObjectList();
		Grid stratumsGrid = new Grid(stratumClauses.size(), 4);
		stratumsGrid.getElement().setAttribute("style", "border-spacing:40px 10px;");
		for (int i = 0; i < stratumClauses.size(); i++) {

			PopulationClauseObject populationClauseObject = stratumClauses.get(i);
			FocusPanel nameFocusPanel = new FocusPanel();
			FormLabel nameLabel = new FormLabel();
			nameLabel.setText(populationClauseObject.getDisplayName());
			nameLabel.setTitle(populationClauseObject.getDisplayName());
			nameLabel.getElement().setAttribute("aria-label", populationClauseObject.getDisplayName());
			nameLabel.setId("nameLabel" + i);
			nameLabel.setWidth("100px");
			nameLabel.setMarginLeft(20.00);
			nameFocusPanel.add(nameLabel);
			
			stratumsGrid.setWidget(i, 0, nameFocusPanel);
	
			// Set a listbox with all definition names in it.
			ListBox definitionListBox = new ListBox();
			definitionListBox.setSize("180px", "30px");			
			definitionListBox.addItem("--Select Definition--", "");
			definitionListBox.setTitle("Select Definition List");
			definitionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());
			definitionListBox.addChangeHandler(new ChangeHandler() {
				
				@Override
				public void onChange(ChangeEvent event) {
					setIsDirty(true);
					
				}
			});

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
				String definitionName = definitionListBox.getItemText(j);
				if (definitionName.equals(populationClauseObject.getCqlExpressionDisplayName())) {
					definitionListBox.setItemSelected(j, true);
					break;
				}
			}

			stratumsGrid.setWidget(i, 1, definitionListBox);

			// button for Delete
			Button deleteButton = new Button();
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");
			deleteButton.setText("Delete");
			deleteButton.getElement().setAttribute("aria-label", "click this button to delete " +  populationClauseObject.getDisplayName());
			deleteButton.setIcon(IconType.TRASH);
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");
			deleteButton.setMarginRight(100.00);
			deleteButton.setMarginLeft(-20.00);
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					isDirty = true;
				}
			});

			stratumsGrid.setWidget(i, 2, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button();
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			viewHRButton.setText("View");
			viewHRButton.getElement().setAttribute("aria-label", "Click this button to view Human Readable for " + populationClauseObject.getDisplayName() );
			viewHRButton.setIcon(IconType.BINOCULARS);
			viewHRButton.setColor("black");
			viewHRButton.setMarginLeft(-100.00);			
			viewHRButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);
					
					if(!definitionListBox.getSelectedItemText().equals("--Select Definition--")) {
						population.setCqlExpressionDisplayName(definitionListBox.getSelectedItemText());
					}else {
						population.setCqlExpressionDisplayName("");
					}
	
					population.setCqlExpressionUUID(definitionListBox.getSelectedValue());
					
					observer.onViewHRClick(population);
				}
			});
			

			stratumsGrid.setWidget(i, 3, viewHRButton);

		}
		return stratumsGrid;
	}

	/**
	 * This method Generates Stratification Level Grid.
	 * @param stratificationsObject
	 * @return Grid.
	 */
	private Grid generateStratificationGrid(StratificationsObject stratificationsObject) {
		Grid stratificationParentGrid = new Grid(1, 3);
		stratificationParentGrid.getElement().setId("grid_"+stratificationsObject.getDisplayName());
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
		addNewStratum.getElement().setAttribute("aria-label", "Click this button Add Stratum under "+ stratificationsObject.getDisplayName());
		addNewStratum.setIcon(IconType.PLUS);
		addNewStratum.setColor("#0964A2");
		addNewStratum.setMarginRight(150.00);
		addNewStratum.setMarginLeft(-105.00);
		
		addNewStratum.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isDirty = true;
				observer.onAddNewStratumClick(stratificationsObject);
			}
		});

		stratificationParentGrid.setWidget(0, 1, addNewStratum);
		
		// button for Delete
		Button deleteButton = new Button();
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_" + stratificationsObject.getDisplayName());
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		deleteButton.getElement().setAttribute("aria-label", "Click this button to delete "+ stratificationsObject.getDisplayName() +" and stratums attached to it.");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		deleteButton.setMarginLeft(-100.00);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isDirty = true;
			}
		});

		stratificationParentGrid.setWidget(0, 2, deleteButton);
		
		return stratificationParentGrid;
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
	 * @param observer the new observer
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
	public void populateGrid(FlowPanel flowPanel, List<PopulationClauseObject> popClauses, Grid populationGrid, int i) {
		// TODO Auto-generated method stub
		
	}

	public void addStratificationGrid(StratificationsObject stratificationsObject) {
		Grid grid = generateStratificationGrid(stratificationsObject);
		mainVP.add(grid);
		parentGridList.add(grid);
	}

	public void addStratumGrid(StratificationsObject stratificationsObject) {
		// TODO Auto-generated method stub
		Grid existingStratumsGrid = parentToChildGridMap.get(stratificationsObject.getDisplayName());
		mainVP.remove(existingStratumsGrid);
		Grid newStratumsGrid = generateStratumGrid(stratificationsObject);
		mainVP.add(newStratumsGrid);
		parentToChildGridMap.put(stratificationsObject.getDisplayName(), newStratumsGrid);
	}
	
}
