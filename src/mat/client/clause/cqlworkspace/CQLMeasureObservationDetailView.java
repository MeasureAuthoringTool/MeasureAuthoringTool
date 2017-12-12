package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
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

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;

public class CQLMeasureObservationDetailView implements CQLPopulationDetail{
	
	private CQLPopulationObserver observer; 
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;
	
	public CQLMeasureObservationDetailView(PopulationDataModel populationDataModel, String populationType) {
		
		setPopulationDataModel(populationDataModel);
		setPopulationsObject(populationDataModel.getMeasureObservationsObject());		
	}
	
	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();		
		Grid populationGrid = new Grid(popClauses.size(), 5);
		populationGrid.addStyleName("borderSpacing");
		
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(
				populationsObject.getPopulationName() , populationsObject.getDisplayName(), "Save", "Add New");
		
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


			//Add all Aggregate function names to aggFuncListBox 
			PopulationWorkSpaceConstants.AggfuncNames.forEach(name -> aggFuncListBox.addItem(name, name));
			
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

			populationDataModel.getFunctionNameList()
							.forEach(
										functionExpressionObject -> functionListBox.addItem(functionExpressionObject.getName(), functionExpressionObject.getUuid())
									);
			
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
			Button deleteButton = new Button("Delete", IconType.TRASH, new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					observer.onDeleteClick(functionListBox.getSelectedItemText());
				}
			} );
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");			
			deleteButton.getElement().setAttribute("aria-label", "Delete");			
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");
			
			populationGrid.setWidget(i, 3, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button("View", IconType.BINOCULARS, new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					
					PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);
					
					if(!functionListBox.getSelectedItemText().equals("--Select Function--")) {
						population.setCqlExpressionDisplayName(functionListBox.getSelectedItemText());
					}else {
						population.setCqlExpressionDisplayName("");
					}
					
					if(!aggFuncListBox.getSelectedItemText().equals("--Select--")) {
						population.setAggFunctionName(aggFuncListBox.getSelectedItemText());
					}else {
						population.setAggFunctionName("");
					}
					
					population.setCqlExpressionUUID(functionListBox.getSelectedValue());
					
					observer.onViewHRClick(population);
				}
			});
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");
			viewHRButton.setSize("20px", "30px");
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);			
			viewHRButton.setColor("black");
						
			populationGrid.setWidget(i, 4, viewHRButton);

		}

		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		scrollPanel.setSize("700px", "250px");

		mainFlowPanel.add(new SpacerWidget());
		
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.setStyleName("marginLeftButtons");		
		
		btnPanel.add(cqlPopulationTopLevelButtonGroup.getButtonGroup());
		
		mainFlowPanel.add(btnPanel);		
		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());
	}

	
	public Button getSaveButton() {
		return null;
		// TODO Auto-generated method stub
		
	}

	
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
	
	public void setObserver(CQLPopulationObserver observer) {
		this.observer = observer;
		
	}

	public CQLPopulationObserver getObserver() {
		return this.observer;
	}
}
