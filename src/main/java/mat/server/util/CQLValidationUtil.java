package mat.server.util;

import java.util.HashSet;
import java.util.Set;
import mat.client.shared.MatContext;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLDefinition;
import mat.model.cql.CQLFunctions;
import mat.model.cql.CQLIncludeLibrary;
import mat.model.cql.CQLModel;
import mat.model.cql.CQLParameter;
import mat.model.cql.CQLQualityDataSetDTO;
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
	
	public static boolean doesModelHaveDuplicateIdentifierOrIdentifierAsKeyword(CQLModel cqlModel) {

		    Set<String> identifiersSet = new HashSet<>(); 
		    
		    for (CQLQualityDataSetDTO dto : cqlModel.getValueSetList()) {
		    	if (identifiersSet.contains(dto.getName())) {
		    		return true;
		    	}
		    	identifiersSet.add(dto.getName());
		    }
	    	for (CQLCode code : cqlModel.getCodeList()) {
	    		if (identifiersSet.contains(code.getDisplayName())) {
	    			return true;
	    		}
    			identifiersSet.add(code.getName());
	    	}
	    	for (CQLDefinition def : cqlModel.getDefinitionList()) {
	    		if (identifiersSet.contains(def.getDefinitionName())) {
	    			return true;
	    		}
    			identifiersSet.add(def.getDefinitionName());
	    	}
	    	for (CQLParameter par : cqlModel.getCqlParameters()) {
	    		if (identifiersSet.contains(par.getParameterName())) {
	    			return true;
	    		}
    			identifiersSet.add(par.getParameterName());
	    	}
	    	for (CQLFunctions func : cqlModel.getCqlFunctions()) {
	    		if (identifiersSet.contains(func.getFunctionName())) {
	    			return true;
	    		}
    			identifiersSet.add(func.getFunctionName());
	    	}
	    	for (CQLIncludeLibrary lib : cqlModel.getCqlIncludeLibrarys()) {
	    		if (identifiersSet.contains(lib.getCqlLibraryName())) {
	    			return true;
	    		}
    			identifiersSet.add(lib.getCqlLibraryName());
	    	}
	    	for (String name : MatContext.get().getCqlConstantContainer().getFunctionNames()) {
	    		if (identifiersSet.contains(name)) {
	    			return true;
	    		}
    			identifiersSet.add(name);
	    	}
	    	for (String name : CQLKeywordsUtil.getCQLKeywords().getCqlKeywordsList()) {
	    		if (identifiersSet.contains(name)) {
	    			return true;
	    		}
    			identifiersSet.add(name);
	    	}
		    return false;
	}
}
