package ro.pagepo.countdownpresets.fragments;

import java.util.ArrayList;
import java.util.Iterator;

import ro.pagepo.countdownpresets.R;
import ro.pagepo.countdownpresets.TimerInfo;
import ro.pagepo.countdownpresets.TimersActivity;
import ro.pagepo.countdownpresets.TimersUtil;
import ro.pagepo.countdownpresets.views.TimerButton;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridLayout;

public class TimerButtonsFragment extends Fragment implements
		TimerButtonsFragmentCallback {
	private static final String PREF_KEY_TIMERS_LIST = "PREF_KEY_TIMERS_LIST";
	public static final String SHARED_PREF_TAG = "timers presets";
	private static final int MINCELLWIDTH = 80;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// return super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater
				.inflate(R.layout.fragment_timers, container, false);

		Log.d("xxl", "on create view fragment");
		final GridLayout gl = (GridLayout) view
				.findViewById(R.id.gridButtonsContainer);

		gl.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						populateWithTimerButtons();
						gl.getViewTreeObserver().removeGlobalOnLayoutListener(
								this);
					}
				});

		Button butAdd = (Button) view.findViewById(R.id.butTimerPresetAdd);
		butAdd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				TimerPresetDialog tpd = TimerPresetDialog.newInstance(null,
						TimerButtonsFragment.this);
				tpd.show(getFragmentManager(), "Add  timer");
			}
		});

		return view;
	}

	private ArrayList<TimerInfo> getTimersList() {
		SharedPreferences prefs = this.getActivity().getSharedPreferences(
				SHARED_PREF_TAG, Context.MODE_PRIVATE);
		String listString = prefs.getString(PREF_KEY_TIMERS_LIST, null);
		if (listString == null) {
			return TimersUtil.getDefaultTimersList();
		}
		return TimersUtil.timersListFromString(listString);
	}

	private void populateWithTimerButtons() {



		ArrayList<TimerInfo> listButtonsInfo = getTimersList();

		GridLayout gl = (GridLayout) getView().findViewById(
				R.id.gridButtonsContainer);
		ArrayList<TimerButton> butList = getButtonTimersList();
		gl.removeAllViews();
		Display display = getActivity().getWindowManager().getDefaultDisplay();
	    DisplayMetrics outMetrics = new DisplayMetrics ();
	    display.getMetrics(outMetrics);

	    DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();

        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;		
		int numColumns = (int) Math.floor(dpWidth / MINCELLWIDTH);
		Log.d("xxxl", "columns: "+numColumns);
		int columnWidth = (int)dpWidth / numColumns;
		gl.setColumnCount(numColumns);

		Iterator<TimerInfo> it = listButtonsInfo.iterator();

		columnWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, columnWidth, getResources().getDisplayMetrics());
		int column = 0;
		int row = 0;
		while (it.hasNext()) {
			if (column == numColumns) {
				column = 0;
				row++;
			}

			TimerInfo ti = it.next();

			int index = butList.indexOf(ti);

			TimerButton but;
			if (index != -1) {
				but = butList.get(index);
			} else {
				but = new TimerButton(getActivity());
				but.setTimerInfo(ti);
				but.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TimerButton tb = (TimerButton) v;

						FragmentTransaction ft = TimerButtonsFragment.this
								.getFragmentManager().beginTransaction();
						TimersActivity ta = (TimersActivity) getActivity();
						CountDownJobFragment frg = (CountDownJobFragment) ta.getFragmentInstanceByTag(TimersActivity.TAG_FRAGMENT_COUNTDOWN);
						frg.setMillisecondsTimer(tb.getTimerInfo().getMilliseconds());
						frg.setMillisecondsLeft(tb.getTimerInfo().getMilliseconds());
						ft.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
						ft.replace(R.id.container, frg,TimersActivity.TAG_FRAGMENT_COUNTDOWN);

						ft.commit();
					}
				});
				registerForContextMenu(but);
			}

			//Log.d("xxxl", "column width px: "+columnWidth);
			//int columnWidtht = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, columnWidth, getResources().getDisplayMetrics());
			//Log.d("xxxl", "column width: "+columnWidth);
			but.setRow(row);
			but.setColumn(column);
			but.setWidth(columnWidth);
			but.setHeight(columnWidth);

			gl.addView(but);
			column++;

		}

	}

	private View lastViewWithContextMenu; // stupid stupid stupid

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		Log.d("xxx", "on context menu");
		if (v instanceof TimerButton) {
			getActivity().getMenuInflater().inflate(
					R.menu.context_menu_presets, menu);
			this.lastViewWithContextMenu = v;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final TimerButton tb = (TimerButton) lastViewWithContextMenu;
		switch (item.getItemId()) {
		case R.id.menu_edit_option:
			TimerPresetDialog tpd = TimerPresetDialog.newInstance(
					tb.getTimerInfo(), this);
			tpd.show(getFragmentManager(), "Edit timer");
			return false;

		case R.id.menu_delete_option:
			DialogInterface.OnClickListener dialoglistener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (DialogInterface.BUTTON_POSITIVE == which) {
						TimerInfo ti = tb.getTimerInfo();
						ArrayList<TimerInfo> al = getCurrentTimerInfoListFromButtonList(getButtonTimersList());
						int index = al.indexOf(ti);
						Log.d("xxx", "index is" + index);
						al.remove(ti);
						saveTimersList(al);
						populateWithTimerButtons();
					}
					dialog.dismiss();
				}
			};
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setPositiveButton("Yes", dialoglistener)
					.setNegativeButton("No", dialoglistener)
					.setMessage("Are you sure you want to delete preset?")
					.create().show();

			return false;
		}
		return true;

	}

	@Override
	public void returnedFromDialogTimerPreset(TimerInfo oldTi, TimerInfo newTi) {
		if (oldTi == null) {
			ArrayList<TimerInfo> al = getCurrentTimerInfoListFromButtonList(getButtonTimersList());
			al.add(newTi);
			TimersUtil.sortTimersList(al);
			saveTimersList(al);
		} else {
			ArrayList<TimerInfo> al = getCurrentTimerInfoListFromButtonList(getButtonTimersList());
			int index = al.indexOf(oldTi);
			Log.d("xxx", "index is" + index);
			oldTi.setMilliseconds(newTi.getMilliseconds());
			oldTi.setName(newTi.getName());

			TimersUtil.sortTimersList(al);
			saveTimersList(al);
		}

		populateWithTimerButtons();
	}

	private void saveTimersList(ArrayList<TimerInfo> al) {
		SharedPreferences prefs = this.getActivity().getSharedPreferences(
				SHARED_PREF_TAG, Context.MODE_PRIVATE);
		Editor ed = prefs.edit();
		ed.putString(PREF_KEY_TIMERS_LIST, TimersUtil.timersListToString(al));
		ed.commit();
	}

	private ArrayList<TimerButton> getButtonTimersList() {
		GridLayout gl = (GridLayout) getView().findViewById(
				R.id.gridButtonsContainer);
		ArrayList<TimerButton> butList = new ArrayList<TimerButton>();
		for (int i = 0; i < gl.getChildCount(); i++) {
			View v = gl.getChildAt(i);
			if (v instanceof TimerButton)
				butList.add((TimerButton) v);
		}
		return butList;
	}

	private ArrayList<TimerInfo> getCurrentTimerInfoListFromButtonList(
			ArrayList<TimerButton> butList) {
		ArrayList<TimerInfo> al = new ArrayList<TimerInfo>();
		Iterator<TimerButton> it = butList.iterator();
		while (it.hasNext()) {
			al.add(it.next().getTimerInfo());
		}
		return al;
	}
	
	public void onBackPressed(){
		if (this.isVisible()) getActivity().finish();
	}

}
