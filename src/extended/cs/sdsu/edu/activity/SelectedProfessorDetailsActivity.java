package extended.cs.sdsu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.service.ApplicationFactory;
import extended.cs.sdsu.edu.service.ProfessorService;

public class SelectedProfessorDetailsActivity extends Activity {

	private TextView firstNameTextView;
	private TextView lastNameTextView;
	private TextView officeTextView;
	private TextView phoneTextView;
	private TextView emailTextView;
	private TextView averageTextView;
	private TextView totalRatingTextView;
	private int selectedProfessorId;
	private ProfessorService professorDetailsService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_professor_details);

		firstNameTextView = (TextView) findViewById(R.id.firstName);
		lastNameTextView = (TextView) findViewById(R.id.lastName);
		officeTextView = (TextView) findViewById(R.id.office);
		phoneTextView = (TextView) findViewById(R.id.phone);
		emailTextView = (TextView) findViewById(R.id.email);
		averageTextView = (TextView) findViewById(R.id.average);
		totalRatingTextView = (TextView) findViewById(R.id.totalRating);

		Bundle professorId = getIntent().getExtras();
		selectedProfessorId = professorId.getInt("selectedProfessorID");
		professorDetailsService = ApplicationFactory.getProfessorService(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_action, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_comments:
			Toast.makeText(this, "Comments", Toast.LENGTH_SHORT).show();
			Intent professorCommentsIntent = new Intent();
			professorCommentsIntent
					.setClassName("extended.cs.sdsu.edu.activity",
							"extended.cs.sdsu.edu.activity.ViewProfessorCommentsActivity");
			professorCommentsIntent
					.setAction("cs.assignment.intent.action.PROFESSOR_COMMENTS");
			professorCommentsIntent.putExtra("selectedProfessorID",
					selectedProfessorId);
			startActivity(professorCommentsIntent);
			// finish();
			return true;

		case R.id.menu_rate:
			Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show();
			Intent rateProfessorIntent = new Intent();
			rateProfessorIntent.setClassName("extended.cs.sdsu.edu.activity",
					"extended.cs.sdsu.edu.activity.RateProfessorActivity");
			rateProfessorIntent
					.setAction("cs.assignment.intent.action.RATE_PROFESSOR");
			rateProfessorIntent.putExtra("selectedProfessorID",
					selectedProfessorId);
			startActivity(rateProfessorIntent);
			// finish();
			return true;
		}
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		displayProfessorDetails();

	}

	private void displayProfessorDetails() {
		Professor professorDetails = new Professor();
		try {

			professorDetails = professorDetailsService
					.getProfessorDetails(selectedProfessorId);
			firstNameTextView.setText(professorDetails.getFirstName());
			lastNameTextView.setText(professorDetails.getLastName());
			officeTextView.setText(professorDetails.getOffice());
			phoneTextView.setText(professorDetails.getPhone());
			emailTextView.setText(professorDetails.getEmail());
			averageTextView.setText(String.valueOf(professorDetails
					.getAverage()));
			totalRatingTextView.setText(String.valueOf(professorDetails
					.getTotalRatings()));
		} catch (Exception e) {
			Log.e("RateMyProfessorTablet", e.getMessage(), e);
		}

	}

}