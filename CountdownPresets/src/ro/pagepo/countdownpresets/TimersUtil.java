package ro.pagepo.countdownpresets;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.util.Base64;

public class TimersUtil {
	private final static int[] minutesDefault = { 1, 2, 3, 4, 5, 6, 7, 8, 9,
			10, 12, 15, 17, 20, 25, 30, 40, 45, 50, 60 };

	public static ArrayList<TimerInfo> getDefaultTimersList() {
		ArrayList<TimerInfo> al = new ArrayList<TimerInfo>();

		for (int i = 0; i < minutesDefault.length; i++) {
			TimerInfo ti = new TimerInfo(minutesDefault[i]);
			al.add(ti);
		}

		return al;
	}

	public static void sortTimersList(ArrayList<TimerInfo> al) {
		Collections.sort(al, new Comparator<TimerInfo>() {

			@Override
			public int compare(TimerInfo lhs, TimerInfo rhs) {
				return lhs.compareTo(rhs);
			}
		});
	}

	/** Read the object from Base64 string. */
	@SuppressWarnings("unchecked")
	public static ArrayList<TimerInfo> timersListFromString(String s) {

		try {
			byte[] data = Base64.decode(s, Base64.DEFAULT);
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();

			return (ArrayList<TimerInfo>) o;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return new ArrayList<TimerInfo>();

	}

	/** Write the object to a Base64 string. */
	public static String timersListToString(ArrayList<TimerInfo> o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(o);
			oos.close();
			return new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}
