package callproxy.dashboard;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BMyAdapter extends ArrayAdapter<BData> {

	Context context;
	List<BData> data;

	public BMyAdapter(Context context, int textViewResourceId, List<BData> data) {
		super(context, textViewResourceId, data);
		this.data = data;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		View v = view;
		if (v == null) {
			v = (LinearLayout) LayoutInflater.from(getContext()).inflate(
					R.layout.blistitem, parent, false);
		}

		BData d = data.get(position);
		TextView cName = (TextView) v.findViewById(R.id.empId);
		cName.setText(d.getName());
		TextView cNum = (TextView) v.findViewById(R.id.phno);
		cNum.setText("" + d.getPhone());
//		TextView ctime = (TextView) v.findViewById(R.id.datetime);
//		ctime.setText(d.getMdateTime());
		
		return v;
	}
}

