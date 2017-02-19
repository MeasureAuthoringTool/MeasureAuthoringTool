package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLFunctionsWrapper implements IsSerializable {
	
	private List<CQLFunctions> cqlFunctionsList = new ArrayList<CQLFunctions>();
	
	public List<CQLFunctions> getCqlFunctionsList() {
		return cqlFunctionsList;
	}
	
	public void setCqlFunctionsList(List<CQLFunctions> cqlFunctionsList) {
		this.cqlFunctionsList = cqlFunctionsList;
	}
	
	
}
