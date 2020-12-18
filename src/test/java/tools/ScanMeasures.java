package tools;

import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ScanMeasures {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/matprod?serverTimezone=UTC&characterEncoding=latin1&useConfigs=maxPerformance";
    private static final String DB_USER = "root";
    private static final String DB_P = "password";

    public static void main(String args[]) {
        System.err.println("1.0.0".matches("\\d*\\.\\d*\\.\\d"));
        System.err.println("1.0.000".matches("\\d*\\.\\d*\\.\\d"));

        File measuresDir = new File("temp/invalidIncludes");
        measuresDir.mkdirs();
        StringBuilder output = new StringBuilder();
        int counter = 0;
        try (var c = getConnection()) {
            try (var s = c.createStatement()) {
                try (var r = s.executeQuery("select m.CQL_NAME,m.version,m.revision_number,xml.MEASURE_XML from MEASURE_XML xml, MEASURE m where xml.MEASURE_ID = m.ID and m.DRAFT='1'")) {
                    while (r.next()) {
                        counter++;
                        String cqlName = r.getString(1);
                        String version = r.getString(2);
                        String revision = r.getString(3);
                        String matXml = r.getString(4);

                        CQLModel model = CQLUtilityClass.getCQLModelFromXML(matXml);
                        if (!CollectionUtils.isEmpty(model.getCqlIncludeLibrarys())) {
                            model.getCqlIncludeLibrarys().forEach(l -> {
                                if (l.getVersion().matches("\\d*\\.\\d*\\.\\d")) {
                                    output.append("Found invalid version in measure: " + cqlName + " " +
                                            version + "." + revision +
                                            " Lib: " + l.getCqlLibraryName() + " " + l.getVersion() + "\n");
                                    try (var w = new FileWriter("temp/invalidIncludes/" + cqlName + "-" + version + "-" + revision + ".xml", false)) {
                                        w.write(matXml);
                                    } catch (IOException ioe) {
                                        throw new RuntimeException(ioe);
                                    }
                                }
                            });
                        }
                    }
                    output.append("Scanned " + counter + " measures.");
                    System.err.println(output);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
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