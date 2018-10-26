package mat.client.measure.measuredetails;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.NavPills;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.MatDetailItem;
import mat.client.shared.MeasureDetailsConstants.MeasureDetailsItems;

public class MeasureDetailsNavigation {
	FlowPanel mainPanel = new FlowPanel();
	Map<MatDetailItem, AnchorListItem> menuItemMap;
	MeasureDetailsObserver observer;
	
	public MeasureDetailsNavigation() {
		menuItemMap = new HashMap<>();
		NavPills navPills = buildNavigationMenu();
		mainPanel.add(navPills);
		mainPanel.setWidth("250px");
	}
	
	public Widget getWidget() {
		return mainPanel;
	}

	public NavPills buildNavigationMenu() {
		NavPills navPills = new NavPills();
		List<MeasureDetailsItems> detailsList = Arrays.asList(MeasureDetailsItems.values());
		for(MeasureDetailsItems measureDetail: detailsList) {
			//TODO handle composite and populations
			//TODO add ids to dom objects
			AnchorListItem anchorListItem = new AnchorListItem(measureDetail.displayName());
			menuItemMap.put(measureDetail, anchorListItem);
			navPills.add(anchorListItem);
			anchorListItem.addClickHandler(event -> anchorListItemClicked(measureDetail));
		}
		navPills.setStacked(true);
		return navPills;
	}
	
	private void anchorListItemClicked(MatDetailItem menuItem) {
		observer.onMenuItemClicked(menuItem);
		setActiveMenuItem(menuItem);
	}

	public void setActiveMenuItem(MatDetailItem activeMenuItem) {
		Iterator<AnchorListItem>menuItems = menuItemMap.values().iterator();
		while(menuItems.hasNext()) {
			AnchorListItem anchorListItem = menuItems.next();
			anchorListItem.setActive(false);
		}
		menuItemMap.get(activeMenuItem).setActive(true);
	}
	
	public void setObserver(MeasureDetailsObserver observer) {
		this.observer = observer;
	}
}
