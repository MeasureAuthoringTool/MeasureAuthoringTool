package mat.shared.error.measure;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DeleteMeasureException extends Exception implements IsSerializable {
	private static final long serialVersionUID = 1L;

	public DeleteMeasureException() {
		
	}
	
	public DeleteMeasureException(String message) {
		super(message);
	}
}
