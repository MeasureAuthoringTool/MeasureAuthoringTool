package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mat.client.CustomPager;
import mat.client.clause.QDSAppliedListModel;
import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.PrimaryButton;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.client.shared.WarningMessageDisplay;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackagerView.
 */
public class MeasurePackagerView implements MeasurePackagePresenter.PackageView {
	
	
	public static interface Observer {
		/**
		 * On edit clicked.
		 * @param result
		 *            the result
		 */
		void onEditClicked(MeasurePackageDetail detail);
		/**
		 * On clone clicked.
		 * @param result
		 *            the result
		 */
		void onDeleteClicked(MeasurePackageDetail detail);
		
	}
	
	
	/** The add qdm right. */
	private Button addQDMRight = buildAddButton("customAddRightButton");
	
	/** The add qdm left. */
	private Button addQDMLeft = buildAddButton("customAddLeftButton");
	
	/** The add all qdm right. */
	private Button addAllQDMRight = buildDoubleAddButton("customAddALlRightButton");
	
	/** The add all qdm left. */
	private Button addAllQDMLeft = buildDoubleAddButton("customAddAllLeftButton");
	
	/** The qdm element id map. */
	private Map<String, QualityDataSetDTO> qdmElementIdMap = new HashMap<String, QualityDataSetDTO>();
	
	/** The error messages. */
	private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
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
	
	/** The qdm elements list box. */
	private ListBox qdmElementsListBox = new ListBox();
	
	/** The supp elements list box. */
	private ListBox suppElementsListBox = new ListBox();
	
	/** The add qdm element button panel. */
	private Widget addQDMElementButtonPanel = buildQDMElementAddButtonWidget();
	
	/** The qdm elements panel. */
	private FlowPanel qdmElementsPanel = new FlowPanel();
	
	/** The supp elements panel. */
	private FlowPanel suppElementsPanel = new FlowPanel();
	
	/** The add qdm elements to measure. */
	private PrimaryButton addQDMElementsToMeasure = new PrimaryButton("Save Supplemental Data Elements", "primaryButton");
	
	/** The qdm tab name. */
	private Label qdmTabName = new Label("Supplemental Data Elements");
	
	/** The list visible count. */
	private final int listVisibleCount = 10;
	
	/**	MeasurePackageClauseListWidget. *  */
	private MeasurePackageClauseCellListWidget packageGroupingWidget = new MeasurePackageClauseCellListWidget();
	
	private PrimaryButton createNew = new PrimaryButton("Create New Grouping");
	
	private Label viewOrEditLabel = new Label();
	
	private CellTable<MeasurePackageDetail> table;
	
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	private List<MeasurePackageDetail> measureGroupingList;
	
	private Observer observer;
	
	private ErrorMessageDisplay measureErrorMessages = new ErrorMessageDisplay();
	
	private  CellTable<QualityDataSetDTO> cellTable;
	
	
	public Observer getObserver() {
		return observer;
	}
	
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/**
	 * Constructor.
	 */
	public MeasurePackagerView() {
		addQDMElementLeftRightClickHandlers();
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
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
		qdmElementsListBox.setVisibleItemCount(listVisibleCount);
		suppElementsListBox.setVisibleItemCount(listVisibleCount);
		content.setStyleName("contentPanel");
		cellTablePanel.setStylePrimaryName("valueSetSearchPanel");
	}
	
	// QDM elements
	/**
	 * Adds the qdm element left right click handlers.
	 */
	private void addQDMElementLeftRightClickHandlers() {
		
		addQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addQDMElementRight();
			}
		});
		addQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addQDMElementLeft();
			}
		});
		addAllQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addAllQDMElementsRight();
			}
		});
		addAllQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				addAllQDMElementsLeft();
			}
		});
	}
	
	// QDM elements
	/**
	 * Builds the qdm element left right panel.
	 * 
	 * @return the panel
	 */
	private Panel buildQDMElementLeftRightPanel() {
		SimplePanel panel = new SimplePanel();
		FlowPanel qdmTopContainer = new FlowPanel();
		VerticalPanel vPanel = new VerticalPanel();
		qdmTabName.setStyleName("valueSetHeader");
		qdmTopContainer.add(qdmTabName);
		qdmTopContainer.add(suppDataSuccessMessages);
		qdmElementsPanel.addStyleName("newColumn");
		suppElementsPanel.addStyleName("newColumn");
		addQDMElementButtonPanel.addStyleName("column");
		Widget qdmElementsLabel = LabelBuilder.buildLabel(qdmElementsListBox,"QDM Elements");
		qdmElementsLabel.addStyleName("bold");
		qdmElementsPanel.add(qdmElementsLabel);
		qdmElementsPanel.add(qdmElementsListBox);
		Widget suppElementsLabel = LabelBuilder.buildLabel(suppElementsListBox,"Supplemental Data Elements");
		suppElementsLabel.addStyleName("bold");
		suppElementsPanel.add(suppElementsLabel);
		suppElementsPanel.add(suppElementsListBox);
		
		SimplePanel wrapper = new SimplePanel();
		wrapper.setStylePrimaryName("measurePackageAddButtonHolder");
		wrapper.add(addQDMElementsToMeasure);
		
		suppElementsPanel.add(new SpacerWidget());
		suppElementsPanel.add(wrapper);
		vPanel.add(suppElementsPanel);
		qdmTopContainer.add(qdmElementsPanel);
		qdmTopContainer.add(addQDMElementButtonPanel);
		qdmTopContainer.add(vPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		qdmTopContainer.add(spacer);
		qdmTopContainer.setStylePrimaryName("valueSetSearchPanel");
		panel.setStylePrimaryName("measurePackageLeftRightPanel");
		panel.add(qdmTopContainer);
		return panel;
	}
	
	/**
	 * Builds the qdm element add button widget.
	 *
	 * @return the widget
	 */
	private Widget buildQDMElementAddButtonWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.setStyleName("qdmElementAddButtonPanel");
		addQDMRight.setTitle("Add QDM element to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt","Add QDM element to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove QDM Element from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt","Remove QDM Element from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all QDM Elements to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt","Add all QDM Elements to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all QDM Elements from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt","Remove all QDM Elements from Supplemental Data Elements");
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
	
	private CellTable<MeasurePackageDetail> addColumnToTable() {
		
		Column<MeasurePackageDetail, SafeHtml> measureGrouping = new Column<MeasurePackageDetail, SafeHtml>(new SafeHtmlCell()){
			
			@Override
			public SafeHtml getValue(MeasurePackageDetail object) {
				return CellTableUtility.getColumnToolTip(object.getPackageName());
			}};
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
					
					observer.onEditClicked(object);
				}
			});
			
			table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit"
					+ "</span>"));
			
			Cell<String> deleteButtonCell = new MatButtonCell("Click to Delete Measure Grouping", "customDeleteButton");
			Column<MeasurePackageDetail, String> deleteColumn = new Column<MeasurePackageDetail, String>(deleteButtonCell) {
				
				@Override
				public String getValue(MeasurePackageDetail object) {
					return "Delete";
				}
			};
			table.addColumn(deleteColumn, SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete"
					+ "</span>"));
			
			
			return table;
	}
	
	
	@Override
	public void buildCellTable(final List<MeasurePackageDetail> packages) {
		cellTablePanel.clear();
		if ((packages != null) && (packages.size() > 0)) {
			table = new CellTable<MeasurePackageDetail>();
			Label measureSearchHeader = new Label("Measure Grouping List");
			measureSearchHeader.getElement().setId("measureGroupingHeader_Label");
			measureSearchHeader.setStyleName("measureGroupingTableHeader");
			com.google.gwt.dom.client.TableElement elem = table.getElement().cast();
			measureSearchHeader.getElement().setAttribute("tabIndex", "0");
			TableCaptionElement caption = elem.createCaption();
			caption.appendChild(measureSearchHeader.getElement());
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
			ListDataProvider<MeasurePackageDetail> sortProvider = new ListDataProvider<MeasurePackageDetail>();
			measureGroupingList = new ArrayList<MeasurePackageDetail>();
			measureGroupingList.addAll(packages);
			table.setRowData(measureGroupingList);
			table.setPageSize(2);
			table.redraw();
			table.setRowCount(measureGroupingList.size(), true);
			sortProvider.refresh();
			sortProvider.getList().addAll(measureGroupingList);
			table = addColumnToTable();
			sortProvider.addDataDisplay(table);
			CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
			table.setWidth("100%");
			table.setColumnWidth(0, 60.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
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
		}
	}
	
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url
	 * @return the button
	 */
	private Button buildAddButton(String imageUrl) {
		Button btn = new Button();
		btn.setStyleName(imageUrl);
		return btn;
	}
	
	/**
	 * Builds the double add button.
	 *
	 * @param imageUrl the image url
	 * @return the button
	 */
	private Button buildDoubleAddButton(String imageUrl) {
		Button btn = new Button();
		btn.setStyleName(imageUrl);
		return btn;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setQDMElementsInSuppElements(java.util.List)
	 */
	@Override
	public final void setQDMElementsInSuppElements(final List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(suppElementsListBox, clauses);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getQDMElementsInSuppElements()
	 */
	@Override
	public final List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return getQDMElementsItems(suppElementsListBox);
	}
	
	// QDM elements
	/**
	 * Sets the qdm elements items.
	 * 
	 * @param lb
	 *            the lb
	 * @param valuesArg
	 *            the values arg
	 */
	private void setQDMElementsItems(final ListBox lb,final List<QualityDataSetDTO> valuesArg) {
		List<QualityDataSetDTO> values = new ArrayList<QualityDataSetDTO>();
		values.addAll(valuesArg);
		Collections.sort(values, new QualityDataSetDTO.Comparator());
		lb.clear();
		for (QualityDataSetDTO nvp : values) {
			lb.addItem(nvp.getQDMElement(), nvp.getId() + nvp.getDataType());
			qdmElementIdMap.put(nvp.getId() + nvp.getDataType(), nvp);
		}
	}
	
	// QDM elements
	/**
	 * Gets the qDM elements items.
	 * 
	 * @param lb
	 *            the lb
	 * @return the qDM elements items
	 */
	private List<QualityDataSetDTO> getQDMElementsItems(final ListBox lb) {
		List<QualityDataSetDTO> list = new ArrayList<QualityDataSetDTO>();
		for (int i = 0; i < lb.getItemCount(); i++) {
			QualityDataSetDTO detail = qdmElementIdMap.get(lb.getValue(i));
			list.add(detail);
		}
		return list;
	}
	
	/**
	 * Adds the qdm element right.
	 */
	private void addQDMElementRight() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(qdmElementsListBox);
		if (nvp != null) {
			removeQDMElementItem(qdmElementsListBox, nvp);
			addQDMElementItem(suppElementsListBox, nvp);
		}
	}
	
	/**
	 * Adds the qdm element left.
	 */
	private void addQDMElementLeft() {
		QualityDataSetDTO nvp = getQDMElementSelectedValue(suppElementsListBox);
		if (nvp != null) {
			removeQDMElementItem(suppElementsListBox, nvp);
			addQDMElementItem(qdmElementsListBox, nvp);
		}
	}
	
	/**
	 * Adds the all qdm elements right.
	 */
	private void addAllQDMElementsRight() {
		addQDMElementItems(suppElementsListBox,
				getQDMElementsItems(qdmElementsListBox));
		qdmElementsListBox.clear();
	}
	
	/**
	 * Adds the all qdm elements left.
	 */
	private void addAllQDMElementsLeft() {
		addQDMElementItems(qdmElementsListBox,
				getQDMElementsItems(suppElementsListBox));
		suppElementsListBox.clear();
	}
	
	// QDM elements
	/**
	 * Adds the qdm element item.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvp
	 *            the nvp
	 */
	private void addQDMElementItem(final ListBox lb, final QualityDataSetDTO nvp) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.add(nvp);
		Collections.sort(list, new QualityDataSetDTO.Comparator());
		setQDMElementsItems(lb, list);
	}
	
	// QDM elements
	/**
	 * Adds the qdm element items.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvpList
	 *            the nvp list
	 */
	private void addQDMElementItems(final ListBox lb,final List<QualityDataSetDTO> nvpList) {
		List<QualityDataSetDTO> list = getQDMElementsItems(lb);
		list.addAll(nvpList);
		setQDMElementsItems(lb, list);
	}
	
	// QDM elements
	/**
	 * Gets the qDM element selected value.
	 * 
	 * @param lb
	 *            the lb
	 * @return the qDM element selected value
	 */
	private QualityDataSetDTO getQDMElementSelectedValue(final ListBox lb) {
		int index = lb.getSelectedIndex();
		QualityDataSetDTO nvp = null;
		if (index >= 0) {
			nvp = qdmElementIdMap.get(lb.getValue(index));
		}
		return nvp;
	}
	
	// QDM elements
	/**
	 * Removes the qdm element item.
	 * 
	 * @param lb
	 *            the lb
	 * @param nvp
	 *            the nvp
	 */
	private void removeQDMElementItem(final ListBox lb,final QualityDataSetDTO nvp) {
		for (int i = 0; i < lb.getItemCount(); i++) {
			if (lb.getValue(i).equals(nvp.getId() + nvp.getDataType())) {
				lb.removeItem(i);
				break;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setViewIsEditable(boolean, java.util.List)
	 */
	@Override
	public final void setViewIsEditable(final boolean b, final List<MeasurePackageDetail> packages) {
		
		packageMeasure.setEnabled(b);
		addQDMElementsToMeasure.setEnabled(b);
		qdmElementsPanel.setVisible(b);
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setQDMElements(java.util.List)
	 */
	@Override
	public final void setQDMElements(final List<QualityDataSetDTO> clauses) {
		setQDMElementsItems(qdmElementsListBox, clauses);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getQDMElements()
	 */
	@Override
	public final List<QualityDataSetDTO> getQDMElements() {
		return getQDMElementsItems(qdmElementsListBox);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessages;
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
		return errorMessages;
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
		return packageSuccessMessages;
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
	
	@Override
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		packageGroupingWidget.getClausesPopulationList().clear();
		packageGroupingWidget.getClausesPopulationList().addAll(clauses);
		packageGroupingWidget.getLeftPagerPanel().setDisplay(packageGroupingWidget.getLeftCellList());
		packageGroupingWidget.getLeftRangeLabelPager().setDisplay(packageGroupingWidget.getLeftCellList());
	}
	
	@Override
	public void setPackageName(String name) {
		packageGroupingWidget.getPackageName().setText(name);
		
	}
	
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
	public void buildQDMListCellTable(QDSAppliedListModel appliedListModel) {
		packageGroupingWidget.setAppliedQdmList(appliedListModel.getAppliedQDMs());
	}
	
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNew;
	}
	
}
