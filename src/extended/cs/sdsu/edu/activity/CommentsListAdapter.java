package extended.cs.sdsu.edu.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import extended.cs.sdsu.edu.domain.Comment;

public class CommentsListAdapter extends BaseAdapter {

	List<Comment> professorComments;
	Context context;

	public CommentsListAdapter(List<Comment> professorComments, Context context) {
		this.professorComments = professorComments;
		this.context = context;
	}

	@Override
	public int getCount() {
		return professorComments.size();
	}

	@Override
	public Object getItem(int position) {
		return professorComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return professorComments.get(position).getCommentsId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.comments_list_row, null);
			viewHolder.commentsTextView = (TextView) convertView
					.findViewById(R.id.commentsRow);
			viewHolder.commentsDate = (TextView) convertView
					.findViewById(R.id.commentsDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Comment comment = (Comment) getItem(position);
		viewHolder.commentsTextView.setText(comment.getText());
		viewHolder.commentsDate.setText(comment.getDate());
		return convertView;
	}

	private static class ViewHolder {
		TextView commentsTextView;
		TextView commentsDate;
	}

	public void refreshList(List<Comment> professorComments) {
		this.professorComments = professorComments;
	}

}
