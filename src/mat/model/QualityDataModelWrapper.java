package mat.model;

import java.util.ArrayList;
import java.util.List;

import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QualityDataModelWrapper implements IsSerializable{
	
	private ArrayList<QualityDataSetDTO> qualityDataDTOList;
	private ArrayList<QualityDataSetDTO> qualityDataDTOList_I;
	private ArrayList<QualityDataSetDTO> qualityDataDTOList_M;

	public ArrayList<QualityDataSetDTO> getQualityDataDTO() {
		return qualityDataDTOList;
	}

	public void setQualityDataDTO(ArrayList<QualityDataSetDTO> qualityDataDTOList) {
		this.qualityDataDTOList = qualityDataDTOList;
	}
	
	public ArrayList<QualityDataSetDTO> getQualityDataDTOList_I() {
		return qualityDataDTOList_I;
	}

	public void setQualityDataDTOList_I(
			ArrayList<QualityDataSetDTO> qualityDataDTOList_I) {
		this.qualityDataDTOList_I = qualityDataDTOList_I;
	}

	public ArrayList<QualityDataSetDTO> getQualityDataDTOList_M() {
		return qualityDataDTOList_M;
	}

	public void setQualityDataDTOList_M(
			ArrayList<QualityDataSetDTO> qualityDataDTOList_M) {
		this.qualityDataDTOList_M = qualityDataDTOList_M;
	}



}
