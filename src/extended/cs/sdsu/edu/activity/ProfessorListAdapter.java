package extended.cs.sdsu.edu.activity;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import extended.cs.sdsu.edu.domain.Professor;

public class ProfessorListAdapter extends BaseAdapter {

	private List<Professor> professorList;
	private final Context context;

	public ProfessorListAdapter(List<Professor> professorList, Context context) {
		this.professorList = professorList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return professorList.size();
	}

	@Override
	public Object getItem(int position) {
		return professorList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return professorList.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.professor_listview_row, null);
			holder.professorNameText = (TextView) convertView
					.findViewById(R.id.name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Professor professor = (Professor) getItem(position);
		holder.professorNameText.setText(professor.getFirstName() + " "
				+ professor.getLastName());
		return convertView;
	}

	public void refreshList(List<Professor> professorList) {
		this.professorList = professorList;
	}

	private static class ViewHolder {
		TextView professorNameText;
	}
}
