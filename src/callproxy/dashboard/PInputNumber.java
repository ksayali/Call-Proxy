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

public class PInputNumber extends Activity {

	PSDAO dao = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inputnumber);
		dao = new PSDAO(this);
		dao.open();

		Button b1 = (Button) findViewById(R.id.add);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TextView phno = (TextView) findViewById(R.id.bphno);
				TextView name = (TextView) findViewById(R.id.bname);

				String pphone = phno.getText().toString();

				if (pphone.length() >= 10 && pphone.length() < 12) {
					Cursor cursor = dao.getPCNo(new String[] { phno.getText()
							.toString() });
					if (cursor.getCount() == 0) {
						String nm = name.getText().toString();
						if (nm.equals("")) {
							nm = "[No Name]";
						}
						ContentValues cv = new ContentValues();
						cv.put(Constants.PNAME, nm);
						cv.put(Constants.PPH, phno.getText().toString());
						long id = dao.insertPContact(cv);
						if (id == -1) {
							Toast.makeText(getApplicationContext(),
									"Number could not be added",
									Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(),
									"Number added", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(
								getApplicationContext(),
								"This Number is Already a Private contact!",
								Toast.LENGTH_SHORT).show();
					}
					cursor.close();
					finish();
				} else {
					phno.setText("");
					Toast.makeText(getApplicationContext(),
							"Please Insert a Valid Phone Number..", Toast.LENGTH_LONG)
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