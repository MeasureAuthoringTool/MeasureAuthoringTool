package mat.dao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mat.dao.AuditLogDAO;
import mat.dao.CQLLibraryAuditLogDAO;
import mat.dao.CategoryDAO;
import mat.dao.CodeDAO;
import mat.dao.CodeListAuditLogDAO;
import mat.dao.CodeListDAO;
import mat.dao.CodeSystemDAO;
import mat.dao.DataTypeDAO;
import mat.dao.EmailAuditLogDAO;
import mat.dao.FeatureFlagDAO;
import mat.dao.ListObjectDAO;
import mat.dao.MatFlagDAO;
import mat.dao.MeasureAuditLogDAO;
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureValidationLogDAO;
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
import mat.dao.UserBonnieAccessInfoDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordDAO;
import mat.dao.UserPasswordHistoryDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.dao.clause.CQLLibraryAssociationDAO;
import mat.dao.clause.CQLLibraryDAO;
import mat.dao.clause.CQLLibraryShareDAO;
import mat.dao.clause.ComponentMeasuresDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureDetailsReferenceDAO;
import mat.dao.clause.MeasureDeveloperDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributesDAO;

@Service
public class DAOService {
	
	@Autowired
	private AuditLogDAO auditLogDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private CodeDAO codeDAO;
	@Autowired
	private CodeListAuditLogDAO codeListAuditLogDAO;
	@Autowired
	private CodeListDAO codeListDAO;
	@Autowired
	private CodeSystemDAO codeSystemDAO;
	@Autowired
	private DataTypeDAO dataTypeDAO;
	@Autowired
	private FeatureFlagDAO featureFlagDAO;
	@Autowired
	private ListObjectDAO listObjectDAO;
	@Autowired
	private MatFlagDAO matFlagDAO;
	@Autowired
	private MeasureAuditLogDAO measureAuditLogDAO;
	@Autowired
	private CQLLibraryAuditLogDAO cqlLibraryAuditLogDAO;
	@Autowired
	private MeasureDAO measureDAO;
	@Autowired
	private MeasureExportDAO measureExportDAO;
	@Autowired
	private MeasureScoreDAO measureScoreDAO;
	@Autowired
	private MeasureSetDAO measureSetDAO;
	@Autowired
	private MeasureValidationLogDAO measureValidationLogDAO;
	@Autowired
	private MeasureXMLDAO measureXMLDAO;
	@Autowired
	private OperatorDAO operatorDAO;
	@Autowired
	private OrganizationDAO organizationDAO;
	@Autowired
	private QDSAttributesDAO qDSAttributesDAO;
	@Autowired
	private QualityDataSetDAO qualityDataSetDAO;
	@Autowired
	private RecentMSRActivityLogDAO recentMSRActivityLogDAO;
	@Autowired
	private RecentCQLActivityLogDAO recentCQLActivityLogDAO;
	@Autowired
	private SecurityRoleDAO securityRoleDAO;
	@Autowired
	private StatusDAO statusDAO;
	@Autowired
	private StewardDAO stewardDAO;
	@Autowired
	private TransactionAuditLogDAO transactionAuditLogDAO;
	@Autowired
	private UnitDAO unitDAO;
	@Autowired
	private UnitTypeDAO unitTypeDAO;
	@Autowired
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private UserBonnieAccessInfoDAO userBonnieAccessInfoDAO;
	@Autowired
	private UserPasswordDAO userPasswordDAO;
	@Autowired
	private UserSecurityQuestionDAO userSecurityQuestionDAO;
	@Autowired
	private UserPasswordHistoryDAO userPasswordHistoryDAO;
	@Autowired
	private UserAuditLogDAO userAuditLogDAO;
	@Autowired
	private CQLLibraryDAO cqlLibraryDAO;
	@Autowired
	private CQLLibraryAssociationDAO cqlLibraryAssociationDAO;
	@Autowired
	private CQLLibraryShareDAO cqlLibraryShareDAO;
	@Autowired
	private EmailAuditLogDAO emailAuditLogDAO; 
	@Autowired
	private ComponentMeasuresDAO componentMeasureDAO;
	@Autowired
	private MeasureDetailsReferenceDAO measureDetailsReferenceDAO;
	@Autowired
	private MeasureDeveloperDAO measureDeveloperDAO;

	public AuditLogDAO getAuditLogDAO() {
		return auditLogDAO;
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
	
	public FeatureFlagDAO getFeatureFlagsDAO() {
		return featureFlagDAO;
	}
	
	public ListObjectDAO getListObjectDAO() {
		return listObjectDAO;
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
	
	public MeasureDetailsReferenceDAO getMeasureDetailsReferenceDAO() {
		return measureDetailsReferenceDAO;
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
	
	public OperatorDAO getOperatorDAO() {
		return operatorDAO;
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
	
	public UserBonnieAccessInfoDAO getUserBonnieAccessInfoDAO() {
		return userBonnieAccessInfoDAO;
	}
	
	public UserSecurityQuestionDAO getUserSecurityQuestionDAO() {
		return userSecurityQuestionDAO;
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
	
	public void setFeatureFlagsDAO(FeatureFlagDAO featureFlagsDAO) {
		this.featureFlagDAO = featureFlagsDAO;
	}
	
	public void setListObjectDAO(ListObjectDAO listObjectDAO) {
		this.listObjectDAO = listObjectDAO;
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
	
	public void setMeasureDetailsReferenceDAO(MeasureDetailsReferenceDAO measureDetailsReferenceDAO) {
		this.measureDetailsReferenceDAO = measureDetailsReferenceDAO;
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

	public void setOperatorDAO(OperatorDAO operatorDAO) {
		this.operatorDAO = operatorDAO;
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
	
	public void setUserBonnieAccessInfoDAO(UserBonnieAccessInfoDAO userBonnieAccessInfoDAO) {
		this.userBonnieAccessInfoDAO = userBonnieAccessInfoDAO;
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

	public ComponentMeasuresDAO getComponentMeasureDAO() {
		return componentMeasureDAO;
	}

	public void setComponentMeasureDAO(ComponentMeasuresDAO componentMeasureDAO) {
		this.componentMeasureDAO = componentMeasureDAO;
	}

	public MeasureDeveloperDAO getMeasureDeveloperDAO() {
		return measureDeveloperDAO;
	}

	public void setMeasureDeveloperDAO(MeasureDeveloperDAO measureDeveloperDAO) {
		this.measureDeveloperDAO = measureDeveloperDAO;
	}
	
}
