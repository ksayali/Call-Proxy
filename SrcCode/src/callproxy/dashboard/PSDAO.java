package callproxy.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class PSDAO {
	private SQLiteDatabase db;

	private final Context context;
	private final PSDBHelper dbHelper;

	public PSDAO(Context c) {
		context = c;
		dbHelper = new PSDBHelper(context, Constants.DATABASE_NAME_PS, null,
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

	// Private Contacts
	public long insertPContact(ContentValues values) {
		String number = values.getAsString(Constants.PPH);
		if (number.startsWith("+")) {
			number = number.substring(3, number.length());
			number = "0".concat(number);

		} else if (!(number.startsWith("0"))) {
			number = "0".concat(number);
		}
		values.put(Constants.PPH, number.toString());
		long id = db.insert(Constants.TABLE_NAME_PC, null, values);
		return id;
	}

	public Cursor getPCNo(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_PC, new String[] {
				Constants.PNAME, Constants.PPH }, Constants.PPH + "=?", parms,
				null, null, null);
		return result;
	}

	public String getPCName(String[] parms) {
		String name = null;
		Cursor result = db.query(Constants.TABLE_NAME_PC,
				new String[] { Constants.PNAME }, Constants.PPH + "=?", parms,
				null, null, null);

		if (result.moveToNext()) {
			name = result.getString(0);
		}
		return name;
	}

	public Cursor getAllPContacts() {
		Cursor result = null;
		try {
			result = db.query(Constants.TABLE_NAME_PC, new String[] {
					Constants.PNAME, Constants.PPH }, null, null, null, null,
					null);
			Log.i("testfail", "" + result.getCount());
		} catch (Exception e) {
			Log.i("testfail", "testfail");
		}
		return result;
	}

	public Cursor getAll() {
		Cursor cursor = null;
		try {
			cursor = db.query(Constants.TABLE_NAME_PC, new String[] {
					Constants.PNAME, Constants.PPH }, null, null, null, null,
					null);
			Log.i("testSuccess", "" + cursor.getCount());
		} catch (Exception e) {
			Log.i("testfail", "testfail");
		}

		return cursor;
	}

	public int deletePContact(String number) {

		String where = Constants.PPH + "='" + number + "'";
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PC, where, null);
		} catch (Exception e) {

		}
		return res;

	}

	public int deleteAllPContacts() {

		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PC, null, null);
		} catch (Exception e) {

		}
		return res;
	}

	public int editNumberFomPC(String num, ContentValues val) {
		String where = Constants.PPH + "='" + num + "'";
		int res = 0;
		try {
			res = db.update(Constants.TABLE_NAME_PC, val, where, null);

		} catch (Exception e) {

		}
		return res;

	}

	// private logs
	public long insertInPLog(ContentValues values) {
		long id = db.insert(Constants.TABLE_NAME_PCL, null, values);
		return id;
	}

	public Cursor getAllPLogs() {
		Cursor result = db.query(Constants.TABLE_NAME_PCL, new String[] {
				Constants.PNAME, Constants.PPH, Constants.PDTIME }, null, null,
				null, null, null);
		return result;
	}

	public int deletePLog(String params[]) {

		String where = Constants.PPH + "='" + params[0] + "' and "
				+ Constants.PDTIME + "='" + params[1] + "'";
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PCL, where, null);
		} catch (Exception e) {

		}
		return res;

	}

	public int deleteAllPLogs() {

		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PCL, null, null);
		} catch (Exception e) {

		}
		return res;
	}

	// Private Messages inbox
	public long insertPMsg(ContentValues values) {
		long id = 0;
		try {
			id = db.insert(Constants.TABLE_NAME_PM, null, values);
		} catch (Exception e) {

		}
		return id;
	}

	public Cursor getPMsg(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_PM, new String[] {
				Constants.NAME, Constants.PPH, Constants.PBODY,
				Constants.PDTIME }, Constants.PDTIME + "=?", parms, null, null,
				null);
		return result;
	}

	public Cursor getAllPMsgs() {
		Cursor result = db.query(Constants.TABLE_NAME_PM, new String[] {
				Constants.NAME, Constants.PPH, Constants.PBODY,
				Constants.PDTIME }, null, null, null, null, null);
		return result;
	}

	public int deletePMsg(String[] params) {

		String where = Constants.PPH + "='" + params[0] + "' and "
				+ Constants.PDTIME + "='" + params[1] + "'";
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PM, where, null);
		} catch (Exception e) {

		}
		return res;

	}

	public int deleteAllPMsg() {

		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PM, null, null);
		} catch (Exception e) {

		}
		return res;

	}

	// Private Messages sent
	public long insertPSMsg(ContentValues values) {
		long id = 0;
		try {
			id = db.insert(Constants.TABLE_NAME_PSM, null, values);
		} catch (Exception e) {

		}
		return id;
	}

	public Cursor getPSMsg(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_PSM, new String[] {
				Constants.NAME, Constants.PPH, Constants.PBODY,
				Constants.PDTIME }, Constants.PDTIME + "=?", parms, null, null,
				null);
		return result;
	}

	public Cursor getAllPSMsgs() {
		Cursor result = db.query(Constants.TABLE_NAME_PSM, new String[] {
				Constants.NAME, Constants.PPH, Constants.PBODY,
				Constants.PDTIME }, null, null, null, null, null);
		return result;
	}

	public int deletePSMsg(String[] params) {

		String where = Constants.PPH + "='" + params[0] + "' and "
				+ Constants.PDTIME + "='" + params[1] + "'";
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PSM, where, null);
		} catch (Exception e) {

		}
		return res;

	}

	public int deleteAllPSMsg() {

		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_PSM, null, null);
		} catch (Exception e) {

		}
		return res;
	}

}