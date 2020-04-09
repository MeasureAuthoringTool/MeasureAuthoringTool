package mat.cql;

import java.util.List;

/**
 * Visitor pattern for parsing cql. The parsing uses a callback pattern where corresponding
 * methods are invoked when that section is encountered in the cql.
 */
public interface CqlVisitor {
    default void validate() {
    }

    default boolean isRemovingBlockComments() {
        return true;
    }

    default boolean isRemovingLineComments() {
        return true;
    }

    default void libraryTag(String libraryName,
                            String version) {
    }

    default void fhirVersion(String fhirVersion) {
    }

    default void includeLib(String libName,
                            String version,
                            String alias,
                            String model,
                            String modelVersion) {
    }

    default void codeSystem(String name,
                            String uri,
                            String versionUri) {

    }

    default void valueSet(String type, String uri) {
    }

    default void code(String name, String code, String codeSystemName, String displayName) {
    }

    default void parameter(String name, String logic) {
    }

    default void context(String context) {
    }

    default void definition(String name, String logic) {
    }

    default void function(String name, List<FunctionArgument> args, String logic) {
    }
}
