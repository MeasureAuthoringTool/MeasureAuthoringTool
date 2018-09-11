package mat.model.cql.validator;

import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.server.Validator;
import mat.server.util.CQLValidationUtil;
import mat.shared.CQLModelValidator;

public class CQLIncludeLibraryValidator extends Validator {
	
	private static final String LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION = "Invalid Library Alias. Must be unique, start with an alpha-character or underscore followed by an alpha-numeric character(s) or underscore(s), and must not contain spaces.";
	private static final String DUPLCIATE_CQL_KEYWORD = "A library alias can not be an exact match to a defined CQL keyword.";
	private CQLModelValidator validator = new CQLModelValidator();

	public void validate(CQLIncludeLibrary includedLibrary, CQLModel model) {	
		this.setValid(true);
		doesAliasNameHaveSpecialCharacter(includedLibrary.getAliasName());
		doesAliasNameFollowCQLAliasNamingConvention(includedLibrary.getAliasName());
		isDuplicateIdentifierName(includedLibrary.getAliasName(), model);
		isDuplicateCQLLibraryName(includedLibrary.getAliasName(), model);
		isCQLReservedKeyword(includedLibrary.getAliasName());
	}
	
	private void doesAliasNameHaveSpecialCharacter(String libraryName) {
		boolean hasSpecialCharacters = validator.hasSpecialCharacter(libraryName);
		if(hasSpecialCharacters) {
			this.getMessages().add(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION);
			this.setValid(false);
		}
	}
	
	private void doesAliasNameFollowCQLAliasNamingConvention(String libraryName) {
		boolean isValidLibraryName = validator.doesAliasNameFollowCQLAliasNamingConvention(libraryName);
		if(!isValidLibraryName) {
			this.getMessages().add(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION);
			this.setValid(false);
		}
	}
	
	private void isDuplicateIdentifierName(String aliasName, CQLModel model) {
		boolean isInvalidLibraryName = CQLValidationUtil.isDuplicateIdentifierName(aliasName, model);
		if(isInvalidLibraryName) {
			this.getMessages().add(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION);
			this.setValid(false);
		}
	}
	
	private void isDuplicateCQLLibraryName(String aliasName, CQLModel model) {
		boolean isSameNameAsMeasureName = CQLValidationUtil.isCQLIncludedLibrarySameNameAsParentLibrary(aliasName, model);
		if(isSameNameAsMeasureName) {
			this.getMessages().add(LIBRARY_ALIAS_UNIQUE_AND_HAVE_PROPER_NAMING_CONVENTION);
			this.setValid(false);
		}
	}
	
	private void isCQLReservedKeyword(String aliasName) {
		boolean isDuplicateCQLKeyword = CQLValidationUtil.isCQLReservedWord(aliasName);
		if(isDuplicateCQLKeyword) {
			this.getMessages().add(DUPLCIATE_CQL_KEYWORD);
			this.setValid(false);
		}
	}
}
