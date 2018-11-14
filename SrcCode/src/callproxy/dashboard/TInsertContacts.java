package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class TInsertContacts extends ListActivity {

	List<BData> items = new ArrayList<BData>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		populateContacts();
		ListAdapter la = new BMyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);
		getListView().setClickable(true);

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
					TBDispContacts.items.add(new BData(d.phone, d.name));
					Toast.makeText(getApplicationContext(), "Number added",
							Toast.LENGTH_LONG).show();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do you want To Add?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	public void populateContacts() {

		ContentResolver r = getContentResolver();
		Cursor cursor = r.query(Contacts.CONTENT_URI, null, null, null,
				Phone.DISPLAY_NAME + " ASC");

		while (cursor.moveToNext()) {
			String contactId = cursor.getString(cursor
					.getColumnIndex(ContactsContract.Contacts._ID));
			String contact = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));

			Cursor phones = getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
					null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
							+ contactId, null, null);
			while (phones.moveToNext()) {
				String number = phones
						.getString(phones
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				if (contact.equals(null)) {

					items.add(new BData(number, "[no name]"));
				} else {
					items.add(new BData(number, contact));
				}

			}
		}

		cursor.close();
		Log.i("Contacts Error", "contacts");
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

}
