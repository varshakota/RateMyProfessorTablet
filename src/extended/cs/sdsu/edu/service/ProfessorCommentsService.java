package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import extended.cs.sdsu.edu.database.DatabaseAccessor;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.SharedPreferenceWrapper;
import extended.cs.sdsu.edu.network.GETNetworkConnection;
import extended.cs.sdsu.edu.network.POSTNetworkConnection;
import extended.cs.sdsu.edu.util.ApplicationUtils;

public class ProfessorCommentsService {

	private JSONObjectMapper jsonObjectMapper;
	private DatabaseAccessor db;
	private SharedPreferenceWrapper sharedPreferenceWrapper;

	public ProfessorCommentsService(Context context) {
		jsonObjectMapper = new JSONObjectMapper();
		db = ApplicationFactory.getDatabaseAccessor(context);
		sharedPreferenceWrapper = new SharedPreferenceWrapper(context);
	}

	public List<Comment> getProfessorComments(int selectedProfessorId)
			throws InterruptedException, ExecutionException, JSONException {
		List<Comment> comments = new ArrayList<Comment>();
		List<Comment> newComments = new ArrayList<Comment>();
		String dateAccessed = "";
		GETNetworkConnection networkConnection = new GETNetworkConnection();

		if (db.isProfessorCommentsEmpty(selectedProfessorId)) {
			String url = "http://bismarck.sdsu.edu/rateme/comments/"
					+ selectedProfessorId;
			String responseBody = networkConnection.execute(url).get();
			JSONArray jsonArrayComments = new JSONArray(responseBody);
			comments.addAll(jsonObjectMapper.convertJsonCommentsArrayToList(
					selectedProfessorId, jsonArrayComments));
			dateAccessed = ApplicationUtils.getCurrentDateString();
			sharedPreferenceWrapper.putString("dateAccessed", dateAccessed);
			db.addComments(comments);
			System.out.println("from server insert to db");
		} else {
			comments = db.retrieveComments(selectedProfessorId);
			String date = sharedPreferenceWrapper.getString("dateAccessed");
			if (!(date.equals(ApplicationUtils.getCurrentDateString()))) {
				String url = "http://bismarck.sdsu.edu/rateme/comments/"
						+ selectedProfessorId + "/since/" + date;
				// GETNetworkConnection networkConnection = new
				// GETNetworkConnection();
				String responseBody = networkConnection.execute(url).get();
				if (responseBody != null) {
					dateAccessed = ApplicationUtils.getCurrentDateString();
					sharedPreferenceWrapper.putString("dateAccessed",
							dateAccessed);
					JSONArray jsonArrayComments = new JSONArray(responseBody);
					newComments.addAll(jsonObjectMapper
							.convertJsonCommentsArrayToList(
									selectedProfessorId, jsonArrayComments));
					db.addComments(newComments);
					comments.addAll(newComments);
				} else {
					newComments = comments;
				}

			} else {
				dateAccessed = ApplicationUtils.getCurrentDateString();
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
}
