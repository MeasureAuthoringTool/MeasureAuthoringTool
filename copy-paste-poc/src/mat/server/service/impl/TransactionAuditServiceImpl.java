package mat.server.service.impl;

import mat.dao.TransactionAuditLogDAO;
import mat.model.TransactionAuditLog;
import mat.server.service.TransactionAuditService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Service implementation for Transaction Audit Service.
 * 
 * @author aschmidt
 */
public class TransactionAuditServiceImpl implements TransactionAuditService{

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(TransactionAuditServiceImpl.class);
	
	/** The transaction audit log dao. */
	@Autowired
	private TransactionAuditLogDAO transactionAuditLogDAO;

	/* (non-Javadoc)
	 * @see mat.server.service.TransactionAuditService#recordTransactionEvent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public boolean recordTransactionEvent(String primaryId, String secondaryId,
			String activityType, String userId, String additionalInfo, int logLevel) {
		
		if(logLevel ==0){
			logger.info(additionalInfo);
			return true;
		}else{
			TransactionAuditLog tal = new TransactionAuditLog();
			
			//cannot be null
			String activityTypeStr = activityType == null ? "DEFAULT_TRANSACTION_EVENT" : activityType;
			tal.setActivityType(activityTypeStr);
			
			tal.setAdditionalInfo(additionalInfo);
			tal.setPrimaryId(primaryId);
			tal.setSecondaryId(secondaryId);
			
			//cannot be null
			String userIdStr = userId == null ? "UNKNOWN_USER" : userId;
			tal.setUserId(userIdStr);
			try{
				transactionAuditLogDAO.save(tal);
				return true;
			}catch (Exception e) {
				return false;
			}
		}
	}
	
}
