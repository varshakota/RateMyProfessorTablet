package extended.cs.sdsu.edu.service;

import java.util.List;

import extended.cs.sdsu.edu.domain.Professor;

public interface ProfessorChangedListener {

	void professorListUpdated(List<Professor> newProfessorList);
}
