package callproxy.dashboard;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Data>{

	Context context;
	List<Data> data;
	public MyAdapter(Context context, int textViewResourceId,
			List<Data> data) {
		super(context, textViewResourceId, data);
		this.data = data;
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View v = view;
		if(v==null)
		{
			v = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.listitemtemp,parent,false);
		}
		
		Data d = data.get(position);
		TextView cName = (TextView) v.findViewById(R.id.empId);
		cName.setText(d.getName());
		TextView cNum = (TextView)v.findViewById(R.id.phno);
		cNum.setText("" + d.getPhno());
		//CheckBox c = (CheckBox) v.findViewById(R.id.cb1);
		TextView datetime = (TextView)v.findViewById(R.id.datetime);
		datetime.setText("" + d.getDtime());
		
//		if(d.isMgr)
//		{
//			c.setChecked(true);
//		}
				
		return v;
	}
}
