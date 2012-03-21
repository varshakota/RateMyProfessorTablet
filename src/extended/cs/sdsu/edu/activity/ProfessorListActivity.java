package extended.cs.sdsu.edu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import extended.cs.sdsu.edu.domain.Professor;
import extended.cs.sdsu.edu.service.ApplicationFactory;
import extended.cs.sdsu.edu.service.ProfessorChangedListener;
import extended.cs.sdsu.edu.service.ProfessorService;

public class ProfessorListActivity extends ListActivity {

	private List<Professor> professorList = new ArrayList<Professor>();
	private ProfessorListAdapter professorListAdapter = new ProfessorListAdapter(
			professorList, this);
	private ProfessorChangedListener professorChangeListener;
	private ProfessorService professorService;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		professorListAdapter = new ProfessorListAdapter(professorList, this);
		setListAdapter(professorListAdapter);
		professorService = ApplicationFactory.getProfessorService(this);
		initListener();
	}

	private void initListener() {
		professorChangeListener = new ProfessorChangedListener() {
			@Override
			public void professorListUpdated(List<Professor> newProfessorList) {
				professorListAdapter.refreshList(newProfessorList);
				professorListAdapter.notifyDataSetChanged();
			}
		};
	}

	@Override
	protected void onStop() {
		super.onStop();
		professorService
				.removeProfessorChangedListener(professorChangeListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		professorService.addProfessorChangedListener(professorChangeListener);

		refreshProfessorList();
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		Professor professor = (Professor) listView.getItemAtPosition(position);
		int selectedProfessorId = professor.getId();

		Intent professorDetails = new Intent();
		professorDetails
				.setClassName("extended.cs.sdsu.edu.activity",
						"extended.cs.sdsu.edu.activity.SelectedProfessorDetailsActivity");
		professorDetails
				.setAction("cs.assignment.intent.action.PROFESSOR_DETAILS");
		professorDetails.putExtra("selectedProfessorID", selectedProfessorId);
		startActivity(professorDetails);
	}

	private void refreshProfessorList() {
		try {
			professorList = ApplicationFactory.getProfessorService(this)
					.getProfessorList();
			professorListAdapter.refreshList(professorList);
			professorListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("RateMyProfessorTablet", e.getMessage(), e);
		}
	}
}