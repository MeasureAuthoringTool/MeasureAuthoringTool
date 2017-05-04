package mat.server.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mat.client.clause.clauseworkspace.model.MeasureXmlModel;
import mat.client.measure.ManageMeasureShareModel;
import mat.client.measure.service.ValidateMeasureResult;
import mat.dao.DataTypeDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.OrganizationDAO;
import mat.dao.PropertyOperator;
import mat.dao.QualityDataSetDAO;
import mat.dao.StewardDAO;
import mat.dao.UserDAO;
import mat.dao.clause.CQLLibraryDAO;
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
import mat.model.MatValueSet;
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
import mat.model.cql.CQLModel;
import mat.server.CQLUtilityClass;
import mat.server.LoggedInUserUtil;
import mat.server.service.MeasurePackageService;
import mat.server.service.SimpleEMeasureService;
import mat.server.service.SimpleEMeasureService.ExportResult;
import mat.server.util.ExportSimpleXML;
import mat.server.util.MATPropertiesService;
import mat.shared.ValidationUtility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

// TODO: Auto-generated Javadoc
/**
 * The Class MeasurePackageServiceImpl.
 */
public class MeasurePackageServiceImpl implements MeasurePackageService {
	
	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasurePackageServiceImpl.class);
	
	/** The data type dao. */
	@Autowired
	private DataTypeDAO dataTypeDAO;
	
	/** The e measure service. */
	@Autowired
	private SimpleEMeasureService eMeasureService;
	
	/** The measure audit log dao. */
	@Autowired
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	/** The measure dao. */
	@Autowired
	private MeasureDAO measureDAO;
	
	/** The Organization dao. */
	@Autowired
	private OrganizationDAO organizationDAO;
	
	/** The measure export dao. */
	@Autowired
	private MeasureExportDAO measureExportDAO;
	
	/** The measure package dao. */
	@Autowired
	private MeasureDAO measurePackageDAO;
	
	/** The measure set dao. */
	@Autowired
	private MeasureSetDAO measureSetDAO;
	
	/** The measure share dao. */
	@Autowired
	private MeasureShareDAO measureShareDAO;
	
	/** The measure xmldao. */
	@Autowired
	private MeasureXMLDAO measureXMLDAO;
	
	/** The packager dao. */
	@Autowired
	private PackagerDAO packagerDAO;
	
	/** The quality data set dao. */
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	
	/** The share level dao. */
	@Autowired
	private ShareLevelDAO shareLevelDAO;
	
	/** The steward dao. */
	@Autowired
	private StewardDAO stewardDAO;
	
	/** The user dao. */
	@Autowired
	private UserDAO userDAO;
	
	private String currentReleaseVersion;
	
	/** The cql library dao. */
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	
	//	@Override
	//	public void clone(Measure measurePackage, String newCloneName) {
	//		measurePackageDAO.clone(measurePackage, newCloneName);
	//	}
	
	/** The validator. */
	private ValidationUtility validator = new ValidationUtility();
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#count()
	 */
	@Override
	public long count() {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(user);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#count(int)
	 */
	@Override
	public long count(final int filter) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(filter, user);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#count(java.lang.String)
	 */
	@Override
	public long count(final String searchText) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.countMeasureShareInfoForUser(searchText, user);
	}
	
		
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#countUsersForMeasureShare()
	 */
	@Override
	public int countUsersForMeasureShare() {
		return measurePackageDAO.countUsersForMeasureShare();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#deleteExistingPackages(java.lang.String)
	 */
	@Override
	public void deleteExistingPackages(final String measureId) {
		packagerDAO.deleteAllPackages(measureId);
		
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#findDataTypeForSupplimentalCodeList(java.lang.String, java.lang.String)
	 */
	@Override
	public DataType findDataTypeForSupplimentalCodeList(final String dataTypeName , final String categoryId){
		return dataTypeDAO.findDataTypeForSupplimentalCodeList(dataTypeName, categoryId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#findMeasureSet(java.lang.String)
	 */
	@Override
	public MeasureSet findMeasureSet(final String id) {
		return measureSetDAO.findMeasureSet(id);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#findOutMaximumVersionNumber(java.lang.String)
	 */
	@Override
	public String findOutMaximumVersionNumber(final String measureSetId) {
		return measureDAO.findMaxVersion(measureSetId);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#findOutVersionNumber(java.lang.String, java.lang.String)
	 */
	@Override
	public String findOutVersionNumber(final String measureId, final String measureSetId) {
		return measureDAO.findMaxOfMinVersion(measureId, measureSetId);
	}
	
	
	//TODO422:
	/**
	 * Generate export.
	 * 
	 * @param measureId
	 *            the measure id
	 * @param message
	 *            the message
	 * @param matValueSetList
	 *            the mat value set list
	 * @throws Exception
	 *             the exception
	 */
	private void generateExport(final String measureId, final List<String> message ,
			final List<MatValueSet> matValueSetList) throws Exception {
		//TODO
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		Measure measure = measureDAO.find(measureId);
		String exportedXML = "";
				
		if(measure.getReleaseVersion().equalsIgnoreCase(MATPropertiesService.get().getCurrentReleaseVersion())) {
			
			CQLModel cqlModel = CQLUtilityClass.getCQLStringFromXML(measureXML.getMeasureXMLAsString());
//			MATCQLParser matcqlParser = new MATCQLParser();
//			CQLFileObject cqlFileObject = matcqlParser.parseCQL(CQLUtilityClass.getCqlString(cqlModel, "").toString());
			exportedXML = ExportSimpleXML.export(measureXML, message, measureDAO,organizationDAO, cqlLibraryDAO, cqlModel);
			
		} else {
			exportedXML = ExportSimpleXML.export(measureXML, message, measureDAO,organizationDAO);
		}
		if (exportedXML.length() == 0) {
			return;
		}
		SimpleEMeasureService.ExportResult exportResult =
				eMeasureService.exportMeasureIntoSimpleXML(measureId, exportedXML, matValueSetList);
		
		//replace all @id attributes of <elementLookUp>/<qdm> with @uuid attribute value
		exportedXML = ExportSimpleXML.setQDMIdAsUUID(exportedXML);
		
//		Measure measure = measureDAO.find(measureId);
		MeasureExport export = measureExportDAO.findForMeasure(measureId);
		if (export == null) {
			export = new MeasureExport();
			export.setMeasure(measure);
		}
		export.setSimpleXML(exportedXML);
		export.setCodeListBarr(exportResult.wkbkbarr);
		measure.setReleaseVersion(getCurrentReleaseVersion());
		measure.setExportedDate(new Date());
		measureDAO.save(measure);
		measureExportDAO.save(export);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getById(java.lang.String)
	 */
	@Override
	public Measure getById(final String id) {
		return measurePackageDAO.find(id);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getMaxEMeasureId()
	 */
	@Override
	public int getMaxEMeasureId() {
		return measureDAO.getMaxEMeasureId();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getMeasureXmlForMeasure(java.lang.String)
	 */
	@Override
	public MeasureXmlModel getMeasureXmlForMeasure(final String measureId) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureId);
		if (measureXML != null) {
			MeasureXmlModel exportModal = new MeasureXmlModel();
			exportModal.setMeasureId(measureXML.getMeasure_id());
			exportModal.setMeausreExportId(measureXML.getId());
			exportModal.setXml(measureXML.getMeasureXMLAsString());
			return exportModal;
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getUniqueOid()
	 */
	@Override
	public String getUniqueOid() {
		return qualityDataSetDAO.generateUniqueOid();
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getUsersForShare(java.lang.String, int, int)
	 */
	@Override
	public List<MeasureShareDTO> getUsersForShare(final String measureId, final int startIndex, final int pageSize) {
		return measurePackageDAO.getMeasureShareInfoForMeasure(measureId, startIndex - 1, pageSize);
	}
	
	/**
	 * Gets the validator.
	 * 
	 * @return the validator
	 */
	public ValidationUtility getValidator() {
		return validator;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#isMeasureLocked(java.lang.String)
	 */
	@Override
	public boolean isMeasureLocked(final String id) {
		boolean isLocked = measureDAO.isMeasureLocked(id);
		return isLocked;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#retrieveStewardOID(java.lang.String)
	 */
	@Override
	public String retrieveStewardOID(final String stewardName) {
		String oid = null;
		SearchCriteria criteria = new SearchCriteria("orgName",
				stewardName.trim(), PropertyOperator.EQ, null);
		CriteriaQuery query = new CriteriaQuery(criteria);
		List<MeasureSteward> stewards = stewardDAO.find(query);
		
		if ((stewards != null) && !stewards.isEmpty()) {
			MeasureSteward stw = stewards.get(0);
			oid = stw.getOrgOid();
		}
		
		return oid;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#save(mat.model.clause.Measure)
	 */
	@Override
	public void save(final Measure measurePackage) {
		if (measurePackage.getOwner() == null) {
			if (LoggedInUserUtil.getLoggedInUser() != null) {
				User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
				measurePackage.setOwner(currentUser);
			}
		}
		measurePackageDAO.save(measurePackage);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#save(mat.model.clause.MeasureSet)
	 */
	@Override
	public void save(final MeasureSet measureSet) {
		measureSetDAO.save(measureSet);
	}
	
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#saveAndReturnMaxEMeasureId(mat.model.clause.Measure)
	 */
	@Override
	public int saveAndReturnMaxEMeasureId(final Measure measure) {
		return measureDAO.saveandReturnMaxEMeasureId(measure);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#saveMeasureXml(mat.client.clause.clauseworkspace.model.MeasureXmlModel)
	 */
	@Override
	public void saveMeasureXml(final MeasureXmlModel measureXmlModel) {
		MeasureXML measureXML = measureXMLDAO.findForMeasure(measureXmlModel.getMeasureId());
		if (measureXML != null) {
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		} else {
			measureXML = new MeasureXML();
			measureXML.setMeasure_id(measureXmlModel.getMeasureId());
			measureXML.setMeasureXMLAsByteArray(measureXmlModel.getXml());
		}
		measureXMLDAO.save(measureXML);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#saveSupplimentalQDM(mat.model.QualityDataSet)
	 */
	@Override
	public void saveSupplimentalQDM(final QualityDataSet qds) {
		qualityDataSetDAO.save(qds);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#search(int, int)
	 */
	@Override
	public List<MeasureShareDTO> search(final int startIndex,
			final int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUser(user, startIndex - 1, numResults);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#search(java.lang.String, int, int)
	 */
	@Override
	public List<MeasureShareDTO> search(final String searchText, final int startIndex, final int numResults) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUser(searchText,  user, startIndex - 1, numResults);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#searchForAdminWithFilter(java.lang.String, int, int, int)
	 */
	@Override
	public List<MeasureShareDTO> searchForAdminWithFilter(String searchText,
			int startIndex, int numResults, int filter) {
		//User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUserWithFilter(searchText, startIndex - 1, numResults, filter);
	}
	
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#searchWithFilter(java.lang.String, int, int, int)
	 */
	@Override
	public List<MeasureShareDTO> searchWithFilter(final String searchText, final int startIndex,
			final int numResults, final int filter) {
		User user = userDAO.find(LoggedInUserUtil.getLoggedInUser());
		return measurePackageDAO.getMeasureShareInfoForUserWithFilter(searchText,  user, startIndex - 1, numResults, filter);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getComponentMeasuresInfo(java.util.List)
	 */
	@Override
	public List<Measure> getComponentMeasuresInfo(List<String> measureIds){
		return measurePackageDAO.getComponentMeasureInfoForMeasures(measureIds);
	}
	/**
	 * Sets the validator.
	 * 
	 * @param validator
	 *            the new validator
	 */
	public void setValidator(ValidationUtility validator) {
		this.validator = validator;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#transferMeasureOwnerShipToUser(java.util.List, java.lang.String)
	 */
	@Override
	public void transferMeasureOwnerShipToUser(final List<String> list, final String toEmail) {
		User userTo = userDAO.findByEmail(toEmail);
		
		for (int i = 0; i < list.size(); i++) {
			Measure measure = measureDAO.find(list.get(i));
			List<Measure> ms = new ArrayList <Measure>();
			ms.add(measure);
			//Get All Family Measures for each Measure
			List<Measure> allMeasures = measureDAO.getAllMeasuresInSet(ms);
			for (int j = 0; j < allMeasures.size(); j++) {
				String additionalInfo = "Measure Owner transferred from "
						+ allMeasures.get(j).getOwner().getEmailAddress() + " to " + toEmail;
				allMeasures.get(j).setOwner(userTo);
				measureDAO.saveMeasure(allMeasures.get(j));
				measureAuditLogDAO.recordMeasureEvent(allMeasures.get(j), "Measure Ownership Changed", additionalInfo);
				additionalInfo = "";
				
			}
			List<MeasureShare> measureShareInfo = measureDAO.getMeasureShareForMeasure(list.get(i));
			for (int k = 0; k < measureShareInfo.size(); k++) {
				measureShareInfo.get(k).setOwner(userTo);
				measureShareDAO.save(measureShareInfo.get(k));
			}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#updateLockedOutDate(mat.model.clause.Measure)
	 */
	@Override
	public void updateLockedOutDate(final Measure m) {
		measurePackageDAO.resetLockDate(m);
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#updatePrivateColumnInMeasure(java.lang.String, boolean)
	 */
	@Override
	public void updatePrivateColumnInMeasure(final String measureId, final boolean isPrivate){
		measureDAO.updatePrivateColumnInMeasure(measureId, isPrivate);
		measureAuditLogDAO.recordMeasureEvent(getById(measureId), isPrivate
				? "Measure Private " : "Measure Public", "");
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#updateUsersShare(mat.client.measure.ManageMeasureShareModel)
	 */
	@Override
	public void updateUsersShare(final ManageMeasureShareModel model) {
		StringBuffer auditLogAdditionlInfo = new StringBuffer("Measure shared with ");
		StringBuffer auditLogForModifyRemove = new StringBuffer("Measure shared status revoked with ");
		MeasureShare measureShare = null;
		boolean first = true;
		boolean firstRemove = true;
		boolean recordShareEvent = false;
		boolean recordRevokeShareEvent = false;
		for (int i = 0; i < model.getNumberOfRows(); i++) {
			MeasureShareDTO dto = model.get(i);
			if ((dto.getShareLevel() != null) && !"".equals(dto.getShareLevel())) {
				User user = userDAO.find(dto.getUserId());
				ShareLevel sLevel = shareLevelDAO.find(dto.getShareLevel());
				measureShare = null;
				for (MeasureShare ms : user.getMeasureShares()) {
					if (ms.getMeasure().getId().equals(model.getMeasureId())) {
						measureShare = ms;
						break;
					}
				}
				
				if ((measureShare == null) && ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordShareEvent = true;
					measureShare = new MeasureShare();
					measureShare.setMeasure(measurePackageDAO.find(model.getMeasureId()));
					measureShare.setShareUser(user);
					User currentUser = userDAO.find(LoggedInUserUtil.getLoggedInUser());
					measureShare.setOwner(currentUser);
					user.getMeasureShares().add(measureShare);
					currentUser.getOwnedMeasureShares().add(measureShare);
					logger.info("Sharing " + measureShare.getMeasure().getId() + " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					if (!first) { //first time, don't add the comma.
						auditLogAdditionlInfo.append(", ");
					}
					first = false;
					auditLogAdditionlInfo.append(user.getEmailAddress());
					
					measureShare.setShareLevel(sLevel);
					measureShareDAO.save(measureShare);
				} else if (!ShareLevel.MODIFY_ID.equals(dto.getShareLevel())) {
					recordRevokeShareEvent = true;
					measureShareDAO.delete(measureShare.getId());
					logger.info("Removing Sharing " + measureShare.getMeasure().getId()
							+ " with " + user.getId()
							+ " at level " + sLevel.getDescription());
					System.out.println("Removing Sharing " + measureShare.getMeasure().getId()
							+ " with " + user.getId() + " at level " + sLevel.getDescription());
					if (!firstRemove) { //first time, don't add the comma.
						auditLogForModifyRemove.append(", ");
					}
					firstRemove = false;
					auditLogForModifyRemove.append(user.getEmailAddress());
				}
			}
		}
		
		//US 170. Log share event
		if (recordShareEvent || recordRevokeShareEvent) {
			if (recordShareEvent && recordRevokeShareEvent) {
				auditLogAdditionlInfo.append("\n").append(auditLogForModifyRemove);
			} else if (recordRevokeShareEvent) {
				auditLogAdditionlInfo = new StringBuffer(auditLogForModifyRemove);
			}
			measureAuditLogDAO.recordMeasureEvent(measureShare.getMeasure(),
					"Measure Shared", auditLogAdditionlInfo.toString());
		}
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#validateMeasureForExport(java.lang.String, java.util.ArrayList)
	 */
	@Override
	public ValidateMeasureResult validateMeasureForExport(final String key,
			final List<MatValueSet> matValueSetsList) throws Exception {
		List<String> message = new ArrayList<String>();
		generateExport(key, message, matValueSetsList);
		ValidateMeasureResult result = new ValidateMeasureResult();
		result.setValid(message.size() == 0);
		result.setValidationMessages(message);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getHumanReadableForNode(java.lang.String, java.lang.String)
	 */
	@Override
	public String getHumanReadableForNode(final String measureId, final String populationSubXML){
		String humanReadableHTML = "";
		try {
			ExportResult exportResult = eMeasureService.getHumanReadableForNode(measureId, populationSubXML);
			humanReadableHTML = exportResult.export;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return humanReadableHTML;
	}
	
	/* (non-Javadoc)
	 * @see mat.server.service.MeasurePackageService#getMeasure(java.lang.String)
	 */
	@Override
	public boolean getMeasure(String measureId) {
		return measurePackageDAO.getMeasure(measureId);
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
}
