package callproxy.dashboard;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WInputNumber extends Activity {

	BListDAO dao = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputnumber);
		dao = new BListDAO(this);
		dao.open();

		Button b1 = (Button) findViewById(R.id.add);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TextView phno = (TextView) findViewById(R.id.bphno);
				TextView name = (TextView) findViewById(R.id.bname);

				String wphone = phno.getText().toString();
				if (wphone.length() >= 10 && wphone.length() < 12) {
					Cursor cursor = dao.getWlistNo(new String[] { phno
							.getText().toString() });
					if (cursor.getCount() == 0) {
						String wname = name.getText().toString();
						if (wname.equals("")) {
							wname = "[No Name]";
						}
						ContentValues cv = new ContentValues();
						cv.put(Constants.WNAME, wname);
						cv.put(Constants.WPH, phno.getText().toString());
						long id = dao.insertInWlist(cv);
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
								"Number Already Exists !", Toast.LENGTH_SHORT)
								.show();
					}
					cursor.close();
//					Intent i = new Intent(WInputNumber.this, WhiteList.class);
//					startActivity(i);
					finish();
				} else {
					phno.setText("");
					Toast.makeText(getApplicationContext(),
							"Please Insert Valid Phone Number..", Toast.LENGTH_LONG)
							.show();
				}
			}
		});
	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
	}

}
