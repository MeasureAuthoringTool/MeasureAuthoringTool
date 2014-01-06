package mat.client.measure;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.MeasureSearchResultsAdapter.Observer;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.MatSimplePager;
import mat.client.shared.MostRecentMeasureWidget;
import mat.client.shared.SpacerWidget;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.util.CellTableUtility;
import mat.client.util.ClientConstants;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.TableCaptionElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasureSearchView.
 * 
 * @author aschmidt
 */
public class MeasureSearchView  implements HasSelectionHandlers<ManageMeasureSearchModel.Result>{

	/** The odd. */
	boolean odd = false;
	
	/** The add image. */
	boolean addImage = true;
	
	/** The cell table panel. */
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	/** The main panel. */
	private FlowPanel mainPanel = new FlowPanel();
		
	/** The Constant PAGE_SIZE. */
	private static final int PAGE_SIZE = 25;	
	
	/** The selected measure list. */
	List<ManageMeasureSearchModel.Result> selectedMeasureList ;
	
	/** The handler manager. */
	private HandlerManager handlerManager = new HandlerManager(this);
	
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	/** The observer. */
	private Observer observer;
	
	private int tableRowCount;
	
    private CellTable<ManageMeasureSearchModel.Result> table;
	
	
	
/**
 * The Interface Observer.
 */
public static interface Observer {
		
		/**
		 * On edit clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onEditClicked(ManageMeasureSearchModel.Result result);
		
		/**
		 * On clone clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onCloneClicked(ManageMeasureSearchModel.Result result);
		
		/**
		 * On share clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onShareClicked(ManageMeasureSearchModel.Result result);
		
		/**
		 * On export clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onExportClicked(ManageMeasureSearchModel.Result result);
		
		/**
		 * On history clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onHistoryClicked(ManageMeasureSearchModel.Result result);	
		
		/**
		 * On export selected clicked.
		 * 
		 * @param checkBox
		 *            the check box
		 */
		public void onExportSelectedClicked(CustomCheckBox checkBox);
		
		/**
		 * On export selected clicked.
		 *
		 * @param result the result
		 */
		public void onExportSelectedClicked(ManageMeasureSearchModel.Result result);
	}
	
	/**
	 * Instantiates a new measure search view.
	 * 
	 * @param string
	 *            the string
	 */
	public MeasureSearchView(String string) {
		this();
	}
	
	
	/**
	 * Instantiates a new measure search view.
	 */
	public MeasureSearchView(){
		mainPanel.getElement().setId("measureserachView_mainPanel");
		mainPanel.setStylePrimaryName("measureSearchResultsContainer");
		mainPanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("serachView_mainPanel");
	}
	
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable() {
		final MultiSelectionModel<ManageMeasureSearchModel.Result> selectionModel = 
				new MultiSelectionModel<ManageMeasureSearchModel.Result>();
		table.setSelectionModel(selectionModel);
		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new ClickableSafeHtmlCell()) {
			List<String> measureSetID=new ArrayList<String>();
			int rowindex=0;
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				SafeHtmlBuilder sb = new SafeHtmlBuilder();
				String cssClass="customCascadeButton";
				String textCssClass="textEmptySpaces";
				measureSetID.add(object.getMeasureSetId());
				
				if(rowindex>0){
					if(object.getMeasureSetId().equalsIgnoreCase(measureSetID.get(rowindex-1))){
						sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" >" + 
								"<p class='"+textCssClass+"' title='" +object.getName()+"'>"+object.getName()+"</p></a>");
						
						}	
					else{
						sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
								+ "style=\"text-decoration:none\" >" + 
								"<button title='"+object.getName() 
								+"'" + "class='"+cssClass + "'></button>");
						sb.appendHtmlConstant("<span title='" +object.getName()+"'>"+object.getName()+"</span>");
						sb.appendHtmlConstant("</a>");
						
//						sb.appendHtmlConstant("<table><tr><th><a href=\"javascript:void(0);\" "
//								+ "style=\"text-decoration:none\" >" + 
//								"<button title='"+object.getName() 
//								+"'" + "class='"+cssClass + "'></button></a></th>");
//						sb.appendHtmlConstant("<th><a><span title='" +object.getName()+"'>"+object.getName()+"</span>");
//						sb.appendHtmlConstant("</a></th></tr></table>");
					}
				}
				else{
					sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
							+ "style=\"text-decoration:none\" >" + 
							"<button title='"+object.getName() 
							+"'" + "class='"+cssClass + "'></button>");
					sb.appendHtmlConstant("<span title='" +object.getName()+"'>"+object.getName()+"</span>");
					sb.appendHtmlConstant("</a>");
				}
				rowindex++;
				return sb.toSafeHtml();
			}
		};
		measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
					@Override
					public void update(int index,ManageMeasureSearchModel.Result object,
							SafeHtml value) {
						SelectionEvent.fire(MeasureSearchView.this,object);
					}
				});
		table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
						+ "Measure Name" + "</span>"));

		// Version Column
		Column<ManageMeasureSearchModel.Result, SafeHtml> version = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
				new MatSafeHTMLCell()) {
			@Override
			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
				return CellTableUtility.getColumnToolTip(object.getVersion());
			}
		};
		table.addColumn(version, SafeHtmlUtils
				.fromSafeConstant("<span title='Version'>" + "Version"
						+ "</span>"));

		//Finalized Date
				Column<ManageMeasureSearchModel.Result, SafeHtml> finalizedDate = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
						new MatSafeHTMLCell()) {
					@Override
					public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
						return CellTableUtility.getColumnToolTip(convertTimestampToString(object.getFinalizedDate()));
					}
				};
				table.addColumn(finalizedDate, SafeHtmlUtils
						.fromSafeConstant("<span title='Finalized Date'>" + "Finalized Date"
								+ "</span>"));
				
		//History
				
				Cell<String> historyButton = new MatButtonCell("Click to view history", "customClockButton");
				Column<Result, String> historyColumn = new Column<ManageMeasureSearchModel.Result, String>(historyButton) 
						{
					@Override
					public String getValue(ManageMeasureSearchModel.Result object) {
						return "History";
					}
					
				};
				historyColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, String value) {
						observer.onHistoryClicked(object);
					}
				});
				table.addColumn(historyColumn, SafeHtmlUtils.fromSafeConstant("<span title='History'>" + "History" + "</span>"));
				
		//Edit
				
				Column<ManageMeasureSearchModel.Result, SafeHtml> editColumn = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
						new ClickableSafeHtmlCell()){

							@Override
							public SafeHtml getValue(Result object) {
								SafeHtmlBuilder sb= new SafeHtmlBuilder();
								String title;
								String cssClass;
								if(object.isEditable()){
									title="Edit";
									cssClass="customEditButton";
									sb.appendHtmlConstant("<button title='"+title
											+"'" + "class='"+cssClass + "'></button>");
								}else{
									title="ReadOnly";
									cssClass="customReadOnlyButton";
									sb.appendHtmlConstant("<button title='"+title
											+"'" + "class='"+cssClass + "' disabled='disabled' ></button>");
								}
								return sb.toSafeHtml();
						}	
				};
				
				editColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
		         		@Override
						public void update(int index, Result object,
								SafeHtml value) {
		         			if(object.isEditable()){
		         			observer.onEditClicked(object);
		         			}
						}
				});
				table.addColumn(editColumn, SafeHtmlUtils.fromSafeConstant("<span title='Edit'>" + "Edit" + "</span>"));
				
				
		//Share
				Cell<String> shareButton = new MatButtonCell("Click to view sharable", "customShareButton");
				Column<Result, String> shareColumn = new Column<ManageMeasureSearchModel.Result, String>(shareButton) 
						{
					@Override
					public String getValue(ManageMeasureSearchModel.Result object) {
						return "Share";
					}
					
				};
				shareColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, String value) {
					  observer.onShareClicked(object);
					}
				});
				table.addColumn(shareColumn, SafeHtmlUtils.fromSafeConstant("<span title='Share'>" + "Share" + "</span>"));
				
		//Clone
				Cell<String> cloneButton = new MatButtonCell("Click to view cloneable", "customCloneButton");
				Column<Result, String> cloneColumn = new Column<ManageMeasureSearchModel.Result, String>(cloneButton) 
						{
					@Override
					public String getValue(ManageMeasureSearchModel.Result object) {
						return "Clone";
					}
					
				};
				cloneColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index, ManageMeasureSearchModel.Result object, String value) {
						observer.onCloneClicked(object);
					}
				});
				table.addColumn(cloneColumn, SafeHtmlUtils.fromSafeConstant("<span title='Clone'>" + "Clone" + "</span>"));



		// export Column
		Cell<String> exportButton = new MatButtonCell("Click to Export","customExportButton");
		Column<Result, String> exportColumn = new Column<ManageMeasureSearchModel.Result, String>(exportButton) 
				{
			@Override
			public String getValue(ManageMeasureSearchModel.Result object) {
				return "Export";
			}

			@Override
			public void onBrowserEvent(Context context, Element elem,
					final ManageMeasureSearchModel.Result object,
					NativeEvent event) {
				if ((object != null) && object.isExportable()) {
					super.onBrowserEvent(context, elem, object, event);
				}
			}

			@Override
			public void render(Cell.Context context,
					ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
				if (object.isExportable()) {
					super.render(context, object, sb);
				}
			}
		};
		exportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
					@Override
					public void update(int index,ManageMeasureSearchModel.Result object, String value) {
						if ((object != null) && object.isExportable()) {
							observer.onExportClicked(object);
						}
					}
				});
		table.addColumn(exportColumn, SafeHtmlUtils.fromSafeConstant("<span title='Export'>" + "Export"
						+ "</span>"));
		
		
//		CheckboxCell headerCheckbox = new CheckboxCell();
//		Header<Boolean> bulkExportHeader = new Header<Boolean>(headerCheckbox) {
//
//			@Override
//			public Boolean getValue() {
//				return false;
//			}
//		};
//
//		bulkExportHeader.setUpdater(new ValueUpdater<Boolean>() {
//
//			@Override
//			public void update(Boolean value) {
//				List<ManageMeasureSearchModel.Result> displayedItems = table.getVisibleItems();
//
//				for (ManageMeasureSearchModel.Result msg : displayedItems) {
//					selectionModel.setSelected(msg, value);
//
//				}
//			}
//		});
		
		Cell<Boolean> bulkExportCheckbox = new MatCheckBoxCell();
		Column<Result, Boolean> bulkExportColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(bulkExportCheckbox) {
			@Override
			public Boolean getValue(ManageMeasureSearchModel.Result object) {
				//return !object.isExportable();
				return selectionModel.isSelected(object);
			}
			
			@Override
			public void render(Cell.Context context,
					ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
				if (object.isExportable()) {
					super.render(context, object, sb);
				}
			}
		};
		bulkExportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
			@Override
			public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
				object.setExportable(value);
				selectionModel.setSelected(object, value);
				observer.onExportSelectedClicked(object);
			}
		});
		//table.addColumn(bulkExportColumn, bulkExportHeader);
		table.addColumn(bulkExportColumn, SafeHtmlUtils.fromSafeConstant("<span title='Check for Bulk Export'>"
				+ "Bulk Export </span>"));
		
		return table;
	}
	
    
	/**
	 * Builds the cell table.
	 *
	 * @param results the results
	 */
	public void buildCellTable(ManageMeasureSearchModel results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		table = new CellTable<ManageMeasureSearchModel.Result>();
		table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
	    selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData());
		//table.setRowData(selectedMeasureList);
		table.setPageSize(25);
		table.redraw();
		table.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData());
		table = addColumnToTable();
	//	buildTableCssStyle();
		sortProvider.addDataDisplay(table);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(table);
		spager.setPageSize(PAGE_SIZE);
		table.setWidth("100%");
		table.setColumnWidth(0, 35.0, Unit.PCT);
		table.setColumnWidth(1, 25.0, Unit.PCT);
		table.setColumnWidth(2, 25.0, Unit.PCT);
		table.setColumnWidth(3, 5.0, Unit.PCT);
		table.setColumnWidth(4, 5.0, Unit.PCT);
		table.setColumnWidth(5, 5.0, Unit.PCT);
		table.setColumnWidth(6, 5.0, Unit.PCT);
		table.setColumnWidth(7, 5.0, Unit.PCT);
		table.setColumnWidth(8, 5.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
						"In the following Recent Activity table, Measure Name is given in first column,"
								+ " Version in second column and Export in third column.");
		table.getElement().setAttribute("id", "MeasureSearchCellTable");
		table.getElement().setAttribute("aria-describedby", "measureSearchSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(table);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
	}
	
    
    /**
     * Clear bulk export boxes.
     */
    public void clearBulkExportBoxes(){
//    	for (ManageMeasureSearchModel.Result result : selectedMeasureList) {
//			result.setExportable(false);
//		}
//    	MeasureSearchResultsAdapter adapter = new MeasureSearchResultsAdapter();
//		adapter.getData().setData(selectedMeasureList);
//		buildCellTable(adapter);
	}
	

	
	/* (non-Javadoc)
 * @see mat.client.shared.search.SearchView#asWidget()
 */
public Widget asWidget() {
		return mainPanel;
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchView#fireEvent(com.google.gwt.event.shared.GwtEvent)
	 */
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchView#addSelectionHandler(com.google.gwt.event.logical.shared.SelectionHandler)
	 */
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	/**
	 * Gets the select id for edit tool.
	 *
	 * @return the select id for edit tool
	 */
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
	}
	
	
	
	public ManageMeasureSearchModel getData() {
		return data;
	}


	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
	}


	public Observer getObserver() {
		return observer;
	}


	public void setObserver(Observer observer) {
		this.observer = observer;
	}


	/**
	 * assumption made: results are sorted by the time they are given here.
	 * 
	 * @param numRows
	 *            the num rows
	 * @param numColumns
	 *            the num columns
	 * @param results
	 *            the results
	 */
//	@Override
//	protected void buildSearchResults(int numRows,int numColumns,final SearchResults results){
//		
//		for(int i = 0; i < numRows; i++) {
//			
//			if(i > 0){
//				ManageMeasureSearchModel.Result result = (ManageMeasureSearchModel.Result)results.get(i);
//				String currentMid = result.getMeasureSetId();
//				result = (ManageMeasureSearchModel.Result)results.get(i-1);
//				String previousMid = result.getMeasureSetId();
//				if(!currentMid.equalsIgnoreCase(previousMid)){
//					odd = !odd;
//					addImage = true;
//					result.setTransferable(true);
//				}else{
//					addImage = false;
//				}
//			}else{
//				odd = false;
//				addImage = true;
//			}
//			if(addImage){
//				((ManageMeasureSearchModel.Result)results.get(i)).setTransferable(true);
//			}
//				
//			for(int j = 0; j < numColumns; j++) {
//				if(results.isColumnFiresSelection(j)) {
//					String innerText = results.getValue(i, j).getElement().getInnerText();
//					innerText = addSpaces(innerText, 27);
//					Widget a = null;
//					final int rowIndex = i;
//					String currentUserRole = MatContext.get().getLoggedInUserRole();
//					if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
//						Anchor anchor = new Anchor(innerText);
//						addClickHandler(anchor, results, rowIndex);
//						a = anchor;
//					}else{
//						Label label = new Label(innerText);
//						a= label;
//					}
//					
//					Panel holder = new HorizontalFlowPanel();
//					SimplePanel innerPanel = new SimplePanel();
//					if(addImage){
//						innerPanel.setStylePrimaryName("pad-right5px");
//						Image image = createImage(rowIndex, results, innerText);
//						innerPanel.add(image);
//						holder.add(innerPanel);
//						holder.add(a);
//					}else{
//						innerPanel.setStylePrimaryName("pad-left21px");
//						innerPanel.add(a);
//						holder.add(innerPanel);
//					}
//					
//					dataTable.setWidget(i+1, j, holder);
//				}
//				else {
//					dataTable.setWidget(i+1, j,results.getValue(i, j));
//				}
//			}
//			if(odd){
//				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
//			}else{
//				//if already set to 'odd' and we are just refreshing, then 'odd' has to be removed
//				dataTable.getRowFormatter().removeStyleName(i + 1, "odd");
//			}
//		}
//	}
	
	
//	public void buildTableCssStyle(){
//
//		int tablesize=table.getRowCount();
//		boolean EVEN=true;
//		for(int rows=0;rows<tablesize;rows++){
//			if(rows>0){
//				if(EVEN==true){ 
//					if(selectedMeasureList.get(rows).getMeasureSetId()
//						.equalsIgnoreCase(selectedMeasureList.get(rows-1).getMeasureSetId())){
//						table.getRowElement(rows).setClassName("cellTableOddRow");
//						}
//					else{
//						table.getRowElement(rows).setClassName("cellTableEvenRow");
//						EVEN=false;
//						}
//					}
//				else{
//					if(selectedMeasureList.get(rows).getMeasureSetId()
//							.equalsIgnoreCase(selectedMeasureList.get(rows-1).getMeasureSetId())){
//						table.getRowElement(rows).setClassName("cellTableEvenRow");
//						EVEN=true;
//						}
//					else{
//						table.getRowElement(rows).setClassName("cellTableOddRow");
//						EVEN=false;
//						}	
//					}
//				}
//			else{
//				table.getRowElement(rows).setClassName("cellTableOddRow");
//				}
//			}
//		}
	
    /**
     * Builds the image text cell.
     *
     * @param results the results
     */
    public void buildImageTextCell(SearchResults results){
		int numRows=results.getNumberOfRows();
		int numCols=results.getNumberOfColumns();
		
          for(int i = 0; i < numRows; i++) {
			
			if(i > 0){
				ManageMeasureSearchModel.Result result = (ManageMeasureSearchModel.Result)results.get(i);
				String currentMid = result.getMeasureSetId();
				result = (ManageMeasureSearchModel.Result)results.get(i-1);
				String previousMid = result.getMeasureSetId();
				if(!currentMid.equalsIgnoreCase(previousMid)){
					odd = !odd;
					addImage = true;
					result.setTransferable(true);
				}else{
					addImage = false;
				}
			}else{
				odd = false;
				addImage = true;
			}
			
			
}
		
		
	}
	
	/**
	 * Adds the spaces.
	 * 
	 * @param in
	 *            the in
	 * @param frequency
	 *            the frequency
	 * @return the string
	 */
	private String addSpaces(String in, int frequency){
		
		if(in.length() <= frequency)
			return in;
		
		char[] inArr = in.toCharArray();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(char c : inArr){
			if(i == frequency){
				sb.append(' ');
				i = 0;
			}else if(c == ' ')
				i = 0;
			else
				i++;
			sb.append(c);
		}
			
		return sb.toString();
	}
	
	/**
	 * Creates the image.
	 * 
	 * @param rowIndex
	 *            the row index
	 * @param results
	 *            the results
	 * @param text
	 *            value to be assigned to the alt and title attributes of the
	 *            return image
	 * @return the image
	 */
//	private Image createImage(final int rowIndex,final SearchResults results, String text){
//		Image image = new Image(ImageResources.INSTANCE.application_cascade());
//		image.setTitle(text);
//		image.getElement().setAttribute("alt", text);
//		image.setStylePrimaryName("measureSearchResultIcon");
//		addClickHandler(image, results, rowIndex);
//		return image;
//	}
	
	public String convertTimestampToString(Timestamp ts){
		String tsStr;
		if(ts==null){
			tsStr="";
			}else{
		int hours = ts.getHours();
		String ap = hours < 12 ? "AM" : "PM";
		int modhours = hours % 12;
		String mins = ts.getMinutes()+"";
		if(mins.length()==1)
			mins = "0"+mins;
		
		String hoursStr = modhours == 0 ? "12" : modhours+"";
		
		tsStr = (ts.getMonth()+1)+"/"+ts.getDate()+"/"+(ts.getYear()+1900)+" "+hoursStr+":"+mins+" "+ap;
		}
		return tsStr;
	}
	
}
