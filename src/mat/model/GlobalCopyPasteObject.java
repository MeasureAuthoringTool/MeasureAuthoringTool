package mat.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.model.cql.CQLCode;
import mat.model.cql.CQLQualityDataSetDTO;

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
