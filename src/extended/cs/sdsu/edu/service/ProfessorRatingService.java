package extended.cs.sdsu.edu.service;

import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import extended.cs.sdsu.edu.domain.JSONObjectMapper;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.network.GETNetworkConnection;
import extended.cs.sdsu.edu.network.POSTNetworkConnection;

public class ProfessorRatingService {

	private JSONObjectMapper jsonObjectMapper;

	public ProfessorRatingService(Context context) {
		jsonObjectMapper = new JSONObjectMapper();
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
}
