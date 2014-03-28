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
import android.util.Log;

public class CountdownTimerService extends Service {
	public static final int NOTIFICATION_ID = 1211;
	public static final String KEY_SECONDS_LEFT = "KEY_SECONDS_LEFT";
	public static final String EXTRA_IS_FINISHED ="EXTRA_IS_FINISHED";

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
		builder.setContentTitle("Countdown timer started");
		Intent intent = new Intent(this, CountdownActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);
		builder.setContentIntent(pendingIntent);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		startForeground(NOTIFICATION_ID, builder.getNotification());
		if (cdt != null)
			cdt.cancel();
		int seconds = intent.getIntExtra(CountDownJobFragment.KEY_SECONDS_TOTAL, 60);
		cdt = new CountDownTimer(seconds * 1000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				Log.d("xxx", (int) (millisUntilFinished / 1000)
						+ " seconds left");
				Intent intent = new Intent(CountdownActivity.MAIN_BROADCAST_ACTION);
				intent.putExtra(KEY_SECONDS_LEFT,
						(int) (millisUntilFinished / 1000));
				sendBroadcast(intent);
				builder.setContentText((int) (millisUntilFinished / 1000)
						+ " seconds left");
				Notification notification = builder.getNotification();
				NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
				nm.notify(NOTIFICATION_ID, notification);
			}

			@Override
			public void onFinish() {
				Intent fIntent = new Intent(CountdownTimerService.this,
						CountdownActivity.class);
				fIntent.putExtra(EXTRA_IS_FINISHED, true);
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

}
