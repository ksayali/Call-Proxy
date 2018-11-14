package callproxy.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPMsgDetails extends Activity {
	PSDAO dao = null;
	int flag = 0;
	String status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dao = new PSDAO(this);
		dao.open();

		setContentView(R.layout.msg_pdtls);

		Bundle extras = getIntent().getExtras();
		status = extras.getString("pmsg");

		TextView tv1 = (TextView) findViewById(R.id.pphno);
		tv1.setText(extras.getString("pphno"));

		final String phoneNo = tv1.getText().toString();

		TextView tv2 = (TextView) findViewById(R.id.pbody);
		tv2.setText(extras.getString("pbody"));

		TextView tv3 = (TextView) findViewById(R.id.pdatentime);
		tv3.setText(extras.getString("pdtime"));

		Button del = (Button) findViewById(R.id.pdelete);

		Button reply = (Button) findViewById(R.id.prly);

		if (status.equalsIgnoreCase("sent")) {
			reply.setVisibility(1);
		}

		final String phno = extras.getString("pphno");
		final String dtime = extras.getString("pdtime");

		del.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String[] params = { phno, dtime };
				int res = dao.deletePMsg(params);
				// if (res == 1)
				Toast.makeText(getApplicationContext(), "Msg Deleted.." + res,
						Toast.LENGTH_SHORT).show();
				flag = 1;
				Intent i = new Intent(ViewPMsgDetails.this, PSMSGDisp.class);
				startActivity(i);
				finish();
			}

		});

		reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ViewPMsgDetails.this,
						SMSReplyActivity.class);
				i.putExtra("phoneNo", phoneNo);
				startActivity(i);
			}
		});

	}

	@Override
	public void onDestroy() {
		dao.close();
		super.onDestroy();
	}
}
