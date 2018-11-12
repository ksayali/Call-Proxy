package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class BlockSMS extends BroadcastReceiver {
	BListDAO dao = null;
//	MsgDAO dao2 = null;
	PSDAO daoP = null;
	public static final String SMS_EXTRA_NAME = "pdus";
	public static final String SMS_URI = "content://sms";

	public void onReceive(Context context, Intent intent) {

		dao = new BListDAO(context);
		dao.open();

//		dao2 = new MsgDAO(context);
//		dao2.open();

		daoP = new PSDAO(context);
		daoP.open();
		Log.i("Database Access", "SmS Data...");

		Bundle extras = intent.getExtras();

		if (extras != null) {
			// Get received SMS array
			String messages = "";
			String phoneno = "";
			String body = "";
			String mdatetime = "";
			Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);


			for (int i = 0; i < smsExtra.length; ++i) {

				SmsMessage sms = SmsMessage.createFromPdu((byte[]) smsExtra[i]);
				body = sms.getMessageBody().toString();
				phoneno = sms.getOriginatingAddress();
				messages += "SMS from " + phoneno + " :\n";
				messages += body + "\n";
				long timeStamp = sms.getTimestampMillis();
				Date date = new Date(timeStamp);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				mdatetime = sdf.format(date);

				messages += mdatetime + "\n";

			}
			
			
			if (phoneno.startsWith("+")) {
				phoneno = phoneno.substring(3, phoneno.length());
				phoneno = "0".concat(phoneno);
			}

			String[] params = new String[] { phoneno };
			Cursor cursor  = dao.getBlistNo(params);
			
			try {
			
				if (cursor.moveToNext()) {
					String name = dao.getBLName(new String [] {phoneno});
					ContentValues cv = new ContentValues();
					cv.put(Constants.NAME, name);
					cv.put(Constants.PHONENO, phoneno);
					cv.put(Constants.BODY, body);
					cv.put(Constants.MDATETIME, mdatetime);

					long id = dao.insertMsg(cv);
					if (id == -1) {
						Toast.makeText(context, "Row not inserted",
								Toast.LENGTH_SHORT).show();
					} else {
				//		Toast.makeText(context, "Row inserted",	Toast.LENGTH_LONG).show();
					}
					Toast.makeText(context, "Message Blocked", Toast.LENGTH_LONG)
							.show();
					this.abortBroadcast();
					
					cursor.close();
				} 
				else {
					Cursor PCur = daoP.getPCNo(params);
					if (PCur.moveToNext()) {
						ContentValues cv = new ContentValues();
						String name = daoP.getPCName(new String [] {phoneno});
						cv.put(Constants.PNAME, name);
						cv.put(Constants.PPH, phoneno);
						cv.put(Constants.PBODY, body);
						cv.put(Constants.PDTIME, mdatetime);

						long id = daoP.insertPMsg(cv);
						if (id == -1) {
							Toast.makeText(context, "Row not inserted",
									Toast.LENGTH_LONG).show();
						} else {
					//		Toast.makeText(context, "Row inserted",	Toast.LENGTH_LONG).show();
						}
						Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
						v.vibrate(300);
						
					//	Toast.makeText(context, "Private Message Recieved",	Toast.LENGTH_LONG).show();
						PCur.close();
						this.abortBroadcast();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cursor.close();
				dao.close();
			//	dao2.close();
				daoP.close();
			}

		}

	}
}
