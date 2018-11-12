package callproxy.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class Backup1 extends Activity {

	public ArrayList<String> ttables = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ttables = new ArrayList<String>();
		ttables.add("mlist.db");
		ttables.add("bCalmsg.db");
		ttables.add("privatespace.db");
		ttables.add("tbp.db");

		try {

			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.exists()) {

				if (sd.canWrite()) {

					for (int i = 0; i < ttables.size(); i++) {

						String currentDBPath = "//data//data//callproxy.dashboard//databases//"
								+ ttables.get(i);
						String backupDBPath = "//sdcard//CallProxy//"
								+ ttables.get(i);
						File currentDB = new File(data, currentDBPath);
						File backupDB = new File(sd, backupDBPath);

						if (currentDB.exists()) {
							FileChannel src = new FileInputStream(currentDB)
									.getChannel();
							FileChannel dst = new FileOutputStream(backupDB)
									.getChannel();
							dst.transferFrom(src, 0, src.size());
							src.close();
							dst.close();
						} else {
							Toast.makeText(getApplicationContext(),
									" No data in " + currentDB,
									Toast.LENGTH_SHORT).show();

						}
					}

					Toast.makeText(getApplicationContext(),
							"Backup Process Complete!", Toast.LENGTH_SHORT)
							.show();

					finish();

					Intent intent = new Intent(Backup1.this,
							Backup_Restore_Activity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"Can't write to SD Card", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"SD Card Unavailable !", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"exception !", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
