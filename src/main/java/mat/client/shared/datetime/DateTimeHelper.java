package mat.client.shared.datetime;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import org.gwtbootstrap3.client.ui.constants.ValidationState;

public class DateTimeHelper {

	public static final String DATE_TIME = "Date/Time";
	public static final String ENTER_DATE_TIME = "Enter Date/Time";

	private DateTimeHelper() {
		throw new IllegalStateException("DateTimeHelper class");
	}

	private static boolean isValid(final String day, final PredefinedFormat predef) {
		try {
			final DateTimeFormat dtf = DateTimeFormat.getFormat(predef);
			return (dtf.parseStrict(day) != null);
		} catch (final Exception e) {
			return false;
		}
	}

	private static boolean isValid(final String day, final String format) {
		try {
			final DateTimeFormat dtf = DateTimeFormat.getFormat(format);
			return (dtf.parseStrict(day) != null);
		} catch (final Exception e) {
			return false;
		}
	}

	public static String appendZeroString(final int count){
		final StringBuilder sb = new StringBuilder();
		for (int i=0;i<count;i++) {
			sb.append("0");
		}
		return sb.toString();
	}

	public static String buildZeroString(final String str, final int length) {
		final StringBuilder sb = new StringBuilder();
		if (str.length() < length){
			sb.append(DateTimeHelper.appendZeroString(length - str.length())).append(str);
		} else {
			sb.append(str);
		}
		return sb.toString();
	}

	private static boolean inRange(final String str, final int lower, final int upper) {
		if (str.isEmpty()){
			return true;
		}
		final int value = Integer.parseInt(str);

		return (value >= lower && value <= upper);
	}

	public static ValidationState getValidationState(final String str, final int lowerBound, final int upperBound) {

		return (inRange(str, lowerBound, upperBound)) ? ValidationState.NONE : ValidationState.ERROR;
	}

	public static boolean validateDate(final String year, final String month, final String day) {
		if (!month.isEmpty() && year.isEmpty()) {
			return false;
		} else if (!day.isEmpty() && (month.isEmpty()|| year.isEmpty())) {
			return false;
		} else if (!year.isEmpty() && !month.isEmpty() && !day.isEmpty()) {
			final StringBuilder sb = new StringBuilder();
			sb.append(year).append("/").append(month).append("/").append(day);
			if (!DateTimeHelper.isValidDate(sb.toString())) {
				return false;
			}
		} else if (!DateTimeHelper.inRange(year, 0, 9999)
				|| !DateTimeHelper.inRange(month, 1, 12)
				|| !DateTimeHelper.inRange(day, 1, 31)) {
			return false;
		}
		return true;
	}

	public static boolean validateTime(final String hh, final String mm, final String ss, final String fff) {

		return !( (!mm.isEmpty() && hh.isEmpty()) ||
				(!ss.isEmpty() && (hh.isEmpty()|| mm.isEmpty())) ||
				(!fff.isEmpty() && (hh.isEmpty()|| ss.isEmpty() || mm.isEmpty())) ||
				(!DateTimeHelper.inRange(hh, 0, 24) || !DateTimeHelper.inRange(mm, 0, 59)
						|| !DateTimeHelper.inRange(ss, 0, 59) || !DateTimeHelper.inRange(fff, 0, 999)) );
	}

	public static boolean isValidDate(final String inDate){
		return isValid(inDate, "yyyy/MM/dd");
	}

	public static boolean isValidDay(final String day) {
		return isValid(day, PredefinedFormat.DAY);
	}

	public static boolean isValidMonth(final String month) {
		return isValid(month, "MM");
	}

	public static boolean isValidYear(final String year) {
		return isValid(year, PredefinedFormat.YEAR);
	}

	public static boolean isValidHour(final String hour) {
		return isValid(hour, "HH");
	}

	public static boolean isValidMinute(final String minute) {
		return isValid(minute, "mm");
	}

	public static boolean isValidSecond(final String second) {
		return isValid(second, "ss");
	}

	public static boolean isValidMillisecond(final String millis) {
		return isValid(millis, "SSS");
	}

}
