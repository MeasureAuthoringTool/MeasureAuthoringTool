package mat.client.cqlworkspace.includedlibrary;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.CustomPager;
import mat.client.buttons.CQLIncludesButtonToolBar;
import mat.client.cqlworkspace.SharedCQLWorkspaceUtility;
import mat.client.inapphelp.component.InAppHelp;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.CQLWorkSpaceConstants;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.client.validator.ErrorHandler;
import mat.model.cql.CQLLibraryDataSetObject;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.FormGroup;
import org.gwtbootstrap3.client.ui.FormLabel;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.LabelType;
import org.gwtbootstrap3.client.ui.constants.PanelType;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CQLIncludeLibraryView {

	private static final int TABLE_ROW_COUNT = 5;
	
	private static final String STYLE = "style";
	private static final String REPLACE = "Replace";
	private static final String ALIAS_NAME = "aliasName";
	private static final String CQL_LIBRARY_VIEWER = "CQL Library Viewer";
	private static final String TWO_HUNDRED_AND_SIXTY_PIXELS = "260px";
	private static final String INCLUDE_LIBRARY_SUMMARY = "includeLibrarySummary";

	private String selectedObject;

	private List<String> includedList;
	private List<CQLLibraryDataSetObject> selectedList;

	private MatTextBox aliasNameTxtBox = new MatTextBox();
	private MatTextBox ownerNameTextBox = new MatTextBox();
	private MatTextBox cqlLibraryNameTextBox = new MatTextBox();
	
	private Map<String, CQLLibraryDataSetObject> replaceLibraries; 
	private Map<String, CQLLibraryDataSetObject> availableLibraries = new HashMap<>();
	
	private Panel cellTablePanel = new Panel();
	private PanelBody cellTablePanelBody = new PanelBody();
	private SimplePanel containerPanel = new SimplePanel();
	private SimplePanel searchWidgetFocusPanel = new SimplePanel();
	private VerticalPanel searchCellTablePanel = new VerticalPanel();
	private VerticalPanel ownerTextboxPanel = new VerticalPanel();
	
	private FormGroup aliasNameGroup = new FormGroup();	
	private CellTable<CQLLibraryDataSetObject> table;
	private ListDataProvider<CQLLibraryDataSetObject> listDataProvider;
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;
	
	private CQLIncludesButtonToolBar includesButtonBar = new CQLIncludesButtonToolBar("includes", true);
	private CQLIncludesButtonToolBar includesModifyButtonBar = new CQLIncludesButtonToolBar("includesAliasModify", false);
	
	private VerticalPanel buttonPanel = new VerticalPanel();
	
	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Search", "Enter Search Text here");
	
	private Observer observer;
	private AceEditor cqlAceEditor = new AceEditor();
	
	HTML heading = new HTML();
	
	private InAppHelp inAppHelp = new InAppHelp("");

	private ErrorHandler errorHandler = new ErrorHandler();
	
	public static interface Observer {
		void onCheckBoxClicked(CQLLibraryDataSetObject result);
	}
	
	public CQLIncludeLibraryView(){
		buttonPanel.clear();
		aliasNameGroup.clear();
		
		buttonPanel.getElement().setId("buttonPanel");
		getIncludesButtonBar().setStylePrimaryName("floatRightButtonPanel");
		
		heading.addStyleName("leftAligned");
		
		aliasNameGroup.add(buildAliasName());
		aliasNameGroup.add(new SpacerWidget());
		aliasNameGroup.add(aliasNameTxtBox);
		
		setMarginInButtonBar();

		buttonPanel.clear();
		buttonPanel.add(includesButtonBar);
		buttonPanel.getElement().setAttribute(STYLE, "margin-left:300px;");
		
		initCQLEditor();

		containerPanel.getElement().setAttribute("id", "IncludeSectionContainerPanel");
		containerPanel.add(buildCQLVP());
		containerPanel.setStyleName("cqlqdsContentPanel");
	}

	private Panel buildViewCQLPanel() {
		Panel viewCQLPanel = new Panel(PanelType.PRIMARY);	
		viewCQLPanel.setMarginTop(20);
		viewCQLPanel.setId("IncludeCQLViewPanel_Id");
		viewCQLPanel.add(buildVIewCQLHeader());
		viewCQLPanel.add(buildViewCQLBody());
		return viewCQLPanel;
	}

	private VerticalPanel buildCQLVP() {
		VerticalPanel verticalPanel = new VerticalPanel();

		verticalPanel.getElement().setId("vPanel_VerticalPanelIncludeSection");
		
		verticalPanel.add(SharedCQLWorkspaceUtility.buildHeaderPanel(heading, inAppHelp));
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(buildAliasLabelVP());
		verticalPanel.add(ownerTextboxPanel);
		verticalPanel.add(searchCellTablePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(buildViewCQLPanel());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.setWidth("700px");
		
		return verticalPanel;
	}

	private VerticalPanel buildAliasLabelVP() {
		VerticalPanel aliasNameVP = new VerticalPanel();
		
		VerticalPanel aliasLabelVP = new VerticalPanel();
		aliasLabelVP.add(aliasNameGroup);
		aliasLabelVP.setStylePrimaryName("margintop20px");

		HorizontalPanel aliasLabelHP = new HorizontalPanel();
		aliasLabelHP.add(aliasLabelVP);
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(buttonPanel);
		
		aliasNameVP.add(aliasLabelHP);
		
		return aliasNameVP;
	}

	private void initCQLEditor() {
		cqlAceEditor.startEditor();
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setText("");
		cqlAceEditor.setSize("650px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		cqlAceEditor.setUseWrapMode(true);
		cqlAceEditor.clearAnnotations();
	}

	private FormLabel buildAliasName() {
		FormLabel aliasLabel = new FormLabel();
		aliasLabel.setMarginTop(5);
		aliasLabel.setId("Alias_Label");
		aliasLabel.setTitle("Alias Name");
		aliasLabel.setText("Library Alias");
		aliasLabel.setFor("aliasNameField_IncludeSection");
		
		aliasNameTxtBox.setText("");
		aliasNameTxtBox.setSize(TWO_HUNDRED_AND_SIXTY_PIXELS, "25px");
		aliasNameTxtBox.getElement().setId("aliasNameField_IncludeSection");
		aliasNameTxtBox.setName(ALIAS_NAME);
		aliasNameTxtBox.setTitle("Enter Library Alias Required");
		aliasNameTxtBox.addBlurHandler(errorHandler.buildRequiredBlurHandler(aliasNameTxtBox));
		return aliasLabel;
	}

	private PanelBody buildViewCQLBody() {
		PanelBody viewCQLBody = new PanelBody();
		viewCQLBody.setId("IncludeCQLViewBody_Id");
		viewCQLBody.add(cqlAceEditor);
		return viewCQLBody;
	}

	private PanelHeader buildVIewCQLHeader() {
		PanelHeader viewCQLHeader = new PanelHeader();
		viewCQLHeader.setText(CQL_LIBRARY_VIEWER);
		viewCQLHeader.setTitle(CQL_LIBRARY_VIEWER);
		viewCQLHeader.setId("IncludeCQLViewPanelHeader_id");
		return viewCQLHeader;
	}
	
	private void setMarginInButtonBar() {
		includesButtonBar.getSaveButton().setMarginLeft(-30.00);
		includesButtonBar.getEraseButton().setMarginLeft(-10.00);
	}

	public void buildIncludesReadOnlyView(){
		ownerTextboxPanel.clear();
		searchCellTablePanel.clear();

		buttonPanel.clear();
		buttonPanel.getElement().setAttribute(STYLE, "margin-left:250px;");
		buttonPanel.add(includesModifyButtonBar);

		aliasNameTxtBox.setEnabled(false);

		ownerTextboxPanel.add(buildOwnerName());
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(ownerNameTextBox);
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(buildCQLLibraryName());
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(cqlLibraryNameTextBox);
		ownerTextboxPanel.add(new SpacerWidget());
		
		createReadOnlyViewIncludesButtonBar();
	}

	private FormLabel buildCQLLibraryName() {
		FormLabel cqlLibNameLabel = new FormLabel();
		cqlLibNameLabel.setMarginTop(5);
		cqlLibNameLabel.setId("cqlLibraryName_Label");
		cqlLibNameLabel.setText("CQL Library Name");
		cqlLibNameLabel.setTitle("CQL Library Name");
		cqlLibraryNameTextBox.setText("");
		cqlLibraryNameTextBox.setSize(TWO_HUNDRED_AND_SIXTY_PIXELS, "25px");
		cqlLibraryNameTextBox.getElement().setId("cqlLibraryNameField_IncludeSection");
		cqlLibraryNameTextBox.setName(ALIAS_NAME);
		cqlLibraryNameTextBox.setEnabled(false);
		return cqlLibNameLabel;
	}

	private FormLabel buildOwnerName() {
		FormLabel ownerLabel = new FormLabel();
		ownerLabel.setMarginTop(5);
		ownerLabel.setId("ownerName_Label");
		ownerLabel.setText("Owner Name");
		ownerLabel.setTitle("Owner Name");
		ownerNameTextBox.setText("");
		ownerNameTextBox.setSize(TWO_HUNDRED_AND_SIXTY_PIXELS, "25px");
		ownerNameTextBox.getElement().setId("ownerNameField_IncludeSection");
		ownerNameTextBox.setName(ALIAS_NAME);
		ownerNameTextBox.setEnabled(false);
		return ownerLabel;
	}
	
	public void buildAddNewAliasView() {
		
		searchCellTablePanel.clear();
		searchWidgetFocusPanel.clear();
		ownerTextboxPanel.clear();
		
		VerticalPanel searchLibraryVP = new VerticalPanel();
		Label librariesLabel = new Label(LabelType.INFO, "Library");
		librariesLabel.setMarginTop(5);
		librariesLabel.setId("search_Lib_Lbl");
		librariesLabel.setTitle("Library Search");

		searchLibraryVP.add(new SpacerWidget());

		SpacerWidget sw = new SpacerWidget();
		sWidget.getSearchBox().setWidth("590px");
		sWidget.getSearchBox().addBlurHandler(errorHandler.buildRequiredBlurHandler(sWidget.getSearchBox(), sw));
		searchLibraryVP.add(sWidget.getSearchWidget());
		searchLibraryVP.add(sw);

		searchWidgetFocusPanel.add(searchLibraryVP);

		searchCellTablePanel.add(searchWidgetFocusPanel);
		searchCellTablePanel.add(new SpacerWidget());
		
		searchCellTablePanel.add(cellTablePanel);
		buttonPanel.clear();
		buttonPanel.add(includesButtonBar);
		buttonPanel.getElement().setAttribute(STYLE, "margin-left:300px;");
		
		createIncludesButtonBar();
	}

	public Widget asWidget() {
		return containerPanel;
	}

	public MatTextBox getAliasNameTxtArea() {
		return aliasNameTxtBox;
	}

	public void setAliasNameTxtArea(String string) {
		this.aliasNameTxtBox.setText(string);
	}
	
	private CQLIncludesButtonToolBar createIncludesButtonBar() {
		includesButtonBar.getSaveButton().setVisible(true);
		includesButtonBar.getEraseButton().setVisible(true);
		return includesButtonBar;
	}

	private CQLIncludesButtonToolBar createReadOnlyViewIncludesButtonBar() {
		includesModifyButtonBar.getReplaceButton().setVisible(true);
		includesModifyButtonBar.getReplaceButton().setIcon(IconType.RETWEET);
		includesModifyButtonBar.getReplaceButton().setText(REPLACE);
		includesModifyButtonBar.getReplaceButton().setTitle(REPLACE);
		includesModifyButtonBar.getReplaceButton().setWidth("90px");
		includesModifyButtonBar.getReplaceButton().getElement().setAttribute("aria-label", REPLACE);
		includesModifyButtonBar.getReplaceButton().setMarginLeft(-70.00);

		includesModifyButtonBar.getDeleteButton().setVisible(true);
		includesModifyButtonBar.getDeleteButton().setEnabled(false);
		
		includesModifyButtonBar.getCancelButton().setMarginLeft(10.00);
		includesModifyButtonBar.getCancelButton().setVisible(true);
		
		return includesModifyButtonBar;
	}

	public void buildIncludeLibraryCellTable(SaveCQLLibraryResult result, boolean isEditable, boolean isIncludesTab) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("95%");
		PanelHeader searchHeader = new PanelHeader();
		searchHeader.getElement().setId("searchHeader_Label_IncludeSection");
		searchHeader.setStyleName("measureGroupingTableHeader");
		searchHeader.getElement().setAttribute("tabIndex", "-1");
		
		HTML searchHeaderText = new HTML("<strong>Available Libraries</strong>");
		searchHeader.add(searchHeaderText);
		cellTablePanel.add(searchHeader);
		
		selectedList = new ArrayList<>();
		selectedObject = null;
		
		if (result != null && !result.getCqlLibraryDataSetObjects().isEmpty()) {
			table = new CellTable<>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			listDataProvider = new ListDataProvider<>();
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
			ListHandler<CQLLibraryDataSetObject> sortHandler = new ListHandler<>(listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			selectionModel = new SingleSelectionModel<>();
			table.setSelectionModel(selectionModel);
			table = addColumnToTable(table, isEditable);
			listDataProvider.addDataDisplay(table);
			
			selectionModel.addSelectionChangeHandler(event -> updateSelectionChange());
			
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"cqlIncludes");
			spager.setDisplay(table);
			spager.setPageStart(0);
				
			table.getElement().setAttribute("id", "IncludeLibraryTable");
			table.getElement().setAttribute("aria-describedby", INCLUDE_LIBRARY_SUMMARY);

			cellTablePanelBody.add(buildIncludeLibrarySummaryInvisibleLabel());
			cellTablePanelBody.add(table);
			cellTablePanelBody.add(spager);
			cellTablePanel.add(cellTablePanelBody);
			
		} else {
			
			HTML desc = null;
			
			if(isIncludesTab){
				desc = new HTML("<p> Search to find available libraries.</p>");
			} else {
				desc = new HTML("<p> No available libraries.</p>");
			}
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
	}

	private com.google.gwt.user.client.ui.Label buildIncludeLibrarySummaryInvisibleLabel() {
		return (com.google.gwt.user.client.ui.Label) LabelBuilder
				.buildInvisibleLabel(INCLUDE_LIBRARY_SUMMARY, "In the Following Include Library table Name in First Column"
								+ "Version in Second Column, Owner in Third Column and Select in Fourth Column."
								+" The Library  are listed alphabetically in a table.");
	}
	
	private void updateSelectionChange() {
		CQLLibraryDataSetObject cqlLibraryDataSetObject  = selectionModel.getSelectedObject();
		if(cqlLibraryDataSetObject !=null) {
			for(CQLLibraryDataSetObject obj : listDataProvider.getList()){
				if(!obj.getId().equals(cqlLibraryDataSetObject.getId())){
					obj.setSelected(false);
					selectionModel.setSelected(obj, false);
				}
			}

			listDataProvider.refresh();
		}
	}
	
	private CellTable<CQLLibraryDataSetObject> addColumnToTable(final CellTable<CQLLibraryDataSetObject> table, boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			
			Column<CQLLibraryDataSetObject, SafeHtml> nameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					return CellTableUtility.getNameColumnToolTip(object.getCqlName(), object.getCqlName());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));
			
			
			Column<CQLLibraryDataSetObject, SafeHtml> versionColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					
					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + "</span>"));
			
			Column<CQLLibraryDataSetObject, SafeHtml> ownerColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					StringBuilder owner = new StringBuilder();
					owner = owner.append(object.getOwnerFirstName()).append(" ").append(object.getOwnerLastName());
					return CellTableUtility.getColumnToolTip(owner.toString(), owner.toString());
				}
			};
			table.addColumn(ownerColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));
			
			table.addColumn(new Column<CQLLibraryDataSetObject, CQLLibraryDataSetObject>(getCheckBoxCellForTable(isEditable)) {
				@Override
				public CQLLibraryDataSetObject getValue(CQLLibraryDataSetObject object) {
					return object;
				}
			}, "Select");
			
			
			table.setColumnWidth(0, 70.0, Unit.PCT);
			table.setColumnWidth(1, 10.0, Unit.PCT);
			table.setColumnWidth(2, 15.0, Unit.PCT);
			table.setColumnWidth(3,  5.0, Unit.PCT);
			
		}
		table.setWidth("100%");
		return table;
	}

	
	private CompositeCell<CQLLibraryDataSetObject> getCheckBoxCellForTable(final boolean isEditable){
		//checks to determine libraries selection validation limit.
		boolean isUsed = false;
		if(isEditable){
			isUsed = includedList.size() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT;
		} else {
			isUsed = true;
		}
		
		final List<HasCell<CQLLibraryDataSetObject, ?>> cells = new LinkedList<>();
		cells.add(getCheckBoxCell(isUsed));
		return new CompositeCell<CQLLibraryDataSetObject>(cells) {
			@Override
			public void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				for (HasCell<CQLLibraryDataSetObject, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected <X> void render(Context context, CQLLibraryDataSetObject object,
					SafeHtmlBuilder sb, HasCell<CQLLibraryDataSetObject, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces'>");
				
				if ((object != null)) {
					if(includedList != null && includedList.contains(object.getId())){
						sb.appendHtmlConstant("<i class=\"fa fa-check\" aria-hidden=\"true\" style=\"color:limegreen;\"></i>");
						sb.appendHtmlConstant("<span style=\"color: transparent;\">Yes</span>");
					} else {
						cell.render(context, hasCell.getValue(object), sb);
					}
					
				} else {
					sb.appendHtmlConstant("<span tabindex=\"-1\"></span>");
				}
				sb.appendHtmlConstant("</td>");
			}
			@Override
			protected Element getContainerElement(Element parent) {
				return parent.getFirstChildElement().getFirstChildElement()
						.getFirstChildElement();
			}
		};
		
	}
	
	private HasCell<CQLLibraryDataSetObject, Boolean> getCheckBoxCell(final boolean isUsed){
		return new HasCell<CQLLibraryDataSetObject, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(CQLLibraryDataSetObject object) {
				boolean isSelected = false;
				if (!selectedList.isEmpty()) {
					for (int i = 0; i < selectedList.size(); i++) {
						if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
							isSelected = true;
							selectionModel.setSelected(object, isSelected);
							selectedList.get(i).setSelected(true);
							break;
						} 
					}
			}
			
			else {
				if(selectedObject != null && object.getId().equals(selectedObject)){
					isSelected = true;
				} else {
					isSelected = false;
				}				
				
				selectionModel.setSelected(object, isSelected);
			}
				
				if(isSelected) {
					cell.setTitle("Click to remove " + object.getCqlName() + " as an included library");
				} else {
					cell.setTitle("Click to include " + object.getCqlName());
				}
				
				return isSelected;		

			}
			@Override
			public FieldUpdater<CQLLibraryDataSetObject, Boolean> getFieldUpdater() {
				return new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
					@Override
					public void update(int index, CQLLibraryDataSetObject object,
							Boolean isCBChecked) {

						if(isCBChecked) {
							for (int i = 0; i < selectedList.size(); i++) {
								selectionModel.setSelected(selectedList.get(i), false);
							}
							selectedList.clear();
							selectedList.add(object);
							observer.onCheckBoxClicked(object);
						}
						else{
							for (int i = 0; i < selectedList.size(); i++) {
								if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									selectedList.remove(i);
									break;
								}
							}

							cqlAceEditor.setText("");
						}
						
						if(isCBChecked) {
							cell.setTitle("Click to remove " + object.getCqlName() + " as an included library");
						} else {
							cell.setTitle("Click to include " + object.getCqlName());
						}	
						
						selectionModel.setSelected(object, isCBChecked);
					}
				};
			}
		};
	}
	
	public CQLIncludesButtonToolBar getIncludesButtonBar() {
		return this.includesButtonBar;
	}
	
	public CQLIncludesButtonToolBar getIncludesModifyButtonBar() {
		return includesModifyButtonBar;
	}

	public void resetToDefault(){
		cellTablePanel.clear();
		resetAceEditor();
	}

	private void resetAceEditor() {
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.removeAllMarkers();
		cqlAceEditor.setText("");
	}

	public void resetFromGroup(){
		getAliasNameGroup().setValidationState(ValidationState.NONE);
	}
	
	public void setIncludesButtonBar(CQLIncludesButtonToolBar includesButtonBar) {
		this.includesButtonBar = includesButtonBar;
	}
	
	public Button getSaveButton(){
		return getIncludesButtonBar().getSaveButton();
	}
	
	public Button getSaveModifyButton(){
		return getIncludesModifyButtonBar().getReplaceButton();
	}

	public Button getEraseButton(){
		return getIncludesButtonBar().getEraseButton();
	}
	
	public Button getDeleteButton(){
		return getIncludesModifyButtonBar().getDeleteButton();
	}
	
	public Button getCloseButton(){
		return getIncludesModifyButtonBar().getCancelButton();
	}
	
	public Button getSearchButton(){
		return sWidget.getGo();
	}
	
	public TextBox getSearchTextBox(){
		return sWidget.getSearchBox();
	}
	
	public AceEditor getViewCQLEditor(){
		return cqlAceEditor;
	}
	
	public List<CQLLibraryDataSetObject> getSelectedObjectList(){
		return selectedList;
	}
	
	public void setSelectedObjectList(List<CQLLibraryDataSetObject> selectedObjectList){
		selectedList = selectedObjectList;
	}

	public String getSelectedObject() {
		return selectedObject;
	}

	public void setSelectedObject(String selectedObject) {
		this.selectedObject = selectedObject;
	}
	
	public void redrawCellTable(){
		table.redraw();
	}

	public List<String> getIncludedList() {
		return includedList;
	}

	public void setIncludedList(List<String> includedList) {
		this.includedList = includedList;
	}

	public VerticalPanel getSearchCellTablePanel() {
		return searchCellTablePanel;
	}

	public VerticalPanel getOwnerTextboxPanel() {
		return ownerTextboxPanel;
	}

	public MatTextBox getOwnerNameTextBox() {
		return ownerNameTextBox;
	}

	public MatTextBox getCqlLibraryNameTextBox() {
		return cqlLibraryNameTextBox;
	}

	public Observer getObserver() {
		return observer;
	}

	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	public void setWidgetReadOnly(boolean isEditable) {

		getAliasNameTxtArea().setEnabled(isEditable);
		getIncludesButtonBar().getSaveButton().setEnabled(isEditable);
		getIncludesButtonBar().getEraseButton().setEnabled(isEditable);
	}

	public FormGroup getAliasNameGroup() {
		return aliasNameGroup;
	}

	public void setAliasNameGroup(FormGroup aliasNameGroup) {
		this.aliasNameGroup = aliasNameGroup;
	}
	
	public Map<String, CQLLibraryDataSetObject> getReplaceLibraries() {
		return replaceLibraries;
	}

	public void setReplaceLibraries(Map<String, CQLLibraryDataSetObject> replaceLibraries) {
		this.replaceLibraries = replaceLibraries;
	}

	public Map<String, CQLLibraryDataSetObject> getAvailableLibraries() {
		return availableLibraries;
	}

	public void setAvailableLibraries(Map<String, CQLLibraryDataSetObject> availableLibraries) {
		this.availableLibraries = availableLibraries;
	}
	
	public void setHeading(String text,String linkName) {
		String linkStr = SkipListBuilder.buildEmbeddedString(linkName);
		heading.setHTML(linkStr +"<h4><b>" + text + "</b></h4>");
	}
	
	/**
	 * Added this method as part of MAT-8882.
	 * @param isEditable
	 */
	public void setIsEditable(boolean isEditable) {		
		getSaveButton().setEnabled(isEditable);
		getEraseButton().setEnabled(isEditable);
		getSearchButton().setEnabled(isEditable);
	}

	public InAppHelp getInAppHelp() {
		return inAppHelp;
	}

	public void setInAppHelp(InAppHelp inAppHelp) {
		this.inAppHelp = inAppHelp;
	}

}