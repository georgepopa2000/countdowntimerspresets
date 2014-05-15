package ro.pagepo.countdownpresets.fragments;

import ro.pagepo.countdownpresets.R;
import ro.pagepo.countdownpresets.TimerInfo;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

public class TimerPresetDialog extends DialogFragment {
	
	private TimerButtonsFragmentCallback callback;

	public static TimerPresetDialog newInstance(TimerInfo ti, TimerButtonsFragmentCallback callback){
		TimerPresetDialog tpd = new TimerPresetDialog();
		Bundle bdl = new Bundle();
		bdl.putSerializable("timerinfo", ti);
		tpd.setArguments(bdl);
		tpd.setCallback(callback);
		
		
		return tpd;
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.timer_preset_dialog_fragment, container, false);
		Bundle bdl = getArguments();
		final TimerInfo ti = (TimerInfo) bdl.getSerializable("timerinfo");
		final EditText txtTitle = (EditText) view.findViewById(R.id.txtTimerPresetDialogTitle);
		
		final NumberPicker numberMinutes = (NumberPicker) view.findViewById(R.id.numberMinutes);
		final NumberPicker numberSeconds = (NumberPicker) view.findViewById(R.id.numberSeconds);
		numberSeconds.setMaxValue(59);
		numberSeconds.setMinValue(0);
		numberMinutes.setMinValue(0);
		numberMinutes.setMaxValue(10000);
		Button butOk = (Button) view.findViewById(R.id.butTimerPresetAdd);

		if (ti !=null){		
			txtTitle.setText(ti.getName());
			butOk.setText(getActivity().getResources().getString(R.string.but_timer_preset_edit));			
			numberMinutes.setValue(ti.getMinutes());
			numberSeconds.setValue(ti.getSeconds());
			this.getDialog().setTitle("Edit preset");
		} else {
			this.getDialog().setTitle("Add  preset");
			numberMinutes.setValue(5);
		}
		
		butOk.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String name =txtTitle.getText().toString().equalsIgnoreCase("")?null:txtTitle.getText().toString();
							
				TimerPresetDialog.this.callback.returnedFromDialogTimerPreset(ti, new TimerInfo(name, numberMinutes.getValue(),numberSeconds.getValue()));
				TimerPresetDialog.this.dismiss();
			}
			
		});
		
		
		
		return view;
	}



	public void setCallback(TimerButtonsFragmentCallback callback) {
		this.callback = callback;
	}
	
	
}
