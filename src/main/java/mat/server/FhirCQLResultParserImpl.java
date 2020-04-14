package mat.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import mat.model.cql.CQLModel;
import mat.shared.CQLError;
import mat.shared.SaveUpdateCQLResult;

@Component
public class FhirCQLResultParserImpl implements FhirCQLResultParser {
    @Override
    public SaveUpdateCQLResult generateParsedCqlObject(String cqlValidationResponse, CQLModel cqlModel) {
        SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
        Set<String> includeLibrariesWithErrors = new HashSet<>();
        List<CQLError> errors = new ArrayList<>();
        Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();
        Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();
        JSONArray jsonArray;
        String parentLibraryName = cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed();
        JSONObject cqlValidationResponseJson = new JSONObject(cqlValidationResponse);
        if (cqlValidationResponseJson.has("errorExceptions")) {
            jsonArray = cqlValidationResponseJson.getJSONArray("errorExceptions");
            if (jsonArray != null) {
                buildCqlErrors(jsonArray, parentLibraryName, libraryNameErrorsMap, libraryNameWarningsMap, errors, includeLibrariesWithErrors);
            }
        }
        if (cqlValidationResponseJson.has("library")) {
            JSONObject libraryObject = (JSONObject) cqlValidationResponseJson.get("library");
            if (libraryObject.has("annotation")) {
                jsonArray = libraryObject.getJSONArray("annotation");
                if (jsonArray != null) {
                    buildCqlErrors(jsonArray, parentLibraryName, libraryNameErrorsMap, libraryNameWarningsMap, errors, includeLibrariesWithErrors);
                }
            }
        }
        parsedCQL.setCqlModel(cqlModel);
        parsedCQL.setCqlErrors(errors);
        parsedCQL.setLibraryNameErrorsMap(libraryNameErrorsMap);
        parsedCQL.setLibraryNameWarningsMap(libraryNameWarningsMap);
        parsedCQL.setIncludeLibrariesWithErrors(includeLibrariesWithErrors);
        return parsedCQL;
    }

    private void buildCqlErrors(JSONArray jsonArray, String parentLibraryName, Map<String, List<CQLError>> libraryNameErrorsMap, Map<String, List<CQLError>> libraryNameWarningsMap, List<CQLError> errors, Set<String> includeLibrariesWithErrors) {
        JSONObject jsonObject;
        for (int i = 0; i < jsonArray.length(); i++) {
            CQLError error = new CQLError();
            jsonObject = jsonArray.getJSONObject(i);
            String libraryId = jsonObject.has("libraryId") ? jsonObject.getString("libraryId") : null;
            String targetIncludeLibraryId = jsonObject.has("targetIncludeLibraryId") ? jsonObject.getString("targetIncludeLibraryId") : null;
            String targetIncludeLibraryVersionId = jsonObject.has("targetIncludeLibraryVersionId") ? jsonObject.getString("targetIncludeLibraryVersionId") : null;
            error.setStartErrorInLine(jsonObject.getInt("startLine"));
            error.setErrorInLine(jsonObject.getInt("startLine"));
            error.setErrorAtOffeset(jsonObject.getInt("startChar"));
            error.setEndErrorInLine(jsonObject.getInt("endLine"));
            error.setEndErrorAtOffset(jsonObject.getInt("endChar"));
            error.setErrorMessage(jsonObject.has("message") ? jsonObject.getString("message") : null);
            error.setSeverity(jsonObject.has("errorSeverity") ? jsonObject.getString("errorSeverity") : "Error");
            errors.add(error);
            boolean isError = error.getSeverity().equalsIgnoreCase("Error");
            if (isError) {
                libraryNameErrorsMap.put(parentLibraryName, errors);
            } else {
                libraryNameWarningsMap.put(parentLibraryName, errors);
            }

            if (libraryId != null && targetIncludeLibraryId != null && !libraryId.equals(targetIncludeLibraryId) && isError) {
                includeLibrariesWithErrors.add(targetIncludeLibraryId + "(" + targetIncludeLibraryVersionId + ")");
            }
        }
    }

}
