package ro.pagepo.countdownpresets.service;

import ro.pagepo.countdownpresets.R;
import ro.pagepo.countdownpresets.TimersActivity;
import ro.pagepo.countdownpresets.fragments.CountDownJobFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class CountdownTimerService extends Service {
	public static final int NOTIFICATION_ID = 1211;
	public static final String EXTRA_IS_FINISHED ="EXTRA_IS_FINISHED";

	CountDownTimer cdt = null;
	Notification.Builder builder;
	
	long millisecondsTimer = 0;

	public CountdownTimerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {

		return new Binder();
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}
	
	private void createNotificationBuilder(){
		builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("Countdown timer started");
		builder.setContentTitle("Countdown timer running");
		Intent pintent = new Intent(this, TimersActivity.class);
		pintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		pintent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_TIMER, (long)millisecondsTimer);
		pintent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_LEFT, (long)millisecondsTimer-1);
		
		pintent.putExtra(TimersActivity.KEY_STATE_APP, TimersActivity.STATE_FROM_NOTIFICATION);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				pintent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		builder.setContentIntent(pendingIntent);		
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int seconds = intent.getIntExtra(CountDownJobFragment.KEY_SECONDS_TOTAL, 60);
		millisecondsTimer = seconds*1000;
		createNotificationBuilder();

		
		startForeground(NOTIFICATION_ID, builder.getNotification());
		if (cdt != null)
			cdt.cancel();
		
		cdt = new CountDownTimer(millisecondsTimer, 100) {

			@Override
			public void onTick(long millisUntilFinished) {

				int minutes = (int) ((millisUntilFinished/1000)/60);
				int seconds = (int) (millisUntilFinished/1000 - minutes*60);
				sendTimeToReceivers(millisUntilFinished);
				builder.setContentText("Time left "+String.format("%02d", minutes)+ " : " + String.format("%02d", seconds));

				Notification notification = builder.getNotification();
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(NOTIFICATION_ID, notification);
			}

			@Override
			public void onFinish() {
				//sendTimeToReceivers(0, 0);				;
				Log.d("xxx","finish sent broadcast"+SystemClock.elapsedRealtime());
				Intent fIntent = new Intent(CountdownTimerService.this,
						TimersActivity.class);
				fIntent.putExtra(EXTRA_IS_FINISHED, true);
				fIntent.putExtra(TimersActivity.KEY_STATE_APP, TimersActivity.STATE_FROM_SERVICE_FINISHED);
				fIntent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_LEFT, (long)0);
				fIntent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_TIMER, (long)millisecondsTimer);
				fIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				
				getApplication().startActivity(fIntent);
				this.cancel();
				stopSelf();

			}
		};
		cdt.start();
		return super.onStartCommand(intent, flags, startId);
	}
	

	private void sendTimeToReceivers(long millisecondsLeft){
		Intent intent = new Intent(CountDownJobFragment.MAIN_BROADCAST_ACTION);
		intent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_LEFT, (long)millisecondsLeft);
		intent.putExtra(CountDownJobFragment.KEY_MILLISECONDS_TIMER, (long)this.millisecondsTimer);
		sendBroadcast(intent);		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();		
		stopForeground(true);
		if (cdt!= null) cdt.cancel();
	}

}
