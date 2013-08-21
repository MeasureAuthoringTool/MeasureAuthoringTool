package mat.server.service;

import java.util.List;

import mat.model.MeasureNotes;

public interface MeasureNotesService {
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureID);
	public void saveMeasureNote(MeasureNotes measureNote);
	public void deleteMeasureNote(MeasureNotes measureNote); 
}
