package mat.client.shared;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;

import com.google.gwt.dom.client.Style.Unit;

public class SearchWidgetBootStrap {
	private Button go;
	private TextBox searchBox ;
	
	public SearchWidgetBootStrap(String buttonText , String placeHolderText){
		go = new Button(buttonText);
		go.setType(ButtonType.PRIMARY);
		go.setIcon(IconType.SEARCH);
		go.setIconPosition(IconPosition.LEFT);
		go.setTitle(buttonText);
		searchBox = new TextBox();
		searchBox.setWidth("200px");
		searchBox.setPlaceholder(placeHolderText);
		searchBox.setTitle(placeHolderText);
	}
	
	public InputGroup getSearchWidget(){
		InputGroup iGroup = new InputGroup();
		InputGroupButton iGroupButton = new InputGroupButton();
		iGroupButton.add(go);
		iGroup.add(searchBox);
		iGroup.add(iGroupButton);
		iGroup.getElement().getStyle().setMarginLeft(10, Unit.PX);
		iGroup.getElement().getStyle().setMarginRight(5, Unit.PX);
		iGroup.getElement().getStyle().setMarginBottom(7, Unit.PX);
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

	public void setSearchBox(TextBox searchBox) {
		this.searchBox = searchBox;
	}

}
