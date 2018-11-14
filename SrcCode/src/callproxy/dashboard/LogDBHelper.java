package callproxy.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class LogDBHelper extends SQLiteOpenHelper {

	public LogDBHelper(Context context) {
		super(context, Constants.DATABASE_NAME_LG, null, Constants.DATABASE_VERSION);
	}

	public LogDBHelper(Context context, String dbname, CursorFactory cf,
			int version) {
		super(context, dbname, cf, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_LG + " ("
				+"_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.passwd + " TEXT );");
		/*
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_B + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.BNAME
				+ " TEXT," + Constants.BPH + " TEXT );");*/
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_LG);
		onCreate(db);
	} 

}
