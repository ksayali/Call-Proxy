package callproxy.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class logDAO {

	// private static final String TABLE_NAME = null;
	private SQLiteDatabase db;
	private final Context context;
	private final LogDBHelper dbHelper;

	public logDAO(Context c) {
		context = c;
		dbHelper = new LogDBHelper(context, Constants.DATABASE_NAME_LG, null,
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

	public Cursor getPass(String[] parms) {
		Cursor Rpasswd = db.query(Constants.TABLE_NAME_LG,
				new String[] { Constants.passwd }, Constants.passwd + "=?",
				parms, null, null, null);

		return Rpasswd;
	}

	public Cursor getAllPass() {
		Cursor result = db
				.query(Constants.TABLE_NAME_LG,
						new String[] { Constants.passwd }, null, null, null,
						null, null);
		return result;

	}

	public int updatePass(String pass, ContentValues val)
	{
		String where = Constants.passwd + "='" + pass + "'";
		int res =0;
		try
		{
			res = db.update(Constants.TABLE_NAME_LG, val, where, null);
			
		}
		catch(Exception e)
		{
			
		}
		return res; 
	
	}


	public long insertlog(ContentValues values) {
		long id = db.insert(Constants.TABLE_NAME_LG, null, values);
		return id;
	}
	
	public int deletePass() {
		
		int res = 0;
		try {
			res = db.delete(Constants.TABLE_NAME_LG, null, null);
		} catch (Exception e) {

		}
		return res;
	}
	
	public String getExistingPass() {
		String pass = null;
		Cursor result = db.query(Constants.TABLE_NAME_LG, new String[] {
				"password" }, null,
				null, null, null, null);
		
		if(result.moveToNext())
		{
			pass = result.getString(1);
		}
		else
		{
			pass = "NO";
		}
		return pass;
	}

}
