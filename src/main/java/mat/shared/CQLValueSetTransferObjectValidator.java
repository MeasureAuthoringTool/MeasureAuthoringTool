package mat.shared;

import mat.model.CQLValueSetTransferObject;
import mat.model.cql.CQLQualityDataSetDTO;

import java.util.List;

public class CQLValueSetTransferObjectValidator {
	public boolean isValid(CQLValueSetTransferObject object) {
		boolean isValid = true;
		if(object.getValueSet() != null){
			if(StringUtility.isEmptyOrNull(object.getValueSet().getDisplayName())) {
				isValid = false;
			} 
		} else if(object.getUserDefinedText().trim().isEmpty()){
			isValid = false;
		} 
		
		List<CQLQualityDataSetDTO> existingQDSList = object.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {
			if (dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID) && dataSetDTO
					.getName().equalsIgnoreCase(object.getCqlQualityDataSetDTO().getName())) {
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
}
