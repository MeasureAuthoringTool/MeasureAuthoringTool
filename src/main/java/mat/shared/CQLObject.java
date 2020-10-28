package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

public class CQLObject implements IsSerializable {

	List<CQLExpressionObject> cqlDefinitionObjectList = new ArrayList<CQLExpressionObject>();
	List<CQLExpressionObject> cqlFunctionObjectList = new ArrayList<CQLExpressionObject>();
	List<CQLExpressionObject> cqlParameterObjectList = new ArrayList<CQLExpressionObject>();

	public List<CQLExpressionObject> getCqlDefinitionObjectList() {
		return cqlDefinitionObjectList;
	}

	public void setCqlDefinitionObjectList(List<CQLExpressionObject> cqlDefinitionObjectList) {
		this.cqlDefinitionObjectList = cqlDefinitionObjectList;
	}

	public List<CQLExpressionObject> getCqlFunctionObjectList() {
		return cqlFunctionObjectList;
	}

	public void setCqlFunctionObjectList(List<CQLExpressionObject> cqlFunctionObjectList) {
		this.cqlFunctionObjectList = cqlFunctionObjectList;
	}

	public List<CQLExpressionObject> getCqlParameterObjectList() {
		return cqlParameterObjectList;
	}

	public void setCqlParameterObjectList(List<CQLExpressionObject> cqlParameterObjectList) {
		this.cqlParameterObjectList = cqlParameterObjectList;
	}

	public List<CQLExpressionObject> getAllExpressionList() {
		List<CQLExpressionObject> allExpressionList = new ArrayList<CQLExpressionObject>();
		allExpressionList.addAll(this.getCqlDefinitionObjectList());
		allExpressionList.addAll(this.getCqlFunctionObjectList());
		allExpressionList.addAll(this.getCqlParameterObjectList());

		return allExpressionList;
	}
}
