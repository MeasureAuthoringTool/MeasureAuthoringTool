package mat.client.measure;

import java.sql.Timestamp;

import mat.client.Enableable;
import mat.client.ImageResources;
import mat.client.measure.ManageMeasureSearchModel.Result;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.search.HasPageSelectionHandler;
import mat.client.shared.search.HasPageSizeSelectionHandler;
import mat.client.shared.search.HasSelectAllHandler;
import mat.client.shared.search.HasSortHandler;
import mat.client.shared.search.PageSelectionEventHandler;
import mat.client.shared.search.PageSizeSelectionEventHandler;
import mat.client.shared.search.PageSortEventHandler;
import mat.client.shared.search.SearchResults;
import mat.client.shared.search.SearchView;
import mat.client.shared.search.SelectAllEvent;
import mat.client.shared.search.SelectAllEventHandler;
import mat.client.util.CellTableUtility;
import mat.shared.ClickableSafeHtmlCell;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The Class MeasureSearchResultsAdapter.
 */
public class MeasureSearchResultsAdapter implements SearchResults<ManageMeasureSearchModel.Result>, 
                                                          HasSelectionHandlers<ManageMeasureSearchModel.Result>
                                                              {
	
	/** The headers. */
	private static String[] headers = new String[] { "Measure Name", "Version", "Finalized Date", "Status", "History" ,"Edit","Share", "Clone", "ExportClear"};
	
	/** The widths. */
	private static String[] widths = new String[] { "35%", "16%", "16%", "8%", "5%","5%", "5%", "5%", "5%", "12%" };
	
	private HandlerManager handlerManager = new HandlerManager(this);

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
		
		public void onExportSelectedClicked(ManageMeasureSearchModel.Result result);
	}
		
	/** The data. */
	private ManageMeasureSearchModel data = new ManageMeasureSearchModel();
	
	/** The observer. */
	private Observer observer;
	
	/** The click handler. */
	private ClickHandler clickHandler = buildClickHandler();

	/**
	 * Gets the result for id.
	 * 
	 * @param id
	 *            the id
	 * @return the result for id
	 */
	private ManageMeasureSearchModel.Result getResultForId(String id) {
		for(int i = 0; i < data.getNumberOfRows(); i++) {
			if(id.equals(data.getKey(i))) {
				return get(i);
			}
		}
		Window.alert("Could not find id " + id);
		return null;
	}
	
	/**
	 * Sets the observer.
	 * 
	 * @param observer
	 *            the new observer
	 */
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(ManageMeasureSearchModel data) {
		this.data = data;
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	@Override
	public int getNumberOfRows() {
		return data.getNumberOfRows();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 0;
	}
	
	public CellTable<ManageMeasureSearchModel.Result> addColumnToTable(
			final CellTable<ManageMeasureSearchModel.Result> table) {
		
//		Column<ManageMeasureSearchModel.Result, SafeHtml> measureName = new Column<ManageMeasureSearchModel.Result, SafeHtml>(
//				new ClickableSafeHtmlCell()) {
//			@Override
//			public SafeHtml getValue(ManageMeasureSearchModel.Result object) {
//				SafeHtmlBuilder sb = new SafeHtmlBuilder();
//				if(table.getRowCount()>0){
//					
//				}
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
//						SelectionEvent.fire(MeasureSearchResultsAdapter.this,object);
//					}
//				});
//		table.addColumn(measureName, SafeHtmlUtils.fromSafeConstant("<span title='Measure Name Column'>"
//						+ "Measure Name" + "</span>"));

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
		
		//BulkExportColumn
		
//		Cell<Boolean> bulkExportCheckbox = new MatCheckBoxCell();
//		Column<Result, Boolean> bulkExportColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(bulkExportCheckbox) {
//			@Override
//			public Boolean getValue(ManageMeasureSearchModel.Result object) {
//				return object.isExportable();
//			}
//			
//			@Override
//			public void render(Cell.Context context,
//					ManageMeasureSearchModel.Result object, SafeHtmlBuilder sb) {
//				if (object.isExportable()) {
//					super.render(context, object, sb);
//				}
//			}
//		};
//		bulkExportColumn.setFieldUpdater(new FieldUpdater<ManageMeasureSearchModel.Result, Boolean>() {
//			@Override
//			public void update(int index, ManageMeasureSearchModel.Result object, Boolean value) {
//				object.setExportable(value);
//				//observer.onExportSelectedClicked(checkBox);
//			}
//		});
//		table.addColumn(bulkExportColumn, SafeHtmlUtils.fromSafeConstant("<span title='Check for Bulk Export'>"
//				+ "Bulk Export </span>"));
		
		Cell<Boolean> bulkExportCheckbox = new MatCheckBoxCell();
		Column<Result, Boolean> bulkExportColumn = new Column<ManageMeasureSearchModel.Result, Boolean>(bulkExportCheckbox) {
			@Override
			public Boolean getValue(ManageMeasureSearchModel.Result object) {
				return !object.isExportable();
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
				observer.onExportSelectedClicked(object);
				//observer.onTransferSelectedClicked(object);
			}
		});
		table.addColumn(bulkExportColumn, SafeHtmlUtils.fromSafeConstant("<span title='Check for Bulk Export'>"
				+ "Bulk Export </span>"));

		return table;
	}
	

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		Widget value = null;
		switch(column) {
		case 0:
			value = new Label(data.get(row).getName());
			break;
		case 1:
			value = new Label(data.get(row).getVersion());
			break;
		case 2:
			Timestamp ts = data.get(row).getFinalizedDate();
			String text = ts == null ? "" : convertTimestampToString(ts);
			value = new Label(text);
			break;
		case 3:
			value = new Label(data.get(row).getStatus());
			break;
		case 4: 
			value = getImage("history", ImageResources.INSTANCE.clock(),data.get(row).getId());
			break;
		case 5:
			String emailAddress = " ";
			if(data.get(row).getLockedUserInfo() != null){
				emailAddress = data.get(row).getLockedUserInfo().getEmailAddress();
			}
			/*
			 * If the measure has been locked by some other user, then display the locked image else check for the user role, if he is top-level user 
			 * display edit image, and display "Read-Only" image if the user is a Normal User and has got view-only mode.
			*/
			if(data.get(row).isEditable()){
				if(data.get(row).isMeasureLocked()){
					value = getImageReadOnly("Measure in use by "+ emailAddress, ImageResources.INSTANCE.g_lock(), data.get(row).getId());
				}
				else
					value = getImage("edit", ImageResources.INSTANCE.g_package_edit(), data.get(row).getId());
				
			}else{
				    value = getImageReadOnly("Read-Only", ImageResources.INSTANCE.ReadOnly(), data.get(row).getId());
			}
			break;
		case 6:
			if(data.get(row).isSharable())
				value = getImage("share", ImageResources.INSTANCE.g_package_share(), data.get(row).getId());
			
			break;
		case 7:
			if(data.get(row).isClonable())
				value = getImage("clone", ImageResources.INSTANCE.g_page_copy(), data.get(row).getId());
			break;
		case 8:
			if(data.get(row).isExportable()){
//				value = getImage("export", ImageResources.INSTANCE.g_package_go(), data.get(row).getId());
				value = getImageAndCheckBox("export", ImageResources.INSTANCE.g_package_go(), data.get(row).getId(),data.get(row).getName());
			}
			break;
		default: 
			value = new Label();
		}
		return value;
	}
	
	/**
	 * Gets the image.
	 * 
	 * @param action
	 *            the action
	 * @param url
	 *            the url
	 * @param key
	 *            the key
	 * @return the image
	 */
	private Widget getImage(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		holder.getElement().getStyle().setCursor(Cursor.POINTER);
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		//508 fix - Read only and locked icons do not do anything but they appear to show hand pointer on mouse hover.
		if(!action.equalsIgnoreCase("Read-Only") && !(action.contains("Measure in use")))
			addListener(image);
		else
			image.setEnabled(false);
		holder.add(image);
		return holder;
	}
	
	/**
	 * Gets the image read only.
	 * 
	 * @param action
	 *            the action
	 * @param url
	 *            the url
	 * @param key
	 *            the key
	 * @return the image read only
	 */
	private Widget getImageReadOnly(String action, ImageResource url, String key) {
		SimplePanel holder = new SimplePanel();
		holder.setStyleName("searchTableCenteredHolder");
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("readOnlyButton");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		//508 fix - Read only and locked icons do not do anything but they appear to show hand pointer on mouse hover.
		if(!action.equalsIgnoreCase("Read-Only") && !(action.contains("Measure in use")))
			addListener(image);
		else
			image.setEnabled(false);
		holder.add(image);
		return holder;
	}
	
	
	/**
	 * Gets the image and check box.
	 * 
	 * @param action
	 *            the action
	 * @param url
	 *            the url
	 * @param key
	 *            the key
	 * @param name
	 *            the name
	 * @return the image and check box
	 */
	private Widget getImageAndCheckBox(String action, ImageResource url, String key,String name){
		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.getElement().setId("hPanel_HorizontalPanel");
		hPanel.setStyleName("exportCheckBox");
		CustomButton image = new CustomButton();
		image.getElement().setId("image_CustomButton");
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		addListener(image);
		hPanel.add(image);
				
		CustomCheckBox checkBox = new CustomCheckBox("Select Record to Export", false);	
		checkBox.getElement().setId("checkBox_CustomcheckBox");
		checkBox.setStyleName("centerAligned");
		checkBox.setFormValue(key);
		hPanel.add(checkBox);
		checkBox.getElement().setId("bulkExport_" + key);
		checkBox.addClickHandler(clickHandler);
		return hPanel;
	}
	
	
	/**
	 * Adds the listener.
	 * 
	 * @param image
	 *            the image
	 */
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	
	/**
	 * Sets the image style.
	 * 
	 * @param image
	 *            the new image style
	 */
	private void setImageStyle(FocusableImageButton image) {
		image.setStylePrimaryName("measureSearchResultIcon");
	}
	
	/**
	 * Sets the id.
	 * 
	 * @param image
	 *            the image
	 * @param action
	 *            the action
	 * @param key
	 *            the key
	 */
	private void setId(CustomButton image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}
	
	/**
	 * Builds the click handler.
	 * 
	 * @return the click handler
	 */
	private ClickHandler buildClickHandler() {
		return new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String id = ((Widget)event.getSource()).getElement().getId();
				int index = id.indexOf('_');
				String action = id.substring(0, index);
				String key = id.substring(index + 1);
				ManageMeasureSearchModel.Result result = getResultForId(key);
				if(observer != null) {
					if("edit".equals(action)) {
						observer.onEditClicked(result);
					}
					else if("share".equals(action)) {
						observer.onShareClicked(result);
					}
					else if("clone".equals(action)) {
						observer.onCloneClicked(result);
					}
					else if("export".equals(action)) {
						observer.onExportClicked(result);
					}
					else if("history".equals(action)){
						observer.onHistoryClicked(result);
					}else if("bulkExport".equals(action)){
						observer.onExportSelectedClicked((CustomCheckBox)event.getSource());
					}
				}
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return data.getStartIndex();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return data.getResultsTotal();
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	@Override
	public ManageMeasureSearchModel.Result get(int row) {
		return data.get(row);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	/**
	 * TODO make use of a utility class ex. DateUtility.java though it must be
	 * usable on the client side currently it is not usable on the client side
	 * 
	 * @param ts
	 *            the ts
	 * @return the string
	 */
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

	public ManageMeasureSearchModel getData() {
		return data;
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

	
	//TODO
	
	
	
}
