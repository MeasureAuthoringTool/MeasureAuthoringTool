package mat.client.measure;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.CustomPager;
import mat.client.buttons.CancelButton;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.shared.SearchWidgetWithFilter;
import mat.client.util.CellTableUtility;
import mat.shared.MeasureSearchModel;
import mat.shared.SearchModel.VersionType;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Modal;
import org.gwtbootstrap3.client.ui.ModalBody;
import org.gwtbootstrap3.client.ui.ModalFooter;
import org.gwtbootstrap3.client.ui.ModalSize;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.Progress;
import org.gwtbootstrap3.client.ui.ProgressBar;
import org.gwtbootstrap3.client.ui.constants.ButtonDismiss;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.ModalBackdrop;
import org.gwtbootstrap3.client.ui.constants.ProgressBarType;
import org.gwtbootstrap3.client.ui.constants.ProgressType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EditIncludedComponentMeasureDialogBox {
	
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	private ProgressBar bar = new ProgressBar();
	private Progress progress = new Progress();
	
	private List<ManageMeasureSearchModel.Result> componentMeasuresList = new ArrayList<>();
	
	private List<String> includedList;

	private Panel cellTablePanel = new Panel();
	private PanelBody cellTablePanelBody = new PanelBody();

	private List<ManageMeasureSearchModel.Result> selectedList;

	private CellTable<ManageMeasureSearchModel.Result> table;

	private ListDataProvider<ManageMeasureSearchModel.Result> listDataProvider;

	private static final int TABLE_ROW_COUNT = 5;

	private MatSimplePager spager;

	private SingleSelectionModel<ManageMeasureSearchModel.Result> selectionModel;

	private String selectedObject;

	private String modalText;
	
	private Button applyButton = new Button();
	private Button closeButton = new CancelButton("EditInclude");
	private Modal dialogModal = new Modal();
	private ClickHandler handler;
	
	HorizontalPanel progressBarPanel = new HorizontalPanel();
	
	public EditIncludedComponentMeasureDialogBox(String modalText) {
		this.modalText = modalText;
		progress.setActive(true);
		progress.setType(ProgressType.STRIPED);
		bar.setType(ProgressBarType.INFO);
		bar.setWidth("100%");
		bar.setPercent(50.00);
		bar.setText("Please wait. Loaded 50%");
		progress.add(bar);
	}

	
	private void showDialogBox() {
		errorMessageAlert.clearAlert();
		dialogModal.setTitle(modalText);
		dialogModal.setClosable(true);
		dialogModal.setFade(true);
		dialogModal.setDataBackdrop(ModalBackdrop.STATIC);
		dialogModal.setDataKeyboard(true);
		dialogModal.setId("EditIncludedComponentMeasureDialogBox");
		dialogModal.setSize(ModalSize.LARGE);
		dialogModal.setRemoveOnHide(true);
		
		if(handler == null) {
			handler = MatContext.get().addClickHandlerToResetTimeoutWarning();
		}
		
		dialogModal.addDomHandler(handler, ClickEvent.getType());
		
		ModalBody modalBody = new ModalBody();
		modalBody.add(errorMessageAlert);
		modalBody.add(progressBarPanel);
		modalBody.add(cellTablePanel);

		ModalFooter modalFooter = new ModalFooter();
		ButtonToolBar buttonToolBar = new ButtonToolBar();
		applyButton.setText("Apply");
		applyButton.setTitle("Apply");
		applyButton.setType(ButtonType.PRIMARY);
		applyButton.setSize(ButtonSize.SMALL);
		closeButton.setSize(ButtonSize.SMALL);
		closeButton.setDataDismiss(ButtonDismiss.MODAL);
		buttonToolBar.add(applyButton);
		buttonToolBar.add(closeButton);
		modalFooter.add(buttonToolBar);
		dialogModal.add(modalBody);
		dialogModal.add(modalFooter);
		dialogModal.show();
	}

	public void findAvailableMeasures(String setId, final String currentId, boolean filterForInclude) {
		showDialogBox();
		cellTablePanel.removeStyleName("cellTablePanel");
		cellTablePanel.add(progress);

		MeasureSearchModel searchModel = new MeasureSearchModel(SearchWidgetWithFilter.ALL, 1, Integer.MAX_VALUE, null);
		searchModel.setQdmVersion(MatContext.get().getCurrentQDMVersion());
		searchModel.setOmitCompositeMeasure(true);
		searchModel.setIsDraft(VersionType.VERSION);
		searchModel.setMeasureSetId(setId);
		MatContext.get().getMeasureService().searchComponentMeasures(searchModel, new AsyncCallback<ManageMeasureSearchModel>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
				cellTablePanel.remove(progress);
			}

			@Override
			public void onSuccess(ManageMeasureSearchModel result) {				
				componentMeasuresList = result.getData();
				componentMeasuresList.removeIf(component -> component.getId().equals(currentId));	
				cellTablePanel.remove(progress);
				buildIncludeComponentMeasureCellTable();
			}
		});
		
	}

	private void buildIncludeComponentMeasureCellTable() {
		cellTablePanel.clear();
		cellTablePanelBody.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		cellTablePanel.setWidth("95%");
		PanelHeader searchHeader = new PanelHeader();
		searchHeader.getElement().setId("searchHeader_Label_IncludeSection");
		searchHeader.setStyleName("measureGroupingTableHeader");
		searchHeader.getElement().setAttribute("tabIndex", "-1");

		HTML searchHeaderText = new HTML("<strong>Available Measures</strong>");
		searchHeader.add(searchHeaderText);
		cellTablePanel.add(searchHeader);
		selectedList = new ArrayList<ManageMeasureSearchModel.Result>();
		selectedObject = null;
		
		List<ManageMeasureSearchModel.Result> tempMeasures = new ArrayList<>();
		tempMeasures.addAll(componentMeasuresList);
		
		if (tempMeasures.size() > 0) {
			table = new CellTable<ManageMeasureSearchModel.Result>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			listDataProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();	
			listDataProvider.getList().addAll(tempMeasures);
			ListHandler<ManageMeasureSearchModel.Result> sortHandler = new ListHandler<ManageMeasureSearchModel.Result>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			selectionModel = new SingleSelectionModel<ManageMeasureSearchModel.Result>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			table.setSelectionModel(selectionModel);
			table = addColumnToTable(table, sortHandler);
			listDataProvider.addDataDisplay(table);
			addSelectionHandler();
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true, "cqlIncludes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder.buildInvisibleLabel(
					"includeComponentMeasureSummary",
					"In the Following Include Component Measure table Name in First Column"
							+ "Version in Second Column, Owner in Third Column and Select in Fourth Column."
							+ " The Component Measures  are listed alphabetically in a table.");
			
			table.getElement().setAttribute("id", "IncludeComponentMeasuresTable");
			table.getElement().setAttribute("aria-describedby",
					"includeComponentMeasuresSummary");
			
			cellTablePanelBody.add(invisibleLabel);
			cellTablePanelBody.add(table);
			cellTablePanelBody.add(spager);
			cellTablePanel.add(cellTablePanelBody);
		} else {
			HTML desc =  new HTML("<p> No available measures.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
	}

	private CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table,
			ListHandler<ManageMeasureSearchModel.Result> sortHandler) {
		if (table.getColumnCount() != TABLE_ROW_COUNT) {
			
			// Name Column
			Column<ManageMeasureSearchModel.Result, SafeHtml> nameColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					return CellTableUtility.getNameColumnToolTip(object.getName(), object.getName());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));

			// Expansion Identifier Column
			Column<ManageMeasureSearchModel.Result, SafeHtml> versionColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {

					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(versionColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + "</span>"));

			// Version Column
			Column<ManageMeasureSearchModel.Result, SafeHtml> ownerColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
					StringBuilder owner = new StringBuilder();
					owner = owner.append(object.getOwnerFirstName()).append(" ").append(object.getOwnerLastName());
					return CellTableUtility.getColumnToolTip(owner.toString(), owner.toString());
				}
			};
			table.addColumn(ownerColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));

			table.addColumn(new Column<ManageMeasureSearchModel.Result, ManageMeasureSearchModel.Result>(getCheckBoxCellForTable()) {
				@Override
				public ManageMeasureSearchModel.Result getValue(ManageMeasureSearchModel.Result object) {
					return object;
				}
			}, "Select");

			table.setColumnWidth(0, 70.0, Unit.PCT);
			table.setColumnWidth(1, 10.0, Unit.PCT);
			table.setColumnWidth(2, 15.0, Unit.PCT);
			table.setColumnWidth(3, 5.0, Unit.PCT);

		}
		table.setWidth("100%");
		return table;
	}

	private void addSelectionHandler() {
		selectionModel.addSelectionChangeHandler(new Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				errorMessageAlert.clearAlert();
				ManageMeasureSearchModel.Result selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					for (ManageMeasureSearchModel.Result obj : listDataProvider.getList()) {
						if (!obj.getId().equals(selectedObject.getId())) {
							selectionModel.setSelected(obj, false);
						}
					}

					listDataProvider.refresh();
				}

			}
		});

	}

	private CompositeCell<ManageMeasureSearchModel.Result> getCheckBoxCellForTable() {
		boolean isUsed = false;
		final List<HasCell<ManageMeasureSearchModel.Result, ?>> cells = new LinkedList<HasCell<ManageMeasureSearchModel.Result, ?>>();
		cells.add(getCheckBoxCell(isUsed));
		CompositeCell<ManageMeasureSearchModel.Result> cell = new CompositeCell<ManageMeasureSearchModel.Result>(cells) {
			@Override
			public void render(Context context, ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				for (HasCell<ManageMeasureSearchModel.Result, ?> hasCell : cells) {
					render(context, object, sb, hasCell);
				}
				sb.appendHtmlConstant("</tr></tbody></table>");
			}

			@Override
			protected <X> void render(Context context, ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb,
					HasCell<ManageMeasureSearchModel.Result, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces'>");

				if ((object != null)) {
					if (includedList != null && includedList.contains(object.getId())) {
						sb.appendHtmlConstant("<img src =\"images/bullet_tick.png\" alt=\"Component Measure already in use.\""
								+ "title = \"Component Measure already in use.\"/>");
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
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
		};

		return cell;
	}

	/**
	 * Gets the check box cell.
	 *
	 * @param isUsed
	 *            the is used
	 * @return the check box cell
	 */
	private HasCell<ManageMeasureSearchModel.Result, Boolean> getCheckBoxCell(final boolean isUsed) {
		HasCell<ManageMeasureSearchModel.Result, Boolean> hasCell = new HasCell<ManageMeasureSearchModel.Result, Boolean>() {

			private MatCheckBoxCell cell = new MatCheckBoxCell(false, true);

			@Override
			public Cell<Boolean> getCell() {
				return cell;
			}

			@Override
			public Boolean getValue(ManageMeasureSearchModel.Result object) {
				cell.setTitle("Click to select " + object.getName());
				boolean isSelected = false;
				if (selectedList.size() > 0) {
					for (int i = 0; i < selectedList.size(); i++) {
						if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
							isSelected = true;
							selectionModel.setSelected(object, isSelected);
							break;
						}
					}
				}

				else {
					if (selectedObject != null && object.getId().equals(selectedObject)) {
						isSelected = true;
					} else {
						isSelected = false;
					}

					selectionModel.setSelected(object, isSelected);
				}
				return isSelected;

			}

			@Override
			public FieldUpdater<ManageMeasureSearchModel.Result, Boolean> getFieldUpdater() {
				return new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, Boolean isCBChecked) {
						if (isCBChecked) {
							for (int i = 0; i < selectedList.size(); i++) {
								selectionModel.setSelected(selectedList.get(i), false);
							}
							selectedList.clear();
							selectedList.add(object);
						} else {
							for (int i = 0; i < selectedList.size(); i++) {
								if (selectedList.get(i).getId().equalsIgnoreCase(object.getId())) {
									selectedList.remove(i);
									break;
								}
							}
						}
						selectionModel.setSelected(object, isCBChecked);
					}
				};
			}
		};
		return hasCell;
	}
	
	public Button getApplyButton(){
		return applyButton;
	}
	
	public Button getCancelButton(){
		return closeButton;
	}

	public List<ManageMeasureSearchModel.Result> getSelectedList() {
		return selectedList;
	}

	public Modal getDialogModal() {
		return dialogModal;
	}


	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	
}
