package mat.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.PropertyOperator;
import mat.dao.QualityDataSetDAO;
import mat.dao.StewardDAO;
import mat.dao.UserDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureShareDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.PackagerDAO;
import mat.dao.clause.ShareLevelDAO;
import mat.dao.search.CriteriaQuery;
import mat.dao.search.SearchCriteria;
import mat.model.DataType;
import mat.model.MeasureSteward;
import mat.model.QualityDataSet;
import mat.model.User;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.model.clause.MeasureSet;
import mat.model.clause.MeasureShare;
import mat.model.clause.MeasureShareDTO;
import mat.model.clause.MeasureXML;
import mat.model.clause.ShareLevel;
import mat.server.LoggedInUserUtil;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.util.ExportSimpleXML;
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

	@Autowired
	private MeasureXMLDAO measureXMLDAO;

	@Autowired
	private StewardDAO stewardDAO;

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
	public long count(int filter) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(filter, user);
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
		StringBuffer auditLogForModifyRemove = new StringBuffer("Measure shared status revoked with ");
		MeasureShare measureShare = null;
		boolean first = true;
		boolean firstRemove = true;
		boolean recordShareEvent = false;
		boolean recordRevokeShareEvent = false;
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

				if(measureShare == null && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordShareEvent = true;
					measureShare = new MeasureShare();
					measureShare.setMeasure(measurePackageDAO.find(model.getMeasureId()));
					measureShare.setShareUser(user);
					User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					measureShare.setOwner(currentUser);
					user.getMeasureShares().add(measureShare);
					currentUser.getOwnedMeasureShares().add(measureShare);
					logger.info("Sharing " + measureShare.getMeasure().getId() + " with " + user.getId() + 
							" at level " + sLevel.getDescription());
					if(!first){ //first time, don't add the comma.
						auditLogAdditionlInfo.append(", ");
					}
					first = false;
					auditLogAdditionlInfo.append(user.getEmailAddress());

					measureShare.setShareLevel(sLevel);
					measureShareDAO.save(measureShare);
				}else if(!ShareLevel.MODIFY_ID.equals(dto.getShareLevel())){
					recordRevokeShareEvent = true;
					measureShareDAO.delete(measureShare.getId());
					logger.info("Removing Sharing " + measureShare.getMeasure().getId() + " with " + user.getId() + 
							" at level " + sLevel.getDescription());
					System.out.println("Removing Sharing " + measureShare.getMeasure().getId() + " with " + user.getId() + 
							" at level " + sLevel.getDescription());
					if(!firstRemove){ //first time, don't add the comma.
						auditLogForModifyRemove.append(", ");
					}
					firstRemove = false;
					auditLogForModifyRemove.append(user.getEmailAddress());
				}
			}
		}

		//US 170. Log share event
		if(recordShareEvent || recordRevokeShareEvent){
			if(recordShareEvent && recordRevokeShareEvent){
				auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
			}else if(recordRevokeShareEvent){
				auditLogAdditionlInfo = new StringBuffer(auditLogForModifyRemove);
			}
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
	public ValidateMeasureResult validateMeasureForExport(String key) throws Exception {
		List<String> message = new ArrayList<String>();
		generateExport(key,message);
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(message.size() == 0);
		result.setValidationMessages(message);
		return result;
	}

	//TODO422:
	private void generateExport(String measureId, List<String> message) throws Exception {
		
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);	
		String exportedXML = ExportSimpleXML.export(measureXML, message);
		if(exportedXML.length() == 0){
			return;
		}
		SimpleEMeasureService.ExportResult exportResult = 
			eMeasureService.exportMeasureIntoSimpleXML(measureId,exportedXML);
		
		//replace all @id attributes of <elementLookUp>/<qdm> with @uuid attribute value
		exportedXML = ExportSimpleXML.setQDMIdAsUUID(exportedXML);
		
		Measure measure = measureDAO.find(measureId);
		MeasureExport export = measureExportDAO.findForMeasure(measureId);
		if(export == null) {
			export = new MeasureExport();
			export.setMeasure(measure);
		}
		export.setSimpleXML(exportedXML);
		export.setCodeListBarr(exportResult.wkbkbarr);
		measure.setExportedDate(new Date());
		measureDAO.save(measure);
		measureExportDAO.save(export);
	}
	
	@Override
	public List<MeasureShareDTO> search(String searchText, int startIndex,int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUser(searchText,  user, startIndex-1, numResults);
	}

	@Override
	public List<MeasureShareDTO> searchWithFilter(String searchText, int startIndex,int numResults,int filter) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUserWithFilter(searchText,  user, startIndex-1, numResults,filter);
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
	@Override
	public void transferMeasureOwnerShipToUser(List<String> list, String toEmail){		
		User userTo = userDAO.findByEmail(toEmail);

		for(int i=0;i<list.size();i++){
			Measure measure = measureDAO.find(list.get(i));
			List<Measure> ms = new ArrayList <Measure>();
			ms.add(measure);
			//Get All Family Measures for each Measure
			List<Measure> allMeasures = measureDAO.getAllMeasuresInSet(ms);
			for(int j =0;j<allMeasures.size();j++){
				String additionalInfo = "Measure Owner transferred from "+allMeasures.get(j).getOwner().getEmailAddress()+" to "+ toEmail;
				allMeasures.get(j).setOwner(userTo);
				measureDAO.saveMeasure(allMeasures.get(j));
				measureAuditLogDAO.recordMeasureEvent(allMeasures.get(j), "Measure Ownership Changed", additionalInfo);
				additionalInfo="";

			}
			List<MeasureShare> measureShareInfo = measureDAO.getMeasureShareForMeasure(list.get(i));
			for(int k =0;k<measureShareInfo.size();k++){
				measureShareInfo.get(k).setOwner(userTo);
				measureShareDAO.save(measureShareInfo.get(k));
			}

		}

	}

	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(String measureId) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		if(measureXML != null){
			MeasureXmlModel exportModal = new MeasureXmlModel();
			exportModal.setMeasureId(measureXML.getMeasure_id());
			exportModal.setMeausreExportId(measureXML.getId());
			exportModal.setXml(measureXML.getMeasureXMLAsString());
			return exportModal;
		}
		return null;
	}

	@Override
	public void saveMeasureXml(MeasureXmlModel measureXmlModel) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureXmlModel.getMeasureId());
		if(measureXML != null){
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		}
		else{
			measureXML = new MeasureXML();
			measureXML.setMeasure_id(measureXmlModel.getMeasureId());
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		}
		measureXMLDAO.save(measureXML);
	}

	public String retrieveStewardOID(String stewardName) {
		String oid = null;
		SearchCriteria criteria = new SearchCriteria("orgName", 
				stewardName.trim(), PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<MeasureSteward> stewards = stewardDAO.find(query);

		if (stewards != null && !stewards.isEmpty()) {
			MeasureSteward stw = stewards.get(0);
			oid = stw.getOrgOid();
		}

		return oid;
	}
	
	public void updatePrivateColumnInMeasure(String measureId, boolean isPrivate){
		measureDAO.updatePrivateColumnInMeasure(measureId, isPrivate);
	}

	
}
