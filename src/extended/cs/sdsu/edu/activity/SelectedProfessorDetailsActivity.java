package extended.cs.sdsu.edu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import extended.cs.sdsu.edu.domain.Professor;
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

	}

	@Override
	protected void onResume() {
		super.onResume();
		ProfessorService professorDetailsService = new ProfessorService();
		Professor professorDetails = new Professor();
		try {

			professorDetails = professorDetailsService.getProfessorDetails(
					selectedProfessorId, this);
			firstNameTextView.setText(professorDetails.getFirstName());
			lastNameTextView.setText(professorDetails.getLastName());
			officeTextView.setText(professorDetails.getOffice());
			phoneTextView.setText(professorDetails.getPhone());
			emailTextView.setText(professorDetails.getEmail());
			averageTextView.setText(String.valueOf(professorDetails
					.getAverage()));
			totalRatingTextView.setText(String.valueOf(professorDetails
					.getTotalRatings()));
			// System.out.println(professorDetails.getFirstName());
			// firstNameText.setText("JHJJ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			Intent professorComments = new Intent();
			professorComments
					.setClassName("extended.cs.sdsu.edu.activity",
							"extended.cs.sdsu.edu.activity.ViewProfessorCommentsActivity");
			professorComments
					.setAction("cs.assignment.intent.action.PROFESSOR_COMMENTS");
			startActivity(professorComments);
			// return true;

		case R.id.menu_rate:
			Toast.makeText(this, "Rate", Toast.LENGTH_SHORT).show();
			return true;

		}
		return true;
	}
}