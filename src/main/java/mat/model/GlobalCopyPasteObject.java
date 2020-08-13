package mat.model;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.ArrayList;
import java.util.List;

public class GlobalCopyPasteObject implements IsSerializable{
	
	private List<CQLQualityDataSetDTO> copiedValueSetList = new ArrayList<CQLQualityDataSetDTO>();
	
	private List<CQLCode> copiedCodeList = new ArrayList<CQLCode>();
	
	public List<CQLQualityDataSetDTO> getCopiedValueSetList() {
		return copiedValueSetList;
	}

	public void setCopiedValueSetList(List<CQLQualityDataSetDTO> copiedValueSetList) {
		this.copiedValueSetList = copiedValueSetList;
	}

	public List<CQLCode> getCopiedCodeList() {
		return copiedCodeList;
	}

	public void setCopiedCodeList(List<CQLCode> copiedCodeList) {
		this.copiedCodeList = copiedCodeList;
	}

}
