package ro.pagepo.countdownpresets;

import ro.pagepo.countdownpresets.fragments.AboutDialogFragment;
import ro.pagepo.countdownpresets.fragments.CountDownJobFragment;
import ro.pagepo.countdownpresets.fragments.TimerButtonsFragment;
import ro.pagepo.countdownpresets.service.CountdownTimerService;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TimersActivity extends Activity {
	
	public static final int STATE_FROM_NOTIFICATION = 1;
	public static final int STATE_EMPTY = 0;
	public static final int STATE_FROM_SERVICE_FINISHED = 2;
	
	public static final String TAG_FRAGMENT_TIMERS ="TAG_FRAGMENT_TIMERS";
	public static final String TAG_FRAGMENT_COUNTDOWN = "TAG_FRAGMENT_COUNTDOWN";
	
	public static final String MINUTES_EXTRA = "MINUTES_EXTRA";
	public static final String SECONDS_EXTRA = "SECONDS_EXTRA";
	
	public static final String KEY_STATE_APP = "KEY_STATE_APP";
	
	Fragment timerButtonsFragment=null;
	Fragment countDownTimerFragment=null;

	private boolean isMyServiceRunning() {
	    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
	    for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
	        if (CountdownTimerService.class.getName().equals(service.service.getClassName())) {
	            return true;
	        }
	    }
	    return false;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("oncreate", "activity created");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timers);
		if (savedInstanceState!=null) restoreFragmentInstances(savedInstanceState);

	
			if ((getIntent().getIntExtra(KEY_STATE_APP, STATE_EMPTY) == STATE_FROM_NOTIFICATION)||(isMyServiceRunning())) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				CountDownJobFragment frg =  (CountDownJobFragment) getFragmentInstanceByTag(TAG_FRAGMENT_COUNTDOWN);
				frg.setMillisecondsTimer(getIntent().getLongExtra(CountDownJobFragment.KEY_MILLISECONDS_TIMER, 1000));
				frg.setMillisecondsLeft(getIntent().getLongExtra(CountDownJobFragment.KEY_MILLISECONDS_LEFT, 999));
				ft.replace(R.id.container, frg,TAG_FRAGMENT_COUNTDOWN);			
				ft.commit();			
			} else 
			
			if (getIntent().getIntExtra(KEY_STATE_APP, STATE_EMPTY) == STATE_FROM_SERVICE_FINISHED) {
				FragmentTransaction ft = getFragmentManager().beginTransaction();
				ft.add(R.id.container,getFragmentInstanceByTag(TAG_FRAGMENT_TIMERS), TAG_FRAGMENT_TIMERS);
				CountDownJobFragment frg =  (CountDownJobFragment) getFragmentInstanceByTag(TAG_FRAGMENT_COUNTDOWN);				
				frg.setMillisecondsTimer(getIntent().getLongExtra(CountDownJobFragment.KEY_MILLISECONDS_TIMER, 1000));
				frg.setMillisecondsLeft(getIntent().getLongExtra(CountDownJobFragment.KEY_MILLISECONDS_LEFT, 1000));
				ft.addToBackStack(null);
				ft.replace(R.id.container, frg,TAG_FRAGMENT_COUNTDOWN);			
				ft.commit();			
			} else 
				if ((savedInstanceState == null)||(getIntent().getIntExtra(KEY_STATE_APP, STATE_EMPTY) == STATE_EMPTY)){
					FragmentTransaction ft = getFragmentManager().beginTransaction();
					ft.add(R.id.container,getFragmentInstanceByTag(TAG_FRAGMENT_TIMERS), TAG_FRAGMENT_TIMERS);
					ft.commit();
				} 
				

		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d("onresume", "activity resumed");
	}
	
	@Override
	public void onBackPressed() {
		if (countDownTimerFragment != null) ((CountDownJobFragment)countDownTimerFragment).onBackPressed(); 
		if (timerButtonsFragment != null) ((TimerButtonsFragment)timerButtonsFragment).onBackPressed(); 
		//super.onBackPressed();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.timers, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_show_about){
			
			DialogFragment d = AboutDialogFragment.newInstance();
			
			d.show(getFragmentManager(), "dialog about");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public Fragment getFragmentInstanceByTag(String fragmentTag) {
		if (TAG_FRAGMENT_TIMERS.equals(fragmentTag)){
			if (timerButtonsFragment == null){
				timerButtonsFragment = new TimerButtonsFragment();
			}
			return timerButtonsFragment;
		} else 
			if (TAG_FRAGMENT_COUNTDOWN.equals(fragmentTag)){
				if (countDownTimerFragment == null){
					countDownTimerFragment = CountDownJobFragment.newInstance(0);					
				}
				return countDownTimerFragment;
			}
		return null;
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//if (countDownTimerFragment!=null) getFragmentManager().putFragment(outState, TAG_FRAGMENT_COUNTDOWN, countDownTimerFragment);
		//if (timerButtonsFragment!=null) getFragmentManager().putFragment(outState, TAG_FRAGMENT_TIMERS, timerButtonsFragment);
	}
	
	protected void restoreFragmentInstances(Bundle bundle){
		//countDownTimerFragment = getFragmentManager().getFragment(bundle, TAG_FRAGMENT_COUNTDOWN);
		//timerButtonsFragment = getFragmentManager().getFragment(bundle, TAG_FRAGMENT_TIMERS);
	}
	
	
}
