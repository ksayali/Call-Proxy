package callproxy.dashboard;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

public class CallBlock extends BroadcastReceiver {

	private ITelephony telephonyService;
	BListDAO dao = null;
	PSDAO dao1 = null;
	TBPDAO dao2 = null;
	SharedPreferences sp;
	TelephonyManager telephony;
	Context c1;

	@Override
	public void onReceive(Context ctx, Intent intent) {
		c1 = ctx;
		dao = new BListDAO(ctx);
		dao.open();

		dao1 = new PSDAO(ctx);
		dao1.open();

		sp = ctx.getSharedPreferences("timebased", 0);
		Log.i("Database Access", "Data...");

		telephony = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		Bundle b = intent.getExtras();
		if (b != null) {
			String state = b.getString(TelephonyManager.EXTRA_STATE);
			String number = b.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
			if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				
				Log.i("MyBroadCastReceiver", number);
				
				if (number.startsWith("+")) {
					number = number.substring(3, number.length());
					number = "0".concat(number);
				}

				boolean wflag = false;
				boolean bflag = true;
				int cur = sp.getInt("CurrentActivation", 0);
				switch (cur) {
				case 1:// timebased is active
					if (!isFromCurrentProfile(number))
					{
						blockCall();
						delLog(number);
						handleBlockedCall(number);
					}
					break;

				case 2:// blacklist is active
					bflag= isBlackListed(number);
					if (bflag) {
						blockCall();
						delLog(number);
						handleBlockedCall(number);
					}
					break;

				case 3:// whitelist is active
					wflag = isWhiteListed(number);
					if(!wflag)
					{
						blockCall();
						handleBlockedCall(number);
					}
					break;

				default:
				//	break;

				}

				if (wflag == true || bflag == false) {
					isPrivate(number);
				}
			}
			
		}
		dao.close();
		dao1.close();
	}

	private void delLog(String number) {
		try {
			Thread.sleep(2000);

			String strUriCalls = "content://call_log/calls";
			Uri UriCalls = Uri.parse(strUriCalls);

			int i = c1.getContentResolver().delete(UriCalls,
					"NUMBER = '" + number + "'", null);
		} catch (Exception e) {

		}
	}
	
	private void blockCall() {
		try {
			Class c = Class.forName(telephony.getClass().getName());
			Method m = c.getDeclaredMethod("getITelephony");

			m.setAccessible(true);
			telephonyService = (ITelephony) m.invoke(telephony);
			telephonyService.endCall();
			Toast.makeText(c1, "Call Rejected", Toast.LENGTH_LONG).show();
		} catch (Exception e) {

		}
	}

	private boolean isFromCurrentProfile(String number) {
		boolean flag = false;
		String[] params = { sp.getString("CurrentProfile", "") };
		dao2 = new TBPDAO(c1);
		dao2.open();

		Cursor cursor = dao2.getProfileDetails(params);
		if (cursor.moveToNext()) {
			String ltname = cursor.getString(3);
			SQLiteDatabase db1 = c1.openOrCreateDatabase("Db.db",
					Context.MODE_WORLD_WRITEABLE, null);
			String[] parms = { number };
			Cursor result = db1.query(ltname, new String[] { "name", "phno" },
					"phno" + "=?", parms, null, null, null);
			if (result.moveToNext())
				flag = true;

			result.close();
			db1.close();
		}
		cursor.close();
		dao2.close();

		return flag;
	}

	private boolean isBlackListed(String number) {
		boolean flag = false;
		String[] params = { number };
		Cursor cursor = dao.getBlistNo(params);
		if (cursor.moveToNext()) {
			flag = true;
		}
		cursor.close();
		return flag;
	}

	private boolean isWhiteListed(String number) {
		boolean flag = false;
		String[] params = { number };
		Cursor cursor = dao.getWlistNo(params);
		if (cursor.moveToNext()) {
			flag = true;
		}
		cursor.close();
		return flag;
	}

	private void handleBlockedCall(String number) {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formattedDate = df.format(cal.getTime());

		ContentValues cv = new ContentValues();
		cv.put(Constants.BLNAME, "");
		cv.put(Constants.BLPH, number);
		cv.put(Constants.BLDTIME, formattedDate);

		long id = dao.insertInLog(cv);
		if (id == -1) {
			Toast.makeText(c1, "Number could not be added", Toast.LENGTH_LONG)
					.show();
		}

	}

	private void isPrivate(String number) {
		Cursor cursor2 = dao1.getPCNo(new String[] { number });
		if (cursor2.moveToNext()) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String formattedDate = df.format(cal.getTime());
			ContentValues cv = new ContentValues();

			String name = dao1.getPCName(new String[] { number });
			cv.put(Constants.PNAME, name);
			cv.put(Constants.PPH, number);
			cv.put(Constants.PDTIME, formattedDate);

			long id = dao1.insertInPLog(cv);
			if (id == -1) {
				Toast.makeText(c1, "Number could not be added",
						Toast.LENGTH_SHORT).show();
			}
		}
		cursor2.close();
	}
}
