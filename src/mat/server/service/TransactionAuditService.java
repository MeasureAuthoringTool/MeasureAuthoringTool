package mat.server.service;



/**
 * Interface for Transaction Audit Service
 * @author aschmidt
 *
 */
public interface TransactionAuditService {

	public boolean recordTransactionEvent(String primaryId, String secondaryId,	String activityType, String userId, String additionalInfo, int logLevel);
}
