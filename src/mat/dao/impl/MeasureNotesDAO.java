package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.MeasureNotes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;



public class MeasureNotesDAO extends GenericDAO<MeasureNotes, String> implements mat.dao.MeasureNotesDAO{

	private static final Log logger = LogFactory.getLog(MeasureNotesDAO.class);
	
	@Override
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureId) {
		List<MeasureNotes> measureNotesList = new ArrayList<MeasureNotes>();
		logger.info("Getting all the notes for the measure ::::" + measureId);
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MeasureNotes.class);
		criteria.add(Restrictions.eq("measure_id", measureId));
		criteria.addOrder(Order.desc("lastModifiedDate"));
		measureNotesList = criteria.list();
		return measureNotesList;
	}

	@Override
	public void saveMeasureNote(MeasureNotes measureNote) {
		Session session = null;
		Transaction transaction = null;
		try {
			logger.info("Saving measure note, measure note id ::::" + measureNote.getId());			
			session = getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.saveOrUpdate(measureNote);
			
			transaction.commit();
		}
		catch (Exception e) {
			e.printStackTrace();		  
		}
		finally {
	    	rollbackUncommitted(transaction);
	    	closeSession(session);
		}
	}

	@Override
	public void deleteMeasureNote(MeasureNotes measureNote) {
		 
		try {
				logger.info("Deleting measure note, measure note id ::::" + measureNote.getId());
			 	Session session = getSessionFactory().openSession();
			 	session.beginTransaction();  
			 	session.delete(measureNote);
			 	session.getTransaction().commit();  
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 	
	}

}
