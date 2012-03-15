package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import extended.cs.sdsu.edu.database.DatabaseHelper;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.network.NetworkConnection;

public class ProfessorService {
	private JSONObjectMapper jsonObjectMapper;

	public ProfessorService() {
		jsonObjectMapper = new JSONObjectMapper();
	}

	public List<Professor> getProfessorList(Context context)
			throws InterruptedException, ExecutionException {
		List<Professor> professorList = new ArrayList<Professor>();
		List<Professor> professorListData = new ArrayList<Professor>();
		String url = "http://bismarck.sdsu.edu/rateme/list";
		NetworkConnection networkConnection = new NetworkConnection();
		String responseBody = networkConnection.execute(url).get();
		if (responseBody != null) {
			JSONArray jsonProfessorArray;
			try {
				jsonProfessorArray = new JSONArray(responseBody);

				professorListData = jsonObjectMapper
						.jsonProfessorArrayToList(jsonProfessorArray);

				DatabaseHelper dbHelper = new DatabaseHelper(context);
				SQLiteDatabase db = dbHelper.getWritableDatabase();

				Cursor result = db.rawQuery("SELECT * from PROFESSOR", null);
				int rowCount = result.getCount();

				for (int i = 0; i < professorListData.size(); i++) {

					Professor professorFromList = professorListData.get(i);

					ContentValues cv = new ContentValues();
					cv.put("_ID", professorFromList.getId());
					cv.put("FIRSTNAME", professorFromList.getFirstName());
					cv.put("LASTNAME", professorFromList.getLastName());

					if (rowCount == 0) {
						db.insert("PROFESSOR", null, cv);
					}

				}

				if (rowCount > 0) {
					result.moveToFirst();
					while (result.isAfterLast() == false) {
						Professor professorDb = new Professor();

						professorDb.setId(result.getInt(result
								.getColumnIndex("_ID")));

						professorDb.setLastName(result.getString(result
								.getColumnIndex("LASTNAME")));

						professorDb.setFirstName(result.getString(result
								.getColumnIndex("FIRSTNAME")));
						professorList.add(professorDb);
						result.moveToNext();
					}

				}
				result.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return professorList;
	}

	public void getProfessorDetails() {

	}

	public void getProfessorComments() {

	}

	public void submitProfesssorRating() {

	}

	public void submitProfessorComment() {

	}
}
