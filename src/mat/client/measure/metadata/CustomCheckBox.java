package mat.client.measure.metadata;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HasName;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

/**
 * A standard check box widget.
 * 
 * This class also serves as a base class for
 * {@link com.google.gwt.user.client.ui.RadioButton}.
 * 
 * <p>
 * <img class='gallery' src='doc-files/CheckBox.png'/>
 * </p>
 * 
 * <h3>CSS Style Rules</h3>
 * <dl>
 * <dt>.gwt-CheckBox</dt>
 * <dd>the outer element</dd>
 * <dt>.gwt-CheckBox-disabled</dt>
 * <dd>applied when Checkbox is disabled</dd>
 * </dl>
 * 
 * <p>
 * <h3>Example</h3>
 * {@example com.google.gwt.examples.CheckBoxExample}
 * </p>
 */
public class CustomCheckBox extends ButtonBase implements HasName, HasValue<Boolean> {
  
  /** The input elem. */
  InputElement inputElem;
  
  /** The label elem. */
  LabelElement labelElem;
  
  /** The is label required. */
  Boolean isLabelRequired = false;
  
  /** The value change handler initialized. */
  private boolean valueChangeHandlerInitialized;

  

  /**
	 * Creates a check box with the specified title for Input Element.
	 * 
	 * @param title
	 *            for the check box's
	 * @param isLabelRequired
	 *            the is label required
	 */
  public CustomCheckBox(String title,boolean isLabelRequired) {
	this(DOM.createInputCheck(),title,isLabelRequired);
    setText(title,isLabelRequired);
    this.isLabelRequired=isLabelRequired;
  }

  /**
	 * Creates a check box with the specified title, label for Input Element.
	 * 
	 * @param title
	 *            for the check box's
	 * @param label
	 *            the label
	 * @param isLabelRequired
	 *            the is label required
	 */
  public CustomCheckBox(String title, String label,boolean isLabelRequired) {
	  
	this(DOM.createInputCheck(),title,isLabelRequired);
	setText(label,true);
  }
  
  /**
	 * Instantiates a new custom check box.
	 * 
	 * @param title
	 *            the title
	 * @param label
	 *            the label
	 * @param labelOrder
	 *            the label order
	 */
  public CustomCheckBox(String title, String label, int labelOrder) {
	  	this(DOM.createInputCheck(),title,labelOrder);
		setText(label,true);
 }


  /**
	 * Instantiates a new custom check box.
	 * 
	 * @param elem
	 *            the elem
	 * @param title
	 *            the title
	 * @param isLabelRequired2
	 *            the is label required2
	 */
  protected CustomCheckBox(Element elem,String title, boolean isLabelRequired2) {
    //super(DOM.createSpan());
	  super(DOM.createDiv());
	  
    inputElem = InputElement.as(elem);
    labelElem = Document.get().createLabelElement();
    labelElem.addClassName("customCheckBoxLabel");
	inputElem.addClassName("customCheckBox");
	if(isFirefoxBrowser()) {
		inputElem.getStyle().setTop(2, Unit.PX);
	}
   
    getElement().appendChild(inputElem);
    /**508 fix - Check box with no label should not render label tag inside Input tag.**/
    if(isLabelRequired2)
    	getElement().appendChild(labelElem);

    String uid = DOM.createUniqueId();
    /**508 fix - Id attribute should be removed instead title should be added.**/
  //  inputElem.setPropertyString("id", uid);
    inputElem.setPropertyString("title", title);
    inputElem.setPropertyString("name", title);
    labelElem.setHtmlFor(uid);

    // Accessibility: setting tab index to be 0 by default, ensuring element
    // appears in tab sequence. FocusWidget's setElement method already
    // calls setTabIndex, which is overridden below. However, at the time
    // that this call is made, inputElem has not been created. So, we have
    // to call setTabIndex again, once inputElem has been created.
    setTabIndex(0);
  }
  
  /**
   * Checks if the browser is firefox browser.
   *
   * @return true, if the browser is firefox browser else returns false.
   */
  private boolean isFirefoxBrowser() {
	  if(Navigator.getUserAgent().toLowerCase().contains("firefox")) {
		return true;
	  }
	  return false;
  }

  	/**
	 * Instantiates a new custom check box.
	 * 
	 * @param elem
	 *            the elem
	 * @param title
	 *            the title
	 * @param labelOrder
	 *            the label order
	 */
  protected CustomCheckBox(Element elem,String title,int labelOrder) {
	    //super(DOM.createSpan());
		  super(DOM.createDiv());
 
	    inputElem = InputElement.as(elem);
	    labelElem = Document.get().createLabelElement();
	    labelElem.addClassName("customCheckBoxLabel");
		inputElem.addClassName("customCheckBox");
		if(isFirefoxBrowser()) {
			inputElem.getStyle().setTop(2, Unit.PX);
		}
		
	   if (labelOrder > 2) {
		   getElement().appendChild(inputElem);
		   getElement().appendChild(labelElem);
	   } else {
		 
		   getElement().appendChild(labelElem);
		   getElement().appendChild(inputElem);
	   }

	    String uid = DOM.createUniqueId();
	    /**508 fix - Id attribute should be removed instead title should be added.**/
	  //  inputElem.setPropertyString("id", uid);
	      inputElem.setPropertyString("title", title);
	      inputElem.setPropertyString("id", title);
	    inputElem.setPropertyString("name", title);
	    labelElem.setHtmlFor(uid);

	    // Accessibility: setting tab index to be 0 by default, ensuring element
	    // appears in tab sequence. FocusWidget's setElement method already
	    // calls setTabIndex, which is overridden below. However, at the time
	    // that this call is made, inputElem has not been created. So, we have
	    // to call setTabIndex again, once inputElem has been created.
	    setTabIndex(0);
	  }

  /* (non-Javadoc)
   * @see com.google.gwt.event.logical.shared.HasValueChangeHandlers#addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler)
   */
  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<Boolean> handler) {
    // Is this the first value change handler? If so, time to add handlers
    if (!valueChangeHandlerInitialized) {
      ensureDomEventHandlers();
      valueChangeHandlerInitialized = true;
    }
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
	 * Returns the value property of the input element that backs this widget.
	 * This is the value that will be associated with the CheckBox name and
	 * submitted to the server if a {@link FormPanel} that holds it is submitted
	 * and the box is checked.
	 * <p>
	 * Don't confuse this with {@link #getValue}, which returns true or false if
	 * the widget is checked.
	 * 
	 * @return the form value
	 */
  public String getFormValue() {
    return inputElem.getValue();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ButtonBase#getHTML()
   */
  @Override
  public String getHTML() {
    return "";
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasName#getName()
   */
  public String getName() {
    return inputElem.getName();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#getTabIndex()
   */
  @Override
  public int getTabIndex() {
    return inputElem.getTabIndex();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ButtonBase#getText()
   */
  @Override
  public String getText() {
    return "";
  }
  

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.UIObject#setTitle(java.lang.String)
   */
  public void setTitle(String title){
	  inputElem.setPropertyString("title", title);
  }

  /**
	 * Determines whether this check box is currently checked.
	 * <p>
	 * Note that this <em>is not</em> return the value property of the checkbox
	 * input element wrapped by this widget. For access to that property, see
	 * 
	 * @return <code>true</code> if the check box is checked, false otherwise.
	 *         Will not return null {@link #getFormValue()}
	 */
  public Boolean getValue() {
    if (isAttached()) {
      return inputElem.isChecked();
    } else {
      return inputElem.isDefaultChecked();
    }
  }

  /**
   * Determines whether this check box is currently checked.
   * 
   * @return <code>true</code> if the check box is checked
   * @deprecated Use {@link #getValue} instead
   */
  @Deprecated
  public boolean isChecked() {
    // Funny comparison b/c getValue could in theory return null
    return getValue() == true; 
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#isEnabled()
   */
  @Override
  public boolean isEnabled() {
    return !inputElem.isDisabled();
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#setAccessKey(char)
   */
  @Override
  public void setAccessKey(char key) {
    inputElem.setAccessKey("" + key);
  }

  /**
   * Checks or unchecks this check box. Does not fire {@link ValueChangeEvent}.
   * (If you want the event to fire, use {@link #setValue(Boolean, boolean)})
   * 
   * @param checked <code>true</code> to check the check box.
   * @deprecated Use {@link #setValue(Boolean)} instead
   */
  @Deprecated
  public void setChecked(boolean checked) {
    setValue(checked);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#setEnabled(boolean)
   */
  @Override
  public void setEnabled(boolean enabled) {
    inputElem.setDisabled(!enabled);
    if (enabled) {
      removeStyleDependentName("disabled");
    } else {
      addStyleDependentName("disabled");
    }
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#setFocus(boolean)
   */
  @Override
  public void setFocus(boolean focused) {
    if (focused) {
      inputElem.focus();
    } else {
      inputElem.blur();
    }
  }

  /**
	 * Set the value property on the input element that backs this widget. This
	 * is the value that will be associated with the CheckBox's name and
	 * submitted to the server if a {@link FormPanel} that holds it is submitted
	 * and the box is checked.
	 * <p>
	 * Don't confuse this with {@link #setValue}, which actually checks and
	 * unchecks the box.
	 * 
	 * @param value
	 *            the new form value
	 */
  public void setFormValue(String value) {
    inputElem.setAttribute("value", value);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.ButtonBase#setHTML(java.lang.String)
   */
  @Override
  public void setHTML(String html) {
    
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.HasName#setName(java.lang.String)
   */
  public void setName(String name) {
    inputElem.setName(name);
  }

  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.FocusWidget#setTabIndex(int)
   */
  @Override
  public void setTabIndex(int index) {
    // Need to guard against call to setTabIndex before inputElem is
    // initialized. This happens because FocusWidget's (a superclass of
    // CheckBox) setElement method calls setTabIndex before inputElem is
    // initialized. See CheckBox's protected constructor for more information.
    if (inputElem != null) {
      inputElem.setTabIndex(index);
    }
  }

  /**
	 * Sets the text.
	 * 
	 * @param text
	 *            the text
	 * @param isLabelRequired
	 *            the is label required
	 */
  public void setText(String text,boolean isLabelRequired) {
    if(isLabelRequired){
    	labelElem.setInnerText(text);
    }
  }

  /**
	 * Checks or unchecks the text box.
	 * <p>
	 * Note that this <em>does not</em> set the value property of the checkbox
	 * input element wrapped by this widget. For access to that property, see
	 * 
	 * @param value
	 *            true to check, false to uncheck; must not be null
	 *            {@link #setFormValue(String)}
	 */
  public void setValue(Boolean value) {
    setValue(value, false);
  }

  /**
	 * Checks or unchecks the text box, firing {@link ValueChangeEvent} if
	 * appropriate.
	 * <p>
	 * Note that this <em>does not</em> set the value property of the checkbox
	 * input element wrapped by this widget. For access to that property, see
	 * 
	 * @param value
	 *            true to check, false to uncheck; must not be null
	 * @param fireEvents
	 *            If true, and value has changed, fire a
	 *            {@link #setFormValue(String)} {@link ValueChangeEvent}
	 */
  public void setValue(Boolean value, boolean fireEvents) {
    if (value == null) {
      throw new IllegalArgumentException("value must not be null");
    }

    Boolean oldValue = getValue();
    inputElem.setChecked(value);
    inputElem.setDefaultChecked(value);
    if (value.equals(oldValue)) {
      return;
    }
    if (fireEvents) {
      ValueChangeEvent.fire(this, value);
    }
  }

  // Unlike other widgets the CheckBox sinks on its inputElement, not
  // its wrapper
  /* (non-Javadoc)
   * @see com.google.gwt.user.client.ui.Widget#sinkEvents(int)
   */
  @Override
  public void sinkEvents(int eventBitsToAdd) {
    if (isOrWasAttached()) {
      Event.sinkEvents(inputElem, 
          eventBitsToAdd | Event.getEventsSunk(inputElem));
    } else {
      super.sinkEvents(eventBitsToAdd);
    }
  }

  /**
	 * Ensure dom event handlers.
	 */
  protected void ensureDomEventHandlers() {
    addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        // Checkboxes always toggle their value, no need to compare
        // with old value. Radio buttons are not so lucky, see
        // overrides in RadioButton
        ValueChangeEvent.fire(CustomCheckBox.this, getValue());
      }
    });
  }

  /**
	 * <b>Affected Elements:</b>
	 * <ul>
	 * <li>-label = label next to checkbox.</li>
	 * </ul>
	 * 
	 * @param baseID
	 *            the base id
	 * @see UIObject#onEnsureDebugId(String)
	 */
  @Override
  protected void onEnsureDebugId(String baseID) {
    super.onEnsureDebugId(baseID);
   // ensureDebugId(labelElem, baseID, "label");
    ensureDebugId(inputElem, baseID, "input");
    //labelElem.setHtmlFor(inputElem.getId());
  }

  /**
   * This method is called when a widget is attached to the browser's document.
   * onAttach needs special handling for the CheckBox case. Must still call
   * {@link Widget#onAttach()} to preserve the <code>onAttach</code> contract.
   */
  @Override
  protected void onLoad() {
    setEventListener(inputElem, this);
  }

  /**
   * This method is called when a widget is detached from the browser's
   * document. Overridden because of IE bug that throws away checked state and
   * in order to clear the event listener off of the <code>inputElem</code>.
   */
  @Override
  protected void onUnload() {
    // Clear out the inputElem's event listener (breaking the circular
    // reference between it and the widget).
    setEventListener(asOld(inputElem), null);
    setValue(getValue());
  }

  /**
	 * Replace the current input element with a new one. Preserves all state
	 * except for the name property, for nasty reasons related to radio button
	 * grouping. (See implementation of
	 * 
	 * @param elem
	 *            the new input element {@link RadioButton#setName}.)
	 */
  protected void replaceInputElement(Element elem) {
    InputElement newInputElem = InputElement.as(elem);
    // Collect information we need to set
    int tabIndex = getTabIndex();
    boolean checked = getValue();
    boolean enabled = isEnabled();
    String formValue = getFormValue();
    String uid = inputElem.getId();
    String accessKey = inputElem.getAccessKey();
    int sunkEvents = Event.getEventsSunk(inputElem);   

    // Clear out the old input element
    setEventListener(asOld(inputElem), null);

    getElement().replaceChild(newInputElem, inputElem);

    // Sink events on the new element
    Event.sinkEvents(elem, Event.getEventsSunk(inputElem));
    Event.sinkEvents(inputElem, 0);
    inputElem = newInputElem;

    // Setup the new element
    Event.sinkEvents(inputElem, sunkEvents);
    inputElem.setId(uid);
    if (!"".equals(accessKey)) {
      inputElem.setAccessKey(accessKey);
    }
    setTabIndex(tabIndex);
    setValue(checked);
    setEnabled(enabled);
    setFormValue(formValue);

    // Set the event listener
    if (isAttached()) {
      setEventListener(asOld(inputElem), this);
    }
  }

  /**
	 * As old.
	 * 
	 * @param elem
	 *            the elem
	 * @return the element
	 */
  private Element asOld(com.google.gwt.dom.client.Element elem) {
    Element oldSchool = elem.cast();
    return oldSchool;
  }

  /**
	 * Sets the event listener.
	 * 
	 * @param e
	 *            the e
	 * @param listener
	 *            the listener
	 */
  private void setEventListener(com.google.gwt.dom.client.Element e,
      EventListener listener) {
    DOM.setEventListener(asOld(e), listener);
  }

}