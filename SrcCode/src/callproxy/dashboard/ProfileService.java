package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class ProfileService extends Service {

	private TBPDAO dao;

	@Override
	public void onCreate() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		check();

		TimerTask scanTask;
		final Handler handler = new Handler();
		Timer t = new Timer();

		scanTask = new TimerTask() {
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						check();
					}
				});
			}
		};

		t.schedule(scanTask, 0, 60000);

		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	public void check() {
		Context context = ProfileService.this;
		dao = new TBPDAO(context);
		dao.open();

		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);
		int hours = cal.get(Calendar.HOUR);
		int AM_orPM = cal.get(Calendar.AM_PM);

		if (AM_orPM == 1) {
			hours += 12;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		String dayOfTheWeek = sdf.format(d);

		String ctime = hours + ":" + minutes;
		Cursor cursor = dao.getAllProfiles();
		while (cursor.moveToNext()) {

			if (ctime.equals(cursor.getString(1))&& "A".equalsIgnoreCase(cursor.getString(5))) {
//				Toast.makeText(
//						ProfileService.this,
//						"Hiii..",
//						Toast.LENGTH_SHORT).show();
				SQLiteDatabase myDataBase = null;
				myDataBase = ProfileService.this.openOrCreateDatabase("Db.db",
						Context.MODE_WORLD_WRITEABLE, null);
				Cursor c = myDataBase.query(cursor.getString(4),
						new String[] { "days" }, "days=?",
						new String[] { dayOfTheWeek }, null, null, null);
				if (c.moveToNext()) {

					SharedPreferences sp = getSharedPreferences("timebased", 0);
					SharedPreferences.Editor editor = sp.edit();
					editor.putString("CurrentProfile", cursor.getString(0));
					int cur = sp.getInt("CurrentActivation", 0);
					editor.putInt("PreActivation", cur);
					editor.putInt("CurrentActivation", 1);
					editor.commit();
					editor.commit();
					Toast.makeText(
							ProfileService.this,
							sp.getString("CurrentProfile", "")
									+ " Profile Activated..!",
							Toast.LENGTH_SHORT).show();
				}
				c.close();
				myDataBase.close();
			}
			if (ctime.equals(cursor.getString(2)) && "A".equalsIgnoreCase(cursor.getString(5))) {
				SharedPreferences sp = getSharedPreferences("timebased", 0);
				
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("CurrentProfile", "NOPROFILE");
				int pre = sp.getInt("PreActivation", 0);
				editor.putInt("CurrentActivation", pre);
				editor.putInt("PreActivation", 0);
				editor.commit();
				Toast.makeText(ProfileService.this,
						cursor.getString(0) + " Profile Deactivated..!",
						Toast.LENGTH_SHORT).show();
			}
		}
		cursor.close();
		dao.close();
	}

}