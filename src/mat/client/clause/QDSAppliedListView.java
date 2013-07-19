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
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardPagingPolicy.KeyboardPagingPolicy;
import com.google.gwt.user.client.ui.Button;
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
	
	public Button removeButton = new Button("Remove");
	public List<QualityDataSetDTO> listToRemove;
	@Override
	public List<QualityDataSetDTO> getListToRemove() {
		return listToRemove;
	}

	@Override
	public Button getRemoveButton() {
		return removeButton;
	}

	private CellList<QualityDataSetDTO> cellList;
	
	ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
	RangeLabelPager rangeLabelPager = new RangeLabelPager();

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
		vp.add(new SpacerWidget());
		vp.add(errorMessagePanel);
		vp.add(successMessagePanel);
		
		vp.add(new SpacerWidget());
		vp.add(mainPanelNormal);
		vp.add(new SpacerWidget());
		vp.add(rangeLabelPager);
		vp.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		vp.add(removeButton);
		vp.add(new SpacerWidget());

		mainPanel.add(vp);
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
				cbCell.setUsed(object.isUsed());
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
				sb.appendHtmlConstant("<td style='font-size:100%;'>");
				cell.render(context, hasCell.getValue(value), sb);
				sb.appendHtmlConstant("</td>");
			}

		};

		cellList =  new CellList<QualityDataSetDTO>(myClassCell);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				errorMessagePanel.clear();
				successMessagePanel.clear();
				appliedListModel.setRemoveQDMs(selectionModel.getSelectedSet());
				System.out.println("appliedListModel Remove QDS Set Size =======>>>>" + appliedListModel.getRemoveQDMs().size());
				listToRemove = new ArrayList<QualityDataSetDTO>(appliedListModel.getRemoveQDMs());
				for(int i=0;i<listToRemove.size();i++){
					System.out.println("QDM IDS=======>>>>" + listToRemove.get(i).getUuid());
					
				}
			}
		});
		cellList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<QualityDataSetDTO> createCheckboxManager());
		removeButton.setEnabled(checkForEnable());
		return cellList;
	}

	private boolean checkForEnable(){
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}

	@Override
	public  void buildCellList(QDSAppliedListModel appliedListModel) {
		if(appliedListModel.getAppliedQDMs()!=null){
			cellList = initializeCellListContent(cellList,appliedListModel);
			cellList.setPageSize(15);
			cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
			ListDataProvider<QualityDataSetDTO> dataProvider = new ListDataProvider<QualityDataSetDTO>(appliedListModel.getAppliedQDMs()); 
			dataProvider.addDataDisplay(cellList); 
			pagerPanel.addStyleName("scrollable");
			pagerPanel.setDisplay(cellList);
			rangeLabelPager.setDisplay(cellList);
		}
	}



}
