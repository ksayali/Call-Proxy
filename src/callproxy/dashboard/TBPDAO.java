package callproxy.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class TBPDAO {
	private SQLiteDatabase db;
	private final Context context;
	private final TDBHelper dbHelper;

	public TBPDAO(Context c) {
		context = c;
		dbHelper = new TDBHelper(context, Constants.DATABASE_NAME_TBP, null,
				Constants.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			db = dbHelper.getReadableDatabase();
		}
	}

	public Cursor getAllProfiles() {
		Cursor profiles = db.query(Constants.TABLE_NAME_TBP, new String[] {
				Constants.PRNAME, Constants.PSTIME, Constants.PETIME,
				Constants.PLIST, Constants.PDAYS, Constants.STATE }, null,
				null, null, null, null);
		return profiles;
	}

	public Cursor getProfileDetails(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_TBP, new String[] {
				Constants.PRNAME, Constants.PSTIME, Constants.PETIME,
				Constants.PLIST, Constants.PDAYS, Constants.STATE }, Constants.PRNAME + "=?",
				parms, null, null, null);
		return result;
	}

	public long insertProfile(ContentValues values) {
		long id = db.insert(Constants.TABLE_NAME_TBP, null, values);
		return id;
	}

	public int deleteProfile(String pname) {
		String where = Constants.PRNAME + "='" + pname + "'";
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_TBP, where, null);
		} catch (Exception e) {

		}
		return res;
	}

	public int deleteAllProfiles() {

		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_TBP, null, null);
		} catch (Exception e) {

		}
		return res;

	}
	
	public int editProfile(String pname, ContentValues val) {
		String where = Constants.PRNAME + "='" + pname + "'";
		int res = 0;
		try {
			res = db.update(Constants.TABLE_NAME_TBP, val, where, null);

		} catch (Exception e) {

		}
		return res;

	}

}
