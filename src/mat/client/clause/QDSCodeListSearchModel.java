package mat.client.clause;

import java.util.HashMap;
import java.util.List;

import mat.client.shared.RadioButtonCell;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;
import mat.model.QualityDataSetDTO;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class QDSCodeListSearchModel implements SearchResults<CodeListSearchDTO>,IsSerializable {

	private static String[] headers = new String[] {"Value Set","Category","Code System","Steward"};
	private static String[] widths = new String[] {"50%","40%","10%"};
	
	private HashMap<CodeListSearchDTO, RadioButton> radioButtonMap = new HashMap<CodeListSearchDTO, RadioButton>();
	private List<CodeListSearchDTO> data;
	//private List<QualityDataSetDTO> appliedQDMs;
	private int startIndex;
	private int resultsTotal;
	private boolean editable;
	
	private CodeListSearchDTO lastSelectedCodeList;
	
	private CodeListSearchDTO selectedCodeList;
	
	public CodeListSearchDTO getSelectedCodeList() {
		return selectedCodeList;
	}

	public CodeListSearchDTO getLastSelectedCodeList() {
		return lastSelectedCodeList;
	}

	public void setLastSelectedCodeList(CodeListSearchDTO lastSelectedCodeList) {
		this.lastSelectedCodeList = lastSelectedCodeList;
	}

	public void setSelectedCodeList(CodeListSearchDTO selectedCodeList) {
		this.selectedCodeList = selectedCodeList;
	}
	
	public List<CodeListSearchDTO> getData() {
		return data;
	}

	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
		this.editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		/*for(final CodeListSearchDTO codeList : data) {
			RadioButton rb = new RadioButton("codeListgroup","");
			rb.setText(codeList.getName());
			rb.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					MatContext.get().clearDVIMessages();
					setLastSelectedCodeList(codeList);
					MatContext.get().getEventBus().fireEvent( new OnChangeOptionsEvent());
				}
			});
			radioButtonMap.put(codeList, rb);
		}*/
	}
	
	private SafeHtml getColumnToolTip(String columnText, StringBuilder title) {
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='"+title + "'>"+columnText+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	public CellTable<CodeListSearchDTO> addColumnToTable(final CellTable<CodeListSearchDTO> table){
		
		if(table.getColumnCount() !=5){	
			
			Column<CodeListSearchDTO, Boolean> radioButtonColumn = new Column<CodeListSearchDTO, Boolean>(new RadioButtonCell(true,true)) {  
				public Boolean getValue(CodeListSearchDTO CodeListSearchDTO) {  
					return table.getSelectionModel().isSelected(CodeListSearchDTO);  
				}  
			};  
			radioButtonColumn.setFieldUpdater(new FieldUpdater<CodeListSearchDTO, Boolean>() {
				@Override
				public void update(int index, CodeListSearchDTO object, Boolean value) {
					table.getSelectionModel().setSelected(object, true); 
				}  
			});  
			table.addColumn(radioButtonColumn);  
			
			 Column< CodeListSearchDTO , SafeHtml> nameColumn;
			nameColumn = new Column< CodeListSearchDTO  , SafeHtml>(new SafeHtmlCell()) {
				
				@Override
				public SafeHtml getValue( CodeListSearchDTO   object ) {
					
					StringBuilder title = new StringBuilder();
					title =title.append("OID : ").append(object.getOid());
					return getColumnToolTip(object.getName(), title);
				}
			};
		
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Value Sets'>"+headers[0]+"</span>"));
			
			TextColumn<CodeListSearchDTO > category = new TextColumn<CodeListSearchDTO >() {
				@Override
				public String getValue(CodeListSearchDTO object) {
					return object.getCategoryDisplay();
				}
			};
			table.addColumn(category, SafeHtmlUtils.fromSafeConstant("<span title='Category'>"+headers[1]+"</span>"));
			TextColumn<CodeListSearchDTO > codeSystem = new TextColumn<CodeListSearchDTO >() {
				@Override
				public String getValue(CodeListSearchDTO object) {
					return object.getCodeSystem();
				}
			};
			table.addColumn(codeSystem, SafeHtmlUtils.fromSafeConstant("<span title='Code System'>"+headers[2]+"</span>"));
			
			TextColumn<CodeListSearchDTO > stewardCol = new TextColumn<CodeListSearchDTO >() {
				@Override
				public String getValue(CodeListSearchDTO object) {
					String steward = object.getSteward();
					if(steward.equalsIgnoreCase("Other")){
						steward = object.getStewardOthers();
					}
					return steward;
				}
			};
			table.addColumn(stewardCol, SafeHtmlUtils.fromSafeConstant("<span title='Steward'>"+headers[3]+"</span>"));
		}
		
		return table;
		
	}
	
	public SingleSelectionModel<CodeListSearchDTO> addSelectionHandlerOnTable(){
		final SingleSelectionModel<CodeListSearchDTO> selectionModel = new  SingleSelectionModel<CodeListSearchDTO>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				CodeListSearchDTO codeListObject = selectionModel.getSelectedObject();
				if(codeListObject !=null){
					//lastSelectedCodeList = codeListObject;
					MatContext.get().clearDVIMessages();
					setLastSelectedCodeList(codeListObject);
					setSelectedCodeList(codeListObject);
					MatContext.get().getEventBus().fireEvent( new OnChangeOptionsEvent());

				}
			}
		});
		return selectionModel;
	}
	
	
	@Override
	public boolean isColumnSortable(int columnIndex) {
		return false;
	}


	@Override
	public int getNumberOfColumns() {
		return headers.length;
	}

	@Override
	public int getNumberOfRows() {
		return data != null ? data.size() : 0;
	}

	@Override
	public String getColumnHeader(int columnIndex) {
		return headers[columnIndex];
	}

	@Override
	public String getColumnWidth(int columnIndex) {
		return widths[columnIndex];
	}

	@Override
	public Widget getValue(int row, int column) {
		return getValueImpl(get(row), column);
	}

	
	public Widget getValueImpl(CodeListSearchDTO codeList, int column) {
			Widget value;
		switch(column) {
		case 0:
			if(!editable){
				RadioButton r = radioButtonMap.get(codeList);
				String rbLabel = r.getText();
				if(rbLabel.length() > 25){
					rbLabel = rbLabel.substring(0,25);
					StringBuffer rbLbl = new StringBuffer();
					rbLbl = rbLbl.append(rbLabel).append("...");
					r.setText(rbLbl.toString());
				}
				
				r.setEnabled(editable);
				value = r;
			}else{
				RadioButton r = radioButtonMap.get(codeList);
				String rbLabel = r.getText();
				if(rbLabel.length() > 25){
					rbLabel = rbLabel.substring(0,25);
					StringBuffer rbLbl = new StringBuffer();
					rbLbl = rbLbl.append(rbLabel).append("...");
					r.setText(rbLbl.toString());
				}
				
				value = r;
			}
			
			break;
		case 1:
			String labelValue= codeList.getCategoryDisplay();
			/*if(labelValue.length() >20){
				labelValue = labelValue.substring(0, 20);
				StringBuffer lblValue = new StringBuffer(labelValue);
				labelValue = lblValue.append("...").toString();
			}*/
			value = new Label(labelValue);
			value.setTitle(codeList.getCategoryDisplay());
			break;
		case 2:
			value = new Label(codeList.getCodeSystem());
			value.setTitle(codeList.getCodeSystem());
			break;
		default:
			value = new Label("Unknown Column Index");
		}
		return value;
	}
	
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	

	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
	}


	@Override
	public String getKey(int row) {
		return data.get(row).getId();
	}

	@Override
	public CodeListSearchDTO get(int row) {
		return data.get(row);
	}

	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 1;
	}
	
	

	public boolean isEditable() {
		return editable;
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}

	/**
	 * @param appliedQDMs the appliedQDMs to set
	 *//*
	public void setAppliedQDMs(List<QualityDataSetDTO> appliedQDMs) {
		this.appliedQDMs = appliedQDMs;
	}

	*//**
	 * @return the appliedQDMs
	 *//*
	public List<QualityDataSetDTO> getAppliedQDMs() {
		return appliedQDMs;
	}*/
	
}
