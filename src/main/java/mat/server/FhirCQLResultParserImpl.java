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
    private static final String ERROR_SEVERITY = "Error";

    @Override
    public SaveUpdateCQLResult generateParsedCqlObject(String cqlValidationResponse, CQLModel cqlModel) {
        ResultBuilder resultBuilder = new ResultBuilder();
        resultBuilder.cqlModel(cqlModel);
        resultBuilder.cqlValidationResponse(cqlValidationResponse);
        return resultBuilder.build();
    }


    static class ResultBuilder {
        private final SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
        private final Set<String> includeLibrariesWithErrors = new HashSet<>();
        private final List<CQLError> errors = new ArrayList<>();
        private final Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();
        private final Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();
        private CQLModel cqlModel;
        private String parentLibraryName;
        private String cqlValidationResponse;
        private JSONObject cqlValidationResponseJson;

        public void cqlModel(CQLModel cqlModel) {
            this.cqlModel = cqlModel;
        }

        public void cqlValidationResponse(String cqlValidationResponse) {
            this.cqlValidationResponse = cqlValidationResponse;
        }

        public SaveUpdateCQLResult build() {
            cqlValidationResponseJson = new JSONObject(cqlValidationResponse);
            parentLibraryName = cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed();

            processErrorExceptions();
            processLibraryAnnotations();

            parsedCQL.setCqlModel(cqlModel);
            parsedCQL.setCqlErrors(errors);
            parsedCQL.setLibraryNameErrorsMap(libraryNameErrorsMap);
            parsedCQL.setLibraryNameWarningsMap(libraryNameWarningsMap);
            parsedCQL.setIncludeLibrariesWithErrors(includeLibrariesWithErrors);
            return parsedCQL;
        }

        private void processLibraryAnnotations() {
            if (cqlValidationResponseJson.has("library")) {
                JSONObject libraryObject = (JSONObject) cqlValidationResponseJson.get("library");
                if (libraryObject.has("annotation")) {
                    JSONArray jsonArray = libraryObject.getJSONArray("annotation");
                    buildCqlErrors(jsonArray);
                }
            }
        }

        private void processErrorExceptions() {
            if (cqlValidationResponseJson.has("errorExceptions")) {
                JSONArray jsonArray = cqlValidationResponseJson.getJSONArray("errorExceptions");
                buildCqlErrors(jsonArray);
            }
        }

        private void buildCqlErrors(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                buildCqlError(jsonObject);
            }
        }

        private void buildCqlError(JSONObject jsonObject) {
            CQLError error = new CQLError();
            String libraryId = jsonObject.optString("libraryId", null);
            String targetIncludeLibraryId = jsonObject.optString("targetIncludeLibraryId", null);
            String targetIncludeLibraryVersionId = jsonObject.optString("targetIncludeLibraryVersionId", null);
            error.setStartErrorInLine(jsonObject.getInt("startLine"));
            error.setErrorInLine(jsonObject.getInt("startLine"));
            error.setErrorAtOffeset(jsonObject.getInt("startChar"));
            error.setEndErrorInLine(jsonObject.getInt("endLine"));
            error.setEndErrorAtOffset(jsonObject.getInt("endChar"));
            error.setErrorMessage(jsonObject.optString("message", null));
            error.setSeverity(jsonObject.optString("errorSeverity", ERROR_SEVERITY));
            errors.add(error);
            boolean isError = error.getSeverity().equalsIgnoreCase(ERROR_SEVERITY);
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
