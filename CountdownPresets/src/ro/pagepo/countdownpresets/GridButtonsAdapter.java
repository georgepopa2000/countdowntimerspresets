package ro.pagepo.countdownpresets;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

public class GridButtonsAdapter extends BaseAdapter  {
	
	ArrayList<TimerInfo> al = null;
	Context mContext;
	
	

	public GridButtonsAdapter(Context context,ArrayList<TimerInfo> al) {
		this.al = al;
		this.mContext = context;
	}



	@Override
	public int getCount() {
		return al.size();
	}



	@Override
	public Object getItem(int position) {
		return al.get(position);
	}



	@Override
	public long getItemId(int position) {
		return 0;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Button button;
		if (convertView == null){
			button = new Button(mContext);
			button.setText(al.get(position).getName());
			//button.setLayoutParams(new GridView.LayoutParams(85, 85));
			//button.setPadding(8, 8, 8, 8);
		} else {
			button = (Button) convertView;
		}
		return button;
	}

	

}
