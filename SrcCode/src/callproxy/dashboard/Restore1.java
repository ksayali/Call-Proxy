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

public class Restore1 extends Activity {

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

				if (sd.canRead()) {

					for (int i = 0; i < ttables.size(); i++) {

						String currentDBPath = "\\sdcard\\CallProxy\\"
								+ ttables.get(i);

						String restoreDBPath = "\\data\\data\\callproxy.dashboard\\databases\\"
								+ ttables.get(i);

						File currentDB = new File(data, currentDBPath);
						File restoreDB = new File(sd, restoreDBPath);

						if (currentDB.exists()) {
							FileChannel src = new FileInputStream(currentDB)
									.getChannel();
							FileChannel dst = new FileOutputStream(restoreDB)
									.getChannel();
							dst.transferFrom(src, 0, src.size());
							src.close();
							dst.close();
						} else {
							Toast.makeText(getApplicationContext(),
									" No data to restore in " + currentDB,
									Toast.LENGTH_SHORT).show();

						}
					}

					Toast.makeText(getApplicationContext(),
							"Restore Process Complete!", Toast.LENGTH_SHORT)
							.show();

					finish();

					Intent intent = new Intent(Restore1.this,
							Backup_Restore_Activity.class);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(),
							"Can't read from SD Card", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				Toast.makeText(getApplicationContext(),
						"SD Card Unavailable !", Toast.LENGTH_SHORT).show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
