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

import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;

public class CQLPopulationDetailView implements CQLPopulationDetail{
	
	private CQLPopulationObserver observer; 
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;
	
	public CQLPopulationDetailView(PopulationDataModel populationDataModel) {
		setPopulationDataModel(populationDataModel);
	}
	
	@Override
	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();		
		Grid populationGrid = new Grid(popClauses.size(), 4);
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

			populationGrid.setWidget(i, 1, definitionListBox);

			// button for Delete
			Button deleteButton = new Button("Delete", IconType.TRASH, new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					observer.onDeleteClick(definitionListBox.getSelectedItemText());
				}
			});
			deleteButton.setType(ButtonType.LINK);
			deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
			deleteButton.setTitle("Delete");			
			deleteButton.getElement().setAttribute("aria-label", "Delete");			
			deleteButton.setIconSize(IconSize.LARGE);
			deleteButton.setColor("#0964A2");

			populationGrid.setWidget(i, 2, deleteButton);

			// button for View Human Readable
			Button viewHRButton = new Button("View", IconType.BINOCULARS, new ClickHandler() {

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
			viewHRButton.setType(ButtonType.LINK);
			viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
			viewHRButton.setTitle("View Human Readable");			
			viewHRButton.getElement().setAttribute("aria-label", "View Human Readable");
			viewHRButton.setIcon(IconType.BINOCULARS);			
			viewHRButton.setColor("black");

			populationGrid.setWidget(i, 3, viewHRButton);

		}

		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		scrollPanel.setSize("700px", "250px");

		mainFlowPanel.add(new SpacerWidget());
		
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.setStyleName("marginLeftButtons");	
		
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(populationsObject.getDisplayName() , populationsObject.getPopulationName(), "Save", "Add New");
		btnPanel.add(cqlPopulationTopLevelButtonGroup.getButtonGroup());
		
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
}
