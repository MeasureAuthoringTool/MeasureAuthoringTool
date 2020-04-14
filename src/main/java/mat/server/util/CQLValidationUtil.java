package mat.server.util;

import java.util.HashSet;
import java.util.Objects;
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
import org.apache.commons.lang3.StringUtils;

public class CQLValidationUtil {

    private static String getCodeSystemIdentifier(CQLCode c) {
        String codesystemName = c.getCodeSystemName();
        if (c.isIsCodeSystemVersionIncluded()) {
            codesystemName += ":" + c.getCodeSystemVersion();
        }
        return codesystemName;
    }

    public static boolean isDuplicateIdentifierName(String identifierName, CQLModel model) {
        boolean isLibraryNameMatch = Objects.equals(identifierName, model.getLibraryName());
        boolean isAliasMatch = model.getCqlIncludeLibrarys().stream().anyMatch(l -> l.getAliasName().equals(identifierName));
        boolean isDefinitionMatch = model.getDefinitionList().stream().anyMatch(d -> d.getDefinitionName().equals(identifierName));
        boolean isFunctionMatch = model.getCqlFunctions().stream().anyMatch(f -> f.getFunctionName().equals(identifierName));
        boolean isParameterMatch = model.getCqlParameters().stream().anyMatch(p -> p.getParameterName().equals(identifierName));
        boolean isValuesetMatch = model.getValueSetList().stream().anyMatch(v -> v.getName().equals(identifierName));
        boolean isCodeMatch = model.getCodeList().stream().anyMatch(c -> c.getDisplayName().equals(identifierName));
        boolean isCodesystemMatch = model.getCodeList().stream().anyMatch(c -> Objects.equals(getCodeSystemIdentifier(c), identifierName));
        return isLibraryNameMatch || isAliasMatch || isDefinitionMatch || isFunctionMatch || isParameterMatch || isValuesetMatch || isCodeMatch || isCodesystemMatch;
    }

    public static boolean isCQLReservedWord(String expressionName) {
        final String trimmedExpresion = expressionName.trim();
        return trimmedExpresion.equalsIgnoreCase("Patient")
                || CQLKeywordsUtil.getCQLKeywords().getCqlKeywordsList().stream().anyMatch(definedKeyWord -> definedKeyWord.equalsIgnoreCase(trimmedExpresion));
    }

    public static boolean isCQLIncludedLibrarySameNameAsParentLibrary(String identifierName, CQLModel model) {
        return identifierName.equals(model.getLibraryName());
    }


    public static boolean doesModelHaveDuplicateIdentifierOrIdentifierAsKeyword(CQLModel cqlModel) {
        boolean isFhir = StringUtils.equals(cqlModel.getUsingModel(),"FHIR");

        Set<String> identifiersSet = new HashSet<>();
        Set<String> codesystemsSet = new HashSet<>();

        identifiersSet.add(cqlModel.getLibraryName());

        for (CQLIncludeLibrary library : cqlModel.getCqlIncludeLibrarys()) {
            if (identifiersSet.contains(library.getAliasName())) {
                return true;
            }

            identifiersSet.add(library.getAliasName());
        }

        for (CQLQualityDataSetDTO dto : cqlModel.getValueSetList()) {
            if (identifiersSet.contains(dto.getName())) {
                return true;
            }
            identifiersSet.add(dto.getName());
        }
        for (CQLCode code : cqlModel.getCodeList()) {
            if (isFhir) {
                if (StringUtils.isNotEmpty(code.getDisplayName())) {
                    //Display name is optional here in the FHIR CQL spec so have to handle that case.
                    if (identifiersSet.contains(code.getDisplayName())) {
                        return true;
                    }
                    identifiersSet.add(code.getDisplayName());
                    codesystemsSet.add(getCodeSystemIdentifier(code));
                }
            } else {
                if (identifiersSet.contains(code.getDisplayName())) {
                    return true;
                }
                identifiersSet.add(code.getDisplayName());
                codesystemsSet.add(getCodeSystemIdentifier(code));
            }

        }
        for (String codesystem : codesystemsSet) {
            if (identifiersSet.contains(codesystem)) {
                return true;
            }
        }

        identifiersSet.addAll(codesystemsSet);

        // Fhir allows overloading of functions
        if (!isFhir) {
            for (CQLFunctions func : cqlModel.getCqlFunctions()) {
                if (identifiersSet.contains(func.getFunctionName())) {
                    return true;
                }
                identifiersSet.add(func.getFunctionName());
            }
            for (String name : MatContext.get().getCqlConstantContainer().getFunctionNames()) {
                if (identifiersSet.contains(name)) {
                    return true;
                }
                identifiersSet.add(name);
            }
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

        for (String name : CQLKeywordsUtil.getCQLKeywords().getCqlKeywordsList()) {
            if (identifiersSet.contains(name)) {
                return true;
            }
            identifiersSet.add(name);
        }
        return false;
    }
}
