package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.PhoneLookup;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class BLContactsDisp extends ListActivity {
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
					Cursor cursor = dao.getBlistNo(new String[] { d.phone });
					if (cursor.getCount() == 0) {
						ContentValues cv = new ContentValues();
						cv.put(Constants.BNAME, d.name);
						cv.put(Constants.BPH, d.phone);
						long id = dao.insertInBlist(cv);
						if (id == -1) {
							Toast.makeText(getApplicationContext(),
									"Number could not be added",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Number added", Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(getApplicationContext(),
								"Number already exists!!", Toast.LENGTH_SHORT)
								.show();

					}
					cursor.close();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
				// Intent i = new Intent(BLContactsDisp.this, BlackList.class);
				// startActivity(i);
				// finish();
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do You Want to Add?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	public void populateData() {

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
			phones.close();
		}

		cursor.close();
		ListAdapter la = new BMyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);

	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
		// Intent i = new Intent(BLContactsDisp.this, BlackList.class);
		// startActivity(i);
		finish();
	}
}