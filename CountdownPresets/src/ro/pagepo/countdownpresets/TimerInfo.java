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

	public TimerInfo(String name, int minutes) {
		this.minutes = minutes;
		this.name =name;
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

	public int compareTo(TimerInfo ti) {
		if (this.minutes > ti.minutes)
			return 1;
		else if (this.minutes < ti.minutes)
			return -1;
		else
			return 0;
	}

}
