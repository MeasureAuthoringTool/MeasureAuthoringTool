package mat.client.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.gwtbootstrap3.client.ui.HelpBlock;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.shared.MatConstants;

public class MeasurePackagerAssociations {
	
	private VerticalPanel addAssocationsWidget = new VerticalPanel();
	private ListBoxMVP denominatorListBox = new ListBoxMVP();
	private ListBoxMVP numoratorListBox = new ListBoxMVP();
	private ListBoxMVP measureObservation1ListBox = new ListBoxMVP();
	private ListBoxMVP measureObservation2ListBox = new ListBoxMVP();
	
	private ArrayList<MeasurePackageClauseDetail> denominatorAndNumorators = null;
	private ArrayList<MeasurePackageClauseDetail> measureObservations = null;
	private ArrayList<MeasurePackageClauseDetail> initialPopulations = null;
	
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
		denominatorAndNumorators = new ArrayList<>();
		measureObservations = new ArrayList<>();
		initialPopulations = new ArrayList<>();
		for(MeasurePackageClauseDetail detail : populationList) {
			String detailType = detail.getType();
			if(detailType.contains(MatConstants.DENOMINATOR) || detailType.contains(MatConstants.NUMERATOR)) {
				denominatorAndNumorators.add(detail);
			} else if (detailType.contains(MatConstants.INITIAL_POPULATION)) {
				initialPopulations.add(detail);
			} else if (detailType.contains(MatConstants.MEASURE_OBS_POPULATION)) {
				measureObservations.add(detail);
			}
		}
		int count = 1;
		if(initialPopulations.size() >= 2) {
			for(MeasurePackageClauseDetail detail : denominatorAndNumorators) {
				HorizontalPanel denominatorAndNumeratorPanel= new HorizontalPanel();
				Label denominatorAndNumeratorLabel = new Label(detail.getName());
				denominatorAndNumeratorLabel.setWidth("150px");
				denominatorAndNumeratorPanel.add(denominatorAndNumeratorLabel);
				ListBoxMVP denominatorAndNumeratorListBox = new ListBoxMVP();
				denominatorAndNumeratorListBox.setTitle(detail.getName());
				denominatorAndNumeratorListBox.setWidth("175px");
				denominatorAndNumeratorListBox.addItem("--Select--", "0");
				Map<String, Integer> denomHashMap = new HashMap<>();
				count = 1;
				for(MeasurePackageClauseDetail initialPopulation : initialPopulations) {
					denominatorAndNumeratorListBox.addItem(initialPopulation.getName(), initialPopulation.getId());
					denomHashMap.put(initialPopulation.getId(), count);
					count++;
				}
				denominatorAndNumeratorListBox.setSelectedIndex(detail.getAssociatedPopulationUUID() == null ? 0 : denomHashMap.get(detail.getAssociatedPopulationUUID()));
				if(detail.getName().toLowerCase().contains(MatConstants.DENOMINATOR)) {
					denominatorListBox = denominatorAndNumeratorListBox;
					denominatorDetail = detail;
				} else {
					numoratorListBox = denominatorAndNumeratorListBox;
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
			measureObservationListBox.setTitle(detail.getName());
			measureObservationListBox.setWidth("175px");
			measureObservationListBox.addItem("--Select--", "0");
			Map<String, Integer> measureObservationHashMap = new HashMap<>();
			count = 1;
			for(MeasurePackageClauseDetail denominatorAndNumorator : denominatorAndNumorators) {
				measureObservationListBox.addItem(denominatorAndNumorator.getName(), denominatorAndNumorator.getId());
				measureObservationHashMap.put(denominatorAndNumorator.getId(), count);
				count++;
			}
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
	
	
	private void createOnChangeHandlers() {
		denominatorListBox.addChangeHandler(event -> handleDenominatorChange(denominatorListBox.getSelectedIndex()));
		numoratorListBox.addChangeHandler(event -> handleNumeratorChange(numoratorListBox.getSelectedIndex()));
		measureObservation1ListBox.addChangeHandler(event -> handleMeasureObservation1Change(measureObservation1ListBox.getSelectedIndex()));
		measureObservation2ListBox.addChangeHandler(event -> handelMeasureObservation2Change(measureObservation2ListBox.getSelectedIndex()));
	}
	
	private void handleDenominatorChange(int denominatorSelectedIndex) {
		if(denominatorSelectedIndex == 0) {
			numoratorListBox.setSelectedIndex(0);
		} else if(denominatorSelectedIndex == 1) {
			numoratorListBox.setSelectedIndex(2);
		} else if(denominatorSelectedIndex == 2) {
			numoratorListBox.setSelectedIndex(1);
		}
		setDenominatorAndNumeratorAssociations();
		setHelpBlockMessage(denominatorListBox.getTitle() + " was updated to " + denominatorListBox.getSelectedItemText() + " so " + numoratorListBox.getTitle() + " was updated to " + numoratorListBox.getSelectedItemText());
	}

	private void handleNumeratorChange(int numeratorSelectedIndex) {
		if(numeratorSelectedIndex == 0) {
			denominatorListBox.setSelectedIndex(0);
		} else if(numeratorSelectedIndex == 1) {
			denominatorListBox.setSelectedIndex(2);
		} else if(numeratorSelectedIndex == 2) {
			denominatorListBox.setSelectedIndex(1);
		}
		setDenominatorAndNumeratorAssociations();
		setHelpBlockMessage(numoratorListBox.getTitle() + " was updated to " + numoratorListBox.getSelectedItemText() + " so " + denominatorListBox.getTitle() + " was updated to " + denominatorListBox.getSelectedItemText());
	}
	
	private void handleMeasureObservation1Change(int measureObservation1SelectedIndex) {
		if(measureObservation2ListBox != null) {
			if(measureObservation1SelectedIndex == 0) {
				measureObservation2ListBox.setSelectedIndex(0);
			} else if(measureObservation1SelectedIndex == 1) {
				measureObservation2ListBox.setSelectedIndex(2);
			} else if(measureObservation1SelectedIndex == 2) {
				measureObservation2ListBox.setSelectedIndex(1);
			}
			setHelpBlockMessage(measureObservation1ListBox.getTitle() + " was updated to " + measureObservation1ListBox.getSelectedItemText() + " so " + measureObservation2ListBox.getTitle() + " was updated to " + measureObservation2ListBox.getSelectedItemText());
		}
		setMeasureObservationAssociatations();
	}
	
	private void handelMeasureObservation2Change(int measureObservation2SelectedIndex) {
		if(measureObservation1ListBox != null) {
			if(measureObservation2SelectedIndex == 0) {
				measureObservation1ListBox.setSelectedIndex(0);
			} else if(measureObservation2SelectedIndex == 1) {
				measureObservation1ListBox.setSelectedIndex(2);
			} else if(measureObservation2SelectedIndex == 2) {
				measureObservation1ListBox.setSelectedIndex(1);
			}
			setHelpBlockMessage(measureObservation2ListBox.getTitle() + " was updated to " + measureObservation2ListBox.getSelectedItemText() + " so " + measureObservation1ListBox.getTitle() + " was updated to " + measureObservation1ListBox.getSelectedItemText());
		}
		setMeasureObservationAssociatations();
	}
	
	private void setDenominatorAndNumeratorAssociations() {
		if(denominatorDetail != null) {
			denominatorDetail.setAssociatedPopulation(true);
			denominatorDetail.setAssociatedPopulationUUID(denominatorListBox.getSelectedValue());
		}
		if (numeratorDetail != null) {
			numeratorDetail.setAssociatedPopulation(true);
			numeratorDetail.setAssociatedPopulationUUID(numoratorListBox.getSelectedValue());
		}
	}
	
	private void setMeasureObservationAssociatations() {
		if(measureObservation1Detail != null) {
			measureObservation1Detail.setAssociatedPopulation(true);
			measureObservation1Detail.setAssociatedPopulationUUID(measureObservation1ListBox.getSelectedValue());
		}
		if(measureObservation2Detail != null) {
			measureObservation2Detail.setAssociatedPopulation(true);
			measureObservation2Detail.setAssociatedPopulationUUID(measureObservation2ListBox.getSelectedValue());
		}
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

	public ListBoxMVP getNumoratorListBox() {
		return numoratorListBox;
	}

	public void setNumoratorListBox(ListBoxMVP numoratorListBox) {
		this.numoratorListBox = numoratorListBox;
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

	public ArrayList<MeasurePackageClauseDetail> getMeasureObservations() {
		return measureObservations;
	}

	public void setMeasureObservations(ArrayList<MeasurePackageClauseDetail> measureObservations) {
		this.measureObservations = measureObservations;
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
