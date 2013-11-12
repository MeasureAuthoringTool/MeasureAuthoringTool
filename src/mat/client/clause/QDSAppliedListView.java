package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;



/**
 * The Class QDSAppliedListView.
 */
public class QDSAppliedListView  implements QDSAppliedListPresenter.SearchDisplay {
	/** CellTable Column Count. */
	private static final int TABLE_COL_COUNT = 6;
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 25;
	/** The applied qdm list. */
	private ArrayList<QualityDataSetDTO> appliedQDMList;
	/** The cell list. */
	/* private CellList<QualityDataSetDTO> cellList; */
	/** VerticalPanel for Holding Cell Table. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	/** The last selected object. */
	private QualityDataSetDTO lastSelectedObject;
	/** The modify. */
	private Button modify = new Button("Modify");
	/** The pager panel. */
	/* private ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel(); */
	/** The range label pager. */
	/* private RangeLabelPager rangeLabelPager = new RangeLabelPager(); */
	/** The remove button. */
	private Button removeButton = new Button("Remove");
	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel;
	/** The update vsac button. */
	private Button updateVsacButton = new Button("Update from VSAC");
	/**
	 * Instantiates a new qDS applied list view.
	 */
	public QDSAppliedListView() {
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
		VerticalPanel vp = new VerticalPanel();
		vp.setStylePrimaryName("qdmCellList");
		HorizontalPanel mainPanelNormal = new HorizontalPanel();
		mainPanelNormal.getElement().setId("mainPanelNormal_HorizontalPanel");
		/*
		 * mainPanelNormal.add(pagerPanel); vp.add(new SpacerWidget());
		 */
		vp.add(new SpacerWidget());
		vp.add(errorMessagePanel);
		vp.add(successMessagePanel);
		vp.add(new SpacerWidget());
		vp.add(mainPanelNormal);
		vp.add(new SpacerWidget());
		vp.add(cellTablePanel);
		vp.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		modify.setEnabled(checkForEnable() ? true : false);
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		removeButton.setTitle("Remove");
		modify.setTitle("Modify");
		modify.setStyleName("rightAlignSecondaryButton");
		updateVsacButton.setStylePrimaryName("rightAlignSecondaryButton");
		updateVsacButton.setTitle("Retrieve the most recent versions of applied value sets from VSAC");
		buttonLayout.add(removeButton);
		buttonLayout.add(modify);
		buttonLayout.add(updateVsacButton);
		vp.add(buttonLayout);
		vp.add(new SpacerWidget());
		mainPanel.add(vp);
		containerPanel.add(mainPanel);
		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQdsAppliedListView(this);
	}
	/** This method add's column to Cell Table.
	 * @param table - CellTable
	 * @param sortHandler - ListHandler
	 * @return - CellTable */
	private CellTable<QualityDataSetDTO> addColumnToTable(final CellTable<QualityDataSetDTO> table,
			ListHandler<QualityDataSetDTO> sortHandler) {
		if (table.getColumnCount() != TABLE_COL_COUNT) {
			final RadioButtonCell cbCell = new RadioButtonCell(true, true);
			Column<QualityDataSetDTO, Boolean> radioButtonColumn = new Column<QualityDataSetDTO, Boolean>(cbCell) {
				@Override
				public Boolean getValue(QualityDataSetDTO qualityDataSetDTO) {
					cbCell.setUsed(qualityDataSetDTO.isUsed());
					return table.getSelectionModel().isSelected(qualityDataSetDTO);
				}
			};
			radioButtonColumn.setFieldUpdater(new FieldUpdater<QualityDataSetDTO, Boolean>() {
				@Override
				public void update(int index, QualityDataSetDTO object, Boolean value) {
					errorMessagePanel.clear();
					lastSelectedObject = object;
					if (checkForEnable()) {
						modify.setEnabled(true);
						if (object.isUsed()) {
							removeButton.setEnabled(false);
						} else {
							removeButton.setEnabled(true);
						}
					} else {
						removeButton.setEnabled(false);
						modify.setEnabled(false);
					}
					table.getSelectionModel().setSelected(object, true);
				}
			});
			table.addColumn(radioButtonColumn, SafeHtmlUtils.fromSafeConstant(
					"<span title='Select to Modify' tabindex=\"0\">Select</span>"));
			Column<QualityDataSetDTO, SafeHtml> nameColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String value = null;
					if ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) {
						value = object.getOccurrenceText() + " of " + object.getCodeListName();
						title = title.append("Name : ").append(value);
					} else {
						value = object.getCodeListName();
						title = title.append("Name : ").append(value);
					}
					return getColumnToolTip(value, title);
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Name' tabindex=\"0\">" + "Name" + "</span>"));
			Column<QualityDataSetDTO, SafeHtml> dataTypeColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					title = title.append("Datatype : ").append(object.getDataType());
					return getColumnToolTip(object.getDataType(), title);
				}
			};
			table.addColumn(dataTypeColumn, SafeHtmlUtils.fromSafeConstant("<span title='Datatype' tabindex=\"0\">" + "Datatype"
					+ "</span>"));
			Column<QualityDataSetDTO, SafeHtml> oidColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String oid = null;
					if (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						title = title.append("OID : ").append(ConstantMessages.USER_DEFINED_QDM_NAME);
						oid = ConstantMessages.USER_DEFINED_CONTEXT_DESC;
					} else {
						title = title.append("OID : ").append(object.getOid());
						oid = object.getOid();
					}
					return getColumnToolTip(oid, title);
				}
			};
			table.addColumn(oidColumn, SafeHtmlUtils.fromSafeConstant("<span title='OID' tabindex=\"0\">" + "OID" + "</span>"));
			Column<QualityDataSetDTO, SafeHtml> versionColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (object.getVersion().equalsIgnoreCase("1.0")) {
						title = title.append("Version : ").append("Most Recent");
						version = "Most Recent";
					} else {
						if (object.getEffectiveDate() == null) {
							title = title.append("Version : ").append(object.getVersion());
							version = object.getVersion();
						} else {
							version = "";
						}
					}
					return getColumnToolTip(version, title);
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title='Version' tabindex=\"0\">" + "Version"
					+ "</span>"));
			Column<QualityDataSetDTO, SafeHtml> effectiveDateColumn = new Column<QualityDataSetDTO,
					SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String effectiveDate = "";
					if (object.getEffectiveDate() != null) {
						title = title.append("Effective Date : ").append(object.getEffectiveDate());
						effectiveDate = object.getEffectiveDate();
					} else {
						title = title.append("Effective Date : ").append("No Effective Date Selected");
					}
					return getColumnToolTip(effectiveDate, title);
				}
			};
			table.addColumn(effectiveDateColumn, SafeHtmlUtils.fromSafeConstant(
					"<span title='Effective Date' tabindex=\"0\">" + "Effective Date"
							+ "</span>"));
			table.setColumnWidth(0, 2.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 25.0, Unit.PCT);
			table.setColumnWidth(4, 10.0, Unit.PCT);
			table.setColumnWidth(5, 15.0, Unit.PCT);
		}
		return table;
	}
	/** This method add's selection Model to Cell Table.
	 * @param appliedListModel - QDSAppliedListModel
	 * @return SingleSelectionModel */
	public SingleSelectionModel<QualityDataSetDTO> addSelectionHandlerOnTable(final QDSAppliedListModel appliedListModel) {
		final SingleSelectionModel<QualityDataSetDTO> selectionModel = new  SingleSelectionModel<QualityDataSetDTO>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				QualityDataSetDTO qualityDataSetDTO = selectionModel.getSelectedObject();
				if (qualityDataSetDTO != null) {
					errorMessagePanel.clear();
					successMessagePanel.clear();
					appliedListModel.setLastSelected(selectionModel.getSelectedObject());
					System.out.println("appliedListModel.getLastSelected() =======>>>>"
							+ appliedListModel.getLastSelected());
				}
			}
		});
		return selectionModel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#buildCellTable(mat.client.clause.QDSAppliedListModel)
	 */
	@Override
	public void buildCellTable(QDSAppliedListModel appliedListModel) {
		if (appliedListModel.getAppliedQDMs() != null) {
			/*cellList = initializeCellListContent(appliedListModel);
			cellList.setPageSize(15);
			cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
			ListDataProvider<QualityDataSetDTO> dataProvider =
			new ListDataProvider<QualityDataSetDTO>(appliedListModel.getAppliedQDMs());
			dataProvider.addDataDisplay(cellList);
			pagerPanel.addStyleName("scrollable");
			pagerPanel.setDisplay(cellList);
			rangeLabelPager.setDisplay(cellList);*/
			CellTable<QualityDataSetDTO> table = new CellTable<QualityDataSetDTO>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<QualityDataSetDTO> sortProvider = new ListDataProvider<QualityDataSetDTO>();
			table.setSelectionModel(addSelectionHandlerOnTable(appliedListModel));
			table.setPageSize(TABLE_ROW_COUNT);
			table.redraw();
			sortProvider.refresh();
			sortProvider.getList().addAll(appliedListModel.getAppliedQDMs());
			ListHandler<QualityDataSetDTO> sortHandler = new ListHandler<QualityDataSetDTO>(sortProvider.getList());
			table.addColumnSortHandler(sortHandler);
			table = addColumnToTable(table, sortHandler);
			sortProvider.addDataDisplay(table);
			MatSimplePager spager;
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
			spager.setDisplay(table);
			spager.setPageStart(0);
			spager.setToolTipAndTabIndex(spager);
			cellTablePanel.clear();
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			removeButton.setEnabled(false);
			modify.setEnabled(checkForEnable() && (appliedListModel.getLastSelected() != null) ? true : false);
		}
	}
	/**
	 * Check for enable.
	 *
	 * @return true, if successful
	 */
	private boolean checkForEnable() {
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getAllAppliedQDMList()
	 */
	@Override
	public List<QualityDataSetDTO> getAllAppliedQDMList() {
		return getAppliedQDMList();
	}
	/**
	 * Gets the applied qdm list.
	 *
	 * @return the applied qdm list
	 */
	public ArrayList<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getApplyToMeasureSuccessMsg()
	 */
	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
	}
	/**
	 * Gets the column tool tip.
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @return the column tool tip
	 */
	private SafeHtml getColumnToolTip(String columnText, StringBuilder title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>" + columnText + "</span></body>"
				+ "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}
	/**
	 * Gets the error message panel.
	 *
	 * @return the error message panel
	 */
	public ErrorMessageDisplay getErrorMessagePanel() {
		return errorMessagePanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getModifyButton()
	 */
	@Override
	public Button getModifyButton() {
		return modify;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getRemoveButton()
	 */
	@Override
	public Button getRemoveButton() {
		return removeButton;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getSelectedElementToRemove()
	 */
	@Override
	public QualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	/**
	 * Gets the success message panel.
	 *
	 * @return the success message panel
	 */
	public SuccessMessageDisplay getSuccessMessagePanel() {
		return successMessagePanel;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getUpdateVsacButton()
	 */
	@Override
	public Button getUpdateVsacButton() {
		return updateVsacButton;
	}
	/** Initialize cell list content.
	 * @param appliedListModel the applied list model
	 * @return the cell list */
	/*
	 * private CellList<QualityDataSetDTO> initializeCellListContent(final QDSAppliedListModel appliedListModel) {
	 * ArrayList<HasCell<QualityDataSetDTO, ?>> hasCells = new ArrayList<HasCell<QualityDataSetDTO, ?>>(); // final
	 * MultiSelectionModel<QualityDataSetDTO> selectionModel = new MultiSelectionModel<QualityDataSetDTO>(); final
	 * SingleSelectionModel<QualityDataSetDTO> selectionModel = new SingleSelectionModel<QualityDataSetDTO>(); //
	 * cellList.setSelectionModel(selectionModel); hasCells.add(new HasCell<QualityDataSetDTO, Boolean>() {
	 * // private MatCheckBoxCell cbCell = new MatCheckBoxCell(); private RadioButtonCell cbCell = new RadioButtonCell(true, true);
	 * @Override public Cell<Boolean> getCell() { return cbCell; }
	 * @Override public FieldUpdater<QualityDataSetDTO, Boolean> getFieldUpdater() { return new FieldUpdater<QualityDataSetDTO,
	 * Boolean>() {
	 * @Override public void update(int index, QualityDataSetDTO object, Boolean value) { errorMessagePanel.clear(); lastSelectedObject =
	 * object; if (checkForEnable()) { modify.setEnabled(true); updateVsacButton.setEnabled(true); if (object.isUsed()) {
	 * removeButton.setEnabled(false); } else { removeButton.setEnabled(true); } } else { removeButton.setEnabled(false);
	 * modify.setEnabled(false); updateVsacButton.setEnabled(false); }
	 * } }; }
	 * @Override public Boolean getValue(QualityDataSetDTO object) { cbCell.setUsed(object.isUsed()); return
	 * selectionModel.isSelected(object); } });
	 * hasCells.add(new HasCell<QualityDataSetDTO, String>() { private TextCell cell = new TextCell();
	 * @SuppressWarnings("unchecked")
	 * @Override public Cell<String> getCell() { return cell; }
	 * @Override public FieldUpdater<QualityDataSetDTO, String> getFieldUpdater() { return null; }
	 * @Override public String getValue(QualityDataSetDTO object) { String value; String QDMDetails = ""; if
	 * (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) { QDMDetails = "(User defined)"; } else { String version =
	 * object.getVersion(); String effectiveDate = object.getEffectiveDate(); if (effectiveDate != null) { QDMDetails = "(OID: " +
	 * object.getOid() + ", Effective Date: " + effectiveDate + ")"; } else if (!version.equals("1.0") && !version.equals("1")) { QDMDetails
	 * = "(OID: " + object.getOid() + ", Version: " + version + ")"; } else { QDMDetails = "(OID: " + object.getOid() + ")"; } }
	 * if ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) { value = object.getOccurrenceText() + " of " +
	 * object.getCodeListName() + ": " + object.getDataType() + " " + QDMDetails; } else { value = object.getCodeListName() + ": " +
	 * object.getDataType() + " " + QDMDetails; }
	 * return value; } });
	 * Cell<QualityDataSetDTO> myClassCell = new CompositeCell<QualityDataSetDTO>(hasCells) {
	 * @Override protected Element getContainerElement(Element parent) { // Return the first TR element in the table. return
	 * parent.getFirstChildElement().getFirstChildElement().getFirstChildElement(); }
	 * 
	 * @Override public void render(Context context, QualityDataSetDTO value, SafeHtmlBuilder sb) {
	 * sb.appendHtmlConstant("<table><tbody><tr>"); super.render(context, value, sb); sb.appendHtmlConstant("</tr></tbody></table>"); }
	 * 
	 * @Override protected <X> void render(Context context, QualityDataSetDTO value, SafeHtmlBuilder sb, HasCell<QualityDataSetDTO, X>
	 * hasCell) { // this renders each of the cells inside the composite cell in a new table cell Cell<X> cell = hasCell.getCell();
	 * sb.appendHtmlConstant("<td style='font-size:100%;'>"); cell.render(context, hasCell.getValue(value), sb);
	 * sb.appendHtmlConstant("</td>"); }
	 * 
	 * };
	 * 
	 * CellList<QualityDataSetDTO> cellsList = new CellList<QualityDataSetDTO>(myClassCell); selectionModel.addSelectionChangeHandler(new
	 * SelectionChangeEvent.Handler() {
	 * 
	 * @Override public void onSelectionChange(SelectionChangeEvent event) { errorMessagePanel.clear(); successMessagePanel.clear(); //
	 * appliedListModel.setRemoveQDMs(selectionModel.getSelectedSet()); //
	 * System.out.println("appliedListModel Remove QDS Set Size =======>>>>" + appliedListModel.getRemoveQDMs().size());
	 * 
	 * listToRemove = new ArrayList<QualityDataSetDTO>(appliedListModel.getRemoveQDMs()); for(int i=0;i<listToRemove.size();i++){
	 * System.out.println("QDM IDS=======>>>>" + listToRemove.get(i).getUuid());
	 * 
	 * }
	 * 
	 * appliedListModel.setLastSelected(selectionModel.getSelectedObject());
	 * System.out.println("appliedListModel.getLastSelected() =======>>>>" + appliedListModel.getLastSelected());
	 * 
	 * } });
	 * 
	 * cellsList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager()); //
	 * removeButton.setEnabled(checkForEnable()); return cellsList; }
	 */
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#setAppliedQDMList(java.util.ArrayList)
	 */
	@Override
	public void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}
	
	
}
