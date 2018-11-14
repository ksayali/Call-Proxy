package callproxy.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class TDBHelper extends SQLiteOpenHelper {

	public TDBHelper(Context context) {
		super(context, Constants.DATABASE_NAME_TBP, null, Constants.DATABASE_VERSION);
	}

	public TDBHelper(Context context, String dbname, CursorFactory cf,
			int version) {
		super(context, dbname, cf, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_TBP + " ("
				+"_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.PRNAME + " TEXT, "
				+ Constants.PSTIME + " TEXT, "
				+ Constants.PETIME + " TEXT, "
				+ Constants.PLIST + " TEXT, "
				+ Constants.PDAYS + " TEXT, "
				+ Constants.STATE + " TEXT );");			
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " +Constants.TABLE_NAME_TBP);
		onCreate(db);
	}

}