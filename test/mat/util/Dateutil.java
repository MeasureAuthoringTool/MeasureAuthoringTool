package mat.util;

public class Dateutil {
	  public static void main(String[] av) {
		     String dateString = "12-06-0000";

		     String monthText = dateString.substring(0, 2);
		     String dayText  = dateString.substring(3, 5);
		     String yearText  = dateString.substring(6);
		     String newDate = yearText+monthText+dayText;
		     System.out.println(newDate);

	  }
}
