package mat.server.service.cql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import mat.model.cql.CQLModel;
import mat.shared.CQLError;
import mat.shared.SaveUpdateCQLResult;

public class CqlValidationResultBuilder {
    private final SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
    private final Set<String> includeLibrariesWithErrors = new HashSet<>();
    private final List<CQLError> errors = new ArrayList<>();
    private final Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();
    private final Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();
    private CQLModel cqlModel;
    private String parentLibraryName;

    private List<LibraryErrors> libraryErrors = Collections.emptyList();

    public void cqlModel(CQLModel cqlModel) {
        this.cqlModel = cqlModel;
    }

    public void libraryErrors(List<LibraryErrors> libraryErrors) {
        this.libraryErrors = libraryErrors;
    }

    public SaveUpdateCQLResult buildFromLibraryErrors() {
        parentLibraryName = cqlModel.getLibraryName() + "-" + cqlModel.getVersionUsed();
        for (LibraryErrors libraryErrors : libraryErrors) {
            for (CQLError cqlError : libraryErrors.getErrors()) {
                boolean isError = addError(cqlError);
                if (!StringUtils.equals(cqlModel.getLibraryName(), libraryErrors.getName()) && isError) {
                    includeLibrariesWithErrors.add(libraryErrors.getName() + "(" + libraryErrors.getVersion() + ")");
                }
            }
        }
        parsedCQL.setCqlModel(cqlModel);
        parsedCQL.setCqlErrors(errors);
        parsedCQL.setLibraryNameErrorsMap(libraryNameErrorsMap);
        parsedCQL.setLibraryNameWarningsMap(libraryNameWarningsMap);
        parsedCQL.setIncludeLibrariesWithErrors(includeLibrariesWithErrors);

        for (CQLError e : parsedCQL.getCqlErrors()) {
            if (StringUtils.equalsIgnoreCase("SEVERE",e.getSeverity())) {
                parsedCQL.setSevereError(true);
                break;
            }
        }

        return parsedCQL;
    }

    private boolean addError(CQLError error) {
        errors.add(error);
        boolean isError = isError(error);
        // TODO: this is just an existing logic refactored.
        // For some reason the map below always uses the same key (parent library) and value the value (all errors).
        if (isError) {
            libraryNameErrorsMap.put(parentLibraryName, errors);
        } else {
            libraryNameWarningsMap.put(parentLibraryName, errors);
        }
        return isError;
    }

    private boolean isError(CQLError error) {
        return error.getSeverity().equalsIgnoreCase(CQLError.ERROR_SEVERITY)
                || error.getSeverity().equalsIgnoreCase(CQLError.SEVERE_SEVERITY);
    }
}
