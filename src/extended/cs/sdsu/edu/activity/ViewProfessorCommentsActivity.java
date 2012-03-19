package extended.cs.sdsu.edu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.service.ProfessorService;
import extended.cs.sdsu.edu.service.ApplicationFactory;

public class ViewProfessorCommentsActivity extends ListActivity {
	private int selectedProfessorId;
	private ProfessorService professorService;
	private CommentsListAdapter commentsListAdapter;
	private List<Comment> professorCommentsList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.professor_comments_list);
		Bundle bundleProfessorId = getIntent().getExtras();
		selectedProfessorId = bundleProfessorId.getInt("selectedProfessorID");

		professorService = ApplicationFactory.getProfessorService(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshProfessorComments();
	}

	private void refreshProfessorComments() {
		try {
			professorCommentsList = new ArrayList<Comment>();
			professorCommentsList = professorService
					.getProfessorComments(selectedProfessorId);
			commentsListAdapter = new CommentsListAdapter(
					professorCommentsList, this);
			setListAdapter(commentsListAdapter);
			commentsListAdapter.refreshList(professorCommentsList);
			commentsListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("RateMyProfessorTablet", e.getMessage(), e);
		}
	}
}
