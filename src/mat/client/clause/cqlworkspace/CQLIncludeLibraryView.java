package mat.client.clause.cqlworkspace;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.LabelType;

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

import edu.ycp.cs.dh.acegwt.client.ace.AceAnnotationType;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditor;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorMode;
import edu.ycp.cs.dh.acegwt.client.ace.AceEditorTheme;
import mat.client.CustomPager;
import mat.client.Mat;
import mat.client.shared.CQLButtonToolBar;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetBootStrap;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.util.CellTableUtility;
import mat.client.util.MatTextBox;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.shared.CQLErrors;

public class CQLIncludeLibraryView {
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	private FocusPanel searchWidgetFocusPanel = new FocusPanel();
	
	/** The cell table panel. */
	private Panel cellTablePanel = new Panel();
	
	private PanelBody cellTablePanelBody = new PanelBody();
	
	private AceEditor cqlAceEditor = new AceEditor();
	
	/** The table. */
	private CellTable<CQLLibraryDataSetObject> table;
	
	/** The sort provider. */
	private ListDataProvider<CQLLibraryDataSetObject> listDataProvider;
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 5;
	
	/** The spager. */
	private MatSimplePager spager;
	
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;
	
	private CQLButtonToolBar includesButtonBar = new CQLButtonToolBar("includes");
	
	private SearchWidgetBootStrap sWidget = new SearchWidgetBootStrap("Search", "Enter Search Text here");
	
	private MessageAlert warningMessageAlert = new WarningMessageAlert();
	
	private MessageAlert successMessageAlert = new SuccessMessageAlert();
	/**
	 * Textbox aliasNameTxtArea.
	 */
	private MatTextBox aliasNameTxtBox = new MatTextBox();
	private List<CQLLibraryDataSetObject> selectedList;
	private String selectedObject;
	private List<String> includedList;
	
	public CQLIncludeLibraryView(){
	
		getIncludesButtonBar().setStylePrimaryName("floatRightButtonPanel");
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanelIncludeSection");
		verticalPanel.add(new SpacerWidget());
		
		VerticalPanel aliasNameVP = new VerticalPanel();
		HorizontalPanel aliasLabelHP = new HorizontalPanel();
		Label aliasLabel = new Label(LabelType.INFO, "Alias Name");
		aliasLabel.setMarginTop(5);
		aliasLabel.setId("Alias_Label");
		aliasNameTxtBox.setText("");
		aliasNameTxtBox.setSize("260px", "25px");
		aliasNameTxtBox.getElement().setId("aliasNameField_IncludeSection");
		aliasNameTxtBox.setName("aliasName");
		aliasLabel.setText("Library Alias");
		
		VerticalPanel aliasLabelVP = new VerticalPanel();
		aliasLabelVP.add(aliasLabel);
		aliasLabelVP.add(new SpacerWidget());
		aliasLabelVP.add(aliasNameTxtBox);
		aliasLabelVP.setWidth("580px");
		aliasLabelVP.setStylePrimaryName("margintop20px");
		
		aliasLabelHP.add(aliasLabelVP);
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(new SpacerWidget());
		aliasLabelHP.add(createIncludesButtonBar());
		aliasNameVP.add(aliasLabelHP);
		
		VerticalPanel searchLibraryVP = new VerticalPanel();
		Label librariesLabel = new Label(LabelType.INFO, "Library");
		librariesLabel.setMarginTop(5);
		librariesLabel.setId("search_Lib_Lbl");
		librariesLabel.setTitle("Library Search");
		
		searchLibraryVP.add(new SpacerWidget());
		//searchLibraryVP.add(librariesLabel);
		//searchLibraryVP.add(new SpacerWidget());
		sWidget.getSearchBox().setWidth("590px");
		searchLibraryVP.add(sWidget.getSearchWidget());
		searchLibraryVP.add(new SpacerWidget());
		
		searchWidgetFocusPanel.add(searchLibraryVP);
		cqlAceEditor.startEditor();
		cqlAceEditor.setMode(AceEditorMode.CQL);
		cqlAceEditor.setTheme(AceEditorTheme.ECLIPSE);
		cqlAceEditor.getElement().getStyle().setFontSize(14, Unit.PX);
		cqlAceEditor.setSize("590px", "500px");
		cqlAceEditor.setAutocompleteEnabled(true);
		cqlAceEditor.setReadOnly(true);
		cqlAceEditor.setUseWrapMode(true);
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.redisplay();
		Label viewCQlFileLabel = new Label(LabelType.INFO);
		viewCQlFileLabel.setText("View CQL file here");
		viewCQlFileLabel.setTitle("View CQL file here");
		
		VerticalPanel viewCQLVP = new VerticalPanel();
		viewCQLVP.add(new SpacerWidget());
		viewCQLVP.add(new SpacerWidget());
		viewCQLVP.add(warningMessageAlert);
		viewCQLVP.add(successMessageAlert);
		viewCQLVP.add(new SpacerWidget());
		viewCQLVP.add(new SpacerWidget());
		viewCQLVP.add(viewCQlFileLabel);
		viewCQLVP.add(new SpacerWidget());
		viewCQLVP.add(cqlAceEditor);
		
		verticalPanel.add(aliasNameVP);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(searchWidgetFocusPanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(cellTablePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(viewCQLVP);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.setWidth("700px");
		containerPanel.getElement().setAttribute("id",
				"IncludeSectionContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
		
		
		
	}

	public MessageAlert getWarningMessageAlert() {
		warningMessageAlert.getElement().setAttribute("bg-color", "#ff3232");
		return warningMessageAlert; 
	}
	
	public Widget asWidget() {
		return containerPanel;
	}

	public HasKeyDownHandlers getFocusPanel(){
		return searchWidgetFocusPanel;
	}
	
	public TextBox getAliasNameTxtArea() {
		return aliasNameTxtBox;
	}

	public void setAliasNameTxtArea(String string) {
		this.aliasNameTxtBox.setText("");
		
	}
	
	private CQLButtonToolBar createIncludesButtonBar() {
		getIncludesButtonBar().getSaveButton().setVisible(true);
		getIncludesButtonBar().getEraseButton().setVisible(true);
		getIncludesButtonBar().getDeleteButton().setVisible(true);
		getIncludesButtonBar().getInfoButton().removeFromParent();
		getIncludesButtonBar().getInsertButton().removeFromParent();
		getIncludesButtonBar().getTimingExpButton().removeFromParent();
		return getIncludesButtonBar();
	}

	
	
	public void buildIncludeLibraryCellTable(List<CQLLibraryDataSetObject> cqlLibraryList, boolean isEditable) {
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
		//includedList = new ArrayList<String>();
		
		if ((cqlLibraryList != null)
				&& (cqlLibraryList.size() > 0)) {
			table = new CellTable<CQLLibraryDataSetObject>();
			//setEditable(isEditable);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<CQLLibraryDataSetObject>();
			/*qdmSelectedList = new ArrayList<CQLLibraryModel>();*/
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(cqlLibraryList);
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
					pagerResources, false, 0, true);
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
			HTML desc = new HTML("<p> No libraries available for include.</p>");
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
			/*Label searchHeader = new Label("QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("measureGroupingTableHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());*/
			
			//table.setSelectionModel(selectionModel);
			
			// Name Column
			Column<CQLLibraryDataSetObject, SafeHtml> nameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder();
					value = value.append(object.getCqlName());
					title = title.append("Name : ").append(value);

					title.append("");
					return CellTableUtility.getColumnToolTip(value.toString(),
							title.toString());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Name\">" + "Name"
							+ "</span>"));
			
			
			
			// Expansion Identifier Column
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
			
			
			table.setColumnWidth(0, 35.0, Unit.PCT);
			table.setColumnWidth(1, 35.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			table.setColumnWidth(3, 14.0, Unit.PCT);
			
		}
		table.setWidth("100%");
		return table;
	}

	
	private CompositeCell<CQLLibraryDataSetObject> getCheckBoxCellForTable(final boolean isEditable){
		final List<HasCell<CQLLibraryDataSetObject, ?>> cells = new LinkedList<HasCell<CQLLibraryDataSetObject, ?>>();
		cells.add(getCheckBoxCell());
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

				if(isEditable){
					if (selectedObject != null && object.getId().equals(selectedObject)){
						sb.appendHtmlConstant("<td class='emptySpaces'>");
					}  else if(includedList != null && includedList.contains(object.getId())){
						sb.appendHtmlConstant("<td class='emptySpaces' disabled=\"disabled\">");
					}  else {
						sb.appendHtmlConstant("<td class='emptySpaces'>");
					}
				} else {
					sb.appendHtmlConstant("<td class='emptySpaces' disabled=\"disabled\">");
				}
				
				if ((object != null)) {
					cell.render(context, hasCell.getValue(object), sb);
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
	
	
	private HasCell<CQLLibraryDataSetObject, Boolean> getCheckBoxCell(){
		HasCell<CQLLibraryDataSetObject, Boolean> hasCell = new HasCell<CQLLibraryDataSetObject, Boolean>() {
			
			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);
			
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
						getWarningMessageAlert().clearAlert();
						successMessageAlert.clearAlert();
						if(isCBChecked) {
							for (int i = 0; i < selectedList.size(); i++) {
								selectionModel.setSelected(selectedList.get(i), false);
							}
							selectedList.clear();
							selectedList.add(object);
							cqlAceEditor.clearAnnotations();
							cqlAceEditor.removeAllMarkers();
							cqlAceEditor.redisplay();
							if (!object.getCqlErrors().isEmpty()) {
								getWarningMessageAlert().createAlert(
										MatContext.get().getMessageDelegate().getVIEW_CQL_ERROR_MESSAGE());
								for (CQLErrors error : object.getCqlErrors()) {
									String errorMessage = new String();
									errorMessage = errorMessage.concat("Error in line : " + error.getErrorInLine() + " at Offset :"
											+ error.getErrorAtOffeset());
									int line = error.getErrorInLine();
									int column = error.getErrorAtOffeset();
									cqlAceEditor.addAnnotation(line - 1, column, error.getErrorMessage(),
											AceAnnotationType.WARNING);
								}
								cqlAceEditor.setText(object.getCqlText());
								cqlAceEditor.setAnnotations();
								cqlAceEditor.redisplay();
							} else {
								successMessageAlert.createAlert(MatContext.get().getMessageDelegate().getVIEW_CQL_NO_ERRORS_MESSAGE());
								cqlAceEditor.setText(object.getCqlText());
							}
						}
						else{
							for (int i = 0; i < selectedList.size(); i++) {
								if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									selectedList.remove(i);
									break;
								}
							}
							resetAceEditor();
						}
						selectionModel.setSelected(object, isCBChecked);
					}
				};
			}
		};
		return hasCell;
	}
	
	public void resetToDefault(){
		cellTablePanel.clear();
		resetAceEditor();
		successMessageAlert.clearAlert();
		warningMessageAlert.clearAlert();
	}

	private void resetAceEditor() {
		cqlAceEditor.clearAnnotations();
		cqlAceEditor.removeAllMarkers();
		cqlAceEditor.redisplay();
		cqlAceEditor.setText("");
	}
	
	
	public CQLButtonToolBar getIncludesButtonBar() {
		return this.includesButtonBar;
	}

	public void setIncludesButtonBar(CQLButtonToolBar includesButtonBar) {
		this.includesButtonBar = includesButtonBar;
	}
	
	public Button getSaveButton(){
		return getIncludesButtonBar().getSaveButton();
	}
	
	public Button getEraseButton(){
		return getIncludesButtonBar().getEraseButton();
	}
	
	public Button getDeleteButton(){
		return getIncludesButtonBar().getDeleteButton();
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

	
	/**
	 * This method enable/disable's search button
	 * and hide/show loading please wait message.
	 * @param busy
	 */
	public void showSearchingBusy(final boolean busy) {
		if (busy) {
			Mat.showLoadingMessage();
		} else {
			Mat.hideLoadingMessage();
		}
		getSearchButton().setEnabled(!busy);
		
	}
	
	
	
}
