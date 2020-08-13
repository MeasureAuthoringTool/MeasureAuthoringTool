package mat.model.cql;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.RiskAdjustmentDTO;

import java.util.ArrayList;
import java.util.List;

public class CQLDefinitionsWrapper implements IsSerializable{

	
	private List<CQLDefinition> cqlDefinitions = new ArrayList<CQLDefinition>();
	
	private List<RiskAdjustmentDTO> riskAdjVarDTOList = new ArrayList<RiskAdjustmentDTO>();

	public List<CQLDefinition> getCqlDefinitions() {
		return cqlDefinitions;
	}

	public void setCqlDefinitions(List<CQLDefinition> cqlDefinitions) {
		this.cqlDefinitions = cqlDefinitions;
	}

	public List<RiskAdjustmentDTO> getRiskAdjVarDTOList() {
		return riskAdjVarDTOList;
	}

	public void setRiskAdjVarDTOList(List<RiskAdjustmentDTO> riskAdjVarDTOList) {
		this.riskAdjVarDTOList = riskAdjVarDTOList;
	}
}
