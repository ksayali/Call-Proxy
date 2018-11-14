package callproxy.dashboard;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	private ArrayList<String> tbplist;
	TBPDAO dao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new TBPDAO(this);
		dao.open();
		setContentView(R.layout.tmain);

		populateData();

		ImageButton newprofile = (ImageButton) findViewById(R.id.addPro);
		newprofile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this,
						NewProfileActivity.class);
				i.putExtra("Edit", "false");
				startActivity(i);
			}
		});
		ImageButton settings = (ImageButton) findViewById(R.id.settings);
		settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent i = new Intent(MainActivity.this, ProfileSettings.class);
				startActivity(i);
			}
		});

		ListView profiles = (ListView) findViewById(R.id.tbplist);
		profiles.setOnItemClickListener(this);

		profiles.setLongClickable(true);
		profiles.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View v,
					int position, long id) {
				// Do some
				displayDialog(position);

				return true;
			}
		});

	}

	public void populateData() {
		tbplist = new ArrayList<String>();
		tbplist.clear();
		Cursor cursor = dao.getAllProfiles();

		if (cursor.getCount() == 0) {
			Toast.makeText(getApplicationContext(), "No Profiles Created..!",
					Toast.LENGTH_SHORT).show();
			// finish();
		} else {
			while (cursor.moveToNext()) {
				tbplist.add(cursor.getString(0));

			}
		}

		ListView profiles = (ListView) findViewById(R.id.tbplist);
		ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, tbplist);
		profiles.setAdapter(a1);
		profiles.setAdapter(a1);
		profiles.setItemsCanFocus(false);

		cursor.close();
	}

	public void displayDialog(int position) {

		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] ditems = { "Edit this Profile",
				"Delete this Profile", "Delete All" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose from:");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {

				if (item == 0) {
					String pname = tbplist.get(pos);
					SharedPreferences sp = getSharedPreferences("timebased", 0);
					if (pname.equalsIgnoreCase(sp.getString("CurrentProfile",
					""))) {
						Toast.makeText(getApplicationContext(),
								"Currently Activated " + pname + " can not be Edited..!",
								Toast.LENGTH_SHORT).show();
					} else {
						Intent i = new Intent(MainActivity.this,
								NewProfileActivity.class);
						i.putExtra("pname", pname);
						i.putExtra("Edit", "true");
						startActivity(i);
					}
				} else if (item == 1) {
					String pname = tbplist.get(pos);

					SharedPreferences sp = getSharedPreferences("timebased", 0);
					if (pname.equalsIgnoreCase(sp.getString("CurrentProfile",
							""))) {
						SharedPreferences.Editor editor = sp.edit();
						editor.putString("CurrentProfile", "NOPROFILE");
						int pre = sp.getInt("PreActivation", 0);
						editor.putInt("CurrentActivation", pre);
						editor.putInt("PreActivation", 0);
						editor.commit();
					}
					Cursor cursor = dao
							.getProfileDetails(new String[] { pname });
					if (cursor.moveToNext()) {

						SQLiteDatabase myDataBase = null;
						myDataBase = MainActivity.this.openOrCreateDatabase(
								"Db.db", Context.MODE_WORLD_WRITEABLE, null);

						String tname1 = cursor.getString(3);
						int res1 = myDataBase.delete(tname1, null, null);
						Toast.makeText(getApplicationContext(),
								tname1 + " : " + res1, Toast.LENGTH_SHORT)
								.show();

						String tname2 = cursor.getString(4);
						int res2 = myDataBase.delete(tname2, null, null);
						Toast.makeText(getApplicationContext(),
								tname2 + " : " + res2, Toast.LENGTH_SHORT)
								.show();

						int res = dao.deleteProfile(pname);
						if (res == 1 && res1 == 1 && res2 == 1)
							Toast.makeText(getApplicationContext(),
									pname + " Profile Deleted..",
									Toast.LENGTH_SHORT).show();

						tbplist.clear();
						populateData();
						myDataBase.close();
					}
					cursor.close();
				} else if (item == 2) {
					int r = dao.deleteAllProfiles();
					Toast.makeText(getApplicationContext(),
							"All Profiles Deleted..", Toast.LENGTH_SHORT)
							.show();
					populateData();
				}
				return;
			}
		});
		builder.create().show();

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub

		Toast.makeText(getApplicationContext(), "" + tbplist.get(position),
				Toast.LENGTH_LONG).show();

	}

	@Override
	public void onResume() {
		populateData();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
	}

}
