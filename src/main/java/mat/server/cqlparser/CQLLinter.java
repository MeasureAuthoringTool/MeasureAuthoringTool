package mat.server.cqlparser;

import lombok.Getter;
import mat.model.cql.CQLCode;
import mat.model.cql.CQLCodeSystem;
import mat.shared.CQLError;
import org.antlr.v4.runtime.CommonTokenStream;
import org.cqframework.cql.gen.cqlBaseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class CQLLinter extends cqlBaseListener {
    @Getter
    protected CQLLinterConfig config;
    @Getter
    protected List<String> errorMessages;
    protected Set<String> warningMessages;
    @Getter
    protected List<CQLError> errors;

    protected List<CQLCodeSystem> codeSystems;
    protected List<CQLCode> matchedCodes;

    /**
     * An invalid edit means that the library declaration, using declaration, included library declarations,
     * valueset declarations, code declarations, or codesysetm declarations have been edited and do not match
     * what was in the existing model.
     */
    protected boolean hasInvalidEdits = false;
    protected boolean enteredLibraryDefinition = false;
    protected boolean enteredUsingDefinition = false;
    protected int libraryDefinitionStartLine;
    protected int definitionStartLine;
    protected int contextLine;
    protected int parameterStartLine;
    protected int terminologyEndLine;
    protected int noCommentZoneStartLine;
    protected int noCommentZoneEndLine;

    protected int numberOfIncludedLibraries = 0;
    protected int numberOfValuesets = 0;
    protected int numberOfCodes = 0;
    protected int numberOfCodesystems = 0;
    protected  CommonTokenStream tokens;

    public List<String> getWarningMessages() {
        return new ArrayList<>(this.warningMessages);
    }
}
