package game.chess;

/**
 * A chess piece.
 * 
 * @author andrewbyde
 */
public class Piece {
	public enum PieceType {
		PAWN('v', '^'), ROOK('H', 'X'), KNIGHT('7', 'L'), BISHOP('*', '+'), QUEEN('Q', 'q'), KING('@', 'O');

		private final char bc;
		private final char wc;

		private PieceType(char bc, char wc) {
			this.bc = bc;
			this.wc = wc;
		}
	}

	public final boolean isWhite;
	public final PieceType type;
	Location loc;

	public Piece(boolean isWhite, PieceType type) {
		this.isWhite = isWhite;
		this.type = type;
	}

	/**
	 * An exact copy of this piece.
	 */
	public Piece clone() {
		Piece p = new Piece(isWhite, type);
		p.loc = this.loc.clone();
		return p;
	}

	/**
	 * Return a single char representing the piece. Lower case for white, upper
	 * case for black. Used for debugging etc.
	 * 
	 * @return  a single char representing the piece. Lower case for white, upper
	 * case for black. Used for debugging etc.
	 */
	public char c() {
		if (isWhite)
			return type.wc;
		else 
			return type.bc;
	}

	public String toString() {
		return (isWhite ? "white " : "black ") + type;
	}
}
