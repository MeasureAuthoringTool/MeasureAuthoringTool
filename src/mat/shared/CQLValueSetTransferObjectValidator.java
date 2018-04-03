package mat.shared;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import mat.model.CQLValueSetTransferObject;
import mat.model.cql.CQLQualityDataSetDTO;

public class CQLValueSetTransferObjectValidator {
	public boolean isValid(CQLValueSetTransferObject object) {
		boolean isValid = true;
		if(object.getMatValueSet() != null){
			if(StringUtils.isBlank(object.getMatValueSet().getDisplayName())) {
				isValid = false;
			} else if((StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getProgram()) && StringUtils.isBlank(object.getCqlQualityDataSetDTO().getRelease())) || (StringUtils.isBlank(object.getCqlQualityDataSetDTO().getProgram()) && StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getRelease()))) {
				isValid = false;
			} else if ((StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getProgram()) || StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getRelease())) && object.isVersion()) {
				isValid = false;
			}
		} else if(object.getUserDefinedText().trim().isEmpty()){
			isValid = false;
		} else {
			if(StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getProgram()) || StringUtils.isNotBlank(object.getCqlQualityDataSetDTO().getRelease()) || object.isVersion()) {
				isValid = false;
			}
		}
		
		List<CQLQualityDataSetDTO> existingQDSList = object.getAppliedQDMList();
		for (CQLQualityDataSetDTO dataSetDTO : existingQDSList) {
			if (dataSetDTO.getOid().equalsIgnoreCase(ConstantMessages.USER_DEFINED_QDM_OID) && dataSetDTO
					.getCodeListName().equalsIgnoreCase(object.getCqlQualityDataSetDTO().getCodeListName())) {
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}
}
