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

}
