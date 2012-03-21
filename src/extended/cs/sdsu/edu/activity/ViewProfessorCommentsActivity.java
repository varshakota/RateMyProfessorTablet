package extended.cs.sdsu.edu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import extended.cs.sdsu.edu.domain.Comment;
import extended.cs.sdsu.edu.service.ApplicationFactory;
import extended.cs.sdsu.edu.service.ProfessorCommentsChangedListener;
import extended.cs.sdsu.edu.service.ProfessorCommentsService;

public class ViewProfessorCommentsActivity extends ListActivity implements
		ProfessorCommentsChangedListener {

	private int selectedProfessorId;
	private ProfessorCommentsService professorCommentsService;
	private List<Comment> professorCommentsList = new ArrayList<Comment>();
	private CommentsListAdapter commentsListAdapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.professor_comments_list);
		Bundle bundleProfessorId = getIntent().getExtras();

		selectedProfessorId = bundleProfessorId.getInt("selectedProfessorID");

		professorCommentsService = ApplicationFactory
				.getProfessorCommentsService(this);
		commentsListAdapter = new CommentsListAdapter(professorCommentsList,
				this);
		setListAdapter(commentsListAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		professorCommentsService.addProfessorCommentsChangedListener(this);
		refreshProfessorComments();
	}

	@Override
	protected void onStop() {
		super.onStop();
		professorCommentsService.removeProfessorCommentsChangedListener(this);
	}

	private void refreshProfessorComments() {
		try {
			professorCommentsList = professorCommentsService
					.getProfessorComments(selectedProfessorId);

			commentsListAdapter.refreshList(professorCommentsList);
			commentsListAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			Log.e("RateMyProfessorTablet", e.getMessage(), e);
		}
	}

	@Override
	public void professorCommentsUpdated(List<Comment> newProfessorComments) {
		commentsListAdapter.refreshList(professorCommentsList);
		commentsListAdapter.notifyDataSetChanged();
	}
}
