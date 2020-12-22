package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import mat.client.shared.MatContext;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.ModelTypeHelper;
import mat.server.export.ExportResult;
import mat.server.logging.LogFactory;
import mat.shared.FileNameUtility;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * User Story ID 357
 * Delegate creation of a zip file containing eMeasure artifacts for export
 * used by SimpleEMeasureServiceImpl.java
 *
 * @author aschmidt
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ZipPackager {
    private static final String MEASURE = "measure";
    private static final String INCLUDED_LIBRARY = "included-library";
    private static final String SEPARATOR = "-";
    private static final String LIBRARY = "library";
    private static final String ELM = "elm";
    private static final String HUMAN_READABLE = "humanReadable";

    private Log log = LogFactory.getLog(ZipPackager.class);

    @Autowired
    private MeasureDAO measureDAO;

    @Autowired
    private MeasureExportDAO measureExportDAO;

    @Autowired
    private CQLLibraryExportDAO cqlLibraryExportDAO;

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private ExportUtility exportUtility;

    @Autowired
    private FhirContext fhirContext;

    /**
     * Adds the bytes to zip.
     *
     * @param path  the path
     * @param input the input
     * @param zip   the zip
     * @throws Exception the exception
     */
    public void addBytesToZip(String path, byte[] input, ZipOutputStream zip) throws Exception {
        ZipEntry entry = new ZipEntry(path);
        entry.setSize(input.length);
        zip.putNextEntry(entry);
        zip.write(input);
        zip.closeEntry();
    }

    /**
     * Creates the bulk export zip.
     *
     * @param emeasureName    the emeasure name
     * @param exportDate      the export date
     * @param wkbkbarr        the wkbkbarr
     * @param emeasureXMLStr  the emeasure xml str
     * @param emeasureHTMLStr the emeasure html str
     * @param emeasureXSLUrl  the emeasure xsl url
     * @param packageDate     the package date
     * @param simpleXmlStr    the simple xml str
     * @param filesMap        the files map
     * @param seqNum          the seq num
     * @throws Exception the exception
     */
    public void createBulkExportZip(String emeasureName, Date exportDate, byte[] wkbkbarr, String emeasureXMLStr, String emeasureHTMLStr,
                                    String emeasureXSLUrl, String packageDate, String simpleXmlStr, Map<String, byte[]> filesMap, String seqNum, String releaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult, ExportResult jsonExportResult) throws Exception {

        FileNameUtility fnu = new FileNameUtility();
        try {

            URL u = new URL(emeasureXSLUrl);
            int contentLength = u.openConnection().getContentLength();
            InputStream openStream = u.openStream();
            byte[] emeasureXSLBarr = new byte[contentLength];
            openStream.read(emeasureXSLBarr);
            openStream.close();

            String parentPath = "";
            String emeasureXSLPath = "";
            String emeasureXMLPath = "";
            String emeasureHumanReadablePath = "";
            if (releaseVersion.contains(".")) {
                releaseVersion = releaseVersion.replace(".", "-");
            }
            log.debug("Release version zip " + releaseVersion);
            parentPath = replaceUnderscores(fnu.getParentPath(seqNum + "-" + emeasureName + "-" + releaseVersion));
            emeasureXSLPath = replaceUnderscores(parentPath + File.separator + "xslt" + File.separator + "eMeasure.xsl");
            emeasureXMLPath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureXMLName(emeasureName + "_" + releaseVersion));
            emeasureHumanReadablePath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureHumanReadableName(emeasureName + "_" + releaseVersion));

            filesMap.put(emeasureXSLPath, emeasureXSLBarr);
            filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
            filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());

            if (MatContext.get().isCQLMeasure(releaseVersion)) {
                addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult);
            }

        } catch (Exception e) {
            log.error("createBulkExportZip", e);
        }
    }

    /**
     * Gets the zip barr.
     *
     * @param emeasureName    the emeasure name
     * @param emeasureXMLStr  the emeasure xml str
     * @param emeasureHTMLStr the emeasure html str
     * @param emeasureXSLUrl  the emeasure xsl url
     * @return the zip barr
     * @throws Exception the exception
     */
    public byte[] getZipBarr(String emeasureName, String releaseVersion, String emeasureXMLStr,
                             String emeasureHTMLStr, String emeasureXSLUrl) throws Exception {
        byte[] ret = null;

        FileNameUtility fnu = new FileNameUtility();

        try {
            URL u = new URL(emeasureXSLUrl);
            int contentLength = u.openConnection().getContentLength();
            InputStream openStream = u.openStream();
            byte[] emeasureXSLBarr = new byte[contentLength];
            openStream.read(emeasureXSLBarr);
            openStream.close();

            String parentPath = "";
            String emeasureXSLPath = "";
            String emeasureXMLPath = "";
            String emeasureHumanReadablePath = "";
            String eReleaseVersion = releaseVersion;
            if (eReleaseVersion.contains(".")) {
                eReleaseVersion = releaseVersion.replace(".", "-");
            }
            log.debug("Release version zip " + eReleaseVersion);
            parentPath = replaceUnderscores(fnu.getParentPath(emeasureName + eReleaseVersion));
            emeasureXSLPath = replaceUnderscores(parentPath + File.separator + "xslt" + File.separator + "eCQM.xsl");
            emeasureXMLPath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureXMLName(emeasureName + releaseVersion));
            emeasureHumanReadablePath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureHumanReadableName(emeasureName + releaseVersion));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);

            addBytesToZip(emeasureXSLPath, emeasureXSLBarr, zip);
            addBytesToZip(emeasureXMLPath, emeasureXMLStr.getBytes(), zip);
            addBytesToZip(emeasureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);

            zip.close();
            ret = baos.toByteArray();
        } catch (Exception e) {
            log.error("getZipBarr", e);
        }
        return ret;
    }

    /**
     * @param emeasureName
     * @param zip
     * @param emeasureHTMLStr
     * @param emeasureXMLStr
     * @param cqlExportResult
     * @param elmExportResult
     * @param jsonExportResult
     * @param currentRealeaseVersion
     * @param parentPath
     * @param measureId
     */
    public void getZipBarr(String emeasureName, ZipOutputStream zip, String emeasureHTMLStr, String emeasureXMLStr,
                           ExportResult cqlExportResult, ExportResult elmExportResult, ExportResult jsonExportResult,
                           String currentRealeaseVersion, String parentPath, String measureId) {

        Measure measure = measureDAO.getMeasureByMeasureId(measureId);
        MeasureExport measureExport = measureExportDAO.findByMeasureId(measureId);

        try {
            String measureReleaseVersion = currentRealeaseVersion;
            if (currentRealeaseVersion.contains(".")) {
                currentRealeaseVersion = currentRealeaseVersion.replace(".", "_");
            }

            if (!measure.isFhirMeasure()) {
                String measureHumanReadablePath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureHumanReadableName(emeasureName + "_" + currentRealeaseVersion));
                addBytesToZip(measureHumanReadablePath, emeasureHTMLStr.getBytes(), zip);

                String measureXMLPath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureXMLName(emeasureName + "_" + currentRealeaseVersion));
                addBytesToZip(measureXMLPath, emeasureXMLStr.getBytes(), zip);

                if (isV5OrGreater(measureReleaseVersion)) {
                    addFileToZip(measure, cqlExportResult, parentPath, "cql", zip);
                    addFileToZip(measure, elmExportResult, parentPath, "xml", zip);
                    addFileToZip(measure, jsonExportResult, parentPath, "json", zip);
                }
            } else {
                String measureJsonBundle = buildMeasureBundle(fhirContext, measureExport.getMeasureJson(), measureExport.getFhirIncludedLibsJson());
                addBytesToZip(parentPath + File.separator + "measure-json-bundle.json",
                        measureJsonBundle.getBytes(),
                        zip);

                addBytesToZip(parentPath + File.separator + "measure-xml-bundle.xml",
                        convertToXmlBundle(measureJsonBundle).getBytes(),
                        zip);

                addBytesToZip(parentPath + File.separator + "human-readable.html",
                        emeasureHTMLStr.getBytes(),
                        zip);

            }
        } catch (Exception e) {
            log.error("getZipBarr", e);
        }
    }


    private boolean isV5OrGreater(String version) {
        boolean result = false;
        try {
            result = Double.parseDouble(version.substring(1, version.length())) >= 5.0;
        } catch (RuntimeException re) {
            log.error("Error formatting version " + version, re);
        }
        return result;
    }

    /**
     * Creates the bulk export zip.
     *
     * @param emeasureName          the emeasure name
     * @param wkbkbarr              the wkbkbarr
     * @param emeasureXMLStr        the emeasure xml str
     * @param emeasureHTMLStr       the emeasure html str
     * @param packageDate           the package date
     * @param simpleXmlStr          the simple xml str
     * @param filesMap              the files map
     * @param seqNum                the seq num
     * @param currentReleaseVersion
     * @param elmExportResult
     * @param jsonExportResult
     * @throws Exception the exception
     */
    public void createBulkExportZip(String emeasureName, byte[] wkbkbarr, String emeasureXMLStr, String emeasureHTMLStr,
                                    String packageDate, String simpleXmlStr, Map<String, byte[]> filesMap, String seqNum,
                                    String currentReleaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult,
                                    ExportResult jsonExportResult, String parentPath, MeasureExport measureExport) throws Exception {
        try {
            boolean isCQLMeasure = false;
            String emeasureHumanReadablePath = "";
            String emeasureXMLPath = "";
            isCQLMeasure = MatContext.get().isCQLMeasure(currentReleaseVersion);

            if (currentReleaseVersion.contains(".")) {
                currentReleaseVersion = currentReleaseVersion.replace(".", "-");
            }
            if (!ModelTypeHelper.isFhir(measureExport.getMeasure().getMeasureModel())) {
                emeasureHumanReadablePath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureHumanReadableName(emeasureName + "-" + currentReleaseVersion));
                emeasureXMLPath = replaceUnderscores(parentPath + File.separator + FileNameUtility.getEmeasureXMLName(emeasureName + "-" + currentReleaseVersion));
                filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());

                if (isCQLMeasure) {
                    addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult);
                }
            } else {
                String measureJsonBundle = buildMeasureBundle(fhirContext, measureExport.getMeasureJson(), measureExport.getFhirIncludedLibsJson());
                String emeasureJsonBundlePath = replaceUnderscores(parentPath + File.separator + "measure-json-bundle.json");
                String emeasureXmlBundlePath = replaceUnderscores(parentPath + File.separator + "measure-xml-bundle.xml");
                emeasureHumanReadablePath = replaceUnderscores(parentPath + File.separator + "human-readable.html");

                filesMap.put(emeasureJsonBundlePath, measureJsonBundle.getBytes());
                filesMap.put(emeasureXmlBundlePath, convertToXmlBundle(measureJsonBundle).getBytes());
            }
            filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());

        } catch (Exception e) {
            log.error("createBulkExportZip", e);
        }

    }

    /**
     * @param filesMap
     * @param cqlExportResult
     * @param elmExportResult
     * @param parentPath
     * @param jsonExportResult
     */
    private void addVersion5Exports(Map<String, byte[]> filesMap,
                                    ExportResult cqlExportResult, ExportResult elmExportResult,
                                    String parentPath, ExportResult jsonExportResult) {

        if (cqlExportResult.includedCQLExports.size() > 0) {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "cql");
            filesMap.put(filePath, cqlExportResult.export.getBytes());

            for (ExportResult includedResult : cqlExportResult.getIncludedCQLExports()) {
                filePath = replaceUnderscores(parentPath + File.separator + includedResult.getCqlLibraryName() + "." + "cql");
                filesMap.put(filePath, includedResult.export.getBytes());
            }
        } else {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "cql");
            filesMap.put(filePath, cqlExportResult.export.getBytes());
        }

        if (elmExportResult.includedCQLExports.size() > 0) {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "xml");
            filesMap.put(filePath, elmExportResult.export.getBytes());

            for (ExportResult includedResult : elmExportResult.getIncludedCQLExports()) {
                filePath = replaceUnderscores(parentPath + File.separator + includedResult.getCqlLibraryName() + "." + "xml");
                filesMap.put(filePath, includedResult.export.getBytes());
            }
        } else {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "xml");
            filesMap.put(filePath, elmExportResult.export.getBytes());
        }

        if (jsonExportResult.includedCQLExports.size() > 0) {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "json");
            filesMap.put(filePath, jsonExportResult.export.getBytes());

            for (ExportResult includedResult : jsonExportResult.getIncludedCQLExports()) {
                filePath = replaceUnderscores(parentPath + File.separator + includedResult.getCqlLibraryName() + "." + "json");
                filesMap.put(filePath, includedResult.export.getBytes());
            }
        } else {
            String filePath = "";
            filePath = replaceUnderscores(parentPath + File.separator + cqlExportResult.getCqlLibraryName() + "." + "json");
            filesMap.put(filePath, jsonExportResult.export.getBytes());
        }
    }


    public byte[] getCQLZipBarr(Measure measure, ExportResult export, String extension) {
        byte[] ret = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);

            String parentPath = "";
            parentPath = export.measureName + "-" + extension;

            addFileToZip(measure, export, parentPath, extension, zip);

            zip.close();
            ret = baos.toByteArray();
            export.zipbarr = ret;
        } catch (Exception er) {
            er.printStackTrace();
        }
        return ret;
    }

    /**
     * @param export
     * @param extension
     * @param parentPath
     * @param zip
     * @throws Exception
     */
    private void addFileToZip(Measure measure, ExportResult export, String parentPath, String extension,
                              ZipOutputStream zip) throws Exception {

        String cqlFilePath = "";
        cqlFilePath = replaceUnderscores(measure.isFhirMeasure() ? parentPath + File.separator + LIBRARY + "-" + export.getCqlLibraryName() + "." + extension
                : parentPath + File.separator + export.getCqlLibraryName() + "." + extension);
        addBytesToZip(cqlFilePath, export.export.getBytes(), zip);

        for (ExportResult includedResult : export.getIncludedCQLExports()) {
            cqlFilePath = replaceUnderscores(parentPath + File.separator + includedResult.getCqlLibraryName() + "." + extension);
            addBytesToZip(cqlFilePath, includedResult.export.getBytes(), zip);
        }
    }

    public String buildMeasureBundle(FhirContext fhirContext, String measureJson, String libBundleJson) {
        // http://build.fhir.org/ig/HL7/cqf-measures/StructureDefinition-measure-bundle-cqfm.html
        IParser jsonParser = fhirContext.newJsonParser();
        jsonParser.setPrettyPrint(true);
        var measure = jsonParser.parseResource(org.hl7.fhir.r4.model.Measure.class, measureJson);
        var libBundle = jsonParser.parseResource(Bundle.class, libBundleJson);

        Bundle result = new Bundle();
        result.getMeta().addProfile("http://hl7.org/fhir/us/cqfmeasures/StructureDefinition/measure-bundle-cqfm");
        result.setType(Bundle.BundleType.TRANSACTION);
        result.addEntry().setResource(measure).getRequest()
                .setUrl("Measure/" + getFhirId(measure))
                .setMethod(Bundle.HTTPVerb.PUT);

        libBundle.getEntry().forEach(e ->
                result.addEntry().setResource(e.getResource()).setRequest(e.getRequest()));

        return jsonParser.encodeResourceToString(result);
    }

    public String convertToXmlBundle(String bundleJson) {
        IParser jsonParser = fhirContext.newJsonParser();
        IParser xmlParser = fhirContext.newXmlParser();
        xmlParser.setPrettyPrint(true);
        var bundle = jsonParser.parseResource(org.hl7.fhir.r4.model.Bundle.class, bundleJson);
        String bundleXml = xmlParser.encodeResourceToString(bundle);
        return bundleXml;
    }

    private String getFhirId(Resource r) {
        String id = r.getId();
        int endIndex = id.indexOf("/_history");
        if (endIndex >= 0) {
            id = id.substring(0, endIndex);
        }
        int startIndex = id.lastIndexOf('/') + 1;
        return id.substring(startIndex);
    }

    private String replaceUnderscores(String s) {
        return s.replace('_', '-');
    }
}
