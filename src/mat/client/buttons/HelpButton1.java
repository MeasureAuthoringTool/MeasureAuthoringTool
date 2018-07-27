package mat.client.buttons;


	import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import mat.client.HelpModal;

public class HelpButton1 extends Button{

	private HelpModal panel;
	
	public HelpButton1(String uniqueId, String name) {
		super();
		super.setId(uniqueId);
		super.setIcon(IconType.QUESTION_CIRCLE);
		super.setIconSize(IconSize.LARGE);
		super.setTitle(name + " help button");
		super.getElement().getStyle().setBorderWidth(0, Unit.PX);
		panel = new HelpModal();
		addClickHandler();
		
		
	}
	
	private void addClickHandler() {
		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				panel.show();
			}
		});
	}

	public void setHelpInformation(String info) {
		panel.setMessage(info);
	}
	
}
