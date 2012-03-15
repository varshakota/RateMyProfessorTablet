package extended.cs.sdsu.edu.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import android.content.Context;
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
		String url = "http://bismarck.sdsu.edu/rateme/list";
		NetworkConnection networkConnection = new NetworkConnection();
		String responseBody = networkConnection.execute(url).get();
		if (responseBody != null) {
			JSONArray jsonProfessorArray;
			try {
				jsonProfessorArray = new JSONArray(responseBody);
				professorList.addAll(jsonObjectMapper.jsonProfessorArrayToList(
						context, jsonProfessorArray));

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
