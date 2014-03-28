package ro.pagepo.countdownpresets;

import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class CountDownJobFragment extends Fragment implements
		CountdownJobCallback {
	
	long milliseconds = 0;
	CountDownTimerJob cdtj =null;
	Notification.Builder nb =null;
	
	public static final int NOTIFICATION_ID =1211;
	public static final String KEY_SECONDS_TOTAL ="KEY_SECONDS_TOTAL";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countdown, container,false);
				
		Button butCancel = (Button) view.findViewById(R.id.butCancelTimer);
		butCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopTimer();
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

	@Override
	public void onTimerThick(long millisUntilFinished) {
		displayTimeLeft(millisUntilFinished);
		setNotification(formatTime(millisUntilFinished));
	}
	
	public void startTimer(){
		cdtj = new CountDownTimerJob(this.milliseconds, 1000, this);
		cdtj.start();
		
		setNotification("");

	}
	
	public void stopTimer(){
		cdtj.cancel();
	}
	
	@SuppressWarnings("deprecation")
	private void setNotification(String text){
		if (nb == null){
			nb = new Notification.Builder(getActivity());
			nb.setContentTitle("Countdown Timer");
			nb.setOngoing(true);
			nb.setSmallIcon(R.drawable.ic_launcher);
			nb.setTicker("Countdown Timer started ... ");
		}
		nb.setContentText(text);
		NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		Notification not = nb.getNotification();
		nm.notify(NOTIFICATION_ID, not);		
		Log.d("xxl",NOTIFICATION_ID+"");
	}
	
	private void cancelNotification(){
		NotificationManager nm = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_ID);
		nb = null;
	}

	@Override
	public void onTimerFinish() {
		TextView txtTimeLeft = (TextView) getView().findViewById(R.id.txtTimeLeft);
		txtTimeLeft.setText("Finished");
		cancelNotification();
	}
	
	public void setMillisecondsTimer(int minutes,int seconds){		
		this.milliseconds = minutes*60*1000+seconds*1000;
		displayTimeLeft(this.milliseconds);
	}
	
	private void displayTimeLeft(long millis){
		if (getView()== null) return;
		TextView txtTimeLeft = (TextView) getView().findViewById(R.id.txtTimeLeft);
		if (txtTimeLeft == null) return;
		if (millis == 0 ) return;

		txtTimeLeft.setText(formatTime(millis));
	}
	
	private String formatTime(long millis){
		int minutes = (int) ((millis/1000)/60);
		int seconds = (int) (millis/1000 - minutes*60);
		return minutes+" : "+seconds;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		cdtj.cancel();
		cancelNotification();
	}
	
	public void showFinished(){
		
	}

}
