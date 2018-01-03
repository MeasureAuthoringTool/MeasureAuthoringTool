package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.MeasurePackageClauseValidator;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackageClauseCellListWidget.
 */
public class MeasurePackageClauseCellListWidget {
	/**numerator constant string.**/
	private static final String NUMERATOR = "numerator";
	/**denominator constant string.**/
	private static final String DENOMINATOR = "denominator";
	/**measureObservation constant string.**/
	private static final String MEASURE_OBSERVATION = "measureObservation";
	/** The Constant ASOOCIATED_LIST_SIZE. */
	private static final int ASOOCIATED_LIST_SIZE = 10;
	
	/** The Constant STRATIFICATION. */
	private static final String STRATIFICATION = "stratification";
	
	/** The Constant ADD_CLAUSE_RIGHT. */
	private static final String ADD_CLAUSE_RIGHT = "addClauseRight";
	
	/** The Constant ADD_ALL_CLAUSE_RIGHT. */
	private static final String ADD_ALL_CLAUSE_RIGHT = "addAllClauseRight";
	/**
	 * The HTML templates used to render the ClauseCell.
	 */
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
		@SafeHtmlTemplates.Template("<div title=\"{0}\" style=\"margin-left:5px;\">{1}</div>")
		SafeHtml cell(String title, SafeHtml value);
	}
	/** Create a singleton instance of the templates used to render the cell. */
	private static Templates templates = GWT.create(Templates.class);
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> leftCellList;
	/** The cell list. */
	private CellList<MeasurePackageClauseDetail> rightCellList;
	/** The Association cell list. */
	private CellList<MeasurePackageClauseDetail> associatedCellList;
	/** The Right cell LIst pager panel. */
	private ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel("RightPackagePanel");
	/** The Left cell LIst pager panel. */
	private ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel("LeftPackagePanel");
	/** The range label pager. */
	private RangeLabelPager rightRangeLabelPager = new RangeLabelPager();
	/** The range label pager. */
	private RangeLabelPager leftRangeLabelPager = new RangeLabelPager();
	/** The disclosure panel associations. */
	private DisclosurePanel disclosurePanelAssociations = new DisclosurePanel("Add Associations");
	/** The main flow panel. */
	private FlowPanel mainFlowPanel = new FlowPanel();
	/** The add Grouping to measure. */
	private PrimaryButton saveGrouping = new PrimaryButton("Save Grouping", "primaryButton");
	/** The add Clause Right. */
	private Button addClauseRight = buildAddButton("customAddRightButton", "AddClauseToRight");
	/** The add Clause left. */
	private Button addClauseLeft = buildAddButton("customAddLeftButton", "AddClauseToLeft");
	/** The add all Clause right. */
	private Button addAllClauseRight = buildDoubleAddButton("customAddALlRightButton", "AddAllClauseToRight");
	/** The add all Clause left. */
	private Button addAllClauseLeft = buildDoubleAddButton("customAddAllLeftButton", "AddAllClauseToLeft");
	/** The package name. */
	private Label packageName = new Label();
	/** Item Count Table Selection Model. */
	/** Clauses Selection Model.	 */
	private SingleSelectionModel<MeasurePackageClauseDetail> leftCellListSelectionModel	=
			new SingleSelectionModel<MeasurePackageClauseDetail>();
	/** Grouping Selection Model.	 */
	private SingleSelectionModel<MeasurePackageClauseDetail> rightCellListSelectionModel =
			new SingleSelectionModel<MeasurePackageClauseDetail>();
	/** List of Measure Observations in Array.*/
	private List<String> meaObsList = new ArrayList<String>();
	/** List Data Provider for Right(Package Clauses) cell List.*/
	private ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider;
	/** List Data Provider for Left(Clause) cell List. */
	private ListDataProvider<MeasurePackageClauseDetail> leftCellListDataProvider;
	/** Applied QDM List.**/
	private List<QualityDataSetDTO> appliedQdmList;
	/** List of Elements in Grouping List. */
	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Clause List.*/
	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements in Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> associatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements For Denominator Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> denoAssociatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List of Elements For Numerator Association Population List.*/
	private ArrayList<MeasurePackageClauseDetail> numAssociatedPopulationList =
			new ArrayList<MeasurePackageClauseDetail>();
	/** List Data Provider for Associate Cell List Widget.**/
	private ListDataProvider<MeasurePackageClauseDetail> associationListDataProvider;
	/** Cell List Associate Widget. **/
	private CellList<MeasurePackageClauseDetail> associatedPOPCellList;
	/** Error Message in Package Grouping Section. */
	//private ErrorMessageDisplay errorMessages = new ErrorMessageDisplay();
	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();
	/** Success Message in Package Grouping Section.**/
	private MessageAlert successMessages = new SuccessMessageAlert();
	/**Selection Model for Associate Widget.**/
	private SingleSelectionModel<MeasurePackageClauseDetail> associatedSelectionModel;
	/**Map of Grouping Clauses.**/
	private Map<String, MeasurePackageClauseDetail>  groupingClausesMap = new HashMap<String, MeasurePackageClauseDetail>();
	
	/** The clear button panel. */
	private SimplePanel clearButtonPanel = new SimplePanel();
	/*** Gets the Grouping cell list.
	 * @return the cellList.	 */
	public CellList<MeasurePackageClauseDetail> getRightCellList() {
		rightCellList = new CellList<MeasurePackageClauseDetail>(new RightClauseCell());
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		rightCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(groupingPopulationList);
		rightCellListDataProvider.addDataDisplay(rightCellList);
		//Clear the map and re -populate it with current clauses in Grouping List.
		groupingClausesMap.clear();
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			rightCellList.setSelectionModel(rightCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
			for(MeasurePackageClauseDetail detail : groupingPopulationList){
				groupingClausesMap.put(detail.getName(), detail);
			}
		} else {
			rightCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
			
		}
		
		return rightCellList;
	}
	/**
	 * Gets the Clause Cell List.
	 * @return CellList.
	 */
	public CellList<MeasurePackageClauseDetail> getLeftCellList() {
		leftCellList = new CellList<MeasurePackageClauseDetail>(new LeftClauseCell());
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		leftCellListDataProvider = new ListDataProvider<MeasurePackageClauseDetail>(clausesPopulationList);
		leftCellListDataProvider.addDataDisplay(leftCellList);
		leftCellListSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (leftCellListSelectionModel.getSelectedObject() == null) {
					return;
				}
				//if (rightCellListSelectionModel.getSelectedObject() != null) {
				rightCellListSelectionModel.clear();
				//}
				disclosurePanelAssociations.setVisible(false);
			}
		});
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			leftCellList.setSelectionModel(leftCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			leftCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		return leftCellList;
	}
	/**
	 * Gets the widget.
	 *
	 * @return the widget
	 */
	public final Widget getWidget() {
		mainFlowPanel.getElement().setAttribute("id", "MeasurePackageClauseWidget_FlowPanel");
		return mainFlowPanel;
	}
	/**
	 * Builds the add association widget.
	 * @param populationList - {@link List}.
	 * @return the widget
	 */
	private Widget buildAddAssociationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		disclosurePanelAssociations.clear();
		disclosurePanelAssociations.add(getAssociatedPOPCellListWidget(populationList));
		disclosurePanelAssociations.setOpen(false);
		disclosurePanelAssociations.setVisible(false);
		return disclosurePanelAssociations;
	}
	/**
	 * Instantiates a new cell list with context menu.
	 */
	public MeasurePackageClauseCellListWidget() {
		disclosurePanelAssociations.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_DisclosurePanel");
		addClickHandlersToDisclosurePanels();
		leftPagerPanel.addStyleName("measurePackageCellListscrollable");
		leftPagerPanel.setDisplay(getLeftCellList());
		leftRangeLabelPager.setDisplay(getLeftCellList());
		rightPagerPanel.addStyleName("measurePackageCellListscrollable");
		rightPagerPanel.setDisplay(getRightCellList());
		rightRangeLabelPager.setDisplay(getRightCellList());
		Label packageTabName = new Label("Package Grouping");
		packageTabName.getElement().setAttribute("id", "MeasurePackageClause_PackageHeading_Lbl");
		packageTabName.setStyleName("valueSetHeader");
		mainFlowPanel.add(packageTabName);
		mainFlowPanel.add(new SpacerWidget());
		HorizontalPanel hp = new HorizontalPanel();
		VerticalPanel leftCellListVPanel = new VerticalPanel();
		leftCellListVPanel.getElement().setAttribute("id", "MeasurePackageClause_LeftCellListVPanel");
		leftCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Populations </b>"));
		leftCellListVPanel.add(leftPagerPanel);
		VerticalPanel rightCellListVPanel = new VerticalPanel();
		rightCellListVPanel.getElement().setAttribute("id", "MeasurePackageClause_RightCellListVPanel");
		rightCellListVPanel.add(new HTML("<b style='margin-left:15px;'> Package Grouping </b>"));
		rightCellListVPanel.add(rightPagerPanel);
		hp.add(leftCellListVPanel);
		hp.add(buildClauseAddButtonWidget());
		hp.add(rightCellListVPanel);
		VerticalPanel vp = new VerticalPanel();
		vp.getElement().setAttribute("id", "MeasurePackageClause_MainVPanel");
		disclosurePanelAssociations.clear();
		disclosurePanelAssociations.clear();
		vp.add(disclosurePanelAssociations);
		hp.add(vp);
		hp.getElement().setAttribute("id", "MeasurePackageClause_MainHoriPanel");
		packageName.addStyleName("measureGroupPackageName");
		mainFlowPanel.add(packageName);
		mainFlowPanel.add(errorMessages);
		mainFlowPanel.add(successMessages);
		mainFlowPanel.add(hp);
		mainFlowPanel.add(new SpacerWidget());
		mainFlowPanel.add(saveGrouping);
		mainFlowPanel.setStylePrimaryName("valueSetSearchPanel");
	}
	/**
	 * Click Handlers for Disclosure Panel.
	 */
	private void addClickHandlersToDisclosurePanels() {
		disclosurePanelAssociations.addOpenHandler(new OpenHandler<DisclosurePanel>() {
			@Override
			public void onOpen(OpenEvent<DisclosurePanel> event) {
				disclosurePanelAssociations.setOpen(true);
			}
		});
	}
	
	/**
	 * Adds the association to clauses.
	 */
	private void addAssociationToClauses() {
		errorMessages.clearAlert();
		successMessages.clearAlert();
		MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
		MeasurePackageClauseDetail otherClauseCell = null;
		String existingUuid = groupingClausesMap.get(selectedClauseCell.getName()).getAssociatedPopulationUUID();
		ArrayList<MeasurePackageClauseDetail> interimArrayList = null;
		String otherClauseType = null;
		if (selectedClauseCell.getType().equalsIgnoreCase(DENOMINATOR)) {
			otherClauseType = NUMERATOR;
			interimArrayList = denoAssociatedPopulationList;
		} else if (selectedClauseCell.getType().equalsIgnoreCase(NUMERATOR)) {
			otherClauseType = DENOMINATOR;
			interimArrayList = numAssociatedPopulationList;
		}
		if (interimArrayList != null) {
			for (MeasurePackageClauseDetail detail : interimArrayList) {
				if (detail.isAssociatedPopulation()) {
					groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
					setAssociatedPopulationUUID(detail.getId());
				} else {
					otherClauseCell = detail;
				}
			}
			if (otherClauseCell != null) {
				for (Entry<String, MeasurePackageClauseDetail> entry : groupingClausesMap.entrySet()) {
					if (entry.getValue().getType().equalsIgnoreCase(otherClauseType)) {
						MeasurePackageClauseDetail updateDetails = entry.getValue();
						groupingClausesMap.get(updateDetails.getName()).
						setAssociatedPopulationUUID(otherClauseCell.getId());
						break;
					}
				}
			}
		} else if (selectedClauseCell.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
			String scoring = MatContext.get().getCurrentMeasureScoringType();
			if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)){
				//As there are no more than two entries in meaObsList for Ratio do not need to bother about rest.
				String measureObservation1 = ""; 
				String measureObservation2 = ""; 
				for(String entry : meaObsList){
					if(measureObservation1.isEmpty() || measureObservation1.equalsIgnoreCase("")){
						measureObservation1 = entry;
					}
					else if(measureObservation1.length() > 0){
						measureObservation2 = entry;
					}
				}
				if(selectedClauseCell.getName().equalsIgnoreCase(measureObservation1)){
					otherClauseType = measureObservation2;
					interimArrayList = associatedPopulationList;
				}
				else{
					otherClauseType = measureObservation1;
					interimArrayList = associatedPopulationList;
				}
				if (interimArrayList != null) {
					for (MeasurePackageClauseDetail detail : interimArrayList) {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
							setAssociatedPopulationUUID(detail.getId());
						} else {
							otherClauseCell = detail;
						}
					}
					if (otherClauseCell != null) {
						for (Entry<String, MeasurePackageClauseDetail> entry : groupingClausesMap.entrySet()) {
							if (entry.getValue().getName().equalsIgnoreCase(otherClauseType)) {
								MeasurePackageClauseDetail updateDetails = entry.getValue();
								groupingClausesMap.get(updateDetails.getName()).
								setAssociatedPopulationUUID(otherClauseCell.getId());
								break;
							}
						}
					}
				}
			}else {
				for (MeasurePackageClauseDetail detail : associatedPopulationList) {
					if ((existingUuid != null)
							&& existingUuid.equals(detail.getId())) {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.
									getSelectedObject().getName()).
							setAssociatedPopulationUUID(detail.getId());
						} else {
							groupingClausesMap.get(rightCellListSelectionModel.
									getSelectedObject().getName()).
							setAssociatedPopulationUUID(null);
						}
					} else {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.
									getSelectedObject().getName()).
							setAssociatedPopulationUUID(detail.getId());
							break;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Adds the click handlers to clear association.
	 *
	 * @param secondaryButton the secondary button
	 */
	private void addClickHandlersToClearAssociation(SecondaryButton secondaryButton) {
		secondaryButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clearAlert();
				successMessages.clearAlert();
				MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
				if (selectedClauseCell.getType().equals(MEASURE_OBSERVATION)) {
					groupingClausesMap.get(rightCellListSelectionModel.
							getSelectedObject().getName()).
							setAssociatedPopulationUUID(null);
					getClearButtonPanel();
					clearPopulationForMeasureObservation(associatedPopulationList);
					buildAddAssociationWidget(associatedPopulationList);
					disclosurePanelAssociations.setVisible(true);
					disclosurePanelAssociations.setOpen(true);
				}
			}
		});
	}
	
	/**
	 * Gets the associated pop cell list widget.
	 *
	 * @param populationList - ArrayList.
	 * @return Vertical Panel.
	 */
	private Panel getAssociatedPOPCellListWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		associatedSelectionModel = new SingleSelectionModel<MeasurePackageClauseDetail>();
		associatedPOPCellList = new CellList<MeasurePackageClauseDetail>(getAssociatedPOPCompositeCell());
		associatedPOPCellList.redraw();
		associatedSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
			}
		});
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			associatedPOPCellList.setSelectionModel(associatedSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			associatedPOPCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		associatedPOPCellList.setPageSize(ASOOCIATED_LIST_SIZE);
		associatedPOPCellList.setRowData(populationList);
		associatedPOPCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		associationListDataProvider =
				new ListDataProvider<MeasurePackageClauseDetail>(populationList);
		associationListDataProvider.addDataDisplay(associatedPOPCellList);
		vPanel.setSize("200px", "170px");
		vPanel.add(associatedPOPCellList);
		HorizontalPanel associateWidgetButtonPanel = new HorizontalPanel();
		associateWidgetButtonPanel.addStyleName("floatRightButtonPanel");
		associateWidgetButtonPanel.add(clearButtonPanel);
		vPanel.add(associateWidgetButtonPanel);
		return vPanel;
	}
	
	/**
	 * Gets the clear button panel.
	 *
	 * @return the clear button panel
	 */
	private void getClearButtonPanel() {
		clearButtonPanel.clear();
		SecondaryButton clearAssociationInClause = new SecondaryButton("Clear");
		clearButtonPanel.addStyleName("floatRightButtonPanel");
		clearButtonPanel.add(clearAssociationInClause);
		addClickHandlersToClearAssociation(clearAssociationInClause);
	}
	
	/**
	 * Gets the associated pop composite cell.
	 *
	 * @return Cell.
	 */
	private Cell<MeasurePackageClauseDetail> getAssociatedPOPCompositeCell() {
		ArrayList<HasCell<MeasurePackageClauseDetail, ?>> hasCells = new ArrayList<HasCell<MeasurePackageClauseDetail, ?>>();
		hasCells.add(new HasCell<MeasurePackageClauseDetail, Boolean>() {
			private CheckboxCell chkCell = new CheckboxCell(false, true);
			@Override
			public Cell<Boolean> getCell() {
				return chkCell;
			}
			@Override
			public FieldUpdater<MeasurePackageClauseDetail, Boolean> getFieldUpdater() {
				return new FieldUpdater<MeasurePackageClauseDetail, Boolean>() {
					@Override
					public void update(int index,
							MeasurePackageClauseDetail object, Boolean value) {
						errorMessages.clearAlert();
						successMessages.clearAlert();
						MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.
								getSelectedObject();
						if (selectedClauseCell.getType().equalsIgnoreCase(DENOMINATOR)) {
							for (MeasurePackageClauseDetail detail : denoAssociatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(denoAssociatedPopulationList);
							disclosurePanelAssociations.setOpen(true);
						} else if (selectedClauseCell.getType().equalsIgnoreCase(NUMERATOR)) {
							for (MeasurePackageClauseDetail detail : numAssociatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(numAssociatedPopulationList);
							disclosurePanelAssociations.setOpen(true);
						} else {
							for (MeasurePackageClauseDetail detail : associatedPopulationList) {
								if (detail.getId().equals(object.getId())) {
									detail.setAssociatedPopulation(value);
								} else {
									detail.setAssociatedPopulation(!value);
								}
							}
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(associatedPopulationList);
							disclosurePanelAssociations.setOpen(true);
						}
						addAssociationToClauses();
					}
				};
			}
			@Override
			public Boolean getValue(MeasurePackageClauseDetail object) {
				return object.isAssociatedPopulation();
			} });
		hasCells.add(new HasCell<MeasurePackageClauseDetail, String>() {
			private TextCell cell = new TextCell();
			@Override
			public Cell<String> getCell() {
				return cell;
			}
			@Override
			public String getValue(MeasurePackageClauseDetail object) {
				return object.getName();
			}
			@Override
			public FieldUpdater<MeasurePackageClauseDetail, String> getFieldUpdater() {
				return new FieldUpdater<MeasurePackageClauseDetail, String>() {
					@Override
					public void update(int index, MeasurePackageClauseDetail object,
							String value) {
					}
				};
			}
		}
				);
		Cell<MeasurePackageClauseDetail> associatePopulationCell = new CompositeCell<MeasurePackageClauseDetail>(hasCells) {
			@Override
			public void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected Element getContainerElement(Element parent) {
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}
			@Override
			protected <X> void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb
					, HasCell<MeasurePackageClauseDetail, X> hasCell) {
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</font></td>");
			}
			
		};
		
		return associatePopulationCell;
	}
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url
	 * @param id - String.
	 * @return the button
	 */
	private Button buildAddButton(String imageUrl , String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Builds the double add button.
	 *
	 * @param imageUrl the image url
	 * @param id - String Id.
	 * @return the button
	 */
	private Button buildDoubleAddButton(String imageUrl , String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setStyleName(imageUrl);
		return btn;
	}
	/**
	 * Widget to add Left/right/leftAll/RightAll button's.
	 * @return - Widget.
	 */
	private Widget buildClauseAddButtonWidget() {
		VerticalPanel clauseButtonPanel = new VerticalPanel();
		clauseButtonPanel.setStyleName("qdmElementAddButtonPanel");
		clauseButtonPanel.getElement().setAttribute("id", "ClauseButtonVerticalPanel");
		addClauseRight.setTitle("Add item to Grouping");
		addClauseRight.getElement().setAttribute("alt", "Add item to Grouping");
		addClauseLeft.setTitle("Remove item from Grouping");
		addClauseLeft.getElement().setAttribute("alt", "Remove item from Grouping");
		addAllClauseRight.setTitle("Add all items to Grouping");
		addAllClauseRight.getElement().setAttribute("alt", "Add all items to Grouping");
		addAllClauseLeft.setTitle("Remove all items from Grouping");
		addAllClauseLeft.getElement().setAttribute("alt", "Remove all items from Grouping");
		clauseButtonPanel.add(addClauseRight);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addClauseLeft);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addAllClauseRight);
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(new SpacerWidget());
		clauseButtonPanel.add(addAllClauseLeft);
		clauseButtonHandlers();
		return clauseButtonPanel;
	}
	/**
	 * Button Left/Right/LeftAll/RightAll handler's.
	 */
	private void clauseButtonHandlers() {
		addClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clearAlert();
				successMessages.clearAlert();
				if ((clausesPopulationList.size() > 0)
						&& (leftCellListSelectionModel.getSelectedObject() != null)) {
					ArrayList<MeasurePackageClauseDetail> validateGroupingList = new ArrayList<MeasurePackageClauseDetail>();
					validateGroupingList.addAll(groupingPopulationList);
					validateGroupingList.add(leftCellListSelectionModel.getSelectedObject());
					if (isValid(validateGroupingList, ADD_CLAUSE_RIGHT)) {
						groupingPopulationList.add(leftCellListSelectionModel.getSelectedObject());
						groupingClausesMap.put(leftCellListSelectionModel.getSelectedObject().getName(),
								leftCellListSelectionModel.getSelectedObject());
						clausesPopulationList.remove(leftCellListSelectionModel.getSelectedObject());
						Collections.sort(groupingPopulationList);
						Collections.sort(clausesPopulationList);
						getRightPagerPanel().setDisplay(getRightCellList());
						getLeftPagerPanel().setDisplay(getLeftCellList());
						leftCellListSelectionModel.clear();
					}
				}
			}
		});
		addClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clearAlert();
				successMessages.clearAlert();
				if ((groupingPopulationList.size() > 0)
						&& (rightCellListSelectionModel.getSelectedObject() != null)) {
					String otherClauseType = null;
					if (rightCellListSelectionModel.getSelectedObject().
							getType().equalsIgnoreCase(DENOMINATOR)) {
						otherClauseType = NUMERATOR;
					} else if (rightCellListSelectionModel.getSelectedObject().
							getType().equalsIgnoreCase(NUMERATOR)) {
						otherClauseType = DENOMINATOR;
					}
					//If clause is removed, and if it is associated with any other clause,
					//all it's associations are removed.
					String denomClauseType = null;
					String numClauseType = null;
					boolean isAssociated = false;
					if(rightCellListSelectionModel.getSelectedObject().getName().toLowerCase().startsWith("measure observation") || 
							rightCellListSelectionModel.getSelectedObject().getName().toLowerCase().startsWith("stratification")) {
						groupingClausesMap.put(rightCellListSelectionModel.getSelectedObject().getName(), rightCellListSelectionModel.getSelectedObject()); 
					}
					
					else {
						for (MeasurePackageClauseDetail detail : groupingPopulationList) {
							
							// if the detail is the deonimator, set the denom clause type.
							if(detail.getType().equals(DENOMINATOR)){
								denomClauseType = detail.getName();
							} else if(detail.getType().equals(NUMERATOR)){ // if the detail is the numerator, set the numClauseType
								numClauseType = detail.getName();
							}

							if ((detail.getAssociatedPopulationUUID() != null)
									&& detail.getAssociatedPopulationUUID().equalsIgnoreCase(
											rightCellListSelectionModel.getSelectedObject().getId())) { // if the detail is the selected object, set the associations to null
								detail.setAssociatedPopulationUUID(null);
								groupingClausesMap.put(detail.getName(), detail);
								isAssociated = true;
							} else if (detail.getId().equalsIgnoreCase(
									rightCellListSelectionModel.getSelectedObject().getId())) {
								detail.setAssociatedPopulationUUID(null);
								groupingClausesMap.put(detail.getName(), detail);
								isAssociated = true;
							}
							if((denomClauseType != null)  && isAssociated) {
								groupingClausesMap.get(denomClauseType).setAssociatedPopulationUUID(null);
							}
							if((numClauseType!=null) && isAssociated){
								groupingClausesMap.get(numClauseType).setAssociatedPopulationUUID(null);
							}
							if ((otherClauseType != null)
									&& otherClauseType.equalsIgnoreCase(detail.getType())) {
								detail.setAssociatedPopulationUUID(null);
								groupingClausesMap.put(detail.getName(), detail);
							}
						}
					}
					clausesPopulationList.add(rightCellListSelectionModel.getSelectedObject());
					groupingPopulationList.remove(rightCellListSelectionModel.getSelectedObject());
					groupingClausesMap.remove(rightCellListSelectionModel.getSelectedObject().getName());
					Collections.sort(groupingPopulationList);
					Collections.sort(clausesPopulationList);
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
					rightCellListSelectionModel.clear();
					disclosurePanelAssociations.setVisible(false);
				}
			}
		});
		addAllClauseRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clearAlert();
				successMessages.clearAlert();
				if (clausesPopulationList.size() != 0) {
					ArrayList<MeasurePackageClauseDetail> validateGroupingList =
							new ArrayList<MeasurePackageClauseDetail>();
					validateGroupingList.addAll(clausesPopulationList);
					if (isValid(validateGroupingList, "addAllClauseRight")) {
						groupingPopulationList.addAll(clausesPopulationList);
						for (MeasurePackageClauseDetail detail : groupingPopulationList) {
							groupingClausesMap.put(detail.getName(), detail);
						}
						clausesPopulationList.removeAll(clausesPopulationList);
						Collections.sort(groupingPopulationList);
						rightCellListSelectionModel.clear();
						leftCellListSelectionModel.clear();
						getRightPagerPanel().setDisplay(getRightCellList());
						getLeftPagerPanel().setDisplay(getLeftCellList());
					}
				}
			}
		});
		addAllClauseLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				errorMessages.clearAlert();
				successMessages.clearAlert();
				if (groupingPopulationList.size() != 0) {
					for (MeasurePackageClauseDetail detail : groupingPopulationList) {
						detail.setAssociatedPopulationUUID(null);
					}
					clausesPopulationList.addAll(groupingPopulationList);
					for (MeasurePackageClauseDetail detail : groupingPopulationList) {
						groupingClausesMap.remove(detail.getName());
					}
					groupingPopulationList.removeAll(groupingPopulationList);
					Collections.sort(clausesPopulationList);
					rightCellListSelectionModel.clear();
					leftCellListSelectionModel.clear();
					getRightPagerPanel().setDisplay(getRightCellList());
					getLeftPagerPanel().setDisplay(getLeftCellList());
					disclosurePanelAssociations.setVisible(false);
				}
			}
		});
	}
	
	/**
	 * Check for number of starification.
	 *
	 * @param validateGroupingList the validate grouping list
	 * @param messages the messages
	 */
	public void CheckForNumberOfStratification(
			ArrayList<MeasurePackageClauseDetail> validateGroupingList,
			List<String> messages) {
		int count = 0;
		for (MeasurePackageClauseDetail clause : validateGroupingList) {
			if (clause.getType().equalsIgnoreCase(STRATIFICATION)) {
				count++;
			}
		}
		if ((count > 1) && (messages.size() == 0)) {
			messages.add(MatContext.get().getMessageDelegate()
					.getSTRATIFICATION_VALIDATION_FOR_GROUPING());
			
		}
	}
	
	/**
	 * Check for number of measure observations.
	 *
	 * @param validateGroupingList the validate grouping list
	 * @param messages the messages
	 * @param scoring 
	 */
	public void CheckForNumberOfMeasureObs(
			ArrayList<MeasurePackageClauseDetail> validateGroupingList,
			List<String> messages, String scoring) {
		int count = 0;
		for (MeasurePackageClauseDetail clause : validateGroupingList) {
			if (clause.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
				count++;
			}
		}
		
		if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)){
			if ((count > 2) && (messages.size() == 0)) {
				messages.add(MatContext.get().getMessageDelegate()
						.getMEASURE_OBS_VALIDATION_FOR_GROUPING());

			}else{
				meaObsList.clear();
				for (MeasurePackageClauseDetail entry : validateGroupingList) {
					if (entry.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
						meaObsList.add(entry.getName());
					}
				}
			}
		}
	}
	
	
	/**
	 * Method to count number of Clause types.
	 * 
	 * @param clauseList
	 *            -List.
	 * @param type
	 *            - String.
	 * @return int.
	 */
	private int countTypeForAssociation(
			List<MeasurePackageClauseDetail> clauseList,  String type) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		denoAssociatedPopulationList.clear();
		numAssociatedPopulationList.clear();
		int count = 0;
		MeasurePackageClauseDetail selectedClauseNode = rightCellListSelectionModel.getSelectedObject();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (type.equals(detail.getType())) {
				if (detail.getId().equalsIgnoreCase(selectedClauseNode.getAssociatedPopulationUUID())) {
					detail.setAssociatedPopulation(true);
				} else {
					detail.setAssociatedPopulation(false);
				}
				if (selectedClauseNode.getType().equalsIgnoreCase(DENOMINATOR)) {
					if (!denoAssociatedPopulationList.contains(detail)) {
						denoAssociatedPopulationList.add(detail);
					}
				} else if (selectedClauseNode.getType().equalsIgnoreCase(NUMERATOR)){
					if (!numAssociatedPopulationList.contains(detail)) {
						numAssociatedPopulationList.add(detail);
					}
				}
				count++;
			}
		}
		Collections.sort(denoAssociatedPopulationList);
		Collections.sort(numAssociatedPopulationList);
		return count;
	}
	
	/**
	 * Generate's Association List for Measure Observation in Ratio Measure's.
	 * @param clauseList - List.
	 */
	private void addPopulationForMeasureObservation(
			List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if ((DENOMINATOR.equalsIgnoreCase(detail.getType())
					|| NUMERATOR.equalsIgnoreCase(detail.getType()))
					&& !associatedPopulationList.contains(detail)) {
				if (detail.getId().equalsIgnoreCase(rightCellListSelectionModel.
						getSelectedObject().getAssociatedPopulationUUID())) {
					detail.setAssociatedPopulation(true);
				} else {
					detail.setAssociatedPopulation(false);
				}
				associatedPopulationList.add(detail);
			}
		}
		Collections.sort(associatedPopulationList);
	}
	
	/**
	 * Clear population for measure observation.
	 *
	 * @param clauseList the clause list
	 */
	private void clearPopulationForMeasureObservation(
			List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if ((DENOMINATOR.equalsIgnoreCase(detail.getType())
					|| NUMERATOR.equalsIgnoreCase(detail.getType()))
					&& !associatedPopulationList.contains(detail)) {
				detail.setAssociatedPopulation(false);
				associatedPopulationList.add(detail);
			}
		}
		Collections.sort(associatedPopulationList);
	}
	
	
	/**
	 * Right CellList Clause Cell Class.
	 *
	 */
	class RightClauseCell implements Cell<MeasurePackageClauseDetail> {
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getName() != null) {
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());
				SafeHtml rendered = templates.cell(value.getName(), safeValue);
				sb.append(rendered);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#dependsOnSelection()
		 */
		@Override
		public boolean dependsOnSelection() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#getConsumedEvents()
		 */
		@Override
		public Set<String> getConsumedEvents() {
			return Collections.singleton("click");
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#handlesSelection()
		 */
		@Override
		public boolean handlesSelection() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#isEditing(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
			errorMessages.clearAlert();
			successMessages.clearAlert();
			if(rightCellListSelectionModel.getSelectedObject() != null){
				groupingClausesMap.put(rightCellListSelectionModel.getSelectedObject().getName(), rightCellListSelectionModel.getSelectedObject());
			}
			if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
				leftCellListSelectionModel.clear();
				String scoring = MatContext.get().getCurrentMeasureScoringType();
				//Show Association only for Ratio Measures.
				if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
					if ((value.getType().equalsIgnoreCase(DENOMINATOR))
							|| (value.getType().equalsIgnoreCase(NUMERATOR))) {
						clearButtonPanel.clear();
						// If More than one Populations are added in Grouping, Add Association Widget is shown
						// otherwise available population is added to Denominator and Numerator Association List.
						if (countTypeForAssociation(groupingPopulationList
								, ConstantMessages.POPULATION_CONTEXT_ID) == 2) {
							if ((value.getType().equalsIgnoreCase(DENOMINATOR))) {
								buildAddAssociationWidget(denoAssociatedPopulationList);
							} else {
								buildAddAssociationWidget(numAssociatedPopulationList);
							}
							disclosurePanelAssociations.setVisible(true);
							disclosurePanelAssociations.setOpen(true);
						} else  {
							disclosurePanelAssociations.setVisible(false);
							disclosurePanelAssociations.setOpen(false);
						}
					} else if ((value.getType().equalsIgnoreCase(MEASURE_OBSERVATION))) {
						if(countTypeForAssociation(groupingPopulationList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) >= 1){
							addPopulationForMeasureObservation(groupingPopulationList);
							buildAddAssociationWidget(associatedPopulationList);
							disclosurePanelAssociations.setVisible(true);
							disclosurePanelAssociations.setOpen(true);
						}
						
					} else {
						disclosurePanelAssociations.setVisible(false);
						disclosurePanelAssociations.setOpen(false);
						associatedPopulationList.clear();
					}
				} else {
					disclosurePanelAssociations.setVisible(false);
					disclosurePanelAssociations.setOpen(false);
					associatedPopulationList.clear();
				}
			}
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#resetFocus(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#setValue(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
		}
	}
	/**
	 * Left CellList Clause Cell Class.
	 *
	 */
	class LeftClauseCell implements Cell<MeasurePackageClauseDetail> {
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb) {
			if (value == null) {
				return;
			}
			if (value.getName() != null) {
				SafeHtml safeValue = SafeHtmlUtils.fromString(value.getName());
				SafeHtml rendered = templates.cell(value.getName(), safeValue);
				sb.append(rendered);
			}
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#dependsOnSelection()
		 */
		@Override
		public boolean dependsOnSelection() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#getConsumedEvents()
		 */
		@Override
		public Set<String> getConsumedEvents() {
			return null;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#handlesSelection()
		 */
		@Override
		public boolean handlesSelection() {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#isEditing(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#resetFocus(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#setValue(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
		}
	}
	
	/**
	 * Checks if is valid.
	 *
	 * @param validatGroupingList the validate grouping list
	 * @param buttonType TODO
	 * @return true, if is valid
	 */
	private boolean isValid(ArrayList<MeasurePackageClauseDetail> validatGroupingList,
			String buttonType) {
		MeasurePackageClauseValidator validator = new MeasurePackageClauseValidator();
		List<String> messages = validator.isValidClauseMove(validatGroupingList);
		if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel
				.getSelectedObject().getType()
				.equalsIgnoreCase(STRATIFICATION))
				|| buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
			CheckForNumberOfStratification(validatGroupingList, messages);
		}
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel
				.getSelectedObject().getType()
				.equalsIgnoreCase(MEASURE_OBSERVATION))
				|| buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
			CheckForNumberOfMeasureObs(validatGroupingList, messages , scoring);
		}
		if (messages.size() > 0) {
			errorMessages.createAlert(messages);
		} else {
			errorMessages.clearAlert();
			successMessages.clearAlert();
		}
		return messages.size() == 0;
	}
	
	/**
	 * Gets the grouping population list.
	 *
	 * @return the groupingPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getGroupingPopulationList() {
		return groupingPopulationList;
	}
	
	/**
	 * Sets the grouping population list.
	 *
	 * @param groupingPopulationList the groupingPopulationList to set
	 */
	public void setGroupingPopulationList(List<MeasurePackageClauseDetail> groupingPopulationList) {
		this.groupingPopulationList = (ArrayList<MeasurePackageClauseDetail>) groupingPopulationList;
	}
	
	/**
	 * Gets the clauses population list.
	 *
	 * @return the clausesPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getClausesPopulationList() {
		return clausesPopulationList;
	}
	
	/**
	 * Sets the clauses population list.
	 *
	 * @param clauses the clausesPopulationList to set
	 */
	public void setClausesPopulationList(List<MeasurePackageClauseDetail> clauses) {
		clausesPopulationList = (ArrayList<MeasurePackageClauseDetail>) clauses;
	}
	
	/**
	 * Gets the right pager panel.
	 *
	 * @return the rightPagerPanel
	 */
	public ShowMorePagerPanel getRightPagerPanel() {
		return rightPagerPanel;
	}
	
	/**
	 * Gets the left pager panel.
	 *
	 * @return the leftPagerPanel
	 */
	public ShowMorePagerPanel getLeftPagerPanel() {
		return leftPagerPanel;
	}
	
	/**
	 * Gets the right range label pager.
	 *
	 * @return the rightRangeLabelPager
	 */
	public RangeLabelPager getRightRangeLabelPager() {
		return rightRangeLabelPager;
	}
	
	/**
	 * Gets the left range label pager.
	 *
	 * @return the leftRangeLabelPager
	 */
	public RangeLabelPager getLeftRangeLabelPager() {
		return leftRangeLabelPager;
	}
	
	
	/**
	 * Gets the disclosure panel associations.
	 *
	 * @return the disclosurePanelAssociations
	 */
	public DisclosurePanel getDisclosurePanelAssociations() {
		return disclosurePanelAssociations;
	}
	
	/**
	 * Gets the applied qdm list.
	 *
	 * @return the applied qdm list
	 */
	public List<QualityDataSetDTO> getAppliedQdmList() {
		return appliedQdmList;
	}
	
	/**
	 * Sets the applied qdm list.
	 *
	 * @param appliedQdmList the new applied qdm list
	 */
	public void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList) {
		this.appliedQdmList = appliedQdmList;
	}
	
	/**
	 * Gets the package name.
	 *
	 * @return the packageName
	 */
	public Label getPackageName() {
		return packageName;
	}
	
	/**
	 * Sets the package name.
	 *
	 * @param packageName the packageName to set
	 */
	public void setPackageName(Label packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * Gets the save grouping.
	 *
	 * @return the saveGrouping
	 */
	public PrimaryButton getSaveGrouping() {
		return saveGrouping;
	}
	
	/**
	 * Sets the save grouping.
	 *
	 * @param saveGrouping the saveGrouping to set
	 */
	public void setSaveGrouping(PrimaryButton saveGrouping) {
		this.saveGrouping = saveGrouping;
	}
	
	/**
	 * Gets the adds the clause right.
	 *
	 * @return the addClauseRight
	 */
	public Button getAddClauseRight() {
		return addClauseRight;
	}
	
	/**
	 * Gets the adds the clause left.
	 *
	 * @return the addClauseLeft
	 */
	public Button getAddClauseLeft() {
		return addClauseLeft;
	}
	
	/**
	 * Gets the adds the all clause right.
	 *
	 * @return the addAllClauseRight
	 */
	public Button getAddAllClauseRight() {
		return addAllClauseRight;
	}
	
	/**
	 * Gets the adds the all clause left.
	 *
	 * @return the addAllClauseLeft
	 */
	public Button getAddAllClauseLeft() {
		return addAllClauseLeft;
	}
	
	/**
	 * Gets the error messages.
	 *
	 * @return the errorMessages
	 */
	public ErrorMessageAlert getErrorMessages() {
		return errorMessages;
	}
	
	/**
	 * Sets the error messages.
	 *
	 * @param errorMessages the errorMessages to set
	 */
	public void setErrorMessages(ErrorMessageAlert errorMessages) {
		this.errorMessages = errorMessages;
	}
	
	/**
	 * Gets the success messages.
	 *
	 * @return the successMessages
	 */
	public MessageAlert getSuccessMessages() {
		return successMessages;
	}
	
	/**
	 * Sets the success messages.
	 *
	 * @param successMessages the successMessages to set
	 */
	public void setSuccessMessages(MessageAlert successMessages) {
		this.successMessages = successMessages;
	}
	
	/**
	 * Gets the associated cell list.
	 *
	 * @return the associatedCellList
	 */
	public CellList<MeasurePackageClauseDetail> getAssociatedCellList() {
		return associatedCellList;
	}
	
	/**
	 * Sets the associated cell list.
	 *
	 * @param associatedCellList the associatedCellList to set
	 */
	public void setAssociatedCellList(CellList<MeasurePackageClauseDetail> associatedCellList) {
		this.associatedCellList = associatedCellList;
	}
	
	/**
	 * Gets the associated population list.
	 *
	 * @return the associatedPopulationList
	 */
	public ArrayList<MeasurePackageClauseDetail> getAssociatedPopulationList() {
		return associatedPopulationList;
	}
	
	/**
	 * Sets the associated population list.
	 *
	 * @param associatedPopulationList the associatedPopulationList to set
	 */
	public void setAssociatedPopulationList(ArrayList<MeasurePackageClauseDetail> associatedPopulationList) {
		this.associatedPopulationList = associatedPopulationList;
	}
}
