package extended.cs.sdsu.edu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
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
}