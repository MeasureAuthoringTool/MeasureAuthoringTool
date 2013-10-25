package mat.dao.impl;

import java.util.ArrayList;
import java.util.List;

import mat.dao.search.GenericDAO;
import mat.model.MeasureNotes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;



/**
 * The Class MeasureNotesDAO.
 */
public class MeasureNotesDAO extends GenericDAO<MeasureNotes, String> implements mat.dao.MeasureNotesDAO{

	/** The Constant logger. */
	private static final Log logger = LogFactory.getLog(MeasureNotesDAO.class);
	
	/* (non-Javadoc)
	 * @see mat.dao.MeasureNotesDAO#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
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

}
