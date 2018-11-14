package callproxy.dashboard;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Backup_Restore_Activity extends Activity {

	public static ProgressDialog dialog;

	private static final int DIALOG_KEY = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.back_res);

		dialog = new ProgressDialog(this);

		Button backup = (Button) findViewById(R.id.backup);

		backup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				showDialog(DIALOG_KEY);
				Intent i = new Intent(Backup_Restore_Activity.this,
						Backup.class);
				startActivity(i);
				finish();
				dialog.dismiss();
			}
		});

		Button restore = (Button) findViewById(R.id.restore);

		restore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog(DIALOG_KEY);
				Intent i = new Intent(Backup_Restore_Activity.this,
						Restore.class);
				startActivity(i);
				finish();
				dialog.dismiss();
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {

		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("In Progress...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);

		return dialog;

	}

}
