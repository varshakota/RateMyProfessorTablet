package extended.cs.sdsu.edu.util;

import java.util.Calendar;

public class ApplicationUtils {

	public static String getCurrentDateString() {
		Calendar date = Calendar.getInstance();
		int day = date.get(Calendar.DATE);
		int month = (date.get(Calendar.MONTH)) + 1;
		int year = date.get(Calendar.YEAR);
		String dateString = month + "-" + day + "-" + year;
		return dateString;
	}

	public static boolean isResponseBodyEmpty(String responseBody) {
		if ("[]".equals(responseBody)) {
			return true;
		}
		return false;
	}
}
