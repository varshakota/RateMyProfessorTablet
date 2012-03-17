package extended.cs.sdsu.edu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "RateProfessor.db";
	private static final int DATABASE_VERSION = 1;
	static final String _ID = "ID";
	static final String FIRSTNAME = "firstname";
	static final String LASTNAME = "lastname";
	static final String OFFICE = "office";
	static final String PHONE = "phone";
	static final String EMAIL = "email";
	static final String AVERAGE = "average";
	static final String TOTALRATING = "totalrating";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase professordb) {
		/*
		 * professordb.execSQL("CREATE TABLE " + "PROFESSOR " + "(" + "_ID " +
		 * "INTEGER PRIMARY KEY," + "LASTNAME " + "TEXT," + "FIRSTNAME " +
		 * "TEXT," + "OFFICE " + "TEXT," + "PHONE " + "TEXT," + "EMAIL " +
		 * "TEXT," + "AVERAGE " + "FLOAT," + "TOTALRATING " + "FLOAT" + ");");
		 */
		professordb
				.execSQL("CREATE TABLE PROFESSOR (ID INTEGER PRIMARY KEY,firstname TEXT,lastname TEXT);");

		professordb
				.execSQL("CREATE TABLE PROFESSOR_DETAILS (ID INTEGER, office TEXT, phone TEXT,email TEXT,average FLOAT,totalrating INTEGER,FOREIGN KEY(ID) REFERENCES PROFESSOR(ID));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {

	}

}
