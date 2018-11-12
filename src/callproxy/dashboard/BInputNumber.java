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

public class BInputNumber extends Activity {

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

				String bphone = phno.getText().toString();
				if (bphone.length() >= 10 && bphone.length() < 12) {
					Cursor cursor = dao.getBlistNo(new String[] { phno
							.getText().toString() });
					if (cursor.getCount() == 0) {
						String bname = name.getText().toString();
						if (bname.equals("")) {
							bname = "[No Name]";
						}
						ContentValues cv = new ContentValues();
						cv.put(Constants.BNAME, bname);
						cv.put(Constants.BPH, phno.getText().toString());
						long id = dao.insertInBlist(cv);
						if (id == -1) {
							Toast.makeText(getApplicationContext(),
									"Number(s) could not be added",
									Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Number(s) added", Toast.LENGTH_SHORT)
									.show();
						}
						cursor.close();
//						Intent i = new Intent(BInputNumber.this, BlackList.class);
//						startActivity(i);
						finish();
					} else {
						Toast.makeText(getApplicationContext(),
								"Number Already Exist In Black List",
								Toast.LENGTH_SHORT).show();
					}
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
