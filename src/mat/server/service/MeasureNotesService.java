package mat.server.service;

import java.util.List;

import mat.model.MeasureNotes;

/**
 * The Interface MeasureNotesService.
 */
public interface MeasureNotesService {
	
	/**
	 * Gets the all measure notes by measure id.
	 * 
	 * @param measureId
	 *            the measure id
	 * @return the all measure notes by measure id
	 */
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureId);
	
	/**
	 * Save measure note.
	 * 
	 * @param measureNote
	 *            the measure note
	 */
	public void saveMeasureNote(MeasureNotes measureNote);
	
	/**
	 * Delete measure note.
	 * 
	 * @param measureNote
	 *            the measure note
	 */
	public void deleteMeasureNote(MeasureNotes measureNote); 
}
