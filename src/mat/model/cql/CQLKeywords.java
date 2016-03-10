package mat.model.cql;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLKeywords implements IsSerializable{
	private  List<String> cqlDataTypeList = new ArrayList<String>();
	private List<String> cqlTimingList = new ArrayList<String>();
	private List<String> cqlFunctionsList = new ArrayList<String>();
	
	public List<String> getCqlDataTypeList() {
		return cqlDataTypeList;
	}
	
	public void setCqlDataTypeList(List<String> cqlDataTypeList2) {
		cqlDataTypeList = cqlDataTypeList2;
	}
	
	public List<String> getCqlTimingList() {
		return cqlTimingList;
	}
	
	public void setCqlTimingList(List<String> cqlTimingList) {
		this.cqlTimingList = cqlTimingList;
	}
	
	public List<String> getCqlFunctionsList() {
		return cqlFunctionsList;
	}
	
	public void setCqlFunctionsList(List<String> cqlFunctionsList) {
		this.cqlFunctionsList = cqlFunctionsList;
	}
}
