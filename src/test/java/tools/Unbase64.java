package tools;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import mat.client.shared.MatRuntimeException;
import org.apache.commons.io.IOUtils;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class Unbase64 {

    public static String resourceAsString(String resource) {
        try (InputStream i = Unbase64.class.getResourceAsStream(resource)) {
            return IOUtils.toString(i);
        } catch (IOException ioe) {
            throw new MatRuntimeException(ioe);
        }
    }

    public static void main (String[] args) throws Exception{
        String json = resourceAsString("/CMS104.json");
        FhirContext context = FhirContext.forR4();
        IParser parser = context.newJsonParser();
        Bundle bundle = parser.parseResource(Bundle.class,json);
        bundle.getEntry().stream().filter(e -> e.getResource() instanceof Library).forEach(e -> {
            Library l = (Library) e.getResource();

            byte[] encoded = l.getContent().get(1).getData();
            String decoded = new String(Base64.getDecoder().decode(encoded));
            File file = new File("temp/decode");
            file.mkdirs();
            try (FileWriter w = new FileWriter(new File(file,l.getName() + ".elm.xml"))) {
                w.write(decoded);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }

            encoded = l.getContent().get(2).getData();
            decoded = new String(Base64.getDecoder().decode(encoded));
            file = new File("temp/decode");
            file.mkdirs();
            try (FileWriter w = new FileWriter(new File(file,l.getName() + ".elm.json"))) {
                w.write(decoded);
            } catch (IOException ioe) {
                throw new RuntimeException(ioe);
            }
        });
    }
}
