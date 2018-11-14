package callproxy.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TInputNumber extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tinputnumber);
		Button b1 = (Button) findViewById(R.id.add);
		b1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText phno = (EditText) findViewById(R.id.tbno);
				EditText name = (EditText) findViewById(R.id.tbname);
				String tphone = phno.getText().toString();
				String tname = name.getText().toString();

				if (tphone.length() >= 10 && tphone.length() < 12) {
					if (tname.equals("")) {
						tname = "[No Name]";
					}
					TBDispContacts.items.add(new BData(tphone, tname));
					finish();
				} else {
					phno.setText("");
					Toast.makeText(getApplicationContext(),
							"Please Insert Valid Phone Number..",
							Toast.LENGTH_LONG).show();
				}

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
