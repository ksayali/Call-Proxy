package callproxy.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewMsgDetails extends Activity {
	BListDAO dao = null;
	int flag = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new BListDAO(this);
		dao.open();

		setContentView(R.layout.msg_dtls);

		Bundle extras = getIntent().getExtras();

		TextView tv1 = (TextView) findViewById(R.id.mphno);
		tv1.setText(extras.getString("phno"));

		TextView tv2 = (TextView) findViewById(R.id.mbody);
		tv2.setText(extras.getString("body"));

		TextView tv3 = (TextView) findViewById(R.id.mdatentime);
		tv3.setText(extras.getString("dtime"));

		Button del = (Button) findViewById(R.id.delete);

		final String phno = extras.getString("phno");
		final String dtime = extras.getString("dtime");

		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String[] params = { phno, dtime };
				int res = dao.deleteMsg(params);
				// if (res == 1)
				Toast.makeText(getApplicationContext(), "Msg Deleted.." + res,
						Toast.LENGTH_SHORT).show();
				flag = 1;
//				Intent i = new Intent(ViewMsgDetails.this,
//						ManageBlockedMsgActivity.class);
//				startActivity(i);
				finish();
			}

		});

	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
//		if (flag == 0) {
//
//			Intent i = new Intent(ViewMsgDetails.this,
//					ManageBlockedMsgActivity.class);
//			startActivity(i);
//			finish();
//		}
	}
}