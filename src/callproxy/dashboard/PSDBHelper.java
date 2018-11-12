package callproxy.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class PSDBHelper extends SQLiteOpenHelper {
	public PSDBHelper(Context context) {
		super(context, Constants.DATABASE_NAME_PS, null,
				Constants.DATABASE_VERSION);
	}

	public PSDBHelper(Context context, String dbname, CursorFactory cf,
			int version) {
		super(context, dbname, cf, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_PC + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.PNAME
				+ " TEXT," + Constants.PPH + " TEXT );");

		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_PCL + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.PNAME
				+ " TEXT," + Constants.PPH + " TEXT," + Constants.PDTIME
				+ " TEXT );");

		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_PM + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.PNAME
				+ " TEXT," + Constants.PPH + " TEXT," + Constants.PBODY
				+ " TEXT," + Constants.PDTIME + " TEXT );");
		
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_PSM + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.PNAME
				+ " TEXT," + Constants.PPH + " TEXT," + Constants.PBODY
				+ " TEXT," + Constants.PDTIME + " TEXT );");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_PC);
		onCreate(db);

		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_PCL);
		onCreate(db);

		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_PM);
		onCreate(db);
		
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_PSM);
		onCreate(db);
	}
}
