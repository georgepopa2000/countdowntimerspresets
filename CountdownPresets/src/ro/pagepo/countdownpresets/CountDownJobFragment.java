package ro.pagepo.countdownpresets;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CountDownJobFragment extends Fragment  {
	
	long milliseconds = 0;

	public static final String KEY_SECONDS_TOTAL ="KEY_SECONDS_TOTAL";
	public static final String MAIN_BROADCAST_ACTION = "ro.pagepo.countdownpresets.CountdownActivity.receiver.action";
	
	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			displayTimeLeft(intent
					.getIntExtra(CountdownActivity.MINUTES_EXTRA, 0)*60*1000+intent
					.getIntExtra(CountdownActivity.SECONDS_EXTRA, 0)*1000);
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countdown, container,false);
				
		Button butCancel = (Button) view.findViewById(R.id.butCancelTimer);
		butCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopTimer();
				Ringtone r = CountDownJobFragment.this.r;
				if (r!= null) r.stop();			
				getActivity().finish();				
			}
		});
		
		
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		displayTimeLeft(milliseconds);		
	}
	
	Ringtone r = null;
	@Override
	public void onResume() {
		super.onResume();
		Log.d("xxx", "on resume");
		boolean finished = getActivity().getIntent().getBooleanExtra(CountdownTimerService.EXTRA_IS_FINISHED, false);
		
		if (finished){
			displayTimeLeft(0);
			Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			 r = RingtoneManager.getRingtone(this.getActivity(), uri);
			r.play();
			//startFlashBackground();
			displayTimeLeft(0);
		}
		else
			displayTimeLeft(milliseconds);

		
		getActivity().registerReceiver(br, new IntentFilter(MAIN_BROADCAST_ACTION));	
	}

	public void startTimer(){
		startTimerService();
	}
	
	public void startTimerService() {
		Log.d("xxx", "set start serv");
		Intent sintent = new Intent(this.getActivity(),
				CountdownTimerService.class);
		sintent.putExtra(KEY_SECONDS_TOTAL, (int)(this.milliseconds/1000));
		ComponentName srv = getActivity().startService(sintent);
		Log.d("xxx", "service is " + srv);
	}

	
	public void stopTimer(){
		Intent sintent = new Intent(this.getActivity(),
				CountdownTimerService.class);		
		getActivity().stopService(sintent);	
	}
	


	public void setMillisecondsTimer(int minutes,int seconds){		
		this.milliseconds = minutes*60*1000+seconds*1000;
		displayTimeLeft(this.milliseconds);
	}
	
	
	private void displayTimeLeft(long millis){
		if (getView()== null) return;
		TextView txtTimeLeft = (TextView) getView().findViewById(R.id.txtTimeLeft);
		if (txtTimeLeft == null) return;
		//if (millis == 0 ) return;

		txtTimeLeft.setText(formatTime(millis));
	}
	
	private String formatTime(long millis){
		int minutes = (int) ((millis/1000)/60);
		int seconds = (int) (millis/1000 - minutes*60);
		return minutes+" : "+seconds;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(br);
	}
	
}
