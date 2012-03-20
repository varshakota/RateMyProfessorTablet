package extended.cs.sdsu.edu.domain;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceWrapper {

	private static final String PREFS_NAME = "datePref";
	private SharedPreferences sharedPreference;

	public SharedPreferenceWrapper(Context context) {
		sharedPreference = context.getSharedPreferences(PREFS_NAME, 0);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = sharedPreference.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getString(String key) {
		String value = sharedPreference.getString(key, "");
		return value;
	}
}
