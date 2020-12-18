package mat.client.measurepackage;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.CustomPager;
import mat.client.clause.QDSAppliedListModel;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.MessageAlert;
import mat.client.shared.ShowMorePagerPanel;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.model.cql.CQLDefinition;
import mat.shared.ClickableSafeHtmlCell;
import mat.shared.SafeHtmlCell;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;
import org.gwtbootstrap3.client.ui.Panel;
import org.gwtbootstrap3.client.ui.PanelBody;
import org.gwtbootstrap3.client.ui.PanelHeader;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.PanelType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MeasurePackagerView implements MeasurePackagePresenter.PackageView {

	interface Observer {

		void onEditClicked(MeasurePackageDetail detail);

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
	
	private static Templates templates = GWT.create(Templates.class);

	private static final String TWO_HUNDRED_PIXELS = "200px";
	private static final String THREE_HUNDRED_AND_TWENTY_PIXELS = "320px";
	private static final String MEASURE_PACKAGE_CELLLIST_SCROLLABLE = "measurePackageCellListscrollable";
	private static final String SPAN_END = "</span>";
	
	private Button addQDMRight = buildArrowButton(IconType.ANGLE_RIGHT, "addQDMRight");
	private Button addQDMLeft = buildArrowButton(IconType.ANGLE_LEFT, "addQDMLeft");
	private Button addAllQDMRight = buildArrowButton(IconType.ANGLE_DOUBLE_RIGHT, "addAllToRight");
	private Button addAllQDMLeft = buildArrowButton(IconType.ANGLE_DOUBLE_LEFT, "addAllToLeft");
	private Button packageMeasureButton = buildButton(IconType.ARCHIVE, "Create Measure Package") ;
	private Button addSupplementalDataElementsButton = buildButton(IconType.SAVE, "Save Supplemental Data Elements");	
	private Button createNewGroupingButton = buildButton(IconType.PLUS, "Create New Grouping") ;
	private Button addRiskAdjustmentRight = buildArrowButton(IconType.ANGLE_RIGHT, "addRiskAdjRight");
	private Button addRiskAdjustmentLeft = buildArrowButton(IconType.ANGLE_LEFT, "addRiskAdjLeft");
	private Button addAllRiskAdjustmentRight = buildArrowButton(IconType.ANGLE_DOUBLE_RIGHT, "addAllToRight");
	private Button addAllRiskAdjustmentLeft = buildArrowButton(IconType.ANGLE_DOUBLE_LEFT, "addAllToLeft");
	private Button addRiskAdjustmentVariablesToMeasureButton = buildButton(IconType.SAVE, "Save Risk Adjustment Variables") ;
	private Button packageMeasureAndExportButton = buildButton(IconType.DOWNLOAD, "Create Package and Export") ;
	private Button packageMeasureAndUploadToBonnieButton = buildButton(IconType.UPLOAD, "Package and Upload to Bonnie") ;
	
	private MessageAlert measurePackageSuccessMsg = new SuccessMessageAlert();
	private MessageAlert measureErrorMessages = new ErrorMessageAlert();
	private MessageAlert supplementalDataElementSuccessMessage = new SuccessMessageAlert();
	private MessageAlert supplementalDataElementErrorMessage = new ErrorMessageAlert();
	private MessageAlert inProgressMessageDisplay = new WarningMessageAlert();
	private MessageAlert riskAdjustmentSuccessMessage = new SuccessMessageAlert();
	private MessageAlert riskAdjustmentErrorMessage = new ErrorMessageAlert();

	private WarningMessageAlert measurePackageWarningMsg = new WarningMessageAlert();
	private WarningMessageAlert cqlLibraryNameWarningMsg = new WarningMessageAlert();
	
	private WarningConfirmationMessageAlert saveErrorMessageDisplay = new WarningConfirmationMessageAlert();
	private WarningConfirmationMessageAlert saveErrorMessageDisplayOnEdit = new WarningConfirmationMessageAlert();

	private ArrayList<QualityDataSetDTO> qdmPopulationList = new ArrayList<>();
	private ArrayList<QualityDataSetDTO> supplementalElementList = new ArrayList<>();

	private ArrayList<CQLDefinition> cqlQdmPopulationList = new ArrayList<>();
	private ArrayList<CQLDefinition> cqlSupplementalPopulationList = new ArrayList<>();

	private ArrayList<RiskAdjustmentDTO> riskAdjustmentVariablePopulationList = new ArrayList<>();
	private List<RiskAdjustmentDTO> subTreePopulationList = new ArrayList<>();

	private SingleSelectionModel<QualityDataSetDTO> qdmSelectionModel = new SingleSelectionModel<>();
	private SingleSelectionModel<QualityDataSetDTO> supplementalDataSelectionModel = new SingleSelectionModel<>();
	
	private SingleSelectionModel<CQLDefinition> cqlSuppelementalDataSelectionModel = new SingleSelectionModel<>();
	private SingleSelectionModel<CQLDefinition> cqlQDMSelectionModel = new SingleSelectionModel<>();

	private SingleSelectionModel<RiskAdjustmentDTO> riskAdjustmentClauseSelModel = new SingleSelectionModel<>();
	private SingleSelectionModel<RiskAdjustmentDTO> riskAdjustmentVariableSelectionModel = new SingleSelectionModel<>();
	
	private ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel("LeftSidePanel");
	private ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel("RightSidePanel");
	private ShowMorePagerPanel leftRiskAdjustmentVariablesPanel = new ShowMorePagerPanel("LeftRiskAdjPanel");
	private ShowMorePagerPanel rightRiskAdjustmentPanel = new ShowMorePagerPanel("RightRiskAdjPanel");
	
	private FlowPanel content = new FlowPanel();
	private Panel cellTablePanel = new Panel();

	private Widget addQDMElementButtonPanel = buildQDMElementAddButtonWidget();
	private Widget addRiskAdjustmentButtonPanel = buildRiskAdjustmentAddButtonWidget();

	private MeasurePackageClauseCellListWidget packageGroupingWidget = new MeasurePackageClauseCellListWidget();

	private Observer observer;

	private boolean isCQLMeasure;
	
	private HTML riskAdjustLabel = new HTML();
	private HTML qdmElementsLabel = new HTML();

	
	public MeasurePackagerView() {
		addQDMElementLeftRightClickHandlers();
		addRiskAdjLeftRightClickHandlers();
		
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
		Panel topRiskAdjContainer = buildRiskAdjLeftRightPanel();
		content.getElement().setAttribute("id", "MeasurePackagerContentFlowPanel");
		
		content.add(saveErrorMessageDisplay);
		content.add(saveErrorMessageDisplayOnEdit);
		content.add(cqlLibraryNameWarningMsg);
		content.add(cellTablePanel);
		cellTablePanel.setVisible(false); // default hidden, only show if there is more than 0 groupings
		content.add(new SpacerWidget());
		content.add(createNewGroupingButton);
		content.add(packageGroupingWidget.getWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(topQDMElementContainer);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(topRiskAdjContainer);		
		content.add(measurePackageSuccessMsg);
		content.add(measurePackageWarningMsg);
		content.add(measureErrorMessages);
		content.add(inProgressMessageDisplay);
		
		ButtonToolBar packageGroup = new ButtonToolBar();
		packageGroup.add(packageMeasureButton);
		packageGroup.add(packageMeasureAndExportButton);
		packageGroup.add(packageMeasureAndUploadToBonnieButton);
		
		content.add(packageGroup);
		content.setStyleName("contentPanel");
	}
		
	private void addRiskAdjLeftRightClickHandlers(){
		addRiskAdjustmentRight.addClickHandler(event -> addRiskAdjustmentRight());
		addRiskAdjustmentLeft.addClickHandler(event -> addRiskAdjustmentLeft());

		addAllRiskAdjustmentRight.addClickHandler(event -> addAllRiskAdjustmentRight());
		addAllRiskAdjustmentLeft.addClickHandler(event -> addAllRiskAdjustmentLeft());
	}

	private void addRiskAdjustmentRight() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!subTreePopulationList.isEmpty() && riskAdjustmentClauseSelModel.getSelectedObject() != null) {
				riskAdjustmentVariablePopulationList.add(riskAdjustmentClauseSelModel.getSelectedObject());
				subTreePopulationList.remove(riskAdjustmentClauseSelModel.getSelectedObject());
				subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				riskAdjustmentVariablePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				riskAdjustmentClauseSelModel.clear();
				rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
				leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
			}
		}
	}

	private void addRiskAdjustmentLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!riskAdjustmentVariablePopulationList.isEmpty() && riskAdjustmentVariableSelectionModel.getSelectedObject() != null) {
				subTreePopulationList.add(riskAdjustmentVariableSelectionModel.getSelectedObject());
				riskAdjustmentVariablePopulationList.remove(riskAdjustmentVariableSelectionModel.getSelectedObject());
				subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				riskAdjustmentVariablePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
				leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
				riskAdjustmentClauseSelModel.clear();
			}
		}
	}

	private void addAllRiskAdjustmentRight() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!subTreePopulationList.isEmpty()) {
				riskAdjustmentVariablePopulationList.addAll(subTreePopulationList);
				subTreePopulationList.clear();
				riskAdjustmentVariablePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				riskAdjustmentVariableSelectionModel.clear();
				riskAdjustmentClauseSelModel.clear();
				rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
				leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
				riskAdjustmentClauseSelModel.clear();
			}
		}
	}

	private void addAllRiskAdjustmentLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!riskAdjustmentVariablePopulationList.isEmpty()) {
				subTreePopulationList.addAll(riskAdjustmentVariablePopulationList);
				riskAdjustmentVariablePopulationList.clear();
				subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
				riskAdjustmentVariableSelectionModel.clear();
				riskAdjustmentClauseSelModel.clear();
				rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
				leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
				riskAdjustmentClauseSelModel.clear();
			}
		}
	}
	
	private void addQDMElementLeftRightClickHandlers() {
		addQDMRight.addClickHandler(event -> addQDMRight());
		addQDMLeft.addClickHandler(event -> addQDMLeft());
		
		addAllQDMRight.addClickHandler(event -> addAllQDMRight());
		addAllQDMLeft.addClickHandler(event -> addAllQDMLeft());
	}


	private void addQDMRight() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!qdmPopulationList.isEmpty() && qdmSelectionModel.getSelectedObject() != null) {
				supplementalElementList.add(qdmSelectionModel.getSelectedObject());
				qdmPopulationList.remove(qdmSelectionModel.getSelectedObject());
				supplementalElementList.sort(new QualityDataSetDTO.Comparator());
				qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
				qdmSelectionModel.clear();
			}
			
			if (!cqlQdmPopulationList.isEmpty() && cqlQDMSelectionModel.getSelectedObject() != null) {
				cqlSupplementalPopulationList.add(cqlQDMSelectionModel.getSelectedObject());
				cqlQdmPopulationList.remove(cqlQDMSelectionModel.getSelectedObject());
				cqlSupplementalPopulationList.sort(new CQLDefinition.Comparator());
				cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
				cqlQDMSelectionModel.clear();
			}
		}
	}

	private void setDisplayForCQL() {
		rightPagerPanel.setDisplay(getSupCellList());
		leftPagerPanel.setDisplay(getQdmCellList());
	}

	private void setDisplayForQDMCQL() {
		rightPagerPanel.setDisplay(getCQLSupCellList());
		leftPagerPanel.setDisplay(getCQLQdmCellList());
	}

	private void addQDMLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!supplementalElementList.isEmpty() && supplementalDataSelectionModel.getSelectedObject() != null) {
				qdmPopulationList.add(supplementalDataSelectionModel.getSelectedObject());
				supplementalElementList.remove(supplementalDataSelectionModel.getSelectedObject());
				supplementalElementList.sort(new QualityDataSetDTO.Comparator());
				qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
				supplementalDataSelectionModel.clear();
			}
			if (!cqlSupplementalPopulationList.isEmpty() && cqlSuppelementalDataSelectionModel.getSelectedObject() != null) {
				cqlQdmPopulationList.add(cqlSuppelementalDataSelectionModel.getSelectedObject());
				cqlSupplementalPopulationList.remove(cqlSuppelementalDataSelectionModel.getSelectedObject());
				cqlSupplementalPopulationList.sort(new CQLDefinition.Comparator());
				cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
				cqlSuppelementalDataSelectionModel.clear();
			}
		}
	}

	private void addAllQDMRight() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!qdmPopulationList.isEmpty()) {
				supplementalElementList.addAll(qdmPopulationList);
				qdmPopulationList.clear();
				supplementalElementList.sort(new QualityDataSetDTO.Comparator());
				supplementalDataSelectionModel.clear();
				qdmSelectionModel.clear();
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
			}
			if (!cqlQdmPopulationList.isEmpty()) {
				cqlSupplementalPopulationList.addAll(cqlQdmPopulationList);
				cqlQdmPopulationList.clear();
				cqlSupplementalPopulationList.sort(new CQLDefinition.Comparator());
				cqlSuppelementalDataSelectionModel.clear();
				cqlQDMSelectionModel.clear();
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
			}
		}
	}

	private void addAllQDMLeft() {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			if (!supplementalElementList.isEmpty()) {
				qdmPopulationList.addAll(supplementalElementList);
				supplementalElementList.clear();
				qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
				supplementalDataSelectionModel.clear();
				qdmSelectionModel.clear();
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
			}
			if (!cqlSupplementalPopulationList.isEmpty()) {
				cqlQdmPopulationList.addAll(cqlSupplementalPopulationList);
				cqlSupplementalPopulationList.clear();
				cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
				cqlSuppelementalDataSelectionModel.clear();
				cqlQDMSelectionModel.clear();
				if(isCQLMeasure()){
					setDisplayForQDMCQL();
				}
				else{
					setDisplayForCQL();
				}
			}
		}
	}
	
	private Panel buildRiskAdjLeftRightPanel(){	
		Panel riskAdjustmentPanel = new Panel(); 
		riskAdjustmentPanel.setType(PanelType.PRIMARY);
		PanelHeader riskAdjustmentPanelHeader = new PanelHeader();
		riskAdjustmentPanelHeader.setText("Risk Adjustment Variables");
		riskAdjustmentPanelHeader.setTitle("Risk Adjustment Variables");
		
		PanelBody riskAdjustmentPanelBody = new PanelBody();
		riskAdjustmentPanelBody.add(riskAdjustmentSuccessMessage);
		riskAdjustmentPanelBody.add(riskAdjustmentErrorMessage);
		
		riskAdjustmentPanel.add(riskAdjustmentPanelHeader);
		riskAdjustmentPanel.add(riskAdjustmentPanelBody);
		riskAdjustmentPanelBody.getElement().setAttribute("id", "RiskAdjustmentPanelBody");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		VerticalPanel riskAdjustmentDefinitionsVerticalPanel = new VerticalPanel();
		riskAdjustmentDefinitionsVerticalPanel.add(getRiskAdjustLabel());
		leftRiskAdjustmentVariablesPanel.addStyleName(MEASURE_PACKAGE_CELLLIST_SCROLLABLE);
		leftRiskAdjustmentVariablesPanel.setSize(THREE_HUNDRED_AND_TWENTY_PIXELS, TWO_HUNDRED_PIXELS);
		leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
		riskAdjustmentDefinitionsVerticalPanel.add(leftRiskAdjustmentVariablesPanel);
		horizontalPanel.add(riskAdjustmentDefinitionsVerticalPanel);
		riskAdjustmentDefinitionsVerticalPanel.add(new SpacerWidget());
			
		addRiskAdjustmentButtonPanel.addStyleName("column");
		horizontalPanel.add(addRiskAdjustmentButtonPanel);

		VerticalPanel riskAdjustmentVariablesVerticalPanel = new VerticalPanel();
		riskAdjustmentVariablesVerticalPanel.add(new HTML("<b style='margin-left:15px;'> Risk Adjustment Variables </b>"));
		riskAdjustmentVariablesVerticalPanel.getElement().setAttribute("id", "RiskAdjustmentLeftRightButtonVerticalPanel");
		rightRiskAdjustmentPanel.addStyleName(MEASURE_PACKAGE_CELLLIST_SCROLLABLE);
		rightRiskAdjustmentPanel.setSize(THREE_HUNDRED_AND_TWENTY_PIXELS, TWO_HUNDRED_PIXELS);
		rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
		riskAdjustmentVariablesVerticalPanel.add(rightRiskAdjustmentPanel);
		horizontalPanel.add(riskAdjustmentVariablesVerticalPanel);		
		
		riskAdjustmentPanelBody.add(horizontalPanel);
		riskAdjustmentPanelBody.add(addRiskAdjustmentVariablesToMeasureButton);
		
		return riskAdjustmentPanel; 		
	}

	private Panel buildQDMElementLeftRightPanel() {					
		Panel supplementalDataElementsPanel = new Panel(); 
		supplementalDataElementsPanel.setType(PanelType.PRIMARY);
		PanelHeader supplementalDataElementsPanelHeader = new PanelHeader(); 
		supplementalDataElementsPanelHeader.setText("Supplemental Data Elements");
		supplementalDataElementsPanelHeader.setTitle("Supplemental Data Elements");
		
		PanelBody supplementalDataElementsPanelBody = new PanelBody(); 
		supplementalDataElementsPanelBody.add(supplementalDataElementSuccessMessage);
		supplementalDataElementsPanelBody.add(supplementalDataElementErrorMessage);
		
		supplementalDataElementsPanel.add(supplementalDataElementsPanelHeader);
		supplementalDataElementsPanel.add(supplementalDataElementsPanelBody);
		supplementalDataElementsPanelBody.getElement().setAttribute("id", "SupplementalDataElementsPanel");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		VerticalPanel supplementalDataElementsDefinitionsPanel = new VerticalPanel();
		supplementalDataElementsDefinitionsPanel.add(getQdmElementsLabel());
		leftPagerPanel.addStyleName(MEASURE_PACKAGE_CELLLIST_SCROLLABLE);
		leftPagerPanel.setSize(THREE_HUNDRED_AND_TWENTY_PIXELS, TWO_HUNDRED_PIXELS);
		leftPagerPanel.getElement().setId("LeftPanelSuppQDMList");
		if(isCQLMeasure()){
			leftPagerPanel.setDisplay(getCQLQdmCellList());
		}
		else{
			leftPagerPanel.setDisplay(getQdmCellList());
		}
		
		supplementalDataElementsDefinitionsPanel.add(leftPagerPanel);
		horizontalPanel.add(supplementalDataElementsDefinitionsPanel);
		supplementalDataElementsDefinitionsPanel.add(new SpacerWidget());

		addQDMElementButtonPanel.addStyleName("column");	
		horizontalPanel.add(addQDMElementButtonPanel);

		VerticalPanel supplementalDataElementsRightPanel = new VerticalPanel();
		supplementalDataElementsRightPanel.add(new HTML("<b style='margin-left:15px;'> Supplemental Data Elements </b>"));
		supplementalDataElementsRightPanel.getElement().setAttribute("id", "RiskAdjustmentLeftRightButtonVerticalPanel");
		rightPagerPanel.addStyleName(MEASURE_PACKAGE_CELLLIST_SCROLLABLE);
		rightPagerPanel.getElement().setId("RightPanelSuppQDMList");
		rightPagerPanel.setSize(THREE_HUNDRED_AND_TWENTY_PIXELS, TWO_HUNDRED_PIXELS);
		if(isCQLMeasure()){
			rightPagerPanel.setDisplay(getCQLSupCellList());
		}
		else{
			rightPagerPanel.setDisplay(getSupCellList());
		}
		supplementalDataElementsRightPanel.add(rightPagerPanel);
		horizontalPanel.add(supplementalDataElementsRightPanel);
		
		supplementalDataElementsPanelBody.add(horizontalPanel);
		supplementalDataElementsPanelBody.add(addSupplementalDataElementsButton);
		
		return supplementalDataElementsPanel; 
	}

	private CellList<RiskAdjustmentDTO> getRiskAdjVarCellList(){
		CellList<RiskAdjustmentDTO> riskAdjustmentVariableCellList = new CellList<>(new RiskAdjustmentCell());
		riskAdjustmentVariableCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		riskAdjustmentVariableSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (riskAdjustmentVariableSelectionModel.getSelectedObject() == null) {
					return;
				}
				if (riskAdjustmentClauseSelModel.getSelectedObject()!=null){
					riskAdjustmentClauseSelModel.clear();
				}
			}
		});
		ListDataProvider<RiskAdjustmentDTO> riskAdjustmentVariableListProvider = new ListDataProvider<>(riskAdjustmentVariablePopulationList);
		riskAdjustmentVariableListProvider.addDataDisplay(riskAdjustmentVariableCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			riskAdjustmentVariableCellList.setSelectionModel(riskAdjustmentVariableSelectionModel
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		} else {
			riskAdjustmentVariableCellList.setSelectionModel(new NoSelectionModel<RiskAdjustmentDTO>()
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		}
		
		return riskAdjustmentVariableCellList;
	}

	private CellList<RiskAdjustmentDTO> getSubTreeClauseCellList(){
		CellList<RiskAdjustmentDTO> riskAdjustmentClauseCellList = new CellList<>(new RiskAdjustmentCell());
		riskAdjustmentClauseCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		riskAdjustmentClauseSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (riskAdjustmentClauseSelModel.getSelectedObject() == null) {
					return;
				}
				if (riskAdjustmentVariableSelectionModel.getSelectedObject()!=null){
					riskAdjustmentVariableSelectionModel.clear();
				}
			}
		});
		ListDataProvider<RiskAdjustmentDTO> riskAdjustmentClauseListProvider = new ListDataProvider<>(subTreePopulationList);
		riskAdjustmentClauseListProvider.addDataDisplay(riskAdjustmentClauseCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			riskAdjustmentClauseCellList.setSelectionModel(riskAdjustmentClauseSelModel
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		} else {
			riskAdjustmentClauseCellList.setSelectionModel(new NoSelectionModel<RiskAdjustmentDTO>()
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		}
		
		return riskAdjustmentClauseCellList;
	}
	
	private CellList<QualityDataSetDTO> getQdmCellList()
	{
		CellList<QualityDataSetDTO> qdmCellList = new CellList<>(new QualityDataSetCell());
		qdmCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		qdmSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (qdmSelectionModel.getSelectedObject() == null) {
					return;
				}
				if (supplementalDataSelectionModel.getSelectedObject()!=null){
					supplementalDataSelectionModel.clear();
				}
			}
		});
		ListDataProvider<QualityDataSetDTO> qdmListProv = new ListDataProvider<>(qdmPopulationList);
		qdmListProv.addDataDisplay(qdmCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			qdmCellList.setSelectionModel(qdmSelectionModel
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		} else {
			qdmCellList.setSelectionModel(new NoSelectionModel<QualityDataSetDTO>()
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		}
		
		return qdmCellList;
	}

	private CellList<QualityDataSetDTO> getSupCellList()
	{
		CellList<QualityDataSetDTO> supplementalDataElementList = new CellList<>(new QualityDataSetCell());
		supplementalDataElementList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<QualityDataSetDTO> supplementalDataElementProvider = new ListDataProvider<>(supplementalElementList);
		supplementalDataElementProvider.addDataDisplay(supplementalDataElementList);
		supplementalDataSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (supplementalDataSelectionModel.getSelectedObject() == null) {
					return;
				}
				if (qdmSelectionModel.getSelectedObject()!=null){
					qdmSelectionModel.clear();
				}
				
			}
		});
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			supplementalDataElementList.setSelectionModel(supplementalDataSelectionModel
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		} else {
			supplementalDataElementList.setSelectionModel(new NoSelectionModel<QualityDataSetDTO>()
					, DefaultSelectionEventManager.<QualityDataSetDTO> createDefaultManager());
		}

		return supplementalDataElementList;
	}

	private CellList<CQLDefinition> getCQLSupCellList()
	{
		CellList<CQLDefinition> cqlSupplementalDataCellList = new CellList<>(new CQLDefinitionCell());
		cqlSupplementalDataCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<CQLDefinition> cqlSupplementalListProvider = new ListDataProvider<>(cqlSupplementalPopulationList);
		cqlSupplementalListProvider.addDataDisplay(cqlSupplementalDataCellList);
		cqlSuppelementalDataSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (cqlSuppelementalDataSelectionModel.getSelectedObject() == null) {
					return;
				}
				if (cqlQDMSelectionModel.getSelectedObject()!=null){
					cqlQDMSelectionModel.clear();
				}
				
			}
		});
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlSupplementalDataCellList.setSelectionModel(cqlSuppelementalDataSelectionModel
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		} else {
			cqlSupplementalDataCellList.setSelectionModel(new NoSelectionModel<CQLDefinition>()
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		}

		return cqlSupplementalDataCellList;
	}

	private CellList<CQLDefinition> getCQLQdmCellList()
	{
		CellList<CQLDefinition> cqlQdmCellList = new CellList<>(new CQLDefinitionCell());
		cqlQdmCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cqlQDMSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (cqlQDMSelectionModel.getSelectedObject() == null) {
					return;
				}
				if (cqlSuppelementalDataSelectionModel.getSelectedObject()!=null){
					cqlSuppelementalDataSelectionModel.clear();
				}
			}
		});
		
		ListDataProvider<CQLDefinition> cqlQdmListProv = new ListDataProvider<>(cqlQdmPopulationList);
		cqlQdmListProv.addDataDisplay(cqlQdmCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlQdmCellList.setSelectionModel(cqlQDMSelectionModel
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		} else {
			cqlQdmCellList.setSelectionModel(new NoSelectionModel<CQLDefinition>()
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		}
		
		return cqlQdmCellList;
	}
	
	private Widget buildRiskAdjustmentAddButtonWidget(){
		VerticalPanel riskPanel = new VerticalPanel();
		riskPanel.getElement().setAttribute("id", "RiskAdjustmentButtonVerticalPanel");
		riskPanel.setStyleName("qdmElementAddButtonPanel");
		addRiskAdjustmentRight.setTitle("Add item to Risk Adjustment Variables");
		addRiskAdjustmentRight.getElement().setAttribute("alt", "Add item to Risk Adjustment Variables");
		addRiskAdjustmentLeft.setTitle("Remove item from Risk Adjustment Variables");
		addRiskAdjustmentLeft.getElement().setAttribute("alt", "Remove item from Risk Adjustment Variables");
		addAllRiskAdjustmentRight.setTitle("Add all items to Risk Adjustment Variables");
		addAllRiskAdjustmentRight.getElement().setAttribute("alt", "Add all items to Risk Adjustment Variables");
		addAllRiskAdjustmentLeft.setTitle("Remove all items from Risk Adjustment Variables");
		addAllRiskAdjustmentLeft.getElement().setAttribute("alt", "Remove all items from Risk Adjustment Variables");
		riskPanel.add(addRiskAdjustmentRight);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addRiskAdjustmentLeft);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addAllRiskAdjustmentRight);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addAllRiskAdjustmentLeft);
		return riskPanel;
	}

	private Widget buildQDMElementAddButtonWidget() {
		VerticalPanel panel = new VerticalPanel();
		panel.getElement().setAttribute("id", "QDMButtonVerticalPanel");
		panel.setStyleName("qdmElementAddButtonPanel");
		addQDMRight.setTitle("Add item to Supplemental Data Elements");
		addQDMRight.getElement().setAttribute("alt", "Add item to Supplemental Data Elements");
		addQDMLeft.setTitle("Remove item from Supplemental Data Elements");
		addQDMLeft.getElement().setAttribute("alt", "Remove item from Supplemental Data Elements");
		addAllQDMRight.setTitle("Add all items to Supplemental Data Elements");
		addAllQDMRight.getElement().setAttribute("alt", "Add all items to Supplemental Data Elements");
		addAllQDMLeft.setTitle("Remove all items from Supplemental Data Elements");
		addAllQDMLeft.getElement().setAttribute("alt", "Remove all items from Supplemental Data Elements");
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

	private  void addColumnToTable(CellTable<MeasurePackageDetail> table) {
		Column<MeasurePackageDetail, SafeHtml> measureGrouping = new Column<MeasurePackageDetail, SafeHtml>(new SafeHtmlCell()){
			@Override
			public SafeHtml getValue(MeasurePackageDetail object) {
				return CellTableUtility.getColumnToolTip(object.getPackageName());
			}
		};
		
		table.addColumn(measureGrouping, SafeHtmlUtils.fromSafeConstant("<span title='Grouping'>" + "Grouping" + SPAN_END));
		
		boolean isEditable = MatContext.get().getMeasureLockService().checkForEditPermission();
		if (isEditable) {
			table.addColumn(getEditOrViewColumn(isEditable), SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + SPAN_END));
			table.addColumn(getDeleteColumn(), SafeHtmlUtils.fromSafeConstant("<span title='Delete'>" + "Delete" + SPAN_END));
			table.setColumnWidth(0, 60.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
		} else {
			table.addColumn(getEditOrViewColumn(isEditable), SafeHtmlUtils.fromSafeConstant("<span title='View'>" + "View" + SPAN_END));
			table.setColumnWidth(0, 80.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
		}
	}
	
	private Column<MeasurePackageDetail, SafeHtml> getDeleteColumn() {
		Cell<SafeHtml> deleteButtonCell = new ClickableSafeHtmlCell();
		
		Column<MeasurePackageDetail, SafeHtml> deleteColumn = new Column<MeasurePackageDetail, SafeHtml>(deleteButtonCell) {
			@Override
			public SafeHtml getValue(MeasurePackageDetail object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to Delete " + object.getPackageName();
				String cssClass = "btn btn-link";
				String iconCss = "fa fa-trash fa-lg";
					sb.appendHtmlConstant("<button type=\"button\" title='" + title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"margin-left: 0px;margin-right: 10px;\"><i class=\" "+iconCss + "\"></i> <span style=\"font-size:0;\">Delete</span></button>");
			
				return sb.toSafeHtml();
			}
		};
		
		deleteColumn.setFieldUpdater(new FieldUpdater<MeasurePackageDetail, SafeHtml>() {
			@Override
			public void update(int index, MeasurePackageDetail object, SafeHtml value) {
				packageGroupingWidget.getAddAssociationsPanel().setVisible(false);
				observer.onDeleteClicked(object);
			}
		});
		
		return deleteColumn; 
	}
	
	private Column<MeasurePackageDetail, SafeHtml> getEditOrViewColumn(boolean isEditable) {
		Cell<SafeHtml> editButtonCell = new ClickableSafeHtmlCell();
		Column<MeasurePackageDetail, SafeHtml> editColumn = new Column<MeasurePackageDetail, SafeHtml>(editButtonCell) {
			@Override
			public SafeHtml getValue(MeasurePackageDetail object) {
				String editOrView = isEditable ? "Edit" : "View";
				
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String title = "Click to " + editOrView + " " + object.getPackageName();
				String cssClass = "btn btn-link";
				String iconCss = isEditable ? "fa fa-pencil fa-lg" : "fa fa-newspaper-o fa-lg";
				String iconColor = isEditable ? "darkgoldenrod" : "black";
				sb.appendHtmlConstant("<button type=\"button\" title='"
						+ title + "' tabindex=\"0\" class=\" " + cssClass + "\" style=\"color: " + iconColor + ";\" > <i class=\" " + iconCss + "\"></i><span style=\"font-size:0;\">" + editOrView + "</span></button>");
			
				return sb.toSafeHtml();
			}
		};
		
		editColumn.setFieldUpdater(new FieldUpdater<MeasurePackageDetail, SafeHtml>() {
			@Override
			public void update(int index, MeasurePackageDetail object, SafeHtml value) {
				packageGroupingWidget.getAddAssociationsPanel().setVisible(false);
				observer.onEditClicked(object);
				packageGroupingWidget.checkAssociations();
			}
		});
		
		return editColumn; 
	}
	
	@Override
	public void buildCellTable(List<MeasurePackageDetail> packages) {
		cellTablePanel.clear();
		cellTablePanel.setVisible(false);
		if(packages != null && !packages.isEmpty()) {
			cellTablePanel.setType(PanelType.PRIMARY);
			PanelHeader measureGroupingTablePanelHeader = new PanelHeader(); 
			measureGroupingTablePanelHeader.setTitle("Measure Grouping List");
			measureGroupingTablePanelHeader.setText("Measure Grouping List");
	
			PanelBody measureGroupingTablePanelBody = new PanelBody();
			CellTable<MeasurePackageDetail> table = new CellTable<>();
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			ListDataProvider<MeasurePackageDetail> sortProvider = new ListDataProvider<>();
			List<MeasurePackageDetail> measureGroupingList = new ArrayList<>();
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
			measureGroupingTablePanelBody.add(invisibleLabel);
			measureGroupingTablePanelBody.add(table);
			if (measureGroupingList.size() > 2) {
				MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureGrouping");
				spager.setPageStart(0);
				spager.setDisplay(table);
				spager.setPageSize(2);
				measureGroupingTablePanelBody.add(new SpacerWidget());
				measureGroupingTablePanelBody.add(spager);
			}
			
			cellTablePanel.add(measureGroupingTablePanelHeader);
			cellTablePanel.add(measureGroupingTablePanelBody);
			cellTablePanel.setVisible(true);
		}		
	}
	
	private Button buildButton(IconType icon, String text) {
		Button button = new Button(); 
		button.getElement().setAttribute("id", text.replaceAll(" ", "_") + "_button");
		button.setType(ButtonType.PRIMARY);
		button.setIcon(icon);
		button.setText(text);
		button.setTitle("Click to " + text);
		
		return button; 
	}

	private Button buildArrowButton(IconType icon, String id) {
		Button btn = new Button();
		btn.getElement().setAttribute("id", id);
		btn.setIcon(icon);
		btn.setType(ButtonType.SUCCESS);
		btn.setPaddingBottom(3.0);
		btn.setPaddingTop(3.0);
		btn.setWidth("50px");		
		return btn;
	}

	@Override
	public final void setQDMElementsInSuppElements(final List<QualityDataSetDTO> clauses) {
		supplementalElementList.clear();
		supplementalElementList.addAll(clauses);
		supplementalElementList.sort(new QualityDataSetDTO.Comparator());
		rightPagerPanel.setDisplay(getSupCellList());
		supplementalDataSelectionModel.clear();
	}

	@Override
	public final void setCQLElementsInSuppElements(final List<CQLDefinition> clauses) {
		cqlSupplementalPopulationList.clear();
		for(CQLDefinition cqlDef:clauses){
			if(cqlDef.getName() != null && !cqlDef.getName().isEmpty()){
					cqlSupplementalPopulationList.add(cqlDef);
			}
		}
		cqlSupplementalPopulationList.sort(new CQLDefinition.Comparator());
		rightPagerPanel.setDisplay(getCQLSupCellList());
		cqlQDMSelectionModel.clear();
	}

	@Override
	public final List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return supplementalElementList;
	}

	@Override
	public final void setViewIsEditable(final boolean isEnabled, final List<MeasurePackageDetail> packages) {
		createNewGroupingButton.setEnabled(isEnabled);
		packageMeasureButton.setEnabled(isEnabled);
		packageMeasureAndExportButton.setEnabled(isEnabled);
		packageMeasureAndUploadToBonnieButton.setEnabled(isEnabled);
		addSupplementalDataElementsButton.setEnabled(isEnabled);
		packageGroupingWidget.getSaveGrouping().setEnabled(isEnabled);
		addAllQDMLeft.setEnabled(isEnabled);
		addAllQDMRight.setEnabled(isEnabled);
		addQDMLeft.setEnabled(isEnabled);
		addQDMRight.setEnabled(isEnabled);
		packageGroupingWidget.getAddClauseRight().setEnabled(isEnabled);
		packageGroupingWidget.getAddClauseLeft().setEnabled(isEnabled);
		packageGroupingWidget.getAddAllClauseRight().setEnabled(isEnabled);
		packageGroupingWidget.getAddAllClauseLeft().setEnabled(isEnabled);
		addRiskAdjustmentVariablesToMeasureButton.setEnabled(isEnabled);
		addRiskAdjustmentRight.setEnabled(isEnabled);
		addRiskAdjustmentLeft.setEnabled(isEnabled);
		addAllRiskAdjustmentRight.setEnabled(isEnabled);
		addAllRiskAdjustmentLeft.setEnabled(isEnabled);
	}

	@Override
	public final void setQDMElements(final List<QualityDataSetDTO> clauses) {
		qdmPopulationList.clear();
		qdmPopulationList.addAll(clauses);
		qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
		leftPagerPanel.setDisplay(getQdmCellList());
		qdmSelectionModel.clear();
	}

	@Override
	public final List<QualityDataSetDTO> getQDMElements() {
		return qdmPopulationList;
	}
	
	@Override
	public final List<RiskAdjustmentDTO> getRiskAdjClauses(){
		return subTreePopulationList;
	}
	
	@Override
	public final List<RiskAdjustmentDTO> getRiskAdjVar(){
		return riskAdjustmentVariablePopulationList;
	}

	@Override
	public ErrorMessageAlert getErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}

	@Override
	public MessageAlert getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}

	@Override
	public WarningMessageAlert getMeasurePackageWarningMsg() {
		return measurePackageWarningMsg;
	}

	@Override
	public ErrorMessageAlert getPackageErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}

	@Override
	public MessageAlert getInProgressMessageDisplay() {
		return inProgressMessageDisplay;
	}
	
	public void setInProgressMessageDisplay(MessageAlert inProgressMessageDisplay) {
		this.inProgressMessageDisplay = inProgressMessageDisplay;
	}
	
	@Override
	public HasClickHandlers getPackageMeasureButton() {
		return packageMeasureButton;
	}

	@Override
	public MessageAlert getPackageSuccessMessageDisplay() {
		return packageGroupingWidget.getSuccessMessages();
	}

	@Override
	public final Widget asWidget() {
		return content;
	}

	@Override
	public final HasClickHandlers getAddQDMElementsToMeasureButton() {
		return addSupplementalDataElementsButton;
	}
	
	@Override
	public final HasClickHandlers getaddRiskAdjVariablesToMeasure() {
		return addRiskAdjustmentVariablesToMeasureButton;
	}

	@Override
	public final MessageAlert getSupplementalDataElementSuccessMessageDisplay() {
		return supplementalDataElementSuccessMessage;
	}
	
	@Override
	public final MessageAlert getRiskAdjustmentVariableSuccessMessageDisplay() {
		return riskAdjustmentSuccessMessage;
	}

	@Override
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		Collections.sort(clauses);
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
		Collections.sort(list);
		packageGroupingWidget.getGroupingPopulationList().clear();
		packageGroupingWidget.getGroupingPopulationList().addAll(list);
		packageGroupingWidget.getRightPagerPanel().setDisplay(packageGroupingWidget.getRightCellList());
		packageGroupingWidget.getRightRangeLabelPager().setDisplay(packageGroupingWidget.getRightCellList());
	}
	
	@Override
	public MeasurePackageClauseCellListWidget getPackageGroupingWidget() {
		return packageGroupingWidget;
	}
	
	@Override
	public void setAppliedQdmList(QDSAppliedListModel appliedListModel) {
		packageGroupingWidget.setAppliedQdmList(appliedListModel.getAppliedQDMs());
	}
	
	@Override
	public Button getCreateNewButton() {
		return createNewGroupingButton;
	}
	
	public Observer getObserver() {
		return observer;
	}
	
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	class RiskAdjustmentCell implements Cell<RiskAdjustmentDTO>{
		
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, RiskAdjustmentDTO value, SafeHtmlBuilder sb) {
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
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
			return false;
		}
		
		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value, NativeEvent event,
				ValueUpdater<RiskAdjustmentDTO> valueUpdater) {	
		}
		
		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
			return false;
		}

		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
		}
		
	}
	
	class QualityDataSetCell implements Cell<QualityDataSetDTO> {
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

	class CQLDefinitionCell implements Cell<CQLDefinition> {
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context, CQLDefinition value, SafeHtmlBuilder sb) {
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
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
			return false;
		}

		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value, NativeEvent event,
				ValueUpdater<CQLDefinition> valueUpdater) {
			
		}

		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
			return false;
		}

		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
		}
	}

	@Override
	public final MessageAlert getMeasureErrorMessageDisplay() {
		return measureErrorMessages;
	}

	@Override
	public HasClickHandlers getPackageMeasureAndExportButton() {
		return packageMeasureAndExportButton;
	}
	
	@Override
	public HasClickHandlers getPackageMeasureAndUploadToBonnieButton() {
		return packageMeasureAndUploadToBonnieButton;
	}
	
	@Override
	public void setSubTreeClauseList(List<RiskAdjustmentDTO> subTreeClauseList) {
		subTreePopulationList.clear();
		subTreePopulationList.addAll(subTreeClauseList);
		subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
		leftRiskAdjustmentVariablesPanel.setDisplay(getSubTreeClauseCellList());
		riskAdjustmentClauseSelModel.clear();
		
	}

	@Override
	public void setSubTreeInRiskAdjVarList(List<RiskAdjustmentDTO> riskAdjClauseList) {
		riskAdjustmentVariablePopulationList.clear();
		riskAdjustmentVariablePopulationList.addAll(riskAdjClauseList);
		riskAdjustmentVariablePopulationList.sort(new RiskAdjustmentDTO.Comparator());
		rightRiskAdjustmentPanel.setDisplay(getRiskAdjVarCellList());
		riskAdjustmentVariableSelectionModel.clear();
		
	}
	
	public ArrayList<CQLDefinition> getCqlSupPopulationList() {
		return cqlSupplementalPopulationList;
	}

	public void setCqlSupPopulationList(ArrayList<CQLDefinition> cqlSupPopulationList) {
		this.cqlSupplementalPopulationList = cqlSupPopulationList;
	}

	@Override
	public ArrayList<CQLDefinition> getCQLElementsInSuppElements() {
		return cqlSupplementalPopulationList;
	}

	@Override
	public void setCQLQDMElements(List<CQLDefinition> clauses) {
		cqlQdmPopulationList.clear();
		cqlQdmPopulationList.addAll(clauses);
		cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
		leftPagerPanel.setDisplay(getCQLQdmCellList());
		cqlQDMSelectionModel.clear();
		
	}

	@Override
	public List<CQLDefinition> getCQLQDMElements() {
		return cqlQdmPopulationList;
	}

	public boolean isCQLMeasure() {
		return isCQLMeasure;
	}

	@Override
	public void setCQLMeasure(boolean isCQLMeasure) {
		this.isCQLMeasure = isCQLMeasure;
	}

	public HTML getRiskAdjustLabel() {
		return riskAdjustLabel;
	}

	public HTML getQdmElementsLabel() {
		return qdmElementsLabel;
	}

	@Override
	public void setRiskAdjustLabel(boolean isCQLMeasure) {
		if(isCQLMeasure){
			riskAdjustLabel.setHTML("<b style='margin-left:15px;'> Definitions </b>");
		} else {
			riskAdjustLabel.setHTML("<b style='margin-left:15px;'> Clauses </b>");
		}	
	}

	@Override
	public void setQdmElementsLabel(boolean isCQLMeasure) {
		if(isCQLMeasure){
			qdmElementsLabel.setHTML("<b style='margin-left:15px;'> Definitions </b>");
		} else {
			qdmElementsLabel.setHTML("<b style='margin-left:15px;'> QDM Elements </b>");
		}
		
	}

	@Override
	public WarningConfirmationMessageAlert getSaveErrorMessageDisplayOnEdit() {
		return saveErrorMessageDisplayOnEdit;
	}
	
	@Override
	public void setSaveErrorMessageDisplayOnEdit(WarningConfirmationMessageAlert saveErrorMessageDisplayOnEdit) {
		this.saveErrorMessageDisplayOnEdit = saveErrorMessageDisplayOnEdit;
	}	
	
	
	@Override
	public WarningConfirmationMessageAlert getSaveErrorMessageDisplay() {
		return saveErrorMessageDisplay;
	}

	@Override
	public MessageAlert getSupplementalDataElementErrorMessageDisplay() {
		return supplementalDataElementErrorMessage;
	}

	public void setSupplementalDataElementErrorMessage(MessageAlert supplementalDataErrorMessages) {
		this.supplementalDataElementErrorMessage = supplementalDataErrorMessages;
	}

	@Override
	public MessageAlert getRiskAdjustmentVariableErrorMessageDisplay() {
		return riskAdjustmentErrorMessage;
	}

	public void setRiskAdjustmentErrorMessage(MessageAlert riskadjustmentErrorMessage) {
		this.riskAdjustmentErrorMessage = riskadjustmentErrorMessage;
	}
	
	@Override
	public Panel getCellTablePanel() {
		return cellTablePanel;
	}

	@Override
	public void setCellTablePanel(Panel cellTablePanel) {
		this.cellTablePanel = cellTablePanel;
	}
	
	@Override
	public void clearPackageGroupingsAndSDEAndRAVs() {
		setClauses(new ArrayList<MeasurePackageClauseDetail>());
		setClausesInPackage(new ArrayList<MeasurePackageClauseDetail>());

		setSubTreeClauseList(new ArrayList<RiskAdjustmentDTO>());
		setSubTreeInRiskAdjVarList(new ArrayList<RiskAdjustmentDTO>());

		setQDMElements(new ArrayList<QualityDataSetDTO>());
		setQDMElementsInSuppElements(new ArrayList<QualityDataSetDTO>());

		setCQLElementsInSuppElements(new ArrayList<CQLDefinition>());
		setCQLQDMElements(new ArrayList<CQLDefinition>());

	}

	public WarningMessageAlert getCqlLibraryNameWarningMsg() {
		return cqlLibraryNameWarningMsg;
	}

	public void setCqlLibraryNameWarningMsg(WarningMessageAlert cqlLibraryNameWarningMsg) {
		this.cqlLibraryNameWarningMsg = cqlLibraryNameWarningMsg;
	}

}