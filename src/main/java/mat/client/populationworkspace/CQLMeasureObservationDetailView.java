package mat.client.populationworkspace;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.*;
import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.populationworkspace.model.PopulationClauseObject;
import mat.client.populationworkspace.model.PopulationDataModel;
import mat.client.populationworkspace.model.PopulationDataModel.ExpressionObject;
import mat.client.populationworkspace.model.PopulationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;
import mat.shared.UUIDUtilClient;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import java.util.ArrayList;
import java.util.List;

public class CQLMeasureObservationDetailView implements CQLPopulationDetail {

	private CQLPopulationObserver observer;
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;

	private boolean isViewDirty = false;

	public CQLMeasureObservationDetailView(PopulationDataModel populationDataModel, String populationType) {

		setPopulationDataModel(populationDataModel);
		setPopulationsObject(populationDataModel.getMeasureObservationsObject());

		addMissingPopClauseIfEmptyList(populationType,populationsObject.getPopulationClauseObjectList());
	}

	private void addMissingPopClauseIfEmptyList(String populationType, List<PopulationClauseObject> popClauses) {
		if(popClauses.isEmpty()) {
			PopulationClauseObject popClause = new PopulationClauseObject(UUIDUtilClient.uuid());
			popClause.setType(populationType);
			String displayName = "Measure Observation" + " " + (1);
			popClause.setDisplayName(displayName);
			popClause.setSequenceNumber(1);
			popClauses.add(popClause);
		}
	}

	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(
				"Measure Observations", "Measure Observations", "Save", "Add New", 10.00);
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();
		Grid populationGrid = new Grid(popClauses.size(), 5);
		populationGrid.addStyleName("borderSpacing");

		for (int i = 0; i < popClauses.size(); i++) {
			populateGrid(popClauses, populationGrid, i);
		}

		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(event -> onAddNewClickHandler(populationGrid, populationsObject));
		cqlPopulationTopLevelButtonGroup.getSaveButton().addClickHandler(event -> onSavePopulationClickHandler(populationGrid, populationsObject));
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.add(populationGrid);
		verticalPanel.setWidth("700px");

		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());

		mainFlowPanel.add(cqlPopulationTopLevelButtonGroup.getAddNewButton());

		Grid headerGrid = new Grid(1, 2);

		SimplePanel aggFuncHeaderPanel = new SimplePanel();
		HTML aggFuncLabel = new HTML("<b><u>Aggregate Function</u></b>");
		aggFuncLabel.setTitle("Aggregate Function");
		aggFuncLabel.getElement().setAttribute("aria-label", "Aggregate Function");
		aggFuncLabel.getElement().setId("Aggregate_Function_Label");
		aggFuncLabel.getElement().setAttribute("style", "margin-left:120px;");
		aggFuncHeaderPanel.add(aggFuncLabel);

		headerGrid.getCellFormatter().setWidth(0, 0, "300px");
		headerGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		headerGrid.setWidget(0, 0, aggFuncHeaderPanel);

		SimplePanel funcHeaderPanel = new SimplePanel();
		HTML funcLabel = new HTML("<b><u>Function</u></b>");
		funcLabel.setTitle("Function");
		funcLabel.getElement().setAttribute("aria-label", "Function");
		funcLabel.getElement().setId("Function_Label");
		funcLabel.getElement().setAttribute("style", "margin-left:55px;");
		funcHeaderPanel.add(funcLabel);

		headerGrid.getCellFormatter().setWidth(0, 1, "200px");
		headerGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_CENTER);
		headerGrid.setWidget(0, 1, funcHeaderPanel);

		mainFlowPanel.add(headerGrid);

		mainFlowPanel.add(verticalPanel);
		mainFlowPanel.add(cqlPopulationTopLevelButtonGroup.getSaveButton());
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());
	}

	private void onAddNewClickHandler(Grid populationGrid, PopulationsObject populationsObject2) {
		isViewDirty = true;
		observer.onAddNewClick(populationGrid, populationsObject);
	}

	private void onSavePopulationClickHandler(Grid populationGrid, PopulationsObject populationsObject) {
		PopulationsObject newPopulationObject = buildPopulationsObjectFromGrid(populationGrid, populationsObject);
		observer.onSaveClick(newPopulationObject);
	}

	private PopulationsObject buildPopulationsObjectFromGrid(Grid populationGrid, PopulationsObject originalObject) {

		List<PopulationClauseObject> popClauses = new ArrayList<>();

		for(int i = 0; i<populationGrid.getRowCount(); i++) {
			PopulationClauseObject popClause = originalObject.getPopulationClauseObjectList().get(i);

			ListBox aggFunctionListBox = (ListBox)populationGrid.getWidget(i, 1);
			if(aggFunctionListBox.getSelectedValue().isEmpty()) {
				popClause.setAggFunctionName("");
			}else {
				popClause.setAggFunctionName(aggFunctionListBox.getSelectedValue());
			}


			ListBox functionListBox = (ListBox)populationGrid.getWidget(i, 2);
			if(functionListBox.getSelectedValue().isEmpty()) {
				popClause.setCqlExpressionDisplayName("");
				popClause.setCqlExpressionUUID("");
				popClause.setCqlExpressionType("");
			} else {
				popClause.setCqlExpressionDisplayName(getSelectedName(functionListBox.getSelectedValue()));
				popClause.setCqlExpressionUUID(functionListBox.getSelectedValue());
				popClause.setCqlExpressionType("cqlfunction");
			}

			popClauses.add(popClause);
		}

		originalObject.getPopulationClauseObjectList().clear();
		originalObject.getPopulationClauseObjectList().addAll(popClauses);

		return originalObject;
	}

	public void populateGrid(List<PopulationClauseObject> popClauses, Grid populationGrid, int i) {
		PopulationClauseObject populationClauseObject = popClauses.get(i);

		if (i == (populationGrid.getRowCount())) {
			populationGrid.resizeRows(i + 1);
		}

		// set the name of the Initial Population clause.
		FocusPanel nameFocusPanel = new FocusPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(populationClauseObject.getDisplayName());
		nameLabel.setTitle(populationClauseObject.getDisplayName());
		nameLabel.getElement().setAttribute("aria-label", populationClauseObject.getDisplayName());
		nameLabel.setId("nameLabel" + i);
		nameFocusPanel.add(nameLabel);

		populationGrid.setWidget(i, 0, nameFocusPanel);
		populationGrid.getCellFormatter().setWidth(i, 0, "230px");

		// set a listbox with all agg function names in it.
		ListBox aggFuncListBox = new ListBox();
		aggFuncListBox.setSize("140px", "30px");
		aggFuncListBox.addItem("--Select--", "");
		aggFuncListBox.setTitle("Select Aggregate Function");
		aggFuncListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

		// Add all Aggregate function names to aggFuncListBox
		PopulationWorkSpaceConstants.AggfuncNames.forEach(name -> aggFuncListBox.addItem(name, name));

		SelectElement selectAggElement = SelectElement.as(aggFuncListBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> options = selectAggElement.getOptions();
		for (int j = 0; j < options.getLength(); j++) {
			options.getItem(j).setTitle(options.getItem(j).getText());
		}

		// select a function aggregate name in the listbox
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
		functionListBox.addItem("--Select--", "");
		functionListBox.setTitle("Select Function List");
		functionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

		populationDataModel.getFunctionNameList().forEach(functionExpressionObject -> functionListBox
				.addItem(functionExpressionObject.getName(), functionExpressionObject.getUuid()));

		SelectElement selectFuncElement = SelectElement.as(functionListBox.getElement());
		com.google.gwt.dom.client.NodeList<OptionElement> funcOptions = selectFuncElement.getOptions();
		for (int j = 0; j < funcOptions.getLength(); j++) {
			funcOptions.getItem(j).setTitle(funcOptions.getItem(j).getText());
		}

		// select a definition name in the listbox
		for (int j = 0; j < functionListBox.getItemCount(); j++) {
			String functionUUID = functionListBox.getValue(j);
			if (functionUUID.equals(populationClauseObject.getCqlExpressionUUID())) {
				functionListBox.setItemSelected(j, true);
				break;
			}
		}

		populationGrid.setWidget(i, 2, functionListBox);

		Button deleteButton = new Button("Delete");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.addClickHandler(event -> addDeleteButtonEventHandler(event, populationGrid, populationClauseObject));

		deleteButton.setType(ButtonType.LINK);
		deleteButton.getElement().setId("deleteButton_" + populationClauseObject.getDisplayName());
		deleteButton.setTitle("Delete");
		deleteButton.getElement().setAttribute("aria-label",
				"Click this button to delete " + populationClauseObject.getDisplayName());
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");
		if (popClauses.size() == 1) {
			deleteButton.setEnabled(false);
		} else {
			deleteButton.setEnabled(true);
		}
		populationGrid.setWidget(i, 3, deleteButton);

		// button for View Human Readable
		Button viewHRButton = new Button("View");
		viewHRButton.setIcon(IconType.BINOCULARS);
		viewHRButton.addClickHandler(event -> viewHumanReadableEventHanddler(populationClauseObject, functionListBox, aggFuncListBox));
		viewHRButton.setType(ButtonType.LINK);
		viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
		viewHRButton.setTitle("View Human Readable");
		viewHRButton.getElement().setAttribute("aria-label",
				"Click this button to view Human Readable for " + populationClauseObject.getDisplayName());
		viewHRButton.setIcon(IconType.BINOCULARS);
		viewHRButton.setColor("black");
		populationGrid.setWidget(i, 4, viewHRButton);
		addChangeHandlerEvent(aggFuncListBox, functionListBox);

	}

	public void viewHumanReadableEventHanddler(PopulationClauseObject populationClauseObject, ListBox functionListBox, ListBox aggFuncListBox) {
		PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);

		population.setCqlExpressionType("cqlfunction");

		if (!functionListBox.getSelectedItemText().equals("--Select--")) {
			population.setCqlExpressionDisplayName(functionListBox.getSelectedItemText());
			population.setCqlExpressionUUID(functionListBox.getSelectedValue());
		} else {
			population.setCqlExpressionDisplayName("");
			population.setCqlExpressionUUID("");
		}

		if (!aggFuncListBox.getSelectedItemText().equals("--Select--")) {
			population.setAggFunctionName(aggFuncListBox.getSelectedItemText());
		} else {
			population.setAggFunctionName("");
		}

		observer.onViewHRClick(population);
	}

	private void addDeleteButtonEventHandler(ClickEvent event , Grid populationGrid, PopulationClauseObject populationClauseObject) {
		if (populationsObject.getPopulationClauseObjectList().size() == 1) {
			event.stopPropagation();
		} else {
			isViewDirty = true;
			observer.onDeleteClick(populationGrid, populationClauseObject);
		}
	}

	/**
	 * Add Change Event Handler to the List Boxes.
	 * @param aggFuncListBox
	 * @param functionListBox
	 */
	private void addChangeHandlerEvent(ListBox aggFuncListBox, ListBox functionListBox) {
		functionListBox.addChangeHandler(event -> onListBoxChange());
		aggFuncListBox.addChangeHandler(event -> onListBoxChange());
	}

	private void onListBoxChange() {
		observer.clearMessagesOnDropdown();
		setIsDirty(true);
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

	@Override
	public boolean isDirty() {
		return isViewDirty;
	}

	public void setIsDirty(boolean isViewDirty) {
		this.isViewDirty = isViewDirty;
	}

	private String getSelectedName(String selectedUuid) {
		for(ExpressionObject o : this.populationDataModel.getFunctionNameList()) {
			if(o.getUuid().equals(selectedUuid)) {
				return o.getName();
			}
		}

		return "";
	}
}
