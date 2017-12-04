package mat.dao;

import mat.model.EmailAuditLog;

/**
 * DAO Interface for MeasureAuditLog.
 */
public interface EmailAuditLogDAO extends IDAO<EmailAuditLog, String> {
	
	/**
	 * Record email event.
	 * 
	 * @param event
	 *            the event
	 * @param additionalInfo
	 *            the additional info
	 * @return true, if successful
	 */
	public boolean recordEmailEvent(EmailAuditLog log);
}
