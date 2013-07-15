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
import mat.dao.MeasureScoreDAO;
import mat.dao.MeasureValidationLogDAO;
import mat.dao.ObjectStatusDAO;
import mat.dao.QualityDataSetDAO;
import mat.dao.SecurityRoleDAO;
import mat.dao.StatusDAO;
import mat.dao.StewardDAO;
import mat.dao.TransactionAuditLogDAO;
import mat.dao.UnitDAO;
import mat.dao.UnitTypeDAO;
import mat.dao.UnitTypeMatrixDAO;
import mat.dao.UserDAO;
import mat.dao.UserPasswordDAO;
import mat.dao.UserSecurityQuestionDAO;
import mat.dao.clause.AttributeDetailsDAO;
import mat.dao.clause.MeasureDAO;
import mat.dao.clause.MeasureExportDAO;
import mat.dao.clause.MeasureSetDAO;
import mat.dao.clause.MeasureXMLDAO;
import mat.dao.clause.OperatorDAO;
import mat.dao.clause.QDSAttributeDetailsDAO;
import mat.dao.clause.QDSAttributesDAO;
import mat.dao.impl.SecurityQuestionsDAO;


public class DAOService {

	private AuditLogDAO auditLogDAO;
	private SecurityRoleDAO securityRoleDAO;
	private StatusDAO statusDAO;
	private UserDAO userDAO;
	private UserPasswordDAO userPasswordDAO;
	private UserSecurityQuestionDAO userSecurityQuestionDAO;
	private CategoryDAO categoryDAO;
	private StewardDAO stewardDAO;
	private CodeDAO codeDAO;
	private CodeListDAO codeListDAO;
	private CodeSystemDAO codeSystemDAO;
	private DataTypeDAO dataTypeDAO;
	private ListObjectDAO listObjectDAO;
	private ListObjectLTDAO listObjectLTDAO;
	private ObjectStatusDAO objectStatusDAO;
	private QualityDataSetDAO qualityDataSetDAO;
	private MeasureDAO measureDAO;
	private QDSAttributesDAO qDSAttributesDAO;
	private QDSAttributeDetailsDAO qDSAttributeDetailsDAO;
	private AttributeDetailsDAO attributeDetailsDAO;
	private MeasureScoreDAO measureScoreDAO;
	private UnitDAO unitDAO;
	private UnitTypeDAO unitTypeDAO;
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	private MeasureSetDAO measureSetDAO;
	private MeasureExportDAO measureExportDAO;
	private TransactionAuditLogDAO transactionAuditLogDAO;
	//US 171
	private OperatorDAO operatorDAO;
	private MatFlagDAO matFlagDAO;
	private MeasureXMLDAO measureXMLDAO;
	

	//US 170 & 383
	private MeasureAuditLogDAO measureAuditLogDAO;
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	private MeasureValidationLogDAO measureValidationLogDAO;
//	private SecurityQuestionsDAO securityQuestionsDAO;


	public QDSAttributeDetailsDAO getqDSAttributeDetailsDAO() {
		return qDSAttributeDetailsDAO;
	}
	public void setqDSAttributeDetailsDAO(
			QDSAttributeDetailsDAO qDSAttributeDetailsDAO) {
		this.qDSAttributeDetailsDAO = qDSAttributeDetailsDAO;
	}
	public QDSAttributesDAO getqDSAttributesDAO() {
		return qDSAttributesDAO;
	}
	public void setqDSAttributesDAO(QDSAttributesDAO qDSAttributesDAO) {
		this.qDSAttributesDAO = qDSAttributesDAO;
	}
	
	public MeasureDAO getMeasureDAO() {
		return measureDAO;
	}
	public void setMeasureDAO(MeasureDAO measureDAO) {
		this.measureDAO = measureDAO;
	}
	
	
	public CategoryDAO getCategoryDAO() {
		return categoryDAO;
	}
	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}
	public CodeDAO getCodeDAO() {
		return codeDAO;
	}
	public void setCodeDAO(CodeDAO codeDAO) {
		this.codeDAO = codeDAO;
	}
	public CodeListDAO getCodeListDAO() {
		return codeListDAO;
	}
	public void setCodeListDAO(CodeListDAO codeListDAO) {
		this.codeListDAO = codeListDAO;
	}
	public CodeSystemDAO getCodeSystemDAO() {
		return codeSystemDAO;
	}
	public void setCodeSystemDAO(CodeSystemDAO codeSystemDAO) {
		this.codeSystemDAO = codeSystemDAO;
	}
	public DataTypeDAO getDataTypeDAO() {
		return dataTypeDAO;
	}
	public void setDataTypeDAO(DataTypeDAO dataTypeDAO) {
		this.dataTypeDAO = dataTypeDAO;
	}
	public ListObjectDAO getListObjectDAO() {
		return listObjectDAO;
	}
	public void setListObjectDAO(ListObjectDAO listObjectDAO) {
		this.listObjectDAO = listObjectDAO;
	}
	public ListObjectLTDAO getListObjectLTDAO() {
		return listObjectLTDAO;
	}
	public void setListObjectLTDAO(ListObjectLTDAO listObjectLTDAO) {
		this.listObjectLTDAO = listObjectLTDAO;
	}
	public ObjectStatusDAO getObjectStatusDAO() {
		return objectStatusDAO;
	}
	public void setObjectStatusDAO(ObjectStatusDAO objectStatusDAO) {
		this.objectStatusDAO = objectStatusDAO;
	}
	public QualityDataSetDAO getQualityDataSetDAO() {
		return qualityDataSetDAO;
	}
	public void setQualityDataSetDAO(QualityDataSetDAO qualityDataSetDAO) {
		this.qualityDataSetDAO = qualityDataSetDAO;
	}
	public AuditLogDAO getAuditLogDAO() {
		return auditLogDAO;
	}
	public void setAuditLogDAO(AuditLogDAO auditLogDAO) {
		this.auditLogDAO = auditLogDAO;
	}
	public SecurityRoleDAO getSecurityRoleDAO() {
		return securityRoleDAO;
	}
	public void setSecurityRoleDAO(SecurityRoleDAO securityRoleDAO) {
		this.securityRoleDAO = securityRoleDAO;
	}
	public StatusDAO getStatusDAO() {
		return statusDAO;
	}
	public void setStatusDAO(StatusDAO statusDAO) {
		this.statusDAO = statusDAO;
	}
	public UserDAO getUserDAO() {
		return userDAO;
	}
	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	public UserPasswordDAO getUserPasswordDAO() {
		return userPasswordDAO;
	}
	public void setUserPasswordDAO(UserPasswordDAO userPasswordDAO) {
		this.userPasswordDAO = userPasswordDAO;
	}
	public UserSecurityQuestionDAO getUserSecurityQuestionDAO() {
		return userSecurityQuestionDAO;
	}
	public void setUserSecurityQuestionDAO(
			UserSecurityQuestionDAO userSecurityQuestionDAO) {
		this.userSecurityQuestionDAO = userSecurityQuestionDAO;
	}
	public StewardDAO getStewardDAO() {
		return stewardDAO;
	}
	public void setStewardDAO(StewardDAO stewardDAO) {
		this.stewardDAO = stewardDAO;
	}
	public void setAttributeDetailsDAO(AttributeDetailsDAO attributeDetailsDAO) {
		this.attributeDetailsDAO = attributeDetailsDAO;
	}
	public AttributeDetailsDAO getAttributeDetailsDAO() {
		return attributeDetailsDAO;
	}
	
	//US 421
	public MeasureScoreDAO getMeasureScoreDAO() {
		return measureScoreDAO;
	}

	public void setMeasureScoreDAO(MeasureScoreDAO measureScoreDAO) {
		this.measureScoreDAO = measureScoreDAO;
	}
	
	//US 62
	public UnitDAO getUnitDAO() {
		return unitDAO;
	}

	public void setUnitDAO(UnitDAO unitDAO) {
		this.unitDAO = unitDAO;
	}

	public UnitTypeDAO getUnitTypeDAO() {
		return unitTypeDAO;
	}

	public void setUnitTypeDAO(UnitTypeDAO unitTypeDAO) {
		this.unitTypeDAO = unitTypeDAO;
	}

	public UnitTypeMatrixDAO getUnitTypeMatrixDAO() {
		return unitTypeMatrixDAO;
	}

	public void setUnitTypeMatrixDAO(UnitTypeMatrixDAO unitTypeMatrixDAO) {
		this.unitTypeMatrixDAO = unitTypeMatrixDAO;
	}
	
	public MeasureAuditLogDAO getMeasureAuditLogDAO() {
		return measureAuditLogDAO;
	}

	public void setMeasureAuditLogDAO(MeasureAuditLogDAO measureAuditLogDAO) {
		this.measureAuditLogDAO = measureAuditLogDAO;
	}

	public CodeListAuditLogDAO getCodeListAuditLogDAO() {
		return codeListAuditLogDAO;
	}

	public void setCodeListAuditLogDAO(CodeListAuditLogDAO codeListAuditLogDAO) {
		this.codeListAuditLogDAO = codeListAuditLogDAO;
	}

	public void setMeasureValidationLogDAO(MeasureValidationLogDAO measureValidationLogDAO) {
		this.measureValidationLogDAO = measureValidationLogDAO;
	}

	public MeasureValidationLogDAO getMeasureValidationLogDAO() {
		return measureValidationLogDAO;
	}

	public MeasureSetDAO getMeasureSetDAO() {
		return measureSetDAO;
	}

	public void setMeasureSetDAO(MeasureSetDAO measureSetDAO) {
		this.measureSetDAO = measureSetDAO;
	}

	public MeasureExportDAO getMeasureExportDAO() {
		return measureExportDAO;
	}

	public void setMeasureExportDAO(MeasureExportDAO measureExportDAO) {
		this.measureExportDAO = measureExportDAO;
	}

	public TransactionAuditLogDAO getTransactionAuditLogDAO() {
		return transactionAuditLogDAO;
	}

	public void setTransactionAuditLogDAO(
			TransactionAuditLogDAO transactionAuditLogDAO) {
		this.transactionAuditLogDAO = transactionAuditLogDAO;
	}
	//US 171
	public OperatorDAO getOperatorDAO() {
		return operatorDAO;
	}

	public void setOperatorDAO(OperatorDAO operatorDAO) {
		this.operatorDAO = operatorDAO;
	}

	public void setMatFlagDAO(MatFlagDAO matFlagDAO) {
		this.matFlagDAO = matFlagDAO;
	}

	public MatFlagDAO getMatFlagDAO() {
		return matFlagDAO;
	}

	public void setMeasureXMLDAO(MeasureXMLDAO measureXMLDAO) {
		this.measureXMLDAO = measureXMLDAO;
	}

	public MeasureXMLDAO getMeasureXMLDAO() {
		return measureXMLDAO;
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
