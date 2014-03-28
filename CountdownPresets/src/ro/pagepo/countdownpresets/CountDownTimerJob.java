package ro.pagepo.countdownpresets;

import android.os.CountDownTimer;

public class CountDownTimerJob extends CountDownTimer {
	
	CountdownJobCallback callback;
	
	public CountDownTimerJob(long millisInFuture, long countDownInterval,CountdownJobCallback callback) {
		this(millisInFuture, countDownInterval);
		this.callback = callback;
	}

	

	public CountDownTimerJob(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
		
	}
	
	

	public void setCallback(CountdownJobCallback callback) {
		this.callback = callback;
	}



	@Override
	public void onTick(long millisUntilFinished) {
		callback.onTimerThick(millisUntilFinished);
	}

	@Override
	public void onFinish() {
		callback.onTimerFinish();
	}

}
