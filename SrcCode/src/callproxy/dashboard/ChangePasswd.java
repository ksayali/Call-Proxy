package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ChangePasswd extends Activity {
	/** Called when the activity is first created. */

	logDAO dao;
	ProgressDialog Pdialog;
	String oldpass = null;
	long res;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.loginredirect);
		dao = new logDAO(this);
		dao.open();

		// final EditText result = (EditText) findViewById(R.id.epass);

		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.loginpg, null);

		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);

		alertDialogBuilder.setView(promptsView);

		final EditText userInput = (EditText) promptsView
				.findViewById(R.id.epass);

		// set dialog message
		alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// get user input and set it to result
						// edit text
						Cursor cursor;
						try {
							String[] parms = { userInput.getText().toString() };
							cursor = dao.getAllPass();
							// int cnt = cursor.getCount();
							

							if (cursor.getCount() != 0) {
								
								
								ContentValues cv = new ContentValues();
								String newpass = userInput.getText().toString();
								
								//String oldpass = cursor.getString(0);
//								Toast.makeText(ChangePasswd.this,
//										"Old Pass" + oldpass,
//										Toast.LENGTH_SHORT).show();

								
								long res1 = dao.deletePass();
								if (res1 != 0) {
									cv.put(Constants.passwd, newpass);
									res = dao.insertlog(cv);
								}
								Pdialog = ProgressDialog.show(
										ChangePasswd.this,
										"Resetting Password.... ",
										"Please wait...", true);

								if (res == 0) {
									Toast.makeText(ChangePasswd.this,
											"Failed to update!!",
											Toast.LENGTH_SHORT).show();
								} else {
									Toast.makeText(ChangePasswd.this,
											"Password Updated!!",
											Toast.LENGTH_SHORT).show();
								}
								// cursor.close();
								Intent i = new Intent(ChangePasswd.this,
										DashboardActivity.class);
								startActivity(i);

								finish();

							} else {
								Toast.makeText(getApplicationContext(),
										"Cursor count Failed!!! Try Again...",
										Toast.LENGTH_SHORT).show();
								// Pdialog.dismiss();
								// cursor.close();
								// AlertDialog alertDialog =
								// alertDialogBuilder.create();
								// alertDialog.show();
								// finish();
								Intent i = new Intent(ChangePasswd.this,
										ChangePasswd.class);
								startActivity(i);

							}

						} catch (Exception e) {

						}
					}

					// result.setText(userInput.getText());

				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								dao.close();
								finish();

							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	@Override
	public void onBackPressed() {
		finish();
		super.onDestroy();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		dao.close();
	}

}