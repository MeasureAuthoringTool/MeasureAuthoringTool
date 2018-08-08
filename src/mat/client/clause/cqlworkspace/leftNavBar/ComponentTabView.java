package mat.client.clause.cqlworkspace.leftNavBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.CQLSuggestOracle;


public class ComponentTabView extends AnchorListItem {

	private Badge badge = new Badge();
	private ListBox listBox = new ListBox();
	private Label label = new Label("Components");
	private PanelCollapse collapse = new PanelCollapse();
	private SuggestBox suggestBox;
	private Anchor anchor;
	
	public ComponentTabView() {
		collapse = createComponentCollapsablePanel();
		super.setIcon(IconType.PENCIL);
		super.setTitle("Component");
		super.setId("component_Anchor");
		label.setStyleName("transparentLabel");
		label.setId("componentsLabel_Label");
		badge.setText("0"); //+ //componentObjects.size());
		badge.setPull(Pull.RIGHT);
		badge.setMarginLeft(52);
		badge.setId("componentsBadge_Badge");
		anchor = (Anchor) (super.getWidget(0));	
		anchor.add(label);
		anchor.add(badge);
		anchor.setDataParent("#navGroup");
		super.setDataToggle(Toggle.COLLAPSE);
		super.setHref("#collapseComponent");
		super.add(collapse);
	}
	
	private PanelCollapse createComponentCollapsablePanel() {
		collapse.setId("collapseComponent");
		PanelBody componentCollapseBody = new PanelBody();
		HorizontalPanel componentHP = new HorizontalPanel();
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelComponent");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label componentsLabel = new Label("Components");
		buildSearchBox();
		buildListBox();
		clearAndAddToListBox();

		rightVerticalPanel.add(suggestBox);
		rightVerticalPanel.add(listBox);
		
		rightVerticalPanel.setCellHorizontalAlignment(componentsLabel, HasHorizontalAlignment.ALIGN_LEFT);
		componentHP.add(rightVerticalPanel);
		componentCollapseBody.add(componentHP);

		collapse.add(componentCollapseBody);
		return collapse;
	}
	
	private void buildListBox() {
		listBox.clear();
		listBox.setWidth("180px");
		listBox.setVisibleItemCount(10);
		listBox.getElement().setAttribute("id", "componentsListBox");

	}
	
	private void buildSearchBox() {
		
//		nameMap.clear();
//		for(String obj : componentObjects) {
//			nameMap.put(obj.getComponentId(), obj.getAlias());
//		}
		
		suggestBox = new SuggestBox(getSuggestOracle(new ArrayList<String>()));
		suggestBox.setWidth("180px");
		suggestBox.setText("Search");
		suggestBox.setTitle("Search Component Alias");
		suggestBox.getElement().setId("searchSuggesComponentTextBox_SuggestBox");
	}
	

	
	public void clearAndAddToListBox() {
		if (listBox != null) {
			listBox.clear();
//			componentObjects = sortComponentsList(componentObjects);
//			for (String object : componentObjects) {
//				listBox.addItem(object.getAlias(), object.getComponentId());
//			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(listBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}
	
//	private List<ComponentMeasureTabObject> sortComponentsList(List<ComponentMeasureTabObject> objectList) {
//		Collections.sort(objectList, new Comparator<ComponentMeasureTabObject>() {
//			@Override
//			public int compare(final ComponentMeasureTabObject object1, final ComponentMeasureTabObject object2) {
//				return (object1.getAlias()).compareToIgnoreCase(object2.getAlias());
//			}
//		});
//		return objectList;
//	}
	
	private SuggestOracle getSuggestOracle(Collection<String> values) {
		return new CQLSuggestOracle(values);
	}
	
	public PanelCollapse getCollapse() {
		return collapse;
	}
	
	public SuggestBox getSuggestBox() {
		return suggestBox;
	}
	
	public ListBox getListBox() {
		return listBox;
	}

	public Badge getBadge() {
		return badge;
	}
	
	public Anchor getAnchor() {
		return anchor;
	}
	
}