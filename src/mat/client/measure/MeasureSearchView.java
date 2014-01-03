package mat.client.measure;

import java.util.ArrayList;
import java.util.List;

import mat.client.CustomPager;
import mat.client.ImageResources;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.shared.HorizontalFlowPanel;
import mat.client.shared.LabelBuilder;
import mat.client.shared.MatButtonCell;
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
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
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

/**
 * The Class MeasureSearchView.
 * 
 * @author aschmidt
 */
public class MeasureSearchView extends SearchView<ManageMeasureSearchModel.Result> 
                                       implements HasSelectionHandlers<ManageMeasureSearchModel.Result>{

	/** The odd. */
	boolean odd = false;
	
	/** The add image. */
	boolean addImage = true;
	
	private VerticalPanel cellTablePanel = new VerticalPanel();
	
	private FlowPanel mainPanel = new FlowPanel();
		
	private static final int PAGE_SIZE = 25;	
	
	List<ManageMeasureSearchModel.Result> selectedMeasureList ;
	
	private HandlerManager handlerManager = new HandlerManager(this);
	/**
	 * Instantiates a new measure search view.
	 * 
	 * @param string
	 *            the string
	 */
	public MeasureSearchView(String string) {
		this();
	}
	
	
	public MeasureSearchView(){
		mainPanel.getElement().setId("measureserachView_mainPanel");
		mainPanel.setStylePrimaryName("measureSearchResultsContainer");
		mainPanel.add(new SpacerWidget());
		cellTablePanel.getElement().setId("cellTablePanel_VerticalPanel");
		mainPanel.add(cellTablePanel);
		mainPanel.setStyleName("serachView_mainPanel");
	}
	
	
	
    @Override
	public void buildCellTable(MeasureSearchResultsAdapter results) {
		cellTablePanel.clear();
		cellTablePanel.setStyleName("cellTablePanel");
		CellTable<ManageMeasureSearchModel.Result> cellTable = new CellTable<ManageMeasureSearchModel.Result>();
		cellTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
		ListDataProvider<ManageMeasureSearchModel.Result> sortProvider = new ListDataProvider<ManageMeasureSearchModel.Result>();
	    selectedMeasureList = new ArrayList<Result>();
		selectedMeasureList.addAll(results.getData().getData());
		cellTable.setPageSize(25);
		cellTable.redraw();
		cellTable.setRowCount(selectedMeasureList.size(), true);
		sortProvider.refresh();
		sortProvider.getList().addAll(results.getData().getData());
		//buildImageTextCell(results);
		
		//
//		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
//				new ClickableSafeHtmlCell()) {
//			@Override
//			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
//				SafeHtmlBuilder sb = new SafeHtmlBuilder();
//				cellTable.getRowElement(1);
//				String cssClass="customCascadeButton";
//				sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
//						+ "style=\"text-decoration:none\" >" + "<button title='"+object.getName() 
//						+"'" + "class='"+cssClass + "'></button>" +
//						"<span title='" +object.getName()+"'>"+object.getName()+"</span>");
//				//sb.appendEscaped(object.getName());
//				sb.appendHtmlConstant("</a>");
//				return sb.toSafeHtml();
//			}
//		};
//		measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
//					@Override
//					public void update(int index,ManageMeasureSearchModel.Result object,
//							SafeHtml value) {
//						SelectionEvent.fire(MeasureSearchView.this,object);
//					}
//				});
//		cellTable.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
//						+ "Measure Name" + "</span>"));
		//
		cellTable = results.addColumnToTable(cellTable);
		sortProvider.addDataDisplay(cellTable);
		CustomPager.Resources pagerResources = GWT.create(CustomPager.Resources.class);
		MatSimplePager spager = new MatSimplePager(CustomPager.TextLocation.CENTER, pagerResources, false, 0, true);
		spager.setPageStart(0);
		spager.setDisplay(cellTable);
		spager.setPageSize(PAGE_SIZE);
		cellTable.setWidth("100%");
		cellTable.setColumnWidth(0, 35.0, Unit.PCT);
		cellTable.setColumnWidth(1, 25.0, Unit.PCT);
		cellTable.setColumnWidth(2, 25.0, Unit.PCT);
		cellTable.setColumnWidth(3, 5.0, Unit.PCT);
		cellTable.setColumnWidth(4, 5.0, Unit.PCT);
		cellTable.setColumnWidth(5, 5.0, Unit.PCT);
		cellTable.setColumnWidth(6, 5.0, Unit.PCT);
		cellTable.setColumnWidth(7, 5.0, Unit.PCT);
		cellTable.setColumnWidth(8, 5.0, Unit.PCT);
		Label invisibleLabel = (Label) LabelBuilder.buildInvisibleLabel("measureSearchSummary",
						"In the following Recent Activity table, Measure Name is given in first column,"
								+ " Version in second column and Export in third column.");
		cellTable.getElement().setAttribute("id", "MeasureSearchCellTable");
		cellTable.getElement().setAttribute("aria-describedby", "measureSearchSummary");
		cellTablePanel.add(invisibleLabel);
		cellTablePanel.add(cellTable);
		cellTablePanel.add(new SpacerWidget());
		cellTablePanel.add(spager);
	}
	
    
    public void clearBulkExportBoxes(){
    	for (ManageMeasureSearchModel.Result result : selectedMeasureList) {
			result.setExportable(false);
		}
    	MeasureSearchResultsAdapter adapter = new MeasureSearchResultsAdapter();
		adapter.getData().setData(selectedMeasureList);
		buildCellTable(adapter);
	}
	
//	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(final CellTable<ManageMeasureSearchModel.Result> table){
//		
//		    	Column<ManageMeasureSearchModel.Result, SafeHtml> measureName =
//				new Column<ManageMeasureSearchModel.Result, SafeHtml>(new
//						ClickableSafeHtmlCell()) {
//			@Override
//			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
//				SafeHtmlBuilder sb = new SafeHtmlBuilder();
//				sb.appendHtmlConstant("<a href=\"javascript:void(0);\" "
//						+ "style=\"text-decoration:none\" title='" + object.getName()
//						+ "' >");
//				sb.appendEscaped(object.getName());
//				sb.appendHtmlConstant("</a>");
//				return sb.toSafeHtml();
//			}
//		};
//		measureName.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, SafeHtml>() {
//			@Override
//			public void update(int index, ManageMeasureSearchModel.Result object, SafeHtml value) {
//				SelectionEvent.fire(MeasureSearchView.this, object);
//			}
//		});
//			table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant(
//				"<span title='Measure Name Column'>" + "Measure Name" + "</span>"));
//	        
//			//Version Column		
//			Column<ManageMeasureSearchModel.Result, SafeHtml> version =
//					new Column<ManageMeasureSearchModel.Result, SafeHtml>(
//							new MatSafeHTMLCell()) {
//				@Override
//				public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
//					return CellTableUtility.getColumnToolTip(object.getVersion());
//				}
//			};
//			table.addColumn(version, SafeHtmlUtils.fromSafeConstant(
//					"<span title='Version'>" + "Version" + "</span>"));
//			
//			//export Column
//			Cell<String> exportButton = new MatButtonCell("Click to Export", "customExportButton");
//			Column<Result, String> exportColumn =
//					new Column<ManageMeasureSearchModel.Result, String>(exportButton) {
//				@Override
//				public String getValue(ManageMeasureSearchModel.Result object) {
//					return "Export";
//				}
//				@Override
//				public void onBrowserEvent(Context context, Element elem,
//						final ManageMeasureSearchModel.Result object, NativeEvent event) {
//					if ((object != null) && object.isExportable()) {
//						super.onBrowserEvent(context, elem, object, event);
//					}
//				}
//				@Override
//				public void render(Cell.Context context, ManageMeasureSearchModel.Result object,
//						SafeHtmlBuilder sb) {
//					if (object.isExportable()) {
//						super.render(context, object, sb);
//					}
//				}
//			};
//			exportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, String>() {
//				@Override
//				public void update(int index, ManageMeasureSearchModel.Result object, String value) {
//					if ((object != null) && object.isExportable()) {
//						//observer.onExportClicked(object);
//					}
//				}
//			});
//			table.addColumn(exportColumn,
//					SafeHtmlUtils.fromSafeConstant("<span title='Export'>"
//							+ "Export" + "</span>"));
//	
//			
//	return table;
//}
	
	public Widget asWidget() {
		return mainPanel;
	}
	
	@Override
	public void fireEvent(GwtEvent<?> event) {
		handlerManager.fireEvent(event);
	}
	
	@Override
	public HandlerRegistration addSelectionHandler(SelectionHandler<ManageMeasureSearchModel.Result> handler) {
		return handlerManager.addHandler(SelectionEvent.getType(), handler);
	}
	
	
	public HasSelectionHandlers<ManageMeasureSearchModel.Result> getSelectIdForEditTool() {
		return this;
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
	@Override
	protected void buildSearchResults(int numRows,int numColumns,final SearchResults results){
		
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
			if(addImage){
				((ManageMeasureSearchModel.Result)results.get(i)).setTransferable(true);
			}
				
			for(int j = 0; j < numColumns; j++) {
				if(results.isColumnFiresSelection(j)) {
					String innerText = results.getValue(i, j).getElement().getInnerText();
					innerText = addSpaces(innerText, 27);
					Widget a = null;
					final int rowIndex = i;
					String currentUserRole = MatContext.get().getLoggedInUserRole();
					if(!currentUserRole.equalsIgnoreCase(ClientConstants.ADMINISTRATOR)){
						Anchor anchor = new Anchor(innerText);
						addClickHandler(anchor, results, rowIndex);
						a = anchor;
					}else{
						Label label = new Label(innerText);
						a= label;
					}
					
					Panel holder = new HorizontalFlowPanel();
					SimplePanel innerPanel = new SimplePanel();
					if(addImage){
						innerPanel.setStylePrimaryName("pad-right5px");
						Image image = createImage(rowIndex, results, innerText);
						innerPanel.add(image);
						holder.add(innerPanel);
						holder.add(a);
					}else{
						innerPanel.setStylePrimaryName("pad-left21px");
						innerPanel.add(a);
						holder.add(innerPanel);
					}
					
					dataTable.setWidget(i+1, j, holder);
				}
				else {
					dataTable.setWidget(i+1, j,results.getValue(i, j));
				}
			}
			if(odd){
				dataTable.getRowFormatter().addStyleName(i + 1, "odd");
			}else{
				//if already set to 'odd' and we are just refreshing, then 'odd' has to be removed
				dataTable.getRowFormatter().removeStyleName(i + 1, "odd");
			}
		}
	}
	
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
	private Image createImage(final int rowIndex,final SearchResults results, String text){
		Image image = new Image(ImageResources.INSTANCE.application_cascade());
		image.setTitle(text);
		image.getElement().setAttribute("alt", text);
		image.setStylePrimaryName("measureSearchResultIcon");
		addClickHandler(image, results, rowIndex);
		return image;
	}
	
}
