package callproxy.dashboard;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SMSReplyActivity extends Activity{
    Button btnSendSMS;
    EditText txtPhoneNo;
    EditText txtMessage;
    PSDAO dao = null;
    
    private BroadcastReceiver rec;
    private BroadcastReceiver recD;
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
    	dao = new PSDAO(SMSReplyActivity.this);
		dao.open();
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smsreply);        
 
        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
        txtMessage = (EditText) findViewById(R.id.txtMessage);
        
        Bundle extras = getIntent().getExtras();
        String ph = extras.getString("phoneNo");
        txtPhoneNo.setText(ph);
        
        try
        {
        btnSendSMS.setOnClickListener(new View.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	Calendar cal = Calendar.getInstance();
        		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        		String mdatetime = df.format(cal.getTime());
        		
                String phoneNo = txtPhoneNo.getText().toString();
                String message = txtMessage.getText().toString();
                
                if (phoneNo.length()>0 && message.length()>0)
                {
                    sendSMS(phoneNo, message);
                    //String name = dao.getBLName(new String [] {phoneno});
					ContentValues cv = new ContentValues();
					cv.put(Constants.NAME, "[No Name]");
					cv.put(Constants.PHONENO, phoneNo);
					cv.put(Constants.BODY, message);
					cv.put(Constants.MDATETIME, mdatetime);

					long id = dao.insertPSMsg(cv);
					if (id == -1) {
						Toast.makeText(SMSReplyActivity.this, "Row not inserted in Sent PMsg",
								Toast.LENGTH_SHORT).show();
					} else {
				//		Toast.makeText(context, "Row inserted",	Toast.LENGTH_LONG).show();
					}

                }
                else
                    Toast.makeText(getBaseContext(), 
                        "Please enter both phone number and message.", 
                        Toast.LENGTH_SHORT).show();
            }
        });
        }
        catch(NullPointerException ne)
        {
        	Log.i("SMS Reply", "Exception");
        }
    }    
    private void sendSMS(String phoneNumber, String message)
    {        
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";
 
        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);
 
        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);
 
        rec = new BroadcastReceiver() {
        	//---when the SMS has been sent---	
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				// TODO Auto-generated method stub
				switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent", 
                                Toast.LENGTH_SHORT).show();
                        finish();
                        
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off", 
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
			
        };
		
		registerReceiver(rec,new IntentFilter(SENT));
        
 
        //---when the SMS has been delivered---
		recD = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				// TODO Auto-generated method stub
				switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered", 
                                Toast.LENGTH_SHORT).show();
                        break;                        
                }
				
			}
		};
		registerReceiver(recD, new IntentFilter(DELIVERED));
 
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        
    }
    
    @Override
    public void onBackPressed()
    {
    	finish();
    }
    
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dao.close();
		unregisterReceiver(rec);
		unregisterReceiver(recD);
	}
       
}

