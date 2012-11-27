package mat.client.diagramObject;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Anchor;

public class AnchorWithSingleClickHandler extends Anchor {

	HandlerRegistration hr = null;
	
	public AnchorWithSingleClickHandler(){
		super(" ");
	}
	
	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler){
		if(hr != null)
			hr.removeHandler();
		hr = super.addClickHandler(handler);
		return hr;
	}

}
