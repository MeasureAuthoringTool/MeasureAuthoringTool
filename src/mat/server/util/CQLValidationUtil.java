package mat.server.util;

import mat.model.cql.CQLModel;
import mat.server.CQLKeywordsUtil;

public class CQLValidationUtil {
	public static boolean isDuplicateIdentifierName(String identifierName, CQLModel model) {
		boolean isDefinitionMatch = model.getDefinitionList().stream().anyMatch(d -> d.getDefinitionName().equals(identifierName));
		boolean isFunctionMatch = model.getCqlFunctions().stream().anyMatch(f -> f.getFunctionName().equals(identifierName));
		boolean isParameterMatch = model.getCqlParameters().stream().anyMatch(p -> p.getParameterName().equals(identifierName));
		boolean isValuesetMatch = model.getValueSetList().stream().anyMatch(v -> v.getName().equals(identifierName));
		boolean isCodeMatch = model.getCodeList().stream().anyMatch(c -> c.getDisplayName().equals(identifierName));
		return isDefinitionMatch || isFunctionMatch || isParameterMatch || isValuesetMatch || isCodeMatch;
	}
	
	public static boolean isCQLReservedWord(String expressionName) {
		final String trimmedExpresion = expressionName.trim();
				
		return trimmedExpresion.equalsIgnoreCase("Patient") 
				|| trimmedExpresion.equalsIgnoreCase("Population")
				|| CQLKeywordsUtil.getCQLKeywords().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimmedExpresion));
	}
	
	public static boolean isCQLIncludedLibrarySameNameAsParentLibrary(String identifierName, CQLModel model) {
		return identifierName.equals(model.getLibraryName());
	}
}

