package mat.model.cql;

import java.util.List;

import mat.model.RiskAdjustmentDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CQLDefinitionsWrapper implements IsSerializable{

	
	private List<CQLDefinition> cqlDefinitions;
	
	private List<RiskAdjustmentDTO> riskAdjVarDTOList;

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
