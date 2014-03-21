package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import mat.client.CustomPager;
import mat.client.clause.QDSAppliedListModel;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackagerView.
 */
/**
 * @author jnarang
 *
 */
public class MeasurePackagerView implements MeasurePackagePresenter.PackageView {
	/**
	 * Observer Interface.
	 *
	 */
	interface Observer {
		/**
		 * On edit clicked.
		 * @param detail
		 *            the MeasurePackageDetail
		 */
		void onEditClicked(MeasurePackageDetail detail);
		/**
		 * On clone clicked.
		 * @param detail
		 *            the MeasurePackageDetail
		 */
		void onDeleteClicked(MeasurePackageDetail detail);
	}
	
	interface Templates extends SafeHtmlTemplates {
		/**
		 * The template for this Cell, which includes styles and a value.
		 * @param title - Title for div.
		 * @param value the safe value. Since the value type is {@link SafeHtml},
		 *          it will not be escaped before including it in the template.
		 *          Alternatively, you could make the value type String, in which
		 *          case the value would be escaped.
		 * @return a {@link SafeHtml} instance
		 */
		@SafeHtmlTemplates.Template("<div title=\"{0}\" style=\"margin-left:5px;margin-right:5px;white-space:nowrap;\">{1}</div>")
		SafeHtml cell(String title, SafeHtml value);
	}
	/** Create a singleton instance of the templates used to render the cell. */
	private static Templates templates = GWT.create(Templates.class);
	/** The add qdm right. */
	private Button addQDMRight = buildAddButton("customAddRightButton", "addQDMRight");
	/** The add qdm left. */
	private Button addQDMLeft = buildAddButton("customAddLeftButton", "addQDMLeft");
	/** The add all qdm right. */
	private Button addAllQDMRight = buildDoubleAddButton("customAddALlRightButton", "addAllToRight");
	/** The add all qdm left. */
	private Button addAllQDMLeft = buildDoubleAddButton("customAddAllLeftButton", "addAllToLeft");
	
	/** The error messages. */
	
	/** The measure package success msg. */
	private SuccessMessageDisplay measurePackageSuccessMsg = new SuccessMessageDisplay();
	/** The measure package warning msg. */
	private WarningMessageDisplay measurePackageWarningMsg = new WarningMessageDisplay();
	/** The package success messages. */
	private SuccessMessageDisplay packageSuccessMessages = new SuccessMessageDisplay();
	/** The supp data success messages. */
	private SuccessMessageDisplay suppDataSuccessMessages = new SuccessMessageDisplay();
	/** The package measure. */
	private PrimaryButton packageMeasure = new PrimaryButton(
			"Create Measure Package", "primaryButton");
	/** The content. */
	private FlowPanel content = new FlowPanel();
	/** The add qdm element button panel. */
	private Widget addQDMElementButtonPanel = buildQDMElementAddButtonWidget();
	/** The qdm elements panel. */
	private FlowPanel qdmElementsPanel = new FlowPanel();
	/** The supp elements panel. */
	private FlowPanel suppElementsPanel = new FlowPanel();
	/** The add qdm elements to measure. */
	private PrimaryButton addQDMElementsToMeasure = new PrimaryButton("Save Supplemental Data Elements", "primaryButton");
	/** The qdm tab name. */
	private Label supplementalDataElementHeader = new Label("Supplemental Data Elements");
	/**	MeasurePackageClauseListWidget. *  */
	private MeasurePackageClauseCellListWidget packageGroupingWidget = new MeasurePackageClauseCellListWidget();
	/** The Create New Grouping Button. */
	private PrimaryButton createNew = new PrimaryButton("Create New Grouping");
	/** The CellTable for Measure Grouping. */
	
	/** The Vertical Panel for Cell Table. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	/** The measureGrouping List. */
	/** The Observer. */
	private Observer observer;
	
	//added for adding cell list
	private CellList<QualityDataSetDTO> qdmCellList;
	private ListDataProvider<QualityDataSetDTO> qdmListProv;
	private ArrayList<QualityDataSetDTO> qdmPopulationList = new ArrayList<QualityDataSetDTO>();
	private CellList<QualityDataSetDTO> supDataCellList;
	private ListDataProvider<QualityDataSetDTO> supListProv;
	private ArrayList<QualityDataSetDTO> supPopulationList = new ArrayList<QualityDataSetDTO>();
	private SingleSelectionModel<QualityDataSetDTO> qdmSelModel	=
			new SingleSelectionModel<QualityDataSetDTO>();
	private SingleSelectionModel<QualityDataSetDTO> supDataSelModel =
			new SingleSelectionModel<QualityDataSetDTO>();
	
	private ScrollPanel leftPagerPanel = new ScrollPanel();
	private ScrollPanel rightPagerPanel = new ScrollPanel();
	
	/**
	 * Constructor.
	 */
	public MeasurePackagerView() {
		addQDMElementLeftRightClickHandlers();
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
		cellTablePanel.removeStyleName("valueSetSearchPanel");
		content.getElement().setAttribute("id", "MeasurePackagerContentFlowPanel");
		createNew.getElement().setAttribute("id", "CreateNewGroupingButton");
		content.add(cellTablePanel);
		content.add(new SpacerWidget());
		content.add(createNew);
		content.add(packageGroupingWidget.getWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(topQDMElementContainer);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(measurePackageSuccessMsg);
		content.add(measurePackageWarningMsg);
		packageMeasure.setTitle("Create Measure Package");
		content.add(packageMeasure);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.setStyleName("contentPanel");
	}
	
	/**
	 * Button Left/Right/LeftAll/RightAll handler's.
	 */
	private void addQDMElementLeftRightClickHandlers() {
		addQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ((qdmPopulationList.size() > 0)
						&& (qdmSelModel.getSelectedObject() != null)) {
					supPopulationList.add(qdmSelModel.getSelectedObject());
					qdmPopulationList.remove(qdmSelModel.getSelectedObject());
					Collections.sort(supPopulationList , new QualityDataSetDTO.Comparator());
					Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());
					rightPagerPanel.clear();
					rightPagerPanel.add(getSupCellList());
					leftPagerPanel.clear();
					leftPagerPanel.add(getQdmCellList());
					qdmSelModel.clear();
				}
			}
		});
		addQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ((supPopulationList.size() > 0)
						&& (supDataSelModel.getSelectedObject() != null)) {
					qdmPopulationList.add(supDataSelModel.getSelectedObject());
					supPopulationList.remove(supDataSelModel.getSelectedObject());
					Collections.sort(supPopulationList , new QualityDataSetDTO.Comparator());
					Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());
					rightPagerPanel.clear();
					rightPagerPanel.add(getSupCellList());
					leftPagerPanel.clear();
					leftPagerPanel.add(getQdmCellList());
					supDataSelModel.clear();
				}
			}
		});
		addAllQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (qdmPopulationList.size() != 0) {
					supPopulationList.addAll(qdmPopulationList);
					qdmPopulationList.removeAll(qdmPopulationList);
					Collections.sort(supPopulationList , new QualityDataSetDTO.Comparator());
					supDataSelModel.clear();
					qdmSelModel.clear();
					rightPagerPanel.clear();
					rightPagerPanel.add(getSupCellList());
					leftPagerPanel.clear();
					leftPagerPanel.add(getQdmCellList());
				}
			}
		});
		addAllQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (supPopulationList.size() != 0) {
					qdmPopulationList.addAll(supPopulationList);
					supPopulationList.removeAll(supPopulationList);
					Collections.sort(qdmPopulationList , new QualityDataSetDTO.Comparator());
					supDataSelModel.clear();
					qdmSelModel.clear();
					rightPagerPanel.clear();
					rightPagerPanel.add(getSupCellList());
					leftPagerPanel.clear();
					leftPagerPanel.add(getQdmCellList());
				}
			}
		});
	}
	/**
	 * Builds the qdm element left right panel.
	 * @return the panel
	 */
	private Panel buildQDMElementLeftRightPanel() {
		qdmElementsPanel.clear();
		suppElementsPanel.clear();
		SimplePanel panel = new SimplePanel();
		panel.getElement().setAttribute("id", "QDMLeftRightButtonSimplePanel");
		FlowPanel qdmTopContainer = new FlowPanel();
		qdmTopContainer.getElement().setAttribute("id", "QDMTopContainerFlowPanel");
		VerticalPanel vPanel = new VerticalPanel();
		VerticalPanel sPanel = new VerticalPanel();
		vPanel.getElement().setAttribute("id", "QDMLeftRightButtonVerticalPanel");
		supplementalDataElementHeader.setStyleName("valueSetHeader");
		supplementalDataElementHeader.getElement().setAttribute("id", "SupplementalDataElementHeadingLabel");
		qdmTopContainer.add(supplementalDataElementHeader);
		qdmTopContainer.add(suppDataSuccessMessages);
		qdmElementsPanel.addStyleName("newColumn");
		qdmElementsPanel.getElement().setAttribute("id", "QDMElementFlowPanel");
		suppElementsPanel.addStyleName("newColumn");
		suppElementsPanel.getElement().setAttribute("id", "SuppElementFlowPanel");
		addQDMElementButtonPanel.addStyleName("column");
		
		Widget qdmElementsLabel = LabelBuilder.buildLabel(qdmCellList,"QDM Elements");
		qdmElementsLabel.addStyleName("bold");
		sPanel.add(qdmElementsLabel);
		
		leftPagerPanel.addStyleName("measurePackagerSupplementalDatascrollable");
		leftPagerPanel.setSize("320px", "200px");
		leftPagerPanel.setAlwaysShowScrollBars(true);
		leftPagerPanel.add(getQdmCellList());
		
		sPanel.add(leftPagerPanel);
		
		
		
		Widget suppElementsLabel = LabelBuilder.buildLabel(supDataCellList,"Supplemental Data Elements");
		suppElementsLabel.addStyleName("bold");
		
		
		rightPagerPanel.addStyleName("measurePackagerSupplementalDatascrollable");
		rightPagerPanel.setSize("320px", "200px");
		rightPagerPanel.setAlwaysShowScrollBars(true);
		rightPagerPanel.add(getSupCellList());
		vPanel.add(suppElementsLabel);
		vPanel.add(rightPagerPanel);
		suppElementsPanel.add(vPanel);
		sPanel.add(new SpacerWidget());
		qdmElementsPanel.add(sPanel);
		qdmTopContainer.add(qdmElementsPanel);
		qdmTopContainer.add(addQDMElementButtonPanel);
		qdmTopContainer.add(suppElementsPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		qdmTopContainer.add(spacer);
		qdmTopContainer.setStylePrimaryName("valueSetSearchPanel");
		panel.setStylePrimaryName("measurePackageLeftRightPanel");
		qdmTopContainer.add(addQDMElementsToMeasure);
		panel.add(qdmTopContainer);
		return panel;
	}
	
	private CellList<QualityDataSetDTO> getQdmCellList()
	{
		//left cell list initialization
		qdmCellList = new CellList<QualityDataSetDTO>(new ClauseCell());
		qdmCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		qdmSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (qdmSelModel.getSelectedObject() == null) {
					return;
				}
				if (supDataSelModel.getSelectedObject()!=null){
					supDataSelModel.clear();
				}
			}
		});
		qdmListProv = new ListDataProvider<QualityDataSetDTO>(qdmPopulationList);
		qdmListProv.addDataDisplay(qdmCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			qdmCellList.setSelectionModel(qdmSelModel
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		} else {
			qdmCellList.setSelectionModel(new NoSelectionModel<QualityDataSetDTO>()
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		}
		
		//End
		return qdmCellList;
	}
	private CellList<QualityDataSetDTO> getSupCellList()
	{
		//left cell list initialization
		supDataCellList = new CellList<QualityDataSetDTO>(new ClauseCell());
		supDataCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		supListProv = new ListDataProvider<QualityDataSetDTO>(supPopulationList);
		supListProv.addDataDisplay(supDataCellList);
		supDataSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (supDataSelModel.getSelectedObject() == null) {
					return;
				}
				if (qdmSelModel.getSelectedObject()!=null){
					qdmSelModel.clear();
				}
				
			}
		});
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			supDataCellList.setSelectionModel(supDataSelModel
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		} else {
			supDataCellList.setSelectionModel(new NoSelectionModel<QualityDataSetDTO>()
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		}
		//End
		return supDataCellList;
	}
	
	/**
	 * Builds the qdm element add button widget.
	 *
	 * @return the widget
	 */
	private Widget buildQDMElementAddButtonWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().setAttribute("id", "QDMButtonVerticalPanel");
		panel.setStyleName("qdmElementAddButtonPanel");
		addQDMRight.setTitle("Add QDM element to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt", "Add QDM element to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove QDM Element from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt", "Remove QDM Element from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all QDM Elements to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt", "Add all QDM Elements to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all QDM Elements from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt", "Remove all QDM Elements from Supplemental Data Elements");
		panel.add(addQDMRight);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addQDMLeft);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addAllQDMRight);
		panel.add(new SpacerWidget());
		panel.add(new SpacerWidget());
		panel.add(addAllQDMLeft);
		return panel;
	}
	/**
	 * Add columns to Measure Grouping Cell Table.
	 * @param table -  CellTable.
	 */
	private  void addColumnToTable(CellTable<MeasurePackageDetail> table) {
		Column<MeasurePackageDetail, SafeHtml> measureGrouping = new Column<MeasurePackageDetail, SafeHtml>(new SafeHtmlCell()){
			@Override
			public SafeHtml getValue(MeasurePackageDetail object) {
				return CellTableUtility.getColumnToolTip(object.getPackageName());
			}
		};
		table.addColumn(measureGrouping, SafeHtmlUtils.fromSafeConstant("<span title='Grouping'>" + "Grouping"
				+ "</span>"));
		Cell<String> editButtonCell = new MatButtonCell("Click to Edit Measure Grouping", "customEditButton");
		Column<MeasurePackageDetail, String> editColumn = new Column<MeasurePackageDetail, String>(editButtonCell) {
			@Override
			public String getValue(MeasurePackageDetail object) {
				return "Edit";
			}
		};
		editColumn.setFieldUpdater(new FieldUpdater<MeasurePackageDetail, String>() {
			@Override
			public void update(int index, MeasurePackageDetail object, String value) {
				packageGroupingWidget.getDisclosurePanelAssociations().setVisible(false);
				packageGroupingWidget.getDisclosurePanelItemCountTable().setVisible(false);
				observer.onEditClicked(object);
			}
		});
		table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit"
				+ "</span>"));
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			Cell<String> deleteButtonCell = new MatButtonCell("Click to Delete Measure Grouping", "customDeleteButton");
			Column<MeasurePackageDetail, String> deleteColumn = new Column<MeasurePackageDetail, String>(deleteButtonCell) {
				@Override
				public String getValue(MeasurePackageDetail object) {
					return "Delete";
				}
			};
			deleteColumn.setFieldUpdater(new FieldUpdater<MeasurePackageDetail, String>() {
				@Override
				public void update(int index, MeasurePackageDetail object, String value) {
					packageGroupingWidget.getDisclosurePanelAssociations().setVisible(false);
					packageGroupingWidget.getDisclosurePanelItemCountTable().setVisible(false);
					observer.onDeleteClicked(object);
				}
			});
			table.addColumn(deleteColumn, SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete"
					+ "</span>"));
			table.setColumnWidth(0, 60.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
		} else {
			table.setColumnWidth(0, 80.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
		}
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#buildCellTable(java.util.List)
	 */
	@Override
	public void buildCellTable(List<MeasurePackageDetail> packages) {
		cellTablePanel.clear();
		if ((packages != null) && (packages.size() > 0)) {
			CellTable<MeasurePackageDetail> table = new CellTable<MeasurePackageDetail>();
			Label measureSearchHeader = new Label("Measure Grouping List");
			measureSearchHeader.getElement().setId("measureGroupingHeader_Label");
			measureSearchHeader.setStyleName("measureGroupingTableHeader");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			measureSearchHeader.getElement().setAttribute("tabIndex", "0");
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(measureSearchHeader.getElement());
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<MeasurePackageDetail> sortProvider = new ListDataProvider<MeasurePackageDetail>();
			List<MeasurePackageDetail> measureGroupingList = new ArrayList<MeasurePackageDetail>();
			measureGroupingList.addAll(packages);
			table.setRowData(measureGroupingList);
			table.setPageSize(2);
			table.redraw();
			table.setRowCount(measureGroupingList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(measureGroupingList);
			addColumnToTable(table);
			sortProvider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			table.setWidth("100%");
			Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureGroupingSummary",
					"In the following Measure Grouping List table, Grouping Name is given in first column,"
							+ " Edit in second column and Delete in third column.");
			table.getElement().setAttribute("id", "MeasureGroupingCellTable");
			table.getElement().setAttribute("aria-describedby", "measureGroupingSummary");
			cellTablePanel.add(invisibleLabel);
			cellTablePanel.add(table);
			if ((measureGroupingList != null) && (measureGroupingList.size() > 2)) {
				MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
				spager.setPageStart(0);
				spager.setDisplay(table);
				spager.setPageSize(2);
				cellTablePanel.add(new SpacerWidget());
				cellTablePanel.add(spager);
			}
			cellTablePanel.setStylePrimaryName("valueSetSearchPanel");
		} else {
			cellTablePanel.removeStyleName("valueSetSearchPanel");
		}
	}
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url.
	 * @param id button Id.
	 * @return button.
	 */
	private Button buildAddButton(String imageUrl, String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Builds the double add button.
	 *
	 * @param imageUrl the image url
	 * @param id button Id.
	 * @return the button
	 */
	private Button buildDoubleAddButton(String imageUrl, String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setQDMElementsInSuppElements(java.util.List)
	 */
	@Override
	public final void setQDMElementsInSuppElements(final List<QualityDataSetDTO> clauses) {
		supPopulationList.clear();
		supPopulationList.addAll(clauses);
		Collections.sort(supPopulationList, new QualityDataSetDTO.Comparator());
		
		rightPagerPanel.clear();
		rightPagerPanel.add(getSupCellList());
		supDataSelModel.clear();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getQDMElementsInSuppElements()
	 */
	@Override
	public final List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return supPopulationList;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setViewIsEditable(boolean, java.util.List)
	 */
	@Override
	public final void setViewIsEditable(final boolean b, final List<MeasurePackageDetail> packages) {
		if (ConstantMessages.CONTINUOUS_VARIABLE_SCORING.equals(
				MatContext.get().getCurrentMeasureScoringType()) && (packages.size() > 0)) {
			createNew.setEnabled(false);
		} else {
			createNew.setEnabled(b);
		}
		packageMeasure.setEnabled(b);
		addQDMElementsToMeasure.setEnabled(b);
		packageGroupingWidget.getSaveGrouping().setEnabled(b);
		addAllQDMLeft.setEnabled(b);
		addAllQDMRight.setEnabled(b);
		addQDMLeft.setEnabled(b);
		addQDMRight.setEnabled(b);
		packageGroupingWidget.getAddClauseRight().setEnabled(b);
		packageGroupingWidget.getAddClauseLeft().setEnabled(b);
		packageGroupingWidget.getAddAllClauseRight().setEnabled(b);
		packageGroupingWidget.getAddAllClauseLeft().setEnabled(b);
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setQDMElements(java.util.List)
	 */
	@Override
	public final void setQDMElements(final List<QualityDataSetDTO> clauses) {
		qdmPopulationList.clear();
		qdmPopulationList.addAll(clauses);
		Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());
		leftPagerPanel.clear();
		leftPagerPanel.add(getQdmCellList());
		qdmSelModel.clear();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getQDMElements()
	 */
	@Override
	public final List<QualityDataSetDTO> getQDMElements() {
		return qdmPopulationList;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasurePackageSuccessMsg()
	 */
	@Override
	public SuccessMessageDisplayInterface getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasurePackageWarningMsg()
	 */
	@Override
	public WarningMessageDisplay getMeasurePackageWarningMsg() {
		return measurePackageWarningMsg;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getPackageErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getPackageErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getPackageMeasureButton()
	 */
	@Override
	public HasClickHandlers getPackageMeasureButton() {
		return packageMeasure;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getPackageSuccessMessageDisplay()
	 */
	@Override
	public SuccessMessageDisplayInterface getPackageSuccessMessageDisplay() {
		return packageGroupingWidget.getSuccessMessages();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#asWidget()
	 */
	@Override
	public final Widget asWidget() {
		return content;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getAddQDMElementsToMeasureButton()
	 */
	@Override
	public final HasClickHandlers getAddQDMElementsToMeasureButton() {
		return addQDMElementsToMeasure;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getSuppDataSuccessMessageDisplay()
	 */
	@Override
	public final SuccessMessageDisplayInterface getSuppDataSuccessMessageDisplay() {
		return suppDataSuccessMessages;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setClauses(java.util.List)
	 */
	@Override
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		packageGroupingWidget.getClausesPopulationList().clear();
		packageGroupingWidget.getClausesPopulationList().addAll(clauses);
		packageGroupingWidget.getLeftPagerPanel().setDisplay(packageGroupingWidget.getLeftCellList());
		packageGroupingWidget.getLeftRangeLabelPager().setDisplay(packageGroupingWidget.getLeftCellList());
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setPackageName(java.lang.String)
	 */
	@Override
	public void setPackageName(String name) {
		packageGroupingWidget.getPackageName().setText(name);
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setClausesInPackage(java.util.List)
	 */
	@Override
	public void setClausesInPackage(List<MeasurePackageClauseDetail> list) {
		packageGroupingWidget.getGroupingPopulationList().clear();
		packageGroupingWidget.getGroupingPopulationList().addAll(list);
		packageGroupingWidget.getRightPagerPanel().setDisplay(packageGroupingWidget.getRightCellList());
		packageGroupingWidget.getRightRangeLabelPager().setDisplay(packageGroupingWidget.getRightCellList());
	}
	/**
	 * @return the packageGroupingWidget
	 */
	@Override
	public MeasurePackageClauseCellListWidget getPackageGroupingWidget() {
		return packageGroupingWidget;
	}
	@Override
	public void setAppliedQdmList(QDSAppliedListModel appliedListModel) {
		packageGroupingWidget.setAppliedQdmList(appliedListModel.getAppliedQDMs());
	}
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNew;
	}
	/**
	 * @return Observer.
	 */
	public Observer getObserver() {
		return observer;
	}
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	/**
	 * Clause Cell Class.
	 *
	 */
	class ClauseCell implements Cell<QualityDataSetDTO> {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, QualityDataSetDTO value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getQDMElement() != null) {
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getQDMElement());
				SafeHtml rendered = templates.cell(value.getQDMElement(), safeValue);
				sb.append(rendered);
			}
		}
		@Override
		public boolean dependsOnSelection() {
			return false;
		}
		@Override
		public Set<String> getConsumedEvents() {
			return null;
		}
		@Override
		public boolean handlesSelection() {
			return false;
		}
		@Override
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
			return false;
		}
		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value, NativeEvent event,
				ValueUpdater<QualityDataSetDTO> valueUpdater) {
			
		}
		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
			return false;
		}
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
		}
	}
}