package extended.cs.sdsu.edu.activity;

import org.apache.http.HttpResponse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.service.ProfessorService;
import extended.cs.sdsu.edu.service.ApplicationFactory;

public class RateProfessorActivity extends Activity {

	private RatingBar ratingBar;
	private TextView commentsText;
	private ProfessorService professorService;
	private TextView averageTextView;
	private TextView totalRatingTextView;
	private AlertDialog.Builder builder;
	private LayoutInflater inflater;
	private View layout;
	private AlertDialog alertDialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rate_professor);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		commentsText = (EditText) findViewById(R.id.commentsText);
		professorService = ApplicationFactory.getProfessorService(this);
		builder = new AlertDialog.Builder(this);
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		layout = inflater.inflate(R.layout.success_page, null);
		averageTextView = (TextView) layout.findViewById(R.id.averageSuccess);
		totalRatingTextView = (TextView) layout
				.findViewById(R.id.totalRatingsSuccess);

	}

	public void submitRatingComments(View click) {
		try {
			Bundle details = getIntent().getExtras();
			int selectedProfessorId = details.getInt("selectedProfessorID");
			int iRating = (int) ratingBar.getRating();

			String rating = new Integer(iRating).toString();
			String comments = commentsText.getText().toString();

			if (rating.equals("0") && (comments.equals(""))) {
				Toast.makeText(this, "Enter rating and comments",
						Toast.LENGTH_SHORT).show();
			} else if (rating.equals("0") && (!comments.equals(""))) {
				Toast.makeText(this, "Enter a rating", Toast.LENGTH_SHORT)
						.show();
			} else {
				HttpResponse professorCommentsResponse = professorService
						.submitProfessorComments(selectedProfessorId, comments);
				HttpResponse professorRatingResponse = professorService
						.submitProfessorRating(selectedProfessorId, rating);

				if ((professorCommentsResponse.getStatusLine().getStatusCode()) == 200
						&& (professorRatingResponse.getStatusLine()
								.getStatusCode()) == 200) {
					Professor professorWithNewRatings = professorService
							.getProfessorRating(selectedProfessorId, rating);
					Float average = professorWithNewRatings.getAverage();

					Integer totalrating = new Integer(
							professorWithNewRatings.getTotalRatings());

					averageTextView.setText(average.toString());
					totalRatingTextView.setText(totalrating.toString());
					builder.setView(layout);
					builder.setTitle("Comments and Rating submitted successfully!!");
					builder.setCancelable(false);
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									finish();
									Intent professorListActivity = new Intent();
									professorListActivity
											.setClassName(
													"extended.cs.sdsu.edu",
													"extended.cs.sdsu.edu.ProfessorListActivity");
									professorListActivity
											.setAction("cs.assignment.intent.action.LIST_PROFESSORS");
									startActivity(professorListActivity);
								}
							});
					alertDialog = builder.create();
					alertDialog.show();

				}
				if ((professorCommentsResponse.getStatusLine().getStatusCode()) == 501
						&& (professorRatingResponse.getStatusLine()
								.getStatusCode() == 501)) {
					Toast.makeText(
							this,
							"Sorry unable to connect to the network. Try again later.",
							Toast.LENGTH_SHORT).show();
				}

			}
		} catch (Exception e) {
			Toast.makeText(this,
					"Sorry for the inconvenience. Try again later.",
					Toast.LENGTH_SHORT).show();
			Log.e("RateMyProfessorTablet", e.getMessage(), e);
		}
	}
}
