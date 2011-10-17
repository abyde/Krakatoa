package game.chess;

import game.chess.Piece.PieceType;

/**
 * A board layout in chess. It's abstract because there is no implementation of
 * "isLegalMove" ... yet!
 * 
 * @author andrewbyde
 */
public abstract class Board {

	/**
	 * which piece is in each board location? Indices are from the top left not
	 * bottom right. See 'locationToIndex' and vice-versa.
	 */
	private Piece[][] pieces = new Piece[8][8];

	/**
	 * Newly laid out board at the start of the game.
	 */
	public Board() {
		pieces[0] = new Piece[] { new Piece(false, PieceType.ROOK),
				new Piece(false, PieceType.KNIGHT),
				new Piece(false, PieceType.BISHOP),
				new Piece(false, PieceType.QUEEN),
				new Piece(false, PieceType.KING),
				new Piece(false, PieceType.BISHOP),
				new Piece(false, PieceType.KNIGHT),
				new Piece(false, PieceType.ROOK) };
		pieces[1] = new Piece[8];
		pieces[6] = new Piece[8];
		pieces[7] = new Piece[8];
		for (int i = 0; i < 8; i++) {
			pieces[1][i] = new Piece(false, PieceType.PAWN);
			pieces[6][i] = new Piece(true, PieceType.PAWN);
			pieces[7][i] = new Piece(true, pieces[0][i].type);
		}
	}

	/**
	 * @return the piece at a particular location
	 * @throws IllegalArgumentException
	 *             if the coordinates are not on the board.
	 */
	public Piece pieceAt(Location loc) {
		return pieces[loc.row][loc.col];
	}

	/**
	 * Set the piece at a given location.
	 * 
	 * @return the piece that was already there.
	 */
	private Piece setPieceAt(Location loc, Piece p) {
		if (loc == null)
			throw new IllegalArgumentException("Null location");
		Piece pOld = pieces[loc.row][loc.col];
		pieces[loc.row][loc.col] = p;
		return pOld;
	}


	/**
	 * For a piece that can move diagonally or horizontally, is there a piece of
	 * either colour at an intermediate location? Excludes the ends. Should not
	 * be called for Knight moves or it will throw an exception.
	 * 
	 * @throws IllegalArgumentException
	 *             if the coordinates are not on the board.
	 */
	public boolean isBlocked(Location loc1, Location loc2) {
		int r1 = loc1.row;
		int c1 = loc1.col;
		int r2 = loc2.row;
		int c2 = loc2.col;

		int dx = r2 - r1;
		int dy = c2 - c1;

		// trivial move?
		if ((dx == 0) && (dy == 0))
			return false;

		int mdx = Math.abs(dx);
		int mdy = Math.abs(dy);

		// cute test:
		if ((mdx * mdy != 0) && (mdx != mdy)) {
			// illegal move
			throw new IllegalArgumentException("Cannot move any piece from "
					+ r1 + ", " + c1 + " to " + r2 + ", " + c2
					+ " -- not horizontal or diagnoal");
		}

		// convert dx, dy to +-1
		dx = (dx == 0) ? 0 : dx / mdx;
		dy = (dy == 0) ? 0 : dy / mdy;
		int numSteps = Math.max(mdx, mdy);

		for (int i = 1; i < numSteps - 1; i++) {
			int x = r1 + i * dx;
			int y = c1 + i * dy;
			if (pieces[x][y] != null) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Report whether it is legal to move the given piece to the location 'loc'.
	 */
	public abstract boolean isLegalMove(Piece p, Location loc);
	
	/**
	 * Move the given piece to the new location. Does NO checking of validity
	 * returns the removed piece if this move results in a displacement of an
	 * existing piece.
	 */
	Piece movePiece(Location loc1, Location loc2) {
		Piece p = pieceAt(loc1);
		if (p == null)
			return null;

		// get piece first so exception is thrown before adjusting
		// location of p.
		Piece removed = setPieceAt(loc2, p);
		setPieceAt(loc1, null);
		
		return removed;
	}

	/**
	 * Draw the board. Use 3x3 characters for squares. Used for debugging etc.
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String[] l = new String[] { "   ", "###" };
		char[] c = new char[] { ' ', '#' };

		for (int row = 0; row < 8; row++) {
			// one builder per line within a row.
			StringBuilder sb1 = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			sb1.append("  |");
			sb2.append(Location.charOfRow(row)).append(" |");

			for (int col = 0; col < 8; col++) {
				boolean isWhite = ((row + col) % 2 == 0);
				Piece p = pieces[row][col];
				int index = isWhite ? 0 : 1;

				char centre = (p == null) ? c[index] : p.c();

				sb1.append(l[index]);
				sb2.append(c[index]).append(centre).append(c[index]);
			}
			sb1.append("\n");
			sb2.append("\n");
			String s = sb1.toString();
			sb.append(s).append(sb2.toString()).append(s);
		}
		sb.append("--+------------------------\n");
		sb.append("    A  B  C  D  E  F  G  H ");

		return sb.toString();
	}

}
