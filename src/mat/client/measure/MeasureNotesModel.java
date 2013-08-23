package mat.client.measure;

import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

import mat.DTO.MeasureNoteDTO;

public class MeasureNotesModel implements IsSerializable {
	
	private List<MeasureNoteDTO> data;

	public List<MeasureNoteDTO> getData() {
		return data;
	}

	public void setData(List<MeasureNoteDTO> data) {
		this.data = data;
	}
		
}
