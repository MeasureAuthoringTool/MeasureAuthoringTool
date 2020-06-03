package mat.client.cqlworkspace.generalinformation;

import org.gwtbootstrap3.client.ui.constants.ValidationState;

import mat.client.cqlworkspace.AbstractCQLWorkspacePresenter;
import mat.client.shared.MatContext;
import mat.client.shared.MessagePanel;
import mat.shared.CQLModelValidator;

public class CQLGeneralInformationUtility {
	
	public static final String COMMENT_LENGTH_ERROR = "Comment cannot exceed 2500 characters.";
	public static final String LIBRARY_LENGTH_ERROR = "CQL Library Name cannot exceed 500 characters.";
	
	private CQLGeneralInformationUtility() {
		throw new IllegalStateException("CQL General Information Utility");
	}
	
	public static boolean validateGeneralInformationSection(CQLGeneralInformationView view, MessagePanel messagePanel, String libraryName, String commentBoxContent) {
		CQLModelValidator validator = new CQLModelValidator();
		boolean isFhir = MatContext.get().isCurrentModelTypeFhir();

		if(libraryName != null && libraryName.isEmpty()) {
			view.getLibraryNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameRequired());
			return false; 
		} 
		
		if(libraryName != null && !isFhir && !validator.isValidQDMName(libraryName)) {
			view.getLibraryNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getQDMCqlLibyNameError());
			return false; 
		}

		if(libraryName != null && isFhir && !validator.isValidFhirCqlName(libraryName)) {
			view.getLibraryNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getFhirCqlLibyNameError());
			return false;
		}
		
		if(validator.isLibraryNameMoreThan500Characters(libraryName)) {
			view.getLibraryNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(LIBRARY_LENGTH_ERROR);
			return false; 
		}
		
		if(!AbstractCQLWorkspacePresenter.isValidExpressionName(libraryName)) {
			view.getLibraryNameGroup().setValidationState(ValidationState.ERROR);
			messagePanel.getErrorMessageAlert().createAlert(MatContext.get().getMessageDelegate().getLibraryNameIsCqlKeywordError());
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
