package mat.client.buttons;

import com.google.gwt.user.client.ui.Composite;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.ButtonToolBar;

public class SaveDeleteMeasureDetailsButtonBarBuilder extends Composite{
	ButtonToolBar buttonBar = new ButtonToolBar();
	Button save;
	Button delete;


	public SaveDeleteMeasureDetailsButtonBarBuilder(String sectionName, String saveID, String deleteID) {
		super();
		delete = new DeleteButton("sectionName");
		delete.setTitle("Delete Measure");
		delete.setText("Delete Measure");
		delete.getElement().setId(deleteID);
		save = new SaveButton(sectionName);
		save.getElement().setId(saveID);
		buttonBar.add(save);
		buttonBar.add(delete);
		buttonBar.getElement().setAttribute("style", "margin-left: 670px;");
	}
	
	public Button getSaveButton() {
		return save;
	}
	
	public Button getDeleteButton() {
		return delete;
	}
	
	public ButtonToolBar getButtonToolBar() {
		return buttonBar;
	}
}
