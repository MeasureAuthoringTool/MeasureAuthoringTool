package mat.dao.impl;

import java.util.Date;

import mat.model.AuditLog;
import mat.model.CodeListAuditLog;
import mat.model.CodeSystem;
import mat.model.ListObject;
import mat.model.MeasureAuditLog;
import mat.model.QualityDataSet;
import mat.model.clause.Measure;
import mat.model.clause.MeasureExport;
import mat.server.LoggedInUserUtil;
import mat.shared.ConstantMessages;

import org.hibernate.Session;
import org.hibernate.event.EventSource;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * This class has been re-factored completely to support US 170 and 383.
 * 
 * @see AuditEventEvent
 */
@Transactional
public class AuditEventListener implements  PreDeleteEventListener, PreInsertEventListener, PreUpdateEventListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 224312334238542737L;
	
	/* (non-Javadoc)
	 * @see org.hibernate.event.PreDeleteEventListener#onPreDelete(org.hibernate.event.PreDeleteEvent)
	 */
	@Override
	public boolean onPreDelete(PreDeleteEvent arg0) {
		if(shouldAudit(arg0.getEntity(), ConstantMessages.DELETE)) {
			Object logObj = createLogObject(arg0.getEntity(), ConstantMessages.DELETE);
			saveOrUpdate(arg0.getSession(), logObj);
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see org.hibernate.event.PreUpdateEventListener#onPreUpdate(org.hibernate.event.PreUpdateEvent)
	 */
	@Override
	public boolean onPreUpdate(PreUpdateEvent arg0) {
		if(shouldAudit(arg0.getEntity(), ConstantMessages.UPDATE)) {
			EventSource session = arg0.getSession();
			Object logObj = createLogObject(arg0.getEntity(), ConstantMessages.UPDATE);
			saveOrUpdate(arg0.getSession(), logObj);
		}
		return false;
	}
 
	/* (non-Javadoc)
	 * @see org.hibernate.event.PreInsertEventListener#onPreInsert(org.hibernate.event.PreInsertEvent)
	 */
	@Override
	public boolean onPreInsert(PreInsertEvent arg0) {
		if(shouldAudit(arg0.getEntity(), ConstantMessages.INSERT)) {
			Object logObj = createLogObject(arg0.getEntity(), ConstantMessages.INSERT);
			saveOrUpdate(arg0.getSession(), logObj);
		}
		return false;
	}

	/**
	 * Should audit.
	 * 
	 * @param obj
	 *            the obj
	 * @param event
	 *            the event
	 * @return true, if successful
	 */
	private boolean shouldAudit(Object obj, String event) {
		if(event.equals(ConstantMessages.INSERT)){
			return obj instanceof Measure ||  
				obj instanceof QualityDataSet || obj instanceof ListObject || obj instanceof MeasureExport;
		}else{
			//Production Error fix subsequent measurePackaging update information is not logged.
			return obj instanceof MeasureExport  || obj instanceof QualityDataSet;			
		}
	}
	
	/**
	 * Creates the log object.
	 * 
	 * @param obj
	 *            the obj
	 * @param activity
	 *            the activity
	 * @return the object
	 */
	private Object createLogObject(Object obj, String activity) {
		// for some reason, if this object is created in the same session,
		// it is only sometimes inserted into the database 
		Object returnObject = null;
		String user = LoggedInUserUtil.getLoggedInUser();
		String emailAddress = LoggedInUserUtil.getLoggedInUserEmailAddress();

		//JUNIT tests doesn't use any user
		if(user == null){
			user = ConstantMessages.USER_NOT_FOUND;
		}
		if(emailAddress == null){
			emailAddress = ConstantMessages.EMAIL_NOT_FOUND;
		}
		if(obj instanceof Measure || obj instanceof MeasureExport) {
			MeasureAuditLog measureAuditLog = new MeasureAuditLog();
			measureAuditLog.setTime(new Date());				
			measureAuditLog.setUserId(emailAddress);
			if(obj instanceof Measure){
				measureAuditLog.setActivityType("Measure Created");
				measureAuditLog.setMeasure((Measure)obj);
			}else if (obj instanceof MeasureExport){
				measureAuditLog.setActivityType("Measure Package Created");
				measureAuditLog.setMeasure(((MeasureExport)obj).getMeasure());
			}
			returnObject = measureAuditLog;
		}else if(obj instanceof ListObject) {
			CodeSystem codeSystem = ((ListObject) obj).getCodeSystem();
			Boolean draft = ((ListObject) obj).isDraft();
			String descCodeSystem = null;
			if(codeSystem != null){
				descCodeSystem = codeSystem.getDescription();
			}
			CodeListAuditLog codeListAuditLog = new CodeListAuditLog();
			if(descCodeSystem != null && descCodeSystem.equalsIgnoreCase(ConstantMessages.GROUPED_CODE_LIST_CS)){
				if(draft != null && draft.booleanValue())
					codeListAuditLog.setActivityType("Draft Grouped Value Set Created");
			}else{
				if(draft != null && draft.booleanValue())
					codeListAuditLog.setActivityType("Draft Value Set Created");
			}
			codeListAuditLog.setTime(new Date());
			codeListAuditLog.setCodeList(((ListObject)obj));
			codeListAuditLog.setUserId(emailAddress);
			returnObject = codeListAuditLog;
		}else{
			AuditLog auditLog = new AuditLog();
			auditLog.setActivityType(activity);
			auditLog.setUpdateDate(new Date());
			auditLog.setUpdatedBy(user);
			auditLog.setCreateDate(new Date());
			auditLog.setCreatedBy(user);
			if(obj instanceof QualityDataSet) {
				auditLog.setQds((QualityDataSet)obj);
			}
			if(obj instanceof ListObject) {
				auditLog.setListObject((ListObject)obj);
			}
			
			returnObject = auditLog;
		}
		return returnObject;
	}
	
	/**
	 * Save or update.
	 * 
	 * @param eventSource
	 *            the event source
	 * @param obj
	 *            the obj
	 */
	private void saveOrUpdate(EventSource eventSource, Object obj){
		Session session = eventSource.getSessionFactory().openSession();
		try{			
			session.getTransaction().begin();
			session.saveOrUpdate(obj);
			session.flush();
			session.getTransaction().commit();
		}finally{
			if(session.getTransaction().isActive()) {
				session.getTransaction().rollback();
			}
			session.close();			
		}
	}
}
