package cqltoelm;

import org.cqframework.cql.cql2elm.LibrarySourceProvider;
import org.hl7.elm.r1.VersionedIdentifier;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author Jack Meyer
 * <p>
 * Library Source Provider for use with libraries which are made up of strings.
 */
public class StringLibrarySourceProvider implements LibrarySourceProvider {

    /**
     * A mapping of library names to cql library strings. The format is <libraryname-x.x.xxx, cql library string>.
     */
    private Map<String, String> cqlLibraryMap;

    /**
     * String Library Source Provider
     *
     * @param cqlLibraryMap the cql library mapping.
     *                      The key is a string in the format of libraryname-x.x.xxx.
     *                      The value is the cql string.
     *                      <p>
     *                      The key must be in the format given above, this guarantees uniqueness of the key since two libraries, with the same
     *                      name and version cannot be included together. This format is also identical to the one that the Default Library
     *                      Source Provider uses for finding files.
     */
    public StringLibrarySourceProvider(Map<String, String> cqlLibraryMap) {
        this.cqlLibraryMap = cqlLibraryMap;
    }

    @Override
    public InputStream getLibrarySource(VersionedIdentifier libraryIdentifier) {

        String name = libraryIdentifier.getId() + "-" + libraryIdentifier.getVersion();
        String cqlString = this.cqlLibraryMap.get(name);

        if (cqlString == null || cqlString.length() < 1 || cqlString.isEmpty()) {
            throw new IllegalArgumentException(String.format("Could not load source for library %s.", libraryIdentifier.getId()));
        }

        InputStream librarySource = new ByteArrayInputStream(this.cqlLibraryMap.get(name).getBytes(StandardCharsets.UTF_8));

        return librarySource;
    }
}
