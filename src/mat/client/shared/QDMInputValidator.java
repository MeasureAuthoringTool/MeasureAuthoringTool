package mat.client.shared;

import java.util.ArrayList;
import java.util.List;
import mat.model.MatValueSetTransferObject;

public class QDMInputValidator {
	public List<String> validate(MatValueSetTransferObject matValueSetTransferObject){
		List<String> message = new ArrayList<String>();
		if(matValueSetTransferObject.getUserDefinedText() != null) {
			if(matValueSetTransferObject.getUserDefinedText().isEmpty()){
				message.add("Value Set Name cannot be empty.");
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
					message.add(MatContext.get().getMessageDelegate()
							.getINVALID_CHARACTER_VALIDATION_ERROR());
					break;
				}
			}
		}
		return message;
	}
}
