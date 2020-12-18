package mat.client.shared;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.InputGroup;
import org.gwtbootstrap3.client.ui.InputGroupButton;
import org.gwtbootstrap3.client.ui.TextBox;
import org.gwtbootstrap3.client.ui.constants.ButtonType;
import org.gwtbootstrap3.client.ui.constants.IconType;

import java.util.Date;

/**
 * The Class DateBoxWithCalendar.
 */
public class DateBoxWithCalendar extends Composite {

    /**
     * The Constant DATE_BOX_FORMAT_ERROR.
     */
    private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";


    /**
     * The Constant MDY.
     */
    public static final int MDY = 10;

    /**
     * The Constant MDYHMA.
     */
    public static final int MDYHMA = 19;

    /**
     * The blur handler.
     */
    private BlurHandler blurHandler = new BlurHandler() {
        @Override
        public void onBlur(BlurEvent event) {
            if (!isDateValid()) {
                showInvalidDateMessage();
            }
            valueChanged = false;
            ValueChangeEvent.fire(dateBox, dateBox.getValue());

        }
    };

    /**
     * The calendar.
     */
    public Button calendar;

    /**
     * The date box.
     */
    private TextBox dateBox;

    /**
     * The date picker.
     */
    private DatePicker datePicker;

    /**
     * The date picker popup.
     */
    private PopupPanel datePickerPopup;

    /**
     * The df.
     */
    private DateTimeFormat df = DateTimeFormat.getFormat("MM/dd/yyyy");

    /**
     * The enabled.
     */
    boolean enabled;

    /**
     * The focus handler.
     */
    private FocusHandler focusHandler = new FocusHandler() {
        @Override
        public void onFocus(FocusEvent event) {
            hideInvalidDateMessage();
        }
    };

    /**
     * The panel.
     */
    SimplePanel fPanel;

    /**
     * The inv label.
     */
    public Label invLabel;

    /**
     * The label.
     */
    public String label;

    /**
     * The max length.
     */
    private int maxLength;

    /**
     * The panel.
     */
    private HorizontalPanel panel = new HorizontalPanel();

    /**
     * The pop.
     */
    private PopupPanel pop;

    /**
     * The show invalid message.
     */
    private boolean showInvalidMessage;

    /**
     * The value changed.
     */
    private boolean valueChanged = false;

    /**
     * Instantiates a new date box with calendar.
     */
    public DateBoxWithCalendar() {
        this(false, "", MDY);
    }

    /**
     * Instantiates a new date box with calendar.
     *
     * @param showInvalidMessage the show invalid message
     * @param availableDateId    the available date id
     * @param maxLength          the max length
     */
    public DateBoxWithCalendar(boolean showInvalidMessage, String availableDateId, int maxLength) {
        this.showInvalidMessage = showInvalidMessage;
        this.maxLength = maxLength;
        dateBox = new TextBox();
        dateBox.getElement().setId("dateBox_TextBox");
        invLabel = (Label) LabelBuilder.buildInvisibleLabelWithContent(new Label(), "Input Date", "Input Date");
        //dateBox.setStylePrimaryName("no-border");
        panel.getElement().setId("panel_HorizontalPanel");
        panel.add(invLabel);
        Element element = dateBox.getElement();
        element.setAttribute("id", "Input Date");
        element.setAttribute("aria-role", "textbox");
        element.setAttribute("aria-labelledby", "LastModifiedLabel");
        element.setAttribute("aria-live", "assertive");
        element.setAttribute("aria-invalid", "false");
        element.setAttribute("role", "alert");
        dateBox.setMaxLength(maxLength);
        dateBox.setWidth("150");
        if (maxLength == MDY) {
            dateBox.setTitle("Enter a date in MM/DD/YYYY");
        } else {
            dateBox.setTitle("Enter a date in MM/DD/YYYY hh:mm aa format.");
        }
        datePicker = new DatePicker();

        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            @Override
            public void onValueChange(ValueChangeEvent<Date> event) {
                Date date = event.getValue();
                String dateString = df.format(date);
                dateBox.setText(dateString);
                ValueChangeEvent.fire(dateBox, df.format(date));
                datePickerPopup.hide();
                hideInvalidDateMessage();
                dateBox.setFocus(true);
            }
        });

        datePickerPopup = new PopupPanel(true) {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                super.onPreviewNativeEvent(event);
                switch (event.getTypeInt()) {
                    case Event.ONKEYDOWN:
                        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                            hide();
                        }
                        break;
                }
            }
        };
        datePickerPopup.add(datePicker);
        initDateBoxWithCalender(availableDateId);

        pop = new PopupPanel(true) {
            @Override
            protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
                super.onPreviewNativeEvent(event);
                switch (event.getTypeInt()) {
                    case Event.ONKEYDOWN:
                        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                            hide();
                        }
                        break;
                }
            }
        };
        pop.setWidget(new Label("Not a valid date."));
        pop.setStylePrimaryName("WARN");

    }

    /**
     * Instantiates a new date box with calendar.
     *
     * @param dateTimeFormat the date time format
     */
    public DateBoxWithCalendar(DateTimeFormat dateTimeFormat) {
        this(false, "", dateTimeFormat.getPattern().length());
        df = dateTimeFormat;
        dateBox.setTitle("Enter date in " + dateTimeFormat.getPattern().toUpperCase());
        dateBox.setWidth("100");
    }

    /**
     * Instantiates a new date box with calendar.
     *
     * @param maxLength the max length
     */
    public DateBoxWithCalendar(int maxLength) {
        this(false, "", maxLength);
    }

    /**
     * Instantiates a new date box with calendar.
     *
     * @param availableDateId the available date id
     */
    public DateBoxWithCalendar(String availableDateId) {
        this(false, availableDateId, MDY);
    }


    /**
     * Adds the key down handler.
     *
     * @param handler the handler
     * @return the handler registration
     */
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return dateBox.addKeyDownHandler(handler);
    }

    /**
     * Adds the value change handler.
     *
     * @param changeListner the change listner
     * @return the handler registration
     */
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> changeListner) {
        return dateBox.addValueChangeHandler(changeListner);
    }

    /**
     * Format date value.
     *
     * @param event           the event
     * @param availableDateId the available date id
     */
    private void formatDateValue(KeyUpEvent event, String availableDateId) {
        int keyCode = event.getNativeKeyCode();
        if (!event.isAnyModifierKeyDown()) {
            int curCursPos = dateBox.getCursorPos();
            String value = dateBox.getValue();
            String newValue = "";

            for (int i = 0; (i < value.length()) && (newValue.length() < maxLength); i++) {
                char c = value.charAt(i);
                if (availableDateId.equals("") && (i < MDY)) {
                    if ((Character.isDigit(c) || (c == 'x') || (c == 'X'))) {
                        newValue += c;
                    }
                } else {
                    if (Character.isDigit(c) && ((i < MDY) || (i == 11) || (i == 12) || (i == 14) || (i == 15))) {
                        newValue += c;
                    }
                }

                if (((maxLength == MDY) || (maxLength == MDYHMA)) && ((newValue.length() == 2) || (newValue.length() == 5))) {
                    newValue += "/";
                } else if (maxLength == MDYHMA) {
                    if ((newValue.length() == 10) || (newValue.length() == 16)) {
                        newValue += " ";
                    } else if (newValue.length() == 13) {
                        newValue += ":";
                    } else if (newValue.length() == 17) {
                        if ((c == 'a') || (c == 'A')) {
                            newValue += "AM";
                        } else if ((c == 'p') || (c == 'P')) {
                            newValue += "PM";
                        }
                    }
                }
            }
            if (((curCursPos == 2) || (curCursPos == 5) || (curCursPos == 10) || (curCursPos == 16) || (curCursPos == 13)) &&
                    (keyCode != KeyCodes.KEY_BACKSPACE) &&
                    (keyCode != KeyCodes.KEY_LEFT) &&
                    ((maxLength == MDY) || (maxLength == MDYHMA))) {
                curCursPos++;
            }
            dateBox.setValue(newValue);
            if (curCursPos > newValue.length()) {
                curCursPos = newValue.length();
            }
            dateBox.setCursorPos(curCursPos);
            valueChanged = true;
        }
    }

    /**
     * Gets the calendar.
     *
     * @return the calendar
     */
    public Button getCalendar() {
        return calendar;
    }


    /**
     * Gets the date box.
     *
     * @return the dateBox
     */
    public TextBox getDateBox() {
        return dateBox;
    }

    /**
     * Gets the label.
     *
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Gets the tab index.
     *
     * @return the tab index
     */
    public int getTabIndex() {
        return dateBox.getTabIndex();
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return dateBox.getValue();
    }

    /**
     * Hide invalid date message.
     */
    public void hideInvalidDateMessage() {
        if ((pop != null) && pop.isShowing()) {
            pop.hide();
        }
        dateBox.removeStyleName(DATE_BOX_FORMAT_ERROR);
    }

    /**
     * Inits the date box with calender.
     *
     * @param availableDateId the available date id
     */
    private void initDateBoxWithCalender(final String availableDateId) {

        //calendar = new FocusableImageButton(ImageResources.INSTANCE.calendar(), "Calendar");
        calendar = new Button();
        calendar.setType(ButtonType.PRIMARY);
        calendar.setIcon(IconType.CALENDAR);
		/*calendar.getElement().setId("calendar_CustomButton");
		calendar.removeStyleName("gwt-button");
		calendar.setStylePrimaryName("invisibleButtonText");
		calendar.setResource(ImageResources.INSTANCE.calendar(), "Calendar");*/

        InputGroup iGroup = new InputGroup();
        InputGroupButton iGroupButton = new InputGroupButton();
        iGroupButton.add(calendar);
        iGroup.add(dateBox);
        iGroup.add(iGroupButton);
        calendar.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!datePickerPopup.isShowing() && calendar.isEnabled()) {
                    Date date = new Date();
                    if ((dateBox.getValue() != null) &&
                            (dateBox.getValue().length() > 0) &&
                            (df.parseStrict(dateBox.getValue(), 0, date) > 0)) {
                        datePicker.setValue(date);
                        datePicker.setCurrentMonth(date);
                    } else {
                        date = new Date();
                        datePicker.setValue(date);
                        datePicker.setCurrentMonth(date);
                    }

                    datePickerPopup.showRelativeTo(dateBox);
                }
            }
        });
        //calendar.addClickHandler(clickHandler);
        panel.add(iGroup);
        panel.setStylePrimaryName("thin-border");
        dateBox.addFocusHandler(focusHandler);
        dateBox.addBlurHandler(blurHandler);
        dateBox.addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                formatDateValue(event, availableDateId);
            }
        });
        dateBox.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if ((event.getNativeKeyCode() == KeyCodes.KEY_ENTER) && valueChanged) {
                    ValueChangeEvent.fire(dateBox, dateBox.getValue());
                    valueChanged = false;
                }
            }
        });
        fPanel = new SimplePanel(panel);
        initWidget(fPanel);
    }

    /**
     * Checks if is date valid.
     *
     * @return true, if is date valid
     */
    public boolean isDateValid() {
        Date testDate = new Date();
        String value = dateBox.getValue();
        if ((value != null) && (value.length() > 0)) {
            if (value.length() != 10) {
                return false;
            } else {
                int result = df.parse(value, 0, testDate);
                if ((result == 0) || !df.format(testDate).equals(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Get the date as java.util.date.
     *
     * @return Date, if is date not valid return null
     */
    public Date getDate() {
        Date date = new Date();
        String value = dateBox.getValue();
        if ((value != null) && (value.length() > 0)) {
            if (value.length() != 10) {
                return null;
            } else {
                int result = df.parse(value, 0, date);
                if ((result == 0) || !df.format(date).equals(value)) {
                    return null;
                }
            }
        }
        return date;
    }


    /**
     * Checks if is enabled.
     *
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Checks if is valid date time.
     *
     * @return true, if is valid date time
     */
    public boolean isValidDateTime() {
        return dateBox.getValue() == null;
    }

    /**
     * Sets the access key.
     *
     * @param key the new access key
     */
    public void setAccessKey(char key) {
        dateBox.setAccessKey(key);
    }

    /**
     * Sets the enable css.
     *
     * @param enabled the new enable css
     */
    public void setEnableCSS(boolean enabled) {

        /* enabling/disabling via css style
         * as the setEnabled method is not refreshing properly when
         * in transition from disabled to enabled state
         */
        if (enabled) {
            //dateBox.setStyleName("gwt-TextBox");
            dateBox.setEnabled(true);
            dateBox.setReadOnly(false);
            dateBox.setTabIndex(0);
        } else {
            //dateBox.setStyleName("gwt-TextBox-readonly");
            dateBox.setEnabled(false);
            dateBox.setReadOnly(true);
            dateBox.setTabIndex(-1);
        }
        //calendar.enableImage(enabled);
        calendar.setEnabled(enabled);
    }

    /**
     * Sets the enabled.
     *
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        dateBox.setEnabled(enabled);
        dateBox.setReadOnly(!enabled);
        dateBox.setTabIndex(enabled ? 0 : -1);

        //calendar.enableImage(enabled);
        calendar.setEnabled(enabled);
        if (label != null) {
            invLabel.getElement().setAttribute("id", label);
            dateBox.getElement().setAttribute("id", label);
        }

        if (enabled) {
            if (label != null) {
                //	calendar.getImage().setAltText(label + " Calendar Enabled");
                calendar.setTitle(label + " Enabled");
                invLabel.setText(label + " Enabled");
            }
        } else {
            if (label != null) {
                //calendar.getImage().setAltText(label + " Calendar Disabled");
                calendar.setTitle(label + " Disabled");
                invLabel.setText(label + " Disabled");
            }
        }

    }

    /**
     * Sets the focus.
     *
     * @param focused the new focus
     */
    public void setFocus(boolean focused) {
        if (focused) {
            dateBox.setFocus(true);
        }
    }

    /**
     * Sets the label.
     *
     * @param label the new label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Sets the tab index.
     *
     * @param index the new tab index
     */
    public void setTabIndex(int index) {
        dateBox.setTabIndex(index);
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     */
    public void setValue(String value) {
        dateBox.setValue(value);
    }

    /**
     * Show invalid date message.
     */
    private void showInvalidDateMessage() {
        if (showInvalidMessage) {
            if (pop != null) {
                pop.setPopupPosition(dateBox.getAbsoluteLeft() + 50, dateBox.getAbsoluteTop() + 15);
                pop.show();
            }
            dateBox.addStyleName(DATE_BOX_FORMAT_ERROR);
        }
    }

}