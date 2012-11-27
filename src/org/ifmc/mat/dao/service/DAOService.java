package org.ifmc.mat.dao.service;

import org.ifmc.mat.dao.AuditLogDAO;
import org.ifmc.mat.dao.CategoryDAO;
import org.ifmc.mat.dao.CodeDAO;
import org.ifmc.mat.dao.CodeListAuditLogDAO;
import org.ifmc.mat.dao.CodeListDAO;
import org.ifmc.mat.dao.CodeSystemDAO;
import org.ifmc.mat.dao.DataTypeDAO;
import org.ifmc.mat.dao.ListObjectDAO;
import org.ifmc.mat.dao.ListObjectLTDAO;
import org.ifmc.mat.dao.MeasureAuditLogDAO;
import org.ifmc.mat.dao.MeasureScoreDAO;
import org.ifmc.mat.dao.MeasureValidationLogDAO;
import org.ifmc.mat.dao.MetadataDAO;
import org.ifmc.mat.dao.ObjectStatusDAO;
import org.ifmc.mat.dao.QualityDataSetDAO;
import org.ifmc.mat.dao.SecurityRoleDAO;
import org.ifmc.mat.dao.StatusDAO;
import org.ifmc.mat.dao.StewardDAO;
import org.ifmc.mat.dao.TransactionAuditLogDAO;
import org.ifmc.mat.dao.UnitDAO;
import org.ifmc.mat.dao.UnitTypeDAO;
import org.ifmc.mat.dao.UnitTypeMatrixDAO;
import org.ifmc.mat.dao.UserDAO;
import org.ifmc.mat.dao.UserPasswordDAO;
import org.ifmc.mat.dao.UserSecurityQuestionDAO;
import org.ifmc.mat.dao.clause.AttributeDetailsDAO;
import org.ifmc.mat.dao.clause.ClauseDAO;
import org.ifmc.mat.dao.clause.ContextDAO;
import org.ifmc.mat.dao.clause.DecisionDAO;
import org.ifmc.mat.dao.clause.MeasureDAO;
import org.ifmc.mat.dao.clause.MeasureExportDAO;
import org.ifmc.mat.dao.clause.MeasureSetDAO;
import org.ifmc.mat.dao.clause.MeasurementTermDAO;
import org.ifmc.mat.dao.clause.OperatorDAO;
import org.ifmc.mat.dao.clause.QDSAttributeDetailsDAO;
import org.ifmc.mat.dao.clause.QDSAttributesDAO;
import org.ifmc.mat.dao.clause.QDSTermDAO;


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
	private DecisionDAO decisionDAO;
	private MeasurementTermDAO measurementTermDAO;
	private QDSTermDAO qDSTermDAO;
	private MeasureDAO measureDAO;
	private ClauseDAO clauseDAO;
	private QDSAttributesDAO qDSAttributesDAO;
	private QDSAttributeDetailsDAO qDSAttributeDetailsDAO;
	private ContextDAO contextDAO;
	private AttributeDetailsDAO attributeDetailsDAO;
	private MeasureScoreDAO measureScoreDAO;
	private UnitDAO unitDAO;
	private UnitTypeDAO unitTypeDAO;
	private UnitTypeMatrixDAO unitTypeMatrixDAO;
	private MeasureSetDAO measureSetDAO;
	private MetadataDAO metadataDAO;
	private MeasureExportDAO measureExportDAO;
	private TransactionAuditLogDAO transactionAuditLogDAO;
	//US 171
	private OperatorDAO operatorDAO;
	
	
	public MetadataDAO getMetadataDAO() {
		return metadataDAO;
	}

	public void setMetadataDAO(MetadataDAO metadataDAO) {
		this.metadataDAO = metadataDAO;
	}

	//US 170 & 383
	private MeasureAuditLogDAO measureAuditLogDAO;
	private CodeListAuditLogDAO codeListAuditLogDAO;
	
	private MeasureValidationLogDAO measureValidationLogDAO;

	public ContextDAO getContextDAO() {
		return contextDAO;
	}

	public void setContextDAO(ContextDAO contextDAO) {
		this.contextDAO = contextDAO;
	}
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
	public ClauseDAO getClauseDAO() {
		return clauseDAO;
	}
	public void setClauseDAO(ClauseDAO clauseDAO) {
		this.clauseDAO = clauseDAO;
	}
	public MeasureDAO getMeasureDAO() {
		return measureDAO;
	}
	public void setMeasureDAO(MeasureDAO measureDAO) {
		this.measureDAO = measureDAO;
	}
	
	public QDSTermDAO getqDSTermDAO() {
		return qDSTermDAO;
	}
	public void setqDSTermDAO(QDSTermDAO qDSTermDAO) {
		this.qDSTermDAO = qDSTermDAO;
	}
	public MeasurementTermDAO getMeasurementTermDAO() {
		return measurementTermDAO;
	}
	public void setMeasurementTermDAO(MeasurementTermDAO measurementTermDAO) {
		this.measurementTermDAO = measurementTermDAO;
	}
	public DecisionDAO getDecisionDAO() {
		return decisionDAO;
	}
	public void setDecisionDAO(DecisionDAO decisionDAO) {
		this.decisionDAO = decisionDAO;
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

}
