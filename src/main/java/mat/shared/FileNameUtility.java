package mat.shared;

import mat.model.clause.Measure;
import mat.server.export.ExportResult;
import mat.server.util.MeasureUtility;

import java.text.DecimalFormat;

public class FileNameUtility {

    private static final DecimalFormat revisionFormat = new DecimalFormat("000");

    public static String getExportBundleZipName(Measure measure) {
        return getExportFileName(measure) + ".zip";
    }

    public static String getBulkExportZipName(String name) {
        return name.replaceAll("\\W", "") + "-Artifacts.zip";
    }

    public static String getExportFileName(Measure measure) {
        return replaceUnderscores(measure.getaBBRName()) + "-v" +
                getMeasureVersion(measure) + "-" + measure.getMeasureModel() + "-" + getModelVersion(measure);
    }

    // exportResult.getCqlLibraryName() includes cqlLibraryName+-v+libraryVersion
    public static String getExportCqlLibraryFileName(ExportResult exportResult, Measure measure) {
        return replacePeriods(replaceUnderscores(exportResult.getCqlLibraryName())) + "-" + measure.getMeasureModel() + "-" +
                replacePeriods(exportResult.getCqlLibraryModelVersion());
    }

    private static String getModelVersion(Measure measure) {
        return replacePeriods(measure.isFhirMeasure() ? measure.getFhirVersion() : measure.getQdmVersion());
    }

    public static String getMeasureVersion(Measure measure) {
        String measureVersion = MeasureUtility.formatVersionText(measure.getVersion());
        if (measure.isFhirMeasure()) {
            return replacePeriods(measure.isDraft() ?
                measureVersion + "." + revisionFormat.format(Integer.parseInt(measure.getRevisionNumber())) : measureVersion);
        } else {
            return replacePeriods(measureVersion + "." + revisionFormat.format(Integer.parseInt(measure.getRevisionNumber())));
        }
    }

    public static String getExportFolderNameWithExtension(Measure measure, String fileNameExtension) {
        return getExportFileName(measure) + "-" + fileNameExtension;
    }

    public static String replaceUnderscores(String s) {
        return s.replace('_', '-');
    }

    public static String replacePeriods(String s) {
        return s.replace('.','-');
    }

    public static String getEmeasureXMLName(String name) {
        return name.replaceAll("\\W", "") + "-eCQM.xml";
    }

    public static String getEmeasureXLSName(String name, String packageDate) {
        packageDate = packageDate.replace(':', '.');
        return name.replaceAll("\\W", "") + "-" + packageDate + ".xls";
    }

    public String getParentPath(String name) {
        return name.replaceAll("\\W", "") + "-Artifacts";
    }

    public static String getEmeasureHumanReadableName(String name) {
        return name.replaceAll("\\W", "") + "-HumanReadable.html";
    }

    public static String getCSVFileName(String name, String currentTime) {
        return (name.concat(currentTime)).replaceAll("\\W", "").concat(".csv");
    }

    public static String getCQLFileName(String name) {
        return name.replaceAll("\\W", "") + "-CQL.cql";
    }
}
