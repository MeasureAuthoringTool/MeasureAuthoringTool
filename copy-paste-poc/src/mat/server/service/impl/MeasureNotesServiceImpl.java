package mat.server.service.impl;

import java.util.List;

import mat.dao.MeasureNotesDAO;
import mat.model.MeasureNotes;
import mat.server.service.MeasureNotesService;

/**
 * The Class MeasureNotesServiceImpl.
 */
public class MeasureNotesServiceImpl implements MeasureNotesService{
	
	/** The measure notes dao. */
	private MeasureNotesDAO measureNotesDAO;
		
	/**
	 * Gets the measure notes dao.
	 * 
	 * @return the measure notes dao
	 */
	public MeasureNotesDAO getMeasureNotesDAO() {
		return measureNotesDAO;
	}

	/**
	 * Sets the measure notes dao.
	 * 
	 * @param measureNotesDAO
	 *            the new measure notes dao
	 */
	public void setMeasureNotesDAO(MeasureNotesDAO measureNotesDAO) {
		this.measureNotesDAO = measureNotesDAO;
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasureNotesService#getAllMeasureNotesByMeasureID(java.lang.String)
	 */
	@Override
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureId) {
		
		return measureNotesDAO.getAllMeasureNotesByMeasureID(measureId);
		
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasureNotesService#saveMeasureNote(mat.model.MeasureNotes)
	 */
	@Override
	public void saveMeasureNote(MeasureNotes measureNote) {
		measureNotesDAO.save(measureNote);
	}

	/* (non-Javadoc)
	 * @see mat.server.service.MeasureNotesService#deleteMeasureNote(mat.model.MeasureNotes)
	 */
	@Override
	public void deleteMeasureNote(MeasureNotes measureNote) {
		measureNotesDAO.delete(measureNote);
		
	}
	
}
