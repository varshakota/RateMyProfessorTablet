package extended.cs.sdsu.edu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.service.ApplicationFactory;
import extended.cs.sdsu.edu.service.ProfessorCommentsService;

public class ViewProfessorCommentsActivity extends ListActivity {
	private int selectedProfessorId;
	private ProfessorCommentsService professorCommentsService;
	private CommentsListAdapter commentsListAdapter;
	private List<Comment> professorCommentsList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.professor_comments_list);
		Bundle bundleProfessorId = getIntent().getExtras();
		selectedProfessorId = bundleProfessorId.getInt("selectedProfessorID");

		professorCommentsService = ApplicationFactory.getProfessorCommentsService(this);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshProfessorComments();
	}

	private void refreshProfessorComments() {
		try {
			professorCommentsList = new ArrayList<Comment>();
			professorCommentsList = professorCommentsService
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
