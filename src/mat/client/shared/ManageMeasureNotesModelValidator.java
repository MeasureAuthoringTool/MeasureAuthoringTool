package mat.client.shared;

import java.util.ArrayList;
import java.util.List;
import mat.DTO.MeasureNoteDTO;

public class ManageMeasureNotesModelValidator {
	public List<String> validation (MeasureNoteDTO model){
		List<String> message = new ArrayList<String>();
		if ((model.getNoteTitle() == null) || model.getNoteTitle().isEmpty()
				|| (model.getNoteDesc() == null) || model.getNoteDesc().isEmpty()) {
			message.add(MatContext.get().getMessageDelegate().getMEASURE_NOTES_REQUIRED_MESSAGE());
		}
		return message;
	}
}
