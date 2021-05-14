package mat.server;

import mat.client.shared.MatException;
import mat.dao.OrganizationDAO;
import mat.dao.clause.MeasureDAO;
import mat.model.*;
import mat.model.clause.*;
import mat.server.bonnie.api.result.BonnieCalculatedResult;
import mat.server.export.ExportResult;
import mat.server.logging.LogFactory;
import mat.server.service.*;
import mat.server.service.impl.ZipPackager;
import mat.server.service.impl.ZipPackagerFactory;
import mat.shared.CQLError;
import mat.shared.FileNameUtility;
import mat.shared.InCorrectUserRoleException;
import mat.shared.SaveUpdateCQLResult;
import mat.shared.bonnie.error.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExportServlet extends HttpServlet {

    private static final String LIBRARY_ID = "libraryid";
    private static final String USER_ID = "userId";
    private static final String EXPORT_MEASURE_OWNER = "exportMeasureOwner";
    private static final String EXPORT_CQL_ERROR_FILE_FOR_STAND_ALONE = "errorFileStandAlone";
    private static final String EXPORT_CQL_ERROR_FILE_FOR_MEASURE = "errorFileMeasure";
    private static final String EXPORT_ACTIVE_NON_ADMIN_USERS_CSV = "exportActiveNonAdminUsersCSV";
    private static final String EXPORT_ALL_USERS_CSV = "exportAllUsersCSV";
    private static final String EXPORT_ACTIVE_OID_CSV = "exportActiveOIDCSV";
    private static final String EXPORT_ACTIVE_USER_CQL_LIBRARY_OWNERSHIP = "exportCQLLibraryOwner";
    private static final String ZIP = "zip";
    private static final String SUBTREE_HTML = "subtreeHTML";
    private static final String CODELIST = "codelist";
    private static final String SAVE = "save";
    private static final String ATTACHMENT_FILENAME = "attachment; filename=";
    private static final String CONTENT_DISPOSITION = "Content-Disposition";
    private static final String TEXT_CSV = "text/csv";
    private static final String APPLICATION_ZIP = "application/zip";
    private static final String APPLICATION_XSL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String HQMF = "hqmf";
    private static final String HUMAN_READABLE = "humanreadable";
    private static final String SIMPLEXML = "simplexml";
    private static final String CALCULATE_BONNIE_MEASURE_RESULT = "calculateBonnieMeasureResult";
    private static final String TYPE_PARAM = "type";
    private static final String XML = "xml";
    private static final String XML_FILENAME = "XML";
    private static final String ELM_FILENAME = "ELM";
    private static final String FORMAT_PARAM = "format";
    private static final String ID_PARAM = "id";
    private static final Log logger = LogFactory.getLog(ExportServlet.class);
    private static final long serialVersionUID = 4539514145289378238L;
    protected ApplicationContext context;
    private static final String CQL_LIBRARY = "cqlLibrary";
    private static final String ELM = "elm";
    private static final String CQL = "cql";
    private static final String CQL_FILENAME = "CQL";
    private static final String JSON = "json";
    private static final String JSON_FILENAME = "JSON";
    private static final String ADMINISTRATOR = "Administrator";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String CQL_NO_ERRORS_WARNINGS_MESSAGE = "You are viewing CQL with no errors or warnings.";
    private static final String MEASURE_EXPORTED = "Measure Exported";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
        MeasurePackageService service = getMeasurePackageService();
        MeasureLibraryService measureLibraryService = getMeasureLibraryService();

        String id = req.getParameter(ID_PARAM);
        String format = req.getParameter(FORMAT_PARAM);
        String type = req.getParameter(TYPE_PARAM);
        String libraryId = req.getParameter(LIBRARY_ID);
        String userId = req.getParameter(USER_ID);
        Measure measure = null;
        Date exportDate = null;

        logger.debug("FORMAT: " + format);

        if (id != null) {
            measure = service.getById(id);
            exportDate = measure.getExportedDate();
        }

        try {
            switch (format) {
                case SIMPLEXML:
                    exportSimpleXML(resp, id, type, measure);
                    break;
                case HQMF:
                    exportHQMF(resp, id, type, measure);
                    break;
                case HUMAN_READABLE:
                    exportHumanReadableForNewMeasures(resp, id, type, measure);
                    break;
                case CODELIST:
                    exportCodeListXLS(resp, id, measure);
                    break;
                case CQL_LIBRARY:
                    exportFile(resp, id, type, measure, CQL, CQL_FILENAME);
                    break;
                case ELM:
                    exportFile(resp, id, type, measure, XML, ELM_FILENAME);
                    break;
                case JSON:
                    exportFile(resp, id, type, measure, JSON, JSON_FILENAME);
                    break;
                case XML:
                    exportFile(resp, id, type, measure, XML, XML_FILENAME);
                    break;
                case ZIP:
                    zipMeasure(resp, id, measure, exportDate);
                    break;
                case SUBTREE_HTML:
                    exportSubTreeHumanReadable(req, resp, id);
                    break;
                case EXPORT_ACTIVE_NON_ADMIN_USERS_CSV:
                    exportActiveUserListCSV(resp);
                    break;
                case EXPORT_ACTIVE_OID_CSV:
                    exportActiveOrganizationListCSV(resp);
                    break;
                case EXPORT_MEASURE_OWNER:
                    exportActiveUserMeasureOwnershipListCSV(resp);
                    break;
                case EXPORT_ALL_USERS_CSV:
                    exportAllUserCSV(resp);
                    break;
                case EXPORT_ACTIVE_USER_CQL_LIBRARY_OWNERSHIP:
                    exportActiveUserCQLLibraryOwnershipListCSV(resp);
                    break;
                case EXPORT_CQL_ERROR_FILE_FOR_MEASURE:
                    exportErrorFileForMeasure(resp, measureLibraryService, id);
                    break;
                case EXPORT_CQL_ERROR_FILE_FOR_STAND_ALONE:
                    exportErrorFileForStandAloneLib(resp, libraryId);
                    break;
                case CALCULATE_BONNIE_MEASURE_RESULT:
                    exportBonnieMeasureCalculateion(resp, measure, userId);
                    break;
            }

        } catch (Exception e) {
            logger.error("Export Error:", e);
            throw new ServletException(e);
        } finally {
            if (resp != null && resp.getOutputStream() != null)
                resp.getOutputStream().close();
        }
    }

    private boolean canViewExports(Measure measure) {
        String currentUserId = LoggedInUserUtil.getLoggedInUser();

        return !measure.getIsPrivate() || LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.SUPER_USER_ROLE)
                || measure.getOwner().getId().equals(currentUserId) || isSharedWith(measure);
    }

    private boolean isSharedWith(Measure measure) {
        String currentUserId = LoggedInUserUtil.getLoggedInUser();
        boolean isSharedWith = false;
        for (MeasureShare share : measure.getShares()) {
            if (share.getShareUser().getId().equals(currentUserId)) {
                isSharedWith = true;
            }
        }

        return isSharedWith;
    }


    private void zipMeasure(HttpServletResponse resp, String id, Measure measure, Date exportDate) throws Exception {
        if (!canViewExports(measure)) {
            return;
        }

        if (Boolean.TRUE.equals(measure.getIsCompositeMeasure())) {
            exportCompositeMeasureZip(resp, id, measure);
        } else {
            exportEmeasureZip(resp, id, measure, exportDate);
        }
    }

    private void exportBonnieMeasureCalculateion(HttpServletResponse resp, Measure measure, String userId) throws IOException, BonnieUnauthorizedException, BonnieNotFoundException, BonnieServerException, BonnieBadParameterException, BonnieDoesNotExistException {
        if (!canViewExports(measure)) {
            return;
        }

        BonnieCalculatedResult export = getService().getBonnieExportCalculation(measure.getMeasureSet().getId(), userId);
        resp.setHeader(CONTENT_DISPOSITION,
                ATTACHMENT_FILENAME + export.getName());
        resp.setContentType(APPLICATION_XSL);
        resp.getOutputStream().write(export.getResult());
    }

    private void exportErrorFileForMeasure(HttpServletResponse resp, MeasureLibraryService measureLibraryService,
                                           String id) throws IOException {
        if (!canViewExports(getMeasureDAO().find(id))) {
            return;
        }

        SaveUpdateCQLResult result = measureLibraryService.getMeasureCQLLibraryData(id);
        addLineNumberAndErrorMessageToCQLErrorExport(resp, result);
    }

    private void exportErrorFileForStandAloneLib(HttpServletResponse resp, String id) throws IOException {
        SaveUpdateCQLResult result = getCQLLibraryService().getCQLLibraryFileData(id);
        addLineNumberAndErrorMessageToCQLErrorExport(resp, result);
    }

    private void addLineNumberAndErrorMessageToCQLErrorExport(HttpServletResponse resp, SaveUpdateCQLResult result)
            throws IOException {
        StringBuilder sb = new StringBuilder();
        String cqlString = result.getCqlString();
        String[] cqlLinesArray = cqlString.split("\n");
        for (int i = 0; i < cqlLinesArray.length; i++) {
            sb.append((i + 1)).append(" ").append(cqlLinesArray[i]).append("\r\n");
        }

        if (CollectionUtils.isEmpty(result.getCqlErrors()) && CollectionUtils.isEmpty(result.getCqlWarnings())) {
            sb.append("/*******************************************************************************************************************");
            sb.append("\r\n");
            sb.append(CQL_NO_ERRORS_WARNINGS_MESSAGE);
            sb.append("\r\n");
            sb.append("*******************************************************************************************************************/");

        } else {
            sb.append(buildExceptionString(result.getLibraryNameErrorsMap().get(result.getCqlModel().getFormattedName()), "ERRORS:"));
            sb.append(buildExceptionString(result.getLibraryNameWarningsMap().get(result.getCqlModel().getFormattedName()), "WARNINGS:"));
        }

        resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + result.getLibraryName() + ".txt");
        resp.getOutputStream().write(sb.toString().getBytes());
    }

    private String buildExceptionString(List<CQLError> cqlErrors, String heading) {
        StringBuilder sb = new StringBuilder();

        if (CollectionUtils.isNotEmpty(cqlErrors)) {
            Collections.sort(cqlErrors);
            sb.append("\r\n");
            sb.append("/*******************************************************************************************************************");
            sb.append("\r\n");
            sb.append(heading);
            sb.append("\r\n");
            sb.append("********************************************************************************************************************");
            for (CQLError error : cqlErrors) {
                sb.append("\r\n");
                sb.append(createExceptionLine(error));
                sb.append("\r\n");
            }

            sb.append("*******************************************************************************************************************/");
        }

        return sb.toString();
    }

    private StringBuilder createExceptionLine(CQLError error) {
        StringBuilder errorMessage = new StringBuilder();
        return errorMessage.append("Line ").append(error.getErrorInLine()).append(": ").append(error.getErrorMessage());
    }

    private void exportFile(HttpServletResponse resp, String id, String type, Measure measure, String extension, String fileNameExtension) throws Exception {
        MeasureExport measureExport = getService().getMeasureExport(id);
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        if (!canViewExports(measureExport.getMeasure())) {
            return;
        }
        ExportResult export = null;

        if (JSON_FILENAME.equals(fileNameExtension)) {
            export = measure.isFhirMeasure() ? getService().getMeasureBundleExportResult(measureExport, JSON)
                    : getService().createOrGetJSONLibraryFile(id, measureExport);
        } else if (CQL_FILENAME.equals(fileNameExtension)) {
            export = getService().createOrGetCQLLibraryFile(id, measureExport);
        } else if (ELM_FILENAME.equals(fileNameExtension)) {
            export = getService().createOrGetELMLibraryFile(id, measureExport);
        } else if (XML_FILENAME.equals(fileNameExtension)) {
            export = getService().getMeasureBundleExportResult(measureExport, XML); //only for FHIR measures
        }

        if (!export.getIncludedCQLExports().isEmpty()) {
            ZipPackager zp = context.getBean(ZipPackagerFactory.class).getZipPackager();
            zp.getCQLZipBarr(measure, export, extension);

            resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportBundleZipName(measure));
            resp.setContentType(APPLICATION_ZIP);
            resp.getOutputStream().write(export.getZipbarr());
            export.setZipbarr(null);
        } else {
            if (SAVE.equals(type)) {
                if (CQL_FILENAME.equals(fileNameExtension)) {
                    resp.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                }
                resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportFileName(measure) + "." + extension);
            }

            if (JSON_FILENAME.equals(fileNameExtension)) {
                resp.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            } else if (CQL_FILENAME.equals(fileNameExtension) && !SAVE.equals(type)) {
                resp.setHeader(CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
            } else if (ELM_FILENAME.equals(fileNameExtension)) {
                resp.setHeader(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            }

            resp.getOutputStream().write(export.getExport().getBytes());
        }
    }

    private void generateCSVFile(HttpServletResponse resp, String name) throws MatException {
        String userRole = LoggedInUserUtil.getLoggedInUserRole();
        if (ADMINISTRATOR.equalsIgnoreCase(userRole)) {
            String csvFileString = "";
            switch (name) {
                case "activeOrganizationOids":
                    csvFileString = generateCSVOfActiveUserOIDs();
                    break;
                case "activeUsers":
                    csvFileString = generateCSVOfActiveUserEmails();
                    break;
                case "allUsersReport":
                    csvFileString = generateCSVOfAllUser();
                    break;
                case "activeUsersMeasureOwnership":
                    try {
                        csvFileString = generateCSVOfMeasureOwnershipForActiveUser();
                    } catch (XPathExpressionException e1) {
                        logger.debug("generateCSVFile:" + name + e1.getMessage());
                        throw new MatException(e1.getMessage());
                    }
                    break;
                case "activeUsersCQLLibraryOwnership":
                    csvFileString = generateCSVOfCQLLibraryOwnershipForActiveUser();
                    break;
            }

            String activeUserCSVDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
            resp.setHeader(CONTENT_DISPOSITION,
                    ATTACHMENT_FILENAME + FileNameUtility.getCSVFileName(name, activeUserCSVDate) + ";");
            resp.setContentType(TEXT_CSV);
            try {
                resp.getOutputStream().write(csvFileString.getBytes());
            } catch (IOException e) {
                logger.debug("generateCSVFile:" + name + e.getMessage());
                throw new MatException(e.getMessage());
            }
        }
    }

    private void exportActiveOrganizationListCSV(HttpServletResponse resp) throws MatException {
        generateCSVFile(resp, "activeOrganizationOids");
    }

    private void exportActiveUserListCSV(HttpServletResponse resp) throws MatException {
        generateCSVFile(resp, "activeUsers");
    }

    private void exportAllUserCSV(HttpServletResponse resp) throws MatException {
        generateCSVFile(resp, "allUsersReport");
    }

    private void exportActiveUserMeasureOwnershipListCSV(HttpServletResponse resp) throws MatException {
        generateCSVFile(resp, "activeUsersMeasureOwnership");
    }

    private void exportActiveUserCQLLibraryOwnershipListCSV(HttpServletResponse resp) throws MatException {
        generateCSVFile(resp, "activeUsersCQLLibraryOwnership");
    }

    private void exportSubTreeHumanReadable(HttpServletRequest req, HttpServletResponse resp, String id)
            throws Exception {
        String nodeXML = req.getParameter(XML);
        logger.debug("Export servlet received node xml:" + nodeXML + " and Measure ID:" + id);
        ExportResult export = getService().getHumanReadableForNode(id, nodeXML);
        resp.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        resp.getOutputStream().println(export.getExport());
    }

    /*Generates a zip and adds all measure artifacts to it.*/
    private void exportCompositeMeasureZip(HttpServletResponse resp, String id, Measure measure) throws Exception {
        List<ComponentMeasure> componentMeasures = measure.getComponentMeasures();
        ExportResult export = getService().getCompositeExportResult(id, componentMeasures);
        resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportBundleZipName(measure));
        resp.setContentType(APPLICATION_ZIP);
        resp.getOutputStream().write(export.getZipbarr());
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        export.setZipbarr(null);
    }

    /*Generates a zip and adds all measure artifacts to it.*/
    private void exportEmeasureZip(HttpServletResponse resp, String id, Measure measure, Date exportDate)
            throws Exception {
        var export = getService().getEMeasureZIP(id, exportDate);
        resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportBundleZipName(measure));
        resp.setContentType(APPLICATION_ZIP);
        resp.getOutputStream().write(export.getZipbarr());
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        export.setZipbarr(null);
    }

    private void exportCodeListXLS(HttpServletResponse resp, String id, Measure measure) throws Exception {
        if (!canViewExports(measure)) {
            return;
        }

        ExportResult export = getService().getEMeasureXLS(id);

        String currentReleaseVersion = StringUtils.replace(measure.getReleaseVersion(), ".", "-");
        resp.setHeader(CONTENT_DISPOSITION, FileNameUtility.replaceUnderscores(ATTACHMENT_FILENAME + FileNameUtility
                .getEmeasureXLSName(export.getMeasureName() + "-" + currentReleaseVersion, export.getPackageDate())));
        resp.setContentType("application/vnd.ms-excel");
        resp.getOutputStream().write(export.getWkbkbarr());
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        export.setWkbkbarr(null);
    }

    private ExportResult getHQMFExportForMeasure(String measureId, String currentReleaseVersion) throws Exception {
        ExportResult export;

        if (currentReleaseVersion.equals("v3")) {
            export = getService().createOrGetHQMFForv3Measure(measureId);
        } else {
            export = getService().createOrGetHQMF(measureId);
        }
        return export;
    }

    private void exportHQMF(HttpServletResponse resp, String id, String type, Measure measure) throws Exception {
        if (!canViewExports(measure)) {
            return;
        }
        String currentReleaseVersion = measure.getReleaseVersion();
        ExportResult export = getHQMFExportForMeasure(id, currentReleaseVersion);

        if (SAVE.equals(type)) {
            resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportFileName(measure) + "-eCQM.xml");
        }
        resp.setHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        resp.getOutputStream().write(export.getExport().getBytes());
    }


    private void exportHumanReadableForNewMeasures(HttpServletResponse resp, String id, String type, Measure measure)
            throws Exception {
        if (!canViewExports(measure)) {
            return;
        }
        String currentReleaseVersion = measure.getReleaseVersion();
        ExportResult export = currentReleaseVersion.equals("v3") ? getService().createOrGetEMeasureHTML(id)
                : getService().createOrGetHumanReadable(id, currentReleaseVersion);
        if (SAVE.equals(type)) {
            resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportFileName(measure) + ".html");
        }
        resp.setHeader(CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
        resp.getOutputStream().write(export.getExport().getBytes());
    }

    private void exportSimpleXML(HttpServletResponse resp, String id, String type, Measure measure) throws Exception {
        if (LoggedInUserUtil.getLoggedInUserRole().equals(SecurityRole.SUPER_USER_ROLE)) {
            ExportResult export = getService().getSimpleXML(id);
            if (SAVE.equals(type)) {
                resp.setHeader(CONTENT_DISPOSITION, ATTACHMENT_FILENAME + FileNameUtility.getExportFileName(measure) + ".xml");
            }
            resp.setHeader(CONTENT_TYPE, MediaType.TEXT_XML_VALUE);
            getAuditService().recordMeasureEvent(measure.getId(), MEASURE_EXPORTED, null, true);
            resp.getOutputStream().write(export.getExport().getBytes());
        }
    }

    /**
     * Generate csv of active user emails.
     *
     * @return the string
     * @throws InCorrectUserRoleException the in correct user role exception
     */
    private String generateCSVOfActiveUserEmails() {
        logger.debug("Generating CSV of email addrs for all Active Users...");
        // Get all the active users
        List<User> allNonAdminActiveUsersList = getUserService().getAllNonAdminActiveUsers();

        // Iterate through the 'allNonAdminActiveUsersList' and generate a csv
        return createCSVOfAllNonAdminActiveUsers(allNonAdminActiveUsersList);
    }

    private String generateCSVOfAllUser() {
        logger.debug("Generating CSV For All Users...");
        // Get all the active users
        List<User> allUsersList = getUserService().getAllUsers();

        // Iterate through the 'allNonAdminActiveUsersList' and generate a csv
        return createCSVOfUsers(allUsersList);
    }

    /**
     * Generate csv of active user emails.
     *
     * @return the string
     * @throws InCorrectUserRoleException the in correct user role exception
     * @throws XPathExpressionException
     */
    private String generateCSVOfMeasureOwnershipForActiveUser() throws XPathExpressionException {
        logger.debug("Generating CSV of Measure Ownership for all Active Non Admin Users...");
        List<MeasureOwnerReportDTO> ownerReList = getMeasureLibraryService().getMeasuresForMeasureOwner();
        // Iterate through the 'allNonAdminActiveUsersList' and generate a csv
        return createCSVOfActiveUserMeasures(ownerReList);
    }

    /**
     * Generate csv of cql library ownership for all active users
     *
     * @return the csv string
     */
    private String generateCSVOfCQLLibraryOwnershipForActiveUser() {
        logger.debug("Generating CSV of CQL Library Ownership for all Active Non Admin Users...");
        List<CQLLibraryOwnerReportDTO> ownerList = getCQLLibraryService().getCQLLibrariesForOwner();
        return createCSVOfActiveUserCQLLibrary(ownerList);

    }

    /**
     * Generate csv of active OIDs.
     *
     * @return the string
     */
    private String generateCSVOfActiveUserOIDs() {
        logger.debug("Generating CSV of Active User OID's...");
        // Get all the active users
        final List<Organization> activeOrganizationList = getOrganizationDAO().getActiveOrganizationForAdminCSVReport();
        final Map<String, String> activeOidsMap = new TreeMap<>();
        for (final Organization org : activeOrganizationList) {
            activeOidsMap.put(org.getOrganizationOID(), org.getOrganizationName());
        }
        // Iterate through the 'allNonTerminatedUsersList' and generate a csv
        return createCSVOfAllActiveUsersOID(activeOidsMap);
    }

    /**
     * Creates the csv of Active User's OIDs.
     *
     * @param activeOidsMap Map of Distinct OID's
     * @return the string
     */
    private String createCSVOfAllActiveUsersOID(final Map<String, String> activeOidsMap) {

        StringBuilder csvStringBuilder = new StringBuilder();
        // Add the header row
        csvStringBuilder.append("Organization,Organization Id");
        csvStringBuilder.append("\r\n");
        // Add data rows
        for (Map.Entry<String, String> entry : activeOidsMap.entrySet()) {
            csvStringBuilder.append("\"" + entry.getValue() + "\",\"" + entry.getKey() + "\"");
            csvStringBuilder.append("\r\n");
        }
        return csvStringBuilder.toString();
    }

    /**
     * Creates the csv of all non admin active users.
     *
     * @param allNonAdminActiveUsersList the all non admin active users list
     * @return the string
     */
    private String createCSVOfAllNonAdminActiveUsers(final List<User> allNonAdminActiveUsersList) {

        StringBuilder csvStringBuilder = new StringBuilder();
        // Add the header row
        csvStringBuilder.append("User ID,Last Name,First Name,Email Address,Organization,User Role,Organization Id");
        csvStringBuilder.append("\r\n");

        // Add data rows
        for (User user : allNonAdminActiveUsersList) {
            csvStringBuilder.append("\"" + user.getLoginId() + "\",\"" + user.getLastName() + "\",\""
                    + user.getFirstName() + "\",\"" + user.getEmailAddress() + "\",\"" + user.getOrganizationName()
                    + "\",\"" + user.getSecurityRole().getDescription() + "\",\"" + user.getOrgOID() + "\"");
            csvStringBuilder.append("\r\n");
        }
        return csvStringBuilder.toString();
    }

    private String createCSVOfUsers(final List<User> allNonAdminActiveUsersList) {

        StringBuilder csvStringBuilder = new StringBuilder();
        // Add the header row
        csvStringBuilder.append(
                "User ID,Last Name,First Name,Organization,Organization Id,Email Address,User Status,Role,Date Of Termination");
        csvStringBuilder.append("\r\n");

        // Add data rows
        for (User user : allNonAdminActiveUsersList) {
            csvStringBuilder.append("\"" + user.getLoginId() + "\",\"" + user.getLastName() + "\",\""
                    + user.getFirstName() + "\",\"" + user.getOrganizationName() + "\",\"" + user.getOrgOID() + "\",\""
                    + user.getEmailAddress() + "\",\"" + user.getStatus().getDescription() + "\",\""
                    + user.getSecurityRole().getDescription() + "\",\"" + user.getTerminationDate() + "\"");
            csvStringBuilder.append("\r\n");
        }
        return csvStringBuilder.toString();
    }

    /**
     * Generates Measure and Measure Owner report for Active Non Admin Users.
     *
     * @param ownerReList - List.
     * @return CSV String
     */
    private String createCSVOfActiveUserMeasures(final List<MeasureOwnerReportDTO> ownerReList) {

        StringBuilder csvStringBuilder = new StringBuilder();
        // Add the header row
        csvStringBuilder.append("Last Name,First Name,Organization,Measure Name,Emeasure Id , GUID ,NQF Number");
        csvStringBuilder.append("\r\n");
        for (MeasureOwnerReportDTO measureOwnerReportDTO : ownerReList) {
            csvStringBuilder.append("\"" + measureOwnerReportDTO.getLastName() + "\",\""
                    + measureOwnerReportDTO.getFirstName() + "\",\"" + measureOwnerReportDTO.getOrganization() + "\",\""
                    + measureOwnerReportDTO.getName() + "\",\"");
            if (measureOwnerReportDTO.getCmsNumber() != 0) {
                csvStringBuilder.append(measureOwnerReportDTO.getCmsNumber() + "\",\"");
            } else {
                csvStringBuilder.append("" + "\",\"");
            }
            if (measureOwnerReportDTO.getId() != null) {
                csvStringBuilder.append(measureOwnerReportDTO.getId() + "\"");
            } else {
                csvStringBuilder.append("" + "\"");
            }
            if (measureOwnerReportDTO.getNqfId() != null) {
                csvStringBuilder.append(",\"\t" + measureOwnerReportDTO.getNqfId() + "\"");
            } else {
                csvStringBuilder.append(",\"" + "" + "\"");
            }
            csvStringBuilder.append("\r\n");
        }
        return csvStringBuilder.toString();
    }

    /**
     * Creates the csv string for the cql library ownership report
     *
     * @param ownerList the list of cql library owner reports
     * @return the csv string
     */
    private String createCSVOfActiveUserCQLLibrary(final List<CQLLibraryOwnerReportDTO> ownerList) {
        StringBuilder csvStringBuilder = new StringBuilder();

        // add the header
        csvStringBuilder
                .append("CQL Library Name,Type,Status,Version #,ID #,Set ID #,First Name,Last Name,Organization");
        csvStringBuilder.append("\r\n");

        // add data
        for (CQLLibraryOwnerReportDTO cqlLibraryOwnerReport : ownerList) {
            csvStringBuilder.append("\"" + cqlLibraryOwnerReport.getName() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getType() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getStatus() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getVersionNumber() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getId() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getSetId() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getFirstName() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getLastName() + "\",\"");
            csvStringBuilder.append(cqlLibraryOwnerReport.getOrganization() + "" + "\"");
            csvStringBuilder.append("\r\n");
        }
        return csvStringBuilder.toString();
    }

    private SimpleEMeasureService getService() {
        return context.getBean(SimpleEMeasureService.class);
    }

    private UserService getUserService() {
        return context.getBean(UserService.class);
    }

    private MeasurePackageService getMeasurePackageService() {
        return context.getBean(MeasurePackageService.class);
    }

    private MeasureLibraryService getMeasureLibraryService() {
        return context.getBean(MeasureLibraryService.class);
    }

    private CQLLibraryService getCQLLibraryService() {
        return context.getBean(CQLLibraryService.class);
    }

    private OrganizationDAO getOrganizationDAO() {
        return context.getBean(OrganizationDAO.class);
    }

    private MeasureDAO getMeasureDAO() {
        return context.getBean(MeasureDAO.class);
    }

    public MeasureAuditService getAuditService() {
        return context.getBean(MeasureAuditService.class);
    }

}
