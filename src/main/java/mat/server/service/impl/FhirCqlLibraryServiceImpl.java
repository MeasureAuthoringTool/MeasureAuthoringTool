package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirLibraryPackageResult;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.dao.FhirConversionHistoryDao;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dto.fhirconversion.*;
import mat.model.User;
import mat.model.clause.CQLLibrary;
import mat.model.clause.FhirConversionHistory;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.LoggedInUserUtil;
import mat.server.logging.LogFactory;
import mat.server.service.CQLLibraryAuditService;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.FhirCqlLibraryService;
import mat.server.service.FhirLibraryRemoteCall;
import mat.server.service.cql.FhirCqlParser;
import mat.server.util.MATPropertiesService;
import mat.server.util.XmlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hl7.fhir.r4.model.Library;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class FhirCqlLibraryServiceImpl implements FhirCqlLibraryService {

    private static final Log logger = LogFactory.getLog(FhirCqlLibraryServiceImpl.class);

    private final FhirLibraryRemoteCall fhirLibRemote;

    private final CQLLibraryServiceInterface cqlLibService;

    private final CQLLibraryDAO cqlLibraryDAO;

    private final FhirContext fhirContext;

    private final FhirCqlParser cqlParser;

    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    private final CQLLibraryAuditService auditService;

    private final UserDAO userDAO;

    private final FhirConversionHistoryDao fhirConversionHIstoryDAO;

    public FhirCqlLibraryServiceImpl(FhirCqlParser cqlParser,
                                     FhirLibraryRemoteCall fhirLibRemote,
                                     CQLLibraryServiceInterface cqlLibService,
                                     CQLLibraryDAO cqlLibDao,
                                     FhirContext fhirContext,
                                     CQLLibraryAuditService auditService,
                                     UserDAO userDAO,
                                     FhirConversionHistoryDao fhirConversionHIstoryDAO) {
        this.cqlParser = cqlParser;
        this.fhirLibRemote = fhirLibRemote;
        this.cqlLibService = cqlLibService;
        this.cqlLibraryDAO = cqlLibDao;
        this.fhirContext = fhirContext;
        this.auditService = auditService;
        this.userDAO = userDAO;
        this.fhirConversionHIstoryDAO = fhirConversionHIstoryDAO;
    }

    @Override
    public void pushCqlLib(String libId) {
        logger.debug("pushCqlLib: " + libId);
        String hapiUrl = fhirLibRemote.pushStandAlone(libId);
        logger.debug("pushCqlLib: " + hapiUrl);
    }

    @Override
    public FhirLibraryPackageResult packageCqlLib(String libId) {
        FhirLibraryPackageResult result = new FhirLibraryPackageResult();
        logger.debug("packageCqlLib: " + libId);
        Library lib = fhirLibRemote.packageStandAlone(libId);

        lib.getContent().forEach(a -> {
            if (StringUtils.equalsIgnoreCase("text/cql", a.getContentType())) {
                result.setCql(new String(a.getData()));
            } else if (StringUtils.equalsIgnoreCase("application/elm+json", a.getContentType())) {
                result.setElmJson(new String(a.getData()));
            } else if (StringUtils.equalsIgnoreCase("application/elm+xml", a.getContentType())) {
                result.setElmXml(new String(a.getData()));
            }
        });

        result.setFhirJson(fhirContext.newJsonParser().encodeResourceToString(lib));
        result.setFhirXml(fhirContext.newXmlParser().encodeResourceToString(lib));

        return result;
    }

    @Override
    public FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject sourceLibrary, String loggedInUser) throws MatException, MarshalException, MappingException, IOException, ValidationException {

        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();

        if (!sourceLibrary.isFhirConvertible()) {
            throw new MatException("Cql Library cannot be converted to FHIR");
        }

        cqlLibService.isLibraryAvailableAndLogRecentActivity(sourceLibrary.getId(), loggedInUser);

        CQLLibrary existingLibrary = cqlLibraryDAO.find(sourceLibrary.getId());

        ConversionResultDto conversionResult = fhirLibRemote.convert(sourceLibrary.getId(), ConversionType.CONVERSION);
        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        if (fhirCqlOpt.isEmpty()) {
            // If there is no FHIR CQL, then we don't persist the Library. FHIR Library cannot be created.
            throw new MatException("Your library cannot be converted to FHIR. Outcome: " + validationStatus.getOutcome() + " Error Reason: " + validationStatus.getErrorReason());
        } else {
            /*Create a new cql Library object and store it in DB*/
            persistFhirLibrary(loggedInUser, existingLibrary, fhirCqlOpt.get(), fhirConvertResultResponse);
        }

        return fhirConvertResultResponse;
    }

    private FhirValidationStatus createValidationStatus(ConversionResultDto convertResult) {
        FhirValidationStatus validationSummary = new FhirValidationStatus();
        validationSummary.setErrorReason(convertResult.getErrorReason());
        validationSummary.setOutcome(convertResult.getOutcome());
        validationSummary.setValidationPassed(ConversionOutcome.SUCCESS.equals(convertResult.getOutcome()));
        return validationSummary;
    }

    private Optional<String> getFhirCql(ConversionResultDto conversionResult) {
        return Optional.ofNullable(conversionResult.getLibraryConversionResults()).stream()
                .flatMap(Collection::stream)
                .map(LibraryConversionResults::getCqlConversionResult)
                .filter(Objects::nonNull)
                .map(CqlConversionResult::getFhirCql)
                .filter(StringUtils::isNotBlank)
                .findFirst();
    }

    private String getConvertedLibName(String oldName) {
        //Remove _ and append FHIR.
        return StringUtils.replace(oldName,"_","") + "FHIR";
    }

    private void persistFhirLibrary(String loggedInUser,
                                    CQLLibrary existingLibrary,
                                    String fhirCqlOpt,
                                    FhirConvertResultResponse fhirConvertResultResponse)
            throws MarshalException, MappingException, ValidationException, IOException {
        User user = userDAO.findByLoginId(LoggedInUserUtil.getLoggedUserName());
        CQLLibrary newFhirLibrary = new CQLLibrary();
        newFhirLibrary.setDraft(true);
        newFhirLibrary.setName(getConvertedLibName(existingLibrary.getName())); //For conversion append FHIR
        newFhirLibrary.setDescription(existingLibrary.getDescription());
        newFhirLibrary.setSetId(UUID.randomUUID().toString()); //For conversion create a new set id for FHIR.
        newFhirLibrary.setOwnerId(existingLibrary.getOwnerId());
        newFhirLibrary.setVersion("0.000");
        newFhirLibrary.setRevisionNumber("000");
        newFhirLibrary.setFhirVersion(MATPropertiesService.get().getFhirVersion());
        newFhirLibrary.setLibraryModelType(ModelTypeHelper.FHIR);
        newFhirLibrary.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
        newFhirLibrary.setLastModifiedBy(user);
        newFhirLibrary.setLastModifiedOn(LocalDateTime.now());
        newFhirLibrary.setLibraryXMLAsByteArray(generateCqlXml(existingLibrary, newFhirLibrary,fhirCqlOpt));

        cqlLibService.save(newFhirLibrary);

        fhirConvertResultResponse.setFhirMeasureId(newFhirLibrary.getId());
        cqlLibService.isLibraryAvailableAndLogRecentActivity(newFhirLibrary.getId(), loggedInUser);

        createFhirConversionHistory(existingLibrary,newFhirLibrary,user);

        auditService.recordCQLLibraryEvent(newFhirLibrary.getId(),
                "Converted from QDM/CQL to FHIR",
                existingLibrary.getName() + ", Version " + existingLibrary.getMatVersion() + " was converted to FHIR.",
                false);

        auditService.recordCQLLibraryEvent(existingLibrary.getId(),
                "Converted from QDM/CQL to FHIR",
                newFhirLibrary.getName() + " FHIR DRAFT 0.0.000 was created by converting from QDM Version " + existingLibrary.getMatVersion(),
                false);
    }

    private String generateCqlXml(CQLLibrary existingLibrary, CQLLibrary newFhirLibrary, String newCql) throws MarshalException, MappingException, ValidationException, IOException {
        String sourceCqlXml = existingLibrary.getLibraryXMLAsString();

        XmlProcessor processor = new XmlProcessor(sourceCqlXml);
        String cqlModelXmlFrag = processor.getXmlByTagName("cqlLookUp");
        CQLModel sourceCqlModel = (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml",
                cqlModelXmlFrag,
                CQLModel.class);

        var destModel = cqlParser.parse(newCql, sourceCqlModel).getCqlModel();
        destModel.setLibraryName(newFhirLibrary.getName());
        destModel.setVersionUsed(newFhirLibrary.getMatVersion());
        return CQLUtilityClass.getXMLFromCQLModel(destModel);
    }

    private void createFhirConversionHistory(CQLLibrary existingLib, CQLLibrary newFhirLib, User user) {
        var h = new FhirConversionHistory();
        h.setQdmSetId(existingLib.getSetId());
        h.setFhirSetId(newFhirLib.getSetId());
        h.setLastModifiedBy(user);
        h.setLastModifiedOn(new Timestamp(System.currentTimeMillis()));
        fhirConversionHIstoryDAO.save(h);
    }
}