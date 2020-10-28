package mat.client.shared;

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
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.clause.QDSAppliedListModel;
import mat.model.QualityDataSetDTO;

import java.util.ArrayList;

/**
 * The Class QDMAppliedListWidget.
 */
public class QDMAppliedListWidget {
	
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	
	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel;
	
	/** The main applied qdm panel. */
	HorizontalPanel mainAppliedQDMPanel = new HorizontalPanel();
	
	/** The remove button. */
	public Button removeButton = new Button("Remove");
	
	/** The last selected object. */
	public QualityDataSetDTO  lastSelectedObject;
	
	/** The cell list. */
	private CellList<QualityDataSetDTO> cellList;
	
	/** The pager panel. */
	ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel("QDMPanel");
	
	/** The range label pager. */
	RangeLabelPager rangeLabelPager = new RangeLabelPager();
	
	/**
	 * Instantiates a new qDM applied list widget.
	 */
	public QDMAppliedListWidget(){
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		
		VerticalPanel vp = new VerticalPanel();
		
		vp.setStylePrimaryName("qdmCellList");
		HorizontalPanel mainPanelNormal = new HorizontalPanel();
		mainPanelNormal.getElement().setId("mainPanelNormal_HorizontalPanel");
		mainPanelNormal.add(pagerPanel);
		vp.add(new SpacerWidget());
		vp.add(new SpacerWidget());
		vp.add(errorMessagePanel);
		vp.add(successMessagePanel);
		
		vp.add(new SpacerWidget());
		vp.add(mainPanelNormal);
		//vp.add(new SpacerWidget());
		//vp.add(rangeLabelPager);
		vp.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		vp.add(removeButton);
		vp.add(new SpacerWidget());
		mainAppliedQDMPanel.add(vp);
	}
	
	/**
	 * Initialize cell list content.
	 * 
	 * @param appliedListModel
	 *            the applied list model
	 * @return the cell list
	 */
	private CellList<QualityDataSetDTO> initializeCellListContent(final QDSAppliedListModel appliedListModel){
		
		ArrayList<HasCell<QualityDataSetDTO, ?>> hasCells = new ArrayList<HasCell<QualityDataSetDTO, ?>>();
		//final MultiSelectionModel<QualityDataSetDTO> selectionModel = new MultiSelectionModel<QualityDataSetDTO>();
		final SingleSelectionModel<QualityDataSetDTO> selectionModel = new SingleSelectionModel<QualityDataSetDTO>();
		// cellList.setSelectionModel(selectionModel);
		hasCells.add(new HasCell<QualityDataSetDTO, Boolean>(){
			
			//	private MatCheckBoxCell cbCell = new MatCheckBoxCell();
			private RadioButtonCell cbCell = new RadioButtonCell(true,true);
			@Override
			public Cell<Boolean> getCell() {
				return cbCell;
			}
			
			
			@Override
			public Boolean getValue(QualityDataSetDTO object) {
				cbCell.setUsed(object.isUsed());
				return selectionModel.isSelected(object);
			}
			
			@Override
			public FieldUpdater<QualityDataSetDTO, Boolean> getFieldUpdater() {
				return new FieldUpdater<QualityDataSetDTO, Boolean>() {
					
					@Override
					public void update(int index, QualityDataSetDTO object,Boolean value) {
						//appliedListModel.setLastSelected(object);
						//System.out.println("appliedListModel.getLastSelected() =======>>>>" + appliedListModel.getLastSelected());
						//selectionModel.setSelected(object, true);
						lastSelectedObject=object;
						if(object.isUsed()){
							removeButton.setEnabled(false);
						}else{
							removeButton.setEnabled(true);
						}
						
					}
				};
			} });
		
		
		hasCells.add(new HasCell<QualityDataSetDTO, String>(){
			private TextCell cell = new TextCell();
			@Override
			public Cell<String> getCell() {
				return cell;
			}
			
			@Override
			public FieldUpdater<QualityDataSetDTO, String> getFieldUpdater() {
				return null;
			}
			
			@Override
			public String getValue(QualityDataSetDTO object) {
				String value;
				if((object.getOccurrenceText()!= null) && !object.getOccurrenceText().equals("")) {
					value = object.getOccurrenceText() + " of "+object.getCodeListName() + ": " + object.getDataType() ;
				} else {
					value= object.getCodeListName() + ": " + object.getDataType() ;
				}
				
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
		
		CellList<QualityDataSetDTO> cellsList =  new CellList<QualityDataSetDTO>(myClassCell);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				errorMessagePanel.clear();
				successMessagePanel.clear();
				//appliedListModel.setRemoveQDMs(selectionModel.getSelectedSet());
				//System.out.println("appliedListModel Remove QDS Set Size =======>>>>" + appliedListModel.getRemoveQDMs().size());
				/*listToRemove = new ArrayList<QualityDataSetDTO>(appliedListModel.getRemoveQDMs());
				for(int i=0;i<listToRemove.size();i++){
					System.out.println("QDM IDS=======>>>>" + listToRemove.get(i).getUuid());
					
				}*/
				appliedListModel.setLastSelected(selectionModel.getSelectedObject());
				System.out.println("appliedListModel.getLastSelected() =======>>>>" + appliedListModel.getLastSelected());
				
			}
		});
		
		
		
		cellsList.setSelectionModel(selectionModel, DefaultSelectionEventManager.<QualityDataSetDTO>createDefaultManager());
		//removeButton.setEnabled(checkForEnable());
		return cellsList;
	}
	
	/**
	 * Check for enable.
	 * 
	 * @return true, if successful
	 */
	private boolean checkForEnable(){
		return MatContext.get().getMeasureLockService().checkForEditPermission();
	}
	
	/**
	 * Builds the cell list.
	 * 
	 * @param appliedListModel
	 *            the applied list model
	 */
	public  void buildCellList(QDSAppliedListModel appliedListModel) {
		if(appliedListModel.getAppliedQDMs()!=null){
			cellList = initializeCellListContent(appliedListModel);
			cellList.setPageSize(15);
			cellList.setKeyboardPagingPolicy(KeyboardPagingPolicy.INCREASE_RANGE);
			ListDataProvider<QualityDataSetDTO> dataProvider = new ListDataProvider<QualityDataSetDTO>(appliedListModel.getAppliedQDMs());
			dataProvider.addDataDisplay(cellList);
			pagerPanel.addStyleName("scrollable");
			pagerPanel.setDisplay(cellList);
			rangeLabelPager.setDisplay(cellList);
		}
	}
	
	/**
	 * Gets the main applied qdm panel.
	 * 
	 * @return the main applied qdm panel
	 */
	public HorizontalPanel getMainAppliedQDMPanel() {
		return mainAppliedQDMPanel;
	}
	
	/**
	 * Gets the selected element to remove.
	 * 
	 * @return the selected element to remove
	 */
	public QualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}
	
	/**
	 * Gets the removes the button.
	 * 
	 * @return the removes the button
	 */
	public Button getRemoveButton() {
		return removeButton;
	}
	
	/**
	 * Gets the error message panel.
	 * 
	 * @return the error message panel
	 */
	public ErrorMessageDisplay getErrorMessagePanel() {
		return errorMessagePanel;
	}
	
	/**
	 * Gets the success message panel.
	 * 
	 * @return the success message panel
	 */
	public SuccessMessageDisplay getSuccessMessagePanel() {
		return successMessagePanel;
	}
}
