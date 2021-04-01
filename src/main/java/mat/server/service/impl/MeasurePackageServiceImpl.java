package mat.server.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cqltoelm.CQLFormatter;
import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.FhirMeasurePackageResult;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatContext;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.MeasureTypeDAO;
import mat.dao.OrganizationDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibraryExportDAO;
import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureShareDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.PackagerDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.dto.fhirconversion.FhirValidationResult;
import mat.dto.fhirconversion.PushValidationResult;
import mat.model.DataType;
import mat.model.QualityDataSet;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.CQLLibraryExport;
import mat.model.clause.ComponentMeasure;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.MeasureXML;
import mat.model.clause.ShareLevel;
import mat.model.cql.CQLLibraryShare;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.LoggedInUserUtil;
import mat.server.cqlparser.ReverseEngineerListener;
import mat.server.export.ExportResult;
import mat.server.export.MeasureArtifactGenerator;
import mat.server.logging.LogFactory;
import mat.server.service.FhirMeasureService;
import mat.server.service.MeasureLibraryService;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.util.ExportSimpleXML;
import mat.server.util.XmlProcessor;
import mat.server.validator.measure.CompositeMeasurePackageValidator;
import mat.shared.CompositeMeasurePackageValidationResult;
import mat.shared.MeasureSearchModel;
import mat.shared.ValidationUtility;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import mat.vsacmodel.ValueSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MeasurePackageServiceImpl implements MeasurePackageService {

    private static final Log logger = LogFactory.getLog(MeasurePackageServiceImpl.class);

    @Autowired
    private DataTypeDAO dataTypeDAO;

    @Autowired
    private SimpleEMeasureService eMeasureService;

    @Autowired
    private MeasureAuditLogDAO measureAuditLogDAO;

    @Autowired
    private MeasureDAO measureDAO;

    @Autowired
    private MeasureTypeDAO measureTypeDAO;

    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private MeasureExportDAO measureExportDAO;

    @Autowired
    private MeasureSetDAO measureSetDAO;

    @Autowired
    private MeasureShareDAO measureShareDAO;

    @Autowired
    private MeasureXMLDAO measureXMLDAO;

    @Autowired
    private PackagerDAO packagerDAO;

    @Autowired
    private QualityDataSetDAO qualityDataSetDAO;

    @Autowired
    private ShareLevelDAO shareLevelDAO;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private CQLLibraryShareDAO cqlLibraryShareDAO;

    @Autowired
    private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;

    @Autowired
    private ComponentMeasuresDAO componentMeasuresDAO;

    @Autowired
    private CompositeMeasurePackageValidator compositeMeasurePackageValidator;

    @Autowired
    private FhirMeasureService fhirMeasureService;

    @Autowired
    private MeasureLibraryService measureLibraryService;

    @Autowired
    private CQLLibraryExportDAO libraryExportDao;

    @Value("${mat.measure.current.release.version}")
    private String currentReleaseVersion;

    private ValidationUtility validator = new ValidationUtility();

    @Override
    public int countUsersForMeasureShare() {
        return measureDAO.countUsersForMeasureShare();
    }

    @Override
    public void deleteExistingPackages(final String measureId) {
        packagerDAO.deleteAllPackages(measureId);

    }

    @Override
    public DataType findDataTypeForSupplimentalCodeList(final String dataTypeName, final String categoryId) {
        return dataTypeDAO.findDataTypeForSupplimentalCodeList(dataTypeName, categoryId);
    }

    @Override
    public MeasureSet findMeasureSet(final String id) {
        return measureSetDAO.findMeasureSet(id);
    }

    @Override
    public String findOutMaximumVersionNumber(final String measureSetId) {
        return measureDAO.findMaxVersion(measureSetId);
    }

    @Override
    public String findOutVersionNumber(final String measureId, final String measureSetId) {
        return measureDAO.findMaxOfMinVersion(measureId, measureSetId);
    }

    private String generateSimpleXML(final Measure measure, MeasureXML measureXML, final List<ValueSet> ValueSetList) throws Exception {
        String exportedXML = "";

        if (measure.getReleaseVersion() != null && MatContext.get().isCQLMeasure(measure.getReleaseVersion())) {
            final CQLModel cqlModel = CQLUtilityClass.getCQLModelFromXML(measureXML.getMeasureXMLAsString());
            exportedXML = ExportSimpleXML.export(measureXML, measureDAO, organizationDAO, cqlLibraryDAO, cqlModel, measureTypeDAO);
        } else {
            exportedXML = ExportSimpleXML.export(measureXML, measureDAO, organizationDAO);
        }

        return exportedXML;
    }

    private MeasureExport generateExport(final String measureId, final List<ValueSet> ValueSetList) throws Exception {
        MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
        final Measure measure = measureDAO.find(measureId);

        String updatedMeasureXMLString = formatCQL(measureXML);

        MeasureXmlModel xmlModel = new MeasureXmlModel();
        xmlModel.setMeasureId(measure.getId());
        xmlModel.setXml(updatedMeasureXMLString);
        xmlModel.setMeasureModel(measure.getMeasureModel());

        saveMeasureXml(xmlModel);
        measureXML = measureXMLDAO.findForMeasure(measureId);

        final String simpleXML = generateSimpleXML(measure, measureXML, ValueSetList);
        final ExportResult exportResult = eMeasureService.exportMeasureIntoSimpleXML(measure.getId(), simpleXML, ValueSetList);

        MeasureExport export = measureExportDAO.findByMeasureId(measureId);
        if (export == null) {
            export = new MeasureExport();
            export.setMeasure(measure);
        }

        export.setSimpleXML(simpleXML);
        export.setCodeListBarr(exportResult.wkbkbarr);
        return export;
    }

    private String formatCQL(final MeasureXML measureXML) throws IOException {
        CQLModel model = CQLUtilityClass.getCQLModelFromXML(measureXML.getMeasureXMLAsString());
        String cqlString = CQLUtilityClass.getCqlString(model, "").getLeft();
        CQLFormatter formatter = new CQLFormatter();
        cqlString = formatter.format(cqlString);
        String formattedXml = measureXML.getMeasureXMLAsString();

        if (!model.isFhir()) {
            // Right now this uses model 1.4.6 so it won't work with fhir.
            // Once we upgrade to 1.5.0_SNAPSHOT it should work.
            ReverseEngineerListener listener = new ReverseEngineerListener(cqlString, model);
            CQLModel reversedEngineeredCQLModel = listener.getCQLModel();
            formattedXml = CQLUtilityClass.getXMLFromCQLModel(reversedEngineeredCQLModel);
        }

        XmlProcessor processor = new XmlProcessor(measureXML.getMeasureXMLAsString());
        processor.replaceNode(formattedXml, "cqlLookUp", "measure");
        String updatedMeasureXMLString = processor.transform(processor.getOriginalDoc());
        return updatedMeasureXMLString;
    }

    @Override
    public List<FhirValidationResult> createPackageArtifacts(Measure measure, MeasureExport export) {
        String measureId = measure.getId();
        String releaseVersion = measure.getReleaseVersion();
        export.setHqmf(MeasureArtifactGenerator.getHQMFArtifact(measureId, releaseVersion));

        if (measure.isFhirMeasure()) {
            measureExportDAO.save(export);
            PushValidationResult errorResult = fhirMeasureService.push(measureId);
            if (errorResult != null && !errorResult.isValid()) {
                logger.info("MeasurePackageServiceImpl::PushValidationResult from /pushMeasure" + errorResult);
                return errorResult.getFhirValidationResults();
            }

            FhirMeasurePackageResult pkg = fhirMeasureService.packageMeasure(measureId);
            export.setCql(pkg.getMeasureLibCql());
            export.setElmXml(pkg.getMeasureLibElmXml()); //elm xml
            export.setMeasureJson(pkg.getMeasureJson()); //measure json
            export.setFhirIncludedLibsJson(pkg.getInludedLibsJson());

            CQLLibrary library = cqlLibraryDAO.getLibraryByMeasureId(measure.getId());
            if (library != null) {
                CQLLibraryExport libExport = libraryExportDao.findByLibraryId(library.getId());
                if (libExport == null) {
                    libExport = new CQLLibraryExport();
                    libExport.setCqlLibrary(library);
                }
                libExport.setFhirJson(pkg.getMeasureLibJson());
                libExport.setCql(pkg.getMeasureLibCql());
                libExport.setElmXml(pkg.getMeasureLibElmXml());
                libraryExportDao.save(libExport);
            }
            export.setHumanReadable(MeasureArtifactGenerator.getHumanReadableArtifact(measureId, releaseVersion));
        } else {
            export.setHumanReadable(MeasureArtifactGenerator.getHumanReadableArtifact(measureId, releaseVersion));
            export.setCql(MeasureArtifactGenerator.getCQLArtifact(measureId));
            export.setElmXml(MeasureArtifactGenerator.getELMArtifact(measureId));
            export.setMeasureJson(MeasureArtifactGenerator.getJSONArtifact(measureId));
        }
        measureExportDAO.save(export);
        return null;
    }

    @Override
    public Measure getById(final String id) {
        return measureDAO.find(id);
    }

    @Override
    public int getMaxEMeasureId() {
        return measureDAO.getMaxEMeasureId();
    }

    @Override
    public MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
        final MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
        final Measure measure = measureDAO.find(measureId);
        if (measureXML != null) {
            final MeasureXmlModel exportModal = new MeasureXmlModel();
            exportModal.setMeasureId(measureXML.getMeasureId());
            exportModal.setMeausreExportId(measureXML.getId());
            exportModal.setXml(measureXML.getMeasureXMLAsString());
            exportModal.setMeasureModel(measure.getMeasureModel());
            return exportModal;
        }
        return null;
    }

    @Override
    public String getUniqueOid() {
        return qualityDataSetDAO.generateUniqueOid();
    }

    @Override
    public List<MeasureShareDTO> getUsersForShare(final String userName, final String measureId, final int startIndex, final int pageSize) {
        return measureDAO.getMeasureShareInfoForMeasure(userName, measureId, startIndex - 1, pageSize);
    }

    public ValidationUtility getValidator() {
        return validator;
    }

    @Override
    public boolean isMeasureLocked(final String id) {
        final boolean isLocked = measureDAO.isMeasureLocked(id);
        return isLocked;
    }

    @Override
    public void save(final Measure measurePackage) {
        if (measurePackage.getOwner() == null) {
            if (LoggedInUserUtil.getLoggedInUser() != null) {
                final User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
                measurePackage.setOwner(currentUser);
            }
        }

        measureDAO.save(measurePackage);
    }

    @Override
    public void save(final MeasureSet measureSet) {
        measureSetDAO.save(measureSet);
    }

    @Override
    public int saveAndReturnMaxEMeasureId(final Measure measure) {
        return measureDAO.saveandReturnMaxEMeasureId(measure);
    }

    @Override
    public int generateEMeasureId() {
        try {
            return measureDAO.getMaxEMeasureId() + 1;
        } catch (RuntimeException re) {
            logger.error("MeasurePackageServiceImpl::returnMaxEMeasureId -> Error retrieving MaxEMeasureId", re);
            throw re;
        }
    }

    @Override
    public void saveMeasureXml(final MeasureXmlModel measureXmlModel) {
        MeasureXML measureXML = measureXMLDAO.findForMeasure(measureXmlModel.getMeasureId());
        if (measureXML != null) {
            measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
        } else {
            measureXML = new MeasureXML();
            measureXML.setMeasureId(measureXmlModel.getMeasureId());
            measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
        }

        measureXMLDAO.save(measureXML);
    }

    @Override
    public void saveSupplimentalQDM(final QualityDataSet qds) {
        qualityDataSetDAO.save(qds);
    }

    @Override
    public List<MeasureShareDTO> searchForAdminWithFilter(String searchText,
                                                          int startIndex, int numResults, int filter) {
        return measureDAO.getMeasureShareInfoForUserWithFilter(searchText, startIndex - 1, numResults, filter);
    }

    @Override
    public List<MeasureShareDTO> searchWithFilter(MeasureSearchModel measureSearchModel) {
        logger.debug("MeasurePackageServiceImpl::searchWithFilter - enter");
        final User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
        List<MeasureShareDTO> results = measureDAO.getMeasureShareInfoForUserWithFilter(measureSearchModel, user);
        logger.debug("MeasurePackageServiceImpl::searchWithFilter - exit");
        return results;
    }

    @Override
    public List<Measure> getComponentMeasuresInfo(String measureId) {
        final List<Measure> componentMeasures = new ArrayList<>();
        List<ComponentMeasure> components = new ArrayList<>();
        final Measure copositeMeasure = measureDAO.find(measureId);
        if (copositeMeasure != null) {
            components = copositeMeasure.getComponentMeasures();
        }

        for (final ComponentMeasure cm : components) {
            componentMeasures.add(measureDAO.find(cm.getComponentMeasure().getId()));
        }
        return componentMeasures;
    }

    public void setValidator(ValidationUtility validator) {
        this.validator = validator;
    }

    @Override
    public void transferMeasureOwnerShipToUser(final List<String> list, final String toEmail) {
        try {
            final User userTo = userDAO.findByEmail(toEmail);

            for (int i = 0; i < list.size(); i++) {
                final Measure measure = measureDAO.find(list.get(i));
                final List<Measure> ms = new ArrayList<>();
                ms.add(measure);
                //Get All Family Measures for each Measure
                final List<Measure> allMeasures = measureDAO.getAllMeasuresInSet(ms);
                for (int j = 0; j < allMeasures.size(); j++) {

                    String additionalInfo = "Measure Owner transferred from "
                            + allMeasures.get(j).getOwner().getFullName() + " to " + userTo.getFullName();

                    transferAssociatedCQLLibraryOnwnerShipToUser(allMeasures.get(j).getId(), userTo, allMeasures.get(j).getOwner().getFullName());
                    allMeasures.get(j).setOwner(userTo);
                    measureDAO.saveMeasure(allMeasures.get(j));
                    measureAuditLogDAO.recordMeasureEvent(allMeasures.get(j), "Measure Ownership Changed", additionalInfo);
                    additionalInfo = "";


                }
                final List<MeasureShare> measureShareInfo = measureDAO.getMeasureShareForMeasure(list.get(i));
                for (int k = 0; k < measureShareInfo.size(); k++) {
                    measureShareInfo.get(k).setOwner(userTo);
                    measureShareDAO.save(measureShareInfo.get(k));
                }
            }
        } catch (RuntimeException re) {
            logger.error("MeasurePackageServiceImpl::transferMeasureOwnerShipToUser " + re.getMessage(), re);
            throw re;
        }
    }

    private void transferAssociatedCQLLibraryOnwnerShipToUser(String measureId, User toUser, String fromUserFullName) {
        final CQLLibrary cqlLibrary = cqlLibraryDAO.getLibraryByMeasureId(measureId);
        if (cqlLibrary != null) {
            final String additionalInfo = "CQL Library Owner transferred from "
                    + fromUserFullName + " to " + toUser.getFullName();
            cqlLibrary.setOwnerId(toUser);
            cqlLibraryDAO.save(cqlLibrary);
            cqlLibraryAuditLogDAO.recordCQLLibraryEvent(cqlLibrary, "CQL Library Ownership Changed", additionalInfo);

            final List<CQLLibraryShare> cqlLibShareInfo = cqlLibraryDAO.getLibraryShareInforForLibrary(cqlLibrary.getId());
            for (int k = 0; k < cqlLibShareInfo.size(); k++) {
                cqlLibShareInfo.get(k).setOwner(toUser);
                cqlLibraryShareDAO.save(cqlLibShareInfo.get(k));
            }
        }

    }

    @Override
    public void updateLockedOutDate(final Measure m) {
        measureDAO.resetLockDate(m);
    }

    @Override
    public void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate) {
        try {
            measureDAO.updatePrivateColumnInMeasure(measureId, isPrivate);
            measureAuditLogDAO.recordMeasureEvent(getById(measureId), isPrivate
                    ? "Measure Private " : "Measure Public", "");
        } catch (RuntimeException re) {
            logger.error("MeasurePackageServiceImpl::updatePrivateColumnInMeasure " + re.getMessage(), re);
            throw re;
        }
    }

    @Override
    public void updateUsersShare(final ManageMeasureShareModel model) {
        StringBuilder auditLogAdditionlInfo = new StringBuilder("Measure shared with ");
        final StringBuilder auditLogForModifyRemove = new StringBuilder("Measure shared status revoked with ");
        MeasureShare measureShare = null;
        boolean first = true;
        boolean firstRemove = true;
        boolean recordShareEvent = false;
        boolean recordRevokeShareEvent = false;
        try {
            for (int i = 0; i < model.getNumberOfRows(); i++) {
                final MeasureShareDTO dto = model.get(i);
                if ((dto.getShareLevel() != null) && !"".equals(dto.getShareLevel())) {
                    final User user = userDAO.find(dto.getUserId());
                    final ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());
                    measureShare = null;
                    for (final MeasureShare ms : user.getMeasureShares()) {
                        if (ms.getMeasure().getId().equals(model.getMeasureId())) {
                            measureShare = ms;
                            break;
                        }
                    }

                    if ((measureShare == null) && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
                        recordShareEvent = true;
                        measureShare = new MeasureShare();
                        measureShare.setMeasure(measureDAO.find(model.getMeasureId()));
                        measureShare.setShareUser(user);
                        final User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
                        measureShare.setOwner(currentUser);
                        user.getMeasureShares().add(measureShare);
                        currentUser.getOwnedMeasureShares().add(measureShare);
                        logger.debug("Sharing " + measureShare.getMeasure().getId() + " with " + user.getId()
                                + " at level " + sLevel.getDescription());
                        if (!first) { //first time, don't add the comma.
                            auditLogAdditionlInfo.append(", ");
                        }
                        first = false;
                        auditLogAdditionlInfo.append(user.getFullName());

                        measureShare.setShareLevel(sLevel);
                        measureShareDAO.save(measureShare);
                    } else if (measureShare != null && !ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
                        recordRevokeShareEvent = true;
                        measureShareDAO.delete(measureShare.getId());
                        logger.debug("Removing Sharing " + measureShare.getMeasure().getId()
                                + " with " + user.getId()
                                + " at level " + sLevel.getDescription());
                        if (!firstRemove) { //first time, don't add the comma.
                            auditLogForModifyRemove.append(", ");
                        }
                        firstRemove = false;
                        auditLogForModifyRemove.append(user.getFullName());
                    }
                }
            }

            if (recordShareEvent || recordRevokeShareEvent) {
                if (recordShareEvent && recordRevokeShareEvent) {
                    auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
                } else if (recordRevokeShareEvent) {
                    auditLogAdditionlInfo = new StringBuilder(auditLogForModifyRemove);
                }
                if (measureShare != null && measureShare.getMeasure() != null) {
                    measureAuditLogDAO.recordMeasureEvent(measureShare.getMeasure(),
                            "Measure Shared", auditLogAdditionlInfo.toString());
                }
            }
        } catch (Exception e) {
            logger.error("MeasurePackageServiceImpl::updateUsersShare " + e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ValidateMeasureResult validateExportsForCompositeMeasures(final String measureId) throws Exception {
        try {
            final MeasureExport export = generateExport(measureId, null);
            final ValidateMeasureResult result = new ValidateMeasureResult();
            result.setValid(true);

            if (BooleanUtils.isTrue(export.getMeasure().getIsCompositeMeasure())) {
                final CompositeMeasurePackageValidationResult validationResult = compositeMeasurePackageValidator.validate(export.getSimpleXML());
                result.setValid(validationResult.getMessages().isEmpty());
                result.setMessages(validationResult.getMessages());
                result.setValidationMessages(validationResult.getMessages());
            }

            return result;
        } catch (RuntimeException re) {
            logger.error("MeasurePackageServiceImpl::validateExportsForCompositeMeasures " + re.getMessage(), re);
            throw re;
        }
    }

    @Override
    public ValidateMeasureResult createExports(final String key, final List<ValueSet> ValueSetsList, boolean shouldCreateArtifacts) throws Exception {
        final MeasureExport export = generateExport(key, ValueSetsList);
        final ValidateMeasureResult result = new ValidateMeasureResult();
        result.setValidationMessages(new ArrayList<>());
        result.setValid(true);

        List<FhirValidationResult> errorList = createAndSaveExportsAndArtifacts(export, shouldCreateArtifacts);
        if (CollectionUtils.isNotEmpty(errorList)) {
            result.setValid(false);
            errorList.stream()
                    .filter(e -> e.getErrorDescription() != null && !e.getErrorDescription().isEmpty())
                    .limit(5)
                    .forEach(el-> result.getValidationMessages().add(el.getErrorDescription()));
        }
        return result;
    }

    private List<FhirValidationResult> createAndSaveExportsAndArtifacts(MeasureExport export, boolean shouldCreateArtifacts) {
        final Measure measure = export.getMeasure();
        measure.setReleaseVersion(getCurrentReleaseVersion());
        measure.setExportedDate(new Date());
        measureDAO.save(measure);
        measureExportDAO.save(export);
        if (measure.isFhirMeasure()) {
            // New for FHIR we create a CQL_LIBRARY in draft state for the measure so microservices can find it.
            measureLibraryService.exportDraftCQLLibraryForMeasure(measure);
        }
        if (shouldCreateArtifacts) {
            return createPackageArtifacts(measure, export);
        }
        return null;
    }


    @Override
    public String getHumanReadableForNode(final String measureId, final String populationSubXML) {
        String humanReadableHTML = "";
        try {
            final ExportResult exportResult = eMeasureService.getHumanReadableForNode(measureId, populationSubXML);
            humanReadableHTML = exportResult.export;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        return humanReadableHTML;
    }

    @Override
    public boolean getMeasure(String measureId) {
        return measureDAO.getMeasure(measureId);
    }

    public String getCurrentReleaseVersion() {
        return currentReleaseVersion;
    }

    public void setCurrentReleaseVersion(String currentReleaseVersion) {
        this.currentReleaseVersion = currentReleaseVersion;
    }

    public CQLLibraryDAO getCqlLibraryDAO() {
        return cqlLibraryDAO;
    }

    public void setCqlLibraryDAO(CQLLibraryDAO cqlLibraryDAO) {
        this.cqlLibraryDAO = cqlLibraryDAO;
    }

    @Override
    public List<MeasureShareDTO> searchComponentMeasuresWithFilter(MeasureSearchModel measureSearchModel) {
        final User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
        return measureDAO.getComponentMeasureShareInfoForUserWithFilter(measureSearchModel, user);
    }

    @Override
    public void saveComponentMeasures(List<ComponentMeasure> componentMeasuresList) {
        componentMeasuresDAO.saveComponentMeasures(componentMeasuresList);
    }

    @Override
    public void updateComponentMeasures(String compositeMeasureId, List<ComponentMeasure> componentMeasuresList) {
        componentMeasuresDAO.updateComponentMeasures(compositeMeasureId, componentMeasuresList);
    }

}
