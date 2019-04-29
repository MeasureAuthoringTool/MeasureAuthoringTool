package mat.client.cqlworkspace.generalinformation;

import org.gwtbootstrap3.client.ui.constants.ValidationState;

import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.shared.CQLModelValidator;

public class CQLGeneralInformationUtility {
	
	public static final String COMMENT_LENGTH_ERROR = "Comment cannot exceed 2500 characters.";
	
	public static boolean validateGeneralInformationSection(CQLGeneralInformationView view, MessagePanel messagePanel, String libraryName, String commentBoxContent) {
		CQLModelValidator validator = new CQLModelValidator();

		if(libraryName != null && libraryName.isEmpty()) {
			view.getLibNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
			return false; 
		} 
		
		if(libraryName != null && !validator.doesAliasNameFollowCQLAliasNamingConvention(libraryName)) {
			view.getLibNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
			return false; 
		}
		
		if(validator.isCommentMoreThan2500Characters(commentBoxContent)) {
			view.getCommentsGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(COMMENT_LENGTH_ERROR);
			return false; 
		}
		
		if(validator.doesCommentContainInvalidCharacters(commentBoxContent)) {
			view.getCommentsGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getINVALID_COMMENT_CHARACTERS());
			return false;
		}
		
		return true;
	}
}
