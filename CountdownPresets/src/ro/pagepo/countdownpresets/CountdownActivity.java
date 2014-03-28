package ro.pagepo.countdownpresets;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class CountdownActivity extends Activity {
	public static final String MINUTES_EXTRA = "MINUTES_EXTRA";
	public static final String SECONDS_EXTRA = "SECONDS_EXTRA";
	public static final String MAIN_BROADCAST_ACTION = "ro.pagepo.countdownpresets.CountdownActivity.receiver.action";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_countdown);
		int minutes = this.getIntent().getIntExtra(MINUTES_EXTRA,0);
		int seconds = this.getIntent().getIntExtra(SECONDS_EXTRA, 0);
		CountDownJobFragment frg =  (CountDownJobFragment) getFragmentManager().findFragmentById(R.id.fragmentCountDown);
		frg.setMillisecondsTimer(minutes, seconds);
		frg.startTimer();
	}

}
