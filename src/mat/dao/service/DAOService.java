package mat.dao.service;

import mat.dao.AuditLogDAO;
import mat.dao.BonnieLinkDAO;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.CategoryDAO;
import mat.dao.CodeDAO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.CodeListDAO;
import mat.dao.CodeSystemDAO;
import mat.dao.DataTypeDAO;
import mat.dao.EmailAuditLogDAO;
import mat.dao.ListObjectDAO;
import mat.dao.ListObjectLTDAO;
import mat.dao.MatFlagDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureValidationLogDAO;
import mat.dao.ObjectStatusDAO;
import mat.dao.OrganizationDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.RecentCQLActivityLogDAO;
import mat.dao.RecentMSRActivityLogDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.StewardDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UnitDAO;
import mat.dao.UnitTypeDAO;
import mat.dao.UnitTypeMatrixDAO;
import mat.dao.UserAuditLogDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.dao.clause.AttributeDetailsDAO;
import mat.dao.clause.AttributesDAO;
import mat.dao.clause.CQLDAO;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibrarySetDAO;
import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.ModesAttributesDAO;
import mat.dao.clause.ModesDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributeDetailsDAO;
import mat.dao.clause.QDSAttributesDAO;


/**
 * The Class DAOService.
 */
public class DAOService {
	
	private AttributeDetailsDAO attributeDetailsDAO;
	
	private AttributesDAO attributesDAO;
	
	private AuditLogDAO auditLogDAO;
	
	private BonnieLinkDAO bonnieLinkDAO;
	
	private CategoryDAO categoryDAO;
	
	private CodeDAO codeDAO;
	
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	private CodeListDAO codeListDAO;
	
	private CodeSystemDAO codeSystemDAO;
	
	private DataTypeDAO dataTypeDAO;
	
	private ListObjectDAO listObjectDAO;
	
	private ListObjectLTDAO listObjectLTDAO;
	
	private MatFlagDAO matFlagDAO;
	
	private MeasureAuditLogDAO measureAuditLogDAO;
	
	private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;
	
	private MeasureDAO measureDAO;
	
	private MeasureExportDAO measureExportDAO;
	
	private MeasureScoreDAO measureScoreDAO;
	
	private MeasureSetDAO measureSetDAO;
	
	private CQLLibrarySetDAO cqlLibrarySetDAO;
	
	private MeasureValidationLogDAO measureValidationLogDAO;
	
	private MeasureXMLDAO measureXMLDAO;
	
	private ModesAttributesDAO modesAttributesDAO;
	
	private ModesDAO modesDAO;
	
	private ObjectStatusDAO objectStatusDAO;
	
	private OperatorDAO operatorDAO;
	
	private OrganizationDAO organizationDAO;
	
	private QDSAttributeDetailsDAO qDSAttributeDetailsDAO;
	
	private QDSAttributesDAO qDSAttributesDAO;
	
	private QualityDataSetDAO qualityDataSetDAO;
	
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;
	private RecentCQLActivityLogDAO recentCQLActivityLogDAO;
	
	private SecurityRoleDAO securityRoleDAO;

	private StatusDAO statusDAO;
	
	private StewardDAO stewardDAO;
	
	private TransactionAuditLogDAO transactionAuditLogDAO;
	
	private UnitDAO unitDAO;
	
	private UnitTypeDAO unitTypeDAO;
	
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	
	private UserDAO userDAO;
	
	private UserPasswordDAO userPasswordDAO;
	
	private UserSecurityQuestionDAO userSecurityQuestionDAO;
	
	private UserPasswordHistoryDAO userPasswordHistoryDAO;
	
	private UserAuditLogDAO userAuditLogDAO;
	
	private CQLDAO cqlDAO;
	
	private CQLLibraryDAO cqlLibraryDAO;
	
	private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;
	
	private CQLLibraryShareDAO cqlLibraryShareDAO;
	
	private EmailAuditLogDAO emailAuditLogDAO; 
	
	public AttributeDetailsDAO getAttributeDetailsDAO() {
		return attributeDetailsDAO;
	}

	public AttributesDAO getAttributesDAO() {
		return attributesDAO;
	}

	public void setAttributesDAO(AttributesDAO attributesDAO) {
		this.attributesDAO = attributesDAO;
	}

	public AuditLogDAO getAuditLogDAO() {
		return auditLogDAO;
	}

	public BonnieLinkDAO getBonnieLinkDAO() {
		return bonnieLinkDAO;
	}
	
	public void setBonnieLinkDAO(BonnieLinkDAO link) {
		bonnieLinkDAO = link;
	}
	
	public CategoryDAO getCategoryDAO() {
		return categoryDAO;
	}
	
	public CodeDAO getCodeDAO() {
		return codeDAO;
	}
	
	public CodeListAuditLogDAO getCodeListAuditLogDAO() {
		return codeListAuditLogDAO;
	}
	
	public CodeListDAO getCodeListDAO() {
		return codeListDAO;
	}
	
	public CodeSystemDAO getCodeSystemDAO() {
		return codeSystemDAO;
	}
	
	public DataTypeDAO getDataTypeDAO() {
		return dataTypeDAO;
	}
	
	public ListObjectDAO getListObjectDAO() {
		return listObjectDAO;
	}
	
	public ListObjectLTDAO getListObjectLTDAO() {
		return listObjectLTDAO;
	}
	
	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}
	
	public MeasureAuditLogDAO getMeasureAuditLogDAO() {
		return measureAuditLogDAO;
	}
	
	public MeasureDAO getMeasureDAO() {
		return measureDAO;
	}
	
	public MeasureExportDAO getMeasureExportDAO() {
		return measureExportDAO;
	}
	
	public MeasureScoreDAO getMeasureScoreDAO() {
		return measureScoreDAO;
	}
	
	public MeasureSetDAO getMeasureSetDAO() {
		return measureSetDAO;
	}
	
	public MeasureValidationLogDAO getMeasureValidationLogDAO() {
		return measureValidationLogDAO;
	}
	
	public MeasureXMLDAO getMeasureXMLDAO() {
		return measureXMLDAO;
	}
	
	public ModesAttributesDAO getModesAttributesDAO() {
		return modesAttributesDAO;
	}

	public void setModesAttributesDAO(ModesAttributesDAO modesAttributesDAO) {
		this.modesAttributesDAO = modesAttributesDAO;
	}

	public ModesDAO getModesDAO() {
		return modesDAO;
	}
	
	public void setModesDAO(ModesDAO modesDAO) {
		this.modesDAO = modesDAO;
	}

	public ObjectStatusDAO getObjectStatusDAO() {
		return objectStatusDAO;
	}
	
	public OperatorDAO getOperatorDAO() {
		return operatorDAO;
	}
	
	public QDSAttributeDetailsDAO getqDSAttributeDetailsDAO() {
		return qDSAttributeDetailsDAO;
	}
	
	public QDSAttributesDAO getqDSAttributesDAO() {
		return qDSAttributesDAO;
	}
	
	public QualityDataSetDAO getQualityDataSetDAO() {
		return qualityDataSetDAO;
	}

	public RecentMSRActivityLogDAO getRecentMSRActivityLogDAO() {
		return recentMSRActivityLogDAO;
	}
	
	public SecurityRoleDAO getSecurityRoleDAO() {
		return securityRoleDAO;
	}
	
	public StatusDAO getStatusDAO() {
		return statusDAO;
	}
	
	public StewardDAO getStewardDAO() {
		return stewardDAO;
	}
	
	public TransactionAuditLogDAO getTransactionAuditLogDAO() {
		return transactionAuditLogDAO;
	}
	
	public UnitDAO getUnitDAO() {
		return unitDAO;
	}
	
	public UnitTypeDAO getUnitTypeDAO() {
		return unitTypeDAO;
	}
	
	public UnitTypeMatrixDAO getUnitTypeMatrixDAO() {
		return unitTypeMatrixDAO;
	}
	
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	public UserPasswordDAO getUserPasswordDAO() {
		return userPasswordDAO;
	}
	
	public UserSecurityQuestionDAO getUserSecurityQuestionDAO() {
		return userSecurityQuestionDAO;
	}
	
	public void setAttributeDetailsDAO(AttributeDetailsDAO attributeDetailsDAO) {
		this.attributeDetailsDAO = attributeDetailsDAO;
	}
	
	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}
	
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}
	
	public void setCodeDAO(CodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}
	
	public void setCodeListAuditLogDAO(CodeListAuditLogDAO codeListAuditLogDAO) {
		this.codeListAuditLogDAO = codeListAuditLogDAO;
	}
	
	public void setCodeListDAO(CodeListDAO codeListDAO) {
		this.codeListDAO = codeListDAO;
	}
	
	public void setCodeSystemDAO(CodeSystemDAO codeSystemDAO) {
		this.codeSystemDAO = codeSystemDAO;
	}
	
	public void setDataTypeDAO(DataTypeDAO dataTypeDAO) {
		this.dataTypeDAO = dataTypeDAO;
	}
	
	public void setListObjectDAO(ListObjectDAO listObjectDAO) {
		this.listObjectDAO = listObjectDAO;
	}
	
	public void setListObjectLTDAO(ListObjectLTDAO listObjectLTDAO) {
		this.listObjectLTDAO = listObjectLTDAO;
	}
	
	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}
	
	public void setMeasureAuditLogDAO(MeasureAuditLogDAO measureAuditLogDAO) {
		this.measureAuditLogDAO = measureAuditLogDAO;
	}
	
	public void setMeasureDAO(MeasureDAO measureDAO) {
		this.measureDAO = measureDAO;
	}
	
	public void setMeasureExportDAO(MeasureExportDAO measureExportDAO) {
		this.measureExportDAO = measureExportDAO;
	}

	public void setMeasureScoreDAO(MeasureScoreDAO measureScoreDAO) {
		this.measureScoreDAO = measureScoreDAO;
	}

	public void setMeasureSetDAO(MeasureSetDAO measureSetDAO) {
		this.measureSetDAO = measureSetDAO;
	}

	public void setMeasureValidationLogDAO(MeasureValidationLogDAO measureValidationLogDAO) {
		this.measureValidationLogDAO = measureValidationLogDAO;
	}

	public void setMeasureXMLDAO(MeasureXMLDAO measureXMLDAO) {
		this.measureXMLDAO = measureXMLDAO;
	}

	public void setObjectStatusDAO(ObjectStatusDAO objectStatusDAO) {
		this.objectStatusDAO = objectStatusDAO;
	}

	public void setOperatorDAO(OperatorDAO operatorDAO) {
		this.operatorDAO = operatorDAO;
	}

	public void setqDSAttributeDetailsDAO(
			QDSAttributeDetailsDAO qDSAttributeDetailsDAO) {
		this.qDSAttributeDetailsDAO = qDSAttributeDetailsDAO;
	}

	public void setqDSAttributesDAO(QDSAttributesDAO qDSAttributesDAO) {
		this.qDSAttributesDAO = qDSAttributesDAO;
	}

	public void setQualityDataSetDAO(QualityDataSetDAO qualityDataSetDAO) {
		this.qualityDataSetDAO = qualityDataSetDAO;
	}

	public void setRecentMSRActivityLogDAO(RecentMSRActivityLogDAO recentMSRActivityLogDAO) {
		this.recentMSRActivityLogDAO = recentMSRActivityLogDAO;
	}

	public void setSecurityRoleDAO(SecurityRoleDAO securityRoleDAO) {
		this.securityRoleDAO = securityRoleDAO;
	}

	public void setStatusDAO(StatusDAO statusDAO) {
		this.statusDAO = statusDAO;
	}

	public void setStewardDAO(StewardDAO stewardDAO) {
		this.stewardDAO = stewardDAO;
	}
	
	public void setTransactionAuditLogDAO(
			TransactionAuditLogDAO transactionAuditLogDAO) {
		this.transactionAuditLogDAO = transactionAuditLogDAO;
	}
	
	public void setUnitDAO(UnitDAO unitDAO) {
		this.unitDAO = unitDAO;
	}
	
	public void setUnitTypeDAO(UnitTypeDAO unitTypeDAO) {
		this.unitTypeDAO = unitTypeDAO;
	}
	
	public void setUnitTypeMatrixDAO(UnitTypeMatrixDAO unitTypeMatrixDAO) {
		this.unitTypeMatrixDAO = unitTypeMatrixDAO;
	}
	
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void setUserPasswordDAO(UserPasswordDAO userPasswordDAO) {
		this.userPasswordDAO = userPasswordDAO;
	}
	
	public void setUserSecurityQuestionDAO(
			UserSecurityQuestionDAO userSecurityQuestionDAO) {
		this.userSecurityQuestionDAO = userSecurityQuestionDAO;
	}

	public OrganizationDAO getOrganizationDAO() {
		return organizationDAO;
	}

	public void setOrganizationDAO(OrganizationDAO organizationDAO) {
		this.organizationDAO = organizationDAO;
	}

	public UserPasswordHistoryDAO getUserPasswordHistoryDAO() {
		return userPasswordHistoryDAO;
	}

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

	public CQLLibrarySetDAO getCqlLibrarySetDAO() {
		return cqlLibrarySetDAO;
	}

	public void setCqlLibrarySetDAO(CQLLibrarySetDAO cqlLibrarySetDAO) {
		this.cqlLibrarySetDAO = cqlLibrarySetDAO;
	}
	

	public RecentCQLActivityLogDAO getRecentCQLActivityLogDAO() {
		return recentCQLActivityLogDAO;
	}

	public void setRecentCQLActivityLogDAO(RecentCQLActivityLogDAO recentCQLActivityLogDAO) {
		this.recentCQLActivityLogDAO = recentCQLActivityLogDAO;
	}

	public CQLLibraryAuditLogDAO getCqlLibraryAuditLogDAO() {
		return cqlLibraryAuditLogDAO;
	}

	public void setCqlLibraryAuditLogDAO(CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO) {
		this.cqlLibraryAuditLogDAO = cqlLibraryAuditLogDAO;
	}

	public CQLLibraryShareDAO getCqlLibraryShareDAO() {
		return cqlLibraryShareDAO;
	}

	public void setCqlLibraryShareDAO(CQLLibraryShareDAO cqlLibraryShareDAO) {
		this.cqlLibraryShareDAO = cqlLibraryShareDAO;
	}

	public EmailAuditLogDAO getEmailAuditLogDAO() {
		return emailAuditLogDAO;
	}

	public void setEmailAuditLogDAO(EmailAuditLogDAO emailAuditLogDAO) {
		this.emailAuditLogDAO = emailAuditLogDAO;
	}
	
}
