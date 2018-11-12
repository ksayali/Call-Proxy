package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class DashboardActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardmain);
        
        Button privacyEraser = (Button) findViewById(R.id.h);
        Button blockedCS = (Button) findViewById(R.id.bs);
        Button manageContacts = (Button) findViewById(R.id.Button04);
        Button privateSpace = (Button) findViewById(R.id.homeButton);
        Button timeBProiles = (Button) findViewById(R.id.Button01);
        Button backres = (Button) findViewById(R.id.Button02);
        Button bsettings = (Button) findViewById(R.id.Button05);
		privacyEraser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, PrivacyEraserActivity.class);
		         startActivity(i);
		         overridePendingTransition(R.anim.fade, R.anim.hold); 
			}
		});
		
		manageContacts.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, ManageListActivity.class);
		         startActivity(i);
		         
		         overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
		         
			}
		});
		
		blockedCS.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, ViewBCMActivity.class);
		         startActivity(i);
		         overridePendingTransition(R.anim.slide_left,R.anim.slide_right); 
			}
		});
		
		privateSpace.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, PrivateSpaceActivity.class);
		         startActivity(i);
		         overridePendingTransition(R.anim.slide_left,R.anim.slide_right);
			}
		});
		
		
		bsettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				displayBDialog();
			}
		});
		
		timeBProiles.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, MainActivity.class);
		         startActivity(i);
		         overridePendingTransition(R.anim.slide_left,R.anim.slide_right);  
			}
		});
		backres.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(DashboardActivity.this, Backup_Restore_Activity.class);
		         startActivity(i);
		         overridePendingTransition(R.anim.slide_left,R.anim.slide_right);  
			}
		});
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Settings");
		menu.add(0, 2, 0, "Help");

		return super.onCreateOptionsMenu(menu);
	}

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {

		case 1:
			
			displayDialog();
			
			break;
		
    	}
		return super.onOptionsItemSelected(item);
	}
    
    public void displayBDialog() {

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
					Toast.makeText(
							getApplicationContext(),"No Blocking..",
							Toast.LENGTH_LONG).show();
					if (cur == 1) {

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
					dialogInterface.dismiss();

				} else if (item == 1) {
					Toast.makeText(
							getApplicationContext(),"BlackList..",
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
					dialogInterface.dismiss();
				} else if (item == 2) {
					Toast.makeText(
							getApplicationContext(),"WhiteList..",
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
					dialogInterface.dismiss();
				}
				return;
			}
		});
		builder.create().show();
	}
    
    public void displayDialog() {

		// TODO Auto-generated method stub
		final CharSequence[] items = { "Change Login password", "Change Private Space password" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Opt:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				if (item == 0) {
					Intent i = new Intent(DashboardActivity.this, ChangePasswd.class);
					startActivity(i);

					
					
				} else if (item == 1) {
					/*Intent i = new Intent(DashboardActivity.this, BLCallLogDisp.class);
					startActivity(i);*/
				}

				/*else if (item == 2) {
					Intent i = new Intent(DashboardActivity.this, BInputNumber.class);
					startActivity(i);
				}*/
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
