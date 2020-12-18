package mat.client.cqlworkspace;

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
import mat.client.measure.service.SaveCQLLibraryResult;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.cql.CQLLibraryDataSetObject;
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

public class EditIncludedLibraryDialogBox {
	
	private MessageAlert errorMessageAlert = new ErrorMessageAlert();
	
	private ProgressBar bar = new ProgressBar();
	private Progress progress = new Progress();
	
	private List<CQLLibraryDataSetObject> libraries = new ArrayList<CQLLibraryDataSetObject>();

	private List<String> includedList;

	private Panel cellTablePanel = new Panel();
	/** The cell table panel body. */
	private PanelBody cellTablePanelBody = new PanelBody();

	/** The selected list. */
	private List<CQLLibraryDataSetObject> selectedList;

	private CellTable<CQLLibraryDataSetObject> table;

	/** The sort provider. */
	private ListDataProvider<CQLLibraryDataSetObject> listDataProvider;
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 5;

	/** The spager. */
	private MatSimplePager spager;

	/** The selection model. */
	private SingleSelectionModel<CQLLibraryDataSetObject> selectionModel;

	private String selectedObject;

	private String modalText;
	
	private String currentLibraryId = null; 
	
	private Button applyButton = new Button();
	private Button closeButton = new CancelButton("EditInclude");
	private Modal dialogModal = new Modal();
	private ClickHandler handler;
	
	HorizontalPanel progressBarPanel = new HorizontalPanel();
	
	public EditIncludedLibraryDialogBox(String modalText) {
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
		dialogModal.setId("EditIncludedLibraryDialogBox");
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

	public void findAvailableLibraries(String setId, final String currentId) {
		showDialogBox();
		cellTablePanel.removeStyleName("cellTablePanel");
		cellTablePanel.add(progress);
	//	Mat.showLoadingMessage();
		MatContext.get().getCQLLibraryService().searchForReplaceLibraries(setId,
				new AsyncCallback<SaveCQLLibraryResult>() {
					@Override
					public void onFailure(Throwable caught) {
						Window.alert(MatContext.get().getMessageDelegate().getGenericErrorMessage());
						cellTablePanel.remove(progress);

					}
					@Override
					public void onSuccess(SaveCQLLibraryResult result) {
						currentLibraryId = currentId; 
						libraries = result.getCqlLibraryDataSetObjects();
						cellTablePanel.remove(progress);
						buildIncludeLibraryCellTable();
					}

				});
	}

	private void buildIncludeLibraryCellTable() {
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
		selectedList = new ArrayList<CQLLibraryDataSetObject>();
		selectedObject = null;
		
		List<CQLLibraryDataSetObject> tempLibraries = new ArrayList<>();
		tempLibraries.addAll(libraries);
		// filter out the library that is the 'current library'
		for(CQLLibraryDataSetObject library : tempLibraries) {
			if(library.getId().equalsIgnoreCase(currentLibraryId)) {
				tempLibraries.remove(library); 
				break; 
			}
		}
		
		if (tempLibraries.size() > 0) {
			table = new CellTable<CQLLibraryDataSetObject>();
			// setEditable(isEditable);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			listDataProvider = new ListDataProvider<CQLLibraryDataSetObject>();
			/* qdmSelectedList = new ArrayList<CQLLibraryModel>(); */
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			listDataProvider.refresh();	
			listDataProvider.getList().addAll(tempLibraries);
			ListHandler<CQLLibraryDataSetObject> sortHandler = new ListHandler<CQLLibraryDataSetObject>(
					listDataProvider.getList());
			table.addColumnSortHandler(sortHandler);
			selectionModel = new SingleSelectionModel<CQLLibraryDataSetObject>();
			table.setSelectionModel(selectionModel);
			table = addColumnToTable(table, sortHandler);
			listDataProvider.addDataDisplay(table);
			addSelectionHandler();
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true, "cqlIncludes");
			spager.setDisplay(table);
			spager.setPageStart(0);
			com.google.gwt.user.client.ui.Label invisibleLabel = (com.google.gwt.user.client.ui.Label) LabelBuilder.buildInvisibleLabel(
					"includeLibrarySummary",
					"In the Following Include Library table Name in First Column"
							+ "Version in Second Column, Owner in Third Column and Select in Fourth Column."
							+ " The Library  are listed alphabetically in a table.");
			
			table.getElement().setAttribute("id", "IncludeLibraryTable");
			table.getElement().setAttribute("aria-describedby",
					"includeLibrarySummary");
			
			cellTablePanelBody.add(invisibleLabel);
			cellTablePanelBody.add(table);
			cellTablePanelBody.add(spager);
			cellTablePanel.add(cellTablePanelBody);
		} else {
			HTML desc =  new HTML("<p> No available libraries.</p>");
			cellTablePanelBody.add(desc);
			cellTablePanel.add(cellTablePanelBody);
		}
	}

	private CellTable<CQLLibraryDataSetObject> addColumnToTable(final CellTable<CQLLibraryDataSetObject> table,
			ListHandler<CQLLibraryDataSetObject> sortHandler) {
		if (table.getColumnCount() != TABLE_ROW_COUNT) {
			
			// Name Column
			Column<CQLLibraryDataSetObject, SafeHtml> nameColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					return CellTableUtility.getNameColumnToolTip(object.getCqlName(), object.getCqlName());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Name\">" + "Name" + "</span>"));

			// Expansion Identifier Column
			Column<CQLLibraryDataSetObject, SafeHtml> versionColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {

					return CellTableUtility.getColumnToolTip(object.getVersion());
				}
			};
			table.addColumn(versionColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version" + "</span>"));

			// Version Column
			Column<CQLLibraryDataSetObject, SafeHtml> ownerColumn = new Column<CQLLibraryDataSetObject, SafeHtml>(
					new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CQLLibraryDataSetObject object) {
					StringBuilder owner = new StringBuilder();
					owner = owner.append(object.getOwnerFirstName()).append(" ").append(object.getOwnerLastName());
					return CellTableUtility.getColumnToolTip(owner.toString(), owner.toString());
				}
			};
			table.addColumn(ownerColumn,
					SafeHtmlUtils.fromSafeConstant("<span title=\"Owner\">" + "Owner" + "</span>"));

			table.addColumn(new Column<CQLLibraryDataSetObject, CQLLibraryDataSetObject>(getCheckBoxCellForTable()) {
				@Override
				public CQLLibraryDataSetObject getValue(CQLLibraryDataSetObject object) {
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
				CQLLibraryDataSetObject selectedObject = selectionModel.getSelectedObject();
				if (selectedObject != null) {
					for (CQLLibraryDataSetObject obj : listDataProvider.getList()) {
						if (!obj.getId().equals(selectedObject.getId())) {
							obj.setSelected(false);
							selectionModel.setSelected(obj, false);
						}
					}

					listDataProvider.refresh();
				}

			}
		});

	}

	private CompositeCell<CQLLibraryDataSetObject> getCheckBoxCellForTable() {
		boolean isUsed = false;
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
			protected <X> void render(Context context, CQLLibraryDataSetObject object, SafeHtmlBuilder sb,
					HasCell<CQLLibraryDataSetObject, X> hasCell) {
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td class='emptySpaces'>");

				if ((object != null)) {
					if (includedList != null && includedList.contains(object.getId())) {
						sb.appendHtmlConstant("<img src =\"images/bullet_tick.png\" alt=\"Library already in use.\""
								+ "title = \"Library already in use.\"/>");
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
	private HasCell<CQLLibraryDataSetObject, Boolean> getCheckBoxCell(final boolean isUsed) {
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
					if (selectedObject != null && object.getId().equals(selectedObject)) {
						isSelected = true;
					} else {
						isSelected = false;
					}

					selectionModel.setSelected(object, isSelected);
				}
				
				if(isSelected) {
					cell.setTitle("Click to unselect " + object.getCqlName());
				} else {
					cell.setTitle("Click to select " + object.getCqlName());
				}
				
				return isSelected;

			}

			@Override
			public FieldUpdater<CQLLibraryDataSetObject, Boolean> getFieldUpdater() {
				return new FieldUpdater<CQLLibraryDataSetObject, Boolean>() {
					@Override
					public void update(int index, CQLLibraryDataSetObject object, Boolean isCBChecked) {
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
						
						if(isCBChecked) {
							cell.setTitle("Click to unselect " + object.getCqlName());
						} else {
							cell.setTitle("Click to select " + object.getCqlName());
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

	public List<CQLLibraryDataSetObject> getSelectedList() {
		return selectedList;
	}

	public Modal getDialogModal() {
		return dialogModal;
	}


	public MessageAlert getErrorMessageAlert() {
		return errorMessageAlert;
	}

	
}
