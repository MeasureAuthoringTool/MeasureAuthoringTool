package mat.server.service.cql;

import mat.model.cql.CQLModel;
import mat.shared.CQLError;
import mat.shared.CQLObject;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.cql.model.UnusedCqlElements;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class CqlValidationResultBuilder {
    private final SaveUpdateCQLResult parsedCQL = new SaveUpdateCQLResult();
    private final Set<String> includeLibrariesWithErrors = new HashSet<>();
    private final List<CQLError> errors = new ArrayList<>();
    private final Map<String, List<CQLError>> libraryNameErrorsMap = new HashMap<>();
    private final Map<String, List<CQLError>> libraryNameWarningsMap = new HashMap<>();
    private CQLModel cqlModel;
    private CQLObject cqlObject;
    private String parentLibraryName;

    private List<LibraryErrors> libraryErrors = Collections.emptyList();

    private UnusedCqlElements unusedCqlElements;

    public void setUnusedCqlElements(UnusedCqlElements unusedCqlElements) {
        this.unusedCqlElements = unusedCqlElements;
    }

    public void cqlModel(CQLModel cqlModel) {
        this.cqlModel = cqlModel;
    }

    public void cqlObject(CQLObject cqlObject) {
        this.cqlObject = cqlObject;
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
        parsedCQL.setCqlObject(cqlObject);
        parsedCQL.setLibraryNameErrorsMap(libraryNameErrorsMap);
        parsedCQL.setLibraryNameWarningsMap(libraryNameWarningsMap);
        parsedCQL.setIncludeLibrariesWithErrors(includeLibrariesWithErrors);
        parsedCQL.setUnusedCqlElements(unusedCqlElements);

        for (CQLError e : parsedCQL.getCqlErrors()) {
            if (StringUtils.equalsIgnoreCase("SEVERE", e.getSeverity())) {
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
