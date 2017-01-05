package mat.client.clause.cqlworkspace;

import java.util.List;

import org.gwtbootstrap3.client.ui.Label;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.LabelType;

import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import mat.client.CustomPager;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryModel;

public class CQLIncludeLibraryView {
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	
	/** The cell table panel. */
	private Panel cellTablePanel = new Panel();
	
	private PanelBody cellTablePanelBody = new PanelBody();
	
	/** The table. */
	private CellTable<CQLLibraryModel> table;
	
	/** The sort provider. */
	private ListDataProvider<CQLLibraryModel> listDataProvider;
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 5;
	
	/** The spager. */
	private MatSimplePager spager;
	/**
	 * Textbox aliasNameTxtArea.
	 */
	private TextBox aliasNameTxtArea = new TextBox();
	
	public CQLIncludeLibraryView(){
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.getElement().setId("vPanel_VerticalPanelIncludeSection");
		verticalPanel.add(new SpacerWidget());
		
		VerticalPanel aliasNameVP = new VerticalPanel();
		
		Label aliasLabel = new Label(LabelType.INFO, "Alias Name");
		aliasLabel.setMarginTop(5);
		aliasLabel.setId("Alias_Label");
		aliasNameTxtArea.setText("");
		aliasNameTxtArea.setSize("260px", "25px");
		aliasNameTxtArea.getElement().setId("aliasNameField_IncludeSection");
		aliasNameTxtArea.setName("aliasName");
		aliasLabel.setText("Alias Name");
		
		aliasNameVP.add(aliasLabel);
		aliasNameVP.add(new SpacerWidget());
		aliasNameVP.add(aliasNameTxtArea);
		
		verticalPanel.add(aliasNameVP);
		verticalPanel.add(cellTablePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.setWidth("700px");
		containerPanel.getElement().setAttribute("id",
				"IncludeSectionContainerPanel");
		containerPanel.add(verticalPanel);
		containerPanel.setStyleName("cqlqdsContentPanel");
		
		
		
	}

	public Widget asWidget() {
		return containerPanel;
	}

	public TextBox getAliasNameTxtArea() {
		return aliasNameTxtArea;
	}

	public void setAliasNameTxtArea(String string) {
		this.aliasNameTxtArea.setText("");
		
	}
	
	
	public void buildIncludeLibraryCellTable(List<CQLLibraryModel> cqlLibraryList, boolean isEditable) {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("95%");
		PanelHeader searchHeader = new PanelHeader();//new Label("QDM Elements");
		searchHeader.getElement().setId("searchHeader_Label_IncludeSection");
		searchHeader.setStyleName("measureGroupingTableHeader");
		searchHeader.getElement().setAttribute("tabIndex", "0");
		
		HTML searchHeaderText = new HTML("<strong>Include Libraries</strong>");
		searchHeader.add(searchHeaderText);
		cellTablePanel.add(searchHeader);
		if ((cqlLibraryList != null)
				&& (cqlLibraryList.size() > 0)) {
			table = new CellTable<CQLLibraryModel>();
			//setEditable(isEditable);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			listDataProvider = new ListDataProvider<CQLLibraryModel>();
			/*qdmSelectedList = new ArrayList<CQLLibraryModel>();*/
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();
			listDataProvider.getList().addAll(cqlLibraryList);
			ListHandler<CQLLibraryModel> sortHandler = new ListHandler<CQLLibraryModel>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			table = addColumnToTable(table, sortHandler, isEditable);
			listDataProvider.addDataDisplay(table);
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
			HTML desc = new HTML("<p> No Libraries available for include.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
	}
	
	/**
	 * Adds the column to table.
	 *
	 * @param table the table
	 * @param sortHandler the sort handler
	 * @param isEditable the is editable
	 * @return the cell table
	 */
	private CellTable<CQLLibraryModel> addColumnToTable(
			final CellTable<CQLLibraryModel> table,
			ListHandler<CQLLibraryModel> sortHandler, boolean isEditable) {
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
			Column<CQLLibraryModel, SafeHtml> nameColumn = new Column<CQLLibraryModel, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryModel object) {
					StringBuilder title = new StringBuilder();
					StringBuilder value = new StringBuilder();
					value = value.append(object.getLibraryName());
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
			Column<CQLLibraryModel, SafeHtml> versionColumn = new Column<CQLLibraryModel, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryModel object) {
					
					return CellTableUtility.getColumnToolTip(object.getVersionUsed());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Version\">"
							+ "Version" + "</span>"));
			
			// Version Column
			Column<CQLLibraryModel, SafeHtml> ownerColumn = new Column<CQLLibraryModel, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryModel object) {
					StringBuilder owner = new StringBuilder();
					owner = owner.append(object.getOwnerFirstName()).append(" ").append(object.getOwnerLastName());
					return CellTableUtility.getColumnToolTip(owner.toString(),
							owner.toString());
				}
			};
			table.addColumn(ownerColumn, SafeHtmlUtils
					.fromSafeConstant("<span title=\"Owner\">" + "Owner"
							+ "</span>"));
			
			String colName = "Modify";
			
			if(!isEditable){
				colName = "Select";
			}
			
			/*// Modify by Delete Column
			table.addColumn(new Column<CQLLibraryModel, CQLLibraryModel>(
					getCompositeCellForQDMModifyAndDelete(isEditable)) {
				
				@Override
				public CQLLibraryModel getValue(CQLLibraryModel object) {
					return object;
				}
			}, SafeHtmlUtils.fromSafeConstant("<span title='"+colName+"'>  "
					+ colName + "</span>"));*/
			
			
			table.setColumnWidth(0, 35.0, Unit.PCT);
			table.setColumnWidth(1, 35.0, Unit.PCT);
			table.setColumnWidth(2, 14.0, Unit.PCT);
			//table.setColumnWidth(3, 14.0, Unit.PCT);
			
		}
		table.setWidth("100%");
		return table;
	}
	
	
	
}
