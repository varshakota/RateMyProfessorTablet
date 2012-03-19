package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
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
import extended.cs.sdsu.edu.network.GETNetworkConnection;
import extended.cs.sdsu.edu.network.POSTNetworkConnection;

public class ProfessorService {
	private JSONObjectMapper jsonObjectMapper;
	private DatabaseAccessor db;
	private List<Professor> professorListData;
	private Professor professorDetails;

	public ProfessorService(Context context) {
		jsonObjectMapper = new JSONObjectMapper();
		db = ApplicationFactory.getDatabaseAccessor(context);
	}

	public List<Professor> getProfessorList() throws InterruptedException,
			ExecutionException, JSONException {

		professorListData = new ArrayList<Professor>();
		if (db.isProfessorTableEmpty()) {
			String url = "http://bismarck.sdsu.edu/rateme/list";
			GETNetworkConnection networkConnection = new GETNetworkConnection();
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
		if (db.isProfessorCommentsEmpty(selectedProfessorId)) {
			String url = "http://bismarck.sdsu.edu/rateme/comments/"
					+ selectedProfessorId;
			GETNetworkConnection networkConnection = new GETNetworkConnection();
			String responseBody = networkConnection.execute(url).get();
			JSONArray jsonArrayComments = new JSONArray(responseBody);
			JSONObjectMapper jsonObjectmapper = new JSONObjectMapper();
			comments.addAll(jsonObjectmapper.convertJsonCommentsArrayToList(
					selectedProfessorId, jsonArrayComments));
			db.addComments(comments);
		} else {
			comments = db.retrieveComments(selectedProfessorId);
		}
		return comments;
	}

	public HttpResponse submitProfessorComments(int selectedProfessorId,
			String professorComments) throws InterruptedException,
			ExecutionException {
		String url = "http://bismarck.sdsu.edu/rateme/comment/"
				+ selectedProfessorId;
		POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
		HttpResponse httpResponse = netowrkConnection.execute(url,
				professorComments).get();
		int responseCode = httpResponse.getStatusLine().getStatusCode();
		// if (responseCode == 200) {
		// databaseAccessor.insertProfessorCommentsToDb(context,
		// selectedProfessorId, professorComments);
		// }
		return httpResponse;
	}

	public HttpResponse submitProfessorRating(int selectedProfessorId,
			String rating) throws InterruptedException, ExecutionException {
		String url = "http://bismarck.sdsu.edu/rateme/rating/"
				+ selectedProfessorId + "/" + rating;
		POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
		HttpResponse httpResponse = netowrkConnection.execute(url, rating)
				.get();
		return httpResponse;
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

}
