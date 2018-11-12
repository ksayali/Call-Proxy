package callproxy.dashboard;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditContact extends Activity {
	BListDAO dao1 = null;
	PSDAO dao2 = null;
	Intent i = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		dao1 = new BListDAO(this);
		dao1.open();
		dao2 = new PSDAO(this);
		dao2.open();

		super.onCreate(savedInstanceState);

		setContentView(R.layout.inputnumber);

		Bundle extras = getIntent().getExtras();
		final String num = extras.getString("phno");
		final String nm = extras.getString("name");
		final String flag = extras.getString("flag");

		final TextView tv1 = (TextView) findViewById(R.id.bphno);
		tv1.setText(num);
		final TextView tv2 = (TextView) findViewById(R.id.bname);
		tv2.setText(nm);

		Button edit = (Button) findViewById(R.id.add);
		edit.setText("Update");
		edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String newNum = tv1.getText().toString();
				String newName = tv2.getText().toString();
				int choice = Integer.parseInt(flag);
				ContentValues cv = new ContentValues();
				switch (choice) {
				case 1: // Edit Black List Number
					cv.put(Constants.BNAME, newName);
					cv.put(Constants.BPH, newNum);
					int id = dao1.editNumberFomBlist(num, cv);
					if (id == 0) {
						Toast.makeText(EditContact.this, "Contact not Edited",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(EditContact.this, "Contact Updated",
								Toast.LENGTH_SHORT).show();
					}
					i = new Intent(EditContact.this, BlackList.class);
					break;

				case 2: // Edit White List Number
					cv.put(Constants.WNAME, newName);
					cv.put(Constants.WPH, newNum);
					id = dao1.editNumberFomWlist(num, cv);
					if (id == 0) {
						Toast.makeText(EditContact.this,
								"Contact could not be Updated",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(EditContact.this, "Contact Updated",
								Toast.LENGTH_SHORT).show();
					}
					i = new Intent(EditContact.this, WhiteList.class);
					break;

				case 3: // Edit Private Space Contact
					cv.put(Constants.PNAME, newName);
					cv.put(Constants.PPH, newNum);
					id = dao2.editNumberFomPC(num, cv);
					if (id == 0) {
						Toast.makeText(EditContact.this,
								"Contact could not be Updated",
								Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(EditContact.this, "Contact Updated",
								Toast.LENGTH_SHORT).show();
					}
					i = new Intent(EditContact.this, PSConfigure.class);
					break;
				}
				startActivity(i);
				finish();

			}

		});
	}

	@Override
	public void onDestroy() {
		dao1.close();
		dao2.close();
		super.onDestroy();
	}

}
