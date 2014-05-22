package net.gdformula.nbt;

/**
 * 
 * @author Robert Lodico (PandaCoder)
 *
 */
public final class NBTUtils {
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static long hashString(String str) {
		long hash = 0L;
		
		for (int i = 0, j = str.length() - 1; i < str.length(); i++, j--)
			hash += str.charAt(i) * Math.pow(31, j);
		
		return hash;
	}
}
