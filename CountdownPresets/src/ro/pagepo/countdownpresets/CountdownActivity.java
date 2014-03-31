package ro.pagepo.countdownpresets;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class CountdownActivity extends Activity {
	public static final String MINUTES_EXTRA = "MINUTES_EXTRA";
	public static final String SECONDS_EXTRA = "SECONDS_EXTRA";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		int minutes = this.getIntent().getIntExtra(MINUTES_EXTRA,0);
		int seconds = this.getIntent().getIntExtra(SECONDS_EXTRA, 0);
		Log.d("xxx",minutes+":"+seconds);
		boolean xxl = getIntent().getBooleanExtra(CountdownTimerService.FROM_SERVICE, false);
		CountDownJobFragment frg =  (CountDownJobFragment) getFragmentManager().findFragmentById(R.id.fragmentCountDown);
		frg.setMillisecondsTimer(minutes, seconds);
		if (!xxl) frg.startTimer();
	}
	
}
