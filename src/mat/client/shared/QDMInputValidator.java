package mat.client.shared;

import mat.model.CQLValueSetTransferObject;

public class QDMInputValidator {
	public String validate(CQLValueSetTransferObject matValueSetTransferObject){
		String message = "";
		if(matValueSetTransferObject.getUserDefinedText() != null) {
			if(matValueSetTransferObject.getUserDefinedText().isEmpty()){
				message= "Value Set Name cannot be empty.";
			}
			for(int i = 0; i< matValueSetTransferObject.getUserDefinedText().length(); i++){
				if((matValueSetTransferObject.getUserDefinedText().charAt(i) == '+')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '*')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '?')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == ':')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '-')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '|')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '!')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '"')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == ';')
						|| (matValueSetTransferObject.getUserDefinedText().charAt(i) == '%')){
					message= MatContext.get().getMessageDelegate()
							.getINVALID_CHARACTER_VALIDATION_ERROR();
					break;
				}
			}
		}
		return message;
	}
}
