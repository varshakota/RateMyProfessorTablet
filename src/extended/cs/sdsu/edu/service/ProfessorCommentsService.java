package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import extended.cs.sdsu.edu.database.DatabaseAccessor;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.SharedPreferenceWrapper;
import extended.cs.sdsu.edu.network.GETNetworkConnection;
import extended.cs.sdsu.edu.network.POSTNetworkConnection;
import extended.cs.sdsu.edu.util.ApplicationUtils;

public class ProfessorCommentsService {

	private static final String SUBMIT_COMMENT_URL = "http://bismarck.sdsu.edu/rateme/comment/";
	private static final String PROFESSOR_COMMENTS_BASE_URL = "http://bismarck.sdsu.edu/rateme/comments/";

	private JSONObjectMapper jsonObjectMapper;
	private DatabaseAccessor db;
	private SharedPreferenceWrapper sharedPreferenceWrapper;
	private List<ProfessorCommentsChangedListener> professorCommentsListener = new ArrayList<ProfessorCommentsChangedListener>();

	public ProfessorCommentsService(Context context) {
		jsonObjectMapper = new JSONObjectMapper();
		db = ApplicationFactory.getDatabaseAccessor(context);
		sharedPreferenceWrapper = new SharedPreferenceWrapper(context);
	}

	public void addProfessorCommentsChangedListener(
			ProfessorCommentsChangedListener listener) {
		professorCommentsListener.add(listener);
	}

	public void removeProfessorCommentsChangedListener(
			ProfessorCommentsChangedListener listener) {
		professorCommentsListener.remove(listener);
	}

	public void notifyProfessorCommentsChanged(List<Comment> comments) {
		for (ProfessorCommentsChangedListener professorCommentsChangedListener : professorCommentsListener) {
			professorCommentsChangedListener.professorCommentsUpdated(comments);
		}
	}

	public List<Comment> getProfessorComments(int selectedProfessorId)
			throws Exception {
		List<Comment> comments = new ArrayList<Comment>();

		if (db.isProfessorCommentsEmpty(selectedProfessorId)) {
			getCommentsFromServerAndSaveToDB(selectedProfessorId, comments);
		} else {
			comments = db.retrieveComments(selectedProfessorId);
			getUpdatedProfessorCommentsFromServer(selectedProfessorId);
		}
		return comments;
	}

	private void getCommentsFromServerAndSaveToDB(int selectedProfessorId,
			List<Comment> comments) throws InterruptedException,
			ExecutionException, JSONException {
		GETNetworkConnection networkConnection = new GETNetworkConnection();
		String url = PROFESSOR_COMMENTS_BASE_URL + selectedProfessorId;
		String responseBody = networkConnection.execute(url).get();
		JSONArray jsonArrayComments = new JSONArray(responseBody);
		comments.addAll(jsonObjectMapper.convertJsonCommentsArrayToList(
				selectedProfessorId, jsonArrayComments));
		String dateAccessed = ApplicationUtils.getCurrentDateString();
		sharedPreferenceWrapper.putString("dateAccessed", dateAccessed);
		db.saveComments(comments);
	}

	private void getUpdatedProfessorCommentsFromServer(int selectedProfessorId) {
		AsyncTask<Integer, Void, List<Comment>> asyncTask = new AsyncTask<Integer, Void, List<Comment>>() {

			private int professorId;

			@Override
			protected void onPostExecute(List<Comment> updatedComments) {
				super.onPostExecute(updatedComments);
				notifyProfessorCommentsChanged(updatedComments);
			}

			protected List<Comment> doInBackground(
					Integer... selectedProfessorId) {
				professorId = selectedProfessorId[0];
				String lastAccessDate = sharedPreferenceWrapper
						.getString("dateAccessed");
				String currentDate = ApplicationUtils.getCurrentDateString();

				List<Comment> newComments = new ArrayList<Comment>();
				HttpClient httpClient = new DefaultHttpClient();
				String responseBody = null;

				try {
					if (lastAccessDate.equals(currentDate)) {
						String sinceCommentsIdUrl = PROFESSOR_COMMENTS_BASE_URL
								+ professorId + "/since/"
								+ db.getLatestCommentsId(professorId);

						responseBody = executeRequest(httpClient,
								sinceCommentsIdUrl);

						handleGetCommentsResponse(newComments, responseBody);
					} else {
						String commentsSinceDate = PROFESSOR_COMMENTS_BASE_URL
								+ professorId + "/since/" + lastAccessDate;

						responseBody = executeRequest(httpClient,
								commentsSinceDate);

						sharedPreferenceWrapper.putString("dateAccessed",
								currentDate);

						handleGetCommentsResponse(newComments, responseBody);
					}
				} catch (Exception e) {
					Log.e("RateMyProfessorTablet", e.getMessage(), e);
				} finally {
					httpClient.getConnectionManager().shutdown();
				}
				return db.retrieveComments(professorId);
			}

			private String executeRequest(HttpClient httpClient, String restUrl)
					throws Exception {
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				HttpGet httpGet = new HttpGet(restUrl);
				String responseBody = httpClient.execute(httpGet,
						responseHandler);
				return responseBody;
			}

			private void handleGetCommentsResponse(List<Comment> newComments,
					String responseBody) throws JSONException {

				if (ApplicationUtils.isResponseBodyEmpty(responseBody)) {
					newComments = db.retrieveComments(professorId);
				} else {
					JSONArray jsonArrayComments = new JSONArray(responseBody);
					newComments.addAll(jsonObjectMapper
							.convertJsonCommentsArrayToList(professorId,
									jsonArrayComments));
					db.saveComments(newComments);
				}
			}
		};
		asyncTask.execute(selectedProfessorId);
	}

	public int submitProfessorComments(int selectedProfessorId,
			String professorComments) throws InterruptedException,
			ExecutionException {
		String url = SUBMIT_COMMENT_URL + selectedProfessorId;
		POSTNetworkConnection netowrkConnection = new POSTNetworkConnection();
		HttpResponse httpResponse = netowrkConnection.execute(url,
				professorComments).get();
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		return statusCode;
	}
}
