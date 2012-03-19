package extended.cs.sdsu.edu.domain;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONObjectMapper {

	public List<Professor> convertJsonProfessorArrayToList(JSONArray jsonArray) {
		List<Professor> professorList = new ArrayList<Professor>();
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject professorJsonObject = (JSONObject) jsonArray.get(i);
				Professor professor = covertJsonObjectToProfessor(professorJsonObject);
				professorList.add(professor);
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

	public List<Comment> convertJsonCommentsArrayToList(int selectedProfessorId,
			JSONArray jsonArrayComments) throws JSONException {
		JSONObject professorCommentsJson = new JSONObject();
		List<Comment> professorCommentsList = new ArrayList<Comment>();
		Comment professorComments = null;
		for (int i = 0; i < jsonArrayComments.length(); i++) {
			professorComments = new Comment();
			professorCommentsJson = jsonArrayComments.getJSONObject(i);
			professorComments.setCommentsId(professorCommentsJson.getInt("id"));
			professorComments.setText(professorCommentsJson.getString("text"));
			professorComments.setDate(professorCommentsJson.getString("date"));
			professorComments.setProfessorId(selectedProfessorId);
			professorCommentsList.add(professorComments);
		}
		return professorCommentsList;
	}

	public Professor getRating(JSONObject jsonRating)
			throws NumberFormatException, JSONException {
		Professor professorRating = new Professor();
		Float fAverage = new Float(jsonRating.getString("average"));
		professorRating.setAverage(fAverage);
		Integer totalRatings = new Integer(jsonRating.getString("totalRatings"));
		professorRating.setTotalRatings(totalRatings);
		return professorRating;

	}
}
