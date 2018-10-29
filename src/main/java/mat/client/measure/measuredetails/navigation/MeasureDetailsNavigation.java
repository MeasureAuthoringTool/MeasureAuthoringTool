package mat.client.measure.measuredetails.navigation;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.measure.measuredetails.MeasureDetailsObserver;
import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;
import mat.client.shared.MeasureDetailsConstants.PopulationItems;

public class MeasureDetailsNavigation {
	private FlowPanel mainPanel = new FlowPanel();
	private Map<MatDetailItem, AnchorListItem> menuItemMap;
	private MeasureDetailsObserver observer;
	private PanelCollapse populationsCollapse;
	private String scoringType;
	//TODO add ids to dom objects
	public MeasureDetailsNavigation(String scoringType) {
		buildNavigationMenu(scoringType);
		mainPanel.setWidth("250px");
	}

	public void buildNavigationMenu(String scoringType) {
		mainPanel.clear();
		this.scoringType = scoringType;
		GWT.log("now scoring type: " + scoringType);
		menuItemMap = new HashMap<>();
		populationsCollapse = buildPopulationCollapse();
		NavPills navPills = buildNavPills();
		mainPanel.add(navPills);
	}
	
	private PanelCollapse buildPopulationCollapse() {
		populationsCollapse = new PanelCollapse();
		VerticalPanel nestedNavPanel = new VerticalPanel(); 
		nestedNavPanel.setWidth("250px");
		NavPills nestedNavPills = new NavPills();
		nestedNavPills.setStacked(true);
		buildPopulationNavPills(nestedNavPills);
		nestedNavPanel.add(nestedNavPills);
		populationsCollapse.add(nestedNavPanel);
		return populationsCollapse;
	}
	
	public void buildPopulationNavPills(NavPills navPills) {
		GWT.log("scoring type: " + scoringType);
		List<PopulationItems> populationsList = Arrays.asList(PopulationItems.values());
		for(PopulationItems populationDetail: populationsList) {
			GWT.log("population: " + populationDetail.abbreviatedName());
			List<String> applicableScoringTypes = populationDetail.getApplicableMeasureTypes();
			GWT.log("applicableScoringTypes size: " + applicableScoringTypes.size());
			GWT.log("applicableScoringTypes: " + applicableScoringTypes.toString());
			if(applicableScoringTypes.contains(scoringType)) {
				AnchorListItem anchorListItem = new AnchorListItem(populationDetail.abbreviatedName());
				menuItemMap.put(populationDetail, anchorListItem);
				navPills.add(anchorListItem);
				anchorListItem.addClickHandler(event -> anchorListItemClicked(populationDetail));
			}
		}
	}

	public Widget getWidget() {
		return mainPanel;
	}

	private NavPills buildNavPills() {
		NavPills navPills = new NavPills();
		List<MeasureDetailsItems> detailsList = Arrays.asList(MeasureDetailsItems.values());
		for(MeasureDetailsItems measureDetail: detailsList) {
			//TODO handle composite measures
			AnchorListItem anchorListItem = new AnchorListItem(measureDetail.abbreviatedName());
			if(measureDetail == MeasureDetailsItems.POPULATIONS) {
				anchorListItem.setIcon(IconType.PLUS);
				anchorListItem.add(populationsCollapse);
				anchorListItem.addClickHandler(event -> populationItemClicked(anchorListItem));
				navPills.add(anchorListItem);
			} else {
				menuItemMap.put(measureDetail, anchorListItem);
				navPills.add(anchorListItem);
				anchorListItem.addClickHandler(event -> anchorListItemClicked(measureDetail));
			}
		}
		navPills.setStacked(true);
		return navPills;
	}
	
	private void populationItemClicked(AnchorListItem anchorListItem) {
		deselectAllMenuItems();
		if (populationsCollapse.getElement().getClassName().equalsIgnoreCase("panel-collapse collapse in")) {
			populationsCollapse.getElement().setClassName("panel-collapse collapse");
			anchorListItem.setIcon(IconType.PLUS);
		} else {
			populationsCollapse.getElement().setClassName("panel-collapse collapse in");
			anchorListItem.setIcon(IconType.MINUS);
		}
	}

	private void anchorListItemClicked(MatDetailItem menuItem) {
		observer.onMenuItemClicked(menuItem);
		setActiveMenuItem(menuItem);
	}

	public void setActiveMenuItem(MatDetailItem activeMenuItem) {
		deselectAllMenuItems();
		menuItemMap.get(activeMenuItem).setActive(true);
	}

	private void deselectAllMenuItems() {
		Iterator<AnchorListItem>menuItems = menuItemMap.values().iterator();
		while(menuItems.hasNext()) {
			AnchorListItem anchorListItem = menuItems.next();
			anchorListItem.setActive(false);
		}
	}
	
	public void setObserver(MeasureDetailsObserver observer) {
		this.observer = observer;
	}
}
