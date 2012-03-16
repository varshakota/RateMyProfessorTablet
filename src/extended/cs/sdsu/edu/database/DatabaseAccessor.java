package extended.cs.sdsu.edu.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import extended.cs.sdsu.edu.domain.Professor;

public class DatabaseAccessor {

	public Cursor selectQuery(Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor result = db.rawQuery("SELECT * from PROFESSOR", null);
		return result;
	}

	public void insertProfessorListToDb(List<Professor> professorListData,
			Context context) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		System.out.println("Db insert");
		for (int i = 0; i < professorListData.size(); i++) {
			Professor professorFromList = professorListData.get(i);
			ContentValues cv = new ContentValues();
			cv.put("_ID", professorFromList.getId());
			cv.put("FIRSTNAME", professorFromList.getFirstName());
			cv.put("LASTNAME", professorFromList.getLastName());
			db.insert("PROFESSOR", null, cv);
		}
	}

	public List<Professor> retrieveProfessorListFromDb(Cursor result) {

		List<Professor> professorList = new ArrayList<Professor>();
		System.out.println("From db");
		result.moveToFirst();
		while (result.isAfterLast() == false) {
			Professor professorDb = new Professor();

			professorDb.setId(result.getInt(result.getColumnIndex("_ID")));

			professorDb.setLastName(result.getString(result
					.getColumnIndex("LASTNAME")));

			professorDb.setFirstName(result.getString(result
					.getColumnIndex("FIRSTNAME")));
			professorList.add(professorDb);
			result.moveToNext();
		}
		result.close();
		return professorList;
	}

}
