package mat.client.codelist;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

import mat.client.ImageResources;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.CustomButton;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.MatSafeHTMLCell;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;

/**
 * The Class AdminCodeListSearchResultsAdapter.
 */
public class AdminCodeListSearchResultsAdapter /*implements SearchResults<CodeListSearchDTO>*/{

	/**
	 * The Interface Observer.
	 */
	public static interface Observer {
		
		/**
		 * On history clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onHistoryClicked(CodeListSearchDTO result);
		
		/**
		 * On transfer selected clicked.
		 * 
		 * @param result
		 *            the result
		 */
		public void onTransferSelectedClicked(CodeListSearchDTO result);
	}
		
	/** The model. */
	private ManageCodeListSearchModel model = new AdminManageCodeListSearchModel();
	
	/** The observer. */
	private Observer observer;
	
	/** The click handler. */
	private ClickHandler clickHandler = buildClickHandler();
	
	/** The is history clicked. */
	private boolean isHistoryClicked;
	
	/** The last selected code list. */
	private List<CodeListSearchDTO> lastSelectedCodeList;
	
	/** The selected code list. */
	private List<CodeListSearchDTO> selectedCodeList;
	
	/**
	 * Gets the selected code list.
	 * 
	 * @return the selected code list
	 */
	public List<CodeListSearchDTO> getSelectedCodeList() {
		return selectedCodeList;
	}

	/**
	 * Gets the last selected code list.
	 * 
	 * @return the last selected code list
	 */
	public List<CodeListSearchDTO> getLastSelectedCodeList() {
		return lastSelectedCodeList;
	}

	/**
	 * Sets the last selected code list.
	 * 
	 * @param lastSelectedCodeList
	 *            the new last selected code list
	 */
	public void setLastSelectedCodeList(List<CodeListSearchDTO> lastSelectedCodeList) {
		this.lastSelectedCodeList = lastSelectedCodeList;
	}

	/**
	 * Sets the selected code list.
	 * 
	 * @param selectedCodeList
	 *            the new selected code list
	 */
	public void setSelectedCodeList(List<CodeListSearchDTO> selectedCodeList) {
		this.selectedCodeList = selectedCodeList;
	}
	
	/**
	 * Gets the result for id.
	 * 
	 * @param id
	 *            the id
	 * @return the result for id
	 */
	private CodeListSearchDTO getResultForId(String id) {
		for(int i = 0; i < model.getNumberOfRows(); i++) {			
			if(id.equals(model.getKey(i))) {
				return model.get(i);
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
	 * @param model
	 *            the new data
	 */
	public void setData(ManageCodeListSearchModel model) {
		this.model = model;
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
				CodeListSearchDTO result = getResultForId(key);

				if(observer != null) {
					if("history".equals(action)){
						observer.onHistoryClicked(result);
					}else if("transfer".equalsIgnoreCase(action)){
						CustomCheckBox transferCB = (CustomCheckBox)event.getSource();
						result.setTransferable((transferCB.getValue()));
						observer.onTransferSelectedClicked(result);
					}
				}
			}
		};
	}
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSortable(int)
	 */
	/*@Override
	public boolean isColumnSortable(int columnIndex) {
		return model.isColumnSortable(columnIndex);
	}*/


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfColumns()
	 */
	/*@Override
	public int getNumberOfColumns() {
		return model.getNumberOfColumns();
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getNumberOfRows()
	 */
	/*@Override
	public int getNumberOfRows() {
		return model.getNumberOfRows();
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnHeader(int)
	 */
	/*@Override
	public String getColumnHeader(int columnIndex) {
		return model.getColumnHeader(columnIndex);
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getColumnWidth(int)
	 */
	/*@Override
	public String getColumnWidth(int columnIndex) {
		return model.getColumnWidth(columnIndex);
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	/*@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return model.isColumnFiresSelection(columnIndex);
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	/*@Override
	public Widget getValue(int row, int column) {
		Widget value;
		
		switch(column) {
		case 0:
			value = new Label(model.get(row).getName());
			break;
		case 1:
			value = new Label(model.get(row).getLastModified());
			break;
		case 2:
			value = new Label(model.getData().get(row).getSteward());
			break;
		case 3:
			value = new Label(model.getData().get(row).getCategoryDisplay());
			break;
		case 4:
			value=new Label(model.getData().get(row).getCodeSystem());
			break;
		case 5:
			value = getImage("history", ImageResources.INSTANCE.clock(),model.getData().get(row).getId());
			break;
		case 6:
			if(model.get(row).isTransferable()){
				value = getTransferCheckBox(model.get(row).getId());
				value.addStyleName("searchTableCenteredHolder");
			}else
				value = new Label();
			break;	    
		default: 
			value = new Label("Unknown Column Index");
		}
		return value;
	}*/
	
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
		CustomButton image = new CustomButton();
		image.removeStyleName("gwt-button");
		image.setStylePrimaryName("invisibleButtonText");
		image.setTitle(action);
		image.setResource(url,action);
		setId(image, action, key);
		addListener(image);
		holder.add(image);
		return holder;
	}
	
	/**
	 * Gets the transfer check box.
	 * 
	 * @param key
	 *            the key
	 * @return the transfer check box
	 */
	private CustomCheckBox getTransferCheckBox(String key){
		
		CustomCheckBox transFerCheckBox = new CustomCheckBox("Transfer", false);	
		//transFerCheckBox.setFormValue(key);
		transFerCheckBox.getElement().setId("Transfer_" + key);
		transFerCheckBox.setTitle("Select Value Set to Transfer Ownership.");
		transFerCheckBox.addClickHandler(clickHandler);
		return transFerCheckBox;
		
	}
	
	/**
	 * Adds the selection handler on table.
	 * 
	 * @return the multi selection model
	 */
	public MultiSelectionModel<CodeListSearchDTO> addSelectionHandlerOnTable(){
		final MultiSelectionModel<CodeListSearchDTO> selectionModel = new  MultiSelectionModel<CodeListSearchDTO>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Set<CodeListSearchDTO> codeListSet = selectionModel.getSelectedSet();
				if(codeListSet !=null){
					//lastSelectedCodeList = codeListObject;
					MatContext.get().clearDVIMessages();
					MatContext.get().clearModifyPopUpMessages();
					//List<CodeListSearchDTO> selectedCodeList = new ArrayList<CodeListSearchDTO>(codeListSet);
					//setLastSelectedCodeList(selectedCodeList);
					//setSelectedCodeList(selectedCodeList);
					MatContext.get().getEventBus().fireEvent( new OnChangeOptionsEvent());

				}
			}
		});
		return selectionModel;
	}
	
	

	/**
	 * Gets the column tool tip.
	 * 
	 * @param columnText
	 *            the column text
	 * @param title
	 *            the title
	 * @return the column tool tip
	 */
	private SafeHtml getColumnToolTip(String columnText, StringBuilder title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='"+ columnText + ", " + title + "'>"+columnText+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	/**
	 * Gets the column tool tip.
	 * 
	 * @param title
	 *            the title
	 * @return the column tool tip
	 */
	private SafeHtml getColumnToolTip(String title){
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>"+title+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	/**
	 * Adds the column to table.
	 * 
	 * @param table
	 *            the table
	 * @param sortHandler
	 *            the sort handler
	 * @return the cell table
	 */
	public CellTable<CodeListSearchDTO> addColumnToTable(final CellTable<CodeListSearchDTO> table, ListHandler<CodeListSearchDTO> sortHandler){
		
		if(table.getColumnCount() !=6 ){	
			
			Column< CodeListSearchDTO , SafeHtml> nameColumn;
				nameColumn = new Column< CodeListSearchDTO  , SafeHtml>(new MatSafeHTMLCell()) {
					
					@Override
					public SafeHtml getValue( CodeListSearchDTO   object ) {
						
						StringBuilder title = new StringBuilder();
						title =title.append("OID : ").append(object.getOid());
						return getColumnToolTip(object.getName(), title);
					}
				};
			nameColumn.setSortable(true);
			sortHandler.setComparator(nameColumn,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO o1, CodeListSearchDTO o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getName().compareTo(o2.getName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Value Set Name' tabindex=\"0\">" +"Value Set Name"+ "</span>"));
			
			Column<CodeListSearchDTO , SafeHtml> ownerName = new Column<CodeListSearchDTO, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getOwnerFirstName() + "  " + object.getOwnerLastName());
				}
			};
			ownerName.setSortable(true);
			sortHandler.setComparator(ownerName,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO o1, CodeListSearchDTO o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerFirstName().compareTo(o2.getOwnerFirstName()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title='Owner' tabindex=\"0\">" +"Owner"+ "</span>"));
			
			Column<CodeListSearchDTO , SafeHtml> ownerEmailAddress = new Column<CodeListSearchDTO , SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getOwnerEmailAddress());
				}
			};
			ownerEmailAddress.setSortable(true);
			sortHandler.setComparator(ownerEmailAddress,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO o1, CodeListSearchDTO o2) {
					if (o1 == o2) {
						return 0;
					}

					// Compare the name columns.
					if (o1 != null) {
						return (o2 != null) ? o1.getOwnerEmailAddress().compareTo(o2.getOwnerEmailAddress()) : 1;
					}
					return -1;
				}
			});
			table.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title='E-mail Address' tabindex=\"0\">" +"Owner E-mail Address"+ "</span>"));
						
			Column<CodeListSearchDTO, SafeHtml> codeSystem = new Column<CodeListSearchDTO, SafeHtml>(new MatSafeHTMLCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getCodeSystem());
				}
			};
			table.addColumn(codeSystem, SafeHtmlUtils.fromSafeConstant("<span title='Code System' tabindex=\"0\">" +"Code System"+ "</span>"));
									
			Cell<String> historyButton = new MatButtonCell("Click to view history","customClockButton");
			Column<CodeListSearchDTO, String> historyColumn = new Column<CodeListSearchDTO, String>(historyButton) {
				@Override
				public String getValue(CodeListSearchDTO object) {
					return "History";
				 }	
				};
			
			historyColumn.setFieldUpdater(new FieldUpdater<CodeListSearchDTO, String>() {
				  @Override
				  public void update(int index, CodeListSearchDTO object, String value) {
					  observer.onHistoryClicked(object);
				  }
				});
			table.addColumn(historyColumn , SafeHtmlUtils.fromSafeConstant("<span title='History' tabindex=\"0\">" +"History"+ "</span>"));
					
			
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column<CodeListSearchDTO, Boolean> transferColumn = new Column<CodeListSearchDTO, Boolean>(transferCB) {
			  @Override
			  public Boolean getValue(CodeListSearchDTO object) {
			    return object.isTransferable();
			  }
			};
			
			transferColumn.setFieldUpdater(new FieldUpdater<CodeListSearchDTO, Boolean>() {
				  @Override
				  public void update(int index, CodeListSearchDTO object, Boolean value) {
					  object.setTransferable(value);
					  observer.onTransferSelectedClicked(object);
				  }
				});
			table.addColumn(transferColumn , SafeHtmlUtils.fromSafeConstant("<span title='Check for Ownership Transfer' tabindex=\"0\">" +"Transfer"+ "</span>"));
			table.setColumnWidth(0, 30.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 24.0, Unit.PCT);
			table.setColumnWidth(3, 20.0, Unit.PCT);
			table.setColumnWidth(4, 3.0, Unit.PCT);
			table.setColumnWidth(5, 3.0, Unit.PCT);
			table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
			
		}
		return table;
	}
	
	/**
	 * Handle selection event.
	 * 
	 * @param <T>
	 *            the generic type
	 * @param event
	 *            the event
	 * @param selectionModel
	 *            the selection model
	 */
	protected <T> void handleSelectionEvent(CellPreviewEvent<T> event,
			final MultiSelectionModel<? super T> selectionModel) {
		T value = event.getValue();
		NativeEvent nativeEvent = event.getNativeEvent();
		String type = nativeEvent.getType();
		System.out.println("type="+type);
		if ("click".equals(type)) {
			if (nativeEvent.getCtrlKey() || nativeEvent.getMetaKey()) {
				System.out.println("inside the ctrl +click event");
				selectionModel.setSelected(value, !selectionModel.isSelected(value));
			} else {
				System.out.println("inside the click event + onclic");
				selectionModel.setSelected(value, true);
			}
		} else if (("keyup".equals(type))||("focus".equals(type))||("keydown".equals(type))) {
			if("focus".equals(type)){
				System.out.println("inside the focus");
				return;
			}
			int keyCode = nativeEvent.getKeyCode();
			System.out.println("keycode=="+keyCode);
			if (keyCode == 9) {
				System.out.println("inside the tab event");
				selectionModel.setSelected(value, true);
			}
		}
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

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	/*@Override
	public int getStartIndex() {
		return model.getStartIndex();
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	/*@Override
	public int getResultsTotal() {
		return model.getResultsTotal();
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getKey(int)
	 */
	/*@Override
	public String getKey(int row) {
		return model.get(row).getId();
	}*/


	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#get(int)
	 */
	/*@Override
	public CodeListSearchDTO get(int row) {
		return model.get(row);
	}*/

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
	/*@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return model.isColumnSelectAll(columnIndex);
	}*/

	/**
	 * Checks if is history clicked.
	 * 
	 * @return true, if is history clicked
	 */
	public boolean isHistoryClicked() {
		return isHistoryClicked;
	}

	/**
	 * Sets the history clicked.
	 * 
	 * @param isHistoryClicked
	 *            the new history clicked
	 */
	public void setHistoryClicked(boolean isHistoryClicked) {
		this.isHistoryClicked = isHistoryClicked;
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public ManageCodeListSearchModel getModel() {
		return model;
	}
	
	

}
