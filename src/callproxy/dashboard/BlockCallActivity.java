package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class BlockCallActivity extends ListActivity {
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
		Cursor cursor = dao.getAllBLogs();
		try {

			int cnt = cursor.getCount();

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(),
						"No Call Blocked Yet..", Toast.LENGTH_SHORT).show();

			} else {

				cursor.moveToLast();
				do {

					items.add(new Data(cursor.getString(0),
							cursor.getString(1), cursor.getString(2)));
				} while (cursor.moveToPrevious());
			}
		} catch (Exception e) {

		}
		cursor.close();
		la = new MyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);

	}

	public void displayDialog(int position) {

		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] ditems = { "Delete this log", "Delete All" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose from:");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {

				if (item == 0) {
					d = items.get(pos);
					String[] params = { d.phno, d.dtime };
					int res = dao.deleteBlog(params);
					// populateData();
					if (res == 1)
						Toast.makeText(getApplicationContext(),
								"Log Deleted..", Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(getApplicationContext(),
								"Log Not Deleted..", Toast.LENGTH_SHORT).show();
					populateData();
					
				} else if (item == 1) {
					int r = dao.deleteAllBLogs();
					if (r > 0)
						Toast.makeText(getApplicationContext(),
								"All Logs Deleted..", Toast.LENGTH_SHORT)
								.show();
					populateData();

				}
				return;
			}
		});
		builder.create().show();

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