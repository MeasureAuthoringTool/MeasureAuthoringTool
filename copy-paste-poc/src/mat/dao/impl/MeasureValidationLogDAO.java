package mat.dao.impl;

import java.util.Date;

import mat.dao.search.GenericDAO;
import mat.model.MeasureValidationLog;
import mat.model.clause.Measure;
import mat.server.LoggedInUserUtil;

import org.hibernate.Session;

/**
 * Validation Log implementation of a save op.
 * 
 * @author aschmidt
 */
public class MeasureValidationLogDAO extends GenericDAO<MeasureValidationLog, String> implements mat.dao.MeasureValidationLogDAO{

	/**
	 * perform a save op using the non-derivable fields of a validation event.
	 * 
	 * @param measure
	 *            the measure
	 * @param event
	 *            the event
	 * @param interimBarr
	 *            the interim barr
	 * @return true, if successful
	 */
	@Override
	public boolean recordMeasureValidationEvent(Measure measure, String event, byte[] interimBarr) {
		Session session = null;
		boolean result = false;
		try {
			MeasureValidationLog measureValidationLog = new MeasureValidationLog();
			measureValidationLog.setActivityType(event);
			measureValidationLog.setTime(new Date());
			measureValidationLog.setMeasure(measure);
			measureValidationLog.setUserId(LoggedInUserUtil.getLoggedInUserEmailAddress());
			measureValidationLog.setInterimBarr(interimBarr);
			session = getSessionFactory().getCurrentSession();
			session.saveOrUpdate(measureValidationLog);			
			result = true;
		}
		catch (Exception e) { //TODO: handle application exception
			e.printStackTrace();
		}
    	return result;
	}

}
