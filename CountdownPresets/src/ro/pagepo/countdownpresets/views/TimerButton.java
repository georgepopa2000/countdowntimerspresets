package ro.pagepo.countdownpresets.views;

import ro.pagepo.countdownpresets.CountdownActivity;
import ro.pagepo.countdownpresets.TimerInfo;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;

public class TimerButton extends Button{

	TimerInfo timerInfo;
	int column =0;
	int row =0;

	
	public TimerButton(Context context) {
		super(context);
		prepareLayoutParams();		
	}
	
	public TimerInfo getTimerInfo() {
		return timerInfo;
	}

	public void setTimerInfo(TimerInfo timerInfo) {
		this.timerInfo = timerInfo;
		this.setText(timerInfo.getName());
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				TimerButton.this.getContext();
				Intent intent = new Intent(TimerButton.this.getContext(), CountdownActivity.class);
				intent.putExtra(CountdownActivity.SECONDS_EXTRA, 0);
				intent.putExtra(CountdownActivity.MINUTES_EXTRA, TimerButton.this.timerInfo.getMinutes());
				TimerButton.this.getContext().startActivity(intent);
			}
		});
	}
	
	
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
		GridLayout.LayoutParams params= (GridLayout.LayoutParams)this.getLayoutParams();
        params.columnSpec = GridLayout.spec(this.column);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
		GridLayout.LayoutParams params= (GridLayout.LayoutParams)this.getLayoutParams();
        params.rowSpec = GridLayout.spec(this.row);
	}

	private void prepareLayoutParams(){
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.height = LayoutParams.WRAP_CONTENT;
        params.width = LayoutParams.WRAP_CONTENT;
        params.rightMargin = 0;
        params.topMargin = 0;

        params.setGravity(Gravity.CENTER_HORIZONTAL);

		this.setLayoutParams(params);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof TimerInfo) {
		TimerInfo ti = (TimerInfo) o;
		this.timerInfo.equals(ti);
		}
		
		
		return super.equals(o);
	}
	

	
	
	

}
