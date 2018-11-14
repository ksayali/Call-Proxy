package callproxy.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

public class Backup extends Activity {

	public ArrayList<String> ttables = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ttables = new ArrayList<String>();
		ttables.add("mlist.db");
		ttables.add("bCalmsg.db");
		ttables.add("privatespace.db");
		ttables.add("tbp.db");
		InputStream myInput;
		int cnt = 0;
		
		File sd = Environment.getExternalStorageDirectory();
		if (sd.canWrite()) {
		
			try {

				for (int i = 0; i < ttables.size(); i++) {
					myInput = new FileInputStream(
							"//data//data//callproxy.dashboard//databases//"
								+ ttables.get(i));
				
					File directory = new File("//sdcard//CallProxy");
					if (!directory.exists()) {
						directory.mkdirs();
					}

					OutputStream myOutput = new FileOutputStream(
							directory.getPath() + ttables.get(i));

					// Transfer bytes from the input file to the output file
					byte[] buffer = new byte[1024];
					int length;
					while ((length = myInput.read(buffer)) > 0) {
						myOutput.write(buffer, 0, length);
					}
			
					// 	Close and clear the streams

					myOutput.flush();
					
					myOutput.close();

					myInput.close();
					// Toast.makeText(getApplicationContext(), "Backup Succesful!",
					// Toast.LENGTH_SHORT).show();
					
				}
			
				Toast.makeText(getApplicationContext(), "Backup Completed!",
						Toast.LENGTH_SHORT).show();

					finish();

					Intent intent = new Intent(Backup.this, Backup_Restore_Activity.class);
					startActivity(intent);
					
			} catch (FileNotFoundException e) {
				cnt++;
//				Toast.makeText(getApplicationContext(), "FNF - ",
//					Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (IOException e) {
				cnt++;
//				Toast.makeText(getApplicationContext(), "Backup Unsuccesfull!",
//					Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "SD Card Error",
				 Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
