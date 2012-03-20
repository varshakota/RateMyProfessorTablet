package extended.cs.sdsu.edu.service;

import android.content.Context;
import extended.cs.sdsu.edu.database.DatabaseAccessor;

public class ApplicationFactory {

	private static ProfessorService professorService;
	private static DatabaseAccessor databaseAccessor;
	private static ProfessorCommentsService professorCommentsService;
	private static ProfessorRatingService professorRatingService;

	public static ProfessorService getProfessorService(Context context) {
		if (professorService == null) {
			professorService = new ProfessorService(context);
		}
		return professorService;
	}

	public static ProfessorCommentsService getProfessorCommentsService(
			Context context) {
		if (professorCommentsService == null) {
			professorCommentsService = new ProfessorCommentsService(context);
		}
		return professorCommentsService;
	}

	public static ProfessorRatingService getProfessorRatingService(
			Context context) {
		if (professorRatingService == null) {
			professorRatingService = new ProfessorRatingService(context);
		}
		return professorRatingService;
	}

	public static DatabaseAccessor getDatabaseAccessor(Context context) {
		if (databaseAccessor == null) {
			databaseAccessor = new DatabaseAccessor(context);
		}
		return databaseAccessor;
	}
}
