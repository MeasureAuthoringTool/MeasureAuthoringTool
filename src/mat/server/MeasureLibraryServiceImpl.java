package mat.server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.NqfModel;
import mat.client.measure.PeriodModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.client.shared.MetaDataConstants;
import mat.model.Author;
import mat.model.ListObject;
import mat.model.MeasureType;
import mat.model.QualityDataModelWrapper;
import mat.model.QualityDataSetDTO;
import mat.model.SecurityRole;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.Metadata;
import mat.model.clause.ShareLevel;
import mat.server.clause.ClauseBusinessService;
import mat.server.service.CodeListService;
import mat.server.service.InvalidValueSetDateException;
import mat.server.service.MeasurePackageService;
import mat.server.service.UserService;
import mat.server.util.MeasureUtility;
import mat.server.util.ResourceLoader;
import mat.server.util.UuidUtility;
import mat.server.util.XmlProcessor;
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;
import mat.shared.model.util.MeasureDetailsUtil;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;





public class MeasureLibraryServiceImpl extends SpringRemoteServiceServlet implements MeasureService{
	private static final long serialVersionUID = 2280421300224680146L;
	private static final Log logger = LogFactory.getLog(MeasureLibraryServiceImpl.class);
	private static final String MEASURE_DETAILS = "measureDetails";
	private static final String MEASURE = "measure";
	
	/**
	 * This method is no longer used as we are loading all the measure details from XML  in Measure_Xml table 
	 * TODO: This should be used only once before the Prod move to Convert all measure to model and marshall as xml and persist in measure_xml table. 
	 * @param measure
	 * @param measureDetailsList
	 * @return
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private ManageMeasureDetailModel extractModel(Measure measure,List<Metadata> measureDetailsList) {
		ManageMeasureDetailModel model = new ManageMeasureDetailModel();
		List<Author> authorList = new ArrayList<Author>(); 
		List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
		List<String> referenceList = new ArrayList<String>();
		model.setId(measure.getId());
		model.setName(measure.getDescription());
		model.setShortName(measure.getaBBRName());
		model.setMeasScoring(measure.getMeasureScoring());
		model.setOrgVersionNumber(MeasureUtility.formatVersionText(String.valueOf(measure.getVersionNumber())));
		model.setVersionNumber(MeasureUtility.getVersionText(measure.getVersion(), measure.isDraft()));
		model.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		model.setDraft(measure.isDraft());
		model.setMeasureSetId(measure.getMeasureSet().getId());
		model.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		model.seteMeasureId(measure.geteMeasureId());
		if(measureDetailsList != null && measureDetailsList.size() > 0){
			for(Metadata measDet: measureDetailsList){
				if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_DEVELOPER)){
					Author a = new Author();
					a.setAuthorName(measDet.getValue());
					authorList.add(a);
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_TYPE)){
					MeasureType mt = new MeasureType();
					mt.setDescription(measDet.getValue());
					measureTypeList.add(mt);
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_SET)){
					model.setGroupName(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.NQF_NUMBER)){
					model.setNqfId(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASUREMENT_FROM_PERIOD)){
					model.setMeasFromPeriod(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASUREMENT_TO_PERIOD)){
					model.setMeasToPeriod(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_STEWARD)){   //US 413 to support steward and other value and removed measure scoring from meta data since its now part of core Measure.
					model.setMeasSteward(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_STEWARD_OTHER)){
					model.setMeasStewardOther(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.ENDORSE_BY_NQF)){
					if(measDet.getValue().equalsIgnoreCase("true"))
						model.setEndorseByNQF(Boolean.TRUE);
					else
						model.setEndorseByNQF(Boolean.FALSE);
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_STATUS)){
					model.setMeasureStatus(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DESCRIPTION)){
					model.setDescription(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.COPYRIGHT)){
					model.setCopyright(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.CLINICAL_RECOM_STATE)){
					model.setClinicalRecomms(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DEFENITION)){
					model.setDefinitions(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.GUIDANCE)){
					model.setGuidance(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.TRANSMISSION_FORMAT)){
					model.setTransmissionFormat(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.RATIONALE)){
					model.setRationale(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.IMPROVEMENT_NOTATION)){
					model.setImprovNotations(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.STRATIFICATION)){
					model.setStratification(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.RISK_ADJUSTMENT)){
					model.setRiskAdjustment(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.REFERENCES)){
					referenceList.add(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS)){
					model.setSupplementalData(measDet.getValue());
				}else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DISCLAIMER)){
					model.setDisclaimer(measDet.getValue());
				}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.RATE_AGGREGATION)){
						model.setRateAggregation(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.INITIAL_PATIENT_POP)){
						model.setInitialPatientPop(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DENOM)){
						model.setDenominator(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DENOM_EXCL)){
						model.setDenominatorExclusions(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.NUM)){
						model.setNumerator(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.NUM_EXCL)){
						model.setNumeratorExclusions(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.DENOM_EXEP)){
						model.setDenominatorExceptions(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_POP)){
						model.setMeasurePopulation(measDet.getValue());
					}
					else if(measDet.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_OBS)){
						model.setMeasureObservations(measDet.getValue());
					}
			}
		}
		model.setAuthorList(authorList);
		model.setMeasureTypeList(measureTypeList);
		model.setReferencesList(referenceList);
		return model;
	}

	private void setValueFromModel(ManageMeasureDetailModel model, Measure measure) {
		measure.setDescription(model.getName());
		measure.setaBBRName(model.getShortName());
		//US 421. Scoring choice is not part of core measure.
		measure.setMeasureScoring(model.getMeasScoring());
		measure.setVersion(model.getVersionNumber());
		measure.setDraft(model.isDraft());
		if(model.getFinalizedDate() != null  && !model.getFinalizedDate().equals(""))
			measure.setFinalizedDate(new Timestamp(DateUtility.convertStringToDate(model.getFinalizedDate()).getTime()));
		if(model.getValueSetDate() != null  && !model.getValueSetDate().equals(""))
			measure.setValueSetDate(new Timestamp(DateUtility.convertStringToDate(model.getValueSetDate()).getTime()));
	}

	private void setValueFromModel(ManageMeasureDetailModel model, List<Metadata> metadataList,Measure m,List<Metadata> existingMetaData){
		HashMap<String,String> detailModelMap = new HashMap<String,String>();
		detailModelMap.put(MetaDataConstants.EMEASURE_TITLE, model.getName());
		detailModelMap.put(MetaDataConstants.EMEASURE_ABBR_TITLE, model.getShortName());
		detailModelMap.put(MetaDataConstants.MEASURE_SET,model.getGroupName());
		detailModelMap.put(MetaDataConstants.NQF_NUMBER, model.getNqfId());
		detailModelMap.put(MetaDataConstants.MEASUREMENT_FROM_PERIOD, model.getMeasFromPeriod());
		detailModelMap.put(MetaDataConstants.MEASUREMENT_TO_PERIOD, model.getMeasToPeriod());
		detailModelMap.put(MetaDataConstants.MEASURE_STEWARD, model.getMeasSteward());

		//US 413. Populate Steward other value from the model.
		if(model.getMeasStewardOther() != null){
			detailModelMap.put(MetaDataConstants.MEASURE_STEWARD_OTHER, model.getMeasStewardOther());
		}
		if(model.getEndorseByNQF() != null){
			detailModelMap.put(MetaDataConstants.ENDORSE_BY_NQF, model.getEndorseByNQF().toString());}
		detailModelMap.put(MetaDataConstants.MEASURE_STATUS, model.getMeasureStatus());
		m.setMeasureStatus(model.getMeasureStatus());
		m.seteMeasureId(model.geteMeasureId());
		detailModelMap.put(MetaDataConstants.DESCRIPTION,model.getDescription());
		detailModelMap.put(MetaDataConstants.COPYRIGHT,model.getCopyright());
		detailModelMap.put(MetaDataConstants.CLINICAL_RECOM_STATE, model.getClinicalRecomms());
		detailModelMap.put(MetaDataConstants.DEFENITION,model.getDefinitions());
		detailModelMap.put(MetaDataConstants.GUIDANCE, model.getGuidance());
		detailModelMap.put(MetaDataConstants.TRANSMISSION_FORMAT, model.getTransmissionFormat());
		detailModelMap.put(MetaDataConstants.RATIONALE,model.getRationale());
		detailModelMap.put(MetaDataConstants.IMPROVEMENT_NOTATION, model.getImprovNotations());
		detailModelMap.put(MetaDataConstants.STRATIFICATION, model.getStratification());
	    detailModelMap.put(MetaDataConstants.RISK_ADJUSTMENT, model.getRiskAdjustment());
	    detailModelMap.put(MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS, model.getSupplementalData());
		
	    detailModelMap.put(MetaDataConstants.DISCLAIMER, model.getDisclaimer());
	    detailModelMap.put(MetaDataConstants.RATE_AGGREGATION, model.getRateAggregation());
	    detailModelMap.put(MetaDataConstants.INITIAL_PATIENT_POP, model.getInitialPatientPop());
	    detailModelMap.put(MetaDataConstants.DENOM, model.getDenominator());
	    detailModelMap.put(MetaDataConstants.DENOM_EXCL, model.getDenominatorExclusions());
	    detailModelMap.put(MetaDataConstants.NUM, model.getNumerator());
	    detailModelMap.put(MetaDataConstants.NUM_EXCL, model.getNumeratorExclusions());
	    detailModelMap.put(MetaDataConstants.DENOM_EXEP, model.getDenominatorExceptions());
	    detailModelMap.put(MetaDataConstants.MEASURE_POP, model.getMeasurePopulation());
	    detailModelMap.put(MetaDataConstants.MEASURE_OBS, model.getMeasureObservations());
	      
	    
	    HashMap<String,List<Author>> authorListMap = new HashMap<String,List<Author>>();
		authorListMap.put(MetaDataConstants.MEASURE_DEVELOPER,model.getAuthorList());
		
		HashMap<String,List<MeasureType>> measureTypeListMap = new HashMap<String, List<MeasureType>>();
		measureTypeListMap.put(MetaDataConstants.MEASURE_TYPE, model.getMeasureTypeList());
		
		HashMap<String,List<String>> referencesListMap = new HashMap<String, List<String>>();
		referencesListMap.put(MetaDataConstants.REFERENCES, model.getReferencesList());

		
		for(String key:detailModelMap.keySet()){
			if(detailModelMap.get(key)!= null && !detailModelMap.get(key).equals(""))
				addMetadata(m, key, detailModelMap.get(key), metadataList);
		 }
		
		//clean up
		List<Author> authorLst = authorListMap.get(MetaDataConstants.MEASURE_DEVELOPER);
		if(authorLst != null){
			for(Author author: authorListMap.get(MetaDataConstants.MEASURE_DEVELOPER)){
				if(author!= null && !author.getAuthorName().equals(""))
					addMetadata(m,MetaDataConstants.MEASURE_DEVELOPER,author.getAuthorName(),metadataList);
			}
		}
		
		List<MeasureType> measureTypeLst = measureTypeListMap.get(MetaDataConstants.MEASURE_TYPE);
		if(measureTypeLst != null){
			for(MeasureType mt: measureTypeListMap.get(MetaDataConstants.MEASURE_TYPE)){
				if(mt!= null && !mt.getDescription().equals(""))
					addMetadata(m,MetaDataConstants.MEASURE_TYPE,mt.getDescription(),metadataList);
			}
		}
		
		List<String> referenceLst = referencesListMap.get(MetaDataConstants.REFERENCES);
		if(referenceLst != null){
			for(String referenceValue : referencesListMap.get(MetaDataConstants.REFERENCES)){
				if(referenceValue != null && !referenceValue.equals(""))
					addMetadata(m,MetaDataConstants.REFERENCES,referenceValue,metadataList);
			}
		}
		
	}
	
	private void addMetadata(Measure m,String key,String value,List<Metadata> metadataList){
		Metadata mt = new Metadata();
		mt.setMeasure(m);
		mt.setName(key);
		mt.setValue(value);
		metadataList.add(mt);
	}

	@Override
	public ManageMeasureDetailModel getMeasure(String key) {
		logger.info("In MeasureLibraryServiceImpl.getMeasure() method..");
		logger.info("Loading Measure for MeasueId: " + key);
		Measure measure = getService().getById(key);
		MeasureXmlModel xml = getMeasureXmlForMeasure(key);
		return convertXmltoModel(xml, measure);	
		
	}

	@Override
	public SaveMeasureResult save(ManageMeasureDetailModel model) {
		boolean isNewMeasure = false;
		Measure pkg = null;
		MeasureSet measureSet = null;
		if(model.getId() != null) {
			//editing an existing measure
			pkg = getService().getById(model.getId());
			model.setVersionNumber(pkg.getVersion());
			if(pkg.getMeasureSet().getId() != null) {
				measureSet = getService().findMeasureSet(pkg.getMeasureSet().getId());
			}
			if(!pkg.getMeasureScoring().equalsIgnoreCase(model.getMeasScoring())){
				//US 194 User is changing the measure scoring. Make sure to delete any groupings for that measure and save.
				getMeasurePackageService().deleteExistingPackages(pkg.getId());
			}
		}
		else {
			//creating a new measure.
			isNewMeasure = true;
			pkg = new Measure();
			model.setMeasureStatus("In Progress");
			measureSet = new MeasureSet();
			measureSet.setId(UUID.randomUUID().toString());
			getService().save(measureSet);
		}
		
		pkg.setMeasureSet(measureSet);
		setValueFromModel(model, pkg);
		SaveMeasureResult result = new SaveMeasureResult();	
		try{			
			getAndValidateValueSetDate(model.getValueSetDate());
			pkg.setValueSetDate(DateUtility.addTimeToDate(pkg.getValueSetDate()));
			getService().save(pkg);
		}catch(InvalidValueSetDateException e){
			result.setSuccess(false);
			result.setFailureReason(SaveMeasureResult.INVALID_VALUE_SET_DATE);
			result.setId(pkg.getId());
			return result;
		}
		
		getClauseBusinessService().setClauseNameForMeasure(model.getId(), model.getShortName());
		result.setSuccess(true);
		result.setId(pkg.getId());
		if(isNewMeasure){
			//Create Default SupplimentalQDM if it is a new Measure.
			//createSupplimentalQDM(pkg);
		}
		saveMeasureXml(createMeasureXmlModel(model, pkg, MEASURE_DETAILS, MEASURE));		
		
		return result;
	}

	/* When a new Measure has been created, always create the default 4 cms supplimental QDM */
	public QualityDataModelWrapper createSupplimentalQDM(String measureId){
		//Get the Supplimental ListObject from the list_object table
		List<ListObject> listOfSuppElements = getCodeListService().getSupplimentalCodeList();
		QualityDataModelWrapper wrapper = new QualityDataModelWrapper();
		ArrayList<QualityDataSetDTO> qdsList = new ArrayList<QualityDataSetDTO>();
		wrapper.setQualityDataDTO(qdsList);
		for(ListObject lo : listOfSuppElements){
			QualityDataSetDTO qds = new QualityDataSetDTO();
			qds.setOid(lo.getOid());
			qds.setUuid(UUID.randomUUID().toString());
			qds.setCodeListName(lo.getName());
			qds.setTaxonomy(lo.getCodeSystem().getDescription());
			qds.setVersion("1");
			qds.setId(lo.getId());
			if(lo.getOid().equalsIgnoreCase("2.16.840.1.113762.1.4.1")){
				//find out patient characteristic gender dataType.
				qds.setDataType((getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_GENDER, lo.getCategory().getId())).getDescription());
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.836")){
				//find out patient characteristic race dataType.
				qds.setDataType((getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_RACE, lo.getCategory().getId())).getDescription());
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.837")){
				//find out patient characteristic ethnicity dataType.
				qds.setDataType((getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_ETHNICITY, lo.getCategory().getId())).getDescription());
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.3591")){
				//find out patient characteristic payer dataType.
				qds.setDataType((getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_PAYER, lo.getCategory().getId())).getDescription());
			}
			
			qds.setSuppDataElement(true);
			//getMeasurePackageService().saveSupplimentalQDM(qds);
			wrapper.getQualityDataDTO().add(qds);
		}
		
		return wrapper;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see mat.client.measure.service.MeasureService#updateLockedDate(java.lang.String, java.lang.String)
	 * This method has been added to update the measureLock Date. This method first gets the exisitingMeasure and then adds the lockedOutDate 
	 * if it is not there. 
	 */
	 @Override
		public SaveMeasureResult updateLockedDate(String measureId,String userId){
			Measure existingmeasure = null;
			User user = null;
			SaveMeasureResult result = new SaveMeasureResult();
			if(measureId != null && userId != null){
				 existingmeasure = getService().getById(measureId);
				 if(existingmeasure != null){
					 if(!isLocked(existingmeasure)){
						 user =   getUserService().getById(userId);
						 existingmeasure.setLockedUser(user);
						 existingmeasure.setLockedOutDate(new Timestamp(new Date().getTime()));
						 getService().save(existingmeasure);
						 result.setSuccess(true);
					 }
				 }
			}
			
			result.setId(existingmeasure.getId());
			return result;
		}
    
    
    //TODO refactor this logic into a shared location: see MeasureDAO
    private boolean isLocked(Measure m){
    	if(m.getLockedOutDate() == null)
    		return false;
    	long lockTime = m.getLockedOutDate().getTime();
    	long currentTime = System.currentTimeMillis();
    	long threshold = 3*60*1000;
    	boolean isLockExpired = currentTime - lockTime > threshold;
    	return !isLockExpired;
    }
    
    
    /*
     * (non-Javadoc)
     * @see mat.client.measure.service.MeasureService#resetLockedDate(java.lang.String, java.lang.String)
     * This method has been added to release the Measure lock. It gets the existingMeasureLock and checks
     * the loggedinUserId and the LockedUserid to release the lock.
     */
    @Override
    public SaveMeasureResult resetLockedDate(String measureId,String userId){
    	Measure existingMeasure = null;
    	User lockedUser = null;
    	SaveMeasureResult result = new SaveMeasureResult();
		if(measureId != null && userId != null){
			 existingMeasure = getService().getById(measureId);
			 if(existingMeasure != null){
				 lockedUser = getLockedUser(existingMeasure);
				 if(lockedUser != null && lockedUser.getId().equalsIgnoreCase(userId)){
					 //Only if the lockedUser and loggedIn User are same we can allow the user to unlock the measure.
					 if(existingMeasure.getLockedOutDate() != null){//if it is not null then set it to null and save it.
						 existingMeasure.setLockedOutDate(null);
						 existingMeasure.setLockedUser(null);
						 getService().updateLockedOutDate(existingMeasure);
						 result.setSuccess(true);
					 } 
				 }
			 }
		}
		result.setId(existingMeasure.getId());
		return result;
    }
	
    private User getLockedUser(Measure existingMeasure){
    	return existingMeasure.getLockedUser();
    }
    
	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {	
		logger.info("In MeasureLibraryServiceImpl.saveMeasureDetails() method..");
		Measure measure = null;
		if(model.getId() != null){
			measure = getService().getById(model.getId());
		}
		List<Metadata> measureDetails = new ArrayList<Metadata>();

		List<Metadata> existingDetails = getService().getMeasureDetailsById(model.getId());
		if(existingDetails != null && existingDetails.isEmpty()){
			setValueFromModel(model, measureDetails,measure,existingDetails);
			getService().saveMeasureDetails(measureDetails);
		}else{
			getService().deleteALLDetailsForMeasureId(existingDetails);
			setValueFromModel(model, measureDetails,measure,existingDetails);
			getService().saveMeasureDetails(measureDetails);
		}
		logger.info("Saving Measure_Xml");
		saveMeasureXml(createMeasureXmlModel(model, measure, MEASURE_DETAILS, MEASURE));
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);	
		logger.info("Saving of Measure Details Success");
		return result;
	}
	
	
	
	public String createMeasureDetailsXml(ManageMeasureDetailModel measureDetailModel, Measure measure){
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsXml()");
		setAdditionalAttrsForMeasureXml(measureDetailModel, measure);
		logger.info("creating XML from Measure Details Model");
		ByteArrayOutputStream stream = createXml(measureDetailModel); 
		System.out.println(stream.toString());
		return stream.toString();
	}

	private ByteArrayOutputStream createXml(
			ManageMeasureDetailModel measureDetailModel) {
		logger.info("In MeasureLibraryServiceImpl.createXml()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
	        marshaller.marshal(measureDetailModel);
	        logger.info("Marshalling of ManageMeasureDetailsModel is successful.." + stream.toString());
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed" + e);
			}else if(e instanceof ValidationException){
				logger.info("Validation Exception" + e);
			}else{
				logger.info("Other Exception" + e);
			}
		} 
		logger.info("Exiting MeasureLibraryServiceImpl.createXml()");
		return stream;
	}
	
	/**
	 * Setting Additional Attributes for Measure Xml.
	 * @param measureDetailModel
	 */
	private void setAdditionalAttrsForMeasureXml(ManageMeasureDetailModel measureDetailModel, Measure measure){
		logger.info("In MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()");
		measureDetailModel.setId(measure.getId());
		measureDetailModel.setMeasureSetId(measure.getMeasureSet() != null ? measure.getMeasureSet().getId() : null);
		measureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(String.valueOf(measure.getVersionNumber())));	
		measureDetailModel.setVersionNumber(MeasureUtility.getVersionText(measureDetailModel.getOrgVersionNumber(), measure.isDraft()));
		measureDetailModel.setId(UuidUtility.idToUuid(measureDetailModel.getId()));// have to change on unmarshalling.
		if(StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod()) || StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())){
			PeriodModel periodModel = new PeriodModel();
			periodModel.setUuid(UUID.randomUUID().toString());
			if(StringUtils.isNotBlank(measureDetailModel.getMeasFromPeriod())){
				periodModel.setStartDate(measureDetailModel.getMeasFromPeriod());
				periodModel.setStartDateUuid(UUID.randomUUID().toString());	
			}
			if(StringUtils.isNotBlank(measureDetailModel.getMeasToPeriod())){
				periodModel.setStopDate(measureDetailModel.getMeasToPeriod());
				periodModel.setStopDateUuid(UUID.randomUUID().toString());	
			}
			measureDetailModel.setPeriodModel(periodModel);
		}
		
		if(StringUtils.isNotBlank(measureDetailModel.getMeasSteward()) && !StringUtils.equalsIgnoreCase(measureDetailModel.getMeasSteward(), "Other")){
			String oid = getService().retrieveStewardOID(measureDetailModel.getMeasSteward().trim());
			measureDetailModel.setStewardUuid(oid);
		}else if(StringUtils.equalsIgnoreCase(measureDetailModel.getMeasSteward(), "Other") && StringUtils.isNotBlank(measureDetailModel.getMeasStewardOther())){
			measureDetailModel.setStewardUuid(UUID.randomUUID().toString());	
		}
		
		if(StringUtils.isNotBlank(measureDetailModel.getGroupName())){
			measureDetailModel.setQltyMeasureSetUuid(UUID.randomUUID().toString());	
		}
		
		setOrgIdInAuthor(measureDetailModel.getAuthorList());
		setMeasureTypeAbbreviation(measureDetailModel.getMeasureTypeList());
		measureDetailModel.setScoringAbbr(setScoringAbbreviation(measureDetailModel.getMeasScoring()));
		
		if(measureDetailModel.getEndorseByNQF() != null && measureDetailModel.getEndorseByNQF()){
			measureDetailModel.setEndorsement("National Quality Forum");
			measureDetailModel.setEndorsementId("2.16.840.1.113883.3.560");
		}
		NqfModel nqfModel = new NqfModel();
		nqfModel.setExtension(measureDetailModel.getNqfId());
		nqfModel.setRoot("2.16.840.1.113883.3.560.1");
		measureDetailModel.setNqfModel(nqfModel);
		if(CollectionUtils.isEmpty(MeasureDetailsUtil.getTrimmedList(measureDetailModel.getReferencesList()))){
			measureDetailModel.setReferencesList(null);
		}
		logger.info("Exiting MeasureLibraryServiceImpl.setAdditionalAttrsForMeasureXml()..");
	}
	
	/**
	 * Adding additonal fields in model from measure table
	 * @param manageMeasureDetailModel
	 * @param measure
	 */
	private void convertAddlXmlElementsToModel(ManageMeasureDetailModel manageMeasureDetailModel, Measure measure){
		logger.info("In easureLibraryServiceImpl.convertAddlXmlElementsToModel()");
		manageMeasureDetailModel.setId(measure.getId());
		manageMeasureDetailModel.setMeasFromPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel.getPeriodModel().getStartDate() : null);
		manageMeasureDetailModel.setMeasToPeriod(manageMeasureDetailModel.getPeriodModel() != null ? manageMeasureDetailModel.getPeriodModel().getStopDate() : null);
		manageMeasureDetailModel.setEndorseByNQF((StringUtils.isNotBlank(manageMeasureDetailModel.getEndorsement()) ? true : false));
		manageMeasureDetailModel.setOrgVersionNumber(MeasureUtility.formatVersionText(String.valueOf(measure.getVersionNumber())));
		manageMeasureDetailModel.setVersionNumber(MeasureUtility.getVersionText(manageMeasureDetailModel.getOrgVersionNumber(), measure.isDraft()));
		manageMeasureDetailModel.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		manageMeasureDetailModel.setDraft(measure.isDraft());
		manageMeasureDetailModel.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		manageMeasureDetailModel.setNqfId(manageMeasureDetailModel.getNqfModel() != null ? manageMeasureDetailModel.getNqfModel().getExtension() : null);
		manageMeasureDetailModel.seteMeasureId(measure.geteMeasureId());
		logger.info("Exiting easureLibraryServiceImpl.convertAddlXmlElementsToModel() method..");
	}
	
	
	private String setScoringAbbreviation(String measScoring) {
		return MeasureDetailsUtil.getScoringAbbr(measScoring);
	}

	private void setMeasureTypeAbbreviation(List<MeasureType> measureTypeList) {
		if(measureTypeList != null){
			for (MeasureType measureType : measureTypeList) {
				measureType.setAbbrDesc(MeasureDetailsUtil.getMeasureTypeAbbr(measureType.getDescription()));
			}
		}
	}

	private MeasureXmlModel createMeasureXmlModel(ManageMeasureDetailModel manageMeasureDetailModel, Measure measure, String replaceNode, String parentNode){
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measure.getId());
		measureXmlModel.setXml(createMeasureDetailsXml(manageMeasureDetailModel, measure));
		measureXmlModel.setToReplaceNode(replaceNode);
		measureXmlModel.setParentNode(parentNode);
		return measureXmlModel;
	}

	
	private void setOrgIdInAuthor(List<Author> authors){
		if(CollectionUtils.isNotEmpty(authors)){
			for (Author author : authors) {
				String oid = getService().retrieveStewardOID(author.getAuthorName().trim());
				author.setOrgId(oid != null && !oid.equals("") ? oid : UUID.randomUUID().toString());
			}
		}
	}
	
	
	@Override
	public ManageMeasureShareModel getUsersForShare(String measureId, int startIndex, int pageSize) {
		ManageMeasureShareModel model = new ManageMeasureShareModel();
		List<MeasureShareDTO> dtoList = getService().getUsersForShare(measureId, startIndex, pageSize);
		model.setResultsTotal(getService().countUsersForMeasureShare());
		List<MeasureShareDTO> dataList = new ArrayList<MeasureShareDTO>();
		for(MeasureShareDTO dto: dtoList){
			dataList.add(dto);
		}
		//model.setData(dtoList); Directly setting dtoList causes the RPC serialization exception(java.util.RandomaccessSubList) since we are sublisting it.
		model.setData(dataList);
		model.setStartIndex(startIndex);	
		model.setMeasureId(measureId);
		return model;
	}

	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		getService().updateUsersShare(model);
	}

	private MeasurePackageService getService() {
		return (MeasurePackageService)context.getBean("measurePackageService");
	}
	
	private ClauseBusinessService getClauseBusinessService() {
		return (ClauseBusinessService)context.getBean("clauseBusinessService");
	}
	
	public CodeListService getCodeListService() {
		return (CodeListService)context.getBean("codeListService");
	}
	
	private UserService getUserService(){
		return (UserService)context.getBean("userService");
	}
	@Override
	public ManageMeasureDetailModel deleteAuthors(String measureId,
			List<Author> selectedAuthorsList) {

		return getService().deleteAuthors(selectedAuthorsList,measureId);
	}

	@Override
	public ManageMeasureDetailModel deleteMeasureTypes(String measureId,
			List<MeasureType> selectedMeasureTypeList) {
		return getService().deleteMeasureTypes(selectedMeasureTypeList, measureId);
	}

	@Override
	public ValidateMeasureResult validateMeasureForExport(String key) throws MatException {
		try {
			return getService().validateMeasureForExport(key);
		}
		catch(Exception exc) {
			log("Exception validating export for " + key, exc);
			throw new MatException(exc.getMessage());
		}
	}

	@Override
	public ManageMeasureSearchModel search(String searchText, int startIndex,int pageSize, int filter) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchWithFilter(searchText, startIndex, pageSize,filter);
		searchModel.setStartIndex(startIndex);
		//searchModel.setResultsTotal((int)getService().count());
		searchModel.setResultsTotal((int)getService().count(filter));

		List<ManageMeasureSearchModel.Result> detailModelList =
			new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		for(MeasureShareDTO dto : measureList) {
			boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
			ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
			detail.setName(dto.getMeasureName());
			detail.setShortName(dto.getShortName());
			detail.setScoringType(dto.getScoringType());
			detail.setStatus(dto.getStatus());
			detail.setId(dto.getMeasureId());
			detail.setStatus(dto.getStatus());
			detail.setClonable(isOwner || isSuperUser);
			detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
			detail.setExportable(dto.isPackaged());
			detail.setSharable(isOwner || isSuperUser);
			detail.setMeasureLocked(dto.isLocked());
			detail.setLockedUserInfo(dto.getLockedUserInfo());
			
			/*US501*/
			detail.setDraft(dto.isDraft());
			String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
			detail.setVersion(formattedVersion);
			detail.setFinalizedDate(dto.getFinalizedDate());
			detail.setMeasureSetId(dto.getMeasureSetId());
			
			detailModelList.add(detail);
		}

		return searchModel;
	}
	
	@Override
	public ManageMeasureSearchModel searchMeasuresForVersion(int startIndex,int pageSize) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForVersion(startIndex, pageSize);
		searchModel.setStartIndex(startIndex);
		searchModel.setResultsTotal((int)getService().countMeasuresForVersion());
		List<ManageMeasureSearchModel.Result> detailModelList =
			new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
	
		for(MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}
		searchModel.setPageCount(getPageCount(searchModel.getResultsTotal(), pageSize));
		return searchModel;
	}


	@Override
	public ManageMeasureSearchModel searchMeasuresForDraft(int startIndex,int pageSize) {
		String currentUserId = LoggedInUserUtil.getLoggedInUser();
		String userRole = LoggedInUserUtil.getLoggedInUserRole();
		boolean isSuperUser = SecurityRole.SUPER_USER_ROLE.equals(userRole);
		ManageMeasureSearchModel searchModel = new ManageMeasureSearchModel();
		List<MeasureShareDTO> measureList = getService().searchMeasuresForDraft(startIndex, pageSize);
		searchModel.setStartIndex(startIndex);
		searchModel.setResultsTotal((int)getService().countMeasuresForDraft());
		List<ManageMeasureSearchModel.Result> detailModelList =
			new ArrayList<ManageMeasureSearchModel.Result>();
		searchModel.setData(detailModelList);
		for(MeasureShareDTO dto : measureList) {
			setDTOtoModel(detailModelList, dto, currentUserId, isSuperUser);
		}	
		searchModel.setPageCount(getPageCount(searchModel.getResultsTotal(), pageSize));
		return searchModel;
	}

	
	@Override
	public SaveMeasureResult saveFinalizedVersion(String measureId,boolean isMajor,String version) {
		logger.info("In MeasureLibraryServiceImpl.saveFinalizedVersion() method..");
		   Measure m = getService().getById(measureId);
		   logger.info("Measure Loaded for: " + measureId);   
		   String versionNumber = null;
		   if(isMajor){
			   versionNumber =   findOutMaximumVersionNumber(m.getMeasureSet().getId());
			   logger.info("Max Version Number loaded from DB: " + versionNumber);   
		   } else {
			   int versionIndex = version.indexOf('v');
			   logger.info("Min Version number passed from Page Model: " + versionIndex);
			   String selectedVersion = version.substring(versionIndex+1);
			   logger.info("Min Version number after trim: " + selectedVersion);
			   versionNumber =   findOutVersionNumber(m.getMeasureSet().getId(),selectedVersion); 
			   
		   }
		   ManageMeasureDetailModel mDetail = getMeasure(measureId);
		   SaveMeasureResult rs = new SaveMeasureResult();
		   int endIndex = versionNumber.indexOf('.');
		   String majorVersionNumber = versionNumber.substring(0, endIndex);
		   if(!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)){
			   String[] versionArr = versionNumber.split("\\.");
			   if(isMajor){
					if(!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION)){
						return incrementVersionNumberAndSave(majorVersionNumber,"1",mDetail,m);
					}else{
						return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MAJOR_VERSION);
					}
				    
				}else{
					if(!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION)){
						return incrementVersionNumberAndSave(versionNumber,"0.001",mDetail,m);
					}
					else
						return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MINOR_VERSION);
				}
		   }else
			   return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_VERSION);
	}
	
	
	
	

	private String findOutMaximumVersionNumber(String measureSetId){
		String maxVerNum = getService().findOutMaximumVersionNumber(measureSetId);
		return maxVerNum;
	}
	
	private String findOutVersionNumber(String measureId, String measureSetId){
		String maxVerNum = getService().findOutVersionNumber(measureId, measureSetId);
		return maxVerNum;
	}
	
	private SaveMeasureResult incrementVersionNumberAndSave(String maximumVersionNumber, String incrementBy,ManageMeasureDetailModel mDetail,Measure meas){
		BigDecimal mVersion = new BigDecimal(maximumVersionNumber);
		mVersion =  mVersion.add(new BigDecimal(incrementBy));
		mDetail.setVersionNumber(mVersion.toString());
		Date currentDate = new Date();
		mDetail.setFinalizedDate(DateUtility.convertDateToString(currentDate));
		mDetail.setDraft(false);
		setValueFromModel(mDetail, meas);
		getService().save(meas);
		saveMeasureXml(createMeasureXmlModel(mDetail, meas, MEASURE_DETAILS, MEASURE));
		getClauseBusinessService().setClauseNameForMeasure(mDetail.getId(), mDetail.getShortName());
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		result.setId(meas.getId());
		String versionStr = meas.getMajorVersionStr()+"."+meas.getMinorVersionStr();
		result.setVersionStr(versionStr);		
		logger.info("Result passed for Version Number " + versionStr);
		return result;
	}
	
	public MeasurePackageService getMeasurePackageService() {
		return (MeasurePackageService)context.getBean("measurePackageService");
	}
	
	private SaveMeasureResult returnFailureReason(SaveMeasureResult rs, int failureReason){
		rs.setFailureReason(failureReason);
		rs.setSuccess(false);
		return rs;
	}
	/*
	private  ManageMeasureDetailModel getMeasureDetail(String mId,Measure m){
		List<Metadata> measureDetails = getService().getMeasureDetailsById(mId);
		return extractModel(m,measureDetails);
	}*/
	
	private void setDTOtoModel(List<ManageMeasureSearchModel.Result> detailModelList,MeasureShareDTO dto ,String currentUserId, boolean isSuperUser){
		boolean isOwner = currentUserId.equals(dto.getOwnerUserId());
		ManageMeasureSearchModel.Result detail = new ManageMeasureSearchModel.Result();
		detail.setName(dto.getMeasureName());
		detail.setShortName(dto.getShortName());
		detail.setStatus(dto.getStatus());
		detail.setId(dto.getMeasureId());
		detail.setStatus(dto.getStatus());
		detail.setClonable(isOwner || isSuperUser);
		detail.setEditable((isOwner || isSuperUser || ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) && dto.isDraft());
		detail.setMeasureLocked(dto.isLocked());
		detail.setExportable(dto.isPackaged());
		detail.setSharable(isOwner || isSuperUser);
		detail.setLockedUserInfo(dto.getLockedUserInfo());
		detail.setDraft(dto.isDraft());
		String formattedVersion = MeasureUtility.getVersionText(dto.getVersion(), dto.isDraft());
		detail.setVersion(formattedVersion);
		detail.setScoringType(dto.getScoringType());
		detail.setMeasureSetId(dto.getMeasureSetId());
		detailModelList.add(detail);
	}
	
	private int getPageCount(long totalRows, int numberOfRows){
		int pageCount = 0;
		int mod = (int) (totalRows % numberOfRows);
		pageCount = (int) (totalRows / numberOfRows);
		pageCount = (mod > 0)?(pageCount + 1) : pageCount;
		return pageCount;
	}
	
	private void getAndValidateValueSetDate(String valueSetDateStr) throws InvalidValueSetDateException{
		if(StringUtils.isNotBlank(valueSetDateStr)){
			DateStringValidator dsv = new DateStringValidator();
			int validationCode = dsv.isValidDateString(valueSetDateStr);
			if(validationCode != dsv.VALID)
				throw new InvalidValueSetDateException();
		}
	}

	@Override
	public boolean isMeasureLocked(String id) {
		MeasurePackageService service = getService();
		boolean isLocked = service.isMeasureLocked(id);
		return isLocked;
	}
	
	public int getMaxEMeasureId(){
		MeasurePackageService service = getService();
		int emeasureId = service.getMaxEMeasureId();
		logger.info("**********Current Max EMeasure Id from DB ******************"+emeasureId);
		return emeasureId;
		//return 2012;
	}

	@Override
	public int generateAndSaveMaxEmeasureId(ManageMeasureDetailModel measureModel) {
		MeasurePackageService service = getService();
		Measure meas = service.getById(measureModel.getId());
		return service.saveAndReturnMaxEMeasureId(meas);
	}

	@Override
	public TransferMeasureOwnerShipModel searchUsers(int startIndex, int pageSize) {
		UserService userService = getUserService();
		List<User> searchResults = userService.searchNonAdminUsers("",startIndex, pageSize);
		logger.info("User search returned " + searchResults.size());
		
		TransferMeasureOwnerShipModel result = new TransferMeasureOwnerShipModel();
		List<TransferMeasureOwnerShipModel.Result> detailList = new ArrayList<TransferMeasureOwnerShipModel.Result>();  
		for(User user : searchResults) {
			TransferMeasureOwnerShipModel.Result r = new TransferMeasureOwnerShipModel.Result();
			r.setFirstName(user.getFirstName());
			r.setLastName(user.getLastName());
			r.setEmailId(user.getEmailAddress());
			r.setKey(user.getId());
			detailList.add(r);
		}
		result.setData(detailList);
		result.setStartIndex(startIndex);
		result.setResultsTotal(getUserService().countSearchResultsNonAdmin(""));
		
		return result;
		
	}
	
	@Override
	public void transferOwnerShipToUser(List<String> list, String toEmail){
		MeasurePackageService service = getService();
		service.transferMeasureOwnerShipToUser(list, toEmail);
	}

	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		logger.info("In MeasureLibraryServiceImpl.getMeasureXmlForMeasure()");
		MeasureXmlModel measureXmlModel = getService().getMeasureXmlForMeasure(measureId);	
		if( measureXmlModel != null){
			logger.info("Measure XML: " + measureXmlModel.getXml());
		}else{
			logger.info("Measure XML is null");
		}
		return measureXmlModel;
	}

	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if(xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())){
			XmlProcessor xmlProcessor = new XmlProcessor(xmlModel.getXml());
			String newXml = xmlProcessor.replaceNode(measureXmlModel.getXml(), measureXmlModel.getToReplaceNode(), measureXmlModel.getParentNode());
			newXml = xmlProcessor.checkForScoringType();
			measureXmlModel.setXml(newXml);
		}else{
			XmlProcessor processor = new XmlProcessor(measureXmlModel.getXml());
			processor.addParentNode(MEASURE);			
			measureXmlModel.setXml(processor.checkForScoringType());
			
			QualityDataModelWrapper wrapper = createSupplimentalQDM(measureXmlModel.getMeasureId());
			// Object to XML for elementLoopUp
			ByteArrayOutputStream streamQDM = convertQualityDataDTOToXML(wrapper);
			// Object to XML for supplementalDataElements
			ByteArrayOutputStream streamSuppDataEle =convertQDMOToSuppleDataXML(wrapper);
			//Remove <?xml> and then replace.
			String filteredString = removePatternFromXMLString(streamQDM.toString().substring(streamQDM.toString().indexOf("<measure>", 0)),"<measure>","");
			filteredString =removePatternFromXMLString(filteredString,"</measure>","");
			//Remove <?xml> and then replace.
			String filteredStringSupp = removePatternFromXMLString(streamSuppDataEle.toString().substring(streamSuppDataEle.toString().indexOf("<measure>", 0)),"<measure>","");
			filteredStringSupp =removePatternFromXMLString(filteredStringSupp,"</measure>","");
			//Add Supplemental data to elementLoopUp
			String result= callAppendNode(measureXmlModel,filteredString,"qdm","/measure/elementLookUp");
			measureXmlModel.setXml(result);
			//Add Supplemental data to supplementalDataElements
			result= callAppendNode(measureXmlModel,filteredStringSupp,"elementRef","/measure/supplementalDataElements");
			measureXmlModel.setXml(result);
			
		}
		getService().saveMeasureXml(measureXmlModel);
	}
	
	private String removePatternFromXMLString(String xmlString,String patternStart,String replaceWith){
		String newString = xmlString;
		if(patternStart !=null){
			newString = newString.replaceAll(patternStart, replaceWith);
		}
		return newString;
	}
	
	/**
	 * Method to call XMLProcessor appendNode method to append new xml nodes into existing xml.
	 * 
	 * */
	private String callAppendNode(MeasureXmlModel measureXmlModel,String newXml,String nodeName,String parentNodeName){
		XmlProcessor xmlProcessor = new XmlProcessor(measureXmlModel.getXml());
		String result = null;
		try {
			result = xmlProcessor.appendNode(newXml,nodeName,parentNodeName);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	@Override
	public void appendAndSaveNode(MeasureXmlModel measureXmlModel, String nodeName){
		MeasureXmlModel xmlModel = getService().getMeasureXmlForMeasure(measureXmlModel.getMeasureId());
		if((xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())) && (nodeName != null && StringUtils.isNotBlank(nodeName))){
			String result =callAppendNode(xmlModel,measureXmlModel.getXml(),nodeName,measureXmlModel.getParentNode()); 
			measureXmlModel.setXml(result);
			getService().saveMeasureXml(measureXmlModel);
		}
		
	}
	
	/**
	 * Method unmarshalls MeasureXML into ManageMeasureDetailModel object
	 * @param xmlModel
	 * @param measure
	 * @return
	 */
	private ManageMeasureDetailModel convertXmltoModel(MeasureXmlModel xmlModel, Measure measure){
		logger.info("In MeasureLibraryServiceImpl.convertXmltoModel()");
		ManageMeasureDetailModel details = null;
		String xml = null;
		if(xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())){
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName(MEASURE_DETAILS);
			logger.info("xml by tag name measureDetails" + xml);
		}
		try {
			if(xml == null){// TODO: This Check should be replaced when the DataConversion is complete.
				logger.info("xml is null or xml doesn't contain measureDetails tag");
				details = new ManageMeasureDetailModel();
				createMeasureDetailsModelFromMeasure(details, measure);
			}else{
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("MeasureDetailsModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(ManageMeasureDetailModel.class);
				unmar.setWhitespacePreserve(true);
				logger.info("unmarshalling xml.. " + xml);
	            details = (ManageMeasureDetailModel)unmar.unmarshal(new InputSource(new StringReader(xml)));
	            logger.info("unmarshalling complete.." + details.toString());
	            convertAddlXmlElementsToModel(details, measure);
			}
		
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load MeasureDetailsModelMapping.xml" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed" + e);
			}else{
				logger.info("Other Exception" + e);
			}
		} 
		return details;
	}
	
	/**
	 * This should be removed when we do a batch save in Measure_XML on production 
	 * @param model
	 * @param measure
	 * @return
	 */
	private void createMeasureDetailsModelFromMeasure(ManageMeasureDetailModel model, Measure measure){
		logger.info("In MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
		model.setId(measure.getId());
		model.setName(measure.getDescription());
		model.setShortName(measure.getaBBRName());
		model.setMeasScoring(measure.getMeasureScoring());
		model.setOrgVersionNumber(MeasureUtility.formatVersionText(String.valueOf(measure.getVersionNumber())));
		model.setVersionNumber(MeasureUtility.getVersionText(model.getOrgVersionNumber(), measure.isDraft()));
		model.setFinalizedDate(DateUtility.convertDateToString(measure.getFinalizedDate()));
		model.setDraft(measure.isDraft());
		model.setMeasureSetId(measure.getMeasureSet().getId());
		model.setValueSetDate(DateUtility.convertDateToStringNoTime(measure.getValueSetDate()));
		model.seteMeasureId(measure.geteMeasureId());
		model.setMeasureStatus(measure.getMeasureStatus());
		logger.info("Exiting MeasureLibraryServiceImpl.createMeasureDetailsModelFromMeasure()");
	}
	
	
	/**
	 * Method called when Measure Details Clone operation is done or Drafting of a version measure is done.
	 * TODO: Sangeethaa This method will have to change when we get all the page items captued as XML
	 * 		1) The MeasureDAO.clone() method should be re written in here
	 * 		
	 */
	public void cloneMeasureXml(boolean creatingDraft, String oldMeasureId, String clonedMeasureId){
		logger.info("In MeasureLibraryServiceImpl.cloneMeasureXml() method. Clonig for Measure: "+ oldMeasureId);
		ManageMeasureDetailModel measureDetailModel = null;
		if(creatingDraft){			
			measureDetailModel = getMeasure(oldMeasureId);// get the measureDetailsmodel object for which draft have to be created..
			Measure measure = getService().getById(clonedMeasureId);//get the Cloned version of the Measure. 
			createMeasureDetailsModelFromMeasure(measureDetailModel, measure); // apply measure values in the created MeasureDetailsModel.
		}else{
			measureDetailModel = getMeasure(clonedMeasureId);
		}
		MeasureXmlModel measureXmlModel = new MeasureXmlModel();
		measureXmlModel.setMeasureId(measureDetailModel.getId());
		measureXmlModel.setXml(createXml(measureDetailModel).toString());	
		measureXmlModel.setToReplaceNode(MEASURE_DETAILS);
		saveMeasureXml(measureXmlModel);
		logger.info("Clone of Measure_xml is Successful");
	}
	
	@Override
	public ArrayList<QualityDataSetDTO> getMeasureXMLForAppliedQDM(String measureId, boolean checkForSupplementData){
		MeasureXmlModel measureXmlModel = getMeasureXmlForMeasure(measureId);
		QualityDataModelWrapper details = convertXmltoQualityDataDTOModel(measureXmlModel);
		ArrayList<QualityDataSetDTO> finalList = new ArrayList<QualityDataSetDTO>();
		if(details !=null ){
		
			if(details.getQualityDataDTO()!=null && details.getQualityDataDTO().size()!=0){
				logger.info(" details.getQualityDataDTO().size() :"+ details.getQualityDataDTO().size());
				for(QualityDataSetDTO dataSetDTO: details.getQualityDataDTO()){
					if(dataSetDTO.getCodeListName() !=null)
						if(checkForSupplementData && dataSetDTO.isSuppDataElement())
							continue;
						else
							finalList.add(dataSetDTO);
				}
			}
			Collections.sort(finalList, new Comparator<QualityDataSetDTO>() {
				@Override
				public int compare(QualityDataSetDTO o1, QualityDataSetDTO o2) {
					return o1.getCodeListName().compareToIgnoreCase(o2.getCodeListName());
				}
			});
		}
		logger.info("finalList()of QualityDataSetDTO ::"+ finalList.size());
		return finalList;
		
	}
	
	
	
	 /**
     * Method to create XML from QualityDataModelWrapper object for elementLookUp.
     * */
    private ByteArrayOutputStream convertQualityDataDTOToXML(QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In MeasureLibraryServiceImpl.convertQualityDataDTOToXML()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
	        marshaller.marshal(qualityDataSetDTO);
	        logger.info("Marshalling of QualityDataSetDTO is successful.." + stream.toString());
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load QualityDataModelMapping.xml in convertQualityDataDTOToXML method" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed in convertQualityDataDTOToXML method" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed in convertQualityDataDTOToXML method" + e);
			}else if(e instanceof ValidationException){
				logger.info("Validation Exception in convertQualityDataDTOToXML method" + e);
			}else{
				logger.info("Other Exception in convertQualityDataDTOToXML method " + e);
			}
		} 
		logger.info("Exiting MeasureLibraryServiceImpl.convertQualityDataDTOToXML()");
		return stream;
	}
    
    /**
     * Method to create XML from QualityDataModelWrapper object for supplementalDataElement .
     * */
    private ByteArrayOutputStream convertQDMOToSuppleDataXML(QualityDataModelWrapper qualityDataSetDTO) {
		logger.info("In MeasureLibraryServiceImpl.convertQDMOToSuppleDataXML()");
		Mapping mapping = new Mapping();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			mapping.loadMapping(new ResourceLoader().getResourceAsURL("QDMToSupplementDataMapping.xml"));
			Marshaller marshaller = new Marshaller(new OutputStreamWriter(stream));
			marshaller.setMapping(mapping);
	        marshaller.marshal(qualityDataSetDTO);
	        logger.info("Marshalling of QualityDataSetDTO is successful in convertQDMOToSuppleDataXML()" + stream.toString());
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load QualityDataModelMapping.xml in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed in convertQDMOToSuppleDataXML()" + e);
			}else if(e instanceof ValidationException){
				logger.info("Validation Exception in convertQDMOToSuppleDataXML()" + e);
			}else{
				logger.info("Other Exception in convertQDMOToSuppleDataXML()" + e);
			}
		} 
		logger.info("Exiting MeasureLibraryServiceImpl.convertQDMOToSuppleDataXML()");
		return stream;
	}
	
	private QualityDataModelWrapper convertXmltoQualityDataDTOModel(MeasureXmlModel xmlModel){
		logger.info("In MeasureLibraryServiceImpl.convertXmltoQualityDataDTOModel()");
		QualityDataModelWrapper details = null;
		String xml = null;
		if(xmlModel != null && StringUtils.isNotBlank(xmlModel.getXml())){
			xml = new XmlProcessor(xmlModel.getXml()).getXmlByTagName("measure");
			logger.info("xml by tag name elementlookup" + xml);
		}
		try {
			if(xml == null){// TODO: This Check should be replaced when the DataConversion is complete.
				logger.info("xml is null or xml doesn't contain elementlookup tag");
				
			}else{
				Mapping mapping = new Mapping();
				mapping.loadMapping(new ResourceLoader().getResourceAsURL("QualityDataModelMapping.xml"));
				Unmarshaller unmar = new Unmarshaller(mapping);
				unmar.setClass(QualityDataModelWrapper.class);
				unmar.setWhitespacePreserve(true);
				logger.info("unmarshalling xml..elementlookup " + xml);
	            details = (QualityDataModelWrapper)unmar.unmarshal(new InputSource(new StringReader(xml)));
	            logger.info("unmarshalling complete..elementlookup" + details.getQualityDataDTO().get(0).getCodeListName());
	        }
		
		} catch (Exception e) {
			if(e instanceof IOException){
				logger.info("Failed to load QualityDataModelMapping.xml" + e);
			}else if(e instanceof MappingException){
				logger.info("Mapping Failed" + e);
			}else if(e instanceof MarshalException){
				logger.info("Unmarshalling Failed" + e);
			}else{
				logger.info("Other Exception" + e);
			}
		} 
		return details;
	}
}
