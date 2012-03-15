package extended.cs.sdsu.edu.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import extended.cs.sdsu.edu.domain.Professor;

public class Operations {

	public void inserttoDb(Professor professor) {

		DatabaseHelper dbHelper = new DatabaseHelper(null);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("_ID", professor.getId());
		cv.put("FIRSTNAME", professor.getFirstName());
		cv.put("LASTNAME", professor.getLastName());

		db.insert("PROFESSOR", null, cv);

	}
}
