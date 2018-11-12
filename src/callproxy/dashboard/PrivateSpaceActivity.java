package callproxy.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class PrivateSpaceActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.privatespacemain);

		Button b1 = (Button) findViewById(R.id.viewC);
		Button b2 = (Button) findViewById(R.id.ViewM);
		Button b3 = (Button) findViewById(R.id.Pconf);
		Button b4 = (Button) findViewById(R.id.ViewSM);

		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(PrivateSpaceActivity.this,
						PSCallLogDisp.class);
				startActivity(i);
			}
		});		

		b3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(PrivateSpaceActivity.this,
						PSConfigure.class);
				startActivity(i);
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(PrivateSpaceActivity.this,
						PSMSGDisp.class);
				i.putExtra("pmsg", "inbox");
				startActivity(i);
			}
		});

		b4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(PrivateSpaceActivity.this,
						PSMSGDisp.class);
				i.putExtra("pmsg", "sent");
				startActivity(i);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
