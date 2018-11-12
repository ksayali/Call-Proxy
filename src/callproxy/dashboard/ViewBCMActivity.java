package callproxy.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ViewBCMActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blockcallmsg);

		Button b1 = (Button) findViewById(R.id.vbc);
		Button b2 = (Button) findViewById(R.id.vbm);
		
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ViewBCMActivity.this, BlockCallActivity.class);
		         startActivity(i);
			}
		});
		
		b2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(ViewBCMActivity.this, ManageBlockedMsgActivity.class);
		         startActivity(i);
			}
		});
	}
}