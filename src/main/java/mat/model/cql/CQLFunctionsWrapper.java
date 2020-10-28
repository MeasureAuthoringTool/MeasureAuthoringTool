package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class CQLFunctionsWrapper implements IsSerializable {
	
	private List<CQLFunctions> cqlFunctionsList = new ArrayList<CQLFunctions>();
	
	public List<CQLFunctions> getCqlFunctionsList() {
		return cqlFunctionsList;
	}
	
	public void setCqlFunctionsList(List<CQLFunctions> cqlFunctionsList) {
		this.cqlFunctionsList = cqlFunctionsList;
	}
	
	
}
