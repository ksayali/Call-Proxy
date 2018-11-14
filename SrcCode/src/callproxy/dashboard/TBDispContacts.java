package callproxy.dashboard;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

public class TBDispContacts extends ListActivity {

	public static List<BData> items = new ArrayList<BData>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		ListAdapter la = new BMyAdapter(this, R.layout.blistitem, items);
		getListView().setAdapter(la);
		getListView().setClickable(true);

		Bundle extras = getIntent().getExtras();
		String dtls = extras.getString("Details");

		if (dtls.equalsIgnoreCase("true")) {
		} else {

			if (items.size() == 0) {
				Toast.makeText(this,
						"To Insert Contacts Click on Option Button..",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void displayDialog(int position) {
		final int pos = position;
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					// Yes button clicked
					items.remove(pos);
					populateData();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					// No button clicked
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Do You Want To Delete ?")
				.setPositiveButton("Yes", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 1, 0, "Add Contacts");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		displayaddDialog();
		this.getListView();
		return super.onOptionsItemSelected(item);
	}

	public void displayaddDialog() {

		// TODO Auto-generated method stub
		final CharSequence[] items = { "Contacts", "Call_Logs", "Input Number" };
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add from:");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int item) {
				if (item == 0) {
					Intent it1 = new Intent(TBDispContacts.this,
							TInsertContacts.class);
					startActivity(it1);
				} else if (item == 1) {
					Intent it2 = new Intent(TBDispContacts.this,
							TInsCallLog.class);
					startActivity(it2);
				}

				else if (item == 2) {
					Intent it3 = new Intent(TBDispContacts.this,
							TInputNumber.class);
					startActivity(it3);
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
		super.onDestroy();
	}

}
