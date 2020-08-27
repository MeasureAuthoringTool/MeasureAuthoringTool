package cqltoelm;

import org.cqframework.cql.tools.formatter.CqlFormatterVisitor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * The CQL Formatter Tool wrapper.
 *
 * @author Jack Meyer (7/25/2017)
 */
public class CQLFormatter {

    public boolean useSpaces;

    public CQLFormatter() {
        useSpaces = false;
    }

    public CQLFormatter(boolean useSpaces) {
        this.useSpaces = useSpaces;
    }

    /**
     * Formats the string passed to it
     * @param cqlString the cql string to format
     */
    public String format(String cqlString) throws IOException {

        InputStream is = null;
        if(!cqlString.isEmpty()) {
            is = new ByteArrayInputStream(cqlString.getBytes(StandardCharsets.UTF_8));
        }

        return format(is, useSpaces);
    }

    /**
     * Formats the file passed to it
     * @param cqlFile the cql file to format
     */
    public void format(File cqlFile) throws IOException {
        InputStream is = null;
        if (cqlFile != null) {
            is = new FileInputStream(cqlFile);
        }

        String output = format(is, useSpaces);

        PrintWriter writer = new PrintWriter(new FileWriter(cqlFile));
        writer.println(output);
        writer.flush();
        writer.close();
    }

    /**
     * Formats the stream passed to it and returns a formatted cql string
     * @param is the input stream
     * @return the formatted cql string
     * @throws IOException
     */
    private String format(InputStream is, boolean useSpaces) throws IOException {
//        CqlFormatterVisitor.setUseSpaces(useSpaces);
        CqlFormatterVisitor.FormatResult result = CqlFormatterVisitor.getFormattedOutput(is);

        if(result != null) {
            return result.getOutput();
        }

        else {
            return "";
        }
    }

    /**
     * Converts a cql file to a cql string
     * @param file the file to convert
     * @return the cql string
     */
    private static String cqlFileToString(File file) {
        byte[] bytes = new byte[0];
        try {
            bytes = Files.readAllBytes(file.toPath());
            return new String(bytes, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isUseSpaces() {
        return useSpaces;
    }

    public void setUseSpaces(boolean useSpaces) {
        useSpaces = useSpaces;
    }

    public static void main(String[] args) throws IOException {
        File file = new File("C:\\Users\\jmeyer\\Desktop\\test.cql");
//        String toFormat = cqlFileToString(file);

        CQLFormatter formatter = new CQLFormatter(true);
        formatter.format(file);
    }
}
