package ro.pagepo.countdownpresets;

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
	public static final String FROM_SERVICE = "FROM_SERVICE";

	CountDownTimer cdt = null;
	Notification.Builder builder;

	public CountdownTimerService() {
	}

	@Override
	public IBinder onBind(Intent intent) {

		return new Binder();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		builder = new Notification.Builder(this);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setTicker("Countdown timer started");
		builder.setContentTitle("Countdown timer running");
		Intent intent = new Intent(this, CountdownActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(FROM_SERVICE, true);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		builder.setContentIntent(pendingIntent);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startForeground(NOTIFICATION_ID, builder.getNotification());
		if (cdt != null)
			cdt.cancel();
		int seconds = intent.getIntExtra(CountDownJobFragment.KEY_SECONDS_TOTAL, 60);
		cdt = new CountDownTimer(seconds * 1000, 100) {

			@Override
			public void onTick(long millisUntilFinished) {
				//Log.d("xxx", (int) (millisUntilFinished / 1000)	+ " seconds left");
				//Log.d("xxx service","tick "+millisUntilFinished+" "+SystemClock.elapsedRealtime());

				int minutes = (int) ((millisUntilFinished/1000)/60);
				int seconds = (int) (millisUntilFinished/1000 - minutes*60);
				sendTimeToReceivers(minutes, seconds);
				builder.setContentText("Time left "+minutes+" : "+seconds);
				Notification notification = builder.getNotification();
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(NOTIFICATION_ID, notification);
			}

			@Override
			public void onFinish() {
				//sendTimeToReceivers(0, 0);				;
				Log.d("xxx","finish sent broadcast"+SystemClock.elapsedRealtime());
				Intent fIntent = new Intent(CountdownTimerService.this,
						CountdownActivity.class);
				fIntent.putExtra(EXTRA_IS_FINISHED, true);
				fIntent.putExtra(FROM_SERVICE, true);
				fIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				Log.d("xxx","finish starting app"+SystemClock.elapsedRealtime());
				getApplication().startActivity(fIntent);
				this.cancel();
				stopSelf();

			}
		};
		cdt.start();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void sendTimeToReceivers(int minutes,int seconds){
		Log.d("xxx","send broadcast "+seconds+" "+SystemClock.elapsedRealtime());
		Intent intent = new Intent(CountDownJobFragment.MAIN_BROADCAST_ACTION);

		intent.putExtra(CountdownActivity.MINUTES_EXTRA, minutes);
		intent.putExtra(CountdownActivity.SECONDS_EXTRA, seconds);
		sendBroadcast(intent);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d("xxx","on destroy");
		stopForeground(true);
		if (cdt!= null) cdt.cancel();
	}

}
