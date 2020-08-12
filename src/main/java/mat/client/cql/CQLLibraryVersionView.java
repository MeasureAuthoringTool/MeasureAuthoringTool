package mat.client.cql;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.CqlLibraryPresenter;
import mat.client.CustomPager;
import mat.client.buttons.SaveContinueCancelButtonBar;
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ConfirmationDialogBox;
import mat.client.shared.ConfirmationObserver;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SpacerWidget;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;
import org.gwtbootstrap3.client.ui.Button;

import java.util.ArrayList;
import java.util.List;

public class CQLLibraryVersionView implements CqlLibraryPresenter.VersionDisplay{
	private static final int PAGE_SIZE = 25;
	
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	private FlowPanel mainPanel = new FlowPanel();
	
	private RadioButton majorRadio = new RadioButton("group", "Major");
	
	private RadioButton minorRadio = new RadioButton("group", "Minor");
	
	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();
	
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;
	
	private SaveContinueCancelButtonBar buttonBar = new SaveContinueCancelButtonBar("cqlVersion");
	
	CQLLibraryDataSetObject selectedLibraryObject;
	
	ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox();
	
	public CQLLibraryVersionView(){
		mainPanel.setStylePrimaryName("contentPanel");
		mainPanel.addStyleName("leftAligned");
		majorRadio.getElement().setId("CQL_MajorRadioButton");
		minorRadio.getElement().setId("CQL_MinorRadioButton");		
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(errorMessages);
		errorMessages.getElement().setId("errorMessages_ErrorMessageDisplay");
		
		VerticalPanel radioPanel = new VerticalPanel();
		radioPanel.getElement().getStyle().setMarginLeft(5, Unit.PX);
		Label radioLabel = new Label("Select Version Type");
		radioLabel.setTitle("Select Version Type Required");
		radioLabel.getElement().setTabIndex(0);
		radioPanel.add(radioLabel);
		radioPanel.add(new SpacerWidget());
		radioPanel.add(majorRadio);
		majorRadio.getElement().setId("cqlmajorRadio_RadioButton");
		radioPanel.add(minorRadio);
		minorRadio.getElement().setId("cqlminorRadio_RadioButton");
		mainPanel.add(cellTablePanel);
		mainPanel.add(new SpacerWidget());
		mainPanel.add(radioPanel);
		mainPanel.add(new SpacerWidget());
		
		mainPanel.add(buttonBar);
	}
	
	
	
	public void buildHTML(){
		cellTablePanel.clear();
		if(selectedLibraryObject != null){
			String selectedItemName = selectedLibraryObject.getCqlName();
			String selectedItemDraftText = selectedLibraryObject.getVersion();
			StringBuilder paragraph = new StringBuilder("<p>You are creating version of <b>\""+ selectedItemName + " " + selectedItemDraftText +"\"</b>");
			paragraph.append("</p>");
			HTML paragraphHtml = new HTML(paragraph.toString());
			cellTablePanel.add(paragraphHtml);
		}
		
	}

	private CellTable<CQLLibraryDataSetObject> addColumnToTable(final CellTable<CQLLibraryDataSetObject> cellTable) {
		Column<CQLLibraryDataSetObject, Boolean> radioButtonColumn = new Column<CQLLibraryDataSetObject, Boolean>(new RadioButtonCell(true, true)) {
			@Override
			public Boolean getValue(CQLLibraryDataSetObject result) {
				return cellTable.getSelectionModel().isSelected(result);
			}
			
			
		};
		radioButtonColumn.setFieldUpdater(new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
			@Override
			public void update(int index, CQLLibraryDataSetObject object, Boolean value) {
				cellTable.getSelectionModel().setSelected(object, true);
			}
		});
		
		cellTable.addColumn(radioButtonColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Select\">"
				+ "Select" + "</span>"));
		Column<CQLLibraryDataSetObject, SafeHtml> libraryNameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				String title = "Library Name " + object.getCqlName();
				return CellTableUtility.getNameColumnToolTip(object.getCqlName(), title);
			}
		};
		cellTable.addColumn(libraryNameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Library Name\">"
				+ "Library Name" + "</span>"));
		Column<CQLLibraryDataSetObject, SafeHtml> versionColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(new SafeHtmlCell()) {
			@Override
			public SafeHtml getValue(CQLLibraryDataSetObject object) {
				String title = "Version " + object.getVersion();
				return CellTableUtility.getColumnToolTip(object.getVersion(), title);
			}
		};
		cellTable.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">"
				+ "Version" + "</span>"));
		return cellTable;
	}

	@Override
	public void buildDataTable(SaveCQLLibraryResult result) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		Label cellTablePanelHeader = new Label("Select a Draft to create a Library Version.");
		cellTablePanelHeader.getElement().setId("cellTablePanelHeader_Label");
		cellTablePanelHeader.setStyleName("recentSearchHeader");
		selectionModel = null;
		if(result.getCqlLibraryDataSetObjects() != null && result.getCqlLibraryDataSetObjects().size() >0) {
			CellTable<CQLLibraryDataSetObject> cellTable = new CellTable<CQLLibraryDataSetObject>();
			ListDataProvider<CQLLibraryDataSetObject> sortProvider = new ListDataProvider<CQLLibraryDataSetObject>();
			List<CQLLibraryDataSetObject> measureList = new ArrayList<CQLLibraryDataSetObject>();
			measureList.addAll(result.getCqlLibraryDataSetObjects());
			cellTable.setPageSize(PAGE_SIZE);
			cellTable.redraw();
			cellTable.setRowCount(measureList.size(), true);
			cellTable.setSelectionModel(getSelectionModelWithHandler());
			sortProvider.refresh();
			sortProvider.getList().addAll(result.getCqlLibraryDataSetObjects());
			cellTable = addColumnToTable(cellTable);
			sortProvider.addDataDisplay(cellTable);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"cqlVersion");
			spager.setPageStart(0);
			spager.setDisplay(cellTable);
			spager.setPageSize(PAGE_SIZE);
			cellTable.setWidth("100%");
			cellTable.setColumnWidth(0, 15.0, Unit.PCT);
			cellTable.setColumnWidth(1, 63.0, Unit.PCT);
			cellTable.setColumnWidth(2, 22.0, Unit.PCT);
			com.google.gwt.dom.client.TableElement elem = cellTable.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(cellTablePanelHeader.getElement());
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"libraryVersionSummary",
							"In the following CQL Library version of draft table, a radio button is positioned to the left "
									+ "of the table with a select column header followed by Library name in "
									+ "second column and version in the third column. The draft CQL Library "
									+ "are listed alphabetically in a table.");
			cellTable.getElement().setAttribute("id", "libraryVersionFromDraftCellTable");
			cellTable.getElement().setAttribute("aria-describedby", "libraryVersionSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(cellTable);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
		} else {
			HTML desc = new HTML("<p> No available libraries.</p>");
			cellTablePanel.add(cellTablePanelHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
		}
		
	}
	
	private SingleSelectionModel<CQLLibraryDataSetObject> getSelectionModelWithHandler() {
		selectionModel = new SingleSelectionModel<CQLLibraryDataSetObject>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				errorMessages.clearAlert();
			}
		});
		return selectionModel;
	}
	
	@Override
	public Widget asWidget() {
		buildHTML();
		return mainPanel;
	}
	
	@Override
	public RadioButton getMajorRadioButton() {
		return majorRadio;
	}
	@Override
	public RadioButton getMinorRadio() {
		return minorRadio;
	}

	public void setMinorRadio(RadioButton minorRadio) {
		this.minorRadio = minorRadio;
	}
	
	@Override
	public ErrorMessageAlert getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ErrorMessageAlert errorMessages) {
		this.errorMessages = errorMessages;
	}
	@Override
	public Button getSaveButton() {
		return buttonBar.getSaveButton();
	}

	
	@Override
	public Button getCancelButton() {
		return buttonBar.getCancelButton();
	}
	
	@Override
	public CQLLibraryDataSetObject getSelectedLibrary() {
		return selectedLibraryObject;
	}
	@Override
	public void setSelectedLibraryObject(CQLLibraryDataSetObject selectedLibraryObject) {
		this.selectedLibraryObject = selectedLibraryObject;
	}

	@Override
	public void clearRadioButtonSelection() {
		getMajorRadioButton().setValue(false);
		getMinorRadio().setValue(false);
	}



	@Override
	public ConfirmationDialogBox createConfirmationDialogBox(String messageText, String yesButtonText, String noButtonText, ConfirmationObserver observer, boolean isError) {
		confirmationDialogBox = new ConfirmationDialogBox(messageText, yesButtonText, noButtonText, observer, isError);
		return confirmationDialogBox;
	}
}
