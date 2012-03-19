package extended.cs.sdsu.edu.service;

import android.content.Context;
import extended.cs.sdsu.edu.database.DatabaseAccessor;

public class ApplicationFactory {

	private static ProfessorService service;
	private static DatabaseAccessor databaseAccessor;

	public static ProfessorService getProfessorService(Context context) {
		if (service == null) {
			service = new ProfessorService(context);
		}
		return service;
	}

	public static DatabaseAccessor getDatabaseAccessor(Context context) {
		if (databaseAccessor == null) {
			databaseAccessor = new DatabaseAccessor(context);
		}
		return databaseAccessor;
	}
}
