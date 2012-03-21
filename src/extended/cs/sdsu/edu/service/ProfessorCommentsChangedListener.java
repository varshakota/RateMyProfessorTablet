package extended.cs.sdsu.edu.service;

import java.util.List;

import extended.cs.sdsu.edu.domain.Comment;

public interface ProfessorCommentsChangedListener {

	void professorCommentsUpdated(List<Comment> newProfessorComments);
}
