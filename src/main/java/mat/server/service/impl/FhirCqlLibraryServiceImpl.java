package mat.server.service.impl;

import ca.uhn.fhir.context.FhirContext;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirLibraryPackageResult;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.cql.CqlParser;
import mat.cql.CqlVisitorFactory;
import mat.dao.clause.CQLLibraryDAO;
import mat.dto.fhirconversion.ConversionOutcome;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.dto.fhirconversion.CqlConversionResult;
import mat.dto.fhirconversion.LibraryConversionResults;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.FhirCqlLibraryService;
import mat.server.service.FhirLibraryRemoteCall;
import mat.server.util.MATPropertiesService;
import mat.server.util.XmlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.impl.dv.util.Base64;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.hl7.fhir.r4.model.Attachment;
import org.hl7.fhir.r4.model.Library;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class FhirCqlLibraryServiceImpl implements FhirCqlLibraryService {

    private static final Log logger = LogFactory.getLog(FhirCqlLibraryServiceImpl.class);

    private final FhirLibraryRemoteCall fhirLibRemote;

    private final CQLLibraryServiceInterface cqlLibService;

    private final CQLLibraryDAO cqlLibraryDAO;

    private final FhirContext fhirContext;

    private final CqlParser cqlParser;

    private final CqlVisitorFactory cqlVisitorFactory;

    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    public FhirCqlLibraryServiceImpl(CqlParser cqlParser,
                                     CqlVisitorFactory cqlVisitorFactory,
                                     FhirLibraryRemoteCall fhirLibRemote,
                                     CQLLibraryServiceInterface cqlLibService,
                                     CQLLibraryDAO cqlLibDao,
                                     FhirContext fhirContext) {
        this.cqlParser = cqlParser;
        this.cqlVisitorFactory = cqlVisitorFactory;
        this.fhirLibRemote = fhirLibRemote;
        this.cqlLibService = cqlLibService;
        this.cqlLibraryDAO = cqlLibDao;
        this.fhirContext = fhirContext;
    }

    @Override
    public void pushCqlLib(String libId) {
        logger.info("pushCqlLib: " + libId);
        String hapiUrl = fhirLibRemote.pushStandAlone(libId);
        logger.info("pushCqlLib: " + hapiUrl);
    }

    @Override
    public FhirLibraryPackageResult packageCqlLib(String libId) {
        FhirLibraryPackageResult result = new FhirLibraryPackageResult();
        logger.info("packageCqlLib: " + libId);
        Library lib = fhirLibRemote.packageStandAlone(libId);

        lib.getContent().forEach(a -> {
            if (StringUtils.equalsIgnoreCase("text/cql", a.getContentType())) {
                result.setCql(decodeBase64(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+json", a.getContentType())) {
                result.setElmJson(decodeBase64(a));
            } else if (StringUtils.equalsIgnoreCase("application/elm+xml", a.getContentType())) {
                result.setElmXml(decodeBase64(a));
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

        fhirConvertResultResponse.setSourceLibrary(sourceLibrary);
        cqlLibService.isLibraryAvailableAndLogRecentActivity(sourceLibrary.getId(), loggedInUser);

        CQLLibrary existingLibrary = cqlLibraryDAO.find(sourceLibrary.getId());

        /*if draft FHIR library already exists then delete it */
        deleteDraftFhirLibrariesInSet(existingLibrary.getSetId());

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

    private void persistFhirLibrary(String loggedInUser, CQLLibrary existingLibrary, String fhirCqlOpt, FhirConvertResultResponse fhirConvertResultResponse) throws MatException, MarshalException, MappingException, ValidationException, IOException {
        CQLLibrary newFhirLibrary = new CQLLibrary();
        newFhirLibrary.setDraft(true);
        newFhirLibrary.setName(existingLibrary.getName());
        newFhirLibrary.setSetId(existingLibrary.getSetId());
        newFhirLibrary.setOwnerId(existingLibrary.getOwnerId());
        newFhirLibrary.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
        newFhirLibrary.setFhirVersion(MATPropertiesService.get().getQdmVersion());
        newFhirLibrary.setLibraryModelType(ModelTypeHelper.FHIR);

        newFhirLibrary.setLibraryXMLAsByteArray(generateCqlXml(existingLibrary, fhirCqlOpt));

        newFhirLibrary.setVersion(existingLibrary.getVersion());
        newFhirLibrary.setRevisionNumber("000");
        cqlLibService.save(newFhirLibrary);

        CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlLibService.extractCQLLibraryDataObject(newFhirLibrary);

        fhirConvertResultResponse.setFhirLibrary(cqlLibraryDataSetObject);
        cqlLibService.isLibraryAvailableAndLogRecentActivity(newFhirLibrary.getId(), loggedInUser);
    }

    public void deleteDraftFhirLibrariesInSet(String setId) {
        logger.debug("deleteDraftFhirLibrariesInSet : setId = " + setId);
        int removed = cqlLibService.deleteDraftFhirLibrariesBySetId(setId);
        logger.debug("deleteDraftFhirLibrariesInSet : removed " + removed);
    }

    private String generateCqlXml(CQLLibrary existingLibrary, String newCql) throws MarshalException, MappingException, ValidationException, IOException, MatException {
        String sourceCqlXml = existingLibrary.getLibraryXMLAsString();

        XmlProcessor processor = new XmlProcessor(sourceCqlXml);
        String cqlModelXmlFrag = processor.getXmlByTagName("cqlLookUp");
        CQLModel sourceCqlModel = (CQLModel) xmlMarshalUtil.convertXMLToObject("CQLModelMapping.xml",
                cqlModelXmlFrag,
                CQLModel.class);

        var convCqlToMatXml = cqlVisitorFactory.getCqlToMatXmlVisitor();
        convCqlToMatXml.setSourceModel(sourceCqlModel);

        cqlParser.parse(newCql, convCqlToMatXml);

        var destModel = convCqlToMatXml.getDestinationModel();
        return CQLUtilityClass.getXMLFromCQLModel(destModel);
    }

    private String decodeBase64(Attachment a) {
        return new String(Base64.decode(
                new String(a.getData(), StandardCharsets.UTF_8)),
                StandardCharsets.UTF_8);
    }
}
