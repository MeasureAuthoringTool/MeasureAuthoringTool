package mat.client.measure;

import java.util.List;
import mat.DTO.MeasureNoteDTO;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The Class MeasureNotesModel.
 */
public class MeasureNotesModel implements IsSerializable{
	
	/** The data. */
	private List<MeasureNoteDTO> data;
	
	/**
	 * Gets the data.
	 * 
	 * @return the data
	 */
	public List<MeasureNoteDTO> getData() {
		return data;
	}
	
	/**
	 * Sets the data.
	 * 
	 * @param data
	 *            the new data
	 */
	public void setData(List<MeasureNoteDTO> data) {
		this.data = data;
	}
}
