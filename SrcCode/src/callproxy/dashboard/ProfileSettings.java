package callproxy.dashboard;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProfileSettings extends Activity implements OnItemClickListener{
	
	private ArrayList<String> tbplist;
	TBPDAO dao;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new TBPDAO(this);
		dao.open();
		
		setContentView(R.layout.tsettings);
		populateData();
		
		ListView profiles = (ListView) findViewById(R.id.tlist);
		profiles.setOnItemClickListener(this);

	}
	
	public void populateData() {
		tbplist = new ArrayList<String>();
		tbplist.clear();
		Cursor cursor = dao.getAllProfiles();

		if (cursor.getCount() == 0) {
			Toast.makeText(getApplicationContext(), "No Profiles Created..!",
					Toast.LENGTH_SHORT).show();
			// finish();
		} else {
			while (cursor.moveToNext()) {
				tbplist.add(cursor.getString(0));

			}
		}

		ListView profiles = (ListView) findViewById(R.id.tlist);
		ArrayAdapter<String> a1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, tbplist);
		profiles.setAdapter(a1);
		profiles.setAdapter(a1);
		profiles.setItemsCanFocus(false);

		cursor.close();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) {
		// TODO Auto-generated method stub

		Toast.makeText(getApplicationContext(), "" + tbplist.get(position),
				Toast.LENGTH_LONG).show();
		Intent i = new Intent(ProfileSettings.this, ProfileDetails.class);
		i.putExtra("pname", tbplist.get(position));
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
