package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class WhiteList extends ListActivity {

	List<BData> items = new ArrayList<BData>();
	BListDAO dao = null;
	BData d;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		dao = new BListDAO(this);
		dao.open();

		populateData();

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						dispDiaEditDel(position);
						return true;
					}
				});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add More Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		displayDialog();
		this.getListView();
		return super.onOptionsItemSelected(item);
	}

	public void populateData() {
		items.clear();
		Cursor cursor = dao.getAllWList();

		if (cursor.getCount() == 0) {
			Toast.makeText(getApplicationContext(), "White List Empty !",
					Toast.LENGTH_SHORT).show();
		} else {
			while (cursor.moveToNext()) {
				items.add(new BData(cursor.getString(1), cursor.getString(0)));
			}
		}
		cursor.close();
		ListAdapter la = new BMyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);

	}

	public void displayDialog() {

		// TODO Auto-generated method stub
		final CharSequence[] items = { "Contacts", "Call_Logs", "Input Number" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add from:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				if (item == 0) {
					Intent i = new Intent(WhiteList.this, WLContactsDisp.class);
					startActivity(i);
//					finish();
				} else if (item == 1) {
					Intent i = new Intent(WhiteList.this, WLCallLogDisp.class);
					startActivity(i);
//					finish();
				}

				else if (item == 2) {
					Intent i = new Intent(WhiteList.this, WInputNumber.class);
					startActivity(i);
//					finish();
				}
				return;
			}
		});
		builder.create().show();
	}

	public void dispDiaEditDel(int position) {
		final int pos = position;
		final CharSequence[] ditems = { "Delete", "Edit","Delete All"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select Option");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {

				d = items.get(pos);
				String num = d.phone;
				String nm = d.name;

				if (item == 0) {
					int id = dao.deleteNumberFromWlist(num);
					if (id == -1) {
						Toast.makeText(getApplicationContext(),
								"Number could not be deleted",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(getApplicationContext(),
								"Number deleted", Toast.LENGTH_SHORT).show();
						populateData();
//						Intent i = new Intent(WhiteList.this, WhiteList.class);
//						startActivity(i);
//						finish();
					}
				} else if (item == 1) {
					Intent i = new Intent(WhiteList.this, EditContact.class);
					i.putExtra("phno", num);
					i.putExtra("name", nm);
					i.putExtra("flag", "2");
					startActivity(i);
					finish();
				}else if(item == 2){
					int r = dao.deleteAllWlcontacts();
					if (r > 0)
						Toast.makeText(getApplicationContext(),
								"All contacts Deleted..", Toast.LENGTH_SHORT)
								.show();
					populateData();
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
