package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.gwt.FlowPanel;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;

import mat.client.clause.clauseworkspace.presenter.PopulationWorkSpaceConstants;
import mat.client.clause.cqlworkspace.model.PopulationClauseObject;
import mat.client.clause.cqlworkspace.model.PopulationDataModel;
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;

public class CQLMeasureObservationDetailView implements CQLPopulationDetail {

	private CQLPopulationObserver observer;
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;

	private boolean isViewDirty = false;

	public CQLMeasureObservationDetailView(PopulationDataModel populationDataModel, String populationType) {

		setPopulationDataModel(populationDataModel);
		setPopulationsObject(populationDataModel.getMeasureObservationsObject());
	}

	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup(
				"Measure Observations", "Measure Observations", "Save", "Add New");
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		mainFlowPanel.clear();
		Grid populationGrid = new Grid(popClauses.size(), 5);
		populationGrid.addStyleName("borderSpacing");

		for (int i = 0; i < popClauses.size(); i++) {
			populateGrid(popClauses, populationGrid, i);
		}

		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				isViewDirty = true;
				observer.onAddNewClick(populationGrid, populationsObject);
			}
		});

		cqlPopulationTopLevelButtonGroup.getSaveButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isViewDirty = false;

			}
		});
		ScrollPanel scrollPanel = new ScrollPanel(populationGrid);
		scrollPanel.setSize("700px", "250px");

		mainFlowPanel.add(new SpacerWidget());

		HorizontalPanel btnPanel = new HorizontalPanel();
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.setStyleName("marginLeftButtons");

		btnPanel.add(cqlPopulationTopLevelButtonGroup.getButtonGroup());

		mainFlowPanel.add(btnPanel);

		Grid headerGrid = new Grid(1, 2);

		FocusPanel aggFuncHeaderPanel = new FocusPanel();
		HTML aggFuncLabel = new HTML("<b><u>Aggregate Function</u></b>");
		aggFuncLabel.setTitle("Aggregate Function");
		aggFuncLabel.getElement().setAttribute("aria-label", "Aggregate Function");
		aggFuncLabel.getElement().setId("Aggregate_Function_Label");
		aggFuncLabel.getElement().setAttribute("style", "margin-left:120px;");
		aggFuncHeaderPanel.add(aggFuncLabel);

		headerGrid.getCellFormatter().setWidth(0, 0, "300px");
		headerGrid.getCellFormatter().setHorizontalAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT);
		headerGrid.setWidget(0, 0, aggFuncHeaderPanel);

		FocusPanel funcHeaderPanel = new FocusPanel();
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

		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());
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
				if (popClauses.size() == 1) {
					event.stopPropagation();
				} else {
					isViewDirty = true;
					int rowToRemove = findRowForPopulationClause(populationGrid, populationClauseObject);
					observer.onDeleteClick(populationGrid, populationClauseObject, rowToRemove);
				}
			}
		});

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
		Button viewHRButton = new Button("View", IconType.BINOCULARS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

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
		});
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

	/**
	 * Add Change Event Handler to the List Boxes.
	 * @param aggFuncListBox
	 * @param functionListBox
	 */
	private void addChangeHandlerEvent(ListBox aggFuncListBox, ListBox functionListBox) {
		functionListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isViewDirty = true;

			}
		});

		aggFuncListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				isViewDirty = true;
			}
		});

	}

	/**
	 * This method returns the matching grid index based on populationClauseObject
	 * displayName and Grid zeroth column label's aria-label value.
	 * 
	 * @param populationGrid
	 * @param populationClauseObject
	 * @return integer rowIndex.
	 */
	private int findRowForPopulationClause(Grid populationGrid, PopulationClauseObject populationClauseObject) {
		int rowIndex = -1;
		for (int i = 0; i < populationGrid.getRowCount(); i++) {
			NodeList<Element> nodeList = populationGrid.getWidget(i, 0).getElement().getElementsByTagName("label");
			if (nodeList.getItem(0).getAttribute("aria-label")
					.equalsIgnoreCase(populationClauseObject.getDisplayName())) {
				rowIndex = i;
				break;
			}
		}
		return rowIndex;
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
}
