package callproxy.dashboard;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BLDBHelper extends SQLiteOpenHelper {

	public BLDBHelper(Context context) {
		super(context, Constants.DATABASE_NAME_ML, null,
				Constants.DATABASE_VERSION);
	}

	public BLDBHelper(Context context, String dbname, CursorFactory cf,
			int version) {
		super(context, dbname, cf, version);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_B + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.BNAME
				+ " TEXT," + Constants.BPH + " TEXT );");
		
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_W + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.WNAME
				+ " TEXT," + Constants.WPH + " TEXT );");
		
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_BCL + " ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + Constants.BLNAME
				+ " TEXT," + Constants.BLPH + " TEXT," + Constants.BLDTIME
				+ " TEXT );");
		
		db.execSQL("CREATE TABLE " + Constants.TABLE_NAME_MSG + " ("
				+"_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ Constants.NAME + " TEXT,"
				+ Constants.PHONENO + " TEXT,"
				+ Constants.BODY + " TEXT,"
				+ Constants.MDATETIME + " TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_B);
		onCreate(db);
		
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_W);
		onCreate(db);

		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_BCL);
		onCreate(db);
		
		db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_MSG);
		onCreate(db);
	}
}
