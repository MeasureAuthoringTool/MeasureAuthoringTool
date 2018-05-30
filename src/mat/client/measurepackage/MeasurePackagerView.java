package mat.client.measurepackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

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
import com.google.gwt.user.client.ui.HTML;
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

import mat.client.CustomPager;
import mat.client.clause.QDSAppliedListModel;
import mat.client.shared.ErrorMessageAlert;
import mat.client.shared.InProgressMessageDisplay;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MeasurePackageClauseCellListWidget;
import mat.client.shared.MessageAlert;
import mat.client.shared.PrimaryButton;
import mat.client.shared.ShowMorePagerPanel;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageAlert;
import mat.client.shared.WarningConfirmationMessageAlert;
import mat.client.shared.WarningMessageAlert;
import mat.client.util.CellTableUtility;
import mat.model.QualityDataSetDTO;
import mat.model.RiskAdjustmentDTO;
import mat.model.cql.CQLDefinition;


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
	
	/**
	 * The Interface Templates.
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
	
	/** The measure package success msg. */
	private MessageAlert measurePackageSuccessMsg = new SuccessMessageAlert();
	/** The measure package warning msg. */
	private WarningMessageAlert measurePackageWarningMsg = new WarningMessageAlert();
	/** The measure error messages. */
	private MessageAlert measureErrorMessages = new ErrorMessageAlert();
	/** The supp data success messages. */
	private MessageAlert suppDataSuccessMessages = new SuccessMessageAlert();
	
	/** The in progress message display. */
	private InProgressMessageDisplay inProgressMessageDisplay = new InProgressMessageDisplay();
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
	/** The risk adjustment header. */
	private Label riskAdjHeader = new Label("Risk Adjustment Variables");
	/**	MeasurePackageClauseListWidget. *  */
	private MeasurePackageClauseCellListWidget packageGroupingWidget = new MeasurePackageClauseCellListWidget();
	/** The Create New Grouping Button. */
	private PrimaryButton createNew = new PrimaryButton("Create New Grouping");
	
	/** The Vertical Panel for Cell Table. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The Observer. */
	private Observer observer;
	
	/** The save error message display. */
	private WarningConfirmationMessageAlert saveErrorMessageDisplay = new WarningConfirmationMessageAlert();
	
	private WarningConfirmationMessageAlert saveErrorMessageDisplayOnEdit = new WarningConfirmationMessageAlert();
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getSaveErrorMessageDisplay()
	 */
	@Override
	public WarningConfirmationMessageAlert getSaveErrorMessageDisplay() {
		return saveErrorMessageDisplay;
	}
	//added for adding cell list
	/** The qdm cell list. */
	private CellList<QualityDataSetDTO> qdmCellList;
	
	/** The cql qdm cell list. */
	private CellList<CQLDefinition> cqlQdmCellList;
	
	/** The qdm list prov. */
	private ListDataProvider<QualityDataSetDTO> qdmListProv;
	
	/** The cql qdm list prov. */
	private ListDataProvider<CQLDefinition> cqlQdmListProv;
	
	/** The qdm population list. */
	private ArrayList<QualityDataSetDTO> qdmPopulationList = new ArrayList<QualityDataSetDTO>();
	
	/** cql qdm population list. */
	private ArrayList<CQLDefinition> cqlQdmPopulationList = new ArrayList<CQLDefinition>();
	
	/** The sup data cell list. */
	private CellList<QualityDataSetDTO> supDataCellList;
	
	/** The cql sup data cell list. */
	private CellList<CQLDefinition> cqlSupDataCellList;
	
	/** The sup list prov. */
	private ListDataProvider<QualityDataSetDTO> supListProv;
	
	/** The cql sup list prov. */
	private ListDataProvider<CQLDefinition> cqlSupListProv;
	
	/** The sup population list. */
	private ArrayList<QualityDataSetDTO> supElementList = new ArrayList<QualityDataSetDTO>();
	
	/** The cql sup population list. */
	private ArrayList<CQLDefinition> cqlSupPopulationList = new ArrayList<CQLDefinition>();
	
	/** The qdm sel model. */
	private SingleSelectionModel<QualityDataSetDTO> qdmSelModel	=
			new SingleSelectionModel<QualityDataSetDTO>();
	
	/** The sup data sel model. */
	private SingleSelectionModel<QualityDataSetDTO> supDataSelModel =
			new SingleSelectionModel<QualityDataSetDTO>();
	
	/** The cql sup data sel model. */
	private SingleSelectionModel<CQLDefinition> cqlSupDataSelModel =
			new SingleSelectionModel<CQLDefinition>();
	
	/** The cql qdm sel model. */
	private SingleSelectionModel<CQLDefinition> cqlQdmSelModel	=
			new SingleSelectionModel<CQLDefinition>();
	
	/** The left pager panel. */
	private ShowMorePagerPanel leftPagerPanel = new ShowMorePagerPanel("LeftSidePanel");
	
	/** The right pager panel. */
	private ShowMorePagerPanel rightPagerPanel = new ShowMorePagerPanel("RightSidePanel");
	
	
	//RiskAdjustment UI Components
	/** The add risk adj right. */
	private Button addRiskAdjRight = buildAddButton("customAddRightButton", "addRiskAdjRight");
	
	/** The add risk adj left. */
	private Button addRiskAdjLeft = buildAddButton("customAddLeftButton", "addRiskAdjLeft");
	
	/** The add all risk adj right. */
	private Button addAllRiskAdjRight = buildDoubleAddButton("customAddALlRightButton", "addAllToRight");
	
	/** The add all risk adj left. */
	private Button addAllRiskAdjLeft = buildDoubleAddButton("customAddAllLeftButton", "addAllToLeft");
	
	/** The risk adj success messages. */
	private MessageAlert riskAdjSuccessMessages = new SuccessMessageAlert();
	
	/** The add risk adjustment button panel. */
	private Widget addRiskAdjButtonPanel = buildRiskAdjustmentAddButtonWidget();
	
	/** The risk adj flow panel. */
	private FlowPanel riskAdjFlowPanel = new FlowPanel();
	
	/** The risk panel. */
	private FlowPanel riskPanel = new FlowPanel();
	
	/** The add risk adj variables to measure. */
	private PrimaryButton addRiskAdjVariablesToMeasure = new PrimaryButton("Save Risk Adjustment Variables", "primaryButton");
	
	/** The left risk adj panel. */
	private ShowMorePagerPanel leftRiskAdjPanel = new ShowMorePagerPanel("LeftRiskAdjPanel");
	
	/** The right risk adj panel. */
	private ShowMorePagerPanel rightRiskAdjPanel = new ShowMorePagerPanel("RightRiskAdjPanel");
	
	/** The Risk adj cell list. */
	private CellList<RiskAdjustmentDTO> riskAdjClauseCellList;
	
	/** The risk adj sel model. */
	private SingleSelectionModel<RiskAdjustmentDTO> riskAdjClauseSelModel = new SingleSelectionModel<RiskAdjustmentDTO>();
	
	/** The risk adj var sel model. */
	private SingleSelectionModel<RiskAdjustmentDTO> riskAdjVarSelModel = new SingleSelectionModel<RiskAdjustmentDTO>();
	
	/** The risk adj population list. */
	private List<RiskAdjustmentDTO> subTreePopulationList = new ArrayList<RiskAdjustmentDTO>();
	
	/** The risk adj list prov. */
	private ListDataProvider<RiskAdjustmentDTO> riskAdjClauseListProv;
	
	/** The risk adj var cell list. */
	private CellList<RiskAdjustmentDTO> riskAdjVarCellList;
	
	/** The risk adj var list prov. */
	private ListDataProvider<RiskAdjustmentDTO> riskAdjVarListProv;
	
	/** The risk adj var population list. */
	private ArrayList<RiskAdjustmentDTO> riskAdjVarPopulationList = new ArrayList<RiskAdjustmentDTO>();
	
	/** The package measure and export. */
	private PrimaryButton packageMeasureAndExport = new PrimaryButton(
			"Create Package and Export", "primaryButton");
	
	/** The is cql measure. */
	private boolean isCQLMeasure;
	
	/** The risk adjust label. */
	private HTML riskAdjustLabel = new HTML();
	
	/** The QDMElements label. */
	private HTML qdmElementsLabel = new HTML();
	
	/**
	 * Constructor.
	 */
	public MeasurePackagerView() {
		addQDMElementLeftRightClickHandlers();
		addRiskAdjLeftRightClickHandlers();
		Panel topQDMElementContainer = buildQDMElementLeftRightPanel();
		Panel topRiskAdjContainer = buildRiskAdjLeftRightPanel();
		cellTablePanel.removeStyleName("valueSetSearchPanel");
		content.getElement().setAttribute("id", "MeasurePackagerContentFlowPanel");
		createNew.getElement().setAttribute("id", "CreateNewGroupingButton");
		content.add(saveErrorMessageDisplay);
		content.add(saveErrorMessageDisplayOnEdit);
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
		content.add(topRiskAdjContainer);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.add(measurePackageSuccessMsg);
		content.add(measurePackageWarningMsg);
		content.add(measureErrorMessages);
		content.add(inProgressMessageDisplay);
		packageMeasure.setTitle("Create Measure Package");
		packageMeasureAndExport.getElement().setId("Create Measure Package and Export");
		packageMeasureAndExport.setTitle("Create Measure Package and Export");
		packageMeasureAndExport.setStyleName("primaryButton floatRightButtonPanel");
		content.add(packageMeasure);
		content.add(packageMeasureAndExport);
		content.add(new SpacerWidget());
		content.add(new SpacerWidget());
		content.setStyleName("contentPanel");
	}
	
	/**ok.
	 * Adds the risk adj left right click handlers.
	 */
	private void addRiskAdjLeftRightClickHandlers(){
		addRiskAdjRight.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if ((subTreePopulationList.size() > 0)
						&& (riskAdjClauseSelModel.getSelectedObject() != null)) {
					riskAdjVarPopulationList.add(riskAdjClauseSelModel.getSelectedObject());
					subTreePopulationList.remove(riskAdjClauseSelModel.getSelectedObject());
					subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
					riskAdjVarPopulationList.sort(new RiskAdjustmentDTO.Comparator());
					//Collections.sort(subTreePopulationList , new RiskAdjustmentDTO.Comparator());
					//Collections.sort(riskAdjVarPopulationList, new RiskAdjustmentDTO.Comparator());
					riskAdjClauseSelModel.clear();
					rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
					leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
				}
			}
		});
		addRiskAdjLeft.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if ((riskAdjVarPopulationList.size() > 0)
						&& (riskAdjVarSelModel.getSelectedObject() != null)) {
					subTreePopulationList.add(riskAdjVarSelModel.getSelectedObject());
					riskAdjVarPopulationList.remove(riskAdjVarSelModel.getSelectedObject());
					subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
					riskAdjVarPopulationList.sort(new RiskAdjustmentDTO.Comparator());
					/*Collections.sort(riskAdjVarPopulationList , new RiskAdjustmentDTO.Comparator());
					Collections.sort(subTreePopulationList, new RiskAdjustmentDTO.Comparator());*/
					rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
					leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
					riskAdjClauseSelModel.clear();
					
				}
				
				
			}
		});
		addAllRiskAdjRight.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if (subTreePopulationList.size() != 0) {
					riskAdjVarPopulationList.addAll(subTreePopulationList);
					subTreePopulationList.removeAll(subTreePopulationList);
					riskAdjVarPopulationList.sort(new RiskAdjustmentDTO.Comparator());
					//Collections.sort(riskAdjVarPopulationList , new RiskAdjustmentDTO.Comparator());
					riskAdjVarSelModel.clear();
					riskAdjClauseSelModel.clear();
					rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
					leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
					riskAdjClauseSelModel.clear();
				}
			}
		});
		addAllRiskAdjLeft.addClickHandler(new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				if (riskAdjVarPopulationList.size() != 0) {
					subTreePopulationList.addAll(riskAdjVarPopulationList);
					riskAdjVarPopulationList.removeAll(riskAdjVarPopulationList);
					subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
					//Collections.sort(subTreePopulationList , new RiskAdjustmentDTO.Comparator());
					riskAdjVarSelModel.clear();
					riskAdjClauseSelModel.clear();
					rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
					leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
					riskAdjClauseSelModel.clear();
				}
			}
		});
		
		
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
					supElementList.add(qdmSelModel.getSelectedObject());
					qdmPopulationList.remove(qdmSelModel.getSelectedObject());
					supElementList.sort(new QualityDataSetDTO.Comparator());
					qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
					/*Collections.sort(supElementList , new QualityDataSetDTO.Comparator());
					Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());*/
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
					qdmSelModel.clear();
				}
				if ((cqlQdmPopulationList.size() > 0)
						&& (cqlQdmSelModel.getSelectedObject() != null)) {
					cqlSupPopulationList.add(cqlQdmSelModel.getSelectedObject());
					cqlQdmPopulationList.remove(cqlQdmSelModel.getSelectedObject());
					cqlSupPopulationList.sort(new CQLDefinition.Comparator());
					cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
					/*Collections.sort(cqlSupPopulationList , new CQLDefinition.Comparator());
					Collections.sort(cqlQdmPopulationList, new CQLDefinition.Comparator());*/
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
					cqlQdmSelModel.clear();
				}
			}
		});
		addQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ((supElementList.size() > 0)
						&& (supDataSelModel.getSelectedObject() != null)) {
					qdmPopulationList.add(supDataSelModel.getSelectedObject());
					supElementList.remove(supDataSelModel.getSelectedObject());
					supElementList.sort(new QualityDataSetDTO.Comparator());
					qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
					/*Collections.sort(supElementList , new QualityDataSetDTO.Comparator());
					Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());*/
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
					supDataSelModel.clear();
				}
				if ((cqlSupPopulationList.size() > 0)
						&& (cqlSupDataSelModel.getSelectedObject() != null)) {
					cqlQdmPopulationList.add(cqlSupDataSelModel.getSelectedObject());
					cqlSupPopulationList.remove(cqlSupDataSelModel.getSelectedObject());
					cqlSupPopulationList.sort(new CQLDefinition.Comparator());
					cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
					/*Collections.sort(cqlSupPopulationList , new CQLDefinition.Comparator());
					Collections.sort(cqlQdmPopulationList, new CQLDefinition.Comparator());*/
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
					cqlSupDataSelModel.clear();
				}
			}
		});
		addAllQDMRight.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (qdmPopulationList.size() != 0) {
					supElementList.addAll(qdmPopulationList);
					qdmPopulationList.removeAll(qdmPopulationList);
					supElementList.sort(new QualityDataSetDTO.Comparator());
					//Collections.sort(supElementList , new QualityDataSetDTO.Comparator());
					supDataSelModel.clear();
					qdmSelModel.clear();
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
				}
				if (cqlQdmPopulationList.size() != 0) {
					cqlSupPopulationList.addAll(cqlQdmPopulationList);
					cqlQdmPopulationList.removeAll(cqlQdmPopulationList);
					cqlSupPopulationList.sort(new CQLDefinition.Comparator());
					//Collections.sort(cqlSupPopulationList , new CQLDefinition.Comparator());
					cqlSupDataSelModel.clear();
					cqlQdmSelModel.clear();
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
				}
			}
		});
		addAllQDMLeft.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (supElementList.size() != 0) {
					qdmPopulationList.addAll(supElementList);
					supElementList.removeAll(supElementList);
					qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
					//Collections.sort(qdmPopulationList , new QualityDataSetDTO.Comparator());
					supDataSelModel.clear();
					qdmSelModel.clear();
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
				}
				if (cqlSupPopulationList.size() != 0) {
					cqlQdmPopulationList.addAll(cqlSupPopulationList);
					cqlSupPopulationList.removeAll(cqlSupPopulationList);
					cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
					//Collections.sort(cqlQdmPopulationList , new CQLDefinition.Comparator());
					cqlSupDataSelModel.clear();
					cqlQdmSelModel.clear();
					if(isCQLMeasure()){
						rightPagerPanel.setDisplay(getCQLSupCellList());
						leftPagerPanel.setDisplay(getCQLQdmCellList());
					}
					else{
						rightPagerPanel.setDisplay(getSupCellList());
						leftPagerPanel.setDisplay(getQdmCellList());
					}
				}
			}
		});
	}
	
	/**
	 * Builds the risk adjustment left right panel.
	 *
	 * @return the panel
	 */
	private Panel buildRiskAdjLeftRightPanel(){
		riskAdjFlowPanel.clear();
		riskPanel.clear();
		SimplePanel riskSimplepanel = new SimplePanel();
		riskSimplepanel.getElement().setAttribute("id", "RiskAdjustmentLeftRightButtonSimplePanel");
		FlowPanel RiskAdjustmentTopContainer = new FlowPanel();
		RiskAdjustmentTopContainer.getElement().setAttribute("id", "RiskAdjustmentTopContainerFlowPanel");
		VerticalPanel riskVPanel = new VerticalPanel();
		VerticalPanel riskSPanel = new VerticalPanel();
		riskVPanel.getElement().setAttribute("id", "RiskAdjustmentLeftRightButtonVerticalPanel");
		riskAdjHeader.setStyleName("valueSetHeader");
		riskAdjHeader.getElement().setAttribute("id", "RiskAdjustmentHeadingLabel");
		RiskAdjustmentTopContainer.add(riskAdjHeader);
		RiskAdjustmentTopContainer.add(riskAdjSuccessMessages);
		riskAdjFlowPanel.addStyleName("newColumn");
		riskAdjFlowPanel.getElement().setAttribute("id", "RiskAdjustmentFlowPanel");
		riskPanel.addStyleName("newColumn");
		riskPanel.getElement().setAttribute("id", "RiskFlowPanel");
		addRiskAdjButtonPanel.addStyleName("column");
		riskSPanel.add(getRiskAdjustLabel());
		leftRiskAdjPanel.addStyleName("measurePackageCellListscrollable");
		leftRiskAdjPanel.setSize("320px", "200px");
		leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
		riskSPanel.add(leftRiskAdjPanel);
		
		rightRiskAdjPanel.addStyleName("measurePackageCellListscrollable");
		rightRiskAdjPanel.setSize("320px", "200px");
		rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
		riskVPanel.add(new HTML("<b style='margin-left:15px;'> Risk Adjustment Variables </b>"));
		riskVPanel.add(rightRiskAdjPanel);
		riskPanel.add(riskVPanel);
		riskSPanel.add(new SpacerWidget());
		riskAdjFlowPanel.add(riskSPanel);
		RiskAdjustmentTopContainer.add(riskAdjFlowPanel);
		RiskAdjustmentTopContainer.add(addRiskAdjButtonPanel);
		RiskAdjustmentTopContainer.add(riskPanel);
		SpacerWidget spacer = new SpacerWidget();
		spacer.setStylePrimaryName("clearBoth");
		RiskAdjustmentTopContainer.add(spacer);
		RiskAdjustmentTopContainer.setStylePrimaryName("valueSetSearchPanel");
		riskSimplepanel.setStylePrimaryName("measurePackageLeftRightPanel");
		RiskAdjustmentTopContainer.add(addRiskAdjVariablesToMeasure);
		riskSimplepanel.add(RiskAdjustmentTopContainer);
		return riskSimplepanel;
		
		
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
		
		sPanel.add(getQdmElementsLabel());
		
		leftPagerPanel.addStyleName("measurePackageCellListscrollable");
		leftPagerPanel.getElement().setId("LeftPanelSuppQDMList");
		leftPagerPanel.setSize("320px", "200px");
		if(isCQLMeasure()){
			leftPagerPanel.setDisplay(getCQLQdmCellList());
		}
		else{
			leftPagerPanel.setDisplay(getQdmCellList());
		}
		sPanel.add(leftPagerPanel);
		
		rightPagerPanel.addStyleName("measurePackageCellListscrollable");
		rightPagerPanel.getElement().setId("RightPanelSuppQDMList");
		rightPagerPanel.setSize("320px", "200px");
		if(isCQLMeasure()){
			rightPagerPanel.setDisplay(getCQLSupCellList());
		}
		else{
			rightPagerPanel.setDisplay(getSupCellList());
		}
		vPanel.add(new HTML("<b style='margin-left:15px;'> Supplemental Data Elements </b>"));
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
	
	/**
	 * Gets the risk adj var cell list.
	 *
	 * @return the risk adj var cell list
	 */
	private CellList<RiskAdjustmentDTO> getRiskAdjVarCellList(){
		riskAdjVarCellList = new CellList<RiskAdjustmentDTO>(new RiskAdjustmentCell());
		riskAdjVarCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		riskAdjVarSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (riskAdjVarSelModel.getSelectedObject() == null) {
					return;
				}
				if (riskAdjClauseSelModel.getSelectedObject()!=null){
					riskAdjClauseSelModel.clear();
				}
			}
		});
		riskAdjVarListProv = new ListDataProvider<RiskAdjustmentDTO>(riskAdjVarPopulationList);
		riskAdjVarListProv.addDataDisplay(riskAdjVarCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			riskAdjVarCellList.setSelectionModel(riskAdjVarSelModel
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		} else {
			riskAdjVarCellList.setSelectionModel(new NoSelectionModel<RiskAdjustmentDTO>()
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		}
		
		return riskAdjVarCellList;
	}
	/**
	 * Gets the risk adj cell list.
	 *
	 * @return the risk adj cell list
	 */
	private CellList<RiskAdjustmentDTO> getSubTreeClauseCellList(){
		riskAdjClauseCellList = new CellList<RiskAdjustmentDTO>(new RiskAdjustmentCell());
		riskAdjClauseCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		riskAdjClauseSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (riskAdjClauseSelModel.getSelectedObject() == null) {
					return;
				}
				if (riskAdjVarSelModel.getSelectedObject()!=null){
					riskAdjVarSelModel.clear();
				}
			}
		});
		riskAdjClauseListProv = new ListDataProvider<RiskAdjustmentDTO>(subTreePopulationList);
		riskAdjClauseListProv.addDataDisplay(riskAdjClauseCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			riskAdjClauseCellList.setSelectionModel(riskAdjClauseSelModel
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		} else {
			riskAdjClauseCellList.setSelectionModel(new NoSelectionModel<RiskAdjustmentDTO>()
					, DefaultSelectionEventManager.<RiskAdjustmentDTO> createDefaultManager());
		}
		
		//End
		return riskAdjClauseCellList;
	}
	/**
	 * Gets the qdm cell list.
	 *
	 * @return the qdm cell list
	 */
	private CellList<QualityDataSetDTO> getQdmCellList()
	{
		//left cell list initialization
		qdmCellList = new CellList<QualityDataSetDTO>(new QualityDataSetCell());
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
	
	/**
	 * Gets the sup cell list.
	 *
	 * @return the sup cell list
	 */
	private CellList<QualityDataSetDTO> getSupCellList()
	{
		//left cell list initialization
		supDataCellList = new CellList<QualityDataSetDTO>(new QualityDataSetCell());
		supDataCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		supListProv = new ListDataProvider<QualityDataSetDTO>(supElementList);
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
	 * Gets the cql sup cell list.
	 *
	 * @return the cql sup cell list
	 */
	private CellList<CQLDefinition> getCQLSupCellList()
	{
		//left cell list initialization
		cqlSupDataCellList = new CellList<CQLDefinition>(new CQLDefinitionCell());
		cqlSupDataCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cqlSupListProv = new ListDataProvider<CQLDefinition>(cqlSupPopulationList);
		cqlSupListProv.addDataDisplay(cqlSupDataCellList);
		cqlSupDataSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (cqlSupDataSelModel.getSelectedObject() == null) {
					return;
				}
				if (cqlQdmSelModel.getSelectedObject()!=null){
					cqlQdmSelModel.clear();
				}
				
			}
		});
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlSupDataCellList.setSelectionModel(cqlSupDataSelModel
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		} else {
			cqlSupDataCellList.setSelectionModel(new NoSelectionModel<CQLDefinition>()
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		}
		//End
		return cqlSupDataCellList;
	}
	
	/**
	 * Gets the qdm cell list.
	 *
	 * @return the qdm cell list
	 */
	private CellList<CQLDefinition> getCQLQdmCellList()
	{
		//left cell list initialization
		cqlQdmCellList = new CellList<CQLDefinition>(new CQLDefinitionCell());
		cqlQdmCellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		cqlQdmSelModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				if (cqlQdmSelModel.getSelectedObject() == null) {
					return;
				}
				if (cqlSupDataSelModel.getSelectedObject()!=null){
					cqlSupDataSelModel.clear();
				}
			}
		});
		
		cqlQdmListProv = new ListDataProvider<CQLDefinition>(cqlQdmPopulationList);
		cqlQdmListProv.addDataDisplay(cqlQdmCellList);
		
		if (MatContext.get().getMeasureLockService().checkForEditPermission()) {
			cqlQdmCellList.setSelectionModel(cqlQdmSelModel
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		} else {
			cqlQdmCellList.setSelectionModel(new NoSelectionModel<CQLDefinition>()
					, DefaultSelectionEventManager.<CQLDefinition> createDefaultManager());
		}
		
		//End
		return cqlQdmCellList;
	}
	
	/**
	 * Builds the risk adjustment add button widget.
	 *
	 * @return the widget
	 */
	private Widget buildRiskAdjustmentAddButtonWidget(){
		VerticalPanel riskPanel = new VerticalPanel();
		riskPanel.getElement().setAttribute("id", "RiskAdjustmentButtonVerticalPanel");
		riskPanel.setStyleName("qdmElementAddButtonPanel");
		addRiskAdjRight.setTitle("Add item to Risk Adjustment Variables");
		addRiskAdjRight.getElement().setAttribute("alt", "Add item to Risk Adjustment Variables");
		addRiskAdjLeft.setTitle("Remove item from Risk Adjustment Variables");
		addRiskAdjLeft.getElement().setAttribute("alt", "Remove item from Risk Adjustment Variables");
		addAllRiskAdjRight.setTitle("Add all items to Risk Adjustment Variables");
		addAllRiskAdjRight.getElement().setAttribute("alt", "Add all items to Risk Adjustment Variables");
		addAllRiskAdjLeft.setTitle("Remove all items from Risk Adjustment Variables");
		addAllRiskAdjLeft.getElement().setAttribute("alt", "Remove all items from Risk Adjustment Variables");
		riskPanel.add(addRiskAdjRight);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addRiskAdjLeft);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addAllRiskAdjRight);
		riskPanel.add(new SpacerWidget());
		riskPanel.add(new SpacerWidget());
		riskPanel.add(addAllRiskAdjLeft);
		return riskPanel;
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
				MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true,"measureGrouping");
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
		supElementList.clear();
		supElementList.addAll(clauses);
		supElementList.sort(new QualityDataSetDTO.Comparator());
		//Collections.sort(supElementList, new QualityDataSetDTO.Comparator());
		rightPagerPanel.setDisplay(getSupCellList());
		supDataSelModel.clear();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setCQLElementsInSuppElements(java.util.List)
	 */
	@Override
	public final void setCQLElementsInSuppElements(final List<CQLDefinition> clauses) {
		cqlSupPopulationList.clear();
		for(CQLDefinition cqlDef:clauses){
			if(cqlDef.getName() != null && !cqlDef.getName().isEmpty()){
					cqlSupPopulationList.add(cqlDef);
			}
		}
		cqlSupPopulationList.sort(new CQLDefinition.Comparator());
		//Collections.sort(cqlSupPopulationList, new CQLDefinition.Comparator());
		rightPagerPanel.setDisplay(getCQLSupCellList());
		cqlQdmSelModel.clear();
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getQDMElementsInSuppElements()
	 */
	@Override
	public final List<QualityDataSetDTO> getQDMElementsInSuppElements() {
		return supElementList;
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setViewIsEditable(boolean, java.util.List)
	 */
	@Override
	public final void setViewIsEditable(final boolean isEnabled, final List<MeasurePackageDetail> packages) {
		createNew.setEnabled(isEnabled);
		packageMeasure.setEnabled(isEnabled);
		packageMeasureAndExport.setEnabled(isEnabled);
		addQDMElementsToMeasure.setEnabled(isEnabled);
		packageGroupingWidget.getSaveGrouping().setEnabled(isEnabled);
		addAllQDMLeft.setEnabled(isEnabled);
		addAllQDMRight.setEnabled(isEnabled);
		addQDMLeft.setEnabled(isEnabled);
		addQDMRight.setEnabled(isEnabled);
		packageGroupingWidget.getAddClauseRight().setEnabled(isEnabled);
		packageGroupingWidget.getAddClauseLeft().setEnabled(isEnabled);
		packageGroupingWidget.getAddAllClauseRight().setEnabled(isEnabled);
		packageGroupingWidget.getAddAllClauseLeft().setEnabled(isEnabled);
		addRiskAdjVariablesToMeasure.setEnabled(isEnabled);
		addRiskAdjRight.setEnabled(isEnabled);
		addRiskAdjLeft.setEnabled(isEnabled);
		addAllRiskAdjRight.setEnabled(isEnabled);
		addAllRiskAdjLeft.setEnabled(isEnabled);
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setQDMElements(java.util.List)
	 */
	@Override
	public final void setQDMElements(final List<QualityDataSetDTO> clauses) {
		qdmPopulationList.clear();
		qdmPopulationList.addAll(clauses);
		qdmPopulationList.sort(new QualityDataSetDTO.Comparator());
		//Collections.sort(qdmPopulationList, new QualityDataSetDTO.Comparator());
		leftPagerPanel.setDisplay(getQdmCellList());
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
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getRiskAdjClauses()
	 */
	@Override
	public final List<RiskAdjustmentDTO> getRiskAdjClauses(){
		return subTreePopulationList;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getRiskAdjVar()
	 */
	@Override
	public final List<RiskAdjustmentDTO> getRiskAdjVar(){
		return riskAdjVarPopulationList;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageAlert getErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasurePackageSuccessMsg()
	 */
	@Override
	public MessageAlert getMeasurePackageSuccessMsg() {
		return measurePackageSuccessMsg;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasurePackageWarningMsg()
	 */
	@Override
	public WarningMessageAlert getMeasurePackageWarningMsg() {
		return measurePackageWarningMsg;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getPackageErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageAlert getPackageErrorMessageDisplay() {
		return packageGroupingWidget.getErrorMessages();
	}
	/**
	 * Gets the in progress message display.
	 *
	 * @return the inProgressMessageDisplay
	 */
	@Override
	public InProgressMessageDisplay getInProgressMessageDisplay() {
		return inProgressMessageDisplay;
	}
	
	/**
	 * Sets the in progress message display.
	 *
	 * @param inProgressMessageDisplay the inProgressMessageDisplay to set
	 */
	public void setInProgressMessageDisplay(InProgressMessageDisplay inProgressMessageDisplay) {
		this.inProgressMessageDisplay = inProgressMessageDisplay;
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
	public MessageAlert getPackageSuccessMessageDisplay() {
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
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getaddRiskAdjVariablesToMeasure()
	 */
	@Override
	public final HasClickHandlers getaddRiskAdjVariablesToMeasure() {
		return addRiskAdjVariablesToMeasure;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getSuppDataSuccessMessageDisplay()
	 */
	@Override
	public final MessageAlert getSuppDataSuccessMessageDisplay() {
		return suppDataSuccessMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getRiskAdjSuccessMessageDisplay()
	 */
	@Override
	public final MessageAlert getRiskAdjSuccessMessageDisplay() {
		return riskAdjSuccessMessages;
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setClauses(java.util.List)
	 */
	@Override
	public void setClauses(List<MeasurePackageClauseDetail> clauses) {
		Collections.sort(clauses);
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
		Collections.sort(list);
		packageGroupingWidget.getGroupingPopulationList().clear();
		packageGroupingWidget.getGroupingPopulationList().addAll(list);
		packageGroupingWidget.getRightPagerPanel().setDisplay(packageGroupingWidget.getRightCellList());
		packageGroupingWidget.getRightRangeLabelPager().setDisplay(packageGroupingWidget.getRightCellList());
	}
	
	/**
	 * Gets the package grouping widget.
	 *
	 * @return the packageGroupingWidget
	 */
	@Override
	public MeasurePackageClauseCellListWidget getPackageGroupingWidget() {
		return packageGroupingWidget;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setAppliedQdmList(mat.client.clause.QDSAppliedListModel)
	 */
	@Override
	public void setAppliedQdmList(QDSAppliedListModel appliedListModel) {
		packageGroupingWidget.setAppliedQdmList(appliedListModel.getAppliedQDMs());
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getCreateNewButton()
	 */
	@Override
	public HasClickHandlers getCreateNewButton() {
		return createNew;
	}
	
	/**
	 * Gets the observer.
	 *
	 * @return Observer.
	 */
	public Observer getObserver() {
		return observer;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setObserver(mat.client.measurepackage.MeasurePackagerView.Observer)
	 */
	@Override
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/**
	 * The Class RiskAdjustmentCell.
	 */
	class RiskAdjustmentCell implements Cell<RiskAdjustmentDTO>{
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
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
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value, NativeEvent event,
				ValueUpdater<RiskAdjustmentDTO> valueUpdater) {
			
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#resetFocus(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#setValue(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, RiskAdjustmentDTO value) {
		}
		
	}
	/**
	 * QualityDataSet Cell Class.
	 *
	 */
	class QualityDataSetCell implements Cell<QualityDataSetDTO> {
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
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
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value, NativeEvent event,
				ValueUpdater<QualityDataSetDTO> valueUpdater) {
			
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#resetFocus(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#setValue(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, QualityDataSetDTO value) {
		}
		
	}
	
	/**
	 * CQLDefinitionCell Cell Class.
	 *
	 */
	class CQLDefinitionCell implements Cell<CQLDefinition> {
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#render(com.google.gwt.cell.client.Cell.Context, java.lang.Object, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
		 */
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
		public boolean isEditing(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
		 */
		@Override
		public void onBrowserEvent(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value, NativeEvent event,
				ValueUpdater<CQLDefinition> valueUpdater) {
			
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#resetFocus(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public boolean resetFocus(
				com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
			return false;
		}
		
		/* (non-Javadoc)
		 * @see com.google.gwt.cell.client.Cell#setValue(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object)
		 */
		@Override
		public void setValue(com.google.gwt.cell.client.Cell.Context context,
				Element parent, CQLDefinition value) {
		}
	}
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getMeasureErrorMessageDisplay()
	 */
	@Override
	public final MessageAlert getMeasureErrorMessageDisplay() {
		return measureErrorMessages;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getPackageMeasureAndExportButton()
	 */
	@Override
	public HasClickHandlers getPackageMeasureAndExportButton() {
		return packageMeasureAndExport;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setRiskAdjClauseList(java.util.List)
	 */
	@Override
	public void setSubTreeClauseList(List<RiskAdjustmentDTO> subTreeClauseList) {
		subTreePopulationList.clear();
		subTreePopulationList.addAll(subTreeClauseList);
		subTreePopulationList.sort(new RiskAdjustmentDTO.Comparator());
		//Collections.sort(subTreePopulationList, new RiskAdjustmentDTO.Comparator());
		leftRiskAdjPanel.setDisplay(getSubTreeClauseCellList());
		riskAdjClauseSelModel.clear();
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setSubTreeInRiskAdjVarList(java.util.List)
	 */
	@Override
	public void setSubTreeInRiskAdjVarList(List<RiskAdjustmentDTO> riskAdjClauseList) {
		riskAdjVarPopulationList.clear();
		riskAdjVarPopulationList.addAll(riskAdjClauseList);
		riskAdjVarPopulationList.sort(new RiskAdjustmentDTO.Comparator());
		//Collections.sort(riskAdjVarPopulationList, new RiskAdjustmentDTO.Comparator());
		rightRiskAdjPanel.setDisplay(getRiskAdjVarCellList());
		riskAdjVarSelModel.clear();
		
	}

	/**
	 * Gets the cql sup population list.
	 *
	 * @return the cql sup population list
	 */
	public ArrayList<CQLDefinition> getCqlSupPopulationList() {
		return cqlSupPopulationList;
	}

	/**
	 * Sets the cql sup population list.
	 *
	 * @param cqlSupPopulationList the new cql sup population list
	 */
	public void setCqlSupPopulationList(ArrayList<CQLDefinition> cqlSupPopulationList) {
		this.cqlSupPopulationList = cqlSupPopulationList;
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getCQLElementsInSuppElements()
	 */
	@Override
	public ArrayList<CQLDefinition> getCQLElementsInSuppElements() {
		return cqlSupPopulationList;
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setCQLQDMElements(java.util.List)
	 */
	@Override
	public void setCQLQDMElements(List<CQLDefinition> clauses) {
		cqlQdmPopulationList.clear();
		cqlQdmPopulationList.addAll(clauses);
		cqlQdmPopulationList.sort(new CQLDefinition.Comparator());
		//Collections.sort(cqlQdmPopulationList, new CQLDefinition.Comparator());
		leftPagerPanel.setDisplay(getCQLQdmCellList());
		cqlQdmSelModel.clear();
		
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#getCQLQDMElements()
	 */
	@Override
	public List<CQLDefinition> getCQLQDMElements() {
		return cqlQdmPopulationList;
	}

	/**
	 * Checks if is CQL measure.
	 *
	 * @return true, if is CQL measure
	 */
	public boolean isCQLMeasure() {
		return isCQLMeasure;
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setCQLMeasure(boolean)
	 */
	@Override
	public void setCQLMeasure(boolean isCQLMeasure) {
		this.isCQLMeasure = isCQLMeasure;
	}

	/**
	 * Gets the risk adjust label.
	 *
	 * @return the risk adjust label
	 */
	public HTML getRiskAdjustLabel() {
		return riskAdjustLabel;
	}

	
	/**
	 * @return the qdmElementsLabel
	 */
	public HTML getQdmElementsLabel() {
		return qdmElementsLabel;
	}

	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setRiskAdjustLabel(boolean)
	 */
	@Override
	public void setRiskAdjustLabel(boolean isCQLMeasure) {
		if(isCQLMeasure){
			riskAdjustLabel.setHTML("<b style='margin-left:15px;'> Definitions </b>");
		} else {
			riskAdjustLabel.setHTML("<b style='margin-left:15px;'> Clauses </b>");
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.client.measurepackage.MeasurePackagePresenter.PackageView#setRiskAdjustLabel(boolean)
	 */
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
	
}