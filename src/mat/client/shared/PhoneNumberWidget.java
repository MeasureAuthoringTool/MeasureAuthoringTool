package mat.client.shared;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;

public class PhoneNumberWidget extends Composite {
	private TextBox phoneNumber = new TextBox();
	
	private static KeyUpHandler keyUpHandler = new KeyUpHandler() {
		
		@Override
		public void onKeyUp(KeyUpEvent event) {
			HasValue<String> source = (HasValue<String>) event.getSource();
			String value = source.getValue();
			String newValue = "";
			String areaCode = "";
			String prefix = "";
			String suffix = "";
			
			for(int i = 0; i < value.length(); i++) {
				char c = value.charAt(i);
				if(Character.isDigit(c)) {
					newValue += c;
				}
			}
			
			for(int i = 0; i < newValue.length(); i++) {
				if(i < 3) {
					areaCode += newValue.charAt(i);
				}
				else if(i < 6) {
					prefix += newValue.charAt(i);
				}
				else {
					suffix += newValue.charAt(i);
				}
			}
			
			value = areaCode;
			if(!"".equals(prefix)) {
				value = value + "-" + prefix;
			}
			if(!"".equals(suffix)) {
				value = value + "-" + suffix;
			}
			
			source.setValue(value);
		}
	};
	
	public PhoneNumberWidget() {
		phoneNumber.getElement().setId("phoneNumber_TextBox");
		phoneNumber.setMaxLength(12);
		Grid phoneLayout = new Grid(2,2);
		phoneLayout.addStyleName("leftAligned");
		phoneLayout.setWidget(0,0, LabelBuilder.buildRequiredLabel(phoneNumber, "Phone Number"));
		phoneLayout.setWidget(1,0, phoneNumber);
		Label phoneFormat = new Label("(555-555-1234)");
		phoneLayout.setWidget(1, 1, phoneFormat);
		
		phoneNumber.setWidth("90px");
		initWidget(phoneLayout);
		
		phoneNumber.addKeyUpHandler(keyUpHandler);
	}
	public HasValue<String> getPhoneNumber() {
		return phoneNumber;
	}
}
