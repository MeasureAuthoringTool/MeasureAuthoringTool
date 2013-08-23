package mat.server.service;

import java.util.List;

import mat.model.MeasureNotes;
import mat.model.clause.Measure;

public interface MeasureNotesService {
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(Measure measure);
	public void saveMeasureNote(MeasureNotes measureNote);
	public void deleteMeasureNote(MeasureNotes measureNote); 
}
