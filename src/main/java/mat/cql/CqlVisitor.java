package mat.cql;

import mat.shared.CQLError;

import java.util.List;

/**
 * Visitor pattern for parsing cql. The parsing uses a callback pattern where corresponding
 * methods are invoked when that section is encountered in the cql.
 */
public interface CqlVisitor {
    default void validateBeforeParse() {
        //by default do nothing unless its implemented.
    }

    default void validateAfterParse() {
        //by default do nothing unless its implemented.
    }

    default void handleError(CQLError error) {}

    default boolean isRemovingBlockComments() {
        return false;
    }

    default boolean isRemovingLineComments() {
        return false;
    }

    default void libraryTag(String libraryName,
                            String version) {
        //by default do nothing unless its implemented.
    }

    default void fhirVersion(String fhirVersion) {
        //by default do nothing unless its implemented.
    }

    default void includeLib(String libName,
                            String version,
                            String alias,
                            String model,
                            String modelVersion) {
//by default do nothing unless its implemented.
    }

    default void codeSystem(String name,
                            String uri,
                            String versionUri) {
        //by default do nothing unless its implemented.

    }

    default void valueSet(String type, String uri) {
        //by default do nothing unless its implemented.
    }

    default void code(String name, String code, String codeSystemName, String displayName) {
        //by default do nothing unless its implemented.
    }

    default void parameter(String name, String logic, String comment) {
        //by default do nothing unless its implemented.
    }

    default void context(String context) {
        //by default do nothing unless its implemented.
    }

    default void definition(String name, String logic, String comment) {
        //by default do nothing unless its implemented.
    }

    default void function(String name, List<FunctionArgument> args, String logic, String comment) {
        //by default do nothing unless its implemented.
    }
}
