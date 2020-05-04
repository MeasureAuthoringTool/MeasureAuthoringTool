package mat.server.service.impl;

import mat.dto.fhirconversion.ConversionOutcome;
import mat.dto.fhirconversion.ConversionResultDto;
import mat.dto.fhirconversion.ConversionType;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.cql.CqlParser;
import mat.cql.CqlVisitorFactory;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.FhirCqlLibraryService;
import mat.server.service.FhirLibraryConversionRemoteCall;
import mat.server.util.MATPropertiesService;
import mat.server.util.XmlProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class FhirCqlLibraryServiceImpl implements FhirCqlLibraryService{

    private static final Log logger = LogFactory.getLog(FhirCqlLibraryServiceImpl.class);

    @Autowired
    private FhirLibraryConversionRemoteCall fhirLibraryConversionRemoteCall;

    @Autowired
    private CQLLibraryServiceInterface cqlLibraryServiceInterface;

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    private final CqlParser cqlParser;

    private final CqlVisitorFactory cqlVisitorFactory;

    private final XMLMarshalUtil xmlMarshalUtil = new XMLMarshalUtil();

    public FhirCqlLibraryServiceImpl(CqlParser cqlParser, CqlVisitorFactory cqlVisitorFactory) {
        this.cqlParser = cqlParser;
        this.cqlVisitorFactory = cqlVisitorFactory;
    }

    @Override
    public FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject sourceLibrary, String loggedInUser) throws MatException, MarshalException, MappingException, IOException, ValidationException {

        FhirConvertResultResponse fhirConvertResultResponse = new FhirConvertResultResponse();

        if (!sourceLibrary.isFhirConvertible()) {
            throw new MatException("Cql Library cannot be converted to FHIR");
        }

        fhirConvertResultResponse.setSourceLibrary(sourceLibrary);
        cqlLibraryServiceInterface.isLibraryAvailableAndLogRecentActivity(sourceLibrary.getId(), loggedInUser);

        CQLLibrary existingLibrary = cqlLibraryDAO.find(sourceLibrary.getId());

        /*if draft FHIR library already exists then delete it */
        deleteDraftFhirLibrariesInSet(existingLibrary.getSetId());

        ConversionResultDto conversionResult = fhirLibraryConversionRemoteCall.convert(sourceLibrary.getId(), ConversionType.CONVERSION);
        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        if (!fhirCqlOpt.isPresent()) {
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
        validationSummary.setOutcome(convertResult.getOutcome() != null ? convertResult.getOutcome().toString() : null);
        validationSummary.setValidationPassed(ConversionOutcome.SUCCESS.equals(convertResult.getOutcome()));
        return validationSummary;
    }

    private Optional<String> getFhirCql(ConversionResultDto conversionResult) {
        return Optional.ofNullable(conversionResult.getLibraryConversionResults()).stream()
                .flatMap(libConvRes -> libConvRes.stream())
                .map(cqlLibRes -> cqlLibRes.getCqlConversionResult())
                .filter(el -> el != null)
                .map(el -> el.getFhirCql())
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

        newFhirLibrary.setLibraryXMLAsByteArray(generateCqlXml(existingLibrary,fhirCqlOpt));

        newFhirLibrary.setVersion(existingLibrary.getVersion());
        newFhirLibrary.setRevisionNumber("000");
        cqlLibraryServiceInterface.save(newFhirLibrary);

        CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlLibraryServiceInterface.extractCQLLibraryDataObject(newFhirLibrary);

        fhirConvertResultResponse.setFhirLibrary(cqlLibraryDataSetObject);
        cqlLibraryServiceInterface.isLibraryAvailableAndLogRecentActivity(newFhirLibrary.getId(), loggedInUser);
    }

    public void deleteDraftFhirLibrariesInSet(String setId) {
        logger.debug("deleteDraftFhirLibrariesInSet : setId = " + setId);
        int removed = cqlLibraryServiceInterface.deleteDraftFhirLibrariesBySetId(setId);
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

        cqlParser.parse(newCql,convCqlToMatXml);

        var destModel = convCqlToMatXml.getDestinationModel();
        return CQLUtilityClass.getXMLFromCQLModel(destModel);
    }
}
