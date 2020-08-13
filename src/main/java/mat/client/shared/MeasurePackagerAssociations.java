package mat.client.shared;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.shared.MatConstants;
import org.gwtbootstrap3.client.ui.HelpBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MeasurePackagerAssociations {
	
	private static final String EXCLUSION =  "Exclusion";
	
	private VerticalPanel addAssocationsWidget = new VerticalPanel();
	private ListBoxMVP denominatorListBox = new ListBoxMVP();
	private ListBoxMVP numeratorListBox = new ListBoxMVP();
	private ListBoxMVP measureObservation1ListBox = new ListBoxMVP();
	private ListBoxMVP measureObservation2ListBox = new ListBoxMVP();
	
	private MeasurePackageClauseDetail denominatorDetail = null;
	private MeasurePackageClauseDetail numeratorDetail = null;
	private MeasurePackageClauseDetail measureObservation1Detail = null;
	private MeasurePackageClauseDetail measureObservation2Detail = null;
	private final HelpBlock helpBlock = new HelpBlock();
	
	
	public Widget buildAddAssociationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		addAssocationsWidget.clear();
		addAssocationsWidget.setWidth("220px");
		addAssocationsWidget.setHeight("200px");
		addAssocationsWidget.add(new HTML("<b style='margin-left:15px;'> Add Associations </b>"));
		addAssocationsWidget.add(getAssociatedPopulationWidget(populationList));
		createOnChangeHandlers();
		return addAssocationsWidget;
	}

	private VerticalPanel getAssociatedPopulationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		VerticalPanel vPanel = new VerticalPanel();
		createHelpBlock();
		vPanel.add(new SpacerWidget());
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		
		ArrayList<MeasurePackageClauseDetail> denominatorAndNumerators = new ArrayList<>();
		ArrayList<MeasurePackageClauseDetail> measureObservations = new ArrayList<>();
		ArrayList<MeasurePackageClauseDetail> initialPopulations = new ArrayList<>();
		
		for(MeasurePackageClauseDetail detail : populationList) {
			String detailType = detail.getType();
			if((detailType.contains(MatConstants.DENOMINATOR) || detailType.contains(MatConstants.NUMERATOR)) && !detailType.contains(EXCLUSION)) {
				denominatorAndNumerators.add(detail);
			} else if (detailType.contains(MatConstants.INITIAL_POPULATION)) {
				initialPopulations.add(detail);
			} else if (detailType.contains(MatConstants.MEASURE_OBSERVATION_POPULATION)) {
				measureObservations.add(detail);
			}
		}
		if(initialPopulations.size() >= 2) {
			for(MeasurePackageClauseDetail detail : denominatorAndNumerators) {
				HorizontalPanel denominatorAndNumeratorPanel= new HorizontalPanel();
				Label denominatorAndNumeratorLabel = new Label(detail.getName());
				denominatorAndNumeratorLabel.setWidth("150px");
				denominatorAndNumeratorPanel.add(denominatorAndNumeratorLabel);
				ListBoxMVP denominatorAndNumeratorListBox = new ListBoxMVP();
				denominatorAndNumeratorListBox.setTitle("Select association for " + detail.getName());
				denominatorAndNumeratorListBox.setWidth("175px");
				denominatorAndNumeratorListBox.insertItem("--Select--", "0", "Select");
				Map<String, Integer> denomHashMap = new HashMap<>();
				createListBoxes(initialPopulations, denominatorAndNumeratorListBox, denomHashMap);
				denominatorAndNumeratorListBox.setSelectedIndex(detail.getAssociatedPopulationUUID() == null ? 0 : denomHashMap.get(detail.getAssociatedPopulationUUID()));
				if(detail.getName().toLowerCase().contains(MatConstants.DENOMINATOR)) {
					denominatorListBox = denominatorAndNumeratorListBox;
					denominatorDetail = detail;
				} else {
					numeratorListBox = denominatorAndNumeratorListBox;
					numeratorDetail = detail;
				}
				denominatorAndNumeratorPanel.add(new HorizontalSpacerWidget());
				denominatorAndNumeratorPanel.add(denominatorAndNumeratorListBox);
				vPanel.add(denominatorAndNumeratorPanel);
				
			}
		}
		int measureObservationCount = 0;
		for(MeasurePackageClauseDetail detail : measureObservations) {
			HorizontalPanel measureObservationPanel= new HorizontalPanel();
			Label measureObservationLabel = new Label(detail.getName());
			measureObservationLabel.setWidth("150px");
			measureObservationPanel.add(measureObservationLabel);
			ListBoxMVP measureObservationListBox = new ListBoxMVP();
			measureObservationListBox.setTitle("Select association for " + detail.getName());
			measureObservationListBox.setWidth("175px");
			measureObservationListBox.insertItem("--Select--", "0", "Select");
			Map<String, Integer> measureObservationHashMap = new HashMap<>();
			createListBoxes(denominatorAndNumerators, measureObservationListBox, measureObservationHashMap);
			measureObservationListBox.setSelectedIndex(detail.getAssociatedPopulationUUID() == null ? 0 : measureObservationHashMap.get(detail.getAssociatedPopulationUUID()));
			
			if(measureObservationCount == 0) {
				measureObservation1ListBox = measureObservationListBox;
				measureObservation1Detail = detail;
			} else {
				measureObservation2ListBox = measureObservationListBox;
				measureObservation2Detail = detail;
			}
			
			measureObservationPanel.add(new HorizontalSpacerWidget());
			measureObservationPanel.add(measureObservationListBox);
			vPanel.add(measureObservationPanel);
			measureObservationCount++;
		}

		vPanel.setSize("200px", "170px");
		HorizontalPanel associateWidgetButtonPanel = new HorizontalPanel();
		associateWidgetButtonPanel.addStyleName("floatRightButtonPanel");
		vPanel.add(associateWidgetButtonPanel);
		return vPanel;
	}

	private void createListBoxes(ArrayList<MeasurePackageClauseDetail> populations, ListBoxMVP populationListBox, Map<String, Integer> populationCountMap) {
		int count = 1;
		for(MeasurePackageClauseDetail initialPopulation : populations) {
			populationListBox.insertItem(initialPopulation.getName(), initialPopulation.getId(), initialPopulation.getName());
			populationCountMap.put(initialPopulation.getId(), count);
			count++;
		}
	}
	
	public void makeAssociationsIsEditable(Boolean isEditable) {
		denominatorListBox.setEnabled(isEditable);
		numeratorListBox.setEnabled(isEditable);
		measureObservation1ListBox.setEnabled(isEditable);
		measureObservation2ListBox.setEnabled(isEditable);
	}
	
	private void createOnChangeHandlers() {
		denominatorListBox.addChangeHandler(event -> handleDenominatorAndNumeratorListChange(denominatorListBox.getSelectedIndex(), denominatorListBox, numeratorListBox));
		numeratorListBox.addChangeHandler(event -> handleDenominatorAndNumeratorListChange(numeratorListBox.getSelectedIndex(), numeratorListBox, denominatorListBox));
		measureObservation1ListBox.addChangeHandler(event -> handleMeasureObservationChange(measureObservation1ListBox.getSelectedIndex(), measureObservation1ListBox, measureObservation2ListBox));
		measureObservation2ListBox.addChangeHandler(event -> handleMeasureObservationChange(measureObservation2ListBox.getSelectedIndex(), measureObservation2ListBox, measureObservation1ListBox));
	}
	
	private void handleDenominatorAndNumeratorListChange(int selectedIndex, ListBoxMVP listBoxThatWasUpdatedByUser, ListBoxMVP listBoxToUpdate) {
		setListBoxIndexChanges(selectedIndex, listBoxToUpdate);
		setDenominatorAndNumeratorAssociations();
		setHelpBlockMessage(listBoxThatWasUpdatedByUser.getTitle() + " was updated to " + listBoxThatWasUpdatedByUser.getSelectedItemText() + " so " + listBoxToUpdate.getTitle() + " was updated to " + listBoxToUpdate.getSelectedItemText());
	
	}

	private void handleMeasureObservationChange(int selectedIndex, ListBoxMVP listBoxThatWasUpdatedByUser, ListBoxMVP listBoxToUpdate) {
		if(listBoxToUpdate != null) {
			setListBoxIndexChanges(selectedIndex, listBoxToUpdate);
			setHelpBlockMessage(listBoxThatWasUpdatedByUser.getTitle() + " was updated to " + listBoxThatWasUpdatedByUser.getSelectedItemText() + " so " + listBoxToUpdate.getTitle() + " was updated to " + listBoxToUpdate.getSelectedItemText());
		}
		setMeasureObservationAssociatations();
	}
	
	private void setListBoxIndexChanges(int selectedIndex, ListBoxMVP listBoxToUpdate) {
		if(selectedIndex == 0) {
			listBoxToUpdate.setSelectedIndex(0);
		} else if(selectedIndex == 1) {
			listBoxToUpdate.setSelectedIndex(2);
		} else if(selectedIndex == 2) {
			listBoxToUpdate.setSelectedIndex(1);
		}
	}
	
	private void setDenominatorAndNumeratorAssociations() {
		if(denominatorDetail != null) {
			denominatorDetail.setAssociatedPopulation(true);
			denominatorDetail.setAssociatedPopulationUUID(convertZeroValueToNull(denominatorListBox.getSelectedValue()));
		}
		if (numeratorDetail != null) {
			numeratorDetail.setAssociatedPopulation(true);
			numeratorDetail.setAssociatedPopulationUUID(convertZeroValueToNull(numeratorListBox.getSelectedValue()));
		}
	}
	
	private void setMeasureObservationAssociatations() {
		if(measureObservation1Detail != null) {
			measureObservation1Detail.setAssociatedPopulation(true);
			measureObservation1Detail.setAssociatedPopulationUUID(convertZeroValueToNull(measureObservation1ListBox.getSelectedValue()));
		}
		if(measureObservation2Detail != null) {
			measureObservation2Detail.setAssociatedPopulation(true);
			measureObservation2Detail.setAssociatedPopulationUUID(convertZeroValueToNull(measureObservation2ListBox.getSelectedValue()));
		}
	}
	
	private String convertZeroValueToNull(String value) {
		return "0".equalsIgnoreCase(value) ? null : value;
	}
	
	private void createHelpBlock() {
		helpBlock.setText("initial text");
		helpBlock.setColor("transparent");
		helpBlock.setHeight("0px");
		helpBlock.getElement().setAttribute("role", "alert");
		addAssocationsWidget.add(helpBlock);
	}
	
	public void setHelpBlockMessage(String message) {
		helpBlock.setText(message);
		helpBlock.getElement().focus();
	}

	public Widget asWidget() {
		return addAssocationsWidget.asWidget();
	}

	public ListBoxMVP getDenominatorListBox() {
		return denominatorListBox;
	}

	public void setDenominatorListBox(ListBoxMVP denominatorListBox) {
		this.denominatorListBox = denominatorListBox;
	}

	public ListBoxMVP getNumeratorListBox() {
		return numeratorListBox;
	}

	public void setNumeratorListBox(ListBoxMVP numeratorListBox) {
		this.numeratorListBox = numeratorListBox;
	}

	public ListBoxMVP getMeasureObservation1ListBox() {
		return measureObservation1ListBox;
	}

	public void setMeasureObservation1ListBox(ListBoxMVP measureObservation1) {
		this.measureObservation1ListBox = measureObservation1;
	}

	public ListBoxMVP getMeasureObservation2ListBox() {
		return measureObservation2ListBox;
	}

	public void setMeasureObservation2ListBox(ListBoxMVP measureObservation2) {
		this.measureObservation2ListBox = measureObservation2;
	}

	public MeasurePackageClauseDetail getNumeratorDetail() {
		return numeratorDetail;
	}

	public void setNumeratorDetail(MeasurePackageClauseDetail numeratorDetail) {
		this.numeratorDetail = numeratorDetail;
	}

	public MeasurePackageClauseDetail getMeasureObservation1Detail() {
		return measureObservation1Detail;
	}

	public void setMeasureObservation1Detail(MeasurePackageClauseDetail measureObservation1Detail) {
		this.measureObservation1Detail = measureObservation1Detail;
	}

	public MeasurePackageClauseDetail getMeasureObservation2Detail() {
		return measureObservation2Detail;
	}

	public void setMeasureObservation2Detail(MeasurePackageClauseDetail measureObservation2Detail) {
		this.measureObservation2Detail = measureObservation2Detail;
	}

	public HelpBlock getHelpBlock() {
		return helpBlock;
	}
}
