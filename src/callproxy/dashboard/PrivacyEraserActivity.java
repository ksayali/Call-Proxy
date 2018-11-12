package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PrivacyEraserActivity extends ListActivity {
	/** Called when the activity is first created. */

	List<BData> items = new ArrayList<BData>();
	ArrayList<String> phonelist;
	BData d;
	ListView lv;
	AlertDialog alert;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateData();

		ListAdapter la = new BMyAdapterWD(this, R.layout.listitemtemp, items);

		getListView().setAdapter(la);
		getListView().setClickable(true);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		final int pos = position;
		final CharSequence[] MItems = { "Only Call Logs", "Only SMS",
				"Call Logs & SMS" };

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Delete");
		builder.setSingleChoiceItems(MItems, -1,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						int i = -1;
						d = items.get(pos);

						String dNum = d.phone;

						if (item == 0) {
							i = delCalls(dNum);
							Toast.makeText(PrivacyEraserActivity.this,
									i + "Rows Deleted", Toast.LENGTH_SHORT)
									.show();
						} else if (item == 1) {

							i = delSMS(dNum);
							if (i == -1) {
								Toast.makeText(PrivacyEraserActivity.this,
										"No SMS from this Number!",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(PrivacyEraserActivity.this,
										i + " Messages Deleted",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							i = delCalls(dNum);
							Toast.makeText(PrivacyEraserActivity.this,
									i + " Logs Deleted", Toast.LENGTH_SHORT)
									.show();
							i = delSMS(dNum);
							if (i == -1) {
								Toast.makeText(PrivacyEraserActivity.this,
										"No SMS from this Number!",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(PrivacyEraserActivity.this,
										i + " Messages Deleted",
										Toast.LENGTH_SHORT).show();
							}
						}

						populateData();
						finish();
						Intent it = new Intent(PrivacyEraserActivity.this,
								PrivacyEraserActivity.class);
						startActivity(it);
						alert.dismiss();
						finish();
					}
				});
		alert = builder.create();
		alert.show();

	}

	public void populateData() {
		Cursor cursor = null;
		Uri Uricalls = Uri.parse("content://call_log/calls");
		String[] strFields = { android.provider.CallLog.Calls.CACHED_NAME,
				android.provider.CallLog.Calls.NUMBER,android.provider.CallLog.Calls.DATE };

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
					Log.i("Error..", "Exception null ptr");
				}
			} while (cursor.moveToPrevious());
		} else {
			Toast.makeText(getApplicationContext(), "Call Logs Empty..!",
					Toast.LENGTH_LONG).show();
		}
		cursor.close();
	}

	public int delCalls(String dNum) {
		String strUriCalls = "content://call_log/calls";
		Uri UriCalls = Uri.parse(strUriCalls);

		int i = PrivacyEraserActivity.this.getContentResolver().delete(
				UriCalls, "NUMBER = '" + dNum + "'", null);
		return i;
	}

	public int delSMS(String dNum) {
		int i = -1;
		Uri uriSMSURI = Uri.parse("content://sms/inbox");
		Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,
				null);

		while (cur.moveToNext()) {
			if (cur.getString(2).equals(dNum)) {
				try {
					long thread_id = cur.getLong(1); // get the thread_id
					i = this.getBaseContext()
							.getContentResolver()
							.delete(Uri.parse("content://sms/conversations/"
									+ thread_id), null, null);
					break;
				} catch (Exception e) {

				}
			}
			cur.close();
		}
		return i;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}
}
