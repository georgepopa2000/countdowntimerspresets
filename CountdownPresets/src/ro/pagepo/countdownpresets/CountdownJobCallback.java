package ro.pagepo.countdownpresets;

public interface CountdownJobCallback {
	public void onTimerThick(long millisUntilFinished);
	public void onTimerFinish();
}
