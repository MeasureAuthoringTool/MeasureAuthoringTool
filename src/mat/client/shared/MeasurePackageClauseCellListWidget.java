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
import org.gwtbootstrap3.client.ui.constants.ButtonType;
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

import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.MeasurePackageClauseValidator;

public class MeasurePackageClauseCellListWidget {
	private static final String NUMERATOR = "numerator";
	private static final String DENOMINATOR = "denominator";
	private static final String MEASURE_OBSERVATION = "measureObservation";
	private static final int ASSOCIATED_LIST_SIZE = 10;
	private static final String STRATIFICATION = "stratification";
	private static final String ADD_CLAUSE_RIGHT = "addClauseRight";
	private static final String ADD_ALL_CLAUSE_RIGHT = "addAllClauseRight";

	interface Templates extends SafeHtmlTemplates {
		@SafeHtmlTemplates.Template("<div title=\"{0}\" style=\"margin-left:5px;\">{1}</div>")
		SafeHtml cell(String title, SafeHtml value);
	}

	private static Templates templates = GWT.create(Templates.class);

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

	private SingleSelectionModel<MeasurePackageClauseDetail> leftCellListSelectionModel	= new SingleSelectionModel<>();

	private SingleSelectionModel<MeasurePackageClauseDetail> rightCellListSelectionModel = new SingleSelectionModel<>();

	private List<QualityDataSetDTO> appliedQdmList;

	private ArrayList<MeasurePackageClauseDetail> groupingPopulationList = new ArrayList<>();

	private ArrayList<MeasurePackageClauseDetail> clausesPopulationList = new ArrayList<>();

	private ArrayList<MeasurePackageClauseDetail> associatedPopulationList = new ArrayList<>();

	private ArrayList<MeasurePackageClauseDetail> denoAssociatedPopulationList = new ArrayList<>();

	private ArrayList<MeasurePackageClauseDetail> numAssociatedPopulationList = new ArrayList<>();

	private ListDataProvider<MeasurePackageClauseDetail> associationListDataProvider;

	private CellList<MeasurePackageClauseDetail> associatedPOPCellList;

	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();

	private MessageAlert successMessages = new SuccessMessageAlert();

	private Map<String, MeasurePackageClauseDetail>  groupingClausesMap = new HashMap<>();

	private SimplePanel clearButtonPanel = new SimplePanel();

	public CellList<MeasurePackageClauseDetail> getRightCellList() {
		CellList<MeasurePackageClauseDetail> rightCellList = new CellList<>(new RightClauseCell());
		rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider = new ListDataProvider<>(groupingPopulationList);
		rightCellListDataProvider.addDataDisplay(rightCellList);
		//Clear the map and re -populate it with current clauses in Grouping List.
		groupingClausesMap.clear();
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			rightCellList.setSelectionModel(rightCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
			groupingPopulationList.forEach(detail -> groupingClausesMap.put(detail.getName(), detail));
		} else {
			rightCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());

		}

		return rightCellList;
	}

	public CellList<MeasurePackageClauseDetail> getLeftCellList() {
		CellList<MeasurePackageClauseDetail> leftCellList = new CellList<>(new LeftClauseCell());
		leftCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<MeasurePackageClauseDetail> leftCellListDataProvider = new ListDataProvider<>(clausesPopulationList);
		leftCellListDataProvider.addDataDisplay(leftCellList);
		leftCellListSelectionModel.addSelectionChangeHandler(event -> leftCellListSelectionModel());
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			leftCellList.setSelectionModel(leftCellListSelectionModel
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		} else {
			leftCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
					, DefaultSelectionEventManager.<MeasurePackageClauseDetail> createDefaultManager());
		}
		return leftCellList;
	}

	private void leftCellListSelectionModel() {
		if (leftCellListSelectionModel.getSelectedObject() != null) {
			rightCellListSelectionModel.clear();
			addAssocationsWidget.setVisible(false);
		}
	}

	public final Widget getWidget() {
		mainFlowPanel.getElement().setAttribute("id", "MeasurePackageClauseWidget_FlowPanel");
		return mainFlowPanel;
	}

	private Widget buildAddAssociationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		addAssocationsWidget.clear();
		addAssocationsWidget.setWidth("220px");
		addAssocationsWidget.setHeight("200px");

		addAssocationsWidget.add(new HTML("<b style='margin-left:15px;'> Add Associations </b>"));
		addAssocationsWidget.add(getAssociatedPopulationWidget(populationList));
		addAssocationsWidget.setVisible(false);
		return addAssocationsWidget;
	}

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

	private void addAssociationToClauses() {
		clearAlerts();
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
					groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).setAssociatedPopulationUUID(detail.getId());
				} else {
					otherClauseCell = detail;
				}
			}
			if (otherClauseCell != null) {
				for (Entry<String, MeasurePackageClauseDetail> entry : groupingClausesMap.entrySet()) {
					if (entry.getValue().getType().equalsIgnoreCase(otherClauseType)) {
						MeasurePackageClauseDetail updateDetails = entry.getValue();
						groupingClausesMap.get(updateDetails.getName()).setAssociatedPopulationUUID(otherClauseCell.getId());
						break;
					}
				}
			}

		} else if (selectedClauseCell.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
			String scoring = MatContext.get().getCurrentMeasureScoringType();
			if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)){
				List<String> measureObservationList = buildMeasureObservationList();
				//As there are no more than two entries in meaObsList for Ratio do not need to bother about rest.
				String measureObservation1 = ""; 
				String measureObservation2 = ""; 
				for(String entry : measureObservationList){
					if(measureObservation1.isEmpty()){
						measureObservation1 = entry;
					}
					else if(measureObservation1.length() > 0){
						measureObservation2 = entry;
					}
				}
				
				otherClauseType = selectedClauseCell.getName().equalsIgnoreCase(measureObservation1) ? measureObservation2 : measureObservation1;

				interimArrayList = associatedPopulationList;
				if (interimArrayList != null) {
					for (MeasurePackageClauseDetail detail : interimArrayList) {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).setAssociatedPopulationUUID(detail.getId());
						} else {
							otherClauseCell = detail;
						}
					}
					if (otherClauseCell != null) {
						for (Entry<String, MeasurePackageClauseDetail> entry : groupingClausesMap.entrySet()) {
							if (entry.getValue().getName().equalsIgnoreCase(otherClauseType)) {
								MeasurePackageClauseDetail updateDetails = entry.getValue();
								groupingClausesMap.get(updateDetails.getName()).setAssociatedPopulationUUID(otherClauseCell.getId());
								break;
							}
						}
					}
				}
			}else {
				for (MeasurePackageClauseDetail detail : associatedPopulationList) {
					if (existingUuid != null && existingUuid.equals(detail.getId())) {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
							setAssociatedPopulationUUID(detail.getId());
						} else {
							groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
							setAssociatedPopulationUUID(null);
						}
					} else {
						if (detail.isAssociatedPopulation()) {
							groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).
							setAssociatedPopulationUUID(detail.getId());
							break;
						}
					}
				}
			}
		}
	}

	private void clearAssociations() {
		clearAlerts();
		MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
		if (selectedClauseCell.getType().equals(MEASURE_OBSERVATION)) {
			groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).setAssociatedPopulationUUID(null);
			getClearButtonPanel();
			clearPopulationForMeasureObservation(associatedPopulationList);
			buildAddAssociationWidget(associatedPopulationList);
			addAssocationsWidget.setVisible(true);
		}
	}

	private VerticalPanel getAssociatedPopulationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		VerticalPanel vPanel = new VerticalPanel();
		vPanel.getElement().setAttribute("id", "MeasurePackageClause_AssoWgt_VerticalPanel");
		SingleSelectionModel<MeasurePackageClauseDetail> associatedSelectionModel = new SingleSelectionModel<>();
		associatedPOPCellList = new CellList<>(getAssociatedPopulationCompositeCell());
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
		associatedPOPCellList.setPageSize(ASSOCIATED_LIST_SIZE);
		associatedPOPCellList.setRowData(populationList);
		associatedPOPCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		associationListDataProvider = new ListDataProvider<>(populationList);
		associationListDataProvider.addDataDisplay(associatedPOPCellList);
		vPanel.setSize("200px", "170px");
		vPanel.add(associatedPOPCellList);
		HorizontalPanel associateWidgetButtonPanel = new HorizontalPanel();
		associateWidgetButtonPanel.addStyleName("floatRightButtonPanel");
		associateWidgetButtonPanel.add(clearButtonPanel);
		vPanel.add(associateWidgetButtonPanel);
		return vPanel;
	}

	private void getClearButtonPanel() {
		clearButtonPanel.clear();
		SecondaryButton clearAssociationInClause = new SecondaryButton("Clear");
		clearButtonPanel.addStyleName("floatRightButtonPanel");
		clearButtonPanel.add(clearAssociationInClause);
		clearAssociationInClause.addClickHandler(event -> clearAssociations());
	}

	private Cell<MeasurePackageClauseDetail> getAssociatedPopulationCompositeCell() {
		ArrayList<HasCell<MeasurePackageClauseDetail, ?>> hasCells = new ArrayList<>();
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
					public void update(int index, MeasurePackageClauseDetail object, Boolean value) {
						clearAlerts();
						MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
						if (selectedClauseCell.getType().equalsIgnoreCase(DENOMINATOR)) {
							denoAssociatedPopulationList.forEach(detail -> detail.setAssociatedPopulation(detail.getId().equals(object.getId()) ? value : !value));
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(denoAssociatedPopulationList);
						} else if (selectedClauseCell.getType().equalsIgnoreCase(NUMERATOR)) {
							numAssociatedPopulationList.forEach(detail -> detail.setAssociatedPopulation(detail.getId().equals(object.getId()) ? value : !value));
							associationListDataProvider.flush();
							associatedPOPCellList.setRowData(numAssociatedPopulationList);
						} else {
							associatedPopulationList.forEach(detail -> detail.setAssociatedPopulation(detail.getId().equals(object.getId()) ? value : !value));
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
		Button button = new Button(); 
		button.getElement().setAttribute("id", text.replaceAll(" ", "_") + "_button");
		button.setType(ButtonType.PRIMARY);
		button.setIcon(icon);
		button.setText(text);
		button.setTitle("Click to " + text);

		return button; 
	}

	private Button buildAddButton(IconType icon , String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setIcon(icon);
		btn.setType(ButtonType.SUCCESS);
		btn.setPaddingBottom(3.0);
		btn.setPaddingTop(3.0);
		btn.setWidth("50px");
		return btn;
	}

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

	private void clauseButtonHandlers() {
		addClauseRight.addClickHandler(event -> addClauseRight());
		addClauseLeft.addClickHandler(event -> addClauseLeft());
		addAllClauseRight.addClickHandler(event -> addAllClauseRight());
		addAllClauseLeft.addClickHandler(event -> addAllClauseLeft());
	}

	private void addClauseRight() {
		clearAlerts();
		if (!clausesPopulationList.isEmpty() && leftCellListSelectionModel.getSelectedObject() != null) {
			ArrayList<MeasurePackageClauseDetail> validateGroupingList = new ArrayList<>();
			validateGroupingList.addAll(groupingPopulationList);
			validateGroupingList.add(leftCellListSelectionModel.getSelectedObject());
			if (isValid(validateGroupingList, ADD_CLAUSE_RIGHT)) {
				groupingPopulationList.add(leftCellListSelectionModel.getSelectedObject());
				groupingClausesMap.put(leftCellListSelectionModel.getSelectedObject().getName(), leftCellListSelectionModel.getSelectedObject());
				clausesPopulationList.remove(leftCellListSelectionModel.getSelectedObject());
				sortListAndSetPanelOnAddClick();
				leftCellListSelectionModel.clear();
			}
		}

	}

	private void addClauseLeft() {
		clearAlerts();
		if (!groupingPopulationList.isEmpty() && rightCellListSelectionModel.getSelectedObject() != null) {

			createGroupingClausesMap();

			clausesPopulationList.add(rightCellListSelectionModel.getSelectedObject());
			groupingPopulationList.remove(rightCellListSelectionModel.getSelectedObject());
			groupingClausesMap.remove(rightCellListSelectionModel.getSelectedObject().getName());
			sortListAndSetPanelOnAddClick();
			rightCellListSelectionModel.clear();
			addAssocationsWidget.setVisible(false);
		}

	}
	
	private void sortListAndSetPanelOnAddClick() {
		Collections.sort(groupingPopulationList);
		Collections.sort(clausesPopulationList);
		getRightPagerPanel().setDisplay(getRightCellList());
		getLeftPagerPanel().setDisplay(getLeftCellList());
	}
	
	private void createGroupingClausesMap() {
		String otherClauseType = null;
		if (rightCellListSelectionModel.getSelectedObject().getType().equalsIgnoreCase(DENOMINATOR)) {
			otherClauseType = NUMERATOR;
		} else if (rightCellListSelectionModel.getSelectedObject().getType().equalsIgnoreCase(NUMERATOR)) {
			otherClauseType = DENOMINATOR;
		}
		//If clause is removed, and if it is associated with any other clause,
		//all it's associations are removed.
		String denomClauseType = null;
		String numClauseType = null;
		boolean isAssociated = false;

		if(rightCellListSelectionModel.getSelectedObject().getName().toLowerCase().startsWith("measure observation") || 
				rightCellListSelectionModel.getSelectedObject().getName().toLowerCase().startsWith(STRATIFICATION)) {
			groupingClausesMap.put(rightCellListSelectionModel.getSelectedObject().getName(), rightCellListSelectionModel.getSelectedObject()); 
		}

		else {
			for (MeasurePackageClauseDetail detail : groupingPopulationList) {

				if(detail.getType().equals(DENOMINATOR)){
					denomClauseType = detail.getName();
				} else if(detail.getType().equals(NUMERATOR)){
					numClauseType = detail.getName();
				}

				if ((detail.getAssociatedPopulationUUID() != null
						&& detail.getAssociatedPopulationUUID().equalsIgnoreCase(rightCellListSelectionModel.getSelectedObject().getId()))
						|| detail.getId().equalsIgnoreCase(rightCellListSelectionModel.getSelectedObject().getId())) { 
					detail.setAssociatedPopulationUUID(null);
					groupingClausesMap.put(detail.getName(), detail);
					isAssociated = true;
				}
				if(denomClauseType != null  && isAssociated) {
					groupingClausesMap.get(denomClauseType).setAssociatedPopulationUUID(null);
				}
				if(numClauseType!=null && isAssociated){
					groupingClausesMap.get(numClauseType).setAssociatedPopulationUUID(null);
				}
				if (otherClauseType != null && otherClauseType.equalsIgnoreCase(detail.getType())) {
					detail.setAssociatedPopulationUUID(null);
					groupingClausesMap.put(detail.getName(), detail);
				}
			}
		}

	}

	private void addAllClauseRight() {
		clearAlerts();
		if (!clausesPopulationList.isEmpty()) {
			ArrayList<MeasurePackageClauseDetail> validateGroupingList = new ArrayList<>();
			validateGroupingList.addAll(groupingPopulationList);
			validateGroupingList.addAll(clausesPopulationList);
			if (isValid(validateGroupingList, ADD_ALL_CLAUSE_RIGHT)) {
				groupingPopulationList.addAll(clausesPopulationList);
				groupingPopulationList.forEach(detail -> groupingClausesMap.put(detail.getName(), detail));
				clausesPopulationList.clear();
				Collections.sort(groupingPopulationList);
				setPanelOnAddAllClick();
			}
		}
	}

	private void addAllClauseLeft() {
		clearAlerts();
		if (!groupingPopulationList.isEmpty()) {
			for (MeasurePackageClauseDetail detail : groupingPopulationList) {
				detail.setAssociatedPopulationUUID(null);
			}
			clausesPopulationList.addAll(groupingPopulationList);
			groupingPopulationList.forEach(detail -> groupingClausesMap.remove(detail.getName()));
			groupingPopulationList.clear();
			Collections.sort(clausesPopulationList);
			setPanelOnAddAllClick();
			addAssocationsWidget.setVisible(false);
		}
	}

	private void setPanelOnAddAllClick() {
		rightCellListSelectionModel.clear();
		leftCellListSelectionModel.clear();
		getRightPagerPanel().setDisplay(getRightCellList());
		getLeftPagerPanel().setDisplay(getLeftCellList());
	}
	
	private void clearAlerts() {
		errorMessages.clearAlert();
		successMessages.clearAlert();
	}

	public void checkForNumberOfStratification(List<MeasurePackageClauseDetail> validateGroupingList, List<String> messages) {
		long count = validateGroupingList.stream().filter(s -> s.getType().equalsIgnoreCase(STRATIFICATION)).count();
		if (count > 1 && messages.isEmpty()) {
			messages.add(MatContext.get().getMessageDelegate().getSTRATIFICATION_VALIDATION_FOR_GROUPING());
		}
	}

	public void checkForNumberOfMeasureObs(List<MeasurePackageClauseDetail> validateGroupingList, List<String> messages, String scoring) {
		long count = validateGroupingList.stream().filter(mo -> mo.getType().equalsIgnoreCase(MEASURE_OBSERVATION)).count();
		if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring) && count > 2 && messages.isEmpty()){
			messages.add(MatContext.get().getMessageDelegate().getMEASURE_OBS_VALIDATION_FOR_GROUPING());
		}
	}

	private List<String> buildMeasureObservationList() {
		List<String> measureObservationList = new ArrayList<>();
		groupingPopulationList.stream().filter(entry -> entry.getType().equalsIgnoreCase(MEASURE_OBSERVATION)).forEach(mo -> measureObservationList.add(mo.getName()));
		return measureObservationList;
	}

	private int countTypeForAssociation(List<MeasurePackageClauseDetail> clauseList, String type) {
		associatedPopulationList = new ArrayList<>();
		denoAssociatedPopulationList.clear();
		numAssociatedPopulationList.clear();
		int count = 0;
		MeasurePackageClauseDetail selectedClauseNode = rightCellListSelectionModel.getSelectedObject();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (type.equals(detail.getType())) {
				detail.setAssociatedPopulation(detail.getId().equalsIgnoreCase(selectedClauseNode.getAssociatedPopulationUUID()));
				if (selectedClauseNode.getType().equalsIgnoreCase(DENOMINATOR) && !denoAssociatedPopulationList.contains(detail)) {
					denoAssociatedPopulationList.add(detail);
				} else if (selectedClauseNode.getType().equalsIgnoreCase(NUMERATOR) && !numAssociatedPopulationList.contains(detail)){
					numAssociatedPopulationList.add(detail);
				}
				count++;
			}
		}
		Collections.sort(denoAssociatedPopulationList);
		Collections.sort(numAssociatedPopulationList);
		return count;
	}

	private void addPopulationForMeasureObservation(List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if ((DENOMINATOR.equalsIgnoreCase(detail.getType()) || NUMERATOR.equalsIgnoreCase(detail.getType())) 
					&& !associatedPopulationList.contains(detail)) {
				if (detail.getId().equalsIgnoreCase(rightCellListSelectionModel.getSelectedObject().getAssociatedPopulationUUID())) {
					detail.setAssociatedPopulation(true);
				} else {
					detail.setAssociatedPopulation(false);
				}
				associatedPopulationList.add(detail);
			}
		}
		Collections.sort(associatedPopulationList);
	}

	private void clearPopulationForMeasureObservation(List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if ((DENOMINATOR.equalsIgnoreCase(detail.getType()) || NUMERATOR.equalsIgnoreCase(detail.getType()))
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
			clearAlerts();
			if(rightCellListSelectionModel.getSelectedObject() != null){
				groupingClausesMap.put(rightCellListSelectionModel.getSelectedObject().getName(), rightCellListSelectionModel.getSelectedObject());
			}
			if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
				leftCellListSelectionModel.clear();
				String scoring = MatContext.get().getCurrentMeasureScoringType();
				//Show Association only for Ratio Measures.
				if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
					if (value.getType().equalsIgnoreCase(DENOMINATOR) || value.getType().equalsIgnoreCase(NUMERATOR)) {
						clearButtonPanel.clear();
						// If More than one Populations are added in Grouping, Add Association Widget is shown
						// otherwise available population is added to Denominator and Numerator Association List.
						if (countTypeForAssociation(groupingPopulationList, ConstantMessages.POPULATION_CONTEXT_ID) == 2) {
							if (value.getType().equalsIgnoreCase(DENOMINATOR)) {
								buildAddAssociationWidget(denoAssociatedPopulationList);
							} else {
								buildAddAssociationWidget(numAssociatedPopulationList);
							}
							addAssocationsWidget.setVisible(true);
						} else  {
							addAssocationsWidget.setVisible(false);
						}
					} else if (value.getType().equalsIgnoreCase(MEASURE_OBSERVATION)) {
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

	private boolean isValid(ArrayList<MeasurePackageClauseDetail> validatGroupingList, String buttonType) {
		MeasurePackageClauseValidator validator = new MeasurePackageClauseValidator();
		List<String> messages = validator.isValidClauseMove(validatGroupingList);
		if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel.getSelectedObject().getType().equalsIgnoreCase(STRATIFICATION))
				|| buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
			checkForNumberOfStratification(validatGroupingList, messages);
		}
		String scoring = MatContext.get().getCurrentMeasureScoringType();
		if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel.getSelectedObject().getType().equalsIgnoreCase(MEASURE_OBSERVATION))
				|| buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
			checkForNumberOfMeasureObs(validatGroupingList, messages , scoring);
		}
		if (!messages.isEmpty()) {
			errorMessages.createAlert(messages);
		} else {
			clearAlerts();
		}
		return messages.isEmpty();
	}

	public List<MeasurePackageClauseDetail> getGroupingPopulationList() {
		return groupingPopulationList;
	}

	public void setGroupingPopulationList(List<MeasurePackageClauseDetail> groupingPopulationList) {
		this.groupingPopulationList = (ArrayList<MeasurePackageClauseDetail>) groupingPopulationList;
	}

	public List<MeasurePackageClauseDetail> getClausesPopulationList() {
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

	public List<MeasurePackageClauseDetail> getAssociatedPopulationList() {
		return associatedPopulationList;
	}

	public void setAssociatedPopulationList(List<MeasurePackageClauseDetail> associatedPopulationList) {
		this.associatedPopulationList = (ArrayList<MeasurePackageClauseDetail>) associatedPopulationList;
	}
}
