package mat.client.clause;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.shared.MatContext;
import mat.client.shared.RadioButtonCell;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class QDSCodeListSearchModel.
 */
public class QDSCodeListSearchModel implements SearchResults<CodeListSearchDTO>,IsSerializable {

	/** The headers. */
	private static String[] headers = new String[] {"Value Set","Category","Code System","Steward"};
	
	/** The widths. */
	private static String[] widths = new String[] {"50%","40%","10%"};
	
	/** The radio button map. */
	private Map<CodeListSearchDTO, RadioButton> radioButtonMap = new HashMap<CodeListSearchDTO, RadioButton>();
	
	/** The data. */
	private List<CodeListSearchDTO> data;
	//private List<QualityDataSetDTO> appliedQDMs;
	/** The start index. */
	private int startIndex;
	
	/** The results total. */
	private int resultsTotal;
	
	/** The editable. */
	private boolean editable;
	
	/** The last selected code list. */
	private CodeListSearchDTO lastSelectedCodeList;
	
	/** The selected code list. */
	private CodeListSearchDTO selectedCodeList;
	
	/**
	 * Gets the selected code list.
	 * 
	 * @return the selected code list
	 */
	public CodeListSearchDTO getSelectedCodeList() {
		return selectedCodeList;
	}

	/**
	 * Gets the last selected code list.
	 * 
	 * @return the last selected code list
	 */
	public CodeListSearchDTO getLastSelectedCodeList() {
		return lastSelectedCodeList;
	}

	/**
	 * Sets the last selected code list.
	 * 
	 * @param lastSelectedCodeList
	 *            the new last selected code list
	 */
	public void setLastSelectedCodeList(CodeListSearchDTO lastSelectedCodeList) {
		this.lastSelectedCodeList = lastSelectedCodeList;
	}

	/**
	 * Sets the selected code list.
	 * 
	 * @param selectedCodeList
	 *            the new selected code list
	 */
	public void setSelectedCodeList(CodeListSearchDTO selectedCodeList) {
		this.selectedCodeList = selectedCodeList;
	}
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<CodeListSearchDTO> getData() {
		return data;
	}

	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
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
		String htmlConstant = "<html>" + "<head> </head> <Body><span title='"+title + "'>"+columnText+ "</span></body>" + "</html>";
		return new SafeHtmlBuilder().appendHtmlConstant(htmlConstant).toSafeHtml();
	}
	
	/**
	 * Adds the column to table.
	 * 
	 * @param table
	 *            the table
	 * @param isTableEnabled
	 *            the is table enabled
	 * @param sortHandler
	 *            the sort handler
	 * @return the cell table
	 */
	public CellTable<CodeListSearchDTO> addColumnToTable(final CellTable<CodeListSearchDTO> table,boolean isTableEnabled,ListHandler<CodeListSearchDTO> sortHandler){
		
		if(table.getColumnCount() !=5){	
			
			Column<CodeListSearchDTO, Boolean> radioButtonColumn = new Column<CodeListSearchDTO, Boolean>(new RadioButtonCell(true,true,isTableEnabled)) {  
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
			nameColumn.setSortable(true);
			sortHandler.setComparator(nameColumn,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO codeListSearchDTO1, CodeListSearchDTO codeListSearchDTO2) {
					if (codeListSearchDTO1 == codeListSearchDTO2) {
						return 0;
					}

					// Compare the name columns.
					if (codeListSearchDTO1 != null) {
						return (codeListSearchDTO2 != null) ? codeListSearchDTO1.getName().compareTo(codeListSearchDTO2.getName()) : 1;
					}
					return -1;
				}
			});
			
			table.addColumn(nameColumn, SafeHtmlUtils.fromSafeConstant("<span title='Value Sets'>"+headers[0]+"</span>"));
			
			TextColumn<CodeListSearchDTO > category = new TextColumn<CodeListSearchDTO >() {
				@Override
				public String getValue(CodeListSearchDTO object) {
					return object.getCategoryDisplay();
				}
			};
			category.setSortable(true);
			sortHandler.setComparator(category,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO codeListSearchDTO1, CodeListSearchDTO codeListSearchDTO2) {
					if (codeListSearchDTO1 == codeListSearchDTO2) {
						return 0;
					}

					// Compare the name columns.
					if (codeListSearchDTO1 != null) {
						return (codeListSearchDTO2 != null) ? codeListSearchDTO1.getCategoryDisplay().compareTo(codeListSearchDTO2.getCategoryDisplay()) : 1;
					}
					return -1;
				}
			});
			
			table.addColumn(category, SafeHtmlUtils.fromSafeConstant("<span title='Category'>"+headers[1]+"</span>"));
			TextColumn<CodeListSearchDTO > codeSystem = new TextColumn<CodeListSearchDTO >() {
				@Override
				public String getValue(CodeListSearchDTO object) {
					return object.getCodeSystem();
				}
			};
			
			codeSystem.setSortable(true);
			sortHandler.setComparator(codeSystem,new Comparator<CodeListSearchDTO>() {
				public int compare(CodeListSearchDTO codeListSearchDTO1, CodeListSearchDTO codeListSearchDTO2) {
					if (codeListSearchDTO1 == codeListSearchDTO2) {
						return 0;
					}

					// Compare the name columns.
					if (codeListSearchDTO1 != null) {
						return (codeListSearchDTO2 != null) ? codeListSearchDTO1.getCodeSystem().compareTo(codeListSearchDTO2.getCodeSystem()) : 1;
					}
					return -1;
				}
			});
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
	
	/**
	 * Adds the selection handler on table.
	 * 
	 * @return the single selection model
	 */
	public SingleSelectionModel<CodeListSearchDTO> addSelectionHandlerOnTable(){
		final SingleSelectionModel<CodeListSearchDTO> selectionModel = new  SingleSelectionModel<CodeListSearchDTO>();
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				CodeListSearchDTO codeListObject = selectionModel.getSelectedObject();
				if(codeListObject !=null){
					//lastSelectedCodeList = codeListObject;
					MatContext.get().clearDVIMessages();
					MatContext.get().clearModifyPopUpMessages();
					setLastSelectedCodeList(codeListObject);
					setSelectedCodeList(codeListObject);
					MatContext.get().getEventBus().fireEvent( new OnChangeOptionsEvent());

				}
			}
		});
		return selectionModel;
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
		return data != null ? data.size() : 0;
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
	 * @see mat.client.shared.search.SearchResults#getValue(int, int)
	 */
	@Override
	public Widget getValue(int row, int column) {
		return getValueImpl(get(row), column);
	}

	
	/**
	 * Gets the value impl.
	 * 
	 * @param codeList
	 *            the code list
	 * @param column
	 *            the column
	 * @return the value impl
	 */
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
	
	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getStartIndex()
	 */
	@Override
	public int getStartIndex() {
		return startIndex;
	}
	
	/**
	 * Sets the start index.
	 * 
	 * @param startIndex
	 *            the new start index
	 */
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}
	

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#getResultsTotal()
	 */
	@Override
	public int getResultsTotal() {
		return resultsTotal;
	}
	
	/**
	 * Sets the results total.
	 * 
	 * @param resultsTotal
	 *            the new results total
	 */
	public void setResultsTotal(int resultsTotal) {
		this.resultsTotal = resultsTotal;
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
	public CodeListSearchDTO get(int row) {
		return data.get(row);
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnFiresSelection(int)
	 */
	@Override
	public boolean isColumnFiresSelection(int columnIndex) {
		return columnIndex == 1;
	}
	
	

	/**
	 * Checks if is editable.
	 * 
	 * @return true, if is editable
	 */
	public boolean isEditable() {
		return editable;
	}

	/* (non-Javadoc)
	 * @see mat.client.shared.search.SearchResults#isColumnSelectAll(int)
	 */
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
