package mat.dao;

import java.util.List;

import mat.model.MeasureNotes;


public interface MeasureNotesDAO extends IDAO<MeasureNotes, String> {
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measure);
}
