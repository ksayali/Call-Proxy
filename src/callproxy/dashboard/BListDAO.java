package callproxy.dashboard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class BListDAO {

	private SQLiteDatabase db;
	private final Context context;
	private final BLDBHelper dbHelper;

	public BListDAO(Context c) {
		context = c;
		dbHelper = new BLDBHelper(context, Constants.DATABASE_NAME_ML, null,
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

	//BlackList related functions
	
	public String getBLName(String[] parms) {
		String  name = null;
		Cursor result = db.query(Constants.TABLE_NAME_B, new String[] {
				Constants.PNAME }, Constants.BPH + "=?", parms,
				null, null, null);
		
		if(result.moveToNext())
		{
			name = result.getString(0);
		}
		return name;
	}
	
	public Cursor getBlistNo(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_B, new String[] {
				Constants.BNAME, Constants.BPH }, Constants.BPH + "=?", parms,
				null, null, null);
		return result;
	}
	
	public Cursor getWlistNo(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_W, new String[] {
				Constants.WNAME, Constants.WPH }, Constants.WPH + "=?", parms,
				null, null, null);
		return result;
	}

	
	public long insertInBlist(ContentValues values) {
		String number = values.getAsString(Constants.BPH);
		if (number.startsWith("+")) {
			number = number.substring(3, number.length());
			number = "0".concat(number);

		} else if (!(number.startsWith("0"))) {
			number = "0".concat(number);
		}
		values.put(Constants.BPH, number.toString());
		long id = db.insert(Constants.TABLE_NAME_B, null, values);
		return id;
	}
	
	public long insertInWlist(ContentValues values) {
		String number = values.getAsString(Constants.WPH);
		if (number.startsWith("+")) {
			number = number.substring(3, number.length());
			number = "0".concat(number);

		} else if (!(number.startsWith("0"))) {
			number = "0".concat(number);
		}
		values.put(Constants.WPH, number.toString());

		long id = db.insert(Constants.TABLE_NAME_W, null, values);
		return id;
	}
	
	
	public Cursor getAllBList() {
		Cursor result = db.query(Constants.TABLE_NAME_B, new String[] {
				Constants.BNAME, Constants.BPH}, null,
				null, null, null, null);
		return result;
	}
	
	public Cursor getAllWList() {
		Cursor result = db.query(Constants.TABLE_NAME_W, new String[] {
				Constants.WNAME, Constants.WPH}, null,
				null, null, null, null);
		return result;
	}
	
	
	public int deleteNumberFromBlist(String number) {

		String where = Constants.BPH + "='" + number + "'";
		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_B, where, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 		
	}
	
	public int editNumberFomBlist(String num, ContentValues val)
	{
		String where = Constants.BPH + "='" + num + "'";
		int res =0;
		try
		{
			res = db.update(Constants.TABLE_NAME_B, val, where, null);
			
		}
		catch(Exception e)
		{
			
		}
		return res; 
	
	}
	
	
	
	public int deleteNumberFromWlist(String number) {

		String where = Constants.WPH + "='" + number + "'";
		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_W, where, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 		
	}
	
	public int editNumberFomWlist(String num, ContentValues val)
	{
		String where = Constants.WPH + "='" + num + "'";
		int res =0;
		try
		{
			res = db.update(Constants.TABLE_NAME_W, val, where, null);
			
		}
		catch(Exception e)
		{
			
		}
		return res; 
	
	}
	
	//BLog related functions
	public long insertInLog(ContentValues values) {
		long id = db.insert(Constants.TABLE_NAME_BCL, null, values);
		return id;
	}
	
	public Cursor getAllBLogs() {
		Cursor result = db.query(Constants.TABLE_NAME_BCL, new String[] {
				Constants.BLNAME, Constants.BLPH, Constants.BLDTIME }, null,
				null, null, null, null);
		return result;
	}
	
	public int deleteBlog(String[] params) {

//		String where = Constants.BLPH + "='" + number + "'";
		
		String where = Constants.BLPH + "='" + params[0] + "' and "
		+ Constants.BLDTIME + "='" + params[1] + "'";
		
		
		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_BCL, where, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
		
	}
	
	public int deleteAllBLogs() {

		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_BCL, null, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
	}	
	
	
	public int deleteAllBlcontacts() {

		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_B, null, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
	}	
	
	public int deleteAllWlcontacts() {

		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_W, null, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
	}	
	
	//Block msg related functions	
	public Cursor getMsg(String[] parms) {
		Cursor result = db.query(Constants.TABLE_NAME_MSG, new String[] {
				Constants.NAME,Constants.PHONENO, Constants.BODY, Constants.MDATETIME },
				Constants.MDATETIME + "=?", parms, null, null, null);
		return result;
	}

	public Cursor getAllMsgs() {
		Cursor result = db.query(Constants.TABLE_NAME_MSG, new String[] {
				Constants.NAME,Constants.PHONENO, Constants.BODY, Constants.MDATETIME }, null,
				null, null, null, null);
		return result;
	}

	public long insertMsg(ContentValues values) {
		long id = 0;
		try {
			id = db.insert(Constants.TABLE_NAME_MSG, null, values);
		} catch (Exception e) {

		}
		return id;
	}

	public int deleteMsg(String[] params) {

		String where = Constants.PHONENO + "='" + params[0] + "' and "
				+ Constants.MDATETIME + "='" + params[1] + "'";
		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_MSG, where, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
		
	}
	
	public int deleteAllMsg() {

		int res =0;
		try
		{
			res = db.delete(Constants.TABLE_NAME_MSG, null, null);
		}
		catch(Exception e)
		{
			
		}
		return res; 
	}
	
	public void updateMsg(ContentValues values, String[] params) {
		db.update(Constants.TABLE_NAME_MSG, values, Constants.PHONENO + "=?",
				params);
	}


}
