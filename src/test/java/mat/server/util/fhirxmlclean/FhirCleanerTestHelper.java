package mat.server.util.fhirxmlclean;

import mat.server.util.XmlProcessor;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public interface FhirCleanerTestHelper {
    default String getXML() {
        File inputXmlFile = new File(this.getClass().getResource("/clean.xml").getFile());

        try {
            return new String(Files.readAllBytes(inputXmlFile.toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    default boolean xmlContainsOnlyOneDataElement(String xmlToSearch, String tag, String data) {
        String startTag = "<" + tag + ">";
        int start = xmlToSearch.indexOf(startTag) + startTag.length();
        int end = xmlToSearch.indexOf("</" + tag + ">", start);

        String sub = xmlToSearch.substring(start, end);

        int matches = StringUtils.countMatches(sub, data);

        return matches == 1;
    }

    default String convertXmlToString(XmlProcessor processor) {
        return new String(processor.transform(processor.getOriginalDoc()).getBytes());
    }
}
