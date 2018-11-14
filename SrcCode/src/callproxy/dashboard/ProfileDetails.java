package callproxy.dashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileDetails extends Activity {

	TBPDAO dao = null;
	TextView profilename;
	TextView starttime;
	TextView endtime;
	Button adeactive;
	Button viewContacts;
	Button selecteddays;
	Cursor cursor;
	SQLiteDatabase myDataBase = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new TBPDAO(this);
		dao.open();
		setContentView(R.layout.profile_details);

		Bundle extras = getIntent().getExtras();
		String pname = extras.getString("pname");
		adeactive = (Button) findViewById(R.id.adeactive);
		profilename = (TextView) findViewById(R.id.DTname);
		profilename.setText(pname);

		cursor = dao.getProfileDetails(new String[] { pname });

		if (cursor.moveToNext()) {
			starttime = (TextView) findViewById(R.id.Dstarttime);
			starttime.setText(cursor.getString(1));

			endtime = (TextView) findViewById(R.id.Dendtime);
			endtime.setText(cursor.getString(2));
			String state = cursor.getString(5);
			if (state.equalsIgnoreCase("A")) {
				adeactive.setText("Deactivate");
			} else {
				adeactive.setText("Activate");
			}

		}

		viewContacts = (Button) findViewById(R.id.ViewContacts);
		viewContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String tlname = cursor.getString(3);
				TBDispContacts.items.clear();
				SQLiteDatabase mydb = null;
				try {

					mydb = ProfileDetails.this.openOrCreateDatabase("Db.db",
							Context.MODE_WORLD_WRITEABLE, null);
					Cursor c = mydb.query(tlname,
							new String[] { "name", "phno" }, null, null, null,
							null, null);
					int cnt = c.getCount();
					if (cnt != 0) {
						while (c.moveToNext()) {
							for (int i = 0; i < cnt; i++) {
								BData d = new BData(c.getString(1), c
										.getString(0));
								TBDispContacts.items.add(d);
							}
						}
					}
					c.close();

				} catch (Exception e) {

				} finally {
					mydb.close();
				}

				Intent i = new Intent(ProfileDetails.this, TBDispContacts.class);
				i.putExtra("Details", "true");
				startActivity(i);
			}
		});

		adeactive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ContentValues c = new ContentValues();

				c.put(Constants.PRNAME, cursor.getString(0));
				c.put(Constants.PSTIME, cursor.getString(1));
				c.put(Constants.PETIME, cursor.getString(2));
				c.put(Constants.PLIST, cursor.getString(3));
				c.put(Constants.PDAYS, cursor.getString(4));
				String State = null;
				if (adeactive.getText().toString().equalsIgnoreCase("Activate")) {
					State = "A";
					adeactive.setText("Deactive");
				} else {
					State = "D";
					adeactive.setText("Activate");
				}
				c.put(Constants.STATE, State);
				int r = dao.editProfile(cursor.getString(0), c);
				if (r == -1) {
					Toast.makeText(getApplicationContext(),
							"Table Not Updated..", Toast.LENGTH_SHORT).show();
				} else {
					if (State.equalsIgnoreCase("A"))
						Toast.makeText(getApplicationContext(),
								"Profile Activated..", Toast.LENGTH_SHORT)
								.show();
					else
						Toast.makeText(getApplicationContext(),
								"Profile Deactivated..", Toast.LENGTH_SHORT)
								.show();
				}
			}
		});

		selecteddays = (Button) findViewById(R.id.Ddays);
		selecteddays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String tdname = cursor.getString(4);
				showDialogDays(tdname);
			}
		});

	}

	public void showDialogDays(String tdname) {

		final CharSequence[] items = { "All", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday", "Sunday" };
		final boolean[] states = { false, false, false, false, false, false,
				false, false };
		final String TNAME = tdname;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Days");
		SQLiteDatabase mydb = null;
		try {

			mydb = ProfileDetails.this.openOrCreateDatabase("Db.db",
					Context.MODE_WORLD_WRITEABLE, null);
			Cursor c = mydb.query(TNAME, new String[] { "days" }, null, null,
					null, null, null);
			if (c.getCount() != 0) {

				while (c.moveToNext()) {
					for (int i = 0; i < 8; i++) {
						if (c.getString(0).equals(items[i])) {
							states[i] = true;
						}
					}
				}
			}
			c.close();

		} catch (Exception e) {

		} finally {
			mydb.close();
		}

		builder.setMultiChoiceItems(items, states,
				new DialogInterface.OnMultiChoiceClickListener() {
					public void onClick(DialogInterface dialogInterface,
							int item, boolean state) {
					}
				});

		builder.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		builder.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		builder.create().show();
	}

}
