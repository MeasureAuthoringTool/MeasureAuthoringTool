package mat.client.shared;

import java.util.Date;

import mat.client.ImageResources;

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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DatePicker;

public class DateBoxWithCalendar extends Composite{
	private static final String DATE_BOX_FORMAT_ERROR = "dateBoxFormatError";

	
	
	FocusPanel fPanel;
	private HorizontalPanel panel = new HorizontalPanel();
	private TextBox dateBox;
	final DateTimeFormat df = DateTimeFormat.getFormat("MM/dd/yyyy");
	private PopupPanel pop;
	private DatePicker datePicker;
	private PopupPanel datePickerPopup;
	private boolean showInvalidMessage;
	private boolean valueChanged = false;
	private FocusableImageButton calendar;

	private int maxLength;
	public static final int MDY = 10;
	public static final int MDYHMA = 19;


	public DateBoxWithCalendar() {
		this(false,"", MDY);
	}
	
	public DateBoxWithCalendar(int maxLength) {
		this(false,"", maxLength);
	}
	
	public DateBoxWithCalendar(String availableDateId){
		this(false, availableDateId, MDY);
	}
	public DateBoxWithCalendar(boolean showInvalidMessage,String availableDateId, int maxLength) {
		this.showInvalidMessage = showInvalidMessage;
		this.maxLength = maxLength;
		dateBox = new TextBox();
		dateBox.setMaxLength(maxLength);
		int width = maxLength == MDYHMA ? 150 : 100;
		dateBox.setWidth("150");
		if(maxLength == MDY)
			dateBox.setTitle("Enter a date in MM/DD/YYYY");
		else
			dateBox.setTitle("Enter a date in MM/DD/YYYY hh:mm aa format.");
		datePicker = new DatePicker();
		
	    datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
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
		
		datePickerPopup = new PopupPanel(true);
		datePickerPopup.add(datePicker);
		initDateBoxWithCalender(availableDateId);
	
		pop = new PopupPanel(true);
		pop.setWidget(new Label("Not a valid date."));
		pop.setStylePrimaryName("WARN");

	}
 
	private KeyDownHandler keyDownHandler = new KeyDownHandler() {
		
		@Override
		public void onKeyDown(KeyDownEvent event) {
			datePickerPopup.hide();
		}
	};
	
	private FocusHandler focusHandler = new FocusHandler() {
		public void onFocus(FocusEvent event) {
			Widget source = (Widget)event.getSource();
			source.setStylePrimaryName("focus-border");
			hideInvalidDateMessage();
		}
	};
	private BlurHandler blurHandler = new BlurHandler() {
		public void onBlur(BlurEvent event) {
			Widget source = (Widget)event.getSource();
			source.setStylePrimaryName("no-border");
			if(!isDateValid()) {
				showInvalidDateMessage();
			}
			valueChanged = false;
		    ValueChangeEvent.fire(dateBox, dateBox.getValue());
			
		}
	};
	
	private boolean isDateValid() {
		Date testDate = new Date();
		String value = dateBox.getValue();
		if(value != null && value.length() > 0) {
			if(value.length() != 10) {
				return false;
			}
			else {
				int result = df.parse(value, 0, testDate);
				if(result == 0 || !df.format(testDate).equals(value)) {
					return false;
				}
			}
		}
		return true;
	}
	private void showInvalidDateMessage() {
		if(showInvalidMessage) {
			if(pop != null) {
				pop.setPopupPosition(dateBox.getAbsoluteLeft()+50, dateBox.getAbsoluteTop()+15);
			  	pop.show();
			}
	        dateBox.addStyleName(DATE_BOX_FORMAT_ERROR);
		}
	}

	public void hideInvalidDateMessage() {
		if(pop != null && pop.isShowing()) {
			pop.hide();
		}
	    dateBox.removeStyleName(DATE_BOX_FORMAT_ERROR);
	}
	
	private void initDateBoxWithCalender(final String availableDateId)
	{
		dateBox.setStylePrimaryName("no-border");
		panel.add(dateBox);
		calendar = new FocusableImageButton(ImageResources.INSTANCE.calendar(), "Calendar");
		calendar.addClickHandler(new ClickHandler() {			
			public void onClick(ClickEvent event) {
				if(!datePickerPopup.isShowing() && calendar.isEnabled()) {
					Date date = new Date();
					if(dateBox.getValue() != null &&
							dateBox.getValue().length() > 0 &&
							df.parseStrict(dateBox.getValue(), 0, date) > 0) {						
						datePicker.setValue(date);
						datePicker.setCurrentMonth(date);
					}
					else {
						date = new Date();
						datePicker.setValue(date);
						datePicker.setCurrentMonth(date);
					}
					
					datePickerPopup.showRelativeTo(dateBox);
				}
			}
		});
		calendar.addKeyDownHandler(keyDownHandler);
		panel.add(calendar);
		panel.setStylePrimaryName("thin-border");
		dateBox.addFocusHandler(focusHandler);
		dateBox.addBlurHandler(blurHandler);
	    dateBox.addKeyUpHandler(new KeyUpHandler() {
			public void onKeyUp(KeyUpEvent event) {
				formatDateValue(event,availableDateId);
			}
		});
	    dateBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER && valueChanged) {
					 ValueChangeEvent.fire(dateBox, dateBox.getValue());
					 valueChanged = false;
				}
			}
		});
	    fPanel = new FocusPanel(panel);
		initWidget(fPanel);
	}

	private void formatDateValue(KeyUpEvent event,String availableDateId) {
		int keyCode = event.getNativeKeyCode();
		if(!event.isAnyModifierKeyDown()) {
			int curCursPos = dateBox.getCursorPos();
			String value = dateBox.getValue();
			String newValue = "";
			
			for(int i = 0; i < value.length() && newValue.length() < maxLength; i++) {
				char c = value.charAt(i);
				if(availableDateId.equals("") && i < MDY){
					if((Character.isDigit(c) || c == 'x'|| c == 'X')) {
						newValue += c;
					}
				}else{
					if(Character.isDigit(c) && (i < MDY || i==11 || i==12 || i==14 || i==15)){
						newValue += c;
					}
				}
				
				if(newValue.length() == 2 || newValue.length() == 5) {
					newValue += "/";
				}
				else if(maxLength == MDYHMA){
					if(newValue.length() == 10 || newValue.length() == 16){
						newValue += " ";
					}else if(newValue.length() == 13){
						newValue += ":";
					}else if(newValue.length() == 17){
						if(c=='a' || c=='A'){
							newValue+="AM";
						}else if(c=='p' || c=='P')
							newValue+="PM";
					}
				}
			}
			if((curCursPos == 2 || curCursPos == 5 || curCursPos == 10 || curCursPos == 16 || curCursPos == 13) && 
					keyCode != KeyCodes.KEY_BACKSPACE &&
					keyCode != KeyCodes.KEY_LEFT) {
				curCursPos++;
			}
			dateBox.setValue(newValue);
			if(curCursPos > newValue.length()) {
				curCursPos = newValue.length();
			}
			dateBox.setCursorPos(curCursPos);
			valueChanged = true;
		}
	}

	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> changeListner) {
		return dateBox.addValueChangeHandler(changeListner);
	}
	

	public HandlerRegistration addFocusHandler(FocusHandler fHandler) {
		return fPanel.addFocusHandler(fHandler);
	}
	public HandlerRegistration addBlurHandler(BlurHandler bHandler) {
		return fPanel.addBlurHandler(bHandler);
	}

	public String getValue() {
		return dateBox.getValue();
	}

	public void setEnableCSS(boolean enabled) {
		
		/* enabling/disabling via css style
		 * as the setEnabled method is not refreshing properly when
		 * in transition from disabled to enabled state 
		 */
		if(enabled)
			dateBox.setStyleName("gwt-TextBox");
		else
			dateBox.setStyleName("gwt-TextBox-readonly");
		calendar.enableImage(enabled);
	}

	public void setEnabled(boolean enabled) {
		dateBox.setEnabled(enabled);
		calendar.enableImage(enabled);
	}
	
	public void setFocus(boolean focused) {
		fPanel.setFocus(focused);
		if(focused) {
			dateBox.setFocus(focused);
		}
	}

	public void setValue(String value) {
		dateBox.setValue(value);
	}

	public boolean isValidDateTime() {
		return dateBox.getValue() == null;
	}
	public int getTabIndex() {
		return dateBox.getTabIndex();
	}
	public void setAccessKey(char key) {
		dateBox.setAccessKey(key);
	}
	public void setTabIndex(int index) {
		dateBox.setTabIndex(index);
	}
	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return dateBox.addKeyDownHandler(handler);
	}

	/**
	 * @return the dateBox
	 */
	public TextBox getDateBox() {
		return dateBox;
	}

	/**
	 * @return the calendar
	 */
	public FocusableImageButton getCalendar() {
		return calendar;
	}


}