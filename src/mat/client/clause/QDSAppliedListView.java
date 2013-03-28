package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.RangeLabelPager;
import mat.client.shared.ShowMorePagerPanel;
import mat.client.shared.SpacerWidget;
import mat.client.shared.SuccessMessageDisplay;
import mat.client.shared.SuccessMessageDisplayInterface;
import mat.model.QualityDataSetDTO;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;



public class QDSAppliedListView  implements QDSAppliedListPresenter.SearchDisplay {
	private SimplePanel containerPanel = new SimplePanel();
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	private SuccessMessageDisplay successMessagePanel;
	private Button removeButton = new Button("Remove");
	private Button removeButtonJSON = new Button("Remove");
	private CellList<QualityDataSetDTO> cellList;
	private CellList<QualityDataSetDTO> cellListJSON;

	ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
	RangeLabelPager rangeLabelPager = new RangeLabelPager();

	ShowMorePagerPanel pagerPanelJSON = new ShowMorePagerPanel();
	RangeLabelPager rangeLabelPagerJSON = new RangeLabelPager();
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return successMessagePanel;
	}

	public ErrorMessageDisplay getErrorMessagePanel(){
		return errorMessagePanel;
	}

	public QDSAppliedListView() {
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		HorizontalPanel mainPanel = new HorizontalPanel();
		VerticalPanel vp = new VerticalPanel();
		vp.setStylePrimaryName("qdmCellList");
		HorizontalPanel mainPanelNormal = new HorizontalPanel();
		mainPanelNormal.add(pagerPanel);
		vp.add(new SpacerWidget());
		vp.add(new HTML("<h4> Applied QDM Elements </h4>"));
		vp.add(new SpacerWidget());
		vp.add(mainPanelNormal);
		vp.add(new SpacerWidget());
		vp.add(rangeLabelPager);
		vp.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		vp.add(removeButton);
		vp.add(new SpacerWidget());

		VerticalPanel vpJson = new VerticalPanel();
		vpJson.setStylePrimaryName("qdmCellList");
		HorizontalPanel mainPanelJson = new HorizontalPanel();
		mainPanelJson.add(pagerPanelJSON);
		vpJson.add(new SpacerWidget());
		vpJson.add(new HTML("<h4> Applied QDM Elements from SimpleXML </h4>"));
		vpJson.add(new SpacerWidget());
		vpJson.add(mainPanelJson);
		vpJson.add(new SpacerWidget());
		vpJson.add(rangeLabelPagerJSON);
		vpJson.add(new SpacerWidget());
		removeButtonJSON.setEnabled(checkForEnable());
		vpJson.add(removeButtonJSON);
		vpJson.add(new SpacerWidget());

		mainPanel.add(vp);
		mainPanel.add(vpJson);
		containerPanel.add(mainPanel);

		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQdsAppliedListView(this);
	}

	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}

	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
	}

	@Override
	public  void buildCellListWidget(QDSAppliedListModel appliedListModel) {
		cellList = initializeCellListContent(cellList,appliedListModel);
		cellList.setPageSize(15);
		cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
		ListDataProvider<QualityDataSetDTO> dataProvider = new ListDataProvider<QualityDataSetDTO>(appliedListModel.getAppliedQDMs()); 
		dataProvider.addDataDisplay(cellList); 
		pagerPanel.addStyleName("scrollable");
		pagerPanel.setDisplay(cellList);
		rangeLabelPager.setDisplay(cellList);

	}

	private CellList<QualityDataSetDTO> initializeCellListContent(CellList<QualityDataSetDTO> cellList,final QDSAppliedListModel appliedListModel){

		ArrayList<HasCell<QualityDataSetDTO, ?>> hasCells = new ArrayList<HasCell<QualityDataSetDTO, ?>>();
		final MultiSelectionModel<QualityDataSetDTO> selectionModel = new MultiSelectionModel<QualityDataSetDTO>();
		hasCells.add(new HasCell<QualityDataSetDTO, Boolean>(){

			private MatCheckBoxCell cbCell = new MatCheckBoxCell();
			@Override
			public Cell<Boolean> getCell() {
				return cbCell;
			}

			@Override
			public FieldUpdater<QualityDataSetDTO, Boolean> getFieldUpdater() {
				return null;
			}

			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				return selectionModel.isSelected(object);
			} });

		hasCells.add(new HasCell<QualityDataSetDTO, String>(){
			private TextCell cell = new TextCell();
			@Override
			public Cell<String> getCell() {
				return (Cell)cell;
			}

			@Override
			public FieldUpdater<QualityDataSetDTO, String> getFieldUpdater() {
				return null;
			}

			@Override
			public String getValue(QualityDataSetDTO object) {
				String value;
				if(object.getOccurrenceText()!= null && !object.getOccurrenceText().equals(""))
					value = object.getOccurrenceText() + " of "+object.getCodeListName() + ": " + object.getDataType() ;
				else
					value= object.getCodeListName() + ": " + object.getDataType() ;
				return value;
			}});


		Cell<QualityDataSetDTO> myClassCell = new CompositeCell<QualityDataSetDTO>(hasCells){
			@Override
			public void render(Context context, QualityDataSetDTO value, SafeHtmlBuilder sb)
			{
				sb.appendHtmlConstant("<table><tbody><tr>");
				super.render(context, value, sb);
				sb.appendHtmlConstant("</tr></tbody></table>");
			}
			@Override
			protected Element getContainerElement(Element parent)
			{
				// Return the first TR element in the table.
				return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
			}

			@Override
			protected <X> void render(Context context, QualityDataSetDTO value, SafeHtmlBuilder sb, HasCell<QualityDataSetDTO, X> hasCell)
			{
				// this renders each of the cells inside the composite cell in a new table cell
				Cell<X> cell = hasCell.getCell();
				sb.appendHtmlConstant("<td style='font-size:95%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</td>");
			}

		};

		cellList =  new CellList<QualityDataSetDTO>(myClassCell);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				appliedListModel.setRemoveQDMs(selectionModel.getSelectedSet());
				System.out.println("appliedListModel Remove QDS Set Size =======>>>>" + appliedListModel.getRemoveQDMs().size());
			}
		});
		cellList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<QualityDataSetDTO> createCheckboxManager());
		return cellList;
	}

	private boolean checkForEnable(){
		//			return MatContext.get().getMeasureLockService().checkForEditPermission();
		// uncomment above line once remove button action is active and implemented.
		return false;
	}

	@Override
	public  void buildCellList(QDSAppliedListModel appliedListModel) {
		if(appliedListModel.getAppliedQDMs()!=null){
			cellListJSON = initializeCellListContent(cellListJSON,appliedListModel);
			cellListJSON.setPageSize(15);
			cellListJSON.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
			ListDataProvider<QualityDataSetDTO> dataProvider = new ListDataProvider<QualityDataSetDTO>(appliedListModel.getAppliedQDMs()); 
			dataProvider.addDataDisplay(cellListJSON); 
			pagerPanelJSON.addStyleName("scrollableJSON");
			pagerPanelJSON.setDisplay(cellListJSON);
			rangeLabelPagerJSON.setDisplay(cellListJSON);
		}
	}



}
