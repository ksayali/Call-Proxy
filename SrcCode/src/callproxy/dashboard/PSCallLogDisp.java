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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class PSCallLogDisp extends ListActivity {

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
						displayDialog(position);

						return true;
					}
				});
	}

	public void populateData() {
		items.clear();
		Cursor cursor = dao.getAllPLogs();
		try {
			int cnt = cursor.getCount();

			if (cnt == 0) {
				Toast.makeText(getApplicationContext(), "No Call logs Yet..",
						Toast.LENGTH_LONG).show();

			} else {

				cursor.moveToLast();
				do {

					items.add(new BData(cursor.getString(1), cursor
							.getString(0), cursor.getString(2)));
				} while (cursor.moveToPrevious());
			}
		} catch (Exception e) {

		}
		cursor.close();
		la = new BMyAdapterWD(this, R.layout.listitemtemp, items);
		getListView().setAdapter(la);
	}

	public void displayDialog(int position) {

		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] ditems = { "Delete this log", "Delete All" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose Option:");
		builder.setItems(ditems, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {

				if (item == 0) {
					d = items.get(pos);
					String[] params = { d.phone, d.mdatetime };
					int res = dao.deletePLog(params);
					if (res == 1)
						Toast.makeText(getApplicationContext(),
								"Private Log Deleted..", Toast.LENGTH_SHORT)
								.show();
				} else if (item == 1) {
					int r = dao.deleteAllPLogs();
					if (r == 1)
						Toast.makeText(getApplicationContext(),
								"All Private Logs Deleted..",
								Toast.LENGTH_SHORT).show();
				}
				return;
			}
		});
		builder.create().show();
		populateData();
	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
	}
}
