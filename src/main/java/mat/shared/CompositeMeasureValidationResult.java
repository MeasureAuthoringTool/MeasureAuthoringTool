package mat.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import mat.client.measure.ManageCompositeMeasureDetailModel;

import java.util.List;

public class CompositeMeasureValidationResult implements IsSerializable {

	private ManageCompositeMeasureDetailModel model;
	List<String> messages;
	
	public List<String> getMessages() {
		return messages;
	}
	public void setMessages(List<String> messages) {
		this.messages = messages;
	}
	public ManageCompositeMeasureDetailModel getModel() {
		return model;
	}
	public void setModel(ManageCompositeMeasureDetailModel model) {
		this.model = model;
	}

	
}
