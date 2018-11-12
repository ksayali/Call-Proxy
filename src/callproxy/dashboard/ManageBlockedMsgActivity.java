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
import android.widget.ListView;
import android.widget.Toast;

public class ManageBlockedMsgActivity extends ListActivity {
	/** Called when the activity is first created. */
	List<Data> items = new ArrayList<Data>();
	BListDAO dao = null;
	ListAdapter la;
	Data d;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new BListDAO(this);
		dao.open();
		populateData();

		la = new MyAdapter(this, R.layout.listitem2, items);
		getListView().setAdapter(la);

		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {
					public boolean onItemLongClick(AdapterView<?> parent,
							View v, int position, long id) {
						// Do some
						displayDialog(position);

						return true;
					}
				});

	}

	public void populateData() {
		items.clear();
		try {

			Cursor cursor = dao.getAllMsgs();
			int cnt = cursor.getCount();

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"No Blocked Messages..", Toast.LENGTH_SHORT).show();

			} else {
				while (cnt != 0) {
					if (cursor.moveToNext()) {

						items.add(new Data(cursor.getString(0), cursor
								.getString(1), cursor.getString(2), cursor
								.getString(3)));
					}
					cnt--;
				}
			}

			cursor.close();
			la = new MyAdapter(this, R.layout.blistitem, items);
			getListView().setAdapter(la);

		} catch (Exception e) {

		}
	}

	public void displayDialog(int position) {

		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] ditems = { "Delete this Message", "Delete All" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose from:");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {

				if (item == 0) {
					d = items.get(pos);
					String[] params = { d.phno, d.dtime };
					int res = dao.deleteMsg(params);
					// if (res == 1)
					Toast.makeText(getApplicationContext(), "Msg Deleted..",
							Toast.LENGTH_SHORT).show();
					// populateData();
					Intent i = new Intent(ManageBlockedMsgActivity.this,
							ManageBlockedMsgActivity.class);
					startActivity(i);
					finish();
				} else if (item == 1) {
					int r = dao.deleteAllMsg();
					Toast.makeText(getApplicationContext(),
							"All Msgs Deleted..", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(ManageBlockedMsgActivity.this,
							ManageBlockedMsgActivity.class);
					startActivity(i);
					finish();
				}
				return;
			}
		});
		builder.create().show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Delete All");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 1:
			int r = dao.deleteAllMsg();
			Toast.makeText(this, "All Msgs Deleted..", Toast.LENGTH_SHORT)
					.show();
			Intent i = new Intent(ManageBlockedMsgActivity.this,
					ManageBlockedMsgActivity.class);
			startActivity(i);
			finish();
			break;
		}
		this.getListView();
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		d = items.get(position);

		// Toast.makeText(this, d.body, Toast.LENGTH_LONG).show();

		Intent i = new Intent(ManageBlockedMsgActivity.this,
				ViewMsgDetails.class);

		i.putExtra("phno", d.phno);
		i.putExtra("body", d.body);
		i.putExtra("dtime", d.dtime);

		this.startActivity(i);
		// finish();
		Log.i("Error list", "List...");
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