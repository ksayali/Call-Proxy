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

public class Restore extends Activity {

	public ArrayList<String> ttables = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ttables = new ArrayList<String>();
		ttables.add("mlist.db");
		ttables.add("bCalmsg.db");
		ttables.add("privatespace.db");
		ttables.add("tbp.db");
		int cnt = 0;

		OutputStream myOutput;
		File sd = Environment.getExternalStorageDirectory();
		if (sd.canRead()) {

		try {

			for (int i = 0; i < ttables.size(); i++) {
				myOutput = new FileOutputStream(
						"//data//data//callproxy.dashboard//databases//"
								+ ttables.get(i));

				// Set the folder on the SDcard
				File directory = new File("//sdcard//CallProxy");
				// Set the input file stream up:

				InputStream myInputs = new FileInputStream(directory.getPath()
						+ ttables.get(i));

				// Transfer bytes from the input file to the output file
				byte[] buffer = new byte[1024];
				int length;
				while ((length = myInputs.read(buffer)) > 0) {
					myOutput.write(buffer, 0, length);

//					Toast.makeText(getApplicationContext(), buffer.toString(),
//							Toast.LENGTH_SHORT).show();
				}

				// Close and clear the streams
				myOutput.flush();

				myOutput.close();

				myInputs.close();
//				Toast.makeText(getApplicationContext(),
//						"Data Restored Succesfully!", Toast.LENGTH_SHORT)
//						.show();
				
			}
			Toast.makeText(getApplicationContext(),
					"Data Restored Succesfully!", Toast.LENGTH_SHORT)
					.show();
			finish();

			Intent intent = new Intent(Restore.this, Backup_Restore_Activity.class);
			startActivity(intent);

		} catch (FileNotFoundException e) {
			cnt++;
			Toast.makeText(getApplicationContext(),
					"FNF DB missing)!", Toast.LENGTH_SHORT).show();

			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			Toast.makeText(getApplicationContext(), "Restore Unsuccesfull!",
					Toast.LENGTH_SHORT).show();

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "SD Card Error",
				 Toast.LENGTH_SHORT).show();
		}
		
	}

}
