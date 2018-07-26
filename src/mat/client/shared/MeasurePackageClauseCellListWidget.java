package mat.client.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;

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
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

import mat.client.buttons.BlueButton;
import mat.client.buttons.GreenButton;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.MeasurePackageClauseValidator;

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

	private CellList<MeasurePackageClauseDetail> leftCellList;

	private CellList<MeasurePackageClauseDetail> rightCellList;

	private CellList<MeasurePackageClauseDetail> associatedCellList;

	private ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel("RightPackagePanel");

	private ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel("LeftPackagePanel");

	private RangeLabelPager rightRangeLabelPager = new RangeLabelPager();

	private RangeLabelPager leftRangeLabelPager = new RangeLabelPager();

	private VerticalPanel addAssocationsWidget = new VerticalPanel();

	private FlowPanel mainFlowPanel = new FlowPanel();

	private Button saveGroupingButton = buildSaveButton(IconType.SAVE, "Save Grouping") ;
	
	private Button addClauseRight = buildAddButton(IconType.ANGLE_RIGHT, "AddClauseToRight");

	private Button addClauseLeft = buildAddButton(IconType.ANGLE_LEFT, "AddClauseToLeft");

	private Button addAllClauseRight = buildAddButton(IconType.ANGLE_DOUBLE_RIGHT, "AddAllClauseToRight");

	private Button addAllClauseLeft = buildAddButton(IconType.ANGLE_DOUBLE_LEFT, "AddAllClauseToLeft");

	private Label packageName = new Label();

	private SingleSelectionModel<MeasurePackageClauseDetail> leftCellListSelectionModel	= new SingleSelectionModel<MeasurePackageClauseDetail>();
	
	private SingleSelectionModel<MeasurePackageClauseDetail> rightCellListSelectionModel = new SingleSelectionModel<MeasurePackageClauseDetail>();

	private List<String> measureObservationList = new ArrayList<String>();

	private ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider;

	private ListDataProvider<MeasurePackageClauseDetail> leftCellListDataProvider;

	private List<QualityDataSetDTO> appliedQdmList;

	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList = new ArrayList<MeasurePackageClauseDetail>();

	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList = new ArrayList<MeasurePackageClauseDetail>();
	
	private ArrayList<MeasurePackageClauseDetail> associatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();

	private ArrayList<MeasurePackageClauseDetail> denoAssociatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();

	private ArrayList<MeasurePackageClauseDetail> numAssociatedPopulationList = new ArrayList<MeasurePackageClauseDetail>();

	private ListDataProvider<MeasurePackageClauseDetail> associationListDataProvider;

	private CellList<MeasurePackageClauseDetail> associatedPOPCellList;

	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();

	private MessageAlert successMessages = new SuccessMessageAlert();

	private SingleSelectionModel<MeasurePackageClauseDetail> associatedSelectionModel;

	private Map<String, MeasurePackageClauseDetail>  groupingClausesMap = new HashMap<String, MeasurePackageClauseDetail>();
	
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
				rightCellListSelectionModel.clear();
				addAssocationsWidget.setVisible(false);
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
		addAssocationsWidget.clear();
		addAssocationsWidget.setWidth("220px");
		addAssocationsWidget.setHeight("200px");
		
		addAssocationsWidget.add(new HTML("<b style='margin-left:15px;'> Add Associations </b>"));
		addAssocationsWidget.add(getAssociatedPopulationWidget(populationList));
		addAssocationsWidget.setVisible(false);
		return addAssocationsWidget;
	}
	/**
	 * Instantiates a new cell list with context menu.
	 */
	public MeasurePackageClauseCellListWidget() {

		Panel packageGroupingPanel = new Panel(); 
		packageGroupingPanel.setType(PanelType.PRIMARY);
		
		PanelHeader packageGroupingPanelHeader = new PanelHeader(); 
		packageGroupingPanelHeader.setText("Package Grouping");
		packageGroupingPanelHeader.setTitle("Package Grouping");

		PanelBody packageGroupingPanelBody = new PanelBody();
		packageGroupingPanelBody.add(errorMessages);
		packageGroupingPanelBody.add(successMessages);
		packageGroupingPanel.add(packageGroupingPanelHeader);
		packageGroupingPanel.add(packageGroupingPanelBody);
	
		addAssocationsWidget.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_DisclosurePanel");
		leftPagerPanel.addStyleName("measurePackageCellListscrollable");
		leftPagerPanel.setDisplay(getLeftCellList());
		leftRangeLabelPager.setDisplay(getLeftCellList());
		rightPagerPanel.addStyleName("measurePackageCellListscrollable");
		rightPagerPanel.setDisplay(getRightCellList());
		rightRangeLabelPager.setDisplay(getRightCellList());
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
		addAssocationsWidget.clear();
		addAssocationsWidget.clear();
		vp.add(addAssocationsWidget);
		hp.add(vp);
		hp.getElement().setAttribute("id", "MeasurePackageClause_MainHoriPanel");
		packageGroupingPanelBody.add(hp);
		
		
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		packageGroupingPanelBody.add(spacer);
		
		packageGroupingPanelBody.add(saveGroupingButton);
		mainFlowPanel.add(packageGroupingPanel);
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
				for(String entry : measureObservationList){
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
					addAssocationsWidget.setVisible(true);
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
	private VerticalPanel getAssociatedPopulationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		associatedSelectionModel = new SingleSelectionModel<MeasurePackageClauseDetail>();
		associatedPOPCellList = new CellList<MeasurePackageClauseDetail>(getAssociatedPopulationCompositeCell());
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
	private Cell<MeasurePackageClauseDetail> getAssociatedPopulationCompositeCell() {
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
					public void update(int index, MeasurePackageClauseDetail object, String value) {
						// nothing to do here
					}
				};
			}
		});
		
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
			protected <X> void render(Context context, MeasurePackageClauseDetail value, SafeHtmlBuilder sb, HasCell<MeasurePackageClauseDetail, X> hasCell) {
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("&nbsp;</font></td>");
			}
			
		};
		
		return associatePopulationCell;
	}
	
	private Button buildSaveButton(IconType icon, String text) {
		Button button = new BlueButton(text.replaceAll(" ", "_") + "_button", text); 
		button.setIcon(icon);
		button.setTitle("Click to " + text);
		
		return button; 
	}
	
	/**
	 * Builds the add button.
	 *
	 * @param imageUrl the image url
	 * @param id - String.
	 * @return the button
	 */
	private Button buildAddButton(IconType icon , String id) {
		Button btn = new GreenButton(id, "");
		btn.setIcon(icon);
		btn.setPaddingBottom(3.0);
		btn.setPaddingTop(3.0);
		btn.setWidth("50px");
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
					addAssocationsWidget.setVisible(false);
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
					addAssocationsWidget.setVisible(false);
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
	public void checkForNumberOfStratification(
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
				measureObservationList.clear();
				for (MeasurePackageClauseDetail entry : validateGroupingList) {
					if (entry.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
						measureObservationList.add(entry.getName());
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

	class RightClauseCell implements Cell<MeasurePackageClauseDetail> {

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

		@Override
		public boolean dependsOnSelection() {
			return false;
		}

		@Override
		public Set<String> getConsumedEvents() {
			return Collections.singleton("click");
		}

		@Override
		public boolean handlesSelection() {
			return false;
		}

		@Override
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}

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
							addAssocationsWidget.setVisible(true);
						} else  {
							addAssocationsWidget.setVisible(false);
						}
					} else if ((value.getType().equalsIgnoreCase(MEASURE_OBSERVATION))) {
						if(countTypeForAssociation(groupingPopulationList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) >= 1){
							addPopulationForMeasureObservation(groupingPopulationList);
							buildAddAssociationWidget(associatedPopulationList);
							addAssocationsWidget.setVisible(true);
						}
						
					} else {
						addAssocationsWidget.setVisible(false);
						associatedPopulationList.clear();
					}
				} else {
					addAssocationsWidget.setVisible(false);
					associatedPopulationList.clear();
				}
			}
		}

		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}

		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
		}
	}

	class LeftClauseCell implements Cell<MeasurePackageClauseDetail> {

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
		public boolean isEditing(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}

		@Override
		public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value,
				NativeEvent event, ValueUpdater<MeasurePackageClauseDetail> valueUpdater) {
		}

		@Override
		public boolean resetFocus(com.google.gwt.cell.client.Cell.Context context
				, Element parent, MeasurePackageClauseDetail value) {
			return false;
		}

		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context, Element parent, MeasurePackageClauseDetail value) {
		}
	}

	private boolean isValid(ArrayList<MeasurePackageClauseDetail> validatGroupingList,
			String buttonType) {
		MeasurePackageClauseValidator validator = new MeasurePackageClauseValidator();
		List<String> messages = validator.isValidClauseMove(validatGroupingList);
		if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel
				.getSelectedObject().getType()
				.equalsIgnoreCase(STRATIFICATION))
				|| buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
			checkForNumberOfStratification(validatGroupingList, messages);
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

	public ArrayList<MeasurePackageClauseDetail> getGroupingPopulationList() {
		return groupingPopulationList;
	}

	public void setGroupingPopulationList(List<MeasurePackageClauseDetail> groupingPopulationList) {
		this.groupingPopulationList = (ArrayList<MeasurePackageClauseDetail>) groupingPopulationList;
	}

	public ArrayList<MeasurePackageClauseDetail> getClausesPopulationList() {
		return clausesPopulationList;
	}

	public void setClausesPopulationList(List<MeasurePackageClauseDetail> clauses) {
		clausesPopulationList = (ArrayList<MeasurePackageClauseDetail>) clauses;
	}
	
	public ShowMorePagerPanel getRightPagerPanel() {
		return rightPagerPanel;
	}
	
	public ShowMorePagerPanel getLeftPagerPanel() {
		return leftPagerPanel;
	}
	
	public RangeLabelPager getRightRangeLabelPager() {
		return rightRangeLabelPager;
	}

	public RangeLabelPager getLeftRangeLabelPager() {
		return leftRangeLabelPager;
	}
	
	
	public VerticalPanel getAddAssociationsPanel() {
		return addAssocationsWidget;
	}
	
	public List<QualityDataSetDTO> getAppliedQdmList() {
		return appliedQdmList;
	}

	public void setAppliedQdmList(List<QualityDataSetDTO> appliedQdmList) {
		this.appliedQdmList = appliedQdmList;
	}
	
	public Label getPackageName() {
		return packageName;
	}
	
	public void setPackageName(Label packageName) {
		this.packageName = packageName;
	}
	
	public Button getSaveGrouping() {
		return saveGroupingButton;
	}

	public void setSaveGrouping(Button saveGrouping) {
		this.saveGroupingButton = saveGrouping;
	}

	public Button getAddClauseRight() {
		return addClauseRight;
	}

	public Button getAddClauseLeft() {
		return addClauseLeft;
	}

	public Button getAddAllClauseRight() {
		return addAllClauseRight;
	}

	public Button getAddAllClauseLeft() {
		return addAllClauseLeft;
	}

	public ErrorMessageAlert getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(ErrorMessageAlert errorMessages) {
		this.errorMessages = errorMessages;
	}

	public MessageAlert getSuccessMessages() {
		return successMessages;
	}

	public void setSuccessMessages(MessageAlert successMessages) {
		this.successMessages = successMessages;
	}

	public CellList<MeasurePackageClauseDetail> getAssociatedCellList() {
		return associatedCellList;
	}

	public void setAssociatedCellList(CellList<MeasurePackageClauseDetail> associatedCellList) {
		this.associatedCellList = associatedCellList;
	}
	
	public ArrayList<MeasurePackageClauseDetail> getAssociatedPopulationList() {
		return associatedPopulationList;
	}

	public void setAssociatedPopulationList(ArrayList<MeasurePackageClauseDetail> associatedPopulationList) {
		this.associatedPopulationList = associatedPopulationList;
	}
}
