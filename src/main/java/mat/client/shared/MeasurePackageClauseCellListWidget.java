package mat.client.shared;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.safehtml.client.SafeHtmlTemplates;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.measurepackage.MeasurePackageClauseDetail;
import mat.model.QualityDataSetDTO;
import mat.shared.ConstantMessages;
import mat.shared.MatConstants;
import mat.shared.MeasurePackageClauseValidator;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MeasurePackageClauseCellListWidget {
	private static final String STRATIFICATION = "stratification";
	private static final String ADD_CLAUSE_RIGHT = "addClauseRight";
	private static final String ADD_ALL_CLAUSE_RIGHT = "addAllClauseRight";

	private static final String RATIO_ASSOCIATION_WARNING_MSG = "Changing populations in a measure grouping when there are associations assigned "
			+ "will clear all associations in that measure grouping. Do you wish to continue?";

	private String currentUcum;
	private TextBox ucumTextBox;
	private Label ucumLabel;
	private TableCaptionElement detailCaptionElement;

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

	private ErrorMessageAlert errorMessages = new ErrorMessageAlert();

	private MessageAlert successMessages = new SuccessMessageAlert();

	private Map<String, MeasurePackageClauseDetail>  groupingClausesMap = new HashMap<>();

	private SimplePanel clearButtonPanel = new SimplePanel();

	private MeasurePackagerAssociations associations = new MeasurePackagerAssociations();

	private PanelHeader packageGroupingPanelHeader = new PanelHeader();

    public TextBox getUcumTextBox() {
        return ucumTextBox;
    }

    public void setCurrentUcum(String ucumIn) {
        this.currentUcum = ucumIn;
    }

    public CellList<MeasurePackageClauseDetail> getRightCellList() {
        CellList<MeasurePackageClauseDetail> rightCellList = new CellList<>(new RightClauseCell());
        rightCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
        ListDataProvider<MeasurePackageClauseDetail> rightCellListDataProvider = new ListDataProvider<>(groupingPopulationList);
        rightCellListDataProvider.addDataDisplay(rightCellList);
        //Clear the map and re -populate it with current clauses in Grouping List.
        groupingClausesMap.clear();
        if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
            rightCellList.setSelectionModel(rightCellListSelectionModel
                    , DefaultSelectionEventManager.<MeasurePackageClauseDetail>createDefaultManager());
            groupingPopulationList.forEach(detail -> groupingClausesMap.put(detail.getName(), detail));
        } else {
            rightCellList.setSelectionModel(new NoSelectionModel<MeasurePackageClauseDetail>()
                    , DefaultSelectionEventManager.<MeasurePackageClauseDetail>createDefaultManager());

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
		}
	}

	public final Widget getWidget() {
		mainFlowPanel.getElement().setAttribute("id", "MeasurePackageClauseWidget_FlowPanel");
		return mainFlowPanel;
	}

	private Widget buildAddAssociationWidget(ArrayList<MeasurePackageClauseDetail> populationList) {
		associations.buildAddAssociationWidget(populationList);
		addAssocationsWidget.add(associations.asWidget());
		return addAssocationsWidget;
	}

	public MeasurePackageClauseCellListWidget() {

		Panel packageGroupingPanel = new Panel();
		packageGroupingPanel.setType(PanelType.PRIMARY);

		packageGroupingPanelHeader = new PanelHeader();
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

	private void clearAssociations() {
		clearAlerts();
		MeasurePackageClauseDetail selectedClauseCell = rightCellListSelectionModel.getSelectedObject();
		if (selectedClauseCell.getType().equals(MatConstants.MEASURE_OBSERVATION_POPULATION)) {
			groupingClausesMap.get(rightCellListSelectionModel.getSelectedObject().getName()).setAssociatedPopulationUUID(null);
			getClearButtonPanel();
			clearPopulationForMeasureObservation(associatedPopulationList);
			buildAddAssociationWidget(associatedPopulationList);
			addAssocationsWidget.setVisible(true);
		}
	}

	private void getClearButtonPanel() {
		clearButtonPanel.clear();
		SecondaryButton clearAssociationInClause = new SecondaryButton("Clear");
		clearButtonPanel.addStyleName("floatRightButtonPanel");
		clearButtonPanel.add(clearAssociationInClause);
		clearAssociationInClause.addClickHandler(event -> clearAssociations());
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
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
				checkAssociations();
			}
		}
	}

	private void addClauseLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			clearAlerts();
			if (!groupingPopulationList.isEmpty() && rightCellListSelectionModel.getSelectedObject() != null) {
				boolean isPopulationMoveAffectingAssociation = isPopulationAffectingAssociation(rightCellListSelectionModel.getSelectedObject());
				if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(MatContext.get().getCurrentMeasureScoringType())
						&& isPopulationMoveAffectingAssociation && shouldRequireAssociations(groupingPopulationList)) {
					displayWarningMessage(false, isPopulationMoveAffectingAssociation);
				} else {
					moveSelectedPopulationFromRightToLeft(isPopulationMoveAffectingAssociation);
					checkAssociations();
				}
			}
		}
	}

	private boolean isPopulationAffectingAssociation(MeasurePackageClauseDetail selectedPopulation) {
		return ConstantMessages.POPULATION_CONTEXT_ID.equalsIgnoreCase(selectedPopulation.getType()) ||
		MatConstants.MEASURE_OBSERVATION_POPULATION.equalsIgnoreCase(selectedPopulation.getType()) ||
		MatConstants.DENOMINATOR.equalsIgnoreCase(selectedPopulation.getType()) ||
		MatConstants.NUMERATOR.equalsIgnoreCase(selectedPopulation.getType());
	}

	private void sortListAndSetPanelOnAddClick() {
		Collections.sort(groupingPopulationList);
		Collections.sort(clausesPopulationList);
		getRightPagerPanel().setDisplay(getRightCellList());
		getLeftPagerPanel().setDisplay(getLeftCellList());
	}

	private void clearAssociations(MeasurePackageClauseDetail detail) {
		if(MatConstants.DENOMINATOR.equals(detail.getType()) || MatConstants.NUMERATOR.equals(detail.getType()) || MatConstants.MEASURE_OBSERVATION_POPULATION.equalsIgnoreCase(detail.getType())) {
			detail.setAssociatedPopulationUUID(null);
			groupingClausesMap.put(detail.getName(), detail);
		}
	}

	private void addAllClauseRight() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
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
				checkAssociations();
			}
		}
	}

	private void addAllClauseLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			clearAlerts();
			if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(MatContext.get().getCurrentMeasureScoringType()) && shouldRequireAssociations(groupingPopulationList)) {
				displayWarningMessage(true, true);
			} else {
				moveAllPopulationsFromRightToLeft();
				checkAssociations();
			}
		}
	}

	private void displayWarningMessage(boolean isAllPopulations, boolean isPopulationMoveAffectingAssociation) {

		ConfirmationDialogBox confirmationDialogBox = new ConfirmationDialogBox(RATIO_ASSOCIATION_WARNING_MSG, "Yes", "No", null);

		confirmationDialogBox.setObserver(new ConfirmationObserver() {

			@Override
			public void onYesButtonClicked() {
				if (isAllPopulations) {
					moveAllPopulationsFromRightToLeft();
				} else {
					moveSelectedPopulationFromRightToLeft(isPopulationMoveAffectingAssociation);
				}
			}

			@Override
			public void onNoButtonClicked() {
			}

			@Override
			public void onClose() {
			}
		});
		confirmationDialogBox.show();
	}

	private void moveSelectedPopulationFromRightToLeft(boolean isPopulationMoveAffectingAssociation) {
		if(isPopulationMoveAffectingAssociation) {
			groupingPopulationList.forEach(detail -> clearAssociations(detail));
		}
		clausesPopulationList.add(rightCellListSelectionModel.getSelectedObject());
		groupingPopulationList.remove(rightCellListSelectionModel.getSelectedObject());
		groupingClausesMap.remove(rightCellListSelectionModel.getSelectedObject().getName());
		sortListAndSetPanelOnAddClick();
		rightCellListSelectionModel.clear();
		checkAssociations();
	}

	private void moveAllPopulationsFromRightToLeft() {
		if (!groupingPopulationList.isEmpty()) {
			for (MeasurePackageClauseDetail detail : groupingPopulationList) {
				detail.setAssociatedPopulationUUID(null);
			}
			clausesPopulationList.addAll(groupingPopulationList);
			groupingPopulationList.forEach(detail -> groupingClausesMap.remove(detail.getName()));
			groupingPopulationList.clear();
			Collections.sort(clausesPopulationList);
			setPanelOnAddAllClick();
			checkAssociations();
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
		long count = validateGroupingList.stream().filter(mo -> mo.getType().equalsIgnoreCase(MatConstants.MEASURE_OBSERVATION_POPULATION)).count();
		if(ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring) && count > 2 && messages.isEmpty()){
			messages.add(MatContext.get().getMessageDelegate().getMEASURE_OBS_VALIDATION_FOR_GROUPING());
		}
	}

	private int countTypeForAssociation(List<MeasurePackageClauseDetail> clauseList, String type) {
		associatedPopulationList = new ArrayList<>();
		denoAssociatedPopulationList.clear();
		numAssociatedPopulationList.clear();
		int count = 0;
		for (MeasurePackageClauseDetail detail : clauseList) {
			if (type.equals(detail.getType())) {
				count++;
			}
		}
		return count;
	}

	private void clearPopulationForMeasureObservation(List<MeasurePackageClauseDetail> clauseList) {
		associatedPopulationList = new ArrayList<>();
		for (MeasurePackageClauseDetail detail : clauseList) {
			if ((MatConstants.DENOMINATOR.equalsIgnoreCase(detail.getType()) || MatConstants.NUMERATOR.equalsIgnoreCase(detail.getType()))
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

    public void checkAssociations() {
        leftCellListSelectionModel.clear();

        createCaptionElement();
        createUcumLabel();
        createUcumTextBox();

        if (MatContext.get().isCurrentModelTypeFhir()) {
            ucumTextBox.setText(null);
            ucumLabel.setVisible(false);
			ucumTextBox.setVisible(false);
        } else {
			ucumLabel.setVisible(true);
			ucumTextBox.setVisible(true);
            addAssocationsWidget.setVisible(true);
            ucumTextBox.setText(currentUcum);
        }

        String scoring = MatContext.get().getCurrentMeasureScoringType();
        //Show Association only for Ratio Measures.
        if (ConstantMessages.RATIO_SCORING.equalsIgnoreCase(scoring)) {
            clearButtonPanel.clear();
            // If More than one Populations are added in Grouping, Add Association Widget is shown
            if ((countTypeForAssociation(groupingPopulationList, ConstantMessages.POPULATION_CONTEXT_ID) == 2) ||
                    // else if any measure observations are added in Grouping, Add association widget is shown
                    (countTypeForAssociation(groupingPopulationList, ConstantMessages.MEASURE_OBSERVATION_CONTEXT_ID) >= 1)) {
				addAssocationsWidget.getElement().setAttribute("style", "padding:10px;border:1px solid #D3D3D3;");
            	buildAddAssociationWidget(groupingPopulationList);
                addAssocationsWidget.setVisible(true);
                associations.makeAssociationsIsEditable(MatContext.get().getMeasureLockService().checkForEditPermission());
            } else {
                removeAssociationsWidget();
            }
        } else {
            removeAssociationsWidget();
        }
    }

    private void removeAssociationsWidget() {
        if (addAssocationsWidget.getWidgetCount() > 2) {
            addAssocationsWidget.remove(addAssocationsWidget.getWidgetCount() - 1);
        }

		if (MatContext.get().isCurrentModelTypeFhir()) {
			addAssocationsWidget.getElement().setAttribute("style", "display: none;");
		}
    }

    private void createUcumLabel() {
        if (ucumLabel == null) {
            ucumLabel = new Label("Score Unit");
            addAssocationsWidget.getElement().setAttribute("style", "padding:10px;border:1px solid #D3D3D3;");
            addAssocationsWidget.add(ucumLabel);
        }
    }

    private void createCaptionElement() {
        if (detailCaptionElement == null) {
            Element domCaption = DOM.createCaption();
            detailCaptionElement = TableCaptionElement.as(domCaption);
            detailCaptionElement.setInnerHTML("Grouping Details");
            detailCaptionElement.setAttribute("style", "font-weight:bold;color:black;padding:2px;");
            addAssocationsWidget.getElement().appendChild(detailCaptionElement);
        }
    }

    private void createUcumTextBox() {
        if (ucumTextBox == null) {
            ucumTextBox = new TextBox();
            ucumTextBox.setWidth("175px");
            ucumTextBox.getElement().setId("ucum");
            ucumTextBox.getElement().setClassName("search_field");
            ucumTextBox.getElement().setAttribute("placeholder", "UCUM code or name");
            ucumTextBox.getElement().setAttribute("autocomplete", "off");
            ucumTextBox.getElement().setAttribute("onfocus", "setUpUcum()");
            ucumTextBox.getElement().setAttribute("onblur", "ucumBlur()");

            addAssocationsWidget.add(ucumTextBox);
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
        if ((buttonType.equalsIgnoreCase(ADD_CLAUSE_RIGHT) && leftCellListSelectionModel.getSelectedObject().getType().equalsIgnoreCase(MatConstants.MEASURE_OBSERVATION_POPULATION))
                || buttonType.equalsIgnoreCase(ADD_ALL_CLAUSE_RIGHT)) {
            checkForNumberOfMeasureObs(validatGroupingList, messages, scoring);
        }
        if (!messages.isEmpty()) {
            errorMessages.createAlert(messages);
        } else {
            clearAlerts();
        }
        return messages.isEmpty();
    }

	private boolean shouldRequireAssociations(ArrayList<MeasurePackageClauseDetail> validatGroupingList) {
		MeasurePackageClauseValidator validator = new MeasurePackageClauseValidator();
		return validator.canMovingPopulationFromRightToLeftAffectAssociations(validatGroupingList);
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

	public MeasurePackagerAssociations getAssociations() {
		return associations;
	}

	public void setAssociations(MeasurePackagerAssociations associations) {
		this.associations = associations;
	}

	public void setPackageGroupingHeader(String string) {
		packageGroupingPanelHeader.setText(string);
		packageGroupingPanelHeader.setTitle(string);
	}
}
