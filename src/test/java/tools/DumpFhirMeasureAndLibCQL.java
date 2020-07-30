package tools;

import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DumpFhirMeasureAndLibCQL {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/local-prd-sbx-dump?serverTimezone=UTC&characterEncoding=latin1&useConfigs=maxPerformance";
    private static final String DB_USER = "root";
    private static final String DB_P = "password";

    public static void main(String args[]) {
        File measuresDir = new File("temp/fhir/measures");
        File libsDir = new File("temp/fhir/libs");
        measuresDir.mkdirs();
        libsDir.mkdirs();
        try (var c = getConnection()) {
            System.err.println("Connected to DB.");
            try (Statement s = c.createStatement()) {
                try (ResultSet r = s.executeQuery("select m.CQL_NAME,m.version,m.revision_number,xml.MEASURE_XML from MEASURE_XML xml, MEASURE m where xml.MEASURE_ID = m.ID and m.MEASURE_MODEL='FHIR'")) {
                    while (r.next()) {
                        String cqlName = r.getString(1);
                        String version = r.getString(2);
                        String revision = r.getString(3);
                        String matXml = r.getString(4);

                        CQLModel model = CQLUtilityClass.getCQLModelFromXML(matXml);
                        String cql = CQLUtilityClass.getCqlString(model, "").getLeft();


                        File measure = new File(measuresDir, cqlName + "_v" + version + "." + revision + ".cql");

                        try (FileWriter w = new FileWriter(measure, false)) {
                            w.write(cql);
                        }
                    }
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            try (var s = c.createStatement()) {
                try (ResultSet r = s.executeQuery("select l.CQL_NAME,l.version,l.revision_number,l.CQL_XML from CQL_LIBRARY l where l.LIBRARY_MODEL='FHIR'")) {
                    while (r.next()) {
                        String cqlName = r.getString(1);
                        String version = r.getString(2);
                        String revision = r.getString(3);
                        String matXml = r.getString(4);

                        CQLModel model = CQLUtilityClass.getCQLModelFromXML(matXml);
                        String cql = CQLUtilityClass.getCqlString(model, "").getLeft();

                        File lib = new File(libsDir, cqlName + "_v" + version + "." + revision + ".cql");

                        try (FileWriter w = new FileWriter(lib, false)) {
                            w.write(cql);
                        }
                    }
                } catch (IOException | SQLException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
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