package extended.cs.sdsu.edu.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "RateProfessor.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase professordb) {
		professordb.execSQL("CREATE TABLE " + "PROFESSOR " + "(" + "_ID "
				+ "INTEGER PRIMARY KEY," + "LASTNAME " + "TEXT," + "FIRSTNAME "
				+ "TEXT," + "PHONE " + "TEXT," + "EMAIL " + "TEXT,"
				+ "AVERAGE " + "FLOAT," + "TOTALRATING " + "FLOAT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {

	}

}
