package mat.server.service.impl;

import java.util.List;

import mat.dao.MeasureNotesDAO;
import mat.model.MeasureNotes;
import mat.server.service.MeasureNotesService;

public class MeasureNotesServiceImpl implements MeasureNotesService{
	
	private MeasureNotesDAO measureNotesDAO;
		
	public MeasureNotesDAO getMeasureNotesDAO() {
		return measureNotesDAO;
	}

	public void setMeasureNotesDAO(MeasureNotesDAO measureNotesDAO) {
		this.measureNotesDAO = measureNotesDAO;
	}

	@Override
	public List<MeasureNotes> getAllMeasureNotesByMeasureID(String measureId) {
		
		return measureNotesDAO.getAllMeasureNotesByMeasureID(measureId);
		
	}

	@Override
	public void saveMeasureNote(MeasureNotes measureNote) {
		measureNotesDAO.saveMeasureNote(measureNote);
	}

	@Override
	public void deleteMeasureNote(MeasureNotes measureNote) {
		measureNotesDAO.deleteMeasureNote(measureNote);
		
	}
	
}
