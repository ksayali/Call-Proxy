package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class PSConfigure extends ListActivity {

	List<BData> items = new ArrayList<BData>();
	PSDAO dao = null;
	ListAdapter la;
	BData d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new PSDAO(this);
		dao.open();
		populateData();

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						// Do some
						displayEditDelDialog(position);

						return true;
					}
				});

	}

	public void populateData() {
		items.clear();
		try {

			Cursor cursor = dao.getAllPContacts();
			int cnt = cursor.getCount();

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"No Private Contacts..", Toast.LENGTH_SHORT).show();

			} else {
				while (cnt != 0) {
					if (cursor.moveToNext()) {

						items.add(new BData(cursor.getString(1), cursor
								.getString(0)));
					}
					cnt--;
				}
			}
			cursor.close();
		} catch (Exception e) {
			Log.i("View Test", "Test Success..");
		}
		
		la = new BMyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);
	}

	public void displayEditDelDialog(int position) {

		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] ditems = { "Delete this Contact", "Edit This Contact", "Delete All Contacts" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose from:");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				
				d = items.get(pos);
				String num = d.phone;
				String nm = d.name;

				if (item == 0) {
					d = items.get(pos);

					int res = dao.deletePContact(d.phone);
					if (res == 1)
						Toast.makeText(getApplicationContext(),
								"Contact Deleted..", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(),
								"Contact Not Deleted..", Toast.LENGTH_SHORT).show();
					populateData();
						
				} 
				 else if (item == 1) {
					 Intent i = new Intent(PSConfigure.this, EditContact.class);
						i.putExtra("phno", num);
						i.putExtra("name", nm);
						i.putExtra("flag", "3");
						startActivity(i);
//						finish();						
				 
				 }
				else if (item == 2) {
					int r = dao.deleteAllPContacts();
					Toast.makeText(getApplicationContext(),
							"All Private Contacts Deleted..",
							Toast.LENGTH_SHORT).show();

				}
				return;
			}
		});
		builder.create().show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		displayDialog();
		this.getListView();
		return super.onOptionsItemSelected(item);
	}

	
	public void displayDialog() {

		// TODO Auto-generated method stub
		final CharSequence[] items = { "Contacts", "Call_Logs", "Input Number" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add from:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				if (item == 0) {
					Intent i = new Intent(PSConfigure.this, PContacts.class);
					startActivity(i);
				} else if (item == 1) {
					Intent i = new Intent(PSConfigure.this, PCallLogs.class);
					startActivity(i);
				}

				else if (item == 2) {
					Intent i = new Intent(PSConfigure.this, PInputNumber.class);
					startActivity(i);
				}
				return;
			}
		});
		builder.create().show();
	}
	
	@Override
	protected void onResume() {
			populateData();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
	}
}
