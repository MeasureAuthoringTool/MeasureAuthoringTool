package mat.client.measure;

import java.util.List;

import mat.DTO.MeasureNoteDTO;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MeasureNotesModel implements IsSerializable {
	
	private List<MeasureNoteDTO> data;

	public List<MeasureNoteDTO> getData() {
		return data;
	}

	public void setData(List<MeasureNoteDTO> data) {
		this.data = data;
	}
		
}
