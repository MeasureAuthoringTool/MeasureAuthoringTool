package mat.server.service.impl.helper;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ExportResultParserTest {
    @Test //TODO Hasn't been run for a time, review.
    void parseDataRequirement() {
        String json = getStringFromResource();
        ExportResultParser exportResultParser = new ExportResultParser(json);
        List<String> strings =  exportResultParser.parseDataRequirement();
        assertEquals(5, strings.size() );
    }

    @SneakyThrows
    private String getStringFromResource() {
        File inputXmlFile = new File(this.getClass().getResource("/jsonExportResult.json").getFile());

        return new String(Files.readAllBytes(inputXmlFile.toPath()));
    }
}