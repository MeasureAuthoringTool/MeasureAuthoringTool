package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.CustomPager;
import mat.client.buttons.Definition_FunctionButtonToolBar;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SkipListBuilder;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLLibraryDataSetObject;

/**
 * The Class CQLIncludeLibraryView.
 */
public class CQLIncludeLibraryView {
	private SimplePanel containerPanel = new SimplePanel();
	private FocusPanel searchWidgetFocusPanel = new FocusPanel();
	private Panel cellTablePanel = new Panel();
	private PanelBody cellTablePanelBody = new PanelBody();
	private AceEditor cqlAceEditor = new AceEditor();
	private CellTable<CQLLibraryDataSetObject> table;
	private ListDataProvider<CQLLibraryDataSetObject> listDataProvider;
	private static final int TABLE_ROW_COUNT = 5;
	private MatSimplePager spager;
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;
	private Definition_FunctionButtonToolBar includesButtonBar = new Definition_FunctionButtonToolBar("includes");
	private Definition_FunctionButtonToolBar includesModifyButtonBar = new Definition_FunctionButtonToolBar("includesAliasModify");
	private VerticalPanel buttonPanel = new VerticalPanel();
	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Search", "Enter Search Text here");
	private MatTextBox aliasNameTxtBox = new MatTextBox();
	private MatTextBox ownerNameTextBox = new MatTextBox();
	private MatTextBox cqlLibraryNameTextBox = new MatTextBox();
	private List<CQLLibraryDataSetObject> selectedList;
	private Map<String, CQLLibraryDataSetObject> replaceLibraries; 
	private Map<String,CQLLibraryDataSetObject> availableLibraries = new HashMap<String,CQLLibraryDataSetObject>();
	private String selectedObject;
	private List<String> includedList;
	private VerticalPanel searchCellTablePanel = new VerticalPanel();
	private VerticalPanel ownerTextboxPanel = new VerticalPanel();
	private Observer observer;
	private FormGroup aliasNameGroup = new FormGroup();	
	HTML heading = new HTML();
	
	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		void onCheckBoxClicked(CQLLibraryDataSetObject result);
		
	}
	
	
	/**
	 * Instantiates a new CQL include library view.
	 */
	public CQLIncludeLibraryView(){
		buttonPanel.clear();
		aliasNameGroup.clear();
		buttonPanel.getElement().setId("buttonPanel");
		getIncludesButtonBar().setStylePrimaryName("floatRightButtonPanel");
		heading.addStyleName("leftAligned");
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanelIncludeSection");
		verticalPanel.add(heading);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		VerticalPanel aliasNameVP = new VerticalPanel();
		HorizontalPanel aliasLabelHP = new HorizontalPanel();
		
		FormLabel aliasLabel = new FormLabel();
		aliasLabel.setMarginTop(5);
		aliasLabel.setId("Alias_Label");
		aliasLabel.setTitle("Alias Name");
		aliasLabel.setText("Library Alias");
		aliasLabel.setFor("aliasNameField_IncludeSection");
		
		aliasNameTxtBox.setText("");
		aliasNameTxtBox.setSize("260px", "25px");
		aliasNameTxtBox.getElement().setId("aliasNameField_IncludeSection");
		aliasNameTxtBox.setName("aliasName");
		aliasNameTxtBox.setTitle("Enter Library Alias");
		
		aliasNameGroup.add(aliasLabel);
		aliasNameGroup.add(new SpacerWidget());
		aliasNameGroup.add(aliasNameTxtBox);
		
		setMarginInButtonBar();
		
		VerticalPanel aliasLabelVP = new VerticalPanel();
		
		aliasLabelVP.add(aliasNameGroup);
		
		aliasLabelVP.setStylePrimaryName("margintop20px");
		buttonPanel.clear();
		buttonPanel.add(includesButtonBar);
		buttonPanel.getElement().setAttribute("style", "margin-left:300px;");
		aliasLabelHP.add(aliasLabelVP);
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(buttonPanel);
		aliasNameVP.add(aliasLabelHP);
		
		
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
		
		Label viewCQlFileLabel = new Label(LabelType.INFO);
		viewCQlFileLabel.setText("View CQL file here");
		viewCQlFileLabel.setTitle("View CQL file here");
		
		Panel viewCQLPanel = new Panel(PanelType.PRIMARY);	
		viewCQLPanel.setMarginTop(20);
		viewCQLPanel.setId("IncludeCQLViewPanel_Id");
		
		PanelHeader viewCQLHeader = new PanelHeader();
		viewCQLHeader.setText("View CQL file here");
		viewCQLHeader.setTitle("View CQL file here");
		viewCQLHeader.setId("IncludeCQLViewPanelHeader_id");
		
		PanelBody viewCQLBody = new PanelBody();
		viewCQLBody.setId("IncludeCQLViewBody_Id");
		viewCQLBody.add(cqlAceEditor);
		
		viewCQLPanel.add(viewCQLHeader);
		viewCQLPanel.add(viewCQLBody);
		
		verticalPanel.add(aliasNameVP);
		verticalPanel.add(ownerTextboxPanel);
		verticalPanel.add(searchCellTablePanel);
		
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(viewCQLPanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.setWidth("700px");
		containerPanel.getElement().setAttribute("id",
				"IncludeSectionContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
	}
	
	
	/**
	 * Sets the margin in button bar.
	 */
	private void setMarginInButtonBar() {
		includesButtonBar.getSaveButton().setMarginLeft(-30.00);
		includesButtonBar.getEraseButton().setMarginLeft(-10.00);
	}

	/**
	 * Builds the owner text box widget.
	 */
	public void buildIncludesReadOnlyView(){
		ownerTextboxPanel.clear();
		searchCellTablePanel.clear();
		aliasNameTxtBox.setEnabled(false);
		buttonPanel.clear();
		buttonPanel.getElement().setAttribute("style", "margin-left:250px;");
		buttonPanel.add(includesModifyButtonBar);

		FormLabel ownerLabel = new FormLabel();
		ownerLabel.setMarginTop(5);
		ownerLabel.setId("ownerName_Label");
		ownerLabel.setText("Owner Name");
		ownerLabel.setTitle("Owner Name");
		ownerNameTextBox.setText("");
		ownerNameTextBox.setSize("260px", "25px");
		ownerNameTextBox.getElement().setId("ownerNameField_IncludeSection");
		ownerNameTextBox.setName("aliasName");
		ownerNameTextBox.setEnabled(false);

		FormLabel cqlLibNameLabel = new FormLabel();
		cqlLibNameLabel.setMarginTop(5);
		cqlLibNameLabel.setId("cqlLibraryName_Label");
		cqlLibNameLabel.setText("CQL Library Name");
		cqlLibNameLabel.setTitle("CQL Library Name");
		cqlLibraryNameTextBox.setText("");
		cqlLibraryNameTextBox.setSize("260px", "25px");
		cqlLibraryNameTextBox.getElement().setId("cqlLibraryNameField_IncludeSection");
		cqlLibraryNameTextBox.setName("aliasName");
		cqlLibraryNameTextBox.setEnabled(false);
		
		ownerTextboxPanel.add(ownerLabel);
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(ownerNameTextBox);
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(cqlLibNameLabel);
		ownerTextboxPanel.add(new SpacerWidget());
		ownerTextboxPanel.add(cqlLibraryNameTextBox);
		ownerTextboxPanel.add(new SpacerWidget());
		createReadOnlyViewIncludesButtonBar();
	}
	
	
	/**
	 * Buildsearch cell table widget.
	 */
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

		sWidget.getSearchBox().setWidth("590px");
		searchLibraryVP.add(sWidget.getSearchWidget());
		searchLibraryVP.add(new SpacerWidget());

		searchWidgetFocusPanel.add(searchLibraryVP);

		searchCellTablePanel.add(searchWidgetFocusPanel);
		searchCellTablePanel.add(new SpacerWidget());
		
		searchCellTablePanel.add(cellTablePanel);
		buttonPanel.clear();
		buttonPanel.add(includesButtonBar);
		buttonPanel.getElement().setAttribute("style", "margin-left:300px;");
		createIncludesButtonBar();
	}

	/**
	 * As widget.
	 *
	 * @return the widget
	 */
	public Widget asWidget() {
		return containerPanel;
	}

	/**
	 * Gets the focus panel.
	 *
	 * @return the focus panel
	 */
	public HasKeyDownHandlers getFocusPanel(){
		return searchWidgetFocusPanel;
	}
	
	/**
	 * Gets the alias name txt area.
	 *
	 * @return the alias name txt area
	 */
	public TextBox getAliasNameTxtArea() {
		return aliasNameTxtBox;
	}

	/**
	 * Sets the alias name txt area.
	 *
	 * @param string the new alias name txt area
	 */
	public void setAliasNameTxtArea(String string) {
		this.aliasNameTxtBox.setText("");
		
	}
	
	/**
	 * Creates the includes button bar.
	 *
	 * @return the CQL button tool bar
	 */
<<<<<<< HEAD
	private Definition_FuntionButtonToolBar createIncludesButtonBar() {
=======
	private Definition_FunctionButtonToolBar createIncludesButtonBar() {
>>>>>>> fbe7f0aeb31800f29768e9d55e7b99601e924fb4
		includesButtonBar.getSaveButton().setVisible(true);
		includesButtonBar.getEraseButton().setVisible(true);
		includesButtonBar.getCloseButton().setVisible(false);
		includesButtonBar.getDeleteButton().setVisible(false);
		includesButtonBar.getInfoButton().removeFromParent();
		includesButtonBar.getInsertButton().removeFromParent();
		includesButtonBar.getTimingExpButton().removeFromParent();
		return includesButtonBar;
	}
	
	/**
	 * Creates the read only view includes button bar.
	 *
	 * @return the CQL button tool bar
	 */
<<<<<<< HEAD
	private Definition_FuntionButtonToolBar createReadOnlyViewIncludesButtonBar() {
=======
	private Definition_FunctionButtonToolBar createReadOnlyViewIncludesButtonBar() {
>>>>>>> fbe7f0aeb31800f29768e9d55e7b99601e924fb4
		includesModifyButtonBar.getDeleteButton().setVisible(true);
		includesModifyButtonBar.getEditButton().setVisible(true);
		includesModifyButtonBar.getEditButton().setIcon(IconType.RETWEET);
		includesModifyButtonBar.getEditButton().setText("Replace");
		includesModifyButtonBar.getEditButton().setTitle("Replace");
		includesModifyButtonBar.getEditButton().setWidth("90px");
		includesModifyButtonBar.getEditButton().getElement().setAttribute("aria-label", "Replace");
		includesModifyButtonBar.getEditButton().setMarginLeft(-70.00);
		
		includesModifyButtonBar.getCloseButton().setMarginLeft(10.00);
		includesModifyButtonBar.getDeleteButton().setEnabled(false);
		includesModifyButtonBar.getCloseButton().setVisible(true);
		includesModifyButtonBar.getSaveButton().setVisible(true);
		includesModifyButtonBar.getSaveButton().setEnabled(false);
		includesModifyButtonBar.getInfoButton().removeFromParent();
		includesModifyButtonBar.getInsertButton().removeFromParent();
		includesModifyButtonBar.getTimingExpButton().removeFromParent();
		includesModifyButtonBar.getSaveButton().removeFromParent();
		includesModifyButtonBar.getEraseButton().removeFromParent();
		return includesModifyButtonBar;
	}

	
	
	/**
	 * Builds the include library cell table.
	 *
	 * @param result the cql library list
	 * @param isEditable the is editable
	 * @param isIncludesTab
	 */
	public void buildIncludeLibraryCellTable(SaveCQLLibraryResult result, boolean isEditable, boolean isIncludesTab) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("95%");
		PanelHeader searchHeader = new PanelHeader();//new Label("QDM Elements");
		searchHeader.getElement().setId("searchHeader_Label_IncludeSection");
		searchHeader.setStyleName("measureGroupingTableHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Available Libraries</strong>");
		searchHeader.add(searchHeaderText);
		cellTablePanel.add(searchHeader);
		
		selectedList = new ArrayList<CQLLibraryDataSetObject>();
		selectedObject = null;
		
		if ((result != null)
				&& (result.getCqlLibraryDataSetObjects().size() > 0)) {
			table = new CellTable<CQLLibraryDataSetObject>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<CQLLibraryDataSetObject>();
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
			ListHandler<CQLLibraryDataSetObject> sortHandler = new ListHandler<CQLLibraryDataSetObject>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			selectionModel = new SingleSelectionModel<CQLLibraryDataSetObject>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			table.setSelectionModel(selectionModel);
			table = addColumnToTable(table, sortHandler, isEditable);
			listDataProvider.addDataDisplay(table);
			addSelectionHandler();
			CustomPager.Resources pagerResources = GWT
					.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER,
					pagerResources, false, 0, true,"cqlIncludes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel;
			if(isEditable){
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"includeLibrarySummary",
								"In the Following Include Library table Name in First Column"
										+ "Version in Second Column, Owner in Third Column and Select in Fourth Column."
										+" The Library  are listed alphabetically in a table.");
				
				
			} else {
				invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder
						.buildInvisibleLabel(
								"includeLibrarySummary",
								"In the Following Include Library table Name in First Column"
										+ "Version in Second Column, Owner in Third Column and Select in Fourth Column."
										+" The Library  are listed alphabetically in a table.");
			}
			table.getElement().setAttribute("id", "IncludeLibraryTable");
			table.getElement().setAttribute("aria-describedby",
					"includeLibrarySummary");
			
			cellTablePanelBody.add(invisibleLabel);
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
	
	/**
	 * Selection Change Handler for Selection Model to make checkbox behave like radio button.
	 */
	private void addSelectionHandler() {
		selectionModel.addSelectionChangeHandler(new Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				CQLLibraryDataSetObject selectedObject  = selectionModel.getSelectedObject();
				if(selectedObject !=null) {
					for(CQLLibraryDataSetObject obj : listDataProvider.getList()){
						if(!obj.getId().equals(selectedObject.getId())){
							obj.setSelected(false);
							selectionModel.setSelected(obj, false);
						}
					}
					
					listDataProvider.refresh();
				}
				
			}
		});
		
	}

	/**
	 * Adds the column to table.
	 *
	 * @param table the table
	 * @param sortHandler the sort handler
	 * @param isEditable the is editable
	 * @return the cell table
	 */
	private CellTable<CQLLibraryDataSetObject> addColumnToTable(
			final CellTable<CQLLibraryDataSetObject> table,
			ListHandler<CQLLibraryDataSetObject> sortHandler, boolean isEditable) {
		if (table.getColumnCount() != TABLE_ROW_COUNT ) {
			
			Column<CQLLibraryDataSetObject, SafeHtml> nameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					return CellTableUtility.getNameColumnToolTip(object.getCqlName(), object.getCqlName());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Name\">" + "Name"
							+ "</span>"));
			
			
			Column<CQLLibraryDataSetObject, SafeHtml> versionColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					
					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Version\">"
							+ "Version" + "</span>"));
			
			// Version Column
			Column<CQLLibraryDataSetObject, SafeHtml> ownerColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					StringBuilder owner = new StringBuilder();
					owner = owner.append(object.getOwnerFirstName()).append(" ").append(object.getOwnerLastName());
					return CellTableUtility.getColumnToolTip(owner.toString(),
							owner.toString());
				}
			};
			table.addColumn(ownerColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Owner\">" + "Owner"
							+ "</span>"));
			
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

	
	/**
	 * Gets the check box cell for table.
	 *
	 * @param isEditable the is editable
	 * @return the check box cell for table
	 */
	private CompositeCell<CQLLibraryDataSetObject> getCheckBoxCellForTable(final boolean isEditable){
		//checks to determine libraries selection validation limit.
		boolean isUsed = false;
		if(isEditable){
			isUsed = includedList.size() >= CQLWorkSpaceConstants.VALID_INCLUDE_COUNT;
		} else {
			isUsed = true;
		}
		
		final List<HasCell<CQLLibraryDataSetObject, ?>> cells = new LinkedList<HasCell<CQLLibraryDataSetObject, ?>>();
		cells.add(getCheckBoxCell(isUsed));
		CompositeCell<CQLLibraryDataSetObject> cell = new CompositeCell<CQLLibraryDataSetObject>(cells) {
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
		
		return cell;
		
	}
	
	
	/**
	 * Gets the check box cell.
	 *
	 * @param isUsed the is used
	 * @return the check box cell
	 */
	private HasCell<CQLLibraryDataSetObject, Boolean> getCheckBoxCell(final boolean isUsed){
		HasCell<CQLLibraryDataSetObject, Boolean> hasCell = new HasCell<CQLLibraryDataSetObject, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true, isUsed);
			
			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}
			@Override
			public Boolean getValue(CQLLibraryDataSetObject object) {
				boolean isSelected = false;
				if (selectedList.size() > 0) {
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
						selectionModel.setSelected(object, isCBChecked);
					}
				};
			}
		};
		return hasCell;
	}
	
	/**
	 * Gets the includes button bar.
	 *
	 * @return the includes button bar
	 */
<<<<<<< HEAD
	public Definition_FuntionButtonToolBar getIncludesButtonBar() {
		return this.includesButtonBar;
	}
	
	public Definition_FuntionButtonToolBar getIncludesModifyButtonBar() {
=======
	public Definition_FunctionButtonToolBar getIncludesButtonBar() {
		return this.includesButtonBar;
	}
	
	public Definition_FunctionButtonToolBar getIncludesModifyButtonBar() {
>>>>>>> fbe7f0aeb31800f29768e9d55e7b99601e924fb4
		return includesModifyButtonBar;
	}


	/**
	 * Reset to default.
	 */
	public void resetToDefault(){
		cellTablePanel.clear();
		resetAceEditor();
	}

	/**
	 * Reset ace editor.
	 */
	private void resetAceEditor() {
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.removeAllMarkers();
		cqlAceEditor.setText("");
	}

	/**
	 * Reset from group.
	 */
	public void resetFromGroup(){
		getAliasNameGroup().setValidationState(ValidationState.NONE);
	}
	

	/**
	 * Sets the includes button bar.
	 *
	 * @param includesButtonBar the new includes button bar
	 */
<<<<<<< HEAD
	public void setIncludesButtonBar(Definition_FuntionButtonToolBar includesButtonBar) {
=======
	public void setIncludesButtonBar(Definition_FunctionButtonToolBar includesButtonBar) {
>>>>>>> fbe7f0aeb31800f29768e9d55e7b99601e924fb4
		this.includesButtonBar = includesButtonBar;
	}
	
	/**
	 * Gets the save button.
	 *
	 * @return the save button
	 */
	public Button getSaveButton(){
		return getIncludesButtonBar().getSaveButton();
	}
	
	public Button getSaveModifyButton(){
		return getIncludesModifyButtonBar().getEditButton();
	}
	/**
	 * Gets the erase button.
	 *
	 * @return the erase button
	 */
	public Button getEraseButton(){
		return getIncludesButtonBar().getEraseButton();
	}
	
	/**
	 * Gets the delete button.
	 *
	 * @return the delete button
	 */
	public Button getDeleteButton(){
		return getIncludesModifyButtonBar().getDeleteButton();
	}
	
	
	/**
	 * Gets the close button.
	 *
	 * @return the close button
	 */
	public Button getCloseButton(){
		return getIncludesModifyButtonBar().getCloseButton();
	}
	
	/**
	 * Gets the search button.
	 *
	 * @return the search button
	 */
	public Button getSearchButton(){
		return sWidget.getGo();
	}
	
	/**
	 * Gets the search text box.
	 *
	 * @return the search text box
	 */
	public TextBox getSearchTextBox(){
		return sWidget.getSearchBox();
	}
	
	/**
	 * Gets the view CQL editor.
	 *
	 * @return the view CQL editor
	 */
	public AceEditor getViewCQLEditor(){
		return cqlAceEditor;
	}
	
	/**
	 * Gets the selected object list.
	 *
	 * @return the selected object list
	 */
	public List<CQLLibraryDataSetObject> getSelectedObjectList(){
		return selectedList;
	}
	
	/**
	 * Sets the selected object list.
	 *
	 * @param selectedObjectList the new selected object list
	 */
	public void setSelectedObjectList(List<CQLLibraryDataSetObject> selectedObjectList){
		selectedList = selectedObjectList;
	}

	/**
	 * Gets the selected object.
	 *
	 * @return the selected object
	 */
	public String getSelectedObject() {
		return selectedObject;
	}

	/**
	 * Sets the selected object.
	 *
	 * @param selectedObject the new selected object
	 */
	public void setSelectedObject(String selectedObject) {
		this.selectedObject = selectedObject;
	}
	
	/**
	 * Redraw cell table.
	 */
	public void redrawCellTable(){
		table.redraw();
	}

	/**
	 * Gets the included list.
	 *
	 * @return the included list
	 */
	public List<String> getIncludedList() {
		return includedList;
	}

	/**
	 * Sets the included list.
	 *
	 * @param includedList the new included list
	 */
	public void setIncludedList(List<String> includedList) {
		this.includedList = includedList;
	}

	/**
	 * Gets the search cell table panel.
	 *
	 * @return the search cell table panel
	 */
	public VerticalPanel getSearchCellTablePanel() {
		return searchCellTablePanel;
	}

	/**
	 * Gets the owner text box panel.
	 *
	 * @return the owner text box panel
	 */
	public VerticalPanel getOwnerTextboxPanel() {
		return ownerTextboxPanel;
	}

	/**
	 * Gets the owner name text box.
	 *
	 * @return the owner name text box
	 */
	public MatTextBox getOwnerNameTextBox() {
		return ownerNameTextBox;
	}

	/**
	 * Gets the cql library name text box.
	 *
	 * @return the cql library name text box
	 */
	public MatTextBox getCqlLibraryNameTextBox() {
		return cqlLibraryNameTextBox;
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
	
	/**
	 * Sets the widget read only.
	 *
	 * @param isEditable the new widget read only
	 */
	public void setWidgetReadOnly(boolean isEditable) {

		getAliasNameTxtArea().setEnabled(isEditable);
		getIncludesButtonBar().getSaveButton().setEnabled(isEditable);
		getIncludesButtonBar().getEraseButton().setEnabled(isEditable);
	}


	/**
	 * Gets the alias name group.
	 *
	 * @return the alias name group
	 */
	public FormGroup getAliasNameGroup() {
		return aliasNameGroup;
	}


	/**
	 * Sets the alias name group.
	 *
	 * @param aliasNameGroup the new alias name group
	 */
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
	public void setReadOnly(boolean isEditable) {		
		getSaveButton().setEnabled(isEditable);
		getEraseButton().setEnabled(isEditable);
		getSearchButton().setEnabled(isEditable);
	}

}
