package org.ifmc.mat.dao.impl;

import java.util.Date;

import org.hibernate.Session;
import org.ifmc.mat.dao.search.GenericDAO;
import org.ifmc.mat.model.MeasureValidationLog;
import org.ifmc.mat.model.clause.Measure;
import org.ifmc.mat.server.LoggedInUserUtil;

/**
 * Validation Log implementation of a save op
 * @author aschmidt
 *
 */
public class MeasureValidationLogDAO extends GenericDAO<MeasureValidationLog, String> implements org.ifmc.mat.dao.MeasureValidationLogDAO{

	/**
	 * perform a save op using the non-derivable fields of a validation event
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
