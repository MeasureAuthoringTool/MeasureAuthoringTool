package mat.model.cql;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLCodeWrapper implements IsSerializable{

	private List<CQLCode> cqlCodeList = new ArrayList<CQLCode>();

	public List<CQLCode> getCqlCodeList() {
		return cqlCodeList;
	}

	public void setCqlCodeList(List<CQLCode> cqlCodeList) {
		this.cqlCodeList = cqlCodeList;
	}
}
