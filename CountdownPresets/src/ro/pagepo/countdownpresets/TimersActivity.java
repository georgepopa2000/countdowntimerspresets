package ro.pagepo.countdownpresets;

import ro.pagepo.countdownpresets.fragments.CountDownJobFragment;
import ro.pagepo.countdownpresets.fragments.TimerButtonsFragment;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class TimersActivity extends Activity {
	
	public static final int STATE_FROM_NOTIFICATION = 1;
	public static final int STATE_FROM_SERVICE = 2;
	public static final int STATE_FROM_SERVICE_FINISHED = 2;
	
	public static final String TAG_FRAGMENT_TIMERS ="TAG_FRAGMENT_TIMERS";
	public static final String TAG_FRAGMENT_COUNTDOWN = "TAG_FRAGMENT_COUNTDOWN";
	
	public static final String MINUTES_EXTRA = "MINUTES_EXTRA";
	public static final String SECONDS_EXTRA = "SECONDS_EXTRA";
	
	public static final String KEY_STATE_APP = "KEY_STATE_APP";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timers);
		
		
		if (getIntent().getIntExtra(KEY_STATE_APP, 0) == STATE_FROM_NOTIFICATION) {
			int minutes = getIntent().getIntExtra(TimersActivity.MINUTES_EXTRA, 5);
			int seconds = getIntent().getIntExtra(TimersActivity.SECONDS_EXTRA, 0);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			//CountDownJobFragment.newInstance(, `)
			CountDownJobFragment frg =  CountDownJobFragment.newInstance(0,false);
			frg.setMillisecondsTimer(minutes, seconds);
			//ft.addToBackStack(null);
			ft.replace(R.id.container, frg,TAG_FRAGMENT_COUNTDOWN);			
			ft.commit();			
		} else 
		
		if (getIntent().getIntExtra(KEY_STATE_APP, 0) == STATE_FROM_SERVICE_FINISHED) {
			int minutes = getIntent().getIntExtra(TimersActivity.MINUTES_EXTRA, 5);
			int seconds = getIntent().getIntExtra(TimersActivity.SECONDS_EXTRA, 0);
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			//CountDownJobFragment.newInstance(, `)
			ft.add(R.id.container,new TimerButtonsFragment(), TAG_FRAGMENT_TIMERS);
			CountDownJobFragment frg =  CountDownJobFragment.newInstance(0,false);
			frg.setMillisecondsTimer(minutes, seconds);
			ft.addToBackStack(null);
			ft.replace(R.id.container, frg,TAG_FRAGMENT_COUNTDOWN);			
			ft.commit();			
		} else 
	
		if (savedInstanceState == null){
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.add(R.id.container,new TimerButtonsFragment(), TAG_FRAGMENT_TIMERS);
			ft.commit();
		}
		
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		//getFragmentManager().findFragmentByTag(TAG_FRAGMENT_COUNTDOWN);
		TimerButtonsFragment tbf = (TimerButtonsFragment) getFragmentManager().findFragmentByTag(TAG_FRAGMENT_TIMERS);
		if (tbf!= null){
			if (tbf.isVisible()) this.finish();
		}
	}
	
	
}
