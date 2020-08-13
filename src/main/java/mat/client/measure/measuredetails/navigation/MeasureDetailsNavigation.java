package mat.client.measure.measuredetails.navigation;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import mat.client.measure.measuredetails.MeasureDetailState;
import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MeasureDetailsNavigation {
	private static final String RATIO = "ratio";
	private FlowPanel mainPanel = new FlowPanel();
	private Map<MatDetailItem, MeasureDetailsAnchorListItem> menuItemMap;
	private MeasureDetailsObserver observer;
	private PanelCollapse populationsCollapse;
	private String scoringType;
	private boolean isPatientBased;
	private boolean isComposite;
	private MatDetailItem activeMenuItem;

	public MeasureDetailsNavigation(String scoringType, boolean isPatientBased, boolean isCompositeMeasure) {
		buildNavigationMenu(scoringType, isPatientBased, isCompositeMeasure);
		mainPanel.setWidth("275px");
	}

	public void buildNavigationMenu(String scoringType, boolean isPatientBased, boolean isCompositeMeasure) {
		mainPanel.clear();
		this.isComposite = isCompositeMeasure;
		this.isPatientBased = isPatientBased;
		this.scoringType = scoringType;
		menuItemMap = new HashMap<>();
		populationsCollapse = buildPopulationCollapse();
		NavPills navPills = buildNavPills();
		mainPanel.add(navPills);
	}
	
	private PanelCollapse buildPopulationCollapse() {
		populationsCollapse = new PanelCollapse();
		populationsCollapse.getElement().setId("measureDetailsNavigation_populationsCollapse");
		VerticalPanel nestedNavPanel = new VerticalPanel(); 
		nestedNavPanel.getElement().setId("measureDetailsNavigation_populationsPanel");
		nestedNavPanel.setWidth("275px");
		NavPills nestedNavPills = new NavPills();
		nestedNavPills.getElement().setId("measureDetailsNavigation_populationsNavigationPills");
		nestedNavPills.setStacked(true);
		buildPopulationNavPills(nestedNavPills);
		nestedNavPanel.add(nestedNavPills);
		populationsCollapse.add(nestedNavPanel);
		return populationsCollapse;
	}
	
	public void buildPopulationNavPills(NavPills navPills) {
		List<PopulationItems> populationsList = Arrays.asList(PopulationItems.values());
		for(PopulationItems populationDetail: populationsList) {
			List<String> applicableScoringTypes = populationDetail.getApplicableMeasureTypes();		
			for(String applicableScoringType : applicableScoringTypes) {
				if(applicableScoringType.equalsIgnoreCase(scoringType)) {
					
					// don't add measure observation for ratio && patient based measures
					if(populationDetail.equals(PopulationItems.MEASURE_OBSERVATIONS) && this.isPatientBased && this.scoringType.equalsIgnoreCase(RATIO)) {
						continue; 
					}
					
					MeasureDetailsAnchorListItem anchorListItem = new MeasureDetailsAnchorListItem(populationDetail.abbreviatedName());
					anchorListItem.setTitle(populationDetail.abbreviatedName());
					anchorListItem.getElement().getStyle().setMarginLeft(15, Unit.PX);
					menuItemMap.put(populationDetail, anchorListItem);
					navPills.add(anchorListItem);
					anchorListItem.addClickHandler(event -> handleMenuItemClick(populationDetail));
				}
			}
		}
	}

	public Widget getWidget() {
		return mainPanel;
	}

	private NavPills buildNavPills() {
		NavPills navPills = new NavPills();
		navPills.getElement().setId("measureDetailsNavigation_measureDetailsNavigationPills");
		List<MeasureDetailsItems> detailsList = Arrays.asList(MeasureDetailsItems.values());
		for(MeasureDetailsItems measureDetail: detailsList) {
			MeasureDetailsAnchorListItem anchorListItem;

			if(measureDetail == MeasureDetailsItems.POPULATIONS) {
				anchorListItem = new MeasureDetailsAnchorListItem(measureDetail.abbreviatedName());
				anchorListItem.setIcon(IconType.PLUS);
				anchorListItem.add(populationsCollapse);
				anchorListItem.addClickHandler(event -> handleMenuItemClick(measureDetail));
				navPills.add(anchorListItem);
			} else {
				anchorListItem = new MeasureDetailsAnchorListItem(measureDetail.abbreviatedName());
				//if not a composite measure skip the component menu item
				if((measureDetail == MeasureDetailsItems.COMPONENT_MEASURES && !isComposite) || 
						(measureDetail == MeasureDetailsItems.STRATIFICATION && RATIO.equalsIgnoreCase(this.scoringType))) {
					continue;
				}
				navPills.add(anchorListItem);
				anchorListItem.addClickHandler(event -> handleMenuItemClick(measureDetail));
			}
			anchorListItem.getElement().setTitle(measureDetail.abbreviatedName());
			menuItemMap.put(measureDetail, anchorListItem);
		}
		navPills.setStacked(true);
		return navPills;
	}
	
	private void handleMenuItemClick(MatDetailItem menuItem) {
		observer.handleMenuItemClick(menuItem);
	}

	public void expandPopulationCollapse(AnchorListItem anchorListItem) {
		if (populationsCollapse.getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")) {
			setPopulationCollapseCollapsed(anchorListItem);
		} else {
			setPopulationCollapseExpanded(anchorListItem);
		}
	}

	private void setPopulationCollapseCollapsed(AnchorListItem anchorListItem) {
		populationsCollapse.getElement().setClassName("panel-collapse collapse");
		anchorListItem.setIcon(IconType.PLUS);
	}

	private void setPopulationCollapseExpanded(AnchorListItem anchorListItem) {
		populationsCollapse.getElement().setClassName("panel-collapse collapse in");
		anchorListItem.setIcon(IconType.MINUS);
	}
		
	public void setActiveMenuItem(MatDetailItem activeMenuItem) {
		this.activeMenuItem = activeMenuItem;
		deselectAllMenuItems();
		
		if(activeMenuItem instanceof PopulationItems) {
			setPopulationCollapseExpanded(menuItemMap.get(MeasureDetailsItems.POPULATIONS));
		} else if(activeMenuItem == MeasureDetailsItems.POPULATIONS) {
			expandPopulationCollapse(menuItemMap.get(MeasureDetailsItems.POPULATIONS));
		} else {
			setPopulationCollapseCollapsed(menuItemMap.get(MeasureDetailsItems.POPULATIONS));
		}
		
		menuItemMap.get(activeMenuItem).setActive(true);		
	}

	public void deselectAllMenuItems() {
		Iterator<MeasureDetailsAnchorListItem>menuItems = menuItemMap.values().iterator();
		while(menuItems.hasNext()) {
			MeasureDetailsAnchorListItem anchorListItem = menuItems.next();
			anchorListItem.setActive(false);
		}
	}
	
	public void setObserver(MeasureDetailsObserver observer) {
		this.observer = observer;
	}

	public void updateState(MeasureDetailState state) {
		menuItemMap.get(activeMenuItem).setState(state);
		
	}

	public Map<MatDetailItem, MeasureDetailsAnchorListItem> getMenuItemMap() {
		return menuItemMap;
	}

	public void setMenuItemMap(Map<MatDetailItem, MeasureDetailsAnchorListItem> menuItemMap) {
		this.menuItemMap = menuItemMap;
	}
	
	public MatDetailItem getActiveMenuItem() {
		return activeMenuItem;
	}
}