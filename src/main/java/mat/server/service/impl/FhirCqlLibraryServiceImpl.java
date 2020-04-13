package mat.server.service.impl;

import gov.cms.mat.fhir.rest.dto.ConversionOutcome;
import gov.cms.mat.fhir.rest.dto.ConversionResultDto;
import gov.cms.mat.fhir.rest.dto.ConversionType;
import mat.client.measure.service.CQLService;
import mat.client.measure.service.FhirConvertResultResponse;
import mat.client.measure.service.FhirValidationStatus;
import mat.client.shared.MatException;
import mat.dao.clause.CQLLibraryDAO;
import mat.model.clause.CQLLibrary;
import mat.model.clause.ModelTypeHelper;
import mat.model.cql.CQLLibraryDataSetObject;
import mat.server.service.CQLLibraryServiceInterface;
import mat.server.service.FhirCqlLibraryService;
import mat.server.service.FhirLibraryConversionRemoteCall;
import mat.server.util.MATPropertiesService;
import mat.server.util.MeasureUtility;
import mat.server.util.XmlProcessor;
import mat.shared.SaveUpdateCQLResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.xpath.XPathExpressionException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class FhirCqlLibraryServiceImpl implements FhirCqlLibraryService{

    private static final Log logger = LogFactory.getLog(FhirCqlLibraryServiceImpl.class);

    @Autowired
    private FhirLibraryConversionRemoteCall fhirLibraryConversionRemoteCall;

    @Autowired
    private FhirConvertResultResponse fhirConvertResultResponse;

    @Autowired
    private CQLLibraryServiceInterface cqlLibraryServiceInterface;

    @Autowired
    private CQLLibraryDAO cqlLibraryDAO;

    @Autowired
    private CQLService cqlService;

    @Override
    public FhirConvertResultResponse convertCqlLibrary(CQLLibraryDataSetObject sourceLibrary, String loggedInUser) throws MatException {

        if (!sourceLibrary.isFhirConvertible()) {
            throw new MatException("Cql Library cannot be converted to FHIR");
        }

        fhirConvertResultResponse.setSourceLibrary(sourceLibrary);
        cqlLibraryServiceInterface.isLibraryAvailableAndLogRecentActivity(sourceLibrary.getId(), loggedInUser);

        CQLLibrary existingLibrary = cqlLibraryDAO.find(sourceLibrary.getId());

        /*if draft FHIR library already exists then delete it here*/
        deleteDraftFhirLibrariesInSet(existingLibrary.getSetId());

        ConversionResultDto conversionResult = fhirLibraryConversionRemoteCall.convert(sourceLibrary.getId(), ConversionType.CONVERSION);
        Optional<String> fhirCqlOpt = getFhirCql(conversionResult);

        FhirValidationStatus validationStatus = createValidationStatus(conversionResult);
        fhirConvertResultResponse.setValidationStatus(validationStatus);

        if (!fhirCqlOpt.isPresent()) {
            // If there is no FHIR CQL, then we don't persist the Library. FHIR Library cannot be created.
            throw new MatException("Your library cannot be converted to FHIR. Outcome: " + validationStatus.getOutcome() + " Error Reason: " + validationStatus.getErrorReason());
        } else {
            persistFhirLibrary(loggedInUser, existingLibrary);
        }
        //we have to store this library in DB
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

    private void persistFhirLibrary(String loggedInUser, CQLLibrary existingLibrary) {
        CQLLibrary newLibraryObject = new CQLLibrary();
        newLibraryObject.setDraft(true);
        newLibraryObject.setName(existingLibrary.getName());
        newLibraryObject.setSetId(existingLibrary.getSetId());
        newLibraryObject.setOwnerId(existingLibrary.getOwnerId());
        newLibraryObject.setReleaseVersion(MATPropertiesService.get().getCurrentReleaseVersion());
        newLibraryObject.setQdmVersion(MATPropertiesService.get().getQdmVersion());
        newLibraryObject.setLibraryModelType(ModelTypeHelper.FHIR);
        // Update QDM Version to latest QDM Version.
        String versionLibraryXml = getCQLLibraryXml(existingLibrary);
        if (versionLibraryXml != null) {
            XmlProcessor processor = new XmlProcessor(getCQLLibraryXml(existingLibrary));
            try {
                MeasureUtility.updateModelVersion(processor, existingLibrary.isFhirMeasure());
                SaveUpdateCQLResult saveUpdateCQLResult = cqlService.getCQLLibraryData(versionLibraryXml, existingLibrary.getLibraryModelType());
                List<String> usedCodeList = saveUpdateCQLResult.getUsedCQLArtifacts().getUsedCQLcodes();
                processor.removeUnusedDefaultCodes(usedCodeList);
                processor.clearValuesetVersionAttribute();
                processor.updateCQLLibraryName(existingLibrary.getName());
                versionLibraryXml = processor.transform(processor.getOriginalDoc());
            } catch (XPathExpressionException e) {
                logger.error(e.getMessage(), e);
            }
        }

        newLibraryObject.setCQLByteArray(versionLibraryXml.getBytes());
        newLibraryObject.setVersion(existingLibrary.getVersion());
        newLibraryObject.setRevisionNumber("000");
        cqlLibraryServiceInterface.save(newLibraryObject);

        CQLLibraryDataSetObject cqlLibraryDataSetObject = cqlLibraryServiceInterface.extractCQLLibraryDataObject(newLibraryObject);

        fhirConvertResultResponse.setFhirLibrary(cqlLibraryDataSetObject);
        cqlLibraryServiceInterface.isLibraryAvailableAndLogRecentActivity(newLibraryObject.getId(), loggedInUser);
    }

    private String getCQLLibraryXml(CQLLibrary library) {
        String xmlString = null;
        if (library != null) {
            try {
                xmlString = new String(library.getCqlXML().getBytes(1l, (int) library.getCqlXML().length()));
            } catch (SQLException e) {
                logger.error(e);
            }
        }
        return xmlString;
    }

    public void deleteDraftFhirLibrariesInSet(String setId) {
        logger.debug("deleteDraftFhirLibrariesInSet : setId = " + setId);
        int removed = cqlLibraryServiceInterface.deleteDraftFhirLibrariesBySetId(setId);
        logger.debug("deleteDraftFhirLibrariesInSet : removed " + removed);
    }
}
