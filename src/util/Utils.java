package util;

public class Utils {
	public static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1',
			(byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) 'a', (byte) 'b',
			(byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f' };

	public static String hex(byte[] raw) {
		return hex(raw, 0, raw.length);
	}

	/**
	 * Hex output for bytes.
	 */
	public static String hex(byte[] raw, int start, int end) {
		if (raw == null)
			return null;
		start = Math.max(0, start);
		end = Math.min(raw.length, end);
		assert (end >= start);
		if (end == start)
			return "";

		int len = 2 * (end - start);
		byte[] hex = new byte[len];

		int index = 0;
		for (int i = start; i < end; i++) {
			byte b = raw[i];
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}

		try {
			return new String(hex, "ASCII");
		} catch (Exception e) {
			return null;
		}
	}

	public static String hex(byte b) {
		int v = ((int) b) & 0xFF;
		byte[] bytes = new byte[2];
		bytes[0] = HEX_CHAR_TABLE[v >>> 4];
		bytes[1] = HEX_CHAR_TABLE[v & 0xF];
		try {
			return new String(bytes, "ASCII");
		} catch (Exception e) {
			return null;
		}
	}

	static String[] pads = new String[] { "", " ", "  ", "   ", "    ", "     ", "      " };

	private static String pad(int length) {
		assert (length >= 0);
		if (length < pads.length)
			return pads[length];
		return pad(length / 2) + pad(length - length / 2);
	}

	private static String pad(int length, char c) {
		assert (length >= 0);
		if (length == 0)
			return "";
		else if (length == 1)
			return "" + c;
		else {
			String s = pad(length / 2, c);
			if (length % 2 == 0) {
				return s + s;
			} else {
				return s + s + c;
			}
		}
	}

	public static String padCenter(int length, String x) {
		if (x == null)
			return pad(length, "null");
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		int leftPad = padSize / 2;
		int rightPad = padSize - leftPad;
		return pad(leftPad) + x + pad(rightPad);
	}

	public static String pad(int length, String x) {
		if (x == null)
			return pad(length, "null");
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return pad(padSize) + x;
	}

	public static String pad(String x, int length) {
		if (x == null)
			return pad("null", length);
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return x + pad(padSize);
	}

	public static String pad(String x, int length, char filler) {
		if (x == null)
			return pad("null", length, filler);
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return x + pad(padSize, filler);
	}

	public static String pad(int length, String x, char filler) {
		if (x == null)
			return pad(length, "null", filler);
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return pad(padSize, filler) + x;
	}

	static String[] padZeroes = new String[] { "", "0", "00", "000", "0000", "00000", "000000" };

	/**
	 * String of zeroes of the given length.
	 */
	private static String padZero(int length) {
		if (length < padZeroes.length)
			return padZeroes[length];
		return padZero(length / 2) + padZero(length - length / 2);
	}

	public static String padZero(int length, String x) {
		if (x == null)
			return padZero(length);
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return padZero(padSize) + x;
	}

	public static String padZero(String x, int length) {
		if (x == null)
			return padZero(length);
		int padSize = length - x.length();
		if (padSize < 0)
			return x;
		return x + padZero(padSize);
	}

}
