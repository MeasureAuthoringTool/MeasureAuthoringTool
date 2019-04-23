package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLParametersWrapper implements IsSerializable {
	private List<CQLParameter> cqlParameterList = new ArrayList<CQLParameter>();
	
	public List<CQLParameter> getCqlParameterList() {
		return cqlParameterList;
	}
	
	public void setCqlParameterList(List<CQLParameter> cqlParameterList) {
		this.cqlParameterList = cqlParameterList;
	}
}
