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
import android.widget.ListView;
import android.widget.Toast;

public class PSMSGDisp extends ListActivity {
	/** Called when the activity is first created. */
	List<Data> items = new ArrayList<Data>();
	PSDAO dao = null;
	ListAdapter la;
	Data d;
	String status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new PSDAO(this);
		dao.open();

		Bundle extras = getIntent().getExtras();
		status = extras.getString("pmsg");

		populateData();

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
			Cursor cursor;
			if (status.equalsIgnoreCase("sent")) {
				cursor = dao.getAllPSMsgs();
			} else {
				cursor = dao.getAllPMsgs();
			}
			int cnt = cursor.getCount();

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"No Private Messages..", Toast.LENGTH_LONG).show();

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
		} catch (Exception e) {

		}
		la = new MyAdapter(this, R.layout.listitemtemp, items);
		getListView().setAdapter(la);
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
					int res;
					String[] params = { d.phno, d.dtime };
					if (status.equalsIgnoreCase("sent")) {
						res = dao.deletePSMsg(params);
					} else {
						res = dao.deletePMsg(params);
					}
					// populateData();
					if (res == 1)
						Toast.makeText(getApplicationContext(),
								"Msg Deleted..", Toast.LENGTH_SHORT).show();
					Intent i = new Intent(PSMSGDisp.this, PSMSGDisp.class);
					startActivity(i);
					finish();
				} else if (item == 1) {
					int r;
					if (status.equalsIgnoreCase("sent")) {
						r = dao.deleteAllPSMsg();
					} else {
						r = dao.deleteAllPMsg();
					}
					Toast.makeText(getApplicationContext(),
							"All Msgs Deleted..", Toast.LENGTH_LONG).show();
					Intent i = new Intent(PSMSGDisp.this, PSMSGDisp.class);
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
			int r;
			if (status.equalsIgnoreCase("sent")) {
				r = dao.deleteAllPSMsg();
			} else {
				r = dao.deleteAllPMsg();
			}
			Toast.makeText(this, "All Msgs Deleted..", Toast.LENGTH_SHORT)
					.show();
			Intent i = new Intent(PSMSGDisp.this, PSMSGDisp.class);
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
		Intent i = new Intent(PSMSGDisp.this, ViewPMsgDetails.class);
		if (status.equalsIgnoreCase("sent")) {
			i.putExtra("pmsg", "sent");
		}
		else{
			i.putExtra("pmsg", "inbox");
		}
		i.putExtra("pphno", d.phno);
		i.putExtra("pbody", d.body);
		i.putExtra("pdtime", d.dtime);

		this.startActivity(i);

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
