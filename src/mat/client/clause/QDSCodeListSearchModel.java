package mat.client.clause;

import java.util.HashMap;
import java.util.List;

import mat.client.codelist.events.OnChangeOptionsEvent;
import mat.client.shared.MatContext;
import mat.client.shared.search.SearchResults;
import mat.model.CodeListSearchDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class QDSCodeListSearchModel implements SearchResults<CodeListSearchDTO>,IsSerializable {

	private static String[] headers = new String[] {"QDM","Category","Code System"};
	private static String[] widths = new String[] {"50%","25%","25%"};
	
	private HashMap<CodeListSearchDTO, RadioButton> radioButtonMap = new HashMap<CodeListSearchDTO, RadioButton>();
	private List<CodeListSearchDTO> data;
	private int startIndex;
	private int resultsTotal;
	private boolean editable;
	
	private CodeListSearchDTO lastSelectedCodeList;
	
  
	public CodeListSearchDTO getLastSelectedCodeList() {
		return lastSelectedCodeList;
	}

	public void setLastSelectedCodeList(CodeListSearchDTO lastSelectedCodeList) {
		this.lastSelectedCodeList = lastSelectedCodeList;
	}

	public void setData(List<CodeListSearchDTO> data) {
		this.data = data;
		this.editable = MatContext.get().getMeasureLockService().checkForEditPermission();
		for(final CodeListSearchDTO codeList : data) {
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
		}
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
				if(rbLabel.length() > 50){
					rbLabel = rbLabel.substring(0,50);
					StringBuffer rbLbl = new StringBuffer();
					rbLbl = rbLbl.append(rbLabel).append("...");
					r.setText(rbLbl.toString());
				}
				
				r.setEnabled(editable);
				value = r;
			}else{
				RadioButton r = radioButtonMap.get(codeList);
				String rbLabel = r.getText();
				if(rbLabel.length() > 50){
					rbLabel = rbLabel.substring(0,50);
					StringBuffer rbLbl = new StringBuffer();
					rbLbl = rbLbl.append(rbLabel).append("...");
					r.setText(rbLbl.toString());
				}
				
				value = r;
			}
			
			break;
		case 1:
			value = new Label(codeList.getCategoryDisplay());
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
	
	public CodeListSearchDTO getSelectedCodeList() {
		CodeListSearchDTO codeList = null;
		for(int i = 0; i < data.size(); i++) {
			codeList = data.get(i);
			RadioButton rb = radioButtonMap.get(codeList);
			if(rb.getValue().equals(Boolean.TRUE)) {
				return codeList;
			}else{
				codeList = null;
			}
		}
		return codeList;
		
	}

	public boolean isEditable() {
		return editable;
	}

	@Override
	public boolean isColumnSelectAll(int columnIndex) {
		return false;
	}
	
}
