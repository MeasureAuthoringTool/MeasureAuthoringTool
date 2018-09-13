package mat.client.cqlworkspace.generalinformation;

import org.gwtbootstrap3.client.ui.constants.ValidationState;

import mat.client.cqlworkspace.CQLLeftNavBarPanelView;
import mat.client.shared.MatContext;
import mat.shared.CQLModelValidator;

public class CQLGeneralInformationUtility {
	
	public static boolean validateGeneralInformationSection(CQLGeneralInformationView view, CQLLeftNavBarPanelView leftNavView, String libraryName, String commentBoxContent) {
		CQLModelValidator validator = new CQLModelValidator();

		if(libraryName != null && libraryName.isEmpty()) {
			view.getLibNameGroup().setValidationState(ValidationState.ERROR);
			leftNavView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
			return false; 
		} 
		
		if(libraryName != null && !validator.doesAliasNameFollowCQLAliasNamingConvention(libraryName)) {
			view.getLibNameGroup().setValidationState(ValidationState.ERROR);
			leftNavView.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getCqlStandAloneLibraryNameError());
			return false; 
		}
		
		if(validator.isCommentMoreThan250Characters(commentBoxContent)) {
			view.getCommentsGroup().setValidationState(ValidationState.ERROR);
			leftNavView.getErrorMessageAlert().createAlert("Comment cannot exceed 250 characters.");
			return false; 
		}
		
		if(validator.doesCommentContainInvalidCharacters(commentBoxContent)) {
			view.getCommentsGroup().setValidationState(ValidationState.ERROR);
			leftNavView.getErrorMessageAlert().createAlert("Comments can not contain /* or */.");
			return false;
		}
		
		return true;
	}
}
