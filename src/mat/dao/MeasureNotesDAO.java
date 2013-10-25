package mat.dao;

import java.util.List;

import mat.model.MeasureNotes;


/**
 * The Interface MeasureNotesDAO.
 */
public interface MeasureNotesDAO extends IDAO<MeasureNotes, String> {
	
	/**
	 * Gets the all measure notes by measure id.
	 * 
	 * @param measure
	 *            the measure
	 * @return the all measure notes by measure id
	 */
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measure);
}
