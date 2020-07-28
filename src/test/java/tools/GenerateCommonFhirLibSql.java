package tools;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GenerateCommonFhirLibSql {
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

    private static final String INSERT_CQL_LIB = "INSERT INTO CQL_LIBRARY(" +
            "ID,\n" +
            "MEASURE_ID,\n" +
            "SET_ID,\n" +
            "CQL_NAME,\n" +
            "DRAFT,\n" +
            "VERSION,\n" +
            "FINALIZED_DATE,\n" +
            "RELEASE_VERSION,\n" +
            "OWNER_ID,\n" +
            "LOCKED_USER,\n" +
            "LOCKED_OUT_DATE,\n" +
            "CQL_XML,\n" +
            "REVISION_NUMBER,\n" +
            "QDM_VERSION,\n" +
            "LAST_MODIFIED_ON,\n" +
            "LAST_MODIFIED_BY,\n" +
            "LIBRARY_MODEL,\n" +
            "FHIR_VERSION) VALUES(\n" +
            "${ID},\n" +
            "null,\n" +
            "${SET_ID},\n" +
            "${CQL_NAME},\n" +
            "0,\n" +
            "${VERSION},\n " +
            "null,\n" +
            "'v6.0',\n" +
            "${OWNER_ID},\n" +
            "null,\n" +
            "null,\n" +
            "${CQL_XML},\n" +
            "${REVISION_NUMBER},\n" +
            "null,\n" +
            "now(),\n" +
            "${OWNER_ID},\n" +
            "'FHIR',\n" +
            "'4.0.1'\n);";

    private static final String INSERT_EXPORT = "INSERT INTO CQL_LIBRARY_EXPORT(ID, CQL_LIBRARY_ID, CQL, ELM, JSON) values (\n" +
            "${EXPORT_ID},\n${CQL_LIB_ID},\n${CQL},\n${ELM},\n${JSON}\n);";

    public static void main(String args[]) {


        File out = new File("target/out.sql");
        out.delete();
        try (var c = getConnection();
             var writer = new FileWriter(out)) {
            System.err.println("Connected to DB.");

            Arrays.stream(IDS).forEach(id -> {
                //First step update versions based on the IDs.
                try (Statement s = c.createStatement()) {
                    String[] idSplit = id.split("\\-");
                    String version = idSplit[idSplit.length -3] + "." +  idSplit[idSplit.length - 2];
                    String revision = idSplit[idSplit.length - 1];
                    s.executeUpdate("update CQL_LIBRARY set version=" + version + ", revision_number =" + revision + " where id = " + tic(id));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                try (Statement s = c.createStatement()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("ID", tic(id));
                    map.put("CQL_LIB_ID", tic(id));
                    map.put("EXPORT_ID", tic(createId()));
                    //Gen lib sql.
                    try (ResultSet r = s.executeQuery("select * from CQL_LIBRARY where ID=" + tic(escape(id)) + ";")) {
                        System.err.println("\n\nSelected details from CQL_LIBRARY for " + id);
                        r.next();
                        map.put("CQL_NAME", tic(r.getString("CQL_NAME")));
                        map.put("SET_ID", tic(r.getString("SET_ID")));
                        map.put("VERSION", r.getBigDecimal("VERSION").toString());
                        map.put("OWNER_ID", tic(r.getString("OWNER_ID")));
                        map.put("CQL_XML", tic(escape(r.getString("CQL_XML"))));
                        map.put("REVISION_NUMBER", "" + r.getInt("REVISION_NUMBER"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    //Gen lib export sql.
                    try (ResultSet r = s.executeQuery("select * from CQL_LIBRARY_EXPORT where CQL_LIBRARY_ID=" + tic(escape(id)) + ";")) {
                        r.next();
                        System.err.println("Selected details from CQL_LIBRARY_EXPORT for " + id);
                        map.put("CQL", tic(escape(r.getString("CQL"))));
                        map.put("ELM", tic(escape(r.getString("ELM"))));
                        map.put("JSON", tic(escape(r.getString("JSON"))));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                    System.err.println("Wrote sql for " + id + "");
                    writer.write("\n\n\n########" + id + "##########\n");
                    writer.write(runTemplate(INSERT_CQL_LIB, map)+ "\n");
                    writer.write(runTemplate(INSERT_EXPORT, map)+ "\n");
                    System.err.println("Wrote sql for " + id + "");
                } catch (SQLException|IOException e) {
                    throw new RuntimeException(e);
                }
            });
            System.err.println("\nComplete. Results written to: target/out.sql");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_P);
    }

    private static String tic(String s) {
        return '\'' + s + '\'';
    }

    private static String escape(String s) {
        String result = StringUtils.replace(s,"'","''");
        return StringUtils.replace(result,"\n","\\n");
    }

    private static String createId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    private static String runTemplate(String t, Map<String, String> context) {
        String result = t;
        for (String key : context.keySet()) {
            result = StringUtils.replace(result,"${" + key + "}",context.get(key));
        }
        return result;
    }
}