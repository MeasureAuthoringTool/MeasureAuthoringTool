package org.ifmc.mat.client.shared;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class ReadOnlyHelper {
	public static void setReadOnlyForCurrentMeasure(Widget panel,boolean editable) {
		if(!editable) {
			ReadOnlyHelper.setReadOnly(panel.getElement());
		}
		else {
			ReadOnlyHelper.restoreReadOnly(panel.getElement());
		}

	}
	
	private static void setButtonsDisabledForCurrentMeasure(Widget panel) {
		if(MatContext.get().getMeasureLockService().checkForEditPermission()) {
			ReadOnlyHelper.setReadOnlyButtons(panel.getElement());
		}
		else {
			ReadOnlyHelper.restoreReadOnlyButtons(panel.getElement());
		}
	}
	
	/*
	public static void setReadOnlyForCodeList(Widget panel){
		boolean isCodeListEditable = MatContext.get().checkForCodeListEditPermission();
		if(!isCodeListEditable) {
			ReadOnlyHelper.setReadOnly(panel.getElement());
		}else{
			ReadOnlyHelper.restoreReadOnly(panel.getElement());
		}
	}*/
	
	private static native void restoreReadOnly(Element element) /*-{
		
		var restoreReadOnly = function(list) {
			if(! (list === undefined)) {
				if(!(list.originalValue === undefined)) {
					list.readOnly = list.originalValue;
				}
			}
		} 
		var restoreDisabled = function(item) {
			if(! (item === undefined) ){
				if(!(item.originalValue === undefined)) {
					item.disabled = item.originalValue;
				}
			}
		}
				
		var inputs = element.getElementsByTagName("input");
		if(inputs != null) {
			for(input in inputs) {
				restoreDisabled(inputs[input]);
			}
		}
		var textareas = element.getElementsByTagName("textarea");
		if(textareas != null) {
			for(textarea in textareas) {
				restoreDisabled(textareas[textarea]);
			}
		}

		var selects = element.getElementsByTagName("select");
		if(selects != null) {
			for(select in selects) {
				restoreDisabled(selects[select]);
			}
		}
			
	}-*/;
	private static native void setReadOnly(Element element) /*-{
		var setReadOnly = function(list) {
			if(! (list === undefined)) {
				if(list.originalValue === undefined) {
					list.originalValue = list.readOnly;
				}
				list.readOnly = true;
			}
		};
		var setDisabled = function(item) {
			if(! (item === undefined)) {
				if(item.originalValue === undefined) {
					item.originalValue = item.disabled;
				}
				item.disabled = true;
			}
		}
		
		var inputs = element.getElementsByTagName("input");
		if(inputs != null) {
			for(input in inputs) {
				setDisabled(inputs[input]);
			}
		}

		var textareas = element.getElementsByTagName("textarea");
		if(textareas != null) {
			for(textarea in textareas) {
				setDisabled(textareas[textarea]);
			}
		}
		
		var selects = element.getElementsByTagName("select");
		if(selects != null) {
			for(select in selects) {
				setDisabled(selects[select]);
			}
		}

	}-*/;
	
	private static native void restoreReadOnlyButtons(Element element) /*-{

	var restoreDisabled = function(item) {
		if(!(item.originalValue === undefined)) {
			item.disabled = item.originalValue;
		}
	}
	var buttons = element.getElementsByTagName("button");
	Array.forEach(buttons, restoreDisabled);		
		
}-*/;
private static native void setReadOnlyButtons(Element element) /*-{
	var setDisabled = function(item) {
		if(item.originalValue === undefined) {
			item.originalValue = item.disabled;
		}
		item.disabled = true;
	}
	var buttons = element.getElementsByTagName("button");
	Array.forEach(buttons, setDisabled);
}-*/;
}
