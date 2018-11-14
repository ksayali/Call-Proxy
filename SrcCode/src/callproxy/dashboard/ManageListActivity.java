package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ManageListActivity extends Activity {
	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bwlist);

		Button black = (Button) findViewById(R.id.blacklist);
		black.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(ManageListActivity.this,
						BlackList.class);
				ManageListActivity.this.startActivity(myIntent);
			}
		});

		Button white = (Button) findViewById(R.id.whitelist);
		white.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent myIntent = new Intent(ManageListActivity.this,
						WhiteList.class);
				ManageListActivity.this.startActivity(myIntent);
			}
		});

		Button settings = (Button) findViewById(R.id.MLSettings);
		settings.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				displayDialog();
			}
		});

	}

	public void displayDialog() {

		// TODO Auto-generated method stub
		final SharedPreferences sp = getSharedPreferences("timebased", 0);
		final int cur = sp.getInt("CurrentActivation", 0);
		final CharSequence[] items = { "No Blocking",
				"Reject Blacklisted Calls/SMS",
				"Accept Whitelisted Calls/All SMS" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Call Blocking Rule :");
		builder.setSingleChoiceItems(items,-1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				SharedPreferences.Editor editor = sp.edit();
				if (item == 0) {
					if (cur == 1) {
						
						Toast.makeText(
								getApplicationContext(),"No Calls will be blocked..",
								Toast.LENGTH_LONG).show();

						String pname = sp.getString("Currnt Profile", "");
						Toast.makeText(getApplicationContext(),
								"Profile " + pname + " is Active..! \n ",
								Toast.LENGTH_SHORT).show();

						editor.putInt("PreActivation", 0);
						editor.commit();
					} else {
						editor.putInt("CurrentActivation", 0);
						editor.commit();
					}
					finish();
				} else if (item == 1) {
					Toast.makeText(
							getApplicationContext(),"BlackList Enabled..",
							Toast.LENGTH_LONG).show();
					if (cur == 1) {

						String pname = sp.getString("CurrentProfile", "");
						Toast.makeText(
								getApplicationContext(),
								"Profile "
										+ pname
										+ " is Active..! \n Blacklist will be Enabled after this profile Exits, provided there are no more conflicting profiles",
								Toast.LENGTH_SHORT).show();

						editor.putInt("PreActivation", 2);
						editor.commit();
					} else {
						editor.putInt("CurrentActivation", 2);
						editor.commit();
					}
					finish();
				} else if (item == 2) {
					Toast.makeText(
							getApplicationContext(),"WhiteList Enabled..",
							Toast.LENGTH_LONG).show();
					if (cur == 1) {

						String pname = sp.getString("CurrentProfile", "");
						Toast.makeText(
								getApplicationContext(),
								"Profile "
										+ pname
										+ " is Active..! \n Whitelist will be Enabled after this profile Exits, provided there are no more conflicting profiles",
								Toast.LENGTH_SHORT).show();

						editor.putInt("PreActivation", 3);
						editor.commit();
					} else {
						editor.putInt("CurrentActivation", 3);
						editor.commit();
					}
					finish();
				}
				return;
			}
		});
		builder.create().show();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}