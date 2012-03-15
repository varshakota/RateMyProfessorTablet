package extended.cs.sdsu.edu.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import extended.cs.sdsu.edu.database.DatabaseHelper;

public class JSONObjectMapper {

	public List<Professor> jsonProfessorArrayToList(Context context,
			JSONArray jsonArray) {
		List<Professor> professorList = new ArrayList<Professor>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject professorJsonObject = (JSONObject) jsonArray.get(i);
				Professor professor = covertJsonObjectToProfessor(professorJsonObject);

				DatabaseHelper dbHelper = new DatabaseHelper(context);
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				Cursor result = db.rawQuery(
						"SELECT * from PROFESSOR WHERE _ID=?",
						new String[] { String.valueOf(professor.getId()) });
				int rowCount = result.getCount();

				ContentValues cv = new ContentValues();
				cv.put("_ID", professor.getId());
				cv.put("FIRSTNAME", professor.getFirstName());
				cv.put("LASTNAME", professor.getLastName());

				if (rowCount == 0) {
					db.insert("PROFESSOR", null, cv);
				}

				if (rowCount > 0) {
					result.moveToFirst();
					// int columnCount = result.getColumnCount();

					while (result.isAfterLast() == false) {
						Professor professorDb = new Professor();

						professorDb.setId(result.getInt(result
								.getColumnIndex("_ID")));

						professorDb.setLastName(result.getString(result
								.getColumnIndex("LASTNAME")));

						professorDb.setFirstName(result.getString(result
								.getColumnIndex("FIRSTNAME")));

						/*
						 * for (int column = 0; column < columnCount; column++)
						 * { Professor professorDb = new Professor(); if (column
						 * == 0) { professorDb.setId(result.getInt(column)); }
						 * if (column == 1) { professorDb.setLastName(result
						 * .getString(column)); } if (column == 2) {
						 * professorDb.setFirstName(result .getString(column));
						 * } // System.out.println(result.getString(column)); }
						 */
						professorList.add(professorDb);
						result.moveToNext();
					}
				}
				result.close();

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return professorList;
	}

	public Professor covertJsonObjectToProfessor(JSONObject professorJsonObject)
			throws JSONException {
		Professor professor = new Professor();
		if (professorJsonObject.getInt("id") != 0) {
			professor.setId(professorJsonObject.getInt("id"));
		}
		if (!professorJsonObject.isNull("firstName")) {
			professor.setFirstName(professorJsonObject.getString("firstName"));
		}
		if (!professorJsonObject.isNull("lastName")) {
			professor.setLastName(professorJsonObject.getString("lastName"));
		}
		if (!professorJsonObject.isNull("office")) {
			professor.setOffice(professorJsonObject.getString("office"));
		}
		if (!professorJsonObject.isNull("phone")) {
			professor.setPhone(professorJsonObject.getString("phone"));
		}
		if (!professorJsonObject.isNull("email")) {
			professor.setEmail(professorJsonObject.getString("email"));
		}
		if (!professorJsonObject.isNull("rating")) {
			JSONObject professorSelectedRemarks = professorJsonObject
					.getJSONObject("rating");
			Float floatAverage = new Float(
					professorSelectedRemarks.getString("average"));
			professor.setAverage(floatAverage);
			Integer intTotalRatings = new Integer(
					professorSelectedRemarks.getString("totalRatings"));
			professor.setTotalRatings(intTotalRatings);
		}
		return professor;
	}
}
