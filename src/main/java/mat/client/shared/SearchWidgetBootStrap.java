package mat.client.shared;

import com.google.gwt.user.client.ui.TextBox;
import mat.client.util.MatTextBox;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconPosition;
import org.gwtbootstrap3.client.ui.constants.IconType;
import org.gwtbootstrap3.client.ui.constants.Pull;

public class SearchWidgetBootStrap {
	private Button go;
	private MatTextBox searchBox = new MatTextBox();
	
	public SearchWidgetBootStrap(String buttonText, String searchBoxTitle, String placeholder) {
		go = new Button(buttonText);
		go.setType(ButtonType.PRIMARY);
		go.setIcon(IconType.SEARCH);
		go.setIconPosition(IconPosition.LEFT);
		go.setSize(ButtonSize.SMALL);
		go.setTitle(buttonText);
		go.setPull(Pull.LEFT);
	
		searchBox.setHeight("30px");
		searchBox.setTitle(searchBoxTitle);
		searchBox.getElement().setPropertyString("placeholder", placeholder);
	}
	
	
	public SearchWidgetBootStrap(String buttonText , String searchBoxTitle){
		this(buttonText, searchBoxTitle, "");
	}
	
	public InputGroup getSearchWidget(){
		InputGroup iGroup = new InputGroup();
		InputGroupButton iGroupButton = new InputGroupButton();
		iGroupButton.add(go);
		iGroup.add(searchBox);
		iGroup.add(iGroupButton);
		return iGroup;
	}

	/**
	 * 
	 * @param placeHolderText the text to display in the textbox
	 * @param id the text used for defining an element id
	 * 			appended Search for the search box
	 * 			appended Button for the search button
	 * @return
	 */
	public InputGroup getSearchWidget(String placeHolderText, String id) {		
		getSearchBox().getElement().setPropertyString("placeholder", placeHolderText);
		getSearchBox().getElement().setId(id + "_Search");
		getGo().getElement().setId(id + "_Button");
		return getSearchWidget();
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
	
	public void setSearchBoxWidth(String width){
		this.searchBox.setWidth(width);
	}

}
