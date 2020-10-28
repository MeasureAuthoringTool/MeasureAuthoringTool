package mat.server.service.impl;

import mat.dao.TransactionAuditLogDAO;
import mat.model.TransactionAuditLog;
import mat.server.logging.LogFactory;
import mat.server.service.TransactionAuditService;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Service;

/**
 * Service implementation for Transaction Audit Service.
 *
 * @author aschmidt
 */
@Service
public class TransactionAuditServiceImpl implements TransactionAuditService {

    private static final Log logger = LogFactory.getLog(TransactionAuditServiceImpl.class);

    private TransactionAuditLogDAO transactionAuditLogDAO;

    public TransactionAuditServiceImpl(TransactionAuditLogDAO transactionAuditLogDAO) {
        this.transactionAuditLogDAO = transactionAuditLogDAO;
    }

    @Override
    public boolean recordTransactionEvent(String primaryId, String secondaryId,
                                          String activityType, String userId, String additionalInfo, int logLevel) {

        if (logLevel == 0) {
            logger.info(additionalInfo);
            return true;
        } else {
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
            try {
                transactionAuditLogDAO.save(tal);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }

}
