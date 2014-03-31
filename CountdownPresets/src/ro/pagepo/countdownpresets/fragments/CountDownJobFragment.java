package ro.pagepo.countdownpresets.fragments;

import ro.pagepo.countdownpresets.R;
import ro.pagepo.countdownpresets.TimersActivity;
import ro.pagepo.countdownpresets.service.CountdownTimerService;
import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class CountDownJobFragment extends Fragment {

	long milliseconds = 0;

	public static final String KEY_SECONDS_TOTAL = "KEY_SECONDS_TOTAL";
	public static final String MAIN_BROADCAST_ACTION = "ro.pagepo.countdownpresets.CountdownActivity.receiver.action";
	private boolean shouldStart = false;

	BroadcastReceiver br = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			displayTimeLeft(intent.getIntExtra(TimersActivity.MINUTES_EXTRA,
					0)
					* 60
					* 1000
					+ intent.getIntExtra(TimersActivity.SECONDS_EXTRA, 0)
					* 1000);
		}
	};
	
	public static CountDownJobFragment newInstance(long milliseconds, boolean shouldStart){
		CountDownJobFragment cdtf = new CountDownJobFragment();
		cdtf.milliseconds = milliseconds;
		cdtf.shouldStart = shouldStart;
		return cdtf;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_countdown, container,
				false);

		Log.d("xxx", "on create view countdown fragment");
		Button butCancel = (Button) view.findViewById(R.id.butCancelTimer);
		butCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopTimer();
				Ringtone r = CountDownJobFragment.this.r;
				if (r != null) r.stop();
				//getActivity().finish();
				stopFlashing();
				FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
				ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
				ft.replace(R.id.container,new TimerButtonsFragment(), TimersActivity.TAG_FRAGMENT_TIMERS);
				ft.commit();				
			}
		});

		Button butStop = (Button) view.findViewById(R.id.butStopTimer);
		butStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Button thisBut = (Button) v;
				if (thisBut
						.getText()
						.toString()
						.equals(getActivity().getResources().getString(
								R.string.but_restart_timer))) {
					// is restart

					startTimer();
				} else {
					// is stop
					Ringtone r = CountDownJobFragment.this.r;
					if (r != null)
						r.stop();
					stopFlashing();
				}
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
		
		if (shouldStart){
			shouldStart = false;
			startTimer();
		}
		
		boolean finished = getActivity().getIntent().getBooleanExtra(
				CountdownTimerService.EXTRA_IS_FINISHED, false);
		getActivity().getIntent().putExtra(
				CountdownTimerService.EXTRA_IS_FINISHED, false);

		if (finished) {
			Log.d("xxx","finish on resume "+SystemClock.elapsedRealtime());
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
			getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
			
			displayTimeLeft(0);
			Button butStop = (Button) getView().findViewById(R.id.butStopTimer);
			butStop.setText(getActivity().getResources().getString(
					R.string.but_stop_timer));
			Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			r = RingtoneManager.getRingtone(this.getActivity(), uri);
			r.play();
			startFlashBackground();
			displayTimeLeft(0);

		} else
			displayTimeLeft(milliseconds);

		getActivity().registerReceiver(br,
				new IntentFilter(MAIN_BROADCAST_ACTION));
	}

	public void startTimer() {
		
		Button butStop = (Button) getView().findViewById(R.id.butStopTimer);
		butStop.setText(getActivity().getResources().getString(	R.string.but_restart_timer));
		displayTimeLeft(milliseconds);
		//*/
		startTimerService();
	}

	public void startTimerService() {
		Log.d("xxx", "set start serv");
		Intent sintent = new Intent(this.getActivity(),
				CountdownTimerService.class);
		getActivity().stopService(sintent);
		sintent.putExtra(KEY_SECONDS_TOTAL, (int) (this.milliseconds / 1000));
		ComponentName srv = getActivity().startService(sintent);
		Log.d("xxx", "service is " + srv);
	}

	public void stopTimer() {
		Intent sintent = new Intent(this.getActivity(),
				CountdownTimerService.class);
		getActivity().stopService(sintent);
	}

	public void setMillisecondsTimer(int minutes, int seconds) {
		this.milliseconds = minutes * 60 * 1000 + seconds * 1000;
		displayTimeLeft(this.milliseconds);
	}

	private void displayTimeLeft(long millis) {
		if (getView() == null) return;
		TextView txtTimeLeft = (TextView) getView().findViewById(R.id.txtTimeLeft);
		if (txtTimeLeft == null)	return;
		txtTimeLeft.setText(formatTime(millis));
	}

	private String formatTime(long millis) {
		int minutes = (int) ((millis / 1000) / 60);
		int seconds = (int) (millis / 1000 - minutes * 60);
		return String.format("%02d", minutes)+ " : " + String.format("%02d", seconds);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(br);
	}

	ObjectAnimator colorFade; // animates background
	Drawable defaultBackground;

	private void startFlashBackground() {
		View lay = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		defaultBackground = lay.getBackground();


		colorFade = ObjectAnimator
				.ofObject(lay, "backgroundColor", new ArgbEvaluator(),
						Color.argb(255, 255, 255, 255), 0xff000000);
		colorFade.setDuration(200);
		colorFade.setRepeatCount(ObjectAnimator.INFINITE);
		colorFade.setRepeatMode(ObjectAnimator.REVERSE);
		colorFade.start();

	}

	@SuppressWarnings("deprecation")
	private void stopFlashing() {
		if (colorFade == null) return;
		colorFade.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				if (defaultBackground != null) {
					View lay = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
					//lay.setBackgroundDrawable(defaultBackground);
					PaintDrawable  xxx = (PaintDrawable) defaultBackground;
									
					ObjectAnimator colorFade = ObjectAnimator
							.ofObject(lay, "backgroundColor", new ArgbEvaluator(),
									xxx.getPaint().getColor()	, 0xff000000);
					colorFade.setDuration(10);
					colorFade.setRepeatCount(ObjectAnimator.INFINITE);
					colorFade.setRepeatMode(ObjectAnimator.REVERSE);
					colorFade.start();
				}  else {
					View lay = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
					lay.setBackgroundDrawable(new PaintDrawable(Color.rgb(255, 255, 255)));

				}
			Log.d("xxx","default background is "+defaultBackground);
			}
			
			
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		if (colorFade != null)
			colorFade.end();
		View lay = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		lay.setBackgroundDrawable(new PaintDrawable(Color.rgb(255, 255, 255)));

	}
	


}
