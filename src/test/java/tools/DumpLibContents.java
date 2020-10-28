package tools;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Library;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;

public class DumpLibContents {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/local-prd-sbx-dump?serverTimezone=UTC&characterEncoding=latin1&useConfigs=maxPerformance";
    private static final String DB_USER = "root";
    private static final String DB_P = "password";

    private static final String[] IDS = {
            "FHIRHelpers-4-0-001",
            "AdultOutpatientEncounters-FHIR4-2-0-000",
            "AdvancedIllnessandFrailtyExclusion-FHIR4-5-0-000",
            "Hospice-FHIR4-2-0-000",
            "MATGlobalCommonFunctions-FHIR4-5-0-000",
            "SupplementalDataElements-FHIR4-2-0-000",
            "TJCOverall-FHIR4-5-0-000",
            "VTEICU-FHIR4-4-0-000"
    };

    private static final String SELECT_EXPORT = "SELECT * FROM  CQL_LIBRARY_EXPORT where CQL_LIBRARY_ID in (" +
            "'FHIRHelpers-4-0-001'," +
            "'AdultOutpatientEncounters-FHIR4-2-0-000'," +
            "'AdvancedIllnessandFrailtyExclusion-FHIR4-5-0-000'," +
            "'Hospice-FHIR4-2-0-000'," +
            "'MATGlobalCommonFunctions-FHIR4-5-0-000'," +
            "'SupplementalDataElements-FHIR4-2-0-000'," +
            "'TJCOverall-FHIR4-5-0-000'," +
            "'VTEICU-FHIR4-4-0-000'" +
            ")";

    public static void main(String args[]) {
        FhirContext context = FhirContext.forR4();
        IParser jsonParser = context.newJsonParser().setPrettyPrint(true);
        File file = new File("temp/commonLibs");
        file.mkdirs();
        try (var c = getConnection()) {
            System.err.println("Connected to DB.");
            try (Statement s = c.createStatement()) {
                try (ResultSet r = s.executeQuery(SELECT_EXPORT)) {
                    while (r.next()) {
                        String fhirJson = r.getString("JSON");
                        Library lib = jsonParser.parseResource(Library.class, fhirJson);
                        for (Attachment content : lib.getContent()) {
                            String contentType = content.getContentType();
                            if (contentType.equals("text/cql")) {
                                FileWriter writer = new FileWriter(new File("temp/commonLibs/" + lib.getName() + ".cql"), false);
                                writer.write(new String(Base64.getDecoder().decode(content.getData())));
                                writer.close();
                                System.err.println("Wrote " + "temp/commonLibs/" + lib.getName() + ".cql");
                            } else if (contentType.equals("application/elm+json")) {
                                FileWriter writer = new FileWriter(new File("temp/commonLibs/" + lib.getName() + "_elm.json"), false);
                                writer.write(new String(Base64.getDecoder().decode(content.getData())));
                                writer.close();
                                System.err.println("Wrote " + "temp/commonLibs/" + lib.getName() + "_elm.json");
                            } else if (contentType.equals("application/elm+xml")) {
                                FileWriter writer = new FileWriter(new File("temp/commonLibs/" + lib.getName() + "_elm.xml"), false);
                                writer.write(new String(Base64.getDecoder().decode(content.getData())));
                                writer.close();
                                System.err.println("Wrote " + "temp/commonLibs/" + lib.getName() + "_elm.xml");
                            }
                        }
                    }
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_P);
    }
}