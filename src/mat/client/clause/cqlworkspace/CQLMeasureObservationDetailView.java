package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.shared.SpacerWidget;

public class CQLMeasureObservationDetailView implements CQLPopulationWorkSpaceView.CQLPopulationDetail{
	
	public static interface Observer {
		void onDeleteClick(String definitionName); 
		
		void onViewHRClick(PopulationClauseObject population); 
	}
	
	private Observer observer; 
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;
	
	public CQLMeasureObservationDetailView(PopulationDataModel populationDataModel, String populationType) {
		
		setPopulationDataModel(populationDataModel);
		setPopulationsObject(populationDataModel.getMeasureObservationsObject());		
	}
	
	@Override
	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();		
		Grid populationGrid = new Grid(popClauses.size(), 5);
		populationGrid.addStyleName("borderSpacing");

		for (int i = 0; i < popClauses.size(); i++) {

			PopulationClauseObject populationClauseObject = popClauses.get(i);
					
			// set the name of the Initial Population clause.
			FocusPanel nameFocusPanel = new FocusPanel();
			FormLabel nameLabel = new FormLabel();
			nameLabel.setText(populationClauseObject.getDisplayName());
			nameLabel.setTitle(populationClauseObject.getDisplayName());
			nameLabel.setId("nameLabel" + i);
			nameFocusPanel.add(nameLabel);

			populationGrid.setWidget(i, 0, nameFocusPanel);
			populationGrid.getCellFormatter().setWidth(i, 0, "230px");
			
			//set a listbox with all agg function names in it.
			ListBox aggFuncListBox = new ListBox();
			aggFuncListBox.setSize("140px", "30px");			
			aggFuncListBox.addItem("--Select--", "");
			aggFuncListBox.setTitle("Select Aggregate Function");
			aggFuncListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

			//TODO: hard-coding this for now. Need to get these values from Db.
			aggFuncListBox.addItem("Sum", "Sum");
			aggFuncListBox.addItem("Average", "Average");
			aggFuncListBox.addItem("Sample Standard Deviation", "Sample Standard Deviation");
			aggFuncListBox.addItem("Sample Variance", "Sample Variance");
			aggFuncListBox.addItem("Population Standard Deviation", "Population Standard Deviation");
			aggFuncListBox.addItem("Population Variance", "Population Variance");
			aggFuncListBox.addItem("Minimum", "Minimum");
			aggFuncListBox.addItem("Maximum", "Maximum");
			aggFuncListBox.addItem("Median", "Median");
			aggFuncListBox.addItem("Mode", "Mode");
			
			SelectElement selectAggElement = SelectElement.as(aggFuncListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectAggElement.getOptions();
		    for (int j = 0; j < options.getLength(); j++) {
		        options.getItem(j).setTitle(options.getItem(j).getText());
		    }
		    
		    // select a definition name in the listbox
 			for (int j = 0; j < aggFuncListBox.getItemCount(); j++) {
 				String functionName = aggFuncListBox.getItemText(j);
 				if (functionName.equals(populationClauseObject.getAggFunctionName())) {
 					aggFuncListBox.setItemSelected(j, true);
 					break;
 				}
 			}
			
			populationGrid.setWidget(i, 1, aggFuncListBox);

			// Set a listbox with all function names in it.
			ListBox functionListBox = new ListBox();
			functionListBox.setSize("140px", "30px");			
			functionListBox.addItem("--Select Function--", "");
			functionListBox.setTitle("Select Function List");
			functionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

			for (ExpressionObject function : populationDataModel.getFunctionNameList()) {
				functionListBox.addItem(function.getName(), function.getUuid());
			}
			
			SelectElement selectFuncElement = SelectElement.as(functionListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> funcOptions = selectFuncElement.getOptions();
		    for (int j = 0; j < funcOptions.getLength(); j++) {
		    	funcOptions.getItem(j).setTitle(funcOptions.getItem(j).getText());
		    }
			
			// select a definition name in the listbox
			for (int j = 0; j < functionListBox.getItemCount(); j++) {
				String functionName = functionListBox.getItemText(j);
				if (functionName.equals(populationClauseObject.getCqlExpressionDisplayName())) {
					functionListBox.setItemSelected(j, true);
					break;
				}
			}

			populationGrid.setWidget(i, 2, functionListBox);

			// button for Delete
			Button deleteButton = new Button();
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");
			deleteButton.setSize("20px", "30px");
			deleteButton.getElement().setAttribute("aria-label", "Delete");
			deleteButton.setIcon(IconType.TRASH);
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");
			
			deleteButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					observer.onDeleteClick(functionListBox.getSelectedItemText());
				}
			});

			populationGrid.setWidget(i, 3, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button();
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			viewHRButton.setSize("20px", "30px");
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);
			viewHRButton.setIconSize(IconSize.LARGE);
			viewHRButton.setColor("black");
						
			viewHRButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);
					population.setCqlExpressionDisplayName(functionListBox.getSelectedItemText());
					population.setCqlExpressionUUID(functionListBox.getSelectedValue());
					
					observer.onViewHRClick(population);
				}
			});
			

			populationGrid.setWidget(i, 4, viewHRButton);

		}

		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		scrollPanel.setSize("700px", "250px");

		mainFlowPanel.add(new SpacerWidget());
		
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.setStyleName("marginLeftButtons");		
		btnPanel.add(getAllButtons(populationsObject.getDisplayName(), populationsObject.getPopulationName()));
		
		mainFlowPanel.add(btnPanel);		
		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());
	}

	@Override
	public Button getSaveButton() {
		return null;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addButtonClicked() {
		// TODO Auto-generated method stub
		
	}
	
	public PopulationsObject getPopulationsObject() {
		return populationsObject;
	}

	public void setPopulationsObject(PopulationsObject populationObject) {
		this.populationsObject = populationObject;
	}
	
	public PopulationDataModel getPopulationDataModel() {
		return populationDataModel;
	}

	public void setPopulationDataModel(PopulationDataModel populationDataModel) {
		this.populationDataModel = populationDataModel;
	}

	private Button getNewButton(String displayName, String sectionName, String buttonName) {
		Button newBtn = new Button();
		newBtn.setType(ButtonType.LINK);
		newBtn.setText(buttonName);		
		if(buttonName.equals("Save")) {
			newBtn.setId("saveButton_" + sectionName);
			newBtn.setTitle("Click this button to save " + displayName);
			newBtn.setIcon(IconType.SAVE);
			newBtn.setPull(Pull.RIGHT);
			newBtn.setIconSize(IconSize.LARGE);
		}else {			
			newBtn.setId("addNewButton_" + sectionName);
			newBtn.setTitle("Click this button to add a new " + displayName.substring(0, displayName.length()-1));
			newBtn.setIcon(IconType.PLUS);
			newBtn.setPull(Pull.LEFT);
		}				
		newBtn.setSize(ButtonSize.SMALL);
		
		return newBtn;
	}
	
	private ButtonGroup getAllButtons(String displayName, String sectionName) {
		ButtonGroup btnGroup = new ButtonGroup();
		btnGroup.add(getNewButton(displayName, sectionName, "Add New"));
		btnGroup.add(getNewButton(displayName, sectionName, "Save"));
		btnGroup.getElement().setAttribute("class", "btn-group");
		return btnGroup;
	}
	
	@Override
	public void setObserver(mat.client.clause.cqlworkspace.CQLPopulationDetailView.Observer observer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public mat.client.clause.cqlworkspace.CQLPopulationDetailView.Observer getObserver() {
		// TODO Auto-generated method stub
		return null;
	}
}
