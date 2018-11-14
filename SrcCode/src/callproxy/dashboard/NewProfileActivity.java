package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewProfileActivity extends Activity {
	/** Called when the activity is first created. */

	String pnm = "";
	EditText profilename = null;
	SQLiteDatabase myDataBase = null;
	String stime = null;
	String etime = null;
	TBPDAO dao = null;
	int flag = 0;
	int mHour = 0;
	int mMinute = 0;
	static final int TIME_DIALOG_ID = 0;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new TBPDAO(this);
		dao.open();
		myDataBase = this.getApplicationContext().openOrCreateDatabase("Db.db",
				Context.MODE_WORLD_WRITEABLE, null);

		setContentView(R.layout.new_profile);

		profilename = (EditText) findViewById(R.id.profilename);
		pnm = profilename.getText().toString();

		Bundle extras = getIntent().getExtras();
		final String oldname = extras.getString("pname");
		final String edit = extras.getString("Edit");

		if (edit.equalsIgnoreCase("true")) {
			Button insertContacts = (Button) findViewById(R.id.InserContacts);
			insertContacts.setText("Edit Contacts");

			Button selectDays = (Button) findViewById(R.id.days);
			selectDays.setText("Days Selected");

			Button saveProfile = (Button) findViewById(R.id.save);
			saveProfile.setText("Save Changes");

			EditText pname = (EditText) findViewById(R.id.profilename);
			pname.setText(oldname);

			Cursor cursor = dao.getProfileDetails(new String[] { oldname });

			if (cursor.moveToNext()) {

				TextView starttime = (TextView) findViewById(R.id.starttime);
				starttime.setText(cursor.getString(1));

				TextView endtime = (TextView) findViewById(R.id.endtime);
				endtime.setText(cursor.getString(2));
			}

			String tlname = cursor.getString(3);
			TBDispContacts.items.clear();
			SQLiteDatabase mydb = null;
			try {

				mydb = NewProfileActivity.this.openOrCreateDatabase("Db.db",
						Context.MODE_WORLD_WRITEABLE, null);
				Cursor c = mydb.query(tlname, new String[] { "name", "phno" },
						null, null, null, null, null);
				int cnt = c.getCount();
				if (cnt != 0) {
					while (c.moveToNext()) {
						for (int i = 0; i < cnt; i++) {
							BData d = new BData(c.getString(1), c.getString(0));
							TBDispContacts.items.add(d);
						}
					}
				}
				c.close();

			} catch (Exception e) {

			} finally {
				mydb.close();
			}
			cursor.close();
		}

		final Button setSTime = (Button) findViewById(R.id.sset);
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
		setSTime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				flag = 1;
				showDialog(TIME_DIALOG_ID);
			}
		});

		final Button setETime = (Button) findViewById(R.id.eset);
		setETime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				flag = 2;
				showDialog(TIME_DIALOG_ID);
			}
		});

		Button insertContacts = (Button) findViewById(R.id.InserContacts);
		insertContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Intent i = new Intent(NewProfileActivity.this,
						TBDispContacts.class);
				i.putExtra("Edit", "true");
				i.putExtra("Details", "false");
				startActivity(i);
			}
		});

		Button selectDays = (Button) findViewById(R.id.days);
		selectDays.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				EditText pname = (EditText) findViewById(R.id.profilename);
				String prname = pname.getText().toString();
				showDialogDays(prname + "days");
			}
		});

		Button saveProfile = (Button) findViewById(R.id.save);
		saveProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EditText pname = (EditText) findViewById(R.id.profilename);
				String prname = pname.getText().toString();

				if (!prname.equals("")) {

					String ltname = prname + "list";
					String tdname = prname + "days";

					Cursor cursor = dao
							.getProfileDetails(new String[] { prname });

					if (cursor.getCount() != 0) {

						if (!prname.equals(oldname)) {
							Toast.makeText(getApplicationContext(),
									"Profile Already Exists..",
									Toast.LENGTH_SHORT).show();
							pname.setText("");
						}
						cursor.close();
					} else if (TBDispContacts.items.isEmpty()) {
						Toast.makeText(getApplicationContext(),
								"Please Insert some Contacts..!",
								Toast.LENGTH_SHORT).show();
					} else if (stime == "00:00" || etime == "00:00") {
						Toast.makeText(getApplicationContext(),
								"Please Set Timings for the Profile..!",
								Toast.LENGTH_SHORT).show();
					} else {
						String[] stimesep = stime.split(":");
						String[] etimesep = etime.split(":");

						if (Integer.parseInt(stimesep[0]) > Integer
								.parseInt(etimesep[0])) {

							Toast.makeText(
									getApplicationContext(),
									"Please Set Suitable Timings for the Profile..!",
									Toast.LENGTH_SHORT).show();
							TextView st = (TextView) findViewById(R.id.starttime);
							st.setText("00:00");

							TextView et = (TextView) findViewById(R.id.endtime);
							et.setText("00:00");

						} else if (stimesep[1].equalsIgnoreCase(etimesep[1])) {
							if (Integer.parseInt(stimesep[1]) > Integer
									.parseInt(etimesep[1])) {
								Toast.makeText(
										getApplicationContext(),
										"Please Set Suitable Timings for the Profile..!",
										Toast.LENGTH_SHORT).show();
								TextView st = (TextView) findViewById(R.id.starttime);
								st.setText("00:00");

								TextView et = (TextView) findViewById(R.id.endtime);
								et.setText("00:00");
							} else {

								int exist = 0;
								Cursor allpro = dao.getAllProfiles();
								while (allpro.moveToNext()) {
									String[] tstimesep = allpro.getString(1)
											.split(":");
									String[] tetimesep = allpro.getString(2)
											.split(":");
									if (Integer.parseInt(stimesep[0]) > Integer
											.parseInt(tstimesep[0])) {
										if (Integer.parseInt(stimesep[0]) < Integer
												.parseInt(tetimesep[0])) {
											Toast.makeText(
													getApplicationContext(),
													allpro.getString(0)
															+ " profile includes this timing..!",
													Toast.LENGTH_SHORT).show();

											TextView st = (TextView) findViewById(R.id.starttime);
											st.setText("00:00");

											TextView et = (TextView) findViewById(R.id.endtime);
											et.setText("00:00");
											exist = 1;
											break;
										}
									} else {

									}
								}
								allpro.close();

								if (exist != 1) {
									int id = createDynamicDatabase(
											getApplicationContext(), ltname,
											tdname);

									if (id == 1) {

										if (edit.equalsIgnoreCase("true")) {

											Toast.makeText(
													getApplicationContext(),
													"Profile in Edit Mode..!!",
													Toast.LENGTH_SHORT).show();

											ContentValues c = new ContentValues();
											c.put(Constants.PRNAME, prname);
											c.put(Constants.PSTIME, stime);
											c.put(Constants.PETIME, etime);
											c.put(Constants.PLIST, ltname);
											c.put(Constants.PDAYS, tdname);
											c.put(Constants.STATE, "A");
											Bundle extras = getIntent()
													.getExtras();
											String oldname = extras
													.getString("pname");

											dao.deleteProfile(oldname);

											long r = dao.insertProfile(c);
											// long r = dao.editProfile(oldname,
											// c);

											if (r == -1) {
												Toast.makeText(
														getApplicationContext(),
														"Table Not Updated..",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														getApplicationContext(),
														"Profile Updated..",
														Toast.LENGTH_SHORT)
														.show();
											}

										} else {

											ContentValues c = new ContentValues();
											c.put(Constants.PRNAME, prname);
											c.put(Constants.PSTIME, stime);
											c.put(Constants.PETIME, etime);
											c.put(Constants.PLIST, ltname);
											c.put(Constants.PDAYS, tdname);
											c.put(Constants.STATE, "A");

											long r = dao.insertProfile(c);
											if (r == -1) {
												Toast.makeText(
														getApplicationContext(),
														"Table Not created..",
														Toast.LENGTH_SHORT)
														.show();
											} else {
												Toast.makeText(
														getApplicationContext(),
														prname
																+ " Profile Created..",
														Toast.LENGTH_SHORT)
														.show();

												// check(prname);
											}
										}
									}

									// adding list of contacts in the list
									myDataBase = NewProfileActivity.this
											.openOrCreateDatabase(
													"Db.db",
													Context.MODE_WORLD_WRITEABLE,
													null);

									for (int i = 0; i < TBDispContacts.items
											.size(); i++) {
										BData d = TBDispContacts.items.get(i);
										String phoneno = d.phone;
										if (phoneno.startsWith("+")) {
											phoneno = phoneno.substring(3,
													phoneno.length());
											phoneno = "0".concat(phoneno);

										} else if (!(phoneno.startsWith("0"))) {
											phoneno = "0".concat(phoneno);
										}

										String INSERT = "insert into " + ltname
												+ " (name, phno) values(?,?);";
										SQLiteStatement stmt = myDataBase
												.compileStatement(INSERT);
										stmt.bindString(1, d.name);
										stmt.bindString(2, phoneno);
										long res = stmt.executeInsert();

										if (res != -1) {
											// Toast.makeText(getApplicationContext(),
											// d.phone + " Added in table..",
											// Toast.LENGTH_SHORT).show();
										}

									}
									myDataBase.close();
									finish();
								}
							}

						}

					}

				} else {
					Toast.makeText(getApplicationContext(),
							"Please Insert valid Profile name..!",
							Toast.LENGTH_LONG).show();
				}

			}

		});

		Button cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// myDataBase = NewProfileActivity.this.openOrCreateDatabase(
				// "Db.db", Context.MODE_WORLD_WRITEABLE, null);
				try {
					myDataBase.delete(pnm, null, null);
				} catch (Exception e) {
				}
				TBDispContacts.items.clear();
				finish();
			}
		});

		myDataBase.close();
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

			mydb = NewProfileActivity.this.openOrCreateDatabase("Db.db",
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
						if (item == 0) {
							if (state == true) {
								for (int i = 1; i < items.length; i++) {
									((AlertDialog) dialogInterface)
											.getListView().setItemChecked(i,
													true);
								}
							} else {
								for (int i = 1; i < items.length; i++) {
									((AlertDialog) dialogInterface)
											.getListView().setItemChecked(i,
													false);
								}
							}
						}

					}
				});
		builder.setPositiveButton("Okay",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

						myDataBase = NewProfileActivity.this
								.openOrCreateDatabase("Db.db",
										Context.MODE_WORLD_WRITEABLE, null);
						try {
							myDataBase.delete(TNAME, null, null);
						} catch (Exception e) {

						}
						String querryString = "";
						querryString = "CREATE TABLE IF NOT EXISTS "
								+ TNAME
								+ "(_id integer primary key autoincrement, days TEXT);";
						myDataBase.execSQL(querryString);

						for (int i = 0; i < items.length; i++) {
							if (states[i]) {

								String INSERT = "insert into " + TNAME
										+ " (days) values(?);";
								SQLiteStatement stmt = myDataBase
										.compileStatement(INSERT);

								stmt.bindString(1, items[i].toString());
								long res = stmt.executeInsert();

								if (res != -1) {
									// Toast.makeText(getApplicationContext(),
									// items[i] + " Added in table..",
									// Toast.LENGTH_SHORT).show();
								}
							}

						}
						myDataBase.close();

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

	public int createDynamicDatabase(Context context, String ltname,
			String tdname) {
		int id = 0;
		myDataBase = NewProfileActivity.this.openOrCreateDatabase("Db.db",
				Context.MODE_WORLD_WRITEABLE, null);
		Log.i("INSIDE createLoginDatabase() Method",
				"*************creatLoginDatabase*********");
		String querryString;
		try {
			querryString = "";
			querryString = "CREATE TABLE IF NOT EXISTS "
					+ ltname
					+ "(_id integer primary key autoincrement, name TEXT, phno TEXT);";
			myDataBase.execSQL(querryString);

			querryString = "";
			querryString = "CREATE TABLE IF NOT EXISTS " + tdname
					+ "(_id integer primary key autoincrement, days TEXT);";
			myDataBase.execSQL(querryString);

			id = 1;

		} catch (SQLException ex) {
			Log.i("CreateDB Exception ", ex.getMessage());
			id = -1;
		} finally {
			myDataBase.close();
		}
		return id;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					false);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		}
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;

			if (flag == 1) {
				stime = "" + mHour + ":" + mMinute;
				TextView sttime = (TextView) findViewById(R.id.starttime);
				sttime.setText(stime);
				Button setSTime = (Button) findViewById(R.id.sset);
				setSTime.setText("Reset");

			} else if (flag == 2) {
				etime = "" + mHour + ":" + mMinute;
				TextView edtime = (TextView) findViewById(R.id.endtime);
				edtime.setText(etime);
				Button setETime = (Button) findViewById(R.id.eset);
				setETime.setText("Reset");

			}
		}
	};

	public void check(String pname) {
		Context context = this;
		dao = new TBPDAO(context);
		dao.open();

		Calendar cal = Calendar.getInstance();
		int minutes = cal.get(Calendar.MINUTE);
		int hours = cal.get(Calendar.HOUR);
		int AM_orPM = cal.get(Calendar.AM_PM);

		if (AM_orPM == 1) {
			hours += 12;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		Date d = new Date();
		String dayOfTheWeek = sdf.format(d);

		String ctime = hours + ":" + minutes;

		String[] stimesep = stime.split(":");
		String[] etimesep = etime.split(":");

		int start = 0;

		if (ctime.equalsIgnoreCase(stime)) {
			start = 1;
		} else if (hours > Integer.parseInt(stimesep[0])
				&& hours < Integer.parseInt(etimesep[0])) {

			start = 1;
		} else if (hours == Integer.parseInt(stimesep[0]))
			if (minutes > Integer.parseInt(stimesep[1])) {
				start = 1;
			}

		if (start == 1) {

			SQLiteDatabase myDataBase = this.openOrCreateDatabase("Db.db",
					Context.MODE_WORLD_WRITEABLE, null);
			Cursor c = myDataBase.query(pname + "days",
					new String[] { "days" }, "days=?",
					new String[] { dayOfTheWeek }, null, null, null);
			if (c.moveToNext()) {

				SharedPreferences sp = getSharedPreferences("timebased", 0);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("CurrentProfile", pname);
				int cur = sp.getInt("CurrentActivation", 0);
				editor.putInt("PreActivation", cur);
				editor.putInt("CurrentActivation", 1);
				editor.commit();
				editor.commit();
				Toast.makeText(context, pname + " Profile Activated..!",
						Toast.LENGTH_SHORT).show();
			}
			c.close();
		}

		myDataBase.close();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroy() {
		TBDispContacts.items.clear();
		dao.close();
		myDataBase.close();
		super.onDestroy();
	}

}