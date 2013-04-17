package mat.client.clause.clauseworkspace.view;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestionEvent;
import com.google.gwt.user.client.ui.SuggestionHandler;
import com.google.gwt.user.client.ui.VerticalPanel;

@SuppressWarnings("deprecation")
public class QDMDialogBox {

	public static void showQDMDialogBox() {
		final DialogBox dialogBox = new DialogBox(false,true);
		dialogBox.setGlassEnabled(true);
		dialogBox.setAnimationEnabled(true);
		dialogBox.setWidth("20em");
	    dialogBox.setHeight("15em");
	    dialogBox.setText("Select QDM Element");
	    dialogBox.setTitle("Select QDM Element");
		
		// Create a table to layout the content
	    VerticalPanel dialogContents = new VerticalPanel();
	    dialogContents.setWidth("100%");
	    dialogContents.setSpacing(8);
	    dialogBox.setWidget(dialogContents);
	    
	    //Create Search box
	    SuggestBox suggestBox = new SuggestBox(createSuggestOracle());
	    suggestBox.setWidth("18em");
	    suggestBox.setText("Search");
	    
	    dialogContents.add(suggestBox);
	    dialogContents.setCellHorizontalAlignment(
	    		suggestBox, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    	    	    
	    //Create ListBox
	    ListBox listBox = new ListBox();
	    listBox.setWidth("18em");
	    listBox.setVisibleItemCount(10);
	    addQDMNamesToListBox(listBox);
	    
	    //Add listbox to vertical panel and align it in center.
	    dialogContents.add(listBox);
	    dialogContents.setCellHorizontalAlignment(
	    		listBox, HasHorizontalAlignment.ALIGN_CENTER);
	    
	    // Add a Close button at the bottom of the dialog
	    Button closeButton = new Button("Close", new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				dialogBox.hide();				
			}
		});
	  	    
	    dialogContents.add(closeButton);
	    dialogContents.setCellHorizontalAlignment(closeButton, HasHorizontalAlignment.ALIGN_RIGHT);
	    
	    addSuggestHandler(suggestBox,listBox);
	    addListBoxHandler(listBox,suggestBox);
	    
	    dialogBox.center();
	    dialogBox.show();
		
	}

	private static void addListBoxHandler(final ListBox listBox, final SuggestBox suggestBox) {
		listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIndex = listBox.getSelectedIndex();
				String selectedItem = listBox.getItemText(selectedIndex);
				suggestBox.setText(selectedItem);
			}
		});		
	}

	@SuppressWarnings("deprecation")
	private static void addSuggestHandler(final SuggestBox suggestBox, final ListBox listBox) {
		suggestBox.addEventHandler(new SuggestionHandler() {
			
			@Override
			public void onSuggestionSelected(SuggestionEvent event) {
				String selectedQDMName = event.getSelectedSuggestion().getReplacementString();
				for(int i=0;i<listBox.getItemCount();i++){
					if(selectedQDMName.equals(listBox.getItemText(i))){
						listBox.setItemSelected(i, true);
						break;
					}
				}
			}
		});
		
	}

	private static void addQDMNamesToListBox(ListBox listBox) {
		List<String> qdmNamesList = getQDMNames();
		for(String qdmName:qdmNamesList){
			listBox.addItem(qdmName);
		}
	}
	
	private static List<String> getQDMNames(){
		List<String> qdmNamesList = new ArrayList<String>();
		
		qdmNamesList.add("Sample QDM1");
		qdmNamesList.add("Sample QDM2");
		qdmNamesList.add("Sample QDM3");
		qdmNamesList.add("Sample QDM4");
		qdmNamesList.add("Sample QDM5");
		qdmNamesList.add("Sample QDM6");
		qdmNamesList.add("Sample QDM7");
		qdmNamesList.add("Sample QDM8");
		qdmNamesList.add("Sample QDM9");
		qdmNamesList.add("Sample QDM10");
		qdmNamesList.add("Sample QDM12");
		qdmNamesList.add("Sample QDM13");
		qdmNamesList.add("Sample QDM14");
		qdmNamesList.add("Sample QDM15");
		qdmNamesList.add("Sample QDM16");
		
		return qdmNamesList;
	}
	
	private static MultiWordSuggestOracle createSuggestOracle(){
		MultiWordSuggestOracle multiWordSuggestOracle = new MultiWordSuggestOracle();
		List<String> qdmNamesList = getQDMNames();
		multiWordSuggestOracle.addAll(qdmNamesList);
		return multiWordSuggestOracle;
	}

}
