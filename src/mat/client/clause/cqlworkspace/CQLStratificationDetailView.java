package mat.client.clause.cqlworkspace;


import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
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
import mat.client.clause.cqlworkspace.model.StrataDataModel;
import mat.client.clause.cqlworkspace.model.StratificationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;

public class CQLStratificationDetailView {
	
	public static interface Observer {
		void onDeleteStratum(PopulationClauseObject population); 
		void onDeleteStratification(StratificationsObject stratificationsObject);
		void onViewHRClick(PopulationClauseObject population); 
	}
	private Observer observer; 
	private StrataDataModel strataDataModel;
	private PopulationDataModel populationDataModel;
	
	private VerticalPanel mainPanel = new VerticalPanel();
	
	ScrollPanel scrollPanel = new ScrollPanel();
	
	CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup("Stratification" , "Stratification", "Save", "Add New Stratification");
	
	public PopulationDataModel getPopulationDataModel() {
		return populationDataModel;
	}

	public void setPopulationDataModel(PopulationDataModel populationDataModel) {
		this.populationDataModel = populationDataModel;
	}
	
	
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
		mainPanel.add(btnPanel);
		mainPanel.add(scrollPanel);
		buildStratificationView();
		return mainPanel;
	}
	
	private void buildStratificationView() {
		VerticalPanel mainVP = new VerticalPanel();
		//mainVP.setSize("700px", "250px");
		for(StratificationsObject stratificationsObject : strataDataModel.getStratificationObjectList()) {
			Grid parentGrid = generateStratificationGrid(stratificationsObject);
			mainVP.add(parentGrid);
			
			Grid stratumGrid = generateStratumGrid(stratificationsObject);
			mainVP.add(stratumGrid);
		}
		
		scrollPanel.add(mainVP);
		
	}
	
	
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
			nameLabel.setId("nameLabel" + i);
			nameLabel.setWidth("100px");
			nameFocusPanel.add(nameLabel);
			
			stratumsGrid.setWidget(i, 0, nameFocusPanel);
	
			// Set a listbox with all definition names in it.
			ListBox definitionListBox = new ListBox();
			definitionListBox.setSize("180px", "30px");			
			definitionListBox.addItem("--Select Definition--", "");
			definitionListBox.setTitle("Select Definition List");
			definitionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

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
		//	deleteButton.setSize("50px", "30px");
			deleteButton.getElement().setAttribute("aria-label", "Delete");
			deleteButton.setIcon(IconType.TRASH);
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");
			deleteButton.setMarginRight(100.00);
			
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
				}
			});

			stratumsGrid.setWidget(i, 2, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button();
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			viewHRButton.setText("View");
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);
			viewHRButton.setColor("black");
			viewHRButton.setMarginLeft(-100.00);			
			viewHRButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);
					population.setCqlExpressionDisplayName(definitionListBox.getSelectedItemText());
					population.setCqlExpressionUUID(definitionListBox.getSelectedValue());
					
					observer.onViewHRClick(population);
				}
			});
			

			stratumsGrid.setWidget(i, 3, viewHRButton);

		}
		return stratumsGrid;
	}

	private Grid generateStratificationGrid(StratificationsObject stratificationsObject) {
		Grid stratificationParentGrid = new Grid(1, 3);
		stratificationParentGrid.getElement().setId("grid_"+stratificationsObject.getDisplayName());
		//stratificationParentGrid.addStyleName("borderSpacing");
		stratificationParentGrid.getElement().setAttribute("style", "border-spacing:35px 10px;");
		FocusPanel nameFocusPanel = new FocusPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(stratificationsObject.getDisplayName());
		nameLabel.setTitle(stratificationsObject.getDisplayName());
		nameLabel.setId("nameLabel" + 1);
		nameFocusPanel.add(nameLabel);
		

		stratificationParentGrid.setWidget(0, 0, nameFocusPanel);
		stratificationParentGrid.getCellFormatter().setWidth(0, 0, "230px");
		
		// button for Add New Stratum
		Button addNewStratum = new Button();
		addNewStratum.setType(ButtonType.LINK);
		addNewStratum.getElement().setId("addNewStratumButton_" + stratificationsObject.getDisplayName());
		addNewStratum.setTitle("Click to add new stratum");
		addNewStratum.setText("Add Stratum");
		//addNewStratum.setSize("50px", "30px");
		addNewStratum.getElement().setAttribute("aria-label", "Add Stratum");
		addNewStratum.setIcon(IconType.PLUS);
		//addNewStratum.setIconSize(IconSize.LARGE);
		addNewStratum.setColor("#0964A2");
		addNewStratum.setMarginRight(150.00);
		addNewStratum.setMarginLeft(-105.00);
		
		addNewStratum.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

			}
		});

		stratificationParentGrid.setWidget(0, 1, addNewStratum);
		
		
		// button for Delete
		Button deleteButton = new Button();
		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_" + stratificationsObject.getDisplayName());
		deleteButton.setTitle("Delete");
		deleteButton.setText("Delete");
		//deleteButton.setSize("50px", "30px");
		deleteButton.getElement().setAttribute("aria-label", "Delete");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		deleteButton.setMarginLeft(-100.00);
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
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
	public Observer getObserver() {
		return observer;
	}

	/**
	 * Sets the observer.
	 *
	 * @param observer the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
}
