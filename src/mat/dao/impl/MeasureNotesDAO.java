package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import mat.dao.search.GenericDAO;
import mat.model.MeasureNotes;


public class MeasureNotesDAO extends GenericDAO<MeasureNotes, String> implements mat.dao.MeasureNotesDAO{

	private static final Log logger = LogFactory.getLog(MeasureNotesDAO.class);
	
	@Override
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureID) {
		List<MeasureNotes> measureNotesList = new ArrayList<MeasureNotes>();
		logger.info("Getting all the notes for the measure ::::" + measureID);
		Session session = getSessionFactory().getCurrentSession();
		Criteria criteria = session.createCriteria(MeasureNotes.class);
		criteria.add(Restrictions.eq("measure", measureID));
		measureNotesList = criteria.list();
		return measureNotesList;
	}

	@Override
	public void saveMeasureNote(MeasureNotes measureNote) {
		Session session = null;
		Transaction transaction = null;
		try {
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
			 	Session session = getSessionFactory().openSession();
			 	session.beginTransaction();  
			 	session.delete(measureNote);
			 	session.getTransaction().commit();  
		    } catch (Exception e) {
		        e.printStackTrace();
		    } 	
	}

}
