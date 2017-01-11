package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

import com.google.gwt.user.client.ui.TextBox;

import mat.client.util.MatTextBox;

public class SearchWidgetBootStrap {
	private Button go;
	private MatTextBox searchBox ;
	
	public SearchWidgetBootStrap(String buttonText , String placeHolderText){
		go = new Button(buttonText);
		go.setType(ButtonType.PRIMARY);
		go.setIcon(IconType.SEARCH);
		go.setIconPosition(IconPosition.LEFT);
		go.setSize(ButtonSize.SMALL);
		go.setTitle(buttonText);
		go.setPull(Pull.LEFT);
		searchBox = new MatTextBox();
		/*searchBox.removeStyleName("gwt-TextBox");
		searchBox.setStyleName("Text-Box");*/
		searchBox.setWidth("200px");
		searchBox.setHeight("30px");
		//searchBox.setPlaceholder(placeHolderText);
		searchBox.setTitle(placeHolderText);
	}
	
	public InputGroup getSearchWidget(){
		InputGroup iGroup = new InputGroup();
		InputGroupButton iGroupButton = new InputGroupButton();
		iGroupButton.add(go);
		iGroup.add(searchBox);
		iGroup.add(iGroupButton);
		iGroup.setWidth("300px");
		/*iGroup.getElement().getStyle().setMarginLeft(10, Unit.PX);
		iGroup.getElement().getStyle().setMarginRight(5, Unit.PX);
		iGroup.getElement().getStyle().setMarginBottom(7, Unit.PX);*/
		return iGroup;
	}

	public Button getGo() {
		return go;
	}

	public void setGo(Button go) {
		this.go = go;
	}

	public TextBox getSearchBox() {
		return searchBox;
	}

	public void setSearchBox(MatTextBox searchBox) {
		this.searchBox = searchBox;
	}

}
