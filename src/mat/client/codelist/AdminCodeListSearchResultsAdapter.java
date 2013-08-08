package mat.client.codelist;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import mat.client.ImageResources;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.metadata.CustomCheckBox;
import mat.client.shared.CustomButton;
import mat.client.shared.FocusableImageButton;
import mat.client.shared.MatButtonCell;
import mat.client.shared.MatCheckBoxCell;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class AdminCodeListSearchResultsAdapter implements SearchResults<CodeListSearchDTO>{

	public static interface Observer {
		public void onHistoryClicked(CodeListSearchDTO result);
		public void onTransferSelectedClicked(CodeListSearchDTO result);
	}
		
	private ManageCodeListSearchModel model = new AdminManageCodeListSearchModel();
	private Observer observer;
	private ClickHandler clickHandler = buildClickHandler();
	private boolean isHistoryClicked;
	private List<CodeListSearchDTO> lastSelectedCodeList;
	//private List<CodeListSearchDTO> data;
	
	private List<CodeListSearchDTO> selectedCodeList;
	
	public List<CodeListSearchDTO> getSelectedCodeList() {
		return selectedCodeList;
	}

	public List<CodeListSearchDTO> getLastSelectedCodeList() {
		return lastSelectedCodeList;
	}

	public void setLastSelectedCodeList(List<CodeListSearchDTO> lastSelectedCodeList) {
		this.lastSelectedCodeList = lastSelectedCodeList;
	}

	public void setSelectedCodeList(List<CodeListSearchDTO> selectedCodeList) {
		this.selectedCodeList = selectedCodeList;
	}
	
		
	/*public List<CodeListSearchDTO> getData() {
		return data;
	}

	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
	}
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
	
	public void setObserver(Observer observer) {
		this.observer = observer;
	}
	public void setData(ManageCodeListSearchModel model) {
		this.model = model;
	}
		
	
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
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return model.isColumnSortable(columnIndex);
	}


	@Override
	public int getNumberOfColumns() {
		return model.getNumberOfColumns();
	}

	@Override
	public int getNumberOfRows() {
		return model.getNumberOfRows();
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return model.getColumnHeader(columnIndex);
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return model.getColumnWidth(columnIndex);
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return model.isColumnFiresSelection(columnIndex);
	}

	@Override
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
	}
	
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
	
	private CustomCheckBox getTransferCheckBox(String key){
		
		CustomCheckBox transFerCheckBox = new CustomCheckBox("Transfer", false);	
		//transFerCheckBox.setFormValue(key);
		transFerCheckBox.getElement().setId("Transfer_" + key);
		transFerCheckBox.setTitle("Select Value Set to Transfer Ownership.");
		transFerCheckBox.addClickHandler(clickHandler);
		return transFerCheckBox;
		
	}
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
	
	

	private SafeHtml getColumnToolTip(String columnText, StringBuilder title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='"+ columnText + ", " + title + "'>"+columnText+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	private SafeHtml getColumnToolTip(String title){
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='" + title + "'>"+title+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	@SuppressWarnings("unchecked")
	public CellTable<CodeListSearchDTO> addColumnToTable(final CellTable<CodeListSearchDTO> table){
		
		final MultiSelectionModel<CodeListSearchDTO> selectionModel = addSelectionHandlerOnTable();
		
		if(table.getColumnCount() !=6 ){	
			
			Column< CodeListSearchDTO , SafeHtml> nameColumn;
				nameColumn = new Column< CodeListSearchDTO  , SafeHtml>(new SafeHtmlCell()) {
					
					@Override
					public SafeHtml getValue( CodeListSearchDTO   object ) {
						
						StringBuilder title = new StringBuilder();
						title =title.append("OID : ").append(object.getOid());
						return getColumnToolTip(object.getName(), title);
					}
				};
			
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Value Set Name'>" +"Value Set Name"+ "</span>"));
			
			Column<CodeListSearchDTO , SafeHtml> ownerName = new Column<CodeListSearchDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getOwnerFirstName() + "  " + object.getOwnerLastName());
				}
			};
			table.addColumn(ownerName, SafeHtmlUtils.fromSafeConstant("<span title='Owner'>" +"Owner"+ "</span>"));
			
			Column<CodeListSearchDTO , SafeHtml> ownerEmailAddress = new Column<CodeListSearchDTO , SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getOwnerEmailAddress());
				}
			};
			table.addColumn(ownerEmailAddress, SafeHtmlUtils.fromSafeConstant("<span title='Owner E-mail Address'>" +"Owner E-mail Address"+ "</span>"));
						
			Column<CodeListSearchDTO, SafeHtml> codeSystem = new Column<CodeListSearchDTO, SafeHtml>(new SafeHtmlCell()) {
				@Override
				public SafeHtml getValue(CodeListSearchDTO object) {
					return getColumnToolTip(object.getCodeSystem());
				}
			};
			table.addColumn(codeSystem, SafeHtmlUtils.fromSafeConstant("<span title='Code System'>" +"Code System"+ "</span>"));
									
			Cell<String> historyButton = new MatButtonCell("Click to view history");
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
			table.addColumn(historyColumn , SafeHtmlUtils.fromSafeConstant("<span title='History'>" +"History"+ "</span>"));
					
			
			Cell<Boolean> transferCB = new MatCheckBoxCell();
			Column transferColumn = new Column<CodeListSearchDTO, Boolean>(transferCB) {
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
								
			table.addColumn(transferColumn , SafeHtmlUtils.fromSafeConstant("<span title='Check for Ownership Transfer'>" +"Transfer"+ "</span>"));
			table.setColumnWidth(0, 30.0, Unit.PCT);
			table.setColumnWidth(1, 20.0, Unit.PCT);
			table.setColumnWidth(2, 20.0, Unit.PCT);
			table.setColumnWidth(3, 20.0, Unit.PCT);
			table.setColumnWidth(4, 5.0, Unit.PCT);
			table.setColumnWidth(5, 5.0, Unit.PCT);
			/*table.addCellPreviewHandler(new CellPreviewEvent.Handler<CodeListSearchDTO>() {
				@Override
				public void onCellPreview(CellPreviewEvent event){
					handleSelectionEvent(event, selectionModel);
				}
			});*/
			
			
		}
		return table;
	}
	
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
	
	
	
	private void addListener(CustomButton image) {
		image.addClickHandler(clickHandler);
	}
	
	private void setId(CustomButton image, String action, String key) {
		String id = action + "_" + key;
		image.getElement().setAttribute("id", id);
	}

	@Override
	public int getStartIndex() {
		return model.getStartIndex();
	}

	@Override
	public int getResultsTotal() {
		return model.getResultsTotal();
	}

	@Override
	public String getKey(int row) {
		return model.get(row).getId();
	}


	@Override
	public CodeListSearchDTO get(int row) {
		return model.get(row);
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return model.isColumnSelectAll(columnIndex);
	}

	public boolean isHistoryClicked() {
		return isHistoryClicked;
	}

	public void setHistoryClicked(boolean isHistoryClicked) {
		this.isHistoryClicked = isHistoryClicked;
	}

	public ManageCodeListSearchModel getModel() {
		return model;
	}
	
	

}
