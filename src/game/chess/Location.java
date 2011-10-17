package game.chess;

/**
 * A location on the chess board
 * 
 * @author andrewbyde
 */
public class Location {
	int row;
	int col;

	/**
	 * Constructor. Throws an error if it's off the board.
	 */
	public Location(int row, int col) {
		if ((row < 0) || (row >= 8) || (col < 0) || (col >= 8))
			throw new IllegalArgumentException("Location " + row + ", " + col
					+ " is not on the board.");
		this.row = row;
		this.col = col;
	}

	public Location clone() {
		return new Location(row, col);
	}

	/**
	 * Convert to standard chess notation to a location. Standard notation is a
	 * letter in 'a'-'h' followed by a number '1'-'8'.  Col then row.
	 */
	public static Location parse(String std) {
		if (std == null)
			return null;

		if ((std.length() < 2) || (std.length() > 2))
			throw new IllegalArgumentException(
					"Standard notation has 2 chars, a letter and a number, cannot parse '"
							+ std + "'");
		char r = std.charAt(1);
		char c = std.charAt(0);

		return new Location(rowOfChar(r), colOfChar(c));
	}

	public String std() {
		return "" + charOfCol(row) + charOfRow(col);
	}

	public static int rowOfChar(char r) {
		if ((r < '1') || (r > '8'))
			throw new IllegalArgumentException("Illegal row char '" + r + "'");
		return '8' - r;
	}

	public static int colOfChar(char c) {
		c = Character.toLowerCase(c);
		if ((c < 'a') || (c > 'h'))
			throw new IllegalArgumentException("Illegal col char '" + c + "'");
		return c - 'a';
	}

	public static char charOfRow(int r) {
		if ((r < 0) || (r >= 8))
			throw new IllegalArgumentException("Illegal row index '" + r + "'");
		return (char) ('8' - r);
	}

	public static char charOfCol(int c) {
		c = Character.toLowerCase(c);
		if ((c < 0) || (c >= 8))
			throw new IllegalArgumentException("Illegal col index '" + c + "'");
		return (char) ('a' + c);
	}
	
	public String toString() { return std(); }
}
