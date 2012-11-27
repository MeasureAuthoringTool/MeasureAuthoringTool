package org.ifmc.mat.client.shared;

import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.ListBox;

public class ListBoxMVP extends ListBox implements HasValue<String> {
	
	/*
	 * adding field doTrimDisplay
	 * we may want a ListBoxMVP that does no trimming of its option values
	 */
	private boolean doTrimDisplay = true;
	
	private int maxWidth =65;
	
	public int getMaxWidth(){
		return maxWidth;
	}
	public void setMaxWidth(int maxWidth){
		this.maxWidth = maxWidth;
	}
	
	
	
	public ListBoxMVP(boolean doTrimDisplay, int visibleCount) {
		this(doTrimDisplay);
		this.setVisibleItemCount(visibleCount);
	}
	
	
	public ListBoxMVP(boolean doTrimDisplay) {
		this();
		this.doTrimDisplay = doTrimDisplay;
	}
	
	public ListBoxMVP() {
		super();
		addChangeHandler( new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				ValueChangeEvent.fire(ListBoxMVP.this, getValue(getSelectedIndex()));
			}
		});
		
	}
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<String> handler) {
		return super.addHandler(handler, ValueChangeEvent.getType());
	}
	
	public String getValue() {
		int index = getSelectedIndex();
		if(index >= 0) {
			return getValue(index);
		}
		else {
			return null;
		}
	}
	@Override
	 public void insertItem(String item, String value, int index) {
		insertItem(item, value, value, index);
	}
	public void insertItem(String item, String value, String title, int index) {
		insertItem(item, value, title, index, false);
	}
	public void insertItem(String item, String value, String title, int index, boolean isValueSet) {
		if(this.doTrimDisplay){
			if(!isValueSet && !item.equalsIgnoreCase("--Select--")  && item.contains(":") && item.contains("-")){
				//do this only for the QDM dropdown.
				String stripoffOID = MatContext.get().stripOffOID(item);
				item = stripoffOID;
			}
		    SelectElement select = getElement().cast();
		    OptionElement option = Document.get().createOptionElement();
		    if(item.length() > this.maxWidth){
		    	String shortText = item.substring(0,65);
		    	option.setText(shortText);
		    }else{
		    	option.setText(item);
		    }
		    option.setValue(value);
		    option.setTitle(title);

		    if ((index == -1) || (index == select.getLength())) {
		      select.add(option, null);
		    } else {
		      OptionElement before = select.getOptions().getItem(index);
		      select.add(option, before);
		    }
		  }
		else{
			super.insertItem(item, value, index);
		}
	}
	
	public void setValue(String value) {
		setValue(value, false);
	}

	public void setValue(String value, boolean fireEvents) {
		boolean found = false;
		for(int i = 0; i < getItemCount(); i++) {
			if(getValue(i).equals(value)) {
				setItemSelected(i, true);
				found = true;
			}
			else {
				setItemSelected(i, false);
			}
		}
		
		if(found && fireEvents) {
			ValueChangeEvent.fire(this, value);
		}
	}
	
	public void setValueMetadata(String value) {
		setSelectedIndex(0);
		if(value == null){
			value = "";
		}
		for(int i = 0; i < getItemCount(); i++) {
			if(getItemText(i).equals(value)) {
				setSelectedIndex(i);
				break;
			}
		}
	}

	public void setDropdownOptions(List<NameValuePair> optionsArg) {
		clear();
		for(NameValuePair p : optionsArg) {
			addItem(p.getValue(), p.getName());
		}
	}
	
	/*
 	 * when value and item text are not the same thing (ex. one is a number and one is a String),
	 * we cannot always rely on the trimmed item text to be the value we want to process
	 */
	public String getItemTitle(int index){
		if(index >= 0) {
			SelectElement select = getElement().cast();
			OptionElement element =  select.getOptions().getItem(index);
			return element.getTitle();
		} else{
			return null;
		}
	}
	
	public void insertItem(String value, String title) {
	    insertItem(value, value, title);
	}
	
	public void insertItem(String text, String value, String title) {
	    SelectElement select = getElement().cast();
	    OptionElement option = Document.get().createOptionElement();
	    
	    option.setText(text);
	    option.setValue(value);
	    option.setTitle(title);
	    
	    select.add(option, null);
	}
	
}
