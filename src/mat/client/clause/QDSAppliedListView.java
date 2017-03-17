package mat.client.clause;

import java.util.List;
import mat.client.CustomPager;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;



// TODO: Auto-generated Javadoc
/**
 * The Class QDSAppliedListView.
 */
public class QDSAppliedListView  implements QDSAppliedListPresenter.SearchDisplay {
	/** CellTable Column Count. */
	private static final int TABLE_COL_COUNT = 6;
	
	/** Cell Table Row Count. */
	private static final int TABLE_ROW_COUNT = 25;
	/** The applied qdm list. */
	private List<QualityDataSetDTO> appliedQDMList;
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
	private InProgressMessageDisplay inProgressMessageDisplay = new InProgressMessageDisplay();
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
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setStylePrimaryName("qdmCellList");
		HorizontalPanel mainPanelNormal = new HorizontalPanel();
		mainPanelNormal.getElement().setId("mainPanelNormal_HorizontalPanel");
		/*
		 * mainPanelNormal.add(pagerPanel); vp.add(new SpacerWidget());
		 */
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(mainPanelNormal);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(cellTablePanel);
		verticalPanel.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		removeButton.getElement().setId("removeButton_Button");
		modify.setEnabled(checkForEnable() ? true : false);
		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		removeButton.setTitle("Remove");
		modify.setTitle("Modify");
		modify.getElement().setId("modify_Button");
		modify.setStyleName("rightAlignSecondaryButton");
		updateVsacButton.setStylePrimaryName("rightAlignSecondaryButton");
		updateVsacButton.setTitle("Retrieve the most recent versions of applied value sets from VSAC");
		updateVsacButton.getElement().setId("updateVsacButton_Button");
		buttonLayout.add(removeButton);
		buttonLayout.add(modify);
		buttonLayout.add(updateVsacButton);
		verticalPanel.add(inProgressMessageDisplay);
		verticalPanel.add(errorMessagePanel);
		verticalPanel.add(successMessagePanel);
		verticalPanel.add(new SpacerWidget());
		verticalPanel.add(buttonLayout);
		verticalPanel.add(new SpacerWidget());
		
		mainPanel.add(verticalPanel);
		containerPanel.getElement().setAttribute("id", "subQDMAPPliedListContainerPanel");
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
			Label searchHeader = new Label("Applied QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(searchHeader.getElement());
			final RadioButtonCell rbCell = new RadioButtonCell(true, true);
			Column<QualityDataSetDTO, Boolean> radioButtonColumn = new Column<QualityDataSetDTO, Boolean>(rbCell) {
				@Override
				public Boolean getValue(QualityDataSetDTO qualityDataSetDTO) {
					rbCell.setUsed(qualityDataSetDTO.isUsed());
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
					"<span title=\"Select to Modify\">Select</span>"));
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
					return CellTableUtility.getColumnToolTip(value, title.toString());
				}
			};
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant(
					"<span title=\"Name\">" + "Name" + "</span>"));
			Column<QualityDataSetDTO, SafeHtml> dataTypeColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					title = title.append("Datatype : ").append(object.getDataType());
					return getDataTypeColumnToolTip(object.getDataType(), title, object.getHasModifiedAtVSAC(),
							object.isDataTypeHasRemoved());
				}
			};
			table.addColumn(dataTypeColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Datatype\">" + "Datatype"
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
					return getOIDColumnToolTip(oid, title, object.getHasModifiedAtVSAC(),
							object.isNotFoundInVSAC());
				}
			};
			table.addColumn(oidColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"OID\">" + "OID" + "</span>"));
			Column<QualityDataSetDTO, SafeHtml> versionColumn = new Column<QualityDataSetDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(QualityDataSetDTO object) {
					StringBuilder title = new StringBuilder();
					String version = null;
					if (!object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) {
						if (object.getVersion().equalsIgnoreCase("1.0")
								|| object.getVersion().equalsIgnoreCase("1")) {
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
					} else {
						version = "";
					}
					return CellTableUtility.getColumnToolTip(version, title.toString());
				}
			};
			table.addColumn(versionColumn, SafeHtmlUtils.fromSafeConstant("<span title=\"Version\">" + "Version"
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
					return CellTableUtility.getColumnToolTip(effectiveDate, title.toString());
				}
			};
			table.addColumn(effectiveDateColumn, SafeHtmlUtils.fromSafeConstant(
					"<span title=\"Effective Date\" tabindex=\"0\">" + "Effective Date"
							+ "</span>"));
			table.setColumnWidth(0, 2.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 27.0, Unit.PCT);
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
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		if ((appliedListModel.getAppliedQDMs() != null) && (appliedListModel.getAppliedQDMs().size() > 0)) {
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
			spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"AppliedQDM");
			spager.setDisplay(table);
			spager.setPageStart(0);
			/* spager.setToolTipAndTabIndex(spager); */
			Label invisibleLabel = (Label) LabelBuilder
					.buildInvisibleLabel(
							"appliedQDMTableSummary",
							"In the Following Applied QDM Elements table a radio button is positioned to "
									+ "the left of the table with a select Column header followed by "
									+ "QDM name in second column, Datatype in third column, OID in "
									+ "fourth column, Version in fifth column and Effective date in "
									+ "sixth column. The Applied QDM elements are listed alphabetically"
									+ " in a table.  ");
			table.getElement().setAttribute("id", "AppliedQDMTable");
			table.getElement().setAttribute("aria-describedby", "appliedQDMTableSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(spager);
			removeButton.setEnabled(false);
			modify.setEnabled(checkForEnable() && (appliedListModel.getLastSelected() != null) ? true : false);
			updateVsacButton.setEnabled(checkForEnable());
			
		} else {
			Label searchHeader = new Label("Applied QDM Elements");
			searchHeader.getElement().setId("searchHeader_Label");
			searchHeader.setStyleName("recentSearchHeader");
			searchHeader.getElement().setAttribute("tabIndex", "0");
			HTML desc = new HTML("<p> No Applied QDM Elements.</p>");
			cellTablePanel.add(searchHeader);
			cellTablePanel.add(new SpacerWidget());
			cellTablePanel.add(desc);
			removeButton.setEnabled(false);
			modify.setEnabled(false);
			updateVsacButton.setEnabled(false);
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
	public List<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getApplyToMeasureSuccessMsg()
	 */
	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
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
	
	/**
	 * Gets the name column tool tip.
	 *
	 * @param columnText - String.
	 * @param title - StringBuilder.
	 * @param hasImage - Boolean.
	 * @param isUserDefined Boolean.
	 * @return the name column tool tip
	 */
	private SafeHtml getOIDColumnToolTip(String columnText, StringBuilder title, boolean hasImage,
			boolean isUserDefined) {
		if (hasImage && !isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"QDM Updated From VSAC.\""
					+ "title = \"QDM Updated From VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else if (hasImage && isUserDefined) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : QDM not available in VSAC.\""
					+ " title=\"QDM not available in VSAC.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else {
			String htmlConstant = "<html>" + "<head> </head> <Body><span tabIndex = \"0\" title='" + title + "'>" + columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		}
	}
	
	/**
	 * Gets the data type column tool tip.
	 *
	 * @param columnText the column text
	 * @param title the title
	 * @param hasImage the has image
	 * @param dataTypeHasRemoved the data type has removed
	 * @return the data type column tool tip
	 */
	private SafeHtml getDataTypeColumnToolTip(String columnText, StringBuilder title, boolean hasImage,
			boolean dataTypeHasRemoved) {
		if (hasImage && !dataTypeHasRemoved) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/bullet_tick.png\" alt=\"DataType is Valid.\""
					+ "title = \"DataType is Valid.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "'>"
					+ columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else if (hasImage && dataTypeHasRemoved) {
			String htmlConstant = "<html>"
					+ "<head> </head> <Body><img src =\"images/userDefinedWarning.png\""
					+ "alt=\"Warning : DataType is not Valid.\""
					+ " title=\"DataType is not Valid.\"/>"
					+ "<span tabIndex = \"0\" title='" + title + "' class='clauseWorkSpaceInvalidNode'>"
					+ columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		} else {
			String htmlConstant = "<html>" + "<head> </head> <Body><span tabIndex = \"0\" title='" + title + "'>" + columnText
					+ "</span></body>"
					+ "</html>";
			return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
		}
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
	/**
	 * @return the inProgressMessageDisplay
	 */
	@Override
	public InProgressMessageDisplay getInProgressMessageDisplay() {
		return inProgressMessageDisplay;
	}
	/**
	 * @param inProgressMessageDisplay the inProgressMessageDisplay to set
	 */
	public void setInProgressMessageDisplay(InProgressMessageDisplay inProgressMessageDisplay) {
		this.inProgressMessageDisplay = inProgressMessageDisplay;
	}
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getUpdateVsacButton()
	 */
	@Override
	public Button getUpdateVsacButton() {
		return updateVsacButton;
	}
	
	/**
	 * Initialize cell list content.
	 *
	 * @param appliedQDMList the new applied qdm list
	 * @return the cell list
	 */
	/*
	 * private CellList<QualityDataSetDTO> initializeCellListContent(final QDSAppliedListModel appliedListModel) {
	 * ArrayList<HasCell<QualityDataSetDTO, ?>> hasCells = new ArrayList<HasCell<QualityDataSetDTO, ?>>(); // final
	 * MultiSelectionModel<QualityDataSetDTO> selectionModel = new MultiSelectionModel<QualityDataSetDTO>(); final
	 * SingleSelectionModel<QualityDataSetDTO> selectionModel = new SingleSelectionModel<QualityDataSetDTO>(); //
	 * cellList.setSelectionModel(selectionModel); hasCells.add(new HasCell<QualityDataSetDTO, Boolean>() { // private MatCheckBoxCell
	 * cbCell = new MatCheckBoxCell(); private RadioButtonCell cbCell = new RadioButtonCell(true, true);
	 * 
	 * @Override public Cell<Boolean> getCell() { return cbCell; }
	 * 
	 * @Override public FieldUpdater<QualityDataSetDTO, Boolean> getFieldUpdater() { return new FieldUpdater<QualityDataSetDTO, Boolean>() {
	 * 
	 * @Override public void update(int index, QualityDataSetDTO object, Boolean value) { errorMessagePanel.clear(); lastSelectedObject =
	 * object; if (checkForEnable()) { modify.setEnabled(true); updateVsacButton.setEnabled(true); if (object.isUsed()) {
	 * removeButton.setEnabled(false); } else { removeButton.setEnabled(true); } } else { removeButton.setEnabled(false);
	 * modify.setEnabled(false); updateVsacButton.setEnabled(false); } } }; }
	 * 
	 * @Override public Boolean getValue(QualityDataSetDTO object) { cbCell.setUsed(object.isUsed()); return
	 * selectionModel.isSelected(object); } }); hasCells.add(new HasCell<QualityDataSetDTO, String>() { private TextCell cell = new
	 * TextCell();
	 * 
	 * @SuppressWarnings("unchecked")
	 * 
	 * @Override public Cell<String> getCell() { return cell; }
	 * 
	 * @Override public FieldUpdater<QualityDataSetDTO, String> getFieldUpdater() { return null; }
	 * 
	 * @Override public String getValue(QualityDataSetDTO object) { String value; String QDMDetails = ""; if
	 * (object.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID)) { QDMDetails = "(User defined)"; } else { String version =
	 * object.getVersion(); String effectiveDate = object.getEffectiveDate(); if (effectiveDate != null) { QDMDetails = "(OID: " +
	 * object.getOid() + ", Effective Date: " + effectiveDate + ")"; } else if (!version.equals("1.0") && !version.equals("1")) { QDMDetails
	 * = "(OID: " + object.getOid() + ", Version: " + version + ")"; } else { QDMDetails = "(OID: " + object.getOid() + ")"; } } if
	 * ((object.getOccurrenceText() != null) && !object.getOccurrenceText().equals("")) { value = object.getOccurrenceText() + " of " +
	 * object.getCodeListName() + ": " + object.getDataType() + " " + QDMDetails; } else { value = object.getCodeListName() + ": " +
	 * object.getDataType() + " " + QDMDetails; } return value; } }); Cell<QualityDataSetDTO> myClassCell = new
	 * CompositeCell<QualityDataSetDTO>(hasCells) {
	 * 
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
	public void setAppliedQDMList(List<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}
	
	
}
