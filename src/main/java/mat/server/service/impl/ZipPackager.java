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
import mat.server.export.ExportResult;
import mat.server.logging.LogFactory;
import mat.shared.FileNameUtility;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.logging.Log;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Library;
import org.hl7.fhir.r4.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
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

    public void createBulkExportZip(String emeasureXMLStr, String emeasureHTMLStr,
                                    String emeasureXSLUrl, Map<String, byte[]> filesMap,
                                    String releaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult,
                                    ExportResult jsonExportResult, Measure measure) throws Exception {
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
            parentPath = FileNameUtility.getExportFileName(measure);
            emeasureXSLPath = replaceUnderscores(parentPath + File.separator + "xslt" + File.separator + "eMeasure.xsl"); //Todo Rohit What the heck, why do users download xsl
            emeasureXMLPath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".xml";
            emeasureHumanReadablePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".html";

            filesMap.put(emeasureXSLPath, emeasureXSLBarr);
            filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());
            filesMap.put(emeasureHumanReadablePath, emeasureHTMLStr.getBytes());

            if (MatContext.get().isCQLMeasure(releaseVersion)) {
                addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult, measure);
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


    public String buildFhirMeasureJsonBundle(String measureId) {
        MeasureExport measureExport = measureExportDAO.findByMeasureId(measureId);

        return buildMeasureBundle(fhirContext, measureExport.getMeasureJson(), measureExport.getFhirIncludedLibsJson());
    }


    public void getZipBarr(ZipOutputStream zip,
                           String eMeasureHTMLStr,
                           String eMeasureXMLStr,
                           ExportResult cqlExportResult,
                           ExportResult elmExportResult,
                           ExportResult jsonExportResult,
                           String currentReleaseVersion,
                           String parentPath,
                           String measureId,
                           String measureJsonBundle) {
        Measure measure = measureDAO.getMeasureByMeasureId(measureId);

        try {
            if (!measure.isFhirMeasure()) {
                addBytesToZip(parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "-eCQM.xml", eMeasureXMLStr.getBytes(), zip);
                if (isV5OrGreater(currentReleaseVersion)) {
                    addFileToZip(measure, cqlExportResult, parentPath, "cql", zip);
                    addFileToZip(measure, elmExportResult, parentPath, "xml", zip);
                    addFileToZip(measure, jsonExportResult, parentPath, "json", zip);
                }
            } else {
                addBytesToZip(parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".json", measureJsonBundle.getBytes(), zip);
                addBytesToZip(parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".xml", convertToXmlBundle(measureJsonBundle).getBytes(), zip);
            }
            addBytesToZip(parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".html", eMeasureHTMLStr.getBytes(), zip);
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

    public void createBulkExportZip(String emeasureXMLStr, String emeasureHTMLStr, Map<String, byte[]> filesMap,
                                    String currentReleaseVersion, ExportResult cqlExportResult, ExportResult elmExportResult,
                                    ExportResult jsonExportResult, String parentPath, MeasureExport measureExport) throws Exception {
        try {
            String emeasureHumanReadablePath = "";
            Measure measure = measureExport.getMeasure();

            if (measure.isQdmMeasure()) {
                emeasureHumanReadablePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + ".html";
                String emeasureXMLPath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "-eCQM.xml";
                filesMap.put(emeasureXMLPath, emeasureXMLStr.getBytes());

                if (MatContext.get().isCQLMeasure(currentReleaseVersion)) {
                    addVersion5Exports(filesMap, cqlExportResult, elmExportResult, parentPath, jsonExportResult, measure);
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

    private void addVersion5Exports(Map<String, byte[]> filesMap,
                                    ExportResult cqlExportResult, ExportResult elmExportResult,
                                    String parentPath, ExportResult jsonExportResult, Measure measure) {

        if (!cqlExportResult.getIncludedCQLExports().isEmpty()) {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "cql";
            filesMap.put(filePath, cqlExportResult.getExport().getBytes());

            for (ExportResult includedResult : cqlExportResult.getIncludedCQLExports()) {
                filePath = parentPath + File.separator + FileNameUtility.getExportCqlLibraryFileName(includedResult, measure) + "." + "cql";
                filesMap.put(filePath, includedResult.getExport().getBytes());
            }
        } else {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "cql";
            filesMap.put(filePath, cqlExportResult.getExport().getBytes());
        }

        if (!elmExportResult.getIncludedCQLExports().isEmpty()) {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "xml";
            filesMap.put(filePath, elmExportResult.getExport().getBytes());

            for (ExportResult includedResult : elmExportResult.getIncludedCQLExports()) {
                filePath = parentPath + File.separator + FileNameUtility.getExportCqlLibraryFileName(includedResult, measure) + "." + "xml";
                filesMap.put(filePath, includedResult.getExport().getBytes());
            }
        } else {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "xml";
            filesMap.put(filePath, elmExportResult.getExport().getBytes());
        }

        if (!jsonExportResult.getIncludedCQLExports().isEmpty()) {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "json";
            filesMap.put(filePath, jsonExportResult.getExport().getBytes());

            for (ExportResult includedResult : jsonExportResult.getIncludedCQLExports()) {
                filePath = parentPath + File.separator + FileNameUtility.getExportCqlLibraryFileName(includedResult, measure) + "." + "json";
                filesMap.put(filePath, includedResult.getExport().getBytes());
            }
        } else {
            String filePath = parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + "json";
            filesMap.put(filePath, jsonExportResult.getExport().getBytes());
        }
    }


    public byte[] getCQLZipBarr(Measure measure, ExportResult export, String extension) {
        byte[] ret = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zip = new ZipOutputStream(baos);

            String parentPath = FileNameUtility.getExportFileName(measure);
//            parentPath = export.measureName + "-" + extension;

            addFileToZip(measure, export, parentPath, extension, zip);

            zip.close();
            ret = baos.toByteArray();
            export.setZipbarr(ret);
        } catch (Exception er) {
            er.printStackTrace();
        }
        return ret;
    }

    private void addFileToZip(Measure measure, ExportResult export, String parentPath, String extension,
                              ZipOutputStream zip) throws Exception {

        String cqlFilePath = measure.isFhirMeasure() ? replaceUnderscores (parentPath + File.separator + LIBRARY + "-" + export.getCqlLibraryName() + "." + extension)
                : parentPath + File.separator + FileNameUtility.getExportFileName(measure) + "." + extension;
        addBytesToZip(cqlFilePath, export.getExport().getBytes(), zip);

        for (ExportResult includedResult : export.getIncludedCQLExports()) {
            cqlFilePath = parentPath + File.separator + FileNameUtility.getExportCqlLibraryFileName(includedResult, measure) + "." + extension;
            addBytesToZip(cqlFilePath, includedResult.getExport().getBytes(), zip);
        }
    }

    public String buildMeasureBundle(FhirContext fhirContext, String measureJson, String libBundleJson) {
        // http://build.fhir.org/ig/HL7/cqf-measures/StructureDefinition-measure-bundle-cqfm.html
        IParser jsonParser = fhirContext.newJsonParser();
        jsonParser.setPrettyPrint(true);
        var measure = jsonParser.parseResource(org.hl7.fhir.r4.model.Measure.class, measureJson);
        var libBundle = jsonParser.parseResource(Bundle.class, libBundleJson);

        //For export the ids are set to the CQL LIBRARY NAME.
        measure.setId(measure.getName());
        libBundle.getEntry().forEach(e -> {
            Library l = (Library) e.getResource();
            l.setId(l.getName());
        });

        Bundle result = new Bundle();
        result.getMeta().addProfile("http://hl7.org/fhir/us/cqfmeasures/StructureDefinition/measure-bundle-cqfm");
        result.setType(Bundle.BundleType.TRANSACTION);
        result.addEntry().setResource(measure).getRequest()
                .setUrl("Measure/" + getFhirId(measure))
                .setMethod(Bundle.HTTPVerb.PUT);

        libBundle.getEntry().forEach(e ->
                result.addEntry().setResource(e.getResource()).
                        getRequest().setMethod(Bundle.HTTPVerb.PUT).
                        setUrl("Library/" + e.getResource().getId()));
        //Setup the request to match the newly set ID.

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
