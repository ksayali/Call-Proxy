package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class WLCallLogDisp extends ListActivity {
	/** Called when the activity is first created. */

	BListDAO dao = null;
	List<BData> items = new ArrayList<BData>();

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

	public void displayDialog(int position) {
		final int pos = position;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					BData d = items.get(pos);
					// Yes button clicked
					String[] parms = { d.phone };
					Cursor c = dao.getWlistNo(parms);

					if (c.getCount() == 0) {
						ContentValues cv = new ContentValues();
						cv.put(Constants.WNAME, d.name);
						cv.put(Constants.WPH, d.phone);
						long id = dao.insertInWlist(cv);
						if (id == -1) {
							Toast.makeText(getApplicationContext(),
									"Number could not be added",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Number added", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Number Already Exists !",
								Toast.LENGTH_SHORT).show();
					}
					c.close();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Add Number to White List?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	public void populateData() {
		items.clear();
		Cursor cursor = null;
		Uri Uricalls = Uri.parse("content://call_log/calls");
		String[] strFields = { android.provider.CallLog.Calls.CACHED_NAME,
				android.provider.CallLog.Calls.NUMBER, android.provider.CallLog.Calls.DATE };

		cursor = this.getContentResolver().query(Uricalls, strFields, null,
				null, null);
		int cnt = cursor.getCount();
		if (cnt != 0) {
			cursor.moveToLast();

			do {
				int secondindex = cursor.getColumnIndex(Calls.DATE);
				long seconds = cursor.getLong(secondindex);
				SimpleDateFormat formatter = new SimpleDateFormat(
						"dd-MM-yy HH:mm");
				String dateString = formatter.format(new Date(seconds));
				try {
					String contact = cursor.getString(0);
					if (contact.equals(null)) {

						items.add(new BData(cursor.getString(1), "[no name]", dateString));
					} else {
						items.add(new BData(cursor.getString(1), cursor
								.getString(0), dateString));
					}

				} catch (NullPointerException e) {
					items.add(new BData(cursor.getString(1), "[no name]", dateString));
					Log.i("Error..", "Exception");
					// cnt--;

				}
			} while (cursor.moveToPrevious());
		} else {
			Toast.makeText(getApplicationContext(),
					"Call Logs Empty..!",
					Toast.LENGTH_LONG).show();
		}
		cursor.close();

		ListAdapter la = new BMyAdapterWD(this, R.layout.listitemtemp, items);
		getListView().setAdapter(la);
	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
//		Intent i = new Intent(WLCallLogDisp.this, WhiteList.class);
//		startActivity(i);
		finish();
	}
}
