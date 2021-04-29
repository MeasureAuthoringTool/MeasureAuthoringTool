package mat.server.util;


import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertTrue;

public class PatientBasedQdmValidatorTest {
    private PatientBasedQdmValidator patientBasedQDMValidator;

    private String getXML() {
        File inputXmlFile = new File(Objects.requireNonNull(this.getClass()
                .getResource("/patient-based/measure.xml")).getFile());

        try {
            return new String(Files.readAllBytes(inputXmlFile.toPath()));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Before
    public void setUp() {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XmlProcessor xmlProcessor = new XmlProcessor(getXML());
        patientBasedQDMValidator = new PatientBasedQdmValidator(xPath, xmlProcessor);
    }


    @Test
    public void validate() {
        List<String> errors = null;
        try {
            errors = patientBasedQDMValidator.validate(null, "ratio");
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        assertTrue(errors.isEmpty());
    }

}