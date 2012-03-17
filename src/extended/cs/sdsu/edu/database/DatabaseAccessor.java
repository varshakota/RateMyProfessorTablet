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
			cv.put("ID", professorFromList.getId());
			cv.put("firstname", professorFromList.getFirstName());
			cv.put("lastname", professorFromList.getLastName());
			db.insert("PROFESSOR", null, cv);
		}
	}

	public List<Professor> retrieveProfessorListFromDb(Cursor result) {

		List<Professor> professorList = new ArrayList<Professor>();
		System.out.println("From db");
		result.moveToFirst();
		while (result.isAfterLast() == false) {
			Professor professorDb = new Professor();

			professorDb.setId(result.getInt(result.getColumnIndex("ID")));

			professorDb.setLastName(result.getString(result
					.getColumnIndex("lastname")));

			professorDb.setFirstName(result.getString(result
					.getColumnIndex("firstname")));
			professorList.add(professorDb);
			result.moveToNext();
		}
		result.close();
		return professorList;
	}

	public void insertProfessorDetails(int selectedProfessorId,
			Professor professorDetails, Context context) {
		System.out.println("In dbAccessor insert details");
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		ContentValues cvProfessorDetails = new ContentValues();
		cvProfessorDetails.put("ID", selectedProfessorId);
		cvProfessorDetails.put("office", professorDetails.getOffice());
		cvProfessorDetails.put("phone", professorDetails.getPhone());
		cvProfessorDetails.put("email", professorDetails.getEmail());
		cvProfessorDetails.put("average", professorDetails.getAverage());
		cvProfessorDetails.put("totalrating",
				professorDetails.getTotalRatings());

		db.insert("PROFESSOR_DETAILS", null, cvProfessorDetails);

	}

	public Cursor selectProfessorDetails(Context context,
			int selectedProfessorId) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor result = db
				.rawQuery(
						"SELECT office,phone,email,average,totalrating from PROFESSOR_DETAILS WHERE ID=?",
						new String[] { String.valueOf(selectedProfessorId) });
		return result;
	}

	public Professor retrieveProfessorDetailsFromDb(Cursor result) {
		Professor professorDetailsFromDb = new Professor();
		result.moveToFirst();

		professorDetailsFromDb.setOffice(result.getString(result
				.getColumnIndex("office")));
		professorDetailsFromDb.setPhone(result.getString(result
				.getColumnIndex("phone")));
		professorDetailsFromDb.setEmail(result.getString(result
				.getColumnIndex("email")));
		professorDetailsFromDb.setAverage(result.getFloat(result
				.getColumnIndex("average")));
		professorDetailsFromDb.setTotalRatings(result.getInt(result
				.getColumnIndex("totalrating")));
		result.close();
		// retrieveProfessorIdNameFromDb(result, professorDetailsFromDb);
		return professorDetailsFromDb;
	}

	public Professor retrieveProfessorNameIdFromDb(Context context,
			int selectedProfessorId) {
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Professor nameIDFromDb = new Professor();
		Cursor result = db.rawQuery("SELECT * from PROFESSOR WHERE ID=?",
				new String[] { String.valueOf(selectedProfessorId) });
		result.moveToFirst();
		nameIDFromDb.setId(result.getInt(result.getColumnIndex("ID")));
		nameIDFromDb.setLastName(result.getString(result
				.getColumnIndex("lastname")));

		nameIDFromDb.setFirstName(result.getString(result
				.getColumnIndex("firstname")));
		result.close();
		return nameIDFromDb;
	}
}
