package mat.model;

import java.util.ArrayList;


import mat.model.QualityDataSetDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QualityDataModelWrapper implements IsSerializable{
	
	private ArrayList<QualityDataSetDTO> qualityDataDTOList;

	public ArrayList<QualityDataSetDTO> getQualityDataDTO() {
		return qualityDataDTOList;
	}

	public void setQualityDataDTO(ArrayList<QualityDataSetDTO> qualityDataDTOList) {
		this.qualityDataDTOList = qualityDataDTOList;
	}

}
