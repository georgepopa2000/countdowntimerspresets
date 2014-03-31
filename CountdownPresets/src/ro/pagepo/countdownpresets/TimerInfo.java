package ro.pagepo.countdownpresets;

import java.io.Serializable;

public class TimerInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3908882955626915726L;
	/**
	 * timer name, may be null defaults to minutes
	 */
	String name;
	/**
	 * number of minutes
	 */
	int minutes;
	/**
	 * number of seconds
	 */
	int seconds;
	
	
	public TimerInfo(String name, int minutes, int seconds) {
		this.name = name;
		this.minutes = minutes;
		this.seconds = seconds;
	}

	public TimerInfo(String name, int minutes) {
		this(name, minutes, 0);
	}

	public TimerInfo(int minutes) {
		this(null,minutes);
	}

	public String getName() {
		if (name == null)
			return minutes + " min";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	
	

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	public int compareTo(TimerInfo ti) {
		if ((this.minutes*60+this.seconds) > (ti.minutes * 60 + ti.seconds))
			return 1;
		else if ((this.minutes*60+this.seconds) < (ti.minutes * 60 + ti.seconds))
			return -1;
		else
			return 0;
	}

}
