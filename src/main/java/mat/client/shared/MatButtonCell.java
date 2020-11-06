package mat.client.shared;

/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


import com.google.gwt.cell.client.AbstractSafeHtmlCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;

import static com.google.gwt.dom.client.BrowserEvents.CLICK;
import static com.google.gwt.dom.client.BrowserEvents.KEYDOWN;

/**
 * A {@link Cell} used to render a button.
 */
public class MatButtonCell extends AbstractSafeHtmlCell<String> {

   /** The Button title. */
   String ButtonTitle=""; 	
   
   /** The css class. */
   String cssClass="";
   
   String iconClass = "";
   
   String invisibleButtonText ="";
  
  /**
   * Construct a new ButtonCell that will use a {@link SimpleSafeHtmlRenderer}.
   */
  public MatButtonCell() {
    this(SimpleSafeHtmlRenderer.getInstance());
  }

  /**
	 * Instantiates a new mat button cell.
	 * 
	 * @param ButtonTitle
	 *            the button title
	 * @param cssString
	 *            the css string
	 */
  public MatButtonCell(String ButtonTitle , String cssString) {
	    this(SimpleSafeHtmlRenderer.getInstance());
	    this.ButtonTitle=ButtonTitle;
	    this.cssClass = cssString;
	  }
  
  public MatButtonCell(String ButtonTitle , String cssString, String iconCss, String invisibleButtonText) {
	    this(SimpleSafeHtmlRenderer.getInstance());
	    this.ButtonTitle=ButtonTitle;
	    this.cssClass = cssString;
	    this.iconClass = iconCss;
	    this.invisibleButtonText = invisibleButtonText;
	  }
  
  /**
   * Construct a new ButtonCell that will use a given {@link SafeHtmlRenderer}.
   * 
   * @param renderer a {@link SafeHtmlRenderer SafeHtmlRenderer<String>} instance
   */
  public MatButtonCell(SafeHtmlRenderer<String> renderer) {
    super(renderer, CLICK, KEYDOWN);
  }

/* (non-Javadoc)
 * @see com.google.gwt.cell.client.AbstractCell#onBrowserEvent(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
 */
@Override
  public void onBrowserEvent(Context context, Element parent, String value,
      NativeEvent event, ValueUpdater<String> valueUpdater) {
    super.onBrowserEvent(context, parent, value, event, valueUpdater);
    if (CLICK.equals(event.getType())) {
      EventTarget eventTarget = event.getEventTarget();
      if (!Element.is(eventTarget)) {
        return;
      }
      if (parent.getFirstChildElement().isOrHasChild(Element.as(eventTarget))) {
        // Ignore clicks that occur outside of the main element.
        onEnterKeyDown(context, parent, value, event, valueUpdater);
      }
    }
  }

  /* (non-Javadoc)
   * @see com.google.gwt.cell.client.AbstractSafeHtmlCell#render(com.google.gwt.cell.client.Cell.Context, com.google.gwt.safehtml.shared.SafeHtml, com.google.gwt.safehtml.shared.SafeHtmlBuilder)
   */
  @Override
  public void render(Context context, SafeHtml data, SafeHtmlBuilder sb) {
	  
	  sb.appendHtmlConstant("<button type=\"button\" title=\" " + ButtonTitle + "\"  class=\" "+cssClass+"\">");
	  if(cssClass.equalsIgnoreCase("btn btn-link")){
		 sb.appendHtmlConstant("<i class=\" "+iconClass+"\"></i>");
		 sb.appendHtmlConstant("<span style=\"font-size:0;\">"+invisibleButtonText+"</span>");
	  }
	  if (data != null) {
		  sb.append(data);
	}
	  sb.appendHtmlConstant("</button>");
  }

 /* (non-Javadoc)
  * @see com.google.gwt.cell.client.AbstractCell#onEnterKeyDown(com.google.gwt.cell.client.Cell.Context, com.google.gwt.dom.client.Element, java.lang.Object, com.google.gwt.dom.client.NativeEvent, com.google.gwt.cell.client.ValueUpdater)
  */
 @Override
  protected void onEnterKeyDown(Context context, Element parent, String value,
      NativeEvent event, ValueUpdater<String> valueUpdater) {
    if (valueUpdater != null) {
      valueUpdater.update(value);
    }
  }
}


