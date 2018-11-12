package callproxy.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MsgDBHelper extends SQLiteOpenHelper {

	public MsgDBHelper(Context context) {
		super(context, Constants.DATABASE_NAME_ML, null, Constants.DATABASE_VERSION);
	}

	public MsgDBHelper(Context context, String dbname, CursorFactory cf,
			int version) {
		super(context, dbname, cf, version);
	}

	@Override
	
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_MSG + " ("
				+"_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.PHONENO + " TEXT,"
				+ Constants.BODY + " TEXT,"
				+ Constants.MDATETIME + " TEXT);");
}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_MSG);
		onCreate(db);
	}
}