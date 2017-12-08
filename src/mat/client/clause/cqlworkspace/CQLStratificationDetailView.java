package mat.client.clause.cqlworkspace;


import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.hibernate.transaction.BTMTransactionManagerLookup;

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
		scrollPanel.setSize("700px", "250px");
		this.populationDataModel = populationDataModel;
		this.strataDataModel = populationDataModel.getStrataDataModel();
		HorizontalPanel btnPanel = new HorizontalPanel();		
		btnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		btnPanel.getElement().setAttribute("style", "margin-left:450px;");
		btnPanel.add(cqlPopulationTopLevelButtonGroup.getButtonGroup());
		mainPanel.add(btnPanel);
		mainPanel.add(scrollPanel);
		buildStratificationView();
		return mainPanel;
	}
	
	private void buildStratificationView() {
		
		
		VerticalPanel mainVP = new VerticalPanel();
		for(StratificationsObject stratificationsObject : strataDataModel.getStratificationObjectList()) {
			Grid parentGrid = generateStratificationGrid(stratificationsObject);
			mainVP.add(parentGrid);
		}
		
		scrollPanel.add(mainVP);
		
	}
	
	
	private Grid generateStratificationGrid(StratificationsObject stratificationsObject) {
		Grid stratificationParentGrid = new Grid(1, 3);
		stratificationParentGrid.getElement().setId("grid_"+stratificationsObject.getDisplayName());
		
		FocusPanel nameFocusPanel = new FocusPanel();
		FormLabel nameLabel = new FormLabel();
		nameLabel.setText(stratificationsObject.getDisplayName());
		nameLabel.setTitle(stratificationsObject.getDisplayName());
		nameLabel.setId("nameLabel" + 1);
		nameFocusPanel.add(nameLabel);
		nameFocusPanel.getElement().setAttribute("style", "margin-left:55px");

		stratificationParentGrid.setWidget(0, 0, nameFocusPanel);
		stratificationParentGrid.getCellFormatter().setWidth(0, 0, "230px");
		
		// button for Add New Stratum
		Button addNewStratum = new Button();
		addNewStratum.setType(ButtonType.LINK);
		addNewStratum.getElement().setId("addNewStratumButton_" + stratificationsObject.getDisplayName());
		addNewStratum.setTitle("Add New Stratum");
		addNewStratum.setText("Add New Stratum");
		addNewStratum.setSize("50px", "30px");
		addNewStratum.getElement().setAttribute("aria-label", "Add New Stratum");
		addNewStratum.setIcon(IconType.PLUS);
		addNewStratum.setIconSize(IconSize.LARGE);
		addNewStratum.setColor("#0964A2");
		addNewStratum.setMarginRight(150.00);
		
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
		deleteButton.setSize("50px", "30px");
		deleteButton.getElement().setAttribute("aria-label", "Delete");
		deleteButton.setIcon(IconType.TRASH);
		deleteButton.setIconSize(IconSize.LARGE);
		deleteButton.setColor("#0964A2");

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
