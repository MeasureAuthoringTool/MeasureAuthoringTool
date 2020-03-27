package mat.client.cqlworkspace;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.gwtbootstrap3.client.ui.Anchor;
import org.gwtbootstrap3.client.ui.AnchorListItem;
import org.gwtbootstrap3.client.ui.Badge;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.ListBox;
import org.gwtbootstrap3.client.ui.NavPills;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelCollapse;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;
import org.gwtbootstrap3.client.ui.constants.Toggle;

import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import mat.client.shared.CQLSuggestOracle;
import mat.client.shared.MatContext;
import mat.model.ComponentMeasureTabObject;
import mat.model.clause.QDSAttributes;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;

public class CQLLeftNavBarPanelView {
	private static final String CQL_LIBRARY_EDITOR_ANCHOR = "cqlLibraryEditor_Anchor";
	private VerticalPanel rightHandNavPanel = new VerticalPanel();
	private Map<String, String> defineNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, String> funcNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	private Map<String, String> includeLibraryNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	private HashMap<String, CQLDefinition> definitionMap = new HashMap<String, CQLDefinition>();
	private HashMap<String, CQLFunctions> functionMap = new HashMap<String, CQLFunctions>();
	private HashMap<String, CQLIncludeLibrary> includeLibraryMap = new HashMap<String, CQLIncludeLibrary>();
	private Badge includesBadge = new Badge();
	private Badge valueSetBadge = new Badge();
	private Badge codesBadge = new Badge();
	private Badge paramBadge = new Badge();
	private Badge defineBadge = new Badge();
	private Badge functionBadge = new Badge();
	private Label includesLabel = new Label("Includes");
	private Label valueSetLabel = new Label("Value Sets");
	private Label codesLabel = new Label("Codes");
	private Label paramLabel = new Label("Parameter");
	private Label defineLabel = new Label("Definition");
	private Label functionLibLabel = new Label("Function");
	PanelCollapse paramCollapse = new PanelCollapse();
	PanelCollapse defineCollapse = new PanelCollapse();
	PanelCollapse functionCollapse = new PanelCollapse();
	PanelCollapse includesCollapse = new PanelCollapse();
	PanelCollapse codesCollapse = new PanelCollapse();
	PanelCollapse valueSetCollapse = new PanelCollapse();
	private AnchorListItem cqlLibraryEditorTab;
	private AnchorListItem appliedQDM;
	private AnchorListItem codesLibrary;
	private AnchorListItem generalInformation;
	private AnchorListItem includesLibrary;
	private AnchorListItem parameterLibrary;
	private AnchorListItem definitionLibrary;
	private AnchorListItem functionLibrary;
	private List<CQLIncludeLibrary> viewIncludeLibrarys = new ArrayList<CQLIncludeLibrary>();
	private List<CQLParameter> viewParameterList = new ArrayList<CQLParameter>();
	private List<CQLDefinition> viewDefinitions = new ArrayList<CQLDefinition>();
	private List<CQLCode> codesList = new ArrayList<CQLCode>();
	private List<CQLQualityDataSetDTO> appliedQdmTableList = new ArrayList<CQLQualityDataSetDTO>();
	private List<CQLCode> appliedCodeTableList = new ArrayList<CQLCode>();
	private List<CQLLibraryDataSetObject> includeLibraryList = new ArrayList<CQLLibraryDataSetObject>();
	private List<CQLFunctions> viewFunctions = new ArrayList<CQLFunctions>();
	private ListBox includesNameListbox = new ListBox();
	private ListBox defineNameListBox = new ListBox();
	private ListBox funcNameListBox = new ListBox();
	private SuggestBox searchSuggestDefineTextBox;
	private SuggestBox searchSuggestIncludeTextBox;
	private SuggestBox searchSuggestFuncTextBox;
	private SuggestBox searchSuggestParamTextBox;
	private Map<String, String> parameterNameMap = new TreeMap<String, String>(String.CASE_INSENSITIVE_ORDER);
	private HashMap<String, CQLParameter> parameterMap = new HashMap<String, CQLParameter>();
	private ListBox parameterNameListBox = new ListBox();
	private Boolean isDoubleClick = false;
	private Boolean isNavBarClick = false;
	private Boolean isLoading = false;
	private String currentSelectedDefinitionObjId = null;
	private String currentSelectedParamerterObjId = null;
	private String currentSelectedFunctionObjId = null;
	private String currentSelectedFunctionArgumentObjId = null;
	private String currentSelectedFunctionArgumentName = null;
	private String currentSelectedIncLibraryObjId = null;
	private String currentSelectedValueSetObjId = null;
	private String currentSelectedCodesObjId = null;
	private List<QDSAttributes> availableQDSAttributeList;

	private Badge badge = new Badge();
	private ListBox componentNameListBox = new ListBox();
	private Label componentLabel = new Label("Components");
	private PanelCollapse collapse = new PanelCollapse();
	private SuggestBox searchAliasTextBox;
	private AnchorListItem anchor;
	private List<ComponentMeasureTabObject> componentObjectsList = new ArrayList<>();
	private Map<String, ComponentMeasureTabObject> componentObjectsMap = new HashMap<String, ComponentMeasureTabObject>();
	private Map<String, String> aliases = new HashMap<String,String>();
	private PanelCollapse buildComponentCollapsablePanel() {
		collapse.clear();
		collapse.setId("collapseComponent");
		PanelBody componentCollapseBody = new PanelBody();
		HorizontalPanel componentHP = new HorizontalPanel();
		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);
		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelComponent");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label componentsLabel = new Label("Components");
		
		Map<String, String> aliases = new HashMap<String,String>();
		aliases.clear();
		for(ComponentMeasureTabObject obj : componentObjectsList) {
			aliases.put(obj.getComponentId(), obj.getAlias());
		}
		
		buildSearchAliasBox();
		
		componentNameListBox.clear();
		componentNameListBox.setWidth("180px");
		componentNameListBox.setVisibleItemCount(10);
		componentNameListBox.getElement().setAttribute("id", "componentsListBox");
		
		rightVerticalPanel.add(searchAliasTextBox);
		rightVerticalPanel.add(componentNameListBox);
		
		addSuggestHandler(searchAliasTextBox, componentNameListBox);
		addListBoxHandler(componentNameListBox, searchAliasTextBox);
		
		rightVerticalPanel.setCellHorizontalAlignment(componentsLabel, HasHorizontalAlignment.ALIGN_LEFT);
		componentHP.add(rightVerticalPanel);
		componentCollapseBody.add(componentHP);

		collapse.add(componentCollapseBody);
		return collapse;
	}
	
	private ClickHandler createClickHandler(SuggestBox suggestBox) {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ("Search".equals(suggestBox.getText())) {
					suggestBox.setText("");
				}
			}
		};
	}

	private void setBadgeNumber(int size, Badge badge) {
		if (size < 10) {
			badge.setText("0" + size);
		} else {
			badge.setText("" + size);
		}
	}
	
	private void buildComponentsTab() {
		this.anchor.setIcon(IconType.PENCIL);
		this.anchor.setTitle("Component");
		this.anchor.setId("component_Anchor");
		componentLabel.setStyleName("transparentLabel");
		componentLabel.setId("componentsLabel_Label");
		setBadgeNumber(0, badge);
		badge.setPull(Pull.RIGHT);
		badge.setMarginLeft(45);
		badge.setId("componentsBadge_Badge");
		Anchor anchor = (Anchor) (this.anchor.getWidget(0));
		anchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		
		anchor.addClickHandler(event -> {
			this.anchor.setDataToggle(Toggle.COLLAPSE);
			this.anchor.setHref("#collapseComponent");
		});
		
		anchor.add(componentLabel);
		anchor.add(badge);
		anchor.setDataParent("#navGroup");
		this.anchor.add(collapse);
	}
	

	public VerticalPanel buildMeasureLibCQLView(){
		collapse = buildComponentCollapsablePanel();
		includesCollapse = createIncludesCollapsablePanel();
		paramCollapse = createParameterCollapsablePanel();
		defineCollapse = createDefineCollapsablePanel();
		functionCollapse = createFunctionCollapsablePanel();
		buildLeftHandNavBar();
		return rightHandNavPanel;
	}

	private void buildLeftHandNavBar() {
		rightHandNavPanel.clear();
		NavPills navPills = new NavPills();
		navPills.setStacked(true);
		navPills.setWidth("200px");

		generalInformation = new AnchorListItem();
		anchor = new AnchorListItem();
		includesLibrary = new AnchorListItem();
		appliedQDM = new AnchorListItem();
		codesLibrary = new AnchorListItem();
		parameterLibrary = new AnchorListItem();
		definitionLibrary = new AnchorListItem();
		functionLibrary = new AnchorListItem();
		cqlLibraryEditorTab = new AnchorListItem();

		
		buildGeneralInfoTab();
		buildComponentsTab();
		buildIncludesTab();
		buildValueSetsTab();
		buildCodesTab();
		buildParameterTab();
		buildDefinitionsTab();
		buildFunctionsTab();
		buildCQLLibraryEditorTab();
		
		navPills.add(generalInformation);
		navPills.add(anchor);
		navPills.add(includesLibrary);
		navPills.add(appliedQDM);
		navPills.add(codesLibrary);
		navPills.add(parameterLibrary);
		navPills.add(definitionLibrary);
		navPills.add(functionLibrary);
		navPills.add(cqlLibraryEditorTab);

		rightHandNavPanel.add(navPills);
	}
	
	private void buildGeneralInfoTab() {
		generalInformation.setIcon(IconType.INFO);
		generalInformation.setText("General Information");
		generalInformation.setTitle("General Information");
		generalInformation.setActive(true);
		generalInformation.setId("generatalInformation_Anchor");
	}
	
	
	private void buildIncludesTab() {
		includesLibrary.setIcon(IconType.PENCIL);
		includesLibrary.setTitle("Includes");
		includesBadge.setText("0" + viewIncludeLibrarys.size());
		Anchor includesAnchor = (Anchor) (includesLibrary.getWidget(0));
		includesAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		
		includesAnchor.addClickHandler(event -> {
			includesLibrary.setDataToggle(Toggle.COLLAPSE);
			includesLibrary.setHref("#collapseIncludes");
		});
		
		includesLabel.setStyleName("transparentLabel");
		includesLabel.setId("includesLabel_Label");
		includesAnchor.add(includesLabel);
		includesBadge.setPull(Pull.RIGHT);
		includesBadge.setMarginLeft(52);
		includesBadge.setId("includesBadge_Badge");
		includesAnchor.add(includesBadge);
		includesAnchor.setDataParent("#navGroup");
		includesLibrary.setId("includesLibrary_Anchor");
		includesLibrary.add(includesCollapse);
	}
	
	private void buildValueSetsTab() {
		appliedQDM.setIcon(IconType.PENCIL);
		appliedQDM.setTitle("Value Sets");
		valueSetBadge.setText("0" + appliedQdmTableList.size());
		Anchor valueSetAnchor = (Anchor) (appliedQDM.getWidget(0));
		valueSetAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		valueSetLabel.setStyleName("transparentLabel");
		valueSetLabel.setId("valueSetLabel_Label");
		valueSetAnchor.add(valueSetLabel);
		valueSetBadge.setPull(Pull.RIGHT);
		valueSetBadge.setMarginLeft(52);
		valueSetBadge.setId("valueSetBadge_Badge");
		valueSetAnchor.add(valueSetBadge);
		valueSetAnchor.setDataParent("#navGroup");
	}
	
	private void buildCodesTab() {
		codesLibrary.setIcon(IconType.PENCIL);
		codesLibrary.setTitle("Codes");
		codesBadge.setText("0" + codesList.size());
		Anchor codesAnchor = (Anchor) (codesLibrary.getWidget(0));
		codesAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		codesLabel.setStyleName("transparentLabel");
		codesLabel.setId("codesLabel_Label");
		codesAnchor.add(codesLabel);
		codesBadge.setPull(Pull.RIGHT);
		codesBadge.setMarginLeft(52);
		codesBadge.setId("codesBadge_Badge");
		codesAnchor.add(codesBadge);
		codesAnchor.setDataParent("#navGroup");
	}
	
	private void buildParameterTab() {
		parameterLibrary.setIcon(IconType.PENCIL);
		parameterLibrary.setTitle("Parameter");
		paramBadge.setText("" + viewParameterList.size());
		Anchor paramAnchor = (Anchor) (parameterLibrary.getWidget(0));
		paramAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		
		
		paramAnchor.addClickHandler(event -> {
			parameterLibrary.setDataToggle(Toggle.COLLAPSE);
			parameterLibrary.setHref("#collapseParameter");
		});
		
		paramLabel.setStyleName("transparentLabel");
		paramLabel.setId("paramLabel_Label");
		paramAnchor.add(paramLabel);
		paramBadge.setPull(Pull.RIGHT);
		paramBadge.setId("paramBadge_Badge");
		paramAnchor.add(paramBadge);
		paramAnchor.setDataParent("#navGroup");
		parameterLibrary.setId("parameterLibrary_Anchor");
		parameterLibrary.add(paramCollapse);
	}
	
	private void buildDefinitionsTab() {
		definitionLibrary.setIcon(IconType.PENCIL);
		definitionLibrary.setTitle("Define");
		definitionLibrary.setId("definitionLibrary_Anchor");
		defineBadge.setText("" + viewDefinitions.size());
		Anchor defineAnchor = (Anchor) (definitionLibrary.getWidget(0));
		defineAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		
		defineAnchor.addClickHandler(event -> {
			definitionLibrary.setDataToggle(Toggle.COLLAPSE);
			definitionLibrary.setHref("#collapseDefine");
		});
				
		defineLabel.setStyleName("transparentLabel");
		defineLabel.setId("defineLabel_Label");
		defineAnchor.add(defineLabel);
		defineBadge.setPull(Pull.RIGHT);
		defineAnchor.add(defineBadge);
		defineBadge.setId("defineBadge_Badge");
		defineAnchor.setDataParent("#navGroup");

		definitionLibrary.add(defineCollapse);

	}
	
	private void buildFunctionsTab() {
		functionLibrary.setIcon(IconType.PENCIL);
		functionLibrary.setId("functionLibrary_Anchor");
		functionLibrary.setTitle("Functions");

		functionBadge.setText("" + viewFunctions.size());
		Anchor funcAnchor = (Anchor) (functionLibrary.getWidget(0));
		funcAnchor.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				event.stopPropagation();
			}
		});
		
		funcAnchor.addClickHandler(event -> {
			functionLibrary.setDataToggle(Toggle.COLLAPSE);
			functionLibrary.setHref("#collapseFunction");
		});
		
		functionLibLabel.setStyleName("transparentLabel");
		functionLibLabel.setId("functionLibLabel_label");
		funcAnchor.add(functionLibLabel);
		functionBadge.setPull(Pull.RIGHT);

		funcAnchor.add(functionBadge);
		functionBadge.setId("functionBadge_Badge");
		funcAnchor.setDataParent("#navGroup");
		functionLibrary.add(functionCollapse);
	}
	
	private void buildCQLLibraryEditorTab() {
		cqlLibraryEditorTab.setIcon(IconType.BOOK);
		cqlLibraryEditorTab.setText("CQL Library Editor");
		cqlLibraryEditorTab.setTitle("CQL Library Editor");
		cqlLibraryEditorTab.setId(CQL_LIBRARY_EDITOR_ANCHOR);
	}
	
	

	private PanelCollapse createIncludesCollapsablePanel() {
		includesCollapse.setId("collapseIncludes");

		PanelBody includesCollapseBody = new PanelBody();

		HorizontalPanel includesFP = new HorizontalPanel();

		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);

		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelIncludes");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label includesLibraryLabel = new Label("Includes Library");
		buildSearchSuggestIncludeBox();

		includesNameListbox.clear();
		includesNameListbox.setWidth("180px");
		includesNameListbox.setVisibleItemCount(10);
		includesNameListbox.getElement().setAttribute("id", "includesListBox");

		clearAndAddAliasNamesToListBox();

		addSuggestHandler(searchSuggestIncludeTextBox, includesNameListbox);
		addListBoxHandler(includesNameListbox, searchSuggestIncludeTextBox);

		searchSuggestIncludeTextBox.getElement().setAttribute("id", "searchSuggestIncludesTextBox");
		rightVerticalPanel.add(searchSuggestIncludeTextBox);
		searchSuggestIncludeTextBox.getElement().setId("searchSuggesIncludestTextBox_SuggestBox");
		rightVerticalPanel.add(includesNameListbox);
		includesNameListbox.getElement().setId("includesNameListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(includesLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		includesFP.add(rightVerticalPanel);
		includesCollapseBody.add(includesFP);

		includesCollapse.add(includesCollapseBody);
		return includesCollapse;

	}

	private void buildSearchSuggestIncludeBox() {
		searchSuggestIncludeTextBox = new SuggestBox(getSuggestOracle(includeLibraryNameMap.values()));
		searchSuggestIncludeTextBox.setWidth("180px");
		searchSuggestIncludeTextBox.setText("Search");
		searchSuggestIncludeTextBox.setTitle("Search Included Alias");
		searchSuggestIncludeTextBox.getElement().setId("searchTextBox_TextBoxIncludesLib");
		searchSuggestIncludeTextBox.getValueBox().addClickHandler(createClickHandler(searchSuggestIncludeTextBox));
	}
	
	private void buildSearchSuggestParamBox() {
		searchSuggestParamTextBox = new SuggestBox(getSuggestOracle(parameterNameMap.values()));
		searchSuggestParamTextBox.setWidth("180px");
		searchSuggestParamTextBox.setText("Search");
		searchSuggestParamTextBox.setTitle("Search Parameter");
		searchSuggestParamTextBox.getElement().setId("searchTextBox_TextBoxParameterLib");
		searchSuggestParamTextBox.getValueBox().addClickHandler(createClickHandler(searchSuggestParamTextBox));
	}

	private PanelCollapse createParameterCollapsablePanel() {
		paramCollapse.setId("collapseParameter");

		PanelBody parameterCollapseBody = new PanelBody();

		HorizontalPanel parameterFP = new HorizontalPanel();

		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);

		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelParam");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label paramLibraryLabel = new Label("Parameter Library");
		buildSearchSuggestParamBox();
		
		parameterNameListBox.clear();
		parameterNameListBox.setWidth("180px");
		parameterNameListBox.setVisibleItemCount(10);
		parameterNameListBox.getElement().setAttribute("id", "paramListBox");

		clearAndAddParameterNamesToListBox();

		addSuggestHandler(searchSuggestParamTextBox, parameterNameListBox);
		addListBoxHandler(parameterNameListBox, searchSuggestParamTextBox);

		searchSuggestParamTextBox.getElement().setAttribute("id", "searchSuggestTextBox");
		rightVerticalPanel.add(searchSuggestParamTextBox);
		searchSuggestParamTextBox.getElement().setId("searchSuggestTextBox_SuggestBox");
		rightVerticalPanel.add(parameterNameListBox);
		parameterNameListBox.getElement().setId("paramNameListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(paramLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		parameterFP.add(rightVerticalPanel);
		parameterCollapseBody.add(parameterFP);
		paramCollapse.add(parameterCollapseBody);
		return paramCollapse;

	}

	private PanelCollapse createDefineCollapsablePanel() {
		PanelCollapse defineCollapsePanel = new PanelCollapse();
		defineCollapsePanel.setId("collapseDefine");

		PanelBody defineCollapseBody = new PanelBody();

		HorizontalPanel defineFP = new HorizontalPanel();

		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);

		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelParam");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label defineLibraryLabel = new Label("Definition Library");
		buildSearchSuggestDefineBox();

		defineNameListBox.clear();
		defineNameListBox.setWidth("180px");
		defineNameListBox.setVisibleItemCount(10);
		defineNameListBox.getElement().setAttribute("id", "defineListBox");

		clearAndAddDefinitionNamesToListBox();

		addSuggestHandler(searchSuggestDefineTextBox, defineNameListBox);
		addListBoxHandler(defineNameListBox, searchSuggestDefineTextBox);

		searchSuggestDefineTextBox.getElement().setAttribute("id", "searchSuggestTextBox");
		rightVerticalPanel.add(searchSuggestDefineTextBox);
		searchSuggestDefineTextBox.getElement().setId("searchSuggestTextBox_SuggestBox");
		rightVerticalPanel.add(defineNameListBox);
		searchSuggestDefineTextBox.getElement().setId("DefineListBox_ListBox");
		rightVerticalPanel.setCellHorizontalAlignment(defineLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		defineFP.add(rightVerticalPanel);
		defineCollapseBody.add(defineFP);
		defineCollapsePanel.add(defineCollapseBody);
		return defineCollapsePanel;

	}

	private void buildSearchAliasBox() {
		aliases.clear();
		for(ComponentMeasureTabObject obj : componentObjectsList) {
			aliases.put(obj.getComponentId(), obj.getAlias());
		}
		
		searchAliasTextBox = new SuggestBox(getSuggestOracle(aliases.values()));
		searchAliasTextBox.setWidth("180px");
		searchAliasTextBox.setText("Search");
		searchAliasTextBox.setTitle("Search Component Alias");
		searchAliasTextBox.getElement().setId("searchSuggesComponentTextBox_SuggestBox");
		searchAliasTextBox.getValueBox().addClickHandler(createClickHandler(searchAliasTextBox));
	}
	
	private void buildSearchSuggestDefineBox() {
		searchSuggestDefineTextBox = new SuggestBox(getSuggestOracle(defineNameMap.values()));
		searchSuggestDefineTextBox.setWidth("180px");
		searchSuggestDefineTextBox.setText("Search");
		searchSuggestDefineTextBox.setTitle("Search Definition");
		searchSuggestDefineTextBox.getElement().setId("searchSuggestDefineTextBox");
		searchSuggestDefineTextBox.getValueBox().addClickHandler(createClickHandler(searchSuggestDefineTextBox));
	}
	
	private void buildSearchSuggestFuncBox() {
		searchSuggestFuncTextBox = new SuggestBox(getSuggestOracle(funcNameMap.values()));
		searchSuggestFuncTextBox.setWidth("180px");
		searchSuggestFuncTextBox.setText("Search");
		searchSuggestFuncTextBox.setTitle("Search Function");
		searchSuggestFuncTextBox.getElement().setId("searchTextBox_TextBoxFuncLib");
		searchSuggestFuncTextBox.getValueBox().addClickHandler(createClickHandler(searchSuggestFuncTextBox));
	}

	private PanelCollapse createFunctionCollapsablePanel() {
		PanelCollapse funcCollapsePanel = new PanelCollapse();
		funcCollapsePanel.setId("collapseFunction");

		PanelBody funcCollapseBody = new PanelBody();

		HorizontalPanel funcFP = new HorizontalPanel();

		VerticalPanel rightVerticalPanel = new VerticalPanel();
		rightVerticalPanel.setSpacing(10);

		rightVerticalPanel.getElement().setId("rhsVerticalPanel_VerticalPanelFunc");
		rightVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label functionLibraryLabel = new Label("Function Library");
		buildSearchSuggestFuncBox();

		funcNameListBox.clear();
		funcNameListBox.setWidth("180px");
		funcNameListBox.setVisibleItemCount(10);
		funcNameListBox.getElement().setAttribute("id", "funcListBox");

		clearAndAddFunctionsNamesToListBox();

		addSuggestHandler(searchSuggestFuncTextBox, funcNameListBox);
		addListBoxHandler(funcNameListBox, searchSuggestFuncTextBox);

		searchSuggestFuncTextBox.getElement().setAttribute("id", "searchSuggestTextBoxFunc");
		rightVerticalPanel.add(searchSuggestFuncTextBox);
		rightVerticalPanel.add(funcNameListBox);

		rightVerticalPanel.setCellHorizontalAlignment(functionLibraryLabel, HasHorizontalAlignment.ALIGN_LEFT);
		funcFP.add(rightVerticalPanel);
		funcCollapseBody.add(funcFP);
		funcCollapsePanel.add(funcCollapseBody);
		return funcCollapsePanel;

	}	
	
	
	private SuggestOracle getSuggestOracle(Collection<String> values) {
		return new CQLSuggestOracle(values);
	}
	

	public void clearAndAddAliasNamesToListBox() {
		if (includesNameListbox != null) {
			includesNameListbox.clear();
			viewIncludeLibrarys = sortAliasList(viewIncludeLibrarys);
			for (CQLIncludeLibrary incl : viewIncludeLibrarys) {
				if(incl.getId() != null) {
					includesNameListbox.addItem(incl.getAliasName(), incl.getId());
				}
			}

			SelectElement selectElement = SelectElement.as(includesNameListbox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}


	private List<CQLIncludeLibrary> sortAliasList(List<CQLIncludeLibrary> viewAliasList) {
		Collections.sort(viewAliasList, new Comparator<CQLIncludeLibrary>() {
			@Override
			public int compare(final CQLIncludeLibrary object1, final CQLIncludeLibrary object2) {
				return object1.getAliasName().compareToIgnoreCase(object2.getAliasName());
			}
		});
		return viewAliasList;
	}
	
	public void clearAndAddDefinitionNamesToListBox() {
		if (defineNameListBox != null) {
			defineNameListBox.clear();
			viewDefinitions = sortDefinitionNames(viewDefinitions);
			for (CQLDefinition define : viewDefinitions) {
				defineNameListBox.addItem(define.getName(), define.getId());
			}

			SelectElement selectElement = SelectElement.as(defineNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}


	private List<CQLDefinition> sortDefinitionNames(List<CQLDefinition> viewDef) {
		Collections.sort(viewDef, new Comparator<CQLDefinition>() {
			@Override
			public int compare(final CQLDefinition object1, final CQLDefinition object2) {
				return object1.getName().compareToIgnoreCase(object2.getName());
			}
		});
		return viewDef;
	}


	public void clearAndAddFunctionsNamesToListBox() {
		if (funcNameListBox != null) {
			funcNameListBox.clear();
			// sort functions
			viewFunctions = sortFunctionNames(viewFunctions);
			for (CQLFunctions func : viewFunctions) {
				funcNameListBox.addItem(func.getName(), func.getId());
			}

			SelectElement selectElement = SelectElement.as(funcNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}


	private List<CQLFunctions> sortFunctionNames(List<CQLFunctions> viewFunc) {
		Collections.sort(viewFunc, new Comparator<CQLFunctions>() {
			@Override
			public int compare(final CQLFunctions object1, final CQLFunctions object2) {
				return object1.getName().compareToIgnoreCase(object2.getName());
			}
		});
		return viewFunc;
	}


	public void clearAndAddParameterNamesToListBox() {
		if (parameterNameListBox != null) {
			parameterNameListBox.clear();
			viewParameterList = sortParamList(viewParameterList);
			for (CQLParameter param : viewParameterList) {
				parameterNameListBox.addItem(param.getName(), param.getId());
			}

			SelectElement selectElement = SelectElement.as(parameterNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
	}


	private List<CQLParameter> sortParamList(List<CQLParameter> viewParamList) {
		Collections.sort(viewParamList, new Comparator<CQLParameter>() {
			@Override
			public int compare(final CQLParameter object1, final CQLParameter object2) {
				return object1.getName().compareToIgnoreCase(object2.getName());
			}
		});
		return viewParamList;
	}
	

	private void addSuggestHandler(final SuggestBox suggestBox, final ListBox listBox) {
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String selectedQDMName = event.getSelectedItem().getReplacementString();
				for (int i = 0; i < listBox.getItemCount(); i++) {
					if (selectedQDMName.equals(listBox.getItemText(i))) {
						listBox.setItemSelected(i, true);
						listBox.setFocus(true);
						break;
					}
				}
			}
		});
	}
	

	private void addListBoxHandler(final ListBox listBox, final SuggestBox suggestBox) {
		listBox.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				System.out.println("listbox change event:" + event.getAssociatedType().getName());
				int selectedIndex = listBox.getSelectedIndex();
				String selectedItem = listBox.getItemText(selectedIndex);
				suggestBox.setText(selectedItem);
			}
		});

	}
	

	public void updateValueSetMap(List<CQLQualityDataSetDTO> appliedValueSetTableList) {
		if (getAppliedQdmTableList().size() < 10) {
			getValueSetBadge().setText("0" + appliedValueSetTableList.size());
		} else {
			getValueSetBadge().setText("" + appliedValueSetTableList.size());
		}

	}
	
	

	public void setCodeBadgeValue(List<CQLCode> appliedCodeTableList) {
		if (appliedCodeTableList.size() < 10) {
			getCodesBadge().setText("0" + appliedCodeTableList.size());
		} else {
			getCodesBadge().setText("" + appliedCodeTableList.size());
		}

	}
	
	

	public void updateParamMap() {
		getParameterMap().clear();
		getParameterNameMap().clear();
		for (CQLParameter parameter : getViewParameterList()) {
			getParameterNameMap().put(parameter.getId(), parameter.getName());
			getParameterMap().put(parameter.getId(), parameter);
		}

		if (getViewParameterList().size() < 10) {
			getParamBadge().setText("0" + getViewParameterList().size());
		} else {
			getParamBadge().setText("" + getViewParameterList().size());
		}

	}

	public void updateDefineMap() {
		getDefinitionMap().clear();
		getDefineNameMap().clear();
		for (CQLDefinition define : getViewDefinitions()) {
			getDefineNameMap().put(define.getId(), define.getName());
			getDefinitionMap().put(define.getId(), define);
		}

		if (getViewDefinitions().size() < 10) {
			getDefineBadge().setText("0" + getViewDefinitions().size());
		} else {
			getDefineBadge().setText("" + getViewDefinitions().size());
		}
		updateSuggestDefineOracle();
	}

	public void updateFunctionMap() {
		functionMap.clear();
		funcNameMap.clear();
		for (CQLFunctions function : viewFunctions) {
			funcNameMap.put(function.getId(), function.getName());
			functionMap.put(function.getId(), function);
		}

		if (viewFunctions.size() < 10) {
			functionBadge.setText("0" + viewFunctions.size());
		} else {
			functionBadge.setText("" + viewFunctions.size());
		}
		updateSuggestFuncOracle();
	}

	public void udpateIncludeLibraryMap() {
		includeLibraryMap.clear();
		includeLibraryNameMap.clear();
		for (CQLIncludeLibrary incLibrary : viewIncludeLibrarys) {
			includeLibraryNameMap.put(incLibrary.getId(), incLibrary.getAliasName());
			includeLibraryMap.put(incLibrary.getId(), incLibrary);
		}

		if (viewIncludeLibrarys.size() < 10) {
			includesBadge.setText("0" + viewIncludeLibrarys.size());
		} else {
			includesBadge.setText("" + viewIncludeLibrarys.size());
		}
		updateSuggestIncludeOracle();
	}
	
	public void updateSuggestParamOracle() {
		if (searchSuggestParamTextBox != null) {
			CQLSuggestOracle suggestOracle = (CQLSuggestOracle) searchSuggestParamTextBox.getSuggestOracle();
			suggestOracle.setData(parameterNameMap.values());
		}
	}
	

	public void updateSuggestDefineOracle() {
		if (searchSuggestDefineTextBox != null) {
			CQLSuggestOracle suggestOracle = (CQLSuggestOracle) searchSuggestDefineTextBox.getSuggestOracle();
			suggestOracle.setData(defineNameMap.values());
		}
	}


	public void updateSuggestFuncOracle() {
		if (searchSuggestFuncTextBox != null) {
			CQLSuggestOracle suggestOracle = (CQLSuggestOracle) searchSuggestFuncTextBox.getSuggestOracle();
			suggestOracle.setData(funcNameMap.values());
		}
	}
	
	public void updateSuggestIncludeOracle() {
		CQLSuggestOracle suggestOracle = (CQLSuggestOracle) searchSuggestIncludeTextBox.getSuggestOracle();
		suggestOracle.setData(includeLibraryNameMap.values());
	}

	public Map<String, String> getDefineNameMap() {
		return defineNameMap;
	}

	public void setDefineNameMap(Map<String, String> defineNameMap) {
		this.defineNameMap = defineNameMap;
	}

	public Map<String, String> getFuncNameMap() {
		return funcNameMap;
	}

	public void setFuncNameMap(Map<String, String> funcNameMap) {
		this.funcNameMap = funcNameMap;
	}

	public Map<String, String> getIncludeLibraryNameMap() {
		return includeLibraryNameMap;
	}

	public void setIncludeLibraryNameMap(Map<String, String> includeLibraryNameMap) {
		this.includeLibraryNameMap = includeLibraryNameMap;
	}

	public HashMap<String, CQLDefinition> getDefinitionMap() {
		return definitionMap;
	}

	public void setDefinitionMap(HashMap<String, CQLDefinition> definitionMap) {
		this.definitionMap = definitionMap;
	}

	public HashMap<String, CQLFunctions> getFunctionMap() {
		return functionMap;
	}

	public void setFunctionMap(HashMap<String, CQLFunctions> functionMap) {
		this.functionMap = functionMap;
	}
	
	public AnchorListItem getComponentsTab() {
		return anchor;
	}

	public HashMap<String, CQLIncludeLibrary> getIncludeLibraryMap() {
		return includeLibraryMap;
	}

	public void setIncludeLibraryMap(HashMap<String, CQLIncludeLibrary> includeLibraryMap) {
		this.includeLibraryMap = includeLibraryMap;
	}


	public Map<String, String> getParameterNameMap() {
		return parameterNameMap;
	}

	public void setParameterNameMap(Map<String, String> parameterNameMap) {
		this.parameterNameMap = parameterNameMap;
	}

	public HashMap<String, CQLParameter> getParameterMap() {
		return parameterMap;
	}

	public void setParameterMap(HashMap<String, CQLParameter> parameterMap) {
		this.parameterMap = parameterMap;
	}

	public List<CQLIncludeLibrary> getViewIncludeLibrarys() {
		return viewIncludeLibrarys;
	}

	public void setViewIncludeLibrarys(List<CQLIncludeLibrary> viewIncludeLibrarys) {
		this.viewIncludeLibrarys = viewIncludeLibrarys;
	}

	public List<CQLParameter> getViewParameterList() {
		return viewParameterList;
	}

	public void setViewParameterList(List<CQLParameter> viewParameterList) {
		this.viewParameterList = viewParameterList;
	}

	public List<CQLDefinition> getViewDefinitions() {
		return viewDefinitions;
	}

	public void setViewDefinitions(List<CQLDefinition> viewDefinitions) {
		this.viewDefinitions = viewDefinitions;
	}

	public List<CQLFunctions> getViewFunctions() {
		return viewFunctions;
	}

	public void setViewFunctions(List<CQLFunctions> viewFunctions) {
		this.viewFunctions = viewFunctions;
	}

	public Badge getValueSetBadge() {
		return valueSetBadge;
	}

	public void setValueSetBadge(Badge valueSetBadge) {
		this.valueSetBadge = valueSetBadge;
	}

	public Badge getCodesBadge() {
		return codesBadge;
	}

	public void setCodesBadge(Badge codesBadge) {
		this.codesBadge = codesBadge;
	}

	public Badge getIncludesBadge() {
		return includesBadge;
	}

	public void setIncludesBadge(Badge includesBadge) {
		this.includesBadge = includesBadge;
	}

	public Badge getParamBadge() {
		return paramBadge;
	}

	public void setParamBadge(Badge paramBadge) {
		this.paramBadge = paramBadge;
	}

	public Badge getDefineBadge() {
		return defineBadge;
	}

	public void setDefineBadge(Badge defineBadge) {
		this.defineBadge = defineBadge;
	}

	public Badge getFunctionBadge() {
		return functionBadge;
	}

	public void setFunctionBadge(Badge functionBadge) {
		this.functionBadge = functionBadge;
	}


	public VerticalPanel getRightHandNavPanel() {
		return rightHandNavPanel;
	}

	public ListBox getIncludesNameListbox() {
		return includesNameListbox;
	}

	public void setIncludesNameListbox(ListBox includesNameListbox) {
		this.includesNameListbox = includesNameListbox;
	}

	public ListBox getDefineNameListBox() {
		return defineNameListBox;
	}

	public void setDefineNameListBox(ListBox defineNameListBox) {
		this.defineNameListBox = defineNameListBox;
	}

	public ListBox getFuncNameListBox() {
		return funcNameListBox;
	}

	public void setFuncNameListBox(ListBox funcNameListBox) {
		this.funcNameListBox = funcNameListBox;
	}

	public ListBox getParameterNameListBox() {
		return parameterNameListBox;
	}

	public void setParameterNameListBox(ListBox parameterNameListBox) {
		this.parameterNameListBox = parameterNameListBox;
	}

	public AnchorListItem getGeneralInformation() {
		return generalInformation;
	}

	public void setGeneralInformation(AnchorListItem generalInformation) {
		this.generalInformation = generalInformation;
	}

	public AnchorListItem getIncludesLibrary() {
		return includesLibrary;
	}

	public void setIncludesLibrary(AnchorListItem includesLibrary) {
		this.includesLibrary = includesLibrary;
	}

	public AnchorListItem getParameterLibrary() {
		return parameterLibrary;
	}

	public void setParameterLibrary(AnchorListItem parameterLibrary) {
		this.parameterLibrary = parameterLibrary;
	}

	public AnchorListItem getDefinitionLibrary() {
		return definitionLibrary;
	}

	public void setDefinitionLibrary(AnchorListItem definitionLibrary) {
		this.definitionLibrary = definitionLibrary;
	}

	public AnchorListItem getFunctionLibrary() {
		return functionLibrary;
	}

	public void setFunctionLibrary(AnchorListItem functionLibrary) {
		this.functionLibrary = functionLibrary;
	}

	public AnchorListItem getCQLLibraryEditorTab() {
		return cqlLibraryEditorTab;
	}

	public void setCQLLibraryEditorTab(AnchorListItem viewCQL) {
		this.cqlLibraryEditorTab = viewCQL;
	}

	public List<CQLQualityDataSetDTO> getAppliedQdmTableList() {
		return appliedQdmTableList;
	}

	public void setAppliedQdmTableList(List<CQLQualityDataSetDTO> appliedQdmTableList) {
		this.appliedQdmTableList = appliedQdmTableList;
	}

	public List<CQLLibraryDataSetObject> getIncludeLibraryList() {
		return includeLibraryList;
	}

	public void setIncludeLibraryList(List<CQLLibraryDataSetObject> includeLibraryList) {
		this.includeLibraryList = includeLibraryList;
	}

	public AnchorListItem getAppliedQDM() {
		return appliedQDM;
	}

	public void setAppliedQDM(AnchorListItem appliedQDM) {
		this.appliedQDM = appliedQDM;
	}

	public AnchorListItem getCodesLibrary() {
		return codesLibrary;
	}

	public void setCodesLibrary(AnchorListItem codesLibrary) {
		this.codesLibrary = codesLibrary;
	}

	public PanelCollapse getParamCollapse() {
		return paramCollapse;
	}

	public PanelCollapse getDefineCollapse() {
		return defineCollapse;
	}

	public PanelCollapse getFunctionCollapse() {
		return functionCollapse;
	}

	public PanelCollapse getIncludesCollapse() {
		return includesCollapse;
	}

	public SuggestBox getSearchSuggestDefineTextBox() {
		return searchSuggestDefineTextBox;
	}

	public void setSearchSuggestDefineTextBox(SuggestBox searchSuggestDefineTextBox) {
		this.searchSuggestDefineTextBox = searchSuggestDefineTextBox;
	}

	public SuggestBox getSearchSuggestIncludeTextBox() {
		return searchSuggestIncludeTextBox;
	}

	public void setSearchSuggestIncludeTextBox(SuggestBox searchSuggestIncludeTextBox) {
		this.searchSuggestIncludeTextBox = searchSuggestIncludeTextBox;
	}

	public SuggestBox getSearchSuggestFuncTextBox() {
		return searchSuggestFuncTextBox;
	}

	public void setSearchSuggestFuncTextBox(SuggestBox searchSuggestFuncTextBox) {
		this.searchSuggestFuncTextBox = searchSuggestFuncTextBox;
	}

	public SuggestBox getSearchSuggestParamTextBox() {
		return searchSuggestParamTextBox;
	}

	public void setSearchSuggestParamTextBox(SuggestBox searchSuggestParamTextBox) {
		this.searchSuggestParamTextBox = searchSuggestParamTextBox;
	}


	public String getCurrentSelectedDefinitionObjId() {
		return currentSelectedDefinitionObjId;
	}

	public void setCurrentSelectedDefinitionObjId(String currentSelectedDefinitionObjId) {
		this.currentSelectedDefinitionObjId = currentSelectedDefinitionObjId;
	}

	public String getCurrentSelectedParamerterObjId() {
		return currentSelectedParamerterObjId;
	}

	public void setCurrentSelectedParamerterObjId(String currentSelectedParamerterObjId) {
		this.currentSelectedParamerterObjId = currentSelectedParamerterObjId;
	}
	
	public void setIsDoubleClick(Boolean isDoubleClick) {
		this.isDoubleClick = isDoubleClick;
	}

	public Boolean isDoubleClick() {
		return isDoubleClick;
	}

	public void setIsNavBarClick(Boolean isNavBarClick) {
		this.isNavBarClick = isNavBarClick;
	}

	public Boolean isNavBarClick() {
		return isNavBarClick;
	}
	
	public String getOwnerName(CQLLibraryDataSetObject cqlLibrary) {
		StringBuilder owner = new StringBuilder();
		owner = owner.append(cqlLibrary.getOwnerFirstName()).append(" ").append(cqlLibrary.getOwnerLastName());
		return owner.toString();
	}

	public String getCurrentSelectedFunctionObjId() {
		return currentSelectedFunctionObjId;
	}

	public void setCurrentSelectedFunctionObjId(String currentSelectedFunctionObjId) {
		this.currentSelectedFunctionObjId = currentSelectedFunctionObjId;
	}

	public String getCurrentSelectedFunctionArgumentObjId() {
		return currentSelectedFunctionArgumentObjId;
	}

	public void setCurrentSelectedFunctionArgumentObjId(String currentSelectedFunctionArgumentObjId) {
		this.currentSelectedFunctionArgumentObjId = currentSelectedFunctionArgumentObjId;
	}

	public String getCurrentSelectedFunctionArgumentName() {
		return currentSelectedFunctionArgumentName;
	}

	public void setCurrentSelectedFunctionArgumentName(String currentSelectedFunctionArgumentName) {
		this.currentSelectedFunctionArgumentName = currentSelectedFunctionArgumentName;
	}
	
	public String getCurrentSelectedIncLibraryObjId() {
		return currentSelectedIncLibraryObjId;
	}

	public void setCurrentSelectedIncLibraryObjId(String currentSelectedIncLibraryObjId) {
		this.currentSelectedIncLibraryObjId = currentSelectedIncLibraryObjId;
	}

	public List<String> getIncludedList(Map<String, CQLIncludeLibrary> includeMap) {
		List<String> list = new ArrayList<String>();
		for (Map.Entry<String, CQLIncludeLibrary> entry : includeMap.entrySet()) {
			list.add(entry.getValue().getCqlLibraryId());
		}
		return list;
	}

	public void setAvailableQDSAttributeList(List<QDSAttributes> result) {
		availableQDSAttributeList = result;
	}

	public List<QDSAttributes> getAvailableQDSAttributeList(){
		return availableQDSAttributeList;
	}
	
	public void clearShotcutKeyList(){
		MatContext.get().getParameters().clear();
		MatContext.get().getDefinitions().clear();
		MatContext.get().getFuncs().clear();
		MatContext.get().getValuesets().clear();
		MatContext.get().getIncludes().clear();
		MatContext.get().getIncludedValueSetNames().clear();
		MatContext.get().getIncludedCodeNames().clear();
		MatContext.get().getIncludedParamNames().clear();
		MatContext.get().getIncludedDefNames().clear();
		MatContext.get().getIncludedFuncNames().clear();
		
	}
	
	public void buildInfoPanel(Widget sourceWidget) {

		PopupPanel panel = new PopupPanel();
		panel.setAutoHideEnabled(true);
		panel.setPopupPosition(sourceWidget.getAbsoluteLeft() + 40, sourceWidget.getAbsoluteTop() + 20);
		VerticalPanel dialogContents = new VerticalPanel();
		dialogContents.getElement().setId("dialogContents_VerticalPanel");
		panel.setWidget(dialogContents);

		HTML html1 = new HTML("Ctrl-Alt-a: attributes");
		HTML html2 = new HTML("Ctrl-Alt-y: datatypes");
		HTML html3 = new HTML("Ctrl-Alt-d: definitions");
		HTML html4 = new HTML("Ctrl-Alt-f: functions");
		HTML html5 = new HTML("Ctrl-Alt-k: keywords");
		HTML html6 = new HTML("Ctrl-Alt-p: parameters");
		HTML html7 = new HTML("Ctrl-Alt-t: timings");
		HTML html8 = new HTML("Ctrl-Alt-u: units");
		HTML html9 = new HTML("Ctrl-Alt-v: value sets & codes");
		HTML html10 = new HTML("Ctrl-Space: all");

		dialogContents.add(html1);
		dialogContents.add(html2);
		dialogContents.add(html3);
		dialogContents.add(html4);
		dialogContents.add(html5);
		dialogContents.add(html6);
		dialogContents.add(html7);
		dialogContents.add(html8);
		dialogContents.add(html9);
		dialogContents.add(html10);
		panel.show();
	}


	public Boolean getIsLoading() {
		return isLoading;
	}


	public void setIsLoading(Boolean isLoading) {
		this.isLoading = isLoading;
	}


	public List<CQLCode> getCodesTableList() {
		return codesList;
	}


	public List<CQLCode> getAppliedCodeTableList() {
		return appliedCodeTableList;
	}


	public void setAppliedCodeTableList(List<CQLCode> appliedCodeTableList) {
		this.appliedCodeTableList = appliedCodeTableList;
	}

	public String getCurrentSelectedCodesObjId() {
		return currentSelectedCodesObjId;
	}

	public void setCurrentSelectedCodesObjId(String currentSelectedCodesObjId) {
		this.currentSelectedCodesObjId = currentSelectedCodesObjId;
	}

	public String getCurrentSelectedValueSetObjId() {
		return currentSelectedValueSetObjId;
	}

	public void setCurrentSelectedValueSetObjId(String currentSelectedValueSetObjId) {
		this.currentSelectedValueSetObjId = currentSelectedValueSetObjId;
	}
	
	public boolean checkForIncludedLibrariesQDMVersion(boolean isStandAloneCQLLibrary){
		boolean isValid = true;
		if(!isStandAloneCQLLibrary && !MatContext.get().isDraftMeasure()) {
			return isValid;
		}

		if(isStandAloneCQLLibrary && !MatContext.get().isDraftLibrary()) {
			return isValid;
		}
		List<CQLIncludeLibrary> includedLibraryList = getViewIncludeLibrarys();
		if(includedLibraryList.size()==0){
			isValid = true;
		} else {
			if (MatContext.get().getCQLModel() != null &&
					"QDM".equals(MatContext.get().getCQLModel().getUsingModel())) {
				for (CQLIncludeLibrary cqlIncludeLibrary : includedLibraryList) {
					if (cqlIncludeLibrary.getQdmVersion() == null) {
						continue;
					} else if (!cqlIncludeLibrary.getQdmVersion().equalsIgnoreCase(MatContext.get().getCurrentQDMVersion())) {
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}
	
	public ListBox getComponentsListBox() {
		return componentNameListBox;
	}

	public Map<String, ComponentMeasureTabObject> getComponentMap() {
		return componentObjectsMap;
	}

	public UIObject getComponentsCollapse() {
		return collapse;
	}

	public void updateComponentInformation(List<ComponentMeasureTabObject> componentMeasures) {
		componentObjectsList.clear();
		componentObjectsMap.clear();
		aliases.clear();
		componentObjectsList.addAll(componentMeasures);
		if (componentNameListBox != null) {
			componentNameListBox.clear();
			sortComponentsList(componentObjectsList);
			for (ComponentMeasureTabObject object : componentObjectsList) {
				componentObjectsMap.put(object.getComponentId(), object);
				componentNameListBox.addItem(object.getAlias(), object.getComponentId());
				aliases.put(object.getComponentId(), object.getAlias());
			}

			SelectElement selectElement = SelectElement.as(componentNameListBox.getElement());
			com.google.gwt.dom.client.NodeList<OptionElement> options = selectElement.getOptions();
			for (int i = 0; i < options.getLength(); i++) {
				String title = options.getItem(i).getText();
				OptionElement optionElement = options.getItem(i);
				optionElement.setTitle(title);
			}
		}
		
		setBadgeNumber(componentObjectsList.size(), badge);
		updateComponentSearchBox();
	}
	
	public void updateComponentSearchBox() {
		buildSearchAliasBox();
	}
	
	private void sortComponentsList(List<ComponentMeasureTabObject> objectList) {
		Collections.sort(objectList, new Comparator<ComponentMeasureTabObject>() {
			@Override
			public int compare(final ComponentMeasureTabObject object1, final ComponentMeasureTabObject object2) {
				return (object1.getAlias()).compareToIgnoreCase(object2.getAlias());
			}
		});
	}
	
	public void reset() {
		getRightHandNavPanel().clear();
		getViewIncludeLibrarys().clear();
		getViewParameterList().clear();
		getViewDefinitions().clear();
		getViewFunctions().clear();
		getViewIncludeLibrarys().clear();
		getIncludesCollapse().clear();
		getParamCollapse().clear();
		getDefineCollapse().clear();
		getFunctionCollapse().clear();
	}
	
	public void resetSelectedObjects() {
		setCurrentSelectedDefinitionObjId(null);
		setCurrentSelectedParamerterObjId(null);
		setCurrentSelectedFunctionObjId(null);
		setCurrentSelectedFunctionArgumentObjId(null);
		setCurrentSelectedFunctionArgumentName(null);
		setCurrentSelectedIncLibraryObjId(null);
	}

	public void resetActiveAnchorLists() {
		getComponentsTab().setActive(false);
		getIncludesLibrary().setActive(false);
		getAppliedQDM().setActive(false);
		getCodesLibrary().setActive(false);
		getFunctionLibrary().setActive(false);
		getParameterLibrary().setActive(false);
		getDefinitionLibrary().setActive(false);
		getCQLLibraryEditorTab().setActive(false);
	}
}