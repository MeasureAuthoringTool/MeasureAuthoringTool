package mat.model;

import java.util.ArrayList;

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
