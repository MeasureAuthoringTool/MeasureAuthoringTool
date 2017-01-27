package mat.dao.service;

import mat.dao.AuditLogDAO;
import mat.dao.CategoryDAO;
import mat.dao.CodeDAO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.CodeListDAO;
import mat.dao.CodeSystemDAO;
import mat.dao.DataTypeDAO;
import mat.dao.ListObjectDAO;
import mat.dao.ListObjectLTDAO;
import mat.dao.MatFlagDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.MeasureNotesDAO;
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureValidationLogDAO;
import mat.dao.ObjectStatusDAO;
import mat.dao.OrganizationDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.StewardDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UnitDAO;
import mat.dao.UnitTypeDAO;
import mat.dao.UnitTypeMatrixDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.dao.clause.AttributeDetailsDAO;
import mat.dao.clause.AttributesDAO;
import mat.dao.clause.CQLDAO;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.ModesAttributesDAO;
import mat.dao.clause.ModesDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributeDetailsDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.dao.UserAuditLogDAO;


/**
 * The Class DAOService.
 */
public class DAOService {
	
	/** The attribute details dao. */
	private AttributeDetailsDAO attributeDetailsDAO;
	
	/** The attributes dao. */
	private AttributesDAO attributesDAO;
	
	/** The audit log dao. */
	private AuditLogDAO auditLogDAO;
	
	/** The category dao. */
	private CategoryDAO categoryDAO;
	
	/** The code dao. */
	private CodeDAO codeDAO;
	
	/** The code list audit log dao. */
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	/** The code list dao. */
	private CodeListDAO codeListDAO;
	
	/** The code system dao. */
	private CodeSystemDAO codeSystemDAO;
	
	/** The data type dao. */
	private DataTypeDAO dataTypeDAO;
	
	/** The list object dao. */
	private ListObjectDAO listObjectDAO;
	
	/** The list object ltdao. */
	private ListObjectLTDAO listObjectLTDAO;
	
	/** The mat flag dao. */
	private MatFlagDAO matFlagDAO;
	
	//US 170 & 383
	/** The measure audit log dao. */
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	/** The measure dao. */
	private MeasureDAO measureDAO;
	
	/** The measure export dao. */
	private MeasureExportDAO measureExportDAO;
	
	/** The measure notes dao. */
	private MeasureNotesDAO measureNotesDAO;
	
	/** The measure score dao. */
	private MeasureScoreDAO measureScoreDAO;
	
	/** The measure set dao. */
	private MeasureSetDAO measureSetDAO;
	
	/** The measure validation log dao. */
	private MeasureValidationLogDAO measureValidationLogDAO;
	//	private SecurityQuestionsDAO securityQuestionsDAO;
	
	/** The measure xmldao. */
	private MeasureXMLDAO measureXMLDAO;
	
	/** The modes attributes dao. */
	private ModesAttributesDAO modesAttributesDAO;
	
	/** The modes dao. */
	private ModesDAO modesDAO;
	
	/** The object status dao. */
	private ObjectStatusDAO objectStatusDAO;
	
	//US 171
	/** The operator dao. */
	private OperatorDAO operatorDAO;
	
	private OrganizationDAO organizationDAO;
	
	/** The q ds attribute details dao. */
	private QDSAttributeDetailsDAO qDSAttributeDetailsDAO;
	
	/** The q ds attributes dao. */
	private QDSAttributesDAO qDSAttributesDAO;
	
	/** The quality data set dao. */
	private QualityDataSetDAO qualityDataSetDAO;
	
	/** The recent msr activity log dao. */
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;
	
	/** The security role dao. */
	private SecurityRoleDAO securityRoleDAO;
	/** The status dao. */
	private StatusDAO statusDAO;
	
	/** The steward dao. */
	private StewardDAO stewardDAO;
	
	/** The transaction audit log dao. */
	private TransactionAuditLogDAO transactionAuditLogDAO;
	
	/** The unit dao. */
	private UnitDAO unitDAO;
	
	/** The unit type dao. */
	private UnitTypeDAO unitTypeDAO;
	
	/** The unit type matrix dao. */
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	
	/** The user dao. */
	private UserDAO userDAO;
	
	/** The user password dao. */
	private UserPasswordDAO userPasswordDAO;
	
	/** The user security question dao. */
	private UserSecurityQuestionDAO userSecurityQuestionDAO;
	
	private UserPasswordHistoryDAO userPasswordHistoryDAO;
	
	private UserAuditLogDAO userAuditLogDAO;
	
	private CQLDAO cqlDAO;
	
	private CQLLibraryDAO cqlLibraryDAO;
	
	private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;
	
	/**
	 * Gets the attribute details dao.
	 * 
	 * @return the attribute details dao
	 */
	public AttributeDetailsDAO getAttributeDetailsDAO() {
		return attributeDetailsDAO;
	}
	
	/**
	 * @return the attributesDAO
	 */
	public AttributesDAO getAttributesDAO() {
		return attributesDAO;
	}



	/**
	 * @param attributesDAO the attributesDAO to set
	 */
	public void setAttributesDAO(AttributesDAO attributesDAO) {
		this.attributesDAO = attributesDAO;
	}



	/**
	 * Gets the audit log dao.
	 * 
	 * @return the audit log dao
	 */
	public AuditLogDAO getAuditLogDAO() {
		return auditLogDAO;
	}
	
	/**
	 * Gets the category dao.
	 * 
	 * @return the category dao
	 */
	public CategoryDAO getCategoryDAO() {
		return categoryDAO;
	}
	
	/**
	 * Gets the code dao.
	 * 
	 * @return the code dao
	 */
	public CodeDAO getCodeDAO() {
		return codeDAO;
	}
	
	/**
	 * Gets the code list audit log dao.
	 * 
	 * @return the code list audit log dao
	 */
	public CodeListAuditLogDAO getCodeListAuditLogDAO() {
		return codeListAuditLogDAO;
	}
	
	/**
	 * Gets the code list dao.
	 * 
	 * @return the code list dao
	 */
	public CodeListDAO getCodeListDAO() {
		return codeListDAO;
	}
	
	
	/**
	 * Gets the code system dao.
	 * 
	 * @return the code system dao
	 */
	public CodeSystemDAO getCodeSystemDAO() {
		return codeSystemDAO;
	}
	
	/**
	 * Gets the data type dao.
	 * 
	 * @return the data type dao
	 */
	public DataTypeDAO getDataTypeDAO() {
		return dataTypeDAO;
	}
	
	/**
	 * Gets the list object dao.
	 * 
	 * @return the list object dao
	 */
	public ListObjectDAO getListObjectDAO() {
		return listObjectDAO;
	}
	
	/**
	 * Gets the list object ltdao.
	 * 
	 * @return the list object ltdao
	 */
	public ListObjectLTDAO getListObjectLTDAO() {
		return listObjectLTDAO;
	}
	
	/**
	 * Gets the mat flag dao.
	 * 
	 * @return the mat flag dao
	 */
	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}
	
	/**
	 * Gets the measure audit log dao.
	 * 
	 * @return the measure audit log dao
	 */
	public MeasureAuditLogDAO getMeasureAuditLogDAO() {
		return measureAuditLogDAO;
	}
	
	/**
	 * Gets the measure dao.
	 * 
	 * @return the measure dao
	 */
	public MeasureDAO getMeasureDAO() {
		return measureDAO;
	}
	
	/**
	 * Gets the measure export dao.
	 * 
	 * @return the measure export dao
	 */
	public MeasureExportDAO getMeasureExportDAO() {
		return measureExportDAO;
	}
	
	/**
	 * Gets the measure notes dao.
	 * 
	 * @return the measure notes dao
	 */
	public MeasureNotesDAO getMeasureNotesDAO() {
		return measureNotesDAO;
	}
	
	//US 421
	/**
	 * Gets the measure score dao.
	 * 
	 * @return the measure score dao
	 */
	public MeasureScoreDAO getMeasureScoreDAO() {
		return measureScoreDAO;
	}
	
	/**
	 * Gets the measure set dao.
	 * 
	 * @return the measure set dao
	 */
	public MeasureSetDAO getMeasureSetDAO() {
		return measureSetDAO;
	}
	
	/**
	 * Gets the measure validation log dao.
	 * 
	 * @return the measure validation log dao
	 */
	public MeasureValidationLogDAO getMeasureValidationLogDAO() {
		return measureValidationLogDAO;
	}
	
	/**
	 * Gets the measure xmldao.
	 * 
	 * @return the measure xmldao
	 */
	public MeasureXMLDAO getMeasureXMLDAO() {
		return measureXMLDAO;
	}
	
	/**
	 * @return the modesAttributesDAO
	 */
	public ModesAttributesDAO getModesAttributesDAO() {
		return modesAttributesDAO;
	}

	/**
	 * @param modesAttributesDAO the modesAttributesDAO to set
	 */
	public void setModesAttributesDAO(ModesAttributesDAO modesAttributesDAO) {
		this.modesAttributesDAO = modesAttributesDAO;
	}

	/**
	 * @return the modesDAO
	 */
	public ModesDAO getModesDAO() {
		return modesDAO;
	}

	/**
	 * @param modesDAO the modesDAO to set
	 */
	public void setModesDAO(ModesDAO modesDAO) {
		this.modesDAO = modesDAO;
	}

	/**
	 * Gets the object status dao.
	 * 
	 * @return the object status dao
	 */
	public ObjectStatusDAO getObjectStatusDAO() {
		return objectStatusDAO;
	}
	
	//US 171
	/**
	 * Gets the operator dao.
	 * 
	 * @return the operator dao
	 */
	public OperatorDAO getOperatorDAO() {
		return operatorDAO;
	}
	
	/**
	 * Gets the q ds attribute details dao.
	 * 
	 * @return the q ds attribute details dao
	 */
	public QDSAttributeDetailsDAO getqDSAttributeDetailsDAO() {
		return qDSAttributeDetailsDAO;
	}
	
	/**
	 * Gets the q ds attributes dao.
	 * 
	 * @return the q ds attributes dao
	 */
	public QDSAttributesDAO getqDSAttributesDAO() {
		return qDSAttributesDAO;
	}
	
	/**
	 * Gets the quality data set dao.
	 * 
	 * @return the quality data set dao
	 */
	public QualityDataSetDAO getQualityDataSetDAO() {
		return qualityDataSetDAO;
	}
	
	/**
	 * @return the recentMSRActivityLogDAO
	 */
	public RecentMSRActivityLogDAO getRecentMSRActivityLogDAO() {
		return recentMSRActivityLogDAO;
	}
	
	/**
	 * Gets the security role dao.
	 * 
	 * @return the security role dao
	 */
	public SecurityRoleDAO getSecurityRoleDAO() {
		return securityRoleDAO;
	}
	
	/**
	 * Gets the status dao.
	 * 
	 * @return the status dao
	 */
	public StatusDAO getStatusDAO() {
		return statusDAO;
	}
	
	/**
	 * Gets the steward dao.
	 * 
	 * @return the steward dao
	 */
	public StewardDAO getStewardDAO() {
		return stewardDAO;
	}
	
	/**
	 * Gets the transaction audit log dao.
	 * 
	 * @return the transaction audit log dao
	 */
	public TransactionAuditLogDAO getTransactionAuditLogDAO() {
		return transactionAuditLogDAO;
	}
	
	//US 62
	/**
	 * Gets the unit dao.
	 * 
	 * @return the unit dao
	 */
	public UnitDAO getUnitDAO() {
		return unitDAO;
	}
	
	/**
	 * Gets the unit type dao.
	 * 
	 * @return the unit type dao
	 */
	public UnitTypeDAO getUnitTypeDAO() {
		return unitTypeDAO;
	}
	
	/**
	 * Gets the unit type matrix dao.
	 * 
	 * @return the unit type matrix dao
	 */
	public UnitTypeMatrixDAO getUnitTypeMatrixDAO() {
		return unitTypeMatrixDAO;
	}
	
	/**
	 * Gets the user dao.
	 * 
	 * @return the user dao
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	/**
	 * Gets the user password dao.
	 * 
	 * @return the user password dao
	 */
	public UserPasswordDAO getUserPasswordDAO() {
		return userPasswordDAO;
	}
	
	/**
	 * Gets the user security question dao.
	 * 
	 * @return the user security question dao
	 */
	public UserSecurityQuestionDAO getUserSecurityQuestionDAO() {
		return userSecurityQuestionDAO;
	}
	
	/**
	 * Sets the attribute details dao.
	 * 
	 * @param attributeDetailsDAO
	 *            the new attribute details dao
	 */
	public void setAttributeDetailsDAO(AttributeDetailsDAO attributeDetailsDAO) {
		this.attributeDetailsDAO = attributeDetailsDAO;
	}
	
	/**
	 * Sets the audit log dao.
	 * 
	 * @param auditLogDAO
	 *            the new audit log dao
	 */
	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}
	
	/**
	 * Sets the category dao.
	 * 
	 * @param categoryDAO
	 *            the new category dao
	 */
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}
	
	/**
	 * Sets the code dao.
	 * 
	 * @param codeDAO
	 *            the new code dao
	 */
	public void setCodeDAO(CodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}
	
	/**
	 * Sets the code list audit log dao.
	 * 
	 * @param codeListAuditLogDAO
	 *            the new code list audit log dao
	 */
	public void setCodeListAuditLogDAO(CodeListAuditLogDAO codeListAuditLogDAO) {
		this.codeListAuditLogDAO = codeListAuditLogDAO;
	}
	
	/**
	 * Sets the code list dao.
	 * 
	 * @param codeListDAO
	 *            the new code list dao
	 */
	public void setCodeListDAO(CodeListDAO codeListDAO) {
		this.codeListDAO = codeListDAO;
	}
	
	/**
	 * Sets the code system dao.
	 * 
	 * @param codeSystemDAO
	 *            the new code system dao
	 */
	public void setCodeSystemDAO(CodeSystemDAO codeSystemDAO) {
		this.codeSystemDAO = codeSystemDAO;
	}
	
	/**
	 * Sets the data type dao.
	 * 
	 * @param dataTypeDAO
	 *            the new data type dao
	 */
	public void setDataTypeDAO(DataTypeDAO dataTypeDAO) {
		this.dataTypeDAO = dataTypeDAO;
	}
	
	/**
	 * Sets the list object dao.
	 * 
	 * @param listObjectDAO
	 *            the new list object dao
	 */
	public void setListObjectDAO(ListObjectDAO listObjectDAO) {
		this.listObjectDAO = listObjectDAO;
	}
	
	/**
	 * Sets the list object ltdao.
	 * 
	 * @param listObjectLTDAO
	 *            the new list object ltdao
	 */
	public void setListObjectLTDAO(ListObjectLTDAO listObjectLTDAO) {
		this.listObjectLTDAO = listObjectLTDAO;
	}
	
	/**
	 * Sets the mat flag dao.
	 * 
	 * @param matFlagDAO
	 *            the new mat flag dao
	 */
	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}
	
	/**
	 * Sets the measure audit log dao.
	 * 
	 * @param measureAuditLogDAO
	 *            the new measure audit log dao
	 */
	public void setMeasureAuditLogDAO(MeasureAuditLogDAO measureAuditLogDAO) {
		this.measureAuditLogDAO = measureAuditLogDAO;
	}
	
	/**
	 * Sets the measure dao.
	 * 
	 * @param measureDAO
	 *            the new measure dao
	 */
	public void setMeasureDAO(MeasureDAO measureDAO) {
		this.measureDAO = measureDAO;
	}
	
	/**
	 * Sets the measure export dao.
	 * 
	 * @param measureExportDAO
	 *            the new measure export dao
	 */
	public void setMeasureExportDAO(MeasureExportDAO measureExportDAO) {
		this.measureExportDAO = measureExportDAO;
	}
	
	/**
	 * Sets the measure notes dao.
	 * 
	 * @param measureNotesDAO
	 *            the new measure notes dao
	 */
	public void setMeasureNotesDAO(MeasureNotesDAO measureNotesDAO) {
		this.measureNotesDAO = measureNotesDAO;
	}
	
	/**
	 * Sets the measure score dao.
	 * 
	 * @param measureScoreDAO
	 *            the new measure score dao
	 */
	public void setMeasureScoreDAO(MeasureScoreDAO measureScoreDAO) {
		this.measureScoreDAO = measureScoreDAO;
	}
	
	/**
	 * Sets the measure set dao.
	 * 
	 * @param measureSetDAO
	 *            the new measure set dao
	 */
	public void setMeasureSetDAO(MeasureSetDAO measureSetDAO) {
		this.measureSetDAO = measureSetDAO;
	}
	
	/**
	 * Sets the measure validation log dao.
	 * 
	 * @param measureValidationLogDAO
	 *            the new measure validation log dao
	 */
	public void setMeasureValidationLogDAO(MeasureValidationLogDAO measureValidationLogDAO) {
		this.measureValidationLogDAO = measureValidationLogDAO;
	}
	
	/**
	 * Sets the measure xmldao.
	 * 
	 * @param measureXMLDAO
	 *            the new measure xmldao
	 */
	public void setMeasureXMLDAO(MeasureXMLDAO measureXMLDAO) {
		this.measureXMLDAO = measureXMLDAO;
	}
	
	/**
	 * Sets the object status dao.
	 * 
	 * @param objectStatusDAO
	 *            the new object status dao
	 */
	public void setObjectStatusDAO(ObjectStatusDAO objectStatusDAO) {
		this.objectStatusDAO = objectStatusDAO;
	}
	
	/**
	 * Sets the operator dao.
	 * 
	 * @param operatorDAO
	 *            the new operator dao
	 */
	public void setOperatorDAO(OperatorDAO operatorDAO) {
		this.operatorDAO = operatorDAO;
	}
	
	/**
	 * Sets the q ds attribute details dao.
	 * 
	 * @param qDSAttributeDetailsDAO
	 *            the new q ds attribute details dao
	 */
	public void setqDSAttributeDetailsDAO(
			QDSAttributeDetailsDAO qDSAttributeDetailsDAO) {
		this.qDSAttributeDetailsDAO = qDSAttributeDetailsDAO;
	}
	
	/**
	 * Sets the q ds attributes dao.
	 * 
	 * @param qDSAttributesDAO
	 *            the new q ds attributes dao
	 */
	public void setqDSAttributesDAO(QDSAttributesDAO qDSAttributesDAO) {
		this.qDSAttributesDAO = qDSAttributesDAO;
	}
	
	/**
	 * Sets the quality data set dao.
	 * 
	 * @param qualityDataSetDAO
	 *            the new quality data set dao
	 */
	public void setQualityDataSetDAO(QualityDataSetDAO qualityDataSetDAO) {
		this.qualityDataSetDAO = qualityDataSetDAO;
	}
	
	/**
	 * @param recentMSRActivityLogDAO the recentMSRActivityLogDAO to set
	 */
	public void setRecentMSRActivityLogDAO(RecentMSRActivityLogDAO recentMSRActivityLogDAO) {
		this.recentMSRActivityLogDAO = recentMSRActivityLogDAO;
	}
	/**
	 * Sets the security role dao.
	 * 
	 * @param securityRoleDAO
	 *            the new security role dao
	 */
	public void setSecurityRoleDAO(SecurityRoleDAO securityRoleDAO) {
		this.securityRoleDAO = securityRoleDAO;
	}
	
	/**
	 * Sets the status dao.
	 * 
	 * @param statusDAO
	 *            the new status dao
	 */
	public void setStatusDAO(StatusDAO statusDAO) {
		this.statusDAO = statusDAO;
	}
	
	/**
	 * Sets the steward dao.
	 * 
	 * @param stewardDAO
	 *            the new steward dao
	 */
	public void setStewardDAO(StewardDAO stewardDAO) {
		this.stewardDAO = stewardDAO;
	}
	
	/**
	 * Sets the transaction audit log dao.
	 * 
	 * @param transactionAuditLogDAO
	 *            the new transaction audit log dao
	 */
	public void setTransactionAuditLogDAO(
			TransactionAuditLogDAO transactionAuditLogDAO) {
		this.transactionAuditLogDAO = transactionAuditLogDAO;
	}
	
	/**
	 * Sets the unit dao.
	 * 
	 * @param unitDAO
	 *            the new unit dao
	 */
	public void setUnitDAO(UnitDAO unitDAO) {
		this.unitDAO = unitDAO;
	}
	
	/**
	 * Sets the unit type dao.
	 * 
	 * @param unitTypeDAO
	 *            the new unit type dao
	 */
	public void setUnitTypeDAO(UnitTypeDAO unitTypeDAO) {
		this.unitTypeDAO = unitTypeDAO;
	}
	
	/**
	 * Sets the unit type matrix dao.
	 * 
	 * @param unitTypeMatrixDAO
	 *            the new unit type matrix dao
	 */
	public void setUnitTypeMatrixDAO(UnitTypeMatrixDAO unitTypeMatrixDAO) {
		this.unitTypeMatrixDAO = unitTypeMatrixDAO;
	}
	
	/**
	 * Sets the user dao.
	 * 
	 * @param userDAO
	 *            the new user dao
	 */
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	/**
	 * Sets the user password dao.
	 * 
	 * @param userPasswordDAO
	 *            the new user password dao
	 */
	public void setUserPasswordDAO(UserPasswordDAO userPasswordDAO) {
		this.userPasswordDAO = userPasswordDAO;
	}
	
	/**
	 * Sets the user security question dao.
	 * 
	 * @param userSecurityQuestionDAO
	 *            the new user security question dao
	 */
	public void setUserSecurityQuestionDAO(
			UserSecurityQuestionDAO userSecurityQuestionDAO) {
		this.userSecurityQuestionDAO = userSecurityQuestionDAO;
	}

	/**
	 * @return the organizationDAO
	 */
	public OrganizationDAO getOrganizationDAO() {
		return organizationDAO;
	}

	/**
	 * @param organizationDAO the organizationDAO to set
	 */
	public void setOrganizationDAO(OrganizationDAO organizationDAO) {
		this.organizationDAO = organizationDAO;
	}

	/**
	 * @return the userPasswordHistoryDAO
	 */
	public UserPasswordHistoryDAO getUserPasswordHistoryDAO() {
		return userPasswordHistoryDAO;
	}

	/**
	 * @param userPasswordHistoryDAO the userPasswordHistoryDAO to set
	 */
	public void setUserPasswordHistoryDAO(UserPasswordHistoryDAO userPasswordHistoryDAO) {
		this.userPasswordHistoryDAO = userPasswordHistoryDAO;
	}

	public UserAuditLogDAO getUserAuditLogDAO() {
		return userAuditLogDAO;
	}

	public void setUserAuditLogDAO(UserAuditLogDAO userAuditLogDAO) {
		this.userAuditLogDAO = userAuditLogDAO;
	}

	public CQLDAO getCqlDAO() {
		return cqlDAO;
	}

	public void setCqlDAO(CQLDAO cqlDAO) {
		this.cqlDAO = cqlDAO;
	}

	public CQLLibraryDAO getCqlLibraryDAO() {
		return cqlLibraryDAO;
	}

	public void setCqlLibraryDAO(CQLLibraryDAO cqlLibraryDAO) {
		this.cqlLibraryDAO = cqlLibraryDAO;
	}

	public CQLLibraryAssociationDAO getCqlLibraryAssociationDAO() {
		return cqlLibraryAssociationDAO;
	}

	public void setCqlLibraryAssociationDAO(CQLLibraryAssociationDAO cqlLibraryAssociationDAO) {
		this.cqlLibraryAssociationDAO = cqlLibraryAssociationDAO;
	}
	
	/**
	 * @return the securityQuestionsDAO
	 *//*
	public SecurityQuestionsDAO getSecurityQuestionsDAO() {
		return securityQuestionsDAO;
	}
	  *//**
	  * @param securityQuestionsDAO the securityQuestionsDAO to set
	  *//*
	public void setSecurityQuestionsDAO(SecurityQuestionsDAO securityQuestionsDAO) {
		this.securityQuestionsDAO = securityQuestionsDAO;
	}*/
	
}
