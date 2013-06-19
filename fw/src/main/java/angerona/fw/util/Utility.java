package angerona.fw.util;

public class Utility {
	/**
	 * Checks if the both object o1 and o2 are equal.
	 * @param o1	The first object
	 * @param o2	The second object
	 * @return		true if o1 equals o2, false otherwise.
	 */
	public static boolean equals(Object o1, Object o2) {
		if(o1 == null)	{
			return o2 == null;
		}
		return o1.equals(o2);
	}
}
