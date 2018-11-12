package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PrivateSpaceLogin extends Activity{
	
	logDAO dao;
	ProgressDialog Pdialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
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
						Cursor cursor = dao.getAllPass();
						if (cursor.getCount() == 0) {

							ContentValues cv = new ContentValues();
							String pass = userInput.getText().toString();

							if (pass.equals("snts")) {
								cv.put(Constants.passwd, pass);

								dao.insertlog(cv);
								
								Pdialog = ProgressDialog.show(
										PrivateSpaceLogin.this,
										"Authenticating.... ",
										"Loading. Please wait...", true);

								cursor.close();
								Intent i = new Intent(PrivateSpaceLogin.this,
										PrivateSpaceActivity.class);
								startActivity(i);
								
								finish();

								
							} else {
								Toast.makeText(getApplicationContext(),
										"Login Failed!!! Try Again...",
										Toast.LENGTH_SHORT).show();
								cursor.close();
								Intent i = new Intent(PrivateSpaceLogin.this,
										PrivateSpaceLogin.class);
								startActivity(i);
								finish();
							}
						} else {
							try {
								String[] parms = { userInput.getText()
										.toString() };
								cursor = dao.getPass(parms);
								// int cnt = cursor.getCount();

								if (cursor.getCount() != 0) {

									Pdialog = ProgressDialog.show(
											PrivateSpaceLogin.this,
											"Authenticating.... ",
											"Loading. Please wait...", true);

									cursor.close();
									Intent i = new Intent(PrivateSpaceLogin.this,
											PrivateSpaceActivity.class);
									startActivity(i);
									
									finish();
									
								} else {
									Toast.makeText(getApplicationContext(),
											"Login Failed!!! Try Again...",
											Toast.LENGTH_SHORT).show();
									Pdialog.dismiss();
									cursor.close();
									Intent i = new Intent(PrivateSpaceLogin.this,
											PrivateSpaceLogin.class);
									startActivity(i);
									finish();
								}

								

							} catch (Exception e) {

							}
						}

						// result.setText(userInput.getText());
					}
				})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
								dao.close();
								
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dao.close();
	}

}
