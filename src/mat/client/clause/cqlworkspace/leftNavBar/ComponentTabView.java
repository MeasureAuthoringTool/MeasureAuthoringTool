package mat.client.clause.cqlworkspace.leftNavBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.VerticalPanel;

import mat.client.shared.CQLSuggestOracle;
import mat.client.shared.ComponentMeasureTabObject;
import mat.client.shared.MatContext;


public class ComponentTabView extends AnchorListItem {
	
	private Badge badge = new Badge();
	private ListBox listBox = new ListBox();
	private Label label = new Label("Components");
	private PanelCollapse collapse = new PanelCollapse();
	private SuggestBox suggestBox;
	private Anchor anchor;
	
	private List<ComponentMeasureTabObject> componentObjectsList = new ArrayList<>();
	private Map<String, ComponentMeasureTabObject> componentObjectsMap = new HashMap<String, ComponentMeasureTabObject>();
	private Map<String, String> aliases = new HashMap<String,String>();
<<<<<<< HEAD
=======
	private String name;
	private String owner;
	private String content;
>>>>>>> 9302: TODO- direct update button to the newly made edit composite measure screen; fix issue with getting library owner name; get the cql library name, not the measure library name; put alias in list box/search box; get cql library as a string, not as a blob; fix method to remove component tab if not composite (checkCompositeMeasure() in CQLWorkspacePresenter)
	
	private String selectedLibaryName;
	private String selectedOwner;
	private String selectedContent;
	private String selectedAlias;
	
	
	public ComponentTabView() {
		collapse = createComponentCollapsablePanel();
		super.setIcon(IconType.PENCIL);
		super.setTitle("Component");
		super.setId("component_Anchor");
		label.setStyleName("transparentLabel");
		label.setId("componentsLabel_Label");
		setBadgeNumber(0);
		badge.setPull(Pull.RIGHT);
		badge.setMarginLeft(45);
		badge.setId("componentsBadge_Badge");
		anchor = (Anchor) (super.getWidget(0));	
		anchor.add(label);
		anchor.add(badge);
		anchor.setDataParent("#navGroup");
		super.setDataToggle(Toggle.COLLAPSE);
		super.setHref("#collapseComponent");
		super.add(collapse);
	}

	public String getCQLLibraryOwnerNameFromMeasureId(String id) {
		MatContext.get().getCQLLibraryService().getCQLLibraryOwnerNameFromMeasureId(id, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				owner = "";
			}

			@Override
			public void onSuccess(String result) {
				owner = result;
			}
		});
		
		return owner;
	}
	
	public String getCQLLibraryNameFromMeasureId(String id) {
		MatContext.get().getCQLLibraryService().getCQLLibraryNameFromMeasureId(id, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				name = "";
			}

			@Override
			public void onSuccess(String result) {
				name = result;
			}
		});
		return name;
	}
	
	public String getCQLLibraryContentFromMeasaureId(String id){
		
		
		return content;
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
		aliases.clear();
		for(ComponentMeasureTabObject obj : componentObjectsList) {
			aliases.put(obj.getComponentId(), obj.getAlias());
		}
		
		suggestBox = new SuggestBox(getSuggestOracle(aliases.values()));
		suggestBox.setWidth("180px");
		suggestBox.setText("Search");
		suggestBox.setTitle("Search Component Alias");
		suggestBox.getElement().setId("searchSuggesComponentTextBox_SuggestBox");
	}
	
<<<<<<< HEAD
	public void setBadgeNumber(int size) {
		if (size < 10) {
			badge.setText("0" + size);
		} else {
			badge.setText("" + size);
=======
	public void setBadgeNumber() {
		if (componentObjectsList.size() < 10) {
			badge.setText("0" + componentObjectsList.size());
		} else {
			badge.setText("" + componentObjectsList.size());
>>>>>>> 9302: TODO- direct update button to the newly made edit composite measure screen; fix issue with getting library owner name; get the cql library name, not the measure library name; put alias in list box/search box; get cql library as a string, not as a blob; fix method to remove component tab if not composite (checkCompositeMeasure() in CQLWorkspacePresenter)
		}
	}
	
	public void clearAndAddToListBox(List<ComponentMeasureTabObject> componentMeasures) {
		componentObjectsList.clear();
		componentObjectsMap.clear();
		componentObjectsList.addAll(componentMeasures);
		if (listBox != null) {
			listBox.clear();
			componentObjectsList = sortComponentsList(componentObjectsList);
			for (ComponentMeasureTabObject object : componentObjectsList) {
<<<<<<< HEAD
				componentObjectsMap.put(object.getComponentId(), object);
=======
>>>>>>> 9302: TODO- direct update button to the newly made edit composite measure screen; fix issue with getting library owner name; get the cql library name, not the measure library name; put alias in list box/search box; get cql library as a string, not as a blob; fix method to remove component tab if not composite (checkCompositeMeasure() in CQLWorkspacePresenter)
				listBox.addItem(object.getAlias(), object.getComponentId());
			}
			// Set tooltips for each element in listbox
			SelectElement selectElement = SelectElement.as(listBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
		
		setBadgeNumber(componentMeasures.size());
	}
	
	private List<ComponentMeasureTabObject> sortComponentsList(List<ComponentMeasureTabObject> objectList) {
		Collections.sort(objectList, new Comparator<ComponentMeasureTabObject>() {
			@Override
			public int compare(final ComponentMeasureTabObject object1, final ComponentMeasureTabObject object2) {
				return (object1.getAlias()).compareToIgnoreCase(object2.getAlias());
			}
		});
		return objectList;
	}
	
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

	public List<ComponentMeasureTabObject> getComponentObjectsList() {
		return componentObjectsList;
	}
	
	public Map<String, ComponentMeasureTabObject> getComponentObjectsMap() {
		return componentObjectsMap;
	}

	public Map<String, String> getAliases() {
		return aliases;
	}
	public void setComponentObjects(List<ComponentMeasureTabObject> list) {
		this.componentObjectsList = list;
	}
	
	public String getSelectedLibraryName() {
		return selectedLibaryName;
	}

	public void setSelectedLibraryName(String selectedName) {
		this.selectedLibaryName = selectedName;
	}

	public String getSelectedOwner() {
		return selectedOwner;
	}

	public void setSelectedOwner(String selectedOwner) {
		this.selectedOwner = selectedOwner;
	}

	public String getSelectedContent() {
		return selectedContent;
	}

	public void setSelectedContent(String selectedContent) {
		this.selectedContent = selectedContent;
	}

	public String getSelectedAlias() {
		return selectedAlias;
	}

	public void setSelectedAlias(String selectedAlias) {
		this.selectedAlias = selectedAlias;
	}

	public void setSuggestBox(CQLSuggestOracle cqlSuggestOracle) {
		this.suggestBox = new SuggestBox(cqlSuggestOracle);
	}
<<<<<<< HEAD
=======

>>>>>>> 9302: TODO- direct update button to the newly made edit composite measure screen; fix issue with getting library owner name; get the cql library name, not the measure library name; put alias in list box/search box; get cql library as a string, not as a blob; fix method to remove component tab if not composite (checkCompositeMeasure() in CQLWorkspacePresenter)
	
}