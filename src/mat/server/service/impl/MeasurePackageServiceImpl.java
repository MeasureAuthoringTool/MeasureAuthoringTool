package mat.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mat.client.measure.ManageMeasureDetailModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.client.shared.MatContext;
import mat.client.shared.MetaDataConstants;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.MetadataDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureShareDAO;
import mat.dao.clause.PackagerDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.model.Author;
import mat.model.DataType;
import mat.model.MeasureType;
import mat.model.QualityDataSet;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.Metadata;
import mat.model.clause.ShareLevel;
import mat.server.LoggedInUserUtil;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.shared.ValidationUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasurePackageServiceImpl implements MeasurePackageService {
	private static final Log logger = LogFactory.getLog(MeasurePackageServiceImpl.class);
	
	@Autowired
	private MeasureDAO measurePackageDAO;
	@Autowired
	private MeasureExportDAO measureExportDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	@Autowired
	private MeasureShareDAO measureShareDAO;
	@Autowired
	private MeasureDAO measureDAO;
	
	@Autowired
	private MetadataDAO metaDataDAO;
	
	@Autowired
	private PackagerDAO packagerDAO;

	@Autowired 
	private MeasureAuditLogDAO measureAuditLogDAO;

	@Autowired 
	private SimpleEMeasureService eMeasureService;
	
	@Autowired
	private MeasureSetDAO measureSetDAO;
	
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	
	@Autowired
	private DataTypeDAO dataTypeDAO;
//	@Override
//	public void clone(Measure measurePackage, String newCloneName) {
//		measurePackageDAO.clone(measurePackage, newCloneName);
//	}

	@Override
	public long count() {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(user);
	}
	
	@Override
	public long count(String searchText) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(searchText, user);
	}
	
	@Override
	public List<MeasureShareDTO> search(int startIndex,
			int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUser(user, startIndex-1, numResults);
	}

	@Override
	public void updateUsersShare(ManageMeasureShareModel model) {
		StringBuffer auditLogAdditionlInfo = new StringBuffer("Measure shared with ");
		MeasureShare measureShare = null;
		boolean first = true;
		for(int i = 0; i < model.getNumberOfRows(); i++) {
			MeasureShareDTO dto = model.get(i);
			if(dto.getShareLevel() != null && !"".equals(dto.getShareLevel())) {
				User user = userDAO.find(dto.getUserId());
				ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());				
				measureShare = null;
				for(MeasureShare ms : user.getMeasureShares()) {
					if(ms.getMeasure().getId().equals(model.getMeasureId())) {
						measureShare = ms;
						break;
					}
				}
				
				if(measureShare == null) {
					measureShare = new MeasureShare();
					measureShare.setMeasure(measurePackageDAO.find(model.getMeasureId()));
					measureShare.setShareUser(user);
					User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					measureShare.setOwner(currentUser);
					user.getMeasureShares().add(measureShare);
					currentUser.getOwnedMeasureShares().add(measureShare);
				}

				logger.info("Sharing " + measureShare.getMeasure().getId() + " with " + user.getId() + 
						" at level " + sLevel.getDescription());
				
				if(!first){ //first time, don't add the comma.
					auditLogAdditionlInfo.append(", ");
				}
				first = false;
				auditLogAdditionlInfo.append(user.getEmailAddress() + ":" + sLevel.getDescription());

				measureShare.setShareLevel(sLevel);
				measureShareDAO.save(measureShare);
			}
		}
		
		//US 170. Log share event
		if(measureShare != null){
			measureAuditLogDAO.recordMeasureEvent(measureShare.getMeasure(), "Measure Shared", auditLogAdditionlInfo.toString());
		}
	}

	@Override
	public int countUsersForMeasureShare() {
		return measurePackageDAO.countUsersForMeasureShare();
	}
	
	@Override 
	public void save(Measure measurePackage) {
		if(measurePackage.getOwner() == null) {
			if(LoggedInUserUtil.getLoggedInUser() != null){
				User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				measurePackage.setOwner(currentUser);
			}
		}
		measurePackageDAO.save(measurePackage);
	}
	@Override 
	public void save(MeasureSet measureSet) {
		measureSetDAO.save(measureSet);
	}
	
	
	@Override
	public void updateLockedOutDate(Measure m) {
		measurePackageDAO.resetLockDate(m);
	}

	@Override
	public void saveMeasureDetails(List<Metadata> measureDetails){
		if(measureDetails != null){
			metaDataDAO.batchSave(measureDetails);
		}
	}
	
	@Override
	public Measure getById(String id) {
		return measurePackageDAO.find(id);
	}
	
	@Override
	public MeasureSet findMeasureSet(String id) {
		return measureSetDAO.findMeasureSet(id);
	}
	
	@Override
	public List<MeasureShareDTO> getUsersForShare(String measureId, int startIndex, int pageSize) {
		return measurePackageDAO.getMeasureShareInfoForMeasure(measureId, startIndex - 1, pageSize);
	}

	@Override
	public List<Metadata> getMeasureDetailsById(String id) {
		return metaDataDAO.getMeasureDetails(id);
	}
	
	@Override
	public Metadata getMetadata(String id){
		return metaDataDAO.find(id);
	}
	
	private ManageMeasureDetailModel getMeasureMetadatadetails(String id){
		List<Metadata> measureDetailList = metaDataDAO.getMeasureDetails(id);
		ManageMeasureDetailModel model = new ManageMeasureDetailModel();
		model.setId(id);
		setMetadataonModel(model,measureDetailList);
		return model;
	}
	
	
	private void  setMetadataonModel(ManageMeasureDetailModel model, List<Metadata> metadataList){
	    List<Author> authorList = new ArrayList();
	    List<MeasureType> measureTypeList = new ArrayList();
		for(Metadata mt: metadataList){
			if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_DEVELOPER)){
				Author a = new Author();
				a.setAuthorName(mt.getValue());
				authorList.add(a);
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_TYPE)){
				MeasureType m = new MeasureType();
				m.setDescription(mt.getValue());
				measureTypeList.add(m);
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.EMEASURE_TITLE)){
				model.setName(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.EMEASURE_ABBR_TITLE)){
				model.setShortName(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_SET)){
				model.setGroupName(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASUREMENT_FROM_PERIOD)){
				model.setMeasFromPeriod(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASUREMENT_TO_PERIOD)){
				model.setMeasToPeriod(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.MEASURE_STEWARD)){
				model.setMeasSteward(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.ENDORSE_BY_NQF)){
				if(mt.getValue().equalsIgnoreCase("true")){
					model.setEndorseByNQF(Boolean.TRUE);
				}else{
					model.setEndorseByNQF(Boolean.FALSE);
				}
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.DESCRIPTION)){
				model.setDescription(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.COPYRIGHT)){
				model.setCopyright(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.CLINICAL_RECOM_STATE)){
				model.setClinicalRecomms(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.DEFENITION)){
				model.setDefinitions(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.GUIDANCE)){
				model.setGuidance(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.TRANSMISSION_FORMAT)){
				model.setTransmissionFormat(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.RATIONALE)){
				model.setRationale(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.IMPROVEMENT_NOTATION)){
				model.setImprovNotations(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.STRATIFICATION)){
				model.setStratification(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.RISK_ADJUSTMENT)){
				model.setRiskAdjustment(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS)){
				model.setSupplementalData(mt.getValue());
			}else if(mt.getName().equalsIgnoreCase(MetaDataConstants.REFERENCES)){
				//model.setReference(mt.getValue());
			}
			
		}
		model.setAuthorList(authorList);
		model.setMeasureTypeList(measureTypeList);
	}
	
	
	public ManageMeasureDetailModel deleteAuthors(List<Author> authorList,String id){
		if(authorList != null){
			metaDataDAO.deleteAuthor(authorList, id);
		}
		ManageMeasureDetailModel  detailModel = getMeasureMetadatadetails(id);
		return detailModel;
	}
	
	
	public ManageMeasureDetailModel deleteMeasureTypes(List<MeasureType> measureTypeList, String id){
		if(measureTypeList != null){
			metaDataDAO.deleteMeasureTypes(measureTypeList,id);
		}
		ManageMeasureDetailModel  detailModel = getMeasureMetadatadetails(id);
		return detailModel;
	}

	@Override
	public void update(String metadataId,String keyValue) {
		Metadata metadata = getMetadata(metadataId);
		metadata.setValue(keyValue);
		metaDataDAO.save(metadata);
		
	}

	@Override
	public void deleteALLDetailsForMeasureId(List<Metadata> metaDataDetails) {
		if(metaDataDetails != null)
			metaDataDAO.deleteAllMetaData(metaDataDetails);
	}
	
	private Map<String, String> buildMetadataMap(List<Metadata> list) {
		HashMap<String, String> map = new HashMap<String, String>();
		for(Metadata md : list) {
			map.put(md.getName(), md.getValue());
		}
		return map;
	}
	
	
	private boolean isTooLong(Map<String, String> metadata, String key, int maxLength){
		if(metadata.get(key) == null)
			return false;
		if(metadata.get(key).length() > maxLength)
			return true;
		return false;
	}
	
	private boolean isEmpty(Map<String, String> metadata, String key) {
		return !metadata.containsKey(key) || metadata.get(key).equals("") || metadata.get(key).equalsIgnoreCase("--Select--");
	}
	
	private boolean notvalidDate(Map<String, String> metadata, String key){
		if(metadata.get(key) != null){
			return metadata.get(key).length() > 0 && metadata.get(key).length() < 10;
		}else
			return false;
	}
	@Override
	public ValidateMeasureResult validateMeasureForExport(String key) throws Exception {

		List<Metadata> metadataList = metaDataDAO.getMeasureDetails(key);		
		Map<String, String> metadataMap = buildMetadataMap(metadataList);

		//Populate measure and scoring choice. 
		Measure measure = getById(key);
		if(measure != null){
			metadataMap.put(MetaDataConstants.EMEASURE_TITLE, measure.getDescription());
			metadataMap.put(MetaDataConstants.EMEASURE_ABBR_TITLE, measure.getaBBRName());
			metadataMap.put(MetaDataConstants.MEASURE_SCORING, measure.getMeasureScoring());
		}

		//initValidation();
		List<String> message = new ArrayList<String>();

		//  Keep commented until these are implemented so Grace can test
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_STATUS)){
			message.add(MatContext.get().getMessageDelegate().getMeasureStatusRequiredMessage());
		}

		if(isEmpty(metadataMap, MetaDataConstants.NQF_NUMBER)) {
			message.add(MatContext.get().getMessageDelegate().getNQFNumberRequiredMessage());
			
		}

		if(isEmpty(metadataMap, MetaDataConstants.MEASUREMENT_FROM_PERIOD)) {
			message.add(MatContext.get().getMessageDelegate().getMeasurePeriodFromRequiredMessage());
		}
		if(notvalidDate(metadataMap, MetaDataConstants.MEASUREMENT_FROM_PERIOD)){
			message.add(MatContext.get().getMessageDelegate().getMeasurePeriodFromDateInvalidMessage());
		}
		if(isEmpty(metadataMap, MetaDataConstants.MEASUREMENT_TO_PERIOD)) {
			message.add(MatContext.get().getMessageDelegate().getMeasurePeriodToRequiredMessage());
		}
		if(notvalidDate(metadataMap, MetaDataConstants.MEASUREMENT_TO_PERIOD)){
			message.add(MatContext.get().getMessageDelegate().getMeasurePeriodToDateInvalidMessage());
		}
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_STEWARD)) {
			message.add(MatContext.get().getMessageDelegate().getMeasureStewardRequiredMessage());
		}else{
			//US 413. Validate Measure Steward and Steward Other values.			
			String stewardValue = metadataMap.get(MetaDataConstants.MEASURE_STEWARD);			
			if(stewardValue != null && stewardValue.equalsIgnoreCase("Other")){				
				if(isEmpty(metadataMap, MetaDataConstants.MEASURE_STEWARD_OTHER)){
					message.add(MatContext.get().getMessageDelegate().getMeasureStewardRequiredMessage());
				}
			}			
		}
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_DEVELOPER)) {
			message.add(MatContext.get().getMessageDelegate().getAuthorRequiredMessage());
		}
		
		if(isEmpty(metadataMap, MetaDataConstants.ENDORSE_BY_NQF)) {
			message.add(MatContext.get().getMessageDelegate().getEndorsedByRequiredMessage());
		}

		validator.validate(metadataMap, MetaDataConstants.DESCRIPTION);
		if(isEmpty(metadataMap, MetaDataConstants.DESCRIPTION)){
			message.add(MatContext.get().getMessageDelegate().getDescriptionRequiredMeassage());
		}
		if(isTooLong(metadataMap, MetaDataConstants.DESCRIPTION, 15000)){
			message.add("Description is too long message");
		}
		
		validator.validate(metadataMap, MetaDataConstants.COPYRIGHT);
		if(isEmpty(metadataMap, MetaDataConstants.COPYRIGHT)){
			message.add(MatContext.get().getMessageDelegate().getCopyrightRequiredMeassage());
		}

		validator.validate(metadataMap, MetaDataConstants.DISCLAIMER);
		if(isEmpty(metadataMap, MetaDataConstants.DISCLAIMER)){
			message.add(MatContext.get().getMessageDelegate().getDisclaimerRequiredMessage());
		}

		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_TYPE)){
			message.add(MatContext.get().getMessageDelegate().getMeasureTypeRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.STRATIFICATION);
		if(isEmpty(metadataMap, MetaDataConstants.STRATIFICATION)) {
			message.add(MatContext.get().getMessageDelegate().getStratRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.RISK_ADJUSTMENT);
		if(isEmpty(metadataMap, MetaDataConstants.RISK_ADJUSTMENT)) {
			message.add(MatContext.get().getMessageDelegate().getRiskAdjustmentRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.RATE_AGGREGATION);
		if(isEmpty(metadataMap, MetaDataConstants.RATE_AGGREGATION)) {
			message.add(MatContext.get().getMessageDelegate().getRateAggregationRequiredMessage());
		}

		validator.validate(metadataMap, MetaDataConstants.RATIONALE);
		if(isEmpty(metadataMap, MetaDataConstants.RATIONALE)) {
			message.add(MatContext.get().getMessageDelegate().getRationaleRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.CLINICAL_RECOM_STATE);
		if(isEmpty(metadataMap, MetaDataConstants.CLINICAL_RECOM_STATE)) {
			message.add(MatContext.get().getMessageDelegate().getClinicalRecomendRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.IMPROVEMENT_NOTATION);
		if(isEmpty(metadataMap, MetaDataConstants.IMPROVEMENT_NOTATION)) {
			message.add(MatContext.get().getMessageDelegate().getImporvementNotationRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.REFERENCES);
		if(isEmpty(metadataMap, MetaDataConstants.REFERENCES)) {
			message.add(MatContext.get().getMessageDelegate().getReferencesRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.DEFENITION);
		if(isEmpty(metadataMap, MetaDataConstants.DEFENITION)) {
			message.add(MatContext.get().getMessageDelegate().getDefinitionRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.GUIDANCE);
		if(isEmpty(metadataMap, MetaDataConstants.GUIDANCE)) {
			message.add(MatContext.get().getMessageDelegate().getGuidenceRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.TRANSMISSION_FORMAT);
		if(isEmpty(metadataMap, MetaDataConstants.TRANSMISSION_FORMAT)) {
			message.add(MatContext.get().getMessageDelegate().getTransmissionFormatRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.INITIAL_PATIENT_POP);
		if(isEmpty(metadataMap, MetaDataConstants.INITIAL_PATIENT_POP)){
			message.add(MatContext.get().getMessageDelegate().getInitialPatientPopRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.DENOM);
		if(isEmpty(metadataMap, MetaDataConstants.DENOM)){
			message.add(MatContext.get().getMessageDelegate().getDenominatorRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.DENOM_EXCL);
		if(isEmpty(metadataMap, MetaDataConstants.DENOM_EXCL)){	
			message.add(MatContext.get().getMessageDelegate().getDenominatorExclusionsRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.NUM);
		if(isEmpty(metadataMap, MetaDataConstants.NUM)){
			message.add(MatContext.get().getMessageDelegate().getNumeratorRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.NUM_EXCL);
		if(isEmpty(metadataMap, MetaDataConstants.NUM_EXCL)){
			message.add(MatContext.get().getMessageDelegate().getNumeratorExclusionsRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.DENOM_EXEP);
		if(isEmpty(metadataMap, MetaDataConstants.DENOM_EXEP)){
			message.add(MatContext.get().getMessageDelegate().getDenominatorExceptionsRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.MEASURE_POP);
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_POP)){
			message.add(MatContext.get().getMessageDelegate().getMeasurePopulationRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.MEASURE_OBS);
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_OBS)){
			message.add(MatContext.get().getMessageDelegate().getMeasureObservationRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS);
		if(isEmpty(metadataMap, MetaDataConstants.SUPPLEMENTAL_DATA_ELEMENTS)) {
			message.add(MatContext.get().getMessageDelegate().getSuplementalRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.MEASURE_SET);
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_SET)) {
			message.add(MatContext.get().getMessageDelegate().getMeasureSetRequiredMessage());
		}

		validator.validate(metadataMap, MetaDataConstants.EMEASURE_TITLE);
		if(isEmpty(metadataMap, MetaDataConstants.EMEASURE_TITLE)) {
			message.add(MatContext.get().getMessageDelegate().getMeasureNameRequiredMessage());
		}
		validator.validate(metadataMap, MetaDataConstants.EMEASURE_ABBR_TITLE);
		if(isEmpty(metadataMap, MetaDataConstants.EMEASURE_ABBR_TITLE)) {
			message.add(MatContext.get().getMessageDelegate().getAbvMeasureNameRequired());
		}

		validator.validate(metadataMap, MetaDataConstants.MEASURE_SCORING);
		if(isEmpty(metadataMap, MetaDataConstants.MEASURE_SCORING)) {
			message.add(MatContext.get().getMessageDelegate().getMeasureScoreRequiredMessage());
		}
	
		long numPackages = packagerDAO.getNumberOfPackagesForMeasure(key);
		if(numPackages == 0) {
			message.add(MatContext.get().getMessageDelegate().getGroupingRequiredMessage());
		}

		//doPackageGroupingValidation(){

			//}

		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(message.size() == 0);
		result.setValidationMessages(message);

		if(result.isValid()) {
			//TODO422: 
			generateExport(key);
		}

		return result;
	}

	//TODO422:
	private void generateExport(String measureId) throws Exception {
		SimpleEMeasureService.ExportResult exportResult = 
			eMeasureService.exportMeasureIntoSimpleXML(measureId);

		Measure measure = measureDAO.find(measureId);
		MeasureExport export = measureExportDAO.findForMeasure(measureId);
		if(export == null) {
			export = new MeasureExport();
			export.setMeasure(measure);
		}
		export.setSimpleXML(exportResult.export);
		export.setCodeListBarr(exportResult.wkbkbarr);
		measure.setExportedDate(new Date());
		measureDAO.save(measure);
		measureExportDAO.save(export);
	}

	@Override
	public List<MeasureShareDTO> search(String searchText, int startIndex,
			int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUser(searchText, metaDataDAO, user, startIndex-1, numResults);
	}

	
	@Override
	public String findOutMaximumVersionNumber(String measureSetId) {
	   return measureDAO.findMaxVersion(measureSetId);
	}
	
	@Override
	public String findOutVersionNumber(String measureId, String measureSetId) {
	   return measureDAO.findMaxOfMinVersion(measureId, measureSetId);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#searchMeasuresForDraft(int, int)
	 */
	@Override
	public List<MeasureShareDTO> searchMeasuresForDraft(int startIndex,
			int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasuresForDraft(user, startIndex, numResults);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#searchMeasuresForVersion(int, int)
	 */
	@Override
	public List<MeasureShareDTO> searchMeasuresForVersion(int startIndex,
			int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasuresForVersion(user, startIndex, numResults);
	}	
	
	public long countMeasuresForVersion(){
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureForVersion(user);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#countMeasuresForDraft()
	 */
	@Override
	public long countMeasuresForDraft() {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureForDraft(user);
	}

	
	
	ValidationUtility validator = new ValidationUtility();
	
	@Override
	public String getUniqueOid(){
		return qualityDataSetDAO.generateUniqueOid();
	}
	
	@Override
	public DataType findDataTypeForSupplimentalCodeList(String dataTypeName,String categoryId){
		return dataTypeDAO.findDataTypeForSupplimentalCodeList(dataTypeName, categoryId);
	}

	@Override
	public void saveSupplimentalQDM(QualityDataSet qds) {
	    qualityDataSetDAO.save(qds);	
	}

	@Override
	public boolean isMeasureLocked(String id) {
		boolean isLocked = measureDAO.isMeasureLocked(id);
		return isLocked;
	}
	
	@Override
	public int getMaxEMeasureId(){
		return measureDAO.getMaxEMeasureId();	
	}
	
	@Override
	public int saveAndReturnMaxEMeasureId(Measure measure){
		return measureDAO.saveandReturnMaxEMeasureId(measure);
	}

	@Override
	public void deleteExistingPackages(String measureId) {
		packagerDAO.deleteAllPackages(measureId);
		
	}
}
