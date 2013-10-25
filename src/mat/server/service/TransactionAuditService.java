package mat.server.service;



/**
 * Interface for Transaction Audit Service.
 * 
 * @author aschmidt
 */
public interface TransactionAuditService {

	/**
	 * Record transaction event.
	 * 
	 * @param primaryId
	 *            the primary id
	 * @param secondaryId
	 *            the secondary id
	 * @param activityType
	 *            the activity type
	 * @param userId
	 *            the user id
	 * @param additionalInfo
	 *            the additional info
	 * @param logLevel
	 *            the log level
	 * @return true, if successful
	 */
	public boolean recordTransactionEvent(String primaryId, String secondaryId,	String activityType, String userId, String additionalInfo, int logLevel);
}
