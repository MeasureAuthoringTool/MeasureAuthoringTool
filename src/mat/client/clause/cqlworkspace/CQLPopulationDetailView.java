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
import mat.client.clause.cqlworkspace.model.PopulationsObject;
import mat.client.shared.CQLPopulationTopLevelButtonGroup;
import mat.client.shared.SpacerWidget;

public class CQLPopulationDetailView implements CQLPopulationDetail {

	private CQLPopulationObserver observer;
	private PopulationsObject populationsObject;
	private PopulationDataModel populationDataModel;
	boolean isViewDirty = false;

	public CQLPopulationDetailView(PopulationDataModel populationDataModel) {
		setPopulationDataModel(populationDataModel);
	}

	@Override
	public void displayPopulationDetail(FlowPanel mainFlowPanel) {
		mainFlowPanel.clear();
		CQLPopulationTopLevelButtonGroup cqlPopulationTopLevelButtonGroup = new CQLPopulationTopLevelButtonGroup("", "",
				"Save", "Add New");
		List<PopulationClauseObject> popClauses = populationsObject.getPopulationClauseObjectList();
		cqlPopulationTopLevelButtonGroup.getAddNewButton()
				.setId("addNewButton_" + populationsObject.getPopulationType());
		cqlPopulationTopLevelButtonGroup.getAddNewButton()
				.setTitle("Click this button to add a new " + populationsObject.getPopulationType());
		cqlPopulationTopLevelButtonGroup.getSaveButton().setId("saveButton_" + populationsObject.getPopulationType());
		cqlPopulationTopLevelButtonGroup.getSaveButton()
				.setTitle("Click this button to save " + populationsObject.getPopulationType() + "s");

		Grid populationGrid = new Grid(popClauses.size(), 4);
		populationGrid.addStyleName("borderSpacing");

		for (int i = 0; i < popClauses.size(); i++) {
			populateGrid(popClauses, populationGrid, i);

		}

		cqlPopulationTopLevelButtonGroup.getAddNewButton().addClickHandler(event -> onAddNewPopulationClickHandler(populationGrid, populationsObject));
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
		mainFlowPanel.add(scrollPanel);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(new SpacerWidget());
	}

	private void onAddNewPopulationClickHandler(Grid populationGrid, PopulationsObject populationsObject) {
		isViewDirty = true;
		observer.onAddNewClick(populationGrid, populationsObject);
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

		// Set a listbox with all definition names in it.
		ListBox definitionListBox = new ListBox();
		definitionListBox.setSize("180px", "30px");
		definitionListBox.addItem("--Select Definition--", "");
		definitionListBox.setTitle("Select Definition List");
		definitionListBox.setId("definitionList_" + populationClauseObject.getDisplayName());

		populationDataModel.getDefinitionNameList()
				.forEach(definition -> definitionListBox.addItem(definition.getName(), definition.getUuid()));

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
		definitionListBox.addChangeHandler(event -> setIsDirty(true));
		populationGrid.setWidget(i, 1, definitionListBox);
		// button for Delete
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
		
		populationGrid.setWidget(i, 2, deleteButton);

		// button for View Human Readable
		Button viewHRButton = new Button("View", IconType.BINOCULARS, new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				PopulationClauseObject population = new PopulationClauseObject(populationClauseObject);

				if (!definitionListBox.getSelectedItemText().equals("--Select Definition--")) {
					population.setCqlExpressionDisplayName(definitionListBox.getSelectedItemText());
				} else {
					population.setCqlExpressionDisplayName("");
				}

				population.setCqlExpressionUUID(definitionListBox.getSelectedValue());

				observer.onViewHRClick(population);
			}
		});
		viewHRButton.setType(ButtonType.LINK);
		viewHRButton.getElement().setId("viewHRButton_" + populationClauseObject.getDisplayName());
		viewHRButton.setTitle("View Human Readable");
		viewHRButton.getElement().setAttribute("aria-label",
				"Click this button to View Human Readable for " + populationClauseObject.getDisplayName());
		viewHRButton.setIcon(IconType.BINOCULARS);
		viewHRButton.setColor("black");

		populationGrid.setWidget(i, 3, viewHRButton);
	}
	
	private void addDeleteButtonEventHandler(ClickEvent event , Grid populationGrid, PopulationClauseObject populationClauseObject) {
		if (populationsObject.getPopulationClauseObjectList().size() == 1) {
			event.stopPropagation();
		} else {
			isViewDirty = true;
			observer.onDeleteClick(populationGrid, populationClauseObject);
		}
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
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(CQLPopulationObserver observer) {
		this.observer = observer;
	}

	@Override
	public boolean isDirty() {
		return isViewDirty;
	}

	public void setIsDirty(boolean isViewDirty) {
		this.isViewDirty = isViewDirty;
	}
}
