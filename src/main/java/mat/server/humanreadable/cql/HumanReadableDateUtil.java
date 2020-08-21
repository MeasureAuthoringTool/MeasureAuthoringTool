package mat.server.humanreadable.cql;

public class HumanReadableDateUtil {

	public static String getFormattedMeasurementPeriod(boolean isCalendarYear, String startDate, String endDate) {
		String formattedStartDate = " ";
		String formattedEndDate = " ";
		String through = " through ";
		// if start or end are not null format the date
		if(!isCalendarYear){
			if (!" ".equals(startDate)) {
				formattedStartDate = formatDate(startDate);
			}
			if (!" ".equals(endDate)) {
				formattedEndDate = formatDate(endDate);
			}
		} else {
			if (!" ".equals(startDate)) {
				formattedStartDate = formatDate("20XX0101");
			}
			if (!" ".equals(endDate)) {
				formattedEndDate = formatDate("20XX1231");
			}
		}
		// if the ending date is null we don't want through to display
		if (" ".equals(formattedEndDate)) {
			through = "";
		}
		
		return formattedStartDate + through + formattedEndDate;
	}
	
	private static String formatDate(String date) {
		String returnDate = " ";
		// The string should be 8 characters long if not return " "
		if (date.length() == 8) {
			// Separate the string into year, month, and dat
			String year = date.substring(0, 4);
			String month = date.substring(4, 6);
			String day = date.substring(6, 8);
			// Get the string version of the month
			month = getMonth(month);
			// if the year equals 0000 we display 20xx
			if ("0000".equals(year)) {
				year = "20XX";
			}
			// if the day starts with a zero only display the second digit
			if (day.charAt(0) == '0') {
				day = day.substring(1, 2);
			}
			returnDate = month + day + ", " + year;
		}
		return returnDate;
	}
	
	private static String getMonth(String month) {
		String returnMonth = "";
		if ("01".equals(month)) {
			returnMonth = "January ";
		} else if ("02".equals(month)) {
			returnMonth = "February ";
		} else if ("03".equals(month)) {
			returnMonth = "March ";
		} else if ("04".equals(month)) {
			returnMonth = "April ";
		} else if ("05".equals(month)) {
			returnMonth = "May ";
		} else if ("06".equals(month)) {
			returnMonth = "June ";
		} else if ("07".equals(month)) {
			returnMonth = "July ";
		} else if ("08".equals(month)) {
			returnMonth = "August ";
		} else if ("09".equals(month)) {
			returnMonth = "September ";
		} else if ("10".equals(month)) {
			returnMonth = "October ";
		} else if ("11".equals(month)) {
			returnMonth = "November ";
		} else if ("12".equals(month)) {
			returnMonth = "December ";
		}
		return returnMonth;
	}

	public static String getFormattedMeasurementPeriodForFhir(String startDate, String endDate) {

        String[] startDateMonth = startDate.split("/");
        String[] endDateMonth = endDate.split("/");

        String formattedStartDate = getMonth(startDateMonth[0]) + " " + startDateMonth[1] + ", " + startDateMonth[2];
        String formattedEndDate = getMonth(endDateMonth[0]) + " " + endDateMonth[1] + ", " + endDateMonth[2];

        return formattedStartDate + " through "+ formattedEndDate;
    }
}
