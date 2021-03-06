package ro.pagepo.countdownpresets.views;

import ro.pagepo.countdownpresets.R;
import ro.pagepo.countdownpresets.TimerInfo;
import android.content.Context;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridLayout.LayoutParams;

public class TimerButton extends Button{

	TimerInfo timerInfo;
	int column =0;
	int row =0;

	
	public TimerButton(Context context) {
		super(context);
		this.setBackgroundResource(R.drawable.blue_button);
		this.setTextAppearance(context, R.style.but_reallysmalltext);
		prepareLayoutParams();		
	}
	
	public TimerInfo getTimerInfo() {
		return timerInfo;
	}

	public void setTimerInfo(TimerInfo timerInfo) {
		this.timerInfo = timerInfo;
		this.setText(timerInfo.getName());
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
/*
	@Override
	public void setWidth(int pixels) {
		super.setWidth(pixels);
		GridLayout.LayoutParams params= (GridLayout.LayoutParams)this.getLayoutParams();
        params.width = pixels;		
	}
	
	@Override
	public void setHeight(int pixels) {
		super.setHeight(pixels);
		GridLayout.LayoutParams params= (GridLayout.LayoutParams)this.getLayoutParams();
        params.height = pixels;		
	}
	

	//*/
	
	

}
