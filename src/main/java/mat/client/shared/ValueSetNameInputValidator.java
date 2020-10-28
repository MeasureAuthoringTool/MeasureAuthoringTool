package mat.client.shared;

import mat.model.CQLValueSetTransferObject;

public class ValueSetNameInputValidator {
	public String validate(CQLValueSetTransferObject ValueSetTransferObject){
		String valuesetName = ValueSetTransferObject.getUserDefinedText();
		
		if(valuesetName == null || valuesetName.isEmpty()) {
			return "Value Set Name cannot be empty.";
		}
		
		else if(valuesetName.matches(".*[\\*\\?:\\-\\|\\!\"\\+;%].*")) {
			return MatContext.get().getMessageDelegate().getINVALID_CHARACTER_VALIDATION_ERROR();
		}
		
		else {
			return "";
		}
	}
}
