package mat.model.cql;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLGrammarDataType implements IsSerializable{
	private  List<String> cqlDataTypeList = new ArrayList<String>();
	/*public static ArrayList<String> getDataTypeName() {
		DATA_TYPE.clear();
		DATA_TYPE.add("Boolean");
		DATA_TYPE.add("DateTime");
		DATA_TYPE.add("Decimal");
		DATA_TYPE.add("Integer");
		DATA_TYPE.add("QDM Datatype");
		DATA_TYPE.add("String");
		DATA_TYPE.add("Time");
		DATA_TYPE.add("Others");
		return DATA_TYPE;
	}*/
	
	public List<String> getCqlDataTypeList() {
		return cqlDataTypeList;
	}
	
	public void setCqlDataTypeList(List<String> cqlDataTypeList2) {
		cqlDataTypeList = cqlDataTypeList2;
	}
}
