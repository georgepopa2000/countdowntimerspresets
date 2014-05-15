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
	
	long milliseconds;
	
	public TimerInfo(String name,long milliseconds){
		this.name = name;
		this.milliseconds = milliseconds;
	}
	
	
	public TimerInfo(String name, int minutes, int seconds) {
		this.name = name;
		this.milliseconds = minutes*60*1000+seconds*1000;
	}

	public TimerInfo(String name, int minutes) {
		this(name, minutes, 0);
	}

	public TimerInfo(int minutes) {
		this(null,minutes);
	}

	public String getName() {
		if (name == null)
			return getMinutes() + " min";
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMinutes() {
		return (int)(milliseconds/60/1000);
	}

	public int getSeconds() {
		return (int)(milliseconds - getMinutes()*60*1000)/1000;
	}

	public int compareTo(TimerInfo ti) {
		if (this.milliseconds > ti.milliseconds)
			return 1;
		else if (this.milliseconds < ti.milliseconds)
			return -1;
		else
			return 0;
	}


	public long getMilliseconds() {
		return milliseconds;
	}


	public void setMilliseconds(long milliseconds) {
		this.milliseconds = milliseconds;
	}

}
