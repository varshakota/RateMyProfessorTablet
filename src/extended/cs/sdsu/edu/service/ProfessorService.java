package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import extended.cs.sdsu.edu.database.DatabaseAccessor;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.domain.SharedPreferenceWrapper;
import extended.cs.sdsu.edu.network.GETNetworkConnection;
import extended.cs.sdsu.edu.network.POSTNetworkConnection;

public class ProfessorService {
	private JSONObjectMapper jsonObjectMapper;
	private DatabaseAccessor db;
	private Professor professorDetails;
	private SharedPreferenceWrapper sharedPreferenceWrapper;

	public ProfessorService(Context context) {
		jsonObjectMapper = new JSONObjectMapper();
		db = ApplicationFactory.getDatabaseAccessor(context);
		sharedPreferenceWrapper = new SharedPreferenceWrapper(context);
	}

	public List<Professor> getProfessorList() throws InterruptedException,
			ExecutionException, JSONException {

		List<Professor> professorListData = new ArrayList<Professor>();
		List<Professor> newProfessorListData = new ArrayList<Professor>();
		String dateAccessed = "";
		GETNetworkConnection networkConnection = new GETNetworkConnection();

		if (db.isProfessorTableEmpty()) {
			String url = "http://bismarck.sdsu.edu/rateme/list";
			dateAccessed = getCurrentDateString();
			sharedPreferenceWrapper.putString("dateAccessed", dateAccessed);
			// GETNetworkConnection networkConnection = new
			// GETNetworkConnection();
			String responseBody = networkConnection.execute(url).get();
			if (responseBody != null) {
				JSONArray jsonProfessorArray;
				jsonProfessorArray = new JSONArray(responseBody);
				professorListData = jsonObjectMapper
						.convertJsonProfessorArrayToList(jsonProfessorArray);
				db.createProfessors(professorListData);
			}
		} else {
			professorListData = db.retrieveProfessors();
			dateAccessed = sharedPreferenceWrapper.getString("dateAccessed");
			String url = "http://bismarck.sdsu.edu/rateme/list/since/"
					+ dateAccessed;
			String responseBody = networkConnection.execute(url).get();
			if (responseBody != null) {
				JSONArray jsonProfessorArray;
				jsonProfessorArray = new JSONArray(responseBody);
				newProfessorListData = jsonObjectMapper
						.convertJsonProfessorArrayToList(jsonProfessorArray);
				db.updateProfessor(newProfessorListData);
				professorListData = db.retrieveProfessors();
				// professorListData.addAll(newProfessorListData);
			}
		}
		return professorListData;
	}

	public Professor getProfessorDetails(int selectedProfessorId)
			throws InterruptedException, ExecutionException, JSONException {
		professorDetails = new Professor();
		if (db.isProfessorDetailsEmpty(selectedProfessorId)) {
			String url = "http://bismarck.sdsu.edu/rateme/instructor/"
					+ selectedProfessorId;
			GETNetworkConnection networkConnection = new GETNetworkConnection();
			String responseBody = networkConnection.execute(url).get();
			JSONObject jsonProfessorDetails = new JSONObject(responseBody);
			professorDetails = jsonObjectMapper
					.covertJsonObjectToProfessor(jsonProfessorDetails);
			professorDetails.setId(selectedProfessorId);
			db.addProfessorDetails(professorDetails);
		} else {
			professorDetails = db.retrieveProfessorDetails(selectedProfessorId);
		}
		return professorDetails;
	}

	public List<Comment> getProfessorComments(int selectedProfessorId)
			throws InterruptedException, ExecutionException, JSONException {
		List<Comment> comments = new ArrayList<Comment>();
		List<Comment> newComments = new ArrayList<Comment>();
		JSONObjectMapper jsonObjectmapper = new JSONObjectMapper();
		String dateAccessed = "";
		GETNetworkConnection networkConnection = new GETNetworkConnection();

		if (db.isProfessorCommentsEmpty(selectedProfessorId)) {
			String url = "http://bismarck.sdsu.edu/rateme/comments/"
					+ selectedProfessorId;
			String responseBody = networkConnection.execute(url).get();
			JSONArray jsonArrayComments = new JSONArray(responseBody);
			comments.addAll(jsonObjectmapper.convertJsonCommentsArrayToList(
					selectedProfessorId, jsonArrayComments));
			dateAccessed = getCurrentDateString();
			sharedPreferenceWrapper.putString("dateAccessed", dateAccessed);
			db.addComments(comments);
			System.out.println("from server insert to db");
		} else {
			comments = db.retrieveComments(selectedProfessorId);
			String date = sharedPreferenceWrapper.getString("dateAccessed");
			if (!(date.equals(getCurrentDateString()))) {
				String url = "http://bismarck.sdsu.edu/rateme/comments/"
						+ selectedProfessorId + "/since/" + date;
				// GETNetworkConnection networkConnection = new
				// GETNetworkConnection();
				String responseBody = networkConnection.execute(url).get();
				if (responseBody != null) {
					dateAccessed = getCurrentDateString();
					sharedPreferenceWrapper.putString("dateAccessed",
							dateAccessed);
					JSONArray jsonArrayComments = new JSONArray(responseBody);
					newComments.addAll(jsonObjectmapper
							.convertJsonCommentsArrayToList(
									selectedProfessorId, jsonArrayComments));
					db.addComments(newComments);
					comments.addAll(newComments);
				} else {
					newComments = comments;
				}

			} else {
				dateAccessed = getCurrentDateString();
				sharedPreferenceWrapper.putString("dateAccessed", dateAccessed);
			}
		}
		return comments;
	}

	public int submitProfessorComments(int selectedProfessorId,
			String professorComments) throws InterruptedException,
			ExecutionException {
		String url = "http://bismarck.sdsu.edu/rateme/comment/"
				+ selectedProfessorId;
		POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
		HttpResponse httpResponse = netowrkConnection.execute(url,
				professorComments).get();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		return statusCode;
	}

	public int submitProfessorRating(int selectedProfessorId, String rating)
			throws InterruptedException, ExecutionException {
		String url = "http://bismarck.sdsu.edu/rateme/rating/"
				+ selectedProfessorId + "/" + rating;
		POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
		HttpResponse httpResponse = netowrkConnection.execute(url, rating)
				.get();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		return statusCode;
	}

	public Professor getProfessorRating(int selectedProfessorId, String rating)
			throws InterruptedException, ExecutionException, JSONException {
		String url = "http://bismarck.sdsu.edu/rateme/rating/"
				+ selectedProfessorId + "/" + rating;
		GETNetworkConnection networkConnection = new GETNetworkConnection();
		String responseBody = networkConnection.execute(url).get();
		JSONObject jsonObject = new JSONObject(responseBody);
		Professor professorDetails = new Professor();
		professorDetails = jsonObjectMapper.getRating(jsonObject);
		return professorDetails;
	}

	private String getCurrentDateString() {
		Calendar date = Calendar.getInstance();
		int day = date.get(Calendar.DATE);
		int month = (date.get(Calendar.MONTH)) + 1;
		int year = date.get(Calendar.YEAR);
		String dateString = month + "-" + day + "-" + year;
		return dateString;
	}
}
