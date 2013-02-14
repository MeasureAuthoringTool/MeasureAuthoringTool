package mat.server;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import mat.client.clause.clauseworkspace.modal.MeasureExportModal;
import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureSearchModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.TransferMeasureOwnerShipModel;
import mat.client.measure.service.MeasureService;
import mat.client.measure.service.SaveMeasureResult;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatException;
import mat.client.shared.MetaDataConstants;
import mat.model.Author;
import mat.model.ListObject;
import mat.model.MeasureType;
import mat.model.QualityDataSet;
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
import mat.shared.ConstantMessages;
import mat.shared.DateStringValidator;
import mat.shared.DateUtility;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;




public class MeasureLibraryServiceImpl extends SpringRemoteServiceServlet implements MeasureService{
	private static final long serialVersionUID = 2280421300224680146L;
	private static final Log logger = LogFactory.getLog(MeasureLibraryServiceImpl.class);
	
	
	
	private ManageMeasureDetailModel extractModel(Measure measure,List<Metadata> measureDetailsList) {
		ManageMeasureDetailModel model = new ManageMeasureDetailModel();
		List<Author> authorList = new ArrayList<Author>(); 
		List<MeasureType> measureTypeList = new ArrayList<MeasureType>();
		List<String> referenceList = new ArrayList<String>();
		model.setId(measure.getId());
		model.setName(measure.getDescription());
		model.setShortName(measure.getaBBRName());
		model.setMeasScoring(measure.getMeasureScoring());
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
		Measure pkg = getService().getById(key);
		List<Metadata> measureDetails = getService().getMeasureDetailsById(key);
		return extractModel(pkg,measureDetails);
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
			pkg.setMeasureStatus("In Progress");
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
			createSupplimentalQDM(pkg);
		}
		return result;
	}

	/* When a new Measure has been created, always create the default 4 cms supplimental QDM */
	public void createSupplimentalQDM(Measure newMeasure){
		//Get the Supplimental ListObject from the list_object table
		List<ListObject> listOfSuppElements = getCodeListService().getSupplimentalCodeList();
		for(ListObject lo : listOfSuppElements){
			QualityDataSet qds = new QualityDataSet();
			qds.setListObject(lo);
			qds.setMeasureId(newMeasure);
			qds.setOid(getMeasurePackageService().getUniqueOid());
			qds.setVersion("1");//Don't know whether to have version or not?
			if(lo.getOid().equalsIgnoreCase("2.16.840.1.113762.1.4.1")){
				//find out patient characteristic gender dataType.
				qds.setDataType(getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_GENDER, lo.getCategory().getId()));
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.836")){
				//find out patient characteristic race dataType.
				qds.setDataType(getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_RACE, lo.getCategory().getId()));
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.837")){
				//find out patient characteristic ethnicity dataType.
				qds.setDataType(getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_ETHNICITY, lo.getCategory().getId()));
			}else if(lo.getOid().equalsIgnoreCase("2.16.840.1.114222.4.11.3591")){
				//find out patient characteristic payer dataType.
				qds.setDataType(getMeasurePackageService().findDataTypeForSupplimentalCodeList(ConstantMessages.PATIENT_CHARACTERISTIC_PAYER, lo.getCategory().getId()));
			}
			
			qds.setSuppDataElement(true);
			getMeasurePackageService().saveSupplimentalQDM(qds);
		}
		
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
    
    /*
    private String goodChars = "`~1!2@3#4$5%6^7&8*9(0)-_=+qwertyuiop[]\\QWERTYUIOP{}|asdfghjkl;'ASDFGHJKL:\"zxcvbnm,./ZXCVBNM<>?";
    private Set<Character> allowedChars = new HashSet<Character>();
	private void initValidation(){
		for(int i=0;i<goodChars.length();i++){
			allowedChars.add(goodChars.charAt(i));
		}
	}
	private boolean isGoodChars(String s){
		for(int i=0;i<s.length();i++){
			if(!allowedChars.contains(s.charAt(i)))
				return false;
		}
		return true;
	}
    */
    
	@Override
	public SaveMeasureResult saveMeasureDetails(ManageMeasureDetailModel model) {
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
			int emid1 = model.geteMeasureId();
			int emid2 = measure.geteMeasureId();
			setValueFromModel(model, measureDetails,measure,existingDetails);
			//Remove the following block of saving emeasureid , since we can save the emeasureIdentifer when we click the generateEmeasureIdentifier Button
//			if(emid1 != emid2){
//				getService().saveEMeasureId(measure);
//			}
			getService().saveMeasureDetails(measureDetails);
		}

		List<Metadata> metaDetails = getService().getMeasureDetailsById(model.getId());
		ManageMeasureDetailModel detailmodel = extractModel(measure,metaDetails);

		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		if(detailmodel != null){
			result.setAuthorList(detailmodel.getAuthorList());
			result.setMeasureTypeList(detailmodel.getMeasureTypeList());
		}
		
		//Example of setting an error condition.
		//result.setSuccess(false);
		//result.setFailureReason(ConstantMessages.ID_NOT_UNIQUE);
		return result;
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
		   Measure m = getService().getById(measureId);
		   String versionNumber = null;
		   if(isMajor){
			   versionNumber =   findOutMaximumVersionNumber(m.getMeasureSet().getId());
		   } else {
			   int versionIndex = version.indexOf('v');
			   String selectedVersion = version.substring(versionIndex+1);
			   versionNumber =   findOutVersionNumber(m.getMeasureSet().getId(),selectedVersion); 
		   }
		   ManageMeasureDetailModel mDetail = getMeasureDetail(measureId, m);
		   SaveMeasureResult rs = new SaveMeasureResult();
		   int endIndex = versionNumber.indexOf('.');
		   String majorVersionNumber = versionNumber.substring(0, endIndex);
		   if(!versionNumber.equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_VERSION)){
			   String[] versionArr = versionNumber.split("\\.");
			   if(isMajor){
				    if(!versionArr[0].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MAJOR_VERSION))
				    	return incrementVersionNumberAndSave(majorVersionNumber,"1",mDetail,m);
					else
						return returnFailureReason(rs, SaveMeasureResult.REACHED_MAXIMUM_MAJOR_VERSION);
				    
				}else{
					if(!versionArr[1].equalsIgnoreCase(ConstantMessages.MAXIMUM_ALLOWED_MINOR_VERSION))
						return incrementVersionNumberAndSave(versionNumber,"0.001",mDetail,m);
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
		getClauseBusinessService().setClauseNameForMeasure(mDetail.getId(), mDetail.getShortName());
		SaveMeasureResult result = new SaveMeasureResult();
		result.setSuccess(true);
		result.setId(meas.getId());
		String versionStr = meas.getMajorVersionStr()+"."+meas.getMinorVersionStr();
		result.setVersionStr(versionStr);
		
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
	
	private  ManageMeasureDetailModel getMeasureDetail(String mId,Measure m){
		List<Metadata> measureDetails = getService().getMeasureDetailsById(mId);
		return extractModel(m,measureDetails);
	}
	
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
	public MeasureExportModal getMeasureExoportForMeasure(String measureId) {
		return getService().getMeasureExoportForMeasure(measureId);
	}

	@Override
	public void saveMeasureExport(MeasureExportModal measureExportModal) {
		getService().saveMeasureExport(measureExportModal);
	}
	
	
}
