package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */
	private static final String MEDIA = "media";
	private static final int RESOURCES_AUDIO = 3;
	logDAO dao;
	ProgressDialog Pdialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent i = new Intent();
		i.setClass(this, ProfileService.class);

		this.startService(i);

		setContentView(R.layout.loginredirect);
		dao = new logDAO(this);
		dao.open();

		// final EditText result = (EditText) findViewById(R.id.epass);

		SharedPreferences sp = getSharedPreferences("timebased", 0);
		String curProfile = sp.getString("CurrentProfile", "");
		if (curProfile.equalsIgnoreCase("")) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putString("CurrentProfile", "NOPROFILE");
			editor.commit();
		}

		int curActivation = sp.getInt("CurrentActivation", 0);
		if (curActivation == 0) {
			SharedPreferences.Editor editor = sp.edit();
			editor.putInt("CurrentActivation", 0);
			editor.putInt("PreActivation", 0);
			editor.commit();
		}

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

							//if (pass.equals("snts")) {
								
								cv.put(Constants.passwd, pass);
								dao.insertlog(cv);
							
								MediaPlayer mp = MediaPlayer.create(
										getBaseContext(), R.raw.bckmusic);
								mp.start();
								Pdialog = ProgressDialog.show(
										LoginActivity.this,
										"Authenticating.... ",
										"Loading. Please wait...", true);

								cursor.close();
								Intent i = new Intent(LoginActivity.this,
										DashboardActivity.class);
								startActivity(i);
								finish();

								
						//	} else {
							//	try
								//{
//								Toast.makeText(getApplicationContext(),
//										"Login Failed!!! Try Again...",
//										Toast.LENGTH_SHORT).show();
//								Thread.sleep(1000);
//								cursor.close();
								//AlertDialog alertDialog = alertDialogBuilder.create();
								//alertDialog.show();
								
								//finish();
//								Intent i = new Intent(LoginActivity.this,
//										LoginActivity.class);
//								
//								startActivity(i);
//								Toast.makeText(getApplicationContext(),
//										"Login Failed!!! Try Again...",
//										Toast.LENGTH_SHORT).show();
//								}
//								catch(Exception e)
//								{ }
//							}
						} else {
							try {
								String[] parms = { userInput.getText()
										.toString() };
								cursor = dao.getPass(parms);
								// int cnt = cursor.getCount();

								if (cursor.getCount() != 0) {

									MediaPlayer mp = MediaPlayer.create(
											getBaseContext(), R.raw.bckmusic);
									mp.start();
									Pdialog = ProgressDialog.show(
											LoginActivity.this,
											"Authenticating.... ",
											"Loading. Please wait...", true);

									cursor.close();
									Intent i = new Intent(LoginActivity.this,
											DashboardActivity.class);
									startActivity(i);
									finish();
									
								} else {
									Toast.makeText(getApplicationContext(),
											"Login Failed!!! Try Again...",
											Toast.LENGTH_SHORT).show();
//									Pdialog.dismiss();
//									cursor.close();	
//									AlertDialog alertDialog = alertDialogBuilder.create();
//									alertDialog.show();
//									finish();
									Intent i = new Intent(LoginActivity.this,
											LoginActivity.class);
									startActivity(i);
									
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
								finish();
								
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	
	@Override
	public void onBackPressed()
	{
		finish();
		super.onDestroy();
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dao.close();
	}

}