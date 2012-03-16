package extended.cs.sdsu.edu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.service.ProfessorService;

public class SelectedProfessorDetailsActivity extends Activity {

	private TextView firstNameText;
	private TextView lastName;
	private TextView phone;
	private TextView email;
	private TextView average;
	private TextView totalRating;
	private int selectedProfessorId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.selected_professor_details);

		firstNameText = (TextView) findViewById(R.id.firstName);
		lastName = (TextView) findViewById(R.id.lastName);
		phone = (TextView) findViewById(R.id.phone);
		email = (TextView) findViewById(R.id.email);
		average = (TextView) findViewById(R.id.average);
		totalRating = (TextView) findViewById(R.id.totalRating);

		Bundle professorId = getIntent().getExtras();
		selectedProfessorId = professorId.getInt("selectedProfessorID");
	}

	@Override
	protected void onResume() {
		ProfessorService professorDetailsService = new ProfessorService();
		Professor professorDetails = new Professor();
		try {

			// professorDetails =
			professorDetailsService.getProfessorDetails(selectedProfessorId,
					this);
			// System.out.println(professorDetails.getFirstName());
			// firstNameText.setText("JHJJ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}