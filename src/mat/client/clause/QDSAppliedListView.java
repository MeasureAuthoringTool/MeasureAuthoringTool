package mat.client.clause;

import java.util.ArrayList;
import java.util.List;

import mat.client.shared.ErrorMessageDisplay;
import mat.client.shared.ErrorMessageDisplayInterface;
import mat.client.shared.MatContext;
import mat.client.shared.RadioButtonCell;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;



/**
 * The Class QDSAppliedListView.
 */
public class QDSAppliedListView  implements QDSAppliedListPresenter.SearchDisplay {
	
	/** The container panel. */
	private SimplePanel containerPanel = new SimplePanel();
	
	/** The error message panel. */
	private ErrorMessageDisplay errorMessagePanel = new ErrorMessageDisplay();
	
	/** The success message panel. */
	private SuccessMessageDisplay successMessagePanel;
	
	/** The remove button. */
	public Button removeButton = new Button("Remove");
	
	/** The modify. */
	public Button modify = new Button("Modify");
	
	/** The update vsac button. */
	public Button updateVsacButton = new Button("Update from VSAC");
	
	/** The last selected object. */
	public QualityDataSetDTO  lastSelectedObject;
	
	/** The applied qdm list. */
	private ArrayList<QualityDataSetDTO> appliedQDMList;
	
	
	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getSelectedElementToRemove()
	 */
	@Override
	public QualityDataSetDTO getSelectedElementToRemove() {
		return lastSelectedObject;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getRemoveButton()
	 */
	@Override
	public Button getRemoveButton() {
		return removeButton;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getUpdateVsacButton()
	 */
	@Override
	public Button getUpdateVsacButton() {
		return updateVsacButton;
	}

	/** The cell list. */
	private CellList<QualityDataSetDTO> cellList;
	
	/** The pager panel. */
	ShowMorePagerPanel pagerPanel = new ShowMorePagerPanel();
	
	/** The range label pager. */
	RangeLabelPager rangeLabelPager = new RangeLabelPager();

	/**
	 * Gets the success message panel.
	 * 
	 * @return the success message panel
	 */
	public SuccessMessageDisplay getSuccessMessagePanel(){
		return successMessagePanel;
	}

	/**
	 * Gets the error message panel.
	 * 
	 * @return the error message panel
	 */
	public ErrorMessageDisplay getErrorMessagePanel(){
		return errorMessagePanel;
	}

	/**
	 * Instantiates a new qDS applied list view.
	 */
	public QDSAppliedListView() {
		successMessagePanel = new SuccessMessageDisplay();
		successMessagePanel.clear();
		HorizontalPanel mainPanel = new HorizontalPanel();
		mainPanel.getElement().setId("mainPanel_HorizontalPanel");
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
		vp.add(new SpacerWidget());
		vp.add(rangeLabelPager);
		vp.add(new SpacerWidget());
		removeButton.setEnabled(checkForEnable());
		modify.setEnabled(checkForEnable() ? true : false);

		HorizontalPanel buttonLayout = new HorizontalPanel();
		buttonLayout.getElement().setId("buttonLayout_HorizontalPanel");
		buttonLayout.setStylePrimaryName("myAccountButtonLayout");
		removeButton.setTitle("Remove");
		modify.setTitle("Modify");
		modify.setStyleName("rightAlignSecondaryButton");
		updateVsacButton.setStylePrimaryName("rightAlignSecondaryButton");
		updateVsacButton.setTitle("Retrieve the most recent versions of applied value sets from VSAC");
		buttonLayout.add(removeButton);
		buttonLayout.add(modify);
		buttonLayout.add(updateVsacButton);
		vp.add(buttonLayout);
		vp.add(new SpacerWidget());

		mainPanel.add(vp);
		containerPanel.add(mainPanel);

		containerPanel.setStyleName("qdsContentPanel");
		MatContext.get().setQdsAppliedListView(this);
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#asWidget()
	 */
	@Override
	public Widget asWidget() {
		return containerPanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getErrorMessageDisplay()
	 */
	@Override
	public ErrorMessageDisplayInterface getErrorMessageDisplay() {
		return errorMessagePanel;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getApplyToMeasureSuccessMsg()
	 */
	@Override
	public SuccessMessageDisplayInterface getApplyToMeasureSuccessMsg() {
		return successMessagePanel;
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
						errorMessagePanel.clear();
						lastSelectedObject=object;
						if (checkForEnable()) {
							modify.setEnabled(true);
							/*updateVsacButton.setEnabled(true);*/
							if(object.isUsed()){
								removeButton.setEnabled(false);
							}else{
								removeButton.setEnabled(true);
							}
						}else{
							removeButton.setEnabled(false);
							modify.setEnabled(false);
							/*updateVsacButton.setEnabled(false);*/
						}

					}
				};
			} });
		
		
		hasCells.add(new HasCell<QualityDataSetDTO, String>(){
			private TextCell cell = new TextCell();
			@SuppressWarnings("unchecked")
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

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#buildCellList(mat.client.clause.QDSAppliedListModel)
	 */
	@Override
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
			removeButton.setEnabled(false);
			modify.setEnabled(checkForEnable() && appliedListModel.getLastSelected()!=null ?true:false);
		}
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getModifyButton()
	 */
	@Override
	public Button getModifyButton() {
		return modify;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#getAllAppliedQDMList()
	 */
	@Override
	public List<QualityDataSetDTO> getAllAppliedQDMList() {
		return getAppliedQDMList();
	}

	/**
	 * Gets the applied qdm list.
	 * 
	 * @return the applied qdm list
	 */
	public ArrayList<QualityDataSetDTO> getAppliedQDMList() {
		return appliedQDMList;
	}

	/* (non-Javadoc)
	 * @see mat.client.clause.QDSAppliedListPresenter.SearchDisplay#setAppliedQDMList(java.util.ArrayList)
	 */
	@Override
	public void setAppliedQDMList(ArrayList<QualityDataSetDTO> appliedQDMList) {
		this.appliedQDMList = appliedQDMList;
	}



}
