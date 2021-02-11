package mat.shared;

import mat.model.clause.Measure;
import mat.server.util.MeasureUtility;
import java.text.DecimalFormat;

/**
 * Delegate common File name creation behavior shared by ExportServlet.java and ZipPackager.java
 *
 * @author aschmidt
 */
public class FileNameUtility {


    private static DecimalFormat revisionFormat = new DecimalFormat("000");
    /**
     * Gets the eCQM xml name.
     *
     * @param name the name
     * @return the eCQM xml name
     */
    public static String getEmeasureXMLName(String name) {
        return name.replaceAll("\\W", "") + "-eCQM.xml";
    }

    /**
     * Gets the eCQM xls name.
     *
     * @param name        the name
     * @param packageDate the package date
     * @return the eCQM xls name
     */
    public static String getEmeasureXLSName(String name, String packageDate) {
        packageDate = packageDate.replace(':', '.');
        return name.replaceAll("\\W", "") + "-" + packageDate + ".xls";
    }

    /**
     * Gets the zip name.
     *
     * @param name the name
     * @return the zip name
     */
    public static String getZipName(String name) {
        return name.replaceAll("\\W", "") + "-Artifacts.zip";
    }

    public String getFhirZipName(Measure measure) {
        return measure.getaBBRName() + "-v" +
                getMeasureVersionFhir(measure) + "-" + measure.getMeasureModel() + "-" +
                measure.getFhirVersion().replace(".", "-") + ".zip";
    }

    /**
     * Gets the eCQM html name.
     *
     * @param name the name
     * @return the eCQM html name
     */
    public static String getEmeasureHTMLName(String name) {
        return name.replaceAll("\\W", "") + "-eCQM.html";
    }

    /**
     * Gets the simple xml name.
     *
     * @param name the name
     * @return the simple xml name
     */
    public static String getSimpleXMLName(String name) {
        return name.replaceAll("\\W", "") + "-SimpleXML.xml";
    }

    /**
     * Gets the parent path.
     *
     * @param name the name
     * @return the parent path
     */
    public String getParentPath(String name) {
        return name.replaceAll("\\W", "") + "-Artifacts";
    }

    public String getFhirExportFileName(Measure measure) {
        return measure.getaBBRName() + "-v" +
                getMeasureVersionFhir(measure) + "-" + measure.getMeasureModel() + "-" +
                measure.getFhirVersion().replace(".", "-");
    }

    public String getMeasureVersionFhir(Measure measure) {
        String mVersion = MeasureUtility.formatVersionText(measure.getVersion());
        String version = measure.isDraft() ? (mVersion + "." + revisionFormat.format(Integer.parseInt(measure.getRevisionNumber()))) : mVersion;

        return version.replace(".", "-");
    }


    /**
     * Gets the eCQM human readable name.
     *
     * @param name the name
     * @return the eCQM human readable name
     */
    public static String getEmeasureHumanReadableName(String name) {
        return name.replaceAll("\\W", "") + "-HumanReadable.html";
    }

    /**
     * Gets the value set xls name.
     *
     * @param name             the name
     * @param lastModifiedDate the last modified date
     * @return the value set xls name
     */
    public static String getValueSetXLSName(String name, String lastModifiedDate) {
        if (lastModifiedDate == null)
            lastModifiedDate = "DRAFT";
        else
            lastModifiedDate = lastModifiedDate.replace(':', '.');
        return name.replaceAll("\\W", "") + "-" + lastModifiedDate + ".xls";
    }

    /**
     * Gets the bulk zip name.
     *
     * @param name the name
     * @return the bulk zip name
     */
    public static String getBulkZipName(String name) {
        return name.replaceAll("\\W", "") + "-Artifacts.zip";
    }

    /**
     * Gets the cSV file name.
     *
     * @param name        the name
     * @param currentTime the current time
     * @return the cSV file name
     */
    public static String getCSVFileName(String name, String currentTime) {
        return (name.concat(currentTime)).replaceAll("\\W", "").concat(".csv");
    }

    public static String getCQLFileName(String name) {
        return name.replaceAll("\\W", "") + "-CQL.cql";
    }

    public static String getELMFileName(String name) {
        return name.replaceAll("\\W", "") + "-ELM.xml";
    }


    public static String getJsonFilePath(String measureName, String releaseVersion) {
        return "-" + measureName + "-" + releaseVersion + ".json";
    }

    public static String getHtmlFilePath(String measureName, String releaseVersion) {
        return "-" + measureName + "-" + releaseVersion + ".html";
    }
}

