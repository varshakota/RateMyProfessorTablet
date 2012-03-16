package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import extended.cs.sdsu.edu.database.DatabaseAccessor;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.network.NetworkConnection;

public class ProfessorService {
	private JSONObjectMapper jsonObjectMapper;
	private DatabaseAccessor databaseAccessor = new DatabaseAccessor();
	private List<Professor> professorList = new ArrayList<Professor>();

	public ProfessorService() {
		jsonObjectMapper = new JSONObjectMapper();
	}

	public List<Professor> getProfessorList(Context context)
			throws InterruptedException, ExecutionException, JSONException {

		List<Professor> professorListData = new ArrayList<Professor>();

		// DatabaseHelper dbHelper = new DatabaseHelper(context);
		// SQLiteDatabase db = dbHelper.getWritableDatabase();

		// Cursor result = db.rawQuery("SELECT * from PROFESSOR", null);
		Cursor result = databaseAccessor.selectQuery(context);
		int rowCount = result.getCount();

		if (rowCount == 0) {
			System.out.println("From server insert to db");
			String url = "http://bismarck.sdsu.edu/rateme/list";
			NetworkConnection networkConnection = new NetworkConnection();
			String responseBody = networkConnection.execute(url).get();

			if (responseBody != null) {
				JSONArray jsonProfessorArray;

				jsonProfessorArray = new JSONArray(responseBody);

				professorListData = jsonObjectMapper
						.jsonProfessorArrayToList(jsonProfessorArray);
				databaseAccessor.insertProfessorListToDb(professorListData,
						context);

				// Cursor res = db.rawQuery("SELECT * from PROFESSOR", null);
				Cursor res = databaseAccessor.selectQuery(context);
				professorList = databaseAccessor
						.retrieveProfessorListFromDb(res);
			}
		} else {
			professorList = databaseAccessor
					.retrieveProfessorListFromDb(result);
		}

		return professorList;
	}

	/*
	 * public List<Professor> getModifiedProfessorList(Context context) throws
	 * InterruptedException, ExecutionException, JSONException { List<Professor>
	 * modifiedProfessorList = new ArrayList<Professor>(); DatabaseHelper
	 * dbHelper = new DatabaseHelper(context); SQLiteDatabase db =
	 * dbHelper.getWritableDatabase();
	 * 
	 * Calendar currentDate = Calendar.getInstance(); int currentMonth =
	 * currentDate.get(Calendar.MONTH) + 1; int currentDay =
	 * currentDate.get(Calendar.DATE); int currentYear =
	 * currentDate.get(Calendar.YEAR); System.out.println(currentDate + "" +
	 * currentMonth + "" + currentDay + "" + currentYear); // String urlModified
	 * = "http://bismark.sdsu.edu/rateme/list/since/" // + currentMonth + "-" +
	 * currentDay + "-" + currentYear; String urlModified =
	 * "http://bismarck.sdsu.edu/rateme/list/since/3-15-2012"; NetworkConnection
	 * networkConnection = new NetworkConnection(); String responseBodyModified
	 * = networkConnection.execute(urlModified) .get(); if (responseBodyModified
	 * != null) { JSONArray jsonProfessorArray = new
	 * JSONArray(responseBodyModified);
	 * 
	 * modifiedProfessorList = jsonObjectMapper
	 * .jsonProfessorArrayToList(jsonProfessorArray);
	 * databaseAccessor.insertToDb(modifiedProfessorList, db);
	 * 
	 * Cursor result = db.rawQuery("SELECT * from PROFESSOR", null);
	 * professorList = databaseAccessor.retrieveFromDb(result); } return
	 * modifiedProfessorList; }
	 */

	public void getProfessorDetails(int selectedProfessorId)
			throws InterruptedException, ExecutionException, JSONException {

		String url = "http://bismarck.sdsu.edu/rateme/instructor/"
				+ selectedProfessorId;
		NetworkConnection networkConnection = new NetworkConnection();
		String responseBody = networkConnection.execute(url).get();
		JSONObject jsonProfessorDetails = new JSONObject(responseBody);
		Professor professorDetails = jsonObjectMapper
				.covertJsonObjectToProfessor(jsonProfessorDetails);
		System.out.println(professorDetails);
	}

	public void getProfessorComments() {

	}

	public void submitProfesssorRating() {

	}

	public void submitProfessorComment() {

	}

}
