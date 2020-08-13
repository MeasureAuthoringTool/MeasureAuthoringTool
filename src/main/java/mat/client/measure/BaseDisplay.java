package mat.client.measure;

import com.google.gwt.user.client.ui.Widget;
import mat.client.shared.MessageAlert;

public interface BaseDisplay {

	public Widget asWidget();

	public MessageAlert getErrorMessageDisplay();
}
